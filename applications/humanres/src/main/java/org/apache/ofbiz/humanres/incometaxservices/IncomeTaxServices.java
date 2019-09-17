package org.apache.ofbiz.humanres.incometaxservices;

import org.apache.ofbiz.accounting.period.PeriodServices;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.humanres.PayrollServices;
import org.apache.ofbiz.humanres.employee.Employee;
import org.apache.ofbiz.humanres.leaves.LeavesServices;
import org.apache.ofbiz.service.LocalDispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IncomeTaxServices {

    public static int IncomeTaxService(HttpServletRequest request, String partyId, int month, int year) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        String userLoginId = (String) userLogin.getString("userLoginId");

//        String partyId = request.getParameter("partyId");
//        int year = Integer.parseInt(request.getParameter("year"));
//        int month = Integer.parseInt(request.getParameter("month"));
        LocalDate startCalculationMonth = LocalDate.of(year, month, 1);
        int lengthOfMonth = startCalculationMonth.lengthOfMonth();
        LocalDate endCalculationMonth = LocalDate.of(year, month, lengthOfMonth);

        BigDecimal deductedAmount = null;
        BigDecimal taxOfMonthSalary = new BigDecimal(0);
        BigDecimal totalAllowencesWithSalary = new BigDecimal(0);
        BigDecimal incomTaxEligibleAmountAnnual = new BigDecimal(0);
        BigDecimal incomTaxEligibleAmountMonthly = new BigDecimal(0);

        String incomeTaxAllowenceId = null;
        Map<String, Object> criteria = new HashMap<>();

        boolean beganTransaction = false;
        try {

            beganTransaction = TransactionUtil.begin();

            deleteIncomeTaxAllowence(request, partyId, month, year);

            Map IncomeTaxCalcData = getIncomeTaxCalcInfo(request, partyId);

            incomTaxEligibleAmountAnnual = (BigDecimal) IncomeTaxCalcData.get("maritalStatusAmount");
            incomTaxEligibleAmountMonthly = incomTaxEligibleAmountAnnual.divide(new BigDecimal(12));

            totalAllowencesWithSalary = getTotalIncomeTaxAmount(request, partyId, month, year);
            System.out.println("totalAllowencesWithSalary >> " + totalAllowencesWithSalary);

            // Not Eligable // غير غاضع للضريبه
            if (totalAllowencesWithSalary.compareTo(incomTaxEligibleAmountMonthly) < 0) {
                return 0;
            }

            //      200      =    1200     -      1000
            taxOfMonthSalary = totalAllowencesWithSalary.subtract(incomTaxEligibleAmountMonthly);

            deductedAmount = IncomeTaxCalc(request, taxOfMonthSalary,(String) IncomeTaxCalcData.get("incomeTaxId"));
            System.out.println("deductedAmount >>> " + deductedAmount);

            // to get incomeTax allowenceId
            incomeTaxAllowenceId = PayrollServices.getGlobalSetting(request, "INCOME_TAX_ALLOWENCES_ID", "INCOME_TAX");
            System.out.println("deductedAmount "+deductedAmount);
            // to insert income tax allowences
            java.sql.Date ourJavaDateObject = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            criteria = UtilMisc.toMap(
                    "Id", delegator.getNextSeqId("CompanyEmployeeAllowences"),
                    "typeId", "EMP",
                    "allowenceId", incomeTaxAllowenceId,
                    "partyId", partyId,
                    "TransDate", ourJavaDateObject,
                    "calculationId", "AMOUNT",
                    "percentageAmount", BigDecimal.valueOf(0.0),
                    "calculationAmount", BigDecimal.valueOf(0.0),
                    "amount", deductedAmount,
                    "startDate", java.sql.Date.valueOf(startCalculationMonth),
                    "endDate", java.sql.Date.valueOf(endCalculationMonth),
                    "stabilityId", "TEMP",
                    "userLoginId", userLoginId
            );
            Map<String, Object> createEmployeeAllowence = dispatcher.runSync("createAllowenceEmployee", criteria);

        } catch (Exception ex) {
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                Logger.getLogger(LeavesServices.class.getName()).log(Level.SEVERE, null, ex);
            } catch (GenericEntityException e) {
                Logger.getLogger(IncomeTaxServices.class.getName()).log(Level.SEVERE, null, e);
                System.out.println(">>> " + e.getMessage());
            }

        } finally {
            // only commit the transaction if we started one... this will throw an exception if it fails
            try {
                TransactionUtil.commit(beganTransaction);

            } catch (GenericEntityException e) {
//                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
            }
            return 0;
        }
    }

    public static int deleteIncomeTaxAllowence(HttpServletRequest request, String partyId, int month, int year) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        LocalDate startCalculationMonth = LocalDate.of(year, month, 1);
        int lengthOfMonth = startCalculationMonth.lengthOfMonth();
        LocalDate endCalculationMonth = LocalDate.of(year, month, lengthOfMonth);
        int remove = 0;
        try {
            String incomeTaxAllowenceId = PayrollServices.getGlobalSetting(request, "INCOME_TAX_ALLOWENCES_ID", "INCOME_TAX");

            List<EntityCondition> conditionList = new ArrayList<EntityCondition>();
            conditionList.add(EntityCondition.makeCondition("allowenceId", EntityOperator.EQUALS, incomeTaxAllowenceId));
            conditionList.add(EntityCondition.makeCondition("startDate", EntityOperator.EQUALS, java.sql.Date.valueOf(startCalculationMonth)));
            conditionList.add(EntityCondition.makeCondition("endDate", EntityOperator.EQUALS, java.sql.Date.valueOf(endCalculationMonth)));
            conditionList.add(EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId));

            List<GenericValue> list = delegator.findList("CompanyEmployeeAllowences", EntityCondition.makeCondition(conditionList, EntityOperator.AND), null, null, null, true);
            if (!list.isEmpty()) {
                Map<String, Object> deleteCriteria = new HashMap<String, Object>();
                deleteCriteria.put("Id", list.get(0).get("Id"));
                remove = delegator.removeByAnd("CompanyEmployeeAllowences", deleteCriteria);
            }
        } catch (GenericEntityException e) {
            e.printStackTrace();
        }

        return remove;
    }

    public static int deleteIncomeTaxAllowence(HttpServletRequest request, int month, int year) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        LocalDate startCalculationMonth = LocalDate.of(year, month, 1);
        int lengthOfMonth = startCalculationMonth.lengthOfMonth();
        LocalDate endCalculationMonth = LocalDate.of(year, month, lengthOfMonth);
        int remove = 0;
        try {
            String incomeTaxAllowenceId = PayrollServices.getGlobalSetting(request, "INCOME_TAX_ALLOWENCES_ID", "INCOME_TAX");

            List<EntityCondition> conditionList = new ArrayList<EntityCondition>();
            conditionList.add(EntityCondition.makeCondition("allowenceId", EntityOperator.EQUALS, incomeTaxAllowenceId));
            conditionList.add(EntityCondition.makeCondition("startDate", EntityOperator.EQUALS, java.sql.Date.valueOf(startCalculationMonth)));
            conditionList.add(EntityCondition.makeCondition("endDate", EntityOperator.EQUALS, java.sql.Date.valueOf(endCalculationMonth)));

            List<GenericValue> list = delegator.findList("CompanyEmployeeAllowences", EntityCondition.makeCondition(conditionList, EntityOperator.AND), null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue object : list) {
                    Map<String, Object> deleteCriteria = new HashMap<String, Object>();
                    deleteCriteria.put("Id", object.get("Id"));
                    remove = delegator.removeByAnd("CompanyEmployeeAllowences", deleteCriteria);
                }

            }
        } catch (GenericEntityException e) {
            e.printStackTrace();
        }

        return remove;
    }

    public static BigDecimal IncomeTaxCalc(HttpServletRequest request, BigDecimal taxOfMonthSalary, String IncomTaxId) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("IncomTaxId", IncomTaxId);

        BigDecimal tax_amount = new BigDecimal(0);
        BigDecimal amount = new BigDecimal(0);
        BigDecimal percentageCalculation = new BigDecimal(0);

        try {
            // Read IncomTax Settings
            List<String> listOrder = new ArrayList<>();
            listOrder.add("IncomTaxDetailsId");
            List<GenericValue> list = delegator.findList("IncomTaxDetails", EntityCondition.makeCondition(criteria), null, listOrder, null, true);

            for (GenericValue row : list) {
                amount = (BigDecimal) row.get("amount");
                percentageCalculation = (BigDecimal) row.get("percentageCalculation");

                if ((taxOfMonthSalary.compareTo(amount) > 0) && (amount.compareTo(BigDecimal.ZERO) == 0)) {
                    tax_amount = tax_amount.add((taxOfMonthSalary.multiply(percentageCalculation)).divide(new BigDecimal(100)));
                    break;
                } else if (taxOfMonthSalary.compareTo(amount) > 0) {
                    taxOfMonthSalary = taxOfMonthSalary.subtract(amount);
                    tax_amount = tax_amount.add((amount.multiply(percentageCalculation)).divide(new BigDecimal(100)));
                } else {
                    tax_amount = tax_amount.add((taxOfMonthSalary.multiply(percentageCalculation)).divide(new BigDecimal(100)));
                    break;
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tax_amount;
    }

    public static BigDecimal getTotalIncomeTaxAmount(HttpServletRequest request, String partyId, int month, int year) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String socialSecurityAllowenceId = "";
        BigDecimal sum = new BigDecimal(0);

        try {

            // To get Global Setting For Social Security Allowence Id
//            socialSecurityAllowenceId = PayrollServices.getGlobalSetting(request, "SOCIAL_SECURITY_ALLOWENCES_ID", "SOCIAL_SECURITY");
//            String loanAllowenceId = PayrollServices.getGlobalSetting(request, "LOAN_ALLOWENCE_ID", "LOAN");

            Set<String> allowencesNotEffectOnTax = new HashSet<>();
            List<GenericValue> allowencesList = delegator.findAll("Allowences", true);
            for (GenericValue item : allowencesList) {
                if (item.get("effectOnTax").equals("N")) {
                    allowencesNotEffectOnTax.add((String) item.get("allowenceId"));
                }
            }

            List<Employee> employeeData = PayrollServices.getSalaryCalculation_ForEachEmployee(request, partyId, month, year);
            for (Employee data : employeeData) {
//                if (!(data.getAllowenceId().equals(socialSecurityAllowenceId) || data.getAllowenceId().equals(loanAllowenceId)  || data.getAllowenceId().equals("6") )) {
                if (!allowencesNotEffectOnTax.contains(data.getAllowenceId())) {
                    sum = sum.add(BigDecimal.valueOf(data.getAmount()));
                }
            }
//            sum = sum.add(PayrollServices.getBasicSalary(request, partyId));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sum;
    }

    // return maritalStatus_Amount , Income_tax_Id and maritalStatus
    public static Map getIncomeTaxCalcInfo(HttpServletRequest request, String partyId) throws
            SQLException, ParseException, IOException {

        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, String> criteria = new HashMap<String, String>();

        criteria.put("partyId", partyId);
        String maritalStatus = null;
        BigDecimal maritalStatus_Amount = new BigDecimal(0);
        String Income_tax_Id = null;

        boolean wifeIsWorking = false;
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> list = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : list) {
                if (row.get("maritalStatus") != null) {
                    maritalStatus = row.get("maritalStatus").toString();
                }
            }
            System.out.println("martial status " + maritalStatus);

            Map<String, String> familyMemberCriteria = new HashMap<String, String>();
            familyMemberCriteria.put("relatedTo", partyId);
            List<GenericValue> familyMemberList = delegator.findList("Person", EntityCondition.makeCondition(familyMemberCriteria), null, null, null, true);
            for (GenericValue row : familyMemberList) {
                if (row.get("familyMemberType").toString().equals("WIFE") && row.get("wifeIsWorking").toString().equals("Y")) {
                    wifeIsWorking = true;
                }
            }

            System.out.println("wifeIsWorking " + wifeIsWorking);

            if (maritalStatus != null) {
                List<GenericValue> IncomTaxlist = delegator.findAll("IncomTax", true);
                for (GenericValue row : IncomTaxlist) {
                    if (row.get("endDate") == null) {
                        Income_tax_Id = row.get("IncomTaxId").toString();
                        if (maritalStatus.equals("S")) {
                            maritalStatus_Amount = (BigDecimal) row.get("singleAmount");
                        } else if (maritalStatus.equals("M") && wifeIsWorking == true) {
                            maritalStatus_Amount = (BigDecimal) row.get("singleAmount");
                            maritalStatus = "M_and_Wife";
                        } else {
                            maritalStatus_Amount = (BigDecimal) row.get("marriedAmount");
                        }
                    }
                }
            }

            result.put("maritalStatusAmount", maritalStatus_Amount);
            result.put("incomeTaxId", Income_tax_Id);
            result.put("maritalStatus", maritalStatus);

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
