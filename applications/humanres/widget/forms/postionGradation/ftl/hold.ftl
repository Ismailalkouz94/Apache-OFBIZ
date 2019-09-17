<#ftl encoding="utf-8">

<!--Calculation Form-->
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">
<header>
 <h2> Holding Employee</h2>
</header>
<div role="content">
<form class="smart-form" id="myform" name="LookupEmployeeHiredAndHold" method="post" action="javascript:InsertToEmployeeSalaryGrade()">
 <table id="Calc_table" width="75%">
    <tr>
            <td>  ${uiLabelMap.EmployeeId}</td>
            <td> <@htmlTemplate.lookupField value='${requestParameters.partyId!}' formName="LookupEmployeeHiredAndHold" name="emplId" id="emplId" fieldFormName="LookupEmployeeHiredAndHold" event="onChange" />           
            </td>
    </tr>
  
    <tr>
            <td align="lift" >${uiLabelMap.startDate}</td>
            <td align="lift">
		<fieldset>
		    <label class="input"> <i class="icon-append fa fa-calendar"></i>
		       <input style="height: 28px !important" class="required form-control" title="Format: yyyy-MM-dd" type="text" name="holdDate" id="holdDate" required="true">
		    </label>	
	       </fieldset>
            </td>
    </tr>
     <tr>
            <td align="lift" >${uiLabelMap.endDate}</td>
            <td align="lift">
		<fieldset>
		    <label class="input"> <i class="icon-append fa fa-calendar"></i>
		       <input style="height: 28px !important"  title="Format: yyyy-MM-dd" type="text" name="endDate" id="endDate" >
		    </label>	
	       </fieldset>
            </td>
    </tr>
    <tr>
        <td><label>Note</label></td>
        <td><textarea id="note" class="form-control"  required="required" class="required form-control"></textarea></td>
    </tr>
</table> 

        <input class="btn btn-primary1" type="submit" id="holdBtn" value="Save" />

</form> 
</div>
</div>
</article>
</div>

<#include "component://humanres/widget/forms/postionGradation/ftl/MainGrid.ftl"/>

<script>
        <#include "component://humanres/widget/forms/postionGradation/js/hold.js"/>
</script>
