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

<#if productCategoryId?? && productCategory??>
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-3" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.PageTitleEditCategoryParties}</h2>
</header>
<div role="content">
    <div class="screenlet">
        <div class="screenlet-body">
            <table cellspacing="0" class="table table-bordered table-striped">
            <thead>
            <tr class="header-row">
            <th>${uiLabelMap.PartyPartyId}</th>
            <th>${uiLabelMap.PartyRole}</th>
            <th>${uiLabelMap.CommonFromDateTime}</th>
            <th align="center">${uiLabelMap.CommonThruDateTime}</th>
            <th>&nbsp;</td>
            </tr>
            </thead>
            <#assign line = 0>
            <#assign rowClass = "2">
            <#list productCategoryRoles as productCategoryRole>
            <#assign line = line + 1>
            <#assign curRoleType = productCategoryRole.getRelatedOne("RoleType", true)>
            <tr valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>
            <td><a href="/partymgr/control/viewprofile?party_id=${(productCategoryRole.partyId)!}" target="_blank" class="buttontext">${(productCategoryRole.partyId)!}</a></td>
            <td>${(curRoleType.get("description",locale))!}</td>
            <#assign hasntStarted = false>
            <#if (productCategoryRole.getTimestamp("fromDate"))?? && Static["org.apache.ofbiz.base.util.UtilDateTime"].nowTimestamp().before(productCategoryRole.getTimestamp("fromDate"))> <#assign hasntStarted = true></#if>
            <td <#if hasntStarted> style="color: red;"</#if>>${(productCategoryRole.fromDate)!}</td>
            <td align="center">
                <form method="post" action="<@ofbizUrl>updatePartyToCategory</@ofbizUrl>" name="lineForm_update${line}">
                    <#assign hasExpired = false>
                    <#if (productCategoryRole.getTimestamp("thruDate"))?? && (Static["org.apache.ofbiz.base.util.UtilDateTime"].nowTimestamp().after(productCategoryRole.getTimestamp("thruDate")))> <#assign hasExpired = true></#if>
                    <input type="hidden" name="productCategoryId" value="${(productCategoryRole.productCategoryId)!}" />
                    <input type="hidden" name="partyId" value="${(productCategoryRole.partyId)!}" />
                    <input type="hidden" name="roleTypeId" value="${(productCategoryRole.roleTypeId)!}" />
                    <input type="hidden" name="fromDate" value="${(productCategoryRole.getTimestamp("fromDate"))!}" />
                    <#if hasExpired><#assign class="alert"><#else><#assign class=""></#if>
                    <@htmlTemplate.renderDateTimeField name="thruDate" event="" action="" className="${class!}" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" value="${(productCategoryRole. getTimestamp('thruDate'))!}" size="25" maxlength="30" id="thruDate_1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                    <input type="submit" value="${uiLabelMap.CommonUpdate}" style="font-size: x-small;" />
                </form>
            </td>
            <td align="center">
                <form method="post" action="<@ofbizUrl>removePartyFromCategory</@ofbizUrl>" name="lineForm_delete${line}">
                    <#assign hasExpired = false>
                    <input type="hidden" name="productCategoryId" value="${(productCategoryRole.productCategoryId)!}" />
                    <input type="hidden" name="partyId" value="${(productCategoryRole.partyId)!}" />
                    <input type="hidden" name="roleTypeId" value="${(productCategoryRole.roleTypeId)!}" />
                    <input type="hidden" name="fromDate" value="${(productCategoryRole.getTimestamp("fromDate"))!}" />
                    <a href="javascript:document.lineForm_delete${line}.submit()" class="buttontext111">${uiLabelMap.CommonDelete}</a>
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
            </table>
        </div>
    </div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-4" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductAssociatePartyToCategory}</h2>
</header>
<div role="content">
    <div class="screenlet">
        <div class="screenlet-body">
            <table cellspacing="0" class="basic-table">
                <tr>
                    <td>
                        <form method="post" class="smart-form" action="<@ofbizUrl>addPartyToCategory</@ofbizUrl>" style="margin: 0;" name="addNewForm">
                            <input type="hidden" name="productCategoryId" value="${productCategoryId}" />
                            <@htmlTemplate.lookupField value="${parameters.partyId!}"  formName="addNewForm" name="partyId" id="partyId" fieldFormName="LookupPartyName"/>
                            <select name="roleTypeId" size="1">
                            <#list roleTypes as roleType>
                                <option value="${(roleType.roleTypeId)!}" <#if roleType.roleTypeId.equals("_NA_")> selected="selected"</#if>>${(roleType.get("description",locale))!}</option>
                            </#list>
                            </select>

                            <@htmlTemplate.renderDateTimeField name="fromDate" event="" action="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" value="" size="25" maxlength="30" id="fromDate_1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                            <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonAdd}" />
                        </form>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
</div>
</article>
</div>
</#if>
