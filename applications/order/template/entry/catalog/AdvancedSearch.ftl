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
<#assign searchOptionsHistoryList = Static["org.apache.ofbiz.product.product.ProductSearchSession"].getSearchOptionsHistoryList(session)>
<#assign currentCatalogId = Static["org.apache.ofbiz.product.catalog.CatalogWorker"].getCurrentCatalogId(request)>
<article class="col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-8" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductAdvancedSearchInCategory}</h2>
</header>
<div role="content">

<form name="advtokeywordsearchform"  method="post" action="<@ofbizUrl>keywordsearch</@ofbizUrl>" style="margin: 0;">
  <input type="hidden" name="VIEW_SIZE" value="10" />
  <table border="0" style="width:100%">
    <input type="hidden" name="SEARCH_CATALOG_ID" value="${currentCatalogId}" />
    <#if searchCategory?has_content>
        <input type="hidden" name="SEARCH_CATEGORY_ID" value="${searchCategoryId!}" />
        <tr>
          <td>
            <label>${uiLabelMap.ProductCategory}:</label>
          </td>
          <td>
            <div>
              <b>"${(searchCategory.description)!}"</b>${uiLabelMap.ProductIncludeSubCategories}
            <div style="display: -webkit-inline-box;">
	    <label style="margin:0" class="radio radio-inline">
	    <input class="radiobox" type="radio" name="SEARCH_SUB_CATEGORIES" value="Y" checked="checked" /><span>${uiLabelMap.CommonYes}</span>
	    </label>
	    <label style="margin:0" class="radio radio-inline">
	    <input class="radiobox" type="radio" name="SEARCH_SUB_CATEGORIES" value="N" />
	    <span>${uiLabelMap.CommonNo}</span>
	    </label>
            </div>
            </div>
          </td>
        </tr>
    </#if>
    <tr>
      <td>
        <label>${uiLabelMap.ProductKeywords}:</label>
      </td>
      <td>
        <div>
          <input type="text" name="SEARCH_STRING" size="40" value="${requestParameters.SEARCH_STRING!}" />&nbsp;
            <div style="display: -webkit-inline-box;">
	    <label style="margin:0" class="radio radio-inline">
	    <input type="radio" class="radiobox" name="SEARCH_OPERATOR" value="OR" <#if searchOperator == "OR">checked="checked"</#if> /><span>${uiLabelMap.CommonAny}</span>
	    </label>
	    <label style="margin:0" class="radio radio-inline">
	    <input type="radio" class="radiobox" name="SEARCH_OPERATOR" value="AND" <#if searchOperator == "AND">checked="checked"</#if> /><span>${uiLabelMap.CommonAll}</span>
	    </label>
            </div>
        </div>
      </td>
    </tr>
    <#list productFeatureTypeIdsOrdered as productFeatureTypeId>
      <#assign findPftMap = Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("productFeatureTypeId", productFeatureTypeId)>
      <#assign productFeatureType = delegator.findOne("ProductFeatureType", findPftMap, true)>
      <#assign productFeatures = productFeaturesByTypeMap[productFeatureTypeId]>
      <tr>
        <td>
          <label>${(productFeatureType.get("description",locale))!}:</label>
        </td>
        <td>
          <div>
            <select name="pft_${productFeatureTypeId}">
              <option value="">- ${uiLabelMap.CommonSelectAny} -</option>
              <#list productFeatures as productFeature>
              <option value="${productFeature.productFeatureId}">${productFeature.get("description",locale)?default(productFeature.productFeatureId)}</option>
              </#list>
            </select>
          </div>
        </td>
      </tr>
    </#list>
    <tr>
      <td>
        <label>${uiLabelMap.ProductSupplier}:</label>
      </td>
      <td>
        <div>
          <select name="SEARCH_SUPPLIER_ID">
            <option value="">- ${uiLabelMap.CommonSelectAny} -</option>
            <#list supplerPartyRoleAndPartyDetails as supplerPartyRoleAndPartyDetail>
              <option value="${supplerPartyRoleAndPartyDetail.partyId}"<#if (sessionAttributes.orderPartyId?? & sessionAttributes.orderPartyId = supplerPartyRoleAndPartyDetail.partyId)> selected="selected"</#if>>${supplerPartyRoleAndPartyDetail.groupName!} ${supplerPartyRoleAndPartyDetail.firstName!} ${supplerPartyRoleAndPartyDetail.lastName!} [${supplerPartyRoleAndPartyDetail.partyId}]</option>
            </#list>
          </select>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <label>${uiLabelMap.CommonSortedBy}:</label>
      </td>
      <td>
        <div>
          <select name="sortOrder">
            <option value="SortKeywordRelevancy">${uiLabelMap.ProductKeywordRelevancy}</option>
            <option value="SortProductField:productName">${uiLabelMap.ProductProductName}</option>
            <option value="SortProductField:internalName">${uiLabelMap.ProductInternalName}</option>
            <option value="SortProductField:totalQuantityOrdered">${uiLabelMap.ProductPopularityByOrders}</option>
            <option value="SortProductField:totalTimesViewed">${uiLabelMap.ProductPopularityByViews}</option>
            <option value="SortProductField:averageCustomerRating">${uiLabelMap.ProductCustomerRating}</option>
            <option value="SortProductPrice:LIST_PRICE">${uiLabelMap.ProductListPrice}</option>
            <option value="SortProductPrice:DEFAULT_PRICE">${uiLabelMap.ProductDefaultPrice}</option>
            <option value="SortProductPrice:AVERAGE_COST">${uiLabelMap.ProductAverageCost}</option>
          </select>
            <div style="display: -webkit-inline-box;">
	    <label style="margin:0" class="radio radio-inline">
	    <input type="radio" class="radiobox" name="sortAscending" value="Y" checked="checked" /><span>${uiLabelMap.ProductLowToHigh}</span>
	    </label>
	    <label style="margin:0" class="radio radio-inline">
	    <input type="radio" class="radiobox" name="sortAscending" value="N" /><span>${uiLabelMap.ProductHighToLow}</span>
	    </label>
            </div>
        </div>
      </td>
    </tr>
    <#if searchConstraintStrings?has_content>
      <tr>
        <td>
          <label>${uiLabelMap.ProductLastSearch}:</label>
        </td>
        <td>
