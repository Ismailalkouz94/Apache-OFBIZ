<#ftl encoding="utf-8">
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

<#assign extInfo = parameters.extInfo?default("N")>
<style>
td{
    vertical-align: inherit;
}
</style>
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.CommonFind} ${uiLabelMap.HumanResEmployee}</h2>
</header>
<div role="content">
    
<div id="findEmployee" class="screenlet">
    <#if parameters.hideFields?default("N") != "Y">
    <div class="screenlet-body">
      <#-- NOTE: this form is setup to allow a search by partial partyId or userLoginId; to change it to go directly to
          the viewprofile page when these are entered add the follow attribute to the form element:

           onsubmit="javascript:lookupparty('<@ofbizUrl>viewprofile</@ofbizUrl>');"
       -->

        <form method="post" name="lookupparty" action="<@ofbizUrl>findEmployees</@ofbizUrl>" class="basic-form">
            <input type="hidden" name="lookupFlag" value="Y"/>
            <input type="hidden" name="hideFields" value="N"/><br/>


            <table cellspacing="0" style="width:100%">
                <tr>
                    <td>
                     <label class="col-md-12 control-label" width="100%">${uiLabelMap.PartyContactInformation}</label>
                    </td>
                    <td>
                        <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" name="extInfo" value="N" onclick="javascript:refreshInfo();" <#if extInfo == "N">checked="checked"</#if>/><span>${uiLabelMap.CommonNone}</span></label>
                        <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" name="extInfo" value="P" onclick="javascript:refreshInfo();" <#if extInfo == "P">checked="checked"</#if>/><span>${uiLabelMap.PartyPostal}</span>&nbsp;</label>
                        <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" name="extInfo" value="T" onclick="javascript:refreshInfo();" <#if extInfo == "T">checked="checked"</#if>/><span>${uiLabelMap.PartyTelecom}</span>&nbsp;</label>
                        <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" name="extInfo" value="O" onclick="javascript:refreshInfo();" <#if extInfo == "O">checked="checked"</#if>/><span>${uiLabelMap.CommonOther}</span>&nbsp;</label>
                       
                    </td>
                </tr>
                <tr><td><label class="col-md-12 control-label" width="100%">${uiLabelMap.PartyPartyId}</label>
                    </td>
                    <td id="mmomes">
                      <@htmlTemplate.lookupField value='${requestParameters.partyId!}' formName="lookupparty" name="partyId" id="partyId" fieldFormName="LookupPerson"/>
                  </td>
                </tr>
                <tr><td><label class="col-md-12 control-label" width="100%">${uiLabelMap.PartyUserLogin}</label></td>
                    <td><input class="form-control" type="text" name="userLoginId" value="${parameters.userLoginId!}"/></td>
                </tr>
                <tr><td><label class="col-md-12 control-label" width="100%">${uiLabelMap.PartyLastName}</label></td>
                    <td><input class="form-control" type="text" name="lastName" value="${parameters.lastName!}"/></td>
                </tr>
                <tr><td><label class="col-md-12 control-label" width="100%">${uiLabelMap.PartyFirstName}</label></td>
                    <td><input class="form-control" type="text" name="firstName" value="${parameters.firstName!}"/></td>
                </tr>
                <tr><td><input class="form-control" type="hidden" name="groupName" value="${parameters.groupName!}"/></td></tr>
                <tr><td><input class="form-control" type="hidden" name="roleTypeId" value="EMPLOYEE"/></td></tr>
            <#if extInfo == "P">
                <tr><td colspan="3"><hr /></td></tr><tr>
                    <td><label class="col-md-12 control-label" width="100%">${uiLabelMap.CommonAddress1}</label></td>
                    <td><input class="form-control" type="text" name="address1" value="${parameters.address1!}"/></td>
                </tr>
                <tr><td><label class="col-md-12 control-label" width="100%">${uiLabelMap.CommonAddress2}</label></td>
                    <td><input class="form-control" type="text" name="address2" value="${parameters.address2!}"/></td>
                </tr>
                <tr><td><label class="col-md-12 control-label" width="100%">${uiLabelMap.CommonCity}</label></td>
                    <td><input class="form-control" type="text" name="city" value="${parameters.city!}"/></td>
                </tr>
                <tr><td><label class="col-md-12 control-label" width="100%">${uiLabelMap.CommonStateProvince}</label></td>
                    <td><select class="form-control" name="stateProvinceGeoId">
                        <#if currentStateGeo?has_content>
                            <option value="${currentStateGeo.geoId}">${currentStateGeo.geoName?default(currentStateGeo.geoId)}</option>
                            <option value="${currentStateGeo.geoId}">---</option>
                        </#if>
                            <option value="ANY">${uiLabelMap.CommonAnyStateProvince}</option>
                            ${screens.render("component://common/widget/CommonScreens.xml#states")}
                        </select>
                    </td>
                </tr>
                <tr><td><label class="col-md-12 control-label" width="100%">${uiLabelMap.PartyPostalCode}</label></td>
                    <td><input class="form-control" type="text" name="postalCode" value="${parameters.postalCode!}"/></td>
                </tr>
            </#if>
            <#if extInfo == "T">
                <tr><td colspan="3"><hr /></td></tr>
                <tr><td><label class="col-md-12 control-label" width="100%">${uiLabelMap.CommonCountryCode}</label></td>
                    <td><input class="form-control" type="text" name="countryCode" value="${parameters.countryCode!}"/></td>
                </tr>
                <tr><td><label class="col-md-12 control-label" width="100%">${uiLabelMap.PartyAreaCode}</label></td>
                    <td><input class="form-control" type="text" name="areaCode" value="${parameters.areaCode!}"/></td>
                </tr>
                <tr><td><label class="col-md-12 control-label" width="100%">${uiLabelMap.PartyContactNumber}</label></td>
                    <td><input class="form-control" type="text" name="contactNumber" value="${parameters.contactNumber!}"/></td>
                </tr>
            </#if>
            <#if extInfo == "O">
                <tr><td colspan="3"><hr /></td></tr>
                <tr><td><label class="col-md-12 control-label" width="100%">${uiLabelMap.PartyContactInformation}</label></td>
                    <td><input class="form-control" type="text" name="infoString" value="${parameters.infoString!}"/></td>
                </tr>
            </#if>
                <tr><td colspan="3"></td></tr>
                <tr align="center">
                    <td>&nbsp;</td>
                    <td>                                                                                                <#-- ${uiLabelMap.CommonShowAllRecords} ${lookupparty.partyId} -->
                        <!--<a class="btn btn-primary1" href="<@ofbizUrl>findEmployees?roleTypeId=EMPLOYEE&amp;lookupFlag=Y&amp;partyId=</@ofbizUrl>" class="smallSubmit">Search</a>-->
                        <a class="btn btn-primary1" href="javascript:document.lookupparty.submit();" class="smallSubmit">Search</a>
                   </td>                               <!-- &amp;hideFields=Y&amp;lookupFlag=Y-->
                </tr>
            </table>
        </form>
    </div>
    </#if>
