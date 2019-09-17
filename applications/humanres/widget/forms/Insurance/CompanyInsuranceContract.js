$(window).load(function () {
    fillICompanynIsuranceContract();
    fillInsuranceClassCatogery(comInsContId_Parameter);
    fillICompanynsuranceContractDetails(comInsCatId_Parameter, comInsContId_Parameter);

// Action On first Grid Buttons
    document.getElementById("CompanynIsuranceContract_iladd").addEventListener("click", add);
    document.getElementById("CompanynIsuranceContract_iledit").addEventListener("click", edit);
//    document.getElementById("CompanynIsuranceContract_ilsave").addEventListener("click", save);
    document.getElementById("refresh_CompanynIsuranceContract").addEventListener("click", refresh);
    document.getElementById("del_CompanynIsuranceContract").addEventListener("click", deleteCompanyInsuranceCont);

//               .....
// Action On Second Grid Buttons
    document.getElementById("InsuranceClassCatogery_iladd").addEventListener("click", addInsuranceClassCatogery);
    document.getElementById("InsuranceClassCatogery_iledit").addEventListener("click", editInsuranceClassCatogery);
    document.getElementById("refresh_InsuranceClassCatogery").addEventListener("click", refreshInsuranceClassCatogery);
    document.getElementById("del_InsuranceClassCatogery").addEventListener("click", deleteInsuranceClassCat);

//               .....
// Action On Third Grid Buttons
    document.getElementById("CompanynsuranceContractDetails_iladd").addEventListener("click", addCompanynsuranceContractDetails);
    document.getElementById("CompanynsuranceContractDetails_iledit").addEventListener("click", editCompanynsuranceContractDetails);
    document.getElementById("refresh_CompanynsuranceContractDetails").addEventListener("click", refreshCompanynsuranceContractDetails);
    document.getElementById("del_CompanynsuranceContractDetails").addEventListener("click", deleteCompanyInsuranceContDetails);



});



var comInsContId_Parameter = "";
var comInsCatId_Parameter = "";
var comInsContId_Delete = "";
var comInsContDetailsId_Delete = "";
var InsuranceClassCat_Delete = "";

var contractName_Caption = "";
var ClassCategory_Caption = "";
function fillICompanynIsuranceContract() {

    var jqgrid_data = [];
    $.ajax({
        "async": false,
        type: "post",
        url: "getCompanyInsuranceContract",

        dataType: "json",
        success: function (data) {
            for (item in data) {

                jqgrid_data.push({
                    comInsContId: data[item].comInsContId,
                    insListId: data[item].insListId,
                    insTypeId: data[item].insTypeId,
                    contarctAmount: data[item].contarctAmount,
                    covCompanyPer: data[item].covCompanyPer,
                    covEmpPer: data[item].covEmpPer,
                    covCompFmPer: data[item].covCompFmPer,
                    covFmPer: data[item].covFmPer,
                    startDate: data[item].startDate,
                    endDate: data[item].endDate,
                    description: data[item].description,

                });

            }//End Of Loop

        }//End Of Success
    }); //End Of Ajax request
    colNames = ['Contract ID', 'Company Insurance Name', 'Insurance Type', 'Contarct Amount', 'Cover Comp Emp (%)', 'Cover Emp (%)', 'Cover Comp Family Member (%)', 'Cover Family Member (%)', 'Start Date', 'End Date', 'Description'];
    colModel = [{
            key: true,
            name: 'comInsContId',
            index: 'comInsContId',
            editable: false
        }, {
            name: 'insListId',
            index: 'insListId',
            edittype: 'select',
            editoptions: {dataUrl: 'getCopmanyInsuranceDropDown'},
            editable: true

        }, {
            name: 'insTypeId',
            index: 'insTypeId',
            edittype: 'select',
            editoptions: {dataUrl: 'getInsuranceTypeDropDown'},
            editable: true
        }, {
            name: 'contarctAmount',
            index: 'contarctAmount',
            editrules: {required: true, number: true},
            editable: true
        }, {
            name: 'covCompanyPer',
            index: 'covCompanyPer',
            editrules: {required: true, number: true},

            editable: true
        }, {
            name: 'covEmpPer',
            index: 'covEmpPer',
            editrules: {required: true, number: true},
            editable: true
        }, {
            name: 'covCompFmPer',
            index: 'covCompFmPer',
//                                        editrules: {required: true,number:true},            
            editable: true
        }, {
            name: 'covFmPer',
            index: 'covFmPer',
//                                        editrules: {required: true,number:true},            
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
            name: 'description',
            index: 'description',
            editable: true,
            editrules: {required: true},

        }];

    fillGridData(colNames, colModel, jqgrid_data, "CompanynIsuranceContract", "CompanynIsuranceContractDiv", "Company Insurance Contract", "startDate", "desc", " Company Insurance Contract");

}//End Of fillICompanynsuranceContract

