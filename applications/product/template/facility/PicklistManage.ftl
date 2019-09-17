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
<section id="widget-grid">
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductPicklistManage}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-title-bar">
    <ul>
      <#if (picklistInfoList?has_content && 0 < picklistInfoList?size)>
        <#if (picklistCount > highIndex)>
          <li><a href="javascript:paginateOrderList('${viewSize}', '${viewIndex+1}')">${uiLabelMap.CommonNext}</a></li>
        <#else>
          <li><span class="disabled">${uiLabelMap.CommonNext}</span></li>
        </#if>
        <#if (picklistCount > 0)>
          <li><span>${lowIndex} - ${highIndex} ${uiLabelMap.CommonOf} ${picklistCount}</span></li>
        </#if>
        <#if (viewIndex > 0)>
          <li><a href="javascript:paginateOrderList('${viewSize}', '${viewIndex-1}')">${uiLabelMap.CommonPrevious}</a></li>
        <#else>
          <li><span class="disabled">${uiLabelMap.CommonPrevious}</span></li>
        </#if>
      </#if>
    </ul>
  </div>
  <form name="paginationForm" method="post" action="<@ofbizUrl>PicklistManage</@ofbizUrl>">
    <input type="hidden" name="viewSize" value="${viewSize}"/>
    <input type="hidden" name="viewIndex" value="${viewIndex}"/>
    <input type="hidden" name="facilityId" value="${facilityId}"/>
  </form>
  <div class="screenlet-body">
    <#if picklistInfoList?has_content>
      <#list picklistInfoList as picklistInfo>
        <#assign picklist = picklistInfo.picklist>

        <#-- Picklist -->
        <div>
          <span><label>${uiLabelMap.ProductPickList}</label></span> ${picklist.picklistId}
          <span><label>${uiLabelMap.CommonDate}</label></span> ${picklist.picklistDate}
          <form method="post" action="<@ofbizUrl>updatePicklist</@ofbizUrl>" style="display: inline;">
            <input type="hidden" name="facilityId" value="${facilityId}"/>
            <input type="hidden" name="picklistId" value="${picklist.picklistId}"/>
            <select class="form-control" name="statusId">
              <option value="${picklistInfo.statusItem.statusId}" selected>${picklistInfo.statusItem.get("description",locale)}</option>
              <option value="${picklistInfo.statusItem.statusId}">---</option>
              <#list picklistInfo.statusValidChangeToDetailList as statusValidChangeToDetail>
                <option value="${statusValidChangeToDetail.get("statusIdTo", locale)}">${statusValidChangeToDetail.get("description", locale)} (${statusValidChangeToDetail.get("transitionName", locale)})</option>
              </#list>
            </select>
            <input type="submit" value="${uiLabelMap.CommonUpdate}" class="btn btn-primary1"/>
          </form>
          <span><label>${uiLabelMap.ProductCreatedModifiedBy}</label></span> ${picklist.createdByUserLogin}/${picklist.lastModifiedByUserLogin}
          <a href="<@ofbizUrl>PicklistReport.pdf?picklistId=${picklist.picklistId}</@ofbizUrl>" target="_blank" class="btn btn-primary1">${uiLabelMap.ProductPick}/${uiLabelMap.ProductPacking} ${uiLabelMap.CommonReports}</a>
          <hr />
        </div>
        <#if picklistInfo.shipmentMethodType?has_content>
          <div style="margin-left: 15px;">
            <span><label>${uiLabelMap.CommonFor} ${uiLabelMap.ProductShipmentMethodType}</label></span> ${picklistInfo.shipmentMethodType.description?default(picklistInfo.shipmentMethodType.shipmentMethodTypeId)}
          </div>
        </#if>

        <#-- PicklistRole -->
        <#list picklistInfo.picklistRoleInfoList! as picklistRoleInfo>
          <div style="margin-left: 15px;">
            <span class="label">${uiLabelMap.PartyParty}</span> ${picklistRoleInfo.partyNameView.firstName!} ${picklistRoleInfo.partyNameView.middleName!} ${picklistRoleInfo.partyNameView.lastName!} ${picklistRoleInfo.partyNameView.groupName!}
            <span class="label">${uiLabelMap.PartyRole}</span> ${picklistRoleInfo.roleType.description}
            <span class="label">${uiLabelMap.CommonFrom}</span> ${picklistRoleInfo.picklistRole.fromDate}
            <#if picklistRoleInfo.picklistRole.thruDate??><span class="label">${uiLabelMap.CommonThru}</span> ${picklistRoleInfo.picklistRole.thruDate}</#if>
          </div>
        </#list>
        <div style="margin-left: 15px;">
          <span><label>${uiLabelMap.ProductAssignPicker}</label></span>
          <form method="post" action="<@ofbizUrl>createPicklistRole</@ofbizUrl>" style="display: inline;">
            <input type="hidden" name="facilityId" value="${facilityId}"/>
            <input type="hidden" name="picklistId" value="${picklist.picklistId}"/>
            <input type="hidden" name="roleTypeId" value="PICKER"/>
            <select class="form-control"  name="partyId">
              <#list partyRoleAndPartyDetailList as partyRoleAndPartyDetail>
                <option value="${partyRoleAndPartyDetail.partyId}">${partyRoleAndPartyDetail.firstName!} ${partyRoleAndPartyDetail.middleName!} ${partyRoleAndPartyDetail.lastName!} ${partyRoleAndPartyDetail.groupName!} [${partyRoleAndPartyDetail.partyId}]</option>
              </#list>
            </select>
            <input type="submit" value="${uiLabelMap.CommonAdd}" class="btn btn-primary1"/>
          </form>
        </div>

        <#-- PicklistStatusHistory -->
        <#list picklistInfo.picklistStatusHistoryInfoList! as picklistStatusHistoryInfo>
          <div style="margin-left: 15px;">
            <span><label>${uiLabelMap.CommonStatus}</label></span> ${uiLabelMap.CommonChange} ${uiLabelMap.CommonFrom} ${picklistStatusHistoryInfo.statusItem.get("description",locale)}
            ${uiLabelMap.CommonTo} ${picklistStatusHistoryInfo.statusItemTo.description}
            ${uiLabelMap.CommonOn} ${picklistStatusHistoryInfo.picklistStatusHistory.changeDate}
            ${uiLabelMap.CommonBy} ${picklistStatusHistoryInfo.picklistStatusHistory.changeUserLoginId}
          </div>
        </#list>
        <hr />
        <#-- PicklistBin -->
        <#list picklistInfo.picklistBinInfoList! as picklistBinInfo>
          <#assign isBinComplete = Static["org.apache.ofbiz.shipment.picklist.PickListServices"].isBinComplete(delegator, picklistBinInfo.picklistBin.picklistBinId)/>
          <#if (!isBinComplete)>
            <div style="margin-left: 15px;">
              <span><label>${uiLabelMap.ProductBinNum}</label></span> ${picklistBinInfo.picklistBin.binLocationNumber}&nbsp;(${picklistBinInfo.picklistBin.picklistBinId})
              <#if picklistBinInfo.primaryOrderHeader??><span class="label">${uiLabelMap.ProductPrimaryOrderId}</span> ${picklistBinInfo.primaryOrderHeader.orderId}</#if>
              <#if picklistBinInfo.primaryOrderItemShipGroup??><span class="label">${uiLabelMap.ProductPrimaryShipGroupSeqId}</span> ${picklistBinInfo.primaryOrderItemShipGroup.shipGroupSeqId}</#if>
              <#if !picklistBinInfo.picklistItemInfoList?has_content><a href="javascript:document.DeletePicklistBin_${picklistInfo_index}_${picklistBinInfo_index}.submit()" class="buttontext">${uiLabelMap.CommonDelete}</a></#if>
              <form name="DeletePicklistBin_${picklistInfo_index}_${picklistBinInfo_index}" method="post" action="<@ofbizUrl>deletePicklistBin</@ofbizUrl>">
                <input type="hidden" name="picklistBinId" value="${picklistBinInfo.picklistBin.picklistBinId}"/>
                <input type="hidden" name="facilityId" value="${facilityId!}"/>
              </form>
            </div>
            <div style="margin-left: 30px;">
               <span><label>${uiLabelMap.CommonUpdate} ${uiLabelMap.ProductBinNum}</label></span>
              <form method="post" action="<@ofbizUrl>updatePicklistBin</@ofbizUrl>" style="display: inline;">
                <input type="hidden" name="facilityId" value="${facilityId}"/>
                <input type="hidden" name="picklistBinId" value="${picklistBinInfo.picklistBin.picklistBinId}"/>
                <span><label>${uiLabelMap.ProductLocation} ${uiLabelMap.CommonNbr}</label></span>
                <input type"text" class="form-control"  size="2" name="binLocationNumber" value="${picklistBinInfo.picklistBin.binLocationNumber}"/>
                <span><label>${uiLabelMap.PageTitlePickList}</label></span>
                <select class="form-control" name="picklistId">
                  <#list picklistActiveList as picklistActive>
                    <#assign picklistActiveStatusItem = picklistActive.getRelatedOne("StatusItem", true)>
                    <option value="${picklistActive.picklistId}"<#if picklistActive.picklistId == picklist.picklistId> selected="selected"</#if>>${picklistActive.picklistId} [${uiLabelMap.CommonDate}:${picklistActive.picklistDate},${uiLabelMap.CommonStatus}:${picklistActiveStatusItem.get("description",locale)}]</option>
                  </#list>
                </select>
                <input type="submit" value="${uiLabelMap.CommonUpdate}" class="btn btn-primary1"/>
              </form>
            </div>
            <#if picklistBinInfo.picklistItemInfoList?has_content>
              <div style="margin-left: 30px;">
                <table class="table table-bordered table-striped" cellspacing="0">
                  <tr class="header-row">
                    <th>${uiLabelMap.ProductOrderId}</th>
                    <th>${uiLabelMap.ProductOrderShipGroupId}</th>
                    <th>${uiLabelMap.ProductOrderItem}</th>
                    <th>${uiLabelMap.ProductProduct}</th>
                    <th>${uiLabelMap.ProductInventoryItem}</th>
                    <th>${uiLabelMap.ProductLocation}</th>
                    <th>${uiLabelMap.ProductQuantity}</th>
                    <th>&nbsp;</th>
                  </tr>
                  <#assign alt_row = false>
                  <#list picklistBinInfo.picklistItemInfoList! as picklistItemInfo>
                    <#assign picklistItem = picklistItemInfo.picklistItem>
                    <#assign inventoryItemAndLocation = picklistItemInfo.inventoryItemAndLocation>
                    <tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
                      <td>${picklistItem.orderId}</td>
                      <td>${picklistItem.shipGroupSeqId}</td>
                      <td>${picklistItem.orderItemSeqId}</td>
                      <td>${picklistItemInfo.orderItem.productId}<#if picklistItemInfo.orderItem.productId != inventoryItemAndLocation.productId>&nbsp;[${inventoryItemAndLocation.productId}]</#if></td>
                      <td>${inventoryItemAndLocation.inventoryItemId}</td>
                      <td>${inventoryItemAndLocation.areaId!}-${inventoryItemAndLocation.aisleId!}-${inventoryItemAndLocation.sectionId!}-${inventoryItemAndLocation.levelId!}-${inventoryItemAndLocation.positionId!}</td>
                      <td>${picklistItem.quantity}</td>
                      <#if !picklistItemInfo.itemIssuanceList?has_content>
                        <td>
                          <form name="deletePicklistItem_${picklist.picklistId}_${picklistItem.orderId}_${picklistItemInfo_index}" method="post" action="<@ofbizUrl>deletePicklistItem</@ofbizUrl>">
                            <input type="hidden" name="picklistBinId" value="${picklistItemInfo.picklistItem.picklistBinId}"/>
                            <input type="hidden" name="orderId" value= "${picklistItemInfo.picklistItem.orderId}"/>
                            <input type="hidden" name="orderItemSeqId" value="${picklistItemInfo.picklistItem.orderItemSeqId}"/>
                            <input type="hidden" name="shipGroupSeqId" value="${picklistItemInfo.picklistItem.shipGroupSeqId}"/>
                            <input type="hidden" name="inventoryItemId" value="${picklistItemInfo.picklistItem.inventoryItemId}"/>
                            <input type="hidden" name="facilityId" value="${facilityId!}"/>
                            <a href='javascript:document.deletePicklistItem_${picklist.picklistId}_${picklistItem.orderId}_${picklistItemInfo_index}.submit()' class='buttontext'>&nbsp;${uiLabelMap.CommonDelete}&nbsp;</a>
                          </form>
                        </td>
                      </#if>
                      <td>
                        <#-- picklistItem.orderItemShipGrpInvRes (do we want to display any of this info?) -->
                        <#-- picklistItemInfo.itemIssuanceList -->
                        <#list picklistItemInfo.itemIssuanceList! as itemIssuance>
                          <b>${uiLabelMap.ProductIssue} ${uiLabelMap.CommonTo} ${uiLabelMap.ProductShipmentItemSeqId}:</b> ${itemIssuance.shipmentId}:${itemIssuance.shipmentItemSeqId}
                          <b>${uiLabelMap.ProductQuantity}:</b> ${itemIssuance.quantity}
                          <b>${uiLabelMap.CommonDate}: </b> ${itemIssuance.issuedDateTime}
                        </#list>
                      </td>
                    </tr>
                    <#-- toggle the row color -->
                    <#assign alt_row = !alt_row>
                  </#list>
                </table>
              </div>
              <#if picklistBinInfo.productStore.managedByLot?? && picklistBinInfo.productStore.managedByLot = "Y">
                <div style="margin-left: 30px;">
                  <table class="table table-bordered table-striped" cellspacing="0">
                    <tr class="header-row"
                      <th>${uiLabelMap.ProductOrderId}</th>
                      <th>${uiLabelMap.ProductOrderShipGroupId}</th>
                      <th>${uiLabelMap.ProductOrderItem}</th>
                      <th>${uiLabelMap.ProductProduct}</th>
                      <th>${uiLabelMap.ProductInventoryItem}</th>
                      <th>${uiLabelMap.ProductLotId}</th>
                      <th>${uiLabelMap.ProductQuantity}</th>
                      <th>&nbsp;</th>
                      </tr>
                      <#assign alt_row = false>
                      <#list picklistBinInfo.picklistItemInfoList! as picklistItemInfo>
                        <#assign picklistItem = picklistItemInfo.picklistItem>
                        <#assign inventoryItemAndLocation = picklistItemInfo.inventoryItemAndLocation>
                        <#if !picklistItemInfo.product.lotIdFilledIn?has_content || picklistItemInfo.product.lotIdFilledIn != "Forbidden">
                          <form name="editPicklistItem_${picklist.picklistId}_${picklistItem.orderId}_${picklistItemInfo_index}" method="post" action="<@ofbizUrl>editPicklistItem</@ofbizUrl>">
                            <tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
                              <td>${picklistItem.orderId}</td>
                              <td>${picklistItem.shipGroupSeqId}</td>
                              <td>${picklistItem.orderItemSeqId}</td>
                              <td>${picklistItemInfo.orderItem.productId}<#if picklistItemInfo.orderItem.productId != inventoryItemAndLocation.productId>&nbsp;[${inventoryItemAndLocation.productId}]</#if></td>
                              <td>${inventoryItemAndLocation.inventoryItemId}</td>
                              <td><input class="form-control" type="text" name="lotId" <#if inventoryItemAndLocation.lotId?has_content>value="${inventoryItemAndLocation.lotId}"</#if> /></td>
                              <td><input class="form-control" type="text" name="quantity" value="${picklistItem.quantity}" /></td>
                              <td>
                                <input type="hidden" name="picklistBinId" value="${picklistItemInfo.picklistItem.picklistBinId}"/>
                                <input type="hidden" name="orderId" value= "${picklistItemInfo.picklistItem.orderId}"/>
                                <input type="hidden" name="orderItemSeqId" value="${picklistItemInfo.picklistItem.orderItemSeqId}"/>
                                <input type="hidden" name="shipGroupSeqId" value="${picklistItemInfo.picklistItem.shipGroupSeqId}"/>
                                <input type="hidden" name="inventoryItemId" value="${picklistItemInfo.picklistItem.inventoryItemId}"/>
                                <input type="hidden" name="facilityId" value="${facilityId!}"/>
                                <input type="hidden" name="productId" value="${picklistItemInfo.orderItem.productId}" />
                                <#if inventoryItemAndLocation.lotId?has_content>
                                  <input type="hidden" name="oldLotId" value="${inventoryItemAndLocation.lotId}" />
                                </#if>
                                <a href='javascript:document.editPicklistItem_${picklist.picklistId}_${picklistItem.orderId}_${picklistItemInfo_index}.submit()' class='btn btn-primary1'>&nbsp;${uiLabelMap.CommonEdit}&nbsp;</a>
                              </td>
                            </tr>
                          </form>
                          <#-- toggle the row color -->
                          <#assign alt_row = !alt_row>
                        </#if>
                      </#list>
                  </table>
                </div>
              </#if>
            </#if>
          </#if>
        </#list>
        <#if picklistInfo_has_next>
          <hr />
        </#if>
      </#list>
    <#else>
      <div class="alert alert-info fade in">${uiLabelMap.ProductNoPicksStarted}.</div>
    </#if>
  </div>
</div>
</div>
</div>
</article>
</div>
</section>
