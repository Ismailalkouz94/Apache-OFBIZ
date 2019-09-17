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

<#setting locale = locale.toString()>
<#setting time_zone = timeZone.getID()>
<div class="portal-menu">
<li>
<a href="<@ofbizContentUrl>/workeffort/control/EditWorkEffort?workEffortTypeId=TASK&amp;currentStatusId=CAL_NEEDS_ACTION</@ofbizContentUrl>">${uiLabelMap.WorkEffortNewTask}   
</a>
</li>
</div>
<div class="row">

<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-4" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.WorkEffortAssignedTasks}</h2>
<span class="jarviswidget-loader"><i class="fa fa-refresh fa-spin"></i></span>

</header>
<div role="content"></h5>



<div class="screenlet">

  <table class="table table-bordered table-striped" cellspacing="0">
<thead>
    <tr>
      <th>${uiLabelMap.CommonStartDateTime}</th>
      <th>${uiLabelMap.WorkEffortPriority}</th>
      <th>${uiLabelMap.WorkEffortStatus}</th>
      <th>${uiLabelMap.WorkEffortTaskName}</th>
      <th>${uiLabelMap.CommonEdit}</th>
    </tr>
</thead>
    <#assign alt_row = false>
    <#list tasks as workEffort>
      <tr<#if alt_row> class="alternate-row"</#if>>
        <td>${(workEffort.estimatedStartDate.toString())!}</td>
        <td>${workEffort.priority!}</td>
        <td>${(delegator.findOne("StatusItem", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("statusId", workEffort.getString("currentStatusId")), true).get("description",locale))!}</td>
        <td><a href="<@ofbizContentUrl>/workeffort/control/WorkEffortSummary?workEffortId=${workEffort.workEffortId}</@ofbizContentUrl>">${workEffort.workEffortName}</a></td>
        <td class="button-col"><a href="<@ofbizContentUrl>/workeffort/control/WorkEffortSummary?workEffortId=${workEffort.workEffortId}</@ofbizContentUrl>">${workEffort.workEffortId}</a></td>
      </tr>
      <#assign alt_row = !alt_row>
    </#list>
  </table>
  <#if (activities.size() > 0)>
    <h3>${uiLabelMap.WorkEffortWorkflowActivitiesUser}</h3>
    <table class="table table-bordered table-striped" cellspacing="0">
<thead>
      <tr>
        <th>${uiLabelMap.CommonStartDateTime}</th>
        <th>${uiLabelMap.WorkEffortPriority}</th>
        <th>${uiLabelMap.WorkEffortActivityStatus}</th>
        <th>${uiLabelMap.WorkEffortMyStatus}</th>
        <th>${uiLabelMap.PartyRole}</td>
        <th>${uiLabelMap.WorkEffortActivityName}</th>
        <th>${uiLabelMap.CommonEdit}</th>

      </tr>
</thead>
      <#assign alt_row = false>
      <#list activities as workEffort>
        <tr<#if alt_row> class="alternate-row"</#if>>
          <td>${(workEffort.estimatedStartDate.toString())!}</td>
          <td>${workEffort.priority!}</td>
          <td>${(delegator.findOne("StatusItem", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("statusId", workEffort.getString("currentStatusId")), true).get("description",locale))!}</td>
          <td>${(delegator.findOne("StatusItem", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("statusId", workEffort.getString("statusId")), true).get("description",locale))!}</td>
          <td>${workEffort.roleTypeId}</td>
          <td><a href="<@ofbizContentUrl>/workeffort/control/WorkEffortSummary?workEffortId=${workEffort.workEffortId}</@ofbizContentUrl>">${workEffort.workEffortName}</a></td>
          <td class="button-col"><a href="<@ofbizContentUrl>/workeffort/control/WorkEffortSummary?workEffortId=${workEffort.workEffortId}</@ofbizContentUrl>">${workEffort.workEffortId}</a></td>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
    </table>
  </#if>
  <#if (roleActivities.size() > 0)>
    <h3>${uiLabelMap.WorkEffortWorkflowActivitiesUserRole}</h3>
    <table class="basic-table hover-bar" cellspacing="0">
      <tr class="header-row">
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
          <td>${(workEffort.estimatedStartDate.toString())!}</td>
          <td>${workEffort.priority!}</td>
          <td>${(delegator.findOne("StatusItem", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("statusId", workEffort.getString("currentStatusId")), true).get("description",locale))!}</td>
          <td>${(delegator.findOne("StatusItem", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("statusId", workEffort.getString("statusId")), true).get("description",locale))!}</td>
          <td>${workEffort.roleTypeId}</td>
          <td><a href="<@ofbizContentUrl>/workeffort/control/WorkEffortSummary?workEffortId=${workEffort.workEffortId}</@ofbizContentUrl>">${workEffort.workEffortName}</a></td>
          <td class="button-col"><a href="<@ofbizContentUrl>/workeffort/control/acceptRoleAssignment?workEffortId=${workEffort.workEffortId}&amp;partyId=${workEffort.partyId}&amp;roleTypeId=${workEffort.roleTypeId}&amp;fromDate=${workEffort.fromDate.toString()}</@ofbizContentUrl>">${uiLabelMap.WorkEffortAcceptAssignment}&nbsp;[${workEffort.workEffortId}]</a></td>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
    </table>
  </#if>
  <#if (groupActivities.size() > 0)>
    <h3>${uiLabelMap.WorkEffortWorkflowActivitiesUserGroup}</h3>
    <table class="basic-table hover-bar" cellspacing="0">
      <tr class="header-row">
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
          <td>${(workEffort.estimatedStartDate.toString())!}</td>
          <td>${workEffort.priority!}</td>
          <td>${(delegator.findOne("StatusItem", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("statusId", workEffort.getString("currentStatusId")), true).get("description",locale))!}</td>
          <td>${(delegator.findOne("StatusItem", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("statusId", workEffort.getString("statusId")), true).get("description",locale))!}</td>
          <td>${workEffort.groupPartyId}</td>
          <td><a href="<@ofbizContentUrl>/workeffort/control/WorkEffortSummary?workEffortId=${workEffort.workEffortId}</@ofbizContentUrl>">${workEffort.workEffortName}</a></td>
          <td class="button-col"><a href="<@ofbizContentUrl>/workeffort/control/acceptassignment?workEffortId=${workEffort.workEffortId}&amp;partyId=${workEffort.partyId}&amp;roleTypeId=${workEffort.roleTypeId}&amp;fromDate=${workEffort.fromDate}</@ofbizContentUrl>">${uiLabelMap.WorkEffortAcceptAssignment}&nbsp;[${workEffort.workEffortId}]</a></td>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
    </table>
  </#if>
</div>
</div>
</article>
</div>
