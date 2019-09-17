$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images
    fillVacationContractData();
    document.getElementById("vacationContract_iladd").addEventListener("click", add);
    document.getElementById("vacationContract_iledit").addEventListener("click", edit);
    document.getElementById("del_vacationContract").addEventListener("click", deleteReferences);
    document.getElementById("refresh_vacationContract").addEventListener("click", refresh);

});
var partyVacationContractId = "";
var dataIsEmpty = true;

function onChangeValueFromLookUp(lookup_ID) {
    fillVacationContractData();
}

$("form[name='LookupEmployeeName']").keypress(function (e) {
    var key = e.charCode || e.keyCode || 0;
    if (key == 13) {
        fillVacationContractData();
        e.preventDefault();
    }
});

function fillVacationContractData() {
    var jqgrid_data = [];
    fillVacationContractData_Ajax(jqgrid_data);

    colNames = ['ID ', 'partyId', 'From Date', 'To Date', 'Value'];
    colModel = [{
        key: false,
        name: 'partyVacationContractId',
        index: 'partyVacationContractId',
        editable: true,
        hidden: true
    }, {
        name: 'partyId',
        index: 'partyId',
        editable: true,
        hidden: true
    }, {
        name: 'fromDate',
        index: 'fromDate',
        editable: true,
        datatype: 'date',
        editrules: {required: true},
        editoptions: {size: 10, dataInit: date()},
    }, {
        name: 'toDate',
        index: 'toDate',
        editable: true,
        datatype: 'date',
        editrules: {required: true},
        editoptions: {size: 10, dataInit: date()},
        editable:false
    }, {
        name: 'value',
        index: 'value',
        editrules: {required: true},
        editable: true,

    }];
    fillGridData(colNames, colModel, jqgrid_data, "vacationContract", "vacationContractDiv", "Vacation Contract Data", "partyVacationContractId", "desc", "Reference", 12);

} //End of function


function rowSelected(rowId, gridId) {
    if (gridId == "vacationContract") {
        var rowData = jQuery('#vacationContract').getRowData(rowId);
        partyVacationContractId = rowData['partyVacationContractId'];
    }
}

function firstRowSelected(gridId) {
    if (gridId == "vacationContract") {
    }
}

$(document).ready(function () {

    //    Call Success Response Notifications
    jQuery("#vacationContract").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });

    jQuery("#vacationContract").on("jqGridInlineAfterSaveRow", function () {
        var jqgrid_data = [];
        fillVacationContractData_Ajax(jqgrid_data);

        jQuery("#vacationContract").jqGrid('clearGridData');
        jQuery("#vacationContract").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#vacationContract").trigger('reloadGrid');

    });
});

function fillVacationContractData_Ajax(jqgrid_data) {
    $.ajax({
        'async': false,
        type: "post",
        url: "getPartyVacationContractData",
        data: {partyId: $("#0_lookupId_partyId").val()},
        dataType: "json",
        success: function (data) {
            if (data.length > 0) {
                dataIsEmpty = false;
            } else {
                dataIsEmpty = true;
            }
            for (item in data) {
                jqgrid_data.push({
                    partyVacationContractId: data[item].partyVacationContractId,
                    partyId: data[item].partyId,
                    fromDate: data[item].fromDate,
                    startDate: data[item].startDate,
                    toDate: data[item].toDate,
                    value: data[item].value,
                });
            }
            if(data.length == 1){
                $("#vacationContract_iladd").hide();
            }else{
                $("#vacationContract_iladd").show();
            }
        }
    }); // End of Ajax Request
}

function refresh() {
    fillVacationContractData();
}

function add() {
    $("#vacationContract").setGridParam({editurl: "createPartyVacationContract"});

    var partyId_ID = $("input.editable[name='partyId']").attr("id");
    $("#" + partyId_ID).val($("#0_lookupId_partyId").val())

    if (dataIsEmpty) {

        var fromDate_ID = $("input.editable[name='fromDate']").attr("id");
        $("#" + fromDate_ID).prop('disabled', true);

        $.ajax({
            'async': false,
            type: "post",
            url: "getHiredDate",
            data: {partyId: $("#0_lookupId_partyId").val()},
            success: function (data) {
                $("#" + fromDate_ID).val(data);
            }
        });
    }

}

function edit() {
    $("#vacationContract").setGridParam({editurl: "updatePartyVacationContract"});
}

function deleteReferences() {
    $("#vacationContract").setGridParam({editurl: 'deletePartyVacationContract?partyVacationContractId=' + partyVacationContractId});
}

function RefreshGrid(gridID) {
    if (gridID == "vacationContract") {
        fillVacationContractData();
    }

}

