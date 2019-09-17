

  <div id="ListRelatedAccounts" class="screenlet">
    <div class="screenlet-body">
        <table class="table table-bordered table-striped" cellspacing="0">
          <tr class="header-row">
            <td>To Party ID</td>
            <td>${uiLabelMap.CommonType}</td>
            <td>Comments</td>
          </tr>
          <#assign alt_row = false>
          <#list contacts as contactsObj>
               <tr<#if alt_row> class="alternate-row"</#if>>
                 
                 <#assign partyName = delegator.findOne("PartyNameView", {"partyId" : contactsObj.partyIdTo!}, true)>
                 <td>${partyName.firstName!} ${partyName.middleName!} ${partyName.lastName!} ${partyName.groupName!} 
                     <a href="<@ofbizUrl>viewprofile?partyId=${contactsObj.partyIdTo!}</@ofbizUrl>">[${contactsObj.partyIdTo!}]</a></td>
            
                <#assign partyRelationshipType = delegator.findOne("PartyRelationshipType", {"partyRelationshipTypeId" : contactsObj.partyRelationshipTypeId!}, true)>
                <td>${partyRelationshipType.partyRelationshipName!}</td>
                
                <td>${contactsObj.comments!}</td>
                
                <#assign alt_row = !alt_row>   
              </tr>
          </#list>
        </table>
    </div>
  </div>
