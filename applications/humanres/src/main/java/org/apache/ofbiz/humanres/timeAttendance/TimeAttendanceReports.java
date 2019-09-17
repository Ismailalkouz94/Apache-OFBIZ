/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.humanres.timeAttendance;

import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.humanres.HumanResEvents;

import java.io.*;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.humanres.PayrollServices;
import org.apache.ofbiz.humanres.PersonUtilServices;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;

import static org.apache.ofbiz.humanres.timeAttendance.TimeAttendanceServices.getDepartmentEmployees;

/**
 * @author Admin
 */
public class TimeAttendanceReports {

    public static void TimeAttendancePDFReport_Employee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String partyId = request.getParameter("partyId");
        String year = request.getParameter("year");
        String month = request.getParameter("month");

        GenericValue userLogin = null;
        response.setContentType("application/pdf");
        try {
//                String jrxmlFile = "C:/Users/DELL/JaspersoftWorkspace/test/ReportOneDataSet.jrxml";
            String jrxmlFile = "applications\\reports\\hr\\TimeAttendanceReport_Employee.jrxml";

            InputStream input = new FileInputStream(new File(jrxmlFile));
            JasperReport jasperReport = JasperCompileManager.compileReport(input);

            Map<String, LocalDate> dates = new HashMap<String, LocalDate>();
            dates = TimeAttendanceServices.getTimeAttendanceStartAndEndDate(request, year, month);

            List<EntityExpr> exprs = UtilMisc.toList(EntityCondition.makeCondition("AttDate", EntityOperator.GREATER_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("startDate"))),
                    EntityCondition.makeCondition("AttDate", EntityOperator.LESS_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("endDate"))),
                    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId));
            EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);

            System.out.println("from TimeAttendanceEmpl.isEmpty()");
            List<String> listOrder = new ArrayList<>();
            listOrder.add("timeAttendanceId");
            List<GenericValue> list = delegator.findList("TimeAttendance", condition, null, listOrder, null, true);

            HashMap[] reportRows = null;
            reportRows = new HashMap[list.size()];
            HashMap row = new HashMap();
            int reportRowsCount = 0;
            for (GenericValue item : list) {

                row = new HashMap();
                row.put("partyId", item.get("partyId"));
                row.put("Date", item.get("AttDate"));
                row.put("weekDay", item.get("weekDay"));
                row.put("dayType", item.get("dayType"));
                row.put("checkIn", item.get("checkIn"));
                row.put("checkOut", item.get("checkOut"));

                if (!(item.get("checkOut") == null) && !(item.get("checkIn") == null)) {
                    row.put("work", PersonUtilServices.timesCalculation(item.get("checkOut").toString(), item.get("checkIn").toString(), "sub", "all"));
                }

                if (!(item.get("checkIn") == null)) {
                    String lateDeff = PayrollServices.getGlobalPayrollSetting(request, "TO_LATE");
                    SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                    Date checkIn = parser.parse(row.get("checkIn").toString());
                    Date toLate = parser.parse(lateDeff);

                    if (checkIn.after(toLate)) {
                        if (!TimeAttendanceServices.checkIfDateOfficialHolidayOrOffDay(request, PayrollServices.convertFromStringToDate(item.get("AttDate").toString()).toLocalDate())) {
                            row.put("late", PersonUtilServices.timesCalculation(item.get("checkIn").toString(), lateDeff, "sub", "all"));
                        }
                    }
                }

//                row.put("notes", item.get("notes"));
                reportRows[reportRowsCount] = row;
                reportRowsCount++;
            }
            try {
                userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
                String printed_By = userLogin.getString("partyId");

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("printedBy", printed_By);
                params.put("partyId", partyId);
                params.put("partyName", HumanResEvents.getPartyName(request, response, (String) row.get("partyId")));
                params.put("fromDate", dates.get("startDate").toString());
                params.put("toDate", dates.get("endDate").toString());

                JRBeanArrayDataSource ds = new JRBeanArrayDataSource(reportRows);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

                JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void TimeAttendancePDFReport_Summary(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException, GenericEntityException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String partyId = request.getParameter("partyId");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String dept = request.getParameter("dept");

        List<String> employemntList = new ArrayList<String>();
        employemntList = TimeAttendanceServices.getDepartmentEmployees(request, dept);

        Map<String, String> data = new HashMap<String, String>();
        data = TimeAttendanceServices.getTimeAttendanceWorkingHAndIncrementH(request, year, month);

        GenericValue userLogin = null;
        response.setContentType("application/pdf");
        try {
//                String jrxmlFile = "C:/Users/DELL/JaspersoftWorkspace/test/ReportOneDataSet.jrxml";
            String jrxmlFile = "applications\\reports\\hr\\TimeAttendanceReport_Summary.jrxml";

            InputStream input = new FileInputStream(new File(jrxmlFile));
            JasperReport jasperReport = JasperCompileManager.compileReport(input);

            //to get start and and date
            Map<String, LocalDate> dates = new HashMap<String, LocalDate>();
            dates = TimeAttendanceServices.getTimeAttendanceStartAndEndDate(request, year, month);

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
            HashMap[] reportRows = null;
            reportRows = new HashMap[partyId_Join.size()];
            HashMap row = new HashMap();
            int reportRowsCount = 0;
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

                            if (!TimeAttendanceServices.checkIfDateOfficialHolidayOrOffDay(request, PayrollServices.convertFromStringToDate(subRow.get("AttDate").toString()).toLocalDate())) {
                                morningLate = PersonUtilServices.timesCalculation(morningLate, PersonUtilServices.timesCalculation(subRow.get("checkIn").toString(), lateDeff, "sub", "all"), "add", "positive");
                            }
                        }
                    }

                }

                String personalLeave = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "PERSONAL", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String businessLeave = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "BUSENISS", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String vacationAnnual = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "PERSONAL", item, Integer.parseInt(data.get("workDayHours")), "VACATION");

                String emergencyLeave = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "EMERGENCY", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String emergencyVacation = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "EMERGENCY", item, Integer.parseInt(data.get("workDayHours")), "VACATION");
                String sickLeave = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "SICK", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String sickVacation = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "SICK", item, Integer.parseInt(data.get("workDayHours")), "VACATION");
                String businessVacation = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "BUSENISS", item, Integer.parseInt(data.get("workDayHours")), "VACATION");

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
                int noOficialHolidays = TimeAttendanceServices.getOficialHolidays(request, dates.get("startDate"), dates.get("endDate"));

                System.out.println("noOficialHolidays : " + noOficialHolidays);

                int countDaysOfMonthInHours = countDaysOMonth * Integer.parseInt(data.get("workDayHours"));
                System.out.println("countDaysOMonthInHours " + countDaysOfMonthInHours);

                String officialWorkingHours = countDaysOfMonthInHours + ":00:00";
                System.out.println("officialWorkingHours " + officialWorkingHours);
                String hoursPlusMinus = "00:00:00";
                String daysPlusMinus = "00:00:00";

                int publicHolidaysInHours = noOficialHolidays * Integer.parseInt(data.get("workDayHours"));
                String totalWorkingHours_Deference = PersonUtilServices.timesCalculation(totalWorkingHours, morningLate, "sub", "all");

