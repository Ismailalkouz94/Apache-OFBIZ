$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images
    fillSequenceData();

    document.getElementById("sequenceGrid_iladd").addEventListener("click", add);
    document.getElementById("refresh_sequenceGrid").addEventListener("click", refresh);
    document.getElementById("sequenceGrid_ilsave").addEventListener("click", refresh);

});


function onChangeValueFromLookUp(lookup_ID) {

    fillSequenceData();
}

//
//$("form[name='LookupEmployeeName']").keypress(function (e) {
//    var key = e.charCode || e.keyCode || 0;
//    if (key == 13) {
//        fillSequenceData();
//        e.preventDefault();
//    }
//});
//
//var leaveId_Delete = "";


function fillSequenceData() {
    var jqgrid_data = [];
    getSequenceData_Ajax(jqgrid_data);

    colNames = ['Year', 'Acctg Trans', 'Acctg Trans Code', 'Invoice AP', 'Invoice AR', 'Payment AP', 'Payment AR'];
    colModel = [{
//            key: true,
            name: 'year',
            index: 'year',
            editrules: {required: true ,number: true},
            edittype: 'select',
            editoptions: {dataUrl: 'getCustomTimePeriodAcc_DropDown'},
            editable: true
        }, {
            name: 'AcctgTrans',
            index: 'AcctgTrans',
            editrules: {required: true ,number: true},
            editable: true
        }, {
            name: 'AcctgTransCode',
            index: 'AcctgTransCode',
            editrules: {required: true ,number: true},
            editable: true
        }, {
            name: 'Invoice_AP',
            index: 'Invoice_AP',
            editrules: {required: true ,number: true},
            editable: true
        }, {
            name: 'Invoice_AR',
            index: 'Invoice_AR',
            editrules: {required: true ,number: true},
            editable: true
        }, {
            name: 'Payment_AP',
            index: 'Payment_AP',
            editrules: {required: true ,number: true},
            editable: true
        }, {
            name: 'Payment_AR',
            index: 'Payment_AR',
            editrules: {required: true ,number: true},
            editable: true
        }];
//    fillGridData(colNames, colModel, jqgrid_data, "leaveGrid", "leaveGridDiv", "Employee Leave", "refId", "asc", "Employee Leave", 15);
    addGridData_create(colNames, colModel, jqgrid_data, "sequenceGrid", "sequenceGridDiv", "Sequence Step", "year", "asc", "step", 10);
} //End of function fillInsuranceData 



function rowSelected(rowId, gridId) {
}

function firstRowSelected(gridId) {
    if (gridId == "leaveGrid") {
    }
}
$(document).ready(function () {

    //    Call Success Response Notifications
    jQuery("#sequenceGrid").on("jqGridInlineSuccessSaveRow", function (response, request) {
        console.log(request.responseJSON);

        if ('_ERROR_MESSAGE_' in request.responseJSON) {
            ErrorMessage(request.responseJSON._ERROR_MESSAGE_);
        } else if ('_ERROR_MESSAGE_LIST_' in request.responseJSON) {
            ErrorMessage(request.responseJSON._ERROR_MESSAGE_LIST_);
        } else if ('_EVENT_MESSAGE_' in request.responseJSON) {
            for (item in request.responseJSON._EVENT_MESSAGE_) {
                console.log(request.responseJSON._EVENT_MESSAGE_[item].successMessage);
                SuccessMessage(request.responseJSON._EVENT_MESSAGE_[item].seqName + " " + request.responseJSON._EVENT_MESSAGE_[item].successMessage);
            }
            return [true, ""];
        }
//        getNotificationSuccess(response, request);

    });

    jQuery("#sequenceGrid").on("jqGridInlineAfterSaveRow", function () {
        var jqgrid_data = [];
        getSequenceData_Ajax(jqgrid_data);

        jQuery("#sequenceGrid").jqGrid('clearGridData');
        jQuery("#sequenceGrid").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#sequenceGrid").trigger('reloadGrid');

    });
});

function getSequenceData_Ajax(jqgrid_data) {
//    getSequenceData
    $.ajax({
        'async': false,
        type: "post",
        url: "getSequenceData",
        data: {},
        dataType: "json",
        success: function (data) {
            for (item in data) {
                jqgrid_data.push({
                    year: data[item].year,
                    AcctgTrans: data[item].AcctgTrans,
                    AcctgTransCode: data[item].AcctgTransCode,
                    Invoice_AP: data[item].Invoice_AP,
                    Invoice_AR: data[item].Invoice_AR,
                    Payment_AP: data[item].Payment_AP,
                    Payment_AR: data[item].Payment_AR
                });
            }
        }
    }); // End of Ajax Request
//    $.ajax({
//        'async': false,
//        type: "post",
//        url: "getCustomTimePeriodAcc_DropDown",
//        data: {},
//        dataType: "json",
//        success: function (data) {
//
//        }
//    }); // End of Ajax Request
}

function refresh()
{
    fillSequenceData();
}




function add() {

    $("#sequenceGrid").setGridParam({editurl: "createSequenceValueItemAcc"});


//    var partyId_ID = $("input.editable[name='partyId']").attr("id");
//    $("#" + partyId_ID).val($("#0_lookupId_partyId").val())


//    var leaveStatus_ID = $("input.editable[name='leaveStatus']").attr("id");
//    $("#" + leaveStatus_ID).prop("disabled", true);
//    $("#" + leaveStatus_ID).val("LEAVE_CREATED");
//    document.getElementById(userLoginId_ID).value = "${userLogin.userLoginId}";


//    var approverPartyId_ID = $("input.editable[name='approverPartyId']").attr("id");
//    $("#" + approverPartyId_ID).prop("disabled", true);

//    $.ajax({
//        'async': false,
//        type: "post",
//        url: "getEmplPositionReportingTo",
//        data: {partyId: $("#0_lookupId_partyId").val()},
////        dataType: "json",
//        success: function (data) {
//            $("#" + approverPartyId_ID).val(data.trim());
//        }
//    }); // End of Ajax Request

}







    