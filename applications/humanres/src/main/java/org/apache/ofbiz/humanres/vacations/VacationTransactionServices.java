package org.apache.ofbiz.humanres.vacations;

import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VacationTransactionServices {

    public static String getPartyVacationTransactionData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String partyId = request.getParameter("partyId");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyId", partyId);

        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {

            List<GenericValue> list = delegator.findList("PartyVacationTransaction", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    jsonRes = new JSONObject();
                    jsonRes.put("partyVacationTransactionId", row.get("partyVacationTransactionId"));
                    jsonRes.put("partyId", row.get("partyId"));
                    jsonRes.put("noDays", row.get("noDays"));
                    jsonRes.put("action", row.get("action"));

                    if (row.get("transDate") == null) {
                        jsonRes.put("transDate", row.get("transDate"));
                    } else {
                        jsonRes.put("transDate", row.get("transDate").toString());
                    }

                    jsonRes.put("description", row.get("description"));
                    jsonData.add(jsonRes);
//                }
                }
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(VacationTransactionServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

}
