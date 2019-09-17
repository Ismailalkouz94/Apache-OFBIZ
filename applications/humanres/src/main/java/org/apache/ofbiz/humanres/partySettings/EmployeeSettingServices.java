/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.humanres.partySettings;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Admin
 */
public class EmployeeSettingServices {

    public static String setEmployeeSettings(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String partyId = request.getParameter("partyId");
        String annualVacation = request.getParameter("annualVacation");
        String ticketEligable = request.getParameter("ticketEligable");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        List<GenericValue> toBeStored = new LinkedList<GenericValue>();
        GenericValue item = delegator.makeValue("PartySettings");
        item.put("partyId", partyId);
        item.put("key", "ANNUAL_VACATION");
        item.put("value", annualVacation);
        item.put("partySettingType", "EMPLOYEE_SETTINGS");
        toBeStored.add(item);

        GenericValue item2 = delegator.makeValue("PartySettings");
        item2.put("partyId", partyId);
        item2.put("key", "TICKET_ELIGABLE");
        item2.put("value", ticketEligable);
        item2.put("partySettingType", "EMPLOYEE_SETTINGS");
        toBeStored.add(item2);

        try {
            delegator.storeAll(toBeStored);
            return "success";
        } catch (GenericEntityException e) {
            e.getStackTrace();
            return "error";
        }

    }

    public static String getEmployeeSettingsData(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        PrintWriter out = null;

        String partyId = request.getParameter("partyId");
        String partySettingType = request.getParameter("partySettingType");

        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("partyId", partyId);
        criteria.put("partySettingType", partySettingType);

        JSONObject jsonRes;
        JSONArray jsonGrade = new JSONArray();
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> list = delegator.findList("PartySettings", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                jsonRes.put("key", row.get("key"));
                jsonRes.put("value", row.get("value"));
                jsonGrade.add(jsonRes);
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonGrade.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(EmployeeSettingServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

}