</div>
</div>
</div>
</article>
    <#if parameters.hideFields?default("N") != "Y">
        <script language="JavaScript" type="text/javascript">
    <!--//
      document.lookupparty.partyId.focus();
    //-->
        </script>
    </#if>
    <#if partyList??>

<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.PartyPartiesFound}
</h2>
</header>
<div role="content">
    <div id="findEmployeeResults" class="screenlet">

<div class="nav-pager">
    <ul class="pagination dark-theme simple-pagination">
         <#if (partyListSize > 0)>       
               
                    <#if (viewIndex > 0)>
                        <li><a class="nav-previous" href="<@ofbizUrl>findEmployees?VIEW_SIZE=${viewSize}&amp;VIEW_INDEX=${viewIndex-1}&amp;hideFields=${parameters.hideFields?default("N")}${paramList}</@ofbizUrl>">${uiLabelMap.CommonPrevious}</a></li>
                    <#else>
                        <li class="disabled"><a>${uiLabelMap.CommonPrevious}</a></li>
                    </#if>
                    <li><a>${lowIndex} - ${highIndex} ${uiLabelMap.CommonOf} ${partyListSize}</a></li>
                    <#if (partyListSize > highIndex)>
                        <li><a class="nav-next" href="<@ofbizUrl>findEmployees?VIEW_SIZE=${viewSize}&amp;VIEW_INDEX=${viewIndex+1}&amp;hideFields=${parameters.hideFields?default("N")}${paramList}</@ofbizUrl>">${uiLabelMap.CommonNext}</a></li>
                    <#else>
                        <li class="disabled"><a>${uiLabelMap.CommonNext}</a></li>
                    </#if>
        </#if>                
    </ul>
    <br class="clear"/>
