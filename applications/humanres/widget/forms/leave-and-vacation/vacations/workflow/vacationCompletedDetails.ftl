<#ftl encoding="utf-8">
<#assign data = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getTaskData(request, response)>
<#assign instanceProcessId = session.getAttribute('processInstanceId') />
<#assign taskID = session.getAttribute('taskId') />
<#assign processName = session.getAttribute('processName') />
<#assign processDetails = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getApproverActionData(request, response)>
<#assign leaveId= "${data.get('leaveId')}" />

<#assign partyID= "${data.get('partyId')}" />
${session.setAttribute('partyId',"${(partyID)!''}")}



<section id="widget-grid">
    <div class="row">
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
            <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
                <header>
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2 id="partyNameLabel">  </h2>
                    </header>
                <div role="content">
                    <div  class="table-responsive">
                        <form name="" method="" action="">
                            <table width="100%">
                         
                                <tr>
                                    <td width="15%"><label> Party ID </label></td>
                                    <td>
                                        <input class="form-control" type="text" name="partyId" id="partyId"  value="${(partyID)!''}" class="email" required="true" disabled />
                                        </td>
                                    <td>
                                        <label id="partyName">
                                            </label>
                                        </td>

                                    </tr>

                                <tr>
                                    <td><label>  Number Of Days :  </label>  </td>
                                    <td>
                                        <input class="form-control" type="text" name="noOfDays" id="noOfDays"  class="email" required="true" disabled/>
                                        </td>

                                    <td width="15%"> <label> description </label> </td>
                                    <td>
                                        <input class="form-control" type="text" name="description" id="description"  value="${(description)!''}" class="email" required="true" disabled/>
                                        </td>
                                    </tr>

                                <tr>
                                    <td> <label>Start Date </label> </td>
                                    <td width="25%">
                                        <input class="form-control" type="text" name="startDate" id="startDate" value="" class="email" required="true" disabled/></td>
                                    </td>
                                    <td>
                                        <label>  End Date</label>
                                        </td>
                                    <td>
                                        <input class="form-control" type="text" name="endDate" id="endDate" value="" class="email" required="true" disabled/></td>

                                    </td>
                                    </tr>
                                <tr>
                                    <td> <label> Vacation Reasons </label>  </td>
                                    <td>
                                        <input class="form-control" type="text" name="reasons" id="reasons"   value="" class="email" required="true" disabled />
                                        </td>



                                    <td> <label> Type </label> </td>
                                    <td width="25%">
                                        <input class="form-control" type="text" name="type" id="type"  value="" class="email" required="true"  disabled/>
                                        </td>
                                    </tr>
                                <tr>
                                    <td> <label> Route From</label>  </td>
                                    <td>
                                        <input class="form-control" type="text" name="routeFrom" id="routeFrom"   value="" class="email" required="true" disabled />
                                        </td>
                                    <td> <label> Route To</label> </td>
                                    <td width="25%">
                                        <input class="form-control" type="text" name="routeTo" id="routeTo" value="" class="email" required="true"  disabled/>
                                        </td>
                                    </tr>
                                <tr>
                                    <td> <label> Ticket Required</label>  </td>
                                    <td>
                                        <input class="form-control" type="text" name="ticketRequired" id="ticketRequired"   value="" class="email" required="true" checked disabled />
                                        </td>
                                    <td> <label> Ticket Charge To</label>  </td>
                                    <td>
                                        <input class="form-control" type="text" name="ticketChargeTo" id="ticketChargeTo"   value="" class="email" required="true" disabled />
                                        </td>

                                    </tr>
                                <tr>
                                    <td> <label>Visa Required</label> </td>
                                    <td width="25%">
                                        <input class="form-control" type="text" name="visaRequired" id="visaRequired" value="" class="email" required="true"  disabled/>
                                        </td>
                                    <td> <label> Visa Charge To</label> </td>
                                    <td width="25%">
                                        <input class="form-control" type="text" name="visaChargeTo" id="visaChargeTo" value="" class="email" required="true"  disabled/>
                                        </td>

                                    </tr>
                                <tr><td>&nbsp;</td></tr>
                                </table>
                            </form>

                        </div>
                    <table id="vacationExtFMGrid"></table>
                    <div id="vacationExtFMGridDiv"></div>

                    </div>


                </div>

            <div class="row">
                <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable"">
                    <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
                        <header>
                            <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                            <h2>For Approver</h2>
                            </header>
                        <div role="content">

       
                            <div id="grid" style="width:100%" align="center">
                                <table cellpaddin="0" cellspacing="0" id="tab101" class="table table-bordered table-striped"> 
                                    <thead>
                                        <tr>

                                            <th><label>Employee ID</label> </th>
                                            <th> <label>Action Date </label></th>
                                            <th><label> Action </label></th>
                                            <th><label> Remark</label></th>
                                            </tr>
                                        </thead>
