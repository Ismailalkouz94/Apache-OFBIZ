/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.common.activitiServices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.humanres.PayrollServices;
import org.apache.ofbiz.common.activitiServices.Task;
import org.apache.ofbiz.common.activitiServices.Var;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Rbab3a
 */
public class WorkFlowServices extends HttpServlet {

    public static final String resource = "AccountingUiLabels";

//10.99.157.14
//    private final static String BASE_URL = "http://localhost:8088/activiti-rest/service/";
    static String id = "";
    static String password = "";
    public static String jsonResponse = "";
    static GenericValue userLogin = null;

    public static String getBaseUrl() throws IOException {
        String hostName = "";
        String protocol = "";
        String port = "";
        String BASE_URL = "";
        try {
            File fXmlFile = new File("framework\\common\\config\\activiti\\activitiserver.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("server");
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    protocol = eElement.getElementsByTagName("protocol").item(0).getTextContent();
                    hostName = eElement.getElementsByTagName("hostname").item(0).getTextContent();
                    port = eElement.getElementsByTagName("port").item(0).getTextContent();

                    BASE_URL = protocol + "://" + hostName + ":" + port + "/activiti-rest/service/";

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BASE_URL;
    }

    public static String getUrlContentsPost(String theUrl, String requestBody, String userName, String password) throws IOException {
        theUrl = getBaseUrl() + theUrl;
        StringBuilder content = new StringBuilder();
        // many of these calls can throw exceptions, so i've just
        // wrapped them all in one try/catch statement.
        try {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("charset", "utf-8");
            String userPassword = userName + ":" + password;
            String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
            urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
            urlConnection.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())) {
                wr.write(requestBody.getBytes("UTF-8"));
                wr.flush();

                wr.close();
            }
            System.out.println("Response Message " + urlConnection.getResponseMessage());
//            if (urlConnection.getResponseMessage().equalsIgnoreCase("Created") || urlConnection.getResponseMessage().equalsIgnoreCase("OK")) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
//            System.out.println(">>>>2222" + content);
            bufferedReader.close();

//            }
        } catch (Exception e) {
            e.printStackTrace();
            return "401";
        }

        System.out.println("From Url Method " + content.toString());
        return content.toString();
    }

