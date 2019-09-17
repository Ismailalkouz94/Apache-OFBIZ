package org.apache.ofbiz.humanres.holding;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import static org.apache.ofbiz.base.util.UtilValidate.module;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import org.apache.ofbiz.humanres.PayrollServices;
import org.apache.ofbiz.humanres.moving.MovingEmployee;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ofbiz.service.ServiceUtil;
import java.io.PrintWriter;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.humanres.PersonUtilServices;
import org.apache.ofbiz.humanres.employee.EmployeePosition;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rbab3a
 */
public class HoldingEmployee {

    public final static String module = HoldingEmployee.class.getName();
    public static final String securityProperties = "security.properties";
    public static final String resourceError = "HumanResErrorUiLabels";
    private static final String keyValue = UtilProperties.getPropertyValue(securityProperties, "login.secret_key_string");
  public static final String resource = "HumanResUiLabels";
    public static Map<String, Object> updateEmplStatusToHold(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");

        java.sql.Date sqlTransDate = null;
        boolean beganTransaction = false;
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyId", partyId);
        String transAction = "";
        String emplStatus = "";
        try {
            beganTransaction = TransactionUtil.begin();

            List<GenericValue> list = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (int i = 0; i < list.size(); i++) {
                emplStatus = (String) list.get(i).get("EmplStatus");
                if (emplStatus.equals("HIRED")) {
                    GenericValue gvValue = (GenericValue) list.get(i).clone();
                    gvValue.set("EmplStatus", "HOLD");
                    gvValue.store();
                    System.out.println("Done Updated");
                    transAction = "holded";
                } else if (emplStatus.equals("HOLD")) {
                    GenericValue gvValue = (GenericValue) list.get(i).clone();
                    gvValue.set("EmplStatus", "HIRED");
                    gvValue.store();
                    System.out.println("Done Updated");
                    transAction = "unholded";
                } else {
                    try {

                        beganTransaction = TransactionUtil.begin();
                        // only rollback the transaction if we started one...
                        TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : error "));
                        Msg = "";
                        return ServiceUtil.returnError(Msg);
                    } catch (GenericEntityException e) {
                        Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                        Msg = "";
                        return ServiceUtil.returnError(Msg);
                    }
                }
            }

        } catch (GenericEntityException ex) {
            System.out.println(ex.getMessage());
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                Msg = ex.getMessage();
                return ServiceUtil.returnError(Msg);
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                Msg = e.getMessage();
                return ServiceUtil.returnError(Msg);
            }

        } finally {
            // only commit the transaction if we started one... this will throw an exception if it fails
            try {
                TransactionUtil.commit(beganTransaction);

            } catch (GenericEntityException e) {
                Msg = e.getMessage();
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                return ServiceUtil.returnError(Msg);
            }
        }
        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
        Msg = "Employee has been " + transAction + " successfully";
        return ServiceUtil.returnSuccess(Msg);
    }

    public static Map<String, Object> deletePositionGradation_Holding(DispatchContext ctx, Map<String, Object> context) throws ParseException, IOException {
        LocalDispatcher dispatcher = ctx.getDispatcher();

        Locale locale = (Locale) context.get("locale");

        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        String strTransDate = (String) context.get("transDate");
        //        for EMP_SALAY_GRADE Table 
        String rowId = (String) context.get("rowSeq");

        System.out.println("rowId from delete holding " + rowId);
        Map<String, Object> criteria = new HashMap<String, Object>();

        java.sql.Date sqlTransDate = PayrollServices.convertFromStringToDate(strTransDate);
        String transType = PayrollServices.getLastEmployeeTransType_EmplSalaryGrade(delegator, partyId, sqlTransDate);

        boolean beganTransaction = false;
        if (transType.equals("HOLD")) {
            String maxSeq = PayrollServices.getMaxSequence_PositionGradation(delegator, partyId, transType);
            if (maxSeq.equals(rowId)) {
                System.out.println("maxSeq from delete holding " + maxSeq);
                criteria.put("emplSalaryGrade", maxSeq);
                boolean isExist = PayrollServices.isCalculatedPositionGradation(delegator, partyId, sqlTransDate);
                try {
                    if (!isExist) {

                        criteria = UtilMisc.toMap("emplSalaryGrade", maxSeq);
                        Map<String, Object> deleteEmplSalaryResult = dispatcher.runSync("deleteEmplSalaryGrade", criteria);
                        if (ServiceUtil.isError(deleteEmplSalaryResult)) {
                            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError,
                                    "BonusError", locale), null, null, deleteEmplSalaryResult);

                        }
//                  
                    } else {
                        try {
                            beganTransaction = TransactionUtil.begin();
                            // only rollback the transaction if we started one...
                            TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning :Can't delete, this record is calculated"));
                            Msg = "";
                            return ServiceUtil.returnError(Msg);
                        } catch (GenericEntityException e) {
                            Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                            Msg = "";
                            return ServiceUtil.returnError(Msg);
                        }
                    }

                } catch (Exception ex) {

                    Logger.getLogger(MovingEmployee.class.getName()).log(Level.SEVERE, null, ex);
                    try {
                        beganTransaction = TransactionUtil.begin();
                        // only rollback the transaction if we started one...
                        TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                        return ServiceUtil.returnError(ex.getMessage());
                    } catch (Exception e) {
                        Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                        Msg = "Warrning : this is not last action ";
                        return ServiceUtil.returnError(e.getMessage());
                    }
                }
            } else {
                try {
                    beganTransaction = TransactionUtil.begin();
                    // only rollback the transaction if we started one...
                    TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : this is not last action "));
                    Msg = "Warrning : this is not last action ";
                    return ServiceUtil.returnError(Msg);
                } catch (GenericEntityException e) {
                    Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                    Msg = "Warrning : this is not last action ";
                    return ServiceUtil.returnError(Msg);
                }
            }
        } else {
            try {
                beganTransaction = TransactionUtil.begin();
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : this is not last action "));
                Msg = "Warrning : please select hold/unhold action ";
                return ServiceUtil.returnError(Msg);
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                Msg = "Warrning : please select hold/unhold action ";
                return ServiceUtil.returnError(Msg);
            }
        }

        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
        String successMsg = UtilProperties.getMessage(resourceError, "deleteSuccess", messageMap, locale);
        return ServiceUtil.returnSuccess(successMsg);
    }

    public static String isUnPaidVacation(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String rowId = request.getParameter("rowId");

        Map<String, Object> creiteria = new HashMap<String, Object>();

        creiteria.put("emplSalaryGrade", rowId);
        String isUnPaid = "false";
        try {
            List<GenericValue> list = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(creiteria), null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    if (row.get("vacationId") != null) {
                        isUnPaid = "true";
                    }
                }
            }
