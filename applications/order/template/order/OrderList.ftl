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

<script type="text/javascript">

    jQuery(document).ready( function() {
        jQuery('#allcheck').change( function() {
            setCheckboxes();
        });

        jQuery('.statuscheck').change( function() {
            setAllCheckbox();
        });
    });

    function setCheckboxes() {
        if (jQuery('#allcheck').is(':checked')) {
            jQuery('.statuscheck').attr ('checked', true);
        } else {
            jQuery('.statuscheck').attr ('checked', false );
        }
    }
    function setAllCheckbox() {
        var allChecked = true;
        jQuery('.statuscheck').each (function () {
            if (!jQuery(this).is(':checked')) {
                allChecked = false;
            }
        });
        if (allChecked == false && jQuery('#allcheck').is(':checked')) {
            jQuery('#allcheck').attr('checked', false);
        }
        if (allChecked == true && !jQuery('#allcheck').is(':checked')) {
            jQuery('#allcheck').attr('checked', true);
        }
    }

</script>

<#macro pagination>
    <table class="basic-table" cellspacing='0'>
         <tr>
        <td>
          <#if state.hasPrevious()>
            <a href="<@ofbizUrl>orderlist?viewIndex=${state.getViewIndex() - 1}&amp;viewSize=${state.getViewSize()}&amp;filterDate=${filterDate!}</@ofbizUrl>" class="buttontext">${uiLabelMap.CommonPrevious}</a>
          </#if>
        </td>
        <td align="right">
          <#if state.hasNext()>
            <a href="<@ofbizUrl>orderlist?viewIndex=${state.getViewIndex() + 1}&amp;viewSize=${state.getViewSize()}&amp;filterDate=${filterDate!}</@ofbizUrl>" class="buttontext">${uiLabelMap.CommonNext}</a>
          </#if>
        </td>
      </tr>
    </table>
</#macro>

