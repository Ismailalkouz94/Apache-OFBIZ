/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.accounting.ledger;

import java.io.BufferedReader;
import org.apache.ofbiz.accounting.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
//import static org.apache.ofbiz.accounting.invoice.InvoiceServices.module;
//import static org.apache.ofbiz.accounting.invoice.InvoiceServices.resource;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.util.EntityTypeUtil;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

/**
 *
 * @author DELL
 */
public class GlobalGL {

    public static String module = GlobalGL.class.getName();

    public static final String resource = "AccountingUiLabels";

    public static Map<String, Object> importGl(DispatchContext dctx, Map<String, Object> context) {
        System.out.println("**importGl**");
        Locale locale = (Locale) context.get("locale");
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        ByteBuffer fileBytes = (ByteBuffer) context.get("uploadedFile");
        String organizationPartyId = "Company";
        String encoding = System.getProperty("file.encoding");
        String csvString = Charset.forName(encoding).decode(fileBytes).toString();
        final BufferedReader csvReader = new BufferedReader(new StringReader(csvString));
        CSVFormat fmt = CSVFormat.DEFAULT.withHeader();
        List<String> errMsgs = new LinkedList<String>();
        List<String> newErrMsgs = new LinkedList<String>();

        int glCreated = 0;
        List<GenericValue> toBeStored = new LinkedList<GenericValue>();
        GenericValue glAccount = null;

        if (fileBytes == null) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resource, "AccountingUploadedFileDataNotFound", locale));
        }
        try {
            for (final CSVRecord rec : fmt.parse(csvReader)) {
                newErrMsgs = new LinkedList<String>();
                glAccount = delegator.makeValue("GlAccount");

//                if (UtilValidate.isNotEmpty(rec.get("glAccountId"))) {
//                    glAccount.set("glAccountId", rec.get("glAccountId"));
//                } else {
//                    String glAccountId = delegator.getNextSeqId("GlAccount");
//                    glAccount.set("glAccountId", glAccountId);
//                }
                if (UtilValidate.isNotEmpty(rec.get("glAccountTypeId"))) {
                    glAccount.set("glAccountTypeId", rec.get("glAccountTypeId"));
                } else {
                    glAccount.set("glAccountTypeId", "_NA_");
                }
                if (UtilValidate.isNotEmpty(rec.get("glAccountClassId"))) {
                    glAccount.set("glAccountClassId", rec.get("glAccountClassId"));
                } else {
                    glAccount.set("glAccountClassId", "_NA_");
                }

                glAccount.set("glResourceTypeId", "_NA_");

                glAccount.set("parentGlAccountId", rec.get("parentGlAccountId"));

                if (UtilValidate.isNotEmpty(rec.get("accountCode"))) {
                    glAccount.set("glAccountId", rec.get("accountCode"));
                    glAccount.set("accountCode", rec.get("accountCode"));
                } else {
                    newErrMsgs.add("Line number " + rec.getRecordNumber() + " : Mandatory Account Code missing for Gl Account");
                }
                if (UtilValidate.isNotEmpty(rec.get("accountName"))) {
                    glAccount.set("accountName", rec.get("accountName"));
                } else {
                    newErrMsgs.add("Line number " + rec.getRecordNumber() + " : Mandatory Account Name missing for Gl Account");
                }

                toBeStored.add(glAccount);

                if (newErrMsgs.size() > 0) {
                    errMsgs.addAll(newErrMsgs);
                }
            }

        } catch (IOException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(e.getMessage());
        }
        System.out.println(toBeStored);
        if (errMsgs.size() <= 0) {
            try {
                delegator.storeAll(toBeStored);
                System.out.println("toBeStored**");
                glCreated = toBeStored.size();
            } catch (GenericEntityException e) {
                System.out.println("catch");
                try {
                    csvReader.close();
                } catch (IOException ex) {
                    Debug.logError(ex, module);
                    return ServiceUtil.returnError(ex.getMessage());
                }
                Debug.logWarning(e.getMessage(), module);
                Debug.logError(e, module);
                return ServiceUtil.returnError(UtilProperties.getMessage(resource,
                        "AccountingGlFailure", locale) + e.getMessage());
            }
        }

        if (errMsgs.size() > 0) {
            return ServiceUtil.returnError(errMsgs);
        }
        Map<String, Object> result = ServiceUtil.returnSuccess(UtilProperties.getMessage(resource, "AccountingNewGlCreated", UtilMisc.toMap("glCreated", glCreated), locale));
        result.put("organizationPartyId", organizationPartyId);//organizationPartyId
        return result;
    }

    public static String setSessionLookup_partyId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("setSessionLookup_partyId");
        String Lookup_partyId = request.getParameter("Lookup_partyId").toString();
        System.out.println("Lookup_partyId " + Lookup_partyId);
        request.getSession().setAttribute("Lookup_partyId", Lookup_partyId);
        return "";
    }
}
