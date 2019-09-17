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

<#if periods?has_content>
  <#-- Allow containing screens to specify the URL for creating a new event -->
  <#if !newCalEventUrl??>
    <#assign newCalEventUrl = parameters._LAST_VIEW_NAME_>
  </#if>
  <#if (maxConcurrentEntries < 2)>
    <#assign entryWidth = 100>
  <#else>
    <#assign entryWidth = (100 / (maxConcurrentEntries))>
  </#if>
<link rel="stylesheet" type="text/css" href="/images/BusinessInnovation/css/ForMyCalender.css"/>
<div class="btn-group">
    <button  class="btn dropdown-toggle btn-xs btn-default" data-toggle="dropdown" aria-expanded="false">
	    Showing View <i class="fa fa-caret-down"></i>
    </button>
    <ul class="dropdown-menu js-status-update pull-right">
	<li><a href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=upcoming&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}&hideEvents=${(parameters.hideEvents)!}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.WorkEffortUpcomingEvents}</a></li>
	<li><a href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=month&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}&hideEvents=${(parameters.hideEvents)!}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.WorkEffortMonthView}</a></li>
	<li><a href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=week&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}&hideEvents=${(parameters.hideEvents)!}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.WorkEffortWeekView}</a></li>
    </ul>
</div>
<div class="btn-group two">
	<a class="btn btn-default btn-xs" href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=day&startTime=${(prevMillis)!}&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}</@ofbizUrl>" class="smallSubmit"><i class="fa fa-chevron-left"></i>&nbsp;${uiLabelMap.WorkEffortPreviousDay}</a>
	<a class="btn btn-default btn-xs" href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=day&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}&hideEvents=${(parameters.hideEvents)!}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.CommonToday}</a>
	<a class="btn btn-default btn-xs" href="<@ofbizUrl>${parameters._LAST_VIEW_NAME_}?period=day&startTime=${(nextMillis)!}&partyId=${(parameters.partyId)!}&fixedAssetId=${(parameters.fixedAssetId)!}&workEffortTypeId=${(parameters.workEffortTypeId)!}&calendarType=${(parameters.calendarType)!}&facilityId=${(parameters.facilityId)!}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.WorkEffortNextDay}&nbsp;<i class="fa fa-chevron-right"></i></a>
</div>

<table cellspacing="0" class="table table-bordered calenderOf">
<thead>
  <tr>
    <td>${uiLabelMap.CommonTime}</th>
    <td colspan="${maxConcurrentEntries}">${uiLabelMap.WorkEffortCalendarEntries}</th>
  </tr>
</thead>
  <#list periods as period>
    <#assign currentPeriod = false/>
    <#if (nowTimestamp >= period.start) && (nowTimestamp <= period.end)><#assign currentPeriod = true/></#if>
  <tr<#if currentPeriod> class="current-period"<#else><#if (period.calendarEntries?size > 0)> class="active-period"</#if></#if>>
    <td width="10%" class="centered">
      ${period.start?time?string.short}<br />
      <a href="<@ofbizUrl>${newCalEventUrl}?period=day&amp;form=edit&amp;parentTypeId=${parentTypeId!}&amp;startTime=${parameters.start!}&amp;currentStatusId=CAL_TENTATIVE&amp;estimatedStartDate=${period.start?string("yyyy-MM-dd HH:mm:ss")}&amp;estimatedCompletionDate=${period.end?string("yyyy-MM-dd HH:mm:ss")}${urlParam!}${addlParam!}</@ofbizUrl>">${uiLabelMap.CommonAddNew}</a>
    </td>
     <div>
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
    
    <#if calEntry.startOfPeriod>
    <td<#if (calEntry.periodSpan > 1)> rowspan="${calEntry.periodSpan}"</#if> width="${entryWidth?string("#")}%">
   <div class="fc-content theTwo">
    <#if (startDate.compareTo(start)  <= 0 && completionDate?has_content && completionDate.compareTo(next) >= 0)>
      ${uiLabelMap.CommonAllDay}
    <#elseif startDate.before(start) && completionDate?has_content>
      ${uiLabelMap.CommonUntil} ${completionDate?time?string.short}
    <#elseif !completionDate?has_content>
      ${uiLabelMap.CommonFrom} ${startDate?time?string.short} - ?
    <#elseif completionDate.after(period.end)>
      ${uiLabelMap.CommonFrom} ${startDate?time?string.short}
    <#else>
      ${startDate?time?string.short}-${completionDate?time?string.short}
    </#if>
    <i class="fa fa-lock"></i>
    <br />
    ${setRequestAttribute("periodType", "day")}
    ${setRequestAttribute("workEffortId", calEntry.workEffort.workEffortId)}
    ${screens.render("component://workeffort/widget/CalendarScreens.xml#calendarEventContent")}
    </div>
    </td>
    </#if>
    </#list>
    <#if (period.calendarEntries?size < maxConcurrentEntries)>
      <#assign emptySlots = (maxConcurrentEntries - period.calendarEntries?size)>
        <td<#if (emptySlots > 1)> colspan="${emptySlots}"</#if>>&nbsp;</td>
    </#if>
    <#if maxConcurrentEntries = 0>
      <td width="${entryWidth?string("#")}%">&nbsp;</td>
    </#if>
  </tr>
  </#list>
</table>

<#else>
  <div class="alert alert-info fade in">${uiLabelMap.WorkEffortFailedCalendarEntries}!</div>
</#if>
