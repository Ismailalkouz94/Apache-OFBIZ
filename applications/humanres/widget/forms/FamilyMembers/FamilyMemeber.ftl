
<div class="row">

 <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
      <header>
      <span class="jarviswidget-loader"><i class="fa fa-refresh fa-spin"></i></span>
      <h2><a  href="<@ofbizUrl>NewFamilyMember?partyId=${parameters.partyId}</@ofbizUrl>" title="${uiLabelMap.CommonNew}"><i class="fa fa-lg fa-fw fa-plus-circle" style="color:#fff"></i></a>Depandances</h2>
      </header>
<div role="content"> 
  <div id="PartyIdentificationPanel" class="screenlet" navigation-menu-name="NewPartyIdentification">
    <div class="screenlet-body">
        <table class="table table-bordered table-striped" cellspacing="0">
            <#assign FamilyMemberList = delegator.findAll("Person",true) />
    <thead>
          <tr class="header-row">
            <td style="display:none;"> </td>
            <td style="display:none;"> </td>
            <td>Party Id</td>
            <td>Name</td>
            <td>Birth Date</td>
            <td>Relationship</td>
          </tr>
          </thead>
             <#list FamilyMemberList as list>
               <tr>
                   <#if list.relatedTo! == parameters.partyId>
                <td style="display:none;"></td>
                <td style="display:none;"></td>
                <td><a href="<@ofbizUrl>EmployeeProfile?roleType=FM&partyId=${(list.partyId)!}</@ofbizUrl>"><i> ${(list.partyId)!} </i></a></td>
                <td>${(list.firstName)!}</td>
                <td>${(list.birthDate)!}</td>
                <td>${(list.familyMemberType)!}</td>
                <!--<td class="button-col"><a href="#">iou</a></td>-->
                </#if>
              </tr>
             </#list>
        </table>
    </div>
</div>
</div>
</article>
</div>