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

    <#-- reference number -->
    <#if txType?default("") == "PRDS_PAY_CREDIT" || txType?default("") == "PRDS_PAY_CAPTURE" || 
         txType?default("") == "PRDS_PAY_RELEASE" || txType?default("") == "PRDS_PAY_REFUND">
      ${setRequestAttribute("validTx", "true")}
      <#assign validTx = true>
      <tr><td colspan="3"><hr /></td></tr>
      <tr>
        <td><label>${uiLabelMap.AccountingReferenceNumber}<label></td>
        <td>
          <input class="form-control" type="text" size="30" maxlength="60" name="referenceNum" />
          <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
        </td>
      </tr>
      <tr>
        <td><label>${uiLabelMap.FormFieldTitle_orderPaymentPreferenceId}<label></td>
        <td>
          <input class="form-control" type="text" size="20" maxlength="20" name="orderPaymentPreferenceId" />
          <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
        </td>
      </tr>
    </#if>
    <#-- manual credit card information -->
    <#if txType?default("") == "PRDS_PAY_RELEASE">
      <tr><td>
      ${setRequestAttribute("validTx", "true")}
      <script language="JavaScript" type="text/javascript">
      <!-- //
        document.manualTxForm.action = "<@ofbizUrl>processReleaseTransaction</@ofbizUrl>";
      // -->
      </script>
      </td></tr>
    </#if>
    <#if txType?default("") == "PRDS_PAY_REFUND">
      <tr><td>
      ${setRequestAttribute("validTx", "true")}
      <script language="JavaScript" type="text/javascript">
      <!-- //
        document.manualTxForm.action = "<@ofbizUrl>processRefundTransaction</@ofbizUrl>";
      // -->
      </script>
      </td></tr>
    </#if>
    <#if txType?default("") == "PRDS_PAY_CREDIT" || txType?default("") == "PRDS_PAY_AUTH">
      <tr><td>
      ${setRequestAttribute("validTx", "true")}
      <script language="JavaScript" type="text/javascript">
      <!-- //
        document.manualTxForm.action = "<@ofbizUrl>processManualCcTx</@ofbizUrl>";
      // -->
      </script>
      </td></tr>
      <tr><td colspan="3"><hr/></td></tr>
      <tr>
        <td><label>${uiLabelMap.PartyFirstName}<label></td>
        <td>
          <input class="form-control" type="text" size="30" maxlength="60" name="firstName" value="${(person.firstName)!}" />
          <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
        </td>
      </tr>
      <tr>
        <td><label>${uiLabelMap.PartyLastName}<label></td>
        <td>
          <input class="form-control" type="text" size="30" maxlength="60" name="lastName" value="${(person.lastName)!}" />
          <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
        </td>
      </tr>
      <tr>
        <td><label>${uiLabelMap.PartyEmailAddress}<label></td>
        <td>
          <input class="form-control" type="text" size="30" maxlength="60" name="infoString" value="" />
          <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
        </td>
      </tr>
      <tr><td colspan="3"><hr/></td></tr>
      <#assign showToolTip = "true">
      ${screens.render("component://accounting/widget/CommonScreens.xml#creditCardFields")}
      <tr><td colspan="3"><hr/></td></tr>
      <#-- first / last name -->
      <tr>
        <td><label>${uiLabelMap.PartyFirstName}<label></td>
        <td>
          <input class="form-control" type="text" size="30" maxlength="30" name="firstName" value="${(person.firstName)!}" <#if requestParameters.useShipAddr??>disabled</#if> />
          <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
        </td>
      </tr>
      <tr>
        <td><label>${uiLabelMap.PartyLastName}<label></td>
        <td>
          <input class="form-control" type="text" size="30" maxlength="30" name="lastName" value="${(person.lastName)!}" <#if requestParameters.useShipAddr??>disabled</#if> />
          <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
        </td>
      </tr>
      <#-- credit card address -->
      <tr>
        <td><label>${uiLabelMap.AccountingBillToAddress1}<label></td>
        <td>
          <input class="form-control" type="text" size="30" maxlength="30" name="address1" value="${(postalFields.address1)!}" <#if requestParameters.useShipAddr??>disabled</#if> />
          <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
        </td>
      </tr>
      <tr>
        <td><label>${uiLabelMap.AccountingBillToAddress2}<label></td>
        <td>
          <input class="form-control" type="text" size="30" maxlength="30" name="address2" value="${(postalFields.address2)!}" <#if requestParameters.useShipAddr??>disabled</#if> />
        </td>
      </tr>
      <tr>
        <td><label>${uiLabelMap.CommonCity}<label></td>
        <td>
          <input class="form-control" type="text" size="30" maxlength="30" name="city" value="${(postalFields.city)!}" <#if requestParameters.useShipAddr??>disabled</#if> />
          <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
        </td>
      </tr>
      <tr>
        <td><label>${uiLabelMap.CommonStateProvince}<label></td>
        <td>
          <select class="form-control" name="stateProvinceGeoId" <#if requestParameters.useShipAddr??>disabled</#if>>
            <#if (postalFields.stateProvinceGeoId)??>
              <option>${postalFields.stateProvinceGeoId}</option>
              <option value="${postalFields.stateProvinceGeoId}">---</option>
            <#else>
              <option value="">${uiLabelMap.CommonNone} ${uiLabelMap.CommonState}</option>
            </#if>
            ${screens.render("component://common/widget/CommonScreens.xml#states")}
          </select>
        </td>
      </tr>
      <tr>
        <td><label>${uiLabelMap.CommonZipPostalCode}<label></td>
        <td>
          <input class="form-control" type="text" size="12" maxlength="10" name="postalCode" value="${(postalFields.postalCode)!}" <#if requestParameters.useShipAddr??>disabled</#if> />
          <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
        </td>
      </tr>
      <tr>
        <td><label>${uiLabelMap.CommonCountry}<label></td>
        <td>
          <select  class="form-control" name="countryGeoId" <#if requestParameters.useShipAddr??>disabled</#if>>
            <#if (postalFields.countryGeoId)??>
              <option>${postalFields.countryGeoId}</option>
              <option value="${postalFields.countryGeoId}">---</option>
            </#if>
            ${screens.render("component://common/widget/CommonScreens.xml#countries")}
          </select>
          <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
        </td>
      </tr>
    </#if>
