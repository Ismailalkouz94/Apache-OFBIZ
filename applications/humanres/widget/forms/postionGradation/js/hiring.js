var transDate = 0;
var rowSeq = 0;
var transType = 0;
var positionId = 0;
$(window).load(function () {
    fillSalaryGradeTable(0);
    $("#0_lookupId_customAddEmplPositionFulfillment_partyId").attr("required", "required");
    $("#1_lookupId_customAddEmplPositionFulfillment_emplPositionId").attr("required", "required");
    $("#2_lookupId_partyIdFrom").attr("required", "required");
    document.getElementById("del_SalaryGradeGrid").addEventListener("click", deleteSalaryGradation);
    document.getElementById("refresh_SalaryGradeGrid").addEventListener("click", refreshGrid);

});


function onChangeValueFromLookUp(loockupTextId) {

    $("#hiringBtn").show();
//    alert("onChangeValueFromLookUp");
    if ('0_lookupId_customAddEmplPositionFulfillment_partyId' == loockupTextId) {
        getEmployeeDetails($("#" + loockupTextId).val());
//        alert("onChangeValueFromLookUp");
    }
    if ('1_lookupId_customAddEmplPositionFulfillment_emplPositionId' == loockupTextId) {
        getGrade($("#" + loockupTextId).val());
    }
    $("#del_SalaryGradeGrid").show();

}



function InsertTocustomCreateEmplPositionFulfillment() {

    var partyId = $('#0_lookupId_customAddEmplPositionFulfillment_partyId').val();
    var fromDate = $("#customAddEmplPositionFulfillment_fromDate_i18n").val();
    var emplPositionId = $("#1_lookupId_customAddEmplPositionFulfillment_emplPositionId").val();
    var degreeId = $("#degreeSelect").val();

    var roleTypeIdFrom = $("#roleTypeIdFrom").val();
    var roleTypeIdTo = $("#roleTypeIdTo").val();
    var partyIdFrom = $("#2_lookupId_partyIdFrom").val();
    var note = document.getElementById("note").value;
//    alert(roleTypeIdFrom + " " + roleTypeIdTo + " " + partyIdFrom);


    $.ajax({
        'async': false,
        type: "post",
        url: "customCreateEmplPositionFulfillment",
        data: {partyId: partyId, fromDate: fromDate,
            emplPositionId: emplPositionId, degreeId: degreeId,
            roleTypeIdFrom: roleTypeIdFrom, roleTypeIdTo: roleTypeIdTo,
            partyIdTo: partyId, partyIdFrom: partyIdFrom, notes: note},
        success: function (data) {
            refreshGrid(partyId);
            getEmployeeDetails($("#0_lookupId_customAddEmplPositionFulfillment_partyId").val());
            if ('_ERROR_MESSAGE_' in data) {
                ErrorMessage(data._ERROR_MESSAGE_);
            } else if ('_ERROR_MESSAGE_LIST_' in data) {

                ErrorMessage(data._ERROR_MESSAGE_LIST_);
            } else if ('_EVENT_MESSAGE_' in data) {

                SuccessMessage(data._EVENT_MESSAGE_);
            }

        }
    });
}



function deleteSalaryGradation() {

    var partyId = $("#0_lookupId_customAddEmplPositionFulfillment_partyId").val();

    if (transType.trim() == "HIRING") {
        maxSeq = getMaxSequence(transType, partyId);

        if (rowSeq.trim() == maxSeq.trim()) {
            $("#SalaryGradeGrid").setGridParam({editurl: 'deletePositionGradation_HIRING?emplPositionId=' + positionId + '&partyId=' + partyId + '&transDate=' + transDate + '&rowSeq=' + rowSeq});
            document.getElementById("myform").reset();
        } else if (rowSeq != maxSeq) {
            $("#eData").click();

            $.smallBox({
                title: "Warning",
                content: "This is not last transaction  HIRING!",
                color: "#dfb56c",
                timeout: 3000,
                icon: "fa fa-bell"
            });
        }
    } else {
        $("#eData").click();
        $.smallBox({
            title: "Warning",
            content: "You must select hiring action !",
            color: "#dfb56c",
            timeout: 3000,
            icon: "fa fa-bell"
        });
    }
}
function refreshGrid() {
    refreshGridData($("#0_lookupId_customAddEmplPositionFulfillment_partyId").val());

}


function rowSelected(rowId, gridId) {

    var rowData = jQuery('#' + gridId).getRowData(rowId);
    transDate = rowData['transDate'];
    rowSeq = rowData['rowSeq'];
    transType = rowData['transType'];
    positionId = rowData['positionId'];
    isCalculated(transDate, $("#0_lookupId_customAddEmplPositionFulfillment_partyId").val());

}


function afterDelete(response) {

    var first = (response.responseText).indexOf("<i id='ofbizErrorMessage'>");
    var last = (response.responseText).indexOf("<i id='ofbizErrorMessage1'></i>");
    var result = (response.responseText).substring(first, last);
    if (result.includes("ofbizErrorMessage")) {
        if (result.includes("ORA-")) {
            first = (result).indexOf("ORA-");
            last = (result).indexOf("&lt;");
            result = (result).substring(first, last);
        }

        $.smallBox({
            title: "Error Message",
            content: result,
            color: "#C46A69",
            iconSmall: "fa fa-times fa-2x fadeInRight animated",
            timeout: 8000

        });
        $("#eData").click();
        return [false, ""];
    } //end if error
    else {

        first = (response.responseText).indexOf("<i id='ofbizSuccessMessage'>");
        last = (response.responseText).indexOf("<i id='ofbizSuccessMessage1'></i>");
        $.smallBox({
            title: "Success Message",
            content: "Deleted successfully",
            color: "#739E73",
            iconSmall: "fa fa-check",
            timeout: 4000
        });

        onChangeValueFromLookUp("0_lookupId_customAddEmplPositionFulfillment_partyId");
        $("#eData").click();
        return [true, ""];
    }
}

$("#customAddEmplPositionFulfillment_fromDate_i18n").change(function () {
    $("#hiringBtn").show();
    var selectDate = $("#customAddEmplPositionFulfillment_fromDate_i18n").val();
    $.ajax({
        'async': false,
        type: "post",
        url: "isCalculated_MangmentEmployee",
        data: {transDate: selectDate},
        success: function (data) {

            if (data.trim() == "true") {

                $.smallBox({
                    title: "Warning",
                    content: "This date is included in the calculation of salary",
                    color: "#dfb56c",
                    timeout: 3000,
                    icon: "fa fa-bell"
                });
                $("#hiringDate").val("");
                $("#hiringBtn").hide();

            }

        }
    });



});
