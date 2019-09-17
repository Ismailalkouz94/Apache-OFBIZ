/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.accounting.workFlow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;

import org.apache.ofbiz.common.activitiServices.WorkFlowServices;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Rbab3a
 */
public class AccountingWorkflow extends HttpServlet {

    public static final String resource = "AccountingUiLabels";
    static String id = "";
    static String password = "";

    static GenericValue userLogin = null;

    public static String getAmountDue(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        PrintWriter out = null;
        String partyId = request.getParameter("partyId");
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

            resultSet = sqlProcessor.executeQuery(sql);
            while (resultSet.next()) {

                total = resultSet.getString("Total");
                break;
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(total);
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(AccountingWorkflow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public static String getTotal(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        PrintWriter out = null;
        String partyId = request.getParameter("partyId");
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

            resultSet = sqlProcessor.executeQuery(sql);
            while (resultSet.next()) {

                total = resultSet.getString("Total");
                break;
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(total);
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(AccountingWorkflow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public static List getInvoiceDetails(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        ArrayList<  Map<String, String>> dataList = new ArrayList<  Map<String, String>>();
        String partyId = (String) request.getSession().getAttribute("partyId");
        try {

            ResultSet resultSet = null;
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            String sql = "Select Inv.INVOICE_ID, Inv.INVOICE_TYPE_ID, Inv.PARTY_ID_FROM, Inv.PARTY_ID, Inv.STATUS_ID, Inv.INVOICE_DATE, Inv.DUE_DATE,Sum(Inv_Itm.AMOUNT * Inv_Itm.QUANTITY) Total\n"
                    + "From INVOICE Inv,INVOICE_ITEM Inv_Itm\n"
                    + "Where Inv.INVOICE_ID = Inv_Itm.INVOICE_ID And INV.PARTY_ID_FROM = '" + partyId + "'\n"
                    + "group By Inv.INVOICE_ID, Inv.INVOICE_TYPE_ID, Inv.PARTY_ID_FROM, Inv.PARTY_ID, Inv.STATUS_ID, Inv.INVOICE_DATE, Inv.DUE_DATE";

            resultSet = sqlProcessor.executeQuery(sql);
            while (resultSet.next()) {
                Map<String, String> innerMap = new HashMap<String, String>();
                innerMap.put("INVOICE_ID", resultSet.getString("INVOICE_ID"));
                innerMap.put("INVOICE_TYPE_ID", resultSet.getString("INVOICE_TYPE_ID"));
                innerMap.put("PARTY_ID_FROM", resultSet.getString("PARTY_ID_FROM"));
                innerMap.put("PARTY_ID", resultSet.getString("PARTY_ID"));

                innerMap.put("STATUS_ID", resultSet.getString("STATUS_ID"));
                innerMap.put("INVOICE_DATE", resultSet.getString("INVOICE_DATE"));
                innerMap.put("DUE_DATE", resultSet.getString("DUE_DATE"));
                innerMap.put("Total", resultSet.getString("Total"));
                dataList.add(innerMap);
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(AccountingWorkflow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataList;
    }

    public static List getPaymentApp(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        ArrayList<  Map<String, String>> dataList = new ArrayList<  Map<String, String>>();
        String paymentId = (String) request.getSession().getAttribute("paymentId");
        ResultSet resultSet = null;
        try {

            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            String sql = "SELECT PA.PAYMENT_ID,PA.INVOICE_ID, Inv.DESCRIPTION, PA.AMOUNT_APPLIED\n"
                    + "FROM PAYMENT_APPLICATION PA, INVOICE INV\n"
                    + "WHERE PA.INVOICE_ID = INV.INVOICE_ID\n"
                    + "AND PAYMENT_ID   ='" + paymentId + "'";

            resultSet = sqlProcessor.executeQuery(sql);
            while (resultSet.next()) {
                Map<String, String> innerMap = new HashMap<String, String>();
                innerMap.put("INVOICE_ID", resultSet.getString("INVOICE_ID"));
                innerMap.put("PAYMENT_ID", resultSet.getString("PAYMENT_ID"));
                innerMap.put("DESCRIPTION", resultSet.getString("DESCRIPTION"));
                innerMap.put("AMOUNT_APPLIED", resultSet.getString("AMOUNT_APPLIED"));

                dataList.add(innerMap);
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(AccountingWorkflow.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            resultSet.close();
        }
        return dataList;
    }

    public static void paymentDetailsReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("****");
        System.out.println(request.getAttribute("partyId"));
        System.out.println(request.getParameter("partyId"));
        System.out.println("****");

        response.setContentType("application/pdf");
        try {

            String jrxmlFile = "applications\\reports\\accounting\\paymentDetailsReport.jrxml";
            InputStream input = new FileInputStream(new File(jrxmlFile));
            JasperReport jasperReport = JasperCompileManager.compileReport(input);

            HashMap[] reportRows = null;
            reportRows = new HashMap[1];
            HashMap row = new HashMap();

            row = new HashMap();
            row.put("partyId", request.getParameter("partyId"));
            System.out.println("partyId " + request.getParameter("partyId"));
            row.put("totalAmount", request.getParameter("totalAmount"));
            row.put("partyName", request.getParameter("partyName"));
            row.put("paymentId", request.getParameter("paymentId"));
            row.put("amountDue", request.getParameter("amountDue"));
            row.put("paymentAmount", request.getParameter("paymentAmount"));
            row.put("dateDue", request.getParameter("dateDue"));

            reportRows[0] = row;

            System.out.println("2 " + reportRows);
            try {

                userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
                String printed_By = userLogin.getString("partyId");

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("printedBy", printed_By);

                JRBeanArrayDataSource ds = new JRBeanArrayDataSource(reportRows);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

                JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

    }

    public static String getAmountApplied(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String paymentId = request.getParameter("paymentId");

        PrintWriter out = null;

        ResultSet resultSet = null;
        String amountApplied = "";
        try {

            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            String sql = "SELECT SUM(PA.AMOUNT_APPLIED) AmountApplied\n"
                    + "FROM PAYMENT_APPLICATION PA,INVOICE INV\n"
                    + "WHERE PA.PAYMENT_ID='" + paymentId + "' AND PA.INVOICE_ID = INV.INVOICE_ID";

            resultSet = sqlProcessor.executeQuery(sql);
            while (resultSet.next()) {
                amountApplied = resultSet.getString("AMOUNTAPPLIED");
                break;

            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(amountApplied);
            out.flush();
        } catch (Exception ex) {
            Logger.getLogger(AccountingWorkflow.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            resultSet.close();
        }
        return amountApplied;
    }


   
public static String startPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paymentId = request.getParameter("paymentId");
        String partyId = request.getParameter("partyId");
     JSONObject mainJson = new JSONObject();
     JSONArray jsonVariablesArray = new JSONArray();
 
 if (WorkFlowServices.getCount(request,"paymentId" ,paymentId) == 0) {
    String jsonResponse = "";
    userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
    String empId = (String) userLogin.getString("userLoginId");
    String processId = WorkFlowServices.getProcessName(request, "Payment", 1);
    HttpSession session = request.getSession();
    String processName = WorkFlowServices.getProcessName(request, "Payment", 0);
    session.setAttribute("processNameId", processId);
    String comments = request.getParameter("comments");
        JSONObject jsonVariables =new JSONObject();
        jsonVariables.put("name","paymentId");
        jsonVariables.put("value",paymentId);
        jsonVariablesArray.add(jsonVariables);
        jsonVariables = new JSONObject();
        jsonVariables.put("name","partyId");
        jsonVariables.put("value",partyId);
        jsonVariablesArray.add(jsonVariables);
      
        jsonVariables = new JSONObject();
        jsonVariables.put("name","action");
        jsonVariables.put("value","start");
        jsonVariablesArray.add(jsonVariables);
        
        jsonVariables = new JSONObject();
        jsonVariables.put("name","remark");
        jsonVariables.put("value","Send payment for Approval");
        jsonVariablesArray.add(jsonVariables);
        
        jsonVariables = new JSONObject();
        jsonVariables.put("name","description");
        jsonVariables.put("value",comments);
        jsonVariablesArray.add(jsonVariables);
        
                   
        mainJson.put("variables",jsonVariablesArray);
        mainJson.put("processDefinitionId",processId);
            id = (String) userLogin.getString("userLoginId");
            password = (String) userLogin.getString("currentPassword");
            jsonResponse = WorkFlowServices.getUrlContentsPost("runtime/process-instances", mainJson.toString(), id,
                    password);
            System.out.println("start Payment Response : " + jsonResponse);
            try {
                if (!jsonResponse.equals("401")) {
                org.json.JSONObject jsonObj  = new org.json.JSONObject(jsonResponse);
                    String taskID = jsonObj.getString("id");
                    System.out.println("taskId "+taskID);
                    LocalDate date = LocalDate.now();
                    session.setAttribute("employee", empId);
                    HashMap item = new HashMap();
                    item.put("processId", processName);
                    item.put("empId", empId);
                    item.put("taskId", taskID);
                    item.put("actionDate", java.sql.Date.valueOf(date));
                    item.put("remark", "Send payment for Approval");
                    item.put("action", "0");
                    WorkFlowServices.insertAuditRemark(item, request, "0");

                    session.setAttribute("employee", empId);
                    session.setAttribute("view", "1");

                    Map<String, String> messageMap = UtilMisc.toMap("successMessage", "");
                    String successMsg = UtilProperties.getMessage(resource, "ProcessStarted", messageMap, UtilHttp.getLocale(request));
                    request.setAttribute("_EVENT_MESSAGE_", successMsg);
                    return "success";

                } else if (jsonResponse.equals("401")) {
                    System.out.println("Unathoriezd User");
                    Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "Your password and confirm password doesn't match");
                    String errMsg = UtilProperties.getMessage(resource, "ServerShutdown", messageMap, UtilHttp.getLocale(request));
                    request.setAttribute("_ERROR_MESSAGE_", errMsg);
                    return "error";
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("Your payment still in-process");
            Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "your request still in process");
            String errMsg = UtilProperties.getMessage(resource, "StillinProcess", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }

        return "";
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AccountingWorkflow</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AccountingWorkflow at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
