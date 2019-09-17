<div class="row">
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">
            <header>
                <h2>Employee Settings</h2>
                </header>
            <div role="content">
                <form method="post" class="smart-form" name="setEmployeeSettings" class="search_bar">
                    <table class="basic-table">
                        <tr>
                            <td width="10%"><label>Employee ID</label></td>
                            <td colspan="3" width="23%">
				<@htmlTemplate.lookupField value='${requestParameters.partyId!}' formName="setEmployeeSettings" name="partyId" id="partyId" fieldFormName="LookupEmployeeName"/>
                                <span class="required">*</span>
                                </td>       
                            </tr> 
                        <tr>             
                            <td>
                                <label style="vertical-align: sub;">Annual Vacation Days </label>
                                </td>
                            <td>			    
                                <label class="input" > 
                                    <input name="annualVacation" type="text" class="form-control" id="annualVacation"/>
                                    </label>	
                                </td>

                            </tr>
                        <tr>             
                            <td>
                                <label style="vertical-align: sub;">Ticket Eligable </label>
                                </td>
                            <td>			    
                                <label class="input"> 
                                    <select name="ticketEligable" type="text" class="form-control" id="ticketEligable">
                                        <option value="Y">Yes</option>
                                        <option value="N">No</option>
                                    </select>
                                    </label>	
                                </td>
                        <tr>  
                        </table>

                    </form> 
                <button class="btn btn-primary1"  type="button" id="submit_button" onclick="save()">Save</button>
                </div>
        </article>
    </div>
</div>

<script>     
    <#include "component://humanres/widget/forms/employeeSettings/employeeSettings.js"/>
    </script>
