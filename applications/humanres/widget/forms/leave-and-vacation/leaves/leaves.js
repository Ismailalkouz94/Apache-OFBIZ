$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images
    fillEmployeeLeaveData();
    document.getElementById("leaveGrid_iladd").addEventListener("click", add);
    document.getElementById("leaveGrid_iledit").addEventListener("click", edit);
    document.getElementById("del_leaveGrid").addEventListener("click", deleteAllownces);
    document.getElementById("refresh_leaveGrid").addEventListener("click", refresh);
//    document.getElementById("leaveGrid_ilsave").addEventListener("click", save);

});

var date_ID = "";
function onChangeValueFromLookUp(lookup_ID) {

    fillEmployeeLeaveData();
}


$("form[name='LookupEmployeeName']").keypress(function (e) {
    var key = e.charCode || e.keyCode || 0;
    if (key == 13) {
        fillEmployeeLeaveData();
        e.preventDefault();
    }
});

var leaveId_Delete = "";


function fillEmployeeLeaveData() {
    var jqgrid_data = [];
    getEmployeeLeaveData_Ajax(jqgrid_data);

    colNames = ['ID', 'Party Id', 'leave Type Id', 'Application Date', 'Leave Reason Type', 'Date', 'From Time', 'Thru Time', 'Approver Party Id', 'Leave Status', 'Description', 'type'];
    colModel = [{
//            key: true,
            name: 'leaveId',
            index: 'leaveId',
            editable: true,
            hidden: true

        }, {
            name: 'partyId',
            index: 'partyId',
//            edittype: 'select',
//            editrules: {required: true},
//            editoptions: {dataUrl: 'getRelatedToIdDropDown'},
            hidden: true,
            editable: true,

        }, {
            name: 'leaveTypeId',
            index: 'leaveTypeId',
            edittype: 'select',
            editoptions: {dataUrl: 'getLeaveTypeId_DropDown'},
            hidden: true,
            editable: true
        }, {
            name: 'transDate',
            index: 'transDate',
            editoptions: {value: getCurrentDate()},
            editable: true

        }, {
            name: 'emplLeaveReasonTypeId',
            index: 'emplLeaveReasonTypeId',
            edittype: 'select',
            editoptions: {dataUrl: 'getEmplLeaveReasonTypeId_DropDown'},
            editable: true
        }, {
            name: 'date',
            index: 'date',
            editrules: {required: true, datetime: true},
            editoptions: {size: 10, dataInit: date()},
            editable: true,
            hidden: false

        }, {
            name: 'fromDate',
            index: 'fromDate',
            editrules: {required: true, datetime: true},
            editoptions: {size: 10, dataInit: time()},
            editable: true
        }, {
            name: 'thruDate',
            index: 'thruDate',
            editrules: {required: true, datetime: true},
            editoptions: {size: 10, dataInit: time()},
            editable: true
        }, {
            name: 'approverPartyId',
            index: 'approverPartyId',
            editable: true,
            hidden: true
        }, {
            name: 'leaveStatus',
            index: 'leaveStatus',
            editable: true
        }, {
            name: 'description',
            index: 'description',

            editable: true
        }, {
            name: 'type',
            index: 'type',
            editoptions: {value: "LEAVE"},
            editable: true,
            hidden: true
        }];
    fillGridData(colNames, colModel, jqgrid_data, "leaveGrid", "leaveGridDiv", "Employee Leave", "refId", "asc", "Employee Leave", 15);
} //End of function fillInsuranceData 



function rowSelected(rowId, gridId) {
    if (gridId == "leaveGrid") {
        var rowData = jQuery('#leaveGrid').getRowData(rowId);
        leaveId_Delete = rowData['leaveId'];
    }
}

function firstRowSelected(gridId) {
    if (gridId == "leaveGrid") {
    }
}
$(document).ready(function () {

    //    Call Success Response Notifications
    jQuery("#leaveGrid").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);

    });

    jQuery("#leaveGrid").on("jqGridInlineAfterSaveRow", function () {
        var jqgrid_data = [];
        getEmployeeLeaveData_Ajax(jqgrid_data);

        jQuery("#leaveGrid").jqGrid('clearGridData');
        jQuery("#leaveGrid").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#leaveGrid").trigger('reloadGrid');

    });
});

function getEmployeeLeaveData_Ajax(jqgrid_data) {
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeeLeaveData",
        data: {partyId: $("#0_lookupId_partyId").val()},
        dataType: "json",
        success: function (data) {
            for (item in data) {
                jqgrid_data.push({
                    leaveId: data[item].leaveId,
                    partyId: data[item].partyId,
                    leaveTypeId: data[item].leaveTypeId,
                    transDate: data[item].transDate,
                    emplLeaveReasonTypeId: data[item].emplLeaveReasonTypeId,
                    date: data[item].date,
                    fromDate: data[item].fromDate,
                    thruDate: data[item].thruDate,
                    approverPartyId: data[item].approverPartyId,
                    leaveStatus: data[item].leaveStatus,
                    description: data[item].description,
                    type: data[item].type

                });

            }
        }
    }); // End of Ajax Request
}

function refresh()
{
    fillEmployeeLeaveData();
}



function add() {


    if ($("#0_lookupId_partyId").val().length == 0) {
        WarningMessage("Please Fill Employee ID!");

        $("#leaveGrid_ilcancel").click();
    }


    $("#leaveGrid").setGridParam({editurl: "createLeave"});

    var partyId_ID = $("input.editable[name='partyId']").attr("id");
    $("#" + partyId_ID).val($("#0_lookupId_partyId").val())

    date_ID = $("input.editable[name='partyId']").attr("id");

    var leaveStatus_ID = $("input.editable[name='leaveStatus']").attr("id");
    $("#" + leaveStatus_ID).prop("disabled", true);
    $("#" + leaveStatus_ID).val("LEAVE_APPROVED");
//    document.getElementById(userLoginId_ID).value = "${userLogin.userLoginId}";


    var approverPartyId_ID = $("input.editable[name='approverPartyId']").attr("id");
//    $("#" + approverPartyId_ID).prop("disabled", true);

    $.ajax({
        'async': false,
        type: "post",
        url: "getEmplPositionReportingTo",
        data: {partyId: $("#0_lookupId_partyId").val()},
//        dataType: "json",
        success: function (data) {
            $("#" + approverPartyId_ID).val(data.trim());
        }
    }); // End of Ajax Request

}

function edit() {
    $("#leaveGrid").setGridParam({editurl: "updateLeave"});
}

function deleteAllownces() {
    $("#leaveGrid").setGridParam({editurl: 'deleteEmplLeaveExt?leaveId=' + leaveId_Delete});
}

function save() {
//    var partyId_ID = $("input.editable[name='partyId']").attr("id");
    $("#" + date_ID).val("1");
    alert(date_ID)
}





    