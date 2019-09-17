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
public class javaClass {

    public static String FromDate = "";
    public static String ToDate = "";
    public static Vector<ObjRecord> list = new Vector<ObjRecord>();

    public static void getReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection con = null;
        Statement st = null;
        ResultSet resultSet = null;
        GenericValue userLogin = null;
        response.setContentType("application/pdf");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/OFBIZ", "ofbiz", "ofbiz");

            String jrxmlFile = "C:/Users/DELL/JaspersoftWorkspace/test/Last1.jrxml";

            InputStream input = new FileInputStream(new File(jrxmlFile));
            JasperReport jasperReport = JasperCompileManager.compileReport(input);

            try {
                String party_Id = request.getParameter("partyId");

                String sqlStr = "select MANIFEST_COMPANY_NAME from VENDOR WHERE PARTY_ID='" + party_Id + "'";
                st = con.createStatement();
                resultSet = st.executeQuery(sqlStr);
                String render_Name = "";
                while (resultSet.next()) {
                    render_Name = resultSet.getString("MANIFEST_COMPANY_NAME");
                }

                userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
                String printed_By = userLogin.getString("partyId");

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("partyId", party_Id);
                params.put("printedBy", printed_By);
                params.put("renderName", render_Name);
                params.put("FromDate", FromDate);
                params.put("ToDate", ToDate);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);

                JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(javaClass.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public static ArrayList< Map<String, String>> getReportXML(HttpServletRequest request, HttpServletResponse response) throws ParseException {

        String partyId = request.getParameter("partyId");

        FromDate = request.getParameter("fromDate") != null && !((request.getParameter("fromDate")).isEmpty()) ? "'" + request.getParameter("fromDate") + "'" : null;
        ToDate = request.getParameter("toDate") != null && !((request.getParameter("toDate")).isEmpty()) ? "'" + request.getParameter("toDate") + "'" : null;
        String flag = request.getParameter("flagBeginBalance");
        System.out.println("** notTrans **");
        String notTrans = request.getParameter("notTrans");
        System.out.println("** notTrans **" + notTrans);

        ArrayList<  Map<String, String>> dataList = new ArrayList<  Map<String, String>>();

        ResultSet resultSet = null;
        String sqlStr1 = null, sqlStr2 = null, strAll = null;
        String sqlStr1Exp = null, sqlStr2Exp = null;

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlExpense = " And ACCTE1.GL_ACCOUNT_ID IN (SELECT GLA.GL_ACCOUNT_ID\n"
                    + "FROM GL_ACCOUNT_ORGANIZATION GLAO\n"
                    + "INNER JOIN GL_ACCOUNT GLA\n"
                    + "ON GLAO.GL_ACCOUNT_ID         = GLA.GL_ACCOUNT_ID\n"
                    + "WHERE GLA.GL_ACCOUNT_CLASS_ID  IN\n"
                    + "(SELECT GL_ACCOUNT_CLASS_ID\n"
                    + "FROM GL_ACCOUNT_CLASS\n"
                    + "START WITH GL_ACCOUNT_CLASS_ID       = 'EXPENSE'\n"
                    + "CONNECT BY prior GL_ACCOUNT_CLASS_ID = PARENT_CLASS_ID)\n"
                    + "AND GLAO.ORGANIZATION_PARTY_ID='Company')";

            sqlStr1 = "SELECT '' ACCTG_TRANS_ID,\n"
                    + "(TO_DATE(" + FromDate + " ,'YYYY-MM-DD')-1) TRANSACTION_DATE,\n"
                    + "'Begin Balance' DESCRIPTION,\n"
                    + "nvl((SELECT sum(ACCTE1.Amount) Dr\n"
                    + "FROM ACCTG_TRANS ACCT1,\n"
                    + "ACCTG_TRANS_ENTRY ACCTE1\n"
                    + "WHERE ACCT1.ACCTG_TRANS_ID                         = ACCTE1.ACCTG_TRANS_ID\n"
                    + "AND ACCTE1.DEBIT_CREDIT_FLAG                       = 'D'\n"
                    + "AND ACCTE1.PARTY_ID                                = '" + partyId + "'\n"
                    + "AND ACCT1.IS_POSTED                               = 'Y'\n"
                    + "And to_char(ACCT1.TRANSACTION_DATE ,'YYYY-MM-DD') < NVL(" + FromDate + " ,to_char(ACCT1.TRANSACTION_DATE ,'YYYY-MM-DD'))\n" //                    + "And ACCTE1.GL_ACCOUNT_ID not in(SELECT DISTINCT GL_DEF.GL_ACCOUNT_ID\n"
                    //                    + "FROM GL_ACCOUNT_TYPE_DEFAULT GL_DEF,\n"
                    //                    + "PAYMENT_GL_ACCOUNT_TYPE_MAP GL_PAY\n"
                    //                    + "WHERE GL_DEF.GL_ACCOUNT_TYPE_ID = GL_PAY.GL_ACCOUNT_TYPE_ID)\n"

                    + "),0) DR,\n"
                    + "nvl((SELECT sum(ACCTE1.Amount) Cr\n"
                    + "FROM ACCTG_TRANS ACCT1,\n"
                    + "ACCTG_TRANS_ENTRY ACCTE1\n"
                    + "WHERE ACCT1.ACCTG_TRANS_ID                         = ACCTE1.ACCTG_TRANS_ID\n"
                    + "AND ACCTE1.DEBIT_CREDIT_FLAG                       = 'C'\n"
                    + "AND ACCTE1.PARTY_ID                                = '" + partyId + "'\n"
                    + "AND ACCT1.IS_POSTED                               = 'Y'\n"
                    + "And to_char(ACCT1.TRANSACTION_DATE ,'YYYY-MM-DD') < NVL(" + FromDate + " ,to_char(ACCT1.TRANSACTION_DATE ,'YYYY-MM-DD'))\n"
                    //                    + "And ACCTE1.GL_ACCOUNT_ID not in(SELECT DISTINCT GL_DEF.GL_ACCOUNT_ID\n"
                    //                    + "FROM GL_ACCOUNT_TYPE_DEFAULT GL_DEF,\n"
                    //                    + "PAYMENT_GL_ACCOUNT_TYPE_MAP GL_PAY\n"
                    //                    + "WHERE GL_DEF.GL_ACCOUNT_TYPE_ID = GL_PAY.GL_ACCOUNT_TYPE_ID)\n"
                    + "),0) CR\n"
                    + "FROM  dual\n"
                    + "UNION ";

            sqlStr2 = "SELECT ACCT.ACCTG_TRANS_ID,\n"
                    + "TRUNC(to_Date(TO_CHAR(ACCT.TRANSACTION_DATE,'DD-MM-YYYY'),'DD-MM-YYYY HH24:MI')) TRANSACTION_DATE,\n"
                    + "ACCTE.DESCRIPTION,\n"
                    + "nvl((SELECT ACCTE1.Amount Dr\n"
                    + "FROM ACCTG_TRANS ACCT1,\n"
                    + "ACCTG_TRANS_ENTRY ACCTE1\n"
                    + "WHERE ACCT1.ACCTG_TRANS_ID                         = ACCTE1.ACCTG_TRANS_ID\n"
                    + "AND ACCT.ACCTG_TRANS_ID                          = ACCT1.ACCTG_TRANS_ID\n"
                    + "AND ACCTE.ACCTG_TRANS_ENTRY_SEQ_ID               = ACCTE1.ACCTG_TRANS_ENTRY_SEQ_ID\n"
                    + "AND ACCTE1.DEBIT_CREDIT_FLAG                       = 'D'\n"
                    + "),0) DR,\n"
                    + "nvl((SELECT ACCTE1.Amount Cr\n"
                    + "FROM ACCTG_TRANS ACCT1,\n"
                    + "ACCTG_TRANS_ENTRY ACCTE1\n"
                    + "WHERE ACCT1.ACCTG_TRANS_ID                         = ACCTE1.ACCTG_TRANS_ID\n"
                    + "AND ACCT.ACCTG_TRANS_ID                          = ACCT1.ACCTG_TRANS_ID\n"
                    + "AND ACCTE.ACCTG_TRANS_ENTRY_SEQ_ID               = ACCTE1.ACCTG_TRANS_ENTRY_SEQ_ID\n"
                    + "AND ACCTE1.DEBIT_CREDIT_FLAG                       = 'C'\n"
                    + "),0) CR\n"
                    + "FROM ACCTG_TRANS ACCT,\n"
                    + "ACCTG_TRANS_ENTRY ACCTE\n"
                    + "WHERE ACCT.ACCTG_TRANS_ID                         = ACCTE.ACCTG_TRANS_ID\n"
                    + "AND ACCTE.PARTY_ID                                = '" + partyId + "'\n"
                    + "AND ACCT.IS_POSTED                               = 'Y'\n"
                    + "AND TO_CHAR(ACCT.TRANSACTION_DATE ,'YYYY-MM-DD') >= NVL( " + FromDate + " ,TO_CHAR(ACCT.TRANSACTION_DATE ,'YYYY-MM-DD'))\n"
                    + "AND TO_CHAR(ACCT.TRANSACTION_DATE ,'YYYY-MM-DD') <= NVL( " + ToDate + " ,TO_CHAR(ACCT.TRANSACTION_DATE ,'YYYY-MM-DD'))\n"
                    //                    + "And ACCTE.GL_ACCOUNT_ID not in(SELECT DISTINCT GL_DEF.GL_ACCOUNT_ID\n"
                    //                    + "FROM GL_ACCOUNT_TYPE_DEFAULT GL_DEF,\n"
                    //                    + "PAYMENT_GL_ACCOUNT_TYPE_MAP GL_PAY\n"
                    //                    + "WHERE GL_DEF.GL_ACCOUNT_TYPE_ID = GL_PAY.GL_ACCOUNT_TYPE_ID)\n"
                    + "ORDER BY TRANSACTION_DATE";

            sqlStr1Exp = "SELECT '' ACCTG_TRANS_ID,\n"
                    + "(TO_DATE(" + FromDate + " ,'YYYY-MM-DD')-1) TRANSACTION_DATE,\n"
                    + "'Begin Balance' DESCRIPTION,\n"
                    + "nvl((SELECT sum(ACCTE1.Amount) Dr\n"
                    + "FROM ACCTG_TRANS ACCT1,\n"
                    + "ACCTG_TRANS_ENTRY ACCTE1\n"
                    + "WHERE ACCT1.ACCTG_TRANS_ID                         = ACCTE1.ACCTG_TRANS_ID\n"
                    + "AND ACCTE1.DEBIT_CREDIT_FLAG                       = 'D'\n"
                    + "AND ACCTE1.PARTY_ID                                = '" + partyId + "'\n"
                    + "AND ACCT1.IS_POSTED                               = 'Y'\n"
                    + "And to_char(ACCT1.TRANSACTION_DATE ,'YYYY-MM-DD') < NVL(" + FromDate + " ,to_char(ACCT1.TRANSACTION_DATE ,'YYYY-MM-DD'))\n"
                    + "And ACCTE1.GL_ACCOUNT_ID IN (SELECT GLA.GL_ACCOUNT_ID\n"
                    + "FROM GL_ACCOUNT_ORGANIZATION GLAO\n"
                    + "INNER JOIN GL_ACCOUNT GLA\n"
                    + "ON GLAO.GL_ACCOUNT_ID         = GLA.GL_ACCOUNT_ID\n"
                    + "WHERE GLA.GL_ACCOUNT_CLASS_ID  IN\n"
                    + "(SELECT GL_ACCOUNT_CLASS_ID\n"
                    + "FROM GL_ACCOUNT_CLASS\n"
                    + "START WITH GL_ACCOUNT_CLASS_ID       = 'EXPENSE'\n"
                    + "CONNECT BY prior GL_ACCOUNT_CLASS_ID = PARENT_CLASS_ID)\n"
                    + "AND GLAO.ORGANIZATION_PARTY_ID='Company')"
                    + "),0) DR,\n"
                    + "nvl((SELECT sum(ACCTE1.Amount) Cr\n"
                    + "FROM ACCTG_TRANS ACCT1,\n"
                    + "ACCTG_TRANS_ENTRY ACCTE1\n"
                    + "WHERE ACCT1.ACCTG_TRANS_ID                         = ACCTE1.ACCTG_TRANS_ID\n"
                    + "AND ACCTE1.DEBIT_CREDIT_FLAG                       = 'C'\n"
                    + "AND ACCTE1.PARTY_ID                                = '" + partyId + "'\n"
                    + "AND ACCT1.IS_POSTED                               = 'Y'\n"
                    + "And to_char(ACCT1.TRANSACTION_DATE ,'YYYY-MM-DD') < NVL(" + FromDate + " ,to_char(ACCT1.TRANSACTION_DATE ,'YYYY-MM-DD'))\n"
                    + "And ACCTE1.GL_ACCOUNT_ID IN (SELECT GLA.GL_ACCOUNT_ID\n"
                    + "FROM GL_ACCOUNT_ORGANIZATION GLAO\n"
                    + "INNER JOIN GL_ACCOUNT GLA\n"
                    + "ON GLAO.GL_ACCOUNT_ID         = GLA.GL_ACCOUNT_ID\n"
                    + "WHERE GLA.GL_ACCOUNT_CLASS_ID  IN\n"
                    + "(SELECT GL_ACCOUNT_CLASS_ID\n"
                    + "FROM GL_ACCOUNT_CLASS\n"
                    + "START WITH GL_ACCOUNT_CLASS_ID       = 'EXPENSE'\n"
                    + "CONNECT BY prior GL_ACCOUNT_CLASS_ID = PARENT_CLASS_ID)\n"
                    + "AND GLAO.ORGANIZATION_PARTY_ID='Company')"
                    + "),0) CR\n"
                    + "FROM  dual\n"
                    + "UNION ";

            sqlStr2Exp = "SELECT ACCT.ACCTG_TRANS_ID,\n"
                    + "TRUNC(to_Date(TO_CHAR(ACCT.TRANSACTION_DATE,'DD-MM-YYYY'),'DD-MM-YYYY HH24:MI')) TRANSACTION_DATE,\n"
                    + "ACCTE.DESCRIPTION,\n"
                    + "nvl((SELECT ACCTE1.Amount Dr\n"
                    + "FROM ACCTG_TRANS ACCT1,\n"
                    + "ACCTG_TRANS_ENTRY ACCTE1\n"
                    + "WHERE ACCT1.ACCTG_TRANS_ID                         = ACCTE1.ACCTG_TRANS_ID\n"
                    + "AND ACCT.ACCTG_TRANS_ID                          = ACCT1.ACCTG_TRANS_ID\n"
                    + "AND ACCTE.ACCTG_TRANS_ENTRY_SEQ_ID               = ACCTE1.ACCTG_TRANS_ENTRY_SEQ_ID\n"
                    + "AND ACCTE1.DEBIT_CREDIT_FLAG                       = 'D'\n"
                    + "),0) DR,\n"
                    + "nvl((SELECT ACCTE1.Amount Cr\n"
                    + "FROM ACCTG_TRANS ACCT1,\n"
                    + "ACCTG_TRANS_ENTRY ACCTE1\n"
                    + "WHERE ACCT1.ACCTG_TRANS_ID                         = ACCTE1.ACCTG_TRANS_ID\n"
                    + "AND ACCT.ACCTG_TRANS_ID                          = ACCT1.ACCTG_TRANS_ID\n"
                    + "AND ACCTE.ACCTG_TRANS_ENTRY_SEQ_ID               = ACCTE1.ACCTG_TRANS_ENTRY_SEQ_ID\n"
                    + "AND ACCTE1.DEBIT_CREDIT_FLAG                       = 'C'\n"
                    + "),0) CR\n"
                    + "FROM ACCTG_TRANS ACCT,\n"
                    + "ACCTG_TRANS_ENTRY ACCTE\n"
                    + "WHERE ACCT.ACCTG_TRANS_ID                         = ACCTE.ACCTG_TRANS_ID\n"
                    + "AND ACCTE.PARTY_ID                                = '" + partyId + "'\n"
                    + "AND ACCT.IS_POSTED                               = 'Y'\n"
                    + "AND TO_CHAR(ACCT.TRANSACTION_DATE ,'YYYY-MM-DD') >= NVL( " + FromDate + " ,TO_CHAR(ACCT.TRANSACTION_DATE ,'YYYY-MM-DD'))\n"
                    + "AND TO_CHAR(ACCT.TRANSACTION_DATE ,'YYYY-MM-DD') <= NVL( " + ToDate + " ,TO_CHAR(ACCT.TRANSACTION_DATE ,'YYYY-MM-DD'))\n"
                    + "And ACCTE.GL_ACCOUNT_ID IN (SELECT GLA.GL_ACCOUNT_ID\n"
                    + "FROM GL_ACCOUNT_ORGANIZATION GLAO\n"
                    + "INNER JOIN GL_ACCOUNT GLA\n"
                    + "ON GLAO.GL_ACCOUNT_ID         = GLA.GL_ACCOUNT_ID\n"
                    + "WHERE GLA.GL_ACCOUNT_CLASS_ID  IN\n"
                    + "(SELECT GL_ACCOUNT_CLASS_ID\n"
                    + "FROM GL_ACCOUNT_CLASS\n"
                    + "START WITH GL_ACCOUNT_CLASS_ID       = 'EXPENSE'\n"
                    + "CONNECT BY prior GL_ACCOUNT_CLASS_ID = PARENT_CLASS_ID)\n"
                    + "AND GLAO.ORGANIZATION_PARTY_ID='Company')"
                    + "ORDER BY TRANSACTION_DATE";

            if (flag.equals("W")) {
                if (notTrans.equals("E")) {
                    strAll = sqlStr1Exp + sqlStr2Exp;
                } else {
                    strAll = sqlStr1 + sqlStr2;
                }
            } else {
                if (notTrans.equals("E")) {
                    strAll = sqlStr2Exp;
                } else {
                    strAll = sqlStr2;
                }
            }
            System.out.println(strAll);
            resultSet = sqlProcessor.executeQuery(strAll);
            int c = 0;
            BigDecimal b = new BigDecimal(0), dr, cr;
//            float b = 0f;
//            ........... new 19/3/2017
            ObjRecord record = new ObjRecord();
            list.clear();
//            ...........
            while (resultSet.next()) {

                Map<String, String> innerMap = new HashMap<String, String>();
                innerMap.put("AcctgTransId", resultSet.getString("ACCTG_TRANS_ID"));
                innerMap.put("TransactionDate", resultSet.getString("TRANSACTION_DATE").substring(0, 10));
                innerMap.put("Description", resultSet.getString("DESCRIPTION"));
                innerMap.put("CR", resultSet.getString("CR"));
                innerMap.put("DR", resultSet.getString("DR"));

                dr = new BigDecimal(resultSet.getString("DR"));
                cr = new BigDecimal(resultSet.getString("CR"));

                // subtract dr with cr and assign result to b
                b = b.add(dr.subtract(cr));

                innerMap.put("Balance", b.toString());
                dataList.add(innerMap);
//                ...............
                record = new ObjRecord();
                record.setAcctgTransId(resultSet.getString("ACCTG_TRANS_ID"));
                record.setTransactionDate(resultSet.getString("TRANSACTION_DATE").substring(0, 10));
                record.setDescription(resultSet.getString("DESCRIPTION"));
                record.setCR(resultSet.getString("CR"));
                record.setDR(resultSet.getString("DR"));
                record.setBalance(b.toString());
//                .....................

                list.add(record);

                c++;

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

    public static void oneDataSet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ResultSet resultSet = null;
        GenericValue userLogin = null;
        response.setContentType("application/pdf");
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
//                String jrxmlFile = "C:/Users/DELL/JaspersoftWorkspace/test/ReportOneDataSet.jrxml";

            String jrxmlFile = "applications\\reports\\accountingAP\\ReportOneDataSet.jrxml";

            InputStream input = new FileInputStream(new File(jrxmlFile));
            JasperReport jasperReport = JasperCompileManager.compileReport(input);

            HashMap[] reportRows = null;
            reportRows = new HashMap[list.size()];
            HashMap row = new HashMap();
            for (int i = 0; i < list.size(); i++) {

                row = new HashMap();
                row.put("TransId", list.get(i).getAcctgTransId());
                row.put("Date", list.get(i).getTransactionDate());
                row.put("Description", list.get(i).getDescription());
                row.put("CR", list.get(i).getCR());
                row.put("DR", list.get(i).getDR());
                row.put("Balance", list.get(i).getBalance());
                reportRows[i] = row;
            }
            try {
                String party_Id = request.getParameter("partyId");

                String sqlStr2 = "select MANIFEST_COMPANY_NAME from VENDOR WHERE PARTY_ID='" + party_Id + "'";

                resultSet = sqlProcessor.executeQuery(sqlStr2);
                String render_Name = "";
                while (resultSet.next()) {
                    render_Name = resultSet.getString("MANIFEST_COMPANY_NAME");
                }

                userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
                String printed_By = userLogin.getString("partyId");
                String titleReport = "Statement of Acccount";
                System.out.println(titleReport + " getParameter Report 2 = " + request.getParameter("Report"));
                if (Integer.parseInt(request.getParameter("Report")) == 1) {
                    titleReport = "Vendor Statement";
                    System.out.println(titleReport + " getParameter Report 1 = " + request.getParameter("Report"));
                } else if (Integer.parseInt(request.getParameter("Report")) == 2) {
                    titleReport = "Statement Of Account (Transactions)";
                    System.out.println(titleReport + " getParameter Report 2 = " + request.getParameter("Report"));
                }

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("partyId", party_Id);
                params.put("printedBy", printed_By);
                params.put("renderName", render_Name);
                params.put("FromDate", FromDate);
                params.put("ToDate", ToDate);
                params.put("titleReport", titleReport);
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
