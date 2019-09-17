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
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-3" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductSearchCatalog}</h2>
</header>
<div role="content">

<div id="keywordsearchbox" class="screenlet">
  <div class="screenlet-body">
    <form name="keywordsearchform" id="keywordsearchbox_keywordsearchform" method="post" action="<@ofbizUrl>keywordsearch</@ofbizUrl>">
      <fieldset class="inline">
        <input class="form-control" type="hidden" name="VIEW_SIZE" value="10" />
        <input class="form-control" type="hidden" name="PAGING" value="Y" />
        <div>
          <input class="form-control" type="text" name="SEARCH_STRING" size="14" maxlength="50" value="${requestParameters.SEARCH_STRING!}" /><br/><br/>
        </div>
        <#if 0 &lt; otherSearchProdCatalogCategories?size>
          <div>
            <select class="form-control" name="SEARCH_CATEGORY_ID" size="1">
              <option value="${searchCategoryId!}">${uiLabelMap.ProductEntireCatalog}</option>
              <#list otherSearchProdCatalogCategories as otherSearchProdCatalogCategory>
                <#assign searchProductCategory = otherSearchProdCatalogCategory.getRelatedOne("ProductCategory", true)>
                <#if searchProductCategory??>
                  <option value="${searchProductCategory.productCategoryId}">${searchProductCategory.description?default("No Description " + searchProductCategory.productCategoryId)}</option>
                </#if>
              </#list>
            </select>
          </div>
        <#else>
          <input class="form-control" type="hidden" name="SEARCH_CATEGORY_ID" value="${searchCategoryId!}" />
        </#if>
        <div>
          <label for="SEARCH_OPERATOR_OR" class="radio radio-inline" style="margin:0"><input class="radiobox" type="radio" name="SEARCH_OPERATOR" id="SEARCH_OPERATOR_OR" value="OR" <#if searchOperator == "OR">checked="checked"</#if> /><span>${uiLabelMap.CommonAny}</span></label>
          <label for="SEARCH_OPERATOR_AND" class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_OPERATOR" id="SEARCH_OPERATOR_AND" value="AND" <#if searchOperator == "AND">checked="checked"</#if> /><span>${uiLabelMap.CommonAll}</span></label>
          <input type="submit" value="${uiLabelMap.CommonFind}" class="btn btn-primary" />
        </div>
      </fieldset>
    </form>
    <form name="advancedsearchform" id="keywordsearchbox_advancedsearchform" method="post" action="<@ofbizUrl>advancedsearch</@ofbizUrl>">

        <#if 0 &lt; otherSearchProdCatalogCategories?size>
            <label for="SEARCH_CATEGORY_ID">${uiLabelMap.ProductAdvancedSearchIn}: </label>
            <select class="form-control" name="SEARCH_CATEGORY_ID" id="SEARCH_CATEGORY_ID" size="1">
              <option value="${searchCategoryId!}">${uiLabelMap.ProductEntireCatalog}</option>
              <#list otherSearchProdCatalogCategories as otherSearchProdCatalogCategory>
                <#assign searchProductCategory = otherSearchProdCatalogCategory.getRelatedOne("ProductCategory", true)>
                <#if searchProductCategory??>
                  <option value="${searchProductCategory.productCategoryId}">${searchProductCategory.description?default("No Description " + searchProductCategory.productCategoryId)}</option>
                </#if>
              </#list>
            </select>
        <#else>
          <input class="form-control" type="hidden" name="SEARCH_CATEGORY_ID" value="${searchCategoryId!}" />
        </#if>
          <input type="submit" value="${uiLabelMap.ProductAdvancedSearch}" class="btn btn-primary1" />

    </form>
  </div>
</div>
</div>
</div>
</article>
</div>
