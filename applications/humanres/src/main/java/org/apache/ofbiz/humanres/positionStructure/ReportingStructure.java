/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.humanres.positionStructure;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.humanres.PayrollServices;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DELL
 */
public class ReportingStructure {

    public static String getEmplReportStruct(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        System.out.println("** getEmplReportStruct **");
        PrintWriter out = null;
        Map<String, String> criteria = new HashMap<String, String>();
        String emplPositionId = request.getParameter("emplPositionId");
        criteria.put("emplPositionIdManagedBy", emplPositionId);
        String emplPositionIdReportingTo = "", emplPositionIdManagedBy = "";
        JSONObject jsonRes;
        JSONArray jsonEmplPositionReportingStruct = new JSONArray();

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> emplPositionReportingStructs = delegator.findList("EmplPositionReportingStruct", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue structRow : emplPositionReportingStructs) {
                
                String fromDate = "";
                String thruDate = "";
                if (structRow.get("fromDate") != null) {
                    fromDate = (structRow.get("fromDate").toString()).substring(0, 10);
                }
                if (structRow.get("thruDate") != null) {
                    thruDate = (structRow.get("thruDate").toString()).substring(0, 10);
                }
                
                jsonRes = new JSONObject();
                jsonRes.put("emplPositionIdReportingTo", structRow.get("emplPositionIdReportingTo"));
                jsonRes.put("emplPositionIdManagedBy", structRow.get("emplPositionIdManagedBy"));
                jsonRes.put("fromDate", fromDate);
                jsonRes.put("thruDate", thruDate);
                jsonRes.put("comments", structRow.get("comments"));
                jsonRes.put("primaryFlag", structRow.get("primaryFlag"));
                emplPositionIdReportingTo = PayrollServices.getEmplPositionTypeId(request, (String) structRow.get("emplPositionIdReportingTo"));
                jsonRes.put("emplPositionIdReportingToDescription", PayrollServices.getEmplPositionTypeDescription(request, emplPositionIdReportingTo));
                emplPositionIdManagedBy = PayrollServices.getEmplPositionTypeId(request, (String) structRow.get("emplPositionIdManagedBy"));
                jsonRes.put("emplPositionIdManagedByDescription", PayrollServices.getEmplPositionTypeDescription(request, emplPositionIdManagedBy));

                jsonEmplPositionReportingStruct.add(jsonRes);
            }
            System.out.println("** jsonEmplPositionReportingStruct **");
            System.out.println(jsonEmplPositionReportingStruct);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonEmplPositionReportingStruct.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
