var gradeId = 0;

function filterGradeId(jobGroupId) {
    var emplPositionId = $('#emplPositionIdHid').val();
    if (emplPositionId == null) {
        emplPositionId = "";
    }
//     {jobGroupId: jobGroupId, emplPositionId: emplPositionId}
    $.ajax({type: 'post', url: 'filterGradeId',
        data: {jobGroupId: jobGroupId, emplPositionId: emplPositionId},
        success: function (data) {
            if (document.getElementById('gradeId') != null) {
                document.getElementById('gradeId').innerHTML = data;
            }
            if (document.getElementById('gradeIdSearchOptions') != null) {
                var select = "<option value=''></option>" + data;
                document.getElementById('gradeIdSearchOptions').innerHTML = select;
            }
//            var gradeId = $('#gradeId').find(":selected").val();
//            fillDegreeSelect(gradeId);
        }});
}
function fillDegreeSelect(gradeId) {
    $.ajax({
        type: "post",
        url: "getDegreeDropDown",
        data: "gradeId=" + gradeId,
        dataType: "json",
        success: function (data) {
            $('#degreeSelect').find('option').remove();
            if (document.getElementById('degreeSelect') != null) {
                for (item in data) {
                    $("#degreeSelect").append("<option value=" + data[item].degreeId + ">" + data[item].degreeDesc + "</option>");
                }
            }
            if (document.getElementById('degreeIdSearchOptions') != null) {
//                $("#degreeIdSearchOptions").append("<option value=''></option>");
                for (item in data) {
                    $("#degreeIdSearchOptions").append("<option value=" + data[item].degreeId + ">" + data[item].degreeDesc + "</option>");
                }
            }
            var degreeId = $('#degreeSelect').val();
            fillBasicSalary(degreeId)
        }});
}
function fillBasicSalary(degreeId) {
//    alert(degreeId);
//    var gradeId = $('#gradeId').val();
    if (degreeId != null) {
        $.ajax({
            type: "post", url: "fillBasicSalary",
            data: "degreeId=" + degreeId,
            success: function (data) {
                $('#basicSalary').val(data);
            }});
    } else {
        $('#basicSalary').val("");
    }
}

function filterStatus(statusId) {
    if (statusId == 'EMPL_POS_PLANNEDFOR') {
        $("#EditEmplPosition_budgetId_title").parent().parent().css('display', "contents");
        $("#EditEmplPosition_budgetItemSeqId_title").parent().parent().css('display', "contents");
        $("#EditEmplPosition_estimatedFromDate_title").parent().parent().css('display', "contents");
        $("#EditEmplPosition_estimatedThruDate_title").parent().parent().css('display', "contents");
    } else {
        $("#EditEmplPosition_budgetId_title").parent().parent().css('display', "none");
        $("#EditEmplPosition_budgetItemSeqId_title").parent().parent().css('display', "none");
        $("#EditEmplPosition_estimatedFromDate_title").parent().parent().css('display', "none");
        $("#EditEmplPosition_estimatedThruDate_title").parent().parent().css('display', "none");
    }
}
function fulfillmentsPartyId(Id) {
    var partyId;
    if (Id != null) {
        sessionStorage.setItem("partyId", Id);
        partyId = Id;
    } else {
        partyId = sessionStorage.getItem("partyId");
    }
    if (document.getElementById('AddEmplPositionFulfillment') != null) {
        $.ajax({type: 'post', url: 'fulfillmentsPartyId',
            data: 'partyId=' + partyId,
            success: function (data) {
                document.getElementById('AddEmplPositionFulfillment_partyId').innerHTML = data;
            }});
    }
}
function getInternalOrganization(employmentId) {
    $.ajax({type: 'post', url: 'getInternalOrganization',
        data: 'employmentId=' + employmentId,
        success: function (data) {
            $("#EditEmplPosition_partyId").val(data.trim());
        }});
}
function onChangeValueFromLookUp(loockupTextId) {
//    if ('0_lookupId_customAddEmplPositionFulfillment_partyId' == loockupTextId) {
//        getEmplStatus($("#" + loockupTextId).val());
//    }
//    if ('1_lookupId_customAddEmplPositionFulfillment_emplPositionId' == loockupTextId) {
//        getGrade($("#" + loockupTextId).val());
//    }
    //      alert( $("#"+loockupTextId).val());

}
function getEmployeeDetails(partyId) {
//    fill data 
//    alert(partyId);
    fillSalaryGradeTable(partyId);
    var emplPositionId = null;
    $.ajax({
        'async': false,
        type: 'post',
        url: 'getEmplStatusDesciption',
        data: 'partyId=' + partyId,
        dataType: 'json',
        success: function (data) {
//            console.log(data);

            if (data.emplStatus != null) {
//                 alert(data.emplStatus);
                emplPositionId = data.emplPositionId;
                $('#customAddEmplPositionFulfillment_EmplStatus').val(data.emplStatus);
                $('#customAddEmplPositionFulfillment_gradeId').val(data.gradeDesc);
                $('#degreeDesc').val(data.degreeDesc);
                $('#1_lookupId_customAddEmplPositionFulfillment_emplPositionId').val(data.emplPositionId);
                fillBasicSalary(data.degreeId);
                $('#customAddEmplPositionFulfillment_fromDate_i18n').val("");
            } else {
                $('#customAddEmplPositionFulfillment_EmplStatus').val("");
                $('#customAddEmplPositionFulfillment_gradeId').val("");
                $('#degreeDesc').val("");
                $('#1_lookupId_customAddEmplPositionFulfillment_emplPositionId').val("");
                $('#basicSalary').val("");
            }
        }});

    if (!($('#customAddEmplPositionFulfillment_EmplStatus').val())) {
        document.getElementById("customAddEmplPositionFulfillment").onkeypress = function (e) {
            var key = e.charCode || e.keyCode || 0;
            if (key == 13) {
                e.preventDefault();
            }
        }
    }
    if ($('#customAddEmplPositionFulfillment_EmplStatus').val() != "Under Hiring") {
        document.getElementById("customAddEmplPositionFulfillment").onkeypress = function (e) {
            var key = e.charCode || e.keyCode || 0;
            if (key == 13) {
                e.preventDefault();
            }
        }

//        $('#degreeSelect').html("");
//        $('#tdDegreeSelect').html(""); 
        $("#degreeDesc").css('display', "block");
//        $("#degreeDesc").css('margin-left', -15);
        $('#tdDegreeSelect').css('display', "none");
//        $("#degreeSelect").css('display', "none");
//        $("#customAddEmplPositionFulfillment").find("input[name='submitButton']").css('display', "none");
        $("#addEmplPositionFulfillment_Btn").css('display', "none");

        $('#1_lookupId_button').css('pointer-events', 'none');
        $('#1_lookupId_customAddEmplPositionFulfillment_emplPositionId').prop('disabled', true);
        $('#customAddEmplPositionFulfillment_fromDate_i18n').prop('disabled', true);
//        if (emplPositionId != null) {
//            $.ajax({
//                type: 'post',
//                url: 'PositionGradation?emplPositionId=' + emplPositionId,
//                data: 'partyId=' + partyId,
//                success: function (data) {
////                    console.log("=============");
////                    console.log(data);
//                    var n = data.search('<table class="table table-bordered table-striped">');
//                    var str = data.substring(n);
//                    var n1 = str.search("</table>");
//                    var str2 = str.substring(0, n1 + 8);
//
//                    $("#MyListEmplPositionFulfillments > div > div > table > tbody").html("");
//                    $("#MyListEmplPositionFulfillments > div > div > table").html(str2);
//                }});
//        } else {
//            $("#MyListEmplPositionFulfillments > div > div > table > tbody").html("");
//        }
    } else if ($('#customAddEmplPositionFulfillment_EmplStatus').val() == "Under Hiring") {
        $("#degreeDesc").css('display', "none");
//        $("#degreeSelect").css('display', "block");
        $('#tdDegreeSelect').css('display', "block");
        $("#customAddEmplPositionFulfillment").find("input[name='submitButton']").css('display', "block");
        $("#MyListEmplPositionFulfillments > div > div > table > tbody").html("");
        $('#1_lookupId_button').css('pointer-events', 'block');
        $('#1_lookupId_customAddEmplPositionFulfillment_emplPositionId').prop('disabled', false);
        $('#customAddEmplPositionFulfillment_fromDate_i18n').prop('disabled', false);
    }
// fillSalaryGradeTable(partyId);
}
function afterSave() {
    if ($('#customAddEmplPositionFulfillment_EmplStatus').val() != "Under Hiring") {
        $.smallBox({
            title: "Error",
            content: "The Employee Must Be Under Hiring .",
            color: "#C46A69",
            timeout: 5000,
            icon: "fa fa-times fa-x fadeInRight animated"
        });
        $('#0_lookupId_customAddEmplPositionFulfillment_partyId').val("");
    }
    var num = $('#1_lookupId_customAddEmplPositionFulfillment_emplPositionId_auto').html().toLowerCase().indexOf("no records found");
    if (num != -1) {
        $.smallBox({
            title: "Error",
            content: "No Position Found .",
            color: "#C46A69",
            timeout: 5000,
            icon: "fa fa-times fa-x fadeInRight animated"
        });
        $('#1_lookupId_customAddEmplPositionFulfillment_emplPositionId').val("");
    }
}
function getGrade(emplPosId) {
    $.ajax({type: 'post', url: 'getGrade',
        data: 'emplPosId=' + emplPosId,
        dataType: 'json',
        success: function (data) {
//            console.log(data);
            if (data != null) {
                gradeId = data.gradeId;
                $('#customAddEmplPositionFulfillment_gradeId').val(data.gradeDesc);
            }
            fillDegreeSelect(gradeId);
        }});
}
$(window).load(function () {

    if (document.getElementById('EditEmplPosition') != null) {
        var partyId;
        if (document.getElementById('0_lookupId_EditEmplPosition_partyId') != null) {
            partyId = document.getElementById('0_lookupId_EditEmplPosition_partyId').value;
        } else if (document.getElementById('EditEmplPosition_partyId') != null) {
            partyId = document.getElementById('EditEmplPosition_partyId').value;
        }
        fulfillmentsPartyId(partyId);
        var selectedJobGroupId = document.getElementById('EditEmplPosition_jobGroupId');
        var jobGroupId = selectedJobGroupId.value;
        filterGradeId(jobGroupId);
        filterStatus(document.getElementById('EditEmplPosition_statusId').value);
        $("#basicSalary").prop('disabled', true);
        $("#EditEmplPosition_partyId").prop('disabled', true);
//        $('#1_lookupId_button').css('pointer-events', 'none');
    }
    if (document.getElementById('FindFulfillments') != null) {
        var selectedJobGroupId = $('#FindFulfillments_jobGroupId').val();
        filterGradeId(selectedJobGroupId);
    }

    if (document.getElementById('EmplPositionInfo') != null) {
        var partyId = document.getElementById('EmplPositionInfo_partyId').value;
        fulfillmentsPartyId(partyId);
    }

    if (document.getElementById('AddEmplPositionFulfillment') != null) {
//        var url = new URL(document.URL);
//        if (url.searchParams.get("statusId") != null) {
//            var partyId = url.searchParams.get("partyId");
//        }
        fulfillmentsPartyId(null);
//       var flage =document.getElementById('wid-id-1').childNodes[3].childNodes[1].childNodes[1].childNodes[1].childNodes[1].childNodes[3];
        var flage = $("#MyListEmplPositionFulfillments >  div > div > table > tbody");
        if (flage.length != 0)
        {
            $("#AddEmplPositionFulfillment > table > tbody > tr > td > input[name='submitButton']").css('display', "none");
        }

    }
    if (document.getElementById('customAddEmplPositionFulfillment') != null) {
//        $("#MyListEmplPositionFulfillments > div > div > table > tbody").html("");
        $('#customAddEmplPositionFulfillment_EmplStatus').prop('disabled', true);
        $('#customAddEmplPositionFulfillment_gradeId').prop('disabled', true);
        $("#basicSalary").prop('disabled', true);
        $('#degreeDesc').prop('disabled', true);
        $("#degreeDesc").css('display', "none");
        var partyId = $("#0_lookupId_customAddEmplPositionFulfillment_partyId").val();
//        if (partyId != "") {
        getEmployeeDetails(partyId);
//        }
//       $('#0_lookupId_customAddEmplPositionFulfillment_partyId').prop('disabled', true);
//       $('#1_lookupId_customAddEmplPositionFulfillment_emplPositionId').prop('disabled', true);
    }
});



