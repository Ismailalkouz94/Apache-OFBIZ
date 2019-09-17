/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.humanres.positionStructure;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;
import org.apache.ofbiz.humanres.HumanResEvents;
import org.apache.ofbiz.humanres.PayrollServices;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author DELL
 */
public class ReportingStructureTree {

    public static ArrayList< Map<String, String>> reportingStructureTree(HttpServletRequest request, HttpServletResponse response) {

        ArrayList<  Map<String, String>> dataList = new ArrayList<  Map<String, String>>();
        Map<String, String> innerMap = new HashMap<String, String>();
        PrintWriter out = null;
        ResultSet resultSet = null;
        String sqlStr = null;
        String topPos = "CEO_POS";
        String emplPositionName = "";

//        JSONObject jsonRes;
//        JSONArray jsonEmplPositionReportingStructTree = new JSONArray();
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            sqlStr = "SELECT distinct RPAD ('*', 2 * LEVEL, '*') || EMPL_POSITION_ID_REPORTING_TO as tree1,\n"
                    + "substr(sys_connect_by_path(EMPL_POSITION_ID_MANAGED_BY,'->'),3) tree,\n"
                    + "EMPL_POSITION_ID_MANAGED_BY,EMPL_POSITION_ID_REPORTING_TO,CONNECT_BY_ISLEAF leaf ,level LVL\n"
                    + "FROM EMPL_POSITION_REPORTING_STRUCT\n"
                    + "START WITH EMPL_POSITION_ID_REPORTING_TO = '" + topPos + "'\n"
                    + "CONNECT BY PRIOR  EMPL_POSITION_ID_MANAGED_BY =EMPL_POSITION_ID_REPORTING_TO\n"
                    + "order by tree";

            resultSet = sqlProcessor.executeQuery(sqlStr);

            Map<String, String> criteriaTop = new HashMap<String, String>();
            criteriaTop.put("emplPositionId", topPos);

            innerMap.put("LVL", "0");
            innerMap.put("leaf", "0");
            innerMap.put("REPORTING_TO", "");
            innerMap.put("MANAGED_BY", topPos);
            emplPositionName = PayrollServices.getEmplPositionTypeId(request, topPos);
            innerMap.put("POS_NAME", PayrollServices.getEmplPositionTypeDescription(request, emplPositionName));
            if (getPosStatus(request, response, topPos).equals("EMPL_POS_OCCUPIED")) {
                List<GenericValue> partyIdGVTop = delegator.findList("EmplPositionFulfillment", EntityCondition.makeCondition(criteriaTop), null, null, null, true);
                String partyIdTop = partyIdGVTop.get(0).get("partyId").toString();
                innerMap.put("partyId", partyIdTop);
                innerMap.put("partyName", HumanResEvents.getPartyName(request, response, partyIdTop));
            }
            dataList.add(innerMap);

            while (resultSet.next()) {
                innerMap = new HashMap<String, String>();
                String MANAGED_BY = (String) resultSet.getString("EMPL_POSITION_ID_MANAGED_BY");
//                jsonRes = new JSONObject();
                innerMap.put("LVL", resultSet.getString("LVL"));
                innerMap.put("leaf", resultSet.getString("leaf"));
                innerMap.put("REPORTING_TO", resultSet.getString("EMPL_POSITION_ID_REPORTING_TO"));
                innerMap.put("MANAGED_BY", resultSet.getString("EMPL_POSITION_ID_MANAGED_BY"));
                emplPositionName = PayrollServices.getEmplPositionTypeId(request, MANAGED_BY);
                innerMap.put("POS_NAME", PayrollServices.getEmplPositionTypeDescription(request, emplPositionName));

//                EMPL_POS_OCCUPIED
                String statusId = getPosStatus(request, response, MANAGED_BY);
                if (statusId.equals("EMPL_POS_OCCUPIED")) {
                    System.out.println("getPosStatus " + getPosStatus(request, response, MANAGED_BY));

                    // get postion from service rababaa
                    Map<String, String> criteria = new HashMap<String, String>();
                    criteria.put("emplPositionId", MANAGED_BY);
                    List<GenericValue> partyIdGV = delegator.findList("EmplPositionFulfillment", EntityCondition.makeCondition(criteria), null, null, null, true);
                    String partyId = partyIdGV.get(0).get("partyId").toString();

                    System.out.println("partyId  " + partyId);
                    System.out.println("name " + HumanResEvents.getPartyName(request, response, partyId));
                    innerMap.put("partyId", partyId);
                    innerMap.put("partyName", HumanResEvents.getPartyName(request, response, partyId));

                }

//                jsonEmplPositionReportingStructTree.add(jsonRes);
                dataList.add(innerMap);

            }
            System.out.println("** innerMap **");
            System.out.println(innerMap);

//            response.setContentType("text/html; charset=UTF-8");
//            response.setCharacterEncoding("UTF-8");
//            out = response.getWriter();
//            out.println(jsonEmplPositionReportingStructTree.toString());
//            out.flush();
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

    public static String getPosStatus(HttpServletRequest request, HttpServletResponse response, String emplPositionId) {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String posStatus = "";
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("emplPositionId", emplPositionId);
//        criteria.put("statusId", "EMPL_POS_OCCUPIED");
        try {
            List<GenericValue> emplPositionStatus = delegator.findList("EmplPosition", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (emplPositionStatus.size() > 0) {
                for (GenericValue row : emplPositionStatus) {
                    posStatus = (String) row.get("statusId");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return posStatus;
    }
}
