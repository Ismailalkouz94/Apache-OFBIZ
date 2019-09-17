/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.humanres.terminated;

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
import org.apache.ofbiz.base.util.UtilProperties;
import static org.apache.ofbiz.base.util.UtilValidate.module;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import org.apache.ofbiz.humanres.HumanResEvents;
import org.apache.ofbiz.humanres.PayrollServices;
import org.apache.ofbiz.humanres.moving.MovingEmployee;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.ofbiz.base.util.UtilHttp;
import static org.apache.ofbiz.humanres.PayrollServices.getMaxSequence_PositionGradation;

/**
 *
 * @author Rbab3a
 */
public class TerminatedEmployee {

    public final static String module = TerminatedEmployee.class.getName();
    public static final String securityProperties = "security.properties";
    public static final String resourceError = "HumanResErrorUiLabels";
    private static final String keyValue = UtilProperties.getPropertyValue(securityProperties, "login.secret_key_string");

    public static void getTerminationReasonDropDown(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {

        PrintWriter out = null;
        Map<String, String> criteria = new HashMap<String, String>();
        String terminationTypeId = request.getParameter("terminationTypeId");
        criteria.put("terminationTypeId", terminationTypeId);
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> list = delegator.findList("TerminationReason", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    jsonRes = new JSONObject();
                    jsonRes.put("terminationReasonId", row.get("terminationReasonId"));
                    jsonRes.put("description", row.get("description"));
                    jsonData.add(jsonRes);
                }
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(TerminatedEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String getTerminationReasonIdDescription(HttpServletRequest request, String terminationReasonId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        if (terminationReasonId == null) {
            return "";
        } else {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("terminationReasonId", terminationReasonId);
            String description = "";
            try {
                GenericValue result = delegator.findOne("TerminationReason", criteria, true);
                if (result != null) {
                    description = (String) result.get("description");
                    return description;
                } else {
                    description = "-";
                }
            } catch (GenericEntityException ex) {
                Logger.getLogger(TerminatedEmployee.class.getName()).log(Level.SEVERE, null, ex);
            }
            return description;
        }
    }

    public static String getTerminationTypeDescription(HttpServletRequest request, String terminationTypeId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        if (terminationTypeId == null) {
            return "";
        } else {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("terminationTypeId", terminationTypeId);
            String description = "";
            try {
                GenericValue result = delegator.findOne("TerminationType", criteria, true);
                if (result != null) {
                    description = (String) result.get("description");
                    return description;
                } else {
                    description = "-";
                }
            } catch (GenericEntityException ex) {
                Logger.getLogger(TerminatedEmployee.class.getName()).log(Level.SEVERE, null, ex);
            }
            return description;
        }
    }

    public static String getEmploymentData(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        PrintWriter out = null;
        Map<String, String> criteria = new HashMap<String, String>();

        String partyId = request.getParameter("partyId");
        criteria.put("partyIdTo", partyId);
        String emplPositionTypeId = "";
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> list = delegator.findList("Employment", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                jsonRes.put("employmentId", row.get("employmentId"));
                jsonRes.put("roleTypeIdFrom", row.get("roleTypeIdFrom"));
                jsonRes.put("roleTypeIdTo", row.get("roleTypeIdTo"));
                jsonRes.put("partyIdFrom", row.get("partyIdFrom"));
                jsonRes.put("partyIdTo", row.get("partyIdTo"));
                jsonRes.put("fromDate", row.get("fromDate").toString());
                if (row.get("thruDate") != null) {
                    jsonRes.put("thruDate", row.get("thruDate").toString());
                } else {
                    jsonRes.put("thruDate", "");
                }
                jsonRes.put("terminationReasonId", row.get("terminationReasonId"));
                jsonRes.put("reasonDescription", getTerminationReasonIdDescription(request, (String) row.get("terminationReasonId")));
                jsonRes.put("terminationTypeId", row.get("terminationTypeId"));
                jsonRes.put("typeDescription", getTerminationTypeDescription(request, (String) row.get("terminationTypeId")));
                jsonRes.put("depDescription", MovingEmployee.getDeprtmentDescription(request, (String) row.get("partyIdFrom")));

                jsonRes.put("fullName", HumanResEvents.getPartyName(request, response, partyId));
                jsonData.add(jsonRes);
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(TerminatedEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static Map<String, Object> updateDateEmployment_Terminated(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        String serviceType = (String) context.get("serviceType");
        String terminationReasonId = "";
        String thruDate = "";
        String terminationTypeId = "";
        LocalDate endDate = null;
        String maxSeq = "";
        System.out.println("serviceType " + serviceType);
        java.sql.Date sqlThruDate = null;
        if (serviceType.equalsIgnoreCase("terminated")) {
            terminationReasonId = (String) context.get("terminationReasonId");
            terminationTypeId = (String) context.get("terminationTypeId");
            thruDate = (String) context.get("thruDate");
            sqlThruDate = PayrollServices.convertFromStringToDate(thruDate);
            endDate = sqlThruDate.toLocalDate();
            maxSeq = MovingEmployee.getMaxSequence_Employment(delegator, partyId, true);
           

        } else {
             maxSeq = MovingEmployee.getMaxSequence_Employment(delegator, partyId, false);

        }
         System.out.println("maxSeqFulfillment  " + maxSeq);
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("employmentId", maxSeq);
        criteria.put("partyIdTo", partyId);
        boolean beganTransaction = false;

        try {
            List<GenericValue> list = delegator.findList("Employment", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (int i = 0; i < list.size(); i++) {
                GenericValue gvValue = (GenericValue) list.get(i).clone();
                if (endDate != null) {
                    gvValue.set("thruDate", java.sql.Date.valueOf(endDate));
                } else {
                    gvValue.set("thruDate", null);
                }
                gvValue.set("terminationTypeId", terminationTypeId);
                gvValue.set("terminationReasonId", terminationReasonId);
                gvValue.store();
                System.out.println("Done Updated");

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(TerminatedEmployee.class.getName()).log(Level.SEVERE, null, ex);
            try {
                beganTransaction = TransactionUtil.begin();
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : this is not last action "));
//                Msg = "Warrning : this is not last action ";
                return ServiceUtil.returnError(ex.getMessage());
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
//                Msg = "Warrning : this is not last action ";
                return ServiceUtil.returnError(e.getMessage());
            }
        }

        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
        String successMsg = UtilProperties.getMessage(resourceError, "terminated", messageMap, locale);
        return ServiceUtil.returnSuccess(successMsg);
    }

    public static Map<String, Object> updateThruDateEmplFulfillment_Terminated(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = "";
        String thruDate = "";
        LocalDate endDate = null;
        java.sql.Date sqlThruDate = null;
        String serviceType = (String) context.get("serviceType");
        if (serviceType.equalsIgnoreCase("terminated")) {
            partyId = (String) context.get("partyId");
            thruDate = (String) context.get("thruDate");
            sqlThruDate = PayrollServices.convertFromStringToDate(thruDate);
            endDate = sqlThruDate.toLocalDate();
        }

        String maxFulfillement = PayrollServices.getMaxSeq_EmplPositionFullFillment(delegator, partyId, true);

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyId", partyId);
        criteria.put("emplPositionFulfillmentId", maxFulfillement);
        boolean beganTransaction = false;

        try {
            List<GenericValue> list = delegator.findList("EmplPositionFulfillment", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (int i = 0; i < list.size(); i++) {
                GenericValue gvValue = (GenericValue) list.get(i).clone();
                gvValue.set("thruDate", java.sql.Date.valueOf(endDate));
                gvValue.store();
                System.out.println("Done Updated");

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(TerminatedEmployee.class.getName()).log(Level.SEVERE, null, ex);
            try {
                beganTransaction = TransactionUtil.begin();
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : this is not last action "));
//                Msg = "Warrning : this is not last action ";
                return ServiceUtil.returnError(ex.getMessage());
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
//                Msg = "Warrning : this is not last action ";
                return ServiceUtil.returnError(e.getMessage());
            }
        }
        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
        String successMsg = UtilProperties.getMessage(resourceError, "terminated", messageMap, locale);
        return ServiceUtil.returnSuccess(successMsg);
    }

    public static Map<String, Object> updateEmplStatusToTerminated_Terminated(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyId", partyId);
        boolean beganTransaction = false;
        try {

            List<GenericValue> list = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (int i = 0; i < list.size(); i++) {
                GenericValue gvValue = (GenericValue) list.get(i).clone();
//                gvValue.set("EmplStatus", "TERMINATED");
//                gvValue.store();
                System.out.println("Done Updated");

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(TerminatedEmployee.class.getName()).log(Level.SEVERE, null, ex);
            try {
                beganTransaction = TransactionUtil.begin();
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : this is not last action "));
//                Msg = "Warrning : this is not last action ";
                return ServiceUtil.returnError(ex.getMessage());
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
//                Msg = "Warrning : this is not last action ";
                return ServiceUtil.returnError(e.getMessage());
            }

        }
        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
        String successMsg = UtilProperties.getMessage(resourceError, "terminated", messageMap, locale);
        return ServiceUtil.returnSuccess(successMsg);
    }

    public static String getEmplPositionId(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        PrintWriter out = null;
        Map<String, String> criteria = new HashMap<String, String>();
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        String partyId = request.getParameter("partyId");
        String maxFulfillement = PayrollServices.getMaxSeq_EmplPositionFullFillment(delegator, partyId, true);
        criteria.put("partyId", partyId);
        criteria.put("emplPositionFulfillmentId", maxFulfillement);
        String positionId = "";
        try {
            List<GenericValue> listData = delegator.findList("EmplPositionFulfillment", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : listData) {
                positionId = (String) row.get("emplPositionId");
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(positionId);
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(MovingEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }
        return positionId;
    }

    public static Map<String, Object> updateEmplPositionStatus(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String emplPositionId = (String) context.get("emplPositionId");
        boolean beganTransaction = false;
        Map<String, String> currentPositionIdTable = new HashMap<String, String>();
        currentPositionIdTable.put("emplPositionId", emplPositionId);
        try {
            beganTransaction = TransactionUtil.begin();

            List<GenericValue> list = delegator.findList("EmplPosition", EntityCondition.makeCondition(currentPositionIdTable), null, null, null, true);
            for (int i = 0; i < list.size(); i++) {
                GenericValue gvValue = (GenericValue) list.get(i).clone();
                gvValue.set("statusId", "EMPL_POS_ACTIVE");
                gvValue.store();
            }

        } catch (GenericEntityException ex) {
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
        String successMsg = UtilProperties.getMessage(resourceError, "terminated", messageMap, locale);
        return ServiceUtil.returnSuccess(successMsg);
    }

    public static Map<String, Object> updateEmplPositionStatus_DeleteTerminated(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        boolean beganTransaction = false;
        String positionId = (String) context.get("positionId");
        String partyId = (String) context.get("partyId");
        String strTransDate = (String) context.get("transDate");

        Map<String, String> creiteria = new HashMap<String, String>();
        creiteria.put("emplPositionId", positionId);
        java.sql.Date sqlTransDate = PayrollServices.convertFromStringToDate(strTransDate);
        String transType = PayrollServices.getLastEmployeeTransType_EmplSalaryGrade(delegator, partyId, sqlTransDate);
   Map<String, Object> criteriaEmpl = new HashMap<String, Object>();
    String maxSeq = getMaxSequence_PositionGradation(delegator, partyId, "TERMINATED");
        try {
            if (!transType.equals("TERMINATED")) {
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
            beganTransaction = TransactionUtil.begin();

            String posStatus = PayrollServices.getPreviousPositionStatus_EmplPositionFullFillment(delegator, positionId);
            if (posStatus.equals("EMPL_POS_ACTIVE")) {
                List<GenericValue> list = delegator.findList("EmplPosition", EntityCondition.makeCondition(creiteria), null, null, null, true);
                for (int i = 0; i < list.size(); i++) {
                    GenericValue gvValue = (GenericValue) list.get(i).clone();
                    gvValue.set("statusId", "EMPL_POS_OCCUPIED");
                    gvValue.store();
                }
                       criteriaEmpl = UtilMisc.toMap("emplSalaryGrade", maxSeq);
                  Map<String, Object> deleteEmplSalaryResult = dispatcher.runSync("deleteEmplSalaryGrade", criteriaEmpl);
                    if (ServiceUtil.isError(deleteEmplSalaryResult)) {
                        return ServiceUtil.returnError(UtilProperties.getMessage(resourceError,
                                "BonusError", locale), null, null, deleteEmplSalaryResult);

                    }
            } else if (!posStatus.equals("EMPL_POS_ACTIVE")) {

                try {
                    beganTransaction = TransactionUtil.begin();
                    // only rollback the transaction if we started one...
                    TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : Position OCCUPIED"));
                    Msg = " Warrning : The previous Position is OCCUPIED for another employee please delete it or create new position with same"
                            + " details about previous position ";
                    return ServiceUtil.returnError(Msg);
                } catch (GenericEntityException e) {
                    Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                    Msg = "Warrning : The previous Position is OCCUPIED for another employee please delete it or create new position with same"
                            + " details about previous position ";
                    return ServiceUtil.returnError(Msg);
                }

            }
            Msg = "Done";
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);

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
        String successMsg = UtilProperties.getMessage(resourceError, "deleteSuccess", messageMap, locale);
        return ServiceUtil.returnSuccess(successMsg);
    }

}