//            jsonData.add(jsonRes);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(isUnPaid);
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String holdEmployee(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, SQLException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String partyId = request.getParameter("partyId");
        String notes = request.getParameter("notes");
        boolean beganTransaction = false;
        Map<String, Object> creiteria = new HashMap<String, Object>();
        EmployeePosition employeePosition = null;
        String strTransDate = request.getParameter("TransDate");
        java.sql.Date sqlEndDate = null;
        String endDate = request.getParameter("endDate");
        String Msg= "";
        if (endDate != null) {
            sqlEndDate = PayrollServices.convertFromStringToDate(endDate);
        }

        java.sql.Date sqlTransDate = PayrollServices.convertFromStringToDate(strTransDate);
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        String userLoginId = userLogin.getString("userLoginId");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
  Map<String, String> messageMap;
        try {
            employeePosition = PersonUtilServices.getEmployeePositionDetails(request, partyId);
            System.out.println("trans Type "+employeePosition.getTransType());
            if(employeePosition.getTransType().equals("HOLD") &&employeePosition.getEndDate() == null){
                messageMap = UtilMisc.toMap("errorMessage", "error");
            String errMsg = UtilProperties.getMessage(resourceError, "closeHold", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
            }
            
            if(employeePosition.getTransType().equals("HOLD") && employeePosition.getEndDate() != null){
                if(sqlTransDate.toLocalDate().isEqual(employeePosition.getEndDate().toLocalDate())||
                      sqlTransDate.toLocalDate().isBefore(employeePosition.getEndDate().toLocalDate())  ){
                     messageMap = UtilMisc.toMap("errorMessage", "error");
            String errMsg = UtilProperties.getMessage(resourceError, "alreadyHold", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
                }
            }
            System.out.println("employeePosition " + employeePosition.getTransDate() + " "
                    + employeePosition.getBasicSalary());
            String emplSalaryGrade = delegator.getNextSeqId("EmplSalaryGrade");
            System.out.println("emplSalaryGrade sequence = "+emplSalaryGrade);
            Map<String, Object> createHoldData = new HashMap<>();
            createHoldData = UtilMisc.toMap(
                    "emplSalaryGrade", emplSalaryGrade,
                    "degreeId", employeePosition.getDegreeId(),
                    "gradeId", employeePosition.getGradeId(),
                    "TransDate", sqlTransDate,
                    "endDate", sqlEndDate,
                    "emplPositionId", employeePosition.getEmplPositionId(),
                    "jobGroupId", employeePosition.getJobGroupId(),
                    "TransType", "HOLD",
                    "partyIdFrom", employeePosition.getPartyIdFrom(),
                    "notes", notes,
                    "vacationId", "",
                    "userLoginId", userLoginId,
                    "partyId", partyId
            );
            Map<String, Object> createHOLDResult = dispatcher.runSync("createEmplSalaryGradeService", createHoldData);
            System.out.println("createHOLDResult " + createHOLDResult);
          
        } catch (Exception ex) {
           try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                Logger.getLogger(HoldingEmployee.class.getName()).log(Level.SEVERE, null, ex);

                Msg = ex.getMessage();
                System.out.println("Msg " + Msg);
                request.setAttribute("_ERROR_MESSAGE_", Msg);
                return "error";
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                System.out.println(">>> " + e.getMessage());
                Msg = e.getMessage();
                request.setAttribute("_ERROR_MESSAGE_", Msg);
                return "error";
            }
        }
      finally {
            // only commit the transaction if we started one... this will throw an exception if it fails

            try {
                TransactionUtil.commit(beganTransaction);

            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                Msg = e.getMessage();
                request.setAttribute("_ERROR_MESSAGE_", Msg);
                return "error";
            }
        }
       messageMap = UtilMisc.toMap("successMessage", "");
        String successMsg = UtilProperties.getMessage(resource, "createVacation", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", successMsg);

        return "success";
    }

}
