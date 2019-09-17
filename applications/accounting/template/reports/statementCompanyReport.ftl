<?xml version="1.0" encoding="UTF-8"?>
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
<#assign costCenterReport = Static["org.apache.ofbiz.accounting.report.CostCentersReport"].getReportXMLStatementCompany(request, response)>
<#if costCenterReport?has_content>
  <table class="table table-bordered table-striped" cellspacing="0">
      <thead>
    <tr class="header-row">
      <th>PartyId</th>
      <th>GroupName</th>
      <th>CR</th>
      <th>DR</th>
      <th>Balance</th>
      <th> </th>      
    </tr>
    </thead>
      <#assign alt_row = false>
    <#list costCenterReport as list>
        <tr<#if alt_row> class="alternate-row"</#if>>

            
          <td> <a href="<@ofbizUrl>FindAcctgTransEntries?PartyIdCost=${list.PartyId!}</@ofbizUrl>">${list.PartyId!}</a></td>
          <td>${list.GroupName!}</td>
          <td>${list.CR!}</td>
          <td>${list.DR!}</td>
          <td>${list.Balance!}</td>
          <td> 
<!--              <form method="post" name="lookupparty" action="<@ofbizUrl>findEmployees</@ofbizUrl>" class="basic-form">
              <input type="submit" value="Statement of Acccount" />
              </form>-->
          <a href="<@ofbizUrl>findStatementReport?PartyIdCost=${list.PartyId!}</@ofbizUrl>" class="smallSubmit">Statement</a>
          </td>
           
          
        <#assign alt_row = !alt_row>    
        </tr>
</#list>
  </table>
<#else>
  <div class="alert alert-info fade in">${uiLabelMap.CommonNoRecordFound}</div>
</#if>