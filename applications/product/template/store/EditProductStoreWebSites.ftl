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
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-3" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.PageTitleEditProductStoreWebSites}</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
      <a class="btn btn-primary1" href="/content/control/EditWebSite?productStoreId=${productStoreId}&amp;externalLoginKey=${requestAttributes.externalLoginKey}" class="buttontext">${uiLabelMap.ProductCreateNewProductStoreWebSite}</a>
        <table cellspacing="0" class="table table-bordered table-striped">
          <thead>
            <tr class="header-row">
              <th>${uiLabelMap.ProductWebSiteId}</th>
              <th>${uiLabelMap.ProductHost}</th>
              <th>${uiLabelMap.ProductPort}</th>
              <th>&nbsp;</th>
            </tr>
         </thead>
            <#if storeWebSites?has_content>
              <#assign rowClass = "2">
              <#list storeWebSites as webSite>
                <tr valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>
                  <td><a href="/content/control/EditWebSite?webSiteId=${webSite.webSiteId}&amp;externalLoginKey=${requestAttributes.externalLoginKey}" class="buttontext">${webSite.siteName!} [${webSite.webSiteId}]</a></td>
                  <td>${webSite.httpHost?default('&nbsp;')}</td>
                  <td>${webSite.httpPort?default('&nbsp;')}</td>
                  <td align="center">
                    <a href="javascript:document.storeUpdateWebSite_${webSite_index}.submit();" class="buttontext111">${uiLabelMap.CommonDelete}</a>
                    <form name="storeUpdateWebSite_${webSite_index}" method="post" action="<@ofbizUrl>storeUpdateWebSite</@ofbizUrl>">
                        <input type="hidden" name="viewProductStoreId" value="${productStoreId}"/>
                        <input type="hidden" name="productStoreId" value=""/>
                        <input type="hidden" name="webSiteId" value="${webSite.webSiteId}"/>
                    </form>                      
                  </td>
                </tr>
                <#-- toggle the row color -->
                <#if rowClass == "2">
                    <#assign rowClass = "1">
                <#else>
                    <#assign rowClass = "2">
                </#if>
              </#list>
            </#if>
        </table>
    </div>
</div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-4" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductSetStoreOnWebSite}</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <form name="addWebSite" action="<@ofbizUrl>storeUpdateWebSite</@ofbizUrl>" method="post">
            <input type="hidden" name="viewProductStoreId" value="${productStoreId}" />
            <input type="hidden" name="productStoreId" value="${productStoreId}" />
            <select name="webSiteId">
              <#list webSites as webSite>
                <option value="${webSite.webSiteId}">${webSite.siteName!} [${webSite.webSiteId}]</option>
              </#list>
            </select>
            <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonUpdate}" />
        </form>
    </div>
</div>
</div>
</div>
</article>
</div>