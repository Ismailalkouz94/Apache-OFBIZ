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
<article class="col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.OrderSpecialOffers}</h2>
</header>
<div role="content">
<div>
    <div>
        <ul>
        <#-- show promotions text -->
        <#list productPromosAllShowable as productPromo>
            <li><a href="<@ofbizUrl>showPromotionDetails?productPromoId=${productPromo.productPromoId}</@ofbizUrl>" class="button">${uiLabelMap.CommonDetails}</a>${StringUtil.wrapString(productPromo.promoText!)}</li>
        </#list>
        </ul>
    </div>
</div>

<#if (shoppingCartSize > 0)>
  ${screens.render(promoUseDetailsInlineScreen)}
</#if>

</div>
</div>
</article>
</div>
