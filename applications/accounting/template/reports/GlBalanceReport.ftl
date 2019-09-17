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
                    <li><a href="<@ofbizUrl>GlBalanceReportPdf.pdf?fromDate=${parameters.fromDate}&amp;thruDate=${parameters.thruDate}&amp;isPosted=${parameters.isPosted}&amp;glAccountId=${parameters.glAccountId}</@ofbizUrl>" target="_BLANK" class="btn btn-primary1">${uiLabelMap.AccountingInvoicePDF}</a></li>
                </ul>
                 </div>
                <div class="alert alert-info fade in">
                <p>${uiLabelMap.AccountingSubsidiaryLedger}</p>
                <p>${uiLabelMap.FormFieldTitle_companyName} : ${(currentOrganization.groupName)!}</p>
                <p>${uiLabelMap.CommonFromDate} : ${(fromDate)!} ${uiLabelMap.CommonTo} ${uiLabelMap.CommonThruDate} : ${(thruDate)!}</p>
                <!--<p>${uiLabelMap.AccountingTimePeriod} : <#if currentTimePeriod?has_content>${(currentTimePeriod.fromDate)!} ${uiLabelMap.CommonTo} ${(currentTimePeriod.thruDate)!}</#if></p>-->
                <p>${uiLabelMap.AccountingGlAccountNameAndGlAccountCode} : ${(glAccount.accountCode)!} - ${(glAccount.accountName)!}</p>
                </div>
                  <div>
                    <table class="table table-bordered table-striped" >
                      <thead>
                        <tr>
                            <th align="center" class="myStyle">${uiLabelMap.PartyId}</th>
                            <th ALIGN="left" >${uiLabelMap.PartyName}</th>
                            <th align="right" class="myStyle">${uiLabelMap.AccountingOpeningBalance}</th>
                            <th align="right" class="myStyle">${uiLabelMap.AccountingDebitAmount}</th>
                            <th align="center" class="myStyle">${uiLabelMap.AccountingCreditAmount}</th>
                            <th align="center" class="myStyle">${uiLabelMap.AccountingBalanceOfTheAccount}</th>
                        </tr>
                        </thead>
                        <#list glAcctgTrialBalanceList as glAcctgTrialBalance>
                            <#assign acctgTransAndEntries = glAcctgTrialBalance.acctgTransAndEntries/>
                            <#if acctgTransAndEntries?has_content>
                                <tr class="header-row">
                                    <td ALIGN="center" class="myStyle">${(glAcctgTrialBalance.partyId)!}</td>
                                    <td ALIGN="center" class="myStyle">${(glAcctgTrialBalance.partyName)!}</td>
                                    <td ALIGN="center" class="myStyle">${(glAcctgTrialBalance.openingBalanceOfTheAcctg?string(",##0.000"))!}</td>
                                    <td ALIGN="center" class="myStyle">${(glAcctgTrialBalance.totalOfYearToDateDebit?string(",##0.000"))!}</td>
                                    <td ALIGN="center" class="myStyle">${(glAcctgTrialBalance.totalOfYearToDateCredit?string(",##0.000"))!}</td>
                                    <td ALIGN="center" class="myStyle">${(glAcctgTrialBalance.balanceOfTheAcctgForYear?string(",##0.000"))!}</td>
                                </tr>
                            </#if>
                        </#list>
                            <tr class="header-row">
                              <td ALIGN="center" class="myStyle"> </td>
                              <td ALIGN="center" class="totlerow"> ${uiLabelMap.Total}</td>
                              <td ALIGN="center" class="totlerow"> </td>
                              <td ALIGN="center" class="totlerow">${(debitTotal?string(",##0.000"))!}</td>
                              <td ALIGN="center" class="totlerow">${(creditTotal?string(",##0.000"))!}</td>
                              <td ALIGN="center" class="totlerow">${(balanceTotal?string(",##0.000"))!}</td>
                          </tr>  
                    </table>
                </div>
            </form>
        </div>
    <#else>
        <div class="alert alert-info fade in">${uiLabelMap.CommonNoRecordFound}</div>
    </#if>
</div>
