/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images

});


function onChangeValueFromLookUp(lookup_ID) {
    $("#vacationDays").val("");
    $("#ticketEligable").val("");
    getEmployeeSettingsData();
}

$("form[name='setEmployeeSettings']").keypress(function (e) {
    var key = e.charCode || e.keyCode || 0;
    if (key == 13) {
        $("#vacationDays").val("");
        $("#ticketEligable").val("");
        getEmployeeSettingsData();
        e.preventDefault();
    }
});


function getEmployeeSettingsData() {
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeeSettingsData",
        data: {partyId: $("#0_lookupId_partyId").val(), partySettingType: "EMPLOYEE_SETTINGS"},
        dataType: "json",
        success: function (data) {
            for (item in data) {
                switch (data[item].key) {
                    case "VACATION_DAYS":
                        $("#vacationDays").val(data[item].value);
                    case "TICKET_ELIGABLE":
                        $("#ticketEligable").val(data[item].value);
                }
            }
        }
    }); // End of Ajax Request
}

$('.time').timepicker({reset: true});




function save() {
    $.ajax({
        'async': false,
        type: "post",
        url: "setEmployeeSettings",
        data: {
            partyId: $("#0_lookupId_partyId").val(),
            annualVacation: $("#annualVacation").val(),
            ticketEligable: $("#ticketEligable").val()},
//        dataType: "json",
        success: function (data) {
            if (data.indexOf("success") > 0) {
                $.smallBox({
                    title: "Success",
                    content: "Insert Successfully",
                    color: "#739E73",
                    iconSmall: "fa fa-check",
                    timeout: 4000
                });
            } else {
                $.smallBox({
                    title: "Error",
                    content: "Insert Faild",
                    color: "#C46A69",
                    iconSmall: "fa fa-times fa-2x fadeInRight animated",
                    timeout: 4000
                });
            }

        }
    }); // End of Ajax Request

}