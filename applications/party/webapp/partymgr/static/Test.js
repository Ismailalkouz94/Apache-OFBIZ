function costCenterPartyId(screenName) {
    $.ajax({type: 'post', url: 'getPartyId',
        Data: document.getElementsByName('selectStr').value,
        success: function (Data) {
            if (screenName == 'accGLTransaction') {
                document.getElementById('CustomAcctgTransEntry_partyId').innerHTML = Data;
            } else if (screenName == 'CustomEditInvoiceItem') {
                document.getElementById('CustomEditInvoiceItem_partyId').innerHTML = Data;
            }
        }});
}

$(window).load(function () {
  alert('Test JS');

});
