

var activeDate = 0;
var departmentId = 0;
var positionId = 0;
var terminationTypeId = $("#terminationType").val();
var degreeId = 0;
var gradeId = 0;
var jobGroupId = 0;
var serviceType ="";
var rowSeq =0;
var lastTransDate = 0;
var emplSalaryGrade =0;
var departmentId=0;
var reasonDescription="";
fillTerminationReason(terminationTypeId);


$(window).load(function () {

    $("#0_lookupId_emplId").attr("required", "required");
    fillEmploymetGrid(0);
        document.getElementById("del_employmetGrid").addEventListener("click", deleteSalaryGradation);
        document.getElementById("refresh_employmetGrid").addEventListener("click", refreshGrid);


});

function refreshGrid() {
    refreshGridData($("#0_lookupId_emplId").val());

}

$(document).ready(function () {
    document.getElementById("myform").onkeypress = function (e) {
        var key = e.charCode || e.keyCode || 0;
        if (key == 13) {
            onChangeValueFromLookUp("0_lookupId_emplId");
            e.preventDefault();
        }
    }

});


$("#terminateDate").change(function () {
  
    var selectDate = document.getElementById("terminateDate").value;
     $.ajax({
        'async': false,
        type: "post",
        url: "isCalculated_MangmentEmployee",
        data: {transDate: selectDate},
        success: function (data) {
       
            if(data.trim() == "false") {
             $("#btnTerminate").show();
                if ($('#0_lookupId_emplId').val() != "") {
        var today = getCurrentDate();

        if (lastTransDate >= selectDate) {
            WarningMessage("Please select valid date !");
                  document.getElementById("terminateDate").value = "";
        }
    }
            }else {
           
                 $("#btnTerminate").hide();
                       WarningMessage("This date is included in the calculation of salary !");
              
             
            }

        }
    });



});

$("#terminationType").change(function () {
    var terminationTypeId = $("#terminationType").val();
    fillTerminationReason(terminationTypeId);

});
function fillTerminationReason(terminationTypeId) {
    $.ajax({
        'async': false,
        type: "post",
        url: "getTerminationReasonDropDown",
        data: {terminationTypeId: terminationTypeId},
        dataType: "json",
        success: function (data) {
            $('#terminationReason').find('option').remove();
            if (data.length != 0) {
                for (item in data) {
                    $("#terminationReason").append("<option value=" + data[item].terminationReasonId + " id=" + data[item].terminationReasonId + ">" + data[item].description + "</option>");
                }

            }
        }});
//                
}

function getEmployeePoistionDetails(partyId){
    var emplStatus = getEmplStatus(partyId);
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeePositionDetails",
        data: "partyId=" + partyId,
        dataType: "json",
        success: function (data) {
             if (emplStatus == "HIRED") {
                  fillSalaryGradeTable(partyId);
             
                $("#0_lookupId_emplId_lookupDescription").html(" " + data.fullName);
                activeDate = data.TransDate;
                lastTransDate = data.lastTransDate;
                positionId = data.postionId;
                degreeId = data.degreeId;
                gradeId = data.gradeId;
                jobGroupId = data.jobGroupId;
                emplSalaryGrade = data.tableId;
                
        departmentId=data.employment;

            } else {
                $("#0_lookupId_emplId_lookupDescription").html(" ");
              
        
            }
        }

    });
}

var fullName = "";
function onChangeValueFromLookUp(textFieldId) {

    var partyId = document.getElementById('0_lookupId_emplId').value;
    fullName = getPartyName(partyId);
      var emplStatus = getEmplStatus(partyId);
   
    $("#0_lookupId_emplId_lookupDescription").html(" " + fullName);
    if (emplStatus == "HIRED" ||emplStatus == "HOLD") { 
      getEmployeePoistionDetails(partyId);
        if(emplStatus == "HOLD"){
            WarningMessage("Please Note : status for "+fullName + "  is hold now but you can terminate him!");
        }
        $("#btnTerminate").show();
        fillEmploymetGrid(partyId);
    } else if(emplStatus.trim()=="TERMINATED"){
          WarningMessage(fullName + " was terminated from your company!");
                  document.getElementById("myform").reset();
        fillEmploymetGrid(0);
    }
      else{
           WarningMessage("The employee not found!");
        document.getElementById("myform").reset();  
        fillEmploymetGrid(0);
    }
   


}

function deleteEmpl()
{
    swal({
        title: 'Terminate Employee ',
        text: 'Do you want to termination ' + fullName + ' ? \n\n Terminate it?',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, terminate it!',
        cancelButtonText: 'No, cancel!',
        confirmButtonClass: 'btn btn-success',
        cancelButtonClass: 'btn btn-danger',
        buttonsStyling: false
    }).then((result) => {
        if (result.value) {
//            swal(
//                    'Terminated!',
//                    fullName + ' was terminated successfully',
//                    'success'
//                    );
//            $("#btnTerminate").hide();
            terminatedEmployee();

        } else if (result.dismiss === 'cancel') {
            swal(
                    'Cancelled',
                    'termination cancelled',
                    'error'
                    )
        }
    });
}

