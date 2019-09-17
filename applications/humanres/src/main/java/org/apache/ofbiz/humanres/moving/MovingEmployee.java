/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.humanres.moving;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import static org.apache.ofbiz.base.util.UtilValidate.module;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import org.apache.ofbiz.humanres.HumanResEvents;
import org.apache.ofbiz.humanres.PayrollServices;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Rbab3a
 */
public class MovingEmployee {

    public static final String resourceError = "HumanResErrorUiLabels";
    public final static String module = MovingEmployee.class.getName();

    public static String getMaxSequence_Employment(HttpServletRequest request, String partyId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyIdTo", partyId);
        String maxSeq = "";
        String rowValue = "";
        HashMap mapValues= new HashMap();
        
            try {
            List<GenericValue> result = delegator.findList("Employment", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {

                    rowValue = (String) row.get("employmentId");
                    mapValues.put(Integer.valueOf(rowValue),rowValue);
                  
                }
            int maxKey = (int) Collections.max(mapValues.keySet());
                System.out.println("Max value from Key" + mapValues.get(maxKey));
                maxSeq = (String) mapValues.get(maxKey);
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(MovingEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maxSeq;
    }

    public static String getMaxSequence_Employment(Delegator delegator, String partyId, boolean isNull) throws ParseException, IOException {

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyIdTo", partyId);
        String maxSeq = "";
        String rowValue = "";
         HashMap mapValues= new HashMap();
        try {
            List<GenericValue> result = delegator.findList("Employment", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    if (isNull == false) {
                        if (row.get("thruDate") != null) {
                   rowValue = (String) row.get("employmentId");
                    mapValues.put(Integer.valueOf(rowValue),rowValue);

                        }
                    } else if (isNull == true) {
                        if (row.get("thruDate") == null) {
                            maxSeq = (String) row.get("employmentId");
                            return maxSeq;
                        }

                    }
                }
              if (!mapValues.isEmpty()) {
                 int maxKey = (int) Collections.max(mapValues.keySet());
                System.out.println("Max value from Key" + mapValues.get(maxKey));
                maxSeq = (String) mapValues.get(maxKey);
            }

            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(MovingEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maxSeq;
    }

    public static String getCurrnetDepartmentId(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
  
        Map<String, Object> criteria = new HashMap<String, Object>();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String partyId = request.getParameter("partyId");
        String deptId = "";
        try {
            String maxSeq = getMaxSequence_Employment(request, partyId);
            criteria.put("employmentId", maxSeq);
            criteria.put("partyIdTo", partyId);
            List<GenericValue> result = delegator.findList("Employment", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    deptId = (String) row.get("partyIdFrom");
                    break;
                }
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(MovingEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }
        return deptId;
    }

    public static void getEmploymentDropDown(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        PrintWriter out = null;
        Map<String, String> criteria = new HashMap<String, String>();

        criteria.put("roleTypeId", "INTERNAL_ORGANIZATIO");
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> listData = delegator.findList("PartyRoleAndPartyDetail", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : listData) {
                jsonRes = new JSONObject();
                jsonRes.put("partyIdFrom", row.get("partyId"));
                jsonRes.put("groupName", row.get("groupName"));
                jsonData.add(jsonRes);
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(MovingEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String getFromDateEmploymentForEmployee(HttpServletRequest request, String partyId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        Map<String, Object> criteria = new HashMap<String, Object>();
        String maxSeq = getMaxSequence_Employment(request, partyId);
        criteria.put("employmentId", maxSeq);
        criteria.put("partyIdTo", partyId);
        String fromDate = "";

        try {
            List<GenericValue> listData = delegator.findList("Employment", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : listData) {
                if (!listData.isEmpty()) {
                    fromDate = row.get("fromDate").toString();
                    return fromDate;
                }
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(MovingEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fromDate;

    }

    public static String getMovingDetails(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        String partyId = request.getParameter("partyId");
        PrintWriter out = null;
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        java.sql.Date LocalTransDateMax = PayrollServices.getLastActive_TransDate_Employee_SalaryGrade(request, partyId);
                 java.sql.Date lastTransDate = PayrollServices.getMax_TransDate_Employee_SalaryGrade(request, partyId);

        Map<String, Object> EmplSalaryGradeTable = new HashMap<String, Object>();
        EmplSalaryGradeTable.put("partyId", partyId);
        EmplSalaryGradeTable.put("TransDate", LocalTransDateMax);
        JSONObject jsonRes = new JSONObject();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        try {
            //    get emplPostionId field from EmplPositionFulfillment Table
            List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(EmplSalaryGradeTable), null, null, null, true);
            if (PayrollServices.getEmployeeStatus(request, partyId).equals("HIRED")) {
                if (!EmplSalaryGradeList.isEmpty()) {
                    for (GenericValue row : EmplSalaryGradeList) {
                        jsonRes.put("partyId", row.get("partyId"));
                        jsonRes.put("employment", row.get("partyIdFrom"));
                        jsonRes.put("description", getDeprtmentDescription(request, (String) row.get("partyIdFrom")));
                        jsonRes.put("TransDate", row.get("TransDate").toString());
                        jsonRes.put("fullName", HumanResEvents.getPartyName(request, response, partyId));
                        jsonRes.put("positionId", row.get("emplPositionId"));
                        jsonRes.put("jobGroupId", row.get("jobGroupId"));
                        jsonRes.put("movingDate", getFromDateEmploymentForEmployee(request, (String) row.get("partyId")));
                        jsonRes.put("gradeId", row.get("gradeId"));
                        jsonRes.put("degreeId", row.get("degreeId"));
                        jsonRes.put("lastTransDate", lastTransDate.toString());

                    }
                }
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonRes.toString());
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(MovingEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getDeprtmentDescription(HttpServletRequest request, String partyId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        if (partyId == null) {
            return "";
        } else {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("partyId", partyId);
            String groupDescription = "";
            try {
                GenericValue result = delegator.findOne("PartyGroup", criteria, true);
                if (result != null) {
                    System.out.println("result " + result);
                    groupDescription = (String) result.get("groupName");
                    return groupDescription;
                } else {
                    groupDescription = "-";
                }
            } catch (GenericEntityException ex) {
                Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
            }
            return groupDescription;
        }
    }

    public static Map<String, Object> updateThruDateEmployment(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        String maxSeq = getMaxSequence_Employment(delegator, partyId, true);
        String thruDatePreviousEmployment = (String) context.get("thruDateUpdate");
        System.out.println("maxSeqFulfillment  " + maxSeq);
        Map<String, Object> currentPositionIdTable = new HashMap<String, Object>();
        currentPositionIdTable.put("employmentId", maxSeq);
        currentPositionIdTable.put("partyIdTo", partyId);
        boolean beganTransaction = false;
        String strTransDate = (String) context.get("TransDate");
        java.sql.Date sqlTransDate = PayrollServices.convertFromStringToDate(strTransDate);
        boolean isExist = PayrollServices.isCalculatedPositionGradation(delegator, partyId, sqlTransDate);
        String movingDate = (String) context.get("TransDate");
        if (thruDatePreviousEmployment != null) {
            java.sql.Date sqlThruDate = PayrollServices.convertFromStringToDate(thruDatePreviousEmployment);
            java.sql.Date sqlMovingDate = PayrollServices.convertFromStringToDate(movingDate);
            LocalDate endDate = null;

            if (sqlMovingDate.toLocalDate().isEqual(sqlThruDate.toLocalDate())) {
                endDate = sqlMovingDate.toLocalDate();
            } else {
                endDate = sqlMovingDate.toLocalDate().minusDays(1);
            }

            try {
                if (!isExist) {
                    List<GenericValue> list = delegator.findList("Employment", EntityCondition.makeCondition(currentPositionIdTable), null, null, null, true);
                    for (int i = 0; i < list.size(); i++) {
                        GenericValue gvValue = (GenericValue) list.get(i).clone();
                        gvValue.set("thruDate", java.sql.Date.valueOf(endDate));
                        gvValue.store();
                        System.out.println("Done Updated");

                    }
                } else {
                    try {
                        beganTransaction = TransactionUtil.begin();
                        // only rollback the transaction if we started one...
                        TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning :Can't delete, this record is calculated"));
                        Msg = "Warrning :Can't delete, this record is calculated";
                        return ServiceUtil.returnError(Msg);
                    } catch (GenericEntityException e) {
                        Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                        Msg = "Warrning :Can't delete, this record is calculated";
                        return ServiceUtil.returnError(Msg);
                    }
                }

            } catch (GenericEntityException ex) {
                Logger.getLogger(MovingEmployee.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
            Msg = "employee has been moved successfully";
            return ServiceUtil.returnSuccess(Msg);
    }

    public static Map<String, Object> deletePositionGradation_MOVING(DispatchContext ctx, Map<String, Object> context) throws ParseException, IOException {
        LocalDispatcher dispatcher = ctx.getDispatcher();

        Locale locale = (Locale) context.get("locale");

        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        String strTransDate = (String) context.get("transDate");
        //        for EMP_SALAY_GRADE Table 
        String rowId = (String) context.get("rowSeq");

        String maxSeqEmployment = getMaxSequence_Employment(delegator, partyId, true);

        Map<String, Object> criteria = new HashMap<String, Object>();
        Map<String, Object> criteriaEmployment = new HashMap<String, Object>();
   java.sql.Date sqlTransDate = PayrollServices.convertFromStringToDate(strTransDate);
        String transType = PayrollServices.getLastEmployeeTransType_EmplSalaryGrade(delegator, partyId,sqlTransDate);
    
        boolean beganTransaction = false;
        if (transType.equals("MOVING")) {
            String maxSeq = PayrollServices.getMaxSequence_PositionGradation(delegator, partyId, transType);
         
            criteriaEmployment.put("employmentId", maxSeqEmployment);
            criteriaEmployment.put("partyIdTo", partyId);
            if (maxSeq.equals(rowId)) {
                criteria.put("emplSalaryGrade", maxSeq);
                boolean isExist = PayrollServices.isCalculatedPositionGradation(delegator, partyId, sqlTransDate);
                try {
                    if (!isExist) {

                        criteriaEmployment = UtilMisc.toMap("partyIdTo", partyId, "employmentId", maxSeqEmployment);
                        criteria = UtilMisc.toMap("emplSalaryGrade", maxSeq);
                        Map<String, Object> deleteEmlpoymentResult = dispatcher.runSync("deleteEmployment", criteriaEmployment);
                        if (ServiceUtil.isError(deleteEmlpoymentResult)) {
                            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError,
                                    "BonusError", locale), null, null, deleteEmlpoymentResult);

                        }
                        Map<String, Object> deleteEmplSalaryResult = dispatcher.runSync("deleteEmplSalaryGrade", criteria);
                        if (ServiceUtil.isError(deleteEmplSalaryResult)) {
                            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError,
                                    "BonusError", locale), null, null, deleteEmplSalaryResult);

                        }
//                        int remove = delegator.removeByAnd("EmplSalaryGrade", criteria);
//                        int removeFromful = delegator.removeByAnd("Employment", criteriaEmployment);
                     
                    } else {
                        try {
                            beganTransaction = TransactionUtil.begin();
                            // only rollback the transaction if we started one...
                            TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning :Can't delete, this record is calculated"));
                            Msg = "Warrning :Can't delete, this record is calculated";
                            return ServiceUtil.returnError(Msg);
                        } catch (GenericEntityException e) {
                            Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                            return ServiceUtil.returnError(e.getMessage());
                        }
                    }

                } catch (Exception ex) {

                    Logger.getLogger(MovingEmployee.class.getName()).log(Level.SEVERE, null, ex);

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
                return ServiceUtil.returnError(e.getMessage());
            }
        }
  Map<String, String> messageMap = UtilMisc.toMap("successMessage","Success");
                                    String successMsg = UtilProperties.getMessage(resourceError, "deleteSuccess", messageMap, locale);
                                  return ServiceUtil.returnSuccess(successMsg);
    }

    public static Map<String, Object> updateThruDateEmployment_DeleteMoving(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        String maxSeq = getMaxSequence_Employment(delegator, partyId, false);
        System.out.println("maxSeqFulfillment  " + maxSeq);
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("employmentId", maxSeq);
        criteria.put("partyIdTo", partyId);
        try {
            List<GenericValue> list = delegator.findList("Employment", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (int i = 0; i < list.size(); i++) {
                GenericValue gvValue = (GenericValue) list.get(i).clone();
                gvValue.set("thruDate", (java.sql.Date) null);
                gvValue.store();
                System.out.println("Done Updated");
               

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(MovingEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }

      Map<String, String> messageMap = UtilMisc.toMap("successMessage","Success");
                                    String successMsg = UtilProperties.getMessage(resourceError, "deleteSuccess", messageMap, locale);
                                  return ServiceUtil.returnSuccess(successMsg);
    }

}
