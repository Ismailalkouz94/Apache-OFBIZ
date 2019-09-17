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
 <fo:table table-layout="fixed" padding-top="10" border-top="2pt solid" border-width=".1mm" border-color="#696666" width="100%" margin-top="10px" margin-bottom="10px" >
            <fo:table-column column-width="25mm"/>
            <fo:table-column/>
            <fo:table-column/>
            <fo:table-column/>
            <fo:table-column column-width="25mm"/>
            <fo:table-column column-width="25mm"/>
       

            <fo:table-header font-weight="bold" font-size="9" background-color="#e2e1de" padding="30px" border-bottom="2pt solid" border-width=".1mm" border-color="#696666" margin-top="30px" margin-bottom="30px">
                <fo:table-cell padding="3px">
                    <fo:block text-align="center">Account Code</fo:block>
                </fo:table-cell>
                <fo:table-cell padding="3px">
                    <fo:block text-align="left">Account Name</fo:block>
                </fo:table-cell>
                <fo:table-cell padding="3px">
                    <fo:block text-align="left">Party Id</fo:block>
                </fo:table-cell>
                  <fo:table-cell padding="3px">
                    <fo:block text-align="left">Description</fo:block>
                </fo:table-cell>
                <fo:table-cell padding="3px">
                    <fo:block text-align="center">Debit Amount</fo:block>
                </fo:table-cell>creditAmount
                  <fo:table-cell padding="3px">
                    <fo:block text-align="center">Credit Amount</fo:block>
                </fo:table-cell>  
            </fo:table-header>
            
            <fo:table-body>
                 <#list acctgTransEntries as acctgTransEntriesList>
                
                          <fo:table-row padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">
                            <#list glAccount as glAccountList>
                               <#if (glAccountList.glAccountId)! == (acctgTransEntriesList.glAccountId)! >       
                                  <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(glAccountList.accountCode)!}</fo:block>
                                  </fo:table-cell>
                                  <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="left">${(glAccountList.accountName)!}</fo:block>
                                  </fo:table-cell>
                                 </#if>      
                            </#list>
                              <#list partyNameView as partyNameViewList>
                                <#if (partyNameViewList.partyId)! == (acctgTransEntriesList.partyId)! >       
                              <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="left">
                                    ${(partyNameViewList.firstName)!}${(partyNameViewList.middleName)!}${(partyNameViewList.lastName)!}${(partyNameViewList.groupName)!} [ ${(acctgTransEntriesList.partyId)!} ]</fo:block>
                              </fo:table-cell>
                              </#if>     
                            </#list>
                            <#if (acctgTransEntriesList.description.equals('SSE'))! >       
                               <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                   <fo:block text-align="left">${uiLabelMap.SSE}</fo:block>
                               </fo:table-cell>
                            <#elseif (acctgTransEntriesList.description.equals('Net_Salary'))!>
                              <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                   <fo:block text-align="left">${uiLabelMap.NetSalary}</fo:block>
                               </fo:table-cell>
                            <#else>
                               <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                   <fo:block text-align="left">${acctgTransEntriesList.description}</fo:block>
                               </fo:table-cell>
                            </#if>
                                <#if (acctgTransEntriesList.debitCreditFlag.equals('D'))! >       
                                   <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                      <fo:block text-align="center">${(currencyUomId)!} ${(acctgTransEntriesList.amount?string(",##0.000"))!}</fo:block>
                                    </fo:table-cell>
                               <#else>
                                   <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(currencyUomId)!} 0 </fo:block>
                                   </fo:table-cell>
                                </#if>
                               <#if (acctgTransEntriesList.debitCreditFlag.equals('C'))! >       
                                <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(currencyUomId)!} ${(acctgTransEntriesList.amount?string(",##0.000"))!}</fo:block>
                               </fo:table-cell>
                                <#else>
                                 <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(currencyUomId)!} 0 </fo:block>
                               </fo:table-cell>
                              </#if>
                         </fo:table-row>
                </#list>
                 <fo:table-row padding-top="3" border-bottom="2pt solid"  border-width=".1mm" border-color="#94908F"  height="9">
                      <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center"> </fo:block>
                               </fo:table-cell>
                      <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="left">Total</fo:block>
                               </fo:table-cell>
                      <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center"></fo:block>
                               </fo:table-cell>
                      <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center"> </fo:block>
                               </fo:table-cell>
                      <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(currencyUomId)!} ${(debitsTotal?string(",##0.000"))!}  </fo:block>
                               </fo:table-cell>
                      <fo:table-cell   padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                    <fo:block text-align="center">${(currencyUomId)!} ${(creditsTotal?string(",##0.000"))!} </fo:block>
                               </fo:table-cell>
                     </fo:table-row>
                     
            </fo:table-body>
        </fo:table>
</#escape>
