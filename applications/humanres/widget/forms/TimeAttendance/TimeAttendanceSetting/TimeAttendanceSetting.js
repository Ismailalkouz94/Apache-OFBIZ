/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images

});

$.ajax({
    "async": false,
    type: "post",
    url: "getAllowencesIdDropDown",
    success: function (data) {
        $("#AllowenceID").append(data);
    }
});//End Of Ajax Request

$.ajax({
    'async': false,
    type: "post",
    url: "getTimeAttendanceSetting",
    data: {},
    dataType: "json",
    success: function (data) {

        for (item in data) {
            if (data[item].Key == "START_WORK") {
                $("#StartWork").val(data[item].Value)
            } else if (data[item].Key == "TO_LATE") {
                $("#toLate").val(data[item].Value)
            } else if (data[item].Key == "END_WORK") {
                $("#EndWork").val(data[item].Value)
            } else if (data[item].Key == "BEFORE_END_WORK") {
                $("#BeforEndWork").val(data[item].Value)
            } else if (data[item].Key == "FIRST_OFFDAY") {
                $("#FirstOFFDay").val(data[item].Value)
            } else if (data[item].Key == "SECOND_OFFDAY") {
                $("#SecondOFFDay").val(data[item].Value)
            } else if (data[item].Key == "TIME_ATTENDANCE_ALLOWENCE_ID") {
                $("#AllowenceID").val(data[item].Value)
            } else if (data[item].Key == "LEAVE_HOURS") {
                $("#leaveHours").val(data[item].Value)
            }

        }
    }
}); // End of Ajax Request


$('.time').timepicker({timeFormat: 'hh:mm',reset: true});