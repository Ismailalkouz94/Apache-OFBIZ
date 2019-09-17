

  <div id="ListRelatedAccounts" class="screenlet">
    <div class="screenlet-body">
        <table class="table table-bordered table-striped" cellspacing="0">
<thead>
          <tr class="header-row">
            <th>From Party ID</th>
            <th>${uiLabelMap.CommonType}</th>
            <th>Comments</th>
          </tr>
</thead>
          <#assign alt_row = false>
          <#list accounts as accountsObj>
               <tr<#if alt_row> class="alternate-row"</#if>>
                 
                 <#assign partyName = delegator.findOne("PartyNameView", {"partyId" : accountsObj.partyIdFrom!}, true)>
                 <td>${partyName.firstName!} ${partyName.middleName!} ${partyName.lastName!} ${partyName.groupName!} 
                     <a href="<@ofbizUrl>viewprofile?partyId=${accountsObj.partyIdFrom!}</@ofbizUrl>">[${accountsObj.partyIdFrom!}]</a></td>
            
                <#assign partyRelationshipType = delegator.findOne("PartyRelationshipType", {"partyRelationshipTypeId" : accountsObj.partyRelationshipTypeId!}, true)>
                <td>${partyRelationshipType.partyRelationshipName!}</td>
                
                <td>${accountsObj.comments!}</td>
                
                <#assign alt_row = !alt_row>   
              </tr>
          </#list>
        </table>
    </div>
  </div>
