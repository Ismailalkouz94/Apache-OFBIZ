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
function insertImageName(type,nameValue) {
  eval('document.productCategoryForm.' + type + 'ImageUrl.value=nameValue;');
};
</script>
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductResultOfImageUpload}</h2>
</header>
<div role="content">
<#if fileType?has_content>
    <div class="screenlet">
        <div class="screenlet-body">
            <#if !(clientFileName?has_content)>
                <div>${uiLabelMap.ProductNoFileSpecifiedForUpload}.</div>
            <#else>
                <div>${uiLabelMap.ProductTheFileOnYourComputer}: <b>${clientFileName!}</b></div>
                <div>${uiLabelMap.ProductServerFileName}: <b>${fileNameToUse!}</b></div>
                <div>${uiLabelMap.ProductServerDirectory}: <b>${imageServerPath!}</b></div>
                <div>${uiLabelMap.ProductTheUrlOfYourUploadedFile}: <b><a href="<@ofbizContentUrl>${imageUrl!}</@ofbizContentUrl>">${imageUrl!}</a></b></div>
            </#if>
        </div>
    </div>
</#if>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<#if ! productCategory?has_content>
<#if productCategoryId?has_content><h2>${uiLabelMap.ProductCouldNotFindProductCategoryWithId} "${productCategoryId}".</h2>
<#else><h2>${uiLabelMap.PageTitleCreateProductCategory}</h2></#if>
<#else><h2>${uiLabelMap.PageTitleEditProductCategories}</h2></#if>
</header>
<div role="content">
<div class="screenlet">
<#if ! productCategory?has_content>
    <#if productCategoryId?has_content>

        <div class="screenlet-body">
            <form action="<@ofbizUrl>createProductCategory</@ofbizUrl>" method="post" class="smart-form" style="margin: 0;" name="productCategoryForm">
                <table cellspacing="0" class="basic-table">
                    <tr>
                        <td><label>${uiLabelMap.ProductProductCategoryId}</label></td>
                        <td>&nbsp;</td>
                        <td>
                            <input class="form-control" type="text" name="productCategoryId" size="20" maxlength="40" value="${productCategoryId}"/>
                        </td>
                    </tr>
    <#else>

        <div class="screenlet-body">
            <form action="<@ofbizUrl>createProductCategory</@ofbizUrl>" method="post" class="smart-form" style="margin: 0;" name="productCategoryForm">
                <table cellspacing="0" class="basic-table">
                    <tr>
                        <td><label>${uiLabelMap.ProductProductCategoryId}</label></td>
                        <td>&nbsp;</td>
                        <td>
                            <input class="form-control" type="text" name="productCategoryId" size="20" maxlength="40" value=""/>
                        </td>
                    </tr>
    </#if>
<#else>

    <div class="screenlet-body">
        <form action="<@ofbizUrl>updateProductCategory</@ofbizUrl>" method="post" class="smart-form" style="margin: 0;" name="productCategoryForm">
            <input class="form-control" type="hidden" name="productCategoryId" value="${productCategoryId}"/>
            <table cellspacing="0" class="basic-table">
                <tr>
                    <td><label>${uiLabelMap.ProductProductCategoryId}</label></td>
                    <td>&nbsp;</td>
                    <td>
                      <b>${productCategoryId}</b> (${uiLabelMap.ProductNotModificationRecreationCategory}.)
                    </td>
                </tr>
