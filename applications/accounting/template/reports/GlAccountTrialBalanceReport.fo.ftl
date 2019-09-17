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
<#escape x as x?xml>
    <fo:block font-weight="bold" font-size="12" >${uiLabelMap.AccountingSubsidiaryLedger}</fo:block>
    <#if glAcctgTrialBalanceList?has_content>
        <fo:table table-layout="fixed" width="20.5cm" margin-bottom="40px">
            <fo:table-column/>
            <!--<fo:table-column column-number="2" column-width="40%"/>-->
            <fo:table-header>
                <fo:table-cell padding-top="5" border-top="3pt solid"  border-width=".1mm" border-color="#696666" font-size="13pt">
                    <fo:block text-align="start" font-style="normal"></fo:block>
                </fo:table-cell>
               
            </fo:table-header>
            <fo:table-body>
                <fo:table-row  border-bottom="3pt solid"  border-width=".1mm" border-color="#696666"  height="15" margin-top="30px">
                    <fo:table-cell >
                        <fo:block text-align="start"> ${uiLabelMap.AccountingTimePeriod} : ${(currentTimePeriod.fromDate)!} ${uiLabelMap.CommonTo} ${(currentTimePeriod.thruDate)!}</fo:block> 
                        <fo:block text-align="start" >${uiLabelMap.AccountingGlAccountNameAndGlAccountCode} : ${(glAccount.accountCode)!} - ${(glAccount.accountName)!}</fo:block>    
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
        <fo:table table-layout="fixed" padding-top="10" border-top="3pt solid" border-width=".1mm" border-color="#696666" width="20.5cm" >
            <fo:table-column/>
            <fo:table-column/>
            <fo:table-column/>
            <fo:table-column/>
            <fo:table-column/>
            <fo:table-column/>
            <fo:table-column/>
            <fo:table-column/>
            <fo:table-column/>
          <fo:table-header background-color="#e2e1de" font-weight="bold" font-size="9" padding="30px" border-bottom="2pt solid" border-top="2pt solid" border-width=".1mm" border-color="#696666" margin-top="30px" margin-bottom="30px">
                <fo:table-cell >
                    <fo:block text-align="center">${uiLabelMap.FormFieldTitle_transactionDate}</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="center">${uiLabelMap.AccountingAccountTransactionId}</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="center">${uiLabelMap.CommonDescription}</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="center">${uiLabelMap.AccountingTypeOfTheCurrency}</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="center">${uiLabelMap.AccountingOriginalCurrency}</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="center">${uiLabelMap.AccountingDebitAmount}</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="center">${uiLabelMap.AccountingCreditAmount}</fo:block>
                </fo:table-cell>
                <fo:table-cell>
                    <fo:block text-align="center">${uiLabelMap.AccountingDebitOrCreditOfBalance}</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="center">${uiLabelMap.AccountingBalanceOfTheAccount}</fo:block>
                </fo:table-cell>
            </fo:table-header>
            <fo:table-body>
                <fo:table-row padding-top="3" border-top="3pt solid" border-width=".1mm" border-color="#696666" height="9"><fo:table-cell padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block/></fo:table-cell><fo:table-cell padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block/></fo:table-cell><fo:table-cell padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="3" text-align="center" ><fo:block text-align="center">${uiLabelMap.AccountingTheBalanceOfLastYear}</fo:block></fo:table-cell><fo:table-cell padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block/></fo:table-cell><fo:table-cell padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block/></fo:table-cell><fo:table-cell padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block text-align="center"><#if (isDebitAccount)>${uiLabelMap.AccountingDebitFlag}<#else>${uiLabelMap.AccountingCreditFlag}</#if></fo:block></fo:table-cell><fo:table-cell padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block text-align="center">${(openingBalance)!}</fo:block></fo:table-cell></fo:table-row>
                <#list glAcctgTrialBalanceList as glAcctgTrialBalance>
                    <#assign acctgTransAndEntries = glAcctgTrialBalance.acctgTransAndEntries/>
                    <#if (acctgTransAndEntries)?has_content>
                        <#list acctgTransAndEntries as acctgTransAndEntry>
                            <fo:table-row padding-top="3" border-bottom="2pt solid" border-top="2pt solid" border-width=".1mm" border-color="#94908F"  height="9">
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">
                                        <#if acctgTransAndEntry.transactionDate?has_content>
                                            <#assign dateFormat = Static["java.text.DateFormat"].LONG/>
                                            <#assign transactionDate = Static["java.text.DateFormat"].getDateInstance(dateFormat, locale).format((acctgTransAndEntry.transactionDate)!)/>
                                            ${(acctgTransAndEntry.transactionDate?string["yyyy-MM-dd"])!}
                                        </#if>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(acctgTransAndEntry.acctgTransId)!}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(acctgTransAndEntry.transDescription)!}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(acctgTransAndEntry.currencyUomId)!}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(acctgTransAndEntry.origCurrencyUomId)!}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center"><#if (acctgTransAndEntry.debitCreditFlag)! == "D">${(acctgTransAndEntry.amount)!}<#else>0</#if></fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center"><#if (acctgTransAndEntry.debitCreditFlag)! == "C">${(acctgTransAndEntry.amount)!}<#else>0</#if></fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center"></fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center"></fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </#list>
                        <fo:table-row padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" background-color="#e2e1de" font-weight="bold"><fo:table-cell padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block/></fo:table-cell><fo:table-cell padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block/></fo:table-cell><fo:table-cell padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="3" text-align="center" ><fo:block text-align="center">${uiLabelMap.AccountingTotalOfTheCurrentMonth}</fo:block></fo:table-cell><fo:table-cell  padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block text-align="center">${(glAcctgTrialBalance.debitTotal)!}</fo:block></fo:table-cell><fo:table-cell padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block text-align="center">${(glAcctgTrialBalance.creditTotal)!}</fo:block></fo:table-cell><fo:table-cell padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block text-align="center"><#if (isDebitAccount)>${uiLabelMap.AccountingDebitFlag}<#else>${uiLabelMap.AccountingCreditFlag}</#if></fo:block></fo:table-cell><fo:table-cell padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block text-align="center">${(glAcctgTrialBalance.balance)!}</fo:block></fo:table-cell></fo:table-row>
                        <fo:table-row border-bottom="2pt solid" padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" background-color="#e2e1de" font-weight="bold"><fo:table-cell border-bottom="2pt solid" padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block/></fo:table-cell><fo:table-cell border-bottom="2pt solid" padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block/></fo:table-cell><fo:table-cell border-bottom="2pt solid" padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="3" text-align="center" ><fo:block text-align="center">${uiLabelMap.AccountingTotalOfYearToDate}</fo:block></fo:table-cell><fo:table-cell border-bottom="2pt solid" height="9" border-top="2pt solid" padding-top="3" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block text-align="center">${glAcctgTrialBalance.totalOfYearToDateDebit}</fo:block></fo:table-cell><fo:table-cell padding-top="3" border-bottom="2pt solid" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block text-align="center">${glAcctgTrialBalance.totalOfYearToDateCredit}</fo:block></fo:table-cell><fo:table-cell border-bottom="2pt solid" padding-top="3" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block text-align="center"><#if (isDebitAccount)>${uiLabelMap.AccountingDebitFlag}<#else>${uiLabelMap.AccountingCreditFlag}</#if></fo:block></fo:table-cell><fo:table-cell padding-top="3" border-bottom="2pt solid" height="9" border-top="2pt solid" border-width=".1mm" border-color="#94908F" number-columns-spanned="1"><fo:block text-align="center">${(glAcctgTrialBalance.balanceOfTheAcctgForYear)!}</fo:block></fo:table-cell></fo:table-row>
                    </#if>
                </#list>
            </fo:table-body>
        </fo:table>
    <#else>
        ${uiLabelMap.CommonNoRecordFound}
    </#if>
</#escape>
