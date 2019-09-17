<#--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->


<div>
    <#if glAcctgTrialBalanceList?has_content>
        <div>
            <form name="glAccountTrialBalanceReport" id="glAccountTrialBalanceReport">
                <div class="inside-menu">
                 <ul class="nav nav-tabs1">
                    <li><a href="<@ofbizUrl>GlAccountTrialBalanceReportPdf.pdf?timePeriod=${parameters.timePeriod}&amp;isPosted=${parameters.isPosted}&amp;glAccountId=${parameters.glAccountId}&amp;partyId=${parameters.partyId}</@ofbizUrl>" target="_BLANK" class="btn btn-primary1">${uiLabelMap.AccountingInvoicePDF}</a></li>
                </ul>
                 </div>
                <div class="alert alert-info fade in">
                <p>${uiLabelMap.AccountingSubsidiaryLedger}</p>
                <p>${uiLabelMap.FormFieldTitle_companyName} : ${(currentOrganization.groupName)!}</p>
                <p>${uiLabelMap.AccountingTimePeriod} : <#if currentTimePeriod?has_content>${(currentTimePeriod.fromDate)!} ${uiLabelMap.CommonTo} ${(currentTimePeriod.thruDate)!}</#if></p>
                <p>${uiLabelMap.AccountingGlAccountNameAndGlAccountCode} : ${(glAccount.accountCode)!} - ${(glAccount.accountName)!}</p>
                </div>
                  <div>
                    <table class="table table-bordered table-striped" >
                      <thead>
                        <tr >
                            <th align="left" class="myStyle">${uiLabelMap.FormFieldTitle_transactionDate}</th>
                            <th align="left" class="myStyle">${uiLabelMap.AccountingAccountTransactionId}</th>
                            <th align="left" class="myStyle">${uiLabelMap.PartyId}</th>
                            <th align="left" class="myStyle">${uiLabelMap.CommonDescription}</th>
                            <th align="left" class="myStyle">${uiLabelMap.AccountingTypeOfTheCurrency}</th>
                            <th align="left" class="myStyle">${uiLabelMap.AccountingOriginalCurrency}</th>
                            <th align="right" class="myStyle">${uiLabelMap.AccountingDebitAmount}</th>
                            <th align="right" class="myStyle">${uiLabelMap.AccountingCreditAmount}</th>
                            <th align="right" class="myStyle">${uiLabelMap.AccountingDebitOrCreditOfBalance}</th>
                            <th align="right" class="myStyle">${uiLabelMap.AccountingBalanceOfTheAccount}</th>
                        </tr>
                        </thead>
                        <tr class="header-row" >
                            <td colspan=2 class="myStyle"> </td>
                            <td colspan=3 align="center" class="myStyle">${uiLabelMap.AccountingTheBalanceOfLastYear}</td>
                            <td colspan=2 class="myStyle"></td>
                            <td ALIGN="right" class="myStyle"><#if (isDebitAccount)>${uiLabelMap.AccountingDebitFlag}<#else>${uiLabelMap.AccountingCreditFlag}</#if></td>
                            <td ALIGN="right" class="myStyle">${(openingBalance)!}</td>
                            <td ALIGN="right" class="myStyle"></td>
                        </tr>
                        <#list glAcctgTrialBalanceList as glAcctgTrialBalance>
                        
                            <#assign acctgTransAndEntries = glAcctgTrialBalance.acctgTransAndEntries/>
                            <#if acctgTransAndEntries?has_content>
                                <#list acctgTransAndEntries as acctgTransAndEntry>
                                <tr>
                                    <td ALIGN="left" class="myStyle">${(acctgTransAndEntry.transactionDate?string["yyyy-MM-dd"])!}</td>
                                    <td ALIGN="left" class="myStyle">${(acctgTransAndEntry.acctgTransId)!}</td>
                                    <td ALIGN="left" class="myStyle">${(acctgTransAndEntry.partyId)!}</td>
                                    <td ALIGN="left" class="myStyle">${(acctgTransAndEntry.transDescription)!}</td>
                                    <td ALIGN="left" class="myStyle">${(acctgTransAndEntry.currencyUomId)!}</td>
                                    <td ALIGN="left" class="myStyle">${(acctgTransAndEntry.origCurrencyUomId)!}</td>
                                    <td ALIGN="right" class="myStyle"><#if (acctgTransAndEntry.debitCreditFlag)! == "D">${(acctgTransAndEntry.amount)!}<#else>0</#if></td>
                                    <td ALIGN="right" class="myStyle"><#if (acctgTransAndEntry.debitCreditFlag)! == "C">${(acctgTransAndEntry.amount)!}<#else>0</#if></td>
                                    <td ALIGN="right" class="myStyle"></td>
                                    <td ALIGN="right" class="myStyle"></td>
                                </tr>
                                </#list>
                                <tr class="header-row">
                                    <td colspan=2 class="myStyle"></td>
                                    <td colspan=3 ALIGN="center" class="myStyle">${uiLabelMap.AccountingTotalOfTheCurrentMonth}</td>
                                    <td ALIGN="right" colspan=1 class="myStyle">${(glAcctgTrialBalance.debitTotal)!}</td>
                                    <td ALIGN="right" colspan=1 class="myStyle">${(glAcctgTrialBalance.creditTotal)!}</td>
                                    <td ALIGN="right" colspan=1 class="myStyle"><#if (isDebitAccount)>${uiLabelMap.AccountingDebitFlag}<#else>${uiLabelMap.AccountingCreditFlag}</#if></td>
                                    <td ALIGN="right" colspan=1 class="myStyle">${(glAcctgTrialBalance.balance)!}</td>
                                    <td ALIGN="right" class="myStyle"></td>
                                </tr>
                                <tr class="header-row">
                                    <td colspan=2 class="myStyle"></td>
                                    <td ALIGN="center" colspan=3 class="myStyle">${uiLabelMap.AccountingTotalOfYearToDate}</td>
                                    <td ALIGN="right" class="myStyle">${glAcctgTrialBalance.totalOfYearToDateDebit}</td>
                                    <td ALIGN="right" class="myStyle">${glAcctgTrialBalance.totalOfYearToDateCredit}</td>
                                    <td ALIGN="right" class="myStyle"><#if (isDebitAccount)>${uiLabelMap.AccountingDebitFlag}<#else>${uiLabelMap.AccountingCreditFlag}</#if></td>
                                    <td ALIGN="right" class="myStyle">${(glAcctgTrialBalance.balanceOfTheAcctgForYear)!}</td>
                                    <td ALIGN="right" class="myStyle"></td>
                                </tr>
                            </#if>
                        </#list>
                    </table>
                </div>
            </form>
        </div>
    <#else>
        <div class="alert alert-info fade in">${uiLabelMap.CommonNoRecordFound}</div>
    </#if>
</div>
