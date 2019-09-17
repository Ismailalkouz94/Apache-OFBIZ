$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images
    fillEmpVacationData();
    fillEmplVacationExtData();
    fillEmplVacationExtFMData();
    document.getElementById("vacationGrid_iledit").addEventListener("click", editEmpVacation);
    document.getElementById("del_vacationGrid").addEventListener("click", deleteEmpVacation);
    document.getElementById("refresh_vacationGrid").addEventListener("click", refreshEmpVacation);


    document.getElementById("vacationExtGrid_iledit").addEventListener("click", editEmplVacationExt);
    document.getElementById("refresh_vacationExtGrid").addEventListener("click", refreshEmplVacationExt);


    document.getElementById("vacationExtFMGrid_iledit").addEventListener("click", editEmplVacationExtFM);
    document.getElementById("del_vacationExtFMGrid").addEventListener("click", deleteEmplVacationExtFM);
    document.getElementById("refresh_vacationExtFMGrid").addEventListener("click", refreshEmplVacationExtFM);

    $("#vacationGrid_iladd").hide();
    $("#vacationExtGrid_iladd").hide();
    $("#vacationExtFMGrid_iladd").hide();
    $("#del_vacationExtGrid").hide();

});


function onChangeValueFromLookUp(lookup_ID) {

    fillEmpVacationData();
    fillEmplVacationExtData();
    fillEmplVacationExtFMData();

}


$("form[name='LookupEmployeeName']").keypress(function (e) {
    var key = e.charCode || e.keyCode || 0;
    if (key == 13) {
        fillEmpVacationData();
        fillEmplVacationExtData();
        fillEmplVacationExtFMData();
        e.preventDefault();
    }
});

var leaveId = "";
var EmplLeaveExtId = "";
var EmplLeaveExtFMTicketId_Delete = "";


function fillEmpVacationData() {
    var jqgrid_data = [];
    getEmpVacationData_Ajax(jqgrid_data);

    colNames = ['ID', 'Party Id', 'leave Type Id', 'Leave Reason Type', 'Date Of Application', 'From Date', 'Thru Date', 'Approver Party Id', 'Leave Status', 'Note   ', 'type'];
    colModel = [{
            key: true,
            name: 'leaveId',
            index: 'leaveId',
            editable: false,
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
//            edittype: 'select',
//            editoptions: {dataUrl: 'getLeaveTypeId_DropDown'},
            editable: true,
            hidden: true
        }, {
            name: 'emplLeaveReasonTypeId',
            index: 'emplLeaveReasonTypeId',
            // edittype: 'select',
            // editoptions: {dataUrl: 'getEmplLeaveReasonTypeId_DropDown'},
            editable: true
        }, {
            name: 'transDate',
            index: 'transDate',
            editoptions: {value: getCurrentDate()},
            editable: true
        }, {
            name: 'fromDate',
            index: 'fromDate',
            editrules: {required: true, datetime: true},
            editoptions: {size: 10, dataInit: date()},
            editable: true
        }, {
            name: 'thruDate',
            index: 'thruDate',
            editrules: {required: true, datetime: true},
            editoptions: {size: 10, dataInit: date()},
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
            editoptions: {value: "VACATION"},
            editable: true,
            hidden: true
        }];
    fillGridData(colNames, colModel, jqgrid_data, "vacationGrid", "vacationGridDiv", "Employee Vacations", "refId", "asc", " ", 15);
} //End of function fillInsuranceData 




$(document).ready(function () {

    //    Call Success Response Notifications
    jQuery("#vacationGrid").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });

    jQuery("#vacationGrid").on("jqGridInlineAfterSaveRow", function () {
        var jqgrid_data = [];
        getEmpVacationData_Ajax(jqgrid_data);

        jQuery("#vacationGrid").jqGrid('clearGridData');
        jQuery("#vacationGrid").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#vacationGrid").trigger('reloadGrid');

    });
});

