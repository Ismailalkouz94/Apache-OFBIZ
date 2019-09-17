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
<#if product??>
<script language="JavaScript" type="text/javascript">
    function insertNowTimestamp(field) {
        eval('document.productForm.' + field + '.value="${nowTimestampString}";');
    };
    function insertImageName(type,nameValue) {
        eval('document.productForm.' + type + 'ImageUrl.value=nameValue;');
    };
</script>

    <#if fileType?has_content>
<h3>${uiLabelMap.ProductResultOfImageUpload}</h3>
        <#if !(clientFileName?has_content)>
    <div>${uiLabelMap.ProductNoFileSpecifiedForUpload}.</div>
        <#else>
    <div>${uiLabelMap.ProductTheFileOnYourComputer}: <b>${clientFileName!}</b></div>
    <div>${uiLabelMap.ProductServerFileName}: <b>${fileNameToUse!}</b></div>
    <div>${uiLabelMap.ProductServerDirectory}: <b>${imageServerPath!}</b></div>
    <div>${uiLabelMap.ProductTheUrlOfYourUploadedFile}: <b><a href="<@ofbizContentUrl>${imageUrl!}</@ofbizContentUrl>">${imageUrl!}</a></b></div>
        </#if>
    <br />
    </#if>
    <form action="<@ofbizUrl>updateProductContent</@ofbizUrl>" method="post" class="smart-form" style="margin: 0;" name="productForm">
        <input type="hidden" name="productId" value="${productId!}"/>
        <table cellspacing="0" class="basic-table">
            <tr>
                <td width="20%"><label>${uiLabelMap.ProductProductName}</label></td>
                <td>&nbsp;</td>
                <td width="80%" colspan="4">
                    <input type="text" class="form-control" name="productName" value="${(product.productName?html)!}" size="30" maxlength="60"/>
                </td>
            </tr>
            <tr>
                <td width="20%" align="right"><label>${uiLabelMap.ProductProductDescription}</label></td>
                <td>&nbsp;</td>
                <td width="80%" colspan="4">
                    <textarea name="description"  class="form-control" cols="60" rows="2" maxlength="255">${(product.description)!}</textarea>
                </td>
            </tr>
            <tr>
                <td width="20%" align="right"><label>${uiLabelMap.ProductLongDescription}</label></td>
                <td>&nbsp;</td>
                <td width="80%" colspan="4">
                    <textarea class="dojo-ResizableTextArea"  class="form-control" name="longDescription" cols="60" rows="7">${(product.longDescription)!}</textarea>
                </td>
            </tr>
            <tr>
                <td width="20%" align="right"><label>${uiLabelMap.ProductDetailScreen}</label></td>
                <td>&nbsp;</td>
                <td width="80%" colspan="4">
                    <input type="text" class="form-control" name="detailScreen" value="${(product.detailScreen)!}" size="60" maxlength="250"/><span class="tooltip1">${uiLabelMap.ProductIfNotSpecifiedDefaultsIsProductdetail} &quot;productdetail&quot;, ${uiLabelMap.ProductDetailScreenMessage}: &quot;component://ecommerce/widget/CatalogScreens.xml#productdetail&quot;</span>
                </td>
            </tr>
            <tr>
                <td width="20%" align="right">
                    <div><label>${uiLabelMap.ProductSmallImage}</label></div>
                      <#if (product.smallImageUrl)??>
                       <a href="<@ofbizContentUrl>${(product.smallImageUrl)!}</@ofbizContentUrl>" target="_blank"><img alt="Small Image" src="<@ofbizContentUrl>${(product.smallImageUrl)!}</@ofbizContentUrl>" class="cssImgSmall"/></a>
                      </#if>
                </td>
                <td>&nbsp;</td>
                <td width="80%" colspan="4">
                    <input type="text" class="form-control" name="smallImageUrl" value="${(product.smallImageUrl)?default('')}" size="60" maxlength="255"/>
    <#if productId?has_content>
                    <div>
                        <span><label>${uiLabelMap.ProductInsertDefaultImageUrl}: </label></span>
                        <a href="javascript:insertImageName('small','${imageNameSmall}.jpg');" class="buttontext">.jpg</a>
                        <a href="javascript:insertImageName('small','${imageNameSmall}.gif');" class="buttontext">.gif</a>
                        <a href="javascript:insertImageName('small','');" class="buttontext">${uiLabelMap.CommonClear}</a>
                    </div>
    </#if>
                </td>
            </tr>
            <tr>
                <td width="20%" align="right">
                    <div><label>${uiLabelMap.ProductMediumImage}</label></div>
    <#if (product.mediumImageUrl)??>
                    <a href="<@ofbizContentUrl>${product.mediumImageUrl}</@ofbizContentUrl>" target="_blank"><img alt="Medium Image" src="<@ofbizContentUrl>${product.mediumImageUrl}</@ofbizContentUrl>" class="cssImgSmall"/></a>
    </#if>
                </td>
                <td>&nbsp;</td>
                <td width="80%" colspan="4">
                    <input type="text" class="form-control" name="mediumImageUrl" value="${(product.mediumImageUrl)?default('')}" size="60" maxlength="255"/>
    <#if productId?has_content>
                    <div>
                        <span><label>${uiLabelMap.ProductInsertDefaultImageUrl}: </label></span>
                        <a href="javascript:insertImageName('medium','${imageNameMedium}.jpg');" class="buttontext">.jpg</a>
                        <a href="javascript:insertImageName('medium','${imageNameMedium}.gif');" class="buttontext">.gif</a>
                        <a href="javascript:insertImageName('medium','');" class="buttontext">${uiLabelMap.CommonClear}</a>
                    </div>
    </#if>
                </td>
            </tr>
            <tr>
                <td width="20%" align="right">
                    <div><label>${uiLabelMap.ProductLargeImage}</label></div>
    <#if (product.largeImageUrl)??>
                    <a href="<@ofbizContentUrl>${product.largeImageUrl}</@ofbizContentUrl>" target="_blank"><img alt="Large Image" src="<@ofbizContentUrl>${product.largeImageUrl}</@ofbizContentUrl>" class="cssImgSmall"/></a>
    </#if>
                </td>
                <td>&nbsp;</td>
                <td width="80%" colspan="4">
                    <input type="text" class="form-control" name="largeImageUrl" value="${(product.largeImageUrl)?default('')}" size="60" maxlength="255"/>
    <#if productId?has_content>
                    <div>
                        <span><label>${uiLabelMap.ProductInsertDefaultImageUrl}: </label></span>
                        <a href="javascript:insertImageName('large','${imageNameLarge}.jpg');" class="buttontext">.jpg</a>
                        <a href="javascript:insertImageName('large','${imageNameLarge}.gif');" class="buttontext">.gif</a>
                        <a href="javascript:insertImageName('large','');" class="buttontext">${uiLabelMap.CommonClear}</a>
                    </div>
    </#if>
                </td>
            </tr>
            <tr>
                <td width="20%" align="right">
                    <div><label>${uiLabelMap.ProductDetailImage}</label></div>
    <#if (product.detailImageUrl)??>
                    <a href="<@ofbizContentUrl>${product.detailImageUrl}</@ofbizContentUrl>" target="_blank"><img alt="Detail Image" src="<@ofbizContentUrl>${product.detailImageUrl}</@ofbizContentUrl>" class="cssImgSmall"/></a>
    </#if>
                </td>
                <td>&nbsp;</td>
                <td width="80%" colspan="4">
                    <input type="text" class="form-control" name="detailImageUrl" value="${(product.detailImageUrl)?default('')}" size="60" maxlength="255"/>
    <#if productId?has_content>
                    <div>
                        <span><label>${uiLabelMap.ProductInsertDefaultImageUrl}: </label></span>
                        <a href="javascript:insertImageName('detail','${imageNameDetail}.jpg');" class="buttontext">.jpg</a>
                        <a href="javascript:insertImageName('detail','${imageNameDetail}.gif');" class="buttontext">.gif</a>
                        <a href="javascript:insertImageName('detail','');" class="buttontext">${uiLabelMap.CommonClear}</a>
                    </div>
    </#if>
                </td>
            </tr>
            <tr>
                <td width="20%" align="right">
                    <div><label>${uiLabelMap.ProductOriginalImage}</label></div>
    <#if (product.originalImageUrl)??>
                    <a href="<@ofbizContentUrl>${product.originalImageUrl}</@ofbizContentUrl>" target="_blank"><img alt="Original Image" src="<@ofbizContentUrl>${product.originalImageUrl}</@ofbizContentUrl>" class="cssImgSmall"/></a>
    </#if>
                </td>
                <td>&nbsp;</td>
                <td width="80%" colspan="4">
                    <input type="text" class="form-control" name="originalImageUrl" value="${(product.originalImageUrl)?default('')}" size="60" maxlength="255"/>
    <#if productId?has_content>
                    <div>
                        <span><label>${uiLabelMap.ProductInsertDefaultImageUrl}: </label></span>
                        <a href="javascript:insertImageName('original','${imageNameOriginal}.jpg');" class="buttontext">.jpg</a>
                        <a href="javascript:insertImageName('original','${imageNameOriginal}.gif');" class="buttontext">.gif</a>
                        <a href="javascript:insertImageName('original','');" class="buttontext">${uiLabelMap.CommonClear}</a>
                    </div>
    </#if>
                </td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
                <td><input type="submit" name="Update" value="${uiLabelMap.CommonUpdate}" class="btn btn-primary1"/></td>
                <td colspan="3">&nbsp;</td>
            </tr>
        </table>
    </form>
    <script language="JavaScript" type="text/javascript">
        function setUploadUrl(newUrl) {
            var toExec = 'document.imageUploadForm.action="' + newUrl + '";';
            eval(toExec);
        };
    </script>
    <h3>${uiLabelMap.ProductUploadImage}</h3>
    <form method="post" class="smart-form" enctype="multipart/form-data" action="<@ofbizUrl>UploadProductImage?productId=${productId}&amp;upload_file_type=original</@ofbizUrl>" name="imageUploadForm">
        <table cellspacing="0" class="basic-table">
            <tr>
                <td width="20%">
                    <input type="file" class="btn btn-default" size="50" name="fname"/>
                </td>
                <td>&nbsp;</td>
                <td width="80%" colspan="4">
                    <label class="radio radio-inline"><input class="radiobox" type="radio" name="upload_file_type_bogus" value="small" onclick='setUploadUrl("<@ofbizUrl>UploadProductImage?productId=${productId}&amp;upload_file_type=small</@ofbizUrl>");'/><span>${uiLabelMap.CommonSmall}</span></label>
                    <label class="radio radio-inline"><input class="radiobox"  type="radio" name="upload_file_type_bogus" value="medium" onclick='setUploadUrl("<@ofbizUrl>UploadProductImage?productId=${productId}&amp;upload_file_type=medium</@ofbizUrl>");'/><span>${uiLabelMap.CommonMedium}</span></label>
                    <label class="radio radio-inline"><input class="radiobox"  type="radio" name="upload_file_type_bogus" value="large"onclick='setUploadUrl("<@ofbizUrl>UploadProductImage?productId=${productId}&amp;upload_file_type=large</@ofbizUrl>");'/><span>${uiLabelMap.CommonLarge}</span></label>
                    <label class="radio radio-inline"><input class="radiobox"  type="radio" name="upload_file_type_bogus" value="detail" onclick='setUploadUrl("<@ofbizUrl>UploadProductImage?productId=${productId}&amp;upload_file_type=detail</@ofbizUrl>");'/><span>${uiLabelMap.CommonDetail}</span></label>
                    <label class="radio radio-inline"><input class="radiobox"  type="radio" name="upload_file_type_bogus" value="original" checked="checked" onclick='setUploadUrl("<@ofbizUrl>UploadProductImage?productId=${productId}&amp;upload_file_type=original</@ofbizUrl>");'/><span>${uiLabelMap.ProductOriginal}</span></label>
                    <input type="submit" class="btn btn-primary1" value="${uiLabelMap.ProductUploadImage}"/>
                </td>
            </tr>
        </table>
        <span class="tooltip1">${uiLabelMap.ProductOriginalImageMessage} : {ofbiz.home}/applications/product/config/ImageProperties.xml&quot;</span>
    </form>
</#if>
