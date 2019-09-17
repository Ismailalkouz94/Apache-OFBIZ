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
<!--  Contact  -->



<div class="row">


<article class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">

<header>
<span class="widget-icon"> <#if security.hasEntityPermission("PARTYMGR", "_CREATE", session) || userLogin.partyId == partyId>
 <a  href="<@ofbizUrl>editcontactmech?partyId=${partyId}</@ofbizUrl>" title="${uiLabelMap.CommonCreateNew}">
<i class="fa fa-lg fa-fw fa-plus-circle" style="color:#fff"></i></a><#else><i class="fa fa-sitemap"></i></#if></span>
<h2>${uiLabelMap.PartyContactInformation}</h2>
</header>
<div role="content">
  <div id="partyContactInfo" class="screenlet"> 
    <div class="screenlet-body">
      <#if contactMeches?has_content>
        <table class="basic-table" cellspacing="0">
          <tr>
            <td>${uiLabelMap.PartyContactType}</td>
            <td>${uiLabelMap.PartyContactInformation}</td>
            <td>${uiLabelMap.PartyContactSolicitingOk}</td>
            <td>&nbsp;</td>
          </tr>
          <#list contactMeches as contactMechMap>
            <#assign contactMech = contactMechMap.contactMech>
            <#assign partyContactMech = contactMechMap.partyContactMech>
            <tr><td colspan="4"><hr /></td></tr>
            <tr>
              <td><label>${contactMechMap.contactMechType.get("description",locale)}</label></td>
              <td>
                <#list contactMechMap.partyContactMechPurposes as partyContactMechPurpose>
                  <#assign contactMechPurposeType = partyContactMechPurpose.getRelatedOne("ContactMechPurposeType", true)>
                  <div>
                    <#if contactMechPurposeType?has_content>
                      <b>${contactMechPurposeType.get("description",locale)}</b>
                    <#else>
                      <b>${uiLabelMap.PartyMechPurposeTypeNotFound}: "${partyContactMechPurpose.contactMechPurposeTypeId}"</b>
                    </#if>
                    <#if partyContactMechPurpose.thruDate?has_content>
                      (${uiLabelMap.CommonExpire}: ${partyContactMechPurpose.thruDate})
                    </#if>
                  </div>
                </#list>
                <#if "POSTAL_ADDRESS" = contactMech.contactMechTypeId>
                  <#if contactMechMap.postalAddress?has_content>
                    <#assign postalAddress = contactMechMap.postalAddress>
                    ${setContextField("postalAddress", postalAddress)}
                    ${screens.render("component://party/widget/partymgr/PartyScreens.xml#postalAddressHtmlFormatter")}
                    <#if postalAddress.geoPointId?has_content>
                      <#if contactMechPurposeType?has_content>
                        <#assign popUptitle = contactMechPurposeType.get("description", locale) + uiLabelMap.CommonGeoLocation>
                      </#if>
                      <a href="javascript:popUp('<@ofbizUrl>GetPartyGeoLocation?geoPointId=${postalAddress.geoPointId}&partyId=${partyId}</@ofbizUrl>', '${popUptitle!}', '450', '550')" class="buttontext">${uiLabelMap.CommonGeoLocation}</a>
                    </#if>
                  </#if>
                <#elseif "TELECOM_NUMBER" = contactMech.contactMechTypeId>
                  <#if contactMechMap.telecomNumber?has_content>
                    <#assign telecomNumber = contactMechMap.telecomNumber>
                    <div>
                      ${telecomNumber.countryCode!}
                      <#if telecomNumber.areaCode?has_content>${telecomNumber.areaCode?default("000")}-</#if><#if telecomNumber.contactNumber?has_content>${telecomNumber.contactNumber?default("000-0000")}</#if>
                      <#if partyContactMech.extension?has_content>${uiLabelMap.PartyContactExt}&nbsp;${partyContactMech.extension}</#if>
                        <#if !telecomNumber.countryCode?has_content || telecomNumber.countryCode = "011">
                          <a target="_blank" href="${uiLabelMap.CommonLookupAnywhoLink}" class="buttontext">${uiLabelMap.CommonLookupAnywho}</a>
                          <a target="_blank" href="${uiLabelMap.CommonLookupWhitepagesTelNumberLink}" class="buttontext">${uiLabelMap.CommonLookupWhitepages}</a>
                        </#if>
                    </div>
                  </#if>
                <#elseif "EMAIL_ADDRESS" = contactMech.contactMechTypeId>
                  <div>
                    ${contactMech.infoString!}
                    <form method="post" action="<@ofbizUrl>NewDraftCommunicationEvent</@ofbizUrl>" onsubmit="javascript:submitFormDisableSubmits(this)" name="createEmail${contactMech.infoString?replace("&#64;","")?replace("&#x40;","")?replace(".","")}">
                      <#if userLogin.partyId?has_content>
                      <input class="form-control" style="height:26px; margin-top:10px; width:180px; margin-bottom:10px;" name="partyIdFrom" value="${userLogin.partyId}" type="hidden"/>
                      </#if>
                      <input class="form-control"  name="partyIdTo" value="${partyId}" type="hidden"/>
                      <input class="form-control"  name="contactMechIdTo" value="${contactMech.contactMechId}" type="hidden"/>
                      <input class="form-control"  name="my" value="My" type="hidden"/>
                      <input class="form-control"  name="statusId" value="COM_PENDING" type="hidden"/>
                      <input class="form-control"  name="communicationEventTypeId" value="EMAIL_COMMUNICATION" type="hidden"/>
                    </form><a class="btn btn-primary"  href="javascript:document.createEmail${contactMech.infoString?replace("&#64;","")?replace("&#x40;","")?replace(".","")}.submit()">${uiLabelMap.CommonSendEmail}</a>
                  </div>
                <#elseif "WEB_ADDRESS" = contactMech.contactMechTypeId>
                  <div>
                    ${contactMech.infoString!}
                    <#assign openAddress = contactMech.infoString?default("")>
                    <#if !openAddress?starts_with("http") && !openAddress?starts_with("HTTP")><#assign openAddress = "http://" + openAddress></#if>
                    <a target="_blank" href="${openAddress}" class="buttontext">${uiLabelMap.CommonOpenPageNewWindow}</a>
                  </div>
                <#else>
                  ${contactMech.infoString!}
                </#if>
                <div>(${uiLabelMap.CommonUpdated}:&nbsp;${partyContactMech.fromDate})</div>
                <#if partyContactMech.thruDate?has_content><div><b>${uiLabelMap.PartyContactEffectiveThru}:&nbsp;${partyContactMech.thruDate}</b></div></#if>
                <#-- create cust request -->
                <#if custRequestTypes??>
                  <form name="createCustRequestForm" action="<@ofbizUrl>createCustRequest</@ofbizUrl>" method="post" onsubmit="javascript:submitFormDisableSubmits(this)">
                    <input class="form-control"  type="hidden" name="partyId" value="${partyId}"/>
                    <input class="form-control"  type="hidden" name="fromPartyId" value="${partyId}"/>
                    <input class="form-control"  type="hidden" name="fulfillContactMechId" value="${contactMech.contactMechId}"/>
                    <select class="form-control" style="width:200px;height:30px" name="custRequestTypeId">
                      <#list custRequestTypes as type>
                        <option value="${type.custRequestTypeId}">${type.get("description", locale)}</option>
                      </#list>
                    </select>
                    <input class="btn btn-primary" style="width:200px;height:30px;float:right" type="submit" value="${uiLabelMap.PartyCreateNewCustRequest}"/>
                  </form>
                </#if>
              </td>
              <td valign="top"><b>(${partyContactMech.allowSolicitation!})</b></td>
              <td class="button-col">
                <#if security.hasEntityPermission("PARTYMGR", "_UPDATE", session) || userLogin.partyId == partyId>
                  <a class="btn btn-primary1" href="<@ofbizUrl>editcontactmech?partyId=${partyId}&amp;contactMechId=${contactMech.contactMechId}</@ofbizUrl>">${uiLabelMap.CommonUpdate}</a>
                </#if>
                <#if security.hasEntityPermission("PARTYMGR", "_DELETE", session) || userLogin.partyId == partyId>
                  <form name="partyDeleteContact" method="post" action="<@ofbizUrl>deleteContactMech</@ofbizUrl>" onsubmit="javascript:submitFormDisableSubmits(this)">
                    <input class="form-control"  name="partyId" value="${partyId}" type="hidden"/>
                    <input class="form-control"  name="contactMechId" value="${contactMech.contactMechId}" type="hidden"/>
                    <input  type="submit" class="btn btn-primary1" value="${uiLabelMap.CommonExpire}"/>
                  </form>
                </#if>
              </td>
            </tr>
          </#list>
        </table>
      <#else>
       <div class="alert alert-info fade in"> ${uiLabelMap.PartyNoContactInformation}</div>
      </#if>
    </div>
  </div>
