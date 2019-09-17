<#ftl encoding="utf-8">
<!--Calculation Form-->
<div class="row">
  <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
    <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">
	<header>
	 <h2> Moving Employee</h2>
	</header>
	<div role="content">
	    <form class="smart-form" id="myform" name="LookupEmployeeNameHired" method="post" action="javascript:InsertToEmployeeSalaryGrade()">
		<table id="Calc_table" width="75%">
		   <tr>
			<td><label>${uiLabelMap.EmployeeId}</label></td>
			<td> <@htmlTemplate.lookupField value='${requestParameters.partyId!}' formName="LookupEmployeeNameHired" name="emplId" id="emplId" fieldFormName="LookupEmployeeNameHired" event="onChange" />           
			</td>
		   </tr>
		   <tr>
			<td required="true"><label>${uiLabelMap.currentDepartment}</label></td>
			<td><input class="form-control" type="text" name="currentDepartment" id="currentDepartment" required="true" disabled><span id="descSpan" class='tooltip1'></span>
		   </tr>
		   <tr>
			<td required="true"><label>${uiLabelMap.newDepartment}</label></td>
			<td><select required="true" id="newDepartment"> <option></option> </td>
		   </tr>
		   <tr>
			<td><label>${uiLabelMap.effectDate}</label></td>
			<td>
			    <fieldset>
				<label class="input"> <i class="icon-append fa fa-calendar"></i>
				   <input style="height: 28px !important" class="required form-control" title="Format: yyyy-MM-dd" type="text" name="moveDate" id="moveDate" required="true">
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
	       <input class="btn btn-primary1" type="submit" id="moveBtn" value="Move" >
	    </form> 
	</div>
    </div>
  </article>
</div>
<#include "component://humanres/widget/forms/postionGradation/ftl/MainGrid.ftl"/>
<script>
     <#include "component://humanres/widget/forms/postionGradation/js/move.js"/>
</script>
