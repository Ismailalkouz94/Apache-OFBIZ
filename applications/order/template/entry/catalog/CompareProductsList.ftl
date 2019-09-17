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
<article class="col-sm-12 col-md-12 col-lg-6 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-5" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductCompareProducts}</h2>
</header>
<div role="content">
<div id="productcomparelist" class="screenlet">
  <div class="screenlet-body">
  <#assign productCompareList = Static["org.apache.ofbiz.product.product.ProductEvents"].getProductCompareList(request)/>
  <#if productCompareList?has_content>
    <table>
    <#list productCompareList as product>
      <tr>
        <td>
          ${Static["org.apache.ofbiz.product.product.ProductContentWrapper"].getProductContentAsText(product, "PRODUCT_NAME", request, "html")}
        </td>
        <td>
          <form method="post" action="<@ofbizUrl>removeFromCompare</@ofbizUrl>" name="removeFromCompare${product_index}form">
            <input class="form-control" type="hidden" name="productId" value="${product.productId}"/>
          </form>
          <a href="javascript:document.removeFromCompare${product_index}form.submit()" class="btn btn-primary1">${uiLabelMap.CommonRemove}</a>
        </td>
      </tr>
    </#list>
  </table>
  <div>
    <a href="<@ofbizUrl>clearCompareList</@ofbizUrl>" class="btn btn-primary1">${uiLabelMap.CommonClearAll}</a>
  </div>
  <div>
    <a href="javascript:popUp('<@ofbizUrl secure="${request.isSecure()?string}">compareProducts</@ofbizUrl>', 'compareProducts', '650', '750')" class="btn btn-primary1">${uiLabelMap.ProductCompareProducts}</a>
  </div>
<#else>
  <div class="alert alert-info fade in">
    ${uiLabelMap.ProductNoProductsToCompare}
  </div>
</#if>
  </div>
</div>
</div>
</div>
</article>

</div>
