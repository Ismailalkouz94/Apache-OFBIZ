<!--
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
<#if glAcctgAndAmountPercentageList?has_content && glAccountCategories?has_content>
  <form id="costCenters" method="post" action="<@ofbizUrl>createUpdateCostCenter</@ofbizUrl>">
<div class="table-responsive">
    <table class="table table-bordered table-striped" cellspacing="0">
<thead>
      <tr>
        <th>${uiLabelMap.AccountingOrganizationPartyId}</th>
        <th>${uiLabelMap.FormFieldTitle_glAccountId}</th>
        <th>${uiLabelMap.FormFieldTitle_accountCode}</th>
        <th>${uiLabelMap.FormFieldTitle_accountName}</th>
        <#list glAccountCategories as glAccountCategory>
          <th>${glAccountCategory.description!}</th>
        </#list>
      </tr>
</thead>
    <#assign alt_row = false>
      <#list glAcctgAndAmountPercentageList as glAcctgAndAmountPercentage>
        <tr id="row_${glAcctgAndAmountPercentage.glAccountId}" class="alternate-row">
          <td>${glAcctgAndAmountPercentage.organizationPartyId}</td>
          <td><input class="form-control" type="hidden" id="glAccountId_${glAcctgAndAmountPercentage.glAccountId}" name="glAccountId_o_${glAcctgAndAmountPercentage_index}" value="${glAcctgAndAmountPercentage.glAccountId!}"/>
              <input class="form-control" name="_rowSubmit_o_${glAcctgAndAmountPercentage_index}" type="hidden" value="Y"/>          
          ${glAcctgAndAmountPercentage.glAccountId}</td>
          <td>${glAcctgAndAmountPercentage.accountCode!}</td>
          <td>${glAcctgAndAmountPercentage.accountName!}</td>
          <#list glAccountCategories as glAccountCategory>
            <td>
              <#if (glAcctgAndAmountPercentage[glAccountCategory.glAccountCategoryId!])??>
                <input class="form-control" style="width:70px" type="text" id="cc_${glAcctgAndAmountPercentage.glAccountId}_${glAccountCategory.glAccountCategoryId}" size="5" name="amp_${glAccountCategory.glAccountCategoryId!}_o_${glAcctgAndAmountPercentage_index}" value="${(glAcctgAndAmountPercentage[glAccountCategory.glAccountCategoryId!])!}"/>%
              <#else>
                <input class="form-control" type="text" id="cc_${glAcctgAndAmountPercentage.glAccountId}_${glAccountCategory.glAccountCategoryId}" size="5" name="amp_${glAccountCategory.glAccountCategoryId!}_o_${glAcctgAndAmountPercentage_index}" value=""/> %
              </#if>
            </td>
          </#list>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
    </table>
</div>
    <div align="right"><input class="btn btn-primary1"  type="submit" value='${uiLabelMap.CommonSubmit}' /></div>
  </form>
<#else>
  <div class="alert alert-info fade in" >${uiLabelMap.CommonNoRecordFound}</div>
</#if>