<#-- order list -->
<section id="widget-grid">
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.OrderLookupOrder}</h2>
</header>
<div id="orderLookup" class="screenlet">

    <div class="screenlet-body">
      <form method="post" name="findorder" action="<@ofbizUrl>orderlist</@ofbizUrl>">
        <input type="hidden" name="changeStatusAndTypeState" value="Y" />
        <table class="basic-table" width="100%">
          <tr>
            <td><label>${uiLabelMap.CommonStatus}</label></td>
            <td nowrap="nowrap">
                <div>
                    <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="viewall" value="Y" id="allcheck" <#if state.hasAllStatus()>checked="checked"</#if> /><span></span>${uiLabelMap.CommonAll}</label>
                    <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="viewcreated" value="Y" class="statuscheck" <#if state.hasStatus('viewcreated')>checked="checked"</#if> /><span></span>${uiLabelMap.CommonCreated}</label>
                    <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="viewprocessing" value="Y" class="statuscheck" <#if state.hasStatus('viewprocessing')>checked="checked"</#if> /><span></span>${uiLabelMap.CommonProcessing}</label>
                    <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="viewapproved" value="Y" class="statuscheck" <#if state.hasStatus('viewapproved')>checked="checked"</#if> /><span></span>${uiLabelMap.CommonApproved}</label>
                    <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="viewhold" value="Y" class="statuscheck" <#if state.hasStatus('viewhold')>checked="checked"</#if> /><span></span>${uiLabelMap.CommonHeld}</label>
                    <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="viewcompleted" value="Y" class="statuscheck" <#if state.hasStatus('viewcompleted')>checked="checked"</#if> /><span></span>${uiLabelMap.CommonCompleted}</label>
                    <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="viewrejected" value="Y" class="statuscheck" <#if state.hasStatus('viewrejected')>checked="checked"</#if> /><span></span>${uiLabelMap.CommonRejected}</label>
                    <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="viewcancelled" value="Y" class="statuscheck" <#if state.hasStatus('viewcancelled')>checked="checked"</#if> /><span></span>${uiLabelMap.CommonCancelled}</label>
                </div>
            </td>
          </tr>
          <tr>
            <td><label>${uiLabelMap.CommonType}</label></td>
            <td nowrap="nowrap">
                <div>
                    <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="view_SALES_ORDER" value="Y" <#if state.hasType('view_SALES_ORDER')>checked="checked"</#if>/>
                    <span></span>${descr_SALES_ORDER}</label>
                    <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="view_PURCHASE_ORDER" value="Y" <#if state.hasType('view_PURCHASE_ORDER')>checked="checked"</#if>/>
                    <span></span>${descr_PURCHASE_ORDER}</label>
                </div>
            </td>
          </tr>
          <tr>
            <td><label>${uiLabelMap.CommonFilter}</label></td>
            <td nowrap="nowrap">
                <div>
                    <label class="checkbox-inline">
                    <input class="checkbox style-0" type="checkbox" name="filterInventoryProblems" value="Y"
                        <#if state.hasFilter('filterInventoryProblems')>checked="checked"</#if>/>
                        <span></span>${uiLabelMap.OrderFilterInventoryProblems}</label>
                    <label class="checkbox-inline">
                    <input class="checkbox style-0" type="checkbox" name="filterAuthProblems" value="Y"
                        <#if state.hasFilter('filterAuthProblems')>checked="checked"</#if>/>
                        <span></span>${uiLabelMap.OrderFilterAuthProblems}</label>
                </div>
            </td>
          </tr>
          <tr>
            <td><label>${uiLabelMap.CommonFilter} (${uiLabelMap.OrderFilterPOs})</label></td>
            <td nowrap="nowrap">
                <div>
                    <label class="checkbox-inline">
                    <input class="checkbox style-0" type="checkbox" name="filterPartiallyReceivedPOs" value="Y"
                        <#if state.hasFilter('filterPartiallyReceivedPOs')>checked="checked"</#if>/>
                        <span></span>${uiLabelMap.OrderFilterPartiallyReceivedPOs}</label>
                    <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="filterPOsOpenPastTheirETA" value="Y"
                        <#if state.hasFilter('filterPOsOpenPastTheirETA')>checked="checked"</#if>/>
                        <span></span>${uiLabelMap.OrderFilterPOsOpenPastTheirETA}</label>
                    <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="filterPOsWithRejectedItems" value="Y"
                        <#if state.hasFilter('filterPOsWithRejectedItems')>checked="checked"</#if>/>
                        <span></span>${uiLabelMap.OrderFilterPOsWithRejectedItems}</label>
                </div>
            </td>
          </tr>
          <tr>
            <td colspan="3" align="center">
              <a class="btn btn-primary1" href="javascript:document.findorder.submit()" class="buttontext">${uiLabelMap.CommonFind}</a>
            </td>
          </tr>
        </table>
      </form>
    </div>
 </div>
