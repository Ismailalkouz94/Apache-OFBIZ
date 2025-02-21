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
<#if showPromoText?? && showPromoText>
<div class="row">

<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-8" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.OrderSpecialOffers}</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
        <table cellspacing="0" cellpadding="1" border="0">
          <#-- show promotions text -->
          <#list productPromos as productPromo>
            <tr>
              <td>
                <div><a href="<@ofbizUrl>showPromotionDetails?productPromoId=${productPromo.productPromoId}</@ofbizUrl>" class="linktext">${uiLabelMap.CommonDetails}</a> ${StringUtil.wrapString(productPromo.promoText!)}</div>
              </td>
            </tr>
            <#if productPromo_has_next>
              <tr><td><hr /></td></tr>
            </#if>
          </#list>
          <tr><td><hr /></td></tr>
          <tr>
            <td>
              <div><a href="<@ofbizUrl>showAllPromotions</@ofbizUrl>" class="button1 facebook">${uiLabelMap.OrderViewAllPromotions}</a></div>
            </td>
          </tr>
        </table>
    </div>
</div>
</#if>
</div>
</div>
</article>
</div>
</section>
