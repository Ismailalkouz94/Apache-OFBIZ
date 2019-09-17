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


  <h1 style="display:none" id ="h2contenttext">${uiLabelMap.AccountingEditCustomTimePeriods}</h1>
<br />
<#if security.hasPermission("PERIOD_MAINT", session)>
<div class="row">
<article class="col-sm-12 col-md-12 col-lg-6 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.AccountingShowOnlyPeriodsWithOrganization}</h2>
</header>
<div role="content">
   <div class="screenlet">
     <form method="post" action="<@ofbizUrl>EditCustomTimePeriod</@ofbizUrl>" name="setOrganizationPartyIdForm">
         <input type="hidden" name="currentCustomTimePeriodId" value="${currentCustomTimePeriodId!}" />
         <label> ${uiLabelMap.AccountingShowOnlyPeriodsWithOrganization}
         <input class="form-control" type="text" size="20" name="findOrganizationPartyId" value="${findOrganizationPartyId!}" />
         </label>
<hr/>
         <input  class="btn btn-primary1" type="submit" value='${uiLabelMap.CommonUpdate}' />
     </form>
   </div>
</div>
</div>
</article>
<article class="col-sm-12 col-md-12 col-lg-6 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">
<header>
 <#if currentCustomTimePeriod?has_content><h2>${uiLabelMap.AccountingCurrentCustomTimePeriod}
<a href="<@ofbizUrl>EditCustomTimePeriod?findOrganizationPartyId=${findOrganizationPartyId!}</@ofbizUrl>">${uiLabelMap.CommonClearCurrent}</a></h2>
 <#else>
  <h2>${uiLabelMap.AccountingCurrentCustomTimePeriod}</h2>
 </#if>
</header>
<div role="content" style="overflow:auto">
  <div class="screenlet">
    <#if currentCustomTimePeriod?has_content>
<div class="table-responsive">
      <table class="table table-bordered table-striped" cellspacing="0">
<thead>
        <tr>
          <th>${uiLabelMap.CommonId}</th>
          <th>${uiLabelMap.CommonParent}</th>
          <th>${uiLabelMap.AccountingOrgPartyId}</th>
          <th>${uiLabelMap.AccountingPeriodType}</th>
          <th>${uiLabelMap.CommonNbr}</th>
          <th>${uiLabelMap.AccountingPeriodName}</th>
          <th>${uiLabelMap.CommonFromDate}</th>
          <th>${uiLabelMap.CommonThruDate}</th>
          <th colspan="2">&nbsp;</th>
        </tr>