</#if>
                <tr>
                    <td><label>${uiLabelMap.ProductProductCategoryType}</label></td>
                    <td>&nbsp;</td>
                    <td width="74%">
                        <select class="form-control" name="productCategoryTypeId" size="1">
                            <#assign selectedKey = "">
                            <#list productCategoryTypes as productCategoryTypeData>
                                <#if requestParameters.productCategoryTypeId?has_content>
                                    <#assign selectedKey = requestParameters.productCategoryTypeId>
                                <#elseif (productCategory?has_content && productCategory.productCategoryTypeId! == productCategoryTypeData.productCategoryTypeId)>
                                    <#assign selectedKey = productCategory.productCategoryTypeId>
                                </#if>
                                <option <#if selectedKey == productCategoryTypeData.productCategoryTypeId!>selected="selected"</#if> value="${productCategoryTypeData.productCategoryTypeId}">${productCategoryTypeData.get("description",locale)}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label>${uiLabelMap.ProductProductCategoryName}</label></td>
                    <td>&nbsp;</td>
                    <td width="74%"><input class="form-control" type="text" value="${(productCategory.categoryName)!}" name="categoryName" size="60" maxlength="60"/></td>
                </tr>
                <tr>
                    <td><label>${uiLabelMap.ProductProductCategoryDescription}</label></td>
                    <td>&nbsp;</td>
                    <td width="74%"><textarea class="form-control" name="description" cols="60" rows="2"><#if productCategory?has_content>${(productCategory.description)!}</#if></textarea></td>
                </tr>
                <tr>
                    <td><label>
                        ${uiLabelMap.ProductCategoryImageUrl}
                        <#if (productCategory.categoryImageUrl)??>
                            <a href="<@ofbizContentUrl>${(productCategory.categoryImageUrl)!}</@ofbizContentUrl>" target="_blank"><img alt="Category Image" src="<@ofbizContentUrl>${(productCategory.categoryImageUrl)!}</@ofbizContentUrl>" class="cssImgSmall" /></a>
                        </#if>
                    </label></td>
                    <td>&nbsp;</td>
                    <td width="80%" colspan="4" valign="top">
                        <input class="form-control" type="text" name="categoryImageUrl" value="${(productCategory.categoryImageUrl)?default('')}" size="60" maxlength="255"/>
                        <#if productCategory?has_content>
                            <div>
                            ${uiLabelMap.ProductInsertDefaultImageUrl}:
                            <a href="javascript:insertImageName('category','${imageNameCategory}.jpg');" class="btnA">.jpg</a>
                            <a href="javascript:insertImageName('category','${imageNameCategory}.gif');" class="btnA">.gif</a>
                            <a href="javascript:insertImageName('category','');" class="btnA">${uiLabelMap.CommonClear}</a>
                            </div>
                        </#if>
                    </td>
                </tr>
                <tr>
                    <td><label>
                        ${uiLabelMap.ProductLinkOneImageUrl}
                        <#if (productCategory.linkOneImageUrl)??>
                            <a href="<@ofbizContentUrl>${(productCategory.linkOneImageUrl)!}</@ofbizContentUrl>" target="_blank"><img alt="Link One Image" src="<@ofbizContentUrl>${(productCategory.linkOneImageUrl)!}</@ofbizContentUrl>" class="cssImgSmall" /></a>
                        </#if>
                    </label></td>
                    <td>&nbsp;</td>
                    <td>
                        <input class="form-control" type="text" name="linkOneImageUrl" value="${(productCategory.linkOneImageUrl)?default('')}" size="60" maxlength="255"/>
                        <#if productCategory?has_content>
                            <div>
                                ${uiLabelMap.ProductInsertDefaultImageUrl}:
                                <a href="javascript:insertImageName('linkOne','${imageNameLinkOne}.jpg');" class="btnA">.jpg</a>
                                <a href="javascript:insertImageName('linkOne','${imageNameLinkOne}.gif');" class="btnA">.gif</a>
                                <a href="javascript:insertImageName('linkOne','');" class="btnA">${uiLabelMap.CommonClear}</a>
                            </div>
                        </#if>
                    </td>
                </tr>
                <tr>
                    <td><label>
                        ${uiLabelMap.ProductLinkTwoImageUrl}
                        <#if (productCategory.linkTwoImageUrl)??>
                            <a href="<@ofbizContentUrl>${(productCategory.linkTwoImageUrl)!}</@ofbizContentUrl>" target="_blank"><img alt="Link One Image" src="<@ofbizContentUrl>${(productCategory.linkTwoImageUrl)!}</@ofbizContentUrl>" class="cssImgSmall" /></a>
                        </#if>
                    </label></td>
                    <td>&nbsp;</td>
                    <td>
                        <input class="form-control" type="text" name="linkTwoImageUrl" value="${(productCategory.linkTwoImageUrl)?default('')}" size="60" maxlength="255"/>
                        <#if productCategory?has_content>
                            <div>
                                ${uiLabelMap.ProductInsertDefaultImageUrl}:
                                <a href="javascript:insertImageName('linkTwo','${imageNameLinkTwo}.jpg');" class="btnA">.jpg</a>
                                <a href="javascript:insertImageName('linkTwo','${imageNameLinkTwo}.gif');" class="btnA">.gif</a>
                                <a href="javascript:insertImageName('linkTwo','');" class="btnA">${uiLabelMap.CommonClear}</a>
                            </div>
                        </#if>
                    </td>
                </tr>
                <tr>
                    <td><label>${uiLabelMap.ProductDetailScreen}</label></td>
                    <td>&nbsp;</td>
                    <td width="74%">
                        <input class="form-control" type="text" <#if productCategory?has_content>value="${productCategory.detailScreen!}"</#if> name="detailScreen" size="60" maxlength="250"/>
                        <br /><span class="tooltip1">${uiLabelMap.ProductDefaultsTo} &quot;categorydetail&quot;, ${uiLabelMap.ProductDetailScreenMessage}: &quot;component://ecommerce/widget/CatalogScreens.xml#categorydetail&quot;</span>
                    </td>
                </tr>
                <tr>
                    <td><label>${uiLabelMap.ProductPrimaryParentCategory}</label></td>
                    <td>&nbsp;</td>
                    <td width="74%">
                        <@htmlTemplate.lookupField value="${(productCategory.primaryParentCategoryId)?default('')}" formName="productCategoryForm" name="primaryParentCategoryId" id="primaryParentCategoryId" fieldFormName="LookupProductCategory"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                    <td><input class="btn btn-primary1" type="submit" name="Update" value="${uiLabelMap.CommonUpdate}"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>
