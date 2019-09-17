<#ftl encoding="utf-8">
<#assign listOptionsId = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getSelectOptionID(request, response) />
<#assign listOptionsValue = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getSelectOptionValue(request, response) />
<#assign data = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getTaskData(request, response)>
<#assign instanceProcessId = session.getAttribute('processInstanceId') />
<#assign taskID = session.getAttribute('taskId') />
<#assign tasksInf = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getTasks(request, response)/>
<#assign processName = session.getAttribute('processName') />
<#assign processDetails = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getApproverActionData(request, response)>
<#assign leaveId= "${data.get('leaveId')}" />
<#assign final ='null' />
<#assign partyID= "${data.get('partyId')}" />
${session.setAttribute('partyId',"${(partyID)!''}")}
<#assign final ='null' />

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
                        <form name="activiti" method="post" class="smart-form" >
                            <table width="100%">
                                <tr>                                     
                                    <td><input class="form-control" type="text" id="taskId2" hidden="true" name="taskId2" value="${(taskID)!}"   /> </td>
                                    <td><input class="form-control" type="text" id="finalTask"  hidden="true" name="finalTask" value="${(final)!}"   /> </td>
                                    <td><input class="form-control" type="text" id="processInstanceId" hidden="true" name="processInstanceId" value="${(instanceProcessId)!}"   /> </td>
                                    <td><input class="form-control" type="text" id="processName" hidden="true" name="processName" value="${(processName)!}"   /> </td>
                                    </tr>
                                <tr id="emp2" hidden="true">
                                    <td><label> Employee ID </label></td>   
                                    <td><input class="form-control" type="text" id="empId2"  name="empId2" value="${userLogin.userLoginId}"   /> </td>
                                    </tr>
                                <tr id="empp" hidden="true">
                                    <td><label> Employee ID </label> </td>   
                                    <td><input class="form-control" type="text" id="empId1"  name="empId1" value="${userLogin.userLoginId}"  /> </td>
                                    </tr>
                                <tr>
                                    <td width="15%"><label> Party ID </label></td>
                                    <td>
                                        <input class="form-control" type="text" name="partyId" id="partyId" hidden="true" value="${(partyID)!''}" class="email" required="true"  />
                                        </td>
                                    </tr>


                                <tr>
                                    <td> <label>Start Date </label> </td>
                                    <td width="25%">
                        <@htmlTemplate.renderDateTimeField name="startDate" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="" size="" maxlength="" id="startDate" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                                        </td>
                                    <td>
                                        <label>  End Date</label>
                                        </td>

    <!--<input class="form-control" type="date" name="endDate" id="endDate" value="" class="email" required="true" /></td>-->
                                    <td ><@htmlTemplate.renderDateTimeField name="endDate" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="" size="" maxlength="" id="endDate" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                                        </td>

                                    </tr>
                                <tr>
                                    <td><label>  Number Of Days :  </label>  </td>
                                    <td>
                                        <input class="form-control" type="text" name="noOfDays" id="noOfDays"  class="email" required="true" disabled />
                                        </td>

                                    <td width="15%"> <label> Description </label> </td>
                                    <td>
                                        <input class="form-control" type="text" name="description" id="description"  value="${(description)!''}" class="email" required="true" />
                                        </td>
                                    </tr>

                                <tr>
                                    <td><label>Leave Reason Type </label></td>
                                    <td id="emplLeaveReasonTypeId"></td>



                                    <td> <label> Type </label> </td>
                                    <td width="25%">
                                        <input class="form-control" type="text" name="type" id="type"  value="" class="email" required="true" disabled />
                                        </td>
                                    </tr>
                                <tr>
                                    <td> <label> Route From</label>  </td>
                                    <td>
                                        <input class="form-control" type="text" name="routeFrom" id="routeFrom"   value="" class="email" required="true"  />
                                        </td>
                                    <td> <label> Route To</label> </td>
                                    <td width="25%">
                                        <input class="form-control" type="text" name="routeTo" id="routeTo" value="" class="email" required="true"  />
                                        </td>
                                    </tr>
                                <tr>
                                    <td><label>Ticket Required</label></td>
                                    <td class="asmContainer">
                                        <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="ticketRequiredYes" name="ticketRequired" value="Y"><span>Yes</span></label>
                                        <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="ticketRequiredNo" name="ticketRequired" value="N" ><span>No</span>&nbsp;</label>
                                        </td>
                                    </tr>
                                <tr id="TicketChargeTr">
                                    <td><label>Charge To </label></td>
                                    <td class="asmContainer">
                                        <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="TicketChargeCom" name="TicketCharge" value="COMPANY"><span>Company</span></label>
                                        <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="TicketChargeEmp" name="TicketCharge" value="EMPLOYEE"><span>Employee</span>&nbsp;</label>
                                        </td>                           
                                    </tr>
                                <tr>
                                    <td><label>Visa Required</label></td>
                                    <td class="asmContainer">
                                        <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="visaRequiredYes" name="visaRequired" value="Y"><span>Yes</span></label>
                                        <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="visaRequiredNo" name="visaRequired" value="N"><span>No</span>&nbsp;</label>
                                        </td>
                                    </tr>
                                <tr id="VisaChargeTr">
                                    <td><label>Charge To </label></td>
                                    <td class="asmContainer">
                                        <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="VisaChargeCom" name="VisaCharge" value="COMPANY"><span>Company</span></label>
                                        <label style="margin:0" class="radio radio-inline"><input class="radiobox" type="radio" id="VisaChargeEmp" name="VisaCharge" value="EMPLOYEE"><span>Employee</span>&nbsp;</label>
                                        </td>  
                                    </tr>
                                <tr><td>&nbsp;</td></tr>
                                <tr>
                                <table id="vacationExtFMGrid"></table>
                                <div id="vacationExtFMGridDiv"></div>
                                </tr>

                                <tr>

                                    <td><input class="form-control" type="text" name="currnetDate" hidden="true"  id="currnetDate"   class="email" /> </td>
                                    </tr>


                                </table>
                            </form>

                        </div>
                    </div>
                </div>
            </article>
        </div>
    <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
        <div role="content">

            </div>
        </div>

    <div class="row">
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
            <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
                <header>
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2>Remarks</h2>
                    </header>
                <div role="content">
          <form name="activiti" method="post" action="<@ofbizUrl>updateVacationAndLeave_Status</@ofbizUrl>" >
