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
<#-- variable setup and worker calls -->
<div class="row">
<article class="col-sm-12 col-md-12 col-lg-6 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-4" data-widget-editbutton="false">

<#if (requestAttributes.topLevelList)??><#assign topLevelList = requestAttributes.topLevelList></#if>
<#if (requestAttributes.curCategoryId)??><#assign curCategoryId = requestAttributes.curCategoryId></#if>

<#-- looping macro -->
<#macro categoryList parentCategory category>
  <#if curCategoryId?? && curCategoryId == category.productCategoryId>
    <div class="browsecategorytext">
     <#if catContentWrappers?? && catContentWrappers[category.productCategoryId]?? && catContentWrappers[category.productCategoryId].get("CATEGORY_NAME", "html")?has_content>
       <#if sessionAttributes.shoppingCart?? && sessionAttributes.shoppingCart.isPurchaseOrder()>
         <a href="<@ofbizUrl>keywordsearch/~SEARCH_CATEGORY_ID=${category.productCategoryId}/~SEARCH_SUPPLIER_ID=${sessionAttributes.shoppingCart.partyId!}/~category_id=${category.productCategoryId}${pStr!}</@ofbizUrl>" class="browsecategorybuttondisabled">${catContentWrappers[category.productCategoryId].get("CATEGORY_NAME", "html")}</a>
       <#else>
         <a href="<@ofbizUrl>category?category_id=${category.productCategoryId}${pStr!}</@ofbizUrl>" class="browsecategorybuttondisabled">${catContentWrappers[category.productCategoryId].get("CATEGORY_NAME", "html")}</a>
       </#if>
     <#elseif catContentWrappers?? && catContentWrappers[category.productCategoryId]?? && catContentWrappers[category.productCategoryId].get("DESCRIPTION", "html")?has_content>
       <#if sessionAttributes.shoppingCart?? && sessionAttributes.shoppingCart.isPurchaseOrder()>
         <a href="<@ofbizUrl>keywordsearch/~SEARCH_CATEGORY_ID=${category.productCategoryId}/~SEARCH_SUPPLIER_ID=${sessionAttributes.shoppingCart.partyId!}/~category_id=${category.productCategoryId}${pStr!}</@ofbizUrl>" class="browsecategorybuttondisabled">${catContentWrappers[category.productCategoryId].get("DESCRIPTION", "html")}</a>
       <#else>
         <a href="<@ofbizUrl>category?category_id=${category.productCategoryId}${pStr!}</@ofbizUrl>" class="browsecategorybuttondisabled">${catContentWrappers[category.productCategoryId].get("DESCRIPTION", "html")}</a>
       </#if>
     <#else>
      <#if sessionAttributes.shoppingCart?? && sessionAttributes.shoppingCart.isPurchaseOrder()>
        <a href="<@ofbizUrl>keywordsearch/~SEARCH_CATEGORY_ID=${category.productCategoryId}/~SEARCH_SUPPLIER_ID=${sessionAttributes.shoppingCart.partyId!}/~category_id=${category.productCategoryId}${pStr!}</@ofbizUrl>" class="browsecategorybuttondisabled">${category.categoryName!}</a>
      <#else>
        <a href="<@ofbizUrl>category?category_id=${category.productCategoryId}${pStr!}</@ofbizUrl>" class="browsecategorybuttondisabled">${category.categoryName?default(category.description)?default(category.productCategoryId)}</a>
      </#if>
     </#if>
    </div>
  <#else>
    <div class="browsecategorytext">
     <#if catContentWrappers[category.productCategoryId].get("CATEGORY_NAME", "html")?has_content>
      <#if sessionAttributes.shoppingCart?? && sessionAttributes.shoppingCart.isPurchaseOrder()>
        <a href="<@ofbizUrl>keywordsearch/~SEARCH_CATEGORY_ID=${category.productCategoryId}/~SEARCH_SUPPLIER_ID=${sessionAttributes.shoppingCart.partyId!}/~category_id=${category.productCategoryId}${pStr!}</@ofbizUrl>" class="browsecategorybutton">${catContentWrappers[category.productCategoryId].get("CATEGORY_NAME", "html")}</a>
      <#else>
        <a href="<@ofbizUrl>category?category_id=${category.productCategoryId}${pStr!}</@ofbizUrl>" class="browsecategorybutton">${catContentWrappers[category.productCategoryId].get("CATEGORY_NAME", "html")}</a>
      </#if>
     <#elseif catContentWrappers[category.productCategoryId].get("DESCRIPTION", "html")?has_content>
      <#if sessionAttributes.shoppingCart?? && sessionAttributes.shoppingCart.isPurchaseOrder()>
        <a href="<@ofbizUrl>keywordsearch/~SEARCH_CATEGORY_ID=${category.productCategoryId}/~SEARCH_SUPPLIER_ID=${sessionAttributes.shoppingCart.partyId!}/~category_id=${category.productCategoryId}${pStr!}</@ofbizUrl>" class="browsecategorybutton">${catContentWrappers[category.productCategoryId].get("DESCRIPTION", "html")}</a>
      <#else>
        <a href="<@ofbizUrl>category?category_id=${category.productCategoryId}${pStr!}</@ofbizUrl>" class="browsecategorybutton">${catContentWrappers[category.productCategoryId].get("DESCRIPTION", "html")}</a>
      </#if>
     <#else>
      <#if sessionAttributes.shoppingCart?? && sessionAttributes.shoppingCart.isPurchaseOrder()>
        <a href="<@ofbizUrl>keywordsearch/~SEARCH_CATEGORY_ID=${category.productCategoryId}/~SEARCH_SUPPLIER_ID=${sessionAttributes.shoppingCart.partyId!}/~category_id=${category.productCategoryId}${pStr!}</@ofbizUrl>" class="browsecategorybutton">${category.categoryName!}</a>
      <#else>
        <a href="<@ofbizUrl>category?category_id=${category.productCategoryId}${pStr!}</@ofbizUrl>" class="browsecategorybutton">${category.categoryName?default(category.description)?default(category.productCategoryId)}</a>
      </#if>
     </#if>
    </div>
  </#if>

  <#if (Static["org.apache.ofbiz.product.category.CategoryWorker"].checkTrailItem(request, category.getString("productCategoryId"))) || (curCategoryId?? && curCategoryId == category.productCategoryId)>
    <#local subCatList = Static["org.apache.ofbiz.product.category.CategoryWorker"].getRelatedCategoriesRet(request, "subCatList", category.getString("productCategoryId"), true)>
    <#if subCatList??>
      <#list subCatList as subCat>
        <div class="browsecategorylist">
          <@categoryList parentCategory=category category=subCat/>
        </div>
      </#list>
    </#if>
  </#if>
</#macro>

<#if topLevelList?has_content>

<header>
<h2>${uiLabelMap.ProductBrowseCategories}</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <div class="browsecategorylist"> 
         <ul>
          <#list topLevelList as category>
            <@categoryList parentCategory=category category=category/>
          </#list>
          </ul>
        </div>
    </div>
</div>
</div>

</#if>
</div>
</article>
