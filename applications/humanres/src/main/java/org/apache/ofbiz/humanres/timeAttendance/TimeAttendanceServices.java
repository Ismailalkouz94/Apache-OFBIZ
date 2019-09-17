/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.humanres.timeAttendance;

import java.io.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.*;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.humanres.HumanResEvents;
import org.apache.ofbiz.humanres.PayrollServices;
import org.apache.ofbiz.humanres.PersonUtilServices;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author Admin
 */
public class TimeAttendanceServices {

    public static Map<String, Object> ImportTimeATTFile(DispatchContext dctx, Map<String, Object> context) throws ParseException, IOException, SQLException, GenericEntityException {
        Locale locale = (Locale) context.get("locale");

        String yearParam = (String) context.get("year");
        String monthParam = (String) context.get("month");
        System.out.println("year " + yearParam);
        System.out.println("month " + monthParam);

        Delegator delegator = dctx.getDelegator();

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String userLoginId = (String) userLogin.getString("userLoginId");

        LocalDate startDate_TAMCLocalDate = null;
        LocalDate endDate_TAMCLocalDate = null;

        // To Get OFF Days From Global Payroll Settings
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("globalKey", "TIME_ATTENDANCE");
        String firstOFFDay = "";
        String secondOFFDay = "";
        try {

            Map<String, String> removeCriteria = new HashMap<String, String>();
            removeCriteria.put("year", yearParam);
            removeCriteria.put("month", monthParam);
            int remove = delegator.removeByAnd("TimeAttendance", removeCriteria);

            List<GenericValue> GlobalPayrollSettings_list = delegator.findList("GlobalPayrollSettings", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue GPSrow : GlobalPayrollSettings_list) {
                if (GPSrow.get("Key").toString().equals("FIRST_OFFDAY")) {
                    firstOFFDay = GPSrow.get("Value").toString();
                }
                if (GPSrow.get("Key").toString().equals("SECOND_OFFDAY")) {
                    secondOFFDay = GPSrow.get("Value").toString();
                }

            }

            // To Get Start Date And End Date Of Month From Time Attendance Config
            // "TAMC" -> Time Attendance Month Config 
            Map<String, String> criteria2 = new HashMap<String, String>();
            criteria2.put("year", yearParam);
            criteria2.put("month", monthParam);
            List<GenericValue> timeAttendanceMonthConfig_list = delegator.findList("TimeAttendanceConfig", EntityCondition.makeCondition(criteria2), null, null, null, true);
            System.out.println("timeAttendanceMonthConfig_list : " + timeAttendanceMonthConfig_list);

//            startDate_TAMC = timeAttendanceMonthConfig_list.get(0).get("startDate").toString();
            String line[] = timeAttendanceMonthConfig_list.get(0).get("startDate").toString().split("-");
            startDate_TAMCLocalDate = LocalDate.of(Integer.valueOf(line[0]), Integer.valueOf(line[1]), Integer.valueOf(line[2]));
            System.out.println("startDate_TAMCLocalDate " + startDate_TAMCLocalDate);

//            endDate_TAMC = timeAttendanceMonthConfig_list.get(0).get("endDate").toString();
            String line2[] = timeAttendanceMonthConfig_list.get(0).get("endDate").toString().split("-");
            endDate_TAMCLocalDate = LocalDate.of(Integer.valueOf(line2[0]), Integer.valueOf(line2[1]), Integer.valueOf(line2[2]));
            System.out.println("endDate_TAMCLocalDate " + endDate_TAMCLocalDate);

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        //

        ByteBuffer fileBytes = (ByteBuffer) context.get("uploadedFile");
        ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes.array());
//        FileInputStream fileInputStream = new FileInputStream(new File(path));
        Workbook workBook = null;
        LocalDate timeAttendanceLocalDate = null;

        Row rowObj = null;
        try {
            // For .xlsx
            if (POIXMLDocument.hasOOXMLHeader(bais)) {
                workBook = new XSSFWorkbook(bais);

            } else {
                // For .xls
                workBook = new HSSFWorkbook(bais);

            }

            int intSheetsNumber = workBook.getNumberOfSheets();
            for (int sheetIndex = 0; sheetIndex < intSheetsNumber; sheetIndex++) {
                Sheet sheet = workBook.getSheetAt(sheetIndex);

                int intNumberOfRows = sheet.getPhysicalNumberOfRows();
                for (int row = 1; row < intNumberOfRows; row++) {
                    rowObj = sheet.getRow(row);
                    if (rowObj.getCell(0) != null) {

                        int intNumberOfCells = rowObj.getLastCellNum();
//                        System.out.println("party Id" + rowObj.getCell(0));
                        timeAttendanceLocalDate = PayrollServices.convertFromStringToDate(rowObj.getCell(1).toString(), "dd-MMM-yyyy").toLocalDate();
                        if ((timeAttendanceLocalDate.isEqual(startDate_TAMCLocalDate) || timeAttendanceLocalDate.isAfter(startDate_TAMCLocalDate)) && (timeAttendanceLocalDate.isEqual(endDate_TAMCLocalDate) || timeAttendanceLocalDate.isBefore(endDate_TAMCLocalDate))) {
                            GenericValue gvValue = delegator.makeValue("TimeAttendance");
                            gvValue.set("timeAttendanceId", delegator.getNextSeqId("timeAttendanceId"));
                            gvValue.set("partyId", rowObj.getCell(0).toString().replaceAll("\\.0*$", "").trim());

                            gvValue.set("AttDate", PayrollServices.convertFromStringToDate(rowObj.getCell(1).toString(), "dd-MMM-yyyy"));
                            gvValue.set("year", yearParam);
                            gvValue.set("month", monthParam);
                            gvValue.set("weekDay", timeAttendanceLocalDate.getDayOfWeek().toString());

                            if (timeAttendanceLocalDate.getDayOfWeek().toString().toLowerCase().equals(firstOFFDay.toLowerCase()) || timeAttendanceLocalDate.getDayOfWeek().toString().toLowerCase().equals(secondOFFDay.toLowerCase())) {
                                gvValue.set("dayType", "OFFDAY");
                            } else {
                                gvValue.set("dayType", "WORKDAY");
                            }
                            gvValue.set("checkIn", rowObj.getCell(2) != null ? rowObj.getCell(2).toString().trim() : "");
                            gvValue.set("checkOut", rowObj.getCell(3) != null ? rowObj.getCell(3).toString().trim() : "");
                            gvValue.set("userLoginId", userLoginId);
                            gvValue.create();
                        }
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(TimeAttendanceServices.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                workBook.close();
            } catch (IOException ex) {
                Logger.getLogger(TimeAttendanceServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
//        Map<String, Object> result = ServiceUtil.returnSuccess(UtilProperties.getMessage(resource, "AccountingNewGlCreated", UtilMisc.toMap("glCreated", glCreated), locale));
//        result.put("organizationPartyId", organizationPartyId);//organizationPartyId
//        return result;
        return result;
    }


    public static String getTimeAttendanceData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String year = request.getParameter("yearView");
        String month = request.getParameter("monthView");

//        Map<String, Object> criteria = new HashMap<String, Object>();
//        criteria.put("year", year);
//        criteria.put("month", month);
        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {
            Map<String, LocalDate> dates = new HashMap<String, LocalDate>();
            dates = getTimeAttendanceStartAndEndDate(request, year, month);

            // Date Condition From To
            List<EntityExpr> exprs = UtilMisc.toList(EntityCondition.makeCondition("AttDate", EntityOperator.GREATER_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("startDate"))),
                    EntityCondition.makeCondition("AttDate", EntityOperator.LESS_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("endDate"))));
            EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);

            List<GenericValue> list = delegator.findList("TimeAttendance", condition, null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue row : list) {
//                    String line3[] = row.get("AttDate").toString().split("-");
//                    LocalDate AttDateLocalDate = LocalDate.of(Integer.valueOf(line3[0]), Integer.valueOf(line3[1]), Integer.valueOf(line3[2]));
//                    System.out.println("AttDateLocalDate : " + AttDateLocalDate);
//                if ((AttDateLocalDate.isAfter(dates.get("startDate")) || AttDateLocalDate.isEqual(dates.get("startDate"))) && (AttDateLocalDate.isBefore(dates.get("endDate")) || AttDateLocalDate.isEqual(dates.get("endDate")))) {
                    jsonRes = new JSONObject();
                    jsonRes.put("timeAttendanceId", row.get("timeAttendanceId"));
                    jsonRes.put("partyId", row.get("partyId"));
                    jsonRes.put("cardId", row.get("cardId"));
                    jsonRes.put("AttDate", row.get("AttDate").toString());
                    jsonRes.put("weekDay", row.get("weekDay"));
                    jsonRes.put("dayType", row.get("dayType"));
                    jsonRes.put("checkIn", row.get("checkIn"));
                    jsonRes.put("checkOut", row.get("checkOut"));
                    jsonRes.put("userLoginId", row.get("userLoginId"));
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
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String getTimeAttendance_EmployeeData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String partyId = request.getParameter("partyId");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyId", partyId);
        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {
            Map<String, LocalDate> dates = new HashMap<String, LocalDate>();
            dates = getTimeAttendanceStartAndEndDate(request, year, month);

            List<EntityExpr> exprs = UtilMisc.toList(EntityCondition.makeCondition("AttDate", EntityOperator.GREATER_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("startDate"))),
                    EntityCondition.makeCondition("AttDate", EntityOperator.LESS_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("endDate"))),
                    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId));
            EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);

            List<String> listOrder = new ArrayList<>();
            listOrder.add("timeAttendanceId");
            List<GenericValue> list = delegator.findList("TimeAttendance", condition, null, listOrder, null, true);

            for (GenericValue row : list) {

                jsonRes = new JSONObject();
                jsonRes.put("timeAttendanceId", row.get("timeAttendanceId"));
                jsonRes.put("partyId", row.get("partyId"));
                jsonRes.put("cardId", row.get("cardId"));
                jsonRes.put("AttDate", row.get("AttDate").toString());
                jsonRes.put("weekDay", row.get("weekDay"));
                jsonRes.put("dayType", row.get("dayType"));
                jsonRes.put("checkIn", row.get("checkIn"));
                jsonRes.put("checkOut", row.get("checkOut"));
                if (!(row.get("checkOut") == null) && !(row.get("checkIn") == null)) {
                    jsonRes.put("work", PersonUtilServices.timesCalculation(row.get("checkOut").toString(), row.get("checkIn").toString(), "sub", "all"));
                }

                if (!(row.get("checkIn") == null)) {
                    String lateDeff = PayrollServices.getGlobalPayrollSetting(request, "TO_LATE");
                    SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                    Date checkIn = parser.parse(row.get("checkIn").toString());
                    Date toLate = parser.parse(lateDeff);

                    if (checkIn.after(toLate)) {
                        if (!checkIfDateOfficialHolidayOrOffDay(request, PayrollServices.convertFromStringToDate(row.get("AttDate").toString()).toLocalDate())) {
                            jsonRes.put("late", PersonUtilServices.timesCalculation(row.get("checkIn").toString(), lateDeff, "sub", "all"));
                        }
                    }
                }

//                    jsonRes.put("notes", checkIfDateIsVacationOrLeave(request, row.get("AttDate").toString(), dates.get("startDate").toString(), dates.get("endDate").toString(), row.get("partyId").toString()));
                jsonRes.put("userLoginId", row.get("userLoginId"));
                jsonData.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    public static String checkIfDateIsVacationOrLeave(HttpServletRequest request, String date, String monthstartDate, String monthEndDate, String partyId) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        String result = null;
        try {
            LocalDate date_LC = PayrollServices.convertFromStringToDate(date).toLocalDate();
//            date_LC = date_LC.plusDays(1);
            List<EntityExpr> exprs = UtilMisc.toList(
                    EntityCondition.makeCondition("fromDate", EntityOperator.GREATER_THAN_EQUAL_TO, PersonUtilServices.convertStringToTimestamp(monthstartDate, false)),
                    EntityCondition.makeCondition("thruDate", EntityOperator.LESS_THAN, PersonUtilServices.convertStringToTimestamp(monthEndDate, false)),
                    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId)
            );
            EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);
            List<GenericValue> list = delegator.findList("EmplLeave", condition, null, null, null, true);
            System.out.println("checkIfDateVacationOrDate 1" + list);
            if (!list.isEmpty()) {
                for (GenericValue item : list) {
                    LocalDate fromDate_LC = PayrollServices.convertFromStringToDate(item.get("fromDate").toString()).toLocalDate();
                    LocalDate thruDate_LC = PayrollServices.convertFromStringToDate(item.get("thruDate").toString()).toLocalDate();
                    if ((date_LC.equals(fromDate_LC) || date_LC.isAfter(fromDate_LC)) && date_LC.isBefore(thruDate_LC)) {
                        if (item.get("type").toString().equals("LEAVE")) {
                            return result = "LEAVE";
                        } else {
                            return result = "VACATION";
                        }
                    }
                }

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static String setTimeAttendance_Employee(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;

        String partyId = request.getParameter("partyId");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        String userLoginId = (String) userLogin.getString("userLoginId");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyId", partyId);
//
        try {

            Map<String, String> removeCriteria = new HashMap<String, String>();
            removeCriteria.put("year", year);
            removeCriteria.put("month", month);
            removeCriteria.put("partyId", partyId);
            int remove = delegator.removeByAnd("TimeAttendanceEmpl", removeCriteria);

            List<GenericValue> toBeStored = new LinkedList<GenericValue>();

            Map<String, LocalDate> dates = new HashMap<String, LocalDate>();
            dates = getTimeAttendanceStartAndEndDate(request, year, month);
            List<EntityExpr> exprs = UtilMisc.toList(EntityCondition.makeCondition("AttDate", EntityOperator.GREATER_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("startDate"))),
                    EntityCondition.makeCondition("AttDate", EntityOperator.LESS_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("endDate"))),
                    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId));
            EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);

            List<GenericValue> list = delegator.findList("TimeAttendance", condition, null, null, null, true);

            List<GenericValue> TimeAttendanceEmpl_list = delegator.findList("TimeAttendanceEmpl", condition, null, null, null, true);

            if (TimeAttendanceEmpl_list.isEmpty()) {
                for (GenericValue row : list) {
                    GenericValue item = delegator.makeValue("TimeAttendanceEmpl");
                    item.put("timeAttendanceEmpId", delegator.getNextSeqId("timeAttendanceEmpId"));
                    item.put("partyId", row.get("partyId"));
                    item.put("AttDate", row.get("AttDate"));

                    item.put("year", year);
                    item.put("month", month);
                    item.put("day", row.get("day"));

                    item.put("weekDay", row.get("weekDay"));
                    item.put("dayType", row.get("dayType"));
                    item.put("checkIn", row.get("checkIn"));
                    item.put("checkOut", row.get("checkOut"));

                    item.put("notes", null);
                    item.put("late", null);

                    if ((row.get("checkOut") == null) || (row.get("checkIn") == null)) {
                    } else {
                        item.put("work", PersonUtilServices.timesCalculation(row.get("checkOut").toString(), row.get("checkIn").toString(), "sub", "all"));
                    }

                    if (!(row.get("checkIn") == null)) {
                        String lateDeff = PayrollServices.getGlobalPayrollSetting(request, "TO_LATE");
                        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                        Date checkIn = parser.parse(row.get("checkIn").toString());
                        Date toLate = parser.parse(lateDeff);

                        if (checkIn.after(toLate)) {
                            if (!checkIfDateOfficialHolidayOrOffDay(request, PayrollServices.convertFromStringToDate(row.get("AttDate").toString()).toLocalDate())) {
                                item.put("late", PersonUtilServices.timesCalculation(row.get("checkIn").toString(), lateDeff, "sub", "all"));
                            }
                        }
                    }

                    item.put("userLoginId", userLoginId);

//                item.create();
                    toBeStored.add(item);
                }

                try {
                    delegator.storeAll(toBeStored);
                    return "success";
                } catch (GenericEntityException e) {
                    e.getStackTrace();
                }
            } else {
                return "error";

            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
            return "error";
        }
        return "success";
    }

