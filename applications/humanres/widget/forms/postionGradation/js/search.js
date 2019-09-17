
$(window).load(function () {
    fillSalaryGradeTable(0);
    document.getElementById("refresh_SalaryGradeGrid").addEventListener("click", refreshGrid);
    $("#del_SalaryGradeGrid").hide();

    $("#0_lookupId_partyId").attr("required", "required");
});

$(document).ready(function () {


//$("#0_lookupId_emplId").change(function() {
// 
//onChangeValueFromLookUp("0_lookupId_emplId");
//});
    document.getElementById("myform").onkeypress = function (e) {
        var key = e.charCode || e.keyCode || 0;
        if (key == 13) {
            onChangeValueFromLookUp("0_lookupId_partyId");
            fillSalaryGradeTable($("#0_lookupId_partyId").val());
            e.preventDefault();
        }
    }

});







function onChangeValueFromLookUp(textFieldId) {
    var partyId = document.getElementById('0_lookupId_partyId').value;
    var emplStatus = getEmplStatus(partyId);
    
    if (emplStatus != "") {
        var fullName = getPartyName(partyId);
//  var emplStatus =getEmployeeStatus(partyId);


        fillSalaryGradeTable(partyId);
        $("#0_lookupId_partyId_lookupDescription").html(" " + fullName);
    } else {
   WarningMessage("The employee not found!");
          $("#0_lookupId_partyId_lookupDescription").html("");
    }
}

//alert($("#bonus").text());





function rowSelected(rowId, gridId) {

    var rowData = jQuery('#' + gridId).getRowData(rowId);
    transDate = rowData['transDate'];
    rowSeq = rowData['rowSeq'];
    transType = rowData['transType'];

    isCalculated(transDate, $("#0_lookupId_partyId").val());

}

function refreshGrid() {
    refreshGridData($("#0_lookupId_partyId").val());

}

