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
import org.antlr.v4.runtime.tree.Tree;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;

//import org.apache.ofbiz.accounting.report.ObjRecord;
/**
 *
 * @author DELL
 */
public class ProfitAndLoss {

    public static String fromDate, toDate;
    public static Vector<BiTrialBalanceObjRecordReport> list = new Vector<BiTrialBalanceObjRecordReport>();

    public static void parameter(HttpServletRequest request, HttpServletResponse response) {
        fromDate = request.getParameter("fromDate");
        toDate = request.getParameter("toDate");

//        request.getSession().setAttribute("fromDate", fromDate);
//        request.getSession().setAttribute("toDate", toDate);
        System.out.println("parameter");

    }

    public static ArrayList<Map<String, String>> getReportXMLProfitAndLossExpense(HttpServletRequest request, HttpServletResponse response) throws ParseException {

        System.out.println("getReportXMLProfitAndLossExpense");

        ArrayList<  Map<String, String>> dataList = new ArrayList<  Map<String, String>>();

        ResultSet resultSet = null;
        String sqlStr = null;

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            sqlStr = "Select * From (SELECT GL.GL_ACCOUNT_ID,GL.ACCOUNT_NAME,GL_ACCOUNT_CLASS_ID,\n"
                    + "(select nvl(sum(ATE1.Amount),0) Dr\n"
                    + "from  ACCTG_TRANS ACT1\n"
                    + "INNER JOIN ACCTG_TRANS_ENTRY ATE1\n"
                    + "ON ACT1.ACCTG_TRANS_ID = ATE1.ACCTG_TRANS_ID\n"
                    + "Where 'Y'                                         = ACT1.IS_POSTED\n"
                    + "AND 'ACTUAL'                                    = ACT1.GL_FISCAL_TYPE_ID\n"
                    + "AND 'PERIOD_CLOSING'                           <> ACT1.ACCTG_TRANS_TYPE_ID\n"
                    + "AND 'D'                                         = ATE1.DEBIT_CREDIT_FLAG \n"
                    + "AND TO_CHAR(ACT1.TRANSACTION_DATE,'DD-MM-YYYY') >= TO_CHAR(TO_DATE('" + fromDate + "','YYYY-MM-DD'),'DD-MM-YYYY')\n"
                    + "AND TO_CHAR(ACT1.TRANSACTION_DATE,'DD-MM-YYYY')  < TO_CHAR(TO_DATE('" + toDate + "','YYYY-MM-DD'),'DD-MM-YYYY')\n"
                    + "And ATE1.GL_ACCOUNT_ID = GL.GL_ACCOUNT_ID\n"
                    + ") DR,\n"
                    + "(select nvl(sum(ATE2.Amount),0) Cr\n"
                    + "from  ACCTG_TRANS_ENTRY ATE2\n"
                    + "INNER JOIN ACCTG_TRANS ACT2\n"
                    + "ON ATE2.ACCTG_TRANS_ID = ACT2.ACCTG_TRANS_ID\n"
                    + "Where 'Y'                                         = ACT2.IS_POSTED\n"
                    + "AND 'ACTUAL'                                    = ACT2.GL_FISCAL_TYPE_ID\n"
                    + "AND 'PERIOD_CLOSING'                           <> ACT2.ACCTG_TRANS_TYPE_ID\n"
                    + "AND 'C'                                         = ATE2.DEBIT_CREDIT_FLAG\n"
                    + "AND TO_CHAR(ACT2.TRANSACTION_DATE,'DD-MM-YYYY') >= TO_CHAR(TO_DATE('2017-01-01','YYYY-MM-DD'),'DD-MM-YYYY')\n"
                    + "AND TO_CHAR(ACT2.TRANSACTION_DATE,'DD-MM-YYYY')  < TO_CHAR(TO_DATE('2017-12-31','YYYY-MM-DD'),'DD-MM-YYYY')\n"
                    + "And ATE2.GL_ACCOUNT_ID = GL.GL_ACCOUNT_ID\n"
                    + ") CR\n"
                    + "FROM GL_ACCOUNT GL\n"
                    + "where GL.GL_ACCOUNT_CLASS_ID                     IN\n"
                    + "(SELECT GL_ACCOUNT_CLASS_ID\n"
                    + "FROM GL_ACCOUNT_CLASS\n"
                    + "START WITH GL_ACCOUNT_CLASS_ID       = 'EXPENSE'\n"
                    + "CONNECT BY prior GL_ACCOUNT_CLASS_ID = PARENT_CLASS_ID\n"
                    + ")\n"
                    + ")\n"
                    + "Where (CR <> 0\n"
                    + "or DR <> 0)\n"
                    + "ORDER BY GL_ACCOUNT_ID";

            resultSet = sqlProcessor.executeQuery(sqlStr);
            BigDecimal b = new BigDecimal(0), dr, cr;
//            ........... new 29/6/2017
//            BiTrialBalanceObjRecordReport record = new BiTrialBalanceObjRecordReport();
//            list.clear();
//            ...........
            while (resultSet.next()) {

                Map<String, String> innerMap = new HashMap<String, String>();
                innerMap.put("GL_ACCOUNT_ID", resultSet.getString("GL_ACCOUNT_ID"));
                innerMap.put("ACCOUNT_NAME", resultSet.getString("ACCOUNT_NAME"));

                dr = new BigDecimal(resultSet.getString("DR"));
                cr = new BigDecimal(resultSet.getString("CR"));
                // subtract dr with cr and assign result to b
                b = dr.subtract(cr);
                innerMap.put("BALANCE", b.toString());
                dataList.add(innerMap);

//                ...............
//                record = new BiTrialBalanceObjRecordReport();
//                record.setGL_ACCOUNT_ID(resultSet.getString("GL_ACCOUNT_ID"));
//                record.setACCOUNT_NAME(resultSet.getString("ACCOUNT_NAME"));
//                record.setCR(resultSet.getString("CR"));
//                record.setDR(resultSet.getString("DR"));
//                record.setBALANCE(b.toString());
//                .....................
//                list.add(record);
            }
            System.out.println("dataList");
            System.out.println(dataList);
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

    public static void getReportPDFProfitAndLossExpense(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("getReportPDFProfitAndLossExpense");
        ResultSet resultSet = null;
        GenericValue userLogin = null;
        response.setContentType("application/pdf");
        try {
//            Delegator delegator = (Delegator) request.getAttribute("delegator");
//            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
//                String jrxmlFile = "C:/Users/DELL/JaspersoftWorkspace/test/ReportOneDataSet.jrxml";
            String jrxmlFile = "applications\\reports\\accounting\\BiTrialBalance.jrxml";
            System.out.println("1");
            InputStream input = new FileInputStream(new File(jrxmlFile));
            JasperReport jasperReport = JasperCompileManager.compileReport(input);

            HashMap[] reportRows = null;
            reportRows = new HashMap[list.size()];
            HashMap row = new HashMap();
            for (int i = 0; i < list.size(); i++) {

                row = new HashMap();
                row.put("GL_ACCOUNT_ID", list.get(i).getGL_ACCOUNT_ID());
                row.put("ACCOUNT_NAME", list.get(i).getACCOUNT_NAME());
                row.put("CR", list.get(i).getCR());
                row.put("DR", list.get(i).getDR());
                row.put("BALANCE", list.get(i).getBALANCE());
                reportRows[i] = row;
            }
            System.out.println("2");
            try {
                userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
                String printed_By = userLogin.getString("partyId");

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("printedBy", printed_By);
                params.put("fromDate", fromDate);
                params.put("toDate", toDate);
                System.out.println("3");
                JRBeanArrayDataSource ds = new JRBeanArrayDataSource(reportRows);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);
                System.out.println("4");
                JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
                System.out.println("5");
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
