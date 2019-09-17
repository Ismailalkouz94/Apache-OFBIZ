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
    <fo:block font-weight="bold" font-size="12" >     ${uiLabelMap.AccountingIncomeStatement}</fo:block>
 <fo:table table-layout="fixed" padding-top="10" border-top="2.5pt solid" border-width=".1mm" border-color="#696666" width="20.5cm" margin-top="10px" margin-bottom="10px">
            <fo:table-column/>
            <fo:table-column/>
            <fo:table-column/>
  
            <fo:table-header font-weight="bold" font-size="9" background-color="#e2e1de" padding="30px" border-bottom="2.5pt solid" border-width=".1mm" border-color="#696666" margin-top="30px" margin-bottom="30px">
                <fo:table-cell >
                    <fo:block text-align="center">fromDate</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="center">thruDate</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="center">glFiscalTypeId</fo:block>
                </fo:table-cell>
            </fo:table-header>
            
            <fo:table-body>
                 <#list revenueAccountBalanceList as revenueAccountBalance>
                            <fo:table-row padding-top="3" border-bottom="2pt solid" border-top="2pt solid" border-width=".1mm" border-color="#94908F"  height="9">
                              <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(fromDate)!}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(thruDate)!}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(glFiscalTypeId)!}</fo:block>
                                </fo:table-cell>
                         </fo:table-row>
                </#list>
            </fo:table-body>
        </fo:table>
     <fo:block font-weight="bold" font-size="9" >    ${uiLabelMap.AccountingRevenues}</fo:block>
        <fo:table table-layout="fixed" padding-top="10" border-top="2.5pt solid" border-width=".1mm" border-color="#696666" width="20.5cm" margin-top="10px" margin-bottom="10px">
            <fo:table-column/>
            <fo:table-column/>
            <fo:table-column/>
  
            <fo:table-header font-weight="bold" font-size="9" background-color="#e2e1de" padding="30px" border-bottom="2.5pt solid" border-width=".1mm" border-color="#696666" margin-top="30px" margin-bottom="30px">
                <fo:table-cell >
                    <fo:block text-align="center">accountCode</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="left">accountName</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="left">balance</fo:block>
                </fo:table-cell>
            </fo:table-header>
            
            <fo:table-body>
                 <#list revenueAccountBalanceList as revenueAccountBalance>
                            <!--<fo:table-row padding-top="3" border-bottom="2pt solid" border-top="2pt solid" border-width=".1mm" border-color="#94908F"  height="9">-->
                        <#if (revenueAccountBalance.accountCode)! == '' >
                       <fo:table-row background-color="#e2e1de" font-weight="bold" font-size="9" padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">
                        <#else>
                        <fo:table-row padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">
                       </#if>    
                        <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(revenueAccountBalance.accountCode)!}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="left">${(revenueAccountBalance.accountName)!}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="left">${(currencyUomId)!} ${(revenueAccountBalance.balance)!}</fo:block>
                                </fo:table-cell>
                         </fo:table-row>
                </#list>
            </fo:table-body>
        </fo:table>
 <fo:block font-weight="bold" font-size="9" >     ${uiLabelMap.AccountingExpenses}</fo:block>
