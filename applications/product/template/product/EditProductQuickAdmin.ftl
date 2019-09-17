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
<#assign externalKeyParam = "&amp;externalLoginKey=" + requestAttributes.externalLoginKey!>
<div id="content-main-section" class="leftonly">
<div id="centerdiv" class="no-clear">
<#if product?has_content>
<!-- First some general forms and scripts -->
<form name="removeAssocForm" action="<@ofbizUrl>quickAdminUpdateProductAssoc</@ofbizUrl>">
    <input type="hidden" name="productId" value="${product.productId!}"/>
    <input type="hidden" name="PRODUCT_ID" value="${product.productId!}"/>
    <input type="hidden" name="PRODUCT_ID_TO" value=""/>
    <input type="hidden" name="PRODUCT_ASSOC_TYPE_ID" value="PRODUCT_VARIANT"/>
    <input type="hidden" name="FROM_DATE" value=""/>
    <input type="hidden" name="UPDATE_MODE" value="DELETE"/>
    <input type="hidden" name="useValues" value="true"/>
</form>
<form name="removeSelectable" action="<@ofbizUrl>updateProductQuickAdminDelFeatureTypes</@ofbizUrl>">
    <input type="hidden" name="productId" value="${product.productId!}"/>
    <input type="hidden" name="productFeatureTypeId" value=""/>
</form>
<script language="JavaScript" type="text/javascript">

function removeAssoc(productIdTo, fromDate) {
    if (confirm("Are you sure you want to remove the association of " + productIdTo + "?")) {
        document.removeAssocForm.PRODUCT_ID_TO.value = productIdTo;
        document.removeAssocForm.FROM_DATE.value = fromDate;
        document.removeAssocForm.submit();
    }
}

function removeSelectable(typeString, productFeatureTypeId, productId) {
    if (confirm("Are you sure you want to remove all the selectable features of type " + typeString + "?")) {
        document.removeSelectable.productId.value = productId;
        document.removeSelectable.productFeatureTypeId.value = productFeatureTypeId;
        document.removeSelectable.submit();
    }
}

function doPublish() {
    window.open('/ecommerce/control/product?product_id=${productId!}');
    document.publish.submit();
}

</script>
<div class="row">

<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-3" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.PageTitleEditProductQuickAdmin}</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <!-- Name update section -->
        <form action="<@ofbizUrl>updateProductQuickAdminName</@ofbizUrl>" method="post" style="margin: 0;" name="editProduct">
            <input type="hidden" name="productId" value="${productId!}"/>
            <#if (product.isVirtual)! == "Y">
                <input type="hidden" name="isVirtual" value="Y"/>
            </#if>
            <table cellspacing="0" class="basic-table">
                <tr>
                    <td><label>${productId!}</label></td>
                    <td><input type="text" class="form-control" name="productName" size="40" maxlength="40" value="${product.productName!}"/></td>
                    <td><input type="submit" class="btn btn-primary1" value="${uiLabelMap.ProductUpdateName}"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>