function rowSelected(rowId, gridId) {
    if (gridId == "CompanynIsuranceContract") {
        var rowData = jQuery('#CompanynIsuranceContract').getRowData(rowId);
        comInsContId_Parameter = rowData['comInsContId'];
        comInsContId_Delete = rowData['comInsContId'];

        contractName_Caption = rowData['insListId'];
        if ($('select[role="select"]').attr("class") == "editable") {
            contractName_Caption = "";
        }

        DisableButton(comInsContId_Parameter);

        fillInsuranceClassCatogery(comInsContId_Parameter);
        fillICompanynsuranceContractDetails(comInsCatId_Parameter, comInsContId_Parameter);
    }

    if (gridId == "InsuranceClassCatogery") {
        var rowData = jQuery('#InsuranceClassCatogery').getRowData(rowId);
        InsuranceClassCat_Delete = rowData['InsClassCatId'];
        comInsCatId_Parameter = rowData['InsClassCatId'];

        ClassCategory_Caption = rowData['description'];
        if ($('input.editable[name="description"]').attr("class") == "editable") {
            ClassCategory_Caption = "";
        }
//           
        fillICompanynsuranceContractDetails(comInsCatId_Parameter, comInsContId_Parameter);
    }

    if (gridId == "CompanynsuranceContractDetails") {
        var rowData = jQuery('#CompanynsuranceContractDetails').getRowData(rowId);
        comInsContDetailsId_Delete = rowData['comInsContDetailsId'];

    }
}

//to select first Row when grid loaded by default  Master Function
//    function firstRowSelected(gridId,index){
//    if(gridId=="CompanynIsuranceContract"){
//        var idarray = jQuery("#CompanynIsuranceContract").jqGrid('getDataIDs');
//        if (idarray.length > 0) {
//            var firstid = jQuery("#CompanynIsuranceContract").jqGrid('getDataIDs')[index];
//            $("#CompanynIsuranceContract").setSelection(firstid);    
//        } 
//            }
//        }








function refresh()
{
    fillICompanynIsuranceContract();
}



function add() {
    $("#CompanynIsuranceContract").setGridParam({editurl: "createCompanyInsuranceContract"});

    //To calculate coverege aotumatilcly
    var covCompanyPer_ID = $("input.editable[name='covCompanyPer']").attr("id");
    var covEmpPer_ID = $("input.editable[name='covEmpPer']").attr("id");
    document.getElementById(covCompanyPer_ID).addEventListener("change", function () {
        var covCompanyPer_value = document.getElementById(covCompanyPer_ID).value;
        var result = parseInt(covCompanyPer_value - 100);
        ;
        result = Math.abs(result)
        document.getElementById(covEmpPer_ID).value = result;
        document.getElementById(covEmpPer_ID).disabled = true;
    });

    var covCompFmPer_ID = $("input.editable[name='covCompFmPer']").attr("id");
    var covFmPer_ID = $("input.editable[name='covFmPer']").attr("id");
    document.getElementById(covCompFmPer_ID).addEventListener("change", function () {
        var covCompFmPer_value = document.getElementById(covCompFmPer_ID).value;
        var result = parseInt(covCompFmPer_value - 100);
        ;
        result = Math.abs(result)
        document.getElementById(covFmPer_ID).value = result;
        document.getElementById(covFmPer_ID).disabled = true;
    });


}


