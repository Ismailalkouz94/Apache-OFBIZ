
<div class="row">
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
            <header>
                <h2>Create Vacation</h2>
                </header> 
            <div role="content">
            <div class="containerBlock">        
                <table class="basic-table smart-form" id="myTable">
                    <tr>
                        <td width="25%"><label>Party ID</label></td>
                        <td id="mmomes"> 
                            <form method="post" name="LookupEmployeeName" class="smart-form">
                                <@htmlTemplate.lookupField formName="LookupEmployeeName" name="partyId" id="partyId" fieldFormName="LookupEmployeeName"/>
                           </form>
                        </td>
                    </tr>
                    <tr>
                        <td><label>Date Of Application</label></td>
                        <td> <input id="Date" name="Date" type="text" readonly/>
                            </td>
                    </tr>
                    <tr>
                        <td><label>Leave Reason Type </label></td>
                        <td id="emplLeaveReasonTypeId"></td>
                    </tr>
                    <tr>
                        <td><label>First Day of Vacation</label></td>
                        <td ><@htmlTemplate.renderDateTimeField name="fromDate" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="" size="" maxlength="" id="fromDate" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                        </td>
                    </tr>
                    <tr>
                        <td><label>Last Day of Vacation</label></td>
                        <td><@htmlTemplate.renderDateTimeField name="thruDate" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="" size="" maxlength="" id="thruDate" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                            </td>
                        </tr>
                    <tr>
                        <td><label>NO. of Days</label></td>
                        <td><input id="noOfDays" name="noOfDays" type="text" readonly /></td>
                        </tr>

                    <tr>
                        <td><label>Route From</label></td>
                        <td><input id="routeFrom" name="routeFrom" type="text"/></td>
                        </tr>

                    <tr>
                        <td><label>Route To</label></td>
                        <td><input id="routeTo" name="routeTo" type="text"/></td>
                        </tr>

                    <tr>
                        <td><label>Note</label></td>
                        <td><input id="description" name="description" type="text"/></td>
                        </tr>
                    <tr>
                        <td><label>Leave Status</label></td>
                        <td><input id="leaveStatus" name="leaveStatus" type="text" value="LEAVE_APPROVED" hidden/></td>
                        </tr>
                    <tr>
                        <td><input id="type" name="type" type="text" value="VACATION" hidden/></td>
                        </tr>
                    <tr>
                        <td><label>Ticket Required</label></td>
                        <td class="asmContainer">
                            <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="ticketRequired" name="ticketRequired" value="Y"><span>Yes</span></label>
                            <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="ticketRequired" name="ticketRequired" value="N" ><span>No</span>&nbsp;</label>
                            </td>
                        </tr>
                    <tr id="TicketChargeTr">
                        <td><label>Charge To </label></td>
                        <td class="asmContainer">
                            <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="TicketCharge" name="TicketCharge" value="COMPANY"><span>Company</span></label>
                            <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="TicketCharge" name="TicketCharge" value="EMPLOYEE"><span>Employee</span>&nbsp;</label>
                            </td>                           
                        </tr>
                    <tr>
                        <td><label>Visa Required</label></td>
                        <td class="asmContainer">
                            <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="visaRequired" name="visaRequired" value="Y"><span>Yes</span></label>
                            <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="visaRequired" name="visaRequired" value="N"><span>No</span>&nbsp;</label>
                            </td>
                        </tr>
                    <tr id="VisaChargeTr">
                        <td><label>Charge To </label></td>
                        <td class="asmContainer">
                            <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="VisaCharge" name="VisaCharge" value="COMPANY"><span>Company</span></label>
                            <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="VisaCharge" name="VisaCharge" value="EMPLOYEE"><span>Employee</span>&nbsp;</label>
                            </td>  
                        </tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr class="ticketSec">    
                        <td colspan="2">
                            <table class="inside-table" width="60%"> 
                                <tr>
                                    <td><label>Party ID</label></td>
                                    <td id="FM-Select"></td>
<!--                                        <td align="center"><label>Name</label></td>
                                    <td><input class="form-control" type="text" id="FM-name" name="FM-name" disabled/></td>
                                    <td align="center"><label>D.O.B</label></td>
                                    <td><@htmlTemplate.renderDateTimeField name="DOB" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="" size="" maxlength="" id="FM-DOB" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/></td>-->
                                </tr>
                                <tr>
                                    <td><label>Departure Date</label></td>
                                    <td><@htmlTemplate.renderDateTimeField name="DepartureDate" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="" size="" maxlength="" id="FM-DepartureDate" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/></td>
                                
				    <td><label>Return Date</label></td>
                                    <td width="27%"><@htmlTemplate.renderDateTimeField name="ReturnDate" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="" size="" maxlength="" id="FM-ReturnDate" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/></td>
                                    <td><button id="FmSubmit" type="button" class="btn btn-primary">Add</button></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    <tr class="ticketSecS"><td>&nbsp;</td><td>&nbsp;</td></tr>
                    <tr  class="ticketSec">
                        <td colspan="2">
                            <article class="col-xs-8 col-sm-8 col-md-8 col-lg-8">
                                <table id="TicketMGrid"></table>
                                <div id="TicketMGridDiv"></div>
                                </article>
                            </td>                              
                        </tr>
                    <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td colspan="3"><button type="submit" onclick="submit()" class="btn btn-primary1">Submit</button></td>
                        </tr>
                    </table>        
        </div>
    </div>
</div>
</article>
</div>

<script>
<#include "component://humanres/widget/forms/leave-and-vacation/vacations/vacationForm/vacations.js"/>
    </script>
