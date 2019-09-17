/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.accounting.dashboard;

//import com.itextpdf.text.Document;
import org.apache.ofbiz.accounting.report.*;
import com.ibm.icu.util.Calendar;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
public class Dashboard {


    

    public static ArrayList<Map<String, String>> BiTrialBalance(HttpServletRequest request, HttpServletResponse response) throws ParseException {
       
          
        String fromDate = request.getParameter("fromDate");
       String toDate = request.getParameter("toDate");
        
        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int year = now.get(Calendar.YEAR);
        // if(fromDate is null) 
        if (fromDate == null || fromDate.isEmpty()){
            fromDate = year + "-01-01";
            toDate = year + "-12-31";
        }
        
        System.out.println("parameter");
        System.out.println(">>>" + fromDate + ">>>" + toDate);
        request.getSession().setAttribute("fDate", fromDate);
        request.getSession().setAttribute("tDate", toDate);

        ArrayList<  Map<String, String>> dataList = new ArrayList<  Map<String, String>>();

        ResultSet resultSet = null;
        String sqlStr = null;

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            sqlStr = "SELECT distinct RPAD ('*', 2 * LEVEL, '*') || GL.GL_ACCOUNT_ID Parent,substr(sys_connect_by_path(GL.GL_ACCOUNT_ID,'->'),3) tree,LEVEL-1 LVL,GL.GL_ACCOUNT_ID,GL.ACCOUNT_NAME, GL.PARENT_GL_ACCOUNT_ID ,\n"
                    + "(select nvl(sum(ATE1.Amount),0) Dr\n"
                    + "from  ACCTG_TRANS_ENTRY ATE1\n"
                    + "INNER JOIN ACCTG_TRANS ACT1\n"
                    + "ON ATE1.ACCTG_TRANS_ID = ACT1.ACCTG_TRANS_ID\n"
                    + "RIGHT OUTER JOIN GL_ACCOUNT GL1 ON ATE1.GL_ACCOUNT_ID = GL1.GL_ACCOUNT_ID\n"
                    + "Where 'Y'                                         = ACT1.IS_POSTED\n"
                    + "AND 'ACTUAL'                                    = ACT1.GL_FISCAL_TYPE_ID\n"
                    + "AND 'PERIOD_CLOSING'                           <> ACT1.ACCTG_TRANS_TYPE_ID\n"
                    + "AND 'D'                                         = ATE1.DEBIT_CREDIT_FLAG\n"
                    + "AND TO_CHAR(ACT1.TRANSACTION_DATE,'DD-MM-YYYY') >= TO_CHAR(TO_DATE('" + fromDate + "','YYYY-MM-DD'),'DD-MM-YYYY')\n"
                    + "AND TO_CHAR(ACT1.TRANSACTION_DATE,'DD-MM-YYYY')  < TO_CHAR(TO_DATE('" + toDate + "','YYYY-MM-DD'),'DD-MM-YYYY')\n"
                    + "start with GL1.GL_ACCOUNT_ID = GL.GL_ACCOUNT_ID\n"
                    + "connect by prior GL1.GL_ACCOUNT_ID = GL1.PARENT_GL_ACCOUNT_ID) DR,\n"
                    + "(select nvl(sum(ATE2.Amount),0) Cr\n"
                    + "from  ACCTG_TRANS_ENTRY ATE2\n"
                    + "INNER JOIN ACCTG_TRANS ACT2\n"
                    + "ON ATE2.ACCTG_TRANS_ID = ACT2.ACCTG_TRANS_ID\n"
                    + "RIGHT OUTER JOIN GL_ACCOUNT GL2 ON ATE2.GL_ACCOUNT_ID = GL2.GL_ACCOUNT_ID\n"
                    + "Where 'Y'                                         = ACT2.IS_POSTED\n"
                    + "AND 'ACTUAL'                                    = ACT2.GL_FISCAL_TYPE_ID\n"
                    + "AND 'PERIOD_CLOSING'                           <> ACT2.ACCTG_TRANS_TYPE_ID\n"
                    + "AND 'C'                                         = ATE2.DEBIT_CREDIT_FLAG\n"
                    + "AND TO_CHAR(ACT2.TRANSACTION_DATE,'DD-MM-YYYY') >= TO_CHAR(TO_DATE('" + fromDate + "','YYYY-MM-DD'),'DD-MM-YYYY')\n"
                    + "AND TO_CHAR(ACT2.TRANSACTION_DATE,'DD-MM-YYYY')  < TO_CHAR(TO_DATE('" + toDate + "','YYYY-MM-DD'),'DD-MM-YYYY')\n"
                    + "start with GL2.GL_ACCOUNT_ID = GL.GL_ACCOUNT_ID\n"
                    + "connect by prior GL2.GL_ACCOUNT_ID = GL2.PARENT_GL_ACCOUNT_ID) CR\n"
                    + "FROM ACCTG_TRANS_ENTRY ATE\n"
                    + "INNER JOIN ACCTG_TRANS ACT\n"
                    + "ON ATE.ACCTG_TRANS_ID = ACT.ACCTG_TRANS_ID\n"
                    + "RIGHT OUTER JOIN GL_ACCOUNT GL ON ATE.GL_ACCOUNT_ID = GL.GL_ACCOUNT_ID\n"
                    + "where LEVEL =1\n"
                    + "START WITH GL.PARENT_GL_ACCOUNT_ID IS NULL\n"
                    + "CONNECT BY PRIOR GL.GL_ACCOUNT_ID = GL.PARENT_GL_ACCOUNT_ID\n"
                    + "Order by tree";

            resultSet = sqlProcessor.executeQuery(sqlStr);
            BigDecimal b = new BigDecimal(0), dr, cr;

            while (resultSet.next()) {

                Map<String, String> innerMap = new HashMap<String, String>();
                innerMap.put("GL_ACCOUNT_ID", resultSet.getString("GL_ACCOUNT_ID"));
                innerMap.put("ACCOUNT_NAME", resultSet.getString("ACCOUNT_NAME"));
                innerMap.put("PARENT_GL", resultSet.getString("PARENT_GL_ACCOUNT_ID"));
                innerMap.put("DR", resultSet.getString("DR"));
                innerMap.put("CR", resultSet.getString("CR"));
                innerMap.put("LVL", resultSet.getString("LVL"));

                dr = new BigDecimal(resultSet.getString("DR"));
                cr = new BigDecimal(resultSet.getString("CR"));
                // subtract dr with cr and assign result to b
                b = dr.subtract(cr);
                innerMap.put("BALANCE", b.toString());
                dataList.add(innerMap);

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

        return dataList;
    }

}
