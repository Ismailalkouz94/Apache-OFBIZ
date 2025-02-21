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
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-6" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductOverrideSimpleFields}</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <form action="<@ofbizUrl>updateCategoryContent</@ofbizUrl>" class="smart-form" method="post" style="margin: 0;" name="categoryForm">
            <table cellspacing="0" class="basic-table">
                <tr>
                    <td width="26%"><label><input type="hidden" name="productCategoryId" value="${productCategoryId!}" />${uiLabelMap.ProductProductCategoryType}</label></td>
                    <td>&nbsp;</td>
                    <td width="74%">
                        <select name="productCategoryTypeId" size="1">
                        <option value="">&nbsp;</option>
                        <#list productCategoryTypes as productCategoryTypeData>
                            <option <#if productCategory?has_content><#if productCategory.productCategoryTypeId==productCategoryTypeData.productCategoryTypeId> selected="selected"</#if></#if> value="${productCategoryTypeData.productCategoryTypeId}">${productCategoryTypeData.get("description",locale)}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="26%"><label>${uiLabelMap.ProductName}</label></td>
                    <td>&nbsp;</td>
                    <td width="74%"><input type="text" value="${(productCategory.categoryName)!}" name="categoryName" size="60" maxlength="60"/></td>
                </tr>
                <tr>
                    <td width="26%"><label>${uiLabelMap.ProductCategoryDescription}</label></td>
                    <td>&nbsp;</td>
                    <td width="74%" colspan="4" valign="top">
                        <textarea name="description" class="form-control" cols="60" rows="2">${(productCategory.description)!}</textarea>
                    </td>
                </tr>
                <tr>
                    <td width="26%"><label>${uiLabelMap.ProductLongDescription}</label></td>
                    <td>&nbsp;</td>
                    <td width="74%" colspan="4" valign="top">
                        <textarea name="longDescription" class="form-control" cols="60" rows="7">${(productCategory.longDescription)!}</textarea>
                    </td>
                </tr>
                <tr>
                    <td width="26%"><label>${uiLabelMap.ProductDetailScreen}</label></td>
                    <td>&nbsp;</td>
                    <td width="74%">
                        <input type="text" <#if productCategory?has_content>value="${productCategory.detailScreen!}"</#if> name="detailScreen" size="60" maxlength="250" />
                        <br />
                        <span class="tooltip1">${uiLabelMap.ProductDefaultsTo} &quot;categorydetail&quot;, ${uiLabelMap.ProductDetailScreenMessage}: &quot;component://ecommerce/widget/CatalogScreens.xml#categorydetail&quot;</span>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                    <td><input class="btn btn-primary1" type="submit" name="Update" value="${uiLabelMap.CommonUpdate}" /></td>
                    <td colspan="3">&nbsp;</td>
                </tr>
            </table>
        </form>
    </div>
</div>
</div>
</div>
</article>
</div>