</thead>
        <tr>
          <form method="post" action="<@ofbizUrl>updateCustomTimePeriod</@ofbizUrl>" name="updateCustomTimePeriodForm">
            <input type="hidden" name="findOrganizationPartyId" value="${findOrganizationPartyId!}" />
            <input type="hidden" name="customTimePeriodId" value="${currentCustomTimePeriodId!}" />
            <td>${currentCustomTimePeriod.customTimePeriodId}</td>
            <td>
              <select class="form-control" name="parentPeriodId">
                <option value=''>&nbsp;</option>
                <#list allCustomTimePeriods as allCustomTimePeriod>
                  <#assign allPeriodType = allCustomTimePeriod.getRelatedOne("PeriodType", true)>
                  <#assign isDefault = false>
                  <#if (currentCustomTimePeriod.parentPeriodId)??>
                    <#if currentCustomTimePeriod.customTimePeriodId = allCustomTimePeriod.customTimePeriodId>
                      <#assign isDefault = true>
                    </#if>
                  </#if>
                  <option value='${allCustomTimePeriod.customTimePeriodId}'<#if isDefault> selected="selected"</#if>>
                    ${allCustomTimePeriod.organizationPartyId}
                    <#if allPeriodType??>${allPeriodType.description}:</#if>
                    ${allCustomTimePeriod.periodNum!}
                    [${allCustomTimePeriod.customTimePeriodId}]
                  </option>
                </#list>
              </select>
              <#if (currentCustomTimePeriod.parentPeriodId)??>
                <a href='<@ofbizUrl>EditCustomTimePeriod?currentCustomTimePeriodId=${currentCustomTimePeriod.parentPeriodId}&amp;findOrganizationPartyId=${findOrganizationPartyId!}</@ofbizUrl>'>
                ${uiLabelMap.CommonSetAsCurrent}</a>
              </#if>
            </td>
            <td><input class="form-control" type="text" size='12' name="currentCustomTimePeriod" value="${currentCustomTimePeriod.organizationPartyId!}" /></td>
            <td>
              <select  class="form-control" name="periodTypeId">
                <#list periodTypes as periodType>
                  <#assign isDefault = false>
                  <#if (currentCustomTimePeriod.periodTypeId)??>
                    <#if currentCustomTimePeriod.periodTypeId = periodType.periodTypeId>
                      <#assign isDefault = true>
                    </#if>
                  </#if>
                  <option value='${periodType.periodTypeId}'<#if isDefault> selected="selected"</#if>>
                    ${periodType.description} [${periodType.periodTypeId}]
                  </option>
                </#list>
              </select>
            </td>
            <td><input class="form-control" type="text" size='4' name="periodNum" value="${currentCustomTimePeriod.periodNum!}" /></td>
            <td><input class="form-control" type="text" size='10' name="periodName" value="${currentCustomTimePeriod.periodName!}" /></td>
            <td>
              <#assign hasntStarted = false>
              <#assign compareDate = currentCustomTimePeriod.getTimestamp("fromDate")>
              <#if compareDate?has_content>
                <#if nowTimestamp.before(compareDate)><#assign hasntStarted = true></#if>
              </#if>
              <input class="form-control" type="text" size='13' name="fromDate" value="${currentCustomTimePeriod.fromDate?string("yyyy-MM-dd")}"<#if hasntStarted> class="alert"</#if> />
            </td>
            <td>
              <#assign hasExpired = false>
              <#assign compareDate = currentCustomTimePeriod.getTimestamp("thruDate")>
              <#if compareDate?has_content>
                <#if nowTimestamp.after(compareDate)><#assign hasExpired = true></#if>
              </#if>
              <input class="form-control" type="text" size='13' name="thruDate" value="${currentCustomTimePeriod.thruDate?string("yyyy-MM-dd")}"<#if hasntStarted> class="alert"</#if> />
            </td>
            <td class="button-col">
              <input class="btn btn-primary btn-sm" type="submit" value='${uiLabelMap.CommonUpdate}'/>
            </td>
          </form>
          <td class="button-col">
            <form method="post" action='<@ofbizUrl>deleteCustomTimePeriod</@ofbizUrl>' name='deleteCustomTimePeriodForm'>
              <input type="hidden" name="customTimePeriodId" value="${currentCustomTimePeriod.customTimePeriodId!}" />
              <input class="btn btn-primary btn-sm" type="submit" value='${uiLabelMap.CommonDelete}'/>
            </form>
          </td>
        </tr>
      </table>
</div>
    <#else>
      <div class="alert alert-info fade in">${uiLabelMap.AccountingNoCurrentCustomTimePeriodSelected}</div>
    </#if>
  </div>
</div>
</div>
</article>
</div>
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.AccountingChildPeriods}</h2>
</header>
<div role="content" style="overflow:auto">
  <div class="screenlet">
    <#if customTimePeriods?has_content>
<div class="table-responsive">
      <table style="overflow:scroll" class="table table-bordered table-striped" cellspacing="0">
<thead>
        <tr>
          <th>${uiLabelMap.CommonId}</th>
          <th>${uiLabelMap.CommonParent}</th>
          <th>${uiLabelMap.AccountingOrgPartyId}</th>
          <th>${uiLabelMap.AccountingPeriodType}</th>
          <th>${uiLabelMap.CommonNbr}</th>
          <th>${uiLabelMap.AccountingPeriodName}</th>
          <th>${uiLabelMap.CommonFromDate}</th>
          <th>${uiLabelMap.CommonThruDate}</th>
          <th colspan="3">&nbsp;</th>
        </tr>
