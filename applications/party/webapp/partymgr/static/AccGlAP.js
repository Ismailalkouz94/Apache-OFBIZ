function accAPInvoiceItems_onload() {
//    document.getElementById('partyIdTo').disabled = true;
    $("#partyIdTo").prop('disabled', true);
    organizationId = $("#partyIdTo").val();
    $.ajax({type: 'post', url: 'getParameterOrganizationId',
        data: 'organizationId=' + organizationId,
        success: function (data) {
        }});


    $(document).ready(function () {
        invoiceDate = document.getElementById('EditInvoice_invoiceDate_i18n').value;
        dueDate = document.getElementById('EditInvoice_dueDate_i18n').value;


//  if(dueDate.length == 0 && window.location.pathname != "/ap/control/editInvoice" && window.location.pathname != "/ap/control/updateInvoice" ){
        if (dueDate.length == 0 && (window.location.pathname == "/ap/control/createInvoice" || window.location.pathname == "/accounting/control/createInvoice")) {


            partyId = document.getElementById('0_lookupId_partyIdFrom').value
            $.ajax({type: 'post', url: 'getNoOfDaysAP',
                data: 'partyId=' + partyId + "&invoiceDate=" + invoiceDate + "&dueDate=" + dueDate,
                dataType: "json",
                success: function (data) {

                    document.getElementById('EditInvoice_dueDate_i18n').value = data[0].dueDate;
                    document.getElementById('EditInvoice_dueDate').value = data[0].timeStamp;
                }
            });
        }
    });
}
//  ------------------------------------------------
//Ahmad Rbab'ah -- Invoice Due Date



function findVendorsReport() {

    if ($('input[name=flagBeginBalance]:checked').val() == "W")
        document.getElementById("FindVendorsReport_fromDate_i18n").required = true;
    else
        document.getElementById("FindVendorsReport_fromDate_i18n").required = false;
}

$(window).load(function () {

    if (document.getElementById('EditInvoiceId') != null) {
        if (document.getElementById('partyIdFromAR') == null) {
            accAPInvoiceItems_onload();
        }
    }
    if (document.getElementById('CustomEditInvoiceItemId') != null) {
        if (document.getElementById('CustomlistInvoiceItemsAP') != null) {
            //           document.getElementById('CustomEditInvoiceItemId').childNodes[17].childNodes[1].childNodes[10].childNodes[3].childNodes[1].childNodes[1].classList.add('selected');
            document.querySelector('[class*="button-bar tab-bar"]').childNodes[1].childNodes[1].childNodes[5].classList.add('selected');

        }
    }

});



//    
//   $(document).on('change','#EditInvoice_invoiceDate_i18n',function(){
//    alert('Id:'+this.id+'\nName:'+this.name+'\nValue:'+this.value);
//});