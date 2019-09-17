/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images
    fillTimeAttendanceData();
});

var start = 2017;
var end = new Date().getFullYear();
var options = "";
for (var year = start; year <= end; year++) {
    options += "<option>" + year + "</option>";
}
document.getElementById("year").innerHTML = options;

//$("#year").change(function () {
//    fillTimeAttendanceData();
//});
//
//$("#Month").change(function () {
//    fillTimeAttendanceData();
//});



function fillTimeAttendanceData() {
    var jqgrid_data = [];
    $.ajax({
        'async': false,
        type: "post",
        url: "getTimeAttendance_SummaryData",
        data: {year: $("#year").val(), month: $("#month").val(), dept: $("#0_lookupId_LookupInternalOrganization").val()},
        dataType: "json",
        success: function (data) {
            for (item in data) {
                jqgrid_data.push({
                    timeAttendanceSummId: data[item].timeAttendanceSummId,
                    partyId: data[item].partyId,
                    attendedWorkingHours: data[item].attendedWorkingHours,
                    personalLeave: data[item].personalLeave,
//                    emergencyLeave: data[item].emergencyLeave,
                    vacationAnnual: data[item].vacationAnnual,
                    otherVacation: data[item].otherVacation,
                    businessLeave: data[item].businessLeave,
                    morningLate: data[item].morningLate,
                    totalWorkingHours: data[item].totalWorkingHours,
                    officialWorkingHours: data[item].officialWorkingHours,
                    hoursPlusMinus: data[item].hoursPlusMinus,
                    daysPlusMinus: data[item].daysPlusMinus,
                    userLoginId: data[item].userLoginId
                });

            }
        }
    }); // End of Ajax Request
    var colNames = ["timeAttendanceSummId", "Party ID", "Attended Working Hours", "Personal Leave",  "Business Leave", "Vacation Annual", "Other Vacation", 'Morning Late', 'Total Working Hours', 'Official Working Hours', 'Hours(+)(-)', 'days(+)(-)', "userLoginId"];
    var colModel = [{
            key: true,
            name: 'timeAttendanceSummId',
            index: 'timeAttendanceSummId',
            editable: false,
            hidden: true

        }, {
            name: 'partyId',
            index: 'partyId',
            editable: true,

        }, {
            name: 'attendedWorkingHours',
            index: 'attendedWorkingHours',
            editable: true
        }, {
            name: 'personalLeave',
            index: 'personalLeave',
            editable: true
        }, {
            name: 'businessLeave',
            index: 'businessLeave',
            editable: true
        }, {
            name: 'vacationAnnual',
            index: 'vacationAnnual',
            editable: true
        }, {
            name: 'otherVacation',
            index: 'otherVacation',
            editable: true
        }, {
            name: 'morningLate',
            index: 'morningLate',
            editable: true
        }, {
            name: 'totalWorkingHours',
            index: 'totalWorkingHours',
            editable: true
        }, {
            name: 'officialWorkingHours',
            index: 'officialWorkingHours',
            editable: true
        }, {
            name: 'hoursPlusMinus',
            index: 'hoursPlusMinus',
            editable: true
        }, {
            name: 'daysPlusMinus',
            index: 'daysPlusMinus',
            editable: true
        }, {
            name: 'userLoginId',
            index: 'userLoginId',
            editable: true,
            hidden: true
        }];

    getGridData(colNames, colModel, jqgrid_data, "TimeAttendance_jqgrid", "TimeAttendance_jqgridDiv", "Time Attendance Summary", "timeAttendanceSummId", "asc", 30);

} //End of function fillInsuranceData 


$(document).ready(function () {

});

function setTiimeAttendanceSummary() {
    $.ajax({
        'async': false,
        type: "post",
        url: "setTiimeAttendanceSummary",
        data: {year: $("#year").val(), month: $("#month").val(), dept: $("#0_lookupId_LookupInternalOrganization").val()},
//        dataType: "json",
        success: function (data) {
            if (data.indexOf("success") > 0) {
                swal(
                        'Saved!',
                        'Your file has been deleted.',
                        'success'
                        )
            } else {
                swal(
                        'Error',
                        'Error:)',
                        'error'
                        )
            }

        }
    }); // End of Ajax Request
}

$('.warning.cancel #save_button').on('click', () => {
    swal({
        title: 'Are you sure?',
        text: '',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, Save it!',
        cancelButtonText: 'No, cancel!',
        confirmButtonClass: 'btn btn-success',
        cancelButtonClass: 'btn btn-danger',
        buttonsStyling: false
    }).then((result) => {
        if (result.value) {
            setTiimeAttendanceSummary();
//            fillTimeAttendanceData();
            // result.dismiss can be 'cancel', 'overlay', 'close', and 'timer'
        } else if (result.dismiss === 'cancel') {
            swal(
                    'Cancelled',
                    'Your imaginary file is safe :)',
                    'error'
                    )
        }
    })
});

function search() {
    fillTimeAttendanceData();
}


function exportToExcel() {
    window.location.href = "exportToExcel_TimeAttSumm?dept=" + $("#0_lookupId_LookupInternalOrganization").val() + "&year=" + $("#year").val() + "&month=" + $("#month").val();
}

function exportToPDF() {
//    window.location.href = "TimeAttendancePDFReport_Employee?partyId=" + $("#0_lookupId_partyId").val() + "&year=" + $("#year").val() + "&month=" + $("#month").val();
    window.open("TimeAttendancePDFReport_Summary?dept=" + $("#0_lookupId_LookupInternalOrganization").val() + "&year=" + $("#year").val() + "&month=" + $("#month").val(), '_blank');
}

//var rad = $("input[name='myRadios']");
//var prev = null;
//for (var i = 0; i < rad.length; i++) {
//    rad[i].onclick = function () {
//        if (this.value == "E")
//        {
//            $("#Elook").css("display", "block");
//            $("#Dlook").css("display", "none");
//        } else if (this.value == "D")
//        {
//            $("#Dlook").css("display", "block");
//            $("#Elook").css("display", "none");
//        }
//    };
//}