    public static String setTimeAttendanceSetting(HttpServletRequest request, HttpServletResponse response) throws
            ParseException, IOException {
        String startWork = request.getParameter("StartWork");
        String toLate = request.getParameter("toLate");
        String EndWork = request.getParameter("EndWork");
        String BeforEndWork = request.getParameter("BeforEndWork");
        String FirstOFFDay = request.getParameter("FirstOFFDay");
        String SecondOFFDay = request.getParameter("SecondOFFDay");

        String leaveHours = request.getParameter("leaveHours");
        String AllowenceID = request.getParameter("AllowenceID");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        Map<String, Object> criteria = new HashMap<String, Object>();

        List<GenericValue> toBeStored = new LinkedList<GenericValue>();
        GenericValue item = delegator.makeValue("GlobalPayrollSettings");
        item.put("Key", "START_WORK");
        item.put("Value", startWork);
        item.put("globalKey", "TIME_ATTENDANCE");

        toBeStored.add(item);

        GenericValue item2 = delegator.makeValue("GlobalPayrollSettings");
        item2.put("Key", "TO_LATE");
        item2.put("Value", toLate);
        item2.put("globalKey", "TIME_ATTENDANCE");
        toBeStored.add(item2);

        GenericValue item3 = delegator.makeValue("GlobalPayrollSettings");
        item3.put("Key", "END_WORK");
        item3.put("Value", EndWork);
        item3.put("globalKey", "TIME_ATTENDANCE");
        toBeStored.add(item3);

        GenericValue item4 = delegator.makeValue("GlobalPayrollSettings");
        item4.put("Key", "BEFORE_END_WORK");
        item4.put("Value", BeforEndWork);
        item4.put("globalKey", "TIME_ATTENDANCE");
        toBeStored.add(item4);

        GenericValue item5 = delegator.makeValue("GlobalPayrollSettings");
        item5.put("Key", "FIRST_OFFDAY");
        item5.put("Value", FirstOFFDay);
        item5.put("globalKey", "TIME_ATTENDANCE");
        toBeStored.add(item5);

        GenericValue item6 = delegator.makeValue("GlobalPayrollSettings");
        item6.put("Key", "SECOND_OFFDAY");
        item6.put("Value", SecondOFFDay);
        item6.put("globalKey", "TIME_ATTENDANCE");
        toBeStored.add(item6);

        GenericValue item7 = delegator.makeValue("GlobalPayrollSettings");
        item7.put("Key", "TIME_ATTENDANCE_ALLOWENCE_ID");
        item7.put("Value", AllowenceID);
        item7.put("globalKey", "TIME_ATTENDANCE");
        toBeStored.add(item7);

        GenericValue item8 = delegator.makeValue("GlobalPayrollSettings");
        item8.put("Key", "LEAVE_HOURS");
        item8.put("Value", leaveHours);
        item8.put("globalKey", "TIME_ATTENDANCE");
        toBeStored.add(item8);

        try {
            delegator.storeAll(toBeStored);
            return "success";
        } catch (GenericEntityException e) {
            e.getStackTrace();

        }
        return "success";
    }

