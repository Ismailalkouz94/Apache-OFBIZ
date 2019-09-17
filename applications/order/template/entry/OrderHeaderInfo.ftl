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

<#assign shoppingCart = sessionAttributes.shoppingCart!>
<#assign currencyUomId = shoppingCart.getCurrency()>
<#assign partyId = shoppingCart.getPartyId()>
<#assign partyMap = Static["org.apache.ofbiz.party.party.PartyWorker"].getPartyOtherValues(request, partyId, "party", "person", "partyGroup")>
<#assign agreementId = shoppingCart.getAgreementId()!>
<#assign quoteId = shoppingCart.getQuoteId()!>

<#if shoppingCart?has_content>
    <#assign shoppingCartSize = shoppingCart.size()>
<#else>
    <#assign shoppingCartSize = 0>
</#if>
<div class="row">
<article class="col-sm-12 col-md-12 col-lg-6 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.OrderOrderHeaderInfo}</h2>
</header>
<div role="content">
<div class="screenlet">
    <div class="screenlet-body">
              <form method="post" action="setOrderName" name="setCartOrderNameForm">
                <fieldset>
                  <label for="orderName">${uiLabelMap.OrderOrderName}:</label>
                  <input class="form-control" type="text" id="orderName" name="orderName" size="12" maxlength="200" value="${shoppingCart.getOrderName()?default("")}" />
                  <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonSet}" />
                </fieldset>
              </form>
              <p>
              <label>${uiLabelMap.Party}</label>:
                  <a href="${customerDetailLink}${partyId}${externalKeyParam!}" target="partymgr" class="btn btn-default btn-sm">${partyId}</a>
                  <#if partyMap.person??>
                    <label>${partyMap.person.firstName!}&nbsp;${partyMap.person.lastName!}</label>
                  </#if>
                  <#if partyMap.partyGroup??>
                    <label>${partyMap.partyGroup.groupName!}</label>
                  </#if>
              </p>
            <#if shoppingCart.getOrderType() != "PURCHASE_ORDER">
                <form method="post" action="setPoNumber" name="setCartPoNumberForm">
                  <fieldset>
                    <label for="correspondingPoId">${uiLabelMap.OrderPONumber}:</label>
                    <input class="form-control" type="text" id="correspondingPoId" name="correspondingPoId" size="12" value="${shoppingCart.getPoNumber()?default("")}" />
                    <input class="btn btn-primary" type="submit" value="${uiLabelMap.CommonSet}" />
                  </fieldset>
                </form>
            </#if>
            <p>
              <label>${uiLabelMap.CommonCurrency}</label>:
                <label>${currencyUomId} </label>
            </p>
            <#if agreementId?has_content>
            <p>
              <label>${uiLabelMap.AccountingAgreement}</label>:
               <label>${agreementId} </label>
            </p>
            </#if>
            <#if quoteId?has_content>
            <p>
              <label>${uiLabelMap.OrderOrderQuote}</label>:
               <label>${quoteId} </label>
            </p>
            </#if>
            <p><label>${uiLabelMap.CommonTotal}</label>:  <label><@ofbizCurrency amount=shoppingCart.getGrandTotal() isoCode=currencyUomId/> </label></p>
    </div>
</div>
</div>
</div>
</article>


