var origAmount, description, debitCreditFlag, origCurrencyUomId;
//function onChangeValueFromLookUp(lookup_ID) {
//    if (lookup_ID == "1_lookupId_CustomAcctgTransEntryC_partyId") {
//        var partyId = $("#" + lookup_ID).val();
//        alert(partyId);
//        $.ajax({type: 'post', url: 'getPartyCostCenter',
//            data: 'partyId=' + partyId,
//            success: function (data) {
//                alert(data);
//            }});
//    }
//}
//$("form[name='partyCostCenter']").keypress(function (e) {
//    var key = e.charCode || e.keyCode || 0;
//    if (key == 13) {
//        var partyId = $("#" + lookup_ID).val();
//        alert(partyId);
//        e.preventDefault();
//    }
//});
function costCenterPartyId(screenName) {
    $.ajax({type: 'post', url: 'getPartyId',
        Data: document.getElementsByName('selectStr').value,
        success: function (Data) {
            if (screenName == 'accGLTransaction') {
                $('#2_lookupId_CustomAcctgTransEntryC_partyId').val("");
//                document.getElementById('CustomAcctgTransEntryC_partyId').innerHTML = Data;
            } else if (screenName == 'CustomEditInvoiceItem') {
                document.getElementById('CustomEditInvoiceItem_partyId').innerHTML = Data;
            } else if (screenName == 'AddFinAccountTrans') {
                document.getElementById('AddFinAccountTrans_partyId').innerHTML = Data;
            }
        }});
}
function costCenterGlAccountId(screenName) {
    if (screenName == 'accGLTransaction') {
        $("#CustomAcctgTransEntryidC").show();
        $("#CustomAcctgTransEntryidE").hide();
        $("#CustomAcctgTransEntryidV").hide();
        $("#CustomAcctgTransEntryidG").hide();
        $.ajax({type: 'post', url: 'getGlAccountId',
            Data: document.getElementsByName('selectStr2').value,
            success: function (Data) {
//                document.getElementById('CustomAcctgTransEntry_glAccountId').innerHTML = Data;
            }});//end of ajax
    }//end of if outer
    else if (screenName == 'CustomEditInvoiceItem') {
        $.ajax({type: 'post', url: 'getGlAccountId',
            Data: document.getElementsByName('selectStr2').value,
            success: function (Data) {
                document.getElementById('CustomEditInvoiceItem_overrideGlAccountId').innerHTML = Data;
            }});//end of ajax
    }//end of else if 
    else if (screenName == 'AddFinAccountTrans') {
        $.ajax({type: 'post', url: 'getGlAccountId',
            Data: document.getElementsByName('selectStr2').value,
            success: function (Data) {
                document.getElementById('AddFinAccountTrans_glAccountId').innerHTML = Data;
            }});//end of ajax
    }//end of else if
}
function employeesPartyId(screenName) {
    $.ajax({type: 'post', url: 'getPartyIdEmp',
        Data: document.getElementsByName('selectStr').value,
        success: function (Data) {
            if (screenName == 'accGLTransaction') {
                $('#4_lookupId_CustomAcctgTransEntryE_partyId').val("");
//                document.getElementById('CustomAcctgTransEntryE_partyId').innerHTML = Data;
            } else if (screenName == 'CustomEditInvoiceItem') {
                document.getElementById('CustomEditInvoiceItem_partyId').innerHTML = Data;
            } else if (screenName == 'AddFinAccountTrans') {
                document.getElementById('AddFinAccountTrans_partyId').innerHTML = Data;
            }
            employeesGlAccountId(screenName);

        }});
}
function employeesGlAccountId(screenName) {
    $.ajax({type: 'post', url: 'getGlAccountEmp',
        Data: document.getElementsByName('selectStr').value,
        success: function (Data) {
            if (screenName == 'accGLTransaction') {
                $("#CustomAcctgTransEntryidC").hide();
                $("#CustomAcctgTransEntryidE").show();
                $("#CustomAcctgTransEntryidV").hide();
                $("#CustomAcctgTransEntryidG").hide();
            } else if (screenName == 'CustomEditInvoiceItem') {
                document.getElementById('CustomEditInvoiceItem_overrideGlAccountId').innerHTML = Data;
            } else if (screenName == 'AddFinAccountTrans') {
                document.getElementById('AddFinAccountTrans_glAccountId').innerHTML = Data;
            }
        }});
}
function vendorPartyId(screenName) {
    $.ajax({type: 'post', url: 'getPartyIdVendor',
        Data: document.getElementsByName('selectStr').value,
        success: function (Data) {
            if (screenName == 'accGLTransaction') {
                $('#6_lookupId_CustomAcctgTransEntryV_partyId').val("");
//                document.getElementById('CustomAcctgTransEntryV_partyId').innerHTML = Data;
            } else if (screenName == 'CustomEditInvoiceItem') {
                document.getElementById('CustomEditInvoiceItem_partyId').innerHTML = Data;
            } else if (screenName == 'AddFinAccountTrans') {
                document.getElementById('AddFinAccountTrans_partyId').innerHTML = Data;
            }
            vendorGlAccountId(screenName);
        }});

}
function vendorGlAccountId(screenName) {
    $.ajax({type: 'post', url: 'getGlAccountVendor',
        Data: document.getElementsByName('selectStr').value,
        success: function (Data) {
            if (screenName == 'accGLTransaction') {
                $("#CustomAcctgTransEntryidC").hide();
                $("#CustomAcctgTransEntryidE").hide();
                $("#CustomAcctgTransEntryidV").show();
                $("#CustomAcctgTransEntryidG").hide();
            } else if (screenName == 'CustomEditInvoiceItem') {
                document.getElementById('CustomEditInvoiceItem_overrideGlAccountId').innerHTML = Data;
            } else if (screenName == 'AddFinAccountTrans') {
                document.getElementById('AddFinAccountTrans_glAccountId').innerHTML = Data;
            }
        }});
}
function glAccountPartyId(screenName) {
    $.ajax({type: 'post', url: 'glAccountPartyId',
        Data: document.getElementsByName('selectStr').value,
        success: function (Data) {
            if (screenName == 'accGLTransaction') {
                $('#8_lookupId_CustomAcctgTransEntryG_partyId').val("");
//                document.getElementById('CustomAcctgTransEntryG_partyId').innerHTML = Data;
            } else if (screenName == 'CustomEditInvoiceItem') {
                document.getElementById('CustomEditInvoiceItem_partyId').innerHTML = Data;
            } else if (screenName == 'AddFinAccountTrans') {
                document.getElementById('AddFinAccountTrans_partyId').innerHTML = Data;
            }
        }});

}
function glAccountId(screenName) {
    $.ajax({type: 'post', url: 'getGlAccount',
        Data: document.getElementsByName('selectStr').value,
        success: function (Data) {
            if (screenName == 'accGLTransaction') {
                $("#CustomAcctgTransEntryidC").hide();
                $("#CustomAcctgTransEntryidE").hide();
                $("#CustomAcctgTransEntryidV").hide();
                $("#CustomAcctgTransEntryidG").show()
            } else if (screenName == 'CustomEditInvoiceItem') {
                document.getElementById('CustomEditInvoiceItem_overrideGlAccountId').innerHTML = Data;
            } else if (screenName == 'AddFinAccountTrans') {
                document.getElementById('AddFinAccountTrans_glAccountId').innerHTML = Data;
            }
        }});
}



