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
<form method="post" action="<@ofbizUrl>attachProductFeaturesToCategory</@ofbizUrl>" name="attachProductFeaturesToCategory">
    <input type="hidden" name="productCategoryId" value="${productCategoryId!}" />
</form>
<a class="button1 facebook" href="javascript:document.attachProductFeaturesToCategory.submit()" class="buttontext">${uiLabelMap.ProductFeatureCategoryAttach}</a>

<#if productCategoryId?? && productCategory??>
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-3" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.PageTitleEditCategoryFeatureCategories}</h2>
</header>
<div role="content">
  <div class="screenlet">
        <div class="screenlet-body">
            <#-- Feature Groups -->
            <table cellspacing="0" class="table table-bordered table-striped">
               <thead>
                <tr class="header-row">
                    <th>${uiLabelMap.ProductFeatureGroup}</th>
                    <th>${uiLabelMap.CommonFromDateTime}</th>
                    <th align="center">${uiLabelMap.CommonThruDateTime}</th>
                    <th>&nbsp;</th>
                </tr>
               </thead>
                <#assign line = 0>
                <#assign rowClass = "2">
                <#list productFeatureCatGrpAppls as productFeatureCatGrpAppl>
                <#assign line = line + 1>
                <#assign productFeatureGroup = (productFeatureCatGrpAppl.getRelatedOne("ProductFeatureGroup", false))?default(null)>
                <tr valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>
                    <td><a href="<@ofbizUrl>EditFeatureGroupAppls?productFeatureGroupId=${(productFeatureCatGrpAppl.productFeatureGroupId)!}</@ofbizUrl>" class="buttontext"><#if productFeatureGroup??>${(productFeatureGroup.description)!}</#if> [${(productFeatureCatGrpAppl.productFeatureGroupId)!}]</a></td>
                    <#assign hasntStarted = false>
                    <#if (productFeatureCatGrpAppl.getTimestamp("fromDate"))?? && nowTimestamp.before(productFeatureCatGrpAppl.getTimestamp("fromDate"))> <#assign hasntStarted = true></#if>
                    <td><div<#if hasntStarted> style="color: red;</#if>>${(productFeatureCatGrpAppl.fromDate)!}</div></td>
                    <td align="center">
                        <form method="post" class="smart-form" action="<@ofbizUrl>updateProductFeatureCatGrpAppl</@ofbizUrl>" name="lineFormGrp${line}">
                            <#assign hasExpired = false>
                            <#if (productFeatureCatGrpAppl.getTimestamp("thruDate"))?? && nowTimestamp.after(productFeatureCatGrpAppl.getTimestamp("thruDate"))> <#assign hasExpired = true></#if>
                            <input type="hidden" name="productCategoryId" value="${(productFeatureCatGrpAppl.productCategoryId)!}" />
                            <input type="hidden" name="productFeatureGroupId" value="${(productFeatureCatGrpAppl.productFeatureGroupId)!}" />
                            <input type="hidden" name="fromDate" value="${(productFeatureCatGrpAppl.fromDate)!}" />
                            <#if hasExpired><#assign class="alert"><#else><#assign class=""></#if>
                            <@htmlTemplate.renderDateTimeField name="thruDate" event="" action="" value="${(productFeatureCatGrpAppl.thruDate)!}" className="${class!}" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" size="22" maxlength="25" id="fromDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                            <input type="submit" value="${uiLabelMap.CommonUpdate}" style="font-size: x-small;" />
                        </form>
                    </td>
                    <td align="center">
                        <a href="javascript:document.removeProductFeatureCatGrpApplForm_${productFeatureCatGrpAppl_index}.submit()" class="buttontext">${uiLabelMap.CommonDelete}</a>
                        <form method="post" class="smart-form" action="<@ofbizUrl>removeProductFeatureCatGrpAppl</@ofbizUrl>" name="removeProductFeatureCatGrpApplForm_${productFeatureCatGrpAppl_index}">
                            <input type="hidden" name="productFeatureGroupId" value="${(productFeatureCatGrpAppl.productFeatureGroupId)!}" />
                            <input type="hidden" name="productCategoryId" value="${(productFeatureCatGrpAppl.productCategoryId)!}" />
                            <input type="hidden" name="fromDate" value="${(productFeatureCatGrpAppl.fromDate)!}" />
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
        </div>
    </div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-4" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductApplyFeatureGroupFromCategory}</h2>
</header>
<div role="content">
    <div class="screenlet">
        <div class="screenlet-body">
            <#if productFeatureGroups?has_content>
            <table cellspacing="0" class="basic-table">
                <tr><td>
                    <form method="post" class="smart-form" action="<@ofbizUrl>createProductFeatureCatGrpAppl</@ofbizUrl>" style="margin: 0;" name="addNewGroupForm">
                    <input type="hidden" name="productCategoryId" value="${productCategoryId!}" />
                    <select name="productFeatureGroupId">
                    <#list productFeatureGroups as productFeatureGroup>
                        <option value="${(productFeatureGroup.productFeatureGroupId)!}">${(productFeatureGroup.description)!} [${(productFeatureGroup.productFeatureGroupId)!}]</option>
                    </#list>
                    </select>
                    <@htmlTemplate.renderDateTimeField name="fromDate" event="" action="" value="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" size="25" maxlength="30" id="fromDate2" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                    <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonAdd}" />
                    </form>
                </td></tr>
            </table>
            <#else>
                &nbsp;
            </#if>
        </div>
    </div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-5" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductApplyFeatureGroupFromCategory}</h2>
