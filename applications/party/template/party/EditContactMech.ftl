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

<section id="widget-grid">
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<#if !mechMap.contactMech??><#if !preContactMechTypeId?has_content>
<header>
<h2>${uiLabelMap.PartyCreateNewContact}</h2>
</header>
</#if></#if>
<#if mechMap.contactMechTypeId?has_content><#if !mechMap.contactMech?has_content>
<header>
<h2>${uiLabelMap.PartyCreateNewContact}</h2>
</header>
<#else>
<header>
<h2>${uiLabelMap.PartyEditContactInformation}</h2>
</header>
</#if></#if>


<div role="content">
<#if !mechMap.contactMech??>
  <#-- When creating a new contact mech, first select the type, then actually create -->
  <#if !preContactMechTypeId?has_content>
    <form class="smart-form" method="post" action="<@ofbizUrl>editcontactmech</@ofbizUrl>" name="createcontactmechform">
      <input class="form-control" type="hidden" name="partyId" value="${partyId}" />
      <table class="basic-table" cellspacing="0">
        <tr>
          <td><label>${uiLabelMap.PartySelectContactType}</label></td>
          <td>
            <select class="form-control" name="preContactMechTypeId">
              <#list mechMap.contactMechTypes as contactMechType>
                <option value="${contactMechType.contactMechTypeId}">${contactMechType.get("description",locale)}</option>
              </#list>
            </select>
            <a href="javascript:document.createcontactmechform.submit()" class="btn btn-primary1">${uiLabelMap.CommonCreate}</a>
          </td>
        </tr>
      </table>
    </form>
    </#if>
