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
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<#if techDataCalendar?has_content>
  <div class="button-bar"><a href="<@ofbizUrl>EditCalendar</@ofbizUrl>" class="buttontext">${uiLabelMap.ManufacturingNewCalendar}</a></div>

<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ManufacturingUpdateCalendar}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-body">
  <form name="calendarform" method="post" action="<@ofbizUrl>UpdateCalendar</@ofbizUrl>">
    <input class="form-control" type="hidden" name="calendarId" value="${techDataCalendar.calendarId}" />
<#else>
  <a href="<@ofbizUrl>EditCalendar</@ofbizUrl>" class="buttontext">${uiLabelMap.ManufacturingNewCalendar}</a>

<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ManufacturingCreateCalendar}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-body">
  <form name="calendarform" method="post" action="<@ofbizUrl>CreateCalendar</@ofbizUrl>">
</#if>
  <table class="basic-table" cellspacing="0">
    <#if techDataCalendar?has_content>
    <tr>
      <td><label>${uiLabelMap.ManufacturingCalendarId}</label></td>
      <td width="5">&nbsp;</td>
      <td><label>${techDataCalendar.calendarId!}</label> <span class="tooltip1">(${uiLabelMap.CommonNotModifRecreat})</span></td>
    </tr>
    <#else>
    <tr>
      <td><label>${uiLabelMap.ManufacturingCalendarId}</label></td>
      <td>&nbsp;</td>
      <td><input class="form-control" type="text" size="12" name="calendarId" value="${calendarData.calendarId!}" /></td>
    </tr>
    </#if>
    <tr>
      <td><label>${uiLabelMap.CommonDescription}</label></td>
      <td>&nbsp;</td>
      <td><input class="form-control" type="text" size="40" name="description" value="${calendarData.description!}" /></td>
    </tr>
    <tr>
      <td><label>${uiLabelMap.ManufacturingCalendarWeekId}</label></td>
      <td>&nbsp;</td>
      <td>
         <select class="form-control" name="calendarWeekId">
          <#list calendarWeeks as calendarWeek>
          <option value="${calendarWeek.calendarWeekId}" <#if calendarData?has_content && calendarData.calendarWeekId?default("") == calendarWeek.calendarWeekId>SELECTED</#if>>${(calendarWeek.get("description",locale))!}</option>
          </#list>
        </select>
      </td>
    </tr>
    <tr>
      <td></td>
      <td>&nbsp;</td>
      <td><input class="btn btn-primary1" type="submit" value="${uiLabelMap.CommonUpdate}" /></td>
    </tr>
  </table>
  </form>
  </div>
</div>
</div>
</div>
</article>
</div>
</section>
