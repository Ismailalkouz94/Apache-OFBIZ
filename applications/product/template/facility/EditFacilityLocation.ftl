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

<header>
<h2>${title}</h2>
</header>


<div role="content">

<#if facilityId?? && locationSeqId??>
  <div class="button-bar">
    <a href="<@ofbizUrl>EditFacility</@ofbizUrl>" class="buttontext">${uiLabelMap.ProductNewFacility}</a>
    <a href="<@ofbizUrl>EditFacilityLocation?facilityId=${facilityId!}</@ofbizUrl>" class="buttontext">${uiLabelMap.ProductNewFacilityLocation}</a>
    <a href="<@ofbizUrl>EditInventoryItem?facilityId=${facilityId}&amp;locationSeqId=${locationSeqId}</@ofbizUrl>" class="buttontext">${uiLabelMap.ProductNewInventoryItem}</a>
    <#assign latestGeoPoint= Static["org.apache.ofbiz.common.geo.GeoWorker"].findLatestGeoPoint(delegator, "FacilityLocationAndGeoPoint", "facilityId", facilityId, "locationSeqId", locationSeqId)!/>
    <#if latestGeoPoint?has_content>
      <a href="<@ofbizUrl>FacilityLocationGeoLocation?facilityId=${facilityId}&amp;locationSeqId=${locationSeqId}</@ofbizUrl>" class="buttontext">${uiLabelMap.CommonGeoLocation}</a>
    </#if>
  </div>
</#if>

<#if facilityId?? && !(facilityLocation??)>
    <form action="<@ofbizUrl>CreateFacilityLocation</@ofbizUrl>" method="post">
    <input class="form-control" type="hidden" name="facilityId" value="${facilityId}" />
    <table class="basic-table" cellspacing="0">
<#elseif facilityLocation??>
    <form action="<@ofbizUrl>UpdateFacilityLocation</@ofbizUrl>" method="post">
    <input class="form-control" type="hidden" name="facilityId" value="${facilityId!}" />
    <input class="form-control" type="hidden" name="locationSeqId" value="${locationSeqId}" />
    <table class="basic-table" cellspacing="0">
    <tr>
        <td><label>${uiLabelMap.ProductFacilityId}</label></td>
        <td>${facilityId!}</td>
    </tr>
    <tr>
        <td><label>${uiLabelMap.ProductLocationSeqId}</label></td>
        <td>${locationSeqId}</td>
    </tr>
<#else>
    <div class="alert alert-info fade in">${uiLabelMap.ProductNotCreateLocationFacilityId}</div>
</#if>

<#if facilityId??>
    <tr>
        <td><label>${uiLabelMap.ProductType}</label></td>
        <td>
            <select class="form-control" name="locationTypeEnumId">
                <#if (facilityLocation.locationTypeEnumId)?has_content>
                    <#assign locationTypeEnum = facilityLocation.getRelatedOne("TypeEnumeration", true)!>
                    <option value="${facilityLocation.locationTypeEnumId}">${(locationTypeEnum.get("description",locale))?default(facilityLocation.locationTypeEnumId)}</option>
                    <option value="${facilityLocation.locationTypeEnumId}">----</option>
                </#if>
                <#list locationTypeEnums as locationTypeEnum>
                    <option value="${locationTypeEnum.enumId}">${locationTypeEnum.get("description",locale)}</option>
                </#list>
            </select>
        </td>
    </tr>
    <tr>
        <td><label>${uiLabelMap.CommonArea}</label></td>
        <td><input class="form-control" type="text" name="areaId" value="${(facilityLocation.areaId)!}" size="19" maxlength="20" /></td>
    </tr>
    <tr>
       <td><label>${uiLabelMap.ProductAisle}</label></td>
        <td><input class="form-control" type="text" name="aisleId" value="${(facilityLocation.aisleId)!}" size="19" maxlength="20" /></td>
    </tr>
    <tr>
        <td><label>${uiLabelMap.ProductSection}</label></td>
        <td><input class="form-control" type="text" name="sectionId" value="${(facilityLocation.sectionId)!}" size="19" maxlength="20" /></td>
    </tr>
    <tr>
        <td><label>${uiLabelMap.ProductLevel}</label></td>
        <td><input class="form-control" type="text" name="levelId" value="${(facilityLocation.levelId)!}" size="19" maxlength="20" /></td>
    </tr>
    <tr>
        <td><label>${uiLabelMap.ProductPosition}</label></td>
        <td><input class="form-control" type="text" name="positionId" value="${(facilityLocation.positionId)!}" size="19" maxlength="20" /></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <#if locationSeqId??>
          <td><input class="btn btn-primary1" type="submit" value="${uiLabelMap.CommonUpdate}" /></td>
        <#else>
          <td><input class="btn btn-primary1" type="submit" value="${uiLabelMap.CommonSave}" /></td>
        </#if>
    </tr>
  </table>
  </form>
