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
package org.apache.ofbiz.accounting.shamel;

import java.io.BufferedReader;
import org.apache.ofbiz.accounting.ledger.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
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
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import static org.apache.ofbiz.humanres.PayrollServices.module;
import static org.apache.ofbiz.humanres.PayrollServices.resourceError;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

public class ShamelAcctgTrans {

    public static final String module = ShamelAcctgTrans.class.getName();
    public static final String resourceError = "HumanResErrorUiLabels";
    public static final String resource = "AccountingUiLabels";
    private static BigDecimal ZERO = BigDecimal.ZERO;

    public static String post_ShamelAcctgTrans(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        System.out.println("post_ShamelAcctgTrans");
        String returnMessage = "success";
        System.out.println("exchangeRate " + request.getAttribute("exchangeRate"));
        System.out.println("description " + request.getAttribute("description"));
        System.out.println("transactionDate " + request.getAttribute("transactionDate"));
        System.out.println("partyId " + request.getAttribute("partyId"));
        System.out.println("entry " + request.getAttribute("entry"));

        System.out.println("before");
        List<Object> arrayEntry = (List<Object>) request.getAttribute("entry");
        for (int i = 0; i < arrayEntry.size(); i++) {
            Map<Object, Object> rowMap = (Map<Object, Object>) arrayEntry.get(i);
            System.out.println(rowMap.get("debitCreditFlag"));
            System.out.println(rowMap.get("acctgTransEntryTypeId"));
            System.out.println(rowMap.get("origAmount"));
            System.out.println(rowMap.get("partyId"));
            System.out.println(rowMap.get("origCurrencyUomId"));
            System.out.println(rowMap.get("glAccountId"));
            System.out.println(rowMap.get("description"));
        }
        System.out.println("after");
        try {
            response.getOutputStream().println(returnMessage);
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.getStackTrace();
            /*report an error*/
        } finally {
            response.getOutputStream().close();
        }
//        StringBuffer jb = new StringBuffer();
//        String line = null;
//        try {
//            System.out.println("try");
//            BufferedReader reader = request.getReader();
//            System.out.println("reader");
//            System.out.println(reader);
//            while ((line = reader.readLine()) != null) {
//                jb.append(line);
//                System.out.println("line " + line);
//            }
//            response.getOutputStream().println(returnMessage);
//            response.getOutputStream().flush();
//        } catch (Exception e) {
//            e.getStackTrace();
//            /*report an error*/
//        } finally {
//            response.getOutputStream().close();
//        }
//
//        System.out.println("StringBuffer");
//        System.out.println(jb);
//        try {
//            JSONObject jsonObject = org.json.HTTP.toJSONObject(jb.toString());
//            System.out.println("jsonObject");
//            System.out.println(jsonObject);
//        } catch (JSONException e) {
//            // crash and burn
//            throw new IOException("Error parsing JSON request string");
//        }
//        System.out.println(obj);
//        System.out.println("text11 "+request.getReader());
//        request.
//        Delegator delegator = (Delegator) request.getAttribute("delegator");
//        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
//
//        String acctgTransEntrySeqId = "0";
//
//        Date date = new Date();
//        Timestamp timestamp = new Timestamp(date.getTime());
//
//        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
//        String userLoginId = (String) userLogin.getString("userLoginId");
//
//        Map<String, Object> criteriaCurrency = new HashMap<String, Object>();
//        criteriaCurrency.put("partyId", "Company");
//
//        Map<String, String> criteriaTax = new HashMap<String, String>();
//        criteriaTax.put("Key", "INCOME_TAX_ALLOWENCES_ID");
//
//        List<GenericValue> toBeStored = new LinkedList<GenericValue>();
//        GenericValue acctgTrans = null;
//        GenericValue acctgTransEntry = null;
//        boolean beganTransaction = false;
//        try {
//            beganTransaction = TransactionUtil.begin();
//
//            GenericValue resultTax = delegator.findOne("GlobalPayrollSettings", criteriaTax, true);
//            String TaxId = (String) resultTax.get("Value");
//
//            GenericValue resultCurrency = delegator.findOne("PartyAcctgPreference", criteriaCurrency, true);
//            String currencyUomId = (String) resultCurrency.get("baseCurrencyUomId");
//
//            acctgTrans = delegator.makeValue("AcctgTrans");
//            String acctgTransId = delegator.getNextSeqId("AcctgTrans");
//
//            acctgTrans.put("acctgTransId", acctgTransId);
//            acctgTrans.put("acctgTransCode", delegator.getNextSeqId("AcctgTransCode"));
//            acctgTrans.put("acctgTransTypeId", "SHAMEL");
//            acctgTrans.put("description", "acctg Trans description");
//            acctgTrans.put("postedDate", timestamp);
//            acctgTrans.put("transactionDate", timestamp);
//            acctgTrans.put("isPosted", "Y");// edite
//            acctgTrans.put("partyId", "Orig Company");
//            acctgTrans.put("createdByUserLogin", userLoginId);
//            acctgTrans.put("lastModifiedByUserLogin", userLoginId);
////            INVOICE_ID
////            PAYMENT_ID
//            toBeStored.add(acctgTrans);
//
//            Map<String, List<String>> values = new HashMap<String, List<String>>();
//            List<String> subList = new ArrayList<String>();
//
//            for (String key : values.keySet()) {
//                acctgTransEntry = delegator.makeValue("AcctgTransEntry");
//                acctgTransEntrySeqId = Integer.toString(Integer.parseInt(acctgTransEntrySeqId) + 1);
//
//                System.out.println("amount Value Before: " + new BigDecimal(values.get(key).get(0)).abs());
//                Map<String, Object> priceResults = new HashMap<String, Object>();                               //                        USD
//                priceResults = dispatcher.runSync("convertUom", UtilMisc.<String, Object>toMap("uomId", "currency from Shamel JOD", "uomIdTo", currencyUomId,
//                        "originalValue", new BigDecimal(values.get(key).get(0)).abs(), "defaultDecimalScale", Long.valueOf(2), "defaultRoundingMode", "HalfUp"));
//                System.out.println("amount Value after : " + priceResults.get("convertedValue"));
//
//                acctgTransEntry.put("acctgTransId", acctgTransId);
//                acctgTransEntry.put("acctgTransEntrySeqId", acctgTransEntrySeqId);//seq by parent
//                acctgTransEntry.put("debitCreditFlag", "C_D");
//                acctgTransEntry.put("acctgTransEntryTypeId", "another type of SHAMEL");
//                acctgTransEntry.put("amount", new BigDecimal(values.get(key).get(0)).abs()); // edite
//                acctgTransEntry.put("origAmount", "origAmount from Shamel JOD"); // edite
//                acctgTransEntry.put("partyId", "itemOrg");
//                acctgTransEntry.put("currencyUomId", currencyUomId);
//                acctgTransEntry.put("origCurrencyUomId", "currency from Shamel JOD");
//                acctgTransEntry.put("glAccountId", values.get(key).get(1)); // GL
//                acctgTransEntry.put("description", key);
//
//                toBeStored.add(acctgTransEntry);
//
////                    throw new Exception("Test rol back");
//            }//end for values map
//            //     } // end for of organization_Distinct
//
//            if (returnMessage.equals("success")) {
//                try {
//                    delegator.storeAll(toBeStored);
//                } catch (Exception e) {
//                    returnMessage = "error";
//                    e.getMessage();
//                }
//            }
//        } catch (Exception ex) {
//            returnMessage = "error";
////            delegator.rollback();
//            ex.printStackTrace();
//            try {
//                // only rollback the transaction if we started one...
//                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
//            } catch (GenericEntityException e) {
//                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
//            }
//        } finally {
//            // only commit the transaction if we started one... this will throw an exception if it fails
//            try {
//                TransactionUtil.commit(beganTransaction);
////                returnMessage = "success";
//            } catch (GenericEntityException e) {
//                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
//                returnMessage = "error";
//            }
//        }//finaly
        return returnMessage;
    }
}