</div>
</div>
</article>


<!--  .......  -->
<!--  LoyaltyPoints  -->

<article class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-3" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.PartyLoyaltyPoints}</h2>
</header>

  <#if monthsToInclude?? && totalSubRemainingAmount?? && totalOrders??>
    <div id="totalOrders" class="screenlet">

      <div class="alert alert-info fade in">
        ${uiLabelMap.PartyYouHave} ${totalSubRemainingAmount} ${uiLabelMap.PartyPointsFrom} ${totalOrders} ${uiLabelMap.PartyOrderInLast} ${monthsToInclude} ${uiLabelMap.CommonMonths}.
      </div>
    </div>
  </#if>
</div>
</article>
</div>
<!--  .............  -->
<!--  PaymentMethods  -->
<#macro maskSensitiveNumber cardNumber>
  <#assign cardNumberDisplay = "">
  <#if cardNumber?has_content>
    <#assign size = cardNumber?length - 4>
    <#if (size > 0)>
      <#list 0 .. size-1 as foo>
        <#assign cardNumberDisplay = cardNumberDisplay + "*">
      </#list>
      <#assign cardNumberDisplay = cardNumberDisplay + cardNumber[size .. size + 3]>
    <#else>
      <#-- but if the card number has less than four digits (ie, it was entered incorrectly), display it in full -->
      <#assign cardNumberDisplay = cardNumber>
    </#if>
  </#if>
  ${cardNumberDisplay!}
