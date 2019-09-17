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
import com.ibm.icu.util.Calendar
import org.apache.ofbiz.accounting.util.UtilAccounting
import org.apache.ofbiz.base.util.UtilDateTime
import org.apache.ofbiz.base.util.UtilNumber
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.party.party.PartyHelper
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.entity.condition.EntityCondition
import org.apache.ofbiz.entity.condition.EntityOperator
import org.apache.ofbiz.base.util.UtilMisc

import java.sql.Timestamp

println "party id"
//println parameters.partyId
if (!fromDate) {
    return
}
if (!thruDate) {
    thruDate = UtilDateTime.nowTimestamp()
}
// new 
accountMap = UtilMisc.toMap("glAccountId", "", "accountCode", "", "accountName", "", "balance", BigDecimal.ZERO, "openingD", BigDecimal.ZERO, "openingC", BigDecimal.ZERO, "D", BigDecimal.ZERO, "C", BigDecimal.ZERO)
Map lastClosedTimePeriodResult = runService('findLastClosedDate', ["organizationPartyId": parameters.get('ApplicationDecorator|organizationPartyId'), "findDate": fromDate,"userLogin": userLogin])
Timestamp lastClosedDate = (Timestamp)lastClosedTimePeriodResult.lastClosedDate
GenericValue lastClosedTimePeriod = null
if (lastClosedDate) {
    lastClosedTimePeriod = (GenericValue)lastClosedTimePeriodResult.lastClosedTimePeriod
}
if (lastClosedTimePeriod) {
    lastTimePeriodHistory = from("GlAccountAndHistory").where("organizationPartyId", parameters.get('ApplicationDecorator|organizationPartyId'), "glAccountId", parameters.glAccountId, "customTimePeriodId", lastClosedTimePeriod.customTimePeriodId).queryFirst()
    if (lastTimePeriodHistory) {
                accountMap = UtilMisc.toMap("glAccountId", lastTimePeriodHistory.glAccountId, "accountCode", lastTimePeriodHistory.accountCode, "accountName", lastTimePeriodHistory.accountName, "balance", lastTimePeriodHistory.getBigDecimal("endingBalance"), "openingD", lastTimePeriodHistory.getBigDecimal("postedDebits"), "openingC", lastTimePeriodHistory.getBigDecimal("postedCredits"), "D", BigDecimal.ZERO, "C", BigDecimal.ZERO)
    }
}

// old |
//if (parameters.get('ApplicationDecorator|organizationPartyId')) {
//        onlyIncludePeriodTypeIdList = []
//        onlyIncludePeriodTypeIdList.add("FISCAL_YEAR")
//        customTimePeriodResults = runService('findCustomTimePeriods', [findDate : UtilDateTime.nowTimestamp(), organizationPartyId : parameters.get('ApplicationDecorator|organizationPartyId'), onlyIncludePeriodTypeIdList : onlyIncludePeriodTypeIdList, userLogin : userLogin])
//        customTimePeriodList = customTimePeriodResults.customTimePeriodList
//        if (UtilValidate.isNotEmpty(customTimePeriodList)) {
//            context.timePeriod = customTimePeriodList.first().customTimePeriodId
//        }
//  println context.timePeriod     
  
decimals = UtilNumber.getBigDecimalScale("ledger.decimals")
rounding = UtilNumber.getBigDecimalRoundingMode("ledger.rounding")
context.currentOrganization = from("PartyNameView").where("partyId", parameters.get('ApplicationDecorator|organizationPartyId')).queryOne()
if (parameters.glAccountId) {
    glAccount = from("GlAccount").where("glAccountId", parameters.glAccountId).queryOne()
    isDebitAccount = UtilAccounting.isDebitAccount(glAccount)
    context.isDebitAccount = isDebitAccount
    context.glAccount = glAccount
}

//    currentTimePeriod = null
BigDecimal balanceOfTheAcctgForYear = BigDecimal.ZERO

//    if (parameters.timePeriod) {
//        currentTimePeriod = from("CustomTimePeriod").where("customTimePeriodId", parameters.timePeriod).queryOne()
//        previousTimePeriodResult = runService('getPreviousTimePeriod', [customTimePeriodId : parameters.timePeriod, userLogin : userLogin])
//        previousTimePeriod = previousTimePeriodResult.previousTimePeriod
//        if (UtilValidate.isNotEmpty(previousTimePeriod)) {
//            glAccountHistory = from("GlAccountHistory").where("customTimePeriodId", previousTimePeriod.customTimePeriodId, "glAccountId", parameters.glAccountId, "organizationPartyId", parameters.get('ApplicationDecorator|organizationPartyId')).queryOne()
//            if (glAccountHistory && glAccountHistory.endingBalance != null) {
//                context.openingBalance = glAccountHistory.endingBalance
//                balanceOfTheAcctgForYear = glAccountHistory.endingBalance
//            } else {
//                context.openingBalance = BigDecimal.ZERO
//            }
//        }
//    }

//    if (currentTimePeriod) {
//        context.currentTimePeriod = currentTimePeriod
//        customTimePeriodStartDate = UtilDateTime.getMonthStart(UtilDateTime.toTimestamp(currentTimePeriod.fromDate), timeZone, locale)
//        customTimePeriodEndDate = UtilDateTime.getMonthEnd(UtilDateTime.toTimestamp(currentTimePeriod.fromDate), timeZone, locale)

