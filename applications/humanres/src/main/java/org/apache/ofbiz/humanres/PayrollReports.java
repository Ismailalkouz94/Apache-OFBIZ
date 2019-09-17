package org.apache.ofbiz.humanres;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.entity.Delegator;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.jdbc.SQLProcessor;
import org.apache.ofbiz.entity.util.EntityQuery;

import static org.apache.ofbiz.humanres.PayrollServices.getAllowenceDescription;

import org.apache.ofbiz.humanres.payrollReport.ObjRecordEmployeeExpenseHeader;
import org.apache.ofbiz.humanres.payrollReport.ObjRecordCostCentersPayroll;
import org.apache.ofbiz.accounting.GLTransactionClass;

import org.json.simple.JSONObject;
import org.apache.ofbiz.humanres.payrollReport.ObjRecordSalarySlipAll;
import org.json.simple.JSONArray;

/**
 * @author Ajarmeh
 */
public class PayrollReports {

    public final static String module = PayrollReports.class.getName();
    public static final String securityProperties = "security.properties";
    public static final String resourceError = "HumanResErrorUiLabels";
    private static final String keyValue = UtilProperties.getPropertyValue(securityProperties, "login.secret_key_string");
    private static final int TRANS_ITEM_SEQUENCE_ID_DIGITS = 5; // this is the number of digits used for transItemSeqId: 00001, 00002...
    public static Vector<ObjRecordSalarySlipAll> cashRequestsList = new Vector<ObjRecordSalarySlipAll>();
    public static Vector<ObjRecordEmployeeExpenseHeader> employeeExpenseHeaderList = new Vector<ObjRecordEmployeeExpenseHeader>();
    public static Vector<ObjRecordCostCentersPayroll> listCostCentersExpense = new Vector<ObjRecordCostCentersPayroll>();
    public static Vector<ObjRecordCostCentersPayroll> listCostCentersNonExpense = new Vector<ObjRecordCostCentersPayroll>();

    public static String getGlAccountIdExpense(HttpServletRequest request, HttpServletResponse response) {

        ResultSet resultSet = null;
        List<String> listGl = new ArrayList<String>();
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            SQLProcessor sqlProcessor = new SQLProcessor(delegator, delegator.getGroupHelperInfo("org.apache.ofbiz"));
            //cost center
            String sqlStr2 = "SELECT SUM(ES.AMOUNT),ES.ORGANIZATION_PARTY_ID\n"
                    + "FROM EMPL_SALARY ES\n"
                    + "WHERE ES.IS_POSTED         = 'Y'\n"
                    + "AND ES. IS_COMPANY = 'N'\n"
                    + "AND ES.AMOUNT    < 0\n"
                    + "AND ES.YEAR    >= 2016\n"
                    + "AND ES.YEAR    <= 2018\n"
                    + "AND ES.MONTH    >= 1\n"
                    + "AND ES.MONTH    <= 12\n"
                    + "AND ES.GL_ACCOUNT_ID IN (SELECT GLA.GL_ACCOUNT_ID\n"
                    + "FROM GL_ACCOUNT_ORGANIZATION GLAO\n"
                    + "INNER JOIN GL_ACCOUNT GLA\n"
                    + "ON GLAO.GL_ACCOUNT_ID         = GLA.GL_ACCOUNT_ID\n"
                    + "WHERE GLA.GL_ACCOUNT_CLASS_ID  IN\n"
                    + "(SELECT GL_ACCOUNT_CLASS_ID\n"
                    + "FROM GL_ACCOUNT_CLASS\n"
                    + "START WITH GL_ACCOUNT_CLASS_ID       = 'EXPENSE'\n"
                    + "CONNECT BY prior GL_ACCOUNT_CLASS_ID = PARENT_CLASS_ID)\n"
                    + "AND GLAO.ORGANIZATION_PARTY_ID='Company')\n"
                    + "group by ES.ORGANIZATION_PARTY_ID";

            resultSet = sqlProcessor.executeQuery(sqlStr2);
            while (resultSet.next()) {
                String gl = resultSet.getString("GL_ACCOUNT_ID").toString();
                listGl.add(gl);
            }
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return "";
    }

    public static String cashRequests(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        double amount_double = 0.0;
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);

        Map<String, String> criteriaBasicSalary = new HashMap<String, String>();
        criteriaBasicSalary.put("Key", "BASIC_SALARY_ALLOWENCES_ID");

        Map<String, String> criteriaSocialSec = new HashMap<String, String>();
        criteriaSocialSec.put("Key", "SOCIAL_SECURITY_ALLOWENCES_ID");

        Map<String, String> criteriaTrans = new HashMap<String, String>();
        criteriaTrans.put("Key", "TRANSPORTATION_ALLOWANCE_ID");

        Map<String, String> criteriaEdu = new HashMap<String, String>();
        criteriaEdu.put("Key", "EDUCATION_ALLOWANCE_ID");

        BigDecimal totalBasicSalary = new BigDecimal("0");
        BigDecimal totalSocialSecId = new BigDecimal("0");
        BigDecimal totalTransId = new BigDecimal("0");
        BigDecimal totalEduId = new BigDecimal("0");
        BigDecimal totalAddition = new BigDecimal("0");
        BigDecimal totalDeduction = new BigDecimal("0");
        BigDecimal totalGrossBenefits = new BigDecimal("0");
        BigDecimal totalTotalCash = new BigDecimal("0");

