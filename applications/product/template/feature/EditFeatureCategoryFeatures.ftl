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
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductEditFeaturesForFeatureCategory} "${(curProductFeatureCategory.description)!}"</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <div class="button-bar">
         <ul  class="nav nav-tabs1">
         <li> <a class="inside-menu" href="<@ofbizUrl>CreateFeature?productFeatureCategoryId=${productFeatureCategoryId!}</@ofbizUrl>" class="buttontext create">
             ${uiLabelMap.ProductCreateNewFeature}</a></li>
         </ul>
        </div>
        <br/>
        <form action="<@ofbizUrl>QuickAddProductFeatures</@ofbizUrl>" method="post">
          <div>
           <label> ${uiLabelMap.CommonAdd}</label>
            <input class="form-control" type="text" name="featureNum" value="1" size="3" />
            <label>${uiLabelMap.ProductAddFeatureToCategory}</label>
            <input class="btn btn-primary" type="submit" value="${uiLabelMap.CommonCreate}" />
          </div>
          <input type="hidden" name="productFeatureCategoryId" value="${productFeatureCategoryId}" />
        </form>
        <br />
    </div>
</div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductProductFeatureMaintenance}</h2>
</header>
<div role="content" style="overflow:auto">
<div class="screenlet">
    <div class="screenlet-body">
        <#if (listSize > 0)>
            <#if productId?has_content>
              <#assign productString = "&amp;productId=" + productId>
            </#if>
            <table border="0" width="100%" cellpadding="2">
                <tr>
                <td align="right">
                    <label>
                    <b>
                    <#if (viewIndex > 0)>
                    <a href="<@ofbizUrl>EditFeatureCategoryFeatures?productFeatureCategoryId=${productFeatureCategoryId!}&amp;VIEW_SIZE=${viewSize}&amp;VIEW_INDEX=${viewIndex-1}${productString!}</@ofbizUrl>" class="buttontext">[${uiLabelMap.CommonPrevious}]</a> |
                    </#if>
                    ${lowIndex+1} - ${highIndex} ${uiLabelMap.CommonOf} ${listSize}
                    <#if (listSize > highIndex)>
                    | <a href="<@ofbizUrl>EditFeatureCategoryFeatures?productFeatureCategoryId=${productFeatureCategoryId!}&amp;VIEW_SIZE=${viewSize}&amp;VIEW_INDEX=${viewIndex+1}${productString!}</@ofbizUrl>" class="buttontext">[${uiLabelMap.CommonNext}]</a>
                    </#if>
                    </b>
                    </label>
                </td>
                </tr>
            </table>
        </#if>
        <br />
        <form method='post' action='<@ofbizUrl>UpdateProductFeatureInCategory</@ofbizUrl>' name="selectAllForm">
        <input type="hidden" name="_useRowSubmit" value="Y" />
        <input type="hidden" name="_checkGlobalScope" value="N" />
        <input type="hidden" name="productFeatureCategoryId" value="${productFeatureCategoryId}" />
        <table cellspacing="0" class="table table-bordered table-striped table-condensed table-hover smart-form has-tickbox">
              <tr class="header-row">
                <th>${uiLabelMap.CommonId}</th>
                <th>${uiLabelMap.CommonDescription}</th>
                <th>${uiLabelMap.ProductFeatureType}</th>
                <th>${uiLabelMap.ProductFeatureCategory}</th>
                <th>${uiLabelMap.ProductUnitOfMeasureId}</th>
                <th>${uiLabelMap.ProductQuantity}</th>
                <th>${uiLabelMap.ProductAmount}</th>
                <th>${uiLabelMap.ProductIdSeqNum}</th>
                <th>${uiLabelMap.ProductIdCode}</th>
                <th>${uiLabelMap.ProductAbbrev}</th>
                <th align="right">${uiLabelMap.CommonAll}<input type="checkbox" name="selectAll" value="${uiLabelMap.CommonY}" class="selectAll" onclick="highlightAllRows(this, 'productFeatureId_tableRow_', 'selectAllForm');" /></th>
             </tr>
        <#if (listSize > 0)>
            <#assign rowCount = 0>
            <#assign rowClass = "2">
            <#list productFeatures as productFeature>
            <#assign curProductFeatureType = productFeature.getRelatedOne("ProductFeatureType", true)>
            <tr id="productFeatureId_tableRow_${rowCount}" valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>
              <td><input class="form-control" type="hidden" name="productFeatureId_o_${rowCount}" value="${productFeature.productFeatureId}" />
              <a href="<@ofbizUrl>EditFeature?productFeatureId=${productFeature.productFeatureId}</@ofbizUrl>" class="buttontext">${productFeature.productFeatureId}</a></td>
              <td><input class="form-control" style="width:150px" type="text" size='15' name="description_o_${rowCount}" value="${productFeature.description}" /></td>
              <td><select class="form-control" style="width:150px;;height:32px" name='productFeatureTypeId_o_${rowCount}' size="1">
                <#if productFeature.productFeatureTypeId?has_content>
                  <option value='${productFeature.productFeatureTypeId}'><#if curProductFeatureType??>${curProductFeatureType.get("description",locale)!}<#else> [${productFeature.productFeatureTypeId}]</#if></option>
                  <option value='${productFeature.productFeatureTypeId}'>---</option>
                </#if>
                <#list productFeatureTypes as productFeatureType>
                  <option value='${productFeatureType.productFeatureTypeId}'>${productFeatureType.get("description",locale)!}</option>
                </#list>
              </select></td>
              <td><select class="form-control" style="width:150px;height:32px" name='productFeatureCategoryId_o_${rowCount}' size="1">
                <#if productFeature.productFeatureCategoryId?has_content>
                  <#assign curProdFeatCat = productFeature.getRelatedOne("ProductFeatureCategory", false)>
                  <option value='${productFeature.productFeatureCategoryId}'>${(curProdFeatCat.description)!} [${productFeature.productFeatureCategoryId}]</option>
                  <option value='${productFeature.productFeatureCategoryId}'>---</option>
                </#if>
                <#list productFeatureCategories as productFeatureCategory>
                  <option value='${productFeatureCategory.productFeatureCategoryId}'>${productFeatureCategory.get("description",locale)!} [${productFeatureCategory.productFeatureCategoryId}]</option>
                </#list>
              </select></td>
              <td><input class="form-control" style="width:150px" type="text" size='10' name="uomId_o_${rowCount}" value="${productFeature.uomId!}" /></td>
              <td><input class="form-control" style="width:150px" type="text" size='5' name="numberSpecified_o_${rowCount}" value="${productFeature.numberSpecified!}" /></td>
              <td><input class="form-control" style="width:150px" type="text" size='5' name="defaultAmount_o_${rowCount}" value="${productFeature.defaultAmount!}" /></td>
              <td><input class="form-control" style="width:150px" type="text" size='5' name="defaultSequenceNum_o_${rowCount}" value="${productFeature.defaultSequenceNum!}" /></td>
              <td><input class="form-control" style="width:150px" type="text" size='5' name="idCode_o_${rowCount}" value="${productFeature.idCode!}" /></td>
              <td><input class="form-control" style="width:150px" type="text" size='5' name="abbrev_o_${rowCount}" value="${productFeature.abbrev!}" /></td>
              <td><input type="checkbox" name="_rowSubmit_o_${rowCount}" value="Y" onclick="highlightRow(this,'productFeatureId_tableRow_${rowCount}');" /></td>
            </tr>
            <#assign rowCount = rowCount + 1>
            <#-- toggle the row color -->
            <#if rowClass == "2">
              <#assign rowClass = "1">
            <#else>
              <#assign rowClass = "2">
            </#if>
            </#list>
            <tr><td colspan="11" align="center">
            <input type="hidden" name="_rowCount" value="${rowCount}" />
            <input type="submit" class="btn btn-primary1" value='${uiLabelMap.CommonUpdate}'/></td></tr>
        </#if>
        </table>
        </form>
    </div>
</div>
</div>
</div>
</article>
</div>
