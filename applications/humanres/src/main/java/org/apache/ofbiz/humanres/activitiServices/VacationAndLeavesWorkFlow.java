/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rbab3a
 */
package org.apache.ofbiz.humanres.activitiServices;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import javax.servlet.http.HttpSession;
import org.apache.ofbiz.common.activitiServices.WorkFlowServices;
import org.apache.ofbiz.entity.GenericValue;
import org.json.simple.JSONArray;
import org.apache.ofbiz.humanres.PersonUtilServices;
import org.apache.ofbiz.humanres.PayrollServices;
import org.json.simple.JSONObject;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.base.util.Debug;

public class VacationAndLeavesWorkFlow {

    public final static String module = VacationAndLeavesWorkFlow.class.getName();
    public static final String resource = "HumanResUiLabels";
    static String id = "";
    static String password = "";
    public static String jsonResponse = "";
    static GenericValue userLogin = null;

    public static String startVacations(HttpServletRequest request, String partyId, String description, String leaveId,String type) throws SQLException, ParseException, IOException {
        String emplPostionId = PersonUtilServices.getPositionIdByPartyId(request, partyId);
        JSONObject mainJson = new JSONObject();
        String label = "";
        if(type.equals("Leaves")){
            label = "leave";
        }else if( type.equals("Vacations")) {
            label = "vacation";
        }
        boolean beganTransaction = false;
        JSONArray jsonVariablesArray = new JSONArray();
        Map<String, Object> approvers = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        approvers = PersonUtilServices.getApproverPositions(request, emplPostionId);
        System.out.println("approvers " + approvers);
        String hrPostionId = PersonUtilServices.getHrPositionId(request);
        System.out.println("hrPostionId " + hrPostionId);
        String dmPositionId = (String) approvers.get("positionIdParent");
        String ddPostionId = (String) approvers.get("positionIdParent2");
        String ceoPostionId = (String) approvers.get("positionIdParent3");

        String hrPartyId = PersonUtilServices.getPartyIdByPositionId(request, hrPostionId);
        String dmPartyId = PersonUtilServices.getPartyIdByPositionId(request, dmPositionId);
        String ddPartyId = PersonUtilServices.getPartyIdByPositionId(request, ddPostionId);
        String ceoPartyId = PersonUtilServices.getPartyIdByPositionId(request, ceoPostionId);
        System.out.println("PartyId " + hrPartyId + " " + dmPartyId
                + " " + ddPartyId + " " + ceoPartyId);

        String userLoginHR = PersonUtilServices.getUserLoginIdByPartyId(request, hrPartyId);
        String userLoginDM = PersonUtilServices.getUserLoginIdByPartyId(request, dmPartyId);
        String userLoginDD = PersonUtilServices.getUserLoginIdByPartyId(request, ddPartyId);
        String userLoginCEO = PersonUtilServices.getUserLoginIdByPartyId(request, ceoPartyId);
        System.out.println("UserLogin " + userLoginHR + " " + userLoginDM
                + " " + userLoginDD + " " + userLoginCEO);
        String requestorUserLogin = PersonUtilServices.getUserLoginIdByPartyId(request, partyId);;
        String jsonResponse = "";
        String processId = WorkFlowServices.getProcessName(request, type, 1);
        HttpSession session = request.getSession();
        String processName = WorkFlowServices.getProcessName(request, type, 0);
        session.setAttribute("processNameId", processId);
        JSONObject jsonVariables = new JSONObject();
        jsonVariables.put("name", "remarks");
        jsonVariables.put("value", "Send "+label +" to approval");
        jsonVariablesArray.add(jsonVariables);

        jsonVariables = new JSONObject();
        jsonVariables.put("name", "partyId");
        jsonVariables.put("value", partyId);
        jsonVariablesArray.add(jsonVariables);

        jsonVariables = new JSONObject();
        jsonVariables.put("name", "action");
        jsonVariables.put("value", "start");
        jsonVariablesArray.add(jsonVariables);

        jsonVariables = new JSONObject();
        jsonVariables.put("name", "dm");
        jsonVariables.put("value", userLoginDM);
        jsonVariablesArray.add(jsonVariables);

        jsonVariables = new JSONObject();
        jsonVariables.put("name", "leaveId");
        jsonVariables.put("value", leaveId);
        jsonVariablesArray.add(jsonVariables);

        jsonVariables = new JSONObject();
        jsonVariables.put("name", "dd");
        jsonVariables.put("value", userLoginDD);
        jsonVariablesArray.add(jsonVariables);

        jsonVariables = new JSONObject();
        jsonVariables.put("name", "hr");
        jsonVariables.put("value", userLoginHR);
        jsonVariablesArray.add(jsonVariables);

        jsonVariables = new JSONObject();
        jsonVariables.put("name", "description");
        jsonVariables.put("value", description);
        jsonVariablesArray.add(jsonVariables);

        mainJson.put("variables", jsonVariablesArray);
        mainJson.put("processDefinitionId", processId);
        password = WorkFlowServices.getUserPassword(request, requestorUserLogin);
        jsonResponse = WorkFlowServices.getUrlContentsPost("runtime/process-instances", mainJson.toString(), requestorUserLogin,
                password);
        System.out.println("start vacation Response : " + jsonResponse);
        try {
            if (!jsonResponse.equals("401")) {
                org.json.JSONObject jsonObj = new org.json.JSONObject(jsonResponse);
                String taskID = jsonObj.getString("id");
                LocalDate date = LocalDate.now();
                session.setAttribute("employee", requestorUserLogin);
                HashMap item = new HashMap();
                item.put("processId", processName);
                item.put("empId", requestorUserLogin);
                item.put("taskId", taskID);
                item.put("actionDate", java.sql.Date.valueOf(date));
                item.put("remark", "Send "+label+ " for approval");
                item.put("action", "0");
                WorkFlowServices.insertAuditRemark(item, request, "0");

                session.setAttribute("view", "1");

                Map<String, String> messageMap = UtilMisc.toMap("successMessage", "");
                String successMsg = UtilProperties.getMessage(resource, "ProcessStarted", messageMap, UtilHttp.getLocale(request));
                request.setAttribute("_EVENT_MESSAGE_", successMsg);
                return "success";

            } else if (jsonResponse.equals("401")) {

                String errMsg = "";
                try {
                    // only rollback the transaction if we started one...
                    TransactionUtil.rollback(beganTransaction, "Error saving data ", new Exception("error"));
                    System.out.println("Unathoriezd User");
                    Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "");
                    errMsg = UtilProperties.getMessage(resource, "ServerShutdown", messageMap, UtilHttp.getLocale(request));
                    request.setAttribute("_ERROR_MESSAGE_", errMsg);
                    return "error";
                } catch (Exception e) {
                    Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                    System.out.println(">>> " + e.getMessage());
                    request.setAttribute("_ERROR_MESSAGE_", errMsg);
                    return "error";
                }
            }

        } catch (Exception ex) {

            Logger.getLogger(VacationAndLeavesWorkFlow.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }
    
    
        public static String completeTask(HttpServletRequest request, Map<String,Object>taskData) throws IOException, SQLException {
        HttpSession session = request.getSession();
        JSONObject mainJson = new JSONObject();
        JSONArray jsonVariablesArray = new JSONArray();
          LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        String processName = (String) request.getSession().getAttribute("processName");
        String empId = request.getParameter("empId1");
        session.setAttribute("processName", processName);
        String jsonResponse = "";
        String remark = request.getParameter("remark");
        String action = request.getParameter("action");
        String currnetDate = request.getParameter("currnetDate");
        String processInstanceId = request.getParameter("processInstanceId");
        JSONObject jsonVariables = new JSONObject();
        jsonVariables.put("name", "action");
        jsonVariables.put("value", action);
        jsonVariablesArray.add(jsonVariables);
        jsonVariables = new JSONObject();
        jsonVariables.put("name", "remark");
        jsonVariables.put("value", remark);
        jsonVariablesArray.add(jsonVariables);
        mainJson.put("variables", jsonVariablesArray);
        mainJson.put("action", "complete");

        userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        id = (String) userLogin.getString("userLoginId");
        password = (String) userLogin.getString("currentPassword");
        jsonResponse = WorkFlowServices.getUrlContentsPost("runtime/tasks/" + request.getParameter("taskId2"),
                mainJson.toString(), id, password);

        if (!jsonResponse.equals("401")) {
            request.getSession().setAttribute("vars", null);

            try {

                java.sql.Date sqlStartDate = PayrollServices.convertFromStringToDate(currnetDate);

                HashMap item = new HashMap();
                item.put("processId", processName);
                item.put("empId", empId);
                item.put("taskId", request.getParameter("taskId2"));
                item.put("actionDate", sqlStartDate);
                item.put("remark", remark);
                item.put("action", action);
                WorkFlowServices.insertAuditRemark(item, request, processInstanceId);

            }  catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            String finalTask = (String) request.getParameter("finalTask");
            Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Your response send successfully");
            String successMsg = UtilProperties.getMessage(resource, "Your response send successfully", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_EVENT_MESSAGE_", successMsg);
            return "success";
        } else {
            System.out.println("Unauthrized user");
            Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "Your password and confirm password doesn't match");
            String errMsg = UtilProperties.getMessage(resource, "Your response dosen't send", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }

    }

}