function edit() {
    $("#CompanynIsuranceContract").setGridParam({editurl: "updateCompanyInsuranceContract"});
}

function deleteCompanyInsuranceCont() {
    $("#CompanynIsuranceContract").setGridParam({editurl: 'deleteCompanyInsuranceContract?comInsContId=' + comInsContId_Delete});
}

function RefreshGrid(gridID) {
    if (gridID == "CompanynIsuranceContract") {
        fillICompanynIsuranceContract();
    }
    if (gridID == "InsuranceClassCatogery") {
        fillInsuranceClassCatogery(comInsContId_Parameter);
    }
    if (gridID == "CompanynsuranceContractDetails") {
        fillICompanynsuranceContractDetails(comInsCatId_Parameter, comInsContId_Parameter);
    }

}

function DisableButton(contractId) {
    var endDate = "";
    $.ajax({
        type: "post",
        url: "getInsuranceContarctEndDate",
        data: "ContractId=" + contractId,
        success: function (data) {
            endDate = data;
            if (getCurrentDate() > endDate) {

                //Seconed Grid
                document.getElementById("InsuranceClassCatogery_iladd").hidden = true;
                document.getElementById("InsuranceClassCatogery_iledit").hidden = true;
                document.getElementById("del_InsuranceClassCatogery").hidden = true;
                document.getElementById("InsuranceClassCatogery_ilcancel").hidden = true;
                document.getElementById("InsuranceClassCatogery_ilsave").hidden = true;
                //Third Grid
                document.getElementById("CompanynsuranceContractDetails_iladd").hidden = true;
                document.getElementById("CompanynsuranceContractDetails_iledit").hidden = true;
                document.getElementById("del_CompanynsuranceContractDetails").hidden = true;
                document.getElementById("CompanynsuranceContractDetails_ilcancel").hidden = true;
                document.getElementById("CompanynsuranceContractDetails_ilsave").hidden = true;

            } else {

                //Seconed Grid
                document.getElementById("InsuranceClassCatogery_iladd").hidden = false;
                document.getElementById("InsuranceClassCatogery_iledit").hidden = false;
                document.getElementById("del_InsuranceClassCatogery").hidden = false;
                document.getElementById("InsuranceClassCatogery_ilcancel").hidden = false;
                document.getElementById("InsuranceClassCatogery_ilsave").hidden = false;
                //Third Grid
                document.getElementById("CompanynsuranceContractDetails_iladd").hidden = false;
                document.getElementById("CompanynsuranceContractDetails_iledit").hidden = false;
                document.getElementById("del_CompanynsuranceContractDetails").hidden = false;
                document.getElementById("CompanynsuranceContractDetails_ilcancel").hidden = false;
                document.getElementById("CompanynsuranceContractDetails_ilsave").hidden = false;
            }


        }

    });
}



