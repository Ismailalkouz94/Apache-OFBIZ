package org.apache.ofbiz.humanres.loans;

import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.humanres.PayrollServices;
import org.apache.ofbiz.humanres.employee.Employee;
import org.apache.ofbiz.humanres.vacations.VacationsServices;
import org.apache.ofbiz.service.LocalDispatcher;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoansServices {
    public static final String resourceError = "HumanResErrorUiLabels";

    public static String createLoan(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> messageMap;
        messageMap = UtilMisc.toMap("successMessage", "Created SuccessFully");

        String partyId = request.getParameter("partyId");
        String description = request.getParameter("description");
        String amount = request.getParameter("amount");
        String loanDate = request.getParameter("loanDate");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        List<GenericValue> toBeStored = new LinkedList<GenericValue>();
        try {

            GenericValue gvValue = delegator.makeValue("ExpLoan");

            gvValue.set("expenseId", delegator.getNextSeqId("EXPENSE_ID"));
            gvValue.set("partyId", partyId);
            gvValue.set("description", description);
            gvValue.set("amount", new BigDecimal(amount));
            gvValue.set("loanDate", java.sql.Date.valueOf(loanDate));
            gvValue.set("startDate", java.sql.Date.valueOf(startDate));
            gvValue.set("endDate", java.sql.Date.valueOf(endDate));
            gvValue.set("userLoginId", PayrollServices.getUserLoginId(request));

            try {
                gvValue.create();
            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
                messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
                String errMsg = UtilProperties.getMessage(resourceError, e.getMessage(), messageMap, UtilHttp.getLocale(request));
                request.setAttribute("_ERROR_MESSAGE_", errMsg);
                return "error";
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println();
            out.flush();
        } catch (Exception ex) {
            ex.getMessage();
            ex.printStackTrace();
            messageMap = UtilMisc.toMap("errorMessage", ex.getMessage());
            String errMsg = UtilProperties.getMessage(resourceError, ex.getMessage(), messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        String successMsg = UtilProperties.getMessage(resourceError, "Created SuccessFully", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", successMsg);
        return "success";
    }

    public static String getLoansData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String partyId = request.getParameter("partyId");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyId", partyId);

        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {

            List<GenericValue> list = delegator.findList("ExpLoan", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    jsonRes = new JSONObject();
                    jsonRes.put("expenseId", row.get("expenseId"));
                    jsonRes.put("partyId", row.get("partyId"));
                    jsonRes.put("description", row.get("description"));
                    jsonRes.put("amount", row.get("amount"));
                    if (row.get("loanDate") == null) {
                        jsonRes.put("loanDate", row.get("loanDate"));
                    } else {
                        jsonRes.put("loanDate", row.get("loanDate").toString());
                    }

                    if (row.get("startDate") == null) {
                        jsonRes.put("startDate", row.get("startDate"));
                    } else {
                        jsonRes.put("startDate", row.get("startDate").toString());
                    }

                    if (row.get("endDate") == null) {
                        jsonRes.put("endDate", row.get("endDate"));
                    } else {
                        jsonRes.put("endDate", row.get("endDate").toString());
                    }

                    jsonRes.put("userLoginId", row.get("userLoginId"));

                    jsonData.add(jsonRes);
                }
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(LoansServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String createLoanSchedule(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");


        Map<String, String> messageMap;
        messageMap = UtilMisc.toMap("successMessage", "Created SuccessFully");

        String partyId = request.getParameter("partyId");
        String expenseId = request.getParameter("expenseId");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String amount = request.getParameter("amount");

        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        String userLoginId = userLogin.getString("userLoginId");

        LocalDate startDate_localDate = java.sql.Date.valueOf(startDate).toLocalDate();
        LocalDate endDate_localDate = java.sql.Date.valueOf(endDate).toLocalDate();


        Map<String, Object> loanScheduleCriteria = new HashMap<>();
        Map<String, Object> emplAllowenceCriteria = new HashMap<>();
        String loanAllowenceId = null;
        try {
            while (startDate_localDate.isBefore(endDate_localDate) || startDate_localDate.equals(endDate_localDate)) {
                loanScheduleCriteria = UtilMisc.toMap(
                        "loanScheduleId", delegator.getNextSeqId("LoanSchedule"),
                        "expenseId", expenseId,
                        "year", startDate_localDate.getYear() + "",
                        "month", startDate_localDate.getMonthValue() + "",
                        "amount", new BigDecimal(amount),
                        "status", "UN-PAID",
                        "userLoginId", userLoginId
                );
                Map<String, Object> createLoanScheduleService = dispatcher.runSync("createLoanScheduleService", loanScheduleCriteria);

                // to get loan allowenceId
                loanAllowenceId = PayrollServices.getGlobalSetting(request, "LOAN_ALLOWANCE_ID", "LOAN");
                // to insert loan allowences
                java.sql.Date ourJavaDateObject = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                emplAllowenceCriteria = UtilMisc.toMap(
                        "Id", delegator.getNextSeqId("CompanyEmployeeAllowences"),
                        "typeId", "EMP",
                        "allowenceId", loanAllowenceId,
                        "partyId", partyId,
                        "TransDate", ourJavaDateObject,
                        "calculationId", "AMOUNT",
                        "percentageAmount", BigDecimal.valueOf(0.0),
                        "calculationAmount", BigDecimal.valueOf(0.0),
                        "amount", new BigDecimal(amount),
                        "startDate", java.sql.Date.valueOf(startDate_localDate),
                        "endDate", java.sql.Date.valueOf(startDate_localDate.getYear() + "-" + startDate_localDate.getMonthValue() + "-" + startDate_localDate.lengthOfMonth()),
                        "stabilityId", "TEMP",
                        "userLoginId", userLoginId
                );
                Map<String, Object> createEmployeeAllowence = dispatcher.runSync("createAllowenceEmployee", emplAllowenceCriteria);

                startDate_localDate = startDate_localDate.plusMonths(1);
            }
            try {
            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
                messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
                String errMsg = UtilProperties.getMessage(resourceError, e.getMessage(), messageMap, UtilHttp.getLocale(request));
                request.setAttribute("_ERROR_MESSAGE_", errMsg);
                return "error";
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println();
            out.flush();
        } catch (Exception ex) {
            ex.getMessage();
            ex.printStackTrace();
            messageMap = UtilMisc.toMap("errorMessage", ex.getMessage());
            String errMsg = UtilProperties.getMessage(resourceError, ex.getMessage(), messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        String successMsg = UtilProperties.getMessage(resourceError, "Created SuccessFully", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", successMsg);
        return "success";
    }

    public static String getLoanScheduleData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String expenseId = request.getParameter("expenseId");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("expenseId", expenseId);

        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {

            List<GenericValue> list = delegator.findList("LoanSchedule", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    jsonRes = new JSONObject();
                    jsonRes.put("loanScheduleId", row.get("loanScheduleId"));
                    jsonRes.put("expenseId", row.get("expenseId"));
                    jsonRes.put("year", row.get("year"));
                    jsonRes.put("month", row.get("month"));
                    jsonRes.put("amount", row.get("amount"));
                    jsonRes.put("status", row.get("status"));
                    jsonRes.put("userLoginId", row.get("userLoginId"));

                    jsonData.add(jsonRes);
                }
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(LoansServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getEndDateByStartDate(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String startDate = request.getParameter("startDate");
        String noOfMonths = request.getParameter("noOfMonths");
        String result=null;
        try {
            LocalDate startDateLocalDate = java.sql.Date.valueOf(startDate).toLocalDate();
            startDateLocalDate = startDateLocalDate.plusMonths(new Long(noOfMonths)-1);

            result = startDateLocalDate.getYear()+"-"+startDateLocalDate.getMonthValue()+"-"+startDateLocalDate.lengthOfMonth();

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(result);
            out.flush();
        } catch (Exception ex) {
            Logger.getLogger(LoansServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

}

