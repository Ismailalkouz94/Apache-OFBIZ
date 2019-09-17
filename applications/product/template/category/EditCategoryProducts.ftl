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
<#if activeOnly>
    <a href="<@ofbizUrl>EditCategoryProducts?productCategoryId=${productCategoryId!}&amp;activeOnly=false</@ofbizUrl>" class="button1 facebook">${uiLabelMap.ProductActiveAndInactive}</a>
<#else>
    <a href="<@ofbizUrl>EditCategoryProducts?productCategoryId=${productCategoryId!}&amp;activeOnly=true</@ofbizUrl>" class="button1 facebook">${uiLabelMap.ProductActiveOnly}</a>
</#if>
<div class="row">

<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-3" data-widget-editbutton="false">
<header>
<h2></h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-title-bar">
        <#if (listSize > 0)>
            <div class="boxhead-right">
                <#if (viewIndex > 1)>
                    <a href="<@ofbizUrl>EditCategoryProducts?productCategoryId=${productCategoryId!}&amp;VIEW_SIZE=${viewSize}&amp;VIEW_INDEX=${viewIndex-1}&amp;activeOnly=${activeOnly.toString()}</@ofbizUrl>" class="submenutext">${uiLabelMap.CommonPrevious}</a> |
                </#if>
                <span class="submenutextinfo">${lowIndex} - ${highIndex} ${uiLabelMap.CommonOf} ${listSize}</span>
                <#if (listSize > highIndex)>
                    | <a class="lightbuttontext" href="<@ofbizUrl>EditCategoryProducts?productCategoryId=${productCategoryId!}&amp;VIEW_SIZE=${viewSize}&amp;VIEW_INDEX=${viewIndex+1}&amp;activeOnly=${activeOnly.toString()}</@ofbizUrl>" class="submenutextright">${uiLabelMap.CommonNext}</a>
                </#if>
                &nbsp;
            </div>
            <div class="boxhead-left">
                ${uiLabelMap.PageTitleEditCategoryProducts}
            </div>
            <div class="boxhead-fill">&nbsp;</div>
        </#if>
    </div>
    <div class="screenlet-body">
        <#if (listSize == 0)>
           <table cellspacing="0" class="table table-bordered table-striped">
             <thead>
              <tr class="header-row">
                 <th>${uiLabelMap.ProductProductNameId}</th>
                 <th>${uiLabelMap.CommonFromDateTime}</th>
                 <th align="center">${uiLabelMap.ProductThruDateTimeSequenceQuantity} ${uiLabelMap.CommonComments}</th>
                 <th>&nbsp;</th>
              </tr>
              </thead>
           </table>
        <#else>
           <form method="post" action="<@ofbizUrl>updateCategoryProductMember</@ofbizUrl>" name="updateCategoryProductForm">
              <input type="hidden" name="VIEW_SIZE" value="${viewSize}"/>
              <input type="hidden" name="VIEW_INDEX" value="${viewIndex}"/>
              <input type="hidden" name="activeOnly" value="${activeOnly.toString()}" />
              <input type="hidden" name="productCategoryId" value="${productCategoryId!}" />
              <table cellspacing="0" class="table table-bordered table-striped">
             <thead>
              <tr class="header-row">
                    <th>${uiLabelMap.ProductProductNameId}</th>
                    <th>${uiLabelMap.CommonFromDateTime}</th>
                    <th align="center">${uiLabelMap.ProductThruDateTimeSequenceQuantity} ${uiLabelMap.CommonComments}</th>
              </tr>
              </thead>
              <#assign rowClass = "2">
              <#assign rowCount = 0>
              <#list productCategoryMembers as productCategoryMember>
                <#assign suffix = "_o_" + productCategoryMember_index>
                <#assign product = productCategoryMember.getRelatedOne("Product", false)>
                <#assign hasntStarted = false>
                <#if productCategoryMember.fromDate?? && nowTimestamp.before(productCategoryMember.getTimestamp("fromDate"))><#assign hasntStarted = true></#if>
                <#assign hasExpired = false>
                <#if productCategoryMember.thruDate?? && nowTimestamp.after(productCategoryMember.getTimestamp("thruDate"))><#assign hasExpired = true></#if>
                  <tr valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>
                    <td>
                      <#if (product.smallImageUrl)??>
                         <a href="<@ofbizUrl>EditProduct?productId=${(productCategoryMember.productId)!}</@ofbizUrl>"><img alt="Small Image" src="<@ofbizContentUrl>${product.smallImageUrl}</@ofbizContentUrl>" class="cssImgSmall" align="middle" /></a>
                      </#if>
                      <a href="<@ofbizUrl>EditProduct?productId=${(productCategoryMember.productId)!}</@ofbizUrl>" class="buttontext"><#if product??>${(product.internalName)!}</#if> [${(productCategoryMember.productId)!}]</a>
                    </td>
                    <td <#if hasntStarted> style="color: red;"</#if>>${(productCategoryMember.fromDate)!}</td>
                    <td align="center">
                        <input type="hidden" name="productId${suffix}" value="${(productCategoryMember.productId)!}" />
                        <input type="hidden" name="productCategoryId${suffix}" value="${(productCategoryMember.productCategoryId)!}" />
                        <input type="hidden" name="fromDate${suffix}" value="${(productCategoryMember.fromDate)!}" />
                        <#if hasExpired><#assign class="alert"><#else><#assign class=""></#if>
                        <@htmlTemplate.renderDateTimeField name="thruDate${suffix}" event="" action="" className="${class!}" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" value="${(productCategoryMember.thruDate)!}" size="25" maxlength="30" id="thruDate${suffix}" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                        <input type="text" size="5" name="sequenceNum${suffix}" value="${(productCategoryMember.sequenceNum)!}" />
                        <input type="text" size="5" name="quantity${suffix}" value="${(productCategoryMember.quantity)!}" />
                        <br />
                        <textarea name="comments${suffix}" rows="2" cols="40">${(productCategoryMember.comments)!}</textarea>
                    </td>
                    <td align="center">
                      <a href="javascript:document.deleteProductFromCategory_o_${rowCount}.submit()" class="buttontext">${uiLabelMap.CommonDelete}</a>
                    </td>
                  </tr>
                  <#-- toggle the row color -->
                  <#if rowClass == "2">
                      <#assign rowClass = "1">
                  <#else>
                      <#assign rowClass = "2">
                  </#if>
                  <tr valign="middle">
                      <td colspan="4" align="center">
                          <input type="submit" value="${uiLabelMap.CommonUpdate}" style="font-size: x-small;" />
                          <input type="hidden" value="${productCategoryMembers.size()}" name="_rowCount" />
                      </td>
                  </tr>
                  <#assign rowCount = rowCount + 1>
              </#list>
              </table>
           </form>
           <#assign rowCount = 0>
           <#list productCategoryMembers as productCategoryMember>
           <form name="deleteProductFromCategory_o_${rowCount}" method="post" action="<@ofbizUrl>removeCategoryProductMember</@ofbizUrl>">
              <input type="hidden" name="VIEW_SIZE" value="${viewSize}"/>
              <input type="hidden" name="VIEW_INDEX" value="${viewIndex}"/>
              <input type="hidden" name="productId" value="${(productCategoryMember.productId)!}" />
              <input type="hidden" name="productCategoryId" value="${(productCategoryMember.productCategoryId)!}"/>
              <input type="hidden" name="fromDate" value="${(productCategoryMember.fromDate)!}"/>
              <input type="hidden" name="activeOnly" value="${activeOnly.toString()}"/>
           </form>
           <#assign rowCount = rowCount + 1>
           </#list>        
      </#if>
    </div>
    <div class="screenlet-title-bar">
        <#if (listSize > 0)>
            <div class="boxhead-right">
                <#if (viewIndex > 1)>
                    <a href="<@ofbizUrl>EditCategoryProducts?productCategoryId=${productCategoryId!}&amp;VIEW_SIZE=${viewSize}&amp;VIEW_INDEX=${viewIndex-1}&amp;activeOnly=${activeOnly.toString()}</@ofbizUrl>" class="submenutext">${uiLabelMap.CommonPrevious}</a> |
                </#if>
                <span class="submenutextinfo">${lowIndex} - ${highIndex} ${uiLabelMap.CommonOf} ${listSize}</span>
                <#if (listSize > highIndex)>
                    | <a class="lightbuttontext" href="<@ofbizUrl>EditCategoryProducts?productCategoryId=${productCategoryId!}&amp;VIEW_SIZE=${viewSize}&amp;VIEW_INDEX=${viewIndex+1}&amp;activeOnly=${activeOnly.toString()}</@ofbizUrl>" class="submenutextright">${uiLabelMap.CommonNext}</a>
                </#if>
                &nbsp;
            </div>
            <div class="boxhead-left">
                ${uiLabelMap.PageTitleEditCategoryProducts}
            </div>
            <div class="boxhead-fill">&nbsp;</div>
        </#if>
    </div>