</div>
</div>
</article>
<#if (product.isVirtual)! == "Y">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-4" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductSelectableFeatures}</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <!-- ***************************************************** Selectable features section -->
        <form action="<@ofbizUrl>EditProductQuickAdmin</@ofbizUrl>" method="post" style="margin: 0;" name="selectableFeatureTypeSelector">
            <input type="hidden" name="productId" value="${product.productId!}"/>
            <table cellspacing="0" class="basic-table">
                <tr>
                    <td colspan="2"><label>${uiLabelMap.CommonType}</label>
                        <select class="form-control" name="productFeatureTypeId" onchange="javascript:document.selectableFeatureTypeSelector.submit();">
                            <option value="~~any~~">${uiLabelMap.ProductAnyFeatureType}</option>
                            <#list featureTypes as featureType>
                                <#if (featureType.productFeatureTypeId)! == (productFeatureTypeId)!>
                                    <#assign selected="selected"/>
                                <#else>
                                    <#assign selected=""/>
                                </#if>
                                <option ${selected} value="${featureType.productFeatureTypeId!}">${featureType.get("description",locale)!}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
        <br />
        <form action="<@ofbizUrl>updateProductQuickAdminSelFeat</@ofbizUrl>" method="post" class="smart-form" style="margin: 0;" name="selectableFeature">
        <input type="hidden" name="productId" value="${product.productId!}"/>
        <input type="hidden" name="productFeatureTypeId" value="${(productFeatureTypeId)!}"/>
        <table cellspacing="0" class="basic-table">
           <thead>
            <tr class="header-row">
                <th>${uiLabelMap.ProductProductId}</th>
                <th>&nbsp;</th>
                <th>&nbsp;</th>
                <th>&nbsp;</th>
                <th>${uiLabelMap.ProductSRCH}</th>
                <th>${uiLabelMap.ProductDL}</th>
            </tr>
          </thead>
        <#assign idx=0/>
        <#assign rowClass = "2">
        <#list productAssocs as productAssoc>
            <#assign assocProduct = productAssoc.getRelatedOne("AssocProduct", false)/>
            <tr valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>
                <td nowrap="nowrap">
                <input type="hidden" name="productId${idx}" value="${assocProduct.productId!}"/>
                <a class="buttontext" href="<@ofbizUrl>EditProduct?productId=${assocProduct.productId}</@ofbizUrl>">${assocProduct.productId!}</a></td>
                <td width="100%"><a class="buttontext" href="<@ofbizUrl>EditProduct?productId=${assocProduct.productId}</@ofbizUrl>">${assocProduct.internalName!}</a></td>
                <td colspan="2">
                    <input type="text" class="form-control" name="description${idx}" size="70" maxlength="100" value="${selFeatureDesc[assocProduct.productId]!}"/>
                </td>
                <#assign checked=""/>
                <#if ((assocProduct.smallImageUrl! != "") && (assocProduct.smallImageUrl! == product.smallImageUrl!) &&
                        (assocProduct.smallImageUrl! != "") && (assocProduct.smallImageUrl! == product.smallImageUrl!)) >
                    <#assign checked = "checked='checked'"/>
                </#if>
                <td><label><input type="radio" ${checked} name="useImages" value="${assocProduct.productId}"/></label></td>
                <#assign fromDate = Static["org.apache.ofbiz.base.util.UtilFormatOut"].encodeQueryValue(productAssoc.getTimestamp("fromDate").toString())/>
                <td><a class="buttontext" href="javascript:removeAssoc('${productAssoc.productIdTo}','${fromDate}');">x</a></td>
            </tr>
            <#assign idx = idx + 1/>
            <#-- toggle the row color -->
            <#if rowClass == "2">
                <#assign rowClass = "1">
            <#else>
                <#assign rowClass = "2">
            </#if>
        </#list>
            <tr>
                <td colspan="2">&nbsp;</td>
                <td>
                    <table cellspacing="0" class="basic-table">
                        <#list selectableFeatureTypes as selectableFeatureType>
                        <tr><td><a class="buttontext" href="javascript:removeSelectable('${(selectableFeatureType.get("description",locale))!}','${selectableFeatureType.productFeatureTypeId}','${product.productId}')">x</a>
                            <a class="buttontext" href="<@ofbizUrl>EditProductQuickAdmin?productFeatureTypeId=${(selectableFeatureType.productFeatureTypeId)!}&amp;productId=${product.productId!}</@ofbizUrl>">${(selectableFeatureType.get("description",locale))!}</a></td></tr>
                        </#list>
                    </table>
                </td>
                <td align="right">
                    <table cellspacing="0" class="basic-table">
                        <tr><td><input name="applyToAll" class="btn btn-primary" type="submit" value="${uiLabelMap.ProductAddSelectableFeature}"/></td></tr>
                    </table>
                </td>
            </tr>
            </table>
        </form>
    </div>
