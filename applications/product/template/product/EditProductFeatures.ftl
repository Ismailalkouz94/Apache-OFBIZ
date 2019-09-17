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
<#if productId??>
<div class="row">

<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-3" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.PageTitleEditProductFeatures}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-body">
    <form method="post" class="smart-form" action="<@ofbizUrl>UpdateFeatureToProductApplication</@ofbizUrl>" name="selectAllForm">
      <input type="hidden" name="_useRowSubmit" value="Y"/>
      <input type="hidden" name="_checkGlobalScope" value="Y"/>
      <input type="hidden" name="productId" value="${productId}"/>
      <table cellspacing="0" class="table table-bordered table-striped">
        <thead>
        <tr class="header-row">
          <th><b>${uiLabelMap.CommonId}</b></th>
          <th><b>${uiLabelMap.CommonDescription}</b></th>
          <th><b>${uiLabelMap.ProductUomId}</b></th>
          <th><b>${uiLabelMap.ProductType}</b></th>
          <th><b>${uiLabelMap.ProductCategory}</b></th>
          <th><b>${uiLabelMap.CommonFromDate}</b></th>
          <th><b>${uiLabelMap.ProductThruDateAmountSequenceApplicationType}</b></th>
          <th><b><label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="selectAll" value="${uiLabelMap.CommonY}" class="selectAll" onclick="highlightAllRows(this, 'productFeatureId_tableRow_', 'selectAllForm');"/><span>${uiLabelMap.CommonAll}</span></label></b></th>
        </tr>
       </thead>
  <#assign rowClass = "2">
  <#list productFeatureAndAppls as productFeatureAndAppl>
    <#if productFeatureAndAppl.uomId??>
        <#assign curProductFeatureUom = delegator.findOne("Uom",{"uomId",productFeatureAndAppl.uomId}, true)>
    </#if>
    <#assign curProductFeatureType = productFeatureAndAppl.getRelatedOne("ProductFeatureType", true)>
    <#assign curProductFeatureApplType = productFeatureAndAppl.getRelatedOne("ProductFeatureApplType", true)>
    <#assign curProductFeatureCategory = (productFeatureAndAppl.getRelatedOne("ProductFeatureCategory", true)!)>
        <tr id="productFeatureId_tableRow_${productFeatureAndAppl_index}" valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>
          <td>
          <input type="hidden" name="productId_o_${productFeatureAndAppl_index}" value="${(productFeatureAndAppl.productId)!}" />
          <input type="hidden" name="productFeatureId_o_${productFeatureAndAppl_index}" value="${(productFeatureAndAppl.productFeatureId)!}" />
          <input type="hidden" name="fromDate_o_${productFeatureAndAppl_index}" value="${(productFeatureAndAppl.fromDate)!}" />
          <a href="<@ofbizUrl>EditFeature?productFeatureId=${(productFeatureAndAppl.productFeatureId)!}</@ofbizUrl>" class="buttontext">
              ${(productFeatureAndAppl.productFeatureId)!}</a></td>
          <td>${(productFeatureAndAppl.get("description",locale))!}</td>
          <td><#if productFeatureAndAppl.uomId??>${curProductFeatureUom.abbreviation!}</#if></td>
          <td>${(curProductFeatureType.get("description",locale))?default((productFeatureAndAppl.productFeatureTypeId)!)}</td>
          <td><a href="<@ofbizUrl>EditFeatureCategoryFeatures?productFeatureCategoryId=${(productFeatureAndAppl.productFeatureCategoryId)!}&amp;productId=${(productFeatureAndAppl.productId)!}</@ofbizUrl>" class="buttontext">
              ${(curProductFeatureCategory.description)!}
              [${(productFeatureAndAppl.productFeatureCategoryId)!}]</a></td>
    <#assign hasntStarted = false>
    <#if (productFeatureAndAppl.getTimestamp("fromDate"))?? && Static["org.apache.ofbiz.base.util.UtilDateTime"].nowTimestamp().before(productFeatureAndAppl.getTimestamp("fromDate"))> <#assign hasntStarted = true></#if>
          <td <#if hasntStarted> style='color: red;'</#if>>${(productFeatureAndAppl.fromDate)!}</td>
          <td>
    <#assign hasExpired = false>
    <#if (productFeatureAndAppl.getTimestamp("thruDate"))?? && Static["org.apache.ofbiz.base.util.UtilDateTime"].nowTimestamp().after(productFeatureAndAppl.getTimestamp("thruDate"))> <#assign hasExpired = true></#if>
            <#if hasExpired><#assign class="alert"><#else><#assign class=""></#if>
            <@htmlTemplate.renderDateTimeField name="thruDate_o_${productFeatureAndAppl_index}" event="" action="" className="${class!}" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" value="${(productFeatureAndAppl.thruDate)!}" size="25" maxlength="30" id="thruDate_o_${productFeatureAndAppl_index}" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
            <input type="text" size='6' name='amount_o_${productFeatureAndAppl_index}' value='${(productFeatureAndAppl.amount)!}' />
            <input type="text" size='5' name='sequenceNum_o_${productFeatureAndAppl_index}' value='${(productFeatureAndAppl.sequenceNum)!}' />
            <select name='productFeatureApplTypeId_o_${productFeatureAndAppl_index}' size="1">
    <#if (productFeatureAndAppl.productFeatureApplTypeId)??>
              <option value='${(productFeatureAndAppl.productFeatureApplTypeId)!}'><#if curProductFeatureApplType??> ${(curProductFeatureApplType.get("description",locale))!} <#else> [${productFeatureAndAppl.productFeatureApplTypeId}]</#if></option>
              <option value='${productFeatureAndAppl.productFeatureApplTypeId}'> </option>
    </#if>
      <#list productFeatureApplTypes as productFeatureApplType>
              <option value='${(productFeatureApplType.productFeatureApplTypeId)!}'>${(productFeatureApplType.get("description",locale))!} </option>
    </#list>
            </select>
          </td>
          <td align="right">
            <input type="checkbox" name="_rowSubmit_o_${productFeatureAndAppl_index}" value="Y" onclick="highlightRow(this,'productFeatureId_tableRow_${productFeatureAndAppl_index}');" />
          </td>
          <td>
            <a href="javascript:document.RemoveFeatureFromProduct_o_${productFeatureAndAppl_index}.submit()" class="buttontext">${uiLabelMap.CommonDelete}</a>
          </td>
        </tr>
    <#-- toggle the row color -->
    <#if rowClass == "2">
      <#assign rowClass = "1">
    <#else>
      <#assign rowClass = "2">
    </#if>
  </#list>

        <tr>
          <td colspan="8" align="center">
            <input type="hidden" name="_rowCount" value="${productFeatureAndAppls.size()}"/>
            <input type="submit" class="btn btn-primary" value='${uiLabelMap.CommonUpdate}'/>
          </td>
        </tr>
      </table>
    </form>
  <#list productFeatureAndAppls as productFeatureAndAppl>
    <form name= "RemoveFeatureFromProduct_o_${productFeatureAndAppl_index}" class="smart-form" method= "post" action= "<@ofbizUrl>RemoveFeatureFromProduct</@ofbizUrl>">
      <input type= "hidden" name= "productId" value= "${(productFeatureAndAppl.productId)!}"/>
      <input type= "hidden" name= "productFeatureId" value= "${(productFeatureAndAppl.productFeatureId)!}"/>
      <input type= "hidden" name= "fromDate" value= "${(productFeatureAndAppl.fromDate)!}"/>
    </form>
  </#list>
  </div>
