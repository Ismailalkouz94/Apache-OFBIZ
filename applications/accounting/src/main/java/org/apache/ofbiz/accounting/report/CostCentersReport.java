/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.accounting.report;

//import com.itextpdf.text.Document;
import com.ibm.icu.util.Calendar;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;

//import org.apache.ofbiz.accounting.report.ObjRecord;
/**
 *
 * @author DELL
 */
public class CostCentersReport {

    public static Vector<ObjRecordCostCentersReport> list = new Vector<ObjRecordCostCentersReport>();

    public static ArrayList<Map<String, String>> getReportXMLStatementCompany(HttpServletRequest request, HttpServletResponse response) throws ParseException {

        ArrayList<  Map<String, String>> dataList = new ArrayList<  Map<String, String>>();
        ResultSet resultSet = null;
        String sqlStr = null;

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            sqlStr = "Select ACCTE.PARTY_ID,\n"
                    + "PGRP.GROUP_NAME,\n"
                    + "nvl(Sum((Select ACCTE.Amount From ACCTG_TRANS_ENTRY ACCTE1 Where ACCTE1.ACCTG_TRANS_ID = ACCTE.ACCTG_TRANS_ID And ACCTE1.ACCTG_TRANS_ENTRY_SEQ_ID = ACCTE.ACCTG_TRANS_ENTRY_SEQ_ID And ACCTE.DEBIT_CREDIT_FLAG = 'D')),0) DR,\n"
                    + "nvl(Sum((Select ACCTE.Amount From ACCTG_TRANS_ENTRY ACCTE1 Where ACCTE1.ACCTG_TRANS_ID = ACCTE.ACCTG_TRANS_ID And ACCTE1.ACCTG_TRANS_ENTRY_SEQ_ID = ACCTE.ACCTG_TRANS_ENTRY_SEQ_ID And ACCTE.DEBIT_CREDIT_FLAG = 'C')),0) CR\n"
                    + "From ACCTG_TRANS ACCT,\n"
                    + "ACCTG_TRANS_ENTRY ACCTE,\n"
                    + "PARTY_GROUP PGRP,PARTY_ACCTG_PREFERENCE PAP\n"
                    + "Where ACCT.ACCTG_TRANS_ID = ACCTE.ACCTG_TRANS_ID\n"
                    + "And ACCTE.PARTY_ID = PGRP.PARTY_ID\n"
                    + "and  PGRP.PARTY_ID=PAP.PARTY_ID\n"
                    + "group by ACCTE.PARTY_ID,PGRP.GROUP_NAME";

            resultSet = sqlProcessor.executeQuery(sqlStr);
            BigDecimal b = new BigDecimal(0), dr, cr;
//            ........... new 19/3/2017
            ObjRecordCostCentersReport record = new ObjRecordCostCentersReport();
            list.clear();
//            ...........
            while (resultSet.next()) {

                Map<String, String> innerMap = new HashMap<String, String>();
                innerMap.put("PartyId", resultSet.getString("PARTY_ID"));
                innerMap.put("GroupName", resultSet.getString("GROUP_NAME"));
                innerMap.put("CR", resultSet.getString("CR"));
                innerMap.put("DR", resultSet.getString("DR"));

                dr = new BigDecimal(resultSet.getString("DR"));
                cr = new BigDecimal(resultSet.getString("CR"));

                // subtract dr with cr and assign result to b
                b = dr.subtract(cr);

                innerMap.put("Balance", b.toString());

                dataList.add(innerMap);
//                ...............
                record = new ObjRecordCostCentersReport();
                record.setPartyId(resultSet.getString("PARTY_ID"));
                record.setGroupName(resultSet.getString("GROUP_NAME"));
                record.setCR(resultSet.getString("CR"));
                record.setDR(resultSet.getString("DR"));
                record.setBalance(b.toString());
//                .....................

                list.add(record);
            }
//            ....................new 

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        return dataList;
    }

