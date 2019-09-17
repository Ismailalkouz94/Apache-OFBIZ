<#ftl encoding="utf-8">
<#assign data = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getApproverActionData(request, response)>
<#assign instanceProcessId = session.getAttribute('processInstanceId') />


<#assign processName = session.getAttribute('processName') />

<#assign leaveId= "${data.get('leaveId')}" />

<#assign partyID= "${data.get('partyId')}" />

<#assign description = "${data.get('description')}" />


${session.setAttribute('partyId',"${(partyID)!''}")}
${session.setAttribute('leaveId',"${(leaveId)!''}")}

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
   getEmplVacationDetails();
  getPartyName();

});


    </script>

 