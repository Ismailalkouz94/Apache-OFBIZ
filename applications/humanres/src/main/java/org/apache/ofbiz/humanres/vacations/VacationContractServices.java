package org.apache.ofbiz.humanres.vacations;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import org.apache.ofbiz.humanres.PayrollServices;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.ofbiz.humanres.PayrollServices.getMaxSequence_PositionGradation;

public class VacationContractServices {

    public final static String module = VacationContractServices.class.getName();
    public static final String resourceError = "HumanResErrorUiLabels";

    public static String getPartyVacationContractData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String partyId = request.getParameter("partyId");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyId", partyId);

        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {

            List<GenericValue> list = delegator.findList("PartyVacationContract", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    jsonRes = new JSONObject();
                    jsonRes.put("partyVacationContractId", row.get("partyVacationContractId"));
                    jsonRes.put("partyId", row.get("partyId"));

                    if (row.get("fromDate") == null) {
                        jsonRes.put("fromDate", row.get("fromDate"));
                    } else {
                        jsonRes.put("fromDate", row.get("fromDate").toString());
                    }

                    if (row.get("toDate") == null) {
                        jsonRes.put("toDate", row.get("toDate"));
                    } else {
                        jsonRes.put("toDate", row.get("toDate").toString());
                    }

                    jsonRes.put("value", row.get("value"));
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
            Logger.getLogger(VacationContractServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static Map<String, Object> updatePartyVacationContract_EndDate(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        boolean beganTransaction = false;
        java.sql.Date fromDate = (java.sql.Date) context.get("fromDate");
        System.out.println("fromDate " + fromDate);

        String partyId = (String) context.get("partyId");
        System.out.println("partyId " + partyId);

        Map<String, String> creiteria = new HashMap<String, String>();

        try {
            beganTransaction = TransactionUtil.begin();
            creiteria.put("partyId",partyId);

            List<String> listOrder = new ArrayList<>();
            listOrder.add("partyVacationContractId");

            List<GenericValue> list = delegator.findList("PartyVacationContract", EntityCondition.makeCondition(creiteria), null, listOrder, null, true);
            if (!list.isEmpty()) {
                if(list.size() > 1){
                    GenericValue gvValue = (GenericValue) list.get(list.size() - 2).clone();
                    gvValue.set("toDate", fromDate);
                    gvValue.store();
                    Msg = "Done";
                }
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // only commit the transaction if we started one... this will throw an exception if it fails
            try {
                TransactionUtil.commit(beganTransaction);

            } catch (GenericEntityException e) {
                Msg = e.getMessage();
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                return ServiceUtil.returnError(Msg);
            }
        }

        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
        String successMsg = UtilProperties.getMessage(resourceError, "Success", messageMap, locale);
        return ServiceUtil.returnSuccess(successMsg);
    }

}
