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
<article class="col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
<header>
<h2>${title}</h2>
</header>
<div role="content">
<#if !mechMap.facilityContactMech?? && mechMap.contactMech??>
  <p><h3>${uiLabelMap.PartyContactInfoNotBelongToYou}.</h3></p>
<div class="inside-menu">
<ul class="nav nav-tabs1">
  &nbsp;<li><a href="<@ofbizUrl>authview/${donePage}?facilityId=${facilityId}</@ofbizUrl>" class="buttontext">${uiLabelMap.CommonGoBack}</a></li>
</ul>
</div>
<#else>
  <#if !mechMap.contactMech??>
    <#-- When creating a new contact mech, first select the type, then actually create -->
    <#if !preContactMechTypeId?has_content>
    <div class="inside-menu">
<ul class="nav nav-tabs1">
    <li>  <a href='<@ofbizUrl>authview/${donePage}?facilityId=${facilityId}</@ofbizUrl>' class='buttontext'>${uiLabelMap.CommonGoBack}</a></li>
</ul>
    </div>
    <form method="post" action='<@ofbizUrl>EditContactMech</@ofbizUrl>' name="createcontactmechform">
      <input type='hidden' name='facilityId' value='${facilityId}' />
      <input type='hidden' name='DONE_PAGE' value='${donePage!}' />
      <table width="100%"  cellspacing="0">
        <tr>
          <td><label>${uiLabelMap.PartySelectContactType}</label></td>
          <td>
            <select class="form-control" name="preContactMechTypeId" >
              <#list mechMap.contactMechTypes as contactMechType>
                <option value='${contactMechType.contactMechTypeId}'>${contactMechType.get("description",locale)}</option>
              </#list>
            </select>
              </td>
        </tr>
            <tr>
             <td>&nbsp;</td>
             <td>
            <a class="btn btn-primary1" href="javascript:document.createcontactmechform.submit()" class="buttontext">${uiLabelMap.CommonCreate}</a>
          </td>
        </tr>
      </table>
    </form>
    </#if>
  </#if>

  <#if mechMap.contactMechTypeId?has_content>
    <#if !mechMap.contactMech?has_content>
      <div class="inside-menu">
       <ul class="nav nav-tabs1">
       <li> <a href='<@ofbizUrl>authview/${donePage}?facilityId=${facilityId}</@ofbizUrl>' class='buttontext'>${uiLabelMap.CommonGoBack}</a></li>
      </ul>
      </div>
      <#if contactMechPurposeType??>
        <div><label>(${uiLabelMap.PartyMsgContactHavePurpose}</label>"${contactMechPurposeType.get("description",locale)!}")</div>
      </#if>
      <table cellspacing="0" width="100%">
        <form method="post" action='<@ofbizUrl>${mechMap.requestName}</@ofbizUrl>' name="editcontactmechform" id="editcontactmechform">
        <input class="form-control" type='hidden' name='DONE_PAGE' value='${donePage}' />
        <input class="form-control" type='hidden' name='contactMechTypeId' value='${mechMap.contactMechTypeId}' />
        <input class="form-control" type='hidden' name='facilityId' value='${facilityId}' />
        <#if preContactMechTypeId??><input type='hidden' name='preContactMechTypeId' value='${preContactMechTypeId}' /></#if>
        <#if contactMechPurposeTypeId??><input type='hidden' name='contactMechPurposeTypeId' value='${contactMechPurposeTypeId!}' /></#if>

        <#if paymentMethodId??><input class="form-control" type='hidden' name='paymentMethodId' value='${paymentMethodId}' /></#if>

        <tr>
          <td><label>${uiLabelMap.PartyContactPurposes}</label></td>
          <td>
            <select class="form-control" name='contactMechPurposeTypeId' class="required">
              <option></option>
              <#list mechMap.purposeTypes as contactMechPurposeType>
                <option value='${contactMechPurposeType.contactMechPurposeTypeId}'>${contactMechPurposeType.get("description",locale)}</option>
               </#list>
            </select>
          *</td>
        </tr>
    <#else>
      <div class="inside-menu">
       <ul class="nav nav-tabs1">
       <li>
        <a href='<@ofbizUrl>authview/${donePage}?facilityId=${facilityId}</@ofbizUrl>' class='buttontext'>${uiLabelMap.CommonGoBack}</a>
        </li>
	<li>
	<a href="<@ofbizUrl>EditContactMech?facilityId=${facilityId}</@ofbizUrl>" class="buttontext">${uiLabelMap.ProductNewContactMech}</a>
	</li>
       </ul>
       </div>
      <table cellspacing="0" width="100%">
        <#if mechMap.purposeTypes?has_content>
        <tr>
          <td><label>${uiLabelMap.PartyContactPurposes}</label></td>
          <td colspan="2">
            <table class="basic-table" cellspacing="0" width="100%">
            <#if mechMap.facilityContactMechPurposes?has_content>
              <#assign alt_row = false>
              <#list mechMap.facilityContactMechPurposes as facilityContactMechPurpose>
                <#assign contactMechPurposeType = facilityContactMechPurpose.getRelatedOne("ContactMechPurposeType", true)>
                <tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
                  <td>
                      <#if contactMechPurposeType?has_content>
                        <b>${contactMechPurposeType.get("description",locale)}</b>
                      <#else>
                        <b>${uiLabelMap.PartyMechPurposeTypeNotFound}: "${facilityContactMechPurpose.contactMechPurposeTypeId}"</b>
                      </#if>
                      (${uiLabelMap.CommonSince}: ${facilityContactMechPurpose.fromDate})
                      <#if facilityContactMechPurpose.thruDate?has_content>(${uiLabelMap.CommonExpires}: ${facilityContactMechPurpose.thruDate.toString()}</#if>
                      <a href="javascript:document.getElementById('deleteFacilityContactMechPurpose_${facilityContactMechPurpose_index}').submit();" class="buttontext">${uiLabelMap.CommonDelete}</a>
                  </td>
                </tr>
                <#-- toggle the row color -->
                <#assign alt_row = !alt_row>
                <form id="deleteFacilityContactMechPurpose_${facilityContactMechPurpose_index}" method="post" action="<@ofbizUrl>deleteFacilityContactMechPurpose</@ofbizUrl>">
                  <input class="form-control" type="hidden" name="facilityId" value="${facilityId!}" />
                  <input class="form-control" type="hidden" name="contactMechId" value="${contactMechId!}" />
                  <input class="form-control" type="hidden" name="contactMechPurposeTypeId" value="${(facilityContactMechPurpose.contactMechPurposeTypeId)!}" />
                  <input class="form-control" type="hidden" name="fromDate" value="${(facilityContactMechPurpose.fromDate)!}" />
                  <input class="form-control" type="hidden" name="DONE_PAGE" value="${donePage!}" />
                  <input class="form-control" type="hidden" name="useValues" value="true" />
                </form>
              </#list>
              </#if>
              <tr>
                <td>
                  <form method="post" action='<@ofbizUrl>createFacilityContactMechPurpose?DONE_PAGE=${donePage}&amp;useValues=true</@ofbizUrl>' name='newpurposeform'>
                  <input class="form-control" type="hidden" name='facilityId' value='${facilityId}' />
                  <input class="form-control" type="hidden" name='contactMechId' value='${contactMechId!}' />
                    <select class="form-control" class="form-control" name='contactMechPurposeTypeId'>
                      <option></option>
                      <#list mechMap.purposeTypes as contactMechPurposeType>
                        <option value='${contactMechPurposeType.contactMechPurposeTypeId}'>${contactMechPurposeType.get("description",locale)}</option>
                      </#list>
                    </select>
                    &nbsp;<a class="btn btn-primary1" href='javascript:document.newpurposeform.submit()' class='buttontext'>${uiLabelMap.PartyAddPurpose}</a>
                  </form>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        </#if>
        <form method="post" action='<@ofbizUrl>${mechMap.requestName}</@ofbizUrl>' name="editcontactmechform" id="editcontactmechform">
        <input class="form-control" type="hidden" name="contactMechId" value='${contactMechId}' />
        <input class="form-control" type="hidden" name="contactMechTypeId" value='${mechMap.contactMechTypeId}' />
        <input class="form-control" type="hidden" name='facilityId' value='${facilityId}' />
    </#if>

  <#if "POSTAL_ADDRESS" = mechMap.contactMechTypeId!>
    <tr>
      <td><label>${uiLabelMap.PartyToName}</label></td>
      <td>
        <input class="form-control" type="text" size="30" maxlength="60" name="toName" value="${(mechMap.postalAddress.toName)?default(request.getParameter('toName')!)}" />
      </td>
    </tr>
    <tr>
      <td><label>${uiLabelMap.PartyAttentionName}</label></td>
      <td>
        <input class="form-control" type="text" size="30" maxlength="60" name="attnName" value="${(mechMap.postalAddress.attnName)?default(request.getParameter('attnName')!)}" />
      </td>
    </tr>
    <tr>
      <td><label>${uiLabelMap.PartyAddressLine1}</label></td>
      <td>
        <input class="form-control" type="text" class="required" size="30" maxlength="30" name="address1" value="${(mechMap.postalAddress.address1)?default(request.getParameter('address1')!)}" />
      *</td>
    </tr>
    <tr>
      <td><label>${uiLabelMap.PartyAddressLine2}</label></td>
      <td>
          <input class="form-control" type="text" size="30" maxlength="30" name="address2" value="${(mechMap.postalAddress.address2)?default(request.getParameter('address2')!)}" />
      </td>
    </tr>
    <tr>
      <td><label>${uiLabelMap.PartyCity}</label></td>
      <td>
          <input class="form-control" type="text" class="required" size="30" maxlength="30" name="city" value="${(mechMap.postalAddress.city)?default(request.getParameter('city')!)}" />
      *</td>
    </tr>
    <tr>
      <td><label>${uiLabelMap.PartyState}</label></td>
      <td>
        <select class="form-control" name="stateProvinceGeoId" id="editcontactmechform_stateProvinceGeoId">
        </select>
      </td>
    </tr>
    <tr>
     <td><label>${uiLabelMap.PartyZipCode}</label></td>
      <td>
        <input class="form-control" type="text" class="required" size="12" maxlength="10" name="postalCode" value="${(mechMap.postalAddress.postalCode)?default(request.getParameter('postalCode')!)}" />
      *</td>
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
  <#elseif "TELECOM_NUMBER" = mechMap.contactMechTypeId!>
    <tr>
      <td><label>${uiLabelMap.PartyPhoneNumber}</label></td>
      <td  colspan="2">
        <input class="form-control" type="text" style="width:80px" size="4" maxlength="10" name="countryCode" value="${(mechMap.telecomNumber.countryCode)?default(request.getParameter('countryCode')!)}" />
        -&nbsp;<input class="form-control" style="width:80px"  type="text" size="4" maxlength="10" name="areaCode" value="${(mechMap.telecomNumber.areaCode)?default(request.getParameter('areaCode')!)}" />
        -&nbsp;<input class="form-control" style="width:80px" type="text" size="15" maxlength="15" name="contactNumber" value="${(mechMap.telecomNumber.contactNumber)?default(request.getParameter('contactNumber')!)}" />
        &nbsp;ext&nbsp;<input class="form-control"  style="width:80px" type="text" size="6" maxlength="10" name="extension" value="${(mechMap.facilityContactMech.extension)?default(request.getParameter('extension')!)}" />
        <br/><br/>[${uiLabelMap.CommonCountryCode}] [${uiLabelMap.PartyAreaCode}] [${uiLabelMap.PartyContactNumber}] [${uiLabelMap.PartyExtension}]</td>
    </tr>
  <#elseif "EMAIL_ADDRESS" = mechMap.contactMechTypeId!>
    <tr>
      <td><label>${uiLabelMap.PartyEmailAddress}</label></td>
      <td>
          <input class="form-control"  type="text" class="required" size="60" maxlength="255" name="emailAddress" value="${(mechMap.contactMech.infoString)?default(request.getParameter('emailAddress')!)}" />
      *</td>
    </tr>
  <#else>
    <tr>
      <td><label>${mechMap.contactMechType.get("description",locale)}</label></td>
      <td>
          <input class="form-control" type="text" class="required" size="60" maxlength="255" name="infoString" value="${(mechMap.contactMech.infoString)!}" />
      *</td>
    </tr>
  </#if>
    <tr>
      <td colspan="3">
        <a class="btn btn-primary1" href="javascript:document.editcontactmechform.submit()" class="buttontext">${uiLabelMap.CommonSave}</a>
      </td>
    </tr>
  </form>
  </table>
  </#if>
</#if>
</div>
</div>
</article>
</div>
</section>