var flag = false;
var ageTo = 0;
function fillInsuranceClassCatogery(comInsContId_Parameter) {
    var jqgrid_data = [];
    $.ajax({
        'async': false,
        type: "post",
        url: "getInsuranceClassCatogery",
        data: {contractId: comInsContId_Parameter},
        dataType: "json",
        success: function (data) {
            if (data.length == 0) {
                flag = true;
//                                $.smallBox({
//                                title : "Wrong",
//                                content : "No Data Found for ("+contractName_Caption+")",
//                                color : "#5384AF",
//                                timeout: 3000,
//                                icon : "fa fa-bell"
//                               });
            } else {
                flag = false;
            }
            for (item in data) {

                jqgrid_data.push({
                    InsClassCatId: data[item].InsClassCatId,
                    description: data[item].description,
                    ageFrom: data[item].ageFrom,
                    ageTo: data[item].ageTo,

                });
                ageTo = data[item].ageTo;
            }
        }
    });

    colNames = ['ID', 'comInsContId', 'Description', 'Age From', 'Age To']
    colModel = [{
            key: true,
            name: 'InsClassCatId',
            index: 'InsClassCatId',
            editable: false,
            hidden: true

        },
        {
            name: 'comInsContId',
            index: 'comInsContId',
            editable: true,
            editrules: {required: true},
            hidden: true

        }, {
            name: 'description',
            index: 'description',
            editable: true,
            editrules: {required: true}

        }, {
            name: 'ageFrom',
            index: 'ageFrom',
            editable: true,
            editrules: {required: true, number: true}
        }, {
            name: 'ageTo',
            index: 'ageTo',
            editable: true,
            editrules: {required: true, number: true}
        }];



    fillGridData(colNames, colModel, jqgrid_data, "InsuranceClassCatogery", "InsuranceClassCatogeryDiv", "Insurance Class Category", "ageFrom", "asc", "Insurance Class Category");


    var result = contractName_Caption.fontcolor("#f2762e");
    var $grid = $('#InsuranceClassCatogery');
    $grid.jqGrid('setCaption', 'Insurance Class Category [' + result + ']');

}



function refreshInsuranceClassCatogery()
{
    fillInsuranceClassCatogery(comInsContId_Parameter);
}


function addInsuranceClassCatogery() {

    if (contractName_Caption == "") {
        document.getElementById("InsuranceClassCatogery_ilcancel").click();
        $.smallBox({
            title: "Wrong",
            content: "Cant Add please Close Parent Table.",
            color: "#5384AF",
            timeout: 3000,
            icon: "fa fa-bell"
        });
    }
    //To check if select Parent Id   
    if (document.getElementById("CompanynIsuranceContract").querySelector('[class*="ui-state-highlight"]') == null) {
        document.getElementById("InsuranceClassCatogery_ilcancel").click();
        $.smallBox({
            title: "Wrong",
            content: "Must Select Insurance Contract.",
            color: "#5384AF",
            timeout: 3000,
            icon: "fa fa-bell"
        });
    } else {
        $("#InsuranceClassCatogery").setGridParam({editurl: "createInsuranceClassCatogery"});
        //To fill age From and age To automaticly
        if (flag) {
            var ageFrom_ID = $("input.editable[name='ageFrom']").attr("id");

            document.getElementById(ageFrom_ID).value = 0;
            document.getElementById(ageFrom_ID).disabled = true;
        } else {
            var ageFrom_ID = $("input.editable[name='ageFrom']").attr("id");
            document.getElementById(ageFrom_ID).value = ageTo + 1;
            document.getElementById(ageFrom_ID).disabled = true;
        }

        ID = $("input.editable[name='comInsContId']").attr("id");
        document.getElementById(ID).value = comInsContId_Parameter;

    }
}


function editInsuranceClassCatogery() {
    $("#InsuranceClassCatogery").setGridParam({editurl: "updateInsuranceClassCatogery"});

    ID = $("input.editable[name='comInsContId']").attr("id");
    document.getElementById(ID).value = comInsContId_Parameter;

}

function deleteInsuranceClassCat() {
    $("#InsuranceClassCatogery").setGridParam({editurl: 'deleteInsuranceClassCatogery?InsClassCatId=' + InsuranceClassCat_Delete});

}