</div>
</div>
</div>
</article>
</#if>
<#if (product.isVariant)! == "Y">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-5" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductDistinguishingFeatures}</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <form action="<@ofbizUrl>updateProductQuickAdminDistFeat</@ofbizUrl>" method="post" style="margin: 0;" name="distFeature">
            <input type="hidden" name="productId" value="${product.productId!}"/>
            <table cellspacing="0" class="basic-table">
               <thead>
                <tr class="header-row">
                    <th>${uiLabelMap.ProductProductId}</th>
                </tr>
               </thead>
                <#assign idx=0/>
                <#assign rowClass = "2">
                <#list distinguishingFeatures as distinguishingFeature>
                <tr valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>
                    <td><a href="<@ofbizUrl>quickAdminRemoveProductFeature?productId=${productId}&amp;productFeatureId=${distinguishingFeature.productFeatureId}</@ofbizUrl>" class="buttontext">x</a>&nbsp;
                    ${distinguishingFeature.productFeatureId} ${productFeatureTypeLookup.get(distinguishingFeature.productFeatureId).get("description",locale)}: ${distinguishingFeature.get("description",locale)}
                    &nbsp;
                    </td>
                </tr>
                <#-- toggle the row color -->
                <#if rowClass == "2">
                    <#assign rowClass = "1">
                <#else>
                    <#assign rowClass = "2">
                </#if>
                </#list>
            </table>
        </form>
    </div>
