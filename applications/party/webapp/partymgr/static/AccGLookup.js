if ($('input[name=extInfo]:checked').val() == "E") {
    var partyId = $("#4_lookupId_CustomAcctgTransEntryE_partyId").val();
    $("#LookupGlAccountE_myPartyId").val(partyId);
    $.ajax({
        'async': false,
        type: "post",
        url: "setSessionLookup_partyId",
        data: {Lookup_partyId: partyId},
        dataType: "json",
        success: function (data) {

        }
    });
} else if ($('input[name=extInfo]:checked').val() == "V") {
    var partyId = $("#6_lookupId_CustomAcctgTransEntryV_partyId").val();
    $("#LookupGlAccountV_myPartyId").val(partyId);
    $.ajax({
        'async': false,
        type: "post",
        url: "setSessionLookup_partyId",
        data: {Lookup_partyId: partyId},
        dataType: "json",
        success: function (data) {

        }
    });
}