</div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-4" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductAddProductFeatureFromCategory}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-body">
    <form method="post" class="smart-form" action="<@ofbizUrl>ApplyFeaturesFromCategory</@ofbizUrl>" style='margin: 0;'>
      <input type="hidden" name="productId" value="${productId}"/>
      <select name='productFeatureCategoryId' size="1">
        <option value='' selected="selected">${uiLabelMap.ProductChooseFeatureCategory}</option>
  <#list productFeatureCategories as productFeatureCategory>
        <option value='${(productFeatureCategory.productFeatureCategoryId)!}'>${(productFeatureCategory.description)!} [${(productFeatureCategory.productFeatureCategoryId)!}]</option>
  </#list>
      </select>
      <select name='productFeatureGroupId' size="1">
        <option value='' selected="selected">${uiLabelMap.ProductChooseFeatureGroup}</option>
  <#list productFeatureGroups as productFeatureGroup>
        <option value='${(productFeatureGroup.productFeatureGroupId)!}'>${(productFeatureGroup.description)!} [${(productFeatureGroup.productFeatureGroupId)!}]</option>
  </#list>
      </select></br>
      <label>${uiLabelMap.ProductFeatureApplicationType}: </label>
      <select name='productFeatureApplTypeId' size="1">
  <#list productFeatureApplTypes as productFeatureApplType>
        <option value='${(productFeatureApplType.productFeatureApplTypeId)!}'
    <#if (productFeatureApplType.productFeatureApplTypeId?? && product?? && product.isVirtual == 'Y' && productFeatureApplType.productFeatureApplTypeId =="SELECTABLE_FEATURE")>selected="selected"</#if>
    <#if (productFeatureApplType.productFeatureApplTypeId?? && product?? && product.isVirtual == 'N' && productFeatureApplType.productFeatureApplTypeId! =="STANDARD_FEATURE")>selected="selected"</#if>
            >${(productFeatureApplType.get("description",locale))!} </option>
  </#list>
      </select>
      <input type="submit" class="btn btn-primary1" value='${uiLabelMap.CommonAdd}'/>
    </form>
  </div>