</#macro>
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-4" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.AccountingPaymentMethod}</h2>
<h2 style="float: right;margin-right: 15px;">       
<#if security.hasEntityPermission("PAY_INFO", "_CREATE", session) || security.hasEntityPermission("ACCOUNTING", "_CREATE", session)>
      <a  style="color:#fff" href="<@ofbizUrl>editeftaccount?partyId=${partyId}</@ofbizUrl>">( ${uiLabelMap.AccountingCreateEftAccount} /</a>
      <a  style="color:#fff" href="<@ofbizUrl>editgiftcard?partyId=${partyId}</@ofbizUrl>"> ${uiLabelMap.AccountingCreateGiftCard} /</a>
      <a  style="color:#fff" href="<@ofbizUrl>editcreditcard?partyId=${partyId}</@ofbizUrl>"> ${uiLabelMap.AccountingCreateCreditCard} /</a>
      <a  style="color:#fff" href="<@ofbizUrl>EditBillingAccount?partyId=${partyId}</@ofbizUrl>"> ${uiLabelMap.AccountingCreateBillingAccount} /</a>
      <a  style="color:#fff" href="<@ofbizUrl>AddCheckAccount?partyId=${partyId}</@ofbizUrl>"> ${uiLabelMap.AccountingAddCheckAccount} )</a>
 </#if>
