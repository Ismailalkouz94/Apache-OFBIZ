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

<#assign styleTd = "style='height: 5em; width: 10em; vertical-align: middle; '">
  
<#if periods?has_content>
  <#-- Allow containing screens to specify the URL for creating a new event -->
  <#if !newCalEventUrl??>
    <#assign newCalEventUrl = parameters._LAST_VIEW_NAME_>
  </#if>
<link rel="stylesheet" type="text/css" href="/images/BusinessInnovation/css/ForMyCalender.css"/>
<div class="btn-group">
    <button  class="btn dropdown-toggle btn-xs btn-default" data-toggle="dropdown" aria-expanded="false">
	    Showing View <i class="fa fa-caret-down"></i>
    </button>
    <ul class="dropdown-menu js-status-update pull-right">
	<li><a href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=upcoming&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}&hideEvents=${(parameters.hideEvents)!}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.WorkEffortUpcomingEvents}</a></li>
	<li><a href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=week&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}&hideEvents=${(parameters.hideEvents)!}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.WorkEffortWeekView}</a></li>
	<li><a href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=day&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}&hideEvents=${(parameters.hideEvents)!}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.WorkEffortDayView}</a></li>

    </ul>
</div>
<div class="btn-group two">
<a class="btn btn-default btn-xs" href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=month&startTime=${(prevMillis)!}&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}</@ofbizUrl>" class="smallSubmit"><i class="fa fa-chevron-left"></i>&nbsp;${uiLabelMap.WorkEffortPreviousMonth}</a>
<a class="btn btn-default btn-xs" href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=month&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}&hideEvents=${(parameters.hideEvents)!}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.WorkEffortThisMonth}</a>
<a class="btn btn-default btn-xs" href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=month&startTime=${(nextMillis)!}&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.WorkEffortNextMonth}&nbsp;<i class="fa fa-chevron-right"></i></a>
</div>

<table cellspacing="0" class="table table-bordered calenderOf">
<thead>
  <tr>
    <td width="1%">&nbsp;</th>
    <#list periods as day>
      <td>${day.start?date?string("EEEE")?cap_first}</th>
      <#if (day_index > 5)><#break></#if>
    </#list>
  </tr>
</thead>
  <#list periods as period>
    <#assign currentPeriod = false/>
    <#if (nowTimestamp >= period.start) && (nowTimestamp <= period.end)><#assign currentPeriod = true/></#if>
    <#assign indexMod7 = period_index % 7>
    <#if indexMod7 = 0>
      <tr>
        <td ${styleTd}>
          <a href='<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=week&amp;startTime=${period.start.time?string("#")}${urlParam!}${addlParam!}</@ofbizUrl>'>${uiLabelMap.CommonWeek} ${period.start?date?string("w")}</a>
        </td>
    </#if>
    <td ${styleTd} <#if currentPeriod> class="current-period"<#else><#if (period.calendarEntries?size > 0)> class="active-period"</#if></#if>>
      <span class="h1"><a href='<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=day&amp;startTime=${period.start.time?string("#")}${urlParam!}${addlParam!}</@ofbizUrl>'>${period.start?date?string("d")?cap_first}</a></span>
      <a class="add-new" href='<@ofbizUrl>${newCalEventUrl}?period=month&amp;form=edit&amp;startTime=${parameters.start!}&amp;parentTypeId=${parentTypeId!}&amp;currentStatusId=CAL_TENTATIVE&amp;estimatedStartDate=${period.start?string("yyyy-MM-dd HH:mm:ss")}&amp;estimatedCompletionDate=${period.end?string("yyyy-MM-dd HH:mm:ss")}${urlParam!}${addlParam!}</@ofbizUrl>'>${uiLabelMap.CommonAddNew}</a>
      <br class="clear"/>
    <div>
      <#assign maxNumberOfPersons = 0/>
      <#assign maxNumberOfEvents = 0/>
      <#assign ranges = period.calendarEntriesByDateRange.keySet()/>
      <#list ranges as range>
          <#assign eventsInRange = period.calendarEntriesByDateRange.get(range)/>
          <#assign numberOfPersons = 0/>
          <#list eventsInRange as eventInRange>
              <#assign numberOfPersons = numberOfPersons + eventInRange.workEffort.reservPersons?default(0)/>
          </#list>
          <#if (numberOfPersons > maxNumberOfPersons)>
              <#assign maxNumberOfPersons = numberOfPersons/>
          </#if>
          <#if (eventsInRange.size() > maxNumberOfEvents)>
              <#assign maxNumberOfEvents = eventsInRange.size()/>
          </#if>
      </#list>
      <#if (maxNumberOfEvents > 0)>
          ${uiLabelMap.WorkEffortMaxNumberOfEvents}: ${maxNumberOfEvents}<br/>
      </#if>
      <#if (maxNumberOfPersons > 0)>
          ${uiLabelMap.WorkEffortMaxNumberOfPersons}: ${maxNumberOfPersons}<br/>
      </#if>
      <#if parameters.hideEvents?default("") != "Y">
      <#list period.calendarEntries as calEntry>
        <#if calEntry.workEffort.actualStartDate??>
            <#assign startDate = calEntry.workEffort.actualStartDate>
          <#else>
            <#assign startDate = calEntry.workEffort.estimatedStartDate!>
        </#if>

        <#if calEntry.workEffort.actualCompletionDate??>
            <#assign completionDate = calEntry.workEffort.actualCompletionDate>
          <#else>
            <#assign completionDate = calEntry.workEffort.estimatedCompletionDate!>
        </#if>

        <#if !completionDate?has_content && calEntry.workEffort.actualMilliSeconds?has_content>
            <#assign completionDate =  calEntry.workEffort.actualStartDate + calEntry.workEffort.actualMilliSeconds>
        </#if>    
        <#if !completionDate?has_content && calEntry.workEffort.estimatedMilliSeconds?has_content>
            <#assign completionDate =  calEntry.workEffort.estimatedStartDate + calEntry.workEffort.estimatedMilliSeconds>
        </#if>    
        <hr />
        <#if (startDate.compareTo(period.start) <= 0 && completionDate?has_content && completionDate.compareTo(period.end) >= 0)>
          ${uiLabelMap.CommonAllDay}
        <#elseif startDate.before(period.start) && completionDate?has_content>
          ${uiLabelMap.CommonUntil} ${completionDate?time?string.short}
        <#elseif !completionDate?has_content>
          ${uiLabelMap.CommonFrom} ${startDate?time?string.short} - ?
        <#elseif completionDate.after(period.end)>
          ${uiLabelMap.CommonFrom} ${startDate?time?string.short}
        <#else>
          ${startDate?time?string.short}-${completionDate?time?string.short}
        </#if>
        <br />
        ${setRequestAttribute("periodType", "month")}
        ${setRequestAttribute("workEffortId", calEntry.workEffort.workEffortId)}
        ${screens.render("component://workeffort/widget/CalendarScreens.xml#calendarEventContent")}
        <br />
      </#list>
      </#if>
    </div>
    </td>

    <#if !period_has_next && indexMod7 != 6>
    <td colspan='${6 - (indexMod7)}'>&nbsp;</td>
    </#if>
  <#if indexMod7 = 6 || !period_has_next>
  </tr>
  </#if>
  </#list>
</table>

<#else>
  <div class="screenlet-body">${uiLabelMap.WorkEffortFailedCalendarEntries}!</div>
</#if>
