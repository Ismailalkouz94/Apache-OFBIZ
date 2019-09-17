
var newDepartmentDropDown = document.getElementById('newDepartment');
var activeDate = 0;
var departmentId = 0;
var crrentDepartment = 0;
var positionId = 0;
var degreeId = 0;
var gradeId = 0;
var jobGroupId = 0;
var previousMovingDate = 0;
var lastTransDate = 0;
$(window).load(function () {
    fillSalaryGradeTable(0);

    document.getElementById("del_SalaryGradeGrid").addEventListener("click", deleteSalaryGradation);
    document.getElementById("refresh_SalaryGradeGrid").addEventListener("click", refreshGrid);
    $("#0_lookupId_emplId").attr("required", "required");
});
$(document).ready(function () {
    document.getElementById("myform").onkeypress = function (e) {
        var key = e.charCode || e.keyCode || 0;
        if (key == 13) {
            onChangeValueFromLookUp("0_lookupId_emplId");
            e.preventDefault();
        }
    }

});
$("#moveDate").change(function () {

    var selectDate = document.getElementById("moveDate").value;
    $.ajax({
        'async': false,
        type: "post",
        url: "isCalculated_MangmentEmployee",
        data: {transDate: selectDate},
        success: function (data) {

            if (data.trim() == "false") {
                $("#moveBtn").show();
                if ($('#0_lookupId_emplId').val() != "") {
                    var today = getCurrentDate();

                    if (lastTransDate >= selectDate) {
                        WarningMessage("Please select valid date");

                        document.getElementById("moveDate").value = "";
                    }
                }
            } else {

                $("#moveBtn").hide();
                WarningMessage("This date is included in the calculation of salary");


            }

        }
    });



});


function onChangeValueFromLookUp(textFieldId) {



    document.getElementById("moveDate").value = "";
    var partyId = document.getElementById('0_lookupId_emplId').value;
    var fullName = getPartyName(partyId);
    var emplStatus = getEmployeeStatus(partyId);

    $('#newDepartment').find('option').remove();

    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeePositionDetails",
        data: "partyId=" + partyId,
        dataType: "json",
        success: function (data) {
            if (emplStatus == "HIRED") {
                fillSalaryGradeTable(partyId);
                document.getElementById("currentDepartment").value = data.employment;
                crrentDepartment = data.employment;
                $("#0_lookupId_emplId_lookupDescription").html(" " + data.fullName);
                activeDate = data.TransDate;
                lastTransDate = data.lastTransDate;
                positionId = data.postionId;
                degreeId = data.degreeId;
                gradeId = data.gradeId;
                jobGroupId = data.jobGroupId;
                previousMovingDate = data.movingDate;
                $("#descSpan").html(" &ensp;" + data.depDesciption);
                fillEmploymentSelect();


            } else {
                $("#0_lookupId_emplId_lookupDescription").html(" ");
                $("#EmplPostion").html("");

            }
        }

    });

    $("#moveBtn").show();

}

function fillEmploymentSelect() {

    $.ajax({
        'async': false,
        type: "post",
        url: "getEmploymentDropDown",
        data: "",
        dataType: "json",
        success: function (data) {

            $('#newDepartment').find('option').remove();
            for (item in data) {
                $("#newDepartment").append("<option value=" + data[item].partyIdFrom + " id=" + data[item].partyIdFrom + ">" + data[item].partyIdFrom + "- " + data[item].groupName + "</option>");

            }

        }});

}



function InsertToEmployeeSalaryGrade() {

    var newDepartment = $("#newDepartment").val();
    if (crrentDepartment != newDepartment) {
        var partyId = document.getElementById("0_lookupId_emplId").value;
//
        var TransDate = document.getElementById("moveDate").value;
        var note = document.getElementById("note").value;
        $.ajax({
            'async': false,
            type: "post",
            url: "createEmplSalaryGradeMoving",
            data: {partyId: partyId, emplPositionId: positionId, partyIdTo: partyId,
                degreeId: degreeId, gradeId: gradeId, thruDateUpdate: previousMovingDate,
                TransDate: TransDate, partyIdFrom: newDepartment, jobGroupId: jobGroupId, fromDate: TransDate,
                TransType: "${action}", roleTypeIdFrom: "INTERNAL_ORGANIZATIO", roleTypeIdTo: "EMPLOYEE",
                thruDate: "", notes: note
            },
            success: function (data) {
                console.log(data);
                onChangeValueFromLookUp("0_lookupId_emplId");
                refreshGrid(partyId);
                if ('_ERROR_MESSAGE_' in data) {
                    ErrorMessage(data._ERROR_MESSAGE_);
                } else if ('_ERROR_MESSAGE_LIST_' in data) {

                    ErrorMessage(data._ERROR_MESSAGE_LIST_);
                } else if ('_EVENT_MESSAGE_' in data) {
                    fillEmploymentSelect();
                    SuccessMessage(data._EVENT_MESSAGE_);
                }





            }
        });
    } else {
        WarningMessage("You are selected the current employment !");

    }
}



function rowSelected(rowId, gridId) {

    var rowData = jQuery('#' + gridId).getRowData(rowId);
    transDate = rowData['transDate'];
    rowSeq = rowData['rowSeq'];
    transType = rowData['transType'];

    isCalculated(transDate, $("#0_lookupId_emplId").val());
}

function afterDeleteEvents(){
   onChangeValueFromLookUp("0_lookupId_emplId");
     
}

function deleteSalaryGradation() {
    if (transType.trim() == "MOVING") {
        maxSeq = getMaxSequence(transType, $("#0_lookupId_emplId").val());
        if (rowSeq.trim() == maxSeq.trim()) {
            $("#SalaryGradeGrid").setGridParam({editurl: 'deletePositionGradation_MOVING?partyId=' + $("#0_lookupId_emplId").val() + '&transDate=' + transDate + '&rowSeq=' + rowSeq});
            onChangeValueFromLookUp("0_lookupId_emplId");
        } else if (rowSeq != maxSeq) {
            $("#eData").click();
            WarningMessage("This is not last transaction for MOVING !");
        }
    } else {
        $("#eData").click();
        WarningMessage("You must select MOVING action !");

    }
}
function refreshGrid() {
    refreshGridData($("#0_lookupId_emplId").val());

}

