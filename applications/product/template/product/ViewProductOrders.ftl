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

<script language="JavaScript" type="text/javascript">
    function paginateOrderList(viewSize, viewIndex) {
        document.paginationForm.viewSize.value = viewSize;
        document.paginationForm.viewIndex.value = viewIndex;
        document.paginationForm.submit();
    }
</script>
<div class="row">

<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-3" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.OrderOrderFound}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-title-bar">
    <ul>
      <#if (orderList?has_content && 0 < orderList?size)>
        <#if (orderListSize > highIndex)>
          <li><a href="javascript:paginateOrderList('${viewSize}', '${viewIndex+1}')">${uiLabelMap.CommonNext}</a></li>
        <#else>
          <li><span class="disabled">${uiLabelMap.CommonNext}</span></li>
        </#if>
        <#if (orderListSize > 0)>
          <li><span>${lowIndex} - ${highIndex} ${uiLabelMap.CommonOf} ${orderListSize}</span></li>
        </#if>
        <#if (viewIndex > 1)>
          <li><a href="javascript:paginateOrderList('${viewSize}', '${viewIndex-1}')">${uiLabelMap.CommonPrevious}</a></li>
        <#else>
          <li><span class="disabled">${uiLabelMap.CommonPrevious}</span></li>
        </#if>
      </#if>
    </ul>
  </div>
  <div class="screenlet-body">
    <form name="paginationForm" method="post" action="<@ofbizUrl>viewProductOrder</@ofbizUrl>">
      <input type="hidden" name="viewSize"/>
      <input type="hidden" name="viewIndex"/>
      <#if paramIdList?? && paramIdList?has_content>
        <#list paramIdList as paramIds>
          <#assign paramId = paramIds.split("=")/>
          <#if "productId" == paramId[0]>
            <#assign productId = paramId[1]/>
          </#if>
          <input type="hidden" name="${paramId[0]}" value="${paramId[1]}"/>
        </#list>
      </#if>
    </form>
    <table class="table table-bordered table-striped" cellspacing='0'>
     <thead>
      <tr class="header-row">
        <th>${uiLabelMap.OrderOrderId}</th>
        <th>${uiLabelMap.FormFieldTitle_itemStatusId}</th>
        <th>${uiLabelMap.FormFieldTitle_orderItemSeqId}</th>
        <th>${uiLabelMap.OrderDate}</th>
        <th>${uiLabelMap.OrderUnitPrice}</th>
        <th>${uiLabelMap.OrderQuantity}</th>
        <th>${uiLabelMap.OrderOrderType}</th>
      </tr>
     </thead>
      <#if orderList?has_content && productId??>
        <#list orderList as order>
          <#assign orderItems = delegator.findByAnd("OrderItem", {"orderId" : order.orderId, "productId" : productId}, null, false)/>
          <#list orderItems as orderItem>
            <tr>
              <td><a href="/ordermgr/control/orderview?orderId=${orderItem.orderId}" class='buttontext'>${orderItem.orderId}</a></td>
              <#assign currentItemStatus = orderItem.getRelatedOne("StatusItem", false)/>
              <td>${currentItemStatus.get("description",locale)?default(currentItemStatus.statusId)}</td>
              <td>${orderItem.orderItemSeqId}</td>
              <td>${order.orderDate}</td>
              <td>${orderItem.unitPrice}</td>
              <td>${orderItem.quantity}</td>
              <#assign currentOrderType = order.getRelatedOne("OrderType", false)/>
              <td>${currentOrderType.get("description",locale)?default(currentOrderType.orderTypeId)}</td>
            </tr>
          </#list>
        </#list>
      <#else>
        <tr>
          <td colspan='7'><div class="alert alert-info fade in">${uiLabelMap.OrderNoOrderFound}</div></td>
        </tr>
      </#if>
    </table>
  </div>
</div>
</div>
</div>
</article>
</div>
