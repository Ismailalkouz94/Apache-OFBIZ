<#ftl encoding="utf-8">
<#assign listOptionsId = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getSelectOptionID(request, response) />
<#assign listOptionsValue = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getSelectOptionValue(request, response) />
<#assign data = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getTaskData(request, response)>
<#assign instanceProcessId = session.getAttribute('processInstanceId') />
<#assign taskID = session.getAttribute('taskId') />
<#assign processName = session.getAttribute('processName') />
<#assign processDetails = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getApproverActionData(request, response)>
<#assign leaveId= "${data.get('leaveId')}" />
<#assign final ='null' />
<#assign partyID= "${data.get('partyId')}" />
${session.setAttribute('partyId',"${(partyID)!''}")}
<#assign final ='null' />

<#if '${session.getAttribute("taskName")}' == "hrApproval">
<#assign final ='1' />
</#if> 

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
                                        <input class="form-control" type="text" name="partyId" id="partyId"  value="${(partyID)!''}" class="email" required="true" disabled />
                                        </td>
                                    <td> <label> Type </label> </td>
                                    <td width="25%">
                                        <input class="form-control" type="text" name="type" id="type"  value="" class="email" required="true"  disabled/>
                                        </td>
                                   

                                    </tr>
  <tr>
                                    <td> <label> Vacation Reasons </label>  </td>
                                    <td>
                                        <input class="form-control" type="text" name="reasons" id="reasons"   value="" class="email" required="true" disabled />
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

        <form name="activiti" method="post" action="<@ofbizUrl>updateVacationAndLeave_Status</@ofbizUrl>">

                                <table width="100%">
                                    <tr>                                     
                                        <td><input class="form-control" type="text" id="taskId2" hidden="true" name="taskId2" value="${(taskID)!}"   /> </td>
                                        <td><input class="form-control" type="text" id="finalTask"  hidden="true" name="finalTask" value="${(final)!}"   /> </td>
                                        <td><input class="form-control" type="text" id="processInstanceId" hidden="true" name="processInstanceId" value="${(instanceProcessId)!}"   /> </td>
                                        <td><input class="form-control" type="text" id="processName" hidden="true" name="processName" value="${(processName)!}"   /> </td>
                                        <td><input class="form-control" type="text" id="leaveId" hidden="true" name="leaveId" value="${(data.get('leaveId'))!}"   /> </td>
                                     
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



function getEmplVacationDetails(){
     $.ajax({
type: "post",
url: "getEmplLeaveDetails",
data: "leaveId=" +"${leaveId}",
dataType: "json",
success: function (data) {

$("#startDate").val(data.fromDate);
$("#endDate").val(data.thruDate);
$("#description").val(data.description);
$("#reasons").val(data.emplLeaveReasonTypeId); 
$("#type").val(data.type); 



}

}); }


$(window).load(function () {
      var today =getCurrentDate();
  document.getElementById("currnetDate").value=today;
   getEmplVacationDetails();
  getPartyName();

});


    </script>

 