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

<!-- begin editcreditcard.ftl -->
<section id="widget-grid">
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">

<div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0"  data-widget-editbutton="false">
<header>
<h2>   
    <#if !creditCard??>
      ${uiLabelMap.AccountingAddNewCreditCard}
    <#else>
      ${uiLabelMap.AccountingEditCreditCard}
    </#if>
</h2>
</header>
<div role="content">
<div class="screenlet">

  <div class="screenlet-body">
        <div class="button-bar">
          <a class="btn btn-primary1" href="<@ofbizUrl>${donePage}?partyId=${partyId}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.CommonCancelDone}</a>
          <a class="btn btn-primary1 href="javascript:document.editcreditcardform.submit()" class="smallSubmit">${uiLabelMap.CommonSave}</a>
        </div>
    <#if !creditCard??>
      <form class="smart-form" method="post" action="<@ofbizUrl>createCreditCard?DONE_PAGE=${donePage}</@ofbizUrl>" name="editcreditcardform" style="margin: 0;">
    <#else>
      <form class="smart-form" method="post" action="<@ofbizUrl>updateCreditCard?DONE_PAGE=${donePage}</@ofbizUrl>" name="editcreditcardform" style="margin: 0;">
        <input class="form-control" type="hidden" name="paymentMethodId" value="${paymentMethodId}" />
    </#if>
        <input class="form-control" type="hidden" name="partyId" value="${partyId}"/>
        <table class="basic-table" cellspacing="0">

        ${screens.render("component://accounting/widget/CommonScreens.xml#creditCardFields")}
        <tr>
          <td><label>${uiLabelMap.AccountingBillingAddress}</label></td>
          <td>
            <table cellspacing="0">
            <#assign hasCurrent = false>
            <#if curPostalAddress?has_content>
              <#assign hasCurrent = true>
              <tr>
                <td class="button-col">
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="contactMechId" value="${curContactMechId}" checked="checked" /><span></span></label>
                </td>
                <td>
                  <p><b>${uiLabelMap.PartyUseCurrentAddress}:</b></p>
                  <#list curPartyContactMechPurposes as curPartyContactMechPurpose>
                    <#assign curContactMechPurposeType = curPartyContactMechPurpose.getRelatedOne("ContactMechPurposeType", true)>
                    <p>
                      <b>${curContactMechPurposeType.get("description",locale)!}</b>
                      <#if curPartyContactMechPurpose.thruDate??>
                        (${uiLabelMap.CommonExpire}:${curPartyContactMechPurpose.thruDate.toString()})
                      </#if>
                    </p>
                  </#list>
                  <#if curPostalAddress.toName??><p><b>${uiLabelMap.CommonTo}:</b> ${curPostalAddress.toName}</p></#if>
                  <#if curPostalAddress.attnName??><p><b>${uiLabelMap.PartyAddrAttnName}:</b> ${curPostalAddress.attnName}</p></#if>
                  <#if curPostalAddress.address1??><p>${curPostalAddress.address1}</p></#if>
                  <#if curPostalAddress.address2??><p>${curPostalAddress.address2}</p></#if>
                  <p>${curPostalAddress.city!}<#if curPostalAddress.stateProvinceGeoId?has_content>,&nbsp;${curPostalAddress.stateProvinceGeoId!}</#if>&nbsp;${curPostalAddress.postalCode!}</p>
                  <#if curPostalAddress.countryGeoId??><p>${curPostalAddress.countryGeoId}</p></#if>
                  <p>(${uiLabelMap.CommonUpdated}:&nbsp;${(curPartyContactMech.fromDate.toString())!})</p>
                  <#if curPartyContactMech.thruDate??><p><b>${uiLabelMap.CommonDelete}:&nbsp;${curPartyContactMech.thruDate.toString()}</b></p></#if>
                </td>
              </tr>
            <#else>
            </#if>
              <#list postalAddressInfos as postalAddressInfo>
                <#assign contactMech = postalAddressInfo.contactMech>
                <#assign partyContactMechPurposes = postalAddressInfo.partyContactMechPurposes>
                <#assign postalAddress = postalAddressInfo.postalAddress>
                <#assign partyContactMech = postalAddressInfo.partyContactMech>
                <tr>
                  <td class="button-col">
                    <label class="radio radio-inline"><input class="radiobox" type="radio" name="contactMechId" value="${contactMech.contactMechId}" /><span></span></label>
                  </td>
                  <td>
                    <#list partyContactMechPurposes as partyContactMechPurpose>
                      <#assign contactMechPurposeType = partyContactMechPurpose.getRelatedOne("ContactMechPurposeType", true)>
                      <p>
                        <b>${contactMechPurposeType.get("description",locale)!}</b>
                        <#if partyContactMechPurpose.thruDate??>(${uiLabelMap.CommonExpire}:${partyContactMechPurpose.thruDate})</#if>
                      </p>
                    </#list>
                    <#if postalAddress.toName??><p><b>${uiLabelMap.CommonTo}:</b> ${postalAddress.toName}</p></#if>
                    <#if postalAddress.attnName??><p><b>${uiLabelMap.PartyAddrAttnName}:</b> ${postalAddress.attnName}</p></#if>
                    <#if postalAddress.address1??><p>${postalAddress.address1}</p></#if>
                    <#if postalAddress.address2??><p>${postalAddress.address2}</p></#if>
                    <p>${postalAddress.city}<#if postalAddress.stateProvinceGeoId?has_content>,&nbsp;${postalAddress.stateProvinceGeoId}</#if>&nbsp;${postalAddress.postalCode!}</p>
                    <#if postalAddress.countryGeoId??><p>${postalAddress.countryGeoId}</p></#if>
                    <p>(${uiLabelMap.CommonUpdated}:&nbsp;${(partyContactMech.fromDate.toString())!})</p>
                    <#if partyContactMech.thruDate??><p><b>${uiLabelMap.CommonDelete}:&nbsp;${partyContactMech.thruDate.toString()}</b></p></#if>
                  </td>
                </tr>
              </#list>
              <#if !postalAddressInfos?has_content && !curContactMech??>
                  <tr><td>${uiLabelMap.PartyNoContactInformation}.</td></tr>
              </#if>
            </table>
          </td>
        </tr>
        </table>
      </form>
      <div class="button-bar">
        <a class="btn btn-primary1 href="<@ofbizUrl>${donePage}?partyId=${partyId}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.CommonCancelDone}</a>
        <a class="btn btn-primary1  href="javascript:document.editcreditcardform.submit()" class="smallSubmit">${uiLabelMap.CommonSave}</a>
      </div>
  </div>
</div>
</div>
</div>
</article>
</div>
</section>
<!-- end editcreditcard.ftl -->