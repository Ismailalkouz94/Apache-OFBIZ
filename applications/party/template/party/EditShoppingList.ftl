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


<section id="widget-grid">
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
     <div class="button-bar button-style-2">   <form id="createEmptyShoppingList" action="<@ofbizUrl>createEmptyShoppingList</@ofbizUrl>" method="post">
          <input type="hidden" name="partyId" value="${partyId!}" />
          <a class="button1 facebook" href="javascript:document.getElementById('createEmptyShoppingList').submit();" class="buttontext">${uiLabelMap.CommonCreateNew}</a>
        </form></div>
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.PartyShoppingLists}</h2>
</header>
<div role="content">

<div class="screenlet">

  <div class="screenlet-body">
    <#if shoppingLists?has_content>
      <form name="selectShoppingList" method="post" action="<@ofbizUrl>editShoppingList</@ofbizUrl>">
        <select class="form-control" name="shoppingListId">
          <#if shoppingList?has_content>
            <option value="${shoppingList.shoppingListId}">${shoppingList.listName}</option>
            <option value="${shoppingList.shoppingListId}">--</option>
          </#if>
          <#list allShoppingLists as list>
            <option value="${list.shoppingListId}">${list.listName}</option>
          </#list>
        </select><br />
        <input type="hidden" name="partyId" value="${partyId!}" />
        <a class="btn btn-primary1" href="javascript:document.selectShoppingList.submit();" class="smallSubmit">${uiLabelMap.CommonEdit}</a>
      </form><br />
    <#else>
      <div class="alert alert-info fade in"> ${uiLabelMap.PartyNoShoppingListsParty}.</div>
    </#if>
  </div>
</div>

</div>
</div>
</article>

<br />
<#if shoppingList?has_content>

<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">
<a href="javascript:document.updateList.submit();">${uiLabelMap.CommonSave}</a>
<form method="post" name="createQuoteFromShoppingListForm" action="/ordermgr/control/createQuoteFromShoppingList">
<input type= "hidden" name= "applyStorePromotions" value= "N"/>
<input type= "hidden" name= "shoppingListId" value= "${shoppingList.shoppingListId!}"/>
</form>
<a href="javascript:document.createQuoteFromShoppingListForm.submit()">${uiLabelMap.PartyCreateNewQuote}</a>
<a href="/ordermgr/control/createCustRequestFromShoppingList?shoppingListId=${shoppingList.shoppingListId!}">${uiLabelMap.PartyCreateNewCustRequest}</a>
<a href="/ordermgr/control/loadCartFromShoppingList?shoppingListId=${shoppingList.shoppingListId!}">${uiLabelMap.OrderNewOrder}</a>

<header>
<h2>${uiLabelMap.PartyShoppingListDetail} - ${shoppingList.listName}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-body">
    <form name="updateList" method="post" action="<@ofbizUrl>updateShoppingList</@ofbizUrl>">
      <input type="hidden" name="shoppingListId" value="${shoppingList.shoppingListId}" />
      <input type="hidden" name="partyId" value="${shoppingList.partyId!}" />
      <table class="basic-table" width="100%" cellspacing='0'>
        <tr><td>
          <label class="tdWid">${uiLabelMap.PartyListName}</label>
          <input class="form-control" type="text" size="25" name="listName" value="${shoppingList.listName}" <#if shoppingList.listName?default("") == "auto-save">disabled="disabled"</#if> />
        </tr>
        <tr> <td>
          <label class="tdWid">${uiLabelMap.CommonDescription}</label>
          <input class="form-control" type="text" size="70" name="description" value="${shoppingList.description!}" <#if shoppingList.listName?default("") == "auto-save">disabled="disabled"</#if> />
        </tr>
        <tr> <td>
          <label class="tdWid">${uiLabelMap.PartyListType}</label>
            <select class="form-control" name="shoppingListTypeId" <#if shoppingList.listName?default("") == "auto-save">disabled</#if>>
              <#if shoppingListType??>
                <option value="${shoppingListType.shoppingListTypeId}">${shoppingListType.get("description",locale)?default(shoppingListType.shoppingListTypeId)}</option>
                <option value="${shoppingListType.shoppingListTypeId}">--</option>
              </#if>
              <#list shoppingListTypes as shoppingListType>
                <option value="${shoppingListType.shoppingListTypeId}">${shoppingListType.get("description",locale)?default(shoppingListType.shoppingListTypeId)}</option>
              </#list>
            </select>
          </td>
        </tr>
        <tr>
          <td>
          <label class="tdWid">${uiLabelMap.PartyPublic}?</label>
            <select class="form-control" name="isPublic" <#if shoppingList.listName?default("") == "auto-save">disabled</#if>>
              <option>${shoppingList.isPublic}</option>
              <option value="${shoppingList.isPublic}">--</option>
              <option value="Y">${uiLabelMap.CommonYes}</option>
              <option value="N">${uiLabelMap.CommonNo}</option>
            </select>
          </td>
        </tr>
        <tr>
          <td>
          <label class="tdWid">${uiLabelMap.PartyParentList}</label>
            <select class="form-control" name="parentShoppingListId" <#if shoppingList.listName?default("") == "auto-save">disabled</#if>>
              <#if parentShoppingList??>
                <option value="${parentShoppingList.shoppingListId}">${parentShoppingList.listName?default(parentShoppingList.shoppingListId)}</option>
              </#if>
              <option value="">${uiLabelMap.PartyNoParent}</option>
              <#list allShoppingLists as newParShoppingList>
                <option value="${newParShoppingList.shoppingListId}">${newParShoppingList.listName?default(newParShoppingList.shoppingListId)}</option>
              </#list>
            </select>
            <#if parentShoppingList??>
              <a href="<@ofbizUrl>editShoppingList?shoppingListId=${parentShoppingList.shoppingListId}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.CommonGotoParent} (${parentShoppingList.listName?default(parentShoppingList.shoppingListId)})</a>
            </#if>
          </td>
        </tr>
        <#if shoppingList.listName?default("") != "auto-save">
          <tr>
            <td>&nbsp;</td>
            <td><a class="btn btn-primary1" href="javascript:document.updateList.submit();" class="smallSubmit">${uiLabelMap.CommonSave}</a></td>
          </tr>
        </#if>
      </table>
    </form>
  </div>
