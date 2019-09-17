
$(window).load(function () {
//    alert("ssssssss");
    $.ajax({type: 'post', url: 'getOrganizationId',
        data: document.getElementsByName('selectStr').value,
        success: function (data) {
            document.getElementById('CustomCreateAcctgTrans_organizationPartyId').innerHTML = data;
        }});
    
    $("#CustomCreateAcctgTrans_partyId").val("Company");
    $("#CustomCreateAcctgTrans_partyId").prop('readonly', true);

});
//$(document).ready(function () {
//    $("#CustomCreateAcctgTrans_transactionDate_i18n").change(function () {
//        var date = new Date($("#CustomCreateAcctgTrans_transactionDate_i18n").val());
//        $("#CustomCreateAcctgTrans_year").val(date.getFullYear());
//    });
//});