<#assign criteria = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getAuditRemarks(request, response)/> 


<#list  criteria as data>

                                    <tr>
                                        <td> ${(data.empId)!}</td>
                                        <td> ${(data.actionDate)!}</td>
                                        <td>
<#if '${(data.action)!}'=='0'>
                                            Start
<#elseIf '${(data.action)!}'=='1'>
                                            Approved
<#elseIf '${(data.action)!}'=='2'>
                                            Rejected
<#elseIf '${(data.action)!}'=='3'>
                                            Return To Employee
</#if>
                                            </td>
                                        <td> ${(data.remark)!}</td>
                                        </tr>
</#list> 
                                    </table>
                                </div>



<script>

 function getPartyName(){
 $.ajax({
type: "post",
url: "getPartyName",
data: "partyId=" + '${partyID}',
success: function (data) {
//   document.getElementById('partyNameLabel').concat(" "+data.trim());
   document.getElementById('partyNameLabel').innerHTML="Vacation request for " +data.trim();
//        document.getElementById("partyName").value = data.trim(); 
}
});
    }
function getEmplVacationExtFMData_Ajax(leaveId, jqgrid_data) {
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeeVacationExtFMData",
        data: {leaveId: leaveId},
        dataType: "json",
        success: function (data) {
            for (item in data) {
                jqgrid_data.push({
                    EmplLeaveExtFMTicketId: data[item].EmplLeaveExtFMTicketId,
                    partyId: data[item].partyId,
                    DOB: data[item].DOB,
                    departureDate: data[item].departureDate,
                    returnDate: data[item].returnDate,

                });

            }
        }
    }); // End of Ajax Request
}


function fillEmplVacationExtFMData(leaveId) {
    var jqgrid_data = [];
    getEmplVacationExtFMData_Ajax(leaveId, jqgrid_data);

    colNames = ['ID', 'Party Id', 'D.O.B', 'Departure Date', 'Return Date'];
    colModel = [{
            key: true,
            name: 'EmplLeaveExtFMTicketId',
            index: 'EmplLeaveExtFMTicketId',
            editable: false,
            hidden: true

        }, {
            name: 'partyId',
            index: 'partyId',
//            edittype: 'select',
//            editrules: {required: true},
//            editoptions: {dataUrl: 'getRelatedToIdDropDown'},
            hidden: false,
            editable: true,

        }, {
            name: 'DOB',
            index: 'DOB',
            editrules: {required: true, datetime: true},
            editoptions: {size: 10, dataInit: dateTime()},
            editable: true
        }, {
            name: 'departureDate',
            index: 'departureDate',
            editrules: {required: true, datetime: true},
            editoptions: {size: 10, dataInit: dateTime()},
            editable: true
        }, {
            name: 'returnDate',
            index: 'returnDate',
            editrules: {required: true, datetime: true},
            editoptions: {size: 10, dataInit: dateTime()},
            editable: true
        }];
    getGridData(colNames, colModel, jqgrid_data, "vacationExtFMGrid", "vacationExtFMGridDiv", "Ticket And Family Memeber Ticket", "refId", "asc", 15);
} //End of function fillInsuranceData 


function getEmplVacationDetails(){
     $.ajax({
type: "post",
url: "getEmplVacationDetails",
data: "leaveId=" +"${leaveId}",
dataType: "json",
success: function (data) {

$("#startDate").val(data.fromDate);
$("#endDate").val(data.thruDate);
$("#noOfDays").val(data.noOfDays);
$("#description").val(data.description);
$("#reasons").val(data.emplLeaveReasonTypeId); 
$("#routeFrom").val(data.routeFrom); 
$("#routeTo").val(data.routeTo); 
$("#type").val(data.type); 
$("#visaRequired").val(data.visaRequired); 
$("#ticketChargeTo").val(data.ticketChargeTo); 
$("#visaChargeTo").val(data.visaChargeTo);
$("#ticketRequired").val(data.ticketRequired); 


}

}); }


$(window).load(function () {
  fillEmplVacationExtFMData("${(leaveId)!}");
  getEmplVacationDetails();
  getPartyName();

});


    </script>

 