</div>
</article>
<#if hasPermission>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.OrderOrderList}</h2>
</header>
  <div id="findOrdersList" class="screenlet">

    <div class="screenlet-body">
        <table class="table table-bordered table-striped table-condensed table-hover smart-form has-tickbox" cellspacing='0'>
          <thead>
            <th>${uiLabelMap.CommonDate}</th>
            <th>${uiLabelMap.OrderOrder} ${uiLabelMap.CommonNbr}</th>
            <th>${uiLabelMap.OrderOrderName}</th>
            <th>${uiLabelMap.OrderOrderType}</th>
            <th>${uiLabelMap.OrderOrderBillFromParty}</th>
            <th>${uiLabelMap.OrderOrderBillToParty}</th>
            <th>${uiLabelMap.OrderProductStore}</th>
            <th>${uiLabelMap.CommonAmount}</th>
            <th>${uiLabelMap.OrderTrackingCode}</th>
            <#if state.hasFilter('filterInventoryProblems') || state.hasFilter('filterAuthProblems') || state.hasFilter('filterPOsOpenPastTheirETA') || state.hasFilter('filterPOsWithRejectedItems') || state.hasFilter('filterPartiallyReceivedPOs')>
                <th>${uiLabelMap.CommonStatus}</th>
                <th>${uiLabelMap.CommonFilter}</th>
            <#else>
                <th colspan="2">${uiLabelMap.CommonStatus}</th>
            </#if>
          </thead>
          <#list orderHeaderList as orderHeader>
            <#assign status = orderHeader.getRelatedOne("StatusItem", true)>
            <#assign orh = Static["org.apache.ofbiz.order.order.OrderReadHelper"].getHelper(orderHeader)>
            <#assign billToParty = orh.getBillToParty()!>
            <#assign billFromParty = orh.getBillFromParty()!>
            <#if billToParty?has_content>
                <#assign billToPartyNameResult = dispatcher.runSync("getPartyNameForDate", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("partyId", billToParty.partyId, "compareDate", orderHeader.orderDate, "userLogin", userLogin))/>
                <#assign billTo = billToPartyNameResult.fullName?default("[${uiLabelMap.OrderPartyNameNotFound}]")/>
            <#else>
              <#assign billTo = ''/>
            </#if>
            <#if billFromParty?has_content>
              <#assign billFrom = Static["org.apache.ofbiz.party.party.PartyHelper"].getPartyName(billFromParty, true)!>
            <#else>
              <#assign billFrom = ''/>
            </#if>
            <#assign productStore = orderHeader.getRelatedOne("ProductStore", true)! />
            <tr>
              <td><#if orderHeader.orderDate?has_content>${Static["org.apache.ofbiz.base.util.UtilFormatOut"].formatDateTime(orderHeader.orderDate, "", locale, timeZone)!}</#if></td>
              <td>
                <a href="<@ofbizUrl>orderview?orderId=${orderHeader.orderId}</@ofbizUrl>" class="buttontext">${orderHeader.orderId}</a>
              </td>
              <td>${orderHeader.orderName!}</td>
              <td>${orderHeader.getRelatedOne("OrderType", true).get("description",locale)}</td>
              <td>${billFrom!}</td>
              <td>${billTo!}</td>
              <td><#if productStore?has_content>${productStore.storeName?default(productStore.productStoreId)}</#if></td>
              <td><@ofbizCurrency amount=orderHeader.grandTotal isoCode=orderHeader.currencyUom/></td>
              <td>
                <#assign trackingCodes = orderHeader.getRelated("TrackingCodeOrder", null, null, false)>
                <#list trackingCodes as trackingCode>
                    <#if trackingCode?has_content>
                        <a href="/marketing/control/FindTrackingCodeOrders?trackingCodeId=${trackingCode.trackingCodeId}&amp;externalLoginKey=${requestAttributes.externalLoginKey!}">${trackingCode.trackingCodeId}</a><br />
                    </#if>
                </#list>
              </td>
              <td>${orderHeader.getRelatedOne("StatusItem", true).get("description",locale)}</td>
              <#if state.hasFilter('filterInventoryProblems') || state.hasFilter('filterAuthProblems') || state.hasFilter('filterPOsOpenPastTheirETA') || state.hasFilter('filterPOsWithRejectedItems') || state.hasFilter('filterPartiallyReceivedPOs')>
              <td>
                  <#if filterInventoryProblems.contains(orderHeader.orderId)>
                    Inv&nbsp;
                  </#if>
                  <#if filterAuthProblems.contains(orderHeader.orderId)>
                   Aut&nbsp;
                  </#if>
                  <#if filterPOsOpenPastTheirETA.contains(orderHeader.orderId)>
                    ETA&nbsp;
                  </#if>
                  <#if filterPOsWithRejectedItems.contains(orderHeader.orderId)>
                    Rej&nbsp;
                  </#if>
                  <#if filterPartiallyReceivedPOs.contains(orderHeader.orderId)>
                    Part&nbsp;
                  </#if>
              </td>
              <#else>
              <td>&nbsp;</td>
              </#if>
            </tr>
          </#list>
          <#if !orderHeaderList?has_content>
            <tr><td colspan="9"><h3>${uiLabelMap.OrderNoOrderFound}</h3></td></tr>
          </#if>
        </table>
        <@pagination/>
    </div>
  </div>
<#else>
  <h3>${uiLabelMap.OrderViewPermissionError}</h3>
</#if>
</div>
</article>
</div>
</section>
