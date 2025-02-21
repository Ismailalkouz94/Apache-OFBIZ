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
    function setNow(field) { eval('document.selectAllForm.' + field + '.value="${nowTimestamp}"'); }
</script>
        <#if invalidProductId??>
            <div class="errorMessage">${invalidProductId}</div>
        </#if>
        <#-- Receiving Results -->
        <#if receivedItems?has_content>
          <div class="alert alert-info fade in">${uiLabelMap.ProductReceiptPurchaseOrder} ${purchaseOrder.orderId}</div>
          <hr />
          <table class="table table-bordered table-striped" cellspacing="0">
           <thead>
            <tr class="header-row">
              <th>${uiLabelMap.ProductShipmentId}</th>
              <th>${uiLabelMap.ProductReceipt}</th>
              <th>${uiLabelMap.CommonDate}</th>
              <th>${uiLabelMap.ProductPo}</th>
              <th>${uiLabelMap.ProductLine}</th>
              <th>${uiLabelMap.ProductProductId}</th>
              <th>${uiLabelMap.ProductLotId}</th>
              <th>${uiLabelMap.ProductPerUnitPrice}</th>
              <th>${uiLabelMap.CommonRejected}</th>
              <th>${uiLabelMap.CommonAccepted}</th>
            </tr>
           </thead>
            <#list receivedItems as item>
              <form name="cancelReceivedItemsForm_${item_index}" method="post" action="<@ofbizUrl>cancelReceivedItems</@ofbizUrl>">
                <input class="form-control" type="hidden" name="receiptId" value ="${(item.receiptId)!}"/>
                <input class="form-control" type="hidden" name="purchaseOrderId" value ="${(item.orderId)!}"/>
                <input class="form-control" type="hidden" name="facilityId" value ="${facilityId!}"/>
                <tr>
                  <td><a href="<@ofbizUrl>ViewShipment?shipmentId=${item.shipmentId!}</@ofbizUrl>" class="buttontext">${item.shipmentId!} ${item.shipmentItemSeqId!}</a></td>
                  <td>${item.receiptId}</td>
                  <td>${item.getString("datetimeReceived").toString()}</td>
                  <td><a href="/ordermgr/control/orderview?orderId=${item.orderId}" class="btn btn-primary1">${item.orderId}</a></td>
                  <td>${item.orderItemSeqId}</td>
                  <td>${item.productId?default("Not Found")}</td>
                  <td>${item.lotId?default("")}</td>
                  <td>${item.unitCost?default(0)?string("##0.00")}</td>
                  <td>${item.quantityRejected?default(0)?string.number}</td>
                  <td>${item.quantityAccepted?string.number}</td>
                  <td>
                    <#if (item.quantityAccepted?int > 0 || item.quantityRejected?int > 0)>
                      <a href="javascript:document.cancelReceivedItemsForm_${item_index}.submit();" class="buttontext111">${uiLabelMap.CommonCancel}</a>
                    </#if>
                  </td>
                </tr>
              </form>
            </#list>
            <tr><td colspan="10"><hr /></td></tr>
          </table>
          <br />
        </#if>

        <#-- Single Product Receiving -->
        <#if requestParameters.initialSelected?? && product?has_content>
          <form method="post" action="<@ofbizUrl>receiveSingleInventoryProduct</@ofbizUrl>" name="selectAllForm">
            <table class="basic-table" cellspacing="0">
              <#-- general request fields -->
              <input class="form-control" type="hidden" name="facilityId" value="${requestParameters.facilityId!}"/>
              <input class="form-control" type="hidden" name="purchaseOrderId" value="${requestParameters.purchaseOrderId!}"/>
              <#-- special service fields -->
              <input class="form-control" type="hidden" name="productId" value="${requestParameters.productId!}"/>
              <#if purchaseOrder?has_content>
              <#assign unitCost = firstOrderItem.unitPrice?default(standardCosts.get(firstOrderItem.productId)?default(0))/>
              <input class="form-control" type="hidden" name="orderId" value="${purchaseOrder.orderId}"/>
              <input class="form-control" type="hidden" name="orderItemSeqId" value="${firstOrderItem.orderItemSeqId}"/>
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductPurchaseOrder}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  <b>${purchaseOrder.orderId}</b>&nbsp;/&nbsp;<b>${firstOrderItem.orderItemSeqId}</b>
                  <#if 1 < purchaseOrderItems.size()>
                    (${uiLabelMap.ProductMultipleOrderItemsProduct} - ${purchaseOrderItems.size()}:1 ${uiLabelMap.ProductItemProduct})
                  <#else>
                    (${uiLabelMap.ProductSingleOrderItemProduct} - 1:1 ${uiLabelMap.ProductItemProduct})
                  </#if>
                </td>
              </tr>
              </#if>
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductProductId}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  <b>${requestParameters.productId!}</b>
                </td>
              </tr>
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductProductName}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  <a href="/catalog/control/EditProduct?productId=${product.productId}${externalKeyParam!}" target="catalog" class="buttontext">${product.internalName!}</a>
                </td>
              </tr>
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductProductDescription}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  ${product.description!}
                </td>
              </tr>
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductItemDescription}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  <input class="form-control" type="text" name="itemDescription" size="30" maxlength="60"/>
                </td>
              </tr>
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductInventoryItemType}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  <#if product.inventoryItemTypeId?has_content>
                    <input  class="form-control" name="inventoryItemTypeId" type="hidden" value="${product.inventoryItemTypeId}" />
                    <#assign inventoryItemType = product.getRelatedOne("InventoryItemType", true)! />
                    ${inventoryItemType.description!}
                  <#else>
                  <select  class="form-control" name="inventoryItemTypeId" size="1">
                    <#list inventoryItemTypes as nextInventoryItemType>
                      <option value="${nextInventoryItemType.inventoryItemTypeId}"
                        <#if (facility.defaultInventoryItemTypeId?has_content) && (nextInventoryItemType.inventoryItemTypeId == facility.defaultInventoryItemTypeId)>
                          selected="selected"
                        </#if>
                      >${nextInventoryItemType.get("description",locale)?default(nextInventoryItemType.inventoryItemTypeId)}</option>
                    </#list>
                  </select>
                  </#if>
                </td>
              </tr>
              <#assign isSerialized = Static["org.apache.ofbiz.product.product.ProductWorker"].isSerialized(delegator, product.productId)!/>
              <#if isSerialized?has_content>
                <tr>
                  <td width="14%">&nbsp;</td>
                  <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductSerialNumber}</label></td>
                  <td width="6%">&nbsp;</td>
                  <td width="74%">
                      <input class="form-control" type="text" name="serialNumber" value="${parameters.serialNumber!}" />
                  </td>
                </tr>
              </#if>
              <tr>
                <td colspan="4">&nbsp;</td>
              </tr>
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductFacilityOwner}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  <@htmlTemplate.lookupField formName="selectAllForm" name="ownerPartyId" id="ownerPartyId" fieldFormName="LookupPartyName"/>
                </td>
              </tr>
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductSupplier}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  <select class="form-control" name="partyId">
                    <option value=""></option>
                    <#if supplierPartyIds?has_content>
                      <#list supplierPartyIds as supplierPartyId>
                        <option value="${supplierPartyId}" <#if supplierPartyId == parameters.partyId!> selected="selected"</#if>>
                          [${supplierPartyId}] ${Static["org.apache.ofbiz.party.party.PartyHelper"].getPartyName(delegator, supplierPartyId, true)}
                        </option>
                      </#list>
                    </#if>
                  </select>
                </td>
              </tr>
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductDateReceived}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  <input class="form-control" type="text" name="datetimeReceived" size="24" value="${nowTimestamp}" />
                </td>
              </tr>
              
              
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.lotId}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  <input class="form-control" type="text" name="lotId" size="10"/>
                </td>
              </tr>

              <#-- facility location(s) -->
              <#assign facilityLocations = (product.getRelated("ProductFacilityLocation", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("facilityId", facilityId), null, false))!/>
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductFacilityLocation}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  <#if facilityLocations?has_content>
                    <select class="form-control" name="locationSeqId">
                      <#list facilityLocations as productFacilityLocation>
                        <#assign facility = productFacilityLocation.getRelatedOne("Facility", true)/>
                        <#assign facilityLocation = productFacilityLocation.getRelatedOne("FacilityLocation", false)!/>
                        <#assign facilityLocationTypeEnum = (facilityLocation.getRelatedOne("TypeEnumeration", true))!/>
                        <option value="${productFacilityLocation.locationSeqId}"><#if facilityLocation??>${facilityLocation.areaId!}:${facilityLocation.aisleId!}:${facilityLocation.sectionId!}:${facilityLocation.levelId!}:${facilityLocation.positionId!}</#if><#if facilityLocationTypeEnum??>(${facilityLocationTypeEnum.get("description",locale)})</#if>[${productFacilityLocation.locationSeqId}]</option>
                      </#list>
                      <option value="">${uiLabelMap.ProductNoLocation}</option>
                    </select>
                  <#else>
                    <#if parameters.facilityId??>
                      <#assign LookupFacilityLocationView="LookupFacilityLocation?facilityId=${facilityId}">
                    <#else>
                      <#assign LookupFacilityLocationView="LookupFacilityLocation">
                    </#if>
                    <@htmlTemplate.lookupField formName="selectAllForm" name="locationSeqId" id="locationSeqId" fieldFormName="${LookupFacilityLocationView}"/>
                  </#if>
                </td>
              </tr>
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductRejectedReason}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  <select class="form-control" name="rejectionId" size="1">
                    <option></option>
                    <#list rejectReasons as nextRejection>
                      <option value="${nextRejection.rejectionId}">${nextRejection.get("description",locale)?default(nextRejection.rejectionId)}</option>
                    </#list>
                  </select>
                </td>
              </tr>
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductQuantityRejected}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  <input class="form-control" type="text" name="quantityRejected" size="5" value="0" />
                </td>
              </tr>
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductQuantityAccepted}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  <input class="form-control" type="text" name="quantityAccepted" size="5" value="${defaultQuantity?default(1)?string.number}"/>
                </td>
              </tr>
              <tr>
                <td width="14%">&nbsp;</td>
                <td width="6%" align="right" nowrap="nowrap"><label>${uiLabelMap.ProductPerUnitPrice}</label></td>
                <td width="6%">&nbsp;</td>
                <td width="74%">
                  <#-- get the default unit cost -->
                  <#if (!unitCost?? || unitCost == 0.0)><#assign unitCost = standardCosts.get(product.productId)?default(0)/></#if>
                  <input type="text" name="unitCost" size="10" value="${unitCost}"/>
                </td>
              </tr>
              <tr>
                <td colspan="2">&nbsp;</td>
                <td colspan="2"><input class="btn btn-primary1" type="submit" value="${uiLabelMap.CommonReceive}" /></td>
              </tr>
            </table>
            <script language="JavaScript" type="text/javascript">
              document.selectAllForm.quantityAccepted.focus();
            </script>
          </form>

        <#-- Select Shipment Screen -->
        <#elseif requestParameters.initialSelected?? && !requestParameters.shipmentId??>
          <label>${uiLabelMap.ProductSelectShipmentReceive}</label>
          <form method="post" action="<@ofbizUrl>ReceiveInventory</@ofbizUrl>" name="selectAllForm">
            <#-- general request fields -->
            <input class="form-control" type="hidden" name="facilityId" value="${requestParameters.facilityId!}"/>
            <input class="form-control" type="hidden" name="purchaseOrderId" value="${requestParameters.purchaseOrderId!}"/>
            <input class="form-control" type="hidden" name="initialSelected" value="Y"/>
            <input class="form-control" type="hidden" name="partialReceive" value="${partialReceive!}"/>
            <table class="basic-table" cellspacing="0">
              <#list shipments! as shipment>
                <#assign originFacility = shipment.getRelatedOne("OriginFacility", true)!/>
                <#assign destinationFacility = shipment.getRelatedOne("DestinationFacility", true)!/>
                <#assign statusItem = shipment.getRelatedOne("StatusItem", true)/>
                <#assign shipmentType = shipment.getRelatedOne("ShipmentType", true)/>
                <#assign shipmentDate = shipment.estimatedArrivalDate!/>
                <tr>
                  <td><hr /></td>
                </tr>
                <tr>
                  <td>
                    <table class="basic-table" cellspacing="0">
                      <tr>
                        <td nowrap="nowrap"><label class="radio radio-inline"><input class="radiobox" type="radio" name="shipmentId" value="${shipment.shipmentId}" /> <span>${shipment.shipmentId}</span></label></td>
                        <td>${shipmentType.get("description",locale)?default(shipmentType.shipmentTypeId?default(""))}</td>
                        <td>${statusItem.get("description",locale)?default(statusItem.statusId?default("N/A"))}</td>
                        <td>${(originFacility.facilityName)!} [${shipment.originFacilityId!}]</td>
                        <td>${(destinationFacility.facilityName)!} [${shipment.destinationFacilityId!}]</td>
                        <td style="white-space: nowrap;">${(shipment.estimatedArrivalDate.toString())!}</td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </#list>
              <tr>
                <td><hr /></td>
              </tr>
              <tr>
                <td>
                  <table class="basic-table" cellspacing="0">
                    <tr>
                      <td nowrap="nowrap"><label class="radio radio-inline"><input class="radiobox" type="radio" name="shipmentId" value="_NA_" /><span> ${uiLabelMap.ProductNoSpecificShipment}</span></label></td>
                      <td colspan="5"></td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td>&nbsp;<a href="javascript:document.selectAllForm.submit();" class="btn btn-primary1">${uiLabelMap.ProductReceiveSelectedShipment}</a></td>
              </tr>
            </table>
          </form>

        <#-- Multi-Item PO Receiving -->
        <#elseif requestParameters.initialSelected?? && purchaseOrder?has_content>
          <input class="form-control" type="hidden" id="getConvertedPrice" value="<@ofbizUrl secure="${request.isSecure()?string}">getConvertedPrice"</@ofbizUrl> />
          <input class="form-control" type="hidden" id="alertMessage" value="${uiLabelMap.ProductChangePerUnitPrice}" />
          <form method="post" action="<@ofbizUrl>receiveInventoryProduct</@ofbizUrl>" name="selectAllForm">
            <#-- general request fields -->
            <input class="form-control" type="hidden" name="facilityId" value="${requestParameters.facilityId!}"/>
            <input class="form-control" type="hidden" name="purchaseOrderId" value="${requestParameters.purchaseOrderId!}"/>
            <input class="form-control" type="hidden" name="initialSelected" value="Y"/>
            <#if shipment?has_content>
            <input class="form-control" type="hidden" name="shipmentIdReceived" value="${shipment.shipmentId}"/>
            </#if>
            <input class="form-control" type="hidden" name="_useRowSubmit" value="Y"/>
            <#assign rowCount = 0/>
            <table class="basic-table" cellspacing="0">
              <#if !purchaseOrderItems?? || purchaseOrderItems.size() == 0>
                <tr>
                  <td colspan="2"><label>${uiLabelMap.ProductNoItemsPoReceive}.</label></td>
                </tr>
              <#else>
                <tr>
                  <td>
                    <h3>${uiLabelMap.ProductReceivePurchaseOrder} #${purchaseOrder.orderId}</h3>
                    <#if shipment?has_content>
                    <h3>${uiLabelMap.ProductShipmentId} #${shipment.shipmentId}</h3>
                    <label class="checkbox-inline">
                    <input class="checkbox style-0" type="checkbox" name="forceShipmentReceived" value="Y"/><span>Set Shipment As Received</span>&nbsp;</label>
                    </#if>
                  </td>
                  <td align="right">
                    <label class="checkbox-inline">
                    <input class="checkbox style-0" type="checkbox" name="selectAll" value="Y" class="selectAll" checked/><span>${uiLabelMap.CommonSelectAll}</span></label>
                  </td>
                </tr>
                <#list purchaseOrderItems as orderItem>
                  <#assign defaultQuantity = orderItem.quantity - receivedQuantities[orderItem.orderItemSeqId]?double/>
                  <#assign itemCost = orderItem.unitPrice?default(0)/>
                  <#assign salesOrderItem = salesOrderItems[orderItem.orderItemSeqId]!/>
                  <#if shipment?has_content>
                    <#if shippedQuantities[orderItem.orderItemSeqId]??>
                      <#assign defaultQuantity = shippedQuantities[orderItem.orderItemSeqId]?double - receivedQuantities[orderItem.orderItemSeqId]?double/>
                    <#else>
                      <#assign defaultQuantity = 0/>
                    </#if>
                  </#if>
                  <#if 0 < defaultQuantity>
                  <#assign orderItemType = orderItem.getRelatedOne("OrderItemType", false)/>
                  <input class="form-control" type="hidden" name="orderId_o_${rowCount}" value="${orderItem.orderId}"/>
                  <input class="form-control" type="hidden" name="orderItemSeqId_o_${rowCount}" value="${orderItem.orderItemSeqId}"/>
                  <input class="form-control" type="hidden" name="facilityId_o_${rowCount}" value="${requestParameters.facilityId!}"/>
                  <input class="form-control" type="hidden" name="datetimeReceived_o_${rowCount}" value="${nowTimestamp}"/>
                  <#if shipment?? && shipment.shipmentId?has_content>
                    <input class="form-control" type="hidden" name="shipmentId_o_${rowCount}" value="${shipment.shipmentId}"/>
                  </#if>
                  <#if salesOrderItem?has_content>
                    <input class="form-control" type="hidden" name="priorityOrderId_o_${rowCount}" value="${salesOrderItem.orderId}"/>
                    <input class="form-control" type="hidden" name="priorityOrderItemSeqId_o_${rowCount}" value="${salesOrderItem.orderItemSeqId}"/>
                  </#if>

                  <tr>
                    <td colspan="2"><hr /></td>
                  </tr>
                  <tr>
                    <td>
                      <table class="basic-table" cellspacing="0">
                        <tr>
                          <#if orderItem.productId??>
                            <#assign product = orderItem.getRelatedOne("Product", true)/>
                            <input class="form-control" type="hidden" name="productId_o_${rowCount}" value="${product.productId}"/>
                            <td width="45%">
                                ${orderItem.orderItemSeqId}:&nbsp;<a href="/catalog/control/EditProduct?productId=${product.productId}${externalKeyParam!}" target="catalog" class="buttontext">${product.productId}&nbsp;-&nbsp;${orderItem.itemDescription!}</a> : ${product.description!}
                            </td>
                          <#else>
                            <td width="45%">
                                <b>${orderItemType.get("description",locale)}</b> : ${orderItem.itemDescription!}&nbsp;&nbsp;
                                <input class="form-control" type="text" size="12" name="productId_o_${rowCount}"/>
                                <a href="/catalog/control/EditProduct?${StringUtil.wrapString(externalKeyParam)}" target="catalog" class="btn btn-primary1">${uiLabelMap.ProductCreateProduct}</a>
                            </td>
                          </#if>
                          <td align="right"><label>${uiLabelMap.ProductLocation}:</label></td>
                          <#-- location(s) -->
                          <td align="right">
                            <#assign facilityLocations = (orderItem.getRelated("ProductFacilityLocation", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("facilityId", facilityId), null, false))!/>
                            <#if facilityLocations?has_content>
                              <select class="form-control" name="locationSeqId_o_${rowCount}">
                                <#list facilityLocations as productFacilityLocation>
                                  <#assign facility = productFacilityLocation.getRelatedOne("Facility", true)/>
                                  <#assign facilityLocation = productFacilityLocation.getRelatedOne("FacilityLocation", false)!/>
                                  <#assign facilityLocationTypeEnum = (facilityLocation.getRelatedOne("TypeEnumeration", true))!/>
                                  <option value="${productFacilityLocation.locationSeqId}"><#if facilityLocation??>${facilityLocation.areaId!}:${facilityLocation.aisleId!}:${facilityLocation.sectionId!}:${facilityLocation.levelId!}:${facilityLocation.positionId!}</#if><#if facilityLocationTypeEnum??>(${facilityLocationTypeEnum.get("description",locale)})</#if>[${productFacilityLocation.locationSeqId}]</option>
                                </#list>
                                <option value="">${uiLabelMap.ProductNoLocation}</option>
                              </select>
                            <#else>
                              <#if parameters.facilityId??>
                                <#assign LookupFacilityLocationView="LookupFacilityLocation?facilityId=${facilityId}">
                              <#else>
                                <#assign LookupFacilityLocationView="LookupFacilityLocation">
                              </#if>
                              <@htmlTemplate.lookupField formName="selectAllForm" name="locationSeqId_o_${rowCount}" id="locationSeqId_o_${rowCount}" fieldFormName="${LookupFacilityLocationView}"/>
                            </#if>
                          </td>
                          <td align="right"><label>${uiLabelMap.ProductQtyReceived} :</label></td>
                          <td align="right">
                            <input class="form-control" type="text" name="quantityAccepted_o_${rowCount}" size="6" value=<#if partialReceive??>"0"<#else>"${defaultQuantity?string.number}"</#if>/>
                          </td>
                        </tr>
                        <tr>
                          <td width="45%">
                            ${uiLabelMap.ProductInventoryItemType} :&nbsp;
                            <#if product.inventoryItemTypeId?has_content>
                              <input class="form-control" name="inventoryItemTypeId_o_${rowCount}" type="hidden" value="${product.inventoryItemTypeId}" />
                              <#assign inventoryItemType = product.getRelatedOne("InventoryItemType", true)! />
                              ${inventoryItemType.description!}
                            <#else>
                            <select class="form-control" name="inventoryItemTypeId_o_${rowCount}" size="1">
                              <#list inventoryItemTypes as nextInventoryItemType>
                              <option value="${nextInventoryItemType.inventoryItemTypeId}"
                               <#if (facility.defaultInventoryItemTypeId?has_content) && (nextInventoryItemType.inventoryItemTypeId == facility.defaultInventoryItemTypeId)>
                                selected="selected"
                              </#if>
                              >${nextInventoryItemType.get("description",locale)?default(nextInventoryItemType.inventoryItemTypeId)}</option>
                              </#list>
                            </select>
                            </#if>
                          </td>
                          <td align="right"><label>${uiLabelMap.ProductRejectionReason} :</label></td>
                          <td align="right">
                            <select class="form-control" name="rejectionId_o_${rowCount}" size="1">
                              <option></option>
                              <#list rejectReasons as nextRejection>
                              <option value="${nextRejection.rejectionId}">${nextRejection.get("description",locale)?default(nextRejection.rejectionId)}</option>
                              </#list>
                            </select>
                          </td>
                          <td align="right"><label>${uiLabelMap.ProductQtyRejected} :</label></td>
                          <td align="right">
                            <input class="form-control" type="text" name="quantityRejected_o_${rowCount}" value="0" size="6"/>
                          </td>
                          <tr>
                            <td width="45%">
                              <#assign isSerialized = Static["org.apache.ofbiz.product.product.ProductWorker"].isSerialized(delegator, product.productId)!/>
                              <#if isSerialized?has_content>
                                <label>${uiLabelMap.ProductSerialNumber}</label> :&nbsp;
                                <input class="form-control" type="text" name="serialNumber_o_${rowCount}" value="" />
                            </#if>
                            </td>
                            <#if !product.lotIdFilledIn?has_content || product.lotIdFilledIn != "Forbidden">
                              <td align="right"><label>${uiLabelMap.ProductLotId}</label></td>
                              <td align="right">
                                <input class="form-control" type="text" name="lotId_o_${rowCount}" size="20" />
                              </td>
                            <#else>
                              <td align="right">&nbsp;</td>
                              <td align="right">&nbsp;</td>
                            </#if>
                            <td align="right"><label>${uiLabelMap.OrderQtyOrdered} :</label></td>
                            <td align="right">
                              <input class="form-control" type="text" class="inputBox" name="quantityOrdered" value="${orderItem.quantity}" size="6" maxlength="20" disabled="disabled" />
                            </td>
                          </tr>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td align="right"><label>${uiLabelMap.ProductFacilityOwner}:</label></td>
                          <td align="right"><input class="form-control" type="text" name="ownerPartyId_o_${rowCount}" size="20" maxlength="20" value="${facility.ownerPartyId}"/></td>
                          <#if currencyUomId?default('') != orderCurrencyUomId?default('')>
                            <td><label>${uiLabelMap.ProductPerUnitPriceOrder}:</label></td>
                            <td>
                              <input class="form-control" type="hidden" name="orderCurrencyUomId_o_${rowCount}" value="${orderCurrencyUomId!}" />
                              <input class="form-control" type="text" id="orderCurrencyUnitPrice_${rowCount}" name="orderCurrencyUnitPrice_o_${rowCount}" value="${orderCurrencyUnitPriceMap[orderItem.orderItemSeqId]}" onchange="javascript:getConvertedPrice(orderCurrencyUnitPrice_${rowCount}, '${orderCurrencyUomId}', '${currencyUomId}', '${rowCount}', '${orderCurrencyUnitPriceMap[orderItem.orderItemSeqId]}', '${itemCost}');" size="6" maxlength="20" />
                              ${orderCurrencyUomId!}
                            </td>
                            <td><label>${uiLabelMap.ProductPerUnitPriceFacility}:</label></td>
                            <td>
                              <input class="form-control" type="hidden" name="currencyUomId_o_${rowCount}" value="${currencyUomId!}" />
                              <input class="form-control" type="text" id="unitCost_${rowCount}" name="unitCost_o_${rowCount}" value="${itemCost}" readonly="readonly" size="6" maxlength="20" />
                              ${currencyUomId!}
                            </td>
                          <#else>
                            <td align="right"><label>${uiLabelMap.ProductPerUnitPrice}:</label></td>
                            <td align="right">
                              <input class="form-control" type="hidden" name="currencyUomId_o_${rowCount}" value="${currencyUomId!}" />
                              <input class="form-control" type="text" name="unitCost_o_${rowCount}" value="${itemCost}" size="6" maxlength="20" />
                              ${currencyUomId!}
                            </td>
                          </#if>
                        </tr>
                      </table>
                    </td>
                    <td>
                     <label class="checkbox-inline"> <input class="checkbox style-0" type="checkbox" name="_rowSubmit_o_${rowCount}" value="Y" class="selectAllChild"/><span></span></label>
                    </td>
                  </tr>
                  <#assign rowCount = rowCount + 1>
                  </#if>
                </#list>
                <tr>
                  <td colspan="2">
                    <hr />
                  </td>
                </tr>
                <#if rowCount == 0>
                  <tr>
                    <td colspan="2"><label>${uiLabelMap.ProductNoItemsPo} #${purchaseOrder.orderId} ${uiLabelMap.ProductToReceive}.</label></td>
                  </tr>
                  <tr>
                    <td colspan="2">
                      <a href="<@ofbizUrl>ReceiveInventory?facilityId=${requestParameters.facilityId!}</@ofbizUrl>" class="btn btn-primary1">${uiLabelMap.ProductReturnToReceiving}</a>
                    </td>
                  </tr>
                <#else>
                  <tr>
                    <td colspan="2">
                      <a href="javascript:document.selectAllForm.submit();" class="btn btn-primary1">${uiLabelMap.ProductReceiveSelectedProduct}</a>
                    </td>
                  </tr>
                </#if>
              </#if>
            </table>
            <input class="form-control" type="hidden" name="_rowCount" value="${rowCount}"/>
          </form>

        <#-- Initial Screen -->
        <#else>
          <div class="alert alert-info fade in">${uiLabelMap.ProductReceiveItem}</div>
          <form name="selectAllForm" method="post" action="<@ofbizUrl>ReceiveInventory</@ofbizUrl>">
            <input class="form-control" type="hidden" name="facilityId" value="${requestParameters.facilityId!}"/>
            <input class="form-control" type="hidden" name="initialSelected" value="Y"/>
            <table class="basic-table" cellspacing="0">
              <tr>
                <td><label>${uiLabelMap.ProductPurchaseOrderNumber}</label></td>
                <td>
                    <@htmlTemplate.lookupField value="${requestParameters.purchaseOrderId!}" formName="selectAllForm" name="purchaseOrderId" id="purchaseOrderId" fieldFormName="LookupPurchaseOrderHeaderAndShipInfo"/>
                    <span class="tooltip1">${uiLabelMap.ProductLeaveSingleProductReceiving}</span>
                </td>
              </tr>
              <tr>
                <td><label>${uiLabelMap.ProductProductId}</label></td>
                <td>
                  <@htmlTemplate.lookupField value="${requestParameters.productId!}" formName="selectAllForm" name="productId" id="productId" fieldFormName="LookupProduct"/>
                  <span class="tooltip1">${uiLabelMap.ProductLeaveEntirePoReceiving}</span>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>
                  <a href="javascript:document.selectAllForm.submit();" class="btn btn-primary1">${uiLabelMap.ProductReceiveProduct}</a>
                </td>
              </tr>
            </table>
          </form>
        </#if>