    public static String getTimeAttendanceSetting(HttpServletRequest request, HttpServletResponse response) throws
            SQLException, ParseException, IOException {
        PrintWriter out = null;
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("globalKey", "TIME_ATTENDANCE");
        JSONObject jsonRes;
        JSONArray jsonGrade = new JSONArray();
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> list = delegator.findList("GlobalPayrollSettings", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                jsonRes.put("Key", row.get("Key"));
                jsonRes.put("Value", row.get("Value"));
                jsonGrade.add(jsonRes);
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonGrade.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String setTiimeAttendanceSummary(HttpServletRequest request, HttpServletResponse response) throws
            ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        String userLoginId = (String) userLogin.getString("userLoginId");

//        String partyId = request.getParameter("partyId");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String dept = request.getParameter("dept");

        Map<String, String> data = new HashMap<String, String>();
        data = getTimeAttendanceWorkingHAndIncrementH(request, year, month);

        try {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("year", year);
            criteria.put("month", month);
            criteria.put("dept", dept);
            int remove = delegator.removeByAnd("TimeAttendanceSumm", criteria);

            List<String> employemntList = new ArrayList<String>();
            employemntList = getDepartmentEmployees(request, dept);
            System.out.println("employemntList >> ");
            employemntList.forEach(System.out::println);

            List<GenericValue> toBeStored = new LinkedList<GenericValue>();

            Map<String, LocalDate> dates = new HashMap<String, LocalDate>();
            dates = getTimeAttendanceStartAndEndDate(request, year, month);

            List<EntityExpr> exprsMain = UtilMisc.toList(
                    EntityCondition.makeCondition("AttDate", EntityOperator.GREATER_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("startDate"))),
                    EntityCondition.makeCondition("AttDate", EntityOperator.LESS_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("endDate"))));
            EntityCondition conditionMain = EntityCondition.makeCondition(exprsMain, EntityOperator.AND);
            System.out.println("conditionMain : " + conditionMain);

            List<GenericValue> listMain = delegator.findList("TimeAttendance", conditionMain, null, null, null, true);
            System.out.println("listMain : " + listMain);

            // To add Distinct list Of Party ID's
            Set<String> partyId_list = new HashSet<String>();
            for (GenericValue row : listMain) {
                partyId_list.add(row.get("partyId").toString());
            }

            Set<String> partyId_Join = new HashSet<String>();
            for (String item : partyId_list) {
                if (employemntList.contains(item)) {
                    partyId_Join.add(item);
                }
            }

            System.out.println("partyId_Join >> ");
            partyId_Join.forEach(System.out::println);

            for (String item : partyId_Join) {

                List<EntityExpr> exprs = UtilMisc.toList(EntityCondition.makeCondition("AttDate", EntityOperator.GREATER_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("startDate"))),
                        EntityCondition.makeCondition("AttDate", EntityOperator.LESS_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("endDate"))),
                        EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, item));
                EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);
                System.out.println("condition : " + condition);

                List<GenericValue> list = delegator.findList("TimeAttendance", condition, null, null, null, true);
                System.out.println("list : " + list);

                GenericValue gvValue = delegator.makeValue("TimeAttendanceSumm");