</h2>
</header>
  <div id="partyPaymentMethod" class="screenlet">
    <div class="screenlet-body">
      <#if paymentMethodValueMaps?has_content || billingAccounts?has_content>
        <table class="basic-table" cellspacing="0" class="table table-bordered table-striped">
        <#if paymentMethodValueMaps?has_content>
           <tr><td>&nbsp;</td></tr>
          <#list paymentMethodValueMaps as paymentMethodValueMap>
            <#assign paymentMethod = paymentMethodValueMap.paymentMethod/>
            <tr>
              <#if "CREDIT_CARD" == paymentMethod.paymentMethodTypeId && paymentMethodValueMap.creditCard?has_content>
                <#assign creditCard = paymentMethodValueMap.creditCard/>
                <td><label>
                  ${uiLabelMap.AccountingCreditCard}
                 </label>
                </td>
                <td>
                  <#if creditCard.companyNameOnCard?has_content>${creditCard.companyNameOnCard}&nbsp;</#if>
                  <#if creditCard.titleOnCard?has_content>${creditCard.titleOnCard}&nbsp;</#if>
                  ${creditCard.firstNameOnCard}&nbsp;
                  <#if creditCard.middleNameOnCard?has_content>${creditCard.middleNameOnCard}&nbsp;</#if>
                  ${creditCard.lastNameOnCard}
                  <#if creditCard.suffixOnCard?has_content>&nbsp;${creditCard.suffixOnCard}</#if>
                  &nbsp;-&nbsp;
                  <#if security.hasEntityPermission("PAY_INFO", "_VIEW", session) || security.hasEntityPermission("ACCOUNTING", "_VIEW", session)>
                    ${creditCard.cardType}
                    <@maskSensitiveNumber cardNumber=creditCard.cardNumber!/>
                    ${creditCard.expireDate}
                  <#else>
                    ${Static["org.apache.ofbiz.party.contact.ContactHelper"].formatCreditCard(creditCard)}
                  </#if>
                  <#if paymentMethod.description?has_content>(${paymentMethod.description})</#if>
                  <#if paymentMethod.glAccountId?has_content>(for GL Account ${paymentMethod.glAccountId})</#if>
                  <#if paymentMethod.fromDate?has_content>(${uiLabelMap.CommonUpdated}:&nbsp;${paymentMethod.fromDate!})</#if>
                  <#if paymentMethod.thruDate?has_content><b>(${uiLabelMap.PartyContactEffectiveThru}:&nbsp;${paymentMethod.thruDate})</#if>
                </td>
                <td class="button-col" style="display:flex;">
                  <#if security.hasEntityPermission("MANUAL", "_PAYMENT", session)>
                    <a class="btn btn-primary" href="/accounting/control/manualETx?paymentMethodId=${paymentMethod.paymentMethodId}${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyManualTx}</a>
                  </#if>
                  <#if security.hasEntityPermission("PAY_INFO", "_UPDATE", session) || security.hasEntityPermission("ACCOUNTING", "_UPDATE", session)>
                    <a class="btn btn-primary" href="<@ofbizUrl>editcreditcard?partyId=${partyId}&amp;paymentMethodId=${paymentMethod.paymentMethodId}</@ofbizUrl>">${uiLabelMap.CommonUpdate}</a>
                  </#if>
              <#elseif "GIFT_CARD" == paymentMethod.paymentMethodTypeId>
                <#assign giftCard = paymentMethodValueMap.giftCard>
                <td valign="top">
                  <label>${uiLabelMap.AccountingGiftCard}</label>
                </td>
                <td>
                  <#if security.hasEntityPermission("PAY_INFO", "_VIEW", session) || security.hasEntityPermission("ACCOUNTING", "_VIEW", session)>
                    ${giftCard.cardNumber?default("N/A")} [${giftCard.pinNumber?default("N/A")}]
                  <#else>
                    <@maskSensitiveNumber cardNumber=giftCard.cardNumber!/>
                    <#if !cardNumberDisplay?has_content>N/A</#if>
                  </#if>
                  <#if paymentMethod.description?has_content>(${paymentMethod.description})</#if>
                  <#if paymentMethod.glAccountId?has_content>(for GL Account ${paymentMethod.glAccountId})</#if>
                  <#if paymentMethod.fromDate?has_content>(${uiLabelMap.CommonUpdated}:&nbsp;${paymentMethod.fromDate!})</#if>
                  <#if paymentMethod.thruDate?has_content><b>(${uiLabelMap.PartyContactEffectiveThru}:&nbsp;${paymentMethod.thruDate.toString()}</b></#if>
                </td>
                <td class="button-col">
                  <#if security.hasEntityPermission("PAY_INFO", "_UPDATE", session) || security.hasEntityPermission("ACCOUNTING", "_UPDATE", session)>
                    <a href="<@ofbizUrl>editgiftcard?partyId=${partyId}&amp;paymentMethodId=${paymentMethod.paymentMethodId}</@ofbizUrl>">${uiLabelMap.CommonUpdate}</a>
                  </#if>
              <#elseif "EFT_ACCOUNT" == paymentMethod.paymentMethodTypeId>
                <#assign eftAccount = paymentMethodValueMap.eftAccount>
                <td valign="top">
                   <label> ${uiLabelMap.PartyEftAccount}<label>
                </td>
                <td>
                  ${eftAccount.nameOnAccount} - <#if eftAccount.bankName?has_content>${uiLabelMap.PartyBank}: ${eftAccount.bankName}</#if> <#if eftAccount.accountNumber?has_content>${uiLabelMap.PartyAccount} #: ${eftAccount.accountNumber}</#if>                  <#if paymentMethod.description?has_content>(${paymentMethod.description})</#if>
                  <#if paymentMethod.glAccountId?has_content>(for GL Account ${paymentMethod.glAccountId})</#if>
                  <#if paymentMethod.fromDate?has_content>(${uiLabelMap.CommonUpdated}:&nbsp;${paymentMethod.fromDate!})</#if>
                  <#if paymentMethod.thruDate?has_content><b>(${uiLabelMap.PartyContactEffectiveThru}:&nbsp;${paymentMethod.thruDate.toString()}</#if>
                </td>
                <td class="button-col">
                  <#if security.hasEntityPermission("PAY_INFO", "_UPDATE", session) || security.hasEntityPermission("ACCOUNTING", "_UPDATE", session)>
                    <a href="<@ofbizUrl>editeftaccount?partyId=${partyId}&amp;paymentMethodId=${paymentMethod.paymentMethodId}</@ofbizUrl>">${uiLabelMap.CommonUpdate}</a>
                  </#if>
              <#elseif "COMPANY_CHECK" == paymentMethod.paymentMethodTypeId>
                <#if paymentMethodValueMap.companyCheckAccount?has_content>
                  <#assign checkAccount = paymentMethodValueMap.companyCheckAccount>
                </#if>
                <#if checkAccount?has_content>
                  <td valign="top">
                    <#-- TODO: Convert hard-coded text to UI label properties -->
                   <label> Company Check </label>
                  </td>
                  <td>
                    ${checkAccount.nameOnAccount} - <#if checkAccount.bankName?has_content>${uiLabelMap.PartyBank}: ${checkAccount.bankName}</#if>
                    <#if checkAccount.accountNumber?has_content>${uiLabelMap.PartyAccount} #: ${checkAccount.accountNumber}</#if>
                    <#if paymentMethod.description?has_content>(${paymentMethod.description})</#if>
                    <#if paymentMethod.glAccountId?has_content>(for GL Account ${paymentMethod.glAccountId})</#if>
                    <#if paymentMethod.fromDate?has_content>(${uiLabelMap.CommonUpdated}:&nbsp;${paymentMethod.fromDate!})</#if>
                    <#if paymentMethod.thruDate?has_content>(${uiLabelMap.PartyContactEffectiveThru}:&nbsp;${paymentMethod.thruDate.toString()}</#if>
                  </td>
                  <td class="button-col">
                  <#if security.hasEntityPermission("PAY_INFO", "_UPDATE", session) || security.hasEntityPermission("ACCOUNTING", "_UPDATE", session)>
                      <a href="<@ofbizUrl>AddCheckAccount?partyId=${partyId}&amp;paymentMethodId=${paymentMethod.paymentMethodId}</@ofbizUrl>">${uiLabelMap.CommonUpdate}</a>
                  </#if>
                </#if>
              <#elseif "PERSONAL_CHECK" == paymentMethod.paymentMethodTypeId>
                <#if paymentMethodValueMap.personalCheckAccount?has_content>
                  <#assign checkAccount = paymentMethodValueMap.personalCheckAccount>
                </#if>
                <#if checkAccount?has_content>
                  <td valign="top">
                   <label> Personal Check </label>
                  </td>
                  <td>
                    ${checkAccount.nameOnAccount} - <#if checkAccount.bankName?has_content>${uiLabelMap.PartyBank}: ${checkAccount.bankName}</#if>
                    <#if checkAccount.accountNumber?has_content>${uiLabelMap.PartyAccount} #: ${checkAccount.accountNumber}</#if>
                    <#if paymentMethod.description?has_content>(${paymentMethod.description})</#if>
                    <#if paymentMethod.glAccountId?has_content>(for GL Account ${paymentMethod.glAccountId})</#if>
                    <#if paymentMethod.fromDate?has_content>(${uiLabelMap.CommonUpdated}:&nbsp;${paymentMethod.fromDate!})</#if>
                    <#if paymentMethod.thruDate?has_content>(${uiLabelMap.PartyContactEffectiveThru}:&nbsp;${paymentMethod.thruDate.toString()}</#if>
                  </td>
                  <td class="button-col">
                  <#if security.hasEntityPermission("PAY_INFO", "_UPDATE", session) || security.hasEntityPermission("ACCOUNTING", "_UPDATE", session)>
                      <a href="<@ofbizUrl>AddCheckAccount?partyId=${partyId}&amp;paymentMethodId=${paymentMethod.paymentMethodId}</@ofbizUrl>">${uiLabelMap.CommonUpdate}</a>
                  </#if>
                </#if>
              <#elseif "CERTIFIED_CHECK" == paymentMethod.paymentMethodTypeId>
                <#if paymentMethodValueMap.certifiedCheckAccount?has_content>
                  <#assign checkAccount = paymentMethodValueMap.certifiedCheckAccount>
                </#if>
                <#if checkAccount?has_content>
                  <td valign="top">
                 <label> Certified Check</label>
                  </td>
                  <td>
                    ${checkAccount.nameOnAccount} - <#if checkAccount.bankName?has_content>${uiLabelMap.PartyBank}: ${checkAccount.bankName}</#if>
                    <#if checkAccount.accountNumber?has_content>${uiLabelMap.PartyAccount} #: ${checkAccount.accountNumber}</#if>
                    <#if paymentMethod.description?has_content>(${paymentMethod.description})</#if>
                    <#if paymentMethod.glAccountId?has_content>(for GL Account ${paymentMethod.glAccountId})</#if>
                    <#if paymentMethod.fromDate?has_content>(${uiLabelMap.CommonUpdated}:&nbsp;${paymentMethod.fromDate!})</#if>
                    <#if paymentMethod.thruDate?has_content>(${uiLabelMap.PartyContactEffectiveThru}:&nbsp;${paymentMethod.thruDate.toString()}</#if>
                  </td>
                  <td class="button-col">
                  <#if security.hasEntityPermission("PAY_INFO", "_UPDATE", session) || security.hasEntityPermission("ACCOUNTING", "_UPDATE", session)>
                      <a class="btn btn-primary" href="<@ofbizUrl>AddCheckAccount?partyId=${partyId}&amp;paymentMethodId=${paymentMethod.paymentMethodId}</@ofbizUrl>">${uiLabelMap.CommonUpdate}</a>
                  </#if>
                </#if>
              <#else>
                <td class="button-col">
                  &nbsp;
              </#if>
              <#if security.hasEntityPermission("PAY_INFO", "_DELETE", session) || security.hasEntityPermission("ACCOUNTING", "_DELETE", session)>
                <a class="btn btn-primary" style="margin-right:5px" href="<@ofbizUrl>deletePaymentMethod/viewprofile?partyId=${partyId}&amp;paymentMethodId=${paymentMethod.paymentMethodId}</@ofbizUrl>">${uiLabelMap.CommonExpire}</a>
              <#else>
                &nbsp;
              </#if>
              </td> <#-- closes out orphaned <td> elements inside conditionals -->
            </tr>
          </#list>
        </#if>
        <#-- Billing list-->
        <#if billingAccounts?has_content>
            <#list billingAccounts as billing>
            <tr>
              <td><label>${uiLabelMap.AccountingBilling}</label></td>
              <td>
                  <#if billing.billingAccountId?has_content>${billing.billingAccountId}</#if>
                  <#if billing.description?has_content>(${billing.description})</#if>
                  <#if billing.accountLimit?has_content>(${uiLabelMap.AccountingAccountLimit} $${billing.accountLimit})</#if>
                  <#if billing.accountBalance?has_content>(${uiLabelMap.AccountingBillingAvailableBalance} $${billing.accountBalance})</#if>
                  <#if billing.fromDate?has_content>(${uiLabelMap.CommonUpdated}:&nbsp;${billing.fromDate!})</#if>
                  <#if billing.thruDate?has_content><b>(${uiLabelMap.PartyContactEffectiveThru}:&nbsp;${billing.thruDate.toString()}</b></#if>
              </td>
              <td class="button-col">
                <a  class="btn btn-primary" href="<@ofbizUrl>EditBillingAccount?billingAccountId=${billing.billingAccountId}&amp;partyId=${partyId}</@ofbizUrl>">${uiLabelMap.CommonUpdate}</a>
                <a class="btn btn-primary" href="<@ofbizUrl>deleteBillingAccount?partyId=${partyId}&amp;billingAccountId=${billing.billingAccountId}</@ofbizUrl>">${uiLabelMap.CommonExpire}</a>
              </td>
          </tr>
          </#list>
        </#if>
        </table>
      <#else>
       <div class="alert alert-info fade in">${uiLabelMap.PartyNoPaymentMethodInformation}</div>
      </#if>
    </div>
  </div>
