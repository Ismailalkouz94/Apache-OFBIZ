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

<#assign shoppingCartOrderType = "">
<#assign shoppingCartProductStore = "NA">
<#assign shoppingCartChannelType = "">
<#if shoppingCart??>
  <#assign shoppingCartOrderType = shoppingCart.getOrderType()>
  <#assign shoppingCartProductStore = shoppingCart.getProductStoreId()?default("NA")>
  <#assign shoppingCartChannelType = shoppingCart.getChannelType()?default("")>
<#else>
<#-- allow the order type to be set in parameter, so only the appropriate section (Sales or Purchase Order) shows up -->
  <#if parameters.orderTypeId?has_content>
    <#assign shoppingCartOrderType = parameters.orderTypeId>
  </#if>
</#if>
<!-- Sales Order Entry -->

 
<section id="widget-grid">
<div class="row">

<br/>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
 <div  class="div-button">
<li>
 <a  class="button1 facebook" href="javascript:document.salesentryform.submit();">${uiLabelMap.CommonContinue}</a>
</li>
<li>
 <a  class="button1 facebook" href="/partymgr/control/findparty?${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyFindParty}</a>
</li>
</div>
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.OrderSalesOrder}<#if shoppingCart??>&nbsp;${uiLabelMap.OrderInProgress}</#if></h2>
</header>
<#if security.hasEntityPermission("ORDERMGR", "_CREATE", session)>
<#if shoppingCartOrderType != "PURCHASE_ORDER">
<div role="content">
<div class="screenlet">
  <div class="screenlet-body">
      <form method="post" name="salesentryform" class="smart-form" action="<@ofbizUrl>initorderentry</@ofbizUrl>">
      <input type="hidden" name="originOrderId" value="${parameters.originOrderId!}"/>
      <input type="hidden" name="finalizeMode" value="type"/>
      <input type="hidden" name="orderMode" value="SALES_ORDER"/>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td nowrap="nowrap"><label>${uiLabelMap.ProductProductStore}</label></td>
          <td>
            <div class='tabletext'>
              <select class="form-control" name="productStoreId"<#if sessionAttributes.orderMode??> disabled</#if>>
                <#assign currentStore = shoppingCartProductStore>
                <#if defaultProductStore?has_content>
                   <option value="${defaultProductStore.productStoreId}">${defaultProductStore.storeName!}</option>
                   <option value="${defaultProductStore.productStoreId}">----</option>
                </#if>
                <#list productStores as productStore>
                  <option value="${productStore.productStoreId}"<#if productStore.productStoreId == currentStore> selected="selected"</#if>>${productStore.storeName!}</option>
                </#list>
              </select>
              <#if sessionAttributes.orderMode??>${uiLabelMap.OrderCannotBeChanged}</#if>
            </div>
          </td>
        </tr>
        <tr>
          <td nowrap="nowrap"><label>${uiLabelMap.OrderSalesChannel}</label></td>
          <td>
            <div class='tabletext'>
              <select class="form-control" name="salesChannelEnumId">
                <#assign currentChannel = shoppingCartChannelType>
                <#if defaultSalesChannel?has_content>
                   <option value="${defaultSalesChannel.enumId}">${defaultSalesChannel.description!}</option>
                   <option value="${defaultSalesChannel.enumId}"> ---- </option>
                </#if>
                <option value="">${uiLabelMap.OrderNoChannel}</option>
                <#list salesChannels as salesChannel>
                  <option value="${salesChannel.enumId}" <#if (salesChannel.enumId == currentChannel)>selected="selected"</#if>>${salesChannel.get("description",locale)}</option>
                </#list>
              </select>
            </div>
          </td>
        </tr>
        <#if partyId??>
          <#assign thisPartyId = partyId>
        <#else>
          <#assign thisPartyId = requestParameters.partyId!>
        </#if>
        <tr>
          <td nowrap="nowrap"><label>${uiLabelMap.CommonUserLoginId}</label></td>
          <td>
            <div>
              <@htmlTemplate.lookupField value="${parameters.userLogin.userLoginId}" formName="salesentryform" name="userLoginId" id="userLoginId_sales" fieldFormName="LookupUserLoginAndPartyDetails"/>
            </div>
          </td>
        </tr>
        <tr>
          <td nowrap="nowrap"><label>${uiLabelMap.OrderCustomer}</label></td>
          <td>
            <div class='tabletext'>
              <@htmlTemplate.lookupField value='${thisPartyId!}' formName="salesentryform" name="partyId" id="partyId" fieldFormName="LookupCustomerName"/>
            </div>
          </td>
        </tr>
      </table>
      </form><br/>
  </div>
