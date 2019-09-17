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

function search() {
    fillTimeAttendanceData();
}

var month = $("#Month").val();

function fillTimeAttendanceData() {
    var jqgrid_data = [];
    $.ajax({
        'async': false,
        type: "post",
        url: "getTimeAttendance_EmployeeData",
        data: {partyId: $("#0_lookupId_partyId").val(), year: $("#year").val(), month: $("#month").val()},
        dataType: "json",
        success: function (data) {
            for (item in data) {
                jqgrid_data.push({
                    timeAttendanceId: data[item].timeAttendanceId,
                    partyId: data[item].partyId,
                    cardId: data[item].cardId,
                    AttDate: data[item].AttDate,
                    weekDay: data[item].weekDay,
                    dayType: data[item].dayType,
                    checkIn: data[item].checkIn,
                    checkOut: data[item].checkOut,
                    work: data[item].work,
                    late: data[item].late,
                    notes: data[item].notes,
                    userLoginId: data[item].userLoginId
                });

            }
        }
    }); // End of Ajax Request
    var colNames = ["timeAttendanceId", "Party ID", "Card ID", "Date", "week Day", "Day Type", "IN", "OUT", "Notes", "Morning Late", "Work hours", "userLoginId"];
    var colModel = [{
            key: true,
            name: 'timeAttendanceId',
            index: 'timeAttendanceId',
            editable: false,
            hidden: true

        }, {
            name: 'partyId',
            index: 'partyId',
            editable: true,

        }, {
            name: 'cardId',
            index: 'cardId',
            editable: true,
            hidden: true
        }, {
            name: 'AttDate',
            index: 'AttDate',
            editable: true
        }, {
            name: 'weekDay',
            index: 'weekDay',
            editable: true
        }, {
            name: 'dayType',
            index: 'dayType',
            editable: true
        }, {
            name: 'checkIn',
            index: 'checkIn',
            editable: true
        }, {
            name: 'checkOut',
            index: 'checkOut',
            editable: true
        }, {
            name: 'notes',
            index: 'notes',
            editable: true
        }, {
            name: 'late',
            index: 'late',
            editable: true
        }, {
            name: 'work',
            index: 'work',
            editable: true
        }, {
            name: 'userLoginId',
            index: 'userLoginId',
            editable: true,
            hidden: true
        }];

    getGridData(colNames, colModel, jqgrid_data, "TimeAttendance_jqgrid", "TimeAttendance_jqgridDiv", "Time Attendance List", "timeAttendanceId", "asc", 30);

} //End of function fillInsuranceData 


$(document).ready(function () {

});



function setTimeAttendance_Employee() {
    $.ajax({
        'async': false,
        type: "post",
        url: "setTimeAttendance_Employee",
        data: {partyId: $("#0_lookupId_partyId").val(), year: $("#year").val(), month: $("#month").val()},
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
            setTimeAttendance_Employee();

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



function exportToExcel() {
    window.location.href = "exportToExcel?partyId=" + $("#0_lookupId_partyId").val() + "&year=" + $("#year").val() + "&month=" + $("#month").val();
}

function exportToPDF() {
//    window.location.href = "TimeAttendancePDFReport_Employee?partyId=" + $("#0_lookupId_partyId").val() + "&year=" + $("#year").val() + "&month=" + $("#month").val();
    window.open("TimeAttendancePDFReport_Employee?partyId=" + $("#0_lookupId_partyId").val() + "&year=" + $("#year").val() + "&month=" + $("#month").val(), '_blank');
}

