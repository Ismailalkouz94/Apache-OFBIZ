
$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images
    var jqgrid_data = [];
    fillTicketMemerData(jqgrid_data);
    $("#Date").val(getCurrentDate());
    

});


var References_Delete = "";
var ticketRequred = "";
var ticketChargedTo = "";

var visaRequred = "";
var visaChargedTo = "";

var FMRequred = "";

var partyName = "";
var partyBirthDate = "";

function onChangeValueFromLookUp(lookup_ID) {
    getPartyDetails();
}

$("form[name='LookupEmployeeName']").keypress(function (e) {
    var key = e.charCode || e.keyCode || 0;
    if (key == 13) {

        getPartyDetails();
        e.preventDefault();
    }
});



$("#fromDate_i18n").change(function () {
    if($("#thruDate_i18n").val() !=""){
          $("#noOfDays").val(showDays($("#fromDate_i18n").val(), $("#thruDate_i18n").val()));
   if($("#fromDate_i18n").val()>$("#thruDate_i18n").val()){
   WarningMessage("First day must be less then last day !");
   $("#fromDate_i18n").val("");
   
    } }
    });

$("#thruDate_i18n").change(function () {
      $("#noOfDays").val(showDays($("#fromDate_i18n").val(), $("#thruDate_i18n").val()));
    if($("#fromDate_i18n").val() !=""){
   if($("#fromDate_i18n").val() >$("#thruDate_i18n").val()){
   WarningMessage("last day must be greater then first day  !");
    $("#thruDate_i18n").val("");
      $("#noOfDays").val(showDays($("#fromDate_i18n").val(), $("#thruDate_i18n").val()));
    } }
    });

function getPartyDetails() {
    $.ajax({
        "async": false,
        type: "post",
        url: "getPartyIdAndFM_DropDown",
        data: {partyId: $("#0_lookupId_partyId").val()},
        success: function (data) {
            $("#FM-Select").empty();
            $("#FM-Select").append(data);
            $("#FM-partyId").change(function () {
                $.ajax({
                    "async": false,
                    type: "post",
                    url: "getEmployeeDetails",
                    data: {partyId: $(this).val()},
                    dataType: "json",
                    success: function (data) {

                        partyName = data.firstName + " " + data.lastName;
                        partyBirthDate = data.birthDate;

                    }
                });//End Of Ajax Request
            });
        }
    });//End Of Ajax Request
}


$.ajax({
    "async": false,
    type: "post",
    url: "getEmplLeaveReasonTypeId_DropDown",
//    dataType: "json",
    success: function (data) {
        $("#emplLeaveReasonTypeId").append(data);
        
    }
   
});//End Of Ajax Request


function showDays(firstDate, secondDate) {
    var startDay = new Date(firstDate);
    var endDay = new Date(secondDate);
    var millisecondsPerDay = (1000 * 60 * 60 * 24) ;

    var millisBetween = startDay.getTime() - endDay.getTime();
    var days = millisBetween / millisecondsPerDay;
    console.log(firstDate)
    console.log(endDay)
    // Round down.
    return Math.abs(Math.floor(days))+1

}




$('input[name="ticketRequired"]').change(function () {
    var IDtick = $(this).val();
    ticketRequred = $(this).val();
    if (IDtick == "Y") {
        $("#TicketChargeTr").fadeIn("slow", function () {
            $(this).show();
        });
    } else {
        $("#TicketChargeTr").fadeOut("slow", function () {
            $(this).hide();
        });
    }
});

$('input[name="TicketCharge"]').change(function () {
    ticketChargedTo = $(this).val();

});


$('input[name="visaRequired"]').change(function () {
    var IDtick = $(this).val();
    visaRequred = $(this).val();
    if (IDtick == "Y") {
        $("#VisaChargeTr").fadeIn("slow", function () {
            $(this).show();

        });
    } else {
        $("#VisaChargeTr").fadeOut("slow", function () {
            $(this).hide();
        });
    }
});

$('input[name="VisaCharge"]').change(function () {
    visaChargedTo = $(this).val();

});

var dataForm = [];
var jsonText = "";
$("#FmSubmit").click(function () {

    if ($("#FM-DepartureDate_i18n").val().length <= 0 || $("#FM-ReturnDate_i18n").val().length <= 0) {
        $.smallBox({
            title: "Error",
            content: "Start and End Date Required",
            color: "#C46A69",
            iconSmall: "fa fa-times fa-2x fadeInRight animated",
            timeout: 5000
        });

    } else {
        dataForm.push({
            partyId: $("#FM-partyId").val(),
            name: partyName,
            DOB: partyBirthDate,
            departureDate: $("#FM-DepartureDate_i18n").val(),
            returnDate: $("#FM-ReturnDate_i18n").val()
        });

        fillTicketMemerData(dataForm);

        jsonText = JSON.stringify(dataForm);
        console.log(jsonText);

    }
});

