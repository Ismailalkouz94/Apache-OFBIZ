/* global item */

var activeDate = 0;
var departmentId = 0;
var positionId = 0;
var degreeId = 0;
var gradeId = 0;
var jobGroupId = 0;
var lastTransDate = 0;

//$("#activateBtn").hide();

$(window).load(function () {
    fillSalaryGradeTableHold(0);
    document.getElementById("del_SalaryGradeGrid").addEventListener("click", deleteSalaryGradation);
    document.getElementById("refresh_SalaryGradeGrid").addEventListener("click", refreshGrid);
       $("#SalaryGradeGrid_iladd").hide();
  
     document.getElementById("SalaryGradeGrid_iledit").addEventListener("click", unHold);
    $("#0_lookupId_emplId").attr("required", "required");
});
$(document).ready(function () {
        jQuery("#SalaryGradeGrid").on("jqGridInlineAfterSaveRow", function () {
 onChangeValueFromLookUp("0_lookupId_emplId");
// fillSalaryGradeTableHold($("#0_lookupId_emplId").val());
      
    });
jQuery("#SalaryGradeGrid").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });
    
    document.getElementById("myform").onkeypress = function (e) {
        var key = e.charCode || e.keyCode || 0;
        if (key == 13) {
            onChangeValueFromLookUp("0_lookupId_emplId");
            e.preventDefault();
        }
    }

});

function fillSalaryGradeTableHold(partyId) {
  
    var employeeName = "";
    var jqgrid_data = [];
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeeSalaryGrade",
        data: {partyId: partyId},
        dataType: "json",
        success: function (EmployeeData) {
            if (EmployeeData.length != 0) {
                employeeName = EmployeeData[0].fullName;
            }
            for (item in EmployeeData) {
                jqgrid_data.push({
                    emplSalaryGrade: EmployeeData[item].rowSeq,
                    partyId: EmployeeData[item].partyId,
                    TransDate: EmployeeData[item].TransDate,
                    jobGroupId: EmployeeData[item].emplPositionId + "-" + EmployeeData[item].emplPositionDescription,
                    gradeId: EmployeeData[item].gradeDesc,
                    degreeId: EmployeeData[item].degreeDesc,
                    TransType: EmployeeData[item].TransType,
                    basicSalary: EmployeeData[item].basicSalary,
                    partyIdFrom: EmployeeData[item].department,
                    emplPositionId: EmployeeData[item].emplPositionId,
                    endDate:EmployeeData[item].endDate,
                    notes:EmployeeData[item].notes

                });
            }


        }});
    var colNames = ['', 'Employee ID', 'Effect Date', 'Position', 'Grade', 'Degree', 'Transaction Type', 'Basic Salary', 'Department', 'End Date','Notes',''];
    var colModel = [{
            name: 'emplSalaryGrade',
            index: 'emplSalaryGrade',
            align: 'center',
            hidden: false,
            editable: true


        }, {
            name: 'partyId',
            index: 'partyId',
            align: 'center',
            hidden: true,
            editable: false


        }, {

            name: 'TransDate',
            index: 'TransDate',
            editable: false

        }, {
            name: 'jobGroupId',
            index: 'jobGroupId',
            editable: false

        }, {
            name: 'gradeId',
            index: 'gradeId',
            editable: false

        },
        {
            name: 'degreeId',
            index: 'degreeId',
            editable: false

        },
        {
            name: 'TransType',
            index: 'TransType',
            editable: false


        },
        {
            name: 'basicSalary',
            index: 'basicSalary',
            editable: false


        }, {
            name: 'partyIdFrom',
            index: 'partyIdFrom',
            editable: false


        }, {
            name: 'endDate',
            index: 'endDate',
            editable: true,
            editoptions: {size: 10, dataInit: date()},


        }, {
            name: 'notes',
            index: 'notes',
            editable: false


        },{
            name: 'emplPositionId',
            index: 'emplPositionId',
            editable: false,
            hidden: true


        },
     
    ];

    fillGridData(colNames, colModel, jqgrid_data, "SalaryGradeGrid", "SalaryGradeGridDiv", "Employee Salary Grade for [ " + employeeName + "]", "TransDate", "desc", "this action ", 10);

    if(partyId != 0){
    $('#SalaryGradeGrid').jqGrid('setCaption', 'Employee Salary Grade for [ '  + employeeName + ' ]');
    }else {
            $('#SalaryGradeGrid').jqGrid('setCaption', 'Employee Salary Grade for [ ]');

    }

}

$("#holdDate").change(function () {

    var selectDate = document.getElementById("holdDate").value;
    $.ajax({
        'async': false,
        type: "post",
        url: "isCalculated_MangmentEmployee",
        data: {transDate: selectDate},
        success: function (data) {

            if (data.trim() == "false") {
                $("#holdBtn").show();
                if ($('#0_lookupId_emplId').val() != "") {
                    var today = getCurrentDate();

                    if (lastTransDate >= selectDate) {
                        WarningMessage("Please select valid date");
                        document.getElementById("holdDate").value = "";
                    }
                }
            } else {

                $("#holdBtn").hide();
                WarningMessage("This date is included in the calculation of salary");


            }

        }
    });



});



