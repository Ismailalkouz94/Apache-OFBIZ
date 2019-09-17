/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.accounting;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;
import org.apache.ofbiz.service.DispatchContext;

/**
 *
 * @author DELL
 */
public class GLTransactionClass {

    public static String party_Id = " ";
    public static String OrganizationPartyId = "Company";
    public static String glId = " ";

    public static void getParameter(HttpServletRequest request, HttpServletResponse response) {
        party_Id = request.getParameter("partyId");
    }

    public static void setGlId(HttpServletRequest request, HttpServletResponse response) {
        glId = request.getParameter("glId");
    }

    public static String getGlId() {
        String getGl = glId;
        return getGl;
    }

    public static void getParameterOrganizationId(HttpServletRequest request, HttpServletResponse response) {
        if (OrganizationPartyId == null) {
            OrganizationPartyId = request.getParameter("organizationId");
        } else {
            OrganizationPartyId = "Company";
        }

    }

    public static void getPartyId(HttpServletRequest request, HttpServletResponse response) {
        party_Id = " ";
//        Connection con = null;
//        Statement st = null;
        ResultSet resultSet = null;

        try {
            // new 
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/OFBIZ", "ofbiz", "ofbiz");
//            if (con != null) {
//                System.out.println("connect");
//            } else {
//                System.out.println("not connect");
//            }
            String sqlStr = "SELECT PR.PARTY_ID PARTY,\n"
                    + "PG.GROUP_NAME NAME,\n"
                    + "PR.ROLE_TYPE_ID\n"
                    + "FROM PARTY_ROLE PR,\n"
                    + "PARTY_GROUP PG\n"
                    + "where PR.PARTY_ID=PG.PARTY_ID\n"
                    + "and ROLE_TYPE_ID='COST_CENTER'\n"
                    + "and PR.PARTY_ID Not IN ( select PARTY_ID from PARTY_ACCTG_PREFERENCE )";

//            st = con.createStatement();
            resultSet = sqlProcessor.executeQuery(sqlStr);
            String strSelect, strOption = "", strEndSelect, selectStr = "";
            strSelect = "<select>";
            while (resultSet.next()) {
                if (party_Id == " ") {
                    party_Id = resultSet.getString("PARTY");
                }//end if
                strOption = strOption + "<option value='" + resultSet.getString("PARTY") + "'>" + resultSet.getString("PARTY") + " - " + resultSet.getString("NAME") + "</option>";
            }//end while

            strEndSelect = "</select>";
            selectStr = strSelect + strOption + strEndSelect;

            response.getOutputStream().println(selectStr);
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public static void getPartyCostCenter(HttpServletRequest request, HttpServletResponse response) {
        String partyId = (String) request.getParameter("partyId");
        ResultSet resultSet = null;
        boolean flag = false;
        String partyIdSub = "";
        try {
//            Delegator delegator,
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr = "SELECT PR.PARTY_ID PARTY,\n"
                    + "PG.GROUP_NAME NAME,\n"
                    + "PR.ROLE_TYPE_ID\n"
                    + "FROM PARTY_ROLE PR,\n"
                    + "PARTY_GROUP PG\n"
                    + "where PR.PARTY_ID=PG.PARTY_ID\n"
                    + "and ROLE_TYPE_ID='COST_CENTER'\n"
                    + "and PR.PARTY_ID='" + partyId + "'\n"
                    + "and PR.PARTY_ID Not IN ( select PARTY_ID from PARTY_ACCTG_PREFERENCE )";

            resultSet = sqlProcessor.executeQuery(sqlStr);
            while (resultSet.next()) {
                partyIdSub = (String) resultSet.getString("PARTY");
                if (true == partyId.equals(partyIdSub)) {
                    flag = true;
                }
            }//end while

            response.getOutputStream().println(flag);
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    // call from Service XML  Delegator delegator
    public static boolean partyCostCenter(Delegator delegator, String partyId) {
        ResultSet resultSet = null;
        boolean flag = false;
        String partyIdSub = "";
        try {
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr = "SELECT PR.PARTY_ID PARTY,\n"
                    + "PG.GROUP_NAME NAME,\n"
                    + "PR.ROLE_TYPE_ID\n"
                    + "FROM PARTY_ROLE PR,\n"
                    + "PARTY_GROUP PG\n"
                    + "where PR.PARTY_ID=PG.PARTY_ID\n"
                    + "and ROLE_TYPE_ID='COST_CENTER'\n"
                    + "and PR.PARTY_ID='" + partyId + "'\n"
                    + "and PR.PARTY_ID Not IN ( select PARTY_ID from PARTY_ACCTG_PREFERENCE )";

            resultSet = sqlProcessor.executeQuery(sqlStr);
            while (resultSet.next()) {
                partyIdSub = (String) resultSet.getString("PARTY");
                if (true == partyId.equals(partyIdSub)) {
                    flag = true;
                }
            }//end while

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return flag;
    }

    public static void getPartyIdRevenue(HttpServletRequest request, HttpServletResponse response) {
        party_Id = " ";
        ResultSet resultSet = null;

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr = "SELECT PR.PARTY_ID PARTY,\n"
                    + "PG.GROUP_NAME NAME,\n"
                    + "PR.ROLE_TYPE_ID\n"
                    + "FROM PARTY_ROLE PR,\n"
                    + "PARTY_GROUP PG\n"
                    + "where PR.PARTY_ID=PG.PARTY_ID\n"
                    + "and ROLE_TYPE_ID='REVEMUE_COST_CENTER'\n"
                    + "and PR.PARTY_ID Not IN ( select PARTY_ID from PARTY_ACCTG_PREFERENCE )";

//            st = con.createStatement();
            resultSet = sqlProcessor.executeQuery(sqlStr);
            String strSelect, strOption = "", strEndSelect, selectStr = "";
            strSelect = "<select>";
            while (resultSet.next()) {
                if (party_Id == " ") {
                    party_Id = resultSet.getString("PARTY");
                }//end if
                strOption = strOption + "<option value='" + resultSet.getString("PARTY") + "'>" + resultSet.getString("PARTY") + " - " + resultSet.getString("NAME") + "</option>";
            }//end while

            strEndSelect = "</select>";
            selectStr = strSelect + strOption + strEndSelect;

            response.getOutputStream().println(selectStr);
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public static void getGlAccountIdRevenue(HttpServletRequest request, HttpServletResponse response) {

        ResultSet resultSet = null;
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr2 = "SELECT GLA.ACCOUNT_CODE,GLA.ACCOUNT_NAME,GLA.GL_ACCOUNT_ID,GLA.GL_ACCOUNT_TYPE_ID\n"
                    + "FROM GL_ACCOUNT_ORGANIZATION GLAO\n"
                    + "INNER JOIN GL_ACCOUNT GLA\n"
                    + "ON GLAO.GL_ACCOUNT_ID         = GLA.GL_ACCOUNT_ID\n"
                    + "WHERE GLA.GL_ACCOUNT_CLASS_ID  IN\n"
                    + "(SELECT GL_ACCOUNT_CLASS_ID\n"
                    + "FROM GL_ACCOUNT_CLASS\n"
                    + "START WITH GL_ACCOUNT_CLASS_ID       = 'REVENUE'\n"
                    + "CONNECT BY prior GL_ACCOUNT_CLASS_ID = PARENT_CLASS_ID)\n"
                    + "AND GLAO.ORGANIZATION_PARTY_ID='" + OrganizationPartyId + "'\n"
                    + "ORDER BY GLA.ACCOUNT_CODE";

            resultSet = sqlProcessor.executeQuery(sqlStr2);
            String strSelect, strOption = "", strEndSelect, selectStr2 = "";
            strSelect = "<select>";
            while (resultSet.next()) {
                strOption = strOption + "<option value='" + resultSet.getString("GL_ACCOUNT_ID") + "'>" + resultSet.getString("ACCOUNT_CODE") + " - " + resultSet.getString("ACCOUNT_NAME") + " [" + resultSet.getString("GL_ACCOUNT_ID") + "]</option>";
            }
            strEndSelect = "</select>";
            selectStr2 = strSelect + strOption + strEndSelect;

            response.getOutputStream().println(selectStr2);
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public static void getGlAccountId(HttpServletRequest request, HttpServletResponse response) {

        ResultSet resultSet = null;
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            //cost center
            String sqlStr2 = "SELECT GLA.ACCOUNT_CODE,GLA.ACCOUNT_NAME,GLA.GL_ACCOUNT_ID,GLA.GL_ACCOUNT_TYPE_ID\n"
                    + "FROM GL_ACCOUNT_ORGANIZATION GLAO\n"
                    + "INNER JOIN GL_ACCOUNT GLA\n"
                    + "ON GLAO.GL_ACCOUNT_ID         = GLA.GL_ACCOUNT_ID\n"
                    + "WHERE GLA.GL_ACCOUNT_CLASS_ID  IN\n"
                    + "(SELECT GL_ACCOUNT_CLASS_ID\n"
                    + "FROM GL_ACCOUNT_CLASS\n"
                    + "START WITH GL_ACCOUNT_CLASS_ID       = 'EXPENSE'\n"
                    + "CONNECT BY prior GL_ACCOUNT_CLASS_ID = PARENT_CLASS_ID)\n"
                    + "AND GLAO.ORGANIZATION_PARTY_ID='" + OrganizationPartyId + "'\n"
                    + "ORDER BY GLA.ACCOUNT_CODE";

            resultSet = sqlProcessor.executeQuery(sqlStr2);
            String strSelect, strOption = "", strEndSelect, selectStr2 = "";
            strSelect = "<select>";
            while (resultSet.next()) {
                strOption = strOption + "<option value='" + resultSet.getString("GL_ACCOUNT_ID") + "'>" + resultSet.getString("ACCOUNT_CODE") + " - " + resultSet.getString("ACCOUNT_NAME") + " [" + resultSet.getString("GL_ACCOUNT_ID") + "]</option>";
            }
            strEndSelect = "</select>";
            selectStr2 = strSelect + strOption + strEndSelect;

            response.getOutputStream().println(selectStr2);
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    // call from Service XML  Delegator delegator
    public static boolean getGlAccountId(Delegator delegator, String glAccountId) {

        ResultSet resultSet = null;
        String glAccountIdSub = "";
        boolean flag = false;
        try {
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            //cost center
            String sqlStr2 = "SELECT GLA.ACCOUNT_CODE,GLA.ACCOUNT_NAME,GLA.GL_ACCOUNT_ID,GLA.GL_ACCOUNT_TYPE_ID\n"
                    + "FROM GL_ACCOUNT_ORGANIZATION GLAO\n"
                    + "INNER JOIN GL_ACCOUNT GLA\n"
                    + "ON GLAO.GL_ACCOUNT_ID         = GLA.GL_ACCOUNT_ID\n"
                    + "WHERE GLA.GL_ACCOUNT_CLASS_ID  IN\n"
                    + "(SELECT GL_ACCOUNT_CLASS_ID\n"
                    + "FROM GL_ACCOUNT_CLASS\n"
                    + "START WITH GL_ACCOUNT_CLASS_ID       = 'EXPENSE'\n"
                    + "CONNECT BY prior GL_ACCOUNT_CLASS_ID = PARENT_CLASS_ID)\n"
                    + "AND GLAO.ORGANIZATION_PARTY_ID='" + OrganizationPartyId + "'\n"
                    + "AND GLAO.GL_ACCOUNT_ID='" + glAccountId + "'\n"
                    + "ORDER BY GLA.ACCOUNT_CODE";

            resultSet = sqlProcessor.executeQuery(sqlStr2);

            while (resultSet.next()) {
                glAccountIdSub = (String) resultSet.getString("GL_ACCOUNT_ID");
                if (true == glAccountId.equals(glAccountIdSub)) {
                    flag = true;
                }
            }//end while

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return flag;
    }

    public static void getPartyIdEmp(HttpServletRequest request, HttpServletResponse response) {
        party_Id = " ";
        ResultSet resultSet = null;
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr = "select PR.PARTY_ID PARTY,PE.FIRST_NAME FIRST,PE.LAST_NAME LAST,PR.ROLE_TYPE_ID\n"
                    + "from PARTY_ROLE PR,PERSON PE\n"
                    + "where PR.PARTY_ID= PE.PARTY_ID\n"
                    + "AND ROLE_TYPE_ID='EMPLOYEE'";

            resultSet = sqlProcessor.executeQuery(sqlStr);
            String strSelect, strOption = "", strEndSelect, selectStr = "";
            strSelect = "<select>";
            while (resultSet.next()) {
                if (party_Id == " ") {
                    party_Id = resultSet.getString("PARTY");
                }//end if
                strOption = strOption + "<option value='" + resultSet.getString("PARTY") + "'>" + resultSet.getString("PARTY") + " - " + resultSet.getString("FIRST") + " " + resultSet.getString("LAST") + "</option>";
            }
            strEndSelect = "</select>";
            selectStr = strSelect + strOption + strEndSelect;

            response.getOutputStream().println(selectStr);
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public static void getGlAccountEmp(HttpServletRequest request, HttpServletResponse response) {
        ResultSet resultSet = null;
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr2 = "SELECT GLA.ACCOUNT_CODE,GLA.ACCOUNT_NAME,GLA.GL_ACCOUNT_ID,GLA.GL_ACCOUNT_TYPE_ID\n"
                    + "FROM GL_ACCOUNT GLA\n"
                    + "WHERE GLA.GL_ACCOUNT_ID IN\n"
                    + "(nvl((SELECT GL_ACCOUNT_ID\n"
                    + "FROM PARTY_GL_ACCOUNT\n"
                    + "WHERE ROLE_TYPE_ID    ='BILL_FROM_VENDOR'\n"
                    + "AND GL_ACCOUNT_TYPE_ID='AP_EMPLOYEES'\n"
                    + "AND PARTY_ID          ='" + party_Id + "'), (select GL_ACCOUNT_ID from GL_ACCOUNT_TYPE_DEFAULT where GL_ACCOUNT_TYPE_ID='AP_EMPLOYEES'))\n"
                    + ")\n"
                    + "ORDER BY GLA.ACCOUNT_CODE";

            resultSet = sqlProcessor.executeQuery(sqlStr2);
            String strSelect, strOption = "", strEndSelect, selectStr = "";
            strSelect = "<select>";
            while (resultSet.next()) {
                strOption = strOption + "<option value='" + resultSet.getString("GL_ACCOUNT_ID") + "'>" + resultSet.getString("ACCOUNT_CODE") + " - " + resultSet.getString("ACCOUNT_NAME") + " [" + resultSet.getString("GL_ACCOUNT_ID") + "]</option>";
            }
            strEndSelect = "</select>";
            selectStr = strSelect + strOption + strEndSelect;

            response.getOutputStream().println(selectStr);
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public static void getPartyIdVendor(HttpServletRequest request, HttpServletResponse response) {
        party_Id = " ";
        ResultSet resultSet = null;
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr = "Select P.PARTY_ID PARTY_ID, (FIRST_NAME ||' '|| LAST_NAME) VENDOR_NAME\n"
                    + "From PERSON P,PARTY_ROLE PR\n"
                    + "Where P.PARTY_ID = PR.PARTY_ID\n"
                    + "And PR.ROLE_TYPE_ID = 'BILL_FROM_VENDOR'\n"
                    + "And P.PARTY_ID Not In (Select PARTY_ID from vendor)\n"
                    + "UNION\n"
                    + "SELECT PARTY_ID,MANIFEST_COMPANY_NAME VENDOR_NAME FROM VENDOR";

            resultSet = sqlProcessor.executeQuery(sqlStr);
            String strSelect, strOption = "", strEndSelect, selectStr = "";
            strSelect = "<select>";
            while (resultSet.next()) {
                if (party_Id == " ") {
                    party_Id = resultSet.getString("PARTY_ID");
                }//end if
                strOption = strOption + "<option value='" + resultSet.getString("PARTY_ID") + "'>" + resultSet.getString("PARTY_ID") + " " + resultSet.getString("VENDOR_NAME") + "</option>";
            }
            strEndSelect = "</select>";
            selectStr = strSelect + strOption + strEndSelect;

            response.getOutputStream().println(selectStr);
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public static void getGlAccountVendor(HttpServletRequest request, HttpServletResponse response) {

        ResultSet resultSet = null;
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr2 = "SELECT GLA.ACCOUNT_CODE,GLA.ACCOUNT_NAME,GLA.GL_ACCOUNT_ID,GLA.GL_ACCOUNT_TYPE_ID\n"
                    + "FROM GL_ACCOUNT GLA\n"
                    + "WHERE GLA.GL_ACCOUNT_ID IN\n"
                    + "(nvl((SELECT GL_ACCOUNT_ID\n"
                    + "FROM PARTY_GL_ACCOUNT\n"
                    + "WHERE ROLE_TYPE_ID    ='BILL_FROM_VENDOR'\n"
                    + "AND GL_ACCOUNT_TYPE_ID='ACCOUNTS_PAYABLE'\n"
                    + "AND PARTY_ID          ='" + party_Id + "'), (select GL_ACCOUNT_ID from GL_ACCOUNT_TYPE_DEFAULT where GL_ACCOUNT_TYPE_ID='ACCOUNTS_PAYABLE'))\n"
                    + ")\n"
                    + "ORDER BY GLA.ACCOUNT_CODE";

            resultSet = sqlProcessor.executeQuery(sqlStr2);
            String strSelect, strOption = "", strEndSelect, selectStr = "";
            strSelect = "<select>";
            while (resultSet.next()) {
                strOption = strOption + "<option value='" + resultSet.getString("GL_ACCOUNT_ID") + "'>" + resultSet.getString("ACCOUNT_CODE") + " - " + resultSet.getString("ACCOUNT_NAME") + " [" + resultSet.getString("GL_ACCOUNT_ID") + "]</option>";
            }
            strEndSelect = "</select>";
            selectStr = strSelect + strOption + strEndSelect;

            response.getOutputStream().println(selectStr);
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public static void getGlAccountCredit(HttpServletRequest request, HttpServletResponse response) {

        ResultSet resultSet = null;

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr2 = "SELECT GLA.ACCOUNT_CODE,GLA.ACCOUNT_NAME,GLA.GL_ACCOUNT_ID,GLA.GL_ACCOUNT_TYPE_ID\n"
                    + "FROM GL_ACCOUNT_ORGANIZATION GLAO\n"
                    + "INNER JOIN GL_ACCOUNT GLA\n"
                    + "ON GLAO.GL_ACCOUNT_ID         = GLA.GL_ACCOUNT_ID\n"
                    + "AND GLAO.ORGANIZATION_PARTY_ID='" + OrganizationPartyId + "'\n"
                    + "ORDER BY GLA.ACCOUNT_CODE";

            resultSet = sqlProcessor.executeQuery(sqlStr2);
            String s21, s22 = "", s23, selectStr2 = "";
            s21 = "<select>";
            while (resultSet.next()) {
                s22 = s22 + "<option value='" + resultSet.getString("GL_ACCOUNT_ID") + "'>" + resultSet.getString("ACCOUNT_CODE") + " - " + resultSet.getString("ACCOUNT_NAME") + " [" + resultSet.getString("GL_ACCOUNT_ID") + "]</option>";
            }
            s23 = "</select>";
            selectStr2 = s21 + s22 + s23;

            response.getOutputStream().println(selectStr2);
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public static void getOrganizationId(HttpServletRequest request, HttpServletResponse response) {
        OrganizationPartyId = " ";
        ResultSet resultSet = null;

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr2 = "SELECT PAP.PARTY_ID,PG.GROUP_NAME\n"
                    + "FROM PARTY_ACCTG_PREFERENCE PAP,PARTY_GROUP PG\n"
                    + "where PAP.PARTY_ID=PG.PARTY_ID";

            resultSet = sqlProcessor.executeQuery(sqlStr2);
            String strSelect, strOption = "", strEndSelect, selectStr = "";
            strSelect = "<select>";
            while (resultSet.next()) {
                if (OrganizationPartyId == " ") {
                    OrganizationPartyId = resultSet.getString("PARTY_ID");
                }
                strOption = strOption + "<option value='" + resultSet.getString("PARTY_ID") + "'>" + resultSet.getString("PARTY_ID") + " - " + resultSet.getString("GROUP_NAME") + "</option>";
            }
            strEndSelect = "</select>";
            selectStr = strSelect + strOption + strEndSelect;

            response.getOutputStream().println(selectStr);
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public static void getOrganizationPartyName(HttpServletRequest request, HttpServletResponse response) {

        ResultSet resultSet = null;

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr2 = "SELECT PAP.PARTY_ID,PG.GROUP_NAME NAME\n"
                    + "FROM PARTY_ACCTG_PREFERENCE PAP,PARTY_GROUP PG\n"
                    + "where PAP.PARTY_ID=PG.PARTY_ID\n"
                    + "and PAP.PARTY_ID='" + OrganizationPartyId + "'";

            resultSet = sqlProcessor.executeQuery(sqlStr2);
            String organizationPartyName = null;
            while (resultSet.next()) {
                organizationPartyName = resultSet.getString("NAME");
            }

            response.getOutputStream().println(organizationPartyName);
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }

        }
    }

    public static void glAccountPartyId(HttpServletRequest request, HttpServletResponse response) {
        party_Id = " ";
        ResultSet resultSet = null;

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr = "SELECT PR.PARTY_ID PARTY,\n"
                    + "PG.GROUP_NAME NAME,\n"
                    + "PR.ROLE_TYPE_ID\n"
                    + "FROM PARTY_ROLE PR,\n"
                    + "PARTY_GROUP PG,\n"
                    + "PARTY_ACCTG_PREFERENCE PAP\n"
                    + "where PR.PARTY_ID=PAP.PARTY_ID\n"
                    + "and  PR.PARTY_ID=PG.PARTY_ID\n"
                    + "and  PG.PARTY_ID=PAP.PARTY_ID\n"
                    + "and ROLE_TYPE_ID='INTERNAL_ORGANIZATIO'";

            resultSet = sqlProcessor.executeQuery(sqlStr);
            String strSelect, strOption = "", strEndSelect, selectStr = "";
            strSelect = "<select>";
            while (resultSet.next()) {
                if (party_Id == " ") {
                    party_Id = resultSet.getString("PARTY");
                }//end if
                strOption = strOption + "<option value='" + resultSet.getString("PARTY") + "'>" + resultSet.getString("PARTY") + " - " + resultSet.getString("NAME") + "</option>";
            }//end while

            strEndSelect = "</select>";
            selectStr = strSelect + strOption + strEndSelect;

            response.getOutputStream().println(selectStr);
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    // call from Service XML  Delegator delegator
    public static boolean glAccountPartyId(Delegator delegator, String partyId) {
        ResultSet resultSet = null;
        boolean flag = false;
        String partyIdSub = "";
        try {
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr = "SELECT PR.PARTY_ID PARTY,\n"
                    + "PG.GROUP_NAME NAME,\n"
                    + "PR.ROLE_TYPE_ID\n"
                    + "FROM PARTY_ROLE PR,\n"
                    + "PARTY_GROUP PG,\n"
                    + "PARTY_ACCTG_PREFERENCE PAP\n"
                    + "where PR.PARTY_ID=PAP.PARTY_ID\n"
                    + "and  PR.PARTY_ID=PG.PARTY_ID\n"
                    + "and  PG.PARTY_ID=PAP.PARTY_ID\n"
                    + "and  PG.PARTY_ID='" + partyId + "'\n"
                    + "and ROLE_TYPE_ID='INTERNAL_ORGANIZATIO'";

            resultSet = sqlProcessor.executeQuery(sqlStr);
            while (resultSet.next()) {
                partyIdSub = (String) resultSet.getString("PARTY");
                if (true == partyId.equals(partyIdSub)) {
                    flag = true;
                }
            }//end while

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return flag;
    }

    public static void getGlAccount(HttpServletRequest request, HttpServletResponse response) {
        ResultSet resultSet = null;

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr2 = "SELECT GLA.ACCOUNT_CODE,\n"
                    + "GLA.ACCOUNT_NAME,\n"
                    + "GLA.GL_ACCOUNT_ID,\n"
                    + "GLA.GL_ACCOUNT_TYPE_ID\n"
                    + "FROM GL_ACCOUNT_ORGANIZATION GLAO\n"
                    + "INNER JOIN GL_ACCOUNT GLA\n"
                    + "ON GLAO.GL_ACCOUNT_ID         = GLA.GL_ACCOUNT_ID\n"
                    + "AND GLAO.ORGANIZATION_PARTY_ID='" + OrganizationPartyId + "'\n"
                    + "AND GL_ACCOUNT_CLASS_ID NOT   IN (SELECT GL_ACCOUNT_CLASS_ID\n"
                    + "FROM GL_ACCOUNT_CLASS\n"
                    + "START WITH GL_ACCOUNT_CLASS_ID       = 'EXPENSE'\n"
                    + "CONNECT BY prior GL_ACCOUNT_CLASS_ID = PARENT_CLASS_ID)\n"
                    + "AND GLA.GL_ACCOUNT_ID NOT   IN (select GL_ACCOUNT_ID from GL_ACCOUNT_TYPE_DEFAULT where GL_ACCOUNT_TYPE_ID IN ('ACCOUNTS_PAYABLE' ,'ACCOUNTS_RECEIVABLE','AP_EMPLOYEES'))\n"
                    + "ORDER BY GLA.ACCOUNT_CODE";

            resultSet = sqlProcessor.executeQuery(sqlStr2);
            String s21, s22 = "", s23, selectStr = "";
            s21 = "<select>";
            while (resultSet.next()) {
                s22 = s22 + "<option value='" + resultSet.getString("GL_ACCOUNT_ID") + "'>" + resultSet.getString("ACCOUNT_CODE") + " - " + resultSet.getString("ACCOUNT_NAME") + " [" + resultSet.getString("GL_ACCOUNT_ID") + "]</option>";
            }
            s23 = "</select>";
            selectStr = s21 + s22 + s23;

            response.getOutputStream().println(selectStr);
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    // call from Service XML  Delegator delegator
    public static boolean getGlAccount(Delegator delegator, String glAccountId) {
        ResultSet resultSet = null;
        boolean flag = false;
        String glAccountIdSub = "";
        try {
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr = "SELECT GLA.ACCOUNT_CODE,\n"
                    + "GLA.ACCOUNT_NAME,\n"
                    + "GLA.GL_ACCOUNT_ID,\n"
                    + "GLA.GL_ACCOUNT_TYPE_ID\n"
                    + "FROM GL_ACCOUNT_ORGANIZATION GLAO\n"
                    + "INNER JOIN GL_ACCOUNT GLA\n"
                    + "ON GLAO.GL_ACCOUNT_ID         = GLA.GL_ACCOUNT_ID\n"
                    + "AND GLAO.ORGANIZATION_PARTY_ID='" + OrganizationPartyId + "'\n"
                    + "AND GL_ACCOUNT_CLASS_ID NOT   IN (SELECT GL_ACCOUNT_CLASS_ID\n"
                    + "FROM GL_ACCOUNT_CLASS\n"
                    + "START WITH GL_ACCOUNT_CLASS_ID       = 'EXPENSE'\n"
                    + "CONNECT BY prior GL_ACCOUNT_CLASS_ID = PARENT_CLASS_ID)\n"
                    + "AND GLA.GL_ACCOUNT_ID NOT   IN (select GL_ACCOUNT_ID from GL_ACCOUNT_TYPE_DEFAULT where GL_ACCOUNT_TYPE_ID IN ('ACCOUNTS_PAYABLE' ,'ACCOUNTS_RECEIVABLE','AP_EMPLOYEES'))\n"
                    + "AND GLA.GL_ACCOUNT_ID='" + glAccountId + "'\n"
                    + "ORDER BY GLA.ACCOUNT_CODE";

            resultSet = sqlProcessor.executeQuery(sqlStr);
            while (resultSet.next()) {
                glAccountIdSub = (String) resultSet.getString("GL_ACCOUNT_ID");
                if (true == glAccountId.equals(glAccountIdSub)) {
                    flag = true;
                }
            }//end while

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return flag;
    }

    public static void getPartyIdCustomer(HttpServletRequest request, HttpServletResponse response) {
        party_Id = " ";
        ResultSet resultSet = null;
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr = "select PR.PARTY_ID PARTY,PE.FIRST_NAME FIRST,PE.LAST_NAME LAST,PR.ROLE_TYPE_ID\n"
                    + "from PARTY_ROLE PR,PERSON PE\n"
                    + "where PR.PARTY_ID= PE.PARTY_ID\n"
                    + "AND ROLE_TYPE_ID='CUSTOMER'";

            resultSet = sqlProcessor.executeQuery(sqlStr);
            String strSelect, strOption = "", strEndSelect, selectStr = "";
            strSelect = "<select>";
            while (resultSet.next()) {
                if (party_Id == " ") {
                    party_Id = resultSet.getString("PARTY");
                }//end if
                strOption = strOption + "<option value='" + resultSet.getString("PARTY") + "'>" + resultSet.getString("PARTY") + " - " + resultSet.getString("FIRST") + " " + resultSet.getString("LAST") + "</option>";
            }
            strEndSelect = "</select>";
            selectStr = strSelect + strOption + strEndSelect;

            response.getOutputStream().println(selectStr);
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public static List<String> getGlAccountIdExpense(HttpServletRequest request, HttpServletResponse response) {

        ResultSet resultSet = null;
        List<String> listGl = new ArrayList<String>();
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            //cost center
            String sqlStr2 = "SELECT GLA.ACCOUNT_CODE,GLA.ACCOUNT_NAME,GLA.GL_ACCOUNT_ID,GLA.GL_ACCOUNT_TYPE_ID\n"
                    + "FROM GL_ACCOUNT_ORGANIZATION GLAO\n"
                    + "INNER JOIN GL_ACCOUNT GLA\n"
                    + "ON GLAO.GL_ACCOUNT_ID         = GLA.GL_ACCOUNT_ID\n"
                    + "WHERE GLA.GL_ACCOUNT_CLASS_ID  IN\n"
                    + "(SELECT GL_ACCOUNT_CLASS_ID\n"
                    + "FROM GL_ACCOUNT_CLASS\n"
                    + "START WITH GL_ACCOUNT_CLASS_ID       = 'EXPENSE'\n"
                    + "CONNECT BY prior GL_ACCOUNT_CLASS_ID = PARENT_CLASS_ID)\n"
                    + "AND GLAO.ORGANIZATION_PARTY_ID='" + OrganizationPartyId + "'\n"
                    + "ORDER BY GLA.ACCOUNT_CODE";

            resultSet = sqlProcessor.executeQuery(sqlStr2);
            while (resultSet.next()) {
                String gl = resultSet.getString("GL_ACCOUNT_ID").toString();
                listGl.add(gl);
            }
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return listGl;
    }
}