</div>
</article>
</div>
<!--  ..............  -->
<!--  Identification Numbers  -->
<div class="row">
 <article class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-5" data-widget-editbutton="false">

<header>
<span class="widget-icon">
 <a  href="<@ofbizUrl>viewidentifications?partyId=${partyId}</@ofbizUrl>" title="${uiLabelMap.CommonNew}">
<i class="fa fa-lg fa-fw fa-plus-circle" style="color:#fff"></i>
</a>
</span>
<h2>${uiLabelMap.PartyPartyIdentifications}</h2>
</header>
  <div id="PartyIdentificationPanel" class="screenlet" navigation-menu-name="NewPartyIdentification">
    <div class="screenlet-body">
        <table class="table table-bordered table-striped" cellspacing="0">
          <tr class="header-row">
            <td style="display:none;"> </td>
            <td style="display:none;"> </td>
            <td>Identification Type</td>
            <td>Id Value</td>
            <td>${uiLabelMap.CommonDelete}</td>
            <td>${uiLabelMap.CommonUpdate}</td>
          </tr>
          <#assign alt_row = false>
          <#list partyIdentificationAndParty as identificationObj>
               <tr<#if alt_row> class="alternate-row"</#if>>
               <form method="post"  name="ListPartyIdentification" action="<@ofbizUrl>updatePartyIdentification?partyId=${identificationObj.partyId!}&partyIdentificationTypeId=${identificationObj.partyIdentificationTypeId!}</@ofbizUrl>" class="basic-form">
                <td style="display:none;">${identificationObj.partyIdentificationTypeId!}</td>
                <td style="display:none;">${identificationObj.partyId!}</td>
                <td>${identificationObj.partyIdentTypeDesc!}</td>
                <td>${identificationObj.idValue!}</td>
                <td class="button-col"><a href="<@ofbizUrl>deletePartyIdentification?partyId=${identificationObj.partyId!}&partyIdentificationTypeId=${identificationObj.partyIdentificationTypeId!}</@ofbizUrl>">${uiLabelMap.CommonDelete}</a></td>
                <td class="button-col"> <input class="btn btn-primary" type="submit" value="${uiLabelMap.CommonUpdate}"></td>
               </form>
                <#assign alt_row = !alt_row>   
              </tr>
          </#list>
        </table>
    </div>
  </div>
