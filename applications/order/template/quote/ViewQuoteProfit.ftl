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
<label>${uiLabelMap.OrderOrderQuoteCoefficients}</label>
<#list quoteCoefficients as quoteCoefficient>
    <div>${quoteCoefficient.coeffName}:&nbsp;${quoteCoefficient.coeffValue}</div>
</#list>
<br />
<div><label>${uiLabelMap.CommonTotalCostMult}</label>&nbsp;${costMult}</div>
<div><span class="label">${uiLabelMap.CommonTotalCostToPriceMult}</label>&nbsp;${costToPriceMult}</div>
<br />
<div><label>${uiLabelMap.CommonTotalCost}</label>&nbsp;<@ofbizCurrency amount=totalCost isoCode=quote.currencyUomId/></div>
<div><label>${uiLabelMap.CommonTotalAmount}</label>&nbsp;<@ofbizCurrency amount=totalPrice isoCode=quote.currencyUomId/></div>
<br />
<div><label>${uiLabelMap.CommonTotalProfit}</label>&nbsp;<@ofbizCurrency amount=totalProfit isoCode=quote.currencyUomId/></div>
<div><label>${uiLabelMap.CommonTotalPercProfit}</label>&nbsp;${totalPercProfit}%</div>
<br />