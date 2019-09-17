/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.accounting.test;

//import com.itextpdf.text.Document;
import org.apache.ofbiz.accounting.report.*;
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
import org.apache.ofbiz.entity.DelegatorFactory;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;

//import org.apache.ofbiz.accounting.report.ObjRecord;
/**
 *
 * @author DELL
 */
public class TestTenant {

    public static String FromDate = "";
    public static String ToDate = "";

    public static void getReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("** getReport **");
        Connection con = null;
        Statement st = null;
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;
//        response.setContentType("application/pdf");
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            String tenantDelegatorName = delegator.getDelegatorBaseName() + "#1001";
            Delegator delegator1 = DelegatorFactory.getDelegator(tenantDelegatorName);

            SQLProcessor sqlProcessor = new SQLProcessor(delegator1, delegator1.getGroupHelperInfo("org.apache.ofbiz"));

            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/OFBIZ_1001", "ofbiz", "ofbiz");
            System.out.println("con " + con);
            try {

                String sqlStr = "select TESTTABLEID,NAME from TEST_TABLE";
                System.out.println("sqlStr " + sqlStr);
                st = con.createStatement();
                resultSet = st.executeQuery(sqlStr);
                System.out.println("resultSet " + resultSet);
                while (resultSet.next()) {
                    System.out.println("TESTTABLEID " + resultSet.getString("TESTTABLEID"));
                    System.out.println("NAME " + resultSet.getString("NAME"));
                }

                resultSet1 = sqlProcessor.executeQuery(sqlStr);
                System.out.println("resultSet1 **");
                System.out.println(resultSet1);
                while (resultSet1.next()) {
                    System.out.println("TESTTABLEID " + resultSet1.getString("TESTTABLEID"));
                    System.out.println("NAME " + resultSet1.getString("NAME"));
                }

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
                    Logger.getLogger(TestTenant.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
