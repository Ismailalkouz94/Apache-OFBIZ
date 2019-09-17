<section id="widget-grid">
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<span class="widget-icon">
         <#if security.hasEntityPermission("PARTYMGR", "_GRP_UPDATE", session)>
         <a href="<@ofbizUrl>editperson?partyId=${parameters.partyId}</@ofbizUrl>"><i style="color:#fff" class="fa fa-lg fa-fw fa-pencil-square-o" title="Update"></i></a>
        </#if>
</span>
<h2>${uiLabelMap.PartyPersonalInformation}</h2>
</header>
<div role="content">
         <div id="personalInformation" class="screenlet">
     <div class="screenlet-body">
    <#assign employmentData = employmentData/>
     <#assign statusItem1 = delegator.findAll("StatusItem",true) />

      <#if lookupPerson?has_content>
          <#assign statusItem = (delegator.findOne("StatusItem", {"statusId" : lookupPerson.EmplStatus}, true))!>
        <table class="basic-table" cellspacing="0"> 
   <#if partyContentId?has_content>
            <tr>
              <td><label>${uiLabelMap.FormFieldTitle_personalImage}</label></td>
              <td><image src="${(lookupPerson.personalImage)!}" style="cssImgStandard"/> </td>   
            <tr>
    </#if>
              <tr>
              <td width="20%"><label>partyId</label></td>
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
<!--             <tr>
              <td ><label>external Id</label></td>
              <td>{(lookupPerson.externalId)!}</td> 
            <tr>  -->
            <tr>
              <td ><label>Employee Status</label></td>
              <td>${(statusItem.description)!}</td> 
            <tr>  
                 <tr>
              <tr>
              <td ><label>status Id</label></td>
              <td> <#list statusItem1 as list2>
                            <#if list2.statusId = (lookupPerson.statusId)! "0"> ${(list2.description)!}</#if>
                      </#list>
              </td> 
            <tr>   
        </table>
      <#else>
      <div class="alert alert-info fade in">Thers No Party Id = ${parameters.partyId}</div>
      </#if>
    </div>
  </div>

</div>
</div>
</article>
</div>
        <!--...............................-->
           <!--Current Employment information.................................-->
    <#if (parameters.roleType)! != 'FM'>
<div class="row">
<article class="col-xs-6 col-sm-6 col-md-6 col-lg-6 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.HumanResCurrentEmploymentData}</h2>
</header>
<div role="content">

     <div id="currentEmploymentInformation" class="screenlet">
     <div class="screenlet-body">
    <#assign employmentData = employmentData/>
     <#assign partyNameView3 = delegator.findAll("PartyNameView",true) />
       
      <#if employmentData?has_content>
         
         
        <table class="basic-table" cellspacing="0"> 
               <tr>          
              <td ><label>Hired Date</label></td>
              <td id="hiriedDate"></td>            
            </tr>
           <tr>
              <td ><label>Department</label></td>
              <td id="departmentId"></td> 
             </tr>   
             <tr>
              <td ><label>Position</label></td>
              <td id="positionId"></td>            
            </tr>
             <tr>          
              <td ><label>Grade</label></td>
              <td id="gradeId"></td>            
            </tr>            
             <tr>          
              <td ><label>Degree</label></td>
              <td id="degreeId"></td>            
            </tr>            
        </table>
      <#else>
       -1
      </#if>
    </div>
  </div>
  </div>
</div>
</article>
        <!--.............................-->
        <!--User Name(s).................................-->  
<article class="col-xs-6 col-sm-6 col-md-6 col-lg-6 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
<header>
<span id ="addUserLoginBtn" class="widget-icon">         <#if security.hasEntityPermission("PARTYMGR", "_CREATE", session)>
         <a href="<@ofbizUrl>ProfileCreateNewLogin?partyId=${party.partyId}</@ofbizUrl>"><i style="color:#fff" class="fa fa-lg fa-fw fa-plus-circle"  title="Add"></i></a>
        </#if></span>