//                Map<String, Object> criteria = new HashMap<String, Object>();
//                criteria.put("partyId", item);
//                List<GenericValue> TimeAttendanceEmpl_list = delegator.findList("TimeAttendance", EntityCondition.makeCondition(criteria), null, null, null, true);
//                System.out.println("TimeAttendanceEmpl_list : "+TimeAttendanceEmpl_list);

                String attendedWorkingHours = "00:00:00";
                String morningLate = "00:00:00";

                for (GenericValue subRow : list) {
                    if (subRow.get("checkIn") != null && subRow.get("checkOut") != null) {
                        String TD = PersonUtilServices.timesCalculation(subRow.get("checkOut").toString(), subRow.get("checkIn").toString(), "sub", "all");
                        System.out.println("TD : " + TD);
                        attendedWorkingHours = PersonUtilServices.timesCalculation(attendedWorkingHours, PersonUtilServices.timesCalculation(subRow.get("checkOut").toString(), subRow.get("checkIn").toString(), "sub", "all"), "add", "all");
                    }
                    if (subRow.get("checkIn") != null && subRow.get("checkOut") != null) {

                        String lateDeff = PayrollServices.getGlobalPayrollSetting(request, "TO_LATE");
                        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                        Date checkIn = parser.parse(subRow.get("checkIn").toString());
                        Date toLate = parser.parse(lateDeff);
                        if (checkIn.after(toLate)) {
                            if (!checkIfDateOfficialHolidayOrOffDay(request, PayrollServices.convertFromStringToDate(subRow.get("AttDate").toString()).toLocalDate())) {
                                morningLate = PersonUtilServices.timesCalculation(morningLate, PersonUtilServices.timesCalculation(subRow.get("checkIn").toString(), lateDeff, "sub", "all"), "add", "positive");
                            }
                        }
                    }

                }

                String personalLeave = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "PERSONAL", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String businessLeave = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "BUSENISS", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String vacationAnnual = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "PERSONAL", item, Integer.parseInt(data.get("workDayHours")), "VACATION");

                String emergencyLeave = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "EMERGENCY", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String emergencyVacation = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "EMERGENCY", item, Integer.parseInt(data.get("workDayHours")), "VACATION");
                String sickLeave = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "SICK", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String sickVacation = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "SICK", item, Integer.parseInt(data.get("workDayHours")), "VACATION");
                String businessVacation = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "BUSENISS", item, Integer.parseInt(data.get("workDayHours")), "VACATION");

                //Other Vacation equation
                //otherVacation = emergencyLeave + emergencyVacation + sickLeave + sickVacation + businessVacation
                ArrayList<String> otherVacationList = new ArrayList<String>();
                otherVacationList.add(emergencyLeave);
                otherVacationList.add(emergencyVacation);
                otherVacationList.add(sickLeave);
                otherVacationList.add(sickVacation);
                otherVacationList.add(businessVacation);

                String otherVacation = PersonUtilServices.timesCalculation(otherVacationList, "add", "positive");

                //Total Working Hours equation
                //totalWorkingHours = (attendedWorkingHours + personalLeave + businessLeave + vacationAnnual + otherVacation)- morningLate
                ArrayList<String> totalWorkingHoursList = new ArrayList<String>();
                totalWorkingHoursList.add(attendedWorkingHours);
                totalWorkingHoursList.add(personalLeave);
//                    totalWorkingHoursList.add(emergencyLeave);
                totalWorkingHoursList.add(businessLeave);
                totalWorkingHoursList.add(vacationAnnual);
                totalWorkingHoursList.add(otherVacation);
                String totalWorkingHours = PersonUtilServices.timesCalculation(totalWorkingHoursList, "add", "positive");
                totalWorkingHours = PersonUtilServices.timesCalculation(totalWorkingHours, morningLate, "sub", "all");

                System.out.println("timeDefference_Long " + PersonUtilServices.timesCalculation(personalLeave, PayrollServices.getGlobalPayrollSetting(request, "LEAVE_HOURS") + ":00:00", "sub"));
                String personalLeaveTotal = "";
                if (PersonUtilServices.timesCalculation(personalLeave, PayrollServices.getGlobalPayrollSetting(request, "LEAVE_HOURS") + ":00:00", "sub") >= 0) {
                    personalLeaveTotal = PersonUtilServices.timesCalculation(personalLeave, PayrollServices.getGlobalPayrollSetting(request, "LEAVE_HOURS") + ":00:00", "sub", "all");
                    System.out.println("personalLeaveTotal " + PersonUtilServices.timesCalculation(personalLeave, PayrollServices.getGlobalPayrollSetting(request, "LEAVE_HOURS") + ":00:00", "sub", "all"));
                    totalWorkingHours = PersonUtilServices.timesCalculation(totalWorkingHours, personalLeaveTotal, "sub", "all");
                }

                // To Calculate How Many Day In Month To Find totaly Offcialy Houres
                LocalDate newStartDate = dates.get("startDate");
                int countDaysOMonth = 0;
                while (newStartDate.isBefore(dates.get("endDate")) || newStartDate.isEqual(dates.get("endDate"))) {
                    if (newStartDate.getDayOfWeek().toString().toLowerCase().equals(PayrollServices.getGlobalPayrollSetting(request, "FIRST_OFFDAY").toLowerCase()) || newStartDate.getDayOfWeek().toString().toLowerCase().equals(PayrollServices.getGlobalPayrollSetting(request, "SECOND_OFFDAY").toLowerCase())) {
                    } else {
                        countDaysOMonth++;
                    }
                    newStartDate = newStartDate.plusDays(1);
                }

                System.out.println("Count " + countDaysOMonth);

                Map<String, LocalDate> publicHolidays = new HashMap<String, LocalDate>();
                int noOficialHolidays = getOficialHolidays(request, dates.get("startDate"), dates.get("endDate"));

                System.out.println("noOficialHolidays : " + noOficialHolidays);

                int countDaysOfMonthInHours = countDaysOMonth * Integer.parseInt(data.get("workDayHours"));
                System.out.println("countDaysOMonthInHours " + countDaysOfMonthInHours);

                String officialWorkingHours = countDaysOfMonthInHours + ":00:00";
                System.out.println("officialWorkingHours " + officialWorkingHours);
                String hoursPlusMinus = "00:00:00";
                String daysPlusMinus = "00:00:00";

                int publicHolidaysInHours = noOficialHolidays * Integer.parseInt(data.get("workDayHours"));
                String totalWorkingHours_Deference = PersonUtilServices.timesCalculation(totalWorkingHours, morningLate, "sub", "all");
                gvValue.set("timeAttendanceSummId", delegator.getNextSeqId("timeAttendanceSummId"));
                gvValue.set("partyId", item);

                gvValue.set("attendedWorkingHours", attendedWorkingHours);

                gvValue.set("personalLeave", personalLeave);
