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

<div class="col-xs-12 col-sm-9 col-md-9 col-lg-9">
<h1  id ="h2contenttext" style="display:none">${uiLabelMap.PageTitleViewActivityAndTaskList}</h1>
</div>


<section id="widget-grid">
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="button-bar">
<ul class="nav nav-tabs1">
<li>
  <a class="button1 facebook" href="<@ofbizUrl>EditWorkEffort?workEffortTypeId=TASK&amp;currentStatusId=CAL_NEEDS_ACTION</@ofbizUrl>">New Task
</a>
</li>
</ul>
</div>
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">

<header>
<h2>${uiLabelMap.WorkEffortAssignedTasks}</h2>
</header>


<div role="content">

  <table class="table table-bordered table-striped">
    <thead>
	<tr>
	  <th><label>${uiLabelMap.CommonStartDateTime}</label></th>
	  <th><label>${uiLabelMap.WorkEffortTaskName}</label></th>
	  <th><label>${uiLabelMap.WorkEffortPriority}</label></th>
	  <th><label>${uiLabelMap.WorkEffortStatus}</label></th>
	</tr>
    </thead>
    <tbody>
    <#assign alt_row = false>
    <#list tasks as workEffort>
      <tr<#if alt_row> class="alternate-row"</#if>>
        <td>${(workEffort.estimatedStartDate)!}</td>
        <td><a href="<@ofbizUrl>WorkEffortSummary?workEffortId=${workEffort.workEffortId}</@ofbizUrl>">${workEffort.workEffortName}</a></td>
        <td>${workEffort.priority!}</td>
        <td>${(delegator.findOne("StatusItem", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("statusId", workEffort.getString("currentStatusId")), true).get("description",locale))!}</td>
      </tr>
      <#assign alt_row = !alt_row>
    </#list>
  </tbody></table>
  <#if (activities.size() > 0)>
    <h2>${uiLabelMap.WorkEffortWorkflowActivitiesUser}</h2>
    <br />
    <table class="table">
     <thead>
      <tr>
        <th>${uiLabelMap.CommonStartDateTime}</th>
        <th>${uiLabelMap.WorkEffortPriority}</th>
        <th>${uiLabelMap.WorkEffortActivityStatus}</th>
        <th>${uiLabelMap.WorkEffortMyStatus}</th>
        <th>${uiLabelMap.PartyRole}</th>
        <th>${uiLabelMap.WorkEffortActivityName}</th>
        <th>${uiLabelMap.CommonEdit}</th>
      </tr>
     <thead>
     <tbody>
      <#assign alt_row = false>
      <#list activities as workEffort>
        <tr<#if alt_row> class="alternate-row"</#if>>
          <td>${(workEffort.estimatedStartDate)!}</td>
          <td>${workEffort.priority!}</td>
          <td>${(delegator.findOne("StatusItem", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("statusId", workEffort.getString("currentStatusId")), true).get("description",locale))!}</td>
          <td>${(delegator.findOne("StatusItem", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("statusId", workEffort.getString("statusId")), true).get("description",locale))!}</td>
          <td>${workEffort.roleTypeId}</td>
          <td><a href="<@ofbizUrl>WorkEffortSummary?workEffortId=${workEffort.workEffortId}</@ofbizUrl>">${workEffort.workEffortName}</a></td>
          <td class="button-col"><a href="<@ofbizUrl>WorkEffortSummary?workEffortId=${workEffort.workEffortId}</@ofbizUrl>">${workEffort.workEffortId}</a></td>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
    </tbody></table>
  </#if>
  <#if (roleActivities.size() > 0)>
    <h2>${uiLabelMap.WorkEffortWorkflowActivitiesUserRole}</h2>
    <br />
    <table class="table">
      <tr class="header-row-2">
        <td>${uiLabelMap.CommonStartDateTime}</td>
        <td>${uiLabelMap.WorkEffortPriority}</td>
        <td>${uiLabelMap.WorkEffortActivityStatus}</td>
        <td>${uiLabelMap.WorkEffortMyStatus}</td>
        <td>${uiLabelMap.PartyRole}</td>
        <td>${uiLabelMap.WorkEffortActivityName}</td>
        <td>${uiLabelMap.CommonEdit}</td>
      </tr>
      <#assign alt_row = false>
      <#list roleActivities as workEffort>
        <tr<#if alt_row> class="alternate-row"</#if>>
          <td>${(workEffort.estimatedStartDate)!}</td>
          <td>${workEffort.priority!}</td>
          <td>${(delegator.findOne("StatusItem", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("statusId", workEffort.getString("currentStatusId")), true).get("description",locale))!}</td>
          <td>${(delegator.findOne("StatusItem", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("statusId", workEffort.getString("statusId")), true).get("description",locale))!}</td>
          <td>${workEffort.roleTypeId}</td>
          <td><a href="<@ofbizUrl>WorkEffortSummary?workEffortId=${workEffort.workEffortId}</@ofbizUrl>">${workEffort.workEffortName}</a></td>
          <td class="button-col"><a href="<@ofbizUrl>acceptRoleAssignment?workEffortId=${workEffort.workEffortId}&amp;partyId=${workEffort.partyId}&amp;roleTypeId=${workEffort.roleTypeId}&amp;fromDate=${workEffort.fromDate.toString()}</@ofbizUrl>">${uiLabelMap.WorkEffortAcceptAssignment}&nbsp;[${workEffort.workEffortId}]</a></td>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
    </tbody></table>
  </#if>
  <#if (groupActivities.size() > 0)>
    <h2>${uiLabelMap.WorkEffortWorkflowActivitiesUserGroup}</h2>
    <br />
    <table class="table">
      <tr class="header-row-2">
        <td>${uiLabelMap.CommonStartDateTime}</td>
        <td>${uiLabelMap.WorkEffortPriority}</td>
        <td>${uiLabelMap.WorkEffortActivityStatus}</td>
        <td>${uiLabelMap.WorkEffortMyStatus}</td>
        <td>${uiLabelMap.PartyGroupPartyId}</td>
        <td>${uiLabelMap.WorkEffortActivityName}</td>
        <td>${uiLabelMap.CommonEdit}</td>
      </tr>
      <#assign alt_row = false>
      <#list groupActivities as workEffort>
        <tr<#if alt_row> class="alternate-row"</#if>>
          <td>${(workEffort.estimatedStartDate)!}</td>
          <td>${workEffort.priority!}</td>
          <td>${(delegator.findOne("StatusItem", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("statusId", workEffort.getString("currentStatusId")), true).get("description",locale))!}</td>
          <td>${(delegator.findOne("StatusItem", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("statusId", workEffort.getString("statusId")), true).get("description",locale))!}</td>
          <td>${workEffort.groupPartyId}</td>
          <td><a href="<@ofbizUrl>WorkEffortSummary?workEffortId=${workEffort.workEffortId}</@ofbizUrl>">${workEffort.workEffortName}</a></td>
          <td class="button-col"><a href="<@ofbizUrl>acceptassignment?workEffortId=${workEffort.workEffortId}&amp;partyId=${workEffort.partyId}&amp;roleTypeId=${workEffort.roleTypeId}&amp;fromDate=${workEffort.fromDate}</@ofbizUrl>">${uiLabelMap.WorkEffortAcceptAssignment}&nbsp;[${workEffort.workEffortId}]</a></td>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
   </tbody> </table>
  </#if>

</div>
</div>
</article>
</div>
</section>