<h2>${uiLabelMap.PartyUserName}</h2>
</header>
<div role="content">     
  <div id="partyUserLogins" class="screenlet">



    <div class="screenlet-body">
      <#if userLogins?has_content>
        <table class="basic-table" cellspacing="0">
          <#list userLogins as userUserLogin>
            <tr>
              <td ><label>${uiLabelMap.PartyUserLogin}</label></td>
              <td><label>${userUserLogin.userLoginId}</label></td>
              <td>
                <#assign enabled = uiLabelMap.PartyEnabled>
                <#if (userUserLogin.enabled)?default("Y") == "N">
                  <#if userUserLogin.disabledDateTime??>
                    <#assign disabledTime = userUserLogin.disabledDateTime.toString()>
                  <#else>
                    <#assign disabledTime = "??">
                  </#if>
                  <#assign enabled = uiLabelMap.PartyDisabled + " - " + disabledTime>
                </#if>
                ${enabled}
              </td>
              <td class="button-col">
                <#if security.hasEntityPermission("PARTYMGR", "_CREATE", session)>
                  <a class="btn btn-primary" href="<@ofbizUrl>ProfileEditUserLogin?partyId=${party.partyId}&amp;userLoginId=${userUserLogin.userLoginId}</@ofbizUrl>">${uiLabelMap.CommonEdit}</a>
                </#if>
                <#if security.hasEntityPermission("SECURITY", "_VIEW", session)>
                  <a class="btn btn-primary" href="<@ofbizUrl>ProfileEditUserLoginSecurityGroups?partyId=${party.partyId}&amp;userLoginId=${userUserLogin.userLoginId}</@ofbizUrl>">${uiLabelMap.SecurityGroups}</a>
                </#if>
              </td>
            </tr>
          </#list>
        </table>
      <#else>
        <div class="alert alert-info fade in">${uiLabelMap.PartyNoUserLogin}</div>
      </#if>
    </div>
  </div>
  </div>
</div>
</article>
</div>
    </#if>
        <!--.............................-->
          <!--  Contact Information......................................-->
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-3" data-widget-editbutton="false">
<header>
<span class="widget-icon"> 
        <#if security.hasEntityPermission("PARTYMGR", "_CREATE", session) || userLogin.partyId == partyId>
         <a href="<@ofbizUrl>editcontactmech?partyId=${partyId}</@ofbizUrl>"><i style="color:#fff" class="fa fa-lg fa-fw fa-plus-circle"  title="Add"></i></a>
        </#if> </span>