//                for (int i = 0; i < partyId_Join.size(); i++) {
                if (employemntList.contains(item)) {
                    row = new HashMap();
                    row.put("partyId", item);
                    row.put("partyName", HumanResEvents.getPartyName(request, response, item));
                    row.put("attendedWorkingHours", attendedWorkingHours);
                    row.put("personalLeave", personalLeave);
//                    row.put("emergencyLeave", list.get(i).get("emergencyLeave"));
                    row.put("businessLeave", businessLeave);
                    row.put("vacationAnnual", vacationAnnual);
                    row.put("otherVacation", otherVacation);
                    row.put("morningLate", morningLate);

                    String totalWorkingHours_new = PersonUtilServices.timesCalculation(totalWorkingHours, data.get("incrementHours") + ":00:00", "add", "all");
                    row.put("totalWorkingHours", totalWorkingHours_new);

                    String officialWorkingHours_new = PersonUtilServices.timesCalculation(officialWorkingHours, publicHolidaysInHours + ":00:00", "sub", "all");
                    row.put("officialWorkingHours", officialWorkingHours_new);
                    row.put("hoursPlusMinus", PersonUtilServices.timesCalculation(totalWorkingHours_new, officialWorkingHours_new, "sub", "all"));
                    row.put("daysPlusMinus", TimeAttendanceServices.timeDivision(PersonUtilServices.timesCalculation(totalWorkingHours_new, officialWorkingHours_new, "sub", "all"), Integer.parseInt(data.get("workDayHours").toString())).toString());

                    reportRows[reportRowsCount] = row;
                    reportRowsCount++;
                }