</div>
</div>
</article>
<#if productCategoryId?has_content>
    <script language="JavaScript" type="text/javascript">
        function setUploadUrl(newUrl) {
        var toExec = 'document.imageUploadForm.action="' + newUrl + '";';
        eval(toExec);
        };
    </script>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductCategoryUploadImage}</h2>
</header>
<div role="content">
    <div class="screenlet">
        <div class="screenlet-body">
            <form method="post" enctype="multipart/form-data" action="<@ofbizUrl>UploadCategoryImage?productCategoryId=${productCategoryId!}&amp;upload_file_type=category</@ofbizUrl>" name="imageUploadForm">
                <table cellspacing="0" class="basic-table">
                    <tr><td>
                        <input type="file" class="btn btn-default" size="50" name="fname"/>
                        <br />
                        <span>
                            <label class="radio radio-inline" style="margin:0 !important"><input class="radiobox" type="radio" name="upload_file_type_bogus" value="category" checked="checked" onclick='setUploadUrl("<@ofbizUrl>UploadCategoryImage?productCategoryId=${productCategoryId}&amp;upload_file_type=category</@ofbizUrl>");'/><span>${uiLabelMap.ProductCategoryImageUrl}</span></label>
                            <label class="radio radio-inline"><input class="radiobox" type="radio" name="upload_file_type_bogus" value="linkOne" onclick='setUploadUrl("<@ofbizUrl>UploadCategoryImage?productCategoryId=${productCategoryId}&amp;upload_file_type=linkOne</@ofbizUrl>");'/><span>${uiLabelMap.ProductLinkOneImageUrl}</span></label>
                            <label class="radio radio-inline"><input class="radiobox" type="radio" name="upload_file_type_bogus" value="linkTwo"onclick='setUploadUrl("<@ofbizUrl>UploadCategoryImage?productCategoryId=${productCategoryId}&amp;upload_file_type=linkTwo</@ofbizUrl>");'/><span>${uiLabelMap.ProductLinkTwoImageUrl}</span></label>
                        </span>
                        <input type="submit" class="btn btn-primary1" value="${uiLabelMap.ProductUploadImage}"/>
                    </td></tr>
                </table>
            </form>
        </div>
    </div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductDuplicateProductCategory}</h2>
</header>
<div role="content">
    <div class="screenlet">
        <div class="screenlet-body">
            <form action="<@ofbizUrl>DuplicateProductCategory</@ofbizUrl>" class="smart-form" method="post" style="margin: 0;">
                <table cellspacing="0" class="basic-table">
                    <tr><td>
                        <label>${uiLabelMap.ProductDuplicateProductCategorySelected}:</label>
                        <input class="form-control" type="hidden" name="oldProductCategoryId" value="${productCategoryId}"/>
                        <div>
                            <input class="form-control" type="text" size="20" maxlength="20" name="productCategoryId"/>&nbsp;<input type="submit" class="btn btn-primary" class="smallSubmit" value="${uiLabelMap.CommonGo}"/>
                        </div>
                        <div>
                            <label>${uiLabelMap.CommonDuplicate}:</label>
                            <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="duplicateContent" value="Y" checked="checked" /><span>${uiLabelMap.ProductCategoryContent}</span></label>
                            <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="duplicateParentRollup" value="Y" checked="checked" /><span>${uiLabelMap.ProductCategoryRollupParentCategories}</span></label>
                            <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="duplicateChildRollup" value="Y" /><span>${uiLabelMap.ProductCategoryRollupChildCategories}</span></label>
                            <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="duplicateMembers" value="Y" checked="checked" /><span>${uiLabelMap.ProductProducts}</span></label>
                            <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="duplicateCatalogs" value="Y" checked="checked" /><span>${uiLabelMap.ProductCatalogs}</span></label>
                            <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="duplicateFeatureCategories" value="Y" checked="checked" /><span>${uiLabelMap.ProductFeatureCategories}</span></label>
                            <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="duplicateFeatureGroups" value="Y"/><span>${uiLabelMap.ProductFeatureGroups}</span></label>
                            <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="duplicateRoles" value="Y" checked="checked" /><span>${uiLabelMap.PartyParties}</span></label>
                            <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="duplicateAttributes" value="Y" checked="checked" /><span>${uiLabelMap.ProductAttributes}</span></label>
                        </div>
                    </td></tr>
                </table>
            </form>
        </div>
    </div>
</#if>
</div>
</div>
</article>
</div>