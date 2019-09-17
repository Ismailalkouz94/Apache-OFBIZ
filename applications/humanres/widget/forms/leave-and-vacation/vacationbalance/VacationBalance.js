$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images
    fillVacationBalanceData();
});

function onChangeValueFromLookUp(lookup_ID) {
    fillVacationBalanceData();
}

$("form[name='LookupEmployeeName']").keypress(function (e) {
    var key = e.charCode || e.keyCode || 0;
    if (key == 13) {
        fillVacationBalanceData();
        setHiredDate();
        e.preventDefault();
    }
});

function setHiredDate() {
    $.ajax({
        'async': false,
        type: "post",
        url: "getHiredDate",
        data: {partyId: $("#0_lookupId_partyId").val()},
        success: function (data) {
            $("#hiredDate").val(data);
        }
    });
}

function fillVacationBalanceData() {
    var jqgrid_data = [];
    fillVacationBalanceData_Ajax(jqgrid_data);

    colNames = ['Type', 'Take It', 'Balance','Remaining'];
    colModel = [
        {
            name: 'type',
            index: 'type',
            editable: true,
            hidden: false
        }, {
            name: 'takeIt',
            index: 'takeIt',
            editable: true,
            editrules: {required: true, number: true},
        }, {
            name: 'balance',
            index: 'balance',
            editable: true,
        }, {
            name: 'remaining',
            index: 'remaining',
            editable: true,
        }];
    getGridData(colNames, colModel, jqgrid_data, "vacationBalance", "vacationBalanceDiv", "Vacation Balance", "id", "desc", 12);
} //End of function

function rowSelected(rowId, gridId) {
}

function firstRowSelected(gridId) {
}

$(document).ready(function () {
});

function fillVacationBalanceData_Ajax(jqgrid_data) {
    $.ajax({
        'async': false,
        type: "post",
        url: "getVacationBalanceData",
        data: {
            partyId: $("#0_lookupId_partyId").val(),
            fromDate: $("#hiredDate").val(),
            toDate: $("#toDate").val()
        },
        dataType: "json",
        success: function (data) {
            for (item in data) {
                jqgrid_data.push({
                    type: data[item].type,
                    takeIt: data[item].takeIt,
                    balance: data[item].balance,
                    remaining: data[item].remaining,
                });
            }
        }
    }); // End of Ajax Request
}
