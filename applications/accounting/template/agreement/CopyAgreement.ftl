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
<#if agreement??>
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.PageTitleCopyAgreement}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-body">
    <form action="<@ofbizUrl>copyAgreement</@ofbizUrl>" method="post">
        <input type="hidden" name="agreementId" value="${agreementId}"/>
        <div>
            <label>${uiLabelMap.AccountingCopyAgreement}</label>&nbsp;&nbsp;
            <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="copyAgreementTerms" value="Y" checked="checked" /><span>${uiLabelMap.AccountingAgreementTerms}</span></label>
            <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="copyAgreementProducts" value="Y" checked="checked" /><span>${uiLabelMap.ProductProducts}</span></label>
            <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="copyAgreementParties" value="Y" checked="checked" /><span>${uiLabelMap.Party}</span></label>
            <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="copyAgreementFacilities" value="Y" checked="checked" /><span>${uiLabelMap.ProductFacilities}</span></label>
        </div>
      
            <input type="submit" class="btn btn-primary1" value="${uiLabelMap.CommonCopy}"/>

    </form>
  </div>
</div>
</div>
</div>
</article>
</div>
</#if>
