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
<div class="row">

<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-3" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductCatalogAdministrationMainPage}</h2>
</header>
<div role="content">
<#if !sessionAttributes.userLogin??>
  <div class='label'> ${uiLabelMap.ProductGeneralMessage}.</div>
</#if>

<#if security.hasEntityPermission("CATALOG", "_VIEW", session)>

<table class="basic-table">
<tr>
<td><label>${uiLabelMap.ProductEditCatalogWithCatalogId}:</label></td>
  <form method="post" action="<@ofbizUrl>EditProdCatalog</@ofbizUrl>" name="EditProdCatalogForm">
    <td><input type="text" class="form-control" size="20" maxlength="20" name="prodCatalogId" value=""/>
    <input type="submit" class="btn btn-primary" value=" ${uiLabelMap.ProductEditCatalog}"/>
   </td>
  </form>
</tr>

<tr>
<td width="30%"><label>${uiLabelMap.CommonOr}:</label>&nbsp;
<a href="<@ofbizUrl>EditProdCatalog</@ofbizUrl>" class="btnA">${uiLabelMap.ProductCreateNewCatalog}</a></td>
</tr>

<tr>
<td><label>${uiLabelMap.ProductEditCategoryWithCategoryId}:</label></td>
  <td><form method="post" class="smart-form" action="<@ofbizUrl>EditCategory</@ofbizUrl>" name="EditCategoryForm">
    <@htmlTemplate.lookupField name="productCategoryId" id="productCategoryId" formName="EditCategoryForm" fieldFormName="LookupProductCategory"/>
    <input type="submit" class="btn btn-primary" value="${uiLabelMap.ProductEditCategory}"/>
  </form></td>
</tr>

<tr>
<td><label>${uiLabelMap.CommonOr}:</label>&nbsp;
<a href="<@ofbizUrl>EditCategory</@ofbizUrl>" class="btnA">${uiLabelMap.ProductCreateNewCategory}</a></td>
</tr>
<tr>
<td><label>${uiLabelMap.ProductEditProductWithProductId}:</label></td>
<td><form method="post" class="smart-form" action="<@ofbizUrl>EditProduct</@ofbizUrl>" style="margin: 0;" name="EditProductForm">
    <@htmlTemplate.lookupField name="productId" id="productId" formName="EditProductForm" fieldFormName="LookupProduct"/>
    <input type="submit" class="btn btn-primary" value=" ${uiLabelMap.ProductEditProduct}" class="smallSubmit"/>
  </form></td>
<td>
</tr>

<tr>
    <td>
    <label>${uiLabelMap.CommonOr}:</label>&nbsp;
     <a href="<@ofbizUrl>EditProduct</@ofbizUrl>"  class="btnA">${uiLabelMap.ProductCreateNewProduct}</a>
    </td>
</tr>
<tr>
    <td>
      <label>${uiLabelMap.CommonOr}:</label>&nbsp;
      <a href="<@ofbizUrl>CreateVirtualWithVariantsForm</@ofbizUrl>" class="btnA">${uiLabelMap.ProductQuickCreateVirtualFromVariants}</a>
    </td>
</tr>

<tr>
<td><label>${uiLabelMap.ProductFindProductWithIdValue}:</label></td>
<td><form method="post"  action="<@ofbizUrl>FindProductById</@ofbizUrl>">
    <input type="text" class="form-control" size="20" maxlength="20" name="idValue" value=""/>
    <input type="submit"  class="btn btn-primary" value=" ${uiLabelMap.ProductFindProduct}"/>
  </form></td>
</tr>
<tr><td>&nbsp;</td></tr>
</table>
<a class="btn btn-primary" href="<@ofbizUrl>UpdateAllKeywords</@ofbizUrl>"> ${uiLabelMap.ProductAutoCreateKeywordsForAllProducts}</a></br></br>
<a class="btn btn-primary" href="<@ofbizUrl>FastLoadCache</@ofbizUrl>"> ${uiLabelMap.ProductFastLoadCatalogIntoCache}</a>
</#if>
</div>
</div>
</article>
</div>