function partyId_GlAccountId(screenName, checked) {

    var checkedVal = $('input[name=extInfo]:checked').val();
    if (checked != null) {
        checkedVal = checked;
    }

    if (checkedVal == 'C') {
        costCenterPartyId(screenName);
        costCenterGlAccountId(screenName);
        $("#CustomAcctgTransEntryC_origAmount").val(origAmount);
        $("#CustomAcctgTransEntryC_description").val(description);
        $("#CustomAcctgTransEntryC_debitCreditFlag").val(debitCreditFlag);
        if (origCurrencyUomId != null) {
            $("#CustomAcctgTransEntryC_origCurrencyUomId").val(origCurrencyUomId);
        }
        $("input[name=partyIdOther]").val("");
        $("#1_lookupId_CustomAcctgTransEntryC_partyIdOther_lookupDescription").text("");

        $("#0_lookupId_glAccountId1").val("");
        $("#0_lookupId_glAccountId1_lookupDescription").text("");
    }
    if (checkedVal == 'E') {
        employeesPartyId(screenName);
        $("#CustomAcctgTransEntryE_origAmount").val(origAmount);
        $("#CustomAcctgTransEntryE_description").val(description);
        $("#CustomAcctgTransEntryE_debitCreditFlag").val(debitCreditFlag);
        if (origCurrencyUomId != null) {
            $("#CustomAcctgTransEntryE_origCurrencyUomId").val(origCurrencyUomId);
        }
    }
    if (checkedVal == 'V') {
        vendorPartyId(screenName);
        $("#CustomAcctgTransEntryV_origAmount").val(origAmount);
        $("#CustomAcctgTransEntryV_description").val(description);
        $("#CustomAcctgTransEntryV_debitCreditFlag").val(debitCreditFlag);
        if (origCurrencyUomId != null) {
            $("#CustomAcctgTransEntryV_origCurrencyUomId").val(origCurrencyUomId);
        }
    }
    if (checkedVal == 'G') {
        glAccountPartyId(screenName);
        glAccountId(screenName);
        $("#CustomAcctgTransEntryG_origAmount").val(origAmount);
        $("#CustomAcctgTransEntryG_description").val(description);
        $("#CustomAcctgTransEntryG_debitCreditFlag").val(debitCreditFlag);
        if (origCurrencyUomId != null) {
            $("#CustomAcctgTransEntryG_origCurrencyUomId").val(origCurrencyUomId);
        }
        $("input[name=partyIdOther]").val("");
        $("#3_lookupId_CustomAcctgTransEntryG_partyIdOther_lookupDescription").text("");

        $("#2_lookupId_glAccountId1").val("");
        $("#2_lookupId_glAccountId1_lookupDescription").text("");
    }
    $('input[value="' + checkedVal + '"]').attr("checked", "checked");
}
function glId(id) {
    $.ajax({type: 'post', url: 'setGlId',
        data: 'glId=' + id,
        success: function (data) {
        }});
}
function getParameterPartyId(Param, screenName) {
    $.ajax({type: 'post', url: 'getParameter',
        data: 'partyId=' + Param,
        success: function (data) {
            if ($('input[name=extInfo]:checked').val() == 'E') {
                employeesGlAccountId(screenName);
            } else if ($('input[name=extInfo]:checked').val() == 'V') {
                vendorGlAccountId(screenName);
            }
        }});
}
function funDebitCreditFlag(flagDC, screenName) {
    if (flagDC == 'D') {
        if ($('input[name=extInfo]:checked').val() == 'C') {
            costCenterGlAccountId(screenName);
        }
    } else if (flagDC == 'C') {
        if ($('input[name=extInfo]:checked').val() == 'C') {
            $.ajax({type: 'post', url: 'getGlAccountCredit',
                Data: document.getElementsByName('selectStr2').value,
                success: function (Data) {
//                    document.getElementById('CustomAcctgTransEntry_glAccountId').innerHTML = Data;
                }});
        }
    }
}
function GLTrans_onload() {

    $("#CustomEditAcctgTrans_partyId").val("Company");
    $("#CustomEditAcctgTrans_partyId").prop('readonly', true);

    $("#CustomAcctgTransEntryC_origAmount").change(function () {
        origAmount = $("#CustomAcctgTransEntryC_origAmount").val();
    });
    $("#CustomAcctgTransEntryE_origAmount").change(function () {
        origAmount = $("#CustomAcctgTransEntryE_origAmount").val();
    });
    $("#CustomAcctgTransEntryV_origAmount").change(function () {
        origAmount = $("#CustomAcctgTransEntryV_origAmount").val();
    });
    $("#CustomAcctgTransEntryG_origAmount").change(function () {
        origAmount = $("#CustomAcctgTransEntryG_origAmount").val();
    });
    $("#CustomAcctgTransEntryC_description").change(function () {
        description = $("#CustomAcctgTransEntryC_description").val();
    });
    $("#CustomAcctgTransEntryE_description").change(function () {
        description = $("#CustomAcctgTransEntryE_description").val();
    });
    $("#CustomAcctgTransEntryV_description").change(function () {
        description = $("#CustomAcctgTransEntryV_description").val();
    });
    $("#CustomAcctgTransEntryG_description").change(function () {
        description = $("#CustomAcctgTransEntryG_description").val();
    });
    $("#CustomAcctgTransEntryC_debitCreditFlag").change(function () {
        debitCreditFlag = $("#CustomAcctgTransEntryC_debitCreditFlag").val();
    });
    $("#CustomAcctgTransEntryE_debitCreditFlag").change(function () {
        debitCreditFlag = $("#CustomAcctgTransEntryE_debitCreditFlag").val();
    });
    $("#CustomAcctgTransEntryV_debitCreditFlag").change(function () {
        debitCreditFlag = $("#CustomAcctgTransEntryV_debitCreditFlag").val();
    });
    $("#CustomAcctgTransEntryG_debitCreditFlag").change(function () {
        debitCreditFlag = $("#CustomAcctgTransEntryG_debitCreditFlag").val();
    });
    $("#CustomAcctgTransEntryC_origCurrencyUomId").change(function () {
        origCurrencyUomId = $("#CustomAcctgTransEntryC_origCurrencyUomId").val();
    });
    $("#CustomAcctgTransEntryE_origCurrencyUomId").change(function () {
        origCurrencyUomId = $("#CustomAcctgTransEntryE_origCurrencyUomId").val();
    });
    $("#CustomAcctgTransEntryV_origCurrencyUomId").change(function () {
        origCurrencyUomId = $("#CustomAcctgTransEntryV_origCurrencyUomId").val();
    });
    $("#CustomAcctgTransEntryG_origCurrencyUomId").change(function () {
        origCurrencyUomId = $("#CustomAcctgTransEntryG_origCurrencyUomId").val();
    });



    partyId_GlAccountId('accGLTransaction');

    document.getElementById('CustomEditAcctgTrans_organizationPartyName').disabled = true;
    $.ajax({type: 'post', url: 'getOrganizationPartyName',
        data: document.getElementsByName('organizationPartyName').value,
        success: function (data) {
            document.getElementById('CustomEditAcctgTrans_organizationPartyName').value = data;
        }});

    document.getElementById('debitCredit_debit').disabled = true;
    document.getElementById('debitCredit_credit').disabled = true;
    document.getElementById('debitCredit_difference').disabled = true;
    var count = document.getElementsByName('amount').length;


    var sumCredit = 0;
    var sumDebit = 0;
    for (i = 0; i < count; i++) {
        document.getElementsByName('amount')[i].disabled = true;
        document.getElementsByName('glAccountId')[i].disabled = true;
        document.getElementsByName('debitCreditFlag')[i].disabled = true;

        if (document.getElementsByName('debitCreditFlag')[i].value == 'C') {
            sumCredit = sumCredit + parseFloat(document.getElementsByName('amount')[i].value.split(",").join(""));
        } else if (document.getElementsByName('debitCreditFlag')[i].value == 'D') {
            sumDebit = sumDebit + parseFloat(document.getElementsByName('amount')[i].value.split(",").join(""));
        }

    }
    var prevDifference = document.getElementById('debitCredit_difference').value;
    document.getElementById('debitCredit_debit').value = (sumDebit).toFixed(3);
    document.getElementById('debitCredit_credit').value = (sumCredit).toFixed(3);
    document.getElementById('debitCredit_difference').value = (parseFloat(sumDebit) - parseFloat(sumCredit)).toFixed(3);

}