function getEmpVacationData_Ajax(jqgrid_data) {
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeeVacationData",
        data: {partyId: $("#0_lookupId_partyId").val()},
        dataType: "json",
        success: function (data) {
            for (item in data) {
                jqgrid_data.push({
                    leaveId: data[item].leaveId,
                    partyId: data[item].partyId,
                    leaveTypeId: data[item].leaveTypeId,
                    emplLeaveReasonTypeId: data[item].emplLeaveReasonTypeId,
                    transDate: data[item].transDate,
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

function refreshEmpVacation()
{
    fillEmpVacationData();
}

function editEmpVacation() {

    $("#vacationGrid").setGridParam({editurl: "updateVacation"});
    var emplLeaveReasonTypeId_ID = $("input.editable[name='emplLeaveReasonTypeId']").attr("id");
    $("#" + emplLeaveReasonTypeId_ID).prop("disabled", true);

}

function deleteEmpVacation() {

    $("#vacationGrid").setGridParam({editurl: 'deleteVacation?leaveId=' + leaveId});

}


//   ............... Shared Function ...........


function rowSelected(rowId, gridId) {
    if (gridId == "vacationGrid") {
        var rowData = jQuery('#vacationGrid').getRowData(rowId);
        leaveId = rowData['leaveId'];

//        if (rowData['leaveStatus'] != "LEAVE_CREATED") {
//            $("#del_vacationGrid").hide();
//        }

        fillEmplVacationExtData(leaveId);
        fillEmplVacationExtFMData(leaveId);

    }
    if (gridId == "vacationExtFMGrid") {
         var rowData = jQuery('#vacationExtFMGrid').getRowData(rowId);
        EmplLeaveExtFMTicketId_Delete = rowData['EmplLeaveExtFMTicketId'];
    }


}

function firstRowSelected(gridId) {
    if (gridId == "leaveGrid") {
    }
}

//        ............. Second Grid ...............

function fillEmplVacationExtData(leaveId) {
    var jqgrid_data = [];
    getEmplVacationExtData_Ajax(leaveId, jqgrid_data);

    colNames = ['ID', 'Route From', 'Route To', 'No Of Days', 'Ticket Required', 'Ticket Charge To', 'Visa Required', 'Visa Charge To'];
    colModel = [{
            key: true,
            name: 'EmplLeaveExtId',
            index: 'EmplLeaveExtId',
            editable: false,
            hidden: true

        }, {
            name: 'routeFrom',
            index: 'routeFrom',
//            edittype: 'select',
//            editrules: {required: true},
//            editoptions: {dataUrl: 'getRelatedToIdDropDown'},
            hidden: false,
            editable: true,

        }, {
            name: 'routeTo',
            index: 'routeTo',
            editable: true,

        }, {
            name: 'noOfDays',
            index: 'noOfDays',

            editable: true
        }, {
            name: 'ticketRequired',
            index: 'ticketRequired',
            editable: true,
            edittype: 'select',
            editoptions: {value: "Y:YES;N:NO"}

        }, {
            name: 'ticketChargeTo',
            index: 'ticketChargeTo',

            editable: true,
            edittype: 'select',
            editoptions: {value: "C:Company;E:Employee"}
        }, {
            name: 'visaRequired',
            index: 'visaRequired',
            editable: true,
            edittype: 'select',
            editoptions: {value: "Y:YES;N:NO"},
        }, {
            name: 'visaChargeTo',
            index: 'visaChargeTo',
            editable: true,
            edittype: 'select',
            editoptions: {value: "C:Company;E:Employee"}
        }];
    fillGridData(colNames, colModel, jqgrid_data, "vacationExtGrid", "vacationExtGridDiv", "Employee Vacation Extenstion", "refId", "asc", "Employee Leave", 15);
} //End of function fillInsuranceData 


$(document).ready(function () {

    //    Call Success Response Notifications
    jQuery("#vacationExtGrid").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });

    jQuery("#vacationExtGrid").on("jqGridInlineAfterSaveRow", function () {
        var jqgrid_data = [];
        getEmplVacationExtData_Ajax(leaveId, jqgrid_data);

        jQuery("#vacationExtGrid").jqGrid('clearGridData');
        jQuery("#vacationExtGrid").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#vacationExtGrid").trigger('reloadGrid');

    });
});

function getEmplVacationExtData_Ajax(leaveId, jqgrid_data) {
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeeVacationExtData",
        data: {leaveId: leaveId},
        dataType: "json",
        success: function (data) {
            for (item in data) {
                jqgrid_data.push({
                    EmplLeaveExtId: data[item].EmplLeaveExtId,
                    routeFrom: data[item].routeFrom,
                    routeTo: data[item].routeTo,
                    noOfDays: data[item].noOfDays,
                    ticketRequired: data[item].ticketRequired,
                    ticketChargeTo: data[item].ticketChargeTo,
                    visaRequired: data[item].visaRequired,
                    visaChargeTo: data[item].visaChargeTo,

                });

            }
        }
    }); // End of Ajax Request
}

function refreshEmplVacationExt() {
    fillEmplVacationExtData(leaveId);
}


function editEmplVacationExt() {
    $("#vacationExtGrid").setGridParam({editurl: "updateEmplLeaveExtention"});
}



//        ............. Third Grid ...............

function fillEmplVacationExtFMData(leaveId) {
    var jqgrid_data = [];
    getEmplVacationExtFMData_Ajax(leaveId, jqgrid_data);

    colNames = ['ID', 'Party Id', 'D.O.B', 'Departure Date', 'Return Date'];
    colModel = [{
            key: true,
            name: 'EmplLeaveExtFMTicketId',
            index: 'EmplLeaveExtFMTicketId',
            editable: true,
            hidden: true

        }, {
            name: 'partyId',
            index: 'partyId',
//            edittype: 'select',
//            editrules: {required: true},
//            editoptions: {dataUrl: 'getRelatedToIdDropDown'},
            hidden: false,
            editable: true,

        }, {
            name: 'DOB',
            index: 'DOB',
            editrules: {required: true, datetime: true},
            editoptions: {size: 10, dataInit: dateTime()},
            editable: true
        }, {
            name: 'departureDate',
            index: 'departureDate',
            editrules: {required: true, datetime: true},
            editoptions: {size: 10, dataInit: dateTime()},
            editable: true
        }, {
            name: 'returnDate',
            index: 'returnDate',
            editrules: {required: true, datetime: true},
            editoptions: {size: 10, dataInit: dateTime()},
            editable: true
        }];
    fillGridData(colNames, colModel, jqgrid_data, "vacationExtFMGrid", "vacationExtFMGridDiv", "Ticket And Family Memeber Ticket", "refId", "asc", " ", 15);
} //End of function fillInsuranceData 


$(document).ready(function () {

    //    Call Success Response Notifications
    jQuery("#vacationExtFMGrid").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });

    jQuery("#vacationExtFMGrid").on("jqGridInlineAfterSaveRow", function () {
        var jqgrid_data = [];
        getEmplVacationExtFMData_Ajax(leaveId, jqgrid_data);

        jQuery("#vacationExtFMGrid").jqGrid('clearGridData');
        jQuery("#vacationExtFMGrid").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#vacationExtFMGrid").trigger('reloadGrid');

    });
});

function getEmplVacationExtFMData_Ajax(leaveId, jqgrid_data) {
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeeVacationExtFMData",
        data: {leaveId: leaveId},
        dataType: "json",
        success: function (data) {
            for (item in data) {
                jqgrid_data.push({
                    EmplLeaveExtFMTicketId: data[item].EmplLeaveExtFMTicketId,
                    partyId: data[item].partyId,
                    DOB: data[item].DOB,
                    departureDate: data[item].departureDate,
                    returnDate: data[item].returnDate,

                });

            }
        }
    }); // End of Ajax Request
}

function refreshEmplVacationExtFM() {
    fillEmplVacationExtFMData(leaveId);
}


function editEmplVacationExtFM() {
    $("#vacationExtFMGrid").setGridParam({editurl: "updateEmplLeaveExtFMTicket"});
}

function deleteEmplVacationExtFM() {
    $("#vacationExtFMGrid").setGridParam({editurl: 'deleteEmplLeaveExtFMTicket?EmplLeaveExtFMTicketId=' + EmplLeaveExtFMTicketId_Delete});
}