<br/>
            <#list searchConstraintStrings as searchConstraintString>
                <div>&nbsp;-&nbsp;${searchConstraintString}</div>
            </#list>
            <div>${uiLabelMap.CommonSortedBy}: ${searchSortOrderString}</div>
            <div style="display: -webkit-inline-box;">
	    <label style="margin:0" class="radio radio-inline">
	    <input type="radio" class="radiobox" name="clearSearch" value="Y" checked="checked" /><span>${uiLabelMap.ProductNewSearch}</span>
	    </label>
	    <label style="margin:0" class="radio radio-inline">
	    <input type="radio" class="radiobox" name="clearSearch" value="N" /><span>${uiLabelMap.CommonRefineSearch}</span>
	    </label>
            </div>
        </td>
      </tr>
    </#if>
    <tr>
      <td colspan="2">
        <div>
          <a href="javascript:document.advtokeywordsearchform.submit()" class="btn btn-primary1">${uiLabelMap.CommonFind}</a>
        </div>
      </td>
    </tr>
  </table>

  <#if searchOptionsHistoryList?has_content>
    <hr />

    <h2>${uiLabelMap.OrderLastSearches}...</h2>

    <div>
      <a href="<@ofbizUrl>clearSearchOptionsHistoryList</@ofbizUrl>" class="btn btn-primary">${uiLabelMap.OrderClearSearchHistory}</a>
      ${uiLabelMap.OrderClearSearchHistoryNote}
    </div>
    <#list searchOptionsHistoryList as searchOptions>
    <#-- searchOptions type is ProductSearchSession.ProductSearchOptions -->
        <div>
          <b>${uiLabelMap.CommonSearch} #${searchOptions_index + 1}</b>
          <a href="<@ofbizUrl>setCurrentSearchFromHistoryAndSearch?searchHistoryIndex=${searchOptions_index}&amp;clearSearch=N</@ofbizUrl>" class="button1 facebook">${uiLabelMap.CommonSearch}</a>
          <a href="<@ofbizUrl>setCurrentSearchFromHistory?searchHistoryIndex=${searchOptions_index}</@ofbizUrl>" class="button1 facebook">${uiLabelMap.CommonRefine}</a>
        </div>
        <#assign constraintStrings = searchOptions.searchGetConstraintStrings(false, delegator, locale)>
        <#list constraintStrings as constraintString>
          <div>&nbsp;-&nbsp;${constraintString}</div>
        </#list>
        <#if searchOptions_has_next>
          <hr />
        </#if>
    </#list>
  </#if>
</form>
</div>
</div>
</article>
