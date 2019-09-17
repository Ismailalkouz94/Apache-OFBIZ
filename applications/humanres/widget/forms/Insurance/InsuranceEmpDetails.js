$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images
    fillInsuranceEmployeeDetails()
    fillInsuranceEmployeeFMDetails("partyId_param");

    document.getElementById("jqgrid1_iladd").addEventListener("click", addEmployeeFMDetails);
    document.getElementById("jqgrid1_iledit").addEventListener("click", editEmployeeFMDetails);
//    document.getElementById("jqgrid1_ilsave").addEventListener("click", saveForSecondGrid);
    document.getElementById("refresh_jqgrid1").addEventListener("click", refreshEmployeeFMDetails);
    document.getElementById("del_jqgrid1").addEventListener("click", deleteEmployeeFMDetails);
//    document.getElementById("jqgrid1_ilcancel").addEventListener("click",cancel);    


    var contractId = dropdown1.options[dropdown1.selectedIndex].id;
    var endDate = "";

    $.ajax({
        type: "post",
        url: "getInsuranceContarctEndDate",
        data: "ContractId=" + contractId,
        success: function (data) {
            endDate = data;
            if (getCurrentDate() > endDate) {
                document.getElementById("jqgrid1_iladd").hidden = true;
                document.getElementById("jqgrid1_iledit").hidden = true;
                document.getElementById("del_jqgrid1").hidden = true;
                document.getElementById("jqgrid1_ilcancel").hidden = true;
                document.getElementById("jqgrid1_ilsave").hidden = true;


            } else {
                document.getElementById("jqgrid1_iladd").hidden = false;
                document.getElementById("jqgrid1_iledit").hidden = false;
                document.getElementById("del_jqgrid1").hidden = false;
                document.getElementById("jqgrid1_ilcancel").hidden = false;
                document.getElementById("jqgrid1_ilsave").hidden = false;
            }


        }

    });

//        ........

 fillInsuranceEmployeeDetails();
    document.getElementById("jqgrid_iladd").addEventListener("click", Add);
    document.getElementById("jqgrid_iledit").addEventListener("click", edit);
    document.getElementById("del_jqgrid").addEventListener("click", deleteInsuranceEmpDetails);
    document.getElementById("refresh_jqgrid").addEventListener("click", refresh);
    var contractId = dropdown1.options[dropdown1.selectedIndex].id;
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



var InsuranceEmpDetailsId_Delete = "";
var InsuranceEmpFmDetailsId_Delete = "";
var partyId_Parameter = "";
var insuranceclassId_update = "";
var partyIdValue = "";
var PartyID_Caption = "";
var dropdown1 = document.getElementById('Ultra');
var contractId = "";




document.getElementById("Ultra").addEventListener("change", function () {
    fillInsuranceEmployeeDetails();

    fillInsuranceEmployeeFMDetails("partyId_param", "contractId");


});