//                gvValue.set("emergencyLeave", emergencyLeave);
                gvValue.set("businessLeave", businessLeave);
                gvValue.set("vacationAnnual", vacationAnnual);
                gvValue.set("otherVacation", otherVacation);
                gvValue.set("morningLate", morningLate);

                // totalWorkingHours_new totalWorkingHours + incrementHours
                String totalWorkingHours_new = PersonUtilServices.timesCalculation(totalWorkingHours, data.get("incrementHours") + ":00:00", "add", "all");

                gvValue.set("totalWorkingHours", totalWorkingHours_new);

                // officialWorkingHours_new = officialWorkingHours - publicHolidaysInHours
                String officialWorkingHours_new = PersonUtilServices.timesCalculation(officialWorkingHours, publicHolidaysInHours + ":00:00", "sub", "all");

                gvValue.set("officialWorkingHours", officialWorkingHours_new);
                gvValue.set("hoursPlusMinus", PersonUtilServices.timesCalculation(totalWorkingHours_new, officialWorkingHours_new, "sub", "all"));
                gvValue.set("daysPlusMinus", timeDivision(PersonUtilServices.timesCalculation(totalWorkingHours_new, officialWorkingHours_new, "sub", "all"), Integer.parseInt(data.get("workDayHours"))).toString());
                gvValue.set("year", year);
                gvValue.set("month", month);
                gvValue.set("dept", dept);
                gvValue.set("userLoginId", userLoginId);

                toBeStored.add(gvValue);
            }

            try {
//                int removeAll = delegator.removeByAnd("TimeAttendanceSumm", mainCriteria);
                delegator.storeAll(toBeStored);
                return "success";
            } catch (GenericEntityException e) {
                e.getStackTrace();
                return "success";

            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    public static String getTimeAttendance_SummaryData(HttpServletRequest request, HttpServletResponse response) throws
            ParseException, IOException {
//        String partyId = request.getParameter("partyId");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String dept = request.getParameter("dept");

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
//        criteria.put("partyId", partyId);
        criteria.put("year", year);
        criteria.put("month", month);

        List<String> employemntList = new ArrayList<String>();
        employemntList = getDepartmentEmployees(request, dept);
        System.out.println("employemntList >> ");
        employemntList.forEach(System.out::println);

        Map<String, String> data = new HashMap<String, String>();
        data = getTimeAttendanceWorkingHAndIncrementH(request, year, month);

        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {
            //to get start and and date
            Map<String, LocalDate> dates = new HashMap<String, LocalDate>();
            dates = getTimeAttendanceStartAndEndDate(request, year, month);

            List<EntityExpr> exprsMain = UtilMisc.toList(
                    EntityCondition.makeCondition("AttDate", EntityOperator.GREATER_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("startDate"))),
                    EntityCondition.makeCondition("AttDate", EntityOperator.LESS_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("endDate"))));
            EntityCondition conditionMain = EntityCondition.makeCondition(exprsMain, EntityOperator.AND);
            System.out.println("conditionMain : " + conditionMain);

            List<GenericValue> listMain = delegator.findList("TimeAttendance", conditionMain, null, null, null, true);
            System.out.println("listMain : " + listMain);

            // To add Distinct list Of Party ID's
            Set<String> partyId_list = new HashSet<String>();
            for (GenericValue row : listMain) {
                partyId_list.add(row.get("partyId").toString());
            }

            Set<String> partyId_Join = new HashSet<String>();
            for (String item : partyId_list) {
                if (employemntList.contains(item)) {
                    partyId_Join.add(item);
                }
            }

            System.out.println("partyId_Join >> ");
            partyId_Join.forEach(System.out::println);

            for (String item : partyId_Join) {

                List<EntityExpr> exprs = UtilMisc.toList(EntityCondition.makeCondition("AttDate", EntityOperator.GREATER_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("startDate"))),
                        EntityCondition.makeCondition("AttDate", EntityOperator.LESS_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("endDate"))),
                        EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, item));
                EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);
                System.out.println("condition : " + condition);

                List<GenericValue> list = delegator.findList("TimeAttendance", condition, null, null, null, true);
                System.out.println("list : " + list);
                String attendedWorkingHours = "00:00:00";
                String morningLate = "00:00:00";

                for (GenericValue subRow : list) {
                    if (subRow.get("checkIn") != null && subRow.get("checkOut") != null) {
                        String TD = PersonUtilServices.timesCalculation(subRow.get("checkOut").toString(), subRow.get("checkIn").toString(), "sub", "all");
                        System.out.println("TD : " + TD);
                        attendedWorkingHours = PersonUtilServices.timesCalculation(attendedWorkingHours, PersonUtilServices.timesCalculation(subRow.get("checkOut").toString(), subRow.get("checkIn").toString(), "sub", "all"), "add", "all");
                    }
                    if (subRow.get("checkIn") != null && subRow.get("checkOut") != null) {

                        String lateDeff = PayrollServices.getGlobalPayrollSetting(request, "TO_LATE");
                        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                        Date checkIn = parser.parse(subRow.get("checkIn").toString());
                        Date toLate = parser.parse(lateDeff);

                        if (checkIn.after(toLate)) {

                            if (!checkIfDateOfficialHolidayOrOffDay(request, PayrollServices.convertFromStringToDate(subRow.get("AttDate").toString()).toLocalDate())) {
                                morningLate = PersonUtilServices.timesCalculation(morningLate, PersonUtilServices.timesCalculation(subRow.get("checkIn").toString(), lateDeff, "sub", "all"), "add", "positive");
                            }
                        }
                    }

                }

                String personalLeave = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "PERSONAL", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String businessLeave = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "BUSENISS", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String vacationAnnual = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "PERSONAL", item, Integer.parseInt(data.get("workDayHours")), "VACATION");

                String emergencyLeave = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "EMERGENCY", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String emergencyVacation = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "EMERGENCY", item, Integer.parseInt(data.get("workDayHours")), "VACATION");
                String sickLeave = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "SICK", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String sickVacation = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "SICK", item, Integer.parseInt(data.get("workDayHours")), "VACATION");
                String businessVacation = getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "BUSENISS", item, Integer.parseInt(data.get("workDayHours")), "VACATION");

                //Other Vacation equation
                //otherVacation = emergencyLeave + emergencyVacation + sickLeave + sickVacation + businessVacation
                ArrayList<String> otherVacationList = new ArrayList<String>();
                otherVacationList.add(emergencyLeave);
                otherVacationList.add(emergencyVacation);
                otherVacationList.add(sickLeave);
                otherVacationList.add(sickVacation);
                otherVacationList.add(businessVacation);

                String otherVacation = PersonUtilServices.timesCalculation(otherVacationList, "add", "positive");

                //Total Working Hours equation
                //totalWorkingHours = (attendedWorkingHours + personalLeave + businessLeave + vacationAnnual + otherVacation)- morningLate
                ArrayList<String> totalWorkingHoursList = new ArrayList<String>();
                totalWorkingHoursList.add(attendedWorkingHours);
                totalWorkingHoursList.add(personalLeave);
