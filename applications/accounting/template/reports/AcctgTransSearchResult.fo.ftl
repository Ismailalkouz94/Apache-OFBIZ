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
    <!--<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">-->
<!--        <fo:layout-master-set>
            <fo:simple-page-master master-name="main" page-height="8in" page-width="8.5in"
                    margin-top="0.1in" margin-bottom="1in" margin-left="0.2in" margin-right="0.5in">
                <fo:region-body margin-top="1in"/>
            </fo:simple-page-master>
        </fo:layout-master-set>-->
        <!--<fo:page-sequence master-reference="main">-->
            <!--<fo:flow flow-name="xsl-region-body" font-family="Helvetica">-->
                <!--<fo:block text-align="center"></fo:block>-->
                <#if acctgTransList?has_content>
                    <fo:block font-weight="bold" font-size="12">${uiLabelMap.AccountingAcctgTransFor}
                        <#if (organizationPartyId)??>
                            <#assign partyName = (delegator.findOne("PartyNameView", {"partyId" : organizationPartyId}, false))!>
                            <#if partyName.partyTypeId == "PERSON">
                                ${(partyName.firstName)!} ${(partyName.lastName)!}
                            <#elseif (partyName.partyTypeId)! == "PARTY_GROUP">
                                ${(partyName.groupName)!}
                            </#if>
                        </#if>
                    </fo:block>
                    <fo:block>
                        <fo:table table-layout="fixed" padding-top="10" width="20.5cm" margin-top="10px" margin-bottom="10px" >
                            <fo:table-column />
                            <fo:table-column />
                            <!--<fo:table-column />-->
                            <!--<fo:table-column />-->
                            <fo:table-column />
                            <fo:table-column />
                            <fo:table-column />
                            <fo:table-column />
                            <fo:table-column/>
                            <fo:table-column />
                            <fo:table-column />
                            <fo:table-header  background-color="#e2e1de" font-weight="bold" font-size="9px" padding="30px" border-bottom="2pt solid"  border-top="2pt solid" border-width=".1mm" border-color="#696666" margin-top="30px" margin-bottom="30px">
                                <fo:table-cell padding="3px">
                                    <fo:block text-align="center" >${uiLabelMap.FormFieldTitle_theirAcctgTransId}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding="3px">
                                    <fo:block text-align="center" >Acctg Trans Code</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding="3px">
                                    <fo:block text-align="center" >${uiLabelMap.FormFieldTitle_transactionDate}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding="3px">
                                    <fo:block text-align="center" >${uiLabelMap.FormFieldTitle_acctgTransTypeId}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding="3px">
                                    <fo:block text-align="center" >${uiLabelMap.FormFieldTitle_glFiscalTypeId}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding="3px">
                                    <fo:block text-align="center" >${uiLabelMap.FormFieldTitle_invoiceId}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding="3px">
                                    <fo:block text-align="center" >${uiLabelMap.FormFieldTitle_paymentId} - (${uiLabelMap.AccountingPaymentType})</fo:block>
                                </fo:table-cell>
<!--                                <fo:table-cell padding="3px">
                                    <fo:block text-align="center">${uiLabelMap.FormFieldTitle_workEffortId}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding="3px">
                                    <fo:block text-align="center" >${uiLabelMap.FormFieldTitle_shipmentId}</fo:block>
                                </fo:table-cell>-->
                                <fo:table-cell padding="3px">
                                    <fo:block text-align="center" >${uiLabelMap.FormFieldTitle_isPosted}</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding="3px">
                                    <fo:block text-align="center" >${uiLabelMap.FormFieldTitle_postedDate}</fo:block>
                                </fo:table-cell>
                            </fo:table-header>
                            <fo:table-body>
                                <#list acctgTransList as acctgTrans>
                                    <fo:table-row  border-bottom="1.5pt solid" font-size="7px" border-width=".1mm" border-color="#94908F"  height="15" margin-top="30px">
                                        <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                            <fo:block text-align="center" >${(acctgTrans.acctgTransId)!}</fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                            <fo:block text-align="center" >${(acctgTrans.acctgTransCode)!}</fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                            <fo:block text-align="center">
                                                <#assign dateFormat = Static["java.text.DateFormat"].LONG/>
                                                <#assign transactionDate = Static["java.text.DateFormat"].getDateInstance(dateFormat, locale).format((acctgTrans.transactionDate)!)/>
                                                ${(transactionDate)!}
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                            <fo:block text-align="center" >
                                                <#if (acctgTrans.acctgTransTypeId)??>
                                                    <#assign acctgTransType = (delegator.findOne("AcctgTransType", {"acctgTransTypeId" : (acctgTrans.acctgTransTypeId)!}, false))!/>
                                                    <#if acctgTransType?has_content>${acctgTransType.description}</#if>
                                                </#if>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                            <fo:block text-align="center" >
                                                <#if (acctgTrans.glFiscalTypeId)??>
                                                    <#assign glFiscalType = (delegator.findOne("GlFiscalType", {"glFiscalTypeId" : (acctgTrans.glFiscalTypeId)!}, false))!/>
                                                    ${(glFiscalType.description)!}
                                                </#if>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                            <fo:block text-align="center">
                                                ${(acctgTrans.invoiceId)!}
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                            <fo:block text-align="center" >
                                                <#if (acctgTrans.paymentId)??>
                                                    <#assign paymentType = (delegator.findOne("Payment", {"paymentId" : (acctgTrans.paymentId)!}, false)).getRelatedOne("PaymentType", false)/>
                                                    ${(acctgTrans.paymentId)!}<#if (paymentType?has_content)> -(${(paymentType.description)!})</#if>
                                                </#if>
                                            </fo:block>
                                        </fo:table-cell>
<!--                                        <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                            <fo:block text-align="center" >
                                                ${(acctgTrans.workEffortId)!}
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                            <fo:block text-align="center" >
                                                ${(acctgTrans.shipmentId)!}
                                            </fo:block>
                                        </fo:table-cell>-->
                                        <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                            <fo:block text-align="center" >
                                                ${(acctgTrans.isPosted)!}
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3px" border-top="1.5pt solid" border-width=".1mm" border-color="#94908F">
                                            <fo:block text-align="center" >
                                                <#if acctgTrans.postedDate?has_content>
                                                    <#assign dateFormat = Static["java.text.DateFormat"].LONG/>
                                                    <#assign postedDate = Static["java.text.DateFormat"].getDateInstance(dateFormat, locale).format((acctgTrans.postedDate)!)/>
                                                    ${(postedDate)!}
                                                </#if>
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </#list>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                <#else>
                    <fo:block text-align="center">${uiLabelMap.AccountingNoAcctgTransFound}</fo:block>
                </#if>
            <!--</fo:flow>-->
        <!--</fo:page-sequence>-->
    <!--</fo:root>-->
</#escape>
