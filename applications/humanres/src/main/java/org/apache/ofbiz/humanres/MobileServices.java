/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.humanres;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.Calendar;
import java.util.Locale;
import javax.servlet.http.HttpSession;
import org.apache.ofbiz.accounting.workFlow.AccountingWorkflow;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.GeneralException;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.common.activitiServices.WorkFlowServices;
import static org.apache.ofbiz.content.data.DataEvents.module;
import org.apache.ofbiz.content.data.DataResourceWorker;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.util.EntityUtilProperties;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.common.activitiServices.Task;
import org.apache.ofbiz.common.activitiServices.Var;
import org.joda.time.LocalDate;

public class MobileServices {

    static String id = "";
    static String password = "";
    public static String jsonResponse = "";
    static GenericValue userLogin = null;

    //ContentID for image logo
    public static String ContentID = "";

    //contact mech id's
    public static String ContactMech_PostalAddress = "";
    public static String ContactMech_TelecomNumber = "";
    public static String ContactMech_EmailAddress = "";

    //Convert to Time Stamp
    public static Timestamp StringDateToTimestamp(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // date format
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));

        Date parsedDate = sdf.parse(date);

        Timestamp timestamp = new Timestamp(parsedDate.getTime());
        System.out.println(">>>>>>>" + timestamp);
        return timestamp;
    }