</div>
</article>

<!--  ......................  -->
<!--  UserLogin  -->

<article class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-6" data-widget-editbutton="false">

<header>
<span class="widget-icon">
<#if security.hasEntityPermission("PARTYMGR", "_CREATE", session)>
 <a  href="<@ofbizUrl>ProfileCreateNewLogin?partyId=${party.partyId}</@ofbizUrl>" title="${uiLabelMap.CommonCreateNew} ">
<i class="fa fa-lg fa-fw fa-plus-circle" style="color:#fff"></i>
</a>
<#else>
<i class="fa fa-sitemap"></i>
 </#if>
</span>
<h2>${uiLabelMap.PartyUserName}</h2>
</header>
  <div id="partyUserLogins" class="screenlet">

    <div class="screenlet-body">
      <#if userLogins?has_content>
        <table class="basic-table" cellspacing="0">
          <#list userLogins as userUserLogin>
            <tr>
              <td><label>${uiLabelMap.PartyUserLogin}</label></td>
              <td>${userUserLogin.userLoginId}</td>
              <td>
                <#assign enabled = uiLabelMap.PartyEnabled>
                <#if (userUserLogin.enabled)?default("Y") == "N">
                  <#if userUserLogin.disabledDateTime??>
                    <#assign disabledTime = userUserLogin.disabledDateTime.toString()>
                  <#else>
                    <#assign disabledTime = "??">
                  </#if>
                  <#assign enabled = uiLabelMap.PartyDisabled + " - " + disabledTime>
                </#if>
                ${enabled}
              </td>
              <td class="button-col">
                <#if security.hasEntityPermission("PARTYMGR", "_CREATE", session)>
                  <a class="btn btn-primary" href="<@ofbizUrl>ProfileEditUserLogin?partyId=${party.partyId}&amp;userLoginId=${userUserLogin.userLoginId}</@ofbizUrl>">${uiLabelMap.CommonEdit}</a>
                </#if>
                <#if security.hasEntityPermission("SECURITY", "_VIEW", session)>
                  <a class="btn btn-primary" href="<@ofbizUrl>ProfileEditUserLoginSecurityGroups?partyId=${party.partyId}&amp;userLoginId=${userUserLogin.userLoginId}</@ofbizUrl>">${uiLabelMap.SecurityGroups}</a>
                </#if>
              </td>
            </tr>
          </#list>
        </table>
      <#else>
        <div class="alert alert-info fade in">${uiLabelMap.PartyNoUserLogin}</div>
      </#if>
    </div>
  </div>
</div>
</article>
</div>
<!--  .........  -->
<!--  Attributes  -->
<div class="row">
<article class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-7" data-widget-editbutton="false">