</div>
</div>
</div>
</article>
</#if>
<!-- ***************************************************** end Selectable features section -->
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-6" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductShippingDimensionsAndWeights}</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <!-- ***************************************************** Shipping dimensions section -->
        <form action="<@ofbizUrl>updateProductQuickAdminShipping</@ofbizUrl>" method="post" class="smart-form" name="updateShipping">
            <input type="hidden" name="productId" value="${product.productId!}"/>
            <input type="hidden" name="heightUomId" value="LEN_in"/>
            <input type="hidden" name="widthUomId" value="LEN_in"/>
            <input type="hidden" name="depthUomId" value="LEN_in"/>
            <input type="hidden" name="weightUomId" value="WT_oz"/>
            <table cellspacing="0" class="basic-table">
               <thead>
                <tr class="header-row">
                    <th>${uiLabelMap.ProductProductHeight}</th>
                    <th>${uiLabelMap.ProductProductWidth}</th>
                    <th>${uiLabelMap.ProductProductDepth}</th>
                    <th>${uiLabelMap.ProductWeight}</th>
                    <th>${uiLabelMap.ProductFlOz}</th>
                    <th>${uiLabelMap.ProductML}</th>
                    <th>${uiLabelMap.ProductNtWt}</th>
                    <th>${uiLabelMap.ProductGrams}</th>
                    <th>${uiLabelMap.ProductHZ}</th>
                    <th>${uiLabelMap.ProductST}</th>
                    <th>${uiLabelMap.ProductTD}</th>
                </tr>
               </thead>
        <#if (product.isVirtual)! == "Y">
            <#assign idx=0/>
            <#assign rowClass = "2">
            <#list assocProducts as assocProduct>
                <tr valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>
                    <td><input type="text" name="productHeight${idx}" size="6" maxlength="20" value="${assocProduct.productHeight!}"/></td>
                    <td><input type="text" name="productWidth${idx}" size="6" maxlength="20" value="${assocProduct.productWidth!}"/></td>
                    <td><input type="text" name="productDepth${idx}" size="6" maxlength="20" value="${assocProduct.productDepth!}"/></td>
                    <td><input type="text" name="weight${idx}" size="6" maxlength="20" value="${assocProduct.productWeight!}"/></td>
                    <td><input type="text" name="~floz${idx}" size="6" maxlength="20" value="${featureFloz.get(assocProduct.productId)!}"/></td>
                    <td><input type="text" name="~ml${idx}" size="6" maxlength="20" value="${featureMl.get(assocProduct.productId)!}"/></td>
                    <td><input type="text" name="~ntwt${idx}" size="6" maxlength="20" value="${featureNtwt.get(assocProduct.productId)!}"/></td>
                    <td><input type="text" name="~grams${idx}" size="6" maxlength="20" value="${featureGrams.get(assocProduct.productId)!}"/></td>
                    <td><a class="buttontext" href="<@ofbizUrl>EditProductFeatures?productId=${assocProduct.productId}</@ofbizUrl>">${StringUtil.wrapString(featureHazmat.get(assocProduct.productId)!)}</a></td>
                    <td><a class="buttontext" href="<@ofbizUrl>EditProduct?productId=${assocProduct.productId}</@ofbizUrl>">${StringUtil.wrapString(featureSalesThru.get(assocProduct.productId)!)}</a></td>
                    <td><a class="buttontext" href="<@ofbizUrl>EditProductAssoc?productId=${assocProduct.productId}</@ofbizUrl>">${StringUtil.wrapString(featureThruDate.get(assocProduct.productId)!)}</a></td>
                </tr>
                <#assign idx = idx + 1/>
                <#-- toggle the row color -->
                <#if rowClass == "2">
                    <#assign rowClass = "1">
                <#else>
                    <#assign rowClass = "2">
                </#if>
            </#list>
                <tr>
                    <td colspan="10" align="right"><input name="applyToAll" type="submit" value="${uiLabelMap.ProductApplyToAll}"/>
                    &nbsp;&nbsp;<input name="updateShipping" type="submit" value="${uiLabelMap.ProductUpdateShipping}"/></td>
                </tr>
        <#else>
                <tr>
                    <td><input type="text" name="productHeight" size="6" maxlength="20" value="${product.productHeight!}" /></td>
                    <td><input type="text" name="productWidth" size="6" maxlength="20" value="${product.productWidth!}" /></td>
                    <td><input type="text" name="productDepth" size="6" maxlength="20" value="${product.productDepth!}" /></td>
                    <td><input type="text" name="weight" size="6" maxlength="20" value="${product.productWeight!}" /></td>
                    <td><input type="text" name="~floz" size="6" maxlength="20" value="${floz!}" /></td>
                    <td><input type="text" name="~ml" size="6" maxlength="20" value="${ml!}" /></td>
                    <td><input type="text" name="~ntwt" size="6" maxlength="20" value="${ntwt!}" /></td>
                    <td><input type="text" name="~grams" size="6" maxlength="20" value="${grams!}" /></td>
                    <td><a class="buttontext" href="<@ofbizUrl>EditProductFeatures?productId=${product.productId}</@ofbizUrl>">${StringUtil.wrapString(hazmat!)}</a></td>
                    <td><a class="buttontext" href="<@ofbizUrl>EditProduct?productId=${product.productId}</@ofbizUrl>">${StringUtil.wrapString(salesthru!)}</a></td>
                    <td><a class="buttontext" href="<@ofbizUrl>EditProductAssoc?productId=${product.productId}</@ofbizUrl>">${StringUtil.wrapString(thrudate!)}</a></td>
                </tr>
                <tr>
                    <td colspan="10" align="right"><input type="submit" value="${uiLabelMap.ProductUpdateShipping}" /></td>
                </tr>
        </#if>

            </table>
        </form>
    <!--  **************************************************** end - Shipping dimensions section -->
    </div>