function onChangeValueFromLookUp(textFieldId) {
    var partyId = document.getElementById('0_lookupId_emplId').value;
    var emplStatus = getEmplStatus(partyId);
    document.getElementById("holdDate").value = "";
    var fullName = getPartyName(partyId);
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeePositionDetails",
        data: "partyId=" + partyId,
        dataType: "json",
        success: function (data) {
            degreeId = data.degreeId;
            gradeId = data.gradeId;
            jobGroupId = data.jobGroupId;
            activeDate = data.TransDate;
            positionId = data.postionId;
            departmentId = data.employment;
            lastTransDate = data.lastTransDate;

        }});


if (emplStatus.trim() == "TERMINATED") {
        WarningMessage(fullName + " was terminated from your company!");

        document.getElementById("myform").reset();
    } 

    var partyId = document.getElementById('0_lookupId_emplId').value;
    var fullName = getPartyName(partyId);

    fillSalaryGradeTableHold(partyId);




    $("#holdBtn").show();

}


function InsertToEmployeeSalaryGrade() {

    var partyId = document.getElementById("0_lookupId_emplId").value;
//
    var TransDate = document.getElementById("holdDate").value;
   
       var note = document.getElementById("note").value;

    $.ajax({
        'async': false,
        type: "post",
        url: "HoldEmployeeService",
        data: {partyId: partyId, notes: note,
            TransDate: TransDate,endDate:$("#endDate").val()

        },
        success: function (data) {

            onChangeValueFromLookUp("0_lookupId_emplId");
            refreshGrid(partyId);

            if ('_ERROR_MESSAGE_' in data) {
                ErrorMessage(data._ERROR_MESSAGE_);
            } else if ('_ERROR_MESSAGE_LIST_' in data) {

                ErrorMessage(data._ERROR_MESSAGE_LIST_);
            } else if ('_EVENT_MESSAGE_' in data) {
                
                SuccessMessage(data._EVENT_MESSAGE_);
            }

$("#note").val("");



        }
    });
}



function rowSelected(rowId, gridId) {

    var rowData = jQuery('#' + gridId).getRowData(rowId);
    transDate = rowData['TransDate'];
    rowSeq = rowData['emplSalaryGrade'];
    transType = rowData['TransType'];
if(isUnpaidVacation() =="false"){
    isCalculated(transDate, $("#0_lookupId_emplId").val()); 
}
}


function afterDeleteEvents(){
   onChangeValueFromLookUp("0_lookupId_emplId");
     
}

function isUnpaidVacation(){
  
        $.ajax({
        'async': false,
        type: "post",
        url: "isUnPaidVacation",
        data: {rowId: rowSeq
            },
        success: function (data) {
 
            if (data.trim() == "false") {
                $("#del_SalaryGradeGrid").show();
                return "false";
            } else {
                $("#del_SalaryGradeGrid").hide();
               return "true"
            }

        }
    }); 
}

function deleteSalaryGradation() {
    if (transType.trim() == "HOLD") {
        maxSeq = getMaxSequence(transType, $("#0_lookupId_emplId").val());
        if (rowSeq.trim() == maxSeq.trim()) {
            
            $("#SalaryGradeGrid").setGridParam({editurl: 'deletePositionGradation_Holding?partyId=' + $("#0_lookupId_emplId").val() + '&transDate=' + transDate + '&rowSeq=' + rowSeq});
            
        } else if (rowSeq != maxSeq) {
            $("#eData").click();
            WarningMessage("This is not last transaction for hold !");

        }
    } else {
        $("#eData").click();
        WarningMessage("You must select hold or unhold action !");

    }
}
function refreshGrid() {
    
       var jqgrid_data = [];
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeeSalaryGrade",
        data: {partyId: $("#0_lookupId_emplId").val()},
        dataType: "json",
        success: function (EmployeeData) {

            for (item in EmployeeData) {

                jqgrid_data.push({
                    emplSalaryGrade: EmployeeData[item].rowSeq,
                    partyId: EmployeeData[item].partyId,
                    TransDate: EmployeeData[item].TransDate,
                    jobGroupId: EmployeeData[item].emplPositionId + "-" + EmployeeData[item].emplPositionDescription,
                    gradeId: EmployeeData[item].gradeDesc,
                    degreeId: EmployeeData[item].degreeDesc,
                   TransType: EmployeeData[item].TransType,
                    basicSalary: EmployeeData[item].basicSalary,
                    partyIdFrom: EmployeeData[item].department,
                    emplPositionId: EmployeeData[item].emplPositionId,
                    endDate:EmployeeData[item].endDate,
                    notes:EmployeeData[item].notes

                });

            }
        }});

    jQuery("#SalaryGradeGrid").jqGrid('clearGridData');
    jQuery("#SalaryGradeGrid").jqGrid('setGridParam', {data: jqgrid_data});
    jQuery("#SalaryGradeGrid").trigger('reloadGrid');

}


function unHold() {

    $("#SalaryGradeGrid").setGridParam({editurl: "updateEmplSalaryGrade_Hold"});
}