</#if>
<#if mechMap.contactMechTypeId?has_content>
  <#if !mechMap.contactMech?has_content>
    <div id="mech-purpose-types">
    <#if contactMechPurposeType??>
      <p>(${uiLabelMap.PartyMsgContactHavePurpose} <b>"${contactMechPurposeType.get("description",locale)!}"</b>)</p>
    </#if>
    <table class="basic-table" cellspacing="0">
      <form class="smart-form" method="post" action="<@ofbizUrl>${mechMap.requestName}</@ofbizUrl>" name="editcontactmechform" id="editcontactmechform">
        <input class="form-control" type="hidden" name="DONE_PAGE" value="${donePage}" />
        <input class="form-control" type="hidden" name="contactMechTypeId" value="${mechMap.contactMechTypeId}" />
        <input class="form-control" type="hidden" name="partyId" value="${partyId}" />
        <#if cmNewPurposeTypeId?has_content><input type="hidden" name="contactMechPurposeTypeId" value="${cmNewPurposeTypeId}" /></#if>
        <#if preContactMechTypeId??><input type="hidden" name="preContactMechTypeId" value="${preContactMechTypeId}" /></#if>
        <#if contactMechPurposeTypeId??><input type="hidden" name="contactMechPurposeTypeId" value="${contactMechPurposeTypeId!}" /></#if>
        <#if paymentMethodId?has_content><input type='hidden' name='paymentMethodId' value='${paymentMethodId}' /></#if>
  <#else>  
    <div id="mech-purpose-types">
      <table class="basic-table" cellspacing="0">
      <#if mechMap.purposeTypes?has_content>
        <tr>
          <td><label>${uiLabelMap.PartyContactPurposes}</label></td>
          <td>
            <table class="basic-table" cellspacing="0">
              <#if mechMap.partyContactMechPurposes?has_content>
                <#list mechMap.partyContactMechPurposes as partyContactMechPurpose>
                  <#assign contactMechPurposeType = partyContactMechPurpose.getRelatedOne("ContactMechPurposeType", true)>
                  <tr>
                    <td>
                      <#if contactMechPurposeType?has_content>
                        ${contactMechPurposeType.get("description",locale)}
                      <#else>
                        ${uiLabelMap.PartyPurposeTypeNotFound}: "${partyContactMechPurpose.contactMechPurposeTypeId}"
                      </#if>
                      (${uiLabelMap.CommonSince}:${partyContactMechPurpose.fromDate.toString()})
                      <#if partyContactMechPurpose.thruDate?has_content>(${uiLabelMap.CommonExpire}: ${partyContactMechPurpose.thruDate.toString()}</#if>
                    </td>
                    <td class="button-col">
                      <form class="smart-form" name="deletePartyContactMechPurpose_${partyContactMechPurpose.contactMechPurposeTypeId}" method="post" action="<@ofbizUrl>deletePartyContactMechPurpose</@ofbizUrl>" >
                         <input class="form-control" type="hidden" name="partyId" value="${partyId}" />
                         <input class="form-control" type="hidden" name="contactMechId" value="${contactMechId}" />
                         <input class="form-control" type="hidden" name="contactMechPurposeTypeId" value="${partyContactMechPurpose.contactMechPurposeTypeId}" />
                         <input class="form-control" type="hidden" name="fromDate" value="${partyContactMechPurpose.fromDate.toString()}" />
                         <input class="form-control" type="hidden" name="DONE_PAGE" value="${donePage?replace("=","%3d")}" />
                         <input class="form-control" type="hidden" name="useValues" value="true" />
                         <a href="javascript:document.deletePartyContactMechPurpose_${partyContactMechPurpose.contactMechPurposeTypeId}.submit()" class="btn btn-primary1">${uiLabelMap.CommonDelete}</a> 
                       </form>
                    </td>
                  </tr>
                </#list>
              </#if>
              <tr>
                <form class="smart-form" method="post" action="<@ofbizUrl>createPartyContactMechPurpose</@ofbizUrl>" name="newpurposeform">
                  <input class="form-control" type="hidden" name="partyId" value="${partyId}" />
                  <input class="form-control" type="hidden" name="DONE_PAGE" value="${donePage}" />
                  <input class="form-control" type="hidden" name="useValues" value="true" />
                  <input class="form-control" type="hidden" name="contactMechId" value="${contactMechId!}" />
                  <td class="button-col">
                    <select class="form-control" name="contactMechPurposeTypeId">
                      <option></option>
                      <#list mechMap.purposeTypes as contactMechPurposeType>
                        <option value="${contactMechPurposeType.contactMechPurposeTypeId}">${contactMechPurposeType.get("description",locale)}</option>
                      </#list>
                    </select>
                  </td>
                </form>
                <td><a href="javascript:document.newpurposeform.submit()" class="btn btn-primary1">${uiLabelMap.PartyAddPurpose}</a></td>
              </tr>
            </table>
          </tr>
      </#if>
      <form class="smart-form" method="post" action="<@ofbizUrl>${mechMap.requestName}</@ofbizUrl>" name="editcontactmechform" id="editcontactmechform">
        <input class="form-control" type="hidden" name="contactMechId" value="${contactMechId}" />
        <input class="form-control" type="hidden" name="contactMechTypeId" value="${mechMap.contactMechTypeId}" />
        <input class="form-control" type="hidden" name="partyId" value="${partyId}" />
        <input class="form-control" type="hidden" name="DONE_PAGE" value="${donePage!}" />
  </#if>
  <#if "POSTAL_ADDRESS" = mechMap.contactMechTypeId!>
    <tr>
      <td><label>${uiLabelMap.PartyToName}</label></td>
      <td>
        <input class="form-control" type="text" size="50" maxlength="100" name="toName" value="${(mechMap.postalAddress.toName)?default(request.getParameter('toName')!)}" />
      </td>
    </tr>
    <tr>
      <td><label>${uiLabelMap.PartyAttentionName}</label></td>
      <td>
        <input class="form-control" type="text" size="50" maxlength="100" name="attnName" value="${(mechMap.postalAddress.attnName)?default(request.getParameter('attnName')!)}" />
      </td>
    </tr>
    <tr>
      <td><label>${uiLabelMap.PartyAddressLine1} *</label></td>
      <td>
        <input class="form-control" type="text" size="100" maxlength="255" name="address1" value="${(mechMap.postalAddress.address1)?default(request.getParameter('address1')!)}" />
      </td>
    </tr>
    <tr>
      <td><label>${uiLabelMap.PartyAddressLine2}</label></td>
      <td>
        <input class="form-control" type="text" size="100" maxlength="255" name="address2" value="${(mechMap.postalAddress.address2)?default(request.getParameter('address2')!)}" />
      </td>
    </tr>
    <tr>
      <td><label>${uiLabelMap.PartyCity} *</label></td>
      <td>
        <input class="form-control" type="text" size="50" maxlength="100" name="city" value="${(mechMap.postalAddress.city)?default(request.getParameter('city')!)}" />
      </td>
    </tr>
    <tr>
      <td><label>${uiLabelMap.PartyState}</label></td>
      <td>
        <select class="form-control" name="stateProvinceGeoId" id="editcontactmechform_stateProvinceGeoId">
        </select>
      </td>
    </tr>
    <tr>
      <td><label>${uiLabelMap.PartyZipCode} *</label></td>
      <td>
        <input class="form-control" type="text" size="30" maxlength="60" name="postalCode" value="${(mechMap.postalAddress.postalCode)?default(request.getParameter('postalCode')!)}" />
      </td>
    </tr>
    <tr>   
      <td><label>${uiLabelMap.CommonCountry}</label></td>
      
      <td>     
        <select class="form-control" name="countryGeoId" id="editcontactmechform_countryGeoId">
          ${screens.render("component://common/widget/CommonScreens.xml#countries")}
          <#if (mechMap.postalAddress??) && (mechMap.postalAddress.countryGeoId??)>
            <#assign defaultCountryGeoId = mechMap.postalAddress.countryGeoId>
          <#else>
           <#assign defaultCountryGeoId = Static["org.apache.ofbiz.entity.util.EntityUtilProperties"].getPropertyValue("general", "country.geo.id.default", delegator)>
          </#if>
          <option selected="selected" value="${defaultCountryGeoId}">
            <#assign countryGeo = delegator.findOne("Geo",Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("geoId",defaultCountryGeoId), false)>
            ${countryGeo.get("geoName",locale)}
          </option>
        </select>
      </td>
    </tr>
    <#assign isUsps = Static["org.apache.ofbiz.party.contact.ContactMechWorker"].isUspsAddress(mechMap.postalAddress)>
    <tr>
      <td><label>${uiLabelMap.PartyIsUsps}</label></td>
      <td><#if isUsps>${uiLabelMap.CommonY}<#else>${uiLabelMap.CommonN}</#if>
      </td>
    </tr>

  <#elseif "TELECOM_NUMBER" = mechMap.contactMechTypeId!>
    <tr>
      <td><label>${uiLabelMap.PartyPhoneNumber}</label></td>
      <td>
        <input class="form-control" style="width:70px" type="text" size="4" maxlength="10" name="countryCode" value="${(mechMap.telecomNumber.countryCode)?default(request.getParameter('countryCode')!)}" />
        -&nbsp;<input style="width:70px" class="form-control" type="text" size="4" maxlength="10" name="areaCode" value="${(mechMap.telecomNumber.areaCode)?default(request.getParameter('areaCode')!)}" />
        -&nbsp;<input style="width:150px" class="form-control" type="text" size="15" maxlength="15" name="contactNumber" value="${(mechMap.telecomNumber.contactNumber)?default(request.getParameter('contactNumber')!)}" />
        &nbsp;${uiLabelMap.PartyContactExt}&nbsp;<input  style="width:70px" class="form-control" type="text" size="6" maxlength="10" name="extension" value="${(mechMap.partyContactMech.extension)?default(request.getParameter('extension')!)}" />
      </td>
    </tr>
    <tr>
      <td><label></label></td>
      <td>[${uiLabelMap.CommonCountryCode}] [${uiLabelMap.PartyAreaCode}] [${uiLabelMap.PartyContactNumber}] [${uiLabelMap.PartyContactExt}]</td>
    </tr>
  <#elseif "EMAIL_ADDRESS" = mechMap.contactMechTypeId!>
    <tr>
     <td><label>${mechMap.contactMechType.get("description",locale)}</label></td>
      <td>
        <input class="form-control" type="text" size="60" maxlength="255" name="emailAddress" value="${(mechMap.contactMech.infoString)?default(request.getParameter('emailAddress')!)}" />
      </td>
    </tr>
  <#else>
    <tr>
      <td><label>${mechMap.contactMechType.get("description",locale)}</label></td>
      <td>
        <input class="form-control" type="text" size="60" maxlength="255" name="infoString" value="${(mechMap.contactMech.infoString)!}" />
      </td>
    </tr>
  </#if>
  <tr>
    <td><label>${uiLabelMap.PartyContactAllowSolicitation}?</label></td>
    <td>
      <select class="form-control" name="allowSolicitation">
        <#if (((mechMap.partyContactMech.allowSolicitation)!"") == "Y")><option value="Y">${uiLabelMap.CommonY}</option></#if>
        <#if (((mechMap.partyContactMech.allowSolicitation)!"") == "N")><option value="N">${uiLabelMap.CommonN}</option></#if>
        <option></option>
        <option value="Y">${uiLabelMap.CommonY}</option>
        <option value="N">${uiLabelMap.CommonN}</option>
      </select>
    </td>
  </tr>
  </form>
  </table>
  </div>
  <div>
    <a href="<@ofbizUrl>backHome</@ofbizUrl>" class="btn btn-primary1">${uiLabelMap.CommonGoBack}</a>
    <a href="javascript:document.editcontactmechform.submit()" class="btn btn-primary1">${uiLabelMap.CommonSave}</a>
  </div>
<#else>
  <a href="<@ofbizUrl>backHome</@ofbizUrl>" class="btn btn-primary1">${uiLabelMap.CommonGoBack}</a>
</#if>
</div>
</div>
</article>
</div>
</section>