function fillICompanynsuranceContractDetails(comInsCatId_Parameter, comInsContId_Parameter) {

    var jqgrid_data = [];
    $.ajax({
        "async": false,
        type: "post",
        url: "getCompanyInsuranceContractDetails",
        data: {contractId: comInsContId_Parameter, InsClassCatId: comInsCatId_Parameter},
        dataType: "json",
        success: function (data) {
            for (item in data) {

                jqgrid_data.push({
                    comInsContDetailsId: data[item].comInsContDetailsId,
                    comInsContId: data[item].comInsContId,
                    InsClassCatId: data[item].InsClassCatId,
                    gender: data[item].gender,
                    classAPlus: data[item].classAPlus,
                    classA: data[item].classA,
                    classB: data[item].classB,
                    classC: data[item].classC,
                    classD: data[item].classD,
                    comInsContId2: data[item].comInsContId2,
                    InsClassCatId2: data[item].InsClassCatId2,
                });

            }// End OF for
        }//End Of Success
    }); //End Of Ajax request

    colNames = ['ID', 'Comp Insurance Conttract ID', 'Insurance Class Catogery', 'Gender', 'Class A+', 'Class A', 'Class B', 'Class C', 'Class D', 'comInsContId2', 'InsClassCatId2'],
            colModel = [{
                    key: true,
                    name: 'comInsContDetailsId',
                    index: 'comInsContDetailsId',
                    editable: false,
                    hidden: true,
                }, {
                    name: 'comInsContId',
                    index: 'comInsContId',
//                             editoptions: { defaultValue: 1},
                    editable: true,
                    width: 230,
                    hidden: true

                }, {
                    name: 'InsClassCatId',
                    index: 'InsClassCatId',
//                            edittype:'select',
//                            editoptions:{value:":"},
                    editable: true,
                    hidden: true
                }, {
                    name: 'gender',
                    index: 'gender',
                    edittype: 'select',
                    editoptions: {dataInit: function (elem)
                        {
                            $(elem).empty()
                                    .append("<option value='M'>Male</option>")
                                    .append("<option value='F'>Female</option>");
                        }},
                    editable: true
                }, {
                    name: 'classAPlus',
                    index: 'classAPlus',

                    editable: true
                }, {
                    name: 'classA',
                    index: 'classA',
                    editable: true
                }, {
                    name: 'classB',
                    index: 'classB',
                    editable: true
                }, {
                    name: 'classC',
                    index: 'classC',

                    editable: true
                }, {
                    name: 'classD',
                    index: 'classD',

                    editable: true
                }, {
                    name: 'comInsContId2',
                    index: 'comInsContId2',
                    editable: true,
                    hidden: true

                }, {
                    name: 'InsClassCatId2',
                    index: 'InsClassCatId2',
                    editable: true,
                    hidden: true
                }]

    fillGridData(colNames, colModel, jqgrid_data, "CompanynsuranceContractDetails", "CompanynsuranceContractDetailsDiv", "Company Insurance Contract Details", "comInsContDetailsId", "asc", "contract details");
    var result = ClassCategory_Caption.fontcolor("#f2762e");
    var $grid = $('#CompanynsuranceContractDetails');
    $grid.jqGrid('setCaption', 'Company Insurance Contract Details [' + result + ']');

}//End Of fillICompanynsuranceContractDetails     



function refreshCompanynsuranceContractDetails()
{
    fillICompanynsuranceContractDetails(comInsCatId_Parameter, comInsContId_Parameter);
}




function addCompanynsuranceContractDetails() {

    if (ClassCategory_Caption == "") {
        document.getElementById("CompanynsuranceContractDetails_ilcancel").click();
        $.smallBox({
            title: "Wrong",
            content: "Cant Add please Close Parent Table.",
            color: "#5384AF",
            timeout: 3000,
            icon: "fa fa-bell"
        });
    }

    $("#CompanynsuranceContractDetails").setGridParam({editurl: "createCompanyInsContractDet"});

    ID = $("input.editable[name='comInsContId']").attr("id");
    document.getElementById(ID).value = comInsContId_Parameter;

    ID = $("input.editable[name='InsClassCatId']").attr("id");
    document.getElementById(ID).value = comInsCatId_Parameter;

}


function editCompanynsuranceContractDetails() {
    $("#CompanynsuranceContractDetails").setGridParam({editurl: "updateCompanyInsContractDet"});

    ID = $("input.editable[name='comInsContId']").attr("id");
    document.getElementById(ID).value = comInsContId_Parameter;

    ID = $("input.editable[name='InsClassCatId']").attr("id");
    document.getElementById(ID).value = comInsCatId_Parameter;
}

function deleteCompanyInsuranceContDetails() {
    $("#CompanynsuranceContractDetails").setGridParam({editurl: 'deleteCompanyInsContractDet?comInsContDetailsId=' + comInsContDetailsId_Delete});
}