</div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-7" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductStandardFeatures}</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <!--  **************************************************** Standard Features section -->
        <#if addedFeatureTypeIds?has_content || standardFeatureAppls?has_content>
        <table cellspacing="0" class="basic-table">
        <tr>
        <td>
            <#if addedFeatureTypeIds?has_content>
            <form method="post" action="<@ofbizUrl>quickAdminApplyFeatureToProduct</@ofbizUrl>" name="addFeatureById">
            <input type="hidden" name="productId" value="${product.productId!}"/>
            <input type="hidden" name="productFeatureApplTypeId" value="STANDARD_FEATURE"/>
            <input type="hidden" name="fromDate" value="${nowTimestampString}"/>
            <table cellspacing="0" class="basic-table">
              <thead>
                <#assign rowClass = "2">
                <#list addedFeatureTypeIds as addedFeatureTypeId>
                    <tr valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>
                        <th>${addedFeatureTypes.get(addedFeatureTypeId).description}</th>
                        <th>
                            <select class="form-control" name="productFeatureId">
                                <option value="~~any~~">${uiLabelMap.ProductAnyFeatureType}</option>
                            <#list featuresByType.get(addedFeatureTypeId) as feature>
                                <option value="${feature.getString("productFeatureId")}">${feature.description}</option>
                            </#list>
                            </select>
                        </th>
                    </tr>
               </thead>
                    <#-- toggle the row color -->
                    <#if rowClass == "2">
                        <#assign rowClass = "1">
                    <#else>
                        <#assign rowClass = "2">
                    </#if>
                </#list>
                <tr><td colspan="2"><input type="submit" class="btn btn-primary" value="${uiLabelMap.ProductAddFeatures}"/></td></tr>
            </table>
            </form>
            </#if>
        </td>
        <td width="20">&nbsp;</td>
        <td valign="top">
            <#if standardFeatureAppls?has_content>
            <table cellspacing="0" class="basic-table">
                <#assign rowClass = "2">
                <#list standardFeatureAppls as standardFeatureAppl>
                    <#assign featureId = standardFeatureAppl.productFeatureId/>
                    <tr valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>
                        <td colspan="2">
                          <form name="quickAdminRemoveFeature_${standardFeatureAppl_index}" action="<@ofbizUrl>quickAdminRemoveFeatureFromProduct</@ofbizUrl>" method="post">
                            <input type="hidden" name="productId" value="${standardFeatureAppl.productId!}" />
                            <input type="hidden" name="productFeatureId" value="${featureId!}" />
                            <input type="hidden" name="fromDate" value="${(standardFeatureAppl.fromDate)!}" />
                            <a href="javascript:document.quickAdminRemoveFeature_${standardFeatureAppl_index}.submit();" class="buttontext">x</a>
                            ${productFeatureTypeLookup.get(featureId).description}: ${standardFeatureLookup.get(featureId).description}
                          </form>
                        </td>
                    </tr>
                    <#-- toggle the row color -->
                    <#if rowClass == "2">
                        <#assign rowClass = "1">
                    <#else>
                        <#assign rowClass = "2">
                    </#if>
                </#list>
            </table>
            </#if>
        </td>
        </tr>
        </table>
        <br />
        </#if>
        <form action="<@ofbizUrl>EditProductQuickAdmin</@ofbizUrl>">
        <input type="hidden" name="productFeatureTypeId" value="${(productFeatureTypeId)!}"/>
        <input type="hidden" name="productId" value="${product.productId!}"/>
        <table cellspacing="0" class="basic-table">
            <tr>
                <td><label>${uiLabelMap.ProductFeatureTypes}</label></td>
                <td>
                    <select class="form-control" multiple="multiple" name="addFeatureTypeId">
                        <#list featureTypes as featureType>
                            <option value="${featureType.productFeatureTypeId!}">${featureType.get("description",locale)!}</option>
                        </#list>
                    </select>
                </td>
                <td><input type="submit" class="btn btn-primary1" value="${uiLabelMap.ProductAddFeatureType}"/></td>
            </tr>
        </table>
        </form>
        <!--  **************************************************** end - Standard Features section -->
    </div>
