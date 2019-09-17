
         <div id="personalInformation" class="screenlet">
     <div class="screenlet-body">
     <#assign statusItem = delegator.findOne("StatusItem", {"statusId" : (lookupGroup.statusId)!'PARTY_ENABLED'}, true) />

      <#if lookupGroup?has_content>
        <table class="basic-table" cellspacing="0"> 
   <#if partyGroupLogoLinkUrl?has_content>
            <tr>
              <td><label>${uiLabelMap.CommonOrganizationLogo}</label></td>
              <td><image alternate="${uiLabelMap.CommonOrganizationLogo}" src="${(lookupGroup.partyGroupLogoLinkUrl)!}" style="cssImgStandard"/> </td>   
            <tr>
    </#if>
              <tr>
              <td width="30%"><label>partyId</label></td>
              <td>${(lookupGroup.partyId)!} </td> 
            <tr>
              <tr>
              <td ><label>Group Name</label></td>
              <td>${(lookupGroup.groupName)!}  </td>  
            <tr> 
              <tr>
              <tr>
              <td ><label>Group Name Local</label></td>
              <td>${(lookupGroup.groupNameLocal)!}</td> 
            <tr>     
           <tr>
              <tr>
              <td ><label>Office Site Name</label></td>
              <td>${(lookupGroup.officeSiteName)!}</td> 
            <tr>  
              <tr>
              <td ><label>Annual revenue</label></td>
              <td>${(lookupGroup.annualRevenue)!} </td> 
            <tr>
                <tr>
              <td ><label>Number of employees</label></td>
              <td>${(lookupGroup.numEmployees)!} </td> 
            <tr>
                <tr>
              <td ><label>Ticker symbol</label></td>
              <td>${(lookupGroup.tickerSymbol)!} </td> 
            <tr>
                  <tr>
              <td ><label>${uiLabelMap.CommonDescription}</label></td>
              <td>${(lookupGroup.description)!} </td> 
            <tr>
                  <tr>
              <td ><label>${uiLabelMap.CommonCurrency}</label></td>
              <td>${(lookupGroup.preferredCurrencyUomId)!} </td> 
            <tr>
                  <tr>
              <td ><label>External Id</label></td>
              <td>${(lookupGroup.externalId)!} </td> 
            <tr>
              <tr>
              <td ><label>Status ID</label></td>
              <td> ${(statusItem.description)!}
              </td> 
            <tr>   
        </table>
      <#else>
       -1
      </#if>
    </div>
  </div>


