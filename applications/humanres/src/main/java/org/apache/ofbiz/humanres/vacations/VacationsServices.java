/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.humanres.vacations;
//import com.google.api.client.googleapis.util.Utils

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import org.apache.ofbiz.humanres.PayrollServices;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.humanres.PersonUtilServices;
import org.apache.ofbiz.common.activitiServices.WorkFlowServices;
import org.apache.ofbiz.humanres.activitiServices.VacationAndLeavesWorkFlow;
import org.apache.ofbiz.humanres.employee.EmployeePosition;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;
import javax.servlet.RequestDispatcher;

import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.service.GenericServiceException;

/**
 * @author Admin
 */
public class VacationsServices {

    public final static String module = VacationsServices.class.getName();
    public static final String resource = "HumanResUiLabels";
    public static final String resourceError = "HumanResErrorUiLabels";

    public static String createVacation(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, GenericServiceException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String Msg = null;
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        String partyId = "";
        String partyIdFromHr = request.getParameter("partyId");
        String jsonText = request.getParameter("jsonText");
        String partyIdFromPortal = request.getParameter("partyIdFromPortal");
        System.out.println("partyIdFromHr " + partyIdFromHr);
        System.out.println("partyIdFromPortal " + partyIdFromPortal);
        if (partyIdFromHr != null) {
            partyId = partyIdFromHr;
        } else if (partyIdFromPortal != null) {
            partyId = partyIdFromPortal;
        }
        String emplLeaveReasonTypeId = request.getParameter("emplLeaveReasonTypeId");
        String strTransDate = request.getParameter("transDate");
//        java.sql.Timestamp fromDate = java.sql.Timestamp.valueOf(request.getParameter("fromDate_i18n"));
//        java.sql.Timestamp thruDate = java.sql.Timestamp.valueOf(request.getParameter("thruDate_i18n"));
        String strFromDate = request.getParameter("fromDate");
        String strThruDate = request.getParameter("thruDate");
        java.sql.Timestamp fromDate = PersonUtilServices.convertStringToTimestamp(strFromDate, false);
        java.sql.Timestamp thruDate = PersonUtilServices.convertStringToTimestamp(strThruDate, false);

        java.sql.Date sqlFromDate = PayrollServices.convertFromStringToDate(strFromDate);
        java.sql.Date sqlThruDate = PayrollServices.convertFromStringToDate(strThruDate);

        java.sql.Date sqlTransDate = PayrollServices.convertFromStringToDate(strTransDate);

        System.out.println("strFromDate " + fromDate);
        String routeFrom = request.getParameter("routeFrom");
        String routeTo = request.getParameter("routeTo");
        String noOfDays = request.getParameter("noOfDays");
        String description = request.getParameter("description");
        String leaveStatus = request.getParameter("leaveStatus");
        String ticketRequired = request.getParameter("ticketRequired");
        String ticketChargeTo = request.getParameter("ticketChargeTo");
        String visaRequired = request.getParameter("visaRequired");
        String visaChargeTo = request.getParameter("visaChargeTo");
        String type = request.getParameter("type");

        String leaveId = delegator.getNextSeqId("EmplLeave");

        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        String userLoginId = null;
        if(request.getParameterMap().containsKey("userLoginId")){
            userLoginId = request.getParameter("userLoginId");
        }else{
            userLoginId = (String) userLogin.getString("userLoginId");
        }

        Map<String, Object> criteria = new HashMap<>();
        boolean beganTransaction = false;

        try {
            beganTransaction = TransactionUtil.begin();
            if (fromDate == null || thruDate == null || sqlTransDate == null) {
                Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "");
//                String errMsg = UtilProperties.getMessage(resource, "ServerShutdown", messageMap, UtilHttp.getLocale(request));
                String errMsg = "Date cann't be null";
                throw new Exception(errMsg);
            }

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
                    "fromDate", fromDate,
                    "thruDate", thruDate,
                    "approverPartyId", "",
                    "leaveStatus", leaveStatus,
                    "description", description,
                    "type", type
            );
            Map<String, Object> createVacation = dispatcher.runSync("createEmplLeave", criteria);
            System.out.println("createVacation " + createVacation);