</div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-8" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductCategories}</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <!--  **************************************************** Categories section -->
        <form action="<@ofbizUrl>quickAdminAddCategories</@ofbizUrl>">
            <input type="hidden" name="fromDate" value="${nowTimestampString}"/>
            <input type="hidden" name="productId" value="${product.productId!}"/>
            <table cellspacing="0" class="basic-table">
              <tr>
              <td>
                  <table cellspacing="0" class="basic-table">
                      <tr>
                          <td>
                              <select multiple="multiple" class="form-control" name="categoryId">
                                  <#list allCategories as category>
                                      <option value="${category.productCategoryId!}">${category.description!} ${category.productCategoryId}</option>
                                  </#list>
                              </select>&nbsp;
                          </td>
                      </tr>
                  </table>
              </td>
              </tr>
              <tr>
                  <td colspan="2"><input type="submit" class="btn btn-primary1" value="${uiLabelMap.ProductUpdateCategories}"/></td>
              </tr>
            </table>
        </form>
        <table>
          <tr>
            <td valign="top">
                <table cellspacing="0" class="basic-table">
                    <thead>
                    <#assign rowClass = "2">
                    <#list productCategoryMembers as prodCatMemb>
                        <#assign prodCat = prodCatMemb.getRelatedOne("ProductCategory", false)/>
                        <tr valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>                        
                            <td colspan="2">
                              <form name="quickAdminRemoveProductFromCategory_${prodCatMemb_index}" action="<@ofbizUrl>quickAdminRemoveProductFromCategory</@ofbizUrl>" method="post">
                                <input type="hidden" name="productId" value="${prodCatMemb.productId!}" />
                                <input type="hidden" name="productCategoryId" value="${prodCatMemb.productCategoryId}" />
                                <input type="hidden" name="fromDate" value="${(prodCatMemb.fromDate)!}" />
                                <a href="javascript:document.quickAdminRemoveProductFromCategory_${prodCatMemb_index}.submit();" class="buttontext">x</a>
                                ${prodCat.description!} ${prodCat.productCategoryId}
                              </form>
                            </td>
                        </tr>
                     </thead>
                        <#-- toggle the row color -->
                        <#if rowClass == "2">
                            <#assign rowClass = "1">
                        <#else>
                            <#assign rowClass = "2">
                        </#if>
                    </#list>
                </table>
            </td>
          </tr>
        </table>
        <!--  **************************************************** end - Categories section -->
    </div>
</div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-9" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductPublishAndView}</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
    <!--  **************************************************** publish section -->
    <#if (showPublish == "true")>
        <form action="<@ofbizUrl>quickAdminAddCategories</@ofbizUrl>" class="smart-form" name="publish">
        <input type="hidden" name="productId" value="${product.productId!}"/>
        <input type="hidden" name="categoryId" value="${allCategoryId!}"/>
        <table cellspacing="0" class="basic-table">
            <tr>
                <td>
                    <@htmlTemplate.renderDateTimeField name="fromDate" event="" action="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" value="" size="25" maxlength="30" id="fromDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                    <input type="button" class="btn btn-primary1" value="${uiLabelMap.ProductPublishAndView}" onclick="doPublish();"/>
                </td>
            </tr>
        </table>
        </form>
    <#else>
        <form action="<@ofbizUrl>quickAdminUnPublish</@ofbizUrl>" class="smart-form" name="unpublish">
        <input type="hidden" name="productId" value="${product.productId!}"/>
        <input type="hidden" name="productCategoryId" value="${allCategoryId!}"/>
        <table cellspacing="0" class="basic-table">
            <tr>
                <td>
                    <@htmlTemplate.renderDateTimeField name="thruDate" event="" action="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" value="" size="25" maxlength="30" id="thruDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                    <input type="submit" class="btn btn-primary1" value="${uiLabelMap.ProductRemoveFromSite}"/>
                </td>
            </tr>
        </table>
        </form>
    </#if>
    <!--  **************************************************** end - publish section -->
    </div>
</div>
</div>
</div>
</article>
</div>
  <#else>
    <div class="alert alert-info fade in">${uiLabelMap.ProductProductNotFound} ${productId!}</div>
  </#if>
</div>
</div>