</header>
<div role="content">
    <div class="screenlet">
        <div class="screenlet-body">
            <#-- Feature Categories -->
            <table cellspacing="0"  class="table table-bordered table-striped">
              <thead>
                <tr class="header-row">
                    <th>${uiLabelMap.ProductFeatureCategory}</th>
                    <th>${uiLabelMap.CommonFromDateTime}</th>
                    <th align="center">${uiLabelMap.CommonThruDateTime}</th>
                    <th>&nbsp;</th>
                </tr>
              </thead>
                <#assign line = 0>
                <#assign rowClass = "2">
                <#list productFeatureCategoryAppls as productFeatureCategoryAppl>
                <#assign line = line + 1>
                <#assign productFeatureCategory = (productFeatureCategoryAppl.getRelatedOne("ProductFeatureCategory", false))?default(null)>
                <tr valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>
                    <td><a href="<@ofbizUrl>EditFeatureCategoryFeatures?productFeatureCategoryId=${(productFeatureCategoryAppl.productFeatureCategoryId)!}</@ofbizUrl>" class="buttontext"><#if productFeatureCategory??>${(productFeatureCategory.description)!}</#if> [${(productFeatureCategoryAppl.productFeatureCategoryId)!}]</a></td>
                    <#assign hasntStarted = false>
                    <#if (productFeatureCategoryAppl.getTimestamp("fromDate"))?? && nowTimestamp.before(productFeatureCategoryAppl.getTimestamp("fromDate"))> <#assign hasntStarted = true></#if>
                    <td <#if hasntStarted> style="color: red;"</#if>>${(productFeatureCategoryAppl.fromDate)!}</td>
                    <td align="center">
                        <form method="post" class="smart-form" action="<@ofbizUrl>updateProductFeatureCategoryAppl</@ofbizUrl>" name="lineForm${line}">
                            <#assign hasExpired = false>
                            <#if (productFeatureCategoryAppl.getTimestamp("thruDate"))?? && nowTimestamp.after(productFeatureCategoryAppl.getTimestamp("thruDate"))> <#assign hasExpired = true></#if>
                            <input type="hidden" name="productCategoryId" value="${(productFeatureCategoryAppl.productCategoryId)!}" />
                            <input type="hidden" name="productFeatureCategoryId" value="${(productFeatureCategoryAppl.productFeatureCategoryId)!}" />
                            <input type="hidden" name="fromDate" value="${(productFeatureCategoryAppl.fromDate)!}" />
                            <#if hasExpired><#assign class="alert"><#else><#assign class=""></#if>
                            <@htmlTemplate.renderDateTimeField name="thruDate" event="" action="" value="${(productFeatureCategoryAppl.thruDate)!}" className="${class!}" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" size="25" maxlength="30" id="thruDate2" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                            <input type="submit" value="${uiLabelMap.CommonUpdate}" style="font-size: x-small;" />
                        </form>
                    </td>
                    <td align="center">
                    <a href="javascript:document.removeProductFeatureCategoryApplForm_${productFeatureCategoryAppl_index}.submit()" class="buttontext">${uiLabelMap.CommonDelete}</a>
                    <form method="post" class="smart-form" action="<@ofbizUrl>removeProductFeatureCategoryAppl</@ofbizUrl>" name="removeProductFeatureCategoryApplForm_${productFeatureCategoryAppl_index}">
                        <input type="hidden" name="productFeatureCategoryId" value="${(productFeatureCategoryAppl.productFeatureCategoryId)!}" />
                        <input type="hidden" name="productCategoryId" value="${(productFeatureCategoryAppl.productCategoryId)!}" />
                        <input type="hidden" name="fromDate" value="${(productFeatureCategoryAppl.fromDate)!}" />
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
        </div>
    </div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-6" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductApplyFeatureGroupToCategory}</h2>
</header>
<div role="content">
    <div class="screenlet">
        <div class="screenlet-body">
            <table cellspacing="0" class="basic-table">
                <tr><td>
                    <form method="post" class="smart-form" action="<@ofbizUrl>createProductFeatureCategoryAppl</@ofbizUrl>" style="margin: 0;" name="addNewCategoryForm">
                        <input type="hidden" name="productCategoryId" value="${productCategoryId!}" />
                        <select name="productFeatureCategoryId">
                        <#list productFeatureCategories as productFeatureCategory>
                            <option value="${(productFeatureCategory.productFeatureCategoryId)!}">${(productFeatureCategory.description)!} [${(productFeatureCategory.productFeatureCategoryId)!}]</option>
                        </#list>
                        </select>
                        <@htmlTemplate.renderDateTimeField name="fromDate" event="" action="" value="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" size="25" maxlength="30" id="fromDate2" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                        <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonAdd}" />
                    </form>
                </td></tr>
            </table>
        </div>
    </div>
</div>
</div>
</article>
</div>
</#if>