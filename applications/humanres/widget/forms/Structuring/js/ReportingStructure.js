$(window).load(function () {
    $("input[name='emplPositionIdReportingTo']").attr("required", "required");
//  $("input[title='Format: yyyy-MM-dd']").attr("required","required"); 
});
$(document).ready(function () {
//    $.ajax({
//        'async': false,
//        type: "post",
//        url: "ReportingStructureTree",
//        dataType: "json",
//        success: function (data) {
//            alert("ajax ReportingStructureTree");
//        }
//    });

});

function submitReportingStruct() {
    var emplPositionId = $("#emplPositionId").val();
    var emplPositionIdManagedBy = $("#emplPositionIdManagedBy").val();
    var emplPositionIdReportingTo = $("#0_lookupId_emplPositionIdReportingTo").val();
    var fromDate = $("#fromDate_i18n").val();
    var thruDate = $("#thruDate_i18n").val();
    var comments = $("#comments").val();
    var primaryFlag = $("#primaryFlag").val();
    $.ajax({
        'async': false,
        type: "post",
        url: "createEmplPositionReportingStruct",
        data: {emplPositionId: emplPositionId, emplPositionIdManagedBy: emplPositionIdManagedBy,
            emplPositionIdReportingTo: emplPositionIdReportingTo, fromDate: fromDate,
            thruDate: thruDate, comments: comments, primaryFlag: primaryFlag},
        dataType: "json",
        success: function (data) {
            alert("123");
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