<header>
<span class="widget-icon">
<#if security.hasEntityPermission("PARTYMGR", "_CREATE", session)>
 <a  href="<@ofbizUrl>editPartyAttribute?partyId=${party.partyId!}</@ofbizUrl>" title="${uiLabelMap.CommonCreateNew} ">
<i class="fa fa-lg fa-fw fa-plus-circle" style="color:#fff"></i>
</a>
<#else>
<i class="fa fa-sitemap"></i>
 </#if>
</span>
<h2>${uiLabelMap.PartyAttributes}</h2>
</header>
  <div id="partyAttributes" class="screenlet">
   
    <div class="screenlet-body">
      <#if attributes?has_content>
        <table class="basic-table hover-bar" cellspacing="0">
            <tr class="header-row">
              <td>${uiLabelMap.CommonName}</td>
              <td>${uiLabelMap.CommonValue}</td>
              <td>&nbsp;</td>
            </tr>
          <#assign alt_row = false>
          <#list attributes as attr>
            <tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
              <td>
                ${attr.attrName!}
              </td>
              <td>
                ${attr.attrValue!}
              </td>
              <td class="button-col">
                <a href="<@ofbizUrl>editPartyAttribute?partyId=${partyId!}&attrName=${attr.attrName!}</@ofbizUrl>">${uiLabelMap.CommonEdit}</a>
              </td>
            </tr>
            <#-- toggle the row color -->
            <#assign alt_row = !alt_row>
          </#list>
        </table>
      <#else>
       <div class="alert alert-info fade in"> ${uiLabelMap.PartyNoPartyAttributesFound}</div>
      </#if>
    </div>
  </div>
  </div>
</article>

<!--  ..........  -->
<!--  AvsSettings  -->

<article class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-8" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.PartyAvsOver}</h2>
</header>
  <div id="partyAVS" class="screenlet">

    <div class="screenlet-body">
      <label>${uiLabelMap.PartyAvsString}</label>  &emsp; ${(avsOverride.avsDeclineString)?default("${uiLabelMap.CommonGlobal}")}<br/>
      <#if security.hasEntityPermission("PARTYMGR", "_UPDATE", session)>
        <a class="btn btn-primary" style="width:100px;height:30px;float:right" href="<@ofbizUrl>editAvsOverride?partyId=${party.partyId}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.CommonEdit}</a>
        <#if avsOverride??>
          <a class="btn btn-primary" style="width:100px;height:30px;float:right" href="<@ofbizUrl>resetAvsOverride?partyId=${party.partyId}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.CommonReset}</a>
        </#if>
      </#if>
    </div>
  </div>
    </div>
</article>
</div>
<!--  ...........  -->  
<!--  Visits  -->
<div class="row">
<article class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-9" data-widget-editbutton="false">

<header>
<h2>${uiLabelMap.PartyVisits}</h2>
<h2 style="float: right;margin-right: 15px;"><a style="color: #fff;" href="<@ofbizUrl>findVisits?partyId=${partyId}</@ofbizUrl>">${uiLabelMap.CommonListAll}</a></h2>
</header>
  <div id="partyVisits" class="screenlet">
    <div class="screenlet-body">
      <#if visits?has_content>
       <div class="table-responsive">
        <table class="table table-bordered table-striped" cellspacing="0">
         <thead>
          <tr class="header-row">
            <td>${uiLabelMap.PartyVisitId}</td>
            <td>${uiLabelMap.PartyUserLogin}</td>
            <td>${uiLabelMap.PartyNewUser}</td>
            <td>${uiLabelMap.PartyWebApp}</td>
            <td>${uiLabelMap.PartyClientIP}</td>
            <td>${uiLabelMap.CommonFromDate}</td>
            <td>${uiLabelMap.CommonThruDate}</td>
          </tr>
         </thead>
          <#list visits as visitObj>
            <#if (visitObj_index > 4)><#break></#if>
              <tr>
                <td class="button-col">
                  <a href="<@ofbizUrl>visitdetail?visitId=${visitObj.visitId!}</@ofbizUrl>">${visitObj.visitId!}</a>
                </td>
                <td>${visitObj.userLoginId!}</td>
                <td>${visitObj.userCreated!}</td>
                <td>${visitObj.webappName!}</td>
                <td>${visitObj.clientIpAddress!}</td>
                <td>${(visitObj.fromDate.toString())!}</td>
                <td>${(visitObj.thruDate.toString())!}</td>
              </tr>
          </#list>
        </table>
       </div>
      <#else>
       <div class="alert alert-info fade in"> ${uiLabelMap.PartyNoVisitFound}</div>
      </#if>
    </div>
  </div>
</div>
</article>

<!--  ......  -->
<!--  Content  -->