//        Calendar calendarTimePeriodStartDate = UtilDateTime.toCalendar(customTimePeriodStartDate)
glAcctgTrialBalanceList = []
BigDecimal totalOfYearToDateDebit = BigDecimal.ZERO
BigDecimal totalOfYearToDateCredit = BigDecimal.ZERO
isPosted = parameters.isPosted

//       =================================== 
BigDecimal debitTotal = BigDecimal.ZERO
BigDecimal creditTotal = BigDecimal.ZERO
BigDecimal balanceTotal = BigDecimal.ZERO
     
parties.each { party ->  
    totalOfYearToDateDebit = BigDecimal.ZERO
    totalOfYearToDateCredit = BigDecimal.ZERO
    balanceOfTheAcctgForYear = BigDecimal.ZERO
    openingBalanceOfTheAcctg = BigDecimal.ZERO
    if ("ALL".equals(isPosted)) {
        isPosted = ""
    }
    
    
    acctgTransEntriesAndTransTotal = runService('getAcctgTransEntriesAndTransTotal', 
        [customTimePeriodStartDate : fromDate, customTimePeriodEndDate : thruDate, organizationPartyId : parameters.get('ApplicationDecorator|organizationPartyId'), glAccountId : parameters.glAccountId, isPosted : isPosted, userLogin : userLogin ,partyId : party.partyId])
    totalOfYearToDateDebit = totalOfYearToDateDebit + acctgTransEntriesAndTransTotal.debitTotal
    acctgTransEntriesAndTransTotal.totalOfYearToDateDebit = totalOfYearToDateDebit.setScale(decimals, rounding)
    debitTotal = debitTotal + acctgTransEntriesAndTransTotal.debitTotal
            
            
    totalOfYearToDateCredit = totalOfYearToDateCredit + acctgTransEntriesAndTransTotal.creditTotal
    acctgTransEntriesAndTransTotal.totalOfYearToDateCredit = totalOfYearToDateCredit.setScale(decimals, rounding)
    creditTotal = creditTotal + acctgTransEntriesAndTransTotal.creditTotal 

    if (isDebitAccount) {
        acctgTransEntriesAndTransTotal.balance = acctgTransEntriesAndTransTotal.debitCreditDifference
    } else {
        acctgTransEntriesAndTransTotal.balance = -1 * acctgTransEntriesAndTransTotal.debitCreditDifference
    }
    balanceOfTheAcctgForYear = balanceOfTheAcctgForYear + acctgTransEntriesAndTransTotal.balance
    acctgTransEntriesAndTransTotal.balanceOfTheAcctgForYear = balanceOfTheAcctgForYear.setScale(decimals, rounding)
    balanceTotal =  balanceTotal + acctgTransEntriesAndTransTotal.balance
            
    
    // opening  Balance 
    List mainAndExprs = []
    mainAndExprs.add(EntityCondition.makeCondition("organizationPartyId", EntityOperator.EQUALS, parameters.get('ApplicationDecorator|organizationPartyId')))
    mainAndExprs.add(EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, party.partyId))
    mainAndExprs.add(EntityCondition.makeCondition("isPosted", EntityOperator.EQUALS, "Y"))
    mainAndExprs.add(EntityCondition.makeCondition("glAccountId", EntityOperator.EQUALS, parameters.glAccountId))
    mainAndExprs.add(EntityCondition.makeCondition("glFiscalTypeId", EntityOperator.EQUALS, "ACTUAL"))
    mainAndExprs.add(EntityCondition.makeCondition("acctgTransTypeId", EntityOperator.NOT_EQUAL, "PERIOD_CLOSING"))
    mainAndExprs.add(EntityCondition.makeCondition("transactionDate", EntityOperator.GREATER_THAN_EQUAL_TO, lastClosedDate))
    mainAndExprs.add(EntityCondition.makeCondition("transactionDate", EntityOperator.LESS_THAN, fromDate))
    transactionTotals = select("glAccountId", "accountName", "accountCode", "debitCreditFlag", "amount").from("AcctgTransEntrySums2").where(mainAndExprs).orderBy("glAccountId").queryList()
    transactionTotals.each { transactionTotal ->
        UtilMisc.addToBigDecimalInMap(accountMap, "opening" + transactionTotal.debitCreditFlag, transactionTotal.amount)
        if ("D".equals(transactionTotal.debitCreditFlag)) {
            //            println  " *D* transactionTotal.amount "+transactionTotal.getBigDecimal("amount")+" "+party.partyId
            acctgTransEntriesAndTransTotal.openingBalance = transactionTotal.getBigDecimal("amount")
        } else {
            //            println  " *C* transactionTotal.amount "+transactionTotal.getBigDecimal("amount")+" "+party.partyId
            acctgTransEntriesAndTransTotal.openingBalance = -1 * transactionTotal.getBigDecimal("amount")
        }
        openingBalanceOfTheAcctg = openingBalanceOfTheAcctg + acctgTransEntriesAndTransTotal.openingBalance
        
    }
   
    acctgTransEntriesAndTransTotal.openingBalanceOfTheAcctg = openingBalanceOfTheAcctg.setScale(decimals, rounding)
    
 
    acctgTransEntriesAndTransTotal.partyId = party.partyId
    partyName = PartyHelper.getPartyName(party)
    acctgTransEntriesAndTransTotal.partyName = partyName
    glAcctgTrialBalanceList.add(acctgTransEntriesAndTransTotal)

                
  
}
 println "**accountMap**"+accountMap
context.glAcctgTrialBalanceList = glAcctgTrialBalanceList
context.debitTotal = debitTotal
context.creditTotal = creditTotal
context.balanceTotal = balanceTotal
//    }
//}