//                    totalWorkingHoursList.add(emergencyLeave);
                totalWorkingHoursList.add(businessLeave);
                totalWorkingHoursList.add(vacationAnnual);
                totalWorkingHoursList.add(otherVacation);
                String totalWorkingHours = PersonUtilServices.timesCalculation(totalWorkingHoursList, "add", "positive");

                totalWorkingHours = PersonUtilServices.timesCalculation(totalWorkingHours, morningLate, "sub", "all");

                // To Check in Calaculate if Personal >= 8 hours
                System.out.println("timeDefference_Long " + PersonUtilServices.timesCalculation(personalLeave, PayrollServices.getGlobalPayrollSetting(request, "LEAVE_HOURS") + ":00:00", "sub"));
                String personalLeaveTotal = "";
                if (PersonUtilServices.timesCalculation(personalLeave, PayrollServices.getGlobalPayrollSetting(request, "LEAVE_HOURS") + ":00:00", "sub") >= 0) {
                    personalLeaveTotal = PersonUtilServices.timesCalculation(personalLeave, PayrollServices.getGlobalPayrollSetting(request, "LEAVE_HOURS") + ":00:00", "sub", "all");
                    totalWorkingHours = PersonUtilServices.timesCalculation(totalWorkingHours, personalLeaveTotal, "sub", "all");
                }

                // To Calculate How Many Day In Month To Find totaly Offcialy Houres
                LocalDate newStartDate = dates.get("startDate");
                int countDaysOMonth = 0;
                while (newStartDate.isBefore(dates.get("endDate")) || newStartDate.isEqual(dates.get("endDate"))) {
                    if (newStartDate.getDayOfWeek().toString().toLowerCase().equals(PayrollServices.getGlobalPayrollSetting(request, "FIRST_OFFDAY").toLowerCase()) || newStartDate.getDayOfWeek().toString().toLowerCase().equals(PayrollServices.getGlobalPayrollSetting(request, "SECOND_OFFDAY").toLowerCase())) {
                    } else {
                        countDaysOMonth++;
                    }
                    newStartDate = newStartDate.plusDays(1);
                }

                System.out.println("Count " + countDaysOMonth);

                Map<String, LocalDate> publicHolidays = new HashMap<String, LocalDate>();
                int noOficialHolidays = getOficialHolidays(request, dates.get("startDate"), dates.get("endDate"));

                System.out.println("noOficialHolidays : " + noOficialHolidays);

                int countDaysOfMonthInHours = countDaysOMonth * Integer.parseInt(data.get("workDayHours"));
                System.out.println("countDaysOMonthInHours " + countDaysOfMonthInHours);

                String officialWorkingHours = countDaysOfMonthInHours + ":00:00";
                System.out.println("officialWorkingHours " + officialWorkingHours);
                String hoursPlusMinus = "00:00:00";
                String daysPlusMinus = "00:00:00";

                int publicHolidaysInHours = noOficialHolidays * Integer.parseInt(data.get("workDayHours"));
                String totalWorkingHours_Deference = PersonUtilServices.timesCalculation(totalWorkingHours, morningLate, "sub", "all");
                jsonRes = new JSONObject();

                jsonRes.put("partyId", item);

                jsonRes.put("attendedWorkingHours", attendedWorkingHours);

                jsonRes.put("personalLeave", personalLeave);
//                    jsonRes.put("emergencyLeave", emergencyLeave);
                jsonRes.put("businessLeave", businessLeave);
                jsonRes.put("vacationAnnual", vacationAnnual);
                jsonRes.put("otherVacation", otherVacation);
                jsonRes.put("morningLate", morningLate);

                // totalWorkingHours_new totalWorkingHours + incrementHours
                String totalWorkingHours_new = PersonUtilServices.timesCalculation(totalWorkingHours, data.get("incrementHours") + ":00:00", "add", "all");
                jsonRes.put("totalWorkingHours", totalWorkingHours_new);

                // officialWorkingHours_new = officialWorkingHours - publicHolidaysInHours
                String officialWorkingHours_new = PersonUtilServices.timesCalculation(officialWorkingHours, publicHolidaysInHours + ":00:00", "sub", "all");
                jsonRes.put("officialWorkingHours", officialWorkingHours_new);

                //Hours Plus/Minus equation
                //hoursPlusMinus = totalWorkingHours_new - officialWorkingHours_new
                jsonRes.put("hoursPlusMinus", PersonUtilServices.timesCalculation(totalWorkingHours_new, officialWorkingHours_new, "sub", "all"));

                //Days Plus/Minus equation
                //daysPlusMinus = (totalWorkingHours - officialWorkingHours)/workDayHours(8)
                jsonRes.put("daysPlusMinus", timeDivision(PersonUtilServices.timesCalculation(totalWorkingHours_new, officialWorkingHours_new, "sub", "all"), Integer.parseInt(data.get("workDayHours"))).toString());

                jsonData.add(jsonRes);

            }


            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    public static Long timeDivision(String time1, long dayHours) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date1 = format.parse(time1);

        long difference = date1.getTime();