</div>

</div>
</div>
</article>
<#if childShoppingListDatas?has_content>
<br />
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">

<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">

<a href="<@ofbizUrl>addListToCart?shoppingListId=${shoppingList.shoppingListId}&amp;includeChild=yes</@ofbizUrl>">${uiLabelMap.PartyAddChildListsToCart}</a>

<header>
<h2>${uiLabelMap.PartyChildShoppingList} - ${shoppingList.listName}</h2>
</header>
<div role="content">

<div class="screenlet">
  <div class="screenlet-body">
    <table class="basic-table" cellspacing="0">
      <tr class="header-row">
        <td>${uiLabelMap.PartyListName}</td>
        <td>&nbsp;</td>
      </tr>
      <#list childShoppingListDatas as childShoppingListData>
        <#assign childShoppingList = childShoppingListData.childShoppingList>
        <tr>
          <td class="button-col"><a href="<@ofbizUrl>editShoppingList?shoppingListId=${childShoppingList.shoppingListId}</@ofbizUrl>">${childShoppingList.listName?default(childShoppingList.shoppingListId)}</a></li>
          <td class="button-col align-float">
            <a href="<@ofbizUrl>editShoppingList?shoppingListId=${childShoppingList.shoppingListId}</@ofbizUrl>">${uiLabelMap.PartyGotoList}</a>
            <a href="<@ofbizUrl>addListToCart?shoppingListId=${childShoppingList.shoppingListId}</@ofbizUrl>">${uiLabelMap.PartyAddListToCart}</a>
          </td>
        </tr>
      </#list>
    </table>
  </div>
</div>

</div>
</div>
</article>
</#if>
<br />
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">