</div>
</div>
</article>
  <#if locationSeqId??>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">

<header>
<h2>${uiLabelMap.ProductLocationProduct}</h2>
</header>


<div role="content">

  <div class="screenlet">
    <div class="screenlet-body">
        <#-- ProductFacilityLocation stuff -->
        <table class="table table-bordered table-striped" cellspacing="0">
        <thead>
        <tr class="header-row">
            <td>${uiLabelMap.ProductProduct}</td>
            <td>${uiLabelMap.ProductMinimumStockAndMoveQuantity}</td>
        </tr>
       </thead>
        <#list productFacilityLocations! as productFacilityLocation>
            <#assign product = productFacilityLocation.getRelatedOne("Product", false)!>
            <tr>
                <td><#if product??>${(product.internalName)!}</#if>[${productFacilityLocation.productId}]</td>
                <td>
                    <form method="post" action="<@ofbizUrl>updateProductFacilityLocation</@ofbizUrl>" id="lineForm${productFacilityLocation_index}">
                        <input class="form-control" type="hidden" name="productId" value="${(productFacilityLocation.productId)!}"/>
                        <input class="form-control" type="hidden" name="facilityId" value="${(productFacilityLocation.facilityId)!}"/>
                        <input class="form-control" type="hidden" name="locationSeqId" value="${(productFacilityLocation.locationSeqId)!}"/>
                        <input class="form-control" type="text" size="10" name="minimumStock" value="${(productFacilityLocation.minimumStock)!}"/>
                        <input class="form-control" type="text" size="10" name="moveQuantity" value="${(productFacilityLocation.moveQuantity)!}"/>
                        <input class="form-control" type="submit" value="${uiLabelMap.CommonUpdate}"/>
                        <a href="javascript:document.getElementById('lineForm${productFacilityLocation_index}').action='<@ofbizUrl>deleteProductFacilityLocation</@ofbizUrl>';document.getElementById('lineForm${productFacilityLocation_index}').submit();" class="buttontext111">${uiLabelMap.CommonDelete}</a>
                    </form>
                </td>
            </tr>
        </#list>
        </table>
    </div>
  </div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">

<header>
<h2>${uiLabelMap.ProductAddProduct}</h2>
</header>

<div role="content">
  <div class="screenlet">
    <div class="screenlet-body">
        <form method="post" action="<@ofbizUrl>createProductFacilityLocation</@ofbizUrl>" style="margin: 0;" name="createProductFacilityLocationForm">
            <input class="form-control" type="hidden" name="facilityId" value="${facilityId!}" />
            <input class="form-control" type="hidden" name="locationSeqId" value="${locationSeqId!}" />
            <input class="form-control" type="hidden" name="useValues" value="true" />
            <@htmlTemplate.lookupField formName="createProductFacilityLocationForm" name="productId" id="productId" fieldFormName="LookupProduct"/>
            <label>${uiLabelMap.ProductMinimumStock}</label> <input class="form-control" type="text" size="10" name="minimumStock" />
            <label>${uiLabelMap.ProductMoveQuantity}</label> <input class="form-control" type="text" size="10" name="moveQuantity" />
            <input type="submit" class="btn btn-primary1" value="${uiLabelMap.CommonAdd}" />
        </form>
    </div>
  </div>
  </#if>
</#if>
</div>
</div>
</article>
</div>
</section>