<!--action="javascript:completeTaskAndEditVacation()"-->
                                <table width="100%">
                                    <tr>                                     
                                        <td><input class="form-control" type="text" id="taskId2" hidden="true" name="taskId2" value="${(taskID)!}"   /> </td>
                                        <td><input class="form-control" type="text" id="finalTask"  hidden="true" name="finalTask" value="${(final)!}"   /> </td>
                                        <td><input class="form-control" type="text" id="processInstanceId" hidden="true" name="processInstanceId" value="${(instanceProcessId)!}"   /> </td>
                                        <td><input class="form-control" type="text" id="processName" hidden="true" name="processName" value="${(processName)!}"   /> </td>
                                        </tr>
                                    <tr id="" hidden="true">
                                        <td><label> Employee ID </label></td>   
                                        <td><input class="form-control" type="text" id="e2"  name="empId2" value="${userLogin.userLoginId}"   /> </td>
                                        </tr>
                                    <tr id="" hidden="true">
                                        <td><label> Employee ID </label> </td>   
                                        <td><input class="form-control" type="text" id="empId1"  name="empId1" value="${userLogin.userLoginId}"  /> </td>
                                        </tr>
                                    </tr>
                                    <tr>
                                        <td> <label> Remark </label> </td>
                                        <td> 
                                            <textarea name="remark"  id="remark"   class="form-control" ></textarea>
                                            </td>
                                        </tr>
                                    <tr>
     <#assign counter  = 0/>                                 
                                        <td> <label>  Action  </label> </td>
                                        <td>
                                            <select class="form-control" id="action" name="action">

            <#list listOptionsId as ids >  

                                                <option value="${(ids)!''}" >  ${(listOptionsValue[counter])!''}</option>