function fillInsuranceEmployeeDetails() {

    contractId = dropdown1.options[dropdown1.selectedIndex].id;

    var jqgrid_data = [];
    $.ajax({
        'async': false,
        type: "post",
        url: "getInsuranceEmpDetails",
        data: "comInsContId=" + contractId,
        dataType: "json",
        success: function (data) {
            for (item in data) {

                jqgrid_data.push({
                    InsuranceEmpDetailsId: data[item].InsuranceEmpDetailsId,
                    partyId: data[item].partyId,
                    partyName: data[item].partyName,
                    comInsContId: data[item].comInsContId,
                    memberNumber: data[item].memberNumber,
                    InsClass: data[item].InsClass,
                    startDate: data[item].startDate,
                    endDate: data[item].endDate,
                    partyId_hidden: data[item].partyId,
                    comInsContId_hidden: data[item].comInsContId,
                    InsClass_hidden: data[item].InsClass
                });

            }



        }

    }); //End Ajax Request


    colNames = ['ID', 'Party ID', 'Party Name', 'Contarct ID', 'Member Number', 'Insurance Class', 'Start Date', 'End Date', 'partyId_hidden', 'comInsContId_hidden', 'InsClass_hidden'];
    colModel = [
        //                                 
        {
            key: true,
            name: 'InsuranceEmpDetailsId',
            index: 'InsuranceEmpDetailsId',
            editable: false,
            hidden: true

        }, {
            name: 'partyId',
            index: 'partyId',
            edittype: 'select',
            editoptions: {dataUrl: 'getEmployeesDropDown'},
            editable: true

        }, {
            name: 'partyName',
            index: 'partyName',
            editable: false

        }, {
            name: 'comInsContId',
            index: 'comInsContId',

            editable: true,
            hidden: true,

        }, {
            name: 'memberNumber',
            index: 'memberNumber',
            editrules: {required: true},
            editable: true


        }, {
            name: 'InsClass',
            index: 'InsClass',
            edittype: 'select',
            editoptions: {dataUrl: 'getInsuranceClassTypeDropDown'},

            editable: true


        }, {
            name: 'startDate',
            index: 'startDate',
            editrules: {required: true},
            editoptions: {size: 10, dataInit: date()},
            editable: true


        }, {
            name: 'endDate',
            index: 'endDate',
            editrules: {required: true},
            editoptions: {size: 10, dataInit: date()},
            editable: true

        }, {
            name: 'partyId_hidden',
            index: 'partyId_hidden',
            editable: true,
            hidden: true

        }, {
            name: 'comInsContId_hidden',
            index: 'comInsContId_hidden',
            editable: true,
            hidden: true

        }, {
            name: 'InsClass_hidden',
            index: 'InsClass_hidden',
            editable: true,
            hidden: true

        }];



    fillGridData(colNames, colModel, jqgrid_data, "jqgrid", "jqgridDiv", "Insurance Employee Details", "InsuranceEmpDetailsId", "asc", "Insurance Employee Details");





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
}// End Function  fillInsuranceEmployeeDetails

//    ........ For reload grid data


//     .......

function rowSelected(rowId, gridId) {
    if (gridId == "jqgrid") {
        var rowData = jQuery('#jqgrid').getRowData(rowId);
        partyId_Parameter = rowData['partyId'];
        InsuranceEmpDetailsId_Delete = rowData['InsuranceEmpDetailsId'];
        insuranceclassId_update = rowData['InsClass_hidden'];

        PartyID_Caption = rowData['partyId'];

        if ($('select[role="select"]').attr("class") == "editable") {
            PartyID_Caption = "";
        }
        fillInsuranceEmployeeFMDetails(partyId_Parameter);

    }

    if (gridId == "jqgrid1") {
        var rowData = jQuery('#jqgrid1').getRowData(rowId);
        InsuranceEmpFmDetailsId_Delete = rowData['InsuranceEmpFmDetailsId'];

    }
}
function firstRowSelected(gridId) {
    if (gridId == "jqgrid") {
    }
}



function Add() {


    $("#jqgrid").setGridParam({editurl: "createInsuranceEmpDetails"});


    var startDate_ID = $("input.editable[name='startDate']").attr("id");
    document.getElementById(startDate_ID).value = getCurrentDate();


    var contractId_ID = $("input.editable[name='comInsContId']").attr("id");
    document.getElementById(contractId_ID).value = contractId;

    var endDate_ID = $("input.editable[name='endDate']").attr("id");
    document.getElementById(endDate_ID).disabled = true;

    $.ajax({
        type: "post",
        url: "getInsuranceContarctEndDate",
        data: "ContractId=" + contractId,
        success: function (data) {
            var ID = $("input.editable[name='endDate']").attr("id");
            document.getElementById(ID).value = data.trim();

        }

    });


}


function edit() {

    $("#jqgrid").setGridParam({editurl: "updateInsuranceEmpDetails"});
}


function refresh()
{
    fillInsuranceEmployeeDetails();
}

function save() {

    refreshGrid();
}


function deleteInsuranceEmpDetails() {

    $("#jqgrid").setGridParam({editurl: 'deleteInsuranceEmpDetails?InsuranceEmpDetailsId=' + InsuranceEmpDetailsId_Delete});

}

function RefreshGrid(gridID) {
    if (gridID == "jqgrid") {
        fillInsuranceEmployeeDetails()
    }
    if (gridID == "jqgrid1") {
        fillInsuranceEmployeeFMDetails(partyId_Parameter);
    }
}