<h2>${uiLabelMap.PartyContactInformation}</h2>
</header>
<div role="content">

  <div id="partyContactInfo" class="screenlet">


    <div class="screenlet-body">
      <#if contactMeches?has_content>
        <table class="basic-table" cellspacing="0">
          <tr>
            <th><label>${uiLabelMap.PartyContactType}</label></th>
            <th><label>${uiLabelMap.PartyContactInformation}</label></th>
            <th><label>${uiLabelMap.PartyContactSolicitingOk}</label></th>
            <th>&nbsp;</th>
          </tr>
          <#list contactMeches as contactMechMap>
            <#assign contactMech = contactMechMap.contactMech>
            <#assign partyContactMech = contactMechMap.partyContactMech>
            <tr><td colspan="4"><hr /></td></tr>
            <tr>
              <td class="label align-top">${contactMechMap.contactMechType.get("description",locale)}</td>
              <td>
                <#list contactMechMap.partyContactMechPurposes as partyContactMechPurpose>
                  <#assign contactMechPurposeType = partyContactMechPurpose.getRelatedOne("ContactMechPurposeType", true)>
                  <div>
                    <#if contactMechPurposeType?has_content>
                      <b>${contactMechPurposeType.get("description",locale)}</b>
                    <#else>
                      <b>${uiLabelMap.PartyMechPurposeTypeNotFound}: "${partyContactMechPurpose.contactMechPurposeTypeId}"</b>
                    </#if>
                    <#if partyContactMechPurpose.thruDate?has_content>
                      (${uiLabelMap.CommonExpire}: ${partyContactMechPurpose.thruDate})
                    </#if>
                  </div>
                </#list>
                <#if "POSTAL_ADDRESS" = contactMech.contactMechTypeId>
                  <#if contactMechMap.postalAddress?has_content>
                    <#assign postalAddress = contactMechMap.postalAddress>
                    ${setContextField("postalAddress", postalAddress)}
                    ${screens.render("component://party/widget/partymgr/PartyScreens.xml#postalAddressHtmlFormatter")}
                    <#if postalAddress.geoPointId?has_content>
                      <#if contactMechPurposeType?has_content>
                        <#assign popUptitle = contactMechPurposeType.get("description", locale) + uiLabelMap.CommonGeoLocation>
                      </#if>
                      <a href="javascript:popUp('<@ofbizUrl>GetPartyGeoLocation?geoPointId=${postalAddress.geoPointId}&partyId=${partyId}</@ofbizUrl>', '${popUptitle!}', '450', '550')" class="buttontext">${uiLabelMap.CommonGeoLocation}</a>
                    </#if>
                  </#if>
                <#elseif "TELECOM_NUMBER" = contactMech.contactMechTypeId>
                  <#if contactMechMap.telecomNumber?has_content>
                    <#assign telecomNumber = contactMechMap.telecomNumber>
                    <div>
                      ${telecomNumber.countryCode!}
                      <#if telecomNumber.areaCode?has_content>${telecomNumber.areaCode?default("000")}-</#if><#if telecomNumber.contactNumber?has_content>${telecomNumber.contactNumber?default("000-0000")}</#if>
                      <#if partyContactMech.extension?has_content>${uiLabelMap.PartyContactExt}&nbsp;${partyContactMech.extension}</#if>
                        <#if !telecomNumber.countryCode?has_content || telecomNumber.countryCode = "011">
                          <a target="_blank" href="${uiLabelMap.CommonLookupAnywhoLink}" class="buttontext">${uiLabelMap.CommonLookupAnywho}</a>
                          <a target="_blank" href="${uiLabelMap.CommonLookupWhitepagesTelNumberLink}" class="buttontext">${uiLabelMap.CommonLookupWhitepages}</a>
                        </#if>
                    </div>
                  </#if>
                <#elseif "EMAIL_ADDRESS" = contactMech.contactMechTypeId>
                  <div>
                    ${contactMech.infoString!}
                    <form method="post" action="<@ofbizUrl>NewDraftCommunicationEvent</@ofbizUrl>" onsubmit="javascript:submitFormDisableSubmits(this)" name="createEmail${contactMech.infoString?replace("&#64;","")?replace("&#x40;","")?replace(".","")}">
                      <#if userLogin.partyId?has_content>
                      <input name="partyIdFrom" value="${userLogin.partyId}" type="hidden"/>
                      </#if>
                      <input name="partyIdTo" value="${partyId}" type="hidden"/>
                      <input name="contactMechIdTo" value="${contactMech.contactMechId}" type="hidden"/>
                      <input name="my" value="My" type="hidden"/>
                      <input name="statusId" value="COM_PENDING" type="hidden"/>
                      <input name="communicationEventTypeId" value="EMAIL_COMMUNICATION" type="hidden"/>
                    </form><a class="buttontext" href="javascript:document.createEmail${contactMech.infoString?replace("&#64;","")?replace("&#x40;","")?replace(".","")}.submit()">${uiLabelMap.CommonSendEmail}</a>
                  </div>
                <#elseif "WEB_ADDRESS" = contactMech.contactMechTypeId>
                  <div>
                    ${contactMech.infoString!}
                    <#assign openAddress = contactMech.infoString?default("")>
                    <#if !openAddress?starts_with("http") && !openAddress?starts_with("HTTP")><#assign openAddress = "http://" + openAddress></#if>
                    <a target="_blank" href="${openAddress}" class="buttontext">${uiLabelMap.CommonOpenPageNewWindow}</a>
                  </div>
                <#else>
                  <div>${contactMech.infoString!}</div>
                </#if>
                <div>(${uiLabelMap.CommonUpdated}:&nbsp;${partyContactMech.fromDate})</div>
                <#if partyContactMech.thruDate?has_content><div><b>${uiLabelMap.PartyContactEffectiveThru}:&nbsp;${partyContactMech.thruDate}</b></div></#if>
                <#-- create cust request -->
                <#if custRequestTypes??>
                  <form name="createCustRequestForm" action="<@ofbizUrl>createCustRequest</@ofbizUrl>" method="post" onsubmit="javascript:submitFormDisableSubmits(this)">
                    <input type="hidden" name="partyId" value="${partyId}"/>
                    <input type="hidden" name="fromPartyId" value="${partyId}"/>
                    <input type="hidden" name="fulfillContactMechId" value="${contactMech.contactMechId}"/>
                    <select name="custRequestTypeId">
                      <#list custRequestTypes as type>
                        <option value="${type.custRequestTypeId}">${type.get("description", locale)}</option>
                      </#list>
                    </select>
                    <input type="submit" class="smallSubmit" value="${uiLabelMap.PartyCreateNewCustRequest}"/>
                  </form>
                </#if>
              </td>
              <td valign="top"><b>(${partyContactMech.allowSolicitation!})</b></td>
              <td class="button-col">
                <#if security.hasEntityPermission("PARTYMGR", "_UPDATE", session) || userLogin.partyId == partyId>
                  <a class="btn btn-primary1" href="<@ofbizUrl>editcontactmech?partyId=${partyId}&amp;contactMechId=${contactMech.contactMechId}</@ofbizUrl>">${uiLabelMap.CommonUpdate}</a>
                </#if>
                <#if security.hasEntityPermission("PARTYMGR", "_DELETE", session) || userLogin.partyId == partyId>
                  <form name="partyDeleteContact" method="post" action="<@ofbizUrl>deleteContactMech</@ofbizUrl>" onsubmit="javascript:submitFormDisableSubmits(this)">
                    <input name="partyId" value="${partyId}" type="hidden"/>
                    <input name="contactMechId" value="${contactMech.contactMechId}" type="hidden"/>
                    <input type="submit" class="btn btn-primary1" value="${uiLabelMap.CommonExpire}"/>
                  </form>
                </#if>
              </td>
            </tr>
          </#list>
        </table>
      <#else>
        <div class="alert alert-info fade in"> ${uiLabelMap.PartyNoContactInformation}</div>
      </#if>
    </div>
  </div>
  </div>