</div>
</#if>
</#if>
</div>
</div>

<br />
<!-- Purchase Order Entry -->

<#if security.hasEntityPermission("ORDERMGR", "_PURCHASE_CREATE", session)>
  <#if shoppingCartOrderType != "SALES_ORDER">
<div class="div-button">
   <li><a class="button1 facebook" href="javascript:document.poentryform.submit();">${uiLabelMap.CommonContinue} </a></li>
   <li><a class="button1 facebook" href="/partymgr/control/findparty?${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyFindParty}</a></li>
</div>
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.OrderPurchaseOrder}<#if shoppingCart??>&nbsp;${uiLabelMap.OrderInProgress}</#if></h2>
</header>
<div role="content">
  <div class="screenlet">
    <div class="screenlet-body">
      <form method="post" class="smart-form" name="poentryform" action="<@ofbizUrl>initorderentry</@ofbizUrl>">
      <input type='hidden' name='finalizeMode' value='type'/>
      <input type='hidden' name='orderMode' value='PURCHASE_ORDER'/>
      <table width="100%" border='0' cellspacing='0' cellpadding='0'>
        <#if partyId??>
          <#assign thisPartyId = partyId>
        <#else>
          <#assign thisPartyId = requestParameters.partyId!>
        </#if>
        <tr>
          <td nowrap="nowrap"><label>${uiLabelMap.OrderOrderEntryInternalOrganization}</label></td>
          <td>
            <div class='tabletext'>
              <select class="form-control" name="billToCustomerPartyId"<#if sessionAttributes.orderMode?default("") == "SALES_ORDER"> disabled</#if>>
                <#list organizations as organization>
                  <#assign organizationName = Static["org.apache.ofbiz.party.party.PartyHelper"].getPartyName(organization, true)/>
                    <#if (organizationName.length() != 0)>
                      <option value="${organization.partyId}">${organization.partyId} - ${organizationName}</option>
                    </#if>
                </#list>
              </select>
            </div>
          </td>
        </tr>
        <tr>
          <td nowrap="nowrap"><label>${uiLabelMap.CommonUserLoginId}</label></td>
          <td>
            <div class='tabletext'>
              <@htmlTemplate.lookupField value='${parameters.userLogin.userLoginId}'formName="poentryform" name="userLoginId" id="userLoginId_purchase" fieldFormName="LookupUserLoginAndPartyDetails"/>
            </div>
          </td>
        </tr>
        <tr>
          <td nowrap="nowrap"><label>${uiLabelMap.PartySupplier}</label></td>
          <td>
            <div class='tabletext'>
              <select class="form-control" name="supplierPartyId"<#if sessionAttributes.orderMode?default("") == "SALES_ORDER"> disabled</#if>>
                <option value="">${uiLabelMap.OrderSelectSupplier}</option>
                <#list suppliers as supplier>
                  <option value="${supplier.partyId}"<#if supplier.partyId == thisPartyId> selected="selected"</#if>>[${supplier.partyId}] - ${Static["org.apache.ofbiz.party.party.PartyHelper"].getPartyName(supplier, true)}</option>
                </#list>
              </select>
            </div>
          </td>
        </tr>
      </table>
      </form>
    </div>
  </div>
</div>
</div>
  </#if>
</#if>
</article>
</div>
</section>