function fillInsuranceEmployeeFMDetails(partyId_param) {
    var contid = dropdown1.options[dropdown1.selectedIndex].id
    var jqgrid_data = [];
    $.ajax({
        'async': false,
        type: "post",
        url: "getInsuranceEmpFmDetails",
        data: {partyId_param: partyId_param, contractId: contid},
        dataType: "json",
        success: function (data) {
            for (item in data) {
                jqgrid_data.push({
                    InsuranceEmpFmDetailsId: data[item].InsuranceEmpDetailsId,
                    partyId: data[item].partyId,
                    partyName: data[item].partyName,
                    relatedTo: data[item].relatedTo,
                    memberNumber: data[item].memberNumber,
                    partyId_hidden: data[item].partyId,

                });

            }

        }
    }); //End ajax Request


    colNames = ['ID', 'Party ID', 'Party Name', 'relatedTo', 'Member Number', 'partyId_hidden', "comInsContId"];
    colModel = [{
            key: true,
            name: 'InsuranceEmpFmDetailsId',
            index: 'InsuranceEmpFmDetailsId',
            editable: false,
            hidden: true

        }, {
            name: 'partyId',
            index: 'partyId',
            edittype: 'select',
            editoptions: {value: ":"},
            editable: true

        }, {
            name: 'partyName',
            index: 'partyName',
            editable: false

        }, {
            name: 'relatedTo',
            index: 'relatedTo',
            editable: true,
            hidden: true

        }, {
            name: 'memberNumber',
            index: 'memberNumber',
            editrules: {required: true},
            editable: true


        }, {
            name: 'partyId_hidden',
            index: 'partyId_hidden',
            editable: true,
            hidden: true

        }, {
            name: 'comInsContId',
            index: 'comInsContId',

            editable: true,
            hidden: true,

        }]

    fillGridData(colNames, colModel, jqgrid_data, "jqgrid1", "jqgridDiv1", "Insurance Employee Family Member Details", "InsuranceEmpFmDetailsId", "asc", "Employee Family Member Details");
    //To Change Caption
    var result = PartyID_Caption.fontcolor("#f2762e");
    var $grid = $('#jqgrid1');
    $grid.jqGrid('setCaption', 'Insurance Employee Family Member Details [' + result + ']');



    var contractId = dropdown1.options[dropdown1.selectedIndex].id;
    var endDate = "";

    $.ajax({
        type: "post",
        url: "getInsuranceContarctEndDate",
        data: "ContractId=" + contractId,
        success: function (data) {
            endDate = data;
            if (getCurrentDate() > endDate) {
                document.getElementById("jqgrid1_iladd").hidden = true;
                document.getElementById("jqgrid1_iledit").hidden = true;
                document.getElementById("del_jqgrid1").hidden = true;
                document.getElementById("jqgrid1_ilcancel").hidden = true;
                document.getElementById("jqgrid1_ilsave").hidden = true;


            } else {
                document.getElementById("jqgrid1_iladd").hidden = false;
                document.getElementById("jqgrid1_iledit").hidden = false;
                document.getElementById("del_jqgrid1").hidden = false;
                document.getElementById("jqgrid1_ilcancel").hidden = false;
                document.getElementById("jqgrid1_ilsave").hidden = false;
            }


        }

    });


}// End Function fillInsuranceEmployeeFMDetails





function addEmployeeFMDetails() {


    var contid = dropdown1.options[dropdown1.selectedIndex].id

    if (PartyID_Caption == "") {
        document.getElementById("jqgrid1_ilcancel").click();
        $.smallBox({
            title: "Wrong",
            content: "Cant Add please Close Parent Table.",
            color: "#5384AF",
            timeout: 3000,
            icon: "fa fa-bell"
        });
    }


    // to set Contract Id In Hidden Feild
    ID = $("input.editable[name='comInsContId']").attr("id");
    document.getElementById(ID).value = contid;

    // Message 
    if (document.getElementById("jqgrid").querySelector('[class*="ui-state-highlight"]') == null) {
        document.getElementById("jqgrid1_ilcancel").click();
        $.smallBox({
            title: "Wrong",
            content: "Must Select Parent Employee.",
            color: "#5384AF",
            timeout: 3000,
            icon: "fa fa-bell"
        });
    } else {

        $("#jqgrid1").setGridParam({editurl: "createInsuranceEmpFmDetails"});

        // to set relaredto/Parent  Id In Hidden Feild
        var relaredto_ID = $("input.editable[name='relatedTo']").attr("id");
        document.getElementById(relaredto_ID).value = partyId_Parameter;

        $.ajax({
            'async': false,
            type: "post",
            url: "getEmployeeFamilyMemberDropDown",
            data: "partyIdValue=" + partyId_Parameter,
            success: function (data) {
                if (data.trim() == "<select></select>") {
                    document.getElementById("jqgrid1_ilcancel").click();
                    $.smallBox({
                        title: "Wrong",
                        content: "This Employee Dosent Have Family Member To Add.",
                        color: "#5384AF",
                        timeout: 5000,
                        icon: "fa fa-bell"
                    });
                }
                var ID = $("select[name='partyId']").attr("id");
                document.getElementById(ID).innerHTML = data.trim();

            }
        });



    }

}



