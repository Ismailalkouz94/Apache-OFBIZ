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


<#if security.hasEntityPermission("MANUAL", "_PAYMENT", session) || security.hasEntityPermission("ACCOUNTING", "_CREATE", session)>
  ${setRequestAttribute("validTx", "false")}
  <form name="manualTxForm" method="post" action="<@ofbizUrl>manualETx</@ofbizUrl>">
    <#if requestParameters.paymentMethodId??>
      <input type="hidden" name="paymentMethodId" value="${requestParameters.paymentMethodId}" />
    </#if>

    <table width="100%">
      <tr>
        <td><label>${uiLabelMap.CommonPaymentMethodType}&emsp;  </label></td>
        
        <td width='74%'>
          <#if paymentMethodType?has_content>
            <div>${paymentMethodType.get("description",locale)}</div>
            <input class="form-control" style="width:200px;float:left;margin-right:10px;margin-top:10px" type="hidden" name="paymentMethodTypeId" value="${paymentMethodType.paymentMethodTypeId}" />
          <#else>
            <select class="form-control" name="paymentMethodTypeId">
              <option value="CREDIT_CARD">${uiLabelMap.AccountingCreditCard}</option>
            </select>
          </#if>
        </td>
      </tr>
      <tr>
        <td><label>${uiLabelMap.ProductProductStore} &emsp;</label></td>
     
        <td width='74%'>
          <#if currentStore?has_content>
            <div><#if currentStore.storeName??>${currentStore.storeName}<#else>${currentStore.productStoreId}</#if></div>
            <input class="form-control"  type="hidden" name="productStoreId" value="${currentStore.productStoreId}" />
          <#else>
            <select  class="form-control" name="productStoreId">
              <#list productStores as productStore>
                <option value="${productStore.productStoreId}"><#if productStore.storeName??>${productStore.storeName}<#else>${productStore.productStoreId}</#if></option>
              </#list>
            </select>
          </#if>
        </td>
      </tr>
      <tr>
        <td><label>${uiLabelMap.AccountingTransactionType} &emsp;</label></td>
    
        <td width='74%'>
          <#if currentTx?has_content>
            <div>${currentTx.get("description",locale)}</div>
            <input class="form-control" style="width:200px;float:left;margin-right:10px;margin-top:10px" type="hidden" name="transactionType" value="${currentTx.enumId}" />
          <#else>
            <select class="form-control" name="transactionType" onchange="javascript:document.manualTxForm.submit();">
            <#-- the select one option is so the list will fire on any seletion -->
              <option value="Select one">${uiLabelMap.CommonSelectOne}</option>
              <#list paymentSettings as setting>
                <option value="${setting.enumId}">${setting.get("description",locale)}</option>
              </#list>
            </select>
          </#if>
        </td>
      </tr>

      <#-- payment method information -->
      <#if paymentMethodType?has_content && paymentMethodTypeId == "CREDIT_CARD">
        ${screens.render("component://accounting/widget/PaymentScreens.xml#manualCCTx")}
      <#elseif paymentMethodType?has_content && paymentMethodTypeId == "GIFT_CARD">
        ${screens.render("component://accounting/widget/PaymentScreens.xml#manualGCTx")}
      </#if>

     <#if requestAttributes.validTx?default("false") == "true">
        <tr><td colspan="3"><hr/></td></tr>
        <#-- amount field -->
        <tr>
          <td><label>${uiLabelMap.CommonAmount}</label></td>     
          <td width="74%">
            <input class="form-control" type="text" size="20" maxlength="30" name="amount" />
            <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
          </td>
        </tr>
        <#-- submit button -->
        <tr>
          <td width="40%" align="right" valign="middle">&nbsp;</td>
       
          <td width="74%">
            <input class="btn btn-primary1" type="submit" value="${uiLabelMap.CommonSubmit}" />
          </td>
        </tr>
      <#elseif txType?has_content>
        <tr>
          <td colspan="3">
            <br />
            <div class="alert alert-info fade in">${uiLabelMap.AccountingTransactionTypeNotYetSupported}</div>
          </td>
        </tr>
      </#if>
    </table>
  </form>
<br/>
<#else>
  <div class="alert alert-info fade in">${uiLabelMap.AccountingPermissionError}</div>
</#if>
