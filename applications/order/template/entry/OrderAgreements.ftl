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
 <div  class="div-button"  style="margin-left:12px">
<li>
    <a class="button1 facebook" href="javascript:document.agreementForm.submit()">${uiLabelMap.CommonContinue} </a>
</li>
</div>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.OrderOrderEntryCurrencyAgreementShipDates}</h2>
</header>

<div role="content">
<form method="post" name="agreementForm" class="smart-form" action="<@ofbizUrl>setOrderCurrencyAgreementShipDates</@ofbizUrl>">
<div class="screenlet">
  <div class="screenlet-body">
    <table>

      <#if agreements??>
      <tr><td colspan="4">&nbsp;<input class="form-control" type='hidden' name='hasAgreements' value='Y'/></td></tr>
      
      <tr>
        <td nowrap="nowrap">
          <div class='tableheadtext'>
            <label>${uiLabelMap.OrderSelectAgreement}</label>
          </div>
        </td>
        <td>
          <div class='tabletext'>
            <select class="form-control"  name="agreementId">
            <option value="">${uiLabelMap.CommonNone}</option>
            <#list agreements as agreement>
            <option value='${agreement.agreementId}' >${agreement.agreementId} - ${agreement.description!}</option>
            </#list>
            </select>
          </div>
        </td>
      </tr>
      <#else>
      <tr><td colspan="4">&nbsp;<input class="form-control" type='hidden' name='hasAgreements' value='N'/></td></tr>
      </#if>
      <#if agreementRoles??>
        <tr>
          <td nowrap="nowrap">
            <div class='tableheadtext'>
              <label>${uiLabelMap.OrderSelectAgreementRoles}</label>
            </div>
          </td>
          <td>
            <div class='tabletext'>
              <select class="form-control" name="agreementId">
              <option value="">${uiLabelMap.CommonNone}</option>
              <#list agreementRoles as agreementRole>
                  <option value='${agreementRole.agreementId!}' >${agreementRole.agreementId!} - ${agreementRole.roleTypeId!}</option>
              </#list>
              </select>
            </div>
          </td>
        </tr>
      </#if>

      <#if "PURCHASE_ORDER" == cart.getOrderType()>
        <tr>
          <td class='tableheadtext'>
            <label>${uiLabelMap.OrderOrderId}</label>
          </td>
          <td>
            <input class="form-control" type='text' size='15' maxlength='100' name='orderId' value=""/>
          </td>
        </tr>
      </#if>

      <tr>
        <td class='tableheadtext' nowrap="nowrap" width="50%">
           <label>${uiLabelMap.OrderOrderName}</label>
        </td>
        <td>
          <input class="form-control" type='text' size='60' maxlength='100' name='orderName'/>
        </td>
      </tr>

      <#if cart.getOrderType() != "PURCHASE_ORDER">
      <tr>
        <td class='tableheadtext' nowrap="nowrap">
          <label>${uiLabelMap.OrderPONumber}</label>
        </td>
        <td>
          <input class="form-control" type="text" class='inputBox' name="correspondingPoId" size="15" />
        </td>
      </tr>
      </#if>

      <tr>
        <td nowrap="nowrap">
          <div class='tableheadtext'>
            <#if agreements??><label>${uiLabelMap.OrderSelectCurrencyOr}</label>
            <#else><label>${uiLabelMap.OrderSelectCurrency}</label>
            </#if>
          </div>
        </td>
        <td>
          <div class='tabletext'>
            <select class="form-control" name="currencyUomId">
              <option value=""></option>
              <#list currencies as currency>
              <option value="${currency.uomId}" <#if currencyUomId?default('') == currency.uomId>selected="selected"</#if> >${currency.uomId}</option>
              </#list>
            </select>
          </div>
        </td>
      </tr>

      <tr>
        <td>
          <label>${uiLabelMap.ProductChooseCatalog}</label>
        </td>
        <td>
           <#if catalogCol?has_content>
           <select class="form-control" name='CURRENT_CATALOG_ID'>
            <#list catalogCol! as catalogId>
              <#assign thisCatalogName = Static["org.apache.ofbiz.product.catalog.CatalogWorker"].getCatalogName(request, catalogId)>
              <option value="${catalogId}" <#if currentCatalogId?default('') == catalogId>selected="selected"</#if> >${thisCatalogName}</option>
            </#list>
          </select>
          <#else>
             <input class="form-control" type="hidden" name='CURRENT_CATALOG_ID' value=""/> 
          </#if>
        </td>
      </tr>

      <tr>
        <td>
          <label>${uiLabelMap.WorkEffortWorkEffortId}</label>
        </td>
        <td>
          <@htmlTemplate.lookupField formName="agreementForm" name="workEffortId" id="workEffortId" fieldFormName="LookupWorkEffort"/>
        </td>
      </tr>

      <tr>
        <td nowrap="nowrap">
          <div class='tableheadtext'>
            <label>${uiLabelMap.OrderShipAfterDateDefault}</label>
          </div>
        </td>
        <td>
            <@htmlTemplate.renderDateTimeField name="shipAfterDate" event="" action="" value="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" size="25" maxlength="30" id="shipAfterDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
        </td>
      </tr>

      <tr>
        <td  nowrap="nowrap">
          <div class='tableheadtext'>
            <label>${uiLabelMap.OrderShipBeforeDateDefault}</label>
          </div>
        </td>
        <td>
            <@htmlTemplate.renderDateTimeField name="shipBeforeDate" event="" action="" value="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" size="25" maxlength="30" id="shipBeforeDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
        </td>
      </tr>

      <#if cart.getOrderType() == "PURCHASE_ORDER">
        <tr>
          <td>
            <div class='tableheadtext'>
             <label> ${uiLabelMap.FormFieldTitle_cancelBackOrderDate}</label>
            </div>
          </td>
          <td>
              <@htmlTemplate.renderDateTimeField name="cancelBackOrderDate" event="" action="" value="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" size="25" maxlength="30" id="cancelBackOrderDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
          </td>
        </tr>
      </#if>

    </table>
  </div>
</div>
</form><br/>
</div>
</div>
</article>
</div>
</section>
