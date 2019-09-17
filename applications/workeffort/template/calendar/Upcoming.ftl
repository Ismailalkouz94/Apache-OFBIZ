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

  <#if days?has_content>
<link rel="stylesheet" type="text/css" href="/images/BusinessInnovation/css/ForMyCalender.css"/>
<div class="btn-group">
    <button  class="btn dropdown-toggle btn-xs btn-default" data-toggle="dropdown" aria-expanded="false">
	    Showing View <i class="fa fa-caret-down"></i>
    </button>
    <ul class="dropdown-menu js-status-update pull-right">
	<li><a href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=month&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}&hideEvents=${(parameters.hideEvents)!}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.WorkEffortMonthView}</a></li>
	<li><a href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=week&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}&hideEvents=${(parameters.hideEvents)!}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.WorkEffortWeekView}</a></li>
	<li><a href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=day&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}&hideEvents=${(parameters.hideEvents)!}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.WorkEffortDayView}</a></li>
    </ul>
</div>
<table class="table table-bordered table-striped" cellspacing="0">
<thead>
      <tr class="header-row">
        <th>${uiLabelMap.CommonStartDateTime}</th>
        <th>${uiLabelMap.CommonEndDateTime}</th>
        <th>${uiLabelMap.CommonType}</th>
        <th>${uiLabelMap.WorkEffortName}</th>
      </tr>
</thead>
      <#list days as day>
        <#assign workEfforts = day.calendarEntries>
        <#if workEfforts?has_content>
          <tr class="header-row"><td colspan="4"><hr /></td></tr>
          <#assign alt_row = false>
          <#list workEfforts as calendarEntry>
            <#assign workEffort = calendarEntry.workEffort>
            <tr<#if alt_row> class="alternate-row"</#if>>
              <td><#if workEffort.actualStartDate??>${workEffort.actualStartDate}<#else>${workEffort.estimatedStartDate}</#if></td>
              <td><#if workEffort.actualCompletionDate??>${workEffort.actualCompletionDate}<#else>${workEffort.estimatedCompletionDate}</#if></td>
              <td>${workEffort.getRelatedOne("WorkEffortType", false).get("description",locale)}</td>
              <td class="button-col"><a href="<@ofbizUrl>EditWorkEffort?workEffortId=${workEffort.workEffortId}${addlParam!}</@ofbizUrl>">${workEffort.workEffortName}</a></td>
            </tr>
            <#assign alt_row = !alt_row>
          </#list>
        </#if>
      </#list>
    </table>
  <#else>
    <div class="screenlet-body">${uiLabelMap.WorkEffortNoEventsFound}.</div>
  </#if>