<#ftl encoding="utf-8">
<!--Calculation Form-->
<div class="row">
  <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
    <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
	<header>
	 <h2>Find Promotion Employee</h2>
	</header>
	<div role="content">
	    <form class="smart-form" id="myform" name="LookupEmployeeNameHired" method="post" action="javascript:INSERT()">
		<table id="Calc_table" width="75%">
		   <tr>
			<td><label>${uiLabelMap.EmployeeId}</label></td>
			<td> <@htmlTemplate.lookupField value='${requestParameters.partyId!}' formName="LookupEmployeeNameHired" name="partyId" id="partyId" fieldFormName="LookupEmployeeNameHired" event="onChange" />           
			</td>
		   </tr>
		   <tr>
			<td><label>${uiLabelMap.Currentgrade}</label></td>
			<td><input  type="text"  name="gradeId" id="gradeId" value="" disabled> </td>
			<td required="true"><label>${uiLabelMap.currentDegree}</label></td>
			<td><input class="form-control" type="text" name="currentDegree" id="currentDegree" required="true"  disabled>
		   </tr>
		   <tr>
			<td><label>${uiLabelMap.currentPostion}</label></td>
			<td> <input class="form-control"  type="text" name="postion" id="postion"  placeholder="" disabled > <span class="tooltip1" id="EmplPostion"></span></td>
			<td required="true"><label>${uiLabelMap.newPostion}</label></td>
			<td> <select required="true" id="newPostion"> <option></option> </td>
		   </tr>
		   <tr>
			<td required="true"><label>${uiLabelMap.Employment}</label></td>
			<td><input class="form-control"  type="text" name="employment" id="employment"  placeholder="" disabled ></td>
			<td><label>${uiLabelMap.PromotionDate}</label></td>
			<td>
			    <fieldset>
				<label class="input"> <i class="icon-append fa fa-calendar"></i>
				   <input style="height: 28px !important" class="required form-control" title="Format: yyyy-MM-dd" type="text" name="bounseDate" id="bounseDate" required="true">
				</label>	
			   </fieldset>
			</td>
		   </tr>
		   <tr>
			<td required="true"><label>${uiLabelMap.NewGrade}</label></td>
			<td><select required="true" id="gradeSelect"> <option></option> </td>
			<td required="true"><label>${uiLabelMap.NewDegree}</label></td>
			<td><select required="true" id="degreeSelect"> <option></option> </td>
		   </tr>
		   <tr>
			<td required="true"><label>${uiLabelMap.currentBasicSalary}</label></td>
			<td><input class="form-control" type="text" name="basicSalary" id="basicSalary"  disabled></td>
			<td required="true"><label>${uiLabelMap.newSalary}</label></td>
			<td><input class="form-control" type="text" name="newSalary" id="newSalary"  disabled></td>
		   </tr>
                    <tr>
                        <td><label>Note</label></td>
                        <td>
                            <textarea class="form-control" id="note"></textarea>
                        </td>
                    </tr>
		</table> 
		<input class="btn btn-primary1" type="submit" id="promotionBtn" value="Save" >
	    </form> 
	</div>
    </div>
  </article>
</div>
<#include "component://humanres/widget/forms/postionGradation/ftl/MainGrid.ftl"/>
<script>
    <#include "component://humanres/widget/forms/postionGradation/js/promotion.js"/>
</script>