</div>
</article>
</div>
          <!-- ......................................-->
        
        <!-- Party Attribute......................................-->
<div class="row">
<article class="col-xs-6 col-sm-6 col-md-6 col-lg-6 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-4" data-widget-editbutton="false">
<header>
<span class="widget-icon">         <#if security.hasEntityPermission("PARTYMGR", "_CREATE", session)>
         <a href="<@ofbizUrl>editPartyAttribute?partyId=${party.partyId!}</@ofbizUrl>"><i style="color:#fff" class="fa fa-lg fa-fw fa-plus-circle"  title="Add"></i></a>
        </#if> </span>
<h2>${uiLabelMap.PartyAttributes}</h2>
</header>
<div role="content">
      
  <div id="partyAttributes" class="screenlet">



    <div class="screenlet-body">
      <#if attributes?has_content>
        <table class="basic-table hover-bar" cellspacing="0">
            <tr class="header-row">
              <td>${uiLabelMap.CommonName}</td>
              <td>${uiLabelMap.CommonValue}</td>
              <td>&nbsp;</td>
            </tr>
          <#assign alt_row = false>
          <#list attributes as attr>
            <tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
              <td>
                ${attr.attrName!}
              </td>
              <td>
                ${attr.attrValue!}
              </td>
              <td class="button-col">
                <a href="<@ofbizUrl>editPartyAttribute?partyId=${partyId!}&attrName=${attr.attrName!}</@ofbizUrl>">${uiLabelMap.CommonEdit}</a>
              </td>
            </tr>
            <#-- toggle the row color -->
            <#assign alt_row = !alt_row>
          </#list>
        </table>
      <#else>
        <div class="alert alert-info fade in"> ${uiLabelMap.PartyNoPartyAttributesFound}</div>
      </#if>
    </div>
  </div>
  </div>
</div>
</article>

        <!--......................................-->
<!--party Notes...............................-->

<article class="col-xs-6 col-sm-6 col-md-6 col-lg-6 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-6" data-widget-editbutton="false">
<header>
<span class="widget-icon">  <#if security.hasEntityPermission("PARTYMGR", "_NOTE", session)>
         <a href="<@ofbizUrl>AddPartyNote?partyId=${partyId}</@ofbizUrl>"><i style="color:#fff" class="fa fa-lg fa-fw fa-plus-circle"></i></a>
        </#if> </span>