</div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-4" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductAddProductCategoryMember}:</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <table cellspacing="0" class="basic-table">
            <tr><td>
                <form method="post" class="smart-form" action="<@ofbizUrl>addCategoryProductMember</@ofbizUrl>" style="margin: 0;" name="addProductCategoryMemberForm">
                    <input type="hidden" name="productCategoryId" value="${productCategoryId!}" />
                    <input type="hidden" name="activeOnly" value="${activeOnly.toString()}" />
                    <div>
                        <label>${uiLabelMap.ProductProductId}</label> &emsp;
                        <@htmlTemplate.lookupField formName="addProductCategoryMemberForm" name="productId" id="productId" fieldFormName="LookupProduct"/>
                        <br/>
                        <label>${uiLabelMap.CommonFromDate}</label> &emsp;
                        <@htmlTemplate.renderDateTimeField name="fromDate" event="" action="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" value="" size="25" maxlength="30" id="fromDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                        <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
                          <br />
                          <label>${uiLabelMap.CommonComments}</label> &emsp; <textarea class="form-control" name="comments" rows="2" cols="40"></textarea></br>
                          <input type="submit" class="btn btn-primary1" value="${uiLabelMap.CommonAdd}" />
                    </div>
                </form>
            </td></tr>
        </table>
    </div>
