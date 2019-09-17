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
<div id="content-main-section" class="leftonly">
<div id="centerdiv" class="no-clear">
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">

<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductAdvancedSearchInCategory}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-body">
    <form name="advtokeywordsearchform" method="post" action="<@ofbizUrl>keywordsearch</@ofbizUrl>" style="margin: 0;">
      <input type="hidden" name="VIEW_SIZE" value="25"/>
      <input type="hidden" name="PAGING" value="Y"/>
      <input type="hidden" name="noConditionFind" value="Y"/>
      <#if searchCategory?has_content>
          <input type="hidden" name="SEARCH_CATEGORY_ID" value="${searchCategoryId!}"/>
      </#if>
      <table cellspacing="0" class="basic-table">
        <#if searchCategory?has_content>
            <tr>
              <td>
                <label>${uiLabelMap.ProductCategory}: </label>
              </td>
              <td valign="middle">
                <div>
                  <b>"${(searchCategory.description)!}" [${(searchCategory.productCategoryId)!}]</b> ${uiLabelMap.ProductIncludeSubCategories}
                  <label>${uiLabelMap.CommonYes}<input type="radio" name="SEARCH_SUB_CATEGORIES" value="Y" checked="checked" /></label>
                  <label>${uiLabelMap.CommonNo}<input type="radio" name="SEARCH_SUB_CATEGORIES" value="N"/></label>
                </div>
              </td>
            </tr>
        <#else>
            <tr>
               <td >
                 <label style="width:160px">${uiLabelMap.ProductCatalog}:</label>
               </td>
               <td valign="middle">
                 <div>
                    <select class="form-control" name="SEARCH_CATALOG_ID">
                      <option value="">- ${uiLabelMap.ProductAnyCatalog} -</option>
                      <#list prodCatalogs as prodCatalog>
                        <#assign displayDesc = prodCatalog.catalogName?default("${uiLabelMap.ProductNoDescription}")>
                          <#if 18 < displayDesc?length>
                            <#assign displayDesc = displayDesc[0..15] + "...">
                          </#if>
                          <option value="${prodCatalog.prodCatalogId}">${displayDesc} [${prodCatalog.prodCatalogId}]</option>
                      </#list>
                    </select>
                 </div>
               </td>
            </tr>
            <tr>
              <td>
                <label>${uiLabelMap.ProductCategory}:</label>
              </td>
              <td id="mmomes" valign="middle" width="100%">
                  <@htmlTemplate.lookupField value="${requestParameters.SEARCH_CATEGORY_ID!}" formName="advtokeywordsearchform" name="SEARCH_CATEGORY_ID" id="SEARCH_CATEGORY_ID" fieldFormName="LookupProductCategory"/>
                  <div>${uiLabelMap.ProductIncludeSubCategories}
                  <label style="margin-top:0px !important;" class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_SUB_CATEGORIES" value="Y" checked="checked"/><span>${uiLabelMap.CommonYes}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_SUB_CATEGORIES" value="N"/><span>${uiLabelMap.CommonNo}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_CATEGORY_EXC" value="" checked="checked"/><span>${uiLabelMap.CommonInclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_CATEGORY_EXC" value="Y"/><span>${uiLabelMap.CommonExclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_CATEGORY_EXC" value="N"/><span>${uiLabelMap.CommonAlwaysInclude}</span></label>
                </div>
              </td>
            </tr>
        </#if>
        <tr>
          <td>
            <label>${uiLabelMap.ProductProductName}:</label>
          </td>
          <td valign="middle">
              <input class="form-control" type="text" name="SEARCH_PRODUCT_NAME" size="20" value="${requestParameters.SEARCH_PRODUCT_NAME!}"/>
          </td>
        </tr>
        <tr>
          <td>
            <label>${uiLabelMap.ProductInternalName}:</label>
          </td>
          <td valign="middle">
              <input class="form-control" type="text" name="SEARCH_INTERNAL_PROD_NAME" size="20" value="${requestParameters.SEARCH_INTERNAL_PROD_NAME!}"/>
          </td>
        </tr>
        <tr>
          <td>
            <label>${uiLabelMap.ProductKeywords}:</label>
          </td>
          <td valign="middle">
              <input class="form-control" type="text" name="SEARCH_STRING" size="40" value="${requestParameters.SEARCH_STRING!}"/>&nbsp;
              <label style="margin-top:0px !important;" class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_OPERATOR" value="OR" <#if searchOperator == "OR">checked="checked"</#if>/><span>${uiLabelMap.CommonAny}</span></label>
              <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_OPERATOR" value="AND" <#if searchOperator == "AND">checked="checked"</#if>/><span>${uiLabelMap.CommonAll}</span></label><br/><br/>
          </td>
        </tr>
        <tr>
          <td>
            <label>${uiLabelMap.ProductFeatureCategory} ${uiLabelMap.CommonIds}:</label>
          </td>
          <td valign="middle">
            <div>
              <input class="form-control" type="text" name="SEARCH_PROD_FEAT_CAT1" size="15" value="${requestParameters.SEARCH_PROD_FEAT_CAT1!}"/>&nbsp;
                  <label style="margin-top:0px !important;" class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_CAT_EXC1" value="" checked="checked"/><span>${uiLabelMap.CommonInclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_CAT_EXC1" value="Y"/><span>${uiLabelMap.CommonExclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_CAT_EXC1" value="N"/><span>${uiLabelMap.CommonAlwaysInclude}</span></label>
            </div>
            <div>
              <input class="form-control" type="text" name="SEARCH_PROD_FEAT_CAT2" size="15" value="${requestParameters.SEARCH_PROD_FEAT_CAT2!}"/>&nbsp;
                  <label style="margin-top:0px !important;" class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_CAT_EXC2" value="" checked="checked"/><span>${uiLabelMap.CommonInclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_CAT_EXC2" value="Y"/><span>${uiLabelMap.CommonExclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_CAT_EXC2" value="N"/><span>${uiLabelMap.CommonAlwaysInclude}</span></label>
            </div>
            <div>
              <input class="form-control" type="text" name="SEARCH_PROD_FEAT_CAT3" size="15" value="${requestParameters.SEARCH_PROD_FEAT_CAT3!}"/>&nbsp;
                  <label style="margin-top:0px !important;" class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_CAT_EXC3" value="" checked="checked"/><span>${uiLabelMap.CommonInclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_CAT_EXC3" value="Y"/><span>${uiLabelMap.CommonExclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_CAT_EXC3" value="N"/><span>${uiLabelMap.CommonAlwaysInclude}</span></label><br/><br/>
            </div>
          </td>
        </tr>
        <tr>
          <td>
             <label>${uiLabelMap.ProductFeatureGroup} ${uiLabelMap.CommonIds}: </label>
          </td>
          <td valign="middle">
            <div>
              <input class="form-control" type="text" name="SEARCH_PROD_FEAT_GRP1" size="15" value="${requestParameters.SEARCH_PROD_FEAT_GRP1!}"/>&nbsp;
                  <label style="margin-top:0px !important;" class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_GRP_EXC1" value="" checked="checked"/><span>${uiLabelMap.CommonInclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_GRP_EXC1" value="Y"/><span>${uiLabelMap.CommonExclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_GRP_EXC1" value="N"/><span>${uiLabelMap.CommonAlwaysInclude}</span></label>
            </div>
            <div>
              <input class="form-control" type="text" name="SEARCH_PROD_FEAT_GRP2" size="15" value="${requestParameters.SEARCH_PROD_FEAT_GRP2!}"/>&nbsp;
                  <label style="margin-top:0px !important;" class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_GRP_EXC2" value="" checked="checked"/><span>${uiLabelMap.CommonInclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_GRP_EXC2" value="Y"/><span>${uiLabelMap.CommonExclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_GRP_EXC2" value="N"/><span>${uiLabelMap.CommonAlwaysInclude}</span></label>
            </div>
            <div>
              <input class="form-control" type="text" name="SEARCH_PROD_FEAT_GRP3" size="15" value="${requestParameters.SEARCH_PROD_FEAT_GRP3!}"/>&nbsp;
                  <label style="margin-top:0px !important;" class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_GRP_EXC3" value="" checked="checked"/><span>${uiLabelMap.CommonInclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_GRP_EXC3" value="Y"/><span>${uiLabelMap.CommonExclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_PROD_FEAT_GRP_EXC3" value="N"/><span>${uiLabelMap.CommonAlwaysInclude}</span></label><br/><br/>
            </div>
          </td>
        </tr>

        <tr>
          <td>
           <label> ${uiLabelMap.ProductFeatures} ${uiLabelMap.CommonIds}:</label>
          </td>
          <td valign="middle">
            <div>
              <input class="form-control" type="text" name="SEARCH_FEAT1" size="15" value="${requestParameters.SEARCH_FEAT1!}"/>&nbsp;
                  <label style="margin-top:0px !important;" class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_FEAT_EXC1" value="" checked="checked"/><span>${uiLabelMap.CommonInclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_FEAT_EXC1" value="Y"/><span>${uiLabelMap.CommonExclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_FEAT_EXC1" value="N"/><span>${uiLabelMap.CommonAlwaysInclude}</span></label>
            </div>
            <div>
              <input class="form-control" type="text" name="SEARCH_FEAT2" size="15" value="${requestParameters.SEARCH_FEAT2!}"/>&nbsp;
                  <label style="margin-top:0px !important;" class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_FEAT_EXC2" value="" checked="checked"/><span>${uiLabelMap.CommonInclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_FEAT_EXC2" value="Y"/><span>${uiLabelMap.CommonExclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_FEAT_EXC2" value="N"/><span>${uiLabelMap.CommonAlwaysInclude}</span></label>
            </div>
            <div>
              <input class="form-control" type="text" name="SEARCH_FEAT3" size="15" value="${requestParameters.SEARCH_FEAT3!}"/>&nbsp;
                  <label style="margin-top:0px !important;" class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_FEAT_EXC3" value="" checked="checked"/><span>${uiLabelMap.CommonInclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_FEAT_EXC3" value="Y"/><span>${uiLabelMap.CommonExclude}</span></label>
                  <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_FEAT_EXC3" value="N"/><span>${uiLabelMap.CommonAlwaysInclude}</span></label><br/><br/>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <label>${uiLabelMap.ProductListPriceRange}:</label>
          </td>
          <td valign="middle">
            <div>
              <input class="form-control" type="text" name="LIST_PRICE_LOW" size="8" value="${requestParameters.LIST_PRICE_LOW!}"/>&nbsp;
              <input class="form-control" type="text" name="LIST_PRICE_HIGH" size="8" value="${requestParameters.LIST_PRICE_HIGH!}"/>&nbsp;
            </div>
          </td>
        </tr>
        <#list productFeatureTypeIdsOrdered as productFeatureTypeId>
          <#assign findPftMap = Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("productFeatureTypeId", productFeatureTypeId)>
          <#assign productFeatureType = delegator.findOne("ProductFeatureType", findPftMap, true)>
          <#assign productFeatures = productFeaturesByTypeMap[productFeatureTypeId]>
          <tr>
            <td>
             <label> ${(productFeatureType.get("description",locale))!}:</label>
            </td>
            <td valign="middle">
              <div>
                <select class="form-control" name="pft_${productFeatureTypeId}">
                  <option value="">- ${uiLabelMap.CommonSelectAny} -</option>
                  <#list productFeatures as productFeature>
                  <option value="${productFeature.productFeatureId}">${productFeature.description?default("${uiLabelMap.ProductNoDescription}")} [${productFeature.productFeatureId}]</option>
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
          <td valign="middle">
            <div>
              <select class="form-control" name="SEARCH_SUPPLIER_ID">
                <option value="">- ${uiLabelMap.CommonSelectAny} -</option>
                <#list supplerPartyRoleAndPartyDetails as supplerPartyRoleAndPartyDetail>
                  <option value="${supplerPartyRoleAndPartyDetail.partyId}">${supplerPartyRoleAndPartyDetail.groupName!} ${supplerPartyRoleAndPartyDetail.firstName!} ${supplerPartyRoleAndPartyDetail.lastName!} [${supplerPartyRoleAndPartyDetail.partyId}]</option>
                </#list>
              </select>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <label>${uiLabelMap.CommonSortedBy}:</label>
          </td>
          <td valign="middle">
            <div>
              <select class="form-control" name="sortOrder">
                <option value="SortKeywordRelevancy">${uiLabelMap.ProductKeywordRelevancy}</option>
                <option value="SortProductField:productName">${uiLabelMap.ProductProductName}</option>
                <option value="SortProductField:internalName">${uiLabelMap.ProductInternalName}</option>
                <option value="SortProductField:totalQuantityOrdered">${uiLabelMap.ProductPopularityByOrders}</option>
                <option value="SortProductField:totalTimesViewed">${uiLabelMap.ProductPopularityByViews}</option>
                <option value="SortProductField:averageCustomerRating">${uiLabelMap.ProductCustomerRating}</option>
                <option value="SortProductPrice:LIST_PRICE">${uiLabelMap.ProductListPrice}</option>
                <option value="SortProductPrice:DEFAULT_PRICE">${uiLabelMap.ProductDefaultPrice}</option>
                <option value="SortProductPrice:AVERAGE_COST">${uiLabelMap.ProductAverageCost}</option>
                <option value="SortProductPrice:MINIMUM_PRICE">${uiLabelMap.ProductMinimumPrice}</option>
                <option value="SortProductPrice:MAXIMUM_PRICE">${uiLabelMap.ProductMaximumPrice}</option>
              </select>
              <label style="margin-top:0px !important;" class="radio radio-inline"><input type="radio" class="radiobox" name="sortAscending" value="Y" checked="checked" /><span>${uiLabelMap.ProductLowToHigh}</span></label>
              <label class="radio radio-inline"><input type="radio" class="radiobox" name="sortAscending" value="N"/><span>${uiLabelMap.ProductHighToLow}</span></label>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <label>${uiLabelMap.ProductPrioritizeProductsInCategory}:</label>
          </td>
          <td id="mmomes" valign="middle">
            <@htmlTemplate.lookupField value="${requestParameters.PRIORITIZE_CATEGORY_ID!}" formName="advtokeywordsearchform" name="PRIORITIZE_CATEGORY_ID" id="PRIORITIZE_CATEGORY_ID" fieldFormName="LookupProductCategory"/>
          </td>
        </tr>
        <tr>
          <td>
            <label>${uiLabelMap.ProductGoodIdentificationType}:</label>
          </td>
          <td>
            <select class="form-control" name="SEARCH_GOOD_IDENTIFICATION_TYPE">
              <option value="">- ${uiLabelMap.CommonSelectAny} -</option>
              <#list goodIdentificationTypes as goodIdentificationType>
              <option value="${goodIdentificationType.goodIdentificationTypeId}">${goodIdentificationType.get("description")!}</option>
              </#list>
            </select>
          </td>
        </tr>
        <tr>
          <td>
            <label>${uiLabelMap.ProductGoodIdentificationValue}:</label>
          </td>
          <td>
            <input class="form-control" type="text" name="SEARCH_GOOD_IDENTIFICATION_VALUE" size="60" maxlength="60" value="${requestParameters.SEARCH_GOOD_IDENTIFICATION_VALUE!}"/>
            <label style="margin-top:0px !important;" class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_GOOD_IDENTIFICATION_INCL" value="Y" checked="checked"/><span>${uiLabelMap.CommonInclude}</span></label>
            <label class="radio radio-inline"><input class="radiobox" type="radio" name="SEARCH_GOOD_IDENTIFICATION_INCL" value="N"/><span>${uiLabelMap.CommonExclude}</span></label>
          </td>
        </tr>
        <#if searchConstraintStrings?has_content>
          <tr>
            <td>
              <label>${uiLabelMap.ProductLastSearch}</label>
            </td>
            <td valign="top">
                <#list searchConstraintStrings as searchConstraintString>
                    <div>&nbsp;-&nbsp;${searchConstraintString}</div>
                </#list>
                <label>${uiLabelMap.CommonSortedBy}:</label>${searchSortOrderString}
                <div>
                  <label>${uiLabelMap.ProductNewSearch}<input type="radio" name="clearSearch" value="Y" checked="checked"/></label>
                  <label>${uiLabelMap.CommonRefineSearch}<input type="radio" name="clearSearch" value="N"/></label>
                </div>
            </td>
          </tr>
        </#if>
        <tr>
          <td align="center" colspan="2">
            <hr />
            <a class="btn btn-primary1" href="javascript:document.advtokeywordsearchform.submit()">${uiLabelMap.CommonFind}</a>
          </td>
        </tr>
      </table>
    </form>
  </div>
</div>
</div>
</div>
</article>
</div>
</div>
</div>