//        long hour = (difference / (1000 * 60 * 60)) % 24;
//        long minute = (difference / (1000 * 60)) % 60;
//        long second = (difference / 1000) % 60;
        difference = difference / 1000;
        long hh = difference / 3600;
        System.out.println("from timeDivision");
        long dd = hh / dayHours;
        System.out.println("dd " + dd);
        return dd;

    }

    public static String getTimeAttendanceConfig(HttpServletRequest request, HttpServletResponse response) throws
            SQLException, ParseException, IOException {

        PrintWriter out = null;
        String year = request.getParameter("year");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("year", year);
        JSONObject jsonRes;
        JSONArray jsonGrade = new JSONArray();
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> list = delegator.findList("TimeAttendanceConfig", EntityCondition.makeCondition(criteria), null, null, null, true);
//            List<GenericValue> list = delegator.findAll("TimeAttendanceConfig", true);
//            String arrOfMonth[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
//           int i=list.size()-1;
//           int k=i;
//            for(int j = 0; j <= 11; j++){
//                jsonRes = new JSONObject();
//               if(i>=0){
//                jsonRes.put("TimeAttendanceConfigId", list.get(k-i).get("TimeAttendanceConfigId"));
//                jsonRes.put("year", list.get(k-i).get("tYear"));
//                jsonRes.put("start", list.get(k-i).get("tStart"));
//                jsonRes.put("end", list.get(k-i).get("tEnd"));
//                jsonRes.put("workDayHours", list.get(k-i).get("workDayHours"));
//                jsonRes.put("incrementHours", list.get(k-i).get("incrementHours"));
//                jsonRes.put("userLoginId", list.get(k-i).get("userLoginId"));
//                jsonRes.put("month", arrOfMonth[j]);
////                jsonRes.put("TimeAttendanceConfigId", "");
////                jsonRes.put("year", "");
////                jsonRes.put("start", "");
////                jsonRes.put("end", "");
////                jsonRes.put("workDayHours", "");
////                jsonRes.put("incrementHours", "");
////                jsonRes.put("userLoginId", "");
//                i--;
//               }else{
//                   jsonRes.put("month", arrOfMonth[j]);
//               }
//              jsonGrade.add(jsonRes);
//            }

            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                jsonRes.put("TimeAttendanceConfigId", row.get("TimeAttendanceConfigId"));
                jsonRes.put("year", row.get("year"));
                jsonRes.put("startDate", row.get("startDate").toString());
                jsonRes.put("endDate", row.get("endDate").toString());
                jsonRes.put("workDayHours", row.get("workDayHours"));
                jsonRes.put("incrementHours", row.get("incrementHours"));
                jsonRes.put("userLoginId", row.get("userLoginId"));
                jsonRes.put("month", row.get("month"));

                jsonGrade.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonGrade.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getTimeAttendanceMonthConfig(HttpServletRequest request, HttpServletResponse response) throws
            SQLException, ParseException, IOException {

        PrintWriter out = null;
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("year", year);
        criteria.put("month", month);

        String Flaq = "false";
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> list = delegator.findList("TimeAttendanceConfig", EntityCondition.makeCondition(criteria), null, null, null, true);

            if (!list.isEmpty()) {
                Flaq = "true";
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(Flaq);
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static Map<String, LocalDate> getTimeAttendanceStartAndEndDate(HttpServletRequest request, String year, String month) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, LocalDate> dates = new HashMap<String, LocalDate>();
        try {

            Map<String, String> criteria2 = new HashMap<String, String>();
            criteria2.put("year", year);
            criteria2.put("month", month);
            List<GenericValue> timeAttendanceMonthConfig_list = delegator.findList("TimeAttendanceConfig", EntityCondition.makeCondition(criteria2), null, null, null, true);

            String line[] = timeAttendanceMonthConfig_list.get(0).get("startDate").toString().split("-");
            LocalDate startDateLocalDate = LocalDate.of(Integer.valueOf(line[0]), Integer.valueOf(line[1]), Integer.valueOf(line[2]));
            dates.put("startDate", startDateLocalDate);

            String line2[] = timeAttendanceMonthConfig_list.get(0).get("endDate").toString().split("-");
            LocalDate endDateLocalDate = LocalDate.of(Integer.valueOf(line2[0]), Integer.valueOf(line2[1]), Integer.valueOf(line2[2]));
            dates.put("endDate", endDateLocalDate);

        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return dates;

    }

    public static Map<String, String> getTimeAttendanceWorkingHAndIncrementH(HttpServletRequest request, String year, String month) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> data = new HashMap<String, String>();
        try {

            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("year", year);
            criteria.put("month", month);
            List<GenericValue> timeAttendanceMonthConfig_list = delegator.findList("TimeAttendanceConfig", EntityCondition.makeCondition(criteria), null, null, null, true);

            data.put("workDayHours", timeAttendanceMonthConfig_list.get(0).get("workDayHours").toString());
            data.put("incrementHours", timeAttendanceMonthConfig_list.get(0).get("incrementHours").toString());

        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return data;

    }

    public static int getOficialHolidays(HttpServletRequest request, LocalDate monthStartDate, LocalDate monthEndDate) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, LocalDate> data = new HashMap<String, LocalDate>();
        int count = 0;
        try {

            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("workEffortTypeId", "PUBLIC_HOLIDAY");

            java.sql.Date monthStartDate_Date = java.sql.Date.valueOf(monthStartDate);
            java.sql.Timestamp monthStartDate_TimeStamp = new java.sql.Timestamp(monthStartDate_Date.getTime());

            java.sql.Date monthEndDate_Date = java.sql.Date.valueOf(monthEndDate);
            java.sql.Timestamp monthEndDate_TimeStamp = new java.sql.Timestamp(monthEndDate_Date.getTime());

            List<EntityExpr> exprs = UtilMisc.toList(EntityCondition.makeCondition("estimatedStartDate", EntityOperator.GREATER_THAN_EQUAL_TO, monthStartDate_TimeStamp),
                    EntityCondition.makeCondition("estimatedCompletionDate", EntityOperator.LESS_THAN_EQUAL_TO, monthEndDate_TimeStamp),
                    EntityCondition.makeCondition("workEffortTypeId", EntityOperator.EQUALS, "PUBLIC_HOLIDAY"));
            EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);
            System.out.println("condition : " + condition);
            List<GenericValue> list = delegator.findList("WorkEffort", condition, null, null, null, true);
            System.out.println("PUBLIC_HOLIDAY List : " + list);
//            Date endDateObj = parseDate(list.get(0).get("estimatedStartDate").toString(), "yyyy-MM-dd hh:mm:ss");
//            LocalDate endDate = endDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

//            Date startDateObj = parseDate(list.get(0).get("estimatedCompletionDate").toString(), "yyyy-MM-dd hh:mm:ss");
//            LocalDate startDate = startDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    count++;
                }
            }
//            data.put("endDate", endDate);
//            data.put("startDate", startDate);

//            System.out.println("estimatedStartDate : " + list.get(0).get("estimatedCompletionDate").toString());
//            System.out.println("estimatedCompletionDate : " + java.sql.Date.valueOf(list.get(0).get("estimatedCompletionDate").toString()));
        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return count;

    }

    public static Date parseDate(String date, String pattern) {
        String getDate = date;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date serviceDate = null;
        try {
            serviceDate = sdf.parse(getDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return serviceDate;
    }

    public static Date getDateWithoutTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    //    public static long getDifferenceDays(Date d1, Date d2) {
//        long diff = d2.getTime() - d1.getTime();
//        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
//    }
    public static List<String> getDepartmentEmployees(HttpServletRequest request, String dept) throws
            ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        List<String> data = new ArrayList<String>();
        try {

            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("roleTypeIdFrom", "INTERNAL_ORGANIZATIO");
            criteria.put("partyIdFrom", dept);
            List<GenericValue> employment_list = delegator.findList("Employment", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue item : employment_list) {
                data.add(item.get("partyIdTo").toString());
            }
//            data.put("workDayHours", timeAttendanceMonthConfig_list.get(0).get("workDayHours").toString());
//            data.put("incrementHours", timeAttendanceMonthConfig_list.get(0).get("incrementHours").toString());
        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return data;

    }

    public static String getLeaveAndVacationInHours(HttpServletRequest request, LocalDate monthStartDate, LocalDate monthEndDate, String reasonsType, String partyId, int workingHouresInMonth, String type) throws
            ParseException, IOException, GenericEntityException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        List<String> LeaveIds = new ArrayList<String>();
        String finalResult = "00:00:00";
        try {
//            Timestamp timestamp = new Timestamp(monthStartDate.toDateTimeAtStartOfDay().getMillis());

            if (type.equals("LEAVE")) {

                monthEndDate = monthEndDate.plusDays(1);

                List<EntityExpr> exprs = UtilMisc.toList(
                        EntityCondition.makeCondition("fromDate", EntityOperator.GREATER_THAN_EQUAL_TO, PersonUtilServices.convertStringToTimestamp(monthStartDate.toString(), false)),
                        EntityCondition.makeCondition("thruDate", EntityOperator.LESS_THAN, PersonUtilServices.convertStringToTimestamp(monthEndDate.toString(), false)),
                        EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId),
                        EntityCondition.makeCondition("leaveStatus", EntityOperator.EQUALS, "LEAVE_APPROVED"),
                        EntityCondition.makeCondition("emplLeaveReasonTypeId", EntityOperator.EQUALS, reasonsType),
                        EntityCondition.makeCondition("type", EntityOperator.EQUALS, type)
                );
                EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);
                List<GenericValue> list = delegator.findList("EmplLeave", condition, null, null, null, true);
                if (!list.isEmpty()) {
                    for (GenericValue items : list) {
                        if (!LeaveIds.contains(items.get("leaveId").toString())) {
                            String result = "00:00:00";
                            String fromTime = ((Timestamp) (items.get("fromDate"))).toLocalDateTime().getHour() + ":" + ((Timestamp) (items.get("fromDate"))).toLocalDateTime().getMinute() + ":" + ((Timestamp) (items.get("fromDate"))).toLocalDateTime().getSecond();
                            String threuTime = ((Timestamp) (items.get("thruDate"))).toLocalDateTime().getHour() + ":" + ((Timestamp) (items.get("thruDate"))).toLocalDateTime().getMinute() + ":" + ((Timestamp) (items.get("thruDate"))).toLocalDateTime().getSecond();

                            result = PersonUtilServices.timesCalculation(threuTime, fromTime, "sub", "all");
//                        System.out.println("hoursInDays " + getVacationInHours(request, PayrollServices.convertFromStringToDate(items.get("fromDate").toString()).toLocalDate(), PayrollServices.convertFromStringToDate(items.get("thruDate").toString()).toLocalDate()));
                            finalResult = PersonUtilServices.timesCalculation(finalResult, result, "add", "all");
                            LeaveIds.add(items.get("leaveId").toString());
                        }
                    }
                }

            } else if (type.equals("VACATION")) {

                List<EntityExpr> exprs = UtilMisc.toList(
                        EntityCondition.makeCondition("fromDate", EntityOperator.GREATER_THAN_EQUAL_TO, PersonUtilServices.convertStringToTimestamp(monthStartDate.toString(), false)),
                        EntityCondition.makeCondition("thruDate", EntityOperator.LESS_THAN_EQUAL_TO, PersonUtilServices.convertStringToTimestamp(monthEndDate.toString(), false)),
                        EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId),
                        EntityCondition.makeCondition("leaveStatus", EntityOperator.EQUALS, "LEAVE_APPROVED"),
                        EntityCondition.makeCondition("emplLeaveReasonTypeId", EntityOperator.EQUALS, reasonsType),
                        EntityCondition.makeCondition("type", EntityOperator.EQUALS, type)
                );
                EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);
                List<GenericValue> list = delegator.findList("EmplLeave", condition, null, null, null, true);
                if (!list.isEmpty()) {
                    for (GenericValue items : list) {
                        if (!LeaveIds.contains(items.get("leaveId").toString())) {
                            String result = "00:00:00";
//                        System.out.println("hoursInDays " + getVacationInHours(request, PayrollServices.convertFromStringToDate(items.get("fromDate").toString()).toLocalDate(), PayrollServices.convertFromStringToDate(items.get("thruDate").toString()).toLocalDate()));
                            result = getVacationInHours(request, PayrollServices.convertFromStringToDate(items.get("fromDate").toString()).toLocalDate(), PayrollServices.convertFromStringToDate(items.get("thruDate").toString()).toLocalDate(), workingHouresInMonth);
                            finalResult = PersonUtilServices.timesCalculation(finalResult, result, "add", "all");
                            LeaveIds.add(items.get("leaveId").toString());
                        }
                    }
                }
