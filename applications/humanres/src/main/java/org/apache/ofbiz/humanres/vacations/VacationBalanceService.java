package org.apache.ofbiz.humanres.vacations;

import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.humanres.PayrollServices;
import org.apache.ofbiz.humanres.PersonUtilServices;
import org.apache.ofbiz.humanres.timeAttendance.TimeAttendanceServices;
import org.apache.ofbiz.minilang.method.envops.While;
import org.joda.time.Days;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VacationBalanceService {


    public static String getVacationBalanceData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String partyId = request.getParameter("partyId");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");


        java.sql.Date sqlFromDate = PayrollServices.convertFromStringToDate(fromDate);
        java.sql.Date sqlToDate = PayrollServices.convertFromStringToDate(toDate);

        LocalDate fromDate_LocalDate = PayrollServices.convertFromStringToDate(fromDate).toLocalDate();
        LocalDate toDate_LocalDate = PayrollServices.convertFromStringToDate(toDate).toLocalDate();

        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = null;
        try {
            LocalDate toDateTemp_LocalDate = PayrollServices.convertFromStringToDate(toDate).toLocalDate();

//            while (fromDate_LocalDate.isBefore(toDate_LocalDate)) {

            Map<String, Integer> vacationDays = new HashMap<String, Integer>();
            int annual = 0;
            int conractdays = 0;
            int transactionDays = 0;

//                if (fromDate_LocalDate.plusYears(1).isBefore(toDate_LocalDate)) {
//                    jsonRes.put("toDate", fromDate_LocalDate.plusYears(1).minusDays(1).toString());
//                    vacationDays = getVcationDays(request, partyId, fromDate_LocalDate, fromDate_LocalDate.plusYears(1).minusDays(1));
//                } else {
//            jsonRes.put("toDate", toDate_LocalDate.toString());

//                }
//            jsonRes.put("fromDate", fromDate_LocalDate.toString());

            vacationDays = getVcationDays(request, partyId, fromDate_LocalDate, toDate_LocalDate);

            List<GenericValue> transactionDaysData = getTransactionDays(request, partyId, fromDate_LocalDate, toDate_LocalDate);
            if (!transactionDaysData.isEmpty()) {
                for (GenericValue items : transactionDaysData) {
                    if (items.get("action").toString().equals("+")) {
                        transactionDays += Integer.parseInt(items.get("noDays").toString());
                    } else {
                        transactionDays -= Integer.parseInt(items.get("noDays").toString());
                    }
                }
            }

            if (!vacationDays.isEmpty()) {
                annual = vacationDays.get("annual");
            }
//            ..... for annual row
            jsonRes = new JSONObject();
            float vacation_day_messaure = 0;
            int countDays = 0;
            int balanceResult = 0;

            jsonRes.put("type", "ANNUAL");
            jsonRes.put("takeIt", annual);
            conractdays = getContractDays(request, partyId, fromDate_LocalDate, toDate_LocalDate);
            countDays = (int) ChronoUnit.DAYS.between(fromDate_LocalDate, toDate_LocalDate.plusDays(1));
            vacation_day_messaure = (float) conractdays / 365;
            DecimalFormat df = new DecimalFormat("#.#####");
            df.setRoundingMode(RoundingMode.CEILING);

            System.out.println(">> vacation_day_messaure >>" + vacation_day_messaure);
            System.out.println(">> df.format             >>" + df.format(vacation_day_messaure));
            vacation_day_messaure = Float.parseFloat(df.format(vacation_day_messaure));
            balanceResult = (int) Math.floor(vacation_day_messaure * countDays) + transactionDays;
            jsonRes.put("balance", balanceResult);
            jsonRes.put("remaining", balanceResult - annual);
            jsonData.add(jsonRes);

//            ...... for sick row
            jsonRes = new JSONObject();
            jsonRes.put("type", "SICK");
            jsonRes.put("takeIt", vacationDays.get("sick"));
            int sickVacation = Integer.parseInt(PayrollServices.getGlobalSetting(request, "SICK_VACATION", "VACATION_DAYS"));
            jsonRes.put("balance", sickVacation);
            jsonRes.put("remaining", sickVacation - (int) vacationDays.get("sick"));
            jsonData.add(jsonRes);

//           ...... for sick(surgery) row
            jsonRes = new JSONObject();
            jsonRes.put("type", "SICK(SURGERY)");
            jsonRes.put("takeIt", vacationDays.get("sickSurgery"));
            int sickSurgeryVacation = Integer.parseInt(PayrollServices.getGlobalSetting(request, "SICK_VACATION_SURGERY", "VACATION_DAYS"));
            jsonRes.put("balance", sickSurgeryVacation);
            jsonRes.put("remaining", sickSurgeryVacation - vacationDays.get("sickSurgery"));
            jsonData.add(jsonRes);

            System.out.println("vacationDays " + vacationDays);

//                fromDate_LocalDate = fromDate_LocalDate.plusYears(1);
//                toDateTemp_LocalDate = toDateTemp_LocalDate.plusYears(1);
//            }


            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (Exception ex) {
            Logger.getLogger(VacationBalanceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }


    public static Map getVcationDays(HttpServletRequest request, String partyId, LocalDate fromDate, LocalDate toDate) throws
            ParseException, IOException, GenericEntityException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, Integer> finalResult = new HashMap();
        List<String> vacationIds = new ArrayList<String>();
        int annual = 0;
        int sick = 0;
        int sickSurgery = 0;

        System.out.println("getVcationDays fromDate " + fromDate + " ---- toDate " + toDate);

        try {
            toDate = toDate.plusDays(1);

            List<EntityExpr> exprs = UtilMisc.toList(
                    EntityCondition.makeCondition("fromDate", EntityOperator.GREATER_THAN_EQUAL_TO, PersonUtilServices.convertStringToTimestamp(fromDate.toString(), false)),
                    EntityCondition.makeCondition("thruDate", EntityOperator.LESS_THAN_EQUAL_TO, PersonUtilServices.convertStringToTimestamp(toDate.toString(), false)),
                    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId),
                    EntityCondition.makeCondition("leaveStatus", EntityOperator.EQUALS, "LEAVE_APPROVED"),
                    EntityCondition.makeCondition("type", EntityOperator.EQUALS, "VACATION")
            );

//            List<EntityExpr> exprs = UtilMisc.toList(
//                    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId),
//                    EntityCondition.makeCondition("leaveStatus", EntityOperator.EQUALS, "LEAVE_APPROVED"),
//                    EntityCondition.makeCondition("type", EntityOperator.EQUALS, "VACATION")
//            );

            EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);
            List<GenericValue> list = delegator.findList("EmplLeave", condition, null, null, null, true);
            System.out.println("list " + list);
            List<DateList> listOfDate=new ArrayList<DateList>();
            if (!list.isEmpty()) {

//                for (GenericValue items : list) {
//                    LocalDate vacationFromDate=PayrollServices.convertFromStringToDate(items.get("fromDate").toString()).toLocalDate();
//                    LocalDate vacationThruDate=PayrollServices.convertFromStringToDate(items.get("thruDate").toString()).toLocalDate();
//                    while( vacationFromDate.isBefore(vacationThruDate)|| vacationFromDate.equals(vacationThruDate)){
//                        System.out.println(">> from While ");
//                        System.out.println("vacationFromDate " + vacationFromDate);
//                        System.out.println("vacationThrDuate " + vacationThruDate);
//                        DateList dateList = new DateList();
//                        dateList.setDate(Date.valueOf(vacationFromDate));
//                        listOfDate.add(dateList);
//                        vacationFromDate=vacationFromDate.plusDays(1);
//                    }
//                }
//                 for (DateList dateListItems : listOfDate){
//                     System.out.println("..............");
//                     System.out.println("listOfDate "+dateListItems.getDate());
//                     System.out.println("..............");
//                 }

                System.out.println("list not empty");
                Map<String, Object> vcationTypeDays = getVcationTypeDays(request, list, vacationIds);
                annual += (int) vcationTypeDays.get("annual");
                sick += (int) vcationTypeDays.get("sick");
                sickSurgery += (int) vcationTypeDays.get("sickSurgery");
                vacationIds.add((String) vcationTypeDays.get("vacationId"));
                System.out.println("vcationTypeDays " + vcationTypeDays);
            }


//            ...........2.............

//            List<EntityExpr> exprs2 = UtilMisc.toList(
//                    EntityCondition.makeCondition("fromDate", EntityOperator.GREATER_THAN_EQUAL_TO, PersonUtilServices.convertStringToTimestamp(fromDate.toString(), false)),
//                    EntityCondition.makeCondition("fromDate", EntityOperator.LESS_THAN_EQUAL_TO, PersonUtilServices.convertStringToTimestamp(toDate.toString(), false)),
//                    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId),
//                    EntityCondition.makeCondition("leaveStatus", EntityOperator.EQUALS, "LEAVE_APPROVED"),
//                    EntityCondition.makeCondition("type", EntityOperator.EQUALS, "VACATION")
//            );
//            EntityCondition condition2 = EntityCondition.makeCondition(exprs2, EntityOperator.AND);
//            List<GenericValue> list2 = delegator.findList("EmplLeave", condition2, null, null, null, true);
//            System.out.println("list2 " + list2);
//            if (!list.isEmpty()) {
//                System.out.println("list2 not empty");
//                Map<String, Object> vcationTypeDays = getVcationTypeDays(request, list2, vacationIds);
//                annual += (int) vcationTypeDays.get("annual");
//                sick += (int) vcationTypeDays.get("sick");
//                sickSurgery += (int) vcationTypeDays.get("sickSurgery");
//                vacationIds.add((String) vcationTypeDays.get("vacationId"));
//
//            }
//
////             ............3............
//
//            List<EntityExpr> exprs3 = UtilMisc.toList(
//                    EntityCondition.makeCondition("thruDate", EntityOperator.GREATER_THAN_EQUAL_TO, PersonUtilServices.convertStringToTimestamp(fromDate.toString(), false)),
//                    EntityCondition.makeCondition("thruDate", EntityOperator.LESS_THAN_EQUAL_TO, PersonUtilServices.convertStringToTimestamp(toDate.toString(), false)),
//                    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId),
//                    EntityCondition.makeCondition("leaveStatus", EntityOperator.EQUALS, "LEAVE_APPROVED"),
//                    EntityCondition.makeCondition("type", EntityOperator.EQUALS, "VACATION")
//            );
//            EntityCondition condition3 = EntityCondition.makeCondition(exprs3, EntityOperator.AND);
//            List<GenericValue> list3 = delegator.findList("EmplLeave", condition3, null, null, null, true);
//            System.out.println("list3 " + list3);
//            if (!list.isEmpty()) {
//                System.out.println("list3 not empty");
//                Map<String, Object> vcationTypeDays = getVcationTypeDays(request, list3, vacationIds);
//                annual += (int) vcationTypeDays.get("annual");
//                sick += (int) vcationTypeDays.get("sick");
//                sickSurgery += (int) vcationTypeDays.get("sickSurgery");
//                vacationIds.add((String) vcationTypeDays.get("vacationId"));
//            }

            finalResult.put("annual", annual);
            finalResult.put("sick", sick);
            finalResult.put("sickSurgery", sickSurgery);

        } catch (GenericEntityException ex) {
            Logger.getLogger(VacationBalanceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return finalResult;
    }


    public static Map getVcationTypeDays(HttpServletRequest request, List<GenericValue> list, List<String> vacationIds) throws IOException, ParseException {
        Map<String, Object> result = new HashMap<String, Object>();
        int annual = 0;
        int sick = 0;
        int sickSurgery = 0;
        String vacationId = null;
        try {
            for (GenericValue items : list) {
                if (!vacationIds.contains(items.get("leaveId").toString())) {
                    switch (items.get("emplLeaveReasonTypeId").toString()) {
                        case "PERSONAL":
                            annual += countVacationDays(request,
                                    PayrollServices.convertFromStringToDate(items.get("fromDate").toString()).toLocalDate(),
                                    PayrollServices.convertFromStringToDate(items.get("thruDate").toString()).toLocalDate());
                            break;
                        case "SICK":
                            sick += countVacationDays(request,
                                    PayrollServices.convertFromStringToDate(items.get("fromDate").toString()).toLocalDate(),
                                    PayrollServices.convertFromStringToDate(items.get("thruDate").toString()).toLocalDate());
                            break;
                        case "SICK_SURGERY":
                            sickSurgery += countVacationDays(request,
                                    PayrollServices.convertFromStringToDate(items.get("fromDate").toString()).toLocalDate(),
                                    PayrollServices.convertFromStringToDate(items.get("thruDate").toString()).toLocalDate());
                            break;
                        default:
                            break;
                    }
                    vacationId = items.get("leaveId").toString();
                }
            }

            result.put("annual", annual);
            result.put("sick", sick);
            result.put("sickSurgery", sickSurgery);
            result.put("vacationId", vacationId);
        } catch (Exception ex) {
            Logger.getLogger(VacationBalanceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }


    // count vacation days except offDays and public Holidays
    public static int countVacationDays(HttpServletRequest request, LocalDate fromDate, LocalDate thruDate) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        int countDays = 0;
        int offDaysCount = 0;
        try {

            LocalDate newStartDate = fromDate;
            while (newStartDate.isBefore(thruDate) || newStartDate.isEqual(thruDate)) {
                if (newStartDate.getDayOfWeek().toString().toLowerCase().equals(PayrollServices.getGlobalPayrollSetting(request, "FIRST_OFFDAY").toLowerCase()) || newStartDate.getDayOfWeek().toString().toLowerCase().equals(PayrollServices.getGlobalPayrollSetting(request, "SECOND_OFFDAY").toLowerCase())) {
                    offDaysCount++;
                }
                newStartDate = newStartDate.plusDays(1);
            }

            countDays = (int) ChronoUnit.DAYS.between(fromDate, thruDate.plusDays(1));
            countDays = countDays - TimeAttendanceServices.getOficialHolidays(request, fromDate, thruDate) - offDaysCount;
        } catch (Exception ex) {
            Logger.getLogger(VacationBalanceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return countDays;
    }


    public static int getContractDays(HttpServletRequest request, String partyId, LocalDate fromDate, LocalDate toDate) throws
            ParseException, IOException, GenericEntityException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        int days = 0;
        try {
            List<EntityExpr> exprs = UtilMisc.toList(
                    EntityCondition.makeCondition("fromDate", EntityOperator.LESS_THAN_EQUAL_TO, PayrollServices.convertFromStringToDate(fromDate.toString())),
//                    EntityCondition.makeCondition("toDate", EntityOperator.GREATER_THAN_EQUAL_TO, PayrollServices.convertFromStringToDate(toDate.toString())),
                    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId)
            );
            EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);
            List<GenericValue> list = delegator.findList("PartyVacationContract", condition, null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue items : list) {
                    days = Integer.parseInt(items.get("value").toString());
                }
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(VacationBalanceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return days;
    }


    public static List<GenericValue> getTransactionDays(HttpServletRequest request, String partyId, LocalDate fromDate, LocalDate toDate) throws
            ParseException, IOException, GenericEntityException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        int days = 0;
        Map<String, String> criteria = new HashMap<String, String>();
        ArrayList<Map<Object, Object>> transactionResult = new ArrayList<>();
        List<GenericValue> list = null;
        try {
            List<EntityExpr> exprs = UtilMisc.toList(
                    EntityCondition.makeCondition("transDate", EntityOperator.GREATER_THAN_EQUAL_TO, PayrollServices.convertFromStringToDate(fromDate.toString())),
                    EntityCondition.makeCondition("transDate", EntityOperator.LESS_THAN_EQUAL_TO, PayrollServices.convertFromStringToDate(toDate.toString())),
                    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId)
            );
            EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);
            list = delegator.findList("PartyVacationTransaction", condition, null, null, null, true);

        } catch (GenericEntityException ex) {
            Logger.getLogger(VacationBalanceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }


}
