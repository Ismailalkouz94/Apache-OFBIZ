<#ftl encoding="utf-8">
 <#assign termintanType = delegator.findAll("TerminationType",true) />
 <!--Calculation Form-->
<div class="row">
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">
            <header>
                <h2> Termianted Employee</h2>
                </header>
            <div role="content">
                <form class="smart-form" id="myform" name="LookupEmployeeHiredAndHold" method="post" action="javascript:deleteEmpl()">
                    <table id="Calc_table" width="75%">
                        <tr>
                            <td><label>${uiLabelMap.EmployeeId}</label></td>
                            <td> <@htmlTemplate.lookupField value='${requestParameters.partyId!}' formName="LookupEmployeeHiredAndHold" name="emplId" id="emplId" fieldFormName="LookupEmployeeHiredAndHold" event="onChange" />           
                            </td>
                        </tr>
                        <tr>
                            <td required="true"><label>${uiLabelMap.terminationType}</label></td>
                            <td>
				<select required="true" id="terminationType"> 
				    <#list termintanType as list>
				      <option value ="${(list.terminationTypeId)!''}" id=${(list.terminationTypeId)!''}>${(list.description)!''}</option> 
				    </#list>
				</select> 
                             </td>
                        </tr>
                        <tr>
                            <td required="true"><label>${uiLabelMap.TerminationReason}</label></td>
                            <td><select required="true" id="terminationReason"> 
                                    <option value ="" id=""></option>                               
                                 </select>
                            </td>
                        </tr>
                        <tr>
                            <td><label>${uiLabelMap.effectDate}</label></td>
                            <td>
                                <fieldset>
                                    <label class="input"> <i class="icon-append fa fa-calendar"></i>
                                        <input style="height: 28px !important" class="required form-control" title="Format: yyyy-MM-dd" type="text" name="terminateDate" id="terminateDate" required="true">
                                    </label>	
                                </fieldset>
                            </td>
                        </tr>
                    <tr>
                        <td><label>Note</label></td>
                        <td>
                            <textarea class="form-control" id="note"></textarea>
                        </td>
                    </tr>
                    </table> 
                    <input class="btn btn-primary1" id="btnTerminate" type="submit" value="Terminate" >
                    </form> 
                </div>
            </div>
        </article>
    </div>

<div class="row">                                            <!-- NEW WIDGET START -->
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div  class="jarviswidget jarviswidget-color-darken jarviswidget-sortable"  id="wid-id-1" data-widget-editbutton="false" role="widget">			 
            <div role="content">
                <table id="employmetGrid"></table>
                <div id="employmetGridDiv"></div>
                </div>
            </div>
        </article>
        <!-- WIDGET END -->

    </div>

<script>
 <#include "component://humanres/widget/forms/postionGradation/js/terminate.js"/>
    </script>
