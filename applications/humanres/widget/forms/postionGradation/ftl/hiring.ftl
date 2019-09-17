<#ftl encoding="utf-8">
<!--Calculation Form-->
<div class="row">
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">
            <header>
                <h2>Hiring Employee</h2>
            </header>
            <div role="content" id="AddEmplPositionFulfillmentPanel">
                <form class="smart-form" id="customAddEmplPositionFulfillment" name="customCreateEmplPositionFulfillment" method="post" action="javascript:InsertTocustomCreateEmplPositionFulfillment()">
                    <!--<input name="emplPositionFulfillmentId"><hidden/></input>-->
                    <input type="hidden" name="thruDate" id="customAddEmplPositionFulfillment_thruDate" />
                    <input type="hidden" name="comments" id="customAddEmplPositionFulfillment_comments" />
                    <input type="hidden" name="emplPositionFulfillmentId" id="customAddEmplPositionFulfillment_emplPositionFulfillmentId"/>
                    <table id="Calc_table" width="100%">
                        <tr class="group-label">
                            <td><label class="secLabel">${uiLabelMap.Hiring}</label></td>
                        </tr>
                        <tr>
                            <td><label>${uiLabelMap.PartyPartyId}</label></td>
                            <td>
                                <@htmlTemplate.lookupField value='${requestParameters.partyId!}' formName="customCreateEmplPositionFulfillment" name="partyId" className="required" id="customAddEmplPositionFulfillment_partyId"  fieldFormName="LookupEmployeeNameUnderHiring"/>  
                                <span>*</span>
                            </td>
                            <td><label>Employee Status </label></td>
                            <td>
                                <input type="text" name="EmplStatus" id="customAddEmplPositionFulfillment_EmplStatus" required="true" disabled/>
                            </td>
                        </tr>
                        <tr>
                            <td><label>${uiLabelMap.CommonFromDate}</label></td>
                            <td> 
                                <fieldset>
                                    <label class="input"> <i class="icon-append fa fa-calendar"></i>
                                        <input style="height: 28px !important" class="required form-control" title="Format: yyyy-MM-dd" type="text"  name="fromDate_i18n" id="customAddEmplPositionFulfillment_fromDate_i18n" required="true">
                                        </label>	
                                    </fieldset><span> *</span>
                                </td>
                            <td><label>Grade</label></td>
                            <td> 
                                <input type="text" name="gradeId" class="form-control" id="customAddEmplPositionFulfillment_gradeId" required="true" disabled/>
                            </td>
                        </tr>
                        <tr>
                            <td><label>Empl Position Id</label></td>
                            <td>
                                <@htmlTemplate.lookupField value="${requestParameters.emplPositionId!}" formName="customCreateEmplPositionFulfillment" name="emplPositionId" id="customAddEmplPositionFulfillment_emplPositionId"  fieldFormName="LookupEmplPositionActive"/>
                                 <!--   event="onkeyup" action="javascript:getGrade(this.value);" -->
                                <span>*</span></td>
                            <td><label>${uiLabelMap.NewDegree}</label></td>
                            <td id="tdDegreeSelect" style="display:none">
                                <select name="degreeId" id="degreeSelect" required="required" class="form-control" onchange="fillBasicSalary(this.value);"> <option></option>
                            </td>
                            <td>
                                <input type="text" name="degreeDesc" class="form-control" id="degreeDesc" disabled/>
                            </td>
                       </tr>
                        <tr>
                            <td><label>Basic Salary</label></td>
                            <td><input type="text" name="basicSalary" id="basicSalary" disabled></td>
                        </tr>
                </table>
                <input type="hidden" name="roleTypeIdFrom" id="roleTypeIdFrom" value="INTERNAL_ORGANIZATIO" />
                <input type="hidden" name="roleTypeIdTo" id="roleTypeIdTo" value="EMPLOYEE"/>
                <table id="Employment" width="100%">
                        <tr><td>&nbsp;</td></tr>
                        <tr>
                           <td> <label class="secLabel">${uiLabelMap.Employment}</label></td>
                        </tr>
                         <tr>
                            <td  width="14.87%"><label>${uiLabelMap.HumanResEmploymentPartyIdFrom}</label></td>
                            <td>
                                <@htmlTemplate.lookupField value='${requestParameters.partyId!}' formName="customCreateEmplPositionFulfillment" name="partyIdEmployment" id="partyIdFrom"  fieldFormName="LookupInternalOrganization"/>  <span>*</span>
                            </td>
                        </tr>
                        <tr>
                            <td><label>Note</label></td>
                            <td>
                                <textarea class="form-control" id="note"></textarea>
                            </td>
                        </tr>
                </table>

                    <input  style="display:none" name="submitButton" id="hiringBtn" class="btn btn-primary1" id="addEmplPositionFulfillment_Btn" type="submit" value="Add" 
                           onClick="afterSave()"/>
                    </form> 
                </div>
            </div>
        </article>
    </div>
<#include "component://humanres/widget/forms/postionGradation/ftl/MainGrid.ftl"/>   
<script>
    <#include "component://humanres/widget/forms/postionGradation/js/hiring.js"/> 
</script>