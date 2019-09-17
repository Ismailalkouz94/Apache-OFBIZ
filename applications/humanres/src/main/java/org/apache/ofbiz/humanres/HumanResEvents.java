/**
 * ****************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * *****************************************************************************
 */
package org.apache.ofbiz.humanres;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.humanres.PayrollServices;

import static org.apache.ofbiz.humanres.PayrollServices.resourceError;

import org.apache.ofbiz.service.DispatchContext;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class HumanResEvents {

    public static final String module = HumanResEvents.class.getName();
    public static final String resourceError = "HumanResErrorUiLabels";

    // Please note : the structure of map in this function is according to the JSON data map of the jsTree
    public static String getChildHRCategoryTree(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String partyId = request.getParameter("partyId");
        String onclickFunction = request.getParameter("onclickFunction");
        String additionParam = request.getParameter("additionParam");
        String hrefString = request.getParameter("hrefString");
        String hrefString2 = request.getParameter("hrefString2");

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("delegator", delegator);
        paramMap.put("partyId", partyId);
        paramMap.put("onclsetEmployeeSettingsickFunction", onclickFunction);
        paramMap.put("additionParam", additionParam);
        paramMap.put("hrefString", hrefString);
        paramMap.put("hrefString2", hrefString2);

        List<Map<String, Object>> categoryList = new ArrayList<Map<String, Object>>();

        //check employee position
        try {
            categoryList.addAll(getCurrentEmployeeDetails(paramMap));
        } catch (GenericEntityException e) {
            e.printStackTrace();
            return "error";
        }

        try {
            GenericValue partyGroup = EntityQuery.use(delegator).from("PartyGroup").where("partyId", partyId).queryOne();
            if (partyGroup != null) {
                paramMap.put("partyGroup", partyGroup);
                /* get the child departments of company or party */
                categoryList.addAll(getChildComps(paramMap));

                /* get employee which are working in company or party */
                categoryList.addAll(getEmployeeInComp(paramMap));
            }
        } catch (GenericEntityException e) {
            e.printStackTrace();
            return "error";
        }
        request.setAttribute("hrTree", categoryList);
        return "success";
    }

    private static List<Map<String, Object>> getCurrentEmployeeDetails(Map<String, Object> params) throws GenericEntityException {

        Delegator delegator = (Delegator) params.get("delegator");
        String partyId = (String) params.get("partyId");
        String onclickFunction = (String) params.get("onclickFunction");
        String additionParam = (String) params.get("additionParam");
        String hrefString = (String) params.get("hrefString");
        String hrefString2 = (String) params.get("hrefString2");

        List<Map<String, Object>> responseList = new ArrayList<>();

        long emplPosCount;
        try {
            emplPosCount = EntityQuery.use(delegator).from("EmplPosition")
                    .where("emplPositionId", partyId).queryCount();
            if (emplPosCount > 0) {
                String emplId = partyId;
                List<GenericValue> emlpfillCtxs = EntityQuery.use(delegator).from("EmplPositionFulfillment")
                        .where("emplPositionId", emplId)
                        .filterByDate().queryList();
                if (UtilValidate.isNotEmpty(emlpfillCtxs)) {
                    for (GenericValue emlpfillCtx : emlpfillCtxs) {
                        String memberId = emlpfillCtx.getString("partyId");
                        GenericValue memCtx = EntityQuery.use(delegator).from("Person").where("partyId", partyId).queryOne();
                        String title = null;
                        if (UtilValidate.isNotEmpty(memCtx)) {
                            String firstname = memCtx.getString("firstName");
                            String lastname = memCtx.getString("lastName");
                            if (UtilValidate.isEmpty(lastname)) {
                                lastname = "";
                            }
                            if (UtilValidate.isEmpty(firstname)) {
                                firstname = "";
                            }
                            title = firstname + " " + lastname;
                        }
                        GenericValue memGroupCtx = EntityQuery.use(delegator).from("PartyGroup").where("partyId", partyId).queryOne();
                        if (UtilValidate.isNotEmpty(memGroupCtx)) {
                            title = memGroupCtx.getString("groupName");
                        }

                        Map<String, Object> josonMap = new HashMap<String, Object>();
                        Map<String, Object> dataMap = new HashMap<String, Object>();
                        Map<String, Object> dataAttrMap = new HashMap<String, Object>();
                        Map<String, Object> attrMap = new HashMap<String, Object>();

                        dataAttrMap.put("onClick", onclickFunction + "('" + memberId + additionParam + "')");

                        String hrefStr = hrefString + memberId;
                        if (UtilValidate.isNotEmpty(hrefString2)) {
                            hrefStr = hrefStr + hrefString2;
                        }
                        dataAttrMap.put("href", hrefStr);

                        attrMap.put("rel", "P");
                        attrMap.put("id", memberId);

                        dataMap.put("title", title);
                        dataMap.put("attr", dataAttrMap);

                        josonMap.put("attr", attrMap);
                        josonMap.put("data", dataMap);

                        responseList.add(josonMap);
                    }
                }
            }
        } catch (GenericEntityException e) {
            e.printStackTrace();
            throw new GenericEntityException(e);
        }

        return responseList;
    }

    private static List<Map<String, Object>> getChildComps(Map<String, Object> params) throws GenericEntityException {

        Delegator delegator = (Delegator) params.get("delegator");
        String onclickFunction = (String) params.get("onclickFunction");
        String additionParam = (String) params.get("additionParam");
        String hrefString = (String) params.get("hrefString");
        String hrefString2 = (String) params.get("hrefString2");

        Map<String, Object> partyGroup = (Map<String, Object>) params.get("partyGroup");
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        List<GenericValue> childOfComs = null;

        try {
            childOfComs = EntityQuery.use(delegator).from("PartyRelationship")
                    .where("partyIdFrom", partyGroup.get("partyId"),
                            "partyRelationshipTypeId", "GROUP_ROLLUP")
                    .filterByDate().queryList();
            if (UtilValidate.isNotEmpty(childOfComs)) {

                for (GenericValue childOfCom : childOfComs) {
                    Object catId = null;
                    String catNameField = null;
                    String title = null;

                    Map<String, Object> josonMap = new HashMap<String, Object>();
                    Map<String, Object> dataMap = new HashMap<String, Object>();
                    Map<String, Object> dataAttrMap = new HashMap<String, Object>();
                    Map<String, Object> attrMap = new HashMap<String, Object>();

                    catId = childOfCom.get("partyIdTo");

                    //Department or Sub department
                    GenericValue childContext = EntityQuery.use(delegator).from("PartyGroup").where("partyId", catId).queryOne();
                    if (UtilValidate.isNotEmpty(childContext)) {
                        catNameField = (String) childContext.get("groupName");
                        title = catNameField;
                        josonMap.put("title", title);

                    }
                    //Check child existing
                    List<GenericValue> childOfSubComs = EntityQuery.use(delegator).from("PartyRelationship")
                            .where("partyIdFrom", catId,
                                    "partyRelationshipTypeId", "GROUP_ROLLUP")
                            .filterByDate().queryList();
                    //check employee position
                    List<GenericValue> isPosition = EntityQuery.use(delegator).from("EmplPositionEmplPosFulfillment").where("partyId", catId).queryList();
                    System.out.println("EmplPositionEmplPosFulfillment" + isPosition);
                    if (UtilValidate.isNotEmpty(childOfSubComs) || UtilValidate.isNotEmpty(isPosition)) {
                        josonMap.put("state", "closed");
                    }

                    //Employee
                    GenericValue emContext = EntityQuery.use(delegator).from("Person").where("partyId", catId).queryOne();
                    if (UtilValidate.isNotEmpty(emContext)) {
                        String firstname = (String) emContext.get("firstName");
                        String lastname = (String) emContext.get("lastName");
                        if (UtilValidate.isEmpty(lastname)) {
                            lastname = "";
                        }
                        if (UtilValidate.isEmpty(firstname)) {
                            firstname = "";
                        }
                        title = firstname + " " + lastname;
                    }

                    dataAttrMap.put("onClick", onclickFunction + "('" + catId + additionParam + "')");

                    String hrefStr = hrefString + catId;
                    if (UtilValidate.isNotEmpty(hrefString2)) {
                        hrefStr = hrefStr + hrefString2;
                    }
                    dataAttrMap.put("href", hrefStr);

                    dataMap.put("attr", dataAttrMap);
                    dataMap.put("title", title);

                    attrMap.put("rel", "Y");
                    attrMap.put("id", catId);

                    josonMap.put("attr", attrMap);
                    josonMap.put("data", dataMap);

                    resultList.add(josonMap);
                }
            }
        } catch (GenericEntityException e) {
            e.printStackTrace();
            throw new GenericEntityException(e);
        }

        return resultList;

    }

    private static List<Map<String, Object>> getEmployeeInComp(Map<String, Object> params) throws GenericEntityException {
        List<GenericValue> isEmpls = null;
        Delegator delegator = (Delegator) params.get("delegator");
        String partyId = (String) params.get("partyId");

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        try {
            isEmpls = EntityQuery.use(delegator).from("EmplPositionEmplPosFulfillment")
                    .where(EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId),
                            EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "EMPL_POS_INACTIVE"))
                    .filterByDate("actualFromDate", "actualThruDate")
                    .queryList();
            System.out.println("isEmpls " + isEmpls);
            if (UtilValidate.isNotEmpty(isEmpls)) {
                for (GenericValue childOfEmpl : isEmpls) {
                    Map<String, Object> emplMap = new HashMap<String, Object>();
                    Map<String, Object> emplAttrMap = new HashMap<String, Object>();
                    Map<String, Object> empldataMap = new HashMap<String, Object>();
                    Map<String, Object> emplDataAttrMap = new HashMap<String, Object>();

                    String emplId = (String) childOfEmpl.get("emplPositionId");
                    String typeId = (String) childOfEmpl.get("emplPositionTypeId");

                    //check child
                    List<GenericValue> emlpfCtxs = EntityQuery.use(delegator).from("EmplPositionFulfillment")
                            .where("emplPositionId", emplId)
                            .filterByDate().queryList();
                    if (UtilValidate.isNotEmpty(emlpfCtxs)) {
                        emplMap.put("state", "closed");
                    }

                    GenericValue emplContext = EntityQuery.use(delegator).from("EmplPositionType").where("emplPositionTypeId", typeId).queryOne();
                    String title = null;
                    if (UtilValidate.isNotEmpty(emplContext)) {
                        title = (String) emplContext.get("description") + " " + "[" + emplId + "]";
                    }

                    String hrefStr = "emplPositionView?emplPositionId=" + emplId;
                    emplAttrMap.put("href", hrefStr);
                    emplAttrMap.put("onClick", "callEmplDocument" + "('" + emplId + "')");

                    empldataMap.put("title", title);
                    empldataMap.put("attr", emplAttrMap);

                    emplDataAttrMap.put("id", emplId);
                    emplDataAttrMap.put("rel", "N");

                    emplMap.put("data", empldataMap);
                    emplMap.put("attr", emplDataAttrMap);
                    emplMap.put("title", title);

                    resultList.add(emplMap);
                }
            }
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            throw new GenericEntityException(e);
        }

        return resultList;
    }

    public static String getCity(HttpServletRequest request, HttpServletResponse response) throws GenericEntityException, ParseException, IOException {
        String vargeoId = "";
        String cityname = request.getParameter("countryGeoId");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("geoId", cityname);
        String str = "<select>";
        try {
            List<GenericValue> list = delegator.findList("city", EntityCondition.makeCondition(criteria), null, null, null, true);
//                GenericValue gv = delegator.findOne("city", criteria, true);
//            List<GenericValue> gv1 = delegator.findAll("city", true);
            for (GenericValue genericValue : list) {
                str += "<option value='" + genericValue.get("cityname") + "'>" + genericValue.get("cityname") + "</option>";
//                    pw.println("<option value='1'>" + genericValue.get("allowenceTypeName") + "</option>");
//                    pw.flush();

            }
            str += "</select>";
            response.getOutputStream().println(str);
            response.getOutputStream().flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    //    for depandances
    public static String getEmployeesDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("roleTypeId", "EMPLOYEE");
//        criteria.put("roleTypeId", "FAMILY_MEMBER");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findList("PartyRole", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue genericValue : gv1) {

                Map<String, String> criteria2 = new HashMap<String, String>();
                String partyId = genericValue.get("partyId").toString();
                criteria2.put("partyId", partyId);

                List<GenericValue> gv2 = delegator.findList("Person", EntityCondition.makeCondition(criteria2), null, null, null, true);
                for (GenericValue genericValue2 : gv2) {
                    str += "<option value='" + genericValue.get("partyId") + "'>" + genericValue.get("partyId") + " - " + genericValue2.get("firstName") + " " + genericValue2.get("lastName") + "</option>";
                }
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getRelatedTypeDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("DepandancesRelatedType", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("relatedType") + "'>" + genericValue.get("description") + "</option>";

            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getIdeneifreTypeDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("DepDetailsType", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("idType") + "'>" + genericValue.get("description") + "</option>";

            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getPartyId(HttpServletRequest request, HttpServletResponse response) {
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

        String id = (String) userLogin.getString("partyId");
        return id;

    }

    public static List getData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String partyId = getPartyId(request, response);
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        Map<String, String> criteria = new HashMap<String, String>();
        List<GenericValue> gv1 = null;
        criteria.put("partyId", partyId);

        try {
            gv1 = delegator.findList("Depandances", EntityCondition.makeCondition(criteria), null, null, null, true);

        } catch (GenericEntityException ex) {

        }
        return gv1;
    }

    //    ...........Insurance services.......
    public static String getCopmanyInsuranceDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("InsuranceCompanyList", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("insListId") + "'>" + genericValue.get("companyName") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getInsuranceTypeDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("InsuranceType", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("insTypeId") + "'>" + genericValue.get("description") + "</option>";

            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getCompInsuranceContractIdDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("CompanyInsuranceContract", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("comInsContId") + "'>" + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getInsuranceClassCatogeryDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("InsuranceClassCatogery", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("InsClassCatId") + "'>" + genericValue.get("InsClassCatId") + " - " + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static Map ListFamilyMember(HttpServletRequest request, HttpServletResponse response, String partyId) {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        Map<String, String> DataList = new HashMap<String, String>();

        criteria.put("relatedTo", partyId);
//        criteria.put("roleTypeId", "FAMILY_MEMBER");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue genericValue : gv1) {
                DataList.put("partyId", genericValue.get("partyId").toString());
                DataList.put("name", genericValue.get("firstName").toString());
                DataList.put("gender", genericValue.get("gender").toString());
                DataList.put("birthDate", genericValue.get("birthDate").toString());

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return DataList;
    }

    public static String getInsuranceContarctEndDate(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String ContractId = request.getParameter("ContractId");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("comInsContId", ContractId);
//        criteria.put("roleTypeId", "FAMILY_MEMBER");
        String str = "";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findList("CompanyInsuranceContract", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue genericValue : gv1) {

                str += genericValue.get("endDate");
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getEmployeeFamilyMemberDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String partyIdValue = request.getParameter("partyIdValue");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("relatedTo", partyIdValue);
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("partyId") + "'>" + genericValue.get("partyId") + " - " + genericValue.get("firstName") + " " + genericValue.get("lastName") + "</option>";

            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getInsuranceEmpDetails(HttpServletRequest request, HttpServletResponse response) {

        PrintWriter out = null;
        String comInsContId = request.getParameter("comInsContId");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("comInsContId", comInsContId);
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {

            List<GenericValue> list = delegator.findList("InsuranceEmpDetails", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).get("relatedTo") == null) {
                    jsonRes = new JSONObject();
                    jsonRes.put("InsuranceEmpDetailsId", list.get(i).get("InsuranceEmpDetailsId"));
                    jsonRes.put("partyId", list.get(i).get("partyId"));
                    jsonRes.put("partyName", getPartyName(request, response, list.get(i).get("partyId").toString()));
                    jsonRes.put("comInsContId", list.get(i).get("comInsContId"));
                    jsonRes.put("memberNumber", list.get(i).get("memberNumber"));
                    jsonRes.put("InsClass", list.get(i).get("InsClass"));

                    jsonRes.put("startDate", list.get(i).get("startDate").toString());
                    jsonRes.put("endDate", list.get(i).get("endDate").toString());

                    jsonData.add(jsonRes);
                }
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }

    public static String getPartyName(HttpServletRequest request, HttpServletResponse response, String partyIdParam) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String FOLANG = (String) request.getSession().getAttribute("FOLANG");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("partyId", partyIdParam);
        String str = "";
        System.out.println("FOLANG "+FOLANG);
        try {
            List<GenericValue> list = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
            System.out.println("list " + list);
            if (!list.isEmpty()) {
//                if (FOLANG.equals("en") || FOLANG == null ) {
                    str += list.get(0).get("firstName") + " " + list.get(0).get("middleName") + " " + list.get(0).get("grandfatherName") + " " + list.get(0).get("lastName");
//                } else if (FOLANG.equals("ar")) {
                    str += list.get(0).get("firstNameLocal") + " " + list.get(0).get("middleNameLocal") + " " + list.get(0).get("grandfatherNameLocal") + " " + list.get(0).get("lastNameLocal");
//                }
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }


    public static String getPartyName(HttpServletRequest request, String partyIdParam) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String FOLANG = (String) request.getSession().getAttribute("FOLANG");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("partyId", partyIdParam);
        String str = "";
        try {
            List<GenericValue> list = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
            System.out.println("list " + list);
            if (!list.isEmpty()) {
                str += list.get(0).get("firstName") + " " + list.get(0).get("lastName");

            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    public static String getInsuranceEmpFmDetails(HttpServletRequest request, HttpServletResponse response) {

        try {
            String relatedTo = request.getParameter("partyId_param");
            String comInsContId = request.getParameter("contractId");
            PrintWriter out = null;

//            ServletOutputStream pw = response.getOutputStream();
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("relatedTo", relatedTo);
            criteria.put("comInsContId", comInsContId);
            JSONObject jsonRes;
            JSONArray jsonData = new JSONArray();
            try {

                List<GenericValue> list = delegator.findList("InsuranceEmpDetails", EntityCondition.makeCondition(criteria), null, null, null, true);
                for (int i = 0; i < list.size(); i++) {

                    jsonRes = new JSONObject();
                    jsonRes.put("InsuranceEmpDetailsId", list.get(i).get("InsuranceEmpDetailsId"));
                    jsonRes.put("partyId", list.get(i).get("partyId"));
                    jsonRes.put("partyName", getPartyName(request, response, list.get(i).get("partyId").toString()));
                    jsonRes.put("relatedTo", list.get(i).get("relatedTo"));
                    jsonRes.put("memberNumber", list.get(i).get("memberNumber"));

                    jsonData.add(jsonRes);
                }

                response.setContentType("text/html; charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                out = response.getWriter();
                out.println(jsonData.toString());
                out.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";

    }

    public static String getInsuranceClassTypeDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("InsuranceClassType", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("InsClass") + "'>" + genericValue.get("Description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getInsuranceComList(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;

//            ServletOutputStream pw = response.getOutputStream();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            List<GenericValue> list = delegator.findAll("InsuranceCompanyList", true);
            for (int i = 0; i < list.size(); i++) {

                jsonRes = new JSONObject();
                jsonRes.put("insListId", list.get(i).get("insListId"));
                jsonRes.put("companyName", list.get(i).get("companyName"));
                jsonRes.put("remark", list.get(i).get("remark"));

                jsonData.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }

    public static String getInsuranceClassCatogery(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        String comInsContId = request.getParameter("contractId");
//            ServletOutputStream pw = response.getOutputStream();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("comInsContId", comInsContId);

        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {

            List<GenericValue> list = delegator.findList("InsuranceClassCatogery", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (int i = 0; i < list.size(); i++) {

                jsonRes = new JSONObject();
                jsonRes.put("InsClassCatId", list.get(i).get("InsClassCatId"));
                jsonRes.put("description", list.get(i).get("description"));
                jsonRes.put("ageFrom", list.get(i).get("ageFrom"));
                jsonRes.put("ageTo", list.get(i).get("ageTo"));

                jsonData.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }

    public static String getInsuranceContractExtention(HttpServletRequest request, HttpServletResponse response) {

        PrintWriter out = null;
        String comInsContId = request.getParameter("comInsContId");

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("comInsContId", comInsContId);
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {

            List<GenericValue> list = delegator.findList("InsuranceContractExt", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (int i = 0; i < list.size(); i++) {

                jsonRes = new JSONObject();
                jsonRes.put("insContExtId", list.get(i).get("insContExtId"));
                jsonRes.put("comInsContId", getInsuranceContName(request, response, list.get(i).get("comInsContId").toString()));
                jsonRes.put("actionId", getInsuranceContotractExtActionName(request, response, list.get(i).get("actionId").toString()));
                jsonRes.put("Amount", list.get(i).get("Amount"));
                jsonRes.put("currentDate", list.get(i).get("currentDate").toString());
                jsonRes.put("note", list.get(i).get("note"));

                jsonData.add(jsonRes);

            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }

    public static String getInsuranceContName(HttpServletRequest request, HttpServletResponse response, String comInsContId) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("comInsContId", comInsContId);
        String str = "";
        try {
            List<GenericValue> gv1 = delegator.findList("CompanyInsuranceContract", EntityCondition.makeCondition(criteria), null, null, null, true);

            str += gv1.get(0).get("description");

        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    public static String getInsuranceContotractExtActionName(HttpServletRequest request, HttpServletResponse response, String actionId) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("actionId", actionId);
        String str = "";
        try {
            List<GenericValue> gv1 = delegator.findList("InsClassExtAction", EntityCondition.makeCondition(criteria), null, null, null, true);
            str += gv1.get(0).get("description").toString();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    public static String getInsuranceClassActionDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("InsClassExtAction", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("actionId") + "'>" + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getCompanyInsuranceContract(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;

//            ServletOutputStream pw = response.getOutputStream();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            List<GenericValue> list = delegator.findAll("CompanyInsuranceContract", true);
            for (int i = 0; i < list.size(); i++) {

                jsonRes = new JSONObject();
                jsonRes.put("comInsContId", list.get(i).get("comInsContId"));
                jsonRes.put("insListId", getInsuranceCompanyName(request, response, list.get(i).get("insListId").toString()));
                jsonRes.put("description", list.get(i).get("description"));
                jsonRes.put("insTypeId", getInsuranceTypeName(request, response, list.get(i).get("insTypeId").toString()));
                jsonRes.put("contarctAmount", list.get(i).get("contarctAmount"));
                jsonRes.put("covCompanyPer", list.get(i).get("covCompanyPer"));
                jsonRes.put("covEmpPer", list.get(i).get("covEmpPer"));

                jsonRes.put("covCompFmPer", list.get(i).get("covCompFmPer"));
                jsonRes.put("covFmPer", list.get(i).get("covFmPer"));
                jsonRes.put("startDate", list.get(i).get("startDate").toString());
                jsonRes.put("endDate", list.get(i).get("endDate").toString());

                jsonData.add(jsonRes);

            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }

    public static String getCompanyInsuranceContractDetails(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        try {
            String comInsContId = request.getParameter("contractId");
            String InsClassCatId = request.getParameter("InsClassCatId");
            PrintWriter out = null;

            Delegator delegator = (Delegator) request.getAttribute("delegator");
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("comInsContId", comInsContId);
            criteria.put("InsClassCatId", InsClassCatId);
            JSONObject jsonRes;
            JSONArray jsonData = new JSONArray();
            try {

                List<GenericValue> list = delegator.findList("CompanyInsContractDet", EntityCondition.makeCondition(criteria), null, null, null, true);
                for (int i = 0; i < list.size(); i++) {

                    jsonRes = new JSONObject();
                    jsonRes.put("comInsContDetailsId", list.get(i).get("comInsContDetailsId"));
                    jsonRes.put("comInsContId", list.get(i).get("comInsContId"));
                    jsonRes.put("InsClassCatId", getInsuranceClassCatogeryName(request, response, list.get(i).get("InsClassCatId").toString()));
                    jsonRes.put("gender", list.get(i).get("gender"));
                    if (list.get(i).get("gender").equals("M")) {
                        jsonRes.put("gender", "Male");
                    } else {
                        jsonRes.put("gender", "Female");
                    }
                    jsonRes.put("classAPlus", list.get(i).get("classAPlus"));
                    jsonRes.put("classA", list.get(i).get("classA"));
                    jsonRes.put("classB", list.get(i).get("classB"));
                    jsonRes.put("classC", list.get(i).get("classC"));
                    if (list.get(i).get("classD") == null) {
                        jsonRes.put("classD", "0");
                    } else {
                        jsonRes.put("classD", list.get(i).get("classD"));
                    }
                    jsonRes.put("comInsContId2", list.get(i).get("comInsContId"));
                    jsonRes.put("InsClassCatId2", list.get(i).get("InsClassCatId"));
                    jsonData.add(jsonRes);

                }
                response.setContentType("text/html; charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                out = response.getWriter();
                out.println(jsonData.toString());
                out.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getInsuranceCompanyName(HttpServletRequest request, HttpServletResponse response, String insListId) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("insListId", insListId);
        String str = "";
        try {
            List<GenericValue> gv1 = delegator.findList("InsuranceCompanyList", EntityCondition.makeCondition(criteria), null, null, null, true);
            str += gv1.get(0).get("companyName");

        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    public static String getInsuranceTypeName(HttpServletRequest request, HttpServletResponse response, String insTypeId) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("insTypeId", insTypeId);

        String str = "";
        try {
            List<GenericValue> gv1 = delegator.findList("InsuranceType", EntityCondition.makeCondition(criteria), null, null, null, true);

            str += gv1.get(0).get("description");

        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    public static String getInsuranceClassCatogeryName(HttpServletRequest request, HttpServletResponse response, String InsClassCatId) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("InsClassCatId", InsClassCatId);
        String str = "";
        try {
            List<GenericValue> gv1 = delegator.findList("InsuranceClassCatogery", EntityCondition.makeCondition(criteria), null, null, null, true);

            str += gv1.get(0).get("description");
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    public static String getJobGroupData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            List<GenericValue> list = delegator.findAll("JobGroup", true);
            for (int i = 0; i < list.size(); i++) {

                jsonRes = new JSONObject();
                jsonRes.put("jobGroupId", list.get(i).get("jobGroupId"));
                jsonRes.put("description", list.get(i).get("description"));
                jsonData.add(jsonRes);

            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }

    // Screen Employee Psetion .. Filterd DropDown Gradelist By Gob Group ID
    public static String filterGradeId(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String jobGroupId = request.getParameter("jobGroupId");
        String emplPositionId = request.getParameter("emplPositionId");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        System.out.println("emplPositionId " + emplPositionId);
        String gradeId = "";
        try {
//            selected grade Id
            if (!emplPositionId.isEmpty()) {
                System.out.println("!emplPositionId.isEmpty()");
                Map<String, String> criteriaPos = new HashMap<String, String>();
                criteriaPos.put("emplPositionId", emplPositionId);
                List<GenericValue> resultPos = delegator.findList("EmplPosition", EntityCondition.makeCondition(criteriaPos), null, null, null, true);
                System.out.println("resultPos : " + resultPos);
                if (resultPos != null) {
                    for (GenericValue genericValue : resultPos) {
                        gradeId = (String) genericValue.get("gradeId");
                    }
                }
                System.out.println("gradeId : " + gradeId);
            }
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("jobGroupId", jobGroupId);
            List<GenericValue> list = delegator.findList("Grade", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue genericValue : list) {
                if (gradeId.equals(genericValue.get("gradeId"))) {
                    str += "<option value='" + genericValue.get("gradeId") + "' selected='selected'>" + genericValue.get("description") + "</option>";
                } else {
                    str += "<option value='" + genericValue.get("gradeId") + "'>" + genericValue.get("description") + "</option>";
                }
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String fulfillmentsPartyId(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, SQLException {
        ResultSet resultSet = null;
        String partyId = request.getParameter("partyId");
        PrintWriter out = null;
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));

            String sqlStr = "Select PARTY_ID_TO\n"
                    + "From EMPLOYMENT\n"
                    + "Where PARTY_ID_FROM = '" + partyId + "'\n"
                    + "minus\n"
                    + "select F.PARTY_ID\n"
                    + "from  Empl_Position P,EMPL_POSITION_FULFILLMENT F\n"
                    + "where  P.EMPL_POSITION_ID = F.EMPL_POSITION_ID\n"
                    + "And P.PARTY_ID = '" + partyId + "'\n"
                    + "And P.STATUS_ID = 'EMPL_POS_OCCUPIED'";
            // EMPL_POS_ACTIVE

            Map<String, String> criteria = new HashMap<String, String>();

            resultSet = sqlProcessor.executeQuery(sqlStr);
            String strSelect, strOption = "", strEndSelect = "", selectStr = "";
            strSelect = "<select>";
            while (resultSet.next()) {
                criteria.put("partyId", resultSet.getString("PARTY_ID_TO"));
                List<GenericValue> list = delegator.findList("PartyNameView", EntityCondition.makeCondition(criteria), null, null, null, true);
                for (GenericValue genericValue : list) {
                    strOption = strOption + "<option value='" + genericValue.get("partyId") + "'>" + genericValue.get("firstName") + " " + genericValue.get("lastName") + "</option>";
                }
            }//end while

            strEndSelect += "</select>";
            selectStr = strSelect + strOption + strEndSelect;
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(selectStr);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String fillBasicSalary(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String gradeId = request.getParameter("gradeId");
        String degreeId = request.getParameter("degreeId");
        BigDecimal basicSalary = new BigDecimal(0);
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        try {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("gradeId", gradeId);
            criteria.put("degreeId", degreeId);
            List<GenericValue> list = delegator.findList("Degree", EntityCondition.makeCondition(criteria), null, null, null, true);
            System.out.println("list :" + list);
            if (list != null) {
                for (GenericValue genericValue : list) {
                    basicSalary = (BigDecimal) genericValue.get("basicSalary");
                }
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            System.out.println("basicSalary :" + basicSalary);
            out.println(basicSalary.toString());
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getInternalOrganization(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String employmentId = request.getParameter("employmentId");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String internalOrganization = "";
        try {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("employmentId", employmentId);
            GenericValue result = delegator.findOne("Employment", criteria, true);
            System.out.println("result : " + result);
            if (result != null) {
                internalOrganization = (String) result.get("partyIdFrom");
            }
            System.out.println("internalOrganization : " + internalOrganization);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(internalOrganization);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getEmplStatusDesciption(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String partyId = request.getParameter("partyId");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String emplStatus = "";
        JSONObject jsonRes = new JSONObject();
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        java.sql.Date LocalTransDateMax = PayrollServices.getLastActive_TransDate_Employee_SalaryGrade(request, partyId);
//        JSONArray jsonData = new JSONArray();
        try {
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put("partyId", partyId);
            GenericValue result = delegator.findOne("Person", criteria, true);
            if (result != null) {

                Map<String, String> criteriaStatusItem = new HashMap<String, String>();
                criteriaStatusItem.put("statusId", (String) result.get("EmplStatus"));
                GenericValue resultStatusItem = delegator.findOne("StatusItem", criteriaStatusItem, true);
                jsonRes.put("emplStatus", (String) resultStatusItem.get("description"));
                criteria.put("TransDate", LocalTransDateMax);
                List<GenericValue> result2 = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);

                if (result2 != null) {
                    for (GenericValue genericValue : result2) {
                        jsonRes.put("emplPositionId", genericValue.get("emplPositionId"));
                        jsonRes.put("gradeDesc", PayrollServices.getGradeDescription(request, (String) genericValue.get("gradeId")));
                        jsonRes.put("degreeDesc", PayrollServices.getDegreeDescription(request, (String) genericValue.get("degreeId")));
                        jsonRes.put("degreeId", (String) genericValue.get("degreeId"));
                    }

                }
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonRes);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getEmplStatus(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String partyId = request.getParameter("partyId");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String emplStatus = "";
        JSONObject jsonRes = new JSONObject();
//        JSONArray jsonData = new JSONArray();
        try {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("partyId", partyId);
            GenericValue result = delegator.findOne("Person", criteria, true);
            if (result != null) {
                jsonRes.put("emplStatus", (String) result.get("EmplStatus"));
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonRes);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getGrade(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String emplPosId = request.getParameter("emplPosId");
        System.out.println("1 " + emplPosId);
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String gradeId = "";
        JSONObject jsonRes = new JSONObject();
//        JSONArray jsonData = new JSONArray();
        System.out.println("2");
        try {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("emplPositionId", emplPosId);
            System.out.println("3 " + criteria);
//            GenericValue result = delegator.findOne("EmplPosition", criteria, true);
            List<GenericValue> result = delegator.findList("EmplPosition", EntityCondition.makeCondition(criteria), null, null, null, true);
            System.out.println("result 4 :" + result);
            if (result != null) {
                for (GenericValue genericValue : result) {
                    jsonRes.put("gradeId", (String) genericValue.get("gradeId"));
//                    basicSalary = (BigDecimal) genericValue.get("basicSalary");
//                    Map<String, String> criteriaGrade = new HashMap<String, String>();
//                    criteriaGrade.put("gradeId", (String) genericValue.get("gradeId"));
//                    GenericValue resultGrade = delegator.findOne("Grade", criteriaGrade, true);
//                    System.out.println("resultGrade : " + resultGrade);
//                    if (resultGrade != null) {
                    jsonRes.put("gradeDesc", PayrollServices.getGradeDescription(request, (String) genericValue.get("gradeId")));
//                    }

                }
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonRes.toString());
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static List<String> getGradeDegreeByGetEmplStatus(HttpServletRequest request, HttpServletResponse response, String emplPosId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
//        Map<String, Object> resultRet = new HashMap<String, Object>();
        List<String> resultList = new ArrayList<>();
        try {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("emplPositionId", emplPosId);
            GenericValue result = delegator.findOne("EmplPosition", criteria, true);
            System.out.println("result : " + result);
            if (result != null) {
                Map<String, String> criteriaGrade = new HashMap<String, String>();
                criteriaGrade.put("gradeId", (String) result.get("gradeId"));
                GenericValue resultGrade = delegator.findOne("Grade", criteriaGrade, true);
                System.out.println("resultGrade : " + resultGrade);
                if (resultGrade != null) {
//                    resultRet.put("gradeId", resultGrade.get("description"));
                    resultList.add(resultGrade.get("description").toString());
                }

//                Map<String, String> criteriaDegree = new HashMap<String, String>();
//                criteriaDegree.put("degreeId", (String) result.get("degreeId"));
//                GenericValue resultDegree = delegator.findOne("Degree", criteriaDegree, true);
//                System.out.println("criteriaDegree : " + criteriaDegree);
//                if (resultDegree != null) {
////                    resultRet.put("degreeId", resultDegree.get("description"));
//                    resultList.add(resultDegree.get("description").toString());
//                }
            }
            System.out.println("resultList : " + resultList);
//            response.setContentType("text/html; charset=UTF-8");
//            response.setCharacterEncoding("UTF-8");
//            out = response.getWriter();
//            out.println(jsonData);
//            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultList;

    }
//    public static Map<String, Object> EcaUpdateInsuranceEndDate(DispatchContext ctx, Map<String, ? extends Object> context) {
//        Map<String, Object> result = new HashMap<String, Object>();
//        System.out.println("hello from hr eca");
//        String partyIdTo = (String) context.get("partyIdTo");
//        String thruDate = (String) context.get("thruDate");
//        System.out.println("partyIdTo " + partyIdTo);
//        System.out.println("thruDate " + thruDate);
//        
//        ResultSet resultSet = null;
//        ResultSet resultSetPass = null;
//        String sqlStr = null;
//        String sqlStrgetPass = null;
//        try {
//            System.out.println("before update");
//
//            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            DateFormat format2 = new SimpleDateFormat("dd-MMM-YYYY");
//            Date date =   format.parse(thruDate);
//            String temp = format2.format(date);
//            System.out.println("date " + temp);
//
//            Delegator delegator = ctx.getDelegator();;
//            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
//
//            sqlStr = "UPDATE INSURANCE_EMP_DETAILS SET END_DATE='" + temp + "' where PARTY_ID='" + partyIdTo + "'";
//            resultSet = sqlProcessor.executeQuery(sqlStr);
//            System.out.println("after update");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                resultSet.close();
//            } catch (Exception e) {
//                e.getStackTrace();
//            }
//        }
//
//        return result;
//
//    }

    //    to update positionId with approverWorkFlow --rbab3aa
    public static String updateWorkflowApproverPositionId(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String emplPositionId = request.getParameter("emplPositionId");
        String wfSysApproverId = request.getParameter("wfSysApproverId");
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("wfSysApproverId", wfSysApproverId);
        Map<String, String> messageMap;
        try {
            List<GenericValue> list = delegator.findList("WfSysApprover", EntityCondition.makeCondition(criteria), null, null, null, true);

            GenericValue gvValue = (GenericValue) list.get(0).clone();
            gvValue.set("emplPositionId", emplPositionId);
            gvValue.store();

        } catch (GenericEntityException e) {

            messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
            String errMsg = UtilProperties.getMessage(resourceError, "", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";

        }
        messageMap = UtilMisc.toMap("successMessage", "");
        String successMsg = UtilProperties.getMessage(resourceError, "updateSuccessfully", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", successMsg);
        return "success";
    }

}
