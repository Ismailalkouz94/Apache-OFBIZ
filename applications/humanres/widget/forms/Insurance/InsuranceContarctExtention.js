

$(window).load(function () {
    fillInsuranceContractExtention();
    document.getElementById("jqgrid_iladd").addEventListener("click", add);
    document.getElementById("jqgrid_iledit").addEventListener("click", edit);
    document.getElementById("refresh_jqgrid").addEventListener("click", refresh);
    document.getElementById("del_jqgrid").addEventListener("click", deleteInsuranceContractExt);


    var contractId = dropdown.options[dropdown.selectedIndex].id;
    var endDate = "";

    $.ajax({
        type: "post",
        url: "getInsuranceContarctEndDate",
        data: "ContractId=" + contractId,
        success: function (data) {
            endDate = data;
            if (getCurrentDate() > endDate) {
                document.getElementById("jqgrid_iladd").hidden = true;
                document.getElementById("jqgrid_iledit").hidden = true;
                document.getElementById("del_jqgrid").hidden = true;
                document.getElementById("jqgrid_ilcancel").hidden = true;
                document.getElementById("jqgrid_ilsave").hidden = true;


            } else {
                document.getElementById("jqgrid_iladd").hidden = false;
                document.getElementById("jqgrid_iledit").hidden = false;
                document.getElementById("del_jqgrid").hidden = false;
                document.getElementById("jqgrid_ilcancel").hidden = false;
                document.getElementById("jqgrid_ilsave").hidden = false;
            }


        }

    });
});


$.ajax({
    "async": false,
    type: "post",
    url: "getCompanyInsuranceContract",
    dataType: "json",
    success: function (data) {
        for (item in data) {
            $("#Ultra").append('<option  id="' + data[item].comInsContId + '">' + ' [' + data[item].insListId + ' [' + data[item].startDate + ' - ' + data[item].endDate + '</option>');
        }
    }
});//End Of Ajax Request



var dropdown = document.getElementById('Ultra');

var contractId = dropdown.options[dropdown.selectedIndex].id;

var InsuranceContractExt_Delete = "";




document.getElementById("Ultra").addEventListener("change", function () {
    fillInsuranceContractExtention();

});

function fillInsuranceContractExtention() {
    var contractId = dropdown.options[dropdown.selectedIndex].id;
    var jqgrid_data = [];
    $.ajax({
        'async': false,
        type: "post",
        url: "getInsuranceContractExtention",
        data: "comInsContId=" + contractId,
        dataType: "json",
        success: function (data) {
            for (item in data) {

                jqgrid_data.push({
                    insContExtId: data[item].insContExtId,
                    comInsContId: data[item].comInsContId,
                    actionId: data[item].actionId,
                    Amount: data[item].Amount,
                    currentDate: data[item].currentDate,
                    note: data[item].note,

                });

            }


        }

    });//End Of Ajax Request


    colNames = ['ID', 'Insurance Contract ID', 'Action', 'Amount', 'Date', 'Note', 'actionId_hidden']
    colModel = [{
            key: true,
            name: 'insContExtId',
            index: 'insContExtId',
            editable: false,
            hidden: true

        }, {
            name: 'comInsContId',
            index: 'comInsContId',
            editoptions: {defaultValue: contractId},
            editable: true,
            hidden: true

        }, {
            name: 'actionId',
            index: 'actionId',
            edittype: 'select',
            editoptions: {dataUrl: 'getInsuranceClassActionDropDown'},
            editable: true
        }, {
            name: 'Amount',
            index: 'Amount',
            editrules: {required: true},
            editable: true


        }, {
            name: 'currentDate',
            index: 'currentDate',
            editrules: {required: true},
            editoptions: {size: 10, dataInit: date()},
            editable: true


        }, {
            name: 'note',
            index: 'note',
            editable: true

        }, {
            name: 'actionId_hidden',
            index: 'actionId_hidden',
            editable: true,
            hidden: true


        }, ]


    fillGridData(colNames, colModel, jqgrid_data, "jqgrid", "jqgridDiv", "Insurance Contract Extention", "insContExtId", "asc", "Contract Extention");



    var contractId = dropdown.options[dropdown.selectedIndex].id;
    var endDate = "";

    $.ajax({
        type: "post",
        url: "getInsuranceContarctEndDate",
        data: "ContractId=" + contractId,
        success: function (data) {
            endDate = data;
            if (getCurrentDate() > endDate) {
                document.getElementById("jqgrid_iladd").hidden = true;
                document.getElementById("jqgrid_iledit").hidden = true;
                document.getElementById("del_jqgrid").hidden = true;
                document.getElementById("jqgrid_ilcancel").hidden = true;
                document.getElementById("jqgrid_ilsave").hidden = true;


            } else {
                document.getElementById("jqgrid_iladd").hidden = false;
                document.getElementById("jqgrid_iledit").hidden = false;
                document.getElementById("del_jqgrid").hidden = false;
                document.getElementById("jqgrid_ilcancel").hidden = false;
                document.getElementById("jqgrid_ilsave").hidden = false;
            }


        }

    });




} // End Function FillInsuraceContractEct...

