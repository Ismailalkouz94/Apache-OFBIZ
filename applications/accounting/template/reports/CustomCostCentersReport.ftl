<#ftl encoding="utf-8">
 
<#assign costCenterReport = Static["org.apache.ofbiz.accounting.report.CostCentersReport"].getReportXMLCostCenters(request, response)>
<#if costCenterReport?has_content>
<div class="table-responsive">
    <p>Note : All Expense Gl Type Only</p>
  <table class="table table-bordered table-striped" cellspacing="0">
      <thead>
    <tr class="header-row">
      <th>PartyId</th>
      <th>GroupName</th>
      <th>DR</th>
      <th>CR</th>
      <th>Balance</th>
      <th> </th>
    
    </tr>
      </thead>
      <#assign alt_row = false>
    <#list costCenterReport as list>
        <tr<#if alt_row> class="alternate-row"</#if>>

            
          <td> <a href="<@ofbizUrl>FindAcctgTransEntries?PartyIdCost=${list.PartyId!}</@ofbizUrl>">${list.PartyId!}</a></td>
          <td>${list.GroupName!}</td>
          <td>${(list.DR)!}</td>
          <td>${(list.CR)!}</td>
          <td>${(list.Balance)!}</td>
          <td> 
<!--              <form method="post" name="lookupparty" action="<@ofbizUrl>findEmployees</@ofbizUrl>" class="basic-form">
              <input type="submit" value="Statement of Acccount" />
              </form>-->
          <a href="<@ofbizUrl>findStatementReport?PartyIdCost=${list.PartyId!}&Report=2&notTrans=E</@ofbizUrl>" class="smallSubmit">Statement</a>
          </td>
           
          
        <#assign alt_row = !alt_row>    
        </tr>
</#list>
  </table>
</div>
<#else>
  <div class="alert alert-info fade in">${uiLabelMap.CommonNoRecordFound}</div>
</#if>