function terminatedEmployee() {
    var partyId = document.getElementById("0_lookupId_emplId").value;
    var TransDate = document.getElementById("terminateDate").value;
    var terminationTypeId = $("#terminationType").val()
    var terminationReasonId = $("#terminationReason").val();
    
 var note = document.getElementById("note").value;

    $.ajax({
        'async': false,
        type: "post",
        url: "TERMINATEDEMPLOYEE",
        data: {partyId: partyId, emplPositionId: positionId, terminationReasonId: terminationReasonId,
            terminationTypeId: terminationTypeId, thruDate: TransDate,
                degreeId: degreeId, gradeId: gradeId,  TransDate: TransDate, partyIdFrom: departmentId, jobGroupId: jobGroupId,notes:note,
                TransType: "TERMINATED","serviceType":"terminated"
        },
        success: function (data) {

           
                if ('_ERROR_MESSAGE_' in data) {
                ErrorMessage(data._ERROR_MESSAGE_);
            } else if ('_ERROR_MESSAGE_LIST_' in data) {

                ErrorMessage(data._ERROR_MESSAGE_LIST_);
            } else if ('_EVENT_MESSAGE_' in data) {
                
                SuccessMessage(data._EVENT_MESSAGE_);
                 refreshGridData(partyId);
            }


        }
    });

}


function afterDeleteEvents() {
    onChangeValueFromLookUp("0_lookupId_emplId");
    refreshGridData($("#0_lookupId_emplId").val());
}
function rowSelected(rowId, gridId) {
 var rowData = jQuery('#' + gridId).getRowData(rowId);
    transDate = rowData['thruDate'];
    rowSeq = rowData['rowSeq'];
//    transType = rowData['transType'];
//    positionId = rowData['positionId'];
    isCalculated(transDate, $("#0_lookupId_partyId").val());

    if(reasonDescription.length ==0){
        
     $("#del_employmetGrid").hide();
}else {
    $("#del_employmetGrid").show();
}
}


function fillEmploymetGrid(partyId) {

    var employeeName = "";
    var jqgrid_data = [];
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmploymentData",
        data: {partyId: partyId},
        dataType: "json",
        success: function (EmployeeData) {
            if (EmployeeData.length != 0) {
                employeeName = EmployeeData[0].fullName;
            }
            for (item in EmployeeData) {
                reasonDescription=EmployeeData[item].reasonDescription;
                jqgrid_data.push({
                    rowSeq: EmployeeData[item].employmentId,
                    roleTypeIdFrom: EmployeeData[item].roleTypeIdFrom,
                    roleTypeIdTo: EmployeeData[item].roleTypeIdTo,
                    partyIdFrom: EmployeeData[item].depDescription,
                    fromDate: EmployeeData[item].fromDate,
                    thruDate: EmployeeData[item].thruDate,
                    typeDescription: EmployeeData[item].typeDescription,
                    reasonDescription: EmployeeData[item].reasonDescription

                });
            }

        }});
    var colNames = ['', 'Role From', 'Role To', 'Department', 'Terminated Type', 'Terminated Reason', 'Start Date', 'End Date'];
    var colModel = [{
            name: 'rowSeq',
            index: 'rowSeq',
            align: 'center',
            hidden: true

        }, {
            name: 'roleTypeIdFrom',
            index: 'roleTypeIdFrom',
            align: 'center',
            hidden: true


        }, {

            name: 'roleTypeIdTo',
            index: 'roleTypeIdTo',
            editable: false,
            hidden: true

        }, {
            name: 'partyIdFrom',
            index: 'partyIdFrom',
            editable: false

        }, {
            name: 'typeDescription',
            index: 'typeDescription',
            editable: false

        },
        {
            name: 'reasonDescription',
            index: 'reasonDescription',
            editable: false

        },
        {
            name: 'fromDate',
            index: 'fromDate',
            editable: false


        },
        {
            name: 'thruDate',
            index: 'thruDate',
            editable: false


        }
    ];

    deleteGridData(colNames, colModel, jqgrid_data, "employmetGrid", "employmetGridDiv", "Employement for  [ " + employeeName + "]", "fromDate", "desc","this action ", 5);
    if (partyId != 0) {
        $('#employmetGrid').jqGrid('setCaption', 'Employee Salary Grade for [ ' + employeeName + ' ]');
    } else {
        $('#employmetGrid').jqGrid('setCaption', 'Employee Salary Grade for [ ]');

    }

}


function refreshGridData(partyId) {

    var employeeName = "";
    var jqgrid_data = [];
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmploymentData",
        data: {partyId: partyId},
        dataType: "json",
        success: function (EmployeeData) {
            if (EmployeeData.length != 0) {
                employeeName = EmployeeData[0].fullName;
            }
            for (item in EmployeeData) {
                jqgrid_data.push({
                    rowSeq: EmployeeData[item].employmentId,
//                    roleTypeIdFrom: EmployeeData[item].roleTypeIdFrom,
//                    roleTypeIdTo: EmployeeData[item].roleTypeIdTo,
                    partyIdFrom: EmployeeData[item].depDescription,
                    fromDate: EmployeeData[item].fromDate,
                    thruDate: EmployeeData[item].thruDate,
                    typeDescription: EmployeeData[item].typeDescription,
                    reasonDescription: EmployeeData[item].reasonDescription

                });
            }

        }});

    jQuery("#employmetGrid").jqGrid('clearGridData');
    jQuery("#employmetGrid").jqGrid('setGridParam', {data: jqgrid_data});
    jQuery("#employmetGrid").trigger('reloadGrid');
}




function deleteSalaryGradation() {
   
//        maxSeq = getMaxSequence(transType, $("#0_lookupId_partyId").val());

//        if (rowSeq.trim() == maxSeq.trim()) {


            $("#employmetGrid").setGridParam({editurl: 'deleteTerminated?positionId=' + positionId + '&partyId=' + $("#0_lookupId_emplId").val() +  '&thruDate=null&terminationReasonId=null&terminationTypeId=null&transDate=' + transDate + '&emplSalaryGrade='+emplSalaryGrade+'&rowSeq=' + rowSeq + '&fromDate=' + transDate+'&serviceType=delete'});

selectRo
//        } else if (rowSeq != maxSeq) {
//            $("#eData").click();
//   WarningMessage("This is not last transaction  Terminated !");
//           }
     
}