function show() {
    if (document.getElementById('TransactionSelectionForm_Posted').checked == true) {
        document.getElementById('Posted').style.display = 'block';
    }
    if (document.getElementById('TransactionSelectionForm_UnPosted').checked == true) {
        document.getElementById('UnPosted').style.display = 'block';
    }
    if (document.getElementById('TransactionSelectionForm_PostedAndUnposted').checked == true) {
        document.getElementById('PostedAndUnposted').style.display = 'block';
    }
    if (document.getElementById('TransactionSelectionForm_Totals').checked == true) {
        document.getElementById('Posted').style.display = 'block';
        document.getElementById('UnPosted').style.display = 'block';
        document.getElementById('PostedAndUnposted').style.display = 'block';
    }
}
function showCheckBox(check) {
    if (check != 'T') {
        if (document.getElementById('TransactionSelectionForm_Posted').checked == true
                || document.getElementById('TransactionSelectionForm_UnPosted').checked == true
                || document.getElementById('TransactionSelectionForm_PostedAndUnposted').checked == true) {
            document.getElementById('TransactionSelectionForm_Totals').checked = false;
        }
    } else {
        if (document.getElementById('TransactionSelectionForm_Totals').checked == true) {
            document.getElementById('TransactionSelectionForm_PostedAndUnposted').checked = false;
            document.getElementById('TransactionSelectionForm_UnPosted').checked = false;
            document.getElementById('TransactionSelectionForm_Posted').checked = false;
        }
    }
}

