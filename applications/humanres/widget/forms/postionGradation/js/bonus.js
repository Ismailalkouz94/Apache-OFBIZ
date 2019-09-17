
var degreeDropDown = document.getElementById('degreeSelect');
var newPostionDropDown = document.getElementById('newPostion');
var activeDate = 0;
var postionId = 0;
var currentDegreeId = 0;
var CurrentGradeId = 0;
var JobGroupId = 0;
var transDate = 0;
var rowSeq = 0;
var transType = 0;
var lastTransDate = 0;


$(window).load(function () {
    fillSalaryGradeTable(0);

    document.getElementById("del_SalaryGradeGrid").addEventListener("click", deleteSalaryGradation);
    document.getElementById("refresh_SalaryGradeGrid").addEventListener("click", refreshGrid);
    $("#0_lookupId_partyId").attr("required", "required");
});

$(document).ready(function () {


    document.getElementById("myform").onkeypress = function (e) {
        var key = e.charCode || e.keyCode || 0;
        if (key == 13) {
            onChangeValueFromLookUp("0_lookupId_partyId");
            e.preventDefault();
        }
    }


});


function afterDeleteEvents() {
    onChangeValueFromLookUp("0_lookupId_partyId");
    refreshGridData($("#0_lookupId_partyId").val());
}
function fillNewSalary(degreeId) {
    $.ajax({
        'async': false,
        type: "post",
        url: "fillBasicSalary",
        data: {degreeId: degreeId},
        dataType: "json",
        success: function (data) {

            document.getElementById("newSalary").value = data;
        }
    }

    );
}

$("#degreeSelect").change(function () {
    var degreeId = degreeDropDown.options[degreeDropDown.selectedIndex].id;
    fillNewSalary(degreeId);
});

$("#bounseDate").change(function () {
    var selectDate = document.getElementById("bounseDate").value;
    $.ajax({
        'async': false,
        type: "post",
        url: "isCalculated_MangmentEmployee",
        data: {transDate: selectDate},
        success: function (data) {

            if (data.trim() == "false") {
                $("#saveBounse").show();
                if ($('#0_lookupId_partyId').val() != "") {
                    var today = getCurrentDate();

                    if (lastTransDate >= selectDate) {
                        WarningMessage("Please select valid date !");

                        document.getElementById("bounseDate").value = "";
                    }
                }
            } else {

                $("#saveBounse").hide();
                WarningMessage("This date is included in the calculation of salary!");

            }

        }
    });





});


function onChangeValueFromLookUp(textFieldId) {
    $("#del_SalaryGradeGrid").show();

    document.getElementById("bounseDate").value = "";
    var partyId = document.getElementById('0_lookupId_partyId').value;
    var fullName = getPartyName(partyId);
    document.getElementById("newSalary").value = 0;
    var emplStatus = getEmployeeStatus(partyId);

    $('#newPostion').find('option').remove();
    $('#degreeSelect').find('option').remove();
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeePositionDetails",
        data: "partyId=" + partyId,
        dataType: "json",
        success: function (data) {
            if (emplStatus == "HIRED") {
                    if(isEmployeeTerminated(partyId)=="false"){
                fillSalaryGradeTable(partyId);
                $("#EmplPostion").html("");
                postionId = data.postionId;
                document.getElementById("gradeId").value = data.gradeDesc;
                document.getElementById("currentDegree").value = data.degreeDesc;
                document.getElementById("postion").value = data.postionId + "-" + data.postionTypeId;
                document.getElementById("employment").value = data.employment;
                document.getElementById("basicSalary").value = data.basicSalary;

                currentDegreeId = data.degreeId;
                CurrentGradeId = data.gradeId;
                JobGroupId = data.jobGroupId;
                $("#0_lookupId_partyId_lookupDescription").html(" " + fullName);

                activeDate = data.TransDate;
                lastTransDate = data.lastTransDate;

                $("#EmplPostion").html(data.postionTypeDesc);
                fillDegreeSelect(CurrentGradeId, currentDegreeId);
                var degreeId = degreeDropDown.options[degreeDropDown.selectedIndex].id;
                fillNewSalary(degreeId);
            } } else {
                $("#0_lookupId_partyId_lookupDescription").html(" ");

                $("#EmplPostion").html("");

            }
        }

    });


    $("#saveBounse").show();
}

function fillDegreeSelect(gradeId, degreeId) {

    $.ajax({
        'async': false,
        type: "post",
        url: "getDegreeDropDown",
        data: {gradeId: gradeId},
        dataType: "json",
        success: function (data) {

            $('#degreeSelect').find('option').remove();
            for (item in data) {
                $("#degreeSelect").append("<option value=" + data[item].degreeId + " id=" + data[item].degreeId + ">" + data[item].degreeDesc + "</option>");

            }
            var nextDegreeId = getNextDegree(gradeId, degreeId);
//  alert(nextDegreeId);

// $("#degreeSelect").val(nextDegreeId);
            var kouz = "true";


            if (nextDegreeId != "") {

                $("#degreeSelect").val(nextDegreeId);
            }
//$("#"+nextDegreeId).attr("selected","selected");

        }});

}
//alert($("#bonus").text());
function getNextDegree(gradeId, degree) {
    var nextDegreeId = "";
    $.ajax({
        'async': false,
        type: "post",
        url: "getNextDegree",
        data: {degreeId: degree, gradeId: gradeId},
        dataType: "json",
        success: function (data) {


            nextDegreeId = data.degreeId;


        }});

    return nextDegreeId;
}


function InsertToEmployeeSalaryGrade() {

    var NewdegreeId = $("#degreeSelect").val();

    if (NewdegreeId != currentDegreeId) {
        var partyId = document.getElementById("0_lookupId_partyId").value;

        var TransDate = document.getElementById("bounseDate").value;
        var partyIdFrom = document.getElementById("employment").value;
        var note = document.getElementById("note").value;


        $.ajax({
            'async': false,
            type: "post",
            url: "createEmplSalaryGradeBounse",
            data: {partyId: partyId, emplPositionId: postionId,
                degreeId: NewdegreeId, gradeId: CurrentGradeId,
                TransDate: TransDate, partyIdFrom: partyIdFrom, jobGroupId: JobGroupId,
                TransType: "${action}", notes: note, currentDegreeId: currentDegreeId
            },
            success: function (data) {

                console.log(data);
//alert(data._ERROR_MESSAGE_);
                onChangeValueFromLookUp("0_lookupId_partyId");
                refreshGrid(partyId);
                fillDegreeSelect(CurrentGradeId, currentDegreeId);

                if ('_ERROR_MESSAGE_' in data) {
                    ErrorMessage(data._ERROR_MESSAGE_);
                } else if ('_ERROR_MESSAGE_LIST_' in data) {

                    ErrorMessage(data._ERROR_MESSAGE_LIST_);
                } else if ('_EVENT_MESSAGE_' in data) {
                    fillDegreeSelect(CurrentGradeId, currentDegreeId);
                    SuccessMessage(data._EVENT_MESSAGE_);
                }
            }
        });
    } else {
        WarningMessage("You Selected the current degree !");
    }
}

function rowSelected(rowId, gridId) {

    var rowData = jQuery('#' + gridId).getRowData(rowId);
    transDate = rowData['transDate'];
    rowSeq = rowData['rowSeq'];
    transType = rowData['transType'];

    isCalculated(transDate, $("#0_lookupId_partyId").val());

}

function deleteSalaryGradation() {
    if (transType.trim() == "BONUS") {
        maxSeq = getMaxSequence(transType, $("#0_lookupId_partyId").val());

        if (rowSeq.trim() == maxSeq.trim()) {

            $("#SalaryGradeGrid").setGridParam({editurl: 'deletePositionGradation_BONUS?partyId=' + $("#0_lookupId_partyId").val() + '&transDate=' + transDate + '&rowSeq=' + rowSeq});//      onChangeValueFromLookUp("0_lookupId_partyId");


        } else if (rowSeq != maxSeq) {
            $("#eData").click();
            WarningMessage("This is not last transaction  BONUS !");

        }
    } else {
        $("#eData").click();
        WarningMessage("You must select BONUS action !");

    }
}
function refreshGrid() {
    refreshGridData($("#0_lookupId_partyId").val());

}