<h2>${uiLabelMap.CommonNotes}</h2>
</header>
<div role="content">
  <div id="partyNotes" class="screenlet">

       
 
    <div class="screenlet-body">
      <#if notes?has_content>
        <table width="100%" border="0" cellpadding="1">
          <#list notes as noteRef>
            <tr>
              <td>
                <div><b>${uiLabelMap.FormFieldTitle_noteName}: </b>${noteRef.noteName!}</div>
                <#if noteRef.noteParty?has_content>
                  <div><b>${uiLabelMap.CommonBy}: </b>${Static["org.apache.ofbiz.party.party.PartyHelper"].getPartyName(delegator, noteRef.noteParty, true)}</div>
                </#if>
                <div><b>${uiLabelMap.CommonAt}: </b>${noteRef.noteDateTime.toString()}</div>
              </td>
              <td>
                ${noteRef.noteInfo}
              </td>
            </tr>
            <#if noteRef_has_next>
              <tr><td colspan="2"><hr/></td></tr>
            </#if>
          </#list>
        </table>
      <#else>
         <div class="alert alert-info fade in">${uiLabelMap.PartyNoNotesForParty}</div>
      </#if>
    </div>
  </div>
  </div>
</div>
</article>
</div>



<!--Party Content......................................-->
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-5" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.PartyContent}</h2>
</header>
    <div role="content">
            <div id="partyContent" class="screenlet">
        <div class="screenlet-body">
              ${screens.render("component://party/widget/partymgr/ProfileScreens.xml#ContentList")}
          <hr />
          <div >${uiLabelMap.PartyAttachContent}</div>
          <form id="uploadPartyContent" method="post" enctype="multipart/form-data" action="<@ofbizUrl>uploadPartyContent</@ofbizUrl>">
            <input class="form-control" type="hidden" name="dataCategoryId" value="PERSONAL"/>
            <input class="form-control" type="hidden" name="contentTypeId" value="DOCUMENT"/>
            <input class="form-control" type="hidden" name="statusId" value="CTNT_PUBLISHED"/>
            <input class="form-control" type="hidden" name="partyId" value="${partyId}" id="contentPartyId"/>
            <input class="btn btn-default" style="width:220px" type="file" name="uploadedFile" class="required error" size="25"/>

            <select class="form-control" name="partyContentTypeId" class="required error">
              <option value="">${uiLabelMap.PartySelectPurpose}</option>
              <#list partyContentTypes as partyContentType>
                <option value="${partyContentType.partyContentTypeId}">${partyContentType.get("description", locale)?default(partyContentType.partyContentTypeId)}</option>
              </#list>
            </select>

            ${uiLabelMap.PartyIsPublic}
            <select class="form-control" name="isPublic">
                <option value="N">${uiLabelMap.CommonNo}</option>
                <option value="Y">${uiLabelMap.CommonYes}</option>
            </select>
            <select class="form-control" name="roleTypeId">
              <option value="">${uiLabelMap.PartySelectRole}</option>
              <#list roles as role>
                <option value="${role.roleTypeId}" <#if role.roleTypeId == "_NA_">selected="selected"</#if>>${role.get("description", locale)?default(role.roleTypeId)}</option>
              </#list>
            </select>
            <input class="btn btn-primary" type="submit" value="${uiLabelMap.CommonUpload}" />
          </form>
          <div id='progress_bar'><div></div></div>
          </div>
      </div>
      </div>
    </div>
</article>
</div>
  <script type="text/javascript">
    jQuery("#uploadPartyContent").validate({
        submitHandler: function(form) {
            <#-- call upload scripts - functions defined in PartyProfileContent.js -->
            uploadPartyContent();
            getUploadProgressStatus();
            form.submit();
        }
    });
  </script>

 
        <!--List Related Accounts  ..............................-->
  <#if (parameters.roleType)! != 'FM'>
<div class="row">
<article id="content2" class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-7" data-widget-editbutton="false">
<header>
<span><h2>&nbsp;&nbsp;${uiLabelMap.PartyListRelatedAccounts}</h2></span>
</header>
<div role="content">

                                                <!-- NEW WIDGET START -->

                        <table id="jqgrid2"></table>
                        <div id="pjqgrid2"></div>

      </div>