    public static ArrayList<Map<String, String>> getReportXMLCostCenters(HttpServletRequest request, HttpServletResponse response) throws ParseException {

        ArrayList<  Map<String, String>> dataList = new ArrayList<  Map<String, String>>();
        ResultSet resultSet = null;
        String sqlStr = null;

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

       
            sqlStr = "Select ACCTE.PARTY_ID,PGRP.GROUP_NAME,\n"
                    + "nvl(Sum((Select ACCTE.Amount From ACCTG_TRANS_ENTRY ACCTE1 Where ACCTE1.ACCTG_TRANS_ID = ACCTE.ACCTG_TRANS_ID And ACCTE1.ACCTG_TRANS_ENTRY_SEQ_ID = ACCTE.ACCTG_TRANS_ENTRY_SEQ_ID And ACCTE.DEBIT_CREDIT_FLAG = 'D')),0) DR,\n"
                    + "nvl(Sum((Select ACCTE.Amount From ACCTG_TRANS_ENTRY ACCTE1 Where ACCTE1.ACCTG_TRANS_ID = ACCTE.ACCTG_TRANS_ID And ACCTE1.ACCTG_TRANS_ENTRY_SEQ_ID = ACCTE.ACCTG_TRANS_ENTRY_SEQ_ID And ACCTE.DEBIT_CREDIT_FLAG = 'C')),0) CR\n"
                    + "From ACCTG_TRANS_ENTRY ACCTE,\n"
                    + "PARTY_GROUP PGRP\n"
                    + "Where ACCTE.PARTY_ID = PGRP.PARTY_ID\n"
                    + "and PGRP.PARTY_ID IN (select PARTY_ID from PARTY_ROLE  where  ROLE_TYPE_ID ='COST_CENTER')\n"
                    + "and ACCTE.GL_ACCOUNT_ID IN (SELECT GLA.GL_ACCOUNT_ID\n"
                    + "FROM GL_ACCOUNT_ORGANIZATION GLAO\n"
                    + "INNER JOIN GL_ACCOUNT GLA\n"
                    + "ON GLAO.GL_ACCOUNT_ID         = GLA.GL_ACCOUNT_ID\n"
                    + "WHERE GLA.GL_ACCOUNT_CLASS_ID  IN\n"
                    + "(SELECT GL_ACCOUNT_CLASS_ID\n"
                    + "FROM GL_ACCOUNT_CLASS\n"
                    + "START WITH GL_ACCOUNT_CLASS_ID       = 'EXPENSE'\n"
                    + "CONNECT BY prior GL_ACCOUNT_CLASS_ID = PARENT_CLASS_ID)\n"
                    + "AND GLAO.ORGANIZATION_PARTY_ID='Company')\n"
                    + "group by ACCTE.PARTY_ID,PGRP.GROUP_NAME";

            resultSet = sqlProcessor.executeQuery(sqlStr);
            BigDecimal b = new BigDecimal(0), dr, cr;
//            ........... new 19/3/2017
            ObjRecordCostCentersReport record = new ObjRecordCostCentersReport();
            list.clear();
//            ...........
            while (resultSet.next()) {

                Map<String, String> innerMap = new HashMap<String, String>();
                innerMap.put("PartyId", resultSet.getString("PARTY_ID"));
                innerMap.put("GroupName", resultSet.getString("GROUP_NAME"));
                innerMap.put("CR", resultSet.getString("CR"));
                innerMap.put("DR", resultSet.getString("DR"));

                dr = new BigDecimal(resultSet.getString("DR"));
                cr = new BigDecimal(resultSet.getString("CR"));

                // subtract dr with cr and assign result to b
                b = dr.subtract(cr);

                innerMap.put("Balance", b.toString());

                dataList.add(innerMap);
//                ...............
                record = new ObjRecordCostCentersReport();
                record.setPartyId(resultSet.getString("PARTY_ID"));
                record.setGroupName(resultSet.getString("GROUP_NAME"));
                record.setCR(resultSet.getString("CR"));
                record.setDR(resultSet.getString("DR"));
                record.setBalance(b.toString());
//                .....................

                list.add(record);
            }
//            ....................new 

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        return dataList;
    }

    public static void getReportPDFCostCenters(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("getReportPDFCostCenters");
        System.out.println("company " + request.getParameter("company"));
        System.out.println("groupName " + request.getParameter("groupName"));
        System.out.println("userLoginId " + request.getParameter("userLoginId"));
        ResultSet resultSet = null;
        GenericValue userLogin = null;
        response.setContentType("application/pdf");
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
//                String jrxmlFile = "C:/Users/DELL/JaspersoftWorkspace/test/ReportOneDataSet.jrxml";
            String jrxmlFile = "applications\\reports\\accounting\\CostCentersReport.jrxml";

            InputStream input = new FileInputStream(new File(jrxmlFile));
            JasperReport jasperReport = JasperCompileManager.compileReport(input);

            HashMap[] reportRows = null;
            reportRows = new HashMap[list.size()];
            HashMap row = new HashMap();
            for (int i = 0; i < list.size(); i++) {

                row = new HashMap();
                row.put("PartyId", list.get(i).getPartyId());
                row.put("GroupName", list.get(i).getGroupName());
                row.put("CR", list.get(i).getCR());
                row.put("DR", list.get(i).getDR());
                row.put("Balance", list.get(i).getBalance());
                reportRows[i] = row;
            }
            try {
                userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
                String printed_By = userLogin.getString("partyId");

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("company", request.getParameter("company") + " - " + request.getParameter("groupName"));
                params.put("userLoginId", request.getParameter("userLoginId"));

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
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

    }
}