</div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-5" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductAddProductFeatureTypeId}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-body">
    <form method="post" class="smart-form" action="<@ofbizUrl>ApplyFeatureToProductFromTypeAndCode</@ofbizUrl>" name='addFeatureByTypeIdCode'>
      <input type="hidden" name="productId" value="${productId}"/>
      <table class="basic-table">
      <tr>
      <td><label>${uiLabelMap.ProductFeatureType} </label></td>
      <td><select name='productFeatureTypeId' size="1">
  <#list productFeatureTypes as productFeatureType>
        <option value='${(productFeatureType.productFeatureTypeId)!}'>${(productFeatureType.get("description",locale))!} </option>
  </#list>
      </select></td>
      </tr>
      <tr>
      <td><label>${uiLabelMap.CommonIdCode} </label></td>
      <td><input type="text" size='10' name='idCode' value=''/></td>
      </tr>
      <tr>
      <td><label>${uiLabelMap.ProductFeatureApplicationType} </label></td>
      <td><select name='productFeatureApplTypeId' size="1">
  <#list productFeatureApplTypes as productFeatureApplType>
        <option value='${(productFeatureApplType.productFeatureApplTypeId)!}'
    <#if (productFeatureApplType.productFeatureApplTypeId?? && product?? && product.isVirtual == 'Y' && productFeatureApplType.productFeatureApplTypeId =="SELECTABLE_FEATURE")>selected="selected"</#if>
    <#if (productFeatureApplType.productFeatureApplTypeId?? && product?? && product.isVirtual == 'N' && productFeatureApplType.productFeatureApplTypeId =="STANDARD_FEATURE")>selected="selected"</#if>
            >${(productFeatureApplType.get("description",locale))!} </option>
  </#list>
      </select>
      </td>
      </tr>
      <tr>
      <td><label>${uiLabelMap.CommonFrom} </label></td>
      <td><@htmlTemplate.renderDateTimeField name="fromDate" event="" action="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" value="" size="25" maxlength="30" id="fromDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/></td>
      </tr>
      <tr>
      <td><label>${uiLabelMap.CommonThru} </label></td>
      <td><@htmlTemplate.renderDateTimeField name="thruDate" event="" action="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" value="" size="25" maxlength="30" id="thruDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/></td>
      </tr>
      <tr>
      <td><label>${uiLabelMap.CommonSequence} </label></td>
      <td><input type="text" size='5' name='sequenceNum'/></td>
      </tr>
      </table>
      <input type="submit" class="btn btn-primary1" value="${uiLabelMap.CommonAdd}"/>
    </form>
  </div>
</div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-6" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductAddProductFeatureID}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-body">
    <form method="post" class="smart-form" action="<@ofbizUrl>ApplyFeatureToProduct</@ofbizUrl>" name="addFeatureById">
      <input type="hidden" name="productId" value="${productId}"/>
      <table class="basic-table">
      <tr>
      <td><label>${uiLabelMap.CommonId} </label></td>
      <td>
        <@htmlTemplate.lookupField formName="addFeatureById" name="productFeatureId" id="productFeatureId" fieldFormName="LookupProductFeature"/>
      </td>
      </tr>
      <tr>
      <td><label>${uiLabelMap.ProductFeatureApplicationType}</label></td>
      <td><select name="productFeatureApplTypeId" size="1">
  <#list productFeatureApplTypes as productFeatureApplType>
        <option value='${(productFeatureApplType.productFeatureApplTypeId)!}'
    <#if (productFeatureApplType.productFeatureApplTypeId?? && product?? && product.isVirtual == 'Y' && productFeatureApplType.productFeatureApplTypeId =="SELECTABLE_FEATURE")>selected="selected"</#if>
    <#if (productFeatureApplType.productFeatureApplTypeId?? && product?? && product.isVirtual == 'N' && productFeatureApplType.productFeatureApplTypeId =="STANDARD_FEATURE")>selected="selected"</#if>
            >${(productFeatureApplType.get("description",locale))!} </option>
  </#list>
      </select>
      </td>
      </tr>
      <tr>
      <td><label>${uiLabelMap.CommonFrom}  </label></td>
      <td><@htmlTemplate.renderDateTimeField name="fromDate" event="" action="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" value="${nowTimestamp}" size="25" maxlength="30" id="fromDate2" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/></td>
      </tr>
      <tr>
      <td><label>${uiLabelMap.CommonThru} </label></td>
      <td><@htmlTemplate.renderDateTimeField name="thruDate" event="" action="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" value="" size="25" maxlength="30" id="thruDate2" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/></td>
      </tr>
      <tr>
      <td><label>${uiLabelMap.CommonSequence}  </label></td>
      <td><input type="text" size="5" name="sequenceNum"/></td>
      </tr>
      </table>
      <input type="submit" class="btn btn-primary1" value="${uiLabelMap.CommonAdd}"/>
    </form>
  </div>
</div>
</div>
</div>
</article>

</div>
</#if>
