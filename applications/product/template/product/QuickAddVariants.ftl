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
<script type="text/javascript">
function setProductVariantId(e, value, fieldname) {
    var cform = document.selectAllForm;
    var len = cform.elements.length;
    for (var i = 0; i < len; i++) {
        var element = cform.elements[i];
        if (element.name == fieldname) {
            if (e.checked) {
                if (element.value == null || element.value == "") {
                    element.value = value;
                }
            } else {
                element.value = "";
            }
            return;
        }
    }
}
function clickAll(e) {
    var cform = document.selectAllForm;
    var len = cform.elements.length;
    for (var i = 0; i < len; i++) {
        var element = cform.elements[i];
        if (element.name.substring(0, 10) == "_rowSubmit" && element.checked != e.checked) {
            element.click();
        }
    }
}
</script>
<#if (product.isVirtual)! != "Y">
    <div class="alert alert-warning fade in">${uiLabelMap.ProductWarningProductNotVirtual}</div>
</#if>
<#if featureTypes?has_content && (featureTypes.size() > 0)>
        <form method="post" class="smart-form" action="<@ofbizUrl>QuickAddChosenVariants</@ofbizUrl>" name="selectAllForm">
            <input type="hidden" name="productId" value="${productId}" />
            <input type="hidden" name="_useRowSubmit" value="Y" />
            <input type="hidden" name="_checkGlobalScope" value="Y" />
        <table cellspacing="0" class="basic-table">
        <#assign rowCount = 0>
        <thead>
        <tr class="header-row">
            <#list featureTypes as featureType>
                <th><b>${featureType}</b></td>
            </#list>
            <th><b>${uiLabelMap.ProductNewProductCreate} !</b></th>
            <th><b>${uiLabelMap.ProductSequenceNum}</b></th>
            <th><b>${uiLabelMap.ProductExistingVariant} :</b></th>
            <th align="right"><b><label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="selectAll" value="${uiLabelMap.CommonY}" onclick="javascript:clickAll(this);" /><span>${uiLabelMap.CommonAll}</span></label></b></th>
        </tr>
        </thead>
        <#assign defaultSequenceNum = 10>
        <#assign rowClass = "2">
        <#list featureCombinationInfos as featureCombinationInfo>
            <#assign curProductFeatureAndAppls = featureCombinationInfo.curProductFeatureAndAppls>
            <#assign existingVariantProductIds = featureCombinationInfo.existingVariantProductIds>
            <#assign defaultVariantProductId = featureCombinationInfo.defaultVariantProductId>
            <tr valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>
                <#assign productFeatureIds = "">
                <#list curProductFeatureAndAppls as productFeatureAndAppl>
                <td>
                    ${productFeatureAndAppl.description!}
                    <#assign productFeatureIds = productFeatureIds + "|" + productFeatureAndAppl.productFeatureId>
                </td>
                </#list>
                <td>
                    <input type="hidden" name="productFeatureIds_o_${rowCount}" value="${productFeatureIds}"/>
                    <input type="text" size="20" maxlength="20" name="productVariantId_o_${rowCount}" value=""/>
                </td>
                <td>
                    <input type="text" size="5" maxlength="10" name="sequenceNum_o_${rowCount}" value="${defaultSequenceNum}"/>
                </td>
                <td>
                    <div>
                    <#list existingVariantProductIds as existingVariantProductId>
                        <a href="<@ofbizUrl>EditProduct?productId=${existingVariantProductId}</@ofbizUrl>" class="buttontext">${existingVariantProductId}</a>
                    </#list>
                    </div>
                </td>
                <td align="right">
                  <input type="checkbox" name="_rowSubmit_o_${rowCount}" value="Y" onclick="javascript:setProductVariantId(this, '${defaultVariantProductId}', 'productVariantId_o_${rowCount}');" />
                </td>
            </tr>
            <#assign defaultSequenceNum = defaultSequenceNum + 10>
            <#assign rowCount = rowCount + 1>
            <#-- toggle the row color -->
            <#if rowClass == "2">
                <#assign rowClass = "1">
            <#else>
                <#assign rowClass = "2">
            </#if>
        </#list>
        <tr>
            <#assign columns = featureTypes.size() + 4>
            <td colspan="${columns}" align="center">
                <input type="hidden" name="_rowCount" value="${rowCount}" />
                <input type="submit" class="smallSubmit" value="${uiLabelMap.CommonCreate}"/>
            </td>
        </tr>
        </table>
    </form>
<#else>
    <div class="alert alert-info fade in">${uiLabelMap.ProductNoSelectableFeaturesFound}</div>
</#if>
<form action="<@ofbizUrl>addVariantsToVirtual</@ofbizUrl>" class="smart-form" method="post" name="addVariantsToVirtual">
    <table cellspacing="0" class="basic-table">
        <tr class="header-row">
            <td><label>${uiLabelMap.ProductVariantAdd}</label>&nbsp;</td>
        </tr>
        <tr>
            <td>
                <br />
                <input type="hidden" name="productId" value="${productId}"/>
                <label>${uiLabelMap.ProductVariantProductIds}  </label>&nbsp;&nbsp;
                <textarea name="variantProductIdsBag" class="form-control" rows="6" cols="20"></textarea><br/>
                <input type="submit" class="btn btn-primary1" value="${uiLabelMap.ProductVariantAdd}"/>
            </td>
        </tr>
    </table>
</form>