            String EmplLeaveExtId = delegator.getNextSeqId("EmplLeaveExtension");
            Map<String, Object> LeaveExtension_criteria = new HashMap<>();
            LeaveExtension_criteria = UtilMisc.toMap(
                    "EmplLeaveExtId", EmplLeaveExtId,
                    "leaveId", leaveId,
                    "routeFrom", routeFrom,
                    "routeTo", routeTo,
                    "noOfDays", noOfDays,
                    "ticketRequired", ticketRequired,
                    "ticketChargeTo", ticketChargeTo,
                    "visaRequired", visaRequired,
                    "visaChargeTo", visaChargeTo
            );
            Map<String, Object> createVacationExtension = dispatcher.runSync("createEmplLeaveExtention", LeaveExtension_criteria);
            System.out.println("createVacationExtension " + createVacationExtension);
            System.out.println("jsonText " + jsonText.length());

            if (jsonText.length() != 0) {
                JSONParser parser = new JSONParser();
                JSONArray json = (JSONArray) parser.parse(jsonText);
                for (int i = 0; i < json.size(); i++) {
                    System.out.println("1");
                    JSONObject jsonObj = new JSONObject((Map) json.get(i));
                    System.out.println(jsonObj.get("partyId"));
                    System.out.println("2");
                    String EmplLeaveExtFMTicketId = delegator.getNextSeqId("EmplLeaveExtFMTicket");
                    System.out.println("3");
                    Map<String, Object> EmplLeaveExtFMTicket_criteria = new HashMap<>();
                    EmplLeaveExtFMTicket_criteria = UtilMisc.toMap(
                            "EmplLeaveExtFMTicketId", EmplLeaveExtFMTicketId,
                            "leaveId", leaveId,
                            "partyId", jsonObj.get("partyId"),
                            "departureDate", PayrollServices.convertFromStringToDate((String) jsonObj.get("departureDate")),
                            "returnDate", PayrollServices.convertFromStringToDate((String) jsonObj.get("returnDate")),
                            "DOB", PayrollServices.convertFromStringToDate((String) jsonObj.get("DOB"))
                    );

                    System.out.println("4");

                    Map<String, Object> createVacationExtensionFM = dispatcher.runSync("createEmplLeaveExtentionFM", EmplLeaveExtFMTicket_criteria);

                    System.out.println("createVacationExtensionFM " + createVacationExtensionFM);
                    System.out.println("5");

                }
            }

