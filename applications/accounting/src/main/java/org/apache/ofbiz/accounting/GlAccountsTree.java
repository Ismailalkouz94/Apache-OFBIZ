/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.accounting;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;

/**
 *
 * @author DELL
 */
public class GlAccountsTree {

    public static ArrayList< Map<String, String>> glAccountsTree(HttpServletRequest request, HttpServletResponse response) {

        ArrayList<  Map<String, String>> dataList = new ArrayList<  Map<String, String>>();

        ResultSet resultSet = null;
        String sqlStr = null;

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            sqlStr = "select substr(sys_connect_by_path(GL_ACCOUNT_ID,'->'),3) tree,level-1 LVL,CONNECT_BY_ISLEAF leaf,PARENT_GL_ACCOUNT_ID,GL_ACCOUNT_ID, ACCOUNT_NAME\n"
                    + "from GL_ACCOUNT\n"
                    + "start with PARENT_GL_ACCOUNT_ID is null\n"
                    + "connect by prior GL_ACCOUNT_ID = PARENT_GL_ACCOUNT_ID\n"
                    + "order by tree";

            resultSet = sqlProcessor.executeQuery(sqlStr);

            while (resultSet.next()) {

                Map<String, String> innerMap = new HashMap<String, String>();
                innerMap.put("LVL", resultSet.getString("LVL"));
                innerMap.put("leaf", resultSet.getString("leaf"));
                innerMap.put("PARENT_GL_ACCOUNT_ID", resultSet.getString("PARENT_GL_ACCOUNT_ID"));
                innerMap.put("GL_ACCOUNT_ID", resultSet.getString("GL_ACCOUNT_ID"));
                innerMap.put("ACCOUNT_NAME", resultSet.getString("ACCOUNT_NAME"));

                dataList.add(innerMap);

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
}