</div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-5" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductCopyProductCategoryMembersToAnotherCategory}:</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <table cellspacing="0" class="basic-table">
            <tr><td>
                <form method="post" class="smart-form" action="<@ofbizUrl>copyCategoryProductMembers</@ofbizUrl>" style="margin: 0;" name="copyCategoryProductMembersForm">
                    <input type="hidden" name="productCategoryId" value="${productCategoryId!}" />
                    <input type="hidden" name="activeOnly" value="${activeOnly.toString()}" />
                    <div>
                        <label>${uiLabelMap.ProductTargetProductCategory}</label> &emsp;
                        <@htmlTemplate.lookupField formName="copyCategoryProductMembersForm" name="productCategoryIdTo" id="productCategoryIdTo" fieldFormName="LookupProductCategory"/>
                        <br />
                        <label>${uiLabelMap.ProductOptionalFilterWithDate}</label> &emsp;
                        <@htmlTemplate.renderDateTimeField name="validDate" event="" action="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" value="" size="25" maxlength="30" id="validDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                        <br />
                        <label>${uiLabelMap.ProductIncludeSubCategories}</label> &emsp;
                        <select name="recurse">
                            <option value="N">${uiLabelMap.CommonN}</option>
                            <option value="Y">${uiLabelMap.CommonY}</option>
                        </select></br>
                        <input type="submit" class="btn btn-primary1" value="${uiLabelMap.CommonCopy}" />
                    </div>
                </form>
            </td></tr>
        </table>
    </div>
</div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-6" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductExpireAllProductMembers}:</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <table cellspacing="0" class="basic-table">
            <tr><td>
                <form method="post" class="smart-form" action="<@ofbizUrl>expireAllCategoryProductMembers</@ofbizUrl>" style="margin: 0;" name="expireAllCategoryProductMembersForm">
                    <input type="hidden" name="productCategoryId" value="${productCategoryId!}" />
                    <input type="hidden" name="activeOnly" value="${activeOnly.toString()}" />
                    <div>
                        <label>${uiLabelMap.ProductOptionalExpirationDate}</label> &emsp;
                        <@htmlTemplate.renderDateTimeField name="thruDate" event="" action="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" value="" size="25" maxlength="30" id="thruDate2" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                        </br><input type="submit" class="btn btn-primary1" value="${uiLabelMap.CommonExpireAll}" />
                    </div>
                </form>
            </td></tr>
        </table>
    </div>
</div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-7" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductRemoveExpiredProductMembers}:</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <table cellspacing="0" class="basic-table">
            <tr><td>
                <form method="post" class="smart-form" action="<@ofbizUrl>removeExpiredCategoryProductMembers</@ofbizUrl>" style="margin: 0;" name="removeExpiredCategoryProductMembersForm">
                    <input type="hidden" name="productCategoryId" value="${productCategoryId!}" />
                    <input type="hidden" name="activeOnly" value="${activeOnly.toString()}" />
                    <div>
                        <label>${uiLabelMap.ProductOptionalExpiredBeforeDate}</label> &emsp;
                        <@htmlTemplate.renderDateTimeField name="validDate" event="" action="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" value="" size="25" maxlength="30" id="validDate2" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                        </br><input type="submit" class="btn btn-primary1" value="${uiLabelMap.CommonRemoveExpired}" />
                    </div>
                </form>
            </td></tr>
        </table>
    </div>
</div>
</div>
</div>
</article>

</div>