$(window).load(function () {
//    debitCredit
    if (document.getElementById('debitCredit') != null) {
        GLTrans_onload();
    }
    if (document.getElementById('CustomEditInvoiceItemId') != null) {
        partyId_GlAccountId('CustomEditInvoiceItem');
    }
    if (document.getElementById('CustomEditInvoiceItems') != null) {
        var count = document.getElementsByName('viewSize').length;
        for (i = 0; i < count; i++) {
            var partyId = 'CustomEditInvoiceItems_partyId_o_' + [i];
            var glId = 'CustomEditInvoiceItems_overrideGlAccountId_o_' + [i]
            document.getElementById(partyId).disabled = true;
            document.getElementById(glId).disabled = true;
        }
    }
    if (document.getElementById('CustomEditInvoiceItem_AR') != null) {
        $.ajax({type: 'post', url: 'getRevenueCostCenterPartyId',
            Data: document.getElementsByName('selectStr').value,
            success: function (Data) {
                document.getElementById('CustomEditInvoiceItem_partyId').innerHTML = Data;
            }});

        $.ajax({type: 'post', url: 'getRevenueCostCenterGlAccountId',
            Data: document.getElementsByName('selectStr2').value,
            success: function (Data) {
                document.getElementById('CustomEditInvoiceItem_overrideGlAccountId').innerHTML = Data;
            }});//end of ajax
    }
    if (document.getElementById('TransactionSelectionForm') != null) {
        document.getElementById('Posted').style.display = 'none';
        document.getElementById('UnPosted').style.display = 'none';
        document.getElementById('PostedAndUnposted').style.display = 'none';
        show();
    }
});
$(document).ready(function () {
//    alert( "ready!" );
});