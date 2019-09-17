/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images

});


$.ajax({
    'async': false,
    type: "post",
    url: "getGlobalPayrollSetting",
    data: {Key:"",globalKey:"VACATION_DAYS"},
    dataType: "json",
    success: function (data) {
        for (item in data) {
            if (data[item].Key == "SICK_VACATION") {
                $("#sickVacation").val(data[item].Value)
            } else if (data[item].Key == "SICK_VACATION_SURGERY") {
                $("#sickVacationSurgery").val(data[item].Value)
            }
        }
    }
}); // End of Ajax Request

function setSickVacation() {
    $.ajax({
        "async": false,
        type: "post",
        url: "setGlobalPayrollSetting",
        data: {
            Key: 'SICK_VACATION',
            Value: $("#sickVacation").val(),
            globalKey: 'VACATION_DAYS'
        },
        success: function (data) {
            if (data.indexOf("success") > 0) {
                $.smallBox({
                    title: "Success",
                    content: "Success",
                    color: "#739E73",
                    iconSmall: "fa fa-check",
                    timeout: 5000
                });
            } else {
                $.smallBox({
                    title: "Error",
                    content: Error,
                    color: "#C46A69",
                    iconSmall: "fa fa-times fa-2x fadeInRight animated",
                    timeout: 5000
                });
            }
        }
    });//End Of Ajax Request

}

function setSickVacationSurgery() {
    $.ajax({
        "async": false,
        type: "post",
        url: "setGlobalPayrollSetting",
        data: {
            Key: 'SICK_VACATION_SURGERY',
            Value: $("#sickVacationSurgery").val(),
            globalKey: 'VACATION_DAYS'
        },
        success: function (data) {
            if (data.indexOf("success") > 0) {
                $.smallBox({
                    title: "Success",
                    content: "Success",
                    color: "#739E73",
                    iconSmall: "fa fa-check",
                    timeout: 5000
                });
            } else {
                $.smallBox({
                    title: "Error",
                    content: Error,
                    color: "#C46A69",
                    iconSmall: "fa fa-times fa-2x fadeInRight animated",
                    timeout: 5000
                });
            }
        }
    });//End Of Ajax Request

}