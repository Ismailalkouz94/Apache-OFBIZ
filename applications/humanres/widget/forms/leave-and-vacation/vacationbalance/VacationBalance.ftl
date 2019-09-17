<div class="row">
 <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
            <div role="content">
		<form class="smart-form" name="LookupEmployeeName">
		      <table class="basic-table" style="margin-bottom: 2px;">
		       <tr>
			   <td width="15%"><label>Employee ID</label></td>
			   <td width="25%">
			      <@htmlTemplate.lookupField value='${requestParameters.partyId!}' formName="LookupEmployeeName" name="partyId" id="partyId" fieldFormName="LookupEmployeeName"/>
			   </td>
			   <td width="10%">
			       <label style="vertical-align: sub;">Hired Date </label>
			   </td>
			   <td>
			       <label class="input">
				   <input name="hiredDate" type="text" class="form-control" id="hiredDate" readonly/>
			       </label>
			   </td>
			</tr>
		        <tr>
			   <td>
			       <label style="vertical-align: sub;">To Date </label>
			   </td>
			   <td>
			       <@htmlTemplate.renderDateTimeField name="toDate" event="" action="" className="" alert="" title="Format: yyyy-MM-dd" value="" size="25" maxlength="30" id="toDate" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
			     </td>
                         </tr>
			<tr>
			   <td colspan="4"><button class="btn btn-primary1" type="button" id="submit_button" onclick="fillVacationBalanceData()">Calculate</button></td>
		       </tr>
		       </table>
		   </form>
                <table id="vacationBalance"></table>
                <div id="vacationBalanceDiv"></div>
            </div>
        </div>
    </article>
</div>

<script>
    <#include "component://humanres/widget/forms/leave-and-vacation/vacationbalance/VacationBalance.js"/>
</script>
