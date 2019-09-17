/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.party.party;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.apache.ofbiz.base.crypto.HashCrypt;
import static org.apache.ofbiz.common.login.LoginServices.getHashType;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;
import org.apache.ofbiz.service.DispatchContext;

/**
 *
 * @author Admin
 */
public class EcaTrigger {

    public static Map<String, Object> ActEcaInsert(DispatchContext ctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = new HashMap<String, Object>();

        String userLoginId = (String) context.get("userLoginId");
        String currentPassword = (String) context.get("currentPassword");
        String password = "";
        System.out.println(">> userLoginId " + userLoginId);
       
        ResultSet resultSet = null;
        ResultSet resultSetPass = null;
        String sqlStr = null;
        String sqlStrgetPass = null;
        try {
            Delegator delegator = ctx.getDelegator();;
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            SQLProcessor sqlProcessor2 = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
                        sqlStrgetPass = "SELECT CURRENT_PASSWORD FROM USER_LOGIN WHERE USER_LOGIN_ID='" + userLoginId + "'";
            resultSetPass = sqlProcessor2.executeQuery(sqlStrgetPass);
            while (resultSetPass.next()) {
                password = resultSetPass.getString("CURRENT_PASSWORD");
                break;
            }
         
            
            sqlStr = "INSERT INTO ACT_ID_USER (ID_,PWD_) VALUES ('" + userLoginId + "','" + password + "')";
            resultSet = sqlProcessor.executeQuery(sqlStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        return result;

    }
    
    
    
    public static Map<String, Object> ActEcaupdate(DispatchContext ctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = new HashMap<String, Object>();

        String userLoginId = (String) context.get("userLoginId");
        String newPassword = (String) context.get("newPassword");
        String password = "";
        System.out.println(">> userLoginId " + userLoginId);
       
        ResultSet resultSet = null;
        ResultSet resultSetPass = null;
        String sqlStr = null;
        String sqlStrgetPass = null;
        System.out.println(">>> hello from ActEcaInsert");
        try {
            Delegator delegator = ctx.getDelegator();;
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            SQLProcessor sqlProcessor2 = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            
            sqlStrgetPass = "SELECT CURRENT_PASSWORD FROM USER_LOGIN WHERE USER_LOGIN_ID='" + userLoginId + "'";
            System.out.println(">>> query get pass >>"+sqlStrgetPass);
            resultSetPass = sqlProcessor2.executeQuery(sqlStrgetPass);
            while (resultSetPass.next()) {
                password = resultSetPass.getString("CURRENT_PASSWORD");
                break;
            }
            System.out.println(">>> after select");
            System.out.println(">> currentPassword " + password);
            
            sqlStr = "UPDATE ACT_ID_USER SET PWD_='" + password + "' where ID_='" + userLoginId + "'";
            resultSet = sqlProcessor.executeQuery(sqlStr);
            System.out.println(">>> after update");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        return result;

    }

}