    public static String getUrlContentsGet(String theUrl, String userName, String password) throws IOException {

        theUrl = getBaseUrl() + theUrl;
        StringBuilder content = new StringBuilder();

        // many of these calls can throw exceptions, so i've just
        // wrapped them all in one try/catch statement.
        try {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            String userPassword = userName + ":" + password;
            // password decryption

            String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
            urlConnection.setRequestProperty("Authorization", "Basic " + encoding);
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
//            System.out.println(">>>>111" + content);
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static String getPartyNameMiddle(HttpServletRequest request, String partyId) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        Map<String, String> criteriaUserLogin = new HashMap<String, String>();
        criteriaUserLogin.put("userLoginId", partyId);
        String id = "";
        String fullName = "";
        try {
            GenericValue gvPartyId = delegator.findOne("UserLogin", criteriaUserLogin, true);
            id = (String) gvPartyId.get("partyId");
            criteria.put("partyId", id);
            GenericValue gv1 = delegator.findOne("Person", criteria, true);
            fullName += gv1.get("firstName") + " " + gv1.get("lastName");
        } catch (GenericEntityException ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fullName;
    }

    public static String getPartyName(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        String partyId = request.getParameter("partyId");
        criteria.put("partyId", partyId);
        String fullName = "";
        PrintWriter out = null;

        try {
            GenericValue gv1 = delegator.findOne("Person", criteria, true);
            if (gv1 != null) {
                fullName += gv1.get("firstName") + " " + gv1.get("lastName");
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(fullName);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getUserLoginFullName(HttpServletRequest request, String partyId) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("partyId", partyId);
        String fullName = "";
        PrintWriter out = null;

        try {
            GenericValue gv1 = delegator.findOne("Person", criteria, true);

            fullName += gv1.get("firstName") + " " + gv1.get("middleName") + " " + gv1.get("lastName");

        } catch (GenericEntityException ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fullName;
    }

    public static String getPartyId(HttpServletRequest request) {
        userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        id = (String) userLogin.getString("userLoginId");
        return id;
    }

    public static String getProcessName(HttpServletRequest request, String name, int isStart) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
        String processName = "";
        String processType = "";
        ResultSet resultSet = null;
        String type = "";
        String sqlName = "SELECT ID_ ,KEY_ FROM ACT_RE_PROCDEF WHERE  NAME_ ='" + name + "' and \n"
                + " VERSION_ = (select MAX(VERSION_) \n"
                + "FROM ACT_RE_PROCDEF WHERE  NAME_ ='" + name + "')";

        try {
            resultSet = sqlProcessor.executeQuery(sqlName);
            while (resultSet.next()) {
                processName = resultSet.getString("ID_");
                type = resultSet.getString("KEY_");
                break;
            }
            if (isStart == 1) {
                processType = processName;
            } else if (isStart == 0) {
                processType = type;
            }

        } catch (Exception ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return processType;
    }

    public static HashMap<String, Object> getTasks(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ParseException {
        ArrayList tasks = new ArrayList();
        HashMap<String, Object> mapList = new HashMap<String, Object>();
        try {
            userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
            String fullName = "";
            id = (String) userLogin.getString("userLoginId");

            password = (String) userLogin.getString("currentPassword");
            String jsonResponse = getUrlContentsGet("runtime/tasks?size=50&assignee=" + id,
                    id,
                    password);//?assignee=moatasim

            System.out.println("Task : " + jsonResponse);
            System.out.println("userLogin : " + id);
            System.out.println("password : " + password);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(jsonResponse);
            org.json.JSONObject jsonObj;
            jsonObj = new org.json.JSONObject(jsonResponse);
            System.out.println(jsonObj.get("total"));
            String prettyJsonString = gson.toJson(je);
            com.google.gson.JsonObject obj = je.getAsJsonObject();
            com.google.gson.JsonArray arr = (com.google.gson.JsonArray) obj.get("data");
            Task[] task = gson.fromJson(arr, Task[].class);

            for (Task task1 : task) {
                System.out.println("task1 " + task1);
//            tasks.add(task1);
                String type[] = task1.getProcessDefinitionId().split(":");

                String createDate[] = task1.getCreateTime().split("T");

                HashMap item = new HashMap();
                item.put("type", type[0]);
                item.put("fromParty", task1.getDescription());
                fullName = getPartyNameMiddle(request, (String) item.get("fromParty"));
                item.put("fullName", fullName);
                item.put("createTime", createDate[0]);
                item.put("id", task1.getId());
                item.put("executionId", task1.getExecutionId());
                item.put("taskDefinitionKey", task1.getTaskDefinitionKey());
                item.put("processInstanceId", task1.getProcessInstanceId());
                System.out.println("getMaxSequence_ActivitiProcess " + getMaxSequence_ActivitiProcess(request, (String) task1.getProcessInstanceId()));
//  -----------------      New Code 18/7 -----------------------

                item.put("description", getDescription((String) item.get("processInstanceId"), request));
//End New Code -----------------------
                tasks.add(item);
            }

            System.out.println("tasks " + tasks);
            mapList.put("tasks", tasks);
            mapList.put("total", jsonObj.get("total"));

            return mapList;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return mapList;
    }

    public static String ActivitiResponse(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        HttpSession session = request.getSession();
        String taskId = request.getParameter("taskId");
        String view = request.getParameter("view");
        String processInstanceId = request.getParameter("processInstanceId");
        String taskName = request.getParameter("taskName");
//        String processInstanceIdSession =(String)session.getAttribute("processInstanceId");
        String processName = request.getParameter("processName");
        session.setAttribute("taskId", taskId);
        session.setAttribute("processInstanceId", processInstanceId);
        session.setAttribute("taskName", taskName);
        session.setAttribute("processName", processName);
        session.setAttribute("view", view);
//        System.out.println("processName  "+processName +" "+" taskId ="+taskId+" "+"processInstanceId "+processInstanceId+" "+"taskName "+taskName);

        if (processName.equalsIgnoreCase("Payment")) {
            System.out.println("Done Payment");
            response.sendRedirect("PaymentResponse");
        } else if (processName.equalsIgnoreCase("simpleApprovalProcess:8:1933")) {
            response.sendRedirect("LeaveResponse");
        } else if (processName.equalsIgnoreCase("Vacations")) {
            if (taskName.equals("requestor")) {
                response.sendRedirect("EditVacation");
            } else {
                response.sendRedirect("VacationResponse");
            }
        } else if (processName.equalsIgnoreCase("Leaves")) {
            if (taskName.equals("requestor")) {
                response.sendRedirect("EditLeave");
            } else {
                response.sendRedirect("LeavesResponse");
            }
        }else {
             response.sendRedirect("main");
        }

        return "";
    }

    public static String ApproverResponse(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        HttpSession session = request.getSession();
        String payment = getProcessName(request, "Payment", 0);
        String processInstanceId = request.getParameter("processInstanceId");
        String processName = request.getParameter("processName");
        session.setAttribute("processInstanceId", processInstanceId);
        session.setAttribute("processName", processName);
        if (processName.equalsIgnoreCase("Payment")) {
            System.out.println("Done " + payment);
            response.sendRedirect("PaymentApprover");
        } else if (processName.equalsIgnoreCase("Leaves")) {
            response.sendRedirect("LeaveCompletedDetails");
        } else if (processName.equalsIgnoreCase("Vacations")) {
            response.sendRedirect("VacationCompletedDetails");
        } else {
                response.sendRedirect("main");
        }

        return "";
    }

    public static String getDescription(String processInstanceId, HttpServletRequest request) throws SQLException, ParseException, IOException {
        ResultSet resultSet = null;
        String description = "";
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            String sql = "SELECT TEXT_\n"
                    + "FROM ACT_HI_DETAIL \n"
                    + "where NAME_ ='description' and PROC_INST_ID_ ='" + processInstanceId + "'";
            resultSet = sqlProcessor.executeQuery(sql);
            System.out.println("resultSet " + resultSet);
            if (resultSet != null) {
                while (resultSet.next()) {
                    description = resultSet.getString("TEXT_");
//                   String sqlRemark =
                }
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return description;
    }

    public static List getAuditRemarks(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        HttpSession session = request.getSession();

        String processInstanceIdSession = (String) session.getAttribute("processInstanceId");
        String processInstanceId = "";
        if (!processInstanceIdSession.equals("null")) {
            processInstanceId = (String) session.getAttribute("processInstanceId");
        } else {
            processInstanceId = request.getParameter("processInstanceId");
        }
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        List<GenericValue> items = null;
        criteria.put("instanceProcess", processInstanceId);

        try {
            items = delegator.findList("ActivitiProcess", EntityCondition.makeCondition(criteria), null, null, null, true);

        } catch (GenericEntityException ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return items;
    }

    public static String getRemarks(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        HttpSession session = request.getSession();
        PrintWriter out = null;
        session.setAttribute("page", "1");
        JSONObject jsonRes;
        JSONArray jsonRemarks = new JSONArray();
        String processInstanceId = "";

        processInstanceId = request.getParameter("processInstanceId");

        Delegator delegator = (Delegator) request.getAttribute("delegator");
//       String processInstanceId = (String)session.getAttribute("processInstanceId");

        Map<String, String> criteria = new HashMap<String, String>();
        List<GenericValue> items = null;
        criteria.put("instanceProcess", processInstanceId);
        String action = "";
        try {

            items = delegator.findList("ActivitiProcess", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).get("action") != null) {

                    if (items.get(i).get("action").equals("0")) {
                        action = "Start";

                    } else if (items.get(i).get("action").equals("1")) {
                        action = "Approved";

                    } else if (items.get(i).get("action").equals("2")) {
                        action = "Rejected";

                    } else if (items.get(i).get("action").equals("3")) {
                        action = "Return To Employee";

                    } else if (items.get(i).get("action").equals("4")) {
                        action = "Edit form";

                    }
                }
                jsonRes = new JSONObject();
                jsonRes.put("empId", items.get(i).get("empId"));
                jsonRes.put("actionDate", items.get(i).get("actionDate").toString());
                jsonRes.put("instanceProcess", items.get(i).get("instanceProcess"));
                jsonRes.put("action", action);
                jsonRes.put("remark", items.get(i).get("remark"));
                jsonRemarks.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonRemarks.toString());
            out.flush();
            out.close();
        } catch (GenericEntityException ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }
//To get Approved And Rejected Task 

    public static String getInboxStatus(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, IOException, ParseException {
        String partyId = getPartyId(request);
        String statusValue = request.getParameter("statusValue");
        PrintWriter out = null;
        JSONObject mainJson = new JSONObject();
        JSONObject jsonRes;
        JSONArray jsonStatusArray = new JSONArray();

        String startTime = "";
        String endTime = "";
        String fullName = "";
        String processType = "";

        try {

            ResultSet resultSet = null;
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sql = "Select * From (SELECT A.ID_,\n"
                    + "       A.DESCRIPTION_ UserID,\n"
                    + "       A.PROC_INST_ID_,\n"
                    + "       A.PROC_DEF_ID_\n"
                    + "       ,A.START_TIME_,\n"
                    + "       A.END_TIME_,\n"
                    + "       (Select CASE To_Char(B.TEXT_)\n"
                    + "          WHEN '1' THEN 'Approved'\n"
                    + "          WHEN '2' THEN 'Rejected'\n"
                    + "          ELSE 'Others' \n"
                    + "          END From ACT_HI_DETAIL B \n"
                    + "         Where B.PROC_INST_ID_ = A.PROC_INST_ID_ \n"
                    + "           And  B.TASK_ID_ = A.ID_ \n"
                    + "           And B.NAME_ ='action'\n"
                    + "           ) Status,\n"
                    + "       (Select B.TEXT_ \n"
                    + "          From ACT_HI_DETAIL B \n"
                    + "         Where B.PROC_INST_ID_ = A.PROC_INST_ID_ \n"
                    + "           And B.NAME_ ='description' ) Description\n"
                    + "  FROM ACT_HI_TASKINST A\n"
                    + "  Where ASSIGNEE_='" + partyId + "'\n"
                    + "  and DELETE_REASON_ ='completed')\n"
                    + "  Where Status = nvl('" + statusValue + "',Status)";
            resultSet = sqlProcessor.executeQuery(sql);
            while (resultSet.next()) {
                String processType_Split[] = resultSet.getString("PROC_DEF_ID_").split(":");
                processType = processType_Split[0];
                String startTime_Split[] = resultSet.getString("START_TIME_").split(" ");
                String endTime_Split[] = resultSet.getString("END_TIME_").split(" ");
                startTime = startTime_Split[0];
                endTime = endTime_Split[0];
                jsonRes = new JSONObject();
                jsonRes.put("processInstanceId", resultSet.getString("PROC_INST_ID_"));
                jsonRes.put("taskId", resultSet.getString("ID_"));
                jsonRes.put("type", processType);
                jsonRes.put("startTime", startTime.toString());
                jsonRes.put("endTime", endTime.toString());
                fullName = getPartyNameMiddle(request, resultSet.getString("UserID"));
                jsonRes.put("fullName", fullName);
                jsonRes.put("status", resultSet.getString("Status"));
                jsonRes.put("description", resultSet.getString("Description"));

                jsonStatusArray.add(jsonRes);
            }
            System.out.println(jsonStatusArray.size());

//               jsonCount.put("StatusCount",jsonStatusArray.size());
//               jsonCountArray.add(jsonCount);
            mainJson.put("StatusTask", jsonStatusArray);
            mainJson.put("count", jsonStatusArray.size());
            System.out.println("Main " + mainJson.toString());
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(mainJson.toString());
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    public static Map getApproverActionData(HttpServletRequest request, HttpServletResponse response) throws SQLException {

        HttpSession session = request.getSession();
        String processInstanceId = (String) session.getAttribute("processInstanceId");
        System.out.println("processInstanceId " + processInstanceId);
        ResultSet resultSet = null;
        Map<String, String> innerMap = new HashMap<String, String>();
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessorRemark = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            String sql = "SELECT NAME_,TEXT_\n"
                    + "FROM ACT_HI_DETAIL\n"
                    + "WHERE PROC_INST_ID_='" + processInstanceId + "'";
            System.out.println("sql :" + sql);
            resultSet = sqlProcessor.executeQuery(sql);

            while (resultSet.next()) {
                innerMap.put(resultSet.getString("NAME_"), resultSet.getString("TEXT_"));
//                   String sqlRemark =
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return innerMap;
    }

    public static List getRequestsStatus(HttpServletRequest request, String isNull) throws SQLException {
        ArrayList<  Map<String, String>> dataList = new ArrayList<  Map<String, String>>();
        String partyId = getPartyId(request);
        ResultSet resultSet = null;

        try {

            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sql = "SELECT PI.ID_,\n"
                    + "PI.START_TIME_,\n"
                    + "PI.END_TIME_ ,\n"
                    + "B.TEXT_ Description,\n"
                    + "CASE\n"
                    + "WHEN PI.END_TIME_ IS NULL\n"
                    + "THEN 'In-Process'\n"
                    + "WHEN PI.END_TIME_ IS NOT NULL\n"
                    + "THEN 'Finished'\n"
                    + " ELSE 'OTHERS'\n"
                    + "END Status ,\n"
                    + "PI.START_USER_ID_,\n"
                    + "PD.NAME_\n"
                    + "FROM ACT_HI_PROCINST PI , ACT_HI_DETAIL B ,\n"
                    + "ACT_RE_PROCDEF PD\n"
                    + "WHERE PI.PROC_DEF_ID_ = PD.ID_\n"
                    + "AND PI.PROC_INST_ID_ = B.PROC_INST_ID_ \n"
                    + "AND START_USER_ID_ ='" + partyId + "'\n"
                    + "AND PI.END_TIME_ IS " + isNull + " NULL\n"
                    + "AND  B.NAME_ ='description'\n"
                    + "ORDER BY START_TIME_ DESC";
            String startTime = "";
            String endTime = "";
            resultSet = sqlProcessor.executeQuery(sql);
            while (resultSet.next()) {
                String startTime_Split[] = resultSet.getString("START_TIME_").split(" ");
                if (resultSet.getString("END_TIME_") != null) {
                    String endTime_Split[] = resultSet.getString("END_TIME_").split(" ");
                    endTime = endTime_Split[0];
                } else {
                    endTime = "";
                }
                startTime = startTime_Split[0];

                Map<String, String> innerMap = new HashMap<String, String>();
                innerMap.put("ID", resultSet.getString("ID_"));
                innerMap.put("START_TIME", startTime);
                innerMap.put("END_TIME", endTime);
                innerMap.put("Status", resultSet.getString("Status"));
                innerMap.put("START_USER_ID", resultSet.getString("START_USER_ID_"));
                innerMap.put("NAME", resultSet.getString("NAME_"));
                innerMap.put("description", resultSet.getString("Description"));
                dataList.add(innerMap);
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataList;
    }

    public static String setInitSession(HttpServletRequest request, HttpServletResponse response) {
        try {

            HttpSession session = request.getSession();

//    session.setAttribute("processInstanceId", "null");
//    request.setAttribute("processInstanceId", "null");
            session.setAttribute("id", "null");
            session.setAttribute("view", "1");

        } catch (Exception x) {
            System.out.println(x.getStackTrace());
        }

        return "";
    }

    public static Map getTaskData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        HttpSession session = request.getSession();
        String processInstanceIdSession = (String) session.getAttribute("processInstanceId");
        String processInstanceId = "";
        if (!processInstanceIdSession.equals("null")) {
            processInstanceId = (String) session.getAttribute("processInstanceId");
        } else {
            processInstanceId = request.getParameter("processInstanceId");
        }
        id = (String) userLogin.getString("userLoginId");
        password = (String) userLogin.getString("currentPassword");
        String jsonResponse = getUrlContentsGet("runtime/process-instances/" + processInstanceId + "/variables", id,
                password);
//        System.out.println("-------" + jsonResponse);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jsonResponse);
        String prettyJsonString = gson.toJson(je);
        com.google.gson.JsonArray arr = je.getAsJsonArray();
        Var[] process = gson.fromJson(arr, Var[].class);
        HashMap items = new HashMap();
        for (Var process1 : process) {
            items.put(process1.getName(), process1.getValue());
        }
        System.out.println("items " + items);
        return items;

    }

    public static String completeTask(HttpServletRequest request, HttpServletResponse response) throws IOException, GenericEntityException, SQLException {
        HttpSession session = request.getSession();
        int res = 0;
        JSONObject mainJson = new JSONObject();
        JSONArray jsonVariablesArray = new JSONArray();
        RequestDispatcher dispatcher = null;
        String partyId = (String) request.getSession().getAttribute("partyId");
        String processName = (String) request.getSession().getAttribute("processName");
        String empId = request.getParameter("empId1");
        String payment = getProcessName(request, "Payment", 0);
        session.setAttribute("processName", processName);
        String jsonResponse = "";
        String processId = request.getParameter("processId");
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
        jsonResponse = getUrlContentsPost("runtime/tasks/" + request.getParameter("taskId2"),
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

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            String finalTask = (String) request.getParameter("finalTask");

            if (finalTask.equalsIgnoreCase("1")) {
                if (processName.equals(payment)) {
                    String paymentId = (String) request.getSession().getAttribute("paymentId");
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
                        Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
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

//    from Java service 
    @SuppressWarnings("unchecked")
    public static String completeTask(HttpServletRequest request,Map<String,String> dataItem) throws IOException, GenericEntityException, SQLException {
        JSONObject mainJson = new JSONObject();
        JSONArray jsonVariablesArray = new JSONArray();
        String jsonResponse = "";
        JSONObject jsonVariables = new JSONObject();
        jsonVariables.put("name", "action");
        jsonVariables.put("value", dataItem.get("action"));
        jsonVariablesArray.add(jsonVariables);
        jsonVariables = new JSONObject();
        jsonVariables.put("name", "remark");
        jsonVariables.put("value", dataItem.get("remark"));
        jsonVariablesArray.add(jsonVariables);
        mainJson.put("variables", jsonVariablesArray);
        mainJson.put("action", "complete");

        userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        id = (String) userLogin.getString("userLoginId");
        password = (String) userLogin.getString("currentPassword");
        jsonResponse = getUrlContentsPost("runtime/tasks/" + dataItem.get("taskId2"),
                mainJson.toString(), id, password);

        if (!jsonResponse.equals("401")) {
            request.getSession().setAttribute("vars", null);

            try {

                java.sql.Date sqlStartDate = PayrollServices.convertFromStringToDate(dataItem.get("currnetDate"));

                HashMap item = new HashMap();
                item.put("processId", dataItem.get("processName"));
                item.put("empId", dataItem.get("empId"));
                item.put("taskId", dataItem.get("taskId2"));
                item.put("actionDate", sqlStartDate);
                item.put("remark", dataItem.get("remark"));
                item.put("action", dataItem.get("action"));
                WorkFlowServices.insertAuditRemark(item, request, dataItem.get("processInstanceId"));

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return "error";
            }

        } else {
            System.out.println("Unauthrized user");
            return "error";
        }
        return "success";
    }

    public static int getCount(HttpServletRequest request, String variableName, String variableText) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
        int count = 0;

        PrintWriter out = null;
        ResultSet resultSet = null;
        String sqlName = "Select count(*) as COUNT\n"
                + "From ACT_RU_VARIABLE\n"
                + "Where NAME_ = '" + variableName + "'\n"
                + "And TEXT_ = '" + variableText + "'";

        try {
            resultSet = sqlProcessor.executeQuery(sqlName);
            while (resultSet.next()) {
                count = resultSet.getInt("COUNT");
                break;
            }
            return count;

        } catch (Exception ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;

    }

    public static String getUserPassword(HttpServletRequest request, String userLoginId) {

        ResultSet resultSetPass = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String sqlStrgetPass = "";
        String Curpassword = "";
        SQLProcessor sqlProcessor2 = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
        sqlStrgetPass = "SELECT CURRENT_PASSWORD FROM USER_LOGIN WHERE USER_LOGIN_ID='" + userLoginId + "'";

        try {
            resultSetPass = sqlProcessor2.executeQuery(sqlStrgetPass);
            while (resultSetPass.next()) {
                Curpassword = resultSetPass.getString("CURRENT_PASSWORD");
                break;
            }
        } catch (Exception ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Curpassword;
    }

    public static List getSelectOptionValue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList listOptions = new ArrayList();
        userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        String view = (String) request.getSession().getAttribute("view");
        String taskID = (String) request.getSession().getAttribute("taskId");
        String processName = (String) request.getSession().getAttribute("processName");
        id = (String) userLogin.getString("userLoginId");
        password = (String) userLogin.getString("currentPassword");
        String jsonResponse = "";
        if (view.equals("1")) {
            jsonResponse = getUrlContentsGet("form/form-data?processDefinitionId=" + processName, id, password);
        } else if (view.equals("2")) {
            jsonResponse = getUrlContentsGet("form/form-data?taskId=" + taskID, id, password);
        }
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        JSONArray formElements = null;
        try {
            obj = (JSONObject) parser.parse(jsonResponse);
            formElements = (JSONArray) obj.get("formProperties");
        } catch (org.json.simple.parser.ParseException ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < formElements.size(); i++) {
            org.json.simple.JSONObject obj2 = (org.json.simple.JSONObject) formElements.get(i);
            String fieldType = (String) obj2.get("type");
            if (fieldType.equalsIgnoreCase("enum")) {
                JSONArray opt = (JSONArray) obj2.get("enumValues");
                for (int j = 0; j < opt.size(); j++) {

                    listOptions.add(((JSONObject) opt.get(j)).get("name"));
                }
            }

        }
        return listOptions;
    }

    public static List getSelectOptionID(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList listOptions = new ArrayList();
//              String taskId = request.getParameter("taskId");
        userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        String view = (String) request.getSession().getAttribute("view");
        String taskID = (String) request.getSession().getAttribute("taskId");
        String processName = (String) request.getSession().getAttribute("processName");
        id = (String) userLogin.getString("userLoginId");
        password = (String) userLogin.getString("currentPassword");
        String jsonResponse = "";
        if (view.equals("1")) {
            jsonResponse = getUrlContentsGet("form/form-data?processDefinitionId=" + processName, id, password);
        } else if (view.equals("2")) {
            jsonResponse = getUrlContentsGet("form/form-data?taskId=" + taskID, id, password);
        }
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        JSONArray formElements = null;
        try {
            obj = (JSONObject) parser.parse(jsonResponse);
            formElements = (JSONArray) obj.get("formProperties");
        } catch (org.json.simple.parser.ParseException ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < formElements.size(); i++) {
            org.json.simple.JSONObject obj2 = (org.json.simple.JSONObject) formElements.get(i);

            String fieldType = (String) obj2.get("type");
            if (fieldType.equalsIgnoreCase("enum")) {

                String options = "";
                JSONArray opt = (JSONArray) obj2.get("enumValues");
                for (int j = 0; j < opt.size(); j++) {

//                options += "<option value='" + ((JSONObject) opt.get(j)).get("id") + "'>" + ((JSONObject) opt.get(j)).get("name") + "</option>";
                    listOptions.add(((JSONObject) opt.get(j)).get("id"));

                }
            }

        }
        return listOptions;
    }

    public static void insertAuditRemark(Map items, HttpServletRequest request, String processInstanceId) {
        ResultSet resultset = null;
        ResultSet resultSetTask = null;
        HttpSession session = request.getSession();

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        try {
            String sql = "";
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            if (processInstanceId.equals("0")) {
                sql = "select PROC_INST_ID_ from ACT_HI_VARINST where  EXECUTION_ID_ =" + items.get("taskId");

                resultset = sqlProcessor.executeQuery(sql);
                while (resultset.next()) {
                    processInstanceId = resultset.getString("PROC_INST_ID_");
                    break;
                }
            }
            session.setAttribute("processInstanceId", processInstanceId);
            String partyName = getPartyNameMiddle(request, (String) items.get("empId"));
            GenericValue gvValue = delegator.makeValue("ActivitiProcess");
            gvValue.set("id", delegator.getNextSeqId("actitviID"));
            gvValue.set("processId", items.get("processId"));
            gvValue.set("empId", partyName);
            gvValue.set("taskId", items.get("taskId"));
            gvValue.set("actionDate", items.get("actionDate"));
            gvValue.set("remark", items.get("remark"));
            gvValue.set("action", items.get("action"));
            gvValue.set("instanceProcess", processInstanceId);
            gvValue.create();

            SQLProcessor sqlProcessorTask = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            String sqlTask = "Update ACT_HI_DETAIL\n"
                    + " Set TASK_ID_ ='" + items.get("taskId") + "'"
                    + " Where PROC_INST_ID_ ='" + processInstanceId + "'\n"
                    + "And TASK_ID_ is null";
            resultSetTask = sqlProcessorTask.executeQuery(sqlTask);
            System.out.println(">>> after update" + sqlTask);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet WorkFlowServices</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet WorkFlowServices at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    public static String getMaxSequence_ActivitiProcess(HttpServletRequest request, String instanceProcess) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("instanceProcess", instanceProcess);
        HashMap mapValues = new HashMap();
        String rowValue = "";
        String maxSeq = "";
        List<String> listOrder = new ArrayList<>();
        listOrder.add("id");
        try {
            List<GenericValue> result = delegator.findList("ActivitiProcess", EntityCondition.makeCondition(criteria), null, listOrder, null, true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    rowValue = (String) row.get("id");
                    mapValues.put(Integer.valueOf(rowValue), rowValue);
                }
                int maxKey = (int) Collections.max(mapValues.keySet());
                System.out.println("Max value from Key" + mapValues.get(maxKey));
                maxSeq = (String) mapValues.get(maxKey);
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(WorkFlowServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maxSeq;

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
