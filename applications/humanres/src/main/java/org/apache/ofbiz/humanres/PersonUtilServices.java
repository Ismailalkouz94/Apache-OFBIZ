/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @author Rbab3a
 */
package org.apache.ofbiz.humanres;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.io.PrintWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.apache.ofbiz.common.preferences.PreferenceServices;
import org.apache.ofbiz.humanres.PayrollServices;
import org.apache.ofbiz.humanres.HumanResEvents;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.text.DateFormatSymbols;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilFormatOut;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import org.apache.ofbiz.humanres.moving.MovingEmployee;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.humanres.employee.EmployeePosition;
import static org.apache.ofbiz.humanres.holding.HoldingEmployee.module;

import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PersonUtilServices {

    //    using request
    public static String getUserLoginIdByPartyId(HttpServletRequest request, String partyId) throws ParseException, IOException {

	Map<String, Object> cretirea = new HashMap<String, Object>();
	cretirea.put("partyId", partyId);

	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String userLoginId = "";
	try {
	    List<GenericValue> list = delegator.findList("UserLogin", EntityCondition.makeCondition(cretirea), null, null, null, true);
	    if (!list.isEmpty()) {
		for (GenericValue row : list) {
		    userLoginId = (String) row.get("userLoginId");
		    return userLoginId;
		}
	    }

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	return userLoginId;
    }

    //    using delegator
    public static String getUserLoginIdByPartyId(Delegator delegator, String partyId) throws ParseException, IOException {
	Map<String, Object> cretirea = new HashMap<String, Object>();
	cretirea.put("partyId", partyId);
	String userLoginId = "";
	try {
	    List<GenericValue> list = delegator.findList("UserLogin", EntityCondition.makeCondition(cretirea), null, null, null, true);
	    if (!list.isEmpty()) {
		for (GenericValue row : list) {
		    userLoginId = (String) row.get("userLoginId");
		    return userLoginId;
		}
	    }
	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	return userLoginId;
    }

    //    using request
    public static String getPartyIdByPositionId(HttpServletRequest request, String emplPositionId) throws ParseException, IOException {

	Map<String, Object> cretirea = new HashMap<String, Object>();
	cretirea.put("emplPositionId", emplPositionId);
	cretirea.put("thruDate", (java.sql.Date) null);
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String partyId = "";
	try {
	    List<GenericValue> list = delegator.findList("EmplPositionFulfillment", EntityCondition.makeCondition(cretirea), null, null, null, true);
	    if (!list.isEmpty()) {
		for (GenericValue row : list) {
		    partyId = (String) row.get("partyId");
		    return partyId;
		}
	    }

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}

	return partyId;
    }

    public static String getPartyIdByPositionId(Delegator delegator, String emplPositionId) throws ParseException, IOException {
	Map<String, Object> cretirea = new HashMap<String, Object>();
	cretirea.put("emplPositionId", emplPositionId);
	cretirea.put("thruDate", (java.sql.Date) null);

	String partyId = "";
	try {
	    List<GenericValue> list = delegator.findList("EmplPositionFulfillment", EntityCondition.makeCondition(cretirea), null, null, null, true);
	    if (!list.isEmpty()) {
		for (GenericValue row : list) {
		    partyId = (String) row.get("partyId");
		    return partyId;
		}
	    }

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}

	return partyId;
    }

    public static String getPositionIdByPartyId(HttpServletRequest request, String partyId) throws ParseException, IOException {

	Map<String, Object> EmplSalaryGradeTable = new HashMap<String, Object>();
	EmplSalaryGradeTable.put("partyId", partyId);
	EmplSalaryGradeTable.put("thruDate", (java.sql.Date) null);
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String emplPositionId = "";
	try {
	    List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplPositionFulfillment", EntityCondition.makeCondition(EmplSalaryGradeTable), null, null, null, true);
	    if (!EmplSalaryGradeList.isEmpty()) {
		for (GenericValue row : EmplSalaryGradeList) {

		    emplPositionId = (String) row.get("emplPositionId");
		    return emplPositionId;
		}
	    }

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}

	return emplPositionId;
    }

    public static String getPositionIdByPartyId(Delegator delegator, String partyId) throws ParseException, IOException {

	Map<String, Object> EmplSalaryGradeTable = new HashMap<String, Object>();
	EmplSalaryGradeTable.put("partyId", partyId);
	EmplSalaryGradeTable.put("thruDate", (java.sql.Date) null);
	String emplPositionId = "";
	try {
	    List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplPositionFulfillment", EntityCondition.makeCondition(EmplSalaryGradeTable), null, null, null, true);
	    if (!EmplSalaryGradeList.isEmpty()) {
		for (GenericValue row : EmplSalaryGradeList) {

		    emplPositionId = (String) row.get("emplPositionId");
		    return emplPositionId;
		}
	    }

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}

	return emplPositionId;
    }

    public static String getHiredDate(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
	Map<String, String> criteria = new HashMap<String, String>();
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String partyId = request.getParameter("partyId");
	criteria.put("partyId", partyId);
	criteria.put("TransType", "HIRING");
	PrintWriter out = null;
	java.sql.Date hiredDate = null;

	try {
	    List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
	    if (!result.isEmpty()) {

		for (GenericValue row : result) {
		    hiredDate = (java.sql.Date) row.get("TransDate");
		}
	    }

	    response.setContentType("text/html; charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    out = response.getWriter();
	    out.println(hiredDate);
	    out.flush();

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class
		    .getName()).log(Level.SEVERE, null, ex);
	}
	return "";

    }

//    for myPortal service
    public static String getPartyIdByUserLogin(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
	Map<String, String> criteria = new HashMap<String, String>();
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String userLoginId = request.getParameter("userLoginId");
	criteria.put("userLoginId", userLoginId);
	String partyId = "";
	PrintWriter out = null;
	try {
	    List<GenericValue> result = delegator.findList("UserLogin", EntityCondition.makeCondition(criteria), null, null, null, true);
	    if (!result.isEmpty()) {

		for (GenericValue row : result) {
		    partyId = (String) row.get("partyId");
		}
	    }

	    response.setContentType("text/html; charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    out = response.getWriter();
	    out.println(partyId);
	    out.flush();

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class
		    .getName()).log(Level.SEVERE, null, ex);
	}
	return "";

    }

    //full Name arabic and english ,Birthdate ,Address,status ...etc
    public static String getEmployeeDetails(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, IOException, ParseException {
	String partyId = request.getParameter("partyId");
	PrintWriter out = null;
	Map<String, Object> criteria = new HashMap<String, Object>();
	criteria.put("partyId", partyId);
	JSONObject jsonRes = new JSONObject();
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String emplPosition = "";
	String positionDesc = "";
	String emplPositionTypeId = "";
	try {
	    emplPosition = getPositionIdByPartyId(request, partyId);
	    emplPositionTypeId = PayrollServices.getEmplPositionTypeId(request, emplPosition);

	    java.sql.Date hiredDate = null;

	    //    get emplPostionId field from EmplPositionFulfillment Table
	    List<GenericValue> list = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
	    if (!list.isEmpty()) {
		for (GenericValue row : list) {
		    jsonRes.put("partyId", row.get("partyId"));
		    jsonRes.put("firstName", row.get("firstName"));
		    jsonRes.put("middleName", row.get("middleName"));
		    jsonRes.put("grandFather", row.get("grandfatherName"));
		    jsonRes.put("lastName", row.get("lastName"));
		    jsonRes.put("firstNameLocal", row.get("firstNameLocal"));
		    jsonRes.put("middleNameLocal", row.get("middleNameLocal"));
		    jsonRes.put("grandfatherNameLocal", row.get("grandfatherNameLocal"));
		    jsonRes.put("lastNameLocal", row.get("lastNameLocal"));
		    jsonRes.put("positionDesc", PayrollServices.getEmplPositionTypeDescription(request, emplPositionTypeId));
		    jsonRes.put("gender", row.get("gender"));
		    jsonRes.put("image", PreferenceServices.getUserImage(request, getUserLoginIdByPartyId(request, partyId)));
		    hiredDate = PayrollServices.getHiredDate(request, partyId);
		    if (hiredDate == null) {
			jsonRes.put("hiredDate", hiredDate);
		    } else {
			jsonRes.put("hiredDate", hiredDate.toString());
		    }

		    if (row.get("birthDate") == null) {
			jsonRes.put("birthDate", "");
		    } else {
			jsonRes.put("birthDate", row.get("birthDate").toString());
		    }
		    jsonRes.put("martialStatus", row.get("maritalStatus"));
		    jsonRes.put("wifeIsWorking", row.get("wifeIsWorking"));
			jsonRes.put("basicSalary", PayrollServices.getBasicSalary(request,partyId));
			jsonRes.put("sickVacation",PayrollServices.getGlobalSetting(request, "SICK_VACATION", "VACATION_DAYS"));
			jsonRes.put("sickSurgeryVacation",PayrollServices.getGlobalSetting(request, "SICK_VACATION_SURGERY", "VACATION_DAYS"));


		}
	    }
	    response.setContentType("text/html; charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    out = response.getWriter();
	    out.println(jsonRes.toString());
	    out.flush();
	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	return "";
    }

    //from PositionWorkflowStructure entity
    public static Map<String, Object> getApproverPositions(HttpServletRequest request, String childPositionId) throws SQLException, IOException, IOException, ParseException {
	Map<String, Object> cretirea = new HashMap<String, Object>();
	cretirea.put("positionId", childPositionId);
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	Map<String, Object> positions = new HashMap<String, Object>();
	try {

	    List<GenericValue> list = delegator.findList("PositionWorkflowStructure", EntityCondition.makeCondition(cretirea), null, null, null, true);
	    if (!list.isEmpty()) {
		for (GenericValue row : list) {
		    positions.put("positionIdParent", (String) row.get("positionIdParent"));
		    positions.put("positionIdParent2", (String) row.get("positionIdParent2"));
		    if ((String) row.get("positionIdParent3") != null) {
			positions.put("positionIdParent3", (String) row.get("positionIdParent3"));
		    } else {
			positions.put("positionIdParent3", (String) row.get("positionIdParent2"));
		    }

		}
	    }

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	return positions;

    }

    public static Map<String, Object> getApproverPositions(Delegator delegator, String childPositionId) throws SQLException, IOException, IOException, ParseException {
	Map<String, Object> cretirea = new HashMap<String, Object>();
	cretirea.put("positionId", childPositionId);

	Map<String, Object> positions = new HashMap<String, Object>();
	try {
	    List<GenericValue> list = delegator.findList("PositionWorkflowStructure", EntityCondition.makeCondition(cretirea), null, null, null, true);
	    if (!list.isEmpty()) {
		for (GenericValue row : list) {
		    positions.put("positionIdParent", (String) row.get("positionIdParent"));
		    positions.put("positionIdParent2", (String) row.get("positionIdParent2"));
		    if ((String) row.get("positionIdParent3") != null) {
			positions.put("positionIdParent3", (String) row.get("positionIdParent3"));
		    } else {
			positions.put("positionIdParent3", (String) row.get("positionIdParent2"));
		    }

		}
	    }

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	return positions;

    }

    //to get partyId with family Member DropDown
    public static String getPartyIdAndFM_DropDown(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, IOException, ParseException {
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String partyId = request.getParameter("partyId");
	Map<String, String> criteria = new HashMap<String, String>();
	criteria.put("relatedTo", partyId);
	PrintWriter out = null;
	String str = "<select id='FM-partyId'>";
	str += "<option value=' '> </option>";
	str += "<option value='" + partyId + "'>" + partyId + " - " + HumanResEvents.getPartyName(request, response, partyId) + "</option>";

	try {
	    List<GenericValue> list = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
	    if (!list.isEmpty()) {
		for (GenericValue item : list) {
		    str += "<option value='" + item.get("partyId") + "'>" + item.get("partyId") + " - " + item.get("firstName") + " " + item.get("lastName") + "</option>";
		}
	    }

	    str += "</select>";
	    response.setContentType("text/html; charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    out = response.getWriter();
	    out.println(str);
	    out.flush();
	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	return "";
    }

    public static String getPartyIdAndFM_Json(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, IOException, ParseException {
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String partyId = request.getParameter("partyId");
	Map<String, String> criteria = new HashMap<String, String>();
	criteria.put("relatedTo", partyId);
	PrintWriter out = null;
	JSONArray jsonData = new JSONArray();
	JSONObject jsonRes = new JSONObject();

	jsonRes = new JSONObject();
	jsonRes.put("partyId", partyId);
	jsonRes.put("name", HumanResEvents.getPartyName(request, partyId));
	jsonData.add(jsonRes);
	try {
	    jsonRes = new JSONObject();
	    List<GenericValue> list = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
	    if (!list.isEmpty()) {
		for (GenericValue item : list) {
		    jsonRes.put("partyId", item.get("partyId"));
		    jsonRes.put("name", item.get("partyId") + " - " + item.get("firstName") + " " + item.get("lastName"));
		    jsonData.add(jsonRes);
		}
	    }

	    response.setContentType("text/html; charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    out = response.getWriter();
	    out.println(jsonData.toString());
	    out.flush();
	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	return "";
    }

    //to get partyId with family Member JSON
    public static String getPartyIdAndFM_JSON(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, IOException, ParseException {
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String partyId = request.getParameter("partyId");
	Map<String, String> criteria = new HashMap<String, String>();
	criteria.put("relatedTo", partyId);
	PrintWriter out = null;
	JSONArray jsonData = new JSONArray();
	JSONObject jsonRes = new JSONObject();
	System.out.println("partyId " + partyId);
	jsonRes.put("partyId", partyId);
	jsonRes.put("name", HumanResEvents.getPartyName(request, response, partyId));
	jsonData.add(jsonRes);
	try {
	    List<GenericValue> list = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
	    if (!list.isEmpty()) {
		for (GenericValue item : list) {
		    jsonRes = new JSONObject();
		    jsonRes.put("partyId", item.get("partyId"));
		    jsonRes.put("name", item.get("firstName") + " " + item.get("lastName"));

		    jsonData.add(jsonRes);
		}
	    }

	    response.setContentType("text/html; charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    out = response.getWriter();
	    out.println(jsonData.toString());
	    out.flush();
	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	return "";
    }


	public static String getFamilyMemberDate_JSON(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, IOException, ParseException {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		String partyId = request.getParameter("partyId");
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put("relatedTo", partyId);
		PrintWriter out = null;
		JSONArray jsonData = new JSONArray();
		JSONObject jsonRes = new JSONObject();
		try {
			List<GenericValue> list = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
			if (!list.isEmpty()) {
				for (GenericValue item : list) {
					jsonRes = new JSONObject();
					jsonRes.put("partyId", item.get("partyId"));
					jsonRes.put("name", item.get("firstName") + " " + item.get("lastName"));
					jsonRes.put("birthDate", item.get("birthDate").toString());
					jsonRes.put("relationship",getFamilyMemberTypeDesc(request,(String) item.get("familyMemberType")) );
					jsonData.add(jsonRes);
				}
			}

			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			out.println(jsonData.toString());
			out.flush();
		} catch (GenericEntityException ex) {
			Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "";
	}


	public static String getFamilyMemberTypeDesc(HttpServletRequest request,String familyMemberType) throws SQLException, IOException, IOException, ParseException {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put("familyMemberType", familyMemberType);
		String desc="";
		try {
			List<GenericValue> list = delegator.findList("empFamilyMemberType", EntityCondition.makeCondition(criteria), null, null, null, true);
			if (!list.isEmpty()) {
              desc=(String)list.get(0).get("description");
			}

		} catch (GenericEntityException ex) {
			Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
		}
		return desc;
	}


    //    get Position Id for Hr in table WfSysApprovers
    public static String getHrPositionId(HttpServletRequest request) throws ParseException, IOException {
	Map<String, Object> certeria = new HashMap<String, Object>();
	certeria.put("wfSysApproverId", "HR");

	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String emplPositionId = "";
	try {
	    List<GenericValue> list = delegator.findList("WfSysApprover", EntityCondition.makeCondition(certeria), null, null, null, true);
	    if (!list.isEmpty()) {
		for (GenericValue row : list) {

		    emplPositionId = (String) row.get("emplPositionId");
		    return emplPositionId;
		}
	    }

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}

	return emplPositionId;
    }

    public static Timestamp convertStringToTimestamp(String str_date, boolean isFllTimeStamp) {
	try {
	    DateFormat formatter = null;
	    if (isFllTimeStamp == true) {
		formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    } else if (isFllTimeStamp == false) {
		formatter = new SimpleDateFormat("yyyy-MM-dd");
	    }
	    // you can change format of date
	    Date date = formatter.parse(str_date);
	    java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());

	    return timeStampDate;
	} catch (ParseException e) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, e);
	    return null;
	}
    }

    public static Timestamp convertStringToTimestamp_custom(String str_date, boolean isFllTimeStamp) {
	try {
	    DateFormat formatter = null;
	    if (isFllTimeStamp == true) {
		formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
	    } else if (isFllTimeStamp == false) {
		formatter = new SimpleDateFormat("yyyy-MM-dd");
	    }
	    // you can change format of date
	    Date date = formatter.parse(str_date);
	    java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());

	    return timeStampDate;
	} catch (ParseException e) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, e);
	    return null;
	}
    }

    public static String getEmployeeStatus(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String emplStatus = "";
	PrintWriter out = null;
	String partyId = request.getParameter("partyId");
	Map<String, String> creiteria = new HashMap<String, String>();
	creiteria.put("partyId", partyId);
	try {
	    GenericValue ListValue = delegator.findOne("Person", creiteria, true);
	    if (ListValue != null) {
		emplStatus = (String) ListValue.get("EmplStatus");
	    }

	    response.setContentType("text/html; charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    out = response.getWriter();
	    out.println(emplStatus);
	    out.flush();
	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	return emplStatus;
    }

    public static String getEmployeeStatus(HttpServletRequest request, String partyId, LocalDate startMonth, LocalDate endMonth) {
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String emplStatus = "HIRED";
	try {
	    List<EntityExpr> exprsMain = UtilMisc.toList(EntityCondition.makeCondition("TransDate", EntityOperator.GREATER_THAN_EQUAL_TO, java.sql.Date.valueOf(startMonth)),
		    EntityCondition.makeCondition("TransDate", EntityOperator.LESS_THAN_EQUAL_TO, java.sql.Date.valueOf(endMonth)),
		    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId));
	    EntityCondition conditionMain = EntityCondition.makeCondition(exprsMain, EntityOperator.AND);
	    System.out.println("conditionMain : " + conditionMain);

	    List<GenericValue> result = delegator.findList("EmplSalaryGrade", conditionMain, null, null, null, true);
	    System.out.println("List " + result);
	    ArrayList transTypeList = new ArrayList();

	    if (!result.isEmpty()) {
		for (GenericValue row : result) {
		    if (row.get("TransType").equals("HOLD")) {
			emplStatus = "HOLD";
		    }
		}
	    }
	    if (!transTypeList.isEmpty()) {
		if (transTypeList.contains("HOLD")) {

		} else if (transTypeList.contains("TERMINATED")) {
		    emplStatus = "TERMINATED";
		}
	    }
	    System.out.println("emplStatus " + emplStatus);
	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	return emplStatus;
    }

    public static String isEmployeeTerminated(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	PrintWriter out = null;
	String partyId = request.getParameter("partyId");
	Map<String, Object> criteria = new HashMap<String, Object>();
	boolean isTerminated = false;
	criteria.put("partyId", partyId);
	criteria.put("TransType", "TERMINATED");

	try {

	    List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
	    if (!result.isEmpty()) {
		isTerminated = true;
	    }

	    out = response.getWriter();
	    out.println(isTerminated);
	    out.flush();

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class
		    .getName()).log(Level.SEVERE, null, ex);
	}
	return "";
    }

    public static boolean isEmployeeTerminated(HttpServletRequest request, String partyId) throws ParseException, IOException {
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	PrintWriter out = null;

	Map<String, Object> criteria = new HashMap<String, Object>();
	boolean isTerminated = false;
	criteria.put("partyId", partyId);
	criteria.put("TransType", "TERMINATED");

	try {

	    List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
	    if (result.isEmpty()) {
		isTerminated = true;
	    }

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class
		    .getName()).log(Level.SEVERE, null, ex);
	}
	return isTerminated;
    }

    public static boolean isExsistTransType_ByTransDate(HttpServletRequest request, String partyId, java.sql.Date transDate, String transType) throws ParseException, IOException {
	Delegator delegator = (Delegator) request.getAttribute("delegator");

	Map<String, Object> criteria = new HashMap<String, Object>();
	boolean isExsist = false;
	criteria.put("partyId", partyId);
	criteria.put("TransDate", transDate);
	criteria.put("TransType", transType);
	criteria.put("endDate", null);
	try {
	    List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
	    if (!result.isEmpty()) {
		isExsist = true;

	    }
	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	System.out.println(" from service isExsistTransType_ByTransDate " + isExsist);
	return isExsist;
    }

    public static boolean isHoldEmployee(HttpServletRequest request, String partyId, java.sql.Date transDate) throws ParseException, IOException {
	Delegator delegator = (Delegator) request.getAttribute("delegator");

	Map<String, Object> criteria = new HashMap<String, Object>();
	boolean isExsist = false;
	criteria.put("partyId", partyId);
	criteria.put("TransDate", transDate);
	criteria.put("TransType", "HOLD");

	try {
	    List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
	    if (!result.isEmpty()) {
		for (GenericValue row : result) {
		    if (row.get("endDate") != null) {
			isExsist = true;
		    }
		}

	    }
	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	System.out.println(" from service isExsistTransType_ByTransDate " + isExsist);
	return isExsist;
    }

    // VALIDATION : get max TransType in preivous month to check if employee Terminated from start month to endMonth
    public static String isTerminated_isHOLD_Employee_ByMonth(HttpServletRequest request, String partyId, LocalDate startMonth, LocalDate endMonth) throws ParseException, IOException {
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String TransType = "";
	try {
	    List<EntityExpr> exprsMain = UtilMisc.toList(EntityCondition.makeCondition("TransDate", EntityOperator.GREATER_THAN_EQUAL_TO, java.sql.Date.valueOf(startMonth)),
		    EntityCondition.makeCondition("TransDate", EntityOperator.LESS_THAN_EQUAL_TO, java.sql.Date.valueOf(endMonth)),
		    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId));
	    EntityCondition conditionMain = EntityCondition.makeCondition(exprsMain, EntityOperator.AND);
	    System.out.println("conditionMain : " + conditionMain);

	    List<GenericValue> result = delegator.findList("EmplSalaryGrade", conditionMain, null, null, null, true);
	    if (result.isEmpty()) {

		TransType = PayrollServices.getLastEmployeeTransType_EmplSalaryGrade(request, partyId, java.sql.Date.valueOf(startMonth));
		if (!TransType.equals("TERMINATED")) {
		    TransType = isHoldEmployeeInPreviousDate(request, partyId, startMonth);
		}

	    }

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	return TransType;

    }

    //VALIDATION : check if employee terminated or no in month want to calculate
    public static boolean isTerminated_Employee_ByMonth(HttpServletRequest request, String partyId, LocalDate startMonth, LocalDate endMonth) throws ParseException, IOException {
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	boolean isTerminated = false;
	try {
	    List<EntityExpr> exprsMain = UtilMisc.toList(EntityCondition.makeCondition("TransDate", EntityOperator.GREATER_THAN_EQUAL_TO, java.sql.Date.valueOf(startMonth)),
		    EntityCondition.makeCondition("TransDate", EntityOperator.LESS_THAN_EQUAL_TO, java.sql.Date.valueOf(endMonth)),
		    EntityCondition.makeCondition("TransType", EntityOperator.EQUALS, "TERMINATED"),
		    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId));
	    EntityCondition conditionMain = EntityCondition.makeCondition(exprsMain, EntityOperator.AND);
	    System.out.println("conditionMain : " + conditionMain);

	    List<GenericValue> result = delegator.findList("EmplSalaryGrade", conditionMain, null, null, null, true);
	    if (!result.isEmpty()) {

		isTerminated = true;

	    }

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	return isTerminated;

    }

    // VALIDATION : check if employee hold or no
    public static String isHoldEmployeeInPreviousDate(HttpServletRequest request, String partyId, LocalDate transDate) throws ParseException, IOException {
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String TransType = "";
	try {
	    List<EntityExpr> exprsMain = UtilMisc.toList(EntityCondition.makeCondition("TransDate", EntityOperator.LESS_THAN, java.sql.Date.valueOf(transDate)),
		    EntityCondition.makeCondition("TransType", EntityOperator.EQUALS, "HOLD"),
		    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId),
		    EntityCondition.makeCondition("endDate", null));
	    EntityCondition conditionMain = EntityCondition.makeCondition(exprsMain, EntityOperator.AND);
	    System.out.println("conditionMain : " + conditionMain);

	    List<GenericValue> result = delegator.findList("EmplSalaryGrade", conditionMain, null, null, null, true);
	    if (!result.isEmpty()) {
		for (GenericValue row : result) {

		    TransType = (String) row.get("TransType");

		}

	    }

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	return TransType;

    }

    //For Two Times
    //returnType means if return resualt if Positive or negative or All
    public static String timesCalculation(String firstTime, String secondTime, String operator, String returnType) throws ParseException {
	int hh = 0;
	int mm = 0;
	int ss = 0;

	int firstTimeSum = 0;
	int secondTimeSum = 0;
	int resualt = 0;

	String[] time1Split = firstTime.split(":");
	String[] time2Split = secondTime.split(":");

	if (time1Split.length == 3) {
	    firstTimeSum = (Integer.parseInt(time1Split[0]) * 3600) + (Integer.parseInt(time1Split[1]) * 60) + Integer.parseInt(time1Split[2]);
	    secondTimeSum = (Integer.parseInt(time2Split[0]) * 3600) + (Integer.parseInt(time2Split[1]) * 60) + Integer.parseInt(time2Split[2]);
	} else if (time1Split.length == 2) {
	    firstTimeSum = (Integer.parseInt(time1Split[0]) * 3600) + (Integer.parseInt(time1Split[1]) * 60);
	    secondTimeSum = (Integer.parseInt(time2Split[0]) * 3600) + (Integer.parseInt(time2Split[1]) * 60);
	} else if (time1Split.length == 1) {
	    firstTimeSum = (Integer.parseInt(time1Split[0]) * 3600);
	    secondTimeSum = (Integer.parseInt(time2Split[0]) * 3600);
	}

	switch (operator) {
	    case "add":
		resualt = firstTimeSum + secondTimeSum;
		break;
	    case "sub":
		resualt = firstTimeSum - secondTimeSum;
		break;
	}

	switch (returnType) {
	    case "positive":
		if (resualt > 0) {
		    hh = resualt / 3600;
		    resualt %= 3600;
		    mm = resualt / 60;
		    resualt %= 60;
		    ss = resualt;
		    return hh + ":" + mm + ":" + ss;
		}
	    case "negative":
		if (resualt < 0) {
		    hh = resualt / 3600;
		    resualt %= 3600;
		    mm = resualt / 60;
		    resualt %= 60;
		    ss = resualt;
		    return hh + ":" + mm + ":" + ss;
		}
	    case "all":
		hh = resualt / 3600;
		resualt %= 3600;
		mm = resualt / 60;
		resualt %= 60;
		ss = resualt;
		return hh + ":" + mm + ":" + ss;
	}
	return hh + ":" + mm + ":" + ss;
    }

    // return Integer Value
    public static int timesCalculation(String firstTime, String secondTime, String operator) throws ParseException {

	int hh = 0;
	int mm = 0;
	int ss = 0;

	int firstTimeSum = 0;
	int secondTimeSum = 0;
	int resualt = 0;

	String[] time1Split = firstTime.split(":");
	String[] time2Split = secondTime.split(":");

	if (time1Split.length == 3) {
	    firstTimeSum = (Integer.parseInt(time1Split[0]) * 3600) + (Integer.parseInt(time1Split[1]) * 60) + Integer.parseInt(time1Split[2]);
	    secondTimeSum = (Integer.parseInt(time2Split[0]) * 3600) + (Integer.parseInt(time2Split[1]) * 60) + Integer.parseInt(time2Split[2]);
	} else if (time1Split.length == 2) {
	    firstTimeSum = (Integer.parseInt(time1Split[0]) * 3600) + (Integer.parseInt(time1Split[1]) * 60);
	    secondTimeSum = (Integer.parseInt(time2Split[0]) * 3600) + (Integer.parseInt(time2Split[1]) * 60);
	} else if (time1Split.length == 1) {
	    firstTimeSum = (Integer.parseInt(time1Split[0]) * 3600);
	    secondTimeSum = (Integer.parseInt(time2Split[0]) * 3600);
	}

	switch (operator) {
	    case "add":
		resualt = firstTimeSum + secondTimeSum;
		break;
	    case "sub":
		resualt = firstTimeSum - secondTimeSum;
		break;
	}
	return resualt;
    }

    //For List OF Times
    //returnType means if return resualt if Positive or negative or All
    public static String timesCalculation(ArrayList<String> timesList, String operator, String returnType) throws ParseException {
	int hh = 0;
	int mm = 0;
	int ss = 0;
	int total_hh = 0;
	int total_mm = 0;
	int total_ss = 0;
	int resualt = 0;

	String[] timeSplit = null;
	switch (operator) {
	    case "add":
		for (String time : timesList) {
		    timeSplit = time.split(":");
		    total_hh += Integer.parseInt(timeSplit[0]);
		    total_mm += Integer.parseInt(timeSplit[1]);
		    total_ss += Integer.parseInt(timeSplit[2]);
		}
		break;
	    case "sub":
		for (String time : timesList) {
		    timeSplit = time.split(":");
		    total_hh -= Integer.parseInt(timeSplit[0]);
		    total_mm -= Integer.parseInt(timeSplit[1]);
		    total_ss -= Integer.parseInt(timeSplit[2]);
		}
		break;
	}

	resualt = (total_hh * 3600) + (total_mm * 60) + total_ss;
	switch (returnType) {
	    case "positive":
		if (resualt > 0) {
		    hh = resualt / 3600;
		    resualt %= 3600;
		    mm = resualt / 60;
		    resualt %= 60;
		    ss = resualt;
		    return hh + ":" + mm + ":" + ss;
		}
		break;
	    case "negative":
		if (resualt < 0) {
		    hh = resualt / 3600;
		    resualt %= 3600;
		    mm = resualt / 60;
		    resualt %= 60;
		    ss = resualt;
		    return hh + ":" + mm + ":" + ss;
		}
		break;
	    case "all":
		hh = resualt / 3600;
		resualt %= 3600;
		mm = resualt / 60;
		resualt %= 60;
		ss = resualt;
		return hh + ":" + mm + ":" + ss;
	}

	return hh + ":" + mm + ":" + ss;
    }

    public static EmployeePosition getEmployeePositionDetails(HttpServletRequest request, String partyId, int month, int year) throws SQLException, IOException, IOException, ParseException {

//        java.sql.Date LocalTransDateMax = PayrollServices.getLastActive_TransDate_Employee_SalaryGrade(request, partyId);
	java.sql.Date LocalTransDateMax = PayrollServices.getLastActive_TransDate_Employee_SalaryCalculation(request, partyId, month, year);
	String maxSeq = PayrollServices.getMaxSequence_PositionGradation(request, partyId, LocalTransDateMax);

	Map<String, Object> creiteria = new HashMap<String, Object>();
	creiteria.put("partyId", partyId);
	creiteria.put("TransDate", LocalTransDateMax);
	creiteria.put("emplSalaryGrade", maxSeq);
	EmployeePosition employee = null;
	Delegator delegator = (Delegator) request.getAttribute("delegator");

	try {
	    //    get emplPostionId field from EmplPositionFulfillment Table
	    List<GenericValue> list = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(creiteria), null, null, null, true);
	    if (PayrollServices.getEmployeeStatus(request, partyId).equals("HIRED")) {
		if (!list.isEmpty()) {

		    for (GenericValue row : list) {
			employee = new EmployeePosition();
			employee.setPartyId((String) row.get("partyId"));
			employee.setGradeId((String) row.get("gradeId"));
			employee.setDegreeId((String) row.get("degreeId"));
			employee.setJobGroupId((String) row.get("jobGroupId"));
			employee.setPartyIdFrom((String) row.get("partyIdFrom"));
			employee.setBasicSalary((BigDecimal) PayrollServices.getBasicSalaryByDegreeId(request, (String) row.get("degreeId")));
			employee.setTransDate((java.sql.Date) row.get("TransDate"));
			employee.setTransType((String) row.get("TransType"));
			employee.setEndDate((java.sql.Date) row.get("endDate"));
			employee.setEmplPositionId((String) row.get("emplPositionId"));
			employee.setNotes((String) row.get("notes"));
			employee.setVacationId((String) row.get("vacationId"));
			employee.setUserLoginId((String) row.get("userLoginId"));
		    }
		}
	    }

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	System.out.println("employeePostion Details " + employee);
	return employee;
    }

    public static EmployeePosition getEmployeePositionDetails(HttpServletRequest request, String partyId) throws SQLException, IOException, IOException, ParseException {

	java.sql.Date LocalTransDateMax = PayrollServices.getLastActive_TransDate_Employee_SalaryGrade(request, partyId);
	String maxSeq = PayrollServices.getMaxSequence_PositionGradation(request, partyId, LocalTransDateMax);

	Map<String, Object> creiteria = new HashMap<String, Object>();
	creiteria.put("partyId", partyId);
	creiteria.put("TransDate", LocalTransDateMax);
	creiteria.put("emplSalaryGrade", maxSeq);
	EmployeePosition employee = null;
	Delegator delegator = (Delegator) request.getAttribute("delegator");

	try {
	    //    get emplPostionId field from EmplPositionFulfillment Table
	    List<GenericValue> list = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(creiteria), null, null, null, true);
	    if (PayrollServices.getEmployeeStatus(request, partyId).equals("HIRED")) {
		if (!list.isEmpty()) {

		    for (GenericValue row : list) {
			employee = new EmployeePosition();
			employee.setPartyId((String) row.get("partyId"));
			employee.setEmplSalaryGradeId((String) row.get("emplSalaryGrade"));
			employee.setGradeId((String) row.get("gradeId"));
			employee.setDegreeId((String) row.get("degreeId"));
			employee.setJobGroupId((String) row.get("jobGroupId"));
			employee.setPartyIdFrom((String) row.get("partyIdFrom"));
			employee.setBasicSalary((BigDecimal) PayrollServices.getBasicSalaryByDegreeId(request, (String) row.get("degreeId")));
			employee.setTransDate((java.sql.Date) row.get("TransDate"));
			employee.setTransType((String) row.get("TransType"));
			employee.setEndDate((java.sql.Date) row.get("endDate"));
			employee.setEmplPositionId((String) row.get("emplPositionId"));
			employee.setNotes((String) row.get("notes"));
			employee.setVacationId((String) row.get("vacationId"));
			employee.setUserLoginId((String) row.get("userLoginId"));
		    }
		}
	    }

	} catch (GenericEntityException ex) {
	    Logger.getLogger(PersonUtilServices.class.getName()).log(Level.SEVERE, null, ex);
	}
	System.out.println("employeePostion Details " + employee);
	return employee;
    }

    public static String storeSessionFromMyPortal(HttpServletRequest request, HttpServletResponse response) {

	String foLang = request.getParameter("foLang");
	String userLoginId = request.getParameter("userLoginId");

		System.out.println("FOLANG ss "+foLang);
	HttpSession session = request.getSession();
	session.setAttribute("FOLANG", foLang);
	session.setAttribute("userLoginId", userLoginId);
		System.out.println(">> "+session.getAttribute("FOLANG"));

	return ""; 
    }

}