<header>
<h2>${uiLabelMap.PartyListItems} - ${shoppingList.listName}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-body">
    <#if shoppingListItemDatas?has_content>
        <#-- Pagination -->
        <#assign commonUrl = "editShoppingList?partyId=" + partyId + "&shoppingListId="+(shoppingListId!)+"&"/>
        <#assign messageMap = Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("lowCount", lowIndex, "highCount", highIndex, "total", listSize)/>
        <#assign commonDisplaying = Static["org.apache.ofbiz.base.util.UtilProperties"].getMessage("CommonUiLabels", "CommonDisplaying", messageMap, locale)/>
        <@htmlTemplate.nextPrev commonUrl=commonUrl ajaxEnabled=false javaScriptEnabled=false paginateStyle="nav-pager" paginateFirstStyle="nav-first" viewIndex=viewIndex highIndex=highIndex listSize=listSize viewSize=viewSize ajaxFirstUrl="" firstUrl="" paginateFirstLabel="" paginatePreviousStyle="nav-previous" ajaxPreviousUrl="" previousUrl="" paginatePreviousLabel="" pageLabel="" ajaxSelectUrl="" selectUrl="" ajaxSelectSizeUrl="" selectSizeUrl="" commonDisplaying=commonDisplaying paginateNextStyle="nav-next" ajaxNextUrl="" nextUrl="" paginateNextLabel="" paginateLastStyle="nav-last" ajaxLastUrl="" lastUrl="" paginateLastLabel="" paginateViewSizeLabel="" />
      <table class="basic-table" cellspacing="0">
        <tr class="header-row">
          <td>${uiLabelMap.PartyProduct}</td>
          <td>${uiLabelMap.PartyQuantity}</td>
          <td>${uiLabelMap.PartyQuantityPurchased}</td>
          <td>${uiLabelMap.PartyPrice}</td>
          <td>${uiLabelMap.PartyTotal}</td>
          <td>&nbsp;</td>
        </tr>
        <#assign alt_row = false>
        <#list shoppingListItemDatas[lowIndex-1..highIndex-1] as shoppingListItemData>
          <#assign shoppingListItem = shoppingListItemData.shoppingListItem>
          <#assign product = shoppingListItemData.product>
          <#assign productContentWrapper = Static["org.apache.ofbiz.product.product.ProductContentWrapper"].makeProductContentWrapper(product, request)>
          <#assign unitPrice = shoppingListItemData.unitPrice>
          <#assign totalPrice = shoppingListItemData.totalPrice>
          <#assign productVariantAssocs = shoppingListItemData.productVariantAssocs!>
          <#assign isVirtual = product.isVirtual?? && product.isVirtual.equals("Y")>
          <tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
            <td><a href="/catalog/control/EditProduct?productId=${shoppingListItem.productId}&amp;externalLoginKey=${requestAttributes.externalLoginKey}">${shoppingListItem.productId} -
              ${productContentWrapper.get("PRODUCT_NAME", "html")?default("No Name")}</a> : ${productContentWrapper.get("DESCRIPTION", "html")!}
            </td>
            <form method="post" action="<@ofbizUrl>removeFromShoppingList</@ofbizUrl>" name='removeform_${shoppingListItem.shoppingListItemSeqId}'>
              <input type="hidden" name="shoppingListId" value="${shoppingListItem.shoppingListId}" />
              <input type="hidden" name="shoppingListItemSeqId" value="${shoppingListItem.shoppingListItemSeqId}" />
            </form>
            <form method="post" action="<@ofbizUrl>updateShoppingListItem</@ofbizUrl>" name='listform_${shoppingListItem.shoppingListItemSeqId}'>
              <input type="hidden" name="shoppingListId" value="${shoppingListItem.shoppingListId}" />
              <input type="hidden" name="shoppingListItemSeqId" value="${shoppingListItem.shoppingListItemSeqId}" />
              <td>
                <input size="6" type="text" name="quantity" value="${shoppingListItem.quantity?string.number}" />
              </td>
              <td>
                <input size="6" type="text" name="quantityPurchased"
                  <#if shoppingListItem.quantityPurchased?has_content>
                    value="${shoppingListItem.quantityPurchased!?string.number}"
                  </#if> />
              </td>
            </form>
            <td class="align-float"><@ofbizCurrency amount=unitPrice isoCode=currencyUomId/></td>
            <td class="align-float"><@ofbizCurrency amount=totalPrice isoCode=currencyUomId/></td>
            <td class="button-col align-float">
              <a href="javascript:document.listform_${shoppingListItem.shoppingListItemSeqId}.submit();">${uiLabelMap.CommonUpdate}</a>
              <a href="javascript:document.removeform_${shoppingListItem.shoppingListItemSeqId}.submit();">${uiLabelMap.CommonRemove}</a>
            </td>
          </tr>
          <#-- toggle the row color -->

          <#assign alt_row = !alt_row>
        </#list>
      </table>
    <#else>
     <div class="alert alert-info fade in"> ${uiLabelMap.PartyShoppingListEmpty}.</div>
    </#if>
  </div>
</div>
</div>
</div>
</article>
<br />
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">

<header>
<h2>${uiLabelMap.PartyQuickAddList}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-body">
    <form name="addToShoppingList" method="post" action="<@ofbizUrl>addItemToShoppingList</@ofbizUrl>">
      <input type="hidden" name="shoppingListId" value="${shoppingList.shoppingListId}" />
      <input type="hidden" name="partyId" value="${shoppingList.partyId!}" />
      <@htmlTemplate.lookupField formName="addToShoppingList" name="productId" id="productId" fieldFormName="LookupProduct"/>
      <br/><input class="form-control" type="text" size="5" name="quantity" value="${requestParameters.quantity?default("1")}" />
      <br/><input class="btn btn-primary1" type="submit" value="${uiLabelMap.PartyAddToShoppingList}" />
    </form>
  </div>
</div>
</div>
</div>
</article>
</#if>
<!-- begin editShoppingList.ftl -->
</div>
</section>