</div>
</article>
</div>

        <script type="text/javascript">

 $(document).ready(function() {
      <#assign accounts = delegator.findAll("PartyRelationship",true) />
      <#assign partyNameView2 = delegator.findAll("PartyNameView",true) />
     
      var jqgrid_data2=[];
          var c2=0;

          <#list accounts as list>               
          pageSetUp();
        if("${(list.partyIdTo)! "0"}" === "${parameters.partyId}"){ 
        if("${(list.roleTypeIdFrom)! "0"}" === "ACCOUNT"){ 
        if("${(list.roleTypeIdTo)! "0"}" === "CONTACT"){    
            var myDefaul="ss";
         jqgrid_data2.push ({ 
                 partyIdFrom : '<a href="/partymgr/control/viewprofile?partyId=${(list.partyIdFrom)!}">${(list.partyIdFrom)!}</a>',
                 partyRelationshipTypeId : "${(list.partyRelationshipTypeId)!}",
                 comments :"${(list.comments)!}"  
         } );
             
  
              c2++; 
            }//end if   
            }//end if  
            }//end if      
  </#list>  

         jQuery("#jqgrid2").jqGrid({
                 data : jqgrid_data2,
                 datatype : "local",
                 height : 'auto',           
                 colNames : ['From Party ID', 'Type', 'Comments'],
                 colModel : [{
                         name : 'partyIdFrom',
                         index : 'partyIdFrom',
                         width: 200,             
                         editable : false
                 },{
                         name : 'partyRelationshipTypeId',
                         index : 'partyRelationshipTypeId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'comments',
                         index : 'comments',
                         width: 200,             
                         editable : false
                 }],
                                           
                 rowNum : 10,
                 rowList : [10, 20, 30],
                 pager : '#pjqgrid2',
                 sortname : 'id',
                 toolbarfilter : true,
                 viewrecords : true,
                 sortorder : "asc",

                         caption : "${uiLabelMap.PartyListRelatedAccounts}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",
                         multiselect : false,                             
                         autowidth : true
                              
         });
// '<a href="EmployeeProfile?editPartyRel=Y&partyId=">Create New</a>'
             
         jQuery("#jqgrid2").jqGrid('inlineNav', "#pjqgrid2");
         /* Add tooltips */
         $('.navtable .ui-pg-button').tooltip({
                 container : 'body'
         });

         jQuery("#m1").click(function() {
                 var s;
                 s = jQuery("#jqgrid2").jqGrid('getGridParam', 'selarrrow');
//                 alert(s);
         });
                    
         jQuery("#m1s").click(function() {
                 jQuery("#jqgrid2").jqGrid('setSelection', "13");
         });

         // remove classes
         $(".ui-jqgrid").removeClass("ui-widget ui-widget-content");
         $(".ui-jqgrid-view").children().removeClass("ui-widget-header ui-state-default");
         $(".ui-jqgrid-labels, .ui-search-toolbar").children().removeClass("ui-state-default ui-th-column ui-th-ltr");
         $(".ui-jqgrid-pager").removeClass("ui-state-default");
         $(".ui-jqgrid").removeClass("ui-widget-content");

         // add classes
         $(".ui-jqgrid-htable").addClass("table table-bordered table-hover");
         $(".ui-jqgrid-btable").addClass("table table-bordered table-striped");

         $(".ui-pg-div").removeClass().addClass("btn btn-sm btn-primary");
         $(".ui-icon.ui-icon-plus").removeClass().addClass("fa fa-plus");
         $(".ui-icon.ui-icon-pencil").removeClass().addClass("fa fa-pencil");
         $(".ui-icon.ui-icon-trash").removeClass().addClass("fa fa-trash-o");
         $(".ui-icon.ui-icon-search").removeClass().addClass("fa fa-search");
         $(".ui-icon.ui-icon-refresh").removeClass().addClass("fa fa-refresh");
         $(".ui-icon.ui-icon-disk").removeClass().addClass("fa fa-save").parent(".btn-primary").removeClass("btn-primary").addClass("btn-success");
         $(".ui-icon.ui-icon-cancel").removeClass().addClass("fa fa-times").parent(".btn-primary").removeClass("btn-primary").addClass("btn-danger");

         $(".ui-icon.ui-icon-seek-prev").wrap("<div class='btn btn-sm btn-default'></div>");
         $(".ui-icon.ui-icon-seek-prev").removeClass().addClass("fa fa-backward");

         $(".ui-icon.ui-icon-seek-first").wrap("<div class='btn btn-sm btn-default'></div>");
         $(".ui-icon.ui-icon-seek-first").removeClass().addClass("fa fa-fast-backward");

         $(".ui-icon.ui-icon-seek-next").wrap("<div class='btn btn-sm btn-default'></div>");
         $(".ui-icon.ui-icon-seek-next").removeClass().addClass("fa fa-forward");

         $(".ui-icon.ui-icon-seek-end").wrap("<div class='btn btn-sm btn-default'></div>");
         $(".ui-icon.ui-icon-seek-end").removeClass().addClass("fa fa-fast-forward");

  }) 
 $(window).on('resize.jqGrid', function() {
         $("#jqgrid2").jqGrid('setGridWidth', $("#content2").width());
 })
          </script>  

        <!--================================================== -->


        <!--Training............................-->
