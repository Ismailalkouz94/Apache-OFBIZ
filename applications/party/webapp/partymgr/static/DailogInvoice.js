$(window).load(function () {
    var actionCopy = $('[name="InvoiceSubTabBar_copyInvoice_LF_10_"]').attr("action");
    var actionTemp = $('[name="InvoiceSubTabBar_saveInvoiceAsTemplate_LF_16_"]').attr("action");
//    var invoiceIdToCopyFrom = $('[name="InvoiceSubTabBar_copyInvoice_LF_10_"] > [name="invoiceIdToCopyFrom"]').val();
    var invoiceIdToCopyFrom = sessionStorage.getItem("invoiceIdDailog");

    $("#CopyDailog").attr("href", "javascript:void(0);");
    $("#CopyDailog").wrap("<div  data-toggle='modal' data-target='#myModal'></div>");
    $("#forNavigate").attr("href", "javascript:document.InvoiceSubTabBar_copyInvoice_LF_111_.submit()");
    $("#invoiceIdToCopyFrom").val(invoiceIdToCopyFrom);
    $("#copyDailogInvoice").attr("action", actionCopy);

//    var invoiceId = $('[name="InvoiceSubTabBar_saveInvoiceAsTemplate_LF_16_"] > [name="invoiceId"]').val();
    var invoiceId = sessionStorage.getItem("invoiceIdDailog");
    var invoiceTypeId = $('[name="InvoiceSubTabBar_saveInvoiceAsTemplate_LF_16_"] > [name="invoiceTypeId"]').val();
    $("#copyInvoiceToTemplateDailog").attr("href", "javascript:void(0);");
    $("#copyInvoiceToTemplateDailog").wrap("<div  data-toggle='modal' data-target='#myModalTemp'></div>");
    $("#forNavigateTemp").attr("href", "javascript:document.InvoiceSubTabBar_saveInvoiceAsTemplate_LF_161_.submit()");
    $("#invoiceIdTemp").val(invoiceId);
    $("#invoiceTypeIdTemp").val(invoiceTypeId);
    $("#TempDailogInvoice").attr("action", actionTemp);


//    invoiceDater
    if (document.getElementById('copyDailogInvoice') != null) {
        $("#invoiceDate_i18n").attr("required", "required");
        $("#invoiceDate_i18n").val("");
        $("#invoiceDate_i18n").change(function () {
            var date = new Date($("#invoiceDate_i18n").val());
            $("#yearMenu").val(date.getFullYear());
            sessionStorage.setItem("yearInvoice", date.getFullYear());
        });
    }
    if (document.getElementById('TempDailogInvoice') != null) {
        $("#invoiceDateTemp_i18n").attr("required", "required");
        $("#invoiceDateTemp_i18n").val("");
        $("#invoiceDateTemp_i18n").change(function () {
            var date = new Date($("#invoiceDateTemp_i18n").val());
            $("#yearMenuTemp").val(date.getFullYear());
            sessionStorage.setItem("yearInvoice", date.getFullYear());
        });
    }
});