//    ........ For reload grid data

//     .......



function rowSelected(rowId, gridId) {
    if (gridId == "jqgrid") {
        var rowData = jQuery('#jqgrid').getRowData(rowId);
        InsuranceContractExt_Delete = rowData['insContExtId'];
    }
}
function firstRowSelected(gridId) {
    if (gridId == "jqgrid") {
    }
}



$(document).ready(function () {
    // executes when HTML-Document is loaded and DOM is ready

    //    Call Success Response Notifications
    jQuery("#jqgrid").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });


    jQuery("#jqgrid").on("jqGridInlineAfterSaveRow", function () {
        var contractId = dropdown.options[dropdown.selectedIndex].id;
        var jqgrid_data = [];
        $.ajax({
            'async': false,
            type: "post",
            url: "getInsuranceContractExtention",
            data: "comInsContId=" + contractId,
            dataType: "json",
            success: function (data) {
                for (item in data) {

                    jqgrid_data.push({
                        insContExtId: data[item].insContExtId,
                        comInsContId: data[item].comInsContId,
                        actionId: data[item].actionId,
                        Amount: data[item].Amount,
                        currentDate: data[item].currentDate,
                        note: data[item].note,

                    });

                }


            }

        });//End Of Ajax Request


        jQuery("#jqgrid").jqGrid('clearGridData');
        jQuery("#jqgrid").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#jqgrid").trigger('reloadGrid');

    });

});





function refresh()
{
    fillInsuranceContractExtention();
}


function add() {

    var currentDate_ID = $("input.editable[name='currentDate']").attr("id");
    document.getElementById(currentDate_ID).value = getCurrentDate();

    $("#jqgrid").setGridParam({editurl: "createInsuranceContractExt"});


    var contractId = dropdown.options[dropdown.selectedIndex].id;
    var ID = $("input.editable[name='comInsContId']").attr("id");
    document.getElementById(ID).value = contractId;



//    $.ajax({
//        type: "post",
//        url: "getInsuranceClassActionDropDown",
//        data: "",
//        success: function (data) {
//              var ID=$("select[name='actionId']").attr("id");
//              document.getElementById(ID).innerHTML= data.trim();
//
//        }
//    });   


}


function edit() {


    $("#jqgrid").setGridParam({editurl: "updateInsuranceContractExt"});

    //To convert date from "Jun 20, 2017" to 2017-6-20
    // for current date field       
//       var currentdate=document.getElementById("jqgrid").querySelector('[class*="ui-state-highlight"]').cells[5].childNodes[0].value;
//       var date = new Date(currentdate)
//       var str = date.getFullYear() + '-' +(date.getMonth()+1) + '-' + date.getDate()
//      document.getElementById(document.getElementById("jqgrid").querySelector('[class*="ui-state-highlight"]').cells[4].childNodes[0].id).value=str;


    var contractId = dropdown.options[dropdown.selectedIndex].id;
    var ID = $("input.editable[name='comInsContId']").attr("id");
    document.getElementById(ID).value = contractId;

//     $.ajax({
//        type: "post",
//        url: "getInsuranceClassActionDropDown",
//        data: "",
//        success: function (data) {
//              var ID=$("select[name='actionId']").attr("id");
//              document.getElementById(ID).innerHTML= data.trim();
//                  
//            var actionId_hidden_ID= $("input[name='actionId_hidden']").attr("id");
//            document.getElementById(ID).value=document.getElementById(actionId_hidden_ID).value;
//
//        }
//    });       


}

function deleteInsuranceContractExt() {

    $("#jqgrid").setGridParam({editurl: 'deleteInsuranceContractExt?insContExtId=' + InsuranceContractExt_Delete});

}

function RefreshGrid(gridID) {
    if (gridID == "jqgrid") {
        fillInsuranceContractExtention();
    }

}


       