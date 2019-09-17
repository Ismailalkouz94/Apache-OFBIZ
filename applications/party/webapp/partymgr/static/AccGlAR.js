function accARInvoiceItems_onload() {
    document.getElementById('partyIdFromAR').disabled = true;
    organizationId = document.getElementById('partyIdFromAR').value;
    $.ajax({type: 'post', url: 'getParameterOrganizationId',
        data: 'organizationId=' + organizationId,
        success: function (data) {
        }});
    
   
 
     $( document ).ready(function() {
    invoiceDate = document.getElementById('EditInvoice_invoiceDate_i18n').value;
    dueDate =document.getElementById('EditInvoice_dueDate_i18n').value;
  
  
  if(dueDate.length == 0 && (window.location.pathname == "/ar/control/createInvoice" || window.location.pathname == "/accounting/control/createInvoice" )){
   partyId = document.getElementById('0_lookupId_partyIdToAR').value
    $.ajax({type: 'post', url: 'getNoOfDaysAR',
        data: 'partyId=' + partyId+"&invoiceDate="+invoiceDate+"&dueDate="+dueDate,
        dataType: "json",
        success: function (data) {
            
                 document.getElementById('EditInvoice_dueDate_i18n').value = data[0].dueDate;
                    document.getElementById('EditInvoice_dueDate').value = data[0].timeStamp;
        }
    });}
    });
 
}

function  customerPartyId() {
    $.ajax({type: 'post', url: 'getPartyIdCustomer',
        Data: document.getElementsByName('selectStr').value,
        success: function (Data) {
            if (document.getElementById('EditInvoiceId') != null) {
                document.getElementById('partyIdToAR').innerHTML = Data;
            } else if (document.getElementById('NewSalesInvoice') != null) {
                document.getElementById('NewSalesInvoice_partyIdTo').innerHTML = Data;
            }
        }});
}


$(window).load(function () {

    if (document.getElementById('NewSalesInvoice') != null
            || document.getElementById('EditInvoiceId') != null) {
//        customerPartyId();
    }
    if (document.getElementById('EditInvoiceId') != null) {
        if (document.getElementById('0_lookupId_partyIdToAR') != null) {
            accARInvoiceItems_onload();
        }
    }


});