function editEmployeeFMDetails() {


    $("#jqgrid1").setGridParam({editurl: "updateInsuranceEmpFmDetails"});

    var contid = dropdown1.options[dropdown1.selectedIndex].id

    var ID = $("input.editable[name='comInsContId']").attr("id");
    document.getElementById(ID).value = contid;


    var relaredto_ID = $("input.editable[name='relatedTo']").attr("id");
    document.getElementById(relaredto_ID).disabled = true;

    $.ajax({
        type: "post",
        url: "getEmployeeFamilyMemberDropDown",
        data: "partyIdValue=" + partyId_Parameter,
        success: function (data) {
            var ID = $("select[name='partyId']").attr("id");
            document.getElementById(ID).innerHTML = data.trim();
            // to get selected family member in update
            var partyId_hidden_ID = $("input.editable[name='partyId_hidden']").attr("id");
            partyId_hidden_ID = document.getElementById(partyId_hidden_ID).value;
            document.getElementById(ID).value = partyId_hidden_ID;
        }
    });
}


function refreshEmployeeFMDetails()
{
    fillInsuranceEmployeeFMDetails(partyId_Parameter);
}


function deleteEmployeeFMDetails() {

    $("#jqgrid1").setGridParam({editurl: 'deleteInsuranceEmpFmDetails?InsuranceEmpDetailsId=' + InsuranceEmpFmDetailsId_Delete});

}


$(document).ready(function () {
    // executes when HTML-Document is loaded and DOM is ready

    //    Call Success Response Notifications
    jQuery("#jqgrid").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });

    //    Call Success Response Notifications
    jQuery("#jqgrid1").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });



    jQuery("#jqgrid").on("jqGridInlineAfterSaveRow", function () {
        contractId = dropdown1.options[dropdown1.selectedIndex].id;

        var jqgrid_data = [];
        $.ajax({
            'async': false,
            type: "post",
            url: "getInsuranceEmpDetails",
            data: "comInsContId=" + contractId,
            dataType: "json",
            success: function (data) {
                for (item in data) {

                    jqgrid_data.push({
                        InsuranceEmpDetailsId: data[item].InsuranceEmpDetailsId,
                        partyId: data[item].partyId,
                        partyName: data[item].partyName,
                        comInsContId: data[item].comInsContId,
                        memberNumber: data[item].memberNumber,
                        InsClass: data[item].InsClass,
                        startDate: data[item].startDate,
                        endDate: data[item].endDate,
                        partyId_hidden: data[item].partyId,
                        comInsContId_hidden: data[item].comInsContId,
                        InsClass_hidden: data[item].InsClass
                    });

                }
            }
        });

        jQuery("#jqgrid").jqGrid('clearGridData');
        jQuery("#jqgrid").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#jqgrid").trigger('reloadGrid');

    });

//    ...........................

    jQuery("#jqgrid1").on("jqGridInlineAfterSaveRow", function () {
        var contid = dropdown1.options[dropdown1.selectedIndex].id

        var jqgrid_data = [];

        $.ajax({
            'async': false,
            type: "post",
            url: "getInsuranceEmpFmDetails",
            data: {partyId_param: partyId_Parameter, contractId: contid},
            dataType: "json",
            success: function (data) {

                for (item in data) {
                    jqgrid_data.push({
                        InsuranceEmpFmDetailsId: data[item].InsuranceEmpDetailsId,
                        partyId: data[item].partyId,
                        partyName: data[item].partyName,
                        relatedTo: data[item].relatedTo,
                        memberNumber: data[item].memberNumber,
                        partyId_hidden: data[item].partyId

                    });

                }
            }
        }); //End ajax Request

        jQuery("#jqgrid1").jqGrid('clearGridData');
        jQuery("#jqgrid1").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#jqgrid1").trigger('reloadGrid');

    });


});
    