<fo:table table-layout="fixed" padding-top="10" border-top="2.5pt solid" border-width=".1mm" border-color="#696666" width="20.5cm" margin-top="10px" margin-bottom="10px">
            <fo:table-column/>
            <fo:table-column/>
            <fo:table-column/>
  
            <fo:table-header font-weight="bold" font-size="9" background-color="#e2e1de" padding="30px" border-bottom="2.5pt solid" border-width=".1mm" border-color="#696666" margin-top="30px" margin-bottom="30px">
                <fo:table-cell >
                    <fo:block text-align="center">accountCode</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="left">accountName</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="left">balance</fo:block>
                </fo:table-cell>
            </fo:table-header>
            
            <fo:table-body>
                 <#list expenseAccountBalanceList as expenseAccountBalance>
                            <!--<fo:table-row padding-top="3" border-bottom="2pt solid" border-top="2pt solid" border-width=".1mm" border-color="#94908F"  height="9">-->
                     <#if (expenseAccountBalance.accountCode)! == '' >
                        <fo:table-row background-color="#e2e1de" font-weight="bold" font-size="9" padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">
                         <#else>
                         <fo:table-row padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">
                        </#if>          
                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(expenseAccountBalance.accountCode)!}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="left">${(expenseAccountBalance.accountName)!}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="left">${(currencyUomId)!} ${(expenseAccountBalance.balance)!}</fo:block>
                                </fo:table-cell>
                         </fo:table-row>
                </#list>
            </fo:table-body>
        </fo:table>
   <fo:block font-weight="bold" font-size="9" > ${uiLabelMap.AccountingIncome}</fo:block>
    <fo:table table-layout="fixed" padding-top="10" border-top="2.5pt solid" border-width=".1mm" border-color="#696666" width="20.5cm" margin-top="10px" margin-bottom="10px">
            <fo:table-column/>
            <fo:table-column/>
            <fo:table-column/>
  
            <fo:table-header font-weight="bold" font-size="9" background-color="#e2e1de" padding="30px" border-bottom="2.5pt solid" border-width=".1mm" border-color="#696666" margin-top="30px" margin-bottom="30px">
                <fo:table-cell >
                    <fo:block text-align="center">accountCode</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="left">accountName</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="left">balance</fo:block>
                </fo:table-cell>
            </fo:table-header>
            
            <fo:table-body>
                 <#list incomeAccountBalanceList as incomeAccountBalance>
                            <!--<fo:table-row padding-top="3" border-bottom="2pt solid" border-top="2pt solid" border-width=".1mm" border-color="#94908F"  height="9">-->
                    <#if (incomeAccountBalance.accountCode)! == '' >
                        <fo:table-row background-color="#e2e1de" font-weight="bold" font-size="9" padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">
                         <#else>
                         <fo:table-row padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">
                        </#if>              
                     <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(incomeAccountBalance.accountCode)!}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="left">${(incomeAccountBalance.accountName)!}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="left">${(currencyUomId)!} ${(incomeAccountBalance.balance)!}</fo:block>
                                </fo:table-cell>
                         </fo:table-row>
                </#list>
            </fo:table-body>
        </fo:table>
    <fo:block font-weight="bold" font-size="9" >   ${uiLabelMap.CommonTotal}</fo:block>
       <fo:table table-layout="fixed" padding-top="10" border-top="2.5pt solid" border-width=".1mm" border-color="#696666" width="20.5cm" margin-top="10px" margin-bottom="10px">
            <fo:table-column/>
            <fo:table-column/>
      
  
            <fo:table-header font-weight="bold" font-size="9" background-color="#e2e1de" padding="30px" border-bottom="2.5pt solid" border-width=".1mm" border-color="#696666" margin-top="30px" margin-bottom="30px">
                <fo:table-cell padding-left="3">
                    <fo:block text-align="left" >totalName</fo:block>
                </fo:table-cell>
                <fo:table-cell >
                    <fo:block text-align="left">balance</fo:block>
                </fo:table-cell>
            
            </fo:table-header>
            
            <fo:table-body>
                 <#list balanceTotalList as balanceTotal>
                            <fo:table-row padding-top="3" border-bottom="2pt solid" border-top="2pt solid" border-width=".1mm" border-color="#94908F"  height="9">
                              <fo:table-cell padding-left="3" padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F" >
                                    <fo:block text-align="left">${(balanceTotal.totalName)!}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="3" border-top="2pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="left">${(currencyUomId)!} ${(balanceTotal.balance)!}</fo:block>
                                </fo:table-cell>
                         </fo:table-row>
                </#list>
            </fo:table-body>
        </fo:table>
</#escape>
