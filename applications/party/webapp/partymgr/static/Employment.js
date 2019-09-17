$(window).load(function () {
    if (document.getElementById("EditEmployment") != null) {
        if (document.getElementById("EditEmployment_thruDate_i18n").value != "") {
         document.getElementById("EditEmployment_thruDate_i18n").disabled= true;
         document.getElementById("EditEmployment_terminationReasonId").disabled= true;
         document.getElementById("EditEmployment_terminationTypeId").disabled= true;
        $("#EditEmployment > table > tbody > tr > td > input[name='submitButton']").css('display', "none");
            
        }
    }

});



