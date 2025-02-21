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


<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
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

