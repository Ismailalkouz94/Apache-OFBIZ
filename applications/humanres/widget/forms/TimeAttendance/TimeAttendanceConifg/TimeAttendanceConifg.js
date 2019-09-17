$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images
    fillTimeAttendanceConfigData();
    document.getElementById("jqgrid_iladd").addEventListener("click", add);
    document.getElementById("jqgrid_iledit").addEventListener("click", edit);
    document.getElementById("del_jqgrid").addEventListener("click", deleteReferences);
    document.getElementById("refresh_jqgrid").addEventListener("click", refresh);

});
var TimeAttendanceConfigId_Delete = "";

var start = 2016;
var end = new Date().getFullYear();
var options = "";
for (var year = start; year <= end; year++) {
    options += "<option>" + year + "</option>";
}
document.getElementById("yearView").innerHTML = options;


$("#yearView").click(function () {
    fillTimeAttendanceConfigData();

});

function fillTimeAttendanceConfigData() {
    var jqgrid_data = [];
    fillTimeAttendanceConfigData_Ajax(jqgrid_data);


    colNames = ['ID ', 'Year', 'Month', 'Start', 'End', 'Work Day Hours', 'Increment Hours', 'userLoginId'];
    colModel = [{
            key: true,
            name: 'TimeAttendanceConfigId',
            index: 'TimeAttendanceConfigId',
            editable: true,

            hidden: true

        }, {
            name: 'year',
            index: 'year',
            editable: true,

        }, {
            name: 'month',
            index: 'month',
            editable: true,

            edittype: 'select',
            editoptions: {value: "January:January;February:February;March:March;April:April;May:May;June:June;July:July;August:August;September:September;October:October;November:November;December:December"}

        }, {
            name: 'startDate',
            index: 'startDate',

            editable: true,
            datatype: 'date',
            editoptions: {size: 10, dataInit: date()},
//            edittype: 'select', editoptions: {value: "1:1;2:2;3:3;4:4;5:5;6:6;7:7;8:8;9:9;10:10;11:11;12:12;13:13;14:14;15:15;16:17;18:18;19:19;20:20;21:21;22:22;23:23;24:24;25:25;26:26;27:27;28:28;29:29;30:30;31:31"}
        }, {
            name: 'endDate',
            index: 'endDate',

            editable: true,
            datatype: 'date',
            editoptions: {size: 10, dataInit: date()},
//            edittype: 'select', editoptions: {value: "1:1;2:2;3:3;4:4;5:5;6:6;7:7;8:8;9:9;10:10;11:11;12:12;13:13;14:14;15:15;16:17;18:18;19:19;20:20;21:21;22:22;23:23;24:24;25:25;26:26;27:27;28:28;29:29;30:30;31:31"}
        }, {
            name: 'workDayHours',
            index: 'workDayHours',
            editable: true,

            edittype: 'select',
            editoptions: {value: "1:1;2:2;3:3;4:4;5:5;6:6;7:7;8:8;9:9;10:10;11:11;12:12;13:13;14:14;15:15;16:17;18:18;19:19;20:20;21:21;22:22;23:23;24:24"}
        }, {
            name: 'incrementHours',
            index: 'incrementHours',
//            edittype: 'select',
//            editoptions: {dataUrl: 'getRelatedToIdDropDown'},
            editable: true,

//            editoptions: {Value: "0"}
        }, {
            name: 'userLoginId',
            index: 'userLoginId',
            editable: true,
            hidden: true,

        }];
    fillGridData(colNames, colModel, jqgrid_data, "jqgrid", "jqgridDiv", "Time Attendance Config Data", "TimeAttendanceConfigId", "desc", "Reference", 12);

} //End of function  



function rowSelected(rowId, gridId) {
    if (gridId == "jqgrid") {
        var rowData = jQuery('#jqgrid').getRowData(rowId);
        TimeAttendanceConfigId_Delete = rowData['TimeAttendanceConfigId'];
    }
}

function firstRowSelected(gridId) {
    if (gridId == "jqgrid") {
    }
}
$(document).ready(function () {

    //    Call Success Response Notifications
    jQuery("#jqgrid").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });

    jQuery("#jqgrid").on("jqGridInlineAfterSaveRow", function () {
        var jqgrid_data = [];
        fillTimeAttendanceConfigData_Ajax(jqgrid_data);

        jQuery("#jqgrid").jqGrid('clearGridData');
        jQuery("#jqgrid").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#jqgrid").trigger('reloadGrid');

    });
});


function fillTimeAttendanceConfigData_Ajax(jqgrid_data) {
    $.ajax({
        'async': false,
        type: "post",
        url: "getTimeAttendanceConfig",
        data: {year: $("#yearView").val()},
        dataType: "json",
        success: function (data) {
            for (item in data) {
                jqgrid_data.push({
                    TimeAttendanceConfigId: data[item].TimeAttendanceConfigId,
                    year: data[item].year,
                    month: data[item].month,
                    startDate: data[item].startDate,
                    endDate: data[item].endDate,
                    workDayHours: data[item].workDayHours,
                    incrementHours: data[item].incrementHours,
                    userLoginId: data[item].userLoginId,
                });
            }


        }
    }); // End of Ajax Request
}

function refresh()
{
    fillTimeAttendanceConfigData();
}




function add() {

    $("#jqgrid").setGridParam({editurl: "createTimeAttendanceConfig"});

    var year_ID = $("input.editable[name='year']").attr("id");
    document.getElementById(year_ID).value = $("#yearView").val();
    $("#" + year_ID).attr("disabled", true);

    var incrementHours_ID = $("input.editable[name='incrementHours']").attr("id");
    document.getElementById(incrementHours_ID).value = "0";
}


function edit() {

    $("#jqgrid").setGridParam({editurl: "updateTimeAttendanceConfig"});

}

function deleteReferences() {

    $("#jqgrid").setGridParam({editurl: 'deleteTimeAttendanceConfig?TimeAttendanceConfigId=' + TimeAttendanceConfigId_Delete});

}

function RefreshGrid(gridID) {
    if (gridID == "jqgrid") {
        fillTimeAttendanceConfigData();
    }

}

