/*
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
 */
import org.apache.ofbiz.base.util.UtilDateTime
import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.entity.condition.EntityCondition
import org.apache.ofbiz.entity.condition.EntityOperator
import org.apache.ofbiz.accounting.util.UtilAccounting
import org.apache.ofbiz.party.party.PartyWorker
import org.apache.ofbiz.service.calendar.ExpressionUiHelper

println "===================" 
println "IncomeStatementPNLPlan.groovy"

listOfRevenuePlan = null
for(i=0; i<12; i++){
    selectedMonth = Integer.valueOf(i)
    selectedMonthDate = UtilDateTime.toTimestamp((selectedMonth + 1), 1, UtilDateTime.getYear(UtilDateTime.nowTimestamp(), timeZone, locale), 0, 0, 0)
    fromDate1 = UtilDateTime.getMonthStart(selectedMonthDate, timeZone, locale)
    thruDate1 = UtilDateTime.getMonthEnd(selectedMonthDate, timeZone, locale)   

    fromDate = fromDate1
    thruDate = thruDate1
    glFiscalTypeId ="Plan"
    
    if (!glFiscalTypeId) {
        return
    }
    organizationPartyId ="Company"

    // Setup the divisions for which the report is executed
    List partyIds = PartyWorker.getAssociatedPartyIdsByRelationshipType(delegator, organizationPartyId, 'GROUP_ROLLUP')
    partyIds.add(organizationPartyId)

    // Get the group of account classes that will be used to position accounts in the proper section of the financial statement
    GenericValue revenueGlAccountClass = from("GlAccountClass").where("glAccountClassId", "REVENUE").cache(true).queryOne()
    List revenueAccountClassIds = UtilAccounting.getDescendantGlAccountClassIds(revenueGlAccountClass)
    GenericValue contraRevenueGlAccountClass = from("GlAccountClass").where("glAccountClassId", "CONTRA_REVENUE").cache(true).queryOne()
    List contraRevenueAccountClassIds = UtilAccounting.getDescendantGlAccountClassIds(contraRevenueGlAccountClass)
    GenericValue incomeGlAccountClass = from("GlAccountClass").where("glAccountClassId", "INCOME").cache(true).queryOne()
    List incomeAccountClassIds = UtilAccounting.getDescendantGlAccountClassIds(incomeGlAccountClass)
    GenericValue expenseGlAccountClass = from("GlAccountClass").where("glAccountClassId", "EXPENSE").cache(true).queryOne()
    List expenseAccountClassIds = UtilAccounting.getDescendantGlAccountClassIds(expenseGlAccountClass)
    GenericValue cogsExpenseGlAccountClass = from("GlAccountClass").where("glAccountClassId", "COGS_EXPENSE").cache(true).queryOne()
    List cogsExpenseAccountClassIds = UtilAccounting.getDescendantGlAccountClassIds(cogsExpenseGlAccountClass)
    GenericValue sgaExpenseGlAccountClass = from("GlAccountClass").where("glAccountClassId", "SGA_EXPENSE").cache(true).queryOne()
    List sgaExpenseAccountClassIds = UtilAccounting.getDescendantGlAccountClassIds(sgaExpenseGlAccountClass)
    GenericValue depreciationGlAccountClass = from("GlAccountClass").where("glAccountClassId", "DEPRECIATION").cache(true).queryOne()
    List depreciationAccountClassIds = UtilAccounting.getDescendantGlAccountClassIds(depreciationGlAccountClass)

    List mainAndExprs = []
    mainAndExprs.add(EntityCondition.makeCondition("organizationPartyId", EntityOperator.IN, partyIds))
    mainAndExprs.add(EntityCondition.makeCondition("isPosted", EntityOperator.EQUALS, "Y"))
    mainAndExprs.add(EntityCondition.makeCondition("glFiscalTypeId", EntityOperator.EQUALS, glFiscalTypeId))
    mainAndExprs.add(EntityCondition.makeCondition("acctgTransTypeId", EntityOperator.NOT_EQUAL, "PERIOD_CLOSING"))
    mainAndExprs.add(EntityCondition.makeCondition("transactionDate", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate))
    mainAndExprs.add(EntityCondition.makeCondition("transactionDate", EntityOperator.LESS_THAN, thruDate))

    List balanceTotalList = []

    // REVENUE
    // account balances
    accountBalanceList = []
    transactionTotals = []
    balanceTotal = BigDecimal.ZERO
    List revenueAndExprs = mainAndExprs as LinkedList
    revenueAndExprs.add(EntityCondition.makeCondition("glAccountClassId", EntityOperator.IN, revenueAccountClassIds))
    transactionTotals = select("glAccountId", "accountName", "accountCode", "debitCreditFlag", "amount").from("AcctgTransEntrySums").where(revenueAndExprs).orderBy("glAccountId").queryList()
    if (transactionTotals) {
        Map transactionTotalsMap = [:]
        balanceTotalCredit = BigDecimal.ZERO
        balanceTotalDebit = BigDecimal.ZERO
        transactionTotals.each { transactionTotal ->
            Map accountMap = (Map)transactionTotalsMap.get(transactionTotal.glAccountId)
            if (!accountMap) {
                accountMap = UtilMisc.makeMapWritable(transactionTotal)
                accountMap.remove("debitCreditFlag")
                accountMap.remove("amount")
                accountMap.put("D", BigDecimal.ZERO)
                accountMap.put("C", BigDecimal.ZERO)
                accountMap.put("balance", BigDecimal.ZERO)
            }
            UtilMisc.addToBigDecimalInMap(accountMap, transactionTotal.debitCreditFlag, transactionTotal.amount)
            if ("D".equals(transactionTotal.debitCreditFlag)) {
                balanceTotalDebit = balanceTotalDebit.add(transactionTotal.amount)
            } else {
                balanceTotalCredit = balanceTotalCredit.add(transactionTotal.amount)
            }
            BigDecimal debitAmount = (BigDecimal)accountMap.get("D")
            BigDecimal creditAmount = (BigDecimal)accountMap.get("C")
            // revenues are accounts of class CREDIT: the balance is given by credits minus debits
            BigDecimal balance = creditAmount.subtract(debitAmount)
            accountMap.put("balance", balance)
            transactionTotalsMap.put(transactionTotal.glAccountId, accountMap)
        }
        accountBalanceList = UtilMisc.sortMaps(transactionTotalsMap.values().asList(), UtilMisc.toList("accountCode"))
        // revenues are accounts of class CREDIT: the balance is given by credits minus debits
        balanceTotal = balanceTotalCredit.subtract(balanceTotalDebit)
    }
    context.revenueAccountBalanceList = accountBalanceList
    context.revenueAccountBalanceList.add(UtilMisc.toMap("accountName", "TOTAL REVENUES", "balance", balanceTotal))
    context.revenueBalanceTotalPlan = balanceTotal

    if (i == 0){
        listOfRevenuePlan =  context.revenueBalanceTotalPlan
    }
    else{
        listOfRevenuePlan = listOfRevenuePlan +","+  context.revenueBalanceTotalPlan
    }
} // end of for 
println  listOfRevenuePlan

context.listOfRevenuePlan = listOfRevenuePlan

println "===================" 