        Map<String, Map<Object, Object>> values = new HashMap<String, Map<Object, Object>>();
        try {
            GenericValue resultBasicSalary = delegator.findOne("GlobalPayrollSettings", criteriaBasicSalary, true);
            String basicSalaryId = (String) resultBasicSalary.get("Value");

            GenericValue resultSocialSec = delegator.findOne("GlobalPayrollSettings", criteriaSocialSec, true);
            String socialSecId = (String) resultSocialSec.get("Value");

            GenericValue resultTrans = delegator.findOne("GlobalPayrollSettings", criteriaTrans, true);
            String transId = (String) resultTrans.get("Value");

            GenericValue resultEdu = delegator.findOne("GlobalPayrollSettings", criteriaEdu, true);
            String eduId = (String) resultEdu.get("Value");

            List<GenericValue> list = null;
            if (month == 0) {
                System.out.println("error month not found");
            } else {
                list = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
                System.out.println("** " + list);
            }
            for (GenericValue row : list) {
                String isCompany = row.get("isCompany").toString();
                String positionTypeId = PayrollServices.getEmplPositionTypeId(request, row.get("positionId").toString());
                String partyId = row.get("partyId").toString();
                String amount = row.get("amount") != null ? row.get("amount").toString() : "0";
                amount_double = (Double) row.get("amount");
                String allowenceId = row.get("allowenceId").toString();

                if (values.isEmpty()) {
                    Map<Object, Object> subValues = new HashMap<Object, Object>();
                    BigDecimal newAmount = new BigDecimal(amount);
                    subValues.put("basicSalary", 0);
                    subValues.put("socialSecId", 0);
                    subValues.put("transId", 0);
                    subValues.put("eduId", 0);
                    subValues.put("addition", 0);
                    subValues.put("deduction", 0);
                    subValues.put("grossBenefits", 0);
                    subValues.put("totalCash", 0);
                    subValues.put("partyId", partyId);
                    subValues.put("fullName", HumanResEvents.getPartyName(request, response, partyId));
                    subValues.put("organizationPartyId", row.get("organizationPartyId"));
                    subValues.put("positionId", PayrollServices.getEmplPositionTypeDescription(request, positionTypeId));
                    subValues.put("isPosted", row.get("isPosted"));

                    if (isCompany.equals("Y")) {
                        subValues.put("amountCompany", amount);
                        if (allowenceId.equals(socialSecId)) {
                            subValues.put("socialSecId", newAmount.abs());
                            totalSocialSecId = totalSocialSecId.add(newAmount.abs());

                            subValues.put("totalCash", newAmount.abs());
                            totalTotalCash = totalTotalCash.add(newAmount.abs());
                        }
                    } else {
                        if (allowenceId.equals(basicSalaryId)) {
                            subValues.put("basicSalary", amount);
                            totalBasicSalary = totalBasicSalary.add(newAmount);
                        } else if (allowenceId.equals(transId)) {
                            subValues.put("transId", amount);
                            totalTransId = totalTransId.add(newAmount);
                        } else if (allowenceId.equals(eduId)) {
                            subValues.put("eduId", amount);
                            totalEduId = totalEduId.add(newAmount);
                        } else if (amount_double > 0) {
                            subValues.put("addition", amount);
                            totalAddition = totalAddition.add(newAmount);
                        } else if (amount_double < 0) {
                            subValues.put("deduction", amount);
                            totalDeduction = totalDeduction.add(newAmount);
                        }

                        subValues.put("grossBenefits", amount);
                        totalGrossBenefits = totalGrossBenefits.add(newAmount);

                        subValues.put("totalCash", amount);
                        totalTotalCash = totalTotalCash.add(newAmount);
                    }

                    values.put(partyId, subValues);
                } else {
                    if (values.containsKey(partyId)) {
                        Map<Object, Object> subValues = new HashMap<Object, Object>();
                        subValues = values.get(partyId);
                        BigDecimal oldGrossBenefits = new BigDecimal(values.get(partyId).get("grossBenefits").toString());
                        BigDecimal oldTotalCash = new BigDecimal(values.get(partyId).get("totalCash").toString());

                        BigDecimal newAmount = new BigDecimal(amount);
                        if (isCompany.equals("Y")) {
                            if (allowenceId.equals(socialSecId)) {
                                BigDecimal oldSocialSecId = new BigDecimal(values.get(partyId).get("socialSecId").toString());
                                subValues.put("socialSecId", oldSocialSecId.add(newAmount.abs()));
                                totalSocialSecId = totalSocialSecId.add(newAmount.abs());

                                subValues.put("totalCash", oldTotalCash.add(newAmount.abs()));
                                totalTotalCash = totalTotalCash.add(newAmount.abs());
                            }
                        } else {

                            if (allowenceId.equals(basicSalaryId)) {
                                BigDecimal oldBasic = new BigDecimal(values.get(partyId).get("basicSalary").toString());
                                subValues.put("basicSalary", oldBasic.add(newAmount));
                                totalBasicSalary = totalBasicSalary.add(newAmount);
                            } else if (allowenceId.equals(transId)) {
                                BigDecimal oldTransId = new BigDecimal(values.get(partyId).get("transId").toString());
                                subValues.put("transId", oldTransId.add(newAmount));
                                totalTransId = totalTransId.add(newAmount);
                            } else if (allowenceId.equals(eduId)) {
                                BigDecimal oldEduId = new BigDecimal(values.get(partyId).get("eduId").toString());
                                subValues.put("eduId", oldEduId.add(newAmount));
                                subValues.put("eduId", amount);
                            } else if (amount_double > 0) {
                                BigDecimal oldAddition = new BigDecimal(values.get(partyId).get("addition").toString());
                                subValues.put("addition", oldAddition.add(newAmount));
                                totalAddition = totalAddition.add(newAmount);
                            } else if (amount_double < 0) {
                                BigDecimal oldDeduction = new BigDecimal(values.get(partyId).get("deduction").toString());
                                subValues.put("deduction", oldDeduction.add(newAmount));
                                totalDeduction = totalDeduction.add(newAmount);
                            }

                            subValues.put("grossBenefits", oldGrossBenefits.add(newAmount));
                            totalGrossBenefits = totalGrossBenefits.add(newAmount);

                            subValues.put("totalCash", oldTotalCash.add(newAmount));
                            totalTotalCash = totalTotalCash.add(newAmount);
                        }
                        values.put(partyId, subValues);
                    } else {
                        Map<Object, Object> subValues = new HashMap<Object, Object>();
                        BigDecimal newAmount = new BigDecimal(amount);
                        subValues.put("basicSalary", 0);
                        subValues.put("socialSecId", 0);
                        subValues.put("transId", 0);
                        subValues.put("eduId", 0);
                        subValues.put("addition", 0);
                        subValues.put("deduction", 0);
                        subValues.put("grossBenefits", 0);
                        subValues.put("totalCash", 0);
                        subValues.put("partyId", partyId);
                        subValues.put("fullName", HumanResEvents.getPartyName(request, response, partyId));
                        subValues.put("organizationPartyId", row.get("organizationPartyId"));
                        subValues.put("positionId", PayrollServices.getEmplPositionTypeDescription(request, positionTypeId));
                        subValues.put("isPosted", row.get("isPosted"));

                        if (isCompany.equals("Y")) {
                            subValues.put("amountCompany", amount);
                            if (allowenceId.equals(socialSecId)) {
                                subValues.put("socialSecId", newAmount.abs());
                                totalSocialSecId = totalSocialSecId.add(newAmount.abs());

                                subValues.put("totalCash", newAmount.abs());
                                totalTotalCash = totalTotalCash.add(newAmount.abs());
                            }
                        } else {
                            if (allowenceId.equals(basicSalaryId)) {
                                subValues.put("basicSalary", amount);
                                totalBasicSalary = totalBasicSalary.add(newAmount);
                            } else if (allowenceId.equals(transId)) {
                                subValues.put("transId", amount);
                                totalTransId = totalTransId.add(newAmount);
                            } else if (allowenceId.equals(eduId)) {
                                subValues.put("eduId", amount);
                                totalEduId = totalEduId.add(newAmount);
                            } else if (amount_double > 0) {
                                subValues.put("addition", amount);
                                totalAddition = totalAddition.add(newAmount);
                            } else if (amount_double < 0) {
                                subValues.put("deduction", amount);
                                totalDeduction = totalDeduction.add(newAmount);
                            }

                            subValues.put("grossBenefits", amount);
                            totalGrossBenefits = totalGrossBenefits.add(newAmount);

                            subValues.put("totalCash", amount);
                            totalTotalCash = totalTotalCash.add(newAmount);
                        }
                        values.put(partyId, subValues);
                    }
                }
            }

            Map<Object, Object> totals = new HashMap<Object, Object>();
            totals.put("totalBasicSalary", totalBasicSalary);
            totals.put("totalSocialSecId", totalSocialSecId);
            totals.put("totalTransId", totalTransId);
            totals.put("totalEduId", totalEduId);
            totals.put("totalAddition", totalAddition);
            totals.put("totalDeduction", totalDeduction);
            totals.put("totalGrossBenefits", totalGrossBenefits);
            totals.put("totalTotalCash", totalTotalCash);
            values.put("totals", totals);

            Map<String, Map<Object, Object>> treeMap = new TreeMap<String, Map<Object, Object>>(values);
            System.out.println("** treeMap **");
            System.out.println(treeMap);

//            ObjRecordSalarySlipAll recordTotals = new ObjRecordSalarySlipAll();
            ObjRecordSalarySlipAll record = new ObjRecordSalarySlipAll();
            cashRequestsList.clear();
            for (Map.Entry<String, Map<Object, Object>> entry : treeMap.entrySet()) {
                if (!entry.getKey().equals("totals")) {
                    String basicSalary = entry.getValue().get("basicSalary") != null ? entry.getValue().get("basicSalary").toString() : "0";
                    String addition = entry.getValue().get("addition") != null ? entry.getValue().get("addition").toString() : "0";
                    String deduction = entry.getValue().get("deduction") != null ? entry.getValue().get("deduction").toString() : "0";
                    String socialSecIdSub = entry.getValue().get("socialSecId") != null ? entry.getValue().get("socialSecId").toString() : "0";
                    String transIdSub = entry.getValue().get("transId") != null ? entry.getValue().get("transId").toString() : "0";
                    String eduIdSub = entry.getValue().get("eduId") != null ? entry.getValue().get("eduId").toString() : "0";
                    String grossBenefitsSub = entry.getValue().get("grossBenefits") != null ? entry.getValue().get("grossBenefits").toString() : "0";
                    String totalCashSub = entry.getValue().get("totalCash") != null ? entry.getValue().get("totalCash").toString() : "0";

                    record = new ObjRecordSalarySlipAll();
                    record.setPartyId(entry.getValue().get("partyId") + "-" + entry.getValue().get("fullName"));
                    record.setOrganizationPartyId(entry.getValue().get("organizationPartyId") + "");
                    record.setIsPosted(entry.getValue().get("isPosted") + "");
                    record.setBasicSalary(basicSalary);
                    record.setAddition(addition);
                    record.setDeduction(deduction);
                    record.setSocialSecId(socialSecIdSub);
                    record.setTransId(transIdSub);
                    record.setEduId(eduIdSub);
                    record.setGrossBenefits(grossBenefitsSub);
                    record.setTotalCash(totalCashSub);
                    cashRequestsList.add(record);
                }
//                else {
//                    String totalBasicSalarySub = entry.getValue().get("totalBasicSalary") != null ? entry.getValue().get("totalBasicSalary").toString() : "0";
//                    String totalSocialSecIdSub = entry.getValue().get("totalSocialSecId") != null ? entry.getValue().get("totalSocialSecId").toString() : "0";
//                    String totalTransIdSub = entry.getValue().get("totalTransId") != null ? entry.getValue().get("totalTransId").toString() : "0";
//                    String totalEduIdSub = entry.getValue().get("totalEduId") != null ? entry.getValue().get("totalEduId").toString() : "0";
//                    String totalAdditionSub = entry.getValue().get("totalAddition") != null ? entry.getValue().get("totalAddition").toString() : "0";
//                    String totalDeductionSub = entry.getValue().get("totalDeduction") != null ? entry.getValue().get("totalDeduction").toString() : "0";
//                    String totalGrossBenefitsSub = entry.getValue().get("totalGrossBenefits") != null ? entry.getValue().get("totalGrossBenefits").toString() : "0";
//                    String totalTotalCashSub = entry.getValue().get("totalTotalCash") != null ? entry.getValue().get("totalTotalCash").toString() : "0";
//
//                    recordTotals.setPartyId("Totals");
//                    recordTotals.setOrganizationPartyId("");
//                    recordTotals.setIsPosted("");
//                    recordTotals.setBasicSalary(totalBasicSalarySub);
//                    recordTotals.setAddition(totalAdditionSub);
//                    recordTotals.setDeduction(totalDeductionSub);
//                    recordTotals.setSocialSecId(totalSocialSecIdSub);
//                    recordTotals.setTransId(totalTransIdSub);
//                    recordTotals.setEduId(totalEduIdSub);
//                    recordTotals.setGrossBenefits(totalGrossBenefitsSub);
//                    recordTotals.setTotalCash(totalTotalCashSub);
//                }
            }
//            cashRequestsList.add(recordTotals);
            System.out.println("** cashRequests **");
            System.out.println(treeMap);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
//            out.println(values);
            out.println(new JSONObject(values));
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String cashRequestsPdf(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        System.out.println("** cashRequestsPdf **");
        System.out.println(cashRequestsList);
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        String year = request.getParameter("year") + "";
        String month = request.getParameter("month") + "";
        System.out.println("year " + year + " month" + month);
        ResultSet resultSet = null;
        GenericValue userLogin = null;
        response.setContentType("application/pdf");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("partyId", "Company");
        try {
            GenericValue resultPartyGroup = delegator.findOne("PartyGroup", criteria, true);
            String groupName = (String) resultPartyGroup.get("groupName");

            String jrxmlFile = "applications\\reports\\payroll\\cashRequests.jrxml";

            InputStream input = new FileInputStream(new File(jrxmlFile));
            JasperReport jasperReport = JasperCompileManager.compileReport(input);

            try {
                userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
                String printed_By = userLogin.getString("partyId");
                String userLoginId = userLogin.getString("userLoginId");

                Map<String, Object> params = new HashMap<String, Object>();
//                params.put("company", request.getParameter("company") + " - " + request.getParameter("groupName"));
                params.put("groupName", groupName);
                params.put("userLoginId", userLoginId);
                params.put("year", year);
                params.put("month", month);

//                JRBeanArrayDataSource ds = new JRBeanArrayDataSource(listSalarySlipAll);
                JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(cashRequestsList);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

                JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        return "";
    }

    public static String employeeExpenseHeaderPdf(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        System.out.println("** employeeExpenseHeaderPdf **");

        response.setContentType("application/pdf");
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        GenericValue userLogin = null;
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        Map<String, Object> criteria = new HashMap<String, Object>();

        String partyId = request.getParameter("partyId");
        String expenseType = request.getParameter("expenseType");

        criteria.put("partyId", partyId);
        criteria.put("expenseType", expenseType);
        criteria.put("paymentTypeId", "PAY_TYPE_SEPARATED");

        Map<String, String> criteriaPartyGroup = new HashMap<String, String>();
        criteriaPartyGroup.put("partyId", "Company");

        String isCompany = "";
        Map<Integer, Map<Object, Object>> values = new HashMap<Integer, Map<Object, Object>>();
        try {
            List<GenericValue> list = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            System.out.println("***");
            System.out.println(list);
            if (list.size() != 0) {
                for (GenericValue row : list) {
                    isCompany = (String) row.get("isCompany");

                    if (!isCompany.equals("Y")) {
                        String amount = row.get("amount") != null ? row.get("amount").toString() : "0";
                        Integer expenseIdValue = Integer.parseInt((String) row.get("expenseId"));
                        System.out.println("** expenseIdValue " + expenseIdValue);
                        if (values.isEmpty()) {
                            Map<Object, Object> subValues = new HashMap<Object, Object>();

                            subValues.put("expenseId", expenseIdValue);
                            subValues.put("partyId", row.get("partyId"));
                            subValues.put("fullName", HumanResEvents.getPartyName(request, response, (String) row.get("partyId")));
                            subValues.put("organizationPartyId", (String) row.get("organizationPartyId"));
                            subValues.put("allowenceId", getAllowenceDescription(request, (String) row.get("allowenceId")));
                            subValues.put("amount", row.get("amount"));
                            subValues.put("glAccountId", row.get("glAccountId"));
                            subValues.put("year", row.get("year"));
                            subValues.put("month", row.get("month"));
                            subValues.put("isPosted", row.get("isPosted"));
                            subValues.put("isCompany", row.get("isCompany"));

                            values.put(expenseIdValue, subValues);
                        } else {
                            if (values.containsKey(expenseIdValue)) {
                                Map<Object, Object> subValues = new HashMap<Object, Object>();
                                subValues = values.get(expenseIdValue);

                                BigDecimal newAmount = new BigDecimal(amount);
                                BigDecimal oldAmount = new BigDecimal(values.get(expenseIdValue).get("amount").toString());

                                subValues.put("expenseId", expenseIdValue);
                                subValues.put("partyId", row.get("partyId"));
                                subValues.put("fullName", HumanResEvents.getPartyName(request, response, (String) row.get("partyId")));
                                subValues.put("organizationPartyId", (String) row.get("organizationPartyId"));
                                subValues.put("allowenceId", getAllowenceDescription(request, (String) row.get("allowenceId")));
                                subValues.put("amount", oldAmount.add(newAmount));
                                subValues.put("glAccountId", row.get("glAccountId"));
                                subValues.put("year", row.get("year"));
                                subValues.put("month", row.get("month"));
                                subValues.put("isPosted", row.get("isPosted"));
                                subValues.put("isCompany", row.get("isCompany"));

                                values.put(expenseIdValue, subValues);
                            } else {
                                Map<Object, Object> subValues = new HashMap<Object, Object>();

                                subValues.put("expenseId", expenseIdValue);
                                subValues.put("partyId", row.get("partyId"));
                                subValues.put("fullName", HumanResEvents.getPartyName(request, response, (String) row.get("partyId")));
                                subValues.put("organizationPartyId", (String) row.get("organizationPartyId"));
                                subValues.put("allowenceId", getAllowenceDescription(request, (String) row.get("allowenceId")));
                                subValues.put("amount", row.get("amount"));
                                subValues.put("glAccountId", row.get("glAccountId"));
                                subValues.put("year", row.get("year"));
                                subValues.put("month", row.get("month"));
                                subValues.put("isPosted", row.get("isPosted"));
                                subValues.put("isCompany", row.get("isCompany"));

                                values.put(expenseIdValue, subValues);
                            }
                        }
                    }
                }
            }
            Map<Integer, Map<Object, Object>> treeMap = new TreeMap<Integer, Map<Object, Object>>(values);
            System.out.println("** treeMap **");
            System.out.println(treeMap);

            ObjRecordEmployeeExpenseHeader record = new ObjRecordEmployeeExpenseHeader();
            employeeExpenseHeaderList.clear();

            for (Map.Entry<Integer, Map<Object, Object>> entry : treeMap.entrySet()) {
                record = new ObjRecordEmployeeExpenseHeader();
                record.setExpenseId(entry.getValue().get("expenseId") + "");
                record.setPartyId(entry.getValue().get("partyId") + "-" + entry.getValue().get("fullName"));
                record.setOrganizationPartyId(entry.getValue().get("organizationPartyId") + "");
                record.setAllowenceId(entry.getValue().get("allowenceId") + "");
                record.setAmount(entry.getValue().get("amount") + "");
                record.setYear(entry.getValue().get("year") + "");
                record.setMonth(entry.getValue().get("month") + "");
                record.setIsPosted(entry.getValue().get("isPosted") + "");

                employeeExpenseHeaderList.add(record);
            }

            GenericValue resultPartyGroup = delegator.findOne("PartyGroup", criteriaPartyGroup, true);
            String groupName = (String) resultPartyGroup.get("groupName");

            String jrxmlFile = "applications\\reports\\payroll\\employeeExpenseHeader.jrxml";

            InputStream input = new FileInputStream(new File(jrxmlFile));
            JasperReport jasperReport = JasperCompileManager.compileReport(input);

            userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
            String printed_By = userLogin.getString("partyId");
            String userLoginId = userLogin.getString("userLoginId");

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("groupName", groupName);
            params.put("userLoginId", userLoginId);
//            params.put("year", year);
//            params.put("month", month);

            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(employeeExpenseHeaderList);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public static String getLogoUrl(HttpServletRequest request) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String FOLANG = (String) request.getSession().getAttribute("FOLANG");

        Map<String, String> criteriaLogo = new HashMap<String, String>();
        if (FOLANG.equals("en")) {
            criteriaLogo.put("Key", "COMPANY_LOGO_EN");
        } else if (FOLANG.equals("ar")) {
            criteriaLogo.put("Key", "COMPANY_LOGO_AR");
        }
        String imageUrl = "";
        try {
            GenericValue resultConfigurationSettings = delegator.findOne("ConfigurationSettings", criteriaLogo, true);
            imageUrl = (String) resultConfigurationSettings.get("Value");
            imageUrl = "framework/images/webapp" + imageUrl;
            System.out.println("imageUrl **" + imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageUrl;
    }

    public static String costCentersPayrollExp(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        double amount_double = 0.0;
        Set<String> organization_Distinct = new HashSet<String>();

        Long year = Long.valueOf(request.getParameter("year"));
        Long month = Long.valueOf(request.getParameter("month"));
        String isPosted = (String) request.getParameter("isPosted");

        List<EntityCondition> criteriaFields = new LinkedList<EntityCondition>();
        criteriaFields.add(EntityCondition.makeCondition("year", year));
        criteriaFields.add(EntityCondition.makeCondition("month", month));
        criteriaFields.add(EntityCondition.makeCondition("isPosted", isPosted));
        criteriaFields.add(EntityCondition.makeCondition("organizationPartyId", ""));
//        criteriaFields.add(EntityCondition.makeCondition("isCompany", "N"));

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("isPosted", isPosted);

        List<String> listOrder = new ArrayList<>();
        listOrder.add("partyId");

        Map<String, String> criteriaAllowDescription = new HashMap<String, String>();
        String allowDescription = "";

        BigDecimal totalDR = new BigDecimal("0");
        BigDecimal totalCR = new BigDecimal("0");
        BigDecimal totalBalance = new BigDecimal("0");

        Map<String, Object> criteriaPartyGroup = new HashMap<String, Object>();
        Map<String, Map<Object, Object>> values = new HashMap<String, Map<Object, Object>>();
        try {
            List<GenericValue> listEmplSalary = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : listEmplSalary) {
                organization_Distinct.add(row.get("organizationPartyId").toString());
            }
            System.out.println("** organization_Distinct **" + organization_Distinct);
            List<GenericValue> list = null;
//            if (month == 0) {
//                System.out.println("error month not found");
//            } else {
//                list = EntityQuery.use(delegator).from("EmplSalary").where(criteriaFields).queryList();
//                System.out.println("*2* " + list);
//            }
            for (String itemOrg : organization_Distinct) {
                criteriaFields.set(3, EntityCondition.makeCondition("organizationPartyId", itemOrg));
                list = EntityQuery.use(delegator).from("EmplSalary").where(criteriaFields).queryList();
                System.out.println("*1* organizationPartyId " + itemOrg);
                BigDecimal netSalary = new BigDecimal(0);

                for (GenericValue row : list) {
//                String partyId = row.get("partyId").toString();
                    String allow = row.get("allowenceId").toString();
                    String organizationPartyId = (String) row.get("organizationPartyId");
                    String amount = row.get("amount") != null ? row.get("amount").toString() : "0";
                    amount_double = (Double) row.get("amount");
                    System.out.println("*2* organizationPartyId " + organizationPartyId + " allow " + allow);
                    String isCompany = row.get("isCompany").toString();

                    criteriaAllowDescription.put("allowenceId", allow);
                    GenericValue resultAllowDescription = delegator.findOne("Allowences", criteriaAllowDescription, true);
                    allowDescription = (String) resultAllowDescription.get("description");

                    criteriaPartyGroup.put("partyId", organizationPartyId);
                    GenericValue resultPartyGroup = delegator.findOne("PartyGroup", criteriaPartyGroup, true);
                    String groupName = (String) resultPartyGroup.get("groupName");

                    if (values.isEmpty()) {
                        Map<Object, Object> subValues = new HashMap<Object, Object>();
                        BigDecimal newAmount = new BigDecimal(amount);

                        subValues.put("organizationPartyId", organizationPartyId);
                        subValues.put("groupName", groupName);
                        subValues.put("allowenceId", allowDescription);
                        if (amount_double > 0) {
                            subValues.put("DR", amount);
                            totalDR = totalDR.add(newAmount);
                            subValues.put("CR", "0");
                        } else if (amount_double < 0) {
                            subValues.put("CR", amount);
                            totalCR = totalCR.add(newAmount);
                            subValues.put("DR", "0");
                        }
                        subValues.put("balance", amount);
                        totalBalance = totalBalance.add(newAmount);
                        subValues.put("isPosted", row.get("isPosted"));

                        values.put(organizationPartyId + allow, subValues);
                    } else {
                        if (values.containsKey(organizationPartyId + allow)) {
                            Map<Object, Object> subValues = new HashMap<Object, Object>();
                            subValues = values.get(organizationPartyId + allow);

                            BigDecimal oldAmountDR = new BigDecimal(values.get(organizationPartyId + allow).get("DR").toString());
                            BigDecimal oldAmountCR = new BigDecimal(values.get(organizationPartyId + allow).get("CR").toString());
                            BigDecimal oldBalance = new BigDecimal(values.get(organizationPartyId + allow).get("balance").toString());
                            BigDecimal newAmount = new BigDecimal(amount);
                            System.out.println("** oldAmountDR " + oldAmountDR);
                            System.out.println("** newAmount   " + newAmount);
                            subValues.put("organizationPartyId", organizationPartyId);
                            subValues.put("groupName", groupName);
                            subValues.put("allowenceId", allowDescription);
                            if (amount_double > 0) {
                                subValues.put("DR", oldAmountDR.add(newAmount));
                                totalDR = totalDR.add(newAmount);
                            } else if (amount_double < 0) {
                                subValues.put("CR", oldAmountCR.add(newAmount));
                                totalCR = totalCR.add(newAmount);
                            }
                            subValues.put("balance", oldBalance.add(newAmount));
                            totalBalance = totalBalance.add(newAmount);
                            subValues.put("isPosted", row.get("isPosted"));

                            values.put(organizationPartyId + allow, subValues);
                        } else {
                            Map<Object, Object> subValues = new HashMap<Object, Object>();
                            BigDecimal newAmount = new BigDecimal(amount);

                            subValues.put("organizationPartyId", organizationPartyId);
                            subValues.put("groupName", groupName);
                            subValues.put("allowenceId", allowDescription);
                            if (amount_double > 0) {
                                subValues.put("DR", amount);
                                totalDR = totalDR.add(newAmount);
                                subValues.put("CR", "0");
                            } else if (amount_double < 0) {
                                subValues.put("CR", amount);
                                totalCR = totalCR.add(newAmount);
                                subValues.put("DR", "0");
                            }
                            subValues.put("balance", amount);
                            totalBalance = totalBalance.add(newAmount);
                            subValues.put("isPosted", row.get("isPosted"));

                            values.put(organizationPartyId + allow, subValues);
                        }
                    }
                    if (!isCompany.equals("Y")) {
                        BigDecimal subNetSalary = new BigDecimal(amount);
                        netSalary = netSalary.add(subNetSalary);

                        Map<Object, Object> subValues = new HashMap<Object, Object>();
                        BigDecimal newAmount = new BigDecimal(amount);
                        subValues.put("organizationPartyId", organizationPartyId);
                        subValues.put("groupName", groupName);
                        subValues.put("allowenceId", "صافي الراتب");
                        subValues.put("CR", netSalary);
                        totalCR = totalCR.add(newAmount);
                        subValues.put("DR", "0");
                        subValues.put("balance", netSalary);
                        totalBalance = totalBalance.add(newAmount);
                        values.put(organizationPartyId +"Net_Salary", subValues);
                    }
                }
                System.out.println("** values " + values);
            } // end for of organization_Distinct
            Map<Object, Object> totals = new HashMap<Object, Object>();
            totals.put("totalDR", totalDR);
            totals.put("totalCR", totalCR);
            totals.put("totalBalance", totalBalance);
            values.put("totals", totals);

            Map<String, Map<Object, Object>> treeMap = new TreeMap<String, Map<Object, Object>>(values);
            System.out.println("** treeMap **");
            System.out.println(treeMap);

            ObjRecordCostCentersPayroll record = new ObjRecordCostCentersPayroll();

            listCostCentersExpense.clear();
            for (Map.Entry<String, Map<Object, Object>> entry : treeMap.entrySet()) {
                if (!entry.getKey().equals("totals")) {
                    System.out.println("entry.getValue().get(\"allowenceId\") " + entry.getValue().get("allowenceId"));
                    record = new ObjRecordCostCentersPayroll();
                    record.setOrganizationPartyId(entry.getValue().get("organizationPartyId") + "");
                    record.setAllowenceId(entry.getValue().get("allowenceId") + "");
                    record.setGroupName(entry.getValue().get("groupName") + "");
                    record.setDR(entry.getValue().get("DR") + "");
                    record.setCR(entry.getValue().get("CR") + "");
                    record.setBalance(entry.getValue().get("balance") + "");
                    listCostCentersExpense.add(record);
                    System.out.println("** record " + record.getAllowenceId());
                }
            }
            System.out.println("** costCentersPayrollExp **");
            System.out.println(listCostCentersExpense.get(0).getAllowenceId());
            System.out.println(treeMap);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(new JSONObject(values));
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String costCentersPayrollExpPdf(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        System.out.println("** costCentersPayrollExpPdf **");
        System.out.println(listCostCentersExpense);
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        String year = request.getParameter("year") + "";
        String month = request.getParameter("month") + "";
        String isPosted = (String) request.getParameter("isPosted");
//        String expenseFlag = (String) request.getParameter("expenseFlag");
        System.out.println("year " + year + " month" + month + " isPosted" + isPosted);

        ResultSet resultSet = null;
        GenericValue userLogin = null;
        response.setContentType("application/pdf");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("partyId", "Company");
        try {
            GenericValue resultPartyGroup = delegator.findOne("PartyGroup", criteria, true);
            String groupName = (String) resultPartyGroup.get("groupName");

            String jrxmlFile = "applications\\reports\\payroll\\costCentersPayroll.jrxml";

            InputStream input = new FileInputStream(new File(jrxmlFile));
            JasperReport jasperReport = JasperCompileManager.compileReport(input);
            try {
                userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
                String printed_By = userLogin.getString("partyId");
                String userLoginId = userLogin.getString("userLoginId");

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("groupName", groupName);
                params.put("userLoginId", userLoginId);
//                params.put("imageUrl", PayrollReports.getLogoUrl(request));
                params.put("year", year);
                params.put("month", month);
                params.put("isPosted", isPosted);
                params.put("reportName", "Cost Centers / Expense");

                JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listCostCentersExpense);
//                if (expenseFlag.equals("Y")) {
//                    params.put("reportName", "Cost Centers / Expense");
//                    ds = new JRBeanCollectionDataSource(listCostCentersExpense);
//                } else if (expenseFlag.equals("N")) {
//                    params.put("reportName", "Cost Centers / Non Expense");
//                    ds = new JRBeanCollectionDataSource(listCostCentersNonExpense);
//                }

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

                JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return "";
    }

    public static String costCentersPayrollExpDetails(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        double amount_double = 0.0;
        Long year = Long.valueOf(request.getParameter("year"));
        Long month = Long.valueOf(request.getParameter("month"));
        String isPosted = (String) request.getParameter("isPosted");
        String expenseFlag = (String) request.getParameter("expenseFlag");

        List<String> glAccountIdExpenses = GLTransactionClass.getGlAccountIdExpense(request, response);
        System.out.println("*glAccountIdExpenses* " + glAccountIdExpenses);

        List<EntityCondition> criteriaFields = new LinkedList<EntityCondition>();
        criteriaFields.add(EntityCondition.makeCondition("year", year));
        criteriaFields.add(EntityCondition.makeCondition("month", month));
        criteriaFields.add(EntityCondition.makeCondition("isPosted", isPosted));
        criteriaFields.add(EntityCondition.makeCondition("isCompany", "N"));
        if (expenseFlag.equals("Y")) {
            System.out.println("1 expenseFlag " + expenseFlag);
            criteriaFields.add(EntityCondition.makeCondition("glAccountId", EntityOperator.IN, glAccountIdExpenses));
        } else if (expenseFlag.equals("N")) {
            System.out.println("2 expenseFlag " + expenseFlag);
            criteriaFields.add(EntityCondition.makeCondition("glAccountId", EntityOperator.NOT_IN, glAccountIdExpenses));
        }

        BigDecimal totalDR = new BigDecimal("0");
        BigDecimal totalCR = new BigDecimal("0");
        BigDecimal totalBalance = new BigDecimal("0");
        Map<String, Object> criteriaPartyGroup = new HashMap<String, Object>();
        Map<String, Map<Object, Object>> values = new HashMap<String, Map<Object, Object>>();
        try {
            List<GenericValue> list = null;
            if (month == 0) {
                System.out.println("error month not found");
            } else {
                list = EntityQuery.use(delegator).from("EmplSalary").where(criteriaFields).queryList();
                System.out.println("*2* " + list);
            }
            for (GenericValue row : list) {
//                String partyId = row.get("partyId").toString();
                String organizationPartyId = (String) row.get("organizationPartyId");
                String amount = row.get("amount") != null ? row.get("amount").toString() : "0";
                amount_double = (Double) row.get("amount");

                criteriaPartyGroup.put("partyId", organizationPartyId);
                GenericValue resultPartyGroup = delegator.findOne("PartyGroup", criteriaPartyGroup, true);
                String groupName = (String) resultPartyGroup.get("groupName");

                if (values.isEmpty()) {
                    Map<Object, Object> subValues = new HashMap<Object, Object>();
                    BigDecimal newAmount = new BigDecimal(amount);

                    subValues.put("organizationPartyId", organizationPartyId);
                    subValues.put("groupName", groupName);
                    if (amount_double > 0) {
                        subValues.put("DR", amount);
                        totalDR = totalDR.add(newAmount);
                        subValues.put("CR", "0");
                    } else if (amount_double < 0) {
                        subValues.put("CR", amount);
                        totalCR = totalCR.add(newAmount);
                        subValues.put("DR", "0");
                    }
                    subValues.put("balance", amount);
                    totalBalance = totalBalance.add(newAmount);
                    subValues.put("isPosted", row.get("isPosted"));

                    values.put(organizationPartyId, subValues);
                } else {
                    if (values.containsKey(organizationPartyId)) {
                        Map<Object, Object> subValues = new HashMap<Object, Object>();
                        subValues = values.get(organizationPartyId);

                        BigDecimal oldAmountDR = new BigDecimal(values.get(organizationPartyId).get("DR").toString());
                        BigDecimal oldAmountCR = new BigDecimal(values.get(organizationPartyId).get("CR").toString());
                        BigDecimal oldBalance = new BigDecimal(values.get(organizationPartyId).get("balance").toString());
                        BigDecimal newAmount = new BigDecimal(amount);
                        subValues.put("organizationPartyId", organizationPartyId);
                        subValues.put("groupName", groupName);
                        if (amount_double > 0) {
                            subValues.put("DR", oldAmountDR.add(newAmount));
                            totalDR = totalDR.add(newAmount);
                        } else if (amount_double < 0) {
                            subValues.put("CR", oldAmountCR.add(newAmount));
                            totalCR = totalCR.add(newAmount);
                        }
                        subValues.put("balance", oldBalance.add(newAmount));
                        totalBalance = totalBalance.add(newAmount);
                        subValues.put("isPosted", row.get("isPosted"));

                        values.put(organizationPartyId, subValues);
                    } else {
                        Map<Object, Object> subValues = new HashMap<Object, Object>();
                        BigDecimal newAmount = new BigDecimal(amount);

                        subValues.put("organizationPartyId", organizationPartyId);
                        subValues.put("groupName", groupName);
                        if (amount_double > 0) {
                            subValues.put("DR", amount);
                            totalDR = totalDR.add(newAmount);
                            subValues.put("CR", "0");
                        } else if (amount_double < 0) {
                            subValues.put("CR", amount);
                            totalCR = totalCR.add(newAmount);
                            subValues.put("DR", "0");
                        }
                        subValues.put("balance", amount);
                        totalBalance = totalBalance.add(newAmount);
                        subValues.put("isPosted", row.get("isPosted"));

                        values.put(organizationPartyId, subValues);
                    }
                }
            }

            Map<Object, Object> totals = new HashMap<Object, Object>();
            totals.put("totalDR", totalDR);
            totals.put("totalCR", totalCR);
            totals.put("totalBalance", totalBalance);
            values.put("totals", totals);

            Map<String, Map<Object, Object>> treeMap = new TreeMap<String, Map<Object, Object>>(values);
            System.out.println("** treeMap **");
            System.out.println(treeMap);

            ObjRecordCostCentersPayroll record = new ObjRecordCostCentersPayroll();

            if (expenseFlag.equals("Y")) {
                listCostCentersExpense.clear();
            } else if (expenseFlag.equals("N")) {
                listCostCentersNonExpense.clear();
            }
            for (Map.Entry<String, Map<Object, Object>> entry : treeMap.entrySet()) {
                if (!entry.getKey().equals("totals")) {
                    record = new ObjRecordCostCentersPayroll();
                    record.setOrganizationPartyId(entry.getValue().get("organizationPartyId") + "");
                    record.setGroupName(entry.getValue().get("groupName") + "");
                    record.setDR(entry.getValue().get("DR") + "");
                    record.setCR(entry.getValue().get("CR") + "");
                    record.setBalance(entry.getValue().get("balance") + "");
                    if (expenseFlag.equals("Y")) {
                        listCostCentersExpense.add(record);
                    } else if (expenseFlag.equals("N")) {
                        listCostCentersNonExpense.add(record);
                    }

                }
            }
            System.out.println("** costCentersPayrollExp **");
            System.out.println(treeMap);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(new JSONObject(values));
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