//                }
            }
            try {
                userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
                String printed_By = userLogin.getString("partyId");

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("printedBy", printed_By);
                params.put("dept", dept);
                params.put("fromDate", dates.get("startDate").toString());
                params.put("toDate", dates.get("endDate").toString());

                JRBeanArrayDataSource ds = new JRBeanArrayDataSource(reportRows);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

                JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void exportToExcel_TimeAttSumm(HttpServletRequest request, HttpServletResponse response) throws
            IOException, ParseException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String dept = request.getParameter("dept");

        List<String> employemntList = new ArrayList<String>();
        employemntList = TimeAttendanceServices.getDepartmentEmployees(request, dept);

        Map<String, String> data = new HashMap<String, String>();
        data = TimeAttendanceServices.getTimeAttendanceWorkingHAndIncrementH(request, year, month);
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Time Attendance Summary");

            XSSFCellStyle style = workbook.createCellStyle();
            style.setWrapText(true);

            Row rowTitle = sheet.createRow(0);

            rowTitle.setRowStyle(style);

            Cell cellTitle = rowTitle.createCell(0);
            cellTitle.setCellValue((String) "Party ID");
            rowTitle.getCell(0).setCellStyle(style);

            Cell cellTitle1 = rowTitle.createCell(1);
            cellTitle1.setCellValue((String) "Party Name");
            rowTitle.getCell(1).setCellStyle(style);

            Cell cellTitle2 = rowTitle.createCell(2);
            cellTitle2.setCellValue((String) "Year");
            rowTitle.getCell(1).setCellStyle(style);

            Cell cellTitle3 = rowTitle.createCell(3);
            cellTitle3.setCellValue((String) "Month");
            rowTitle.getCell(3).setCellStyle(style);

            Cell cellTitle4 = rowTitle.createCell(4);
            cellTitle4.setCellValue((String) "Attended Working Hours");
            rowTitle.getCell(4).setCellStyle(style);

            Cell cellTitle5 = rowTitle.createCell(5);
            cellTitle5.setCellValue((String) "Personal Leave");
            rowTitle.getCell(5).setCellStyle(style);

//            Cell cellTitle5 = rowTitle.createCell(5);
//            cellTitle5.setCellValue((String) "Emergency Leave");
//            rowTitle.getCell(5).setCellStyle(style);
            Cell cellTitle6 = rowTitle.createCell(6);
            cellTitle6.setCellValue((String) "Business Leave");
            rowTitle.getCell(6).setCellStyle(style);

            Cell cellTitle7 = rowTitle.createCell(7);
            cellTitle7.setCellValue((String) "Vacation Annual");
            rowTitle.getCell(7).setCellStyle(style);

            Cell cellTitle8 = rowTitle.createCell(8);
            cellTitle8.setCellValue((String) "Other Vacation");
            rowTitle.getCell(7).setCellStyle(style);

            Cell cellTitle9 = rowTitle.createCell(9);
            cellTitle9.setCellValue((String) "Morning Late");
            rowTitle.getCell(9).setCellStyle(style);

            Cell cellTitle10 = rowTitle.createCell(10);
            cellTitle10.setCellValue((String) "Total Working Hours");
            rowTitle.getCell(10).setCellStyle(style);

            Cell cellTitle11 = rowTitle.createCell(11);
            cellTitle11.setCellValue((String) "Official Working Hours");
            rowTitle.getCell(11).setCellStyle(style);

            Cell cellTitle12 = rowTitle.createCell(12);
            cellTitle12.setCellValue((String) "Hours(+/-)");
            rowTitle.getCell(12).setCellStyle(style);

            Cell cellTitle13 = rowTitle.createCell(13);
            cellTitle13.setCellValue((String) "days(+/-)");
            rowTitle.getCell(13).setCellStyle(style);

            //to get start and and date
            Map<String, LocalDate> dates = new HashMap<String, LocalDate>();
            dates = TimeAttendanceServices.getTimeAttendanceStartAndEndDate(request, year, month);

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
            int rowCount = 1;
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

                            if (!TimeAttendanceServices.checkIfDateOfficialHolidayOrOffDay(request, PayrollServices.convertFromStringToDate(subRow.get("AttDate").toString()).toLocalDate())) {
                                morningLate = PersonUtilServices.timesCalculation(morningLate, PersonUtilServices.timesCalculation(subRow.get("checkIn").toString(), lateDeff, "sub", "all"), "add", "positive");
                            }
                        }
                    }
                }


                String personalLeave = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "PERSONAL", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String businessLeave = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "BUSENISS", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String vacationAnnual = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "PERSONAL", item, Integer.parseInt(data.get("workDayHours")), "VACATION");

                String emergencyLeave = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "EMERGENCY", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String emergencyVacation = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "EMERGENCY", item, Integer.parseInt(data.get("workDayHours")), "VACATION");
                String sickLeave = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "SICK", item, Integer.parseInt(data.get("workDayHours")), "LEAVE");
                String sickVacation = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "SICK", item, Integer.parseInt(data.get("workDayHours")), "VACATION");
                String businessVacation = TimeAttendanceServices.getLeaveAndVacationInHours(request, dates.get("startDate"), dates.get("endDate"), "BUSENISS", item, Integer.parseInt(data.get("workDayHours")), "VACATION");

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
                int noOficialHolidays = TimeAttendanceServices.getOficialHolidays(request, dates.get("startDate"), dates.get("endDate"));

                System.out.println("noOficialHolidays : " + noOficialHolidays);

                int countDaysOfMonthInHours = countDaysOMonth * Integer.parseInt(data.get("workDayHours"));
                System.out.println("countDaysOMonthInHours " + countDaysOfMonthInHours);

                String officialWorkingHours = countDaysOfMonthInHours + ":00:00";
                System.out.println("officialWorkingHours " + officialWorkingHours);
                String hoursPlusMinus = "00:00:00";
                String daysPlusMinus = "00:00:00";

                int publicHolidaysInHours = noOficialHolidays * Integer.parseInt(data.get("workDayHours"));
                String totalWorkingHours_Deference = PersonUtilServices.timesCalculation(totalWorkingHours, morningLate, "sub", "all");

//                for (int i = 0; i < partyId_Join.size(); i++) {

                if (employemntList.contains(item)) {
                    Row row = sheet.createRow(rowCount++);

                    Cell cell = row.createCell(0);
                    cell.setCellValue((String) item);

                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue((String) HumanResEvents.getPartyName(request, response, item));

//                    row.put("partyName", HumanResEvents.getPartyName(request, response, item));

                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue((String) year);

                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue((String) month);

                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue((String) attendedWorkingHours);

                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue((String) personalLeave);

                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue((String) businessLeave);

                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue((String) vacationAnnual);

                    Cell cell8 = row.createCell(8);
                    cell8.setCellValue((String) otherVacation);

                    Cell cell9 = row.createCell(9);
                    cell9.setCellValue((String) morningLate);

                    totalWorkingHours = PersonUtilServices.timesCalculation(totalWorkingHours, data.get("incrementHours") + ":00:00", "add", "all");
                    Cell cell10 = row.createCell(10);
                    cell10.setCellValue((String) totalWorkingHours);

                    officialWorkingHours = PersonUtilServices.timesCalculation(officialWorkingHours, publicHolidaysInHours + ":00:00", "sub", "all");
                    Cell cell11 = row.createCell(11);
                    cell11.setCellValue((String) officialWorkingHours);

                    Cell cell12 = row.createCell(12);
                    cell12.setCellValue((String) PersonUtilServices.timesCalculation(totalWorkingHours, officialWorkingHours, "sub", "all"));

                    Cell cell13 = row.createCell(13);
                    cell13.setCellValue((String) TimeAttendanceServices.timeDivision(PersonUtilServices.timesCalculation(totalWorkingHours, officialWorkingHours, "sub", "all"), Integer.parseInt(data.get("workDayHours").toString())).toString());
                }

            }

            try {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment; filename=TimeAttendanceSummary_" + dept + ".xlsx");
                workbook.write(response.getOutputStream());
                workbook.close();
            } catch (IOException ex) {
                System.out.println(ex.getStackTrace());
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }


    }


    //Time Attendance For Employee
    public static void exportToExcel_Employee(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        try {

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Time Attendance");

            Delegator delegator = (Delegator) request.getAttribute("delegator");
            String partyId = request.getParameter("partyId");
            String year = request.getParameter("year");
            String month = request.getParameter("month");

            Map<String, LocalDate> dates = new HashMap<String, LocalDate>();
            dates = TimeAttendanceServices.getTimeAttendanceStartAndEndDate(request, year, month);

            List<EntityExpr> exprs = UtilMisc.toList(EntityCondition.makeCondition("AttDate", EntityOperator.GREATER_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("startDate"))),
                    EntityCondition.makeCondition("AttDate", EntityOperator.LESS_THAN_EQUAL_TO, java.sql.Date.valueOf(dates.get("endDate"))),
                    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId));
            EntityCondition condition = EntityCondition.makeCondition(exprs, EntityOperator.AND);

            System.out.println("from TimeAttendanceEmpl.isEmpty()");
            List<String> listOrder = new ArrayList<>();
            listOrder.add("timeAttendanceId");
            List<GenericValue> list = delegator.findList("TimeAttendance", condition, null, listOrder, null, true);

            //headers
            Row rowTitle = sheet.createRow(0);
            Cell cellTitle = rowTitle.createCell(0);
            cellTitle.setCellValue((String) "Party ID");

            Row rowTitle1 = sheet.createRow(1);
            Cell cellTitle1 = rowTitle.createCell(1);
            cellTitle1.setCellValue((String) "Party Name");

            Cell cellTitle2 = rowTitle.createCell(2);
            cellTitle2.setCellValue((String) "Date");

            Cell cellTitle3 = rowTitle.createCell(3);
            cellTitle3.setCellValue((String) "Week Day");

            Cell cellTitle4 = rowTitle.createCell(4);
            cellTitle4.setCellValue((String) "Day Type");

            Cell cellTitle5 = rowTitle.createCell(5);
            cellTitle5.setCellValue((String) "Check In");

            Cell cellTitle6 = rowTitle.createCell(6);
            cellTitle6.setCellValue((String) "Check Out");

            Cell cellTitle7 = rowTitle.createCell(7);
            cellTitle7.setCellValue((String) "notes");

            Cell cellTitle8 = rowTitle.createCell(8);
            cellTitle8.setCellValue((String) "late");

            Cell cellTitle9 = rowTitle.createCell(9);
            cellTitle9.setCellValue((String) "Work");
            int rowCount = 1;
            if (!list.isEmpty()) {
                for (GenericValue list_row : list) {
//                    System.out.println("form for");
//                    String line3[] = list_row.get("AttDate").toString().split("-");
//                    LocalDate AttDateLocalDate = LocalDate.of(Integer.valueOf(line3[0]), Integer.valueOf(line3[1]), Integer.valueOf(line3[2]));
//                    System.out.println("AttDateLocalDate " + AttDateLocalDate);
//                if ((AttDateLocalDate.isAfter(dates.get("startDate")) || AttDateLocalDate.isEqual(dates.get("startDate"))) && (AttDateLocalDate.isBefore(dates.get("endDate")) || AttDateLocalDate.isEqual(dates.get("endDate")))) {


                    Row row = sheet.createRow(rowCount++);

                    Cell cell = row.createCell(0);
                    cell.setCellValue((String) list_row.get("partyId").toString());

                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue((String) HumanResEvents.getPartyName(request, response, list_row.get("partyId").toString()));

                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue((String) list_row.get("AttDate").toString());

                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue((String) list_row.get("weekDay").toString());

                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue((String) list_row.get("dayType").toString());

                    Cell cell5 = row.createCell(5);
                    if (list_row.get("checkIn") == null) {
                        cell5.setCellValue((String) " ");
                    } else {
                        cell5.setCellValue((String) list_row.get("checkIn").toString());
                    }

                    Cell cell6 = row.createCell(6);
                    if (list_row.get("checkOut") == null) {
                        cell6.setCellValue((String) " ");
                    } else {
                        cell6.setCellValue((String) list_row.get("checkOut").toString());
                    }

                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue((String) " ");


                    if (!(list_row.get("checkIn") == null)) {
                        Cell cell8 = row.createCell(8);

                        String lateDeff = PayrollServices.getGlobalPayrollSetting(request, "TO_LATE");
                        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                        Date checkIn = parser.parse(list_row.get("checkIn").toString());
                        Date toLate = parser.parse(lateDeff);
                        if (checkIn.after(toLate)) {
                            if (!TimeAttendanceServices.checkIfDateOfficialHolidayOrOffDay(request, PayrollServices.convertFromStringToDate(list_row.get("AttDate").toString()).toLocalDate())) {
                                cell8.setCellValue((String) PersonUtilServices.timesCalculation(list_row.get("checkIn").toString(), lateDeff, "sub", "all"));
                            }

                        }
                    }

                    if (!(list_row.get("checkOut") == null) && !(list_row.get("checkIn") == null)) {
                        Cell cell9 = row.createCell(9);
                        cell9.setCellValue((String) " ");
                        cell9.setCellValue((String) PersonUtilServices.timesCalculation(list_row.get("checkOut").toString(), list_row.get("checkIn").toString(), "sub", "all"));

                    }
//                }
                }
            }

            try {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment; filename=TimeAttendance_" + partyId + ".xlsx");
                workbook.write(response.getOutputStream());
                workbook.close();
            } catch (IOException ex) {
                System.out.println(ex.getStackTrace());
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(TimeAttendanceServices.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}