//..2.....
                List<EntityExpr> exprs_Exception1 = UtilMisc.toList(
                        EntityCondition.makeCondition("fromDate", EntityOperator.GREATER_THAN_EQUAL_TO, PersonUtilServices.convertStringToTimestamp(monthStartDate.toString(), false)),
                        EntityCondition.makeCondition("fromDate", EntityOperator.LESS_THAN_EQUAL_TO, PersonUtilServices.convertStringToTimestamp(monthEndDate.toString(), false)),
                        EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId),
                        EntityCondition.makeCondition("leaveStatus", EntityOperator.EQUALS, "LEAVE_APPROVED"),
                        EntityCondition.makeCondition("emplLeaveReasonTypeId", EntityOperator.EQUALS, reasonsType),
                        EntityCondition.makeCondition("type", EntityOperator.EQUALS, type)
                );
                System.out.println("exprs_Exception1 " + exprs_Exception1);
                EntityCondition condition_Exception1 = EntityCondition.makeCondition(exprs_Exception1, EntityOperator.AND);
                List<GenericValue> list_Exception1 = delegator.findList("EmplLeave", condition_Exception1, null, null, null, true);
                if (!list_Exception1.isEmpty()) {
                    for (GenericValue items : list_Exception1) {
                        if (!LeaveIds.contains(items.get("leaveId").toString())) {
                            String result = "00:00:00";
//                        System.out.println("hoursInDays " + getVacationInHours(request, PayrollServices.convertFromStringToDate(items.get("fromDate").toString()).toLocalDate(), PayrollServices.convertFromStringToDate(items.get("thruDate").toString()).toLocalDate()));
                            result = getVacationInHours(request, PayrollServices.convertFromStringToDate(items.get("fromDate").toString()).toLocalDate(), monthEndDate, workingHouresInMonth);
                            finalResult = PersonUtilServices.timesCalculation(finalResult, result, "add", "all");
                            LeaveIds.add(items.get("leaveId").toString());
                        }
                    }

                }
//..3....
                List<EntityExpr> exprs_Exception2 = UtilMisc.toList(
                        EntityCondition.makeCondition("thruDate", EntityOperator.LESS_THAN_EQUAL_TO, PersonUtilServices.convertStringToTimestamp(monthEndDate.toString(), false)),
                        EntityCondition.makeCondition("thruDate", EntityOperator.GREATER_THAN_EQUAL_TO, PersonUtilServices.convertStringToTimestamp(monthStartDate.toString(), false)),
                        EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId),
                        EntityCondition.makeCondition("leaveStatus", EntityOperator.EQUALS, "LEAVE_APPROVED"),
                        EntityCondition.makeCondition("emplLeaveReasonTypeId", EntityOperator.EQUALS, reasonsType),
                        EntityCondition.makeCondition("type", EntityOperator.EQUALS, type)
                );
                EntityCondition condition_Exception2 = EntityCondition.makeCondition(exprs_Exception2, EntityOperator.AND);
                List<GenericValue> list_Exception2 = delegator.findList("EmplLeave", condition_Exception2, null, null, null, true);
                if (!exprs_Exception2.isEmpty()) {
                    for (GenericValue items : list_Exception2) {
                        if (!LeaveIds.contains(items.get("leaveId").toString())) {
                            String result = "00:00:00";
                            result = getVacationInHours(request, monthStartDate, PayrollServices.convertFromStringToDate(items.get("thruDate").toString()).toLocalDate(), workingHouresInMonth);
                            finalResult = PersonUtilServices.timesCalculation(finalResult, result, "add", "all");
                            LeaveIds.add(items.get("leaveId").toString());
                        }
                    }
                }

            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("finalResult " + finalResult);
        return finalResult;

    }

    public static String getVacationInHours(HttpServletRequest request, LocalDate monthStartDate, LocalDate monthEndDate, int workingHouresInMonth) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        int countDays = 0;
        try {
            LocalDate newStartDate = monthStartDate;
            while (newStartDate.isBefore(monthEndDate) || newStartDate.isEqual(monthEndDate)) {
                if (newStartDate.getDayOfWeek().toString().toLowerCase().equals(PayrollServices.getGlobalPayrollSetting(request, "FIRST_OFFDAY").toLowerCase()) || newStartDate.getDayOfWeek().toString().toLowerCase().equals(PayrollServices.getGlobalPayrollSetting(request, "SECOND_OFFDAY").toLowerCase())) {
                } else {
                    countDays++;
                }
                newStartDate = newStartDate.plusDays(1);
            }
            countDays = countDays - getOficialHolidays(request, monthStartDate, monthEndDate);

//            countDaysOMonth = Math.abs(countDaysOMonth - getOficialHolidays(request, monthStartDate, monthEndDate))
            System.out.println("Count countDaysOMonth" + countDays);

        } catch (Exception ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return countDays * workingHouresInMonth + ":00:00";

    }

    public static String timeAttendanceMonthIsExist(HttpServletRequest request, HttpServletResponse response) throws
            ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        criteria.put("year", year);
        criteria.put("month", month);

        boolean result = false;
        try {
            List<GenericValue> list = delegator.findList("TimeAttendance", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (list.isEmpty()) {
                result = false;
            } else {
                result = true;
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(result);
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static boolean checkIfDateOfficialHolidayOrOffDay(HttpServletRequest request, LocalDate date) throws
            ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        boolean result = false;
        try {
            List<EntityExpr> exprs = UtilMisc.toList(EntityCondition.makeCondition("estimatedStartDate", EntityOperator.EQUALS, PersonUtilServices.convertStringToTimestamp(date.toString(), false)),
                    EntityCondition.makeCondition("workEffortTypeId", EntityOperator.EQUALS, "PUBLIC_HOLIDAY"));
            EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);
            System.out.println("condition : " + condition);
            List<GenericValue> list = delegator.findList("WorkEffort", condition, null, null, null, true);
            System.out.println("PUBLIC_HOLIDAY List : " + list);
//
            if (!list.isEmpty()) {
                System.out.println("checkIfDateOfficialHolidayOrOffDay " + list);
                return result = true;
            } else {
                if (date.getDayOfWeek().toString().toLowerCase().equals(PayrollServices.getGlobalPayrollSetting(request, "FIRST_OFFDAY").toLowerCase()) || date.getDayOfWeek().toString().toLowerCase().equals(PayrollServices.getGlobalPayrollSetting(request, "SECOND_OFFDAY").toLowerCase())) {
                    return result = true;
                }
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return result;

    }

}