function submit() {
   
    $.ajax({
        'async': false,
        type: "post",
        url: "createVacation",
        data: {
            jsonText: jsonText,
            partyId: $("#0_lookupId_partyId").val(),
            partyIdFromPortal :$("#partyIdFromPortal").val(),
//          approverPartyId: $("#1_lookupId_approverPartyId").val(),
            emplLeaveReasonTypeId: $("#EmplLeaveReasonType").val(),
            transDate: $("#Date").val(),
            fromDate: $("#fromDate_i18n").val(),
            thruDate: $("#thruDate_i18n").val(),

            routeFrom: $("#routeFrom").val(),
            routeTo: $("#routeTo").val(),
            noOfDays: $("#noOfDays").val(),
            description: $("#description").val(),
            leaveStatus: $("#leaveStatus").val(),
            type: $("#type").val(),

            ticketRequired: ticketRequred,
            ticketChargeTo: ticketChargedTo,
            visaRequired: visaRequred,
            visaChargeTo: visaChargedTo,
//            FMTicketEligable: FMRequred,

        },
        dataType: "json",
        success: function (data) {
               	
            if ('_ERROR_MESSAGE_' in data) {        
                ErrorMessage(data._ERROR_MESSAGE_);
                 
            } else if ('_ERROR_MESSAGE_LIST_' in data) {
                  ErrorMessage(data._ERROR_MESSAGE_LIST_);
            } else if ('_EVENT_MESSAGE_' in data) {
                SuccessMessage(data._EVENT_MESSAGE_);
//                $('#myTable').find('input[type=text]').each(function () {
//                    $(this).val($(this).attr("data-default"));
//                });
//                $("#leaveStatus").val("LEAVE_CREATED");
//                $("#Date").val(getCurrentDate());
//                dataForm = [];
//                fillTicketMemerData(dataForm);
               window.location.href = "vacationsView?partyId="+$("#0_lookupId_partyId").val();
            }
        }
    }); // End of Ajax Request
    	 
}

function fillTicketMemerData(dataForm) {
//    var dataForm = []
    colNames = ['ID', 'Party ID', 'Name', 'D.O.B', 'Departure Date', 'Return Date'];
    colModel = [{
            key: true,
            name: 'id',
            index: 'id',
            hidden: true,
            editable: false

        }, {
            name: 'partyId',
            index: 'partyId',
            editable: true

        }, {
            name: 'name',
            index: 'name',
            editable: true
        }, {
            name: 'DOB',
            index: 'DOB',
            editable: true
        }, {
            name: 'departureDate',
            index: 'departureDate',
            editable: true
        }, {
            name: 'returnDate',
            index: 'returnDate',
            editable: true
        }];
    getGridData(colNames, colModel, dataForm, "TicketMGrid", "TicketMGridDiv", "Ticket Member", "refId", "asc", 5, "Ticket Member");

} //End of function  



function rowSelected(rowId, gridId) {
    if (gridId == "jqgrid") {
        var rowData = jQuery('#jqgrid').getRowData(rowId);
        References_Delete = rowData['refId'];
    }
}

function firstRowSelected(gridId) {
    if (gridId == "jqgrid") {
    }
}
$(document).ready(function () {

    //    Call Success Response Notifications
    jQuery("#jqgrid").on("jqGridInlineSuccessSaveRow", function (response, request) {
        getNotificationSuccess(response, request);
    });

    jQuery("#jqgrid").on("jqGridInlineAfterSaveRow", function () {

        jQuery("#jqgrid").jqGrid('clearGridData');
        jQuery("#jqgrid").jqGrid('setGridParam', {data: jqgrid_data});
        jQuery("#jqgrid").trigger('reloadGrid');

    });
});





function add() {

    $("#jqgrid").setGridParam({editurl: "AddReference"});

    var CurrentDate_ID = $("input.editable[name='refDate']").attr("id");
    document.getElementById(CurrentDate_ID).value = getCurrentDate();
}
;

function edit() {

    $("#jqgrid").setGridParam({editurl: "updateReferences"});

}

function deleteReferences() {

    $("#jqgrid").setGridParam({editurl: 'deleteReference?refId=' + References_Delete});

}

function RefreshGrid(gridID) {
    if (gridID == "jqgrid") {
        fillPayrollReferenceData();
    }

}




$("#waitMe_ex").click(function(){

	var current_effect ="roundBounce";

	$('#waitMe_ex').click(function(){
		run_waitMe($('.containerBlock'), 1, current_effect);
	});
	$('.waitMe_ex_close').click(function(){
		$('.containerBlock').waitMe('hide');
		$('#waitMe_ex2').waitMe('hide');
		$('#waitMe_ex3').waitMe('hide');
	});

	$('#waitMe_ex_effect').change(function(){
		current_effect = $(this).val();
		run_waitMe($('.containerBlock'), 1, current_effect);
		run_waitMe($('#waitMe_ex2'), 2, current_effect);
		run_waitMe($('#waitMe_ex3'), 3, current_effect);
	});
	
	$('#waitMe_ex_effect').click(function(){
		current_effect = $(this).val();
	});
	
	function run_waitMe(el, num, effect){
		text = 'Please wait...';
		fontSize = '';
		switch (num) {
			case 1:
			maxSize = '';
			textPos = 'vertical';
			break;
			case 2:
			text = '';
			maxSize = 30;
			textPos = 'vertical';
			break;
			case 3:
			maxSize = 30;
			textPos = 'horizontal';
			fontSize = '18px';
			break;
		}
		el.waitMe({
			effect: effect,
			text: text,
			bg: 'rgba(255,255,255,0.7)',
			color: '#000',
			maxSize: maxSize,
			waitTime: -1,
			source: 'img.svg',
			textPos: textPos,
			fontSize: fontSize,
			onClose: function(el) {}
		});
	}
	
	$('#waitMe_ex2').click(function(){
		run_waitMe($(this), 2, current_effect);
	});
	
	$('#waitMe_ex3').click(function(){
		run_waitMe($(this), 3, current_effect);
	});

	var current_body_effect = $('#waitMe_ex_body_effect').val();
	
	$('#waitMe_ex_body').click(function(){
		run_waitMe_body(current_body_effect);
	});
	
	$('#waitMe_ex_body_effect').change(function(){
		current_body_effect = $(this).val();
		run_waitMe_body(current_body_effect);
	});
	

});