            EmployeePosition employeePosition = null;
            if (emplLeaveReasonTypeId.equals("UN_PAID")) {
                // insert Hold

                employeePosition = PersonUtilServices.getEmployeePositionDetails(request, partyId);
                System.out.println("employeePosition " + employeePosition.getTransDate() + " "
                        + employeePosition.getBasicSalary());
                String emplSalaryGrade = delegator.getNextSeqId("EmplSalaryGrade");
                Map<String, Object> createHoldData = new HashMap<>();
                createHoldData = UtilMisc.toMap(
                        "emplSalaryGrade", emplSalaryGrade,
                        "degreeId", employeePosition.getDegreeId(),
                        "gradeId", employeePosition.getGradeId(),
                        "TransDate", sqlFromDate,
                        "endDate", sqlThruDate,
                        "emplPositionId", employeePosition.getEmplPositionId(),
                        "jobGroupId", employeePosition.getJobGroupId(),
                        "TransType", "HOLD",
                        "partyIdFrom", employeePosition.getPartyIdFrom(),
                        "notes", "unpaid vacation",
                        "vacationId", leaveId,
                        "userLoginId", userLoginId,
                        "partyId", partyId
                );
                Map<String, Object> createHOLDResult = dispatcher.runSync("createEmplSalaryGradeService", createHoldData);
                System.out.println("createHOLDResult " + createHOLDResult);

            }


//            String startWorkFlow = VacationAndLeavesWorkFlow.startVacations(request, partyId, description, (String) createVacation.get("leaveId"),"Vacations");
//            System.out.println("startWorkFlow " + startWorkFlow);
//            if (startWorkFlow.equals("error")) {
//                Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "");
//                String errMsg = UtilProperties.getMessage(resource, "ServerShutdown", messageMap, UtilHttp.getLocale(request));
//                throw new Exception(errMsg);
//            }
//            }else {
//                Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "");
////                String errMsg = UtilProperties.getMessage(resource, "ServerShutdown", messageMap, UtilHttp.getLocale(request));
//                String errMsg = "Cant Create Vacation in this Date";
//                throw new Exception(errMsg);
//            }
        } catch (Exception ex) {
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                Logger.getLogger(VacationsServices.class.getName()).log(Level.SEVERE, null, ex);

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


    public static String updateVacation(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, GenericServiceException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String Msg = null;
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        String partyId = "";
        String partyIdFromHr = request.getParameter("partyId");
        String partyIdFromPortal = request.getParameter("partyIdFromPortal");
        System.out.println("partyIdFromPortal " + partyIdFromPortal);
        if (partyIdFromHr != null) {
            partyId = partyIdFromHr;
        } else if (partyIdFromPortal != null) {
            partyId = partyIdFromPortal;
        }

        String emplLeaveReasonTypeId = request.getParameter("emplLeaveReasonTypeId");
        String strTransDate = request.getParameter("transDate");
        String strFromDate = request.getParameter("fromDate");
        String strThruDate = request.getParameter("thruDate");
        java.sql.Timestamp fromDate = PersonUtilServices.convertStringToTimestamp(strFromDate, false);
        java.sql.Timestamp thruDate = PersonUtilServices.convertStringToTimestamp(strThruDate, false);

        java.sql.Date sqlFromDate = PayrollServices.convertFromStringToDate(strFromDate);
        java.sql.Date sqlThruDate = PayrollServices.convertFromStringToDate(strThruDate);

        java.sql.Date sqlTransDate = PayrollServices.convertFromStringToDate(strTransDate);

        String description = request.getParameter("description");
        String leaveStatus = request.getParameter("leaveStatus");
        String type = request.getParameter("type");

        String leaveId = request.getParameter("leaveId");

        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
//        String userLoginId = userLogin.getString("userLoginId");
        String userLoginId = "admin";

        Map<String, Object> criteria = new HashMap<>();
        boolean beganTransaction = false;

        try {
            beganTransaction = TransactionUtil.begin();
            if (fromDate == null || thruDate == null || sqlTransDate == null) {
                Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "");
//                String errMsg = UtilProperties.getMessage(resource, "ServerShutdown", messageMap, UtilHttp.getLocale(request));
                String errMsg = "Date cann't be null";
                throw new Exception(errMsg);
            }


            criteria = UtilMisc.toMap("leaveId", leaveId,
                    "partyId", partyId,
                    "emplLeaveReasonTypeId", emplLeaveReasonTypeId,
                    "transDate", sqlTransDate,
                    "fromDate", fromDate,
                    "thruDate", thruDate,
                    "approverPartyId", "",
                    "leaveStatus", leaveStatus,
                    "description", description,
                    "type", type
            );
            Map<String, Object> createVacation = dispatcher.runSync("updateEmplLeave", criteria);
            System.out.println("createVacation " + createVacation);

            EmployeePosition employeePosition = null;
            if (emplLeaveReasonTypeId.equals("UN_PAID")) {

                Map<String, Object> holdCcriteria = new HashMap<String, Object>();
                holdCcriteria.put("vacationId", leaveId);
                List<GenericValue> list = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(holdCcriteria), null, null, null, true);
                System.out.println(">> EmplSalaryGrade >> "+list);
                employeePosition = PersonUtilServices.getEmployeePositionDetails(request, partyId);
                System.out.println("employeePosition " + employeePosition.getTransDate() + " "
                        + employeePosition.getBasicSalary());
                Map<String, Object> createHoldData = new HashMap<>();
                createHoldData = UtilMisc.toMap(
                        "emplSalaryGrade",list.get(0).get("emplSalaryGrade"),
                        "TransDate", sqlFromDate,
                        "endDate", sqlThruDate,
                        "vacationId", leaveId,
                        "userLoginId", userLoginId,
                        "partyId", partyId
                );
                Map<String, Object> createHOLDResult = dispatcher.runSync("updateEmplSalaryGrade_Hold", createHoldData);

            }

        } catch (Exception ex) {
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                Logger.getLogger(VacationsServices.class.getName()).log(Level.SEVERE, null, ex);

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


    public static String getEmployeeVacationData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String partyId = request.getParameter("partyId");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyId", partyId);
        criteria.put("type", "VACATION");

        JSONArray jsonData = new JSONArray();
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
            Logger.getLogger(VacationsServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getEmployeeVacationExtData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String leaveId = request.getParameter("leaveId");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("leaveId", leaveId);

        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {

            List<GenericValue> list = delegator.findList("EmplLeaveExtension", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    jsonRes = new JSONObject();

                    jsonRes.put("EmplLeaveExtId", row.get("EmplLeaveExtId"));
                    jsonRes.put("routeFrom", row.get("routeFrom"));
                    jsonRes.put("routeTo", row.get("routeTo"));
                    jsonRes.put("noOfDays", row.get("noOfDays"));
                    jsonRes.put("ticketRequired", row.get("ticketRequired"));
                    jsonRes.put("ticketChargeTo", row.get("ticketChargeTo"));
                    jsonRes.put("visaRequired", row.get("visaRequired"));
                    jsonRes.put("visaChargeTo", row.get("visaChargeTo"));
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
            Logger.getLogger(VacationsServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getEmployeeVacationExtFMData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String leaveId = request.getParameter("leaveId");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("leaveId", leaveId);

        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {

            List<GenericValue> list = delegator.findList("EmplLeaveExtFMTicket", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    jsonRes = new JSONObject();

                    jsonRes.put("EmplLeaveExtFMTicketId", row.get("EmplLeaveExtFMTicketId"));
                    jsonRes.put("partyId", row.get("partyId"));

                    if (row.get("DOB") == null) {
                        jsonRes.put("DOB", row.get("DOB"));
                    } else {
                        jsonRes.put("DOB", row.get("DOB").toString());
                    }

                    if (row.get("departureDate") == null) {
                        jsonRes.put("departureDate", row.get("departureDate"));
                    } else {
                        jsonRes.put("departureDate", row.get("departureDate").toString());
                    }

                    if (row.get("returnDate") == null) {
                        jsonRes.put("returnDate", row.get("returnDate"));
                    } else {
                        jsonRes.put("returnDate", row.get("returnDate").toString());
                    }

                    jsonData.add(jsonRes);
                }
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(VacationsServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getEmplVacationDetails(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String leaveId = request.getParameter("leaveId");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("leaveId", leaveId);

        JSONObject jsonRes = new JSONObject();
        try {

            List<GenericValue> list = delegator.findList("EmplVacationDetails", EntityCondition.makeCondition(criteria), null, null, null, true);
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

                    jsonRes.put("approverPartyId", row.get("approverPartyId"));
                    jsonRes.put("leaveStatus", row.get("leaveStatus"));
                    jsonRes.put("description", row.get("description"));
                    jsonRes.put("type", row.get("type"));

                    jsonRes.put("EmplLeaveExtId", row.get("EmplLeaveExtId"));
                    jsonRes.put("routeFrom", row.get("routeFrom"));
                    jsonRes.put("routeTo", row.get("routeTo"));
                    jsonRes.put("noOfDays", row.get("noOfDays"));
                    jsonRes.put("ticketRequired", row.get("ticketRequired"));
                    jsonRes.put("ticketChargeTo", row.get("ticketChargeTo"));
                    jsonRes.put("visaRequired", row.get("visaRequired"));
                    jsonRes.put("visaChargeTo", row.get("visaChargeTo"));

//                }
                }
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonRes.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(VacationsServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String deleteVacation(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        boolean beganTransaction = false;
        Map<String, String> messageMap;

        String leaveId = request.getParameter("leaveId");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("leaveId", leaveId);

        Map<String, Object> removeHoldCriteria = new HashMap<String, Object>();
        removeHoldCriteria.put("vacationId", leaveId);

        try {
            beganTransaction = TransactionUtil.begin();
            int removeEmplLeaveExtFMTicket = delegator.removeByAnd("EmplLeaveExtFMTicket", criteria);
            int removeEmplLeaveExtension = delegator.removeByAnd("EmplLeaveExtension", criteria);

            int removeEmplLeave = delegator.removeByAnd("EmplLeave", criteria);

            int removeHold = delegator.removeByAnd("EmplSalaryGrade", removeHoldCriteria);


        } catch (GenericEntityException ex) {
            Logger.getLogger(VacationsServices.class
                    .getName()).log(Level.SEVERE, null, ex);
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                messageMap = UtilMisc.toMap("errorMessage", ex.getMessage());
                String errMsg = UtilProperties.getMessage(resourceError, "", messageMap, UtilHttp.getLocale(request));
                request.setAttribute("_ERROR_MESSAGE_", ex.getMessage());
                return "error";

            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                return "error";
            }

        } finally {
            // only commit the transaction if we started one... this will throw an exception if it fails
            try {
                TransactionUtil.commit(beganTransaction);
                messageMap = UtilMisc.toMap("successMessage", "");

            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
                String errMsg = UtilProperties.getMessage(resourceError, "", messageMap, UtilHttp.getLocale(request));
                request.setAttribute("_ERROR_MESSAGE_", errMsg);
                return "error";
            }
        }
        String successMsg = UtilProperties.getMessage(resourceError, "deleteSuccess", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", successMsg);
        return "";

    }

    public static String updateVacationAndLeave_Status(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String Msg = "";
        HttpSession session = request.getSession();
        JSONObject mainJson = new JSONObject();
        JSONArray jsonVariablesArray = new JSONArray();
        RequestDispatcher dispatcher = null;
        String processName = (String) request.getSession().getAttribute("processName");
        session.setAttribute("processName", processName);
        String action = request.getParameter("action");
        String finalTask = request.getParameter("finalTask");
        String leaveStatus = "";
        String leaveId = request.getParameter("leaveId");
        Map<String, String> taskData = new HashMap<String, String>();
        taskData.put("action", action);
        taskData.put("remark", request.getParameter("remark"));
        taskData.put("processId", request.getParameter("processId"));
        taskData.put("currnetDate", request.getParameter("currnetDate"));
        taskData.put("processInstanceId", request.getParameter("processInstanceId"));
        taskData.put("empId", request.getParameter("empId1"));
        taskData.put("processName", processName);
        taskData.put("taskId2", request.getParameter("taskId2"));

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("leaveId", leaveId);
        boolean beganTransaction = false;

        try {
            if (action.equals("2") || finalTask.equals("1")) {
                if (action.equals("2")) {
                    leaveStatus = "LEAVE_REJECTED";

                } else if (finalTask.equals("1")) {
                    leaveStatus = "LEAVE_APPROVED";
                }

                try {
                    beganTransaction = TransactionUtil.begin();
                    List<GenericValue> list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
                    GenericValue gvValue = (GenericValue) list.get(0).clone();
                    gvValue.set("leaveStatus", leaveStatus);
                    gvValue.store();
                } catch (GenericEntityException ex) {
                    try {
                        // only rollback the transaction if we started one...
                        TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                        return "error";
                    } catch (GenericEntityException e) {
                        Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                        return "error";
                    }

                } finally {
                    // only commit the transaction if we started one... this will throw an exception if it fails
                    try {
                        TransactionUtil.commit(beganTransaction);

                    } catch (GenericEntityException e) {
                        Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                        return "error";
                    }
                }

            }

//            String startWorkFlow = WorkFlowServices.completeTask(request, taskData);
//            System.out.println("completeTask " + startWorkFlow);
//            if (startWorkFlow.equals("error")) {
//                Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "");
//                String errMsg = UtilProperties.getMessage(resource, "ServerShutdown", messageMap, UtilHttp.getLocale(request));
//                throw new Exception(errMsg);
//            }
        } catch (Exception ex) {
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                Logger.getLogger(VacationsServices.class.getName()).log(Level.SEVERE, null, ex);

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

        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Your response send successfully");
        String successMsg = UtilProperties.getMessage(resource, "Your response send successfully", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", successMsg);
        return "success";
    }

}
