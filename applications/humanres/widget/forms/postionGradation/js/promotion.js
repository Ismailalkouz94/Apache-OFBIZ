var degreeDropDown = document.getElementById('degreeSelect');
var newPostionDropDown = document.getElementById('newPostion');
var newGradeDropDown = document.getElementById('gradeSelect');
var activeDate = 0;
var promotionDate = 0;
var currentPositionId = 0;
var currentDegreeId = 0;
var CurrentGradeId = 0;
var jobGroupId = 0;

var transDate = 0;
var rowSeq = 0;
var transType = 0;
var positionId = 0;
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

$("#gradeSelect").change(function () {
    var gradeSelectedId = newGradeDropDown.options[newGradeDropDown.selectedIndex].value;
    fillDegreeSelect(gradeSelectedId, currentDegreeId);
    getNewPostion(gradeSelectedId, jobGroupId);
    var degreeId = degreeDropDown.options[degreeDropDown.selectedIndex].id;
    fillNewSalary(degreeId);

});


$("#newPostion").change(function () {
    var newPositionId = newPostionDropDown.options[newPostionDropDown.selectedIndex].id;

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
                $("#promotionBtn").show();
                if ($('#0_lookupId_partyId').val() != "") {
                    var today = getCurrentDate();

                    if (lastTransDate >= selectDate) {
                         WarningMessage("Please select valid date");
                    
                        document.getElementById("bounseDate").value = "";
                    } else if(today < selectDate){
                         WarningMessage("please select current date");
                    
                        document.getElementById("bounseDate").value = "";
                    }
                }
            } else {

                $("#promotionBtn").hide();
                WarningMessage("This date is included in the calculation of salary !");
             

            }

        }
    });







});


function onChangeValueFromLookUp(textFieldId) {
    document.getElementById("bounseDate").value = "";
    var partyId = document.getElementById('0_lookupId_partyId').value;
    var fullName = getPartyName(partyId);
    document.getElementById("newSalary").value = 0;

    var emplStatus = getEmployeeStatus(partyId);
    $('#newPostion').find('option').remove();
    $('#degreeSelect').find('option').remove();
    $('#gradeSelect').find('option').remove();
    var empId = document.getElementById(textFieldId).value;
    $.ajax({
        'async': false,
        type: "post",
        url: "getPromotionDetails",
        data: "empId=" + empId,
        dataType: "json",
        success: function (data) {
            if (emplStatus == "HIRED") {
               if(isEmployeeTerminated(partyId)=="false"){
                fillSalaryGradeTable(partyId);
                $("#EmplPostion").html("");
                currentPositionId = data.postionId;
                document.getElementById("gradeId").value = data.gradeDesc;
                document.getElementById("currentDegree").value = data.degreeDesc;
                document.getElementById("postion").value = data.postionTypeId;
                document.getElementById("employment").value = data.employment;
                document.getElementById("basicSalary").value = data.basicSalary;
                currentDegreeId = data.degreeId;
                CurrentGradeId = data.gradeId;
                jobGroupId = data.jobGroupId;
                activeDate = data.TransDate;
                promotionDate = data.promotionDate;
                lastTransDate = data.lastTransDate;
                $("#0_lookupId_partyId_lookupDescription").html(" " + fullName);
                $("#EmplPostion").html(data.postionTypeDesc);

                fillGradeSelect(jobGroupId, CurrentGradeId);
                var gradeSelectedId = newGradeDropDown.options[newGradeDropDown.selectedIndex].value;
                fillDegreeSelect(gradeSelectedId, currentDegreeId);
                getNewPostion(gradeSelectedId, jobGroupId);
                var degreeId = degreeDropDown.options[degreeDropDown.selectedIndex].id;
                fillNewSalary(degreeId);
            }  } else {
                $("#EmplPostion").html("");
                $("#0_lookupId_partyId_lookupDescription").html("");

            }

        }

    });


    $("#promotionBtn").show();

}
function fillGradeSelect(jobGroupId, currentGradeId) {
    $.ajax({
        'async': false,
        type: "post",
        url: "getGradeDropDown_Promotion",
        data: {jobGroupId: jobGroupId},
        success: function (data) {
            document.getElementById("gradeSelect").innerHTML = data;
        }});
    var gradeSelectedId = newGradeDropDown.options[newGradeDropDown.selectedIndex].id;
    getNewPostion(gradeSelectedId, jobGroupId);

//$("#"+currentGradeId+"").attr("selected","selected");
//$("#gradeSelect option[value='"+currentGradeId+"']").attr("selected","selected");
    $("#gradeSelect").val(currentGradeId);
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
            if (data.length != 0) {

                for (item in data) {
                    $("#degreeSelect").append("<option value=" + data[item].degreeId + " id=" + data[item].degreeId + ">" + data[item].degreeDesc + "</option>");

                }

                if (gradeId == CurrentGradeId) {
                    $("#degreeSelect").val(degreeId);
                } else {
                    $("#degreeSelect").val(data[0].degreeId);
                }

            } else {
                document.getElementById("newSalary").value = 0;
            }
        }});
//                
}


function getNewPostion(gradeId, jobGroupId) {

    $.ajax({
        'async': false,
        type: "post",
        url: "getNewPostion",
        data: {gradeId: gradeId, jobGroupId: jobGroupId},
        dataType: "json",
        success: function (data) {

            $('#newPostion').find('option').remove();
            for (item in data) {
                $("#newPostion").append("<option id=" + data[item].emplPositionId + ">" + data[item].emplPositionId + "-" + data[item].EmplPositionTypeDescription + "</option>");
            }

        }});

}

function InsertToEmployeeSalaryGrade() {
    var NewdegreeId = degreeDropDown.options[degreeDropDown.selectedIndex].id;
    var newGradeId = newGradeDropDown.options[newGradeDropDown.selectedIndex].value;
    var newPositionId = newPostionDropDown.options[newPostionDropDown.selectedIndex].id;
    var partyId = document.getElementById("0_lookupId_partyId").value;

    var TransDate = document.getElementById("bounseDate").value;
    var partyIdFrom = document.getElementById("employment").value;
    var note = document.getElementById("note").value;


    $.ajax({
        'async': false,
        type: "post",
        url: "createEmplSalaryGradePromotion",
        data: {partyId: partyId, emplPositionId: newPositionId,
            degreeId: NewdegreeId, gradeId: newGradeId,
            TransDate: TransDate, partyIdFrom: partyIdFrom, jobGroupId: jobGroupId,
            TransType: "${action}", currentPositionId: currentPositionId,
            fromDate: TransDate, thruDateUpdate: promotionDate, thruNull: "", notes: note
        },
        success: function (data) {

            onChangeValueFromLookUp("0_lookupId_partyId");
            refreshGrid(partyId);
console.log(data);

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
}

function INSERT() {
    var NewdegreeId = degreeDropDown.options[degreeDropDown.selectedIndex].id;
    var newGradeId = newGradeDropDown.options[newGradeDropDown.selectedIndex].value;
//    var newPositionId = newPostionDropDown.options[newPostionDropDown.selectedIndex].id;
    if (newGradeId != CurrentGradeId) {
        InsertToEmployeeSalaryGrade();

    } else {
        swal({
            title: 'Promotion Employee',
            text: 'You have selected the same grade and degree, Add it?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, add it!',
            cancelButtonText: 'No, cancel!',
            confirmButtonClass: 'btn btn-success',
            cancelButtonClass: 'btn btn-danger',
            buttonsStyling: false
        }).then((result) => {
            if (result.value) {
                InsertToEmployeeSalaryGrade();
            } else if (result.dismiss === 'cancel') {
                swal(
                        'Cancelled',
                        'added promotion  cancelled',
                        'error'
                        )
            }
        });

    }
}


function rowSelected(rowId, gridId) {

    var rowData = jQuery('#' + gridId).getRowData(rowId);
    transDate = rowData['transDate'];
    rowSeq = rowData['rowSeq'];
    transType = rowData['transType'];
    positionId = rowData['positionId'];
    isCalculated(transDate, $("#0_lookupId_partyId").val());

}

function afterDeleteEvents(){
   onChangeValueFromLookUp("0_lookupId_partyId");
     
}

function deleteSalaryGradation() {

    if (transType.trim() == "PROMOTION") {
        maxSeq = getMaxSequence(transType, $("#0_lookupId_partyId").val());

        if (rowSeq.trim() == maxSeq.trim()) {
            $("#SalaryGradeGrid").setGridParam({editurl: 'deleteEmplSalaryGradePromotion?positionId=' + positionId + '&partyId=' + $("#0_lookupId_partyId").val() + '&transDate=' + transDate + '&rowSeq=' + rowSeq + '&fromDate=' + transDate});


        } else if (rowSeq != maxSeq) {
            $("#eData").click();
   WarningMessage("This is not last transaction  PROMOTION !");
           }
    } else {
        $("#eData").click();
          WarningMessage("You must select PROMOTION action !");
      
    }
}
function refreshGrid() {
    refreshGridData($("#0_lookupId_partyId").val());

}