$(document).ready(function () {
    // executes when HTML-Document is loaded and DOM is ready

    //    Call Success Response Notifications
    jQuery("#CompanynIsuranceContract").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });

    //    Call Success Response Notifications
    jQuery("#InsuranceClassCatogery").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });
    //    Call Success Response Notifications
    jQuery("#CompanynsuranceContractDetails").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });

    jQuery("#CompanynIsuranceContract").on("jqGridInlineAfterSaveRow", function () {
        var jqgrid_data = [];
        $.ajax({
            "async": false,
            type: "post",
            url: "getCompanyInsuranceContract",
            dataType: "json",
            success: function (data) {
                for (item in data) {
                    jqgrid_data.push({
                        comInsContId: data[item].comInsContId,
                        insListId: data[item].insListId,
                        insTypeId: data[item].insTypeId,
                        contarctAmount: data[item].contarctAmount,
                        covCompanyPer: data[item].covCompanyPer,
                        covEmpPer: data[item].covEmpPer,
                        covCompFmPer: data[item].covCompFmPer,
                        covFmPer: data[item].covFmPer,
                        startDate: data[item].startDate,
                        endDate: data[item].endDate,
                        description: data[item].description,

                    });
                }//End Of Loop
            }//End Of Success
        }); //End Of Ajax request

        jQuery("#CompanynIsuranceContract").jqGrid('clearGridData');
        jQuery("#CompanynIsuranceContract").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#CompanynIsuranceContract").trigger('reloadGrid');

    });
//    ...........................

    jQuery("#InsuranceClassCatogery").on("jqGridInlineAfterSaveRow", function () {
        var jqgrid_data = [];
        $.ajax({
            'async': false,
            type: "post",
            url: "getInsuranceClassCatogery",
            data: {contractId: comInsContId_Parameter},
            dataType: "json",
            success: function (data) {
                if (data.length == 0) {
                    flag = true;
//                                $.smallBox({
//                                title : "Wrong",
//                                content : "No Data Found for ("+contractName_Caption+")",
//                                color : "#5384AF",
//                                timeout: 3000,
//                                icon : "fa fa-bell"
//                               });
                } else {
                    flag = false;
                }
                for (item in data) {

                    jqgrid_data.push({
                        InsClassCatId: data[item].InsClassCatId,
                        description: data[item].description,
                        ageFrom: data[item].ageFrom,
                        ageTo: data[item].ageTo,

                    });
                    ageTo = data[item].ageTo;
                }
            }
        });

        jQuery("#InsuranceClassCatogery").jqGrid('clearGridData');
        jQuery("#InsuranceClassCatogery").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#InsuranceClassCatogery").trigger('reloadGrid');

    });

//    ...............................

    jQuery("#CompanynsuranceContractDetails").on("jqGridInlineAfterSaveRow", function () {
        var jqgrid_data = [];
        $.ajax({
            "async": false,
            type: "post",
            url: "getCompanyInsuranceContractDetails",
            data: {contractId: comInsContId_Parameter, InsClassCatId: comInsCatId_Parameter},
            dataType: "json",
            success: function (data) {
                for (item in data) {

                    jqgrid_data.push({
                        comInsContDetailsId: data[item].comInsContDetailsId,
                        comInsContId: data[item].comInsContId,
                        InsClassCatId: data[item].InsClassCatId,
                        gender: data[item].gender,
                        classAPlus: data[item].classAPlus,
                        classA: data[item].classA,
                        classB: data[item].classB,
                        classC: data[item].classC,
                        classD: data[item].classD,
                        comInsContId2: data[item].comInsContId2,
                        InsClassCatId2: data[item].InsClassCatId2,
                    });

                }// End OF for
            }//End Of Success
        }); //End Of Ajax request

        jQuery("#CompanynsuranceContractDetails").jqGrid('clearGridData');
        jQuery("#CompanynsuranceContractDetails").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#CompanynsuranceContractDetails").trigger('reloadGrid');

    });
});
             