
         <div id="personalInformation" class="screenlet">
     <div class="screenlet-body">
     <#assign statusItem = delegator.findOne("StatusItem", {"statusId" : (lookupPerson.statusId)!}, true) />
         

      <#if lookupPerson?has_content>
        <table class="table" cellspacing="0"> 
   <#if partyContentId?has_content>
            <tr>
              <td><label>${uiLabelMap.FormFieldTitle_personalImage}</label></td>
              <td><image src="${(lookupPerson.personalImage)!}" style="cssImgStandard"/> </td>   
            <tr>
    </#if>
              <tr>
              <td ><label>partyId</label></td>
              <td>${(lookupPerson.partyId)!} </td> 
            <tr>
              <tr>
              <td ><label>${uiLabelMap.PartyName}</label></td>
              <td>${(lookupPerson.personalTitle)!} ${(lookupPerson.firstName)!} ${(lookupPerson.middleName)!} ${(lookupPerson.lastName)!} </td>  
            <tr> 
              <tr>
              <tr>
              <td ><label>${uiLabelMap.PartyComments}</label></td>
              <td>${(lookupPerson.comments)!}</td> 
            <tr>     
           <tr>
              <tr>
              <td ><label>external Id</label></td>
              <td>${(lookupPerson.externalId)!}</td> 
            <tr>  
                 <tr>
              <tr>
              <td ><label>status Id</label></td>
              <td> 
                 ${(statusItem.description)!} 
              </td> 
            <tr>   
        </table>
      <#else>
       -1
      </#if>
    </div>
  </div>