<article class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-10" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.PartyContent}</h2>
</header>
<div role="content">
  <div id="partyContent" class="screenlet">

    <div class="screenlet-body">
          ${screens.render("component://party/widget/partymgr/ProfileScreens.xml#ContentList")}
      <hr />
      <label>${uiLabelMap.PartyAttachContent}111</label>
      <form id="uploadPartyContent" method="post" enctype="multipart/form-data" action="<@ofbizUrl>uploadPartyContent</@ofbizUrl>">
        <input type="hidden" name="dataCategoryId" value="PERSONAL"/>
        <input type="hidden" name="contentTypeId" value="DOCUMENT"/>
        <input type="hidden" name="statusId" value="CTNT_PUBLISHED"/>
        <input type="hidden" name="partyId" value="${partyId}" id="contentPartyId"/>
         <div>
        <input class="btn btn-default" type="file" name="uploadedFile" class="required error" size="25"/>       
        <select class="form-control" style="width:200px;margin:10px 10px 10px 0" name="partyContentTypeId" class="required error">
          <option value="">${uiLabelMap.PartySelectPurpose}</option>
          <#list partyContentTypes as partyContentType>
            <option value="${partyContentType.partyContentTypeId}">${partyContentType.get("description", locale)?default(partyContentType.partyContentTypeId)}</option>
          </#list>
        </select>
        </div>
        <label>${uiLabelMap.PartyIsPublic}</label>
        <select class="form-control" style="width:100px;"  name="isPublic">
            <option value="N">${uiLabelMap.CommonNo}</option>
            <option value="Y">${uiLabelMap.CommonYes}</option>
        </select>
        <select class="form-control" style="width:200px;" name="roleTypeId">
          <option value="">${uiLabelMap.PartySelectRole}</option>
          <#list roles as role>
            <option value="${role.roleTypeId}" <#if role.roleTypeId == "_NA_">selected="selected"</#if>>${role.get("description", locale)?default(role.roleTypeId)}</option>
          </#list>
        </select>
        <input class="btn btn-primary" style="width:100px;height:30px;float:right" type="submit" value="${uiLabelMap.CommonUpload}" />
      </form><br/><br/>
      <div id='progress_bar'><div></div></div>
    </div>
  </div>
  <script type="text/javascript">
    jQuery("#uploadPartyContent").validate({
        submitHandler: function(form) {
            <#-- call upload scripts - functions defined in PartyProfileContent.js -->
            uploadPartyContent();
            getUploadProgressStatus();
            form.submit();
        }
    });
  </script>
</div>

</article>
</div>
<!--  .......  -->
<!--  Notes  -->
<div class="row">
<article class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-11" data-widget-editbutton="false">

<header>
<span class="widget-icon">
<#if security.hasEntityPermission("PARTYMGR", "_NOTE", session)>
 <a  href="<@ofbizUrl>AddPartyNote?partyId=${partyId}</@ofbizUrl>" title="${uiLabelMap.CommonCreateNew} ">
<i class="fa fa-lg fa-fw fa-plus-circle" style="color:#fff"></i>
</a>
<#else>
<i class="fa fa-sitemap"></i>
 </#if>
</span>
<h2>${uiLabelMap.CommonNotes}</h2>
</header>
  <div id="partyNotes" class="screenlet">
    <div class="screenlet-body">
      <#if notes?has_content>
        <table width="100%" border="0" cellpadding="1">
          <#list notes as noteRef>
            <tr>
              <td>
                <div><b>${uiLabelMap.FormFieldTitle_noteName}: </b>${noteRef.noteName!}</div>
                <#if noteRef.noteParty?has_content>
                  <div><b>${uiLabelMap.CommonBy}: </b>${Static["org.apache.ofbiz.party.party.PartyHelper"].getPartyName(delegator, noteRef.noteParty, true)}</div>
                </#if>
                <div><b>${uiLabelMap.CommonAt}: </b>${noteRef.noteDateTime.toString()}</div>
              </td>
              <td>
                ${noteRef.noteInfo}
              </td>
            </tr>
            <#if noteRef_has_next>
              <tr><td colspan="2"><hr/></td></tr>
            </#if>
          </#list>
        </table>
      <#else>
        <div class="alert alert-info fade in">${uiLabelMap.PartyNoNotesForParty} </div>
      </#if>
    </div>
  </div>
</div>
</article>
<!--.........-->
<!--  Stores  -->

<article class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-12" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductStores}</h2>
</header>
<div id="partyProductStores" class="screenlet">

  <div class="screenlet-body">
    <#if productStoreRoles?has_content>
      <table class="basic-table" cellspacing="0">
        <tr class="header-row">
          <td>${uiLabelMap.ProductStoreNameId}</td>
          <td>${uiLabelMap.PartyRoleType}</td>
        </tr>
        <#list productStoreRoles as productStoreRole>
          <#assign productStore = delegator.findOne("ProductStore", {"productStoreId" : productStoreRole.productStoreId}, true) />
          <#assign roleType = delegator.findOne("RoleType", {"roleTypeId" : productStoreRole.roleTypeId}, true) />
          <tr>
            <td class="button-col">
              <a href="/catalog/control/FindProductStoreRoles?partyId=${productStoreRole.partyId}&amp;productStoreId=${productStore.productStoreId}">${productStore.storeName?default("${uiLabelMap.ProductNoDescription}")} (${productStore.productStoreId})</a>
            </td>
            <td>${roleType.description!}</td>
          </tr>
        </#list>
      </table>
    <#else>
      <div class="alert alert-info fade in">${uiLabelMap.PartyNoProductStoreFoundForThisParty}</div>
    </#if>
  </div>
</div>
</div>
</article>
</div>