<#assign counter  = counter+1/>
</#list> 
                                                </select> 
                                            </td>
                                        </tr>

                                    <tr>
                                        <td><input class="form-control" type="text" name="currnetDate" hidden="true"  id="currnetDate"   class="email" /> </td>
                                        </tr>
                                    </table>
                                <input class="btn btn-primary1" type="submit"  value="Send" id="tt" class="btn"> 
                                </table>
                                </form>
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
var leaveType ="";
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

                    $("#startDate_i18n").val(data.fromDate);
                    $("#endDate_i18n").val(data.thruDate);
                    $("#noOfDays").val(data.noOfDays);
                    $("#description").val(data.description);
                    $("#reasons").val(data.emplLeaveReasonTypeId); 
                    $("#routeFrom").val(data.routeFrom); 
                    $("#routeTo").val(data.routeTo); 
                    $("#type").val(data.type); 
                    leaveType =data.emplLeaveReasonTypeId;
                    getEmplLeaveReasonTypeId();
//                    $("#visaRequired").val(data.visaRequired); 
//                    $("#ticketChargeTo").val(data.ticketChargeTo); 
//                    $("#visaChargeTo").val(data.visaChargeTo);
            
                    if(data.ticketRequired.trim()=="Y"){
                    $("#ticketRequiredYes").prop('checked', true);
                           $("#TicketChargeTr").show();
                    if(data.ticketChargeTo.trim()=="COMPANY"){
                    $("#TicketChargeCom").prop('checked', true);
                        } else if(data.ticketChargeTo.trim()=="EMPLOYEE"){
                        $("#TicketChargeEmp").prop('checked', true);
                    } }
                     else if(data.ticketRequired.trim()=="N") {
                          $("#ticketRequiredNo").prop('checked', true);
                    }
                        
                        if(data.visaRequired.trim()=="Y"){
                          
                        $("#visaRequiredYes").prop('checked', true);
                        $("#VisaChargeTr").show();
                    if(data.visaChargeTo.trim()=="COMPANY"){
                    $("#VisaChargeCom").prop('checked', true);
                        } else if(data.visaChargeTo.trim()=="EMPLOYEE"){
                        $("#VisaChargeEmp").prop('checked', true);
                            } }
                     else if(data.visaRequired.trim()=="N") {
                        $("#visaRequiredNo").prop('checked', true);
                    }
                        
                        
}
                    }); }


$('input[name="ticketRequired"]').change(function () {
    var IDtick = $(this).val();
    ticketRequred = $(this).val();
    if (IDtick == "Y") {
        $("#TicketChargeTr").fadeIn("slow", function () {
            $(this).show();
        });
    } else {
        $("#TicketChargeTr").fadeOut("slow", function () {
            $(this).hide();
        });
    }
});

$('input[name="TicketCharge"]').change(function () {
    ticketChargedTo = $(this).val();

});


$('input[name="visaRequired"]').change(function () {
    var IDtick = $(this).val();
    visaRequred = $(this).val();
    if (IDtick == "Y") {
        $("#VisaChargeTr").fadeIn("slow", function () {
            $(this).show();

        });
    } else {
        $("#VisaChargeTr").fadeOut("slow", function () {
            $(this).hide();
        });
    }
});

$('input[name="VisaCharge"]').change(function () {
    visaChargedTo = $(this).val();

});

function getEmplLeaveReasonTypeId(){
    $.ajax({
    "async": false,
    type: "post",
    url: "getEmplLeaveReasonTypeId_DropDown",
//    dataType: "json",
    success: function (data) {
        $("#emplLeaveReasonTypeId").append(data);
            
        $("#EmplLeaveReasonType").val(leaveType)
    }
});//End Of Ajax Request

}

                    $(window).load(function () {
                      var today =getCurrentDate();
                      document.getElementById("currnetDate").value=today;
                      fillEmplVacationExtFMData("${(leaveId)!}");
                      getEmplVacationDetails();
                      getPartyName();
                   

                    });


                        </script>