/** *****************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ****************************************************************************** */
package org.apache.ofbiz.accounting.ledger;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.apache.ofbiz.accounting.ledger.GlobalGL.resource;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;
import java.sql.SQLException;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import static org.apache.ofbiz.humanres.PayrollServices.module;
import static org.apache.ofbiz.humanres.PayrollServices.resourceError;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import java.io.PrintWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.List;
import java.util.ArrayList;

public class GeneralLedgerServices {

    public static final String module = GeneralLedgerServices.class.getName();
    public static final String resourceError = "HumanResErrorUiLabels";
    public static final String resource = "AccountingUiLabels";
    private static BigDecimal ZERO = BigDecimal.ZERO;
    private static Set<Integer> yearSet = new HashSet<Integer>();

    public static Map<String, Object> createUpdateCostCenter(DispatchContext dctx, Map<String, ? extends Object> context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        BigDecimal totalAmountPercentage = ZERO;
        Map<String, Object> createGlAcctCatMemFromCostCentersMap = null;
        String glAccountId = (String) context.get("glAccountId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map<String, String> amountPercentageMap = UtilGenerics.checkMap(context.get("amountPercentageMap"));
        totalAmountPercentage = GeneralLedgerServices.calculateCostCenterTotal(amountPercentageMap);
        Map<String, Object> result = ServiceUtil.returnSuccess();
        for (String rowKey : amountPercentageMap.keySet()) {
            String rowValue = amountPercentageMap.get(rowKey);
            if (UtilValidate.isNotEmpty(rowValue)) {
                createGlAcctCatMemFromCostCentersMap = UtilMisc.toMap("glAccountId", glAccountId,
                        "glAccountCategoryId", rowKey, "amountPercentage", new BigDecimal(rowValue),
                        "userLogin", userLogin, "totalAmountPercentage", totalAmountPercentage);
            } else {
                createGlAcctCatMemFromCostCentersMap = UtilMisc.toMap("glAccountId", glAccountId,
                        "glAccountCategoryId", rowKey, "amountPercentage", new BigDecimal(0),
                        "userLogin", userLogin, "totalAmountPercentage", totalAmountPercentage);
            }
            try {
                result = dispatcher.runSync("createGlAcctCatMemFromCostCenters", createGlAcctCatMemFromCostCentersMap);
            } catch (GenericServiceException e) {
                Debug.logError(e, module);
                return ServiceUtil.returnError(e.getMessage());
            }
        }
        return result;
    }

    public static BigDecimal calculateCostCenterTotal(Map<String, String> amountPercentageMap) {
        BigDecimal totalAmountPercentage = ZERO;
        for (String rowKey : amountPercentageMap.keySet()) {
            if (UtilValidate.isNotEmpty(amountPercentageMap.get(rowKey))) {
                BigDecimal rowValue = new BigDecimal(amountPercentageMap.get(rowKey));
                if (rowValue != null) {
                    totalAmountPercentage = totalAmountPercentage.add(rowValue);
                }
            }
        }
        return totalAmountPercentage;
    }

    public static Map<String, Object> resetSequenceAcctg(DispatchContext dctx, Map<String, Object> context) throws ParseException, IOException {
        System.out.println("resetSequenceAcctg");
        Locale locale = (Locale) context.get("locale");
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yy");
        SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(sdf.format(date));
        String organizationPartyId = (String) context.get("organizationPartyId");
        if (organizationPartyId.isEmpty()) {
            organizationPartyId = "organizationPartyId";
        }

        List<String> newErrMsgs = new LinkedList<String>();
        List<GenericValue> toBeStored = new LinkedList<GenericValue>();

//        year = 18;
        boolean beganTransaction = false;
        try {
            beganTransaction = TransactionUtil.begin();
            Map<String, String> criteriaInvoiceAR = new HashMap<String, String>();
            criteriaInvoiceAR.put("partyId", organizationPartyId);

            GenericValue resultInvoiceAR = delegator.findOne("PartyAcctgPreference", criteriaInvoiceAR, true);
            String invoiceARPrefix = (String) resultInvoiceAR.get("invoiceIdPrefix");

            if (Integer.parseInt(invoiceARPrefix) < year) {

                Map<String, String> criteriaAcctgTrans = new HashMap<String, String>();
                criteriaAcctgTrans.put("seqName", "AcctgTrans");

                Map<String, String> criteriaAcctgTransCode = new HashMap<String, String>();
                criteriaAcctgTransCode.put("seqName", "AcctgTransCode");

                Map<String, String> criteriaInvoiceAP = new HashMap<String, String>();
                criteriaInvoiceAP.put("seqName", "Invoice");

                Map<String, String> criteriaPaymentAP = new HashMap<String, String>();
                criteriaPaymentAP.put("seqName", "Payment");

                GenericValue resultAcctgTrans = delegator.findOne("SequenceValueItem", criteriaAcctgTrans, true);
                String acctgTransId = String.valueOf(resultAcctgTrans.get("seqId"));

                GenericValue resultAcctgTransCode = delegator.findOne("SequenceValueItem", criteriaAcctgTransCode, true);
                String acctgTransCodeId = String.valueOf(resultAcctgTransCode.get("seqId"));

                GenericValue resultInvoiceAP = delegator.findOne("SequenceValueItem", criteriaInvoiceAP, true);
                String invoiceAPId = String.valueOf(resultInvoiceAP.get("seqId"));

                GenericValue resultPaymentAP = delegator.findOne("SequenceValueItem", criteriaPaymentAP, true);
                String paymentAPId = String.valueOf(resultPaymentAP.get("seqId"));

                GenericValue gvAcctgTrans = (GenericValue) resultAcctgTrans.clone();
                gvAcctgTrans.set("seqId", Long.parseLong(year + "" + acctgTransId.substring(2, 10)));
                toBeStored.add(gvAcctgTrans);

                GenericValue gvAcctgTransCode = (GenericValue) resultAcctgTransCode.clone();
                gvAcctgTransCode.set("seqId", Long.parseLong(year + "" + acctgTransCodeId.substring(2, 10)));
                toBeStored.add(gvAcctgTransCode);

                GenericValue gvInvoiceAP = (GenericValue) resultInvoiceAP.clone();
                gvInvoiceAP.set("seqId", Long.parseLong(year + "" + invoiceAPId.substring(2, 10)));
                toBeStored.add(gvInvoiceAP);

                GenericValue gvInvoiceAR = (GenericValue) resultInvoiceAR.clone();
                gvInvoiceAR.set("invoiceIdPrefix", String.valueOf(year));
                gvInvoiceAR.set("paymentIdPrefix", String.valueOf(year));
                toBeStored.add(gvInvoiceAR);

                GenericValue gvPaymentAP = (GenericValue) resultPaymentAP.clone();
                gvPaymentAP.set("seqId", Long.parseLong(year + "" + paymentAPId.substring(2, 10)));
                toBeStored.add(gvPaymentAP);

            } else {
                newErrMsgs.add("Can't Reset Sequence in this year " + sdfFull.format(date));
            }
            if (newErrMsgs.size() <= 0) {
                try {
                    delegator.storeAll(toBeStored);
                } catch (Exception e) {
                    newErrMsgs.add(e.getMessage());
                    e.getMessage();
                }
            }
        } catch (Exception ex) {
            newErrMsgs.add(ex.getMessage());
            ex.printStackTrace();
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
            } catch (GenericEntityException e) {
                newErrMsgs.add(e.getMessage());
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
            }
        } finally {
            try {
                TransactionUtil.commit(beganTransaction);
            } catch (GenericEntityException e) {
                e.printStackTrace();
                newErrMsgs.add(e.getMessage());
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
            }
        }
        if (newErrMsgs.size() > 0) {
            return ServiceUtil.returnError(newErrMsgs);
        }

        Map<String, Object> result = ServiceUtil.returnSuccess(UtilProperties.getMessage(resource, "AccountingSequenceReset", locale));
        return result;

    }

    public static String getSequenceData(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        System.out.println("** getSequenceData **");
        PrintWriter out = null;
        ResultSet resultSet = null;
        String year = "";
        JSONObject jsonRes = new JSONObject();;
        JSONArray jsonSequence = new JSONArray();

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sql = "select SUBSTR(SEQ_NAME,LENGTH(SEQ_NAME)-3,4) Year,SEQ_NAME,SEQ_ID\n"
                    + "from SEQUENCE_VALUE_ITEM\n"
                    + "where SEQ_NAME like '%20%'\n"
                    + "order by Year";
            System.out.println(sql);
            resultSet = sqlProcessor.executeQuery(sql);

            while (resultSet.next()) {
                yearSet.add(Integer.valueOf(resultSet.getString("Year")));
                if (year == null || year.isEmpty()) {
                    year = (String) resultSet.getString("Year");
                    jsonRes = new JSONObject();
                    jsonRes.put("year", resultSet.getString("Year").toString());
                    System.out.println("** year == null || year.isEmpty() new JSONObject() **" + year);
                }

                if (year.equals(resultSet.getString("Year").toString())) {
                    System.out.println("** resultSet equals **" + resultSet.getString("Year").toString());
                    if ((resultSet.getString("SEQ_NAME").toString()).contains("AcctgTrans_")) {
                        jsonRes.put("AcctgTrans", resultSet.getString("SEQ_ID"));
                    } else if ((resultSet.getString("SEQ_NAME").toString()).contains("AcctgTransCode_")) {
                        jsonRes.put("AcctgTransCode", resultSet.getString("SEQ_ID"));
                    } else if ((resultSet.getString("SEQ_NAME").toString()).contains("Invoice_AP_")) {
                        jsonRes.put("Invoice_AP", resultSet.getString("SEQ_ID"));
                    } else if ((resultSet.getString("SEQ_NAME").toString()).contains("Invoice_AR_")) {
                        jsonRes.put("Invoice_AR", resultSet.getString("SEQ_ID"));
                    } else if ((resultSet.getString("SEQ_NAME").toString()).contains("Payment_AP_")) {
                        jsonRes.put("Payment_AP", resultSet.getString("SEQ_ID"));
                    } else if ((resultSet.getString("SEQ_NAME").toString()).contains("Payment_AR_")) {
                        jsonRes.put("Payment_AR", resultSet.getString("SEQ_ID"));
                    }
                } else {
                    jsonSequence.add(jsonRes);
                    jsonRes = new JSONObject();
                    year = (String) resultSet.getString("Year");
                    jsonRes.put("year", year);
                    if ((resultSet.getString("SEQ_NAME").toString()).contains("AcctgTrans_")) {
                        jsonRes.put("AcctgTrans", resultSet.getString("SEQ_ID"));
                    } else if ((resultSet.getString("SEQ_NAME").toString()).contains("AcctgTransCode_")) {
                        jsonRes.put("AcctgTransCode", resultSet.getString("SEQ_ID"));
                    } else if ((resultSet.getString("SEQ_NAME").toString()).contains("Invoice_AP_")) {
                        jsonRes.put("Invoice_AP", resultSet.getString("SEQ_ID"));
                    } else if ((resultSet.getString("SEQ_NAME").toString()).contains("Invoice_AR_")) {
                        jsonRes.put("Invoice_AR", resultSet.getString("SEQ_ID"));
                    } else if ((resultSet.getString("SEQ_NAME").toString()).contains("Payment_AP_")) {
                        jsonRes.put("Payment_AP", resultSet.getString("SEQ_ID"));
                    } else if ((resultSet.getString("SEQ_NAME").toString()).contains("Payment_AR_")) {
                        jsonRes.put("Payment_AR", resultSet.getString("SEQ_ID"));
                    }
                    System.out.println("** not equals set JSONArray and new JSONObject() **" + year);
                }
            }
            jsonSequence.add(jsonRes);
            System.out.println("** jsonSequence **");
            System.out.println(jsonSequence);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonSequence.toString());
            out.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String createSequenceValueItemAcc(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        System.out.println("** createSequenceValueItemAcc **");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        PrintWriter out = null;
        JSONObject jsonRes = null;
        JSONArray jsonSequence = new JSONArray();

        String year = request.getParameter("year").toString();
        System.out.println(Long.valueOf(request.getParameter("year")));
        long acctgTrans_Parm = Long.valueOf(request.getParameter("AcctgTrans"));
        long acctgTransCode_Parm = Long.valueOf(request.getParameter("AcctgTransCode"));
        long invoice_AP_Parm = Long.valueOf(request.getParameter("Invoice_AP"));
        long invoice_AR_Parm = Long.valueOf(request.getParameter("Invoice_AR"));
        long payment_AP_Parm = Long.valueOf(request.getParameter("Payment_AP"));
        long payment_AR_Parm = Long.valueOf(request.getParameter("Payment_AR"));

        try {
            Map<String, Object> acctgTrans_Map = new HashMap<String, Object>();
            acctgTrans_Map.put("seqName", "AcctgTrans_" + year);
            acctgTrans_Map.put("seqId", acctgTrans_Parm);

            Map<String, Object> acctgTransCode_Map = new HashMap<String, Object>();
            acctgTransCode_Map.put("seqName", "AcctgTransCode_" + year);
            acctgTransCode_Map.put("seqId", acctgTransCode_Parm);

            Map<String, Object> invoice_AP_Map = new HashMap<String, Object>();
            invoice_AP_Map.put("seqName", "Invoice_AP_" + year);
            invoice_AP_Map.put("seqId", invoice_AP_Parm);

            Map<String, Object> invoice_AR_Map = new HashMap<String, Object>();
            invoice_AR_Map.put("seqName", "Invoice_AR_" + year);
            invoice_AR_Map.put("seqId", invoice_AR_Parm);

            Map<String, Object> payment_AP_Map = new HashMap<String, Object>();
            payment_AP_Map.put("seqName", "Payment_AP_" + year);
            payment_AP_Map.put("seqId", payment_AP_Parm);

            Map<String, Object> payment_AR_Map = new HashMap<String, Object>();
            payment_AR_Map.put("seqName", "Payment_AR_" + year);
            payment_AR_Map.put("seqId", payment_AR_Parm);

            Map<String, Object> acctgTrans_MapResults = dispatcher.runSync("createSequenceValueItem", acctgTrans_Map);
            if (ServiceUtil.isError(acctgTrans_MapResults)) {
                request.setAttribute("_ERROR_MESSAGE_", acctgTrans_MapResults.get("errorMessage"));
                return "error";
            }
            jsonSequence.add(new JSONObject(acctgTrans_MapResults));

            Map<String, Object> acctgTransCode_MapResults = dispatcher.runSync("createSequenceValueItem", acctgTransCode_Map);
            if (ServiceUtil.isError(acctgTransCode_MapResults)) {
                request.setAttribute("_ERROR_MESSAGE_", acctgTransCode_MapResults.get("errorMessage"));
                return "error";
            }
            jsonSequence.add(new JSONObject(acctgTransCode_MapResults));

            Map<String, Object> invoice_AP_MapResults = dispatcher.runSync("createSequenceValueItem", invoice_AP_Map);
            if (ServiceUtil.isError(invoice_AP_MapResults)) {
                request.setAttribute("_ERROR_MESSAGE_", invoice_AP_MapResults.get("errorMessage"));
                return "error";
            }
            jsonSequence.add(new JSONObject(invoice_AP_MapResults));

            Map<String, Object> invoice_AR_MapResults = dispatcher.runSync("createSequenceValueItem", invoice_AR_Map);
            if (ServiceUtil.isError(invoice_AR_MapResults)) {
                request.setAttribute("_ERROR_MESSAGE_", invoice_AR_MapResults.get("errorMessage"));
                return "error";
            }
            jsonSequence.add(new JSONObject(invoice_AR_MapResults));

            Map<String, Object> payment_AP_MapResults = dispatcher.runSync("createSequenceValueItem", payment_AP_Map);
            if (ServiceUtil.isError(payment_AP_MapResults)) {
                request.setAttribute("_ERROR_MESSAGE_", payment_AP_MapResults.get("errorMessage"));
                return "error";
            }
            jsonSequence.add(new JSONObject(payment_AP_MapResults));

            Map<String, Object> payment_AR_MapResults = dispatcher.runSync("createSequenceValueItem", payment_AR_Map);
            if (ServiceUtil.isError(payment_AR_MapResults)) {
                request.setAttribute("_ERROR_MESSAGE_", payment_AR_MapResults.get("errorMessage"));
                return "error";
            }
            jsonSequence.add(new JSONObject(payment_AR_MapResults));

//            response.setContentType("text/html; charset=UTF-8");
//            response.setCharacterEncoding("UTF-8");
//            out = response.getWriter();
//            out.println(jsonSequence.toString());
//            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        String successMsg = UtilProperties.getMessage(resourceError, "calculatedSuccessFully", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", jsonSequence);
        return "success";
//        return "";
    }

    public static String getCustomTimePeriodAcc_DropDown(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        System.out.println("** getCustomTimePeriodAcc **");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        PrintWriter out = null;
        String str = "<select>";

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("isClosed", "N");
        SimpleDateFormat formatNowYear = new SimpleDateFormat("yyyy");
        Set<Integer> yearOfTimePeriod = new HashSet<Integer>();
//        yearOfTimePeriod = null;
        try {
            List<GenericValue> result = delegator.findList("CustomTimePeriod", EntityCondition.makeCondition(criteria), null, null, null, true);
            System.out.println("** result **" + result);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    Date date = (Date) row.get("fromDate");
                    if (!yearSet.contains(Integer.parseInt(formatNowYear.format(date)))) {
                        yearOfTimePeriod.add(Integer.parseInt(formatNowYear.format(date)));
                        System.out.println("if date " + formatNowYear.format(date));
                    }
                }
            }
            for (Integer itemOrg : yearOfTimePeriod) {
                str += "<option value='" + itemOrg + "'>" + itemOrg + "</option>";
            }
            str += "</select>";

            System.out.println("yearSet " + yearSet);
            System.out.println("yearOfTimePeriod " + yearOfTimePeriod);
            System.out.println("str " + str);

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
