package org.apache.ofbiz.accounting.payment;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class MyPaymentService {
    
    public static String getMyPaymentData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String partyId = request.getParameter("partyId");

        List<EntityExpr> exprs = UtilMisc.toList(
                EntityCondition.makeCondition("partyIdTo", EntityOperator.EQUALS,partyId ),
                EntityCondition.makeCondition("partyIdFrom", EntityOperator.EQUALS, partyId));
        EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.OR);
        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {
            List<GenericValue> list = delegator.findList("PaymentAndTypeAndCreditCard", condition, null, null, null, true);

            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    jsonRes = new JSONObject();
                    jsonRes.put("paymentId",(String) row.get("paymentId"));
                    jsonRes.put("paymentTypeId",(String) row.get("paymentTypeId"));
                    jsonRes.put("statusId",(String) row.get("statusId"));
                    jsonRes.put("comments",(String) row.get("comments"));
                    jsonRes.put("partyIdFrom",(String) row.get("partyIdFrom"));
                    jsonRes.put("partyIdTo",(String) row.get("partyIdTo"));
                    jsonRes.put("effectiveDate",row.get("effectiveDate").toString());
                    jsonRes.put("amount",row.get("amount").toString());
                    jsonRes.put("outstandingAmount",row.get("amount").toString());
                    jsonData.add(jsonRes);
                }
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(MyPaymentService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }
    
}
