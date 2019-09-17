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
<fo:block font-weight="bold" font-size="12" >${uiLabelMap.AccountingBalanceSheet}</fo:block>
<fo:table table-layout="fixed" padding-top="10" width="20.5cm" margin-top="10px" margin-bottom="10px" >
    <fo:table-column/>
    <fo:table-column/>

    <fo:table-header background-color="#e2e1de" font-weight="bold" font-size="9"  padding="30px" border-bottom="2pt solid"  border-top="2pt solid" border-width=".1mm" border-color="#696666" margin-top="30px" margin-bottom="30px">
        <fo:table-cell padding="3px">
            <fo:block text-align="center">Through Date</fo:block>
            </fo:table-cell>
        <fo:table-cell padding="3px">
            <fo:block text-align="center">${uiLabelMap.FormFieldTitle_glFiscalType}</fo:block>
            </fo:table-cell>
        </fo:table-header>

    <fo:table-body>
        <fo:table-row  border-bottom="1.5pt solid"  border-width=".1mm" border-color="#94908F"  height="15" margin-top="30px">
            <fo:table-cell  padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                <fo:block text-align="center">${(thruDate)!}</fo:block>
                </fo:table-cell>
            <fo:table-cell  padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                <fo:block text-align="center">${(glFiscalTypeId)!}</fo:block>
                </fo:table-cell>
            </fo:table-row>
        </fo:table-body>
    </fo:table>

<fo:block font-weight="bold" font-size="9" > ${uiLabelMap.AccountingAssets}</fo:block>
<fo:table table-layout="fixed" padding-top="10"    width="20.5cm" margin-top="10px" margin-bottom="10px" >
    <fo:table-column/>
    <fo:table-column/>
    <fo:table-column/>

    <fo:table-header background-color="#e2e1de" font-weight="bold" font-size="9" padding="30px" border-bottom="2pt solid" border-top="2pt solid" border-width=".1mm" border-color="#696666" margin-top="30px" margin-bottom="30px">
        <fo:table-cell padding="3px">
            <fo:block text-align="center">Account Code</fo:block>
            </fo:table-cell>
        <fo:table-cell padding="3px" width="300px">
            <fo:block text-align="left">Account Name</fo:block>
            </fo:table-cell>
        <fo:table-cell padding="3px">
            <fo:block text-align="left">Balance</fo:block>
            </fo:table-cell>
        </fo:table-header>

    <fo:table-body>
                 <#list assetAccountBalanceList as assetAccountBalance>
           <#if (assetAccountBalance.accountCode)! == '' >
           <fo:table-row background-color="#e2e1de" font-weight="bold" font-size="9" padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">
            <#else>
            <fo:table-row padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">
           </#if>
            <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                <fo:block text-align="center">${(assetAccountBalance.accountCode)!}</fo:block>
                </fo:table-cell>
               
            <fo:table-cell width="300px" padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                <fo:block text-align="left">${(assetAccountBalance.accountName)!}</fo:block>
                </fo:table-cell>
            <fo:table-cell  padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                <fo:block text-align="left">${(currencyUomId)!} ${(assetAccountBalance.balance?string(",##0.000"))!}</fo:block>
                </fo:table-cell>
          
            </fo:table-row>
                </#list>
        </fo:table-body>
    </fo:table>
<fo:block font-weight="bold" font-size="9" > ${uiLabelMap.AccountingLiabilities}</fo:block>
<fo:table table-layout="fixed" padding-top="10" width="20.5cm" margin-top="10px" margin-bottom="10px" >
    <fo:table-column/>
    <fo:table-column/>
    <fo:table-column/>

    <fo:table-header background-color="#e2e1de" font-weight="bold" font-size="9" padding="30px" border-bottom="2pt solid" border-top="2pt solid" border-width=".1mm" border-color="#696666" margin-top="30px" margin-bottom="30px">
        <fo:table-cell padding="3px">
            <fo:block text-align="center">Account Code</fo:block>
            </fo:table-cell>
        <fo:table-cell padding="3px" width="300px">
            <fo:block text-align="left">Account Name</fo:block>
            </fo:table-cell>
        <fo:table-cell padding="3px">
            <fo:block text-align="left">Balance</fo:block>
            </fo:table-cell>
        </fo:table-header>

    <fo:table-body>
                 <#list liabilityAccountBalanceList as liabilityAccountBalance>
        <!--<fo:table-row padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">-->
         <#if (liabilityAccountBalance.accountCode)! == '' >
           <fo:table-row background-color="#e2e1de" font-weight="bold" font-size="9" padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">
            <#else>
            <fo:table-row padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">
           </#if>
            <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                <fo:block text-align="center">${(liabilityAccountBalance.accountCode)!}</fo:block>
                </fo:table-cell>
            <fo:table-cell width="300px" padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                <fo:block text-align="left">${(liabilityAccountBalance.accountName)!}</fo:block>
                </fo:table-cell>
            <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                <fo:block text-align="left">${(currencyUomId)!} ${(liabilityAccountBalance.balance?string(",##0.000"))!}</fo:block>
                </fo:table-cell>
            </fo:table-row>
                </#list>
        </fo:table-body>
    </fo:table>
<fo:block font-weight="bold" font-size="9" > ${uiLabelMap.AccountingEquities}</fo:block>
<fo:table table-layout="fixed" padding-top="10"  width="20.5cm" margin-top="10px" margin-bottom="10px" >
    <fo:table-column/>
    <fo:table-column/>
    <fo:table-column/>

    <fo:table-header background-color="#e2e1de" font-weight="bold" font-size="9" padding="30px" border-bottom="2pt solid" border-top="2pt solid" border-width=".1mm" border-color="#696666" margin-top="30px" margin-bottom="30px">
        <fo:table-cell padding="3px">
            <fo:block text-align="center">Account Code</fo:block>
            </fo:table-cell>
        <fo:table-cell width="300px" padding="3px">
            <fo:block text-align="left">Account Name</fo:block>
            </fo:table-cell>
        <fo:table-cell padding="3px">
            <fo:block text-align="left">Balance</fo:block>
            </fo:table-cell>
        </fo:table-header>

    <fo:table-body>
                 <#list equityAccountBalanceList as equityAccountBalance>
        <!--<fo:table-row padding-top="3" border-bottom="2pt solid" border-width=".1mm" border-color="#94908F"  height="9">-->
           <#if (equityAccountBalance.accountCode)! == '' >
           <fo:table-row background-color="#e2e1de" font-weight="bold" font-size="9" padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">
            <#else>
            <fo:table-row padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">
           </#if>
             <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                <fo:block text-align="center">${(equityAccountBalance.accountCode)!}</fo:block>
                </fo:table-cell>
            <fo:table-cell width="300px" padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                <fo:block text-align="left">${(equityAccountBalance.accountName)!}</fo:block>
                </fo:table-cell>
            
            <fo:table-cell  padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                <fo:block text-align="left" >${(currencyUomId)!} ${(equityAccountBalance.balance?string(",##0.000"))!}</fo:block>
                </fo:table-cell>
         
            
            </fo:table-row>
                </#list>
        </fo:table-body>
    </fo:table>
</#escape>


 