//    Payment Services 

    public static String getTaskDetails(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        PrintWriter out = null;

        String fullName = "";
        try {
            String partyId = request.getParameter("partyId");
            password = WorkFlowServices.getUserPassword(request, partyId);

            String jsonResponse = WorkFlowServices.getUrlContentsGet("runtime/tasks?size=50&assignee=" + partyId,
                    partyId,
                    password);//?assignee=moatasim
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(jsonResponse);
            String prettyJsonString = gson.toJson(je);

            com.google.gson.JsonObject obj = je.getAsJsonObject();
            com.google.gson.JsonArray arr = (com.google.gson.JsonArray) obj.get("data");
            Task[] task = gson.fromJson(arr, Task[].class);
//        Task item = new Task();
            JSONObject jsonRes;
            JSONArray jsonItems = new JSONArray();

            for (Task task1 : task) {

                String line_split[] = task1.getProcessDefinitionId().split(":");
                String createDate[] = task1.getCreateTime().split("T");

                jsonRes = new JSONObject();
                jsonRes.put("type", line_split[0]);
                fullName = WorkFlowServices.getPartyNameMiddle(request, task1.getDescription());
                jsonRes.put("fullName", fullName);
                jsonRes.put("createTime", createDate[0]);
                jsonRes.put("requestId", task1.getProcessInstanceId());
                jsonRes.put("taskId", task1.getId());
                jsonRes.put("taskName", task1.getTaskDefinitionKey());
                jsonRes.put("description", WorkFlowServices.getDescription(task1.getProcessInstanceId(), request));

                jsonItems.add(jsonRes);
            }
             System.out.println(jsonItems);
                response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonItems.toString());
            out.flush();

           
      

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";

    }

    public static String getAmountDue(HttpServletRequest request, String partyId) throws IOException, SQLException {

        String total = "";
        try {

            ResultSet resultSet = null;
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            String sql = "Select Sum(Inv_Itm.AMOUNT * Inv_Itm.QUANTITY) Total\n"
                    + "From INVOICE Inv,INVOICE_ITEM Inv_Itm\n"
                    + "Where Inv.INVOICE_ID = Inv_Itm.INVOICE_ID\n"
                    + "And INV.PARTY_ID_FROM = '" + partyId + "'\n"
                    + "And  INV.STATUS_ID <> 'INVOICE_PAID' And nvl(INV.DUE_DATE,sysdate) <= sysdate ";
            System.out.println("Sql  " + sql);

            resultSet = sqlProcessor.executeQuery(sql);
            while (resultSet.next()) {

                total = resultSet.getString("Total");
                break;
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public static String getAmountApplied(HttpServletRequest request, String paymentId) throws SQLException {

        System.out.println("payment " + paymentId);
        ResultSet resultSet = null;
        String amountApplied = "";
        try {

            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            String sql = "SELECT SUM(PA.AMOUNT_APPLIED) AmountApplied\n"
                    + "FROM PAYMENT_APPLICATION PA,INVOICE INV\n"
                    + "WHERE PA.PAYMENT_ID='" + paymentId + "' AND PA.INVOICE_ID = INV.INVOICE_ID";

            System.out.println("Sql  " + sql);

            resultSet = sqlProcessor.executeQuery(sql);
            while (resultSet.next()) {
                amountApplied = resultSet.getString("AMOUNTAPPLIED");
                break;

            }
            System.out.println(amountApplied);

        } catch (Exception ex) {
            Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            resultSet.close();
        }
        return amountApplied;
    }

    public static String completeTaskMob(HttpServletRequest request, HttpServletResponse response) throws IOException, GenericEntityException, SQLException {
        HttpSession session = request.getSession();
          JSONObject mainJson = new JSONObject();
       JSONArray jsonVariablesArray = new JSONArray();
        int res = 0;
        String responseStatus = "";
        String paymentId = request.getParameter("paymentId");
        String partyId = request.getParameter("partyId");
        String processName = request.getParameter("processName");
        String empId = request.getParameter("userLoginId");
        String payment = WorkFlowServices.getProcessName(request, "Payment", 0);
        String remark = request.getParameter("remark");
        String action = request.getParameter("action");
        String currnetDate = request.getParameter("currnetDate");
        String processInstanceId = request.getParameter("requestId");
        JSONObject jsonVariables =new JSONObject();
        jsonVariables.put("name","action");
        jsonVariables.put("value",action);
        jsonVariablesArray.add(jsonVariables);
        jsonVariables = new JSONObject();
        jsonVariables.put("name","remark");
        jsonVariables.put("value",remark);
        jsonVariablesArray.add(jsonVariables);
        mainJson.put("variables",jsonVariablesArray);
        mainJson.put("action","complete");
        String jsonResponse = "";
        String Curpassword = WorkFlowServices.getUserPassword(request, empId);
        try {
            jsonResponse = WorkFlowServices.getUrlContentsPost("runtime/tasks/" + request.getParameter("taskId"),
                    mainJson.toString(), empId,
                    Curpassword);
            System.out.println("jsonResponse " + jsonResponse);
            if (!jsonResponse.equals("401")) {
                request.getSession().setAttribute("vars", null);
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                java.util.Date date = sdf1.parse(currnetDate);
                java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
                HashMap item = new HashMap();
                item.put("processId", processName);
                item.put("empId", empId);
                item.put("taskId", request.getParameter("taskId"));
                item.put("actionDate", sqlStartDate);
                item.put("remark", remark);
                item.put("action", action);
                WorkFlowServices.insertAuditRemark(item, request, processInstanceId);
                String finalTask = (String) request.getParameter("finalTask");
                if (finalTask.equalsIgnoreCase("1")) {
                    if (action.equals("1")) {
                        if (payment.equals("Payment")) {
                            res = 1;
                            Map<String, String> criteria = new HashMap<String, String>();
                            criteria.put("paymentId", paymentId);
                            Delegator delegator = (Delegator) request.getAttribute("delegator");

                            try {
                                List<GenericValue> list = delegator.findList("PaymentApplication", EntityCondition.makeCondition(criteria), null, null, null, true);
                                for (int i = 0; i < list.size(); i++) {
                                    GenericValue gvValue = (GenericValue) list.get(i).clone();
                                    gvValue.set("isApproved", "1");
                                    gvValue.store();
                                }

                            } catch (Exception ex) {
                                Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
// code will be write here when we need

                    }

        }

        responseStatus = "{\"code\":\"1\",\"response\":\"success\"}";

        response.getOutputStream().print(responseStatus);
        response.getOutputStream().flush();
    }

        } catch (ParseException ex) {
            Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return "";
    }

    public static String getTotal(HttpServletRequest request, String partyId) throws SQLException, IOException {

        String total = "";
        try {

            ResultSet resultSet = null;
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            String sql = "Select Sum(Inv_Itm.AMOUNT * Inv_Itm.QUANTITY) Total\n"
                    + "From INVOICE Inv,INVOICE_ITEM Inv_Itm\n"
                    + "Where Inv.INVOICE_ID = Inv_Itm.INVOICE_ID\n"
                    + "And INV.PARTY_ID_FROM = '" + partyId + "'\n"
                    + "And  INV.STATUS_ID <> 'INVOICE_PAID'";
            System.out.println("Sql  " + sql);

            resultSet = sqlProcessor.executeQuery(sql);
            while (resultSet.next()) {

                total = resultSet.getString("Total");
                break;
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public static String getTaskData(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, SQLException {
        JSONObject jsonRes;
        JSONObject mainJson = new JSONObject();
        JSONArray jsonItems = new JSONArray();
        JSONObject jsonRemark;
        JSONArray jsonArrayRemark = new JSONArray();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String requestId = "";
        String userLoginId = request.getParameter("userLoginId");
        String Curpassword = WorkFlowServices.getUserPassword(request, userLoginId);
        requestId = request.getParameter("requestId");
        try {
            System.out.println("Mobile Service " + requestId);
            System.out.println("User Login " + userLoginId);
            String jsonResponse = WorkFlowServices.getUrlContentsGet("runtime/process-instances/" + requestId + "/variables", userLoginId,
                    Curpassword);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(jsonResponse);
            String prettyJsonString = gson.toJson(je);
            System.out.println(prettyJsonString);
            com.google.gson.JsonArray arr = je.getAsJsonArray();
            Var[] process = gson.fromJson(arr, Var[].class);
            HashMap items = new HashMap();
            for (Var process1 : process) {
                items.put(process1.getName(), process1.getValue());
            }
            LocalDate date = LocalDate.now();
            String partyId = (String) items.get("partyId");
            String paymentId = (String) items.get("paymentId");
            String fullName = WorkFlowServices.getUserLoginFullName(request, partyId);
            String totalAmount = getTotal(request, partyId);
            String amountDue = getAmountDue(request, partyId);
            String paymentAmount = getAmountApplied(request, paymentId);
            System.out.println("PAYMENT ID" + items.get("paymentId"));
            jsonRes = new JSONObject();
//              jsonRes.put("type",line_split[0] );
            jsonRes.put("fromParty", partyId);
            jsonRes.put("paymentId", paymentId);
            jsonRes.put("partyFullName", fullName);
            jsonRes.put("totalAmount", totalAmount);
            jsonRes.put("amountDue", amountDue);
            jsonRes.put("paymentAmount", paymentAmount);
            jsonRes.put("dueDate", date.toString());

            jsonItems.add(jsonRes);

            Map<String, String> criteria = new HashMap<String, String>();
            List<GenericValue> itemsRemark = null;
            criteria.put("instanceProcess", requestId);

            try {
                String action = "";
                itemsRemark = delegator.findList("ActivitiProcess", EntityCondition.makeCondition(criteria), null, null, null, true);
                for (int i = 0; i < itemsRemark.size(); i++) {
                    if (itemsRemark.get(i).get("action").equals("0")) {
                        action = "Start";

                    } else if (itemsRemark.get(i).get("action").equals("1")) {
                        action = "Approved";

                    } else if (itemsRemark.get(i).get("action").equals("2")) {
                        action = "Rejected";

                    }
                    jsonRemark = new JSONObject();
                    jsonRemark.put("empId", itemsRemark.get(i).get("empId"));
                    jsonRemark.put("actionDate", itemsRemark.get(i).get("actionDate").toString());
//                jsonRemark.put("instanceProcess", itemsRemark.get(i).get("instanceProcess"));
                    jsonRemark.put("action", action);
                    jsonRemark.put("remark", itemsRemark.get(i).get("remark"));
                    jsonArrayRemark.add(jsonRemark);
                }
            } catch (GenericEntityException ex) {
                Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
            }

            mainJson.put("data", jsonItems);
            mainJson.put("Remarks", jsonArrayRemark);
            ServletOutputStream pw = response.getOutputStream();
            System.out.println(mainJson);

//    
            pw.println(mainJson.toString());
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);

        }
        return "";

    }

    public static String getPartyIdOnUserLogin(HttpServletRequest request, HttpServletResponse response) {

        try {
            String userlogin = request.getParameter("userLoginId");
            ServletOutputStream pw = response.getOutputStream();
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("userLoginId", userlogin);
            JSONObject jsonRes;
            JSONArray jsonLeaves = new JSONArray();
            //EntityCondition.makeCondition(criteria);
            try {
                List<GenericValue> list = delegator.findList("UserLogin", EntityCondition.makeCondition(criteria), null, null, null, true);
                for (int i = 0; i < list.size(); i++) {
                    jsonRes = new JSONObject();
                    jsonRes.put("partyId", list.get(i).get("partyId"));
                    jsonLeaves.add(jsonRes);

                    System.out.println(">>partyId>> " + list.get(i).get("partyId"));

                }
                pw.println(jsonLeaves.toString());
                pw.flush();
            } catch (Exception ex) {
                System.out.println("Error getPartyIdOnUserLogin get:" + ex.getMessage());
            }

        } catch (Exception ex) {
            System.out.println("Error getPartyIdOnUserLogin Request :" + ex.getMessage());
        }
        return "";

    }

    //List Of leaves for each employee
    public static String getLeaves(HttpServletRequest request, HttpServletResponse response) {

        try {
            String partyId = request.getParameter("partyId");
            ServletOutputStream pw = response.getOutputStream();
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("partyId", partyId);
            JSONObject jsonRes;
            JSONArray jsonLeaves = new JSONArray();
            //EntityCondition.makeCondition(criteria);
            try {
                List<GenericValue> list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
//            List<GenericValue> list = delegator.findAll("EmplLeave", true);
                for (int i = 0; i < list.size(); i++) {
                    jsonRes = new JSONObject();
                    jsonRes.put("partyId", list.get(i).get("partyId"));
                    jsonRes.put("leaveTypeId", list.get(i).get("leaveTypeId"));
                    jsonRes.put("emplLeaveReasonTypeId", list.get(i).get("emplLeaveReasonTypeId"));
                    jsonRes.put("fromDate", list.get(i).get("fromDate").toString());
                    jsonRes.put("thruDate", list.get(i).get("thruDate").toString());
                    jsonRes.put("approverPartyId", list.get(i).get("approverPartyId"));
                    jsonRes.put("description", list.get(i).get("description"));
                    jsonRes.put("type", list.get(i).get("type"));
                    jsonRes.put("Id", list.get(i).get("Id"));

                    if (list.get(i).get("leaveStatus").equals("LEAVE_CREATED")) {
                        jsonRes.put("leaveStatus", "Created");
                    } else if (list.get(i).get("leaveStatus").equals("LEAVE_APPROVED")) {
                        jsonRes.put("leaveStatus", "Approved");
                    } else {
                        jsonRes.put("leaveStatus", "Rejected");
                    }

                    jsonLeaves.add(jsonRes);

                }
                pw.println(jsonLeaves.toString());
                pw.flush();
            } catch (Exception ex) {
                System.out.println("Error leaves get:" + ex.getMessage());
            }

        } catch (Exception ex) {
            System.out.println("Error leaves Request :" + ex.getMessage());
        }
        return "";

    }

    //Create new leave
    public static String createLeaves(HttpServletRequest request, HttpServletResponse response) throws ParseException {

        String description = request.getParameter("description");
        String emplLeaveReasonTypeId = request.getParameter("emplLeaveReasonTypeId");
        String leaveTypeId = request.getParameter("leaveTypeId");
        String partyId = request.getParameter("partyId");
        String fromDate = request.getParameter("fromDate");
        String thruDate = request.getParameter("thruDate");
        String leaveStatus = request.getParameter("leaveStatus");
        String approverPartyId = request.getParameter("approverPartyId");
        String Type = request.getParameter("type");
        fromDate = fromDate.replace("T", " ");
        fromDate = fromDate.replace("Z", " ");
        thruDate = thruDate.replace("T", " ");
        thruDate = thruDate.replace("Z", " ");
//   
        //SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("after convert to time stamp" + fromDate);
        Timestamp st = StringDateToTimestamp(fromDate);
        Timestamp stThrowdate = StringDateToTimestamp(thruDate);
        System.out.println("before convert to time stamp" + st);
//        try {
//            Date date = newDate.parse(fromDate);
//            Date dateThrow =newDate.parse(thruDate);
//           
//            System.out.println("Time "+ st);
//        } catch (ParseException ex) {
//            Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);
//        }

        //String leaveStatus = request.getParameter("leaveStatus");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        System.out.println(fromDate + " -----" + description + " ------ " + leaveTypeId + "------" + partyId + " " + emplLeaveReasonTypeId + "--" + approverPartyId);
        String jsonResponse = "";
        try {
            GenericValue gvValue = delegator.makeValue("EmplLeave");
            gvValue.set("partyId", partyId);
            gvValue.set("leaveTypeId", leaveTypeId);
            gvValue.set("emplLeaveReasonTypeId", emplLeaveReasonTypeId);
            gvValue.set("description", description);
            gvValue.set("leaveStatus", leaveStatus);

            gvValue.set("fromDate", st);
            gvValue.set("thruDate", stThrowdate);
            gvValue.set("approverPartyId", approverPartyId);
            gvValue.set("type", Type);
            gvValue.set("Id", delegator.getNextSeqId("seqleave8"));

            //gvValue.set("leaveStatus", leaveStatus);
            try {
                gvValue.create();

                jsonResponse = "{\"code\":\"1\",\"response\":\"success\"}";
            } catch (GenericEntityException ex) {
                Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.getOutputStream().print(jsonResponse);
            response.getOutputStream().flush();

        } catch (IOException ex) {
            Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    // list task(leaves only) for each approver or employee
    public static String mytask(HttpServletRequest request, HttpServletResponse response) {

        try {
            String partyId = request.getParameter("partyId");
            ServletOutputStream pw = response.getOutputStream();
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("approverPartyId", partyId);
            criteria.put("leaveStatus", "LEAVE_CREATED");
            JSONObject jsonRes;
            JSONArray jsonLeaves = new JSONArray();
            //EntityCondition.makeCondition(criteria);
            try {
                List<GenericValue> list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
//            List<GenericValue> list = delegator.findAll("EmplLeave", true);
                for (int i = 0; i < list.size(); i++) {
                    jsonRes = new JSONObject();
                    jsonRes.put("partyId", list.get(i).get("partyId"));
                    jsonRes.put("leaveTypeId", list.get(i).get("leaveTypeId"));
                    jsonRes.put("emplLeaveReasonTypeId", list.get(i).get("emplLeaveReasonTypeId"));
                    jsonRes.put("fromDate", list.get(i).get("fromDate").toString());
                    jsonRes.put("thruDate", list.get(i).get("thruDate").toString());
                    jsonRes.put("approverPartyId", list.get(i).get("approverPartyId"));
                    jsonRes.put("description", list.get(i).get("description"));
                    jsonRes.put("type", list.get(i).get("type"));
                    jsonRes.put("Id", list.get(i).get("Id"));
                    if (list.get(i).get("leaveStatus").equals("LEAVE_CREATED")) {
                        jsonRes.put("leaveStatus", "Created");
                    } else if (list.get(i).get("leaveStatus").equals("LEAVE_APPROVED")) {
                        jsonRes.put("leaveStatus", "Approved");
                    } else {
                        jsonRes.put("leaveStatus", "Rejected");
                    }
                    jsonLeaves.add(jsonRes);

                }
                pw.println(jsonLeaves.toString());
                pw.flush();
            } catch (Exception ex) {
                System.out.println("Error leaves get:" + ex.getMessage());
            }

        } catch (Exception ex) {
            System.out.println("Error leaves Request :" + ex.getMessage());
        }
        return "";

    }

    // .....................................................................................................................
    //get personal information for Profile
    public static String getInfo(HttpServletRequest request, HttpServletResponse response) {
        try {
            getContentId(request, response);

            String partyId = request.getParameter("partyId");
            ServletOutputStream pw = response.getOutputStream();
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("partyId", partyId);
            JSONObject jsonRes;
            JSONArray jsonLeaves = new JSONArray();
            try {
                List<GenericValue> list = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
//              
                for (int i = 0; i < list.size(); i++) {
                    jsonRes = new JSONObject();
                    jsonRes.put("partyId", list.get(i).get("partyId"));
                    jsonRes.put("contentId", ContentID);
                    jsonRes.put("firstName", list.get(i).get("firstName"));
                    jsonRes.put("middleName", list.get(i).get("middleName"));
                    jsonRes.put("lastName", list.get(i).get("lastName"));
                    if (list.get(i).get("gender").equals("M")) {
                        jsonRes.put("gender", "Male");
                    } else {
                        jsonRes.put("gender", "Female");

                    }
                    if (list.get(i).get("birthDate").equals("null")) {
                        jsonRes.put("birthDate", list.get(i).get("birthDate"));

                    } else {
                        jsonRes.put("birthDate", list.get(i).get("birthDate").toString());

                    }
//                    jsonRes.put("birthDate", list.get(i).get("birthDate").toString());

                    jsonLeaves.add(jsonRes);
                }
                pw.println(jsonLeaves.toString());
                pw.flush();
            } catch (Exception ex) {
                System.out.println("Error info get:" + ex.getStackTrace());
            }
        } catch (Exception ex) {
            System.out.println("Error leaves Request :" + ex.getStackTrace());
        }
        return "";
    }

    //get PostalAddres information for Profile
    public static String getInfo2(HttpServletRequest request, HttpServletResponse response) {
        try {
            getContactMech(request, response);
            String partyId = request.getParameter("partyId");
            ServletOutputStream pw = response.getOutputStream();
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("contactMechId", ContactMech_PostalAddress);

            JSONObject jsonRes;
            JSONArray jsonLeaves = new JSONArray();
            try {
                List<GenericValue> list = delegator.findList("PostalAddress", EntityCondition.makeCondition(criteria), null, null, null, true);
                for (int i = 0; i < list.size(); i++) {
                    jsonRes = new JSONObject();
                    jsonRes.put("contactMechId", list.get(i).get("contactMechId"));
                    jsonRes.put("address1", list.get(i).get("address1"));
                    jsonRes.put("address2", list.get(i).get("address2"));
                    jsonRes.put("city", list.get(i).get("city"));
                    jsonRes.put("houseNumber", list.get(i).get("houseNumber"));

                    jsonLeaves.add(jsonRes);
                }
                pw.println(jsonLeaves.toString());
                pw.flush();
            } catch (Exception ex) {
                System.out.println("Error info get:" + ex.getMessage());
            }
        } catch (Exception ex) {
            System.out.println("Error leaves Request :" + ex.getMessage());
        }
        return "";
    }

    //get TelecomNumber  information for Profile
    public static String getInfo3(HttpServletRequest request, HttpServletResponse response) {
        try {
            String partyId = request.getParameter("partyId");
            ServletOutputStream pw = response.getOutputStream();
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("contactMechId", ContactMech_TelecomNumber);

            JSONObject jsonRes;
            JSONArray jsonLeaves = new JSONArray();
            try {
                List<GenericValue> list = delegator.findList("TelecomNumber", EntityCondition.makeCondition(criteria), null, null, null, true);

                jsonRes = new JSONObject();
                jsonRes.put("contactNumber", list.get(0).get("contactNumber"));
                jsonLeaves.add(jsonRes);

                pw.println(jsonLeaves.toString());
                pw.flush();
            } catch (Exception ex) {
                System.out.println("Error info get:" + ex.getMessage());
            }
        } catch (Exception ex) {
            System.out.println("Error leaves Request :" + ex.getMessage());
        }
        return "";
    }

    //get EmailAddress  information for Profile
    public static String getInfo4(HttpServletRequest request, HttpServletResponse response) {
        try {
            String partyId = request.getParameter("partyId");
            ServletOutputStream pw = response.getOutputStream();
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("contactMechId", ContactMech_EmailAddress);

            JSONObject jsonRes;
            JSONArray jsonLeaves = new JSONArray();
            try {
                List<GenericValue> list = delegator.findList("ContactMech", EntityCondition.makeCondition(criteria), null, null, null, true);
                for (int i = 0; i < list.size(); i++) {
                    jsonRes = new JSONObject();
                    jsonRes.put("emailaddress", list.get(i).get("infoString"));
                    jsonLeaves.add(jsonRes);
                }
                pw.println(jsonLeaves.toString());
                pw.flush();
            } catch (Exception ex) {
                System.out.println("Error info get:" + ex.getMessage());
            }
        } catch (Exception ex) {
            System.out.println("Error leaves Request :" + ex.getMessage());
        }
        return "";
    }

    //get Party Content Id to get image logo for Profile
    public static String getContentId(HttpServletRequest request, HttpServletResponse response) {
        try {
            String partyId = request.getParameter("partyId");
            ServletOutputStream pw = response.getOutputStream();
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("partyId", partyId);

//            String ContentID = null;
            try {
                List<GenericValue> list = delegator.findList("PartyContent", EntityCondition.makeCondition(criteria), null, null, null, true);
                for (int i = 0; i < list.size(); i++) {

                    ContentID = list.get(i).get("contentId").toString();
                }
                System.out.println("--->" + ContentID);
            } catch (Exception ex) {
                System.out.println("Error info get:" + ex.getMessage());
            }
        } catch (Exception ex) {
            System.out.println("Error leaves Request :" + ex.getMessage());
        }
        return "";
    }

    //service to load personal image depend on Content Id
    public static String serveObjectData(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        HttpSession session = request.getSession();
        Locale locale = UtilHttp.getLocale(request);

        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        String userAgent = request.getHeader("User-Agent");

        Map<String, Object> httpParams = UtilHttp.getParameterMap(request);
        String contentId = (String) httpParams.get("contentId");
        if (UtilValidate.isEmpty(contentId)) {
            String errorMsg = "Required parameter contentId not found!";
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }

        // get the permission service required for streaming data; default is always the genericContentPermission
        String permissionService = EntityUtilProperties.getPropertyValue("content", "stream.permission.service", "genericContentPermission", delegator);

        // get the content record
        GenericValue content;
        try {
            content = EntityQuery.use(delegator).from("Content").where("contentId", contentId).queryOne();
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        }

        // make sure content exists
        if (content == null) {
            String errorMsg = "No content found for Content ID: " + contentId;
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }

        // make sure there is a DataResource for this content
        String dataResourceId = content.getString("dataResourceId");
        if (UtilValidate.isEmpty(dataResourceId)) {
            String errorMsg = "No Data Resource found for Content ID: " + contentId;
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }

        // get the data resource
        GenericValue dataResource;
        try {
            dataResource = EntityQuery.use(delegator).from("DataResource").where("dataResourceId", dataResourceId).queryOne();
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        }

        // make sure the data resource exists
        if (dataResource == null) {
            String errorMsg = "No Data Resource found for ID: " + dataResourceId;
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }

        // see if data resource is public or not
        String isPublic = dataResource.getString("isPublic");
        if (UtilValidate.isEmpty(isPublic)) {
            isPublic = "N";
        }

        // not public check security
        if (!"Y".equalsIgnoreCase(isPublic)) {
            // do security check
            Map<String, ? extends Object> permSvcCtx = UtilMisc.toMap("userLogin", userLogin, "locale", locale, "mainAction", "VIEW", "contentId", contentId);
            Map<String, Object> permSvcResp;
            try {
                permSvcResp = dispatcher.runSync(permissionService, permSvcCtx);
            } catch (GenericServiceException e) {
                Debug.logError(e, module);
                request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
                return "error";
            }
            if (ServiceUtil.isError(permSvcResp)) {
                String errorMsg = ServiceUtil.getErrorMessage(permSvcResp);
                Debug.logError(errorMsg, module);
                request.setAttribute("_ERROR_MESSAGE_", errorMsg);
                return "error";
            }

            // no service errors; now check the actual response
            Boolean hasPermission = (Boolean) permSvcResp.get("hasPermission");
            if (!hasPermission.booleanValue()) {
                String errorMsg = (String) permSvcResp.get("failMessage");
                Debug.logError(errorMsg, module);
                request.setAttribute("_ERROR_MESSAGE_", errorMsg);
                return "error";
            }
        }

        // get objects needed for data processing
        String contextRoot = (String) request.getAttribute("_CONTEXT_ROOT_");
        String webSiteId = (String) session.getAttribute("webSiteId");
        String dataName = dataResource.getString("dataResourceName");

        // get the mime type
        String mimeType = "image/jpeg";

        // hack for IE and mime types
        if (userAgent.indexOf("MSIE") > -1) {
            Debug.logInfo("Found MSIE changing mime type from - " + mimeType, module);
            mimeType = "application/octet-stream";
        }

        // for local resources; use HTTPS if we are requested via HTTPS
        String https = "false";
        String protocol = request.getProtocol();
        if ("https".equalsIgnoreCase(protocol)) {
            https = "true";
        }

        // get the data resource stream and content length
        Map<String, Object> resourceData;
        try {
            resourceData = DataResourceWorker.getDataResourceStream(dataResource, https, webSiteId, locale, contextRoot, false);
        } catch (IOException e) {
            Debug.logError(e, "Error getting DataResource stream", module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        } catch (GeneralException e) {
            Debug.logError(e, "Error getting DataResource stream", module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        }

        // get the stream data
        InputStream stream = null;
        Long length = null;

        if (resourceData != null) {
            stream = (InputStream) resourceData.get("stream");
            length = (Long) resourceData.get("length");
        }
        Debug.logInfo("Got resource data stream: " + length + " bytes", module);

        // stream the content to the browser
        if (stream != null && length != null) {
            try {
                UtilHttp.streamContentToBrowser(response, stream, length.intValue(), mimeType, null);
            } catch (IOException e) {
                Debug.logError(e, "Unable to write content to browser", module);
                request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
                // this must be handled with a special error string because the output stream has been already used and we will not be able to return the error page;
                // the "io-error" should be associated to a response of type "none"
                return "io-error";
            }
        } else {
            String errorMsg = "No data is available.";
            Debug.logError(errorMsg, module);
            request.setAttribute("_ERROR_MESSAGE_", errorMsg);
            return "error";
        }

        return "success";
    }
//............................................................................................................................

    // To delete leaves
    public static String delete(HttpServletRequest request, HttpServletResponse response) throws ParseException {

        String description = request.getParameter("description");
        String emplLeaveReasonTypeId = request.getParameter("emplLeaveReasonTypeId");
        String leaveTypeId = request.getParameter("leaveTypeId");
        String partyId = request.getParameter("partyId");
        String fromDate = request.getParameter("fromDate");
        String thruDate = request.getParameter("thruDate");
        String leaveStatus = request.getParameter("leaveStatus");
        String approverPartyId = request.getParameter("approverPartyId");
        String Id = request.getParameter("Id");
        fromDate = fromDate.replace("T", " ");
        fromDate = fromDate.replace("Z", " ");
        thruDate = thruDate.replace("T", " ");
        thruDate = thruDate.replace("Z", " ");
//   
        //SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Timestamp st = StringDateToTimestamp(fromDate);
        Timestamp stThrowdate = StringDateToTimestamp(thruDate);
//        try {
//            Date date = newDate.parse(fromDate);
//            Date dateThrow =newDate.parse(thruDate);
//           
//            System.out.println("Time "+ st);
//        } catch (ParseException ex) {
//            Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);
//        }

        //String leaveStatus = request.getParameter("leaveStatus");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
//        System.out.println(fromDate + " -----" + description + " ------ " + leaveTypeId + "------" + partyId + " " +);
        String jsonResponse = "";

        Calendar cal = Calendar.getInstance();
        cal.setTime(st);

        try {
            System.out.println(partyId + "--" + leaveTypeId + "--" + emplLeaveReasonTypeId + "--" + description + "" + new SimpleDateFormat("dd-MMM-YY").format(st) + "--" + approverPartyId + "--" + leaveStatus + "-");

            List<EntityCondition> conditionList = new ArrayList<EntityCondition>();
            conditionList.add(EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId));
            conditionList.add(EntityCondition.makeCondition("Id", EntityOperator.EQUALS, Id));
//           conditionList.add( EntityCondition.makeCondition( new SimpleDateFormat("dd-MMM-YY").format("fromDate") , EntityOperator.EQUALS , new SimpleDateFormat("dd-MMM-YY").format(st)));
//            conditionList.add(EntityCondition.makeCondition("leaveTypeId", EntityOperator.EQUALS, leaveTypeId));

            delegator.removeByCondition("EmplLeave", EntityCondition.makeCondition(conditionList, EntityOperator.AND));

            jsonResponse = "{\"code\":\"1\",\"response\":\"success\"}";

            response.getOutputStream().print(jsonResponse);
            response.getOutputStream().flush();

        } catch (IOException ex) {
            Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GenericEntityException ex) {
            Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    //to update leave status 
    public static String update(HttpServletRequest request, HttpServletResponse response) throws ParseException {

        String description = request.getParameter("description");
        String emplLeaveReasonTypeId = request.getParameter("emplLeaveReasonTypeId");
        String leaveTypeId = request.getParameter("leaveTypeId");
        String partyId = request.getParameter("partyId");
        String fromDate = request.getParameter("fromDate");
        String thruDate = request.getParameter("thruDate");
        String leaveStatus = request.getParameter("leaveStatus");
        String approverPartyId = request.getParameter("approverPartyId");
        String Id = request.getParameter("Id");
        fromDate = fromDate.replace("T", " ");
        fromDate = fromDate.replace("Z", " ");
        thruDate = thruDate.replace("T", " ");
        thruDate = thruDate.replace("Z", " ");
//   
        //SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Timestamp st = StringDateToTimestamp(fromDate);
        Timestamp stThrowdate = StringDateToTimestamp(thruDate);
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("partyId", partyId);
//        criteria.put("leaveTypeId", leaveTypeId);
        criteria.put("Id", Id);
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String jsonResponse = "";
        try {
            List<GenericValue> list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
            GenericValue gvValue = (GenericValue) list.get(0).clone();
            gvValue.set("leaveStatus", leaveStatus);
//            gvValue.set("approverPartyId", approverPartyId);

            gvValue.store();
//            list.get(0).replace("leaveStatus", leaveStatus);
            jsonResponse = "{\"code\":\"1\",\"response\":\"success\"}";
            response.getOutputStream().print(jsonResponse);
            response.getOutputStream().flush();

        } catch (IOException ex) {
            Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GenericEntityException ex) {
            Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    //to update Approver Id (re Assign)
    public static String updateApproverId(HttpServletRequest request, HttpServletResponse response) throws ParseException {

        String partyId = request.getParameter("partyId");
        String approverPartyId = request.getParameter("approverPartyId");
        String Id = request.getParameter("Id");

        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("partyId", partyId);
        criteria.put("Id", Id);
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String jsonResponse = "";
        try {
            List<GenericValue> list = delegator.findList("EmplLeave", EntityCondition.makeCondition(criteria), null, null, null, true);
            GenericValue gvValue = (GenericValue) list.get(0).clone();
            gvValue.set("approverPartyId", approverPartyId);

            gvValue.store();

        } catch (GenericEntityException ex) {
            Logger.getLogger(MobileServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    //get Contact Mech Id's for PostalAddress , TelecomNumber and EmailAddress
    public static String getContactMech(HttpServletRequest request, HttpServletResponse response) {
        try {
            String partyId = request.getParameter("partyId");
            ServletOutputStream pw = response.getOutputStream();
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("partyId", partyId);

//            String ContentID = null;
            try {
                List<GenericValue> list = delegator.findList("PartyContactMech", EntityCondition.makeCondition(criteria), null, null, null, true);

                ContactMech_PostalAddress = list.get(0).getString("contactMechId");
                ContactMech_TelecomNumber = list.get(1).getString("contactMechId");
                ContactMech_EmailAddress = list.get(2).getString("contactMechId");
                System.out.println("--->" + ContactMech_PostalAddress);
                System.out.println("--->" + ContactMech_TelecomNumber);
                System.out.println("--->" + ContactMech_EmailAddress);

            } catch (Exception ex) {
                System.out.println("Error info get:" + ex.getMessage());
            }
        } catch (Exception ex) {
            System.out.println("Error leaves Request :" + ex.getMessage());
        }
        return "";
    }

}
