/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.webapp.control;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ofbiz.base.crypto.HashCrypt;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.common.activitiServices.WorkFlowServices;
import static org.apache.ofbiz.common.login.LoginServices.getHashType;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.condition.EntityCondition;

/**
 *
 * @author Rbab3a
 */
public class ChangePassword {

    public final static String module = ChangePassword.class.getName();
    public static final String securityProperties = "security.properties";
    public static final String resourceError = "HumanResErrorUiLabels";
    private static final String keyValue = UtilProperties.getPropertyValue(securityProperties, "login.secret_key_string");

    public static String changePassword(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String userLoginId = null;

        if(request.getParameterMap().containsKey("userLoginId")){
            userLoginId = request.getParameter("userLoginId");
        }else{
             userLoginId = (String) userLogin.getString("userLoginId");
        }

        String newPassword = request.getParameter("newPassword");
        String newPasswordVerify = request.getParameter("newPasswordVerify");

        String password = "";

        if (newPassword.length() < 5) {
            Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "");
            String errMsg = UtilProperties.getMessage(resourceError, "passwordmustbe5chracters", messageMap, UtilHttp.getLocale(request));

            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";

        } else if (newPassword.equals(newPasswordVerify)) {

            String newPasswordEnc = HashCrypt.cryptUTF8(getHashType(), null, newPasswordVerify);
            System.out.println("newPasswordEnc " + newPasswordEnc);
            System.out.println(">> userLoginId " + userLoginId);

            ResultSet resultSet = null;
            ResultSet resultSetPass = null;
            ResultSet resultGetPass = null;
            String sqlStr = null;
            String sqlGetPass = null;
            String sqlSetPass = null;
            System.out.println(">>> hello from ActEcaInsert");
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("userLoginId", userLoginId);

            try {
                List<GenericValue> list = delegator.findList("UserLogin", EntityCondition.makeCondition(criteria), null, null, null, true);
                GenericValue gvValue = (GenericValue) list.get(0).clone();
                gvValue.set("currentPassword", newPasswordEnc);
                gvValue.store();

            } catch (GenericEntityException ex) {
                Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                password = WorkFlowServices.getUserPassword(request, userLoginId);
                SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

                sqlStr = "UPDATE ACT_ID_USER SET PWD_='" + password + "' where ID_='" + userLoginId + "'";
                resultSet = sqlProcessor.executeQuery(sqlStr);
                System.out.println(">>> after update" + sqlStr);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            Map<String, String> messageMap = UtilMisc.toMap("successMessage", "");
            String successMsg = UtilProperties.getMessage(resourceError, "PasswordChangedSuccessfully", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_EVENT_MESSAGE_", successMsg);
            return "success";

        } else {
            Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "");
            String errMsg = UtilProperties.getMessage(resourceError, "YourPasswordDosentMatch", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";

        }

    }

}