</div>

    <#if partyList?has_content>

<div class="table-responsive">
        <table class="table table-bordered table-striped">
          <thead>
            <tr class="header-row">
                <th>${uiLabelMap.PartyPartyId}</th>
                <th>${uiLabelMap.PartyUserLogin}</th>
                <th>${uiLabelMap.PartyName}</th>
                <#if extInfo?default("") == "P" >
                    <th>${uiLabelMap.PartyCity}</th>
                </#if>
                <#if extInfo?default("") == "P">
                    <th>${uiLabelMap.PartyPostalCode}</th>
                </#if>
                <#if extInfo?default("") == "T">
                    <th>${uiLabelMap.PartyAreaCode}</th>
                </#if>
                <th>Employee Status</th>
                <th>&nbsp;</th>
            </tr>
           </thead>
            <#assign alt_row = false>
            <#list partyList as partyRow>
            <#assign partyType = partyRow.getRelatedOne("PartyType", false)!>
          
            <#assign statusItem = (delegator.findOne("StatusItem", {"statusId" : partyRow.EmplStatus}, true))!>
            <tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
                <td><a href="<@ofbizUrl>EmployeeProfile?partyId=${partyRow.partyId}</@ofbizUrl>">${partyRow.partyId}</a></td>
                <td><#if partyRow.containsKey("userLoginId")>
                        ${partyRow.userLoginId?default("N/A")}
                    <#else>
                    <#assign userLogins = partyRow.getRelated("UserLogin", null, null, false)>
                    <#if (userLogins.size() > 0)>
                        <#if (userLogins.size() > 1)>
                            (${uiLabelMap.CommonMany})
                        <#else>
                        <#assign userLogin = userLogins.get(0)>
                            ${userLogin.userLoginId}
                        </#if>
                        <#else>
                            (${uiLabelMap.CommonNone})
                        </#if>
                    </#if>
                </td>
                <td><#if partyRow.getModelEntity().isField("lastName") && lastName?has_content>
                        ${partyRow.lastName}<#if partyRow.firstName?has_content>, ${partyRow.firstName}</#if>
                    <#elseif partyRow.getModelEntity().isField("groupName") && partyRow.groupName?has_content>
                        ${partyRow.groupName}
                    <#else>
                    <#assign partyName = Static["org.apache.ofbiz.party.party.PartyHelper"].getPartyName(partyRow, true)>
                    <#if partyName?has_content>
                        ${partyName}
                    <#else>
                        (${uiLabelMap.PartyNoNameFound})
                    </#if>
                    </#if>
                </td>
                <#if extInfo?default("") == "T">
                    <td>${partyRow.areaCode!}</td>
                </#if>
                <#if extInfo?default("") == "P" >
                    <td>${partyRow.city!}, ${partyRow.stateProvinceGeoId!}</td>
                </#if>
                <#if extInfo?default("") == "P">
                    <td>${partyRow.postalCode!}</td>
                </#if>
                <td>${(statusItem.description)!} </td>
                <td class="button-col align-float">
                    <a href="<@ofbizUrl>EmployeeProfile?partyId=${partyRow.partyId}</@ofbizUrl>">${uiLabelMap.CommonDetails}</a>
                </td>
            </tr>
          <#-- toggle the row color -->
            <#assign alt_row = !alt_row>
            </#list>
        </table>
</div>

    <#else>
        <div class="screenlet-body">
            <div class="alert alert-info fade in">${uiLabelMap.PartyNoPartiesFound}</div>
        </div>
    </#if>
    <#if lookupErrorMessage??>
        <div class="alert alert-info fade in">${lookupErrorMessage}</div>
    </#if>
    </div>
    </#if>
<!-- end findEmployees.ftl -->
</div>
</div>
</article>
</div>
   