</thead>
        <#assign line = 0>
        <#list customTimePeriods as customTimePeriod>
          <#assign line = line + 1>
          <#assign periodType = customTimePeriod.getRelatedOne("PeriodType", true)>
          <tr>
            <form method="post" action='<@ofbizUrl>updateCustomTimePeriod</@ofbizUrl>' name='lineForm${line}'>
              <input type="hidden" name="customTimePeriodId" value="${customTimePeriod.customTimePeriodId!}" />
              <td>${customTimePeriod.customTimePeriodId}</td>
              <td>
                <select class="form-control" name="parentPeriodId">
                  <option value=''>&nbsp;</option>
                  <#list allCustomTimePeriods as allCustomTimePeriod>
                    <#assign allPeriodType = allCustomTimePeriod.getRelatedOne("PeriodType", true)>
                    <#assign isDefault = false>
                    <#if (currentCustomTimePeriod.parentPeriodId)??>
                      <#if currentCustomTimePeriod.customTimePeriodId = allCustomTimePeriod.customTimePeriodId>
                        <#assign isDefault = true>
                      </#if>
                    </#if>
                    <option value='${allCustomTimePeriod.customTimePeriodId}'<#if isDefault> selected="selected"</#if>>
                      ${allCustomTimePeriod.organizationPartyId}
                      <#if allPeriodType??> ${allPeriodType.description}: </#if>
                      ${allCustomTimePeriod.periodNum!}
                      [${allCustomTimePeriod.customTimePeriodId}]
                    </option>
                  </#list>
                </select>
              </td>
              <td><input class="form-control" type="text" size='12' name="organizationPartyId" value="${customTimePeriod.organizationPartyId!}" /></td>
              <td>
                <select class="form-control" name="periodTypeId">
                  <#list periodTypes as periodType>
                    <#assign isDefault = false>
                    <#if (customTimePeriod.periodTypeId)??>
                      <#if customTimePeriod.periodTypeId = periodType.periodTypeId>
                       <#assign isDefault = true>
                      </#if>
                    </#if>
                    <option value='${periodType.periodTypeId}'<#if isDefault> selected="selected"</#if>>${periodType.description} [${periodType.periodTypeId}]</option>
                  </#list>
                </select>
              </td>
              <td><input  class="form-control" type="text" size='4' name="periodNum" value="${customTimePeriod.periodNum!}" /></td>
              <td><input  class="form-control" type="text" size='10' name="periodName" value="${customTimePeriod.periodName!}" /></td>
              <td>
                <#assign hasntStarted = false>
                <#assign compareDate = customTimePeriod.getTimestamp("fromDate")>
                <#if compareDate?has_content>
                  <#if nowTimestamp.before(compareDate)><#assign hasntStarted = true></#if>
                </#if>
                <input class="form-control" type="text" size='13' name="fromDate" value="${customTimePeriod.fromDate?string("yyyy-MM-dd")}"<#if hasntStarted> class="alert"</#if> />
              </td>
              <td>
                <#assign hasExpired = false>
                <#assign compareDate = customTimePeriod.getTimestamp("thruDate")>
                <#if compareDate?has_content>
                  <#if nowTimestamp.after(compareDate)><#assign hasExpired = true></#if>
                </#if>
                <input class="form-control" type="text" size='13' name="thruDate" value="${customTimePeriod.thruDate?string("yyyy-MM-dd")}"<#if hasExpired> class="alert"</#if> />
              </td>
              <td>
                <input class="btn btn-primary btn-sm" type="submit" value='${uiLabelMap.CommonUpdate}'/>
              </td>
            </form>
            <td class="button-col">
              <form method="post" action='<@ofbizUrl>deleteCustomTimePeriod</@ofbizUrl>' name='lineForm${line}'>
                <input type="hidden" name="customTimePeriodId" value="${customTimePeriod.customTimePeriodId!}" />
                <input class="btn btn-primary btn-sm" type="submit" value='${uiLabelMap.CommonDelete}'/>
              </form>
            </td>
            <td class="button-col">
              <a href='<@ofbizUrl>EditCustomTimePeriod?currentCustomTimePeriodId=${customTimePeriod.customTimePeriodId!}&amp;findOrganizationPartyId=${findOrganizationPartyId!}</@ofbizUrl>'>
              ${uiLabelMap.CommonSetAsCurrent}</a>
            </td>
          </tr>
        </#list>
      </table>
