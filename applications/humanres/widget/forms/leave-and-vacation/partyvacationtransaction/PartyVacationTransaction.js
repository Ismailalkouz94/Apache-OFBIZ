$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images
    fillVacationTransactionData();
    document.getElementById("vacationTransaction_iladd").addEventListener("click", add);
    document.getElementById("vacationTransaction_iledit").addEventListener("click", edit);
    document.getElementById("del_vacationTransaction").addEventListener("click", deleteTransaction);
    document.getElementById("refresh_vacationTransaction").addEventListener("click", refresh);
});
var partyVacationTransactionId = "";

function onChangeValueFromLookUp(lookup_ID) {
    fillVacationTransactionData();
}

$("form[name='LookupEmployeeName']").keypress(function (e) {
    var key = e.charCode || e.keyCode || 0;
    if (key == 13) {
        fillVacationTransactionData();
        e.preventDefault();
    }
});

function fillVacationTransactionData() {
    var jqgrid_data = [];
    fillVacationTransactionData_Ajax(jqgrid_data);

    colNames = ['partyVacationTransactionId', 'partyId', 'No Of Days','Action' , 'Date', 'Description'];
    colModel = [{
        key: false,
        name: 'partyVacationTransactionId',
        index: 'partyVacationTransactionId',
        editable: true,
        hidden: true
    }, {
        name: 'partyId',
        index: 'partyId',
        editable: true,
        hidden: true
    }, {
        name: 'noDays',
        index: 'noDays',
        editable: true,
        editrules: {required: true, number: true},
    }, {
        name: 'action',
        index: 'action',
        editable: true,
        edittype: 'select',
        editoptions: {value: "+:+;-:-"}
    }, {
        name: 'transDate',
        index: 'transDate',
        editable: true,
        datatype: 'date',
        editrules: {required: true},
        editoptions: {size: 10, dataInit: date()},
    }, {
        name: 'description',
        index: 'description',
        editable: true,
    }];
    fillGridData(colNames, colModel, jqgrid_data, "vacationTransaction", "vacationTransactionDiv", "Vacation Transaction Data", "partyVacationTransactionId", "desc", "Transaction", 12);

} //End of function


function rowSelected(rowId, gridId) {
    if (gridId == "vacationTransaction") {
        var rowData = jQuery('#vacationTransaction').getRowData(rowId);
        partyVacationTransactionId = rowData['partyVacationTransactionId'];
    }
}

function firstRowSelected(gridId) {
    if (gridId == "vacationTransaction") {
    }
}

$(document).ready(function () {

    //    Call Success Response Notifications
    jQuery("#vacationTransaction").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });

    jQuery("#vacationTransaction").on("jqGridInlineAfterSaveRow", function () {
        var jqgrid_data = [];
        fillVacationTransactionData_Ajax(jqgrid_data);

        jQuery("#vacationTransaction").jqGrid('clearGridData');
        jQuery("#vacationTransaction").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#vacationTransaction").trigger('reloadGrid');

    });
});

function fillVacationTransactionData_Ajax(jqgrid_data) {
    $.ajax({
        'async': false,
        type: "post",
        url: "getPartyVacationTransactionData",
        data: {partyId: $("#0_lookupId_partyId").val()},
        dataType: "json",
        success: function (data) {
            for (item in data) {
                jqgrid_data.push({
                    partyVacationTransactionId: data[item].partyVacationTransactionId,
                    partyId: data[item].partyId,
                    noDays: data[item].noDays,
                    action: data[item].action,
                    transDate: data[item].transDate,
                    description: data[item].description
                });
            }
        }
    }); // End of Ajax Request
}

function refresh() {
    fillVacationTransactionData();
}

function add() {
    $("#vacationTransaction").setGridParam({editurl: "createPartyVacationTransaction"});

    var partyId_ID = $("input.editable[name='partyId']").attr("id");
    $("#" + partyId_ID).val($("#0_lookupId_partyId").val())
}

function edit() {
    $("#vacationTransaction").setGridParam({editurl: "updatePartyVacationTransaction"});
}

function deleteTransaction() {
    $("#vacationTransaction").setGridParam({editurl: 'deletePartyVacationTransaction?partyVacationTransactionId=' + partyVacationTransactionId});
}

function RefreshGrid(gridID) {
    if (gridID == "vacationContract") {
        fillVacationTransactionData();
    }

}

