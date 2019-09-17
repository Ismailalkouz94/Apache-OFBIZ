package org.apache.ofbiz.humanres.leaves;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javafx.beans.binding.IntegerExpression;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.json.simple.JSONArray;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.common.activitiServices.WorkFlowServices;
import org.apache.ofbiz.humanres.activitiServices.VacationAndLeavesWorkFlow;
import org.apache.ofbiz.humanres.PersonUtilServices;
import org.apache.ofbiz.humanres.PayrollServices;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.GenericServiceException;

import java.sql.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Admin
 */
public class LeavesServices {

    public final static String module = LeavesServices.class.getName();
    public static final String resource = "HumanResUiLabels";
    public static final String resourceError = "HumanResErrorUiLabels";

    public static String getLeaveTypeId_DropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("EmplLeaveType", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("leaveTypeId") + "'>" + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(LeavesServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getEmplLeaveReasonTypeId_DropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        String str = "<select id='EmplLeaveReasonType' >";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("EmplLeaveReasonType", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("emplLeaveReasonTypeId") + "'>" + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(LeavesServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getEmplLeaveReason(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();

        JSONObject jsonRes = null;
        JSONArray arrayRes = new JSONArray();
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("EmplLeaveReasonType", true);
            for (GenericValue genericValue : gv1) {
                jsonRes = new JSONObject();
                jsonRes.put("emplLeaveReasonTypeId", genericValue.get("emplLeaveReasonTypeId"));
                jsonRes.put("description", genericValue.get("description"));
                arrayRes.add(jsonRes);

            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(arrayRes.toString());
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(LeavesServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getEmplPositionReportingTo(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String partyId = request.getParameter("partyId");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("emplPositionIdManagedBy", partyId);
        String str = "";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findList("EmplPositionReportingStruct", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue genericValue : gv1) {
                str = genericValue.get("emplPositionIdReportingTo").toString();
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(LeavesServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getEmployeeLeaveData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String partyId = request.getParameter("partyId");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyId", partyId);
        criteria.put("type", "LEAVE");

        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {

            List<GenericValue> list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    jsonRes = new JSONObject();

                    int hours = 0;
                    int minute = 0;
                    int second = 0;

                    int yaer = 0;
                    int month = 0;
                    int day = 0;

                    jsonRes.put("leaveId", row.get("leaveId"));
                    jsonRes.put("partyId", row.get("partyId"));
                    if (row.get("transDate") == null) {
                        jsonRes.put("transDate", row.get("transDate"));
                    } else {
                        jsonRes.put("transDate", row.get("transDate").toString());
                    }
                    jsonRes.put("leaveTypeId", row.get("leaveTypeId"));
                    jsonRes.put("emplLeaveReasonTypeId", (String) row.get("emplLeaveReasonTypeId"));

                    if (row.get("fromDate") != null) {
                        yaer = ((Timestamp) (row.get("fromDate"))).toLocalDateTime().getYear();
                        month = ((Timestamp) (row.get("fromDate"))).toLocalDateTime().getMonthValue();
                        day = ((Timestamp) (row.get("fromDate"))).toLocalDateTime().getDayOfMonth();
                    }
                    jsonRes.put("date", yaer + "-" + month + "-" + day);

                    if (row.get("fromDate") != null) {
                        hours = ((Timestamp) (row.get("fromDate"))).toLocalDateTime().getHour();
                        minute = ((Timestamp) (row.get("fromDate"))).toLocalDateTime().getMinute();
                        second = ((Timestamp) (row.get("fromDate"))).toLocalDateTime().getSecond();
                    }
                    jsonRes.put("fromDate", hours + ":" + minute + ":" + second);

                    if (row.get("thruDate") != null) {
                        hours = ((Timestamp) (row.get("thruDate"))).toLocalDateTime().getHour();
                        minute = ((Timestamp) (row.get("thruDate"))).toLocalDateTime().getMinute();
                        second = ((Timestamp) (row.get("thruDate"))).toLocalDateTime().getSecond();
                    }
                    jsonRes.put("thruDate", hours + ":" + minute + ":" + second);

                    jsonRes.put("approverPartyId", row.get("approverPartyId"));
                    jsonRes.put("leaveStatus", row.get("leaveStatus"));
                    jsonRes.put("description", row.get("description"));
                    jsonRes.put("type", row.get("type"));
                    jsonData.add(jsonRes);
//                }
                }
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(LeavesServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String toDate(long timestamp) {
        LocalDate date = Instant.ofEpochMilli(timestamp * 1000).atZone(ZoneId.systemDefault()).toLocalDate();
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String createLeave(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, GenericServiceException {
        System.out.println("from create Leave");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String Msg = null;
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        String partyId = request.getParameter("partyId");
        String emplLeaveReasonTypeId = request.getParameter("emplLeaveReasonTypeId");
        String strTransDate = request.getParameter("transDate");
//        java.sql.Timestamp fromDate = java.sql.Timestamp.valueOf(request.getParameter("fromDate_i18n"));
//        java.sql.Timestamp thruDate = java.sql.Timestamp.valueOf(request.getParameter("thruDate_i18n"));
        String date = request.getParameter("date");
        System.out.println(">> date " + date);

        java.sql.Date sqlTransDate_test = PayrollServices.convertFromStringToDate(strTransDate);
        System.out.println(">> sqlTransDate_test " + sqlTransDate_test);

        String leaveStatus = request.getParameter("leaveStatus");

        String strFromDate = request.getParameter("fromDate");
        String strThruDate = request.getParameter("thruDate");
        System.out.println(">> strFromDate " + strFromDate);


        String[] arrFromDate = strFromDate.split(":");
        String[] arrThruDate = strThruDate.split(":");

        strFromDate = date + " " + strFromDate + (Integer.parseInt(arrFromDate[0]) < 12 ? " AM" : " PM");
        strThruDate = date + " " + strThruDate + (Integer.parseInt(arrThruDate[0]) < 12 ? " AM" : " PM");

        System.out.println("date + fromDate " + strFromDate);
        System.out.println("date + strThruDate " + strThruDate);

        java.sql.Timestamp fromDate = PersonUtilServices.convertStringToTimestamp(strFromDate, true);
        java.sql.Timestamp thruDate = PersonUtilServices.convertStringToTimestamp(strThruDate, true);
        java.sql.Date sqlTransDate = PayrollServices.convertFromStringToDate(strTransDate);
        System.out.println("fromDate " + fromDate);
        System.out.println("thruDate " + thruDate);
        String leaveTypeId = request.getParameter("leaveTypeId");
        String description = request.getParameter("description");
        String type = request.getParameter("type");

        String leaveId = delegator.getNextSeqId("EmplLeave");

        Map<String, Object> criteria = new HashMap<>();
        boolean beganTransaction = false;

        try {
            if (fromDate == null || thruDate == null || sqlTransDate == null) {
                Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "");
//                String errMsg = UtilProperties.getMessage(resource, "ServerShutdown", messageMap, UtilHttp.getLocale(request));
                String errMsg = "Date cann't be null";
                throw new Exception(errMsg);
            }
            beganTransaction = TransactionUtil.begin();


            //to check if leave or vacation are conflict
//            List<EntityExpr> exprs = UtilMisc.toList(
//                    EntityCondition.makeCondition("fromDate", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate),
//                    EntityCondition.makeCondition("thruDate", EntityOperator.GREATER_THAN_EQUAL_TO, thruDate),
//                    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId));
//            EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);
//
//            List<GenericValue> list = delegator.findList("EmplLeave", condition, null, null, null, true);


//            if (list.isEmpty()) {
            criteria = UtilMisc.toMap("leaveId", leaveId,
                    "partyId", partyId,
                    "emplLeaveReasonTypeId", emplLeaveReasonTypeId,
                    "transDate", sqlTransDate,
                    "fromDate", PersonUtilServices.convertStringToTimestamp_custom(strFromDate, true),
                    "thruDate", PersonUtilServices.convertStringToTimestamp_custom(strThruDate, true),
                    "approverPartyId", "",
                    "leaveStatus", leaveStatus,
                    "description", description,
                    "type", type,
                    "leaveTypeId", leaveTypeId
            );
            Map<String, Object> createVacation = dispatcher.runSync("createEmplLeave", criteria);

//            String startWorkFlow = VacationAndLeavesWorkFlow.startVacations(request, partyId, description, (String) createVacation.get("leaveId"),"Leaves");
//            System.out.println("startWorkFlow " + startWorkFlow);
//            if (startWorkFlow.equals("error")) {
//                Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "");
//                String errMsg = UtilProperties.getMessage(resource, "ServerShutdown", messageMap, UtilHttp.getLocale(request));
//                throw new Exception(errMsg);
//            }
//            } else {
//                Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "");
////                String errMsg = UtilProperties.getMessage(resource, "ServerShutdown", messageMap, UtilHttp.getLocale(request));
//                String errMsg = "Cant Create Leave in this Date";
//                throw new Exception(errMsg);
//            }
        } catch (Exception ex) {
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                Logger.getLogger(LeavesServices.class.getName()).log(Level.SEVERE, null, ex);

                Msg = ex.getMessage();
                System.out.println("Msg " + Msg);
                request.setAttribute("_ERROR_MESSAGE_", Msg);
                return "error";
            } catch (GenericEntityException e) {
                Logger.getLogger(LeavesServices.class.getName()).log(Level.SEVERE, null, e);
                System.out.println(">>> " + e.getMessage());
                Msg = e.getMessage();
                request.setAttribute("_ERROR_MESSAGE_", Msg);
                return "error";
            }

        } finally {
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
        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "");
        String successMsg = UtilProperties.getMessage(resource, "createVacation", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", successMsg);

        return "success";
    }


    public static String updateLeave(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, GenericServiceException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String Msg = null;
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        String partyId = request.getParameter("partyId");
        String emplLeaveReasonTypeId = request.getParameter("emplLeaveReasonTypeId");
        String strTransDate = request.getParameter("transDate");
        String leaveStatus = request.getParameter("leaveStatus");
        String date = request.getParameter("date");

//


        String strFromDate = request.getParameter("fromDate");
        String strThruDate = request.getParameter("thruDate");

        String[] arrFromDate = strFromDate.split(":");
        String[] arrThruDate = strThruDate.split(":");

        strFromDate = date + " " + strFromDate + (Integer.parseInt(arrFromDate[0]) < 12 ? " AM" : " PM");
        strThruDate = date + " " + strThruDate + (Integer.parseInt(arrThruDate[0]) < 12 ? " AM" : " PM");

        java.sql.Timestamp fromDate = PersonUtilServices.convertStringToTimestamp(strFromDate, true);
        java.sql.Timestamp thruDate = PersonUtilServices.convertStringToTimestamp(strThruDate, true);
        java.sql.Date sqlTransDate = PayrollServices.convertFromStringToDate(strTransDate);
        System.out.println("strFromDate " + strFromDate);
        System.out.println("fromDate " + fromDate);
        System.out.println("strFromDate " + fromDate);
        String leaveTypeId = request.getParameter("leaveTypeId");
        String description = request.getParameter("description");
        String type = request.getParameter("type");

        String leaveId = request.getParameter("leaveId");

        Map<String, Object> criteria = new HashMap<>();
        boolean beganTransaction = false;

        try {
            if (fromDate == null || thruDate == null || sqlTransDate == null) {
                Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "");
//                String errMsg = UtilProperties.getMessage(resource, "ServerShutdown", messageMap, UtilHttp.getLocale(request));
                String errMsg = "Date cann't be null";
                throw new Exception(errMsg);
            }
            beganTransaction = TransactionUtil.begin();

            criteria = UtilMisc.toMap("leaveId", leaveId,
                    "partyId", partyId,
                    "emplLeaveReasonTypeId", emplLeaveReasonTypeId,
                    "transDate", sqlTransDate,
                    "fromDate", PersonUtilServices.convertStringToTimestamp(strFromDate, true),
                    "thruDate", PersonUtilServices.convertStringToTimestamp(strThruDate, true),
                    "approverPartyId", "",
                    "leaveStatus", leaveStatus,
                    "description", description,
                    "type", type,
                    "leaveTypeId", leaveTypeId
            );
            Map<String, Object> createVacation = dispatcher.runSync("updateEmplLeave", criteria);

        } catch (Exception ex) {
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                Logger.getLogger(LeavesServices.class.getName()).log(Level.SEVERE, null, ex);

                Msg = ex.getMessage();
                System.out.println("Msg " + Msg);
                request.setAttribute("_ERROR_MESSAGE_", Msg);
                return "error";
            } catch (GenericEntityException e) {
                Logger.getLogger(LeavesServices.class.getName()).log(Level.SEVERE, null, e);
                System.out.println(">>> " + e.getMessage());
                Msg = e.getMessage();
                request.setAttribute("_ERROR_MESSAGE_", Msg);
                return "error";
            }

        } finally {
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
        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "");
        String successMsg = UtilProperties.getMessage(resource, "createVacation", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", successMsg);

        return "success";
    }

    public static String getEmplLeaveDetails(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String leaveId = request.getParameter("leaveId");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("leaveId", leaveId);

        JSONObject jsonRes = new JSONObject();
        try {

            List<GenericValue> list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    jsonRes = new JSONObject();
                    jsonRes.put("leaveId", row.get("leaveId"));
                    jsonRes.put("partyId", row.get("partyId"));
                    jsonRes.put("leaveTypeId", row.get("leaveTypeId"));
                    jsonRes.put("emplLeaveReasonTypeId", row.get("emplLeaveReasonTypeId").toString());
                    if (row.get("transDate") == null) {
                        jsonRes.put("transDate", row.get("transDate"));
                    } else {
                        jsonRes.put("transDate", row.get("transDate").toString());
                    }

                    if (row.get("fromDate") == null) {
                        jsonRes.put("fromDate", row.get("fromDate"));
                    } else {
                        jsonRes.put("fromDate", row.get("fromDate").toString());
                    }

                    if (row.get("thruDate") == null) {
                        jsonRes.put("thruDate", row.get("thruDate"));
                    } else {
                        jsonRes.put("thruDate", row.get("thruDate").toString());
                    }
                    jsonRes.put("leaveStatus", row.get("leaveStatus"));
                    jsonRes.put("description", row.get("description"));
                    jsonRes.put("leaveTypeId", row.get("leaveTypeId"));

                    jsonRes.put("type", row.get("type"));

//                }
                }
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonRes.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(LeavesServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    //get leaves information for dashboard
    public static String getLeavesInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String partyId = request.getParameter("partyId");
        Map<String, Object> criteria = new HashMap<String, Object>();

        try {
//            ..........for leave..............
            JSONObject jsonRes = new JSONObject();
            criteria.put("partyId", partyId);
            criteria.put("type", "LEAVE");
            criteria.put("leaveStatus", "LEAVE_APPROVED");
            List<GenericValue> list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
            jsonRes.put("approved", list.size());

            criteria = new HashMap<String, Object>();
            criteria.put("partyId", partyId);
            criteria.put("type", "LEAVE");
            criteria.put("leaveStatus", "LEAVE_REJECTED");
            list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
            jsonRes.put("rejected", list.size());

            criteria = new HashMap<String, Object>();
            criteria.put("partyId", partyId);
            criteria.put("type", "LEAVE");
            criteria.put("leaveStatus", "LEAVE_CREATED");
            list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
            jsonRes.put("created", list.size());

            criteria = new HashMap<String, Object>();
            criteria.put("partyId", partyId);
            criteria.put("type", "LEAVE");
            list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
            jsonRes.put("total", list.size());

            JSONArray leaveData = new JSONArray();
            leaveData.add(jsonRes);

            //.................for vacation............................
            jsonRes = new JSONObject();
            criteria = new HashMap<String, Object>();
            criteria.put("partyId", partyId);
            criteria.put("type", "VACATION");
            criteria.put("leaveStatus", "LEAVE_APPROVED");
            list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
            jsonRes.put("approved", list.size());

            criteria = new HashMap<String, Object>();
            criteria.put("partyId", partyId);
            criteria.put("type", "VACATION");
            criteria.put("leaveStatus", "LEAVE_REJECTED");
            list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
            jsonRes.put("rejected", list.size());

            criteria = new HashMap<String, Object>();
            criteria.put("partyId", partyId);
            criteria.put("type", "VACATION");
            criteria.put("leaveStatus", "LEAVE_CREATED");
            list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
            jsonRes.put("created", list.size());

            criteria = new HashMap<String, Object>();
            criteria.put("partyId", partyId);
            criteria.put("type", "VACATION");
            list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
            jsonRes.put("total", list.size());

            JSONArray vacactionData = new JSONArray();
            vacactionData.add(jsonRes);

            //to store multiple object
            JSONObject finalResult = new JSONObject();
            finalResult.put("leave", leaveData);
            finalResult.put("vacation", vacactionData);

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(finalResult);
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(LeavesServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

}