<div class="row">
<article id="content3" class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-8" data-widget-editbutton="false">
<header>
<span><h2>&nbsp;&nbsp;${uiLabelMap.HumanResTrainings}</h2></span>
</header>
<div role="content">


                        <table id="jqgrid1"></table>
                        <div id="pjqgrid1"></div>

</div>
</div>
</article>
</div>
        <!--================================================== -->

        <script src="/rainbowstone/js/plugin/jqgrid/jquery.jqGrid.min.js"></script>
        <script src="/rainbowstone/js/plugin/jqgrid/grid.locale-en.min.js"></script>



        <script type="text/javascript">

 $(document).ready(function() {

      <#assign allowenceEmp = delegator.findAll("PersonTraining",true) />
     <#assign partyNameView = delegator.findAll("PartyNameView",true) />
     
      var jqgrid_data1=[];
          var c=0;

          <#list allowenceEmp as list>               
          pageSetUp();
        if("${(list.partyId)! "0"}" === "${parameters.partyId}"){ 

            var myDefaul="ss";
         jqgrid_data1.push ({ 
                 fromDate : "${(list.fromDate)!}",
                 thruDate : "${(list.thruDate)!}",
                 trainingClassTypeId :"${(list.trainingClassTypeId)!}",
                 approvalStatus : "${(list.approvalStatus)!}",
                 trainingClassTypeId : "${(list.trainingClassTypeId)!}",
                 approverId : "${(list.approverId)!}"    
         } );
             
            <#list partyNameView as list2>
             if(${list.partyId}=="${list2.partyId}")
             jqgrid_data1[c].approverId="${(list2.firstName)!} ${(list2.middleName)!} ${(list2.lastName)!} ${(list2.groupName)!}";
            </#list>
              c++; 
            }//end if   
  </#list>  

         jQuery("#jqgrid1").jqGrid({
                 data : jqgrid_data1,
                 datatype : "local",
                 height : 'auto',           
                 colNames : ['From Date', 'Through Date', 'Training Class Type','Approver Status','Approver Party'],
                 colModel : [{
                         name : 'fromDate',
                         index : 'fromDate',
                         width: 200,             
                         editable : false
                 },{
                         name : 'thruDate',
                         index : 'thruDate',
                         width: 200, 
                         editable : false
                 },{
                         name : 'trainingClassTypeId',
                         index : 'trainingClassTypeId',
                         width: 200,             
                         editable : false
                 },{
                         name : 'approvalStatus',
                         index : 'approvalStatus',
                         width: 200, 
                         editable : false
                 },{
                         name : 'approverId',
                         index : 'approverId',
                         editable : false
                 }],
                                           
                 rowNum : 10,
                 rowList : [10, 20, 30],
                 pager : '#pjqgrid1',
                 sortname : 'id',
                 toolbarfilter : true,
                 viewrecords : true,
                 sortorder : "asc",

                         caption : "${uiLabelMap.HumanResTrainings}",
                         multiselect : false,
                         autowidth : true
                              
         });
 
             
         jQuery("#jqgrid1").jqGrid('inlineNav', "#pjqgrid1");
         /* Add tooltips */
         $('.navtable .ui-pg-button').tooltip({
                 container : 'body'
         });

         jQuery("#m1").click(function() {
                 var s;
                 s = jQuery("#jqgrid1").jqGrid('getGridParam', 'selarrrow');
//                 alert(s);
         });
                    
         jQuery("#m1s").click(function() {
                 jQuery("#jqgrid1").jqGrid('setSelection', "13");
         });

         // remove classes
         $(".ui-jqgrid").removeClass("ui-widget ui-widget-content");
         $(".ui-jqgrid-view").children().removeClass("ui-widget-header ui-state-default");
         $(".ui-jqgrid-labels, .ui-search-toolbar").children().removeClass("ui-state-default ui-th-column ui-th-ltr");
         $(".ui-jqgrid-pager").removeClass("ui-state-default");
         $(".ui-jqgrid").removeClass("ui-widget-content");

         // add classes
         $(".ui-jqgrid-htable").addClass("table table-bordered table-hover");
         $(".ui-jqgrid-btable").addClass("table table-bordered table-striped");

         $(".ui-pg-div").removeClass().addClass("btn btn-sm btn-primary");
         $(".ui-icon.ui-icon-plus").removeClass().addClass("fa fa-plus");
         $(".ui-icon.ui-icon-pencil").removeClass().addClass("fa fa-pencil");
         $(".ui-icon.ui-icon-trash").removeClass().addClass("fa fa-trash-o");
         $(".ui-icon.ui-icon-search").removeClass().addClass("fa fa-search");
         $(".ui-icon.ui-icon-refresh").removeClass().addClass("fa fa-refresh");
         $(".ui-icon.ui-icon-disk").removeClass().addClass("fa fa-save").parent(".btn-primary").removeClass("btn-primary").addClass("btn-success");
         $(".ui-icon.ui-icon-cancel").removeClass().addClass("fa fa-times").parent(".btn-primary").removeClass("btn-primary").addClass("btn-danger");

         $(".ui-icon.ui-icon-seek-prev").wrap("<div class='btn btn-sm btn-default'></div>");
         $(".ui-icon.ui-icon-seek-prev").removeClass().addClass("fa fa-backward");

         $(".ui-icon.ui-icon-seek-first").wrap("<div class='btn btn-sm btn-default'></div>");
         $(".ui-icon.ui-icon-seek-first").removeClass().addClass("fa fa-fast-backward");

         $(".ui-icon.ui-icon-seek-next").wrap("<div class='btn btn-sm btn-default'></div>");
         $(".ui-icon.ui-icon-seek-next").removeClass().addClass("fa fa-forward");

         $(".ui-icon.ui-icon-seek-end").wrap("<div class='btn btn-sm btn-default'></div>");
         $(".ui-icon.ui-icon-seek-end").removeClass().addClass("fa fa-fast-forward");

  }) 
 $(window).on('resize.jqGrid', function() {
         $("#jqgrid1").jqGrid('setGridWidth', $("#content3").width());
 })
                <!-- Your GOOGLE ANALYTICS CODE Below -->

                var _gaq = _gaq || [];
                _gaq.push(['_setAccount', 'UA-XXXXXXXX-X']);
                _gaq.push(['_trackPageview']);

                (function() {
                        var ga = document.createElement('script');
                        ga.type = 'text/javascript';
                        ga.async = true;
                        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                        var s = document.getElementsByTagName('script')[0];
                        s.parentNode.insertBefore(ga, s);
                 })();
                     </#if>
          </script>
        
  <#if (parameters.roleType)! != 'FM'>   
        <div class="row">
 <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-5" data-widget-editbutton="false">

<header>
<span class="widget-icon">
    
 <a  href="<@ofbizUrl>NewFamilyMember?partyId=${parameters.partyId}</@ofbizUrl>" title="${uiLabelMap.CommonNew}">
<i class="fa fa-lg fa-fw fa-plus-circle" style="color:#fff"></i>
</a>
</span>

<h2>My Depandances</h2>
</header>
    
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
  </#if>

</section>




<script>
   <#include "component://humanres/template/employeeProfile/employeeProfile.js"/>
</script>