/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images
    fillTimeAttendanceData();

    $("#del_TimeAttendance_jqgrid").hide();
    $("#TimeAttendance_jqgrid_iladd").hide();

    document.getElementById("TimeAttendance_jqgrid_iledit").addEventListener("click", edit);
    document.getElementById("refresh_TimeAttendance_jqgrid").addEventListener("click", refresh);

    $.ajax({
        'async': false,
        type: "post",
        url: "getTimeAttendanceMonthConfig",
        data: {year: $("#year").val(), month: $("#month").val()},
//        dataType: "json",
        success: function (data) {
            if (data.trim() == "false") {
                $("#uploadedFile").prop("disabled", true);
            } else {
                $("#uploadedFile").prop("disabled", false);
            }
        }
    }); // End of Ajax Request

});

var start = 2017;
var end = new Date().getFullYear();
var options = "";
for (var year = start; year <= end; year++) {
    options += "<option>" + year + "</option>";
}
document.getElementById("yearView").innerHTML = options;
document.getElementById("year").innerHTML = options;


$("#year").change(function () {
    $.ajax({
        'async': false,
        type: "post",
        url: "getTimeAttendanceMonthConfig",
        data: {year: $("#year").val(), month: $("#month").val()},
//        dataType: "json",
        success: function (data) {
            if (data.trim() == "false") {
                $("#uploadedFile").prop("disabled", true);
                $.smallBox({
                    title: "Wrong",
                    content: "This Month Not Found.",
                    color: "#5384AF",
                    timeout: 5000,
                    icon: "fa fa-bell"
                });
            } else {
                $("#uploadedFile").prop("disabled", false);
            }
        }
    }); // End of Ajax Request


    TimeAttMonthIsExist();



});

$("#month").change(function () {
    $.ajax({
        'async': false,
        type: "post",
        url: "getTimeAttendanceMonthConfig",
        data: {year: $("#year").val(), month: $("#month").val()},
//        dataType: "json",
        success: function (data) {
            if (data.trim() == "false") {
                $("#uploadedFile").prop("disabled", true);
                $.smallBox({
                    title: "Wrong",
                    content: "This Month Not Found.",
                    color: "#5384AF",
                    timeout: 5000,
                    icon: "fa fa-bell"
                });
            } else {
                $("#uploadedFile").prop("disabled", false);
            }
        }
    }); // End of Ajax Request

    TimeAttMonthIsExist();
});


function search() {
    fillTimeAttendanceData();
}
function fillTimeAttendanceData() {
    var jqgrid_data = [];
    fillTimeAttendanceData_Ajax(jqgrid_data);

    var colNames = ["timeAttendanceId", "Party ID", "Card ID", "Date", "week Day", "Day Type", "IN", "OUT", "userLoginId"];
    var colModel = [{
            key: true,
            name: 'timeAttendanceId',
            index: 'timeAttendanceId',
            editable: true,
            hidden: true

        }, {
            name: 'partyId',
            index: 'partyId',
            editable: false,

        }, {
            name: 'cardId',
            index: 'cardId',
            editable: false,
            hidden: true
        }, {
            name: 'AttDate',
            index: 'AttDate',
            editable: false
        }, {
            name: 'weekDay',
            index: 'weekDay',
            editable: false
        }, {
            name: 'dayType',
            index: 'dayType',
            editable: false
        }, {
            name: 'checkIn',
            index: 'checkIn',
            editable: true
        }, {
            name: 'checkOut',
            index: 'checkOut',
            editable: true
        }, {
            name: 'userLoginId',
            index: 'userLoginId',
            editable: true,
            hidden: true
        }];

    fillGridData(colNames, colModel, jqgrid_data, "TimeAttendance_jqgrid", "TimeAttendance_jqgridDiv", "Time Attendance List", "timeAttendanceId", "asc", "", 30);

} //End of function fillInsuranceData 


$(document).ready(function () {

    //    Call Success Response Notifications
    jQuery("#TimeAttendance_jqgrid").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });

    jQuery("#TimeAttendance_jqgrid").on("jqGridInlineAfterSaveRow", function () {
        var jqgrid_data = [];
        ffillTimeAttendanceData_Ajax(jqgrid_data);

        jQuery("#TimeAttendance_jqgrid").jqGrid('clearGridData');
        jQuery("#TimeAttendance_jqgrid").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#TimeAttendance_jqgrid").trigger('reloadGrid');

    });
});

function fillTimeAttendanceData_Ajax(jqgrid_data) {
    $.ajax({
        'async': false,
        type: "post",
        url: "getTimeAttendanceData",
        data: {yearView: $("#yearView").val(), monthView: $("#monthView").val()},
        dataType: "json",
        success: function (data) {
            for (item in data) {
                jqgrid_data.push({
                    timeAttendanceId: data[item].timeAttendanceId,
                    partyId: data[item].partyId,
                    cardId: data[item].cardId,
                    AttDate: data[item].AttDate,
                    weekDay: data[item].weekDay,
                    dayType: data[item].dayType,
                    checkIn: data[item].checkIn,
                    checkOut: data[item].checkOut,
                    userLoginId: data[item].userLoginId
                });

            }
        }
    }); // End of Ajax Request
}

function edit() {
    $("#TimeAttendance_jqgrid").setGridParam({editurl: "updateTimeAttendanceMain"});
}

function refresh() {
    fillTimeAttendanceData();
}

function TimeAttMonthIsExist() {
    $.ajax({
        'async': false,
        type: "post",
        url: "timeAttendanceMonthIsExist",
        data: {year: $("#year").val(), month: $("#month").val()},
//        dataType: "json",
        success: function (data) {
            if (data.trim() == "true") {
                swal(
                        'Exist',
                        'Year And Month are Exist',
                        'error'
                        )
            }
        }
    }); // End of Ajax Request

}