</div>
    <#else>
      <div class="alert alert-info fade in">${uiLabelMap.AccountingNoChildPeriodsFound}</div>
    </#if>
  </div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-3" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.AccountingAddCustomTimePeriod}</h2>
</header>
<div role="content" style="overflow:auto">
  <div class="screenlet">
    <div class="screenlet-body">
      <form method="post" action="<@ofbizUrl>createCustomTimePeriod</@ofbizUrl>" name="createCustomTimePeriodForm">
        <input type="hidden" name="findOrganizationPartyId" value="${findOrganizationPartyId!}" />
        <input type="hidden" name="currentCustomTimePeriodId" value="${currentCustomTimePeriodId!}" />
        <input type="hidden" name="useValues" value="true" />
        <div>
          <label style="width:100px">${uiLabelMap.CommonParent}</label>
          <select style="width:200px;margin-right:10px;" class="form-control" name="parentPeriodId">
            <option value=''>&nbsp;</option>
            <#list allCustomTimePeriods as allCustomTimePeriod>
                <#assign allPeriodType = allCustomTimePeriod.getRelatedOne("PeriodType", true)>
              <#assign isDefault = false>
              <#if currentCustomTimePeriod??>
                <#if currentCustomTimePeriod.customTimePeriodId = allCustomTimePeriod.customTimePeriodId>
                  <#assign isDefault = true>
                </#if>
              </#if>
              <option value="${allCustomTimePeriod.customTimePeriodId}"<#if isDefault> selected="selected"</#if>>
                ${allCustomTimePeriod.organizationPartyId}
                <#if (allCustomTimePeriod.parentPeriodId)??>Par:${allCustomTimePeriod.parentPeriodId}</#if>
                <#if allPeriodType??> ${allPeriodType.description}:</#if>
                ${allCustomTimePeriod.periodNum!}
                [${allCustomTimePeriod.customTimePeriodId}]
              </option>
            </#list>
          </select>
        </div>
        <div>
          <label style="width:100px">${uiLabelMap.AccountingOrgPartyId}</label>
          <input style="width:200px;margin-right:10px;" class="form-control" type="text" size='20' name='organizationPartyId' />
        </div>
        <div>
          <label style="width:100px">${uiLabelMap.AccountingPeriodType}</label>
          <select style="width:200px;margin-right:10px;" class="form-control"  name="periodTypeId">
            <#list periodTypes as periodType>
              <#assign isDefault = false>
              <#if newPeriodTypeId??>
                <#if newPeriodTypeId = periodType.periodTypeId>
                  <#assign isDefault = true>
                </#if>
              </#if>
              <option value="${periodType.periodTypeId}" <#if isDefault>selected="selected"</#if>>${periodType.description} [${periodType.periodTypeId}]</option>
            </#list>
          </select>
          <label style="width:100px">${uiLabelMap.AccountingPeriodNumber}</label>
          <input style="width:200px;margin-right:10px;" class="form-control"  type="text" size='4' name='periodNum' />
          <label style="width:100px">${uiLabelMap.AccountingPeriodName}</label>
          <input style="width:200px;margin-right:10px;" class="form-control"  type="text" size='10' name='periodName' />
        </div>
        <div>
          <label style="width:100px">${uiLabelMap.CommonFromDate}</label>
          <input style="width:200px;margin-right:10px;" class="form-control"  type="text" size='14' name='fromDate' />
          <label style="width:100px">${uiLabelMap.CommonThruDate}</label>
          <input style="width:200px;margin-right:10px;" class="form-control"  type="text" size='14' name='thruDate' /><br/>
          <input class="btn btn-primary1"  type="submit" value="${uiLabelMap.CommonAdd}" />
        </div>
      </form>
    </div>
  </div>
<#else>
  <div class="alert alert-info fade in">${uiLabelMap.AccountingPermissionPeriod}.</div>
</#if>
</div>
</div>
</article>
</div>
</section>
