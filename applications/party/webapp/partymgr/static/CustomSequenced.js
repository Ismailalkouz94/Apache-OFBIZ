
$(document).ready(function () {
    // gl transaction
    if (document.getElementById('CustomCreateAcctgTrans') != null) {
        $("#CustomCreateAcctgTrans_transactionDate_i18n").change(function () {
            var date = new Date($("#CustomCreateAcctgTrans_transactionDate_i18n").val());
            $("#CustomCreateAcctgTrans_year").val(date.getFullYear());
        });
    } else if (document.getElementById('CustomEditAcctgTrans') != null) {
        var oldDate = $("#CustomEditAcctgTrans_transactionDate_i18n").val();
        var date = new Date($("#CustomEditAcctgTrans_transactionDate_i18n").val());
        var yearOfDate = date.getFullYear();
        $("#CustomEditAcctgTrans_transactionDate_i18n").change(function () {
            var newDate = new Date($("#CustomEditAcctgTrans_transactionDate_i18n").val());
            newDate = newDate.getFullYear();
            if (yearOfDate != newDate) {
                $.smallBox({
                    title: "Wrong",
                    content: "Date must be in the same year (" + yearOfDate + ")",
                    color: "#5384AF",
                    timeout: 3000,
                    icon: "fa fa-bell"
                });
                $("#CustomEditAcctgTrans_transactionDate_i18n").val(oldDate);
                $("#CustomEditAcctgTrans_transactionDate").val(oldDate);
            }
        });
    }

    //Quick Create an Accounting Transaction
    if (document.getElementById('CreateAcctgTransAndEntries') != null) {
        $("#CreateAcctgTransAndEntries_transactionDate_i18n").change(function () {
            var date = new Date($("#CreateAcctgTransAndEntries_transactionDate_i18n").val());
            $("#CreateAcctgTransAndEntries_year").val(date.getFullYear());
        });
    } else if (document.getElementById('EditAcctgTrans') != null) {
        var oldDate = $("#EditAcctgTrans_transactionDate_i18n").val();
        var date = new Date($("#EditAcctgTrans_transactionDate_i18n").val());
        var yearOfDate = date.getFullYear();
        $("#EditAcctgTrans_transactionDate_i18n").change(function () {
            var newDate = new Date($("#EditAcctgTrans_transactionDate_i18n").val());
            newDate = newDate.getFullYear();
            if (yearOfDate != newDate) {
                $.smallBox({
                    title: "Wrong",
                    content: "Date must be in the same year (" + yearOfDate + ")",
                    color: "#5384AF",
                    timeout: 3000,
                    icon: "fa fa-bell"
                });
                $("#EditAcctgTrans_transactionDate_i18n").val(oldDate);
                $("#EditAcctgTrans_transactionDate").val(oldDate);
            }
        });
    }

//   newAcctgTrans
    if (document.getElementById('CreateAcctgTrans') != null) {
        $("#CreateAcctgTrans_transactionDate_i18n").change(function () {
            var date = new Date($("#CreateAcctgTrans_transactionDate_i18n").val());
            $("#CreateAcctgTrans_year").val(date.getFullYear());
        });
    }

//  New Purchase Invoice AP
    if (document.getElementById('NewPurchaseInvoice') != null) {
        $("#NewPurchaseInvoice_invoiceDate_i18n").attr("required", "required");
        $("#NewPurchaseInvoice_invoiceDate_i18n").change(function () {
            var date = new Date($("#NewPurchaseInvoice_invoiceDate_i18n").val());
            $("#NewPurchaseInvoice_year").val(date.getFullYear());
            sessionStorage.setItem("yearInvoice", date.getFullYear());
        });
    } else if (document.getElementById('EditInvoiceId') != null) {
        sessionStorage.setItem("invoiceIdDailog", $('[name="invoiceId"]').val());
        var oldDate = $("#EditInvoice_invoiceDate_i18n").val();
        var date = new Date($("#EditInvoice_invoiceDate_i18n").val());
        var yearOfDate = date.getFullYear();
        $("#EditInvoice_year").val(yearOfDate);
        $("#EditInvoice_invoiceDate_i18n").change(function () {
            var newDate = new Date($("#EditInvoice_invoiceDate_i18n").val());
            newDate = newDate.getFullYear();
            if (yearOfDate != newDate) {
                $.smallBox({
                    title: "Wrong",
                    content: "Date must be in the same year (" + yearOfDate + ")",
                    color: "#5384AF",
                    timeout: 3000,
                    icon: "fa fa-bell"
                });
                $("#EditInvoice_invoiceDate_i18n").val(oldDate);
                $("#EditInvoice_invoiceDate").val(oldDate);
            }
        });
    }

    //  New Sales Invoice AR
    if (document.getElementById('NewSalesInvoice') != null) {
        $("#NewSalesInvoice_invoiceDate_i18n").attr("required", "required");
        $("#NewSalesInvoice_invoiceDate_i18n").change(function () {
            var date = new Date($("#NewSalesInvoice_invoiceDate_i18n").val());
            $("#NewSalesInvoice_year").val(date.getFullYear());
            sessionStorage.setItem("yearInvoice", date.getFullYear());
        });
    } else if (document.getElementById('EditInvoiceId') != null) {
        sessionStorage.setItem("invoiceIdDailog", $('[name="invoiceId"]').val());
        var oldDate = $("#EditInvoice_invoiceDate_i18n").val();
        var date = new Date($("#EditInvoice_invoiceDate_i18n").val());
        var yearOfDate = date.getFullYear();
        $("#EditInvoice_year").val(yearOfDate);
        $("#EditInvoice_invoiceDate_i18n").change(function () {
            var newDate = new Date($("#EditInvoice_invoiceDate_i18n").val());
            newDate = newDate.getFullYear();
            if (yearOfDate != newDate) {
                $.smallBox({
                    title: "Wrong",
                    content: "Date must be in the same year (" + yearOfDate + ")",
                    color: "#5384AF",
                    timeout: 3000,
                    icon: "fa fa-bell"
                });
                $("#EditInvoice_invoiceDate_i18n").val(oldDate);
                $("#EditInvoice_invoiceDate").val(oldDate);
            }
        });
    }

  // payment AP
    if (document.getElementById('NewPaymentOut') != null) {
        $("#NewPaymentOut_effectiveDate_i18n").change(function () {
            var date = new Date($("#NewPaymentOut_effectiveDate_i18n").val());
            $("#NewPaymentOut_year").val(date.getFullYear());
        });
    } else if (document.getElementById('EditPayment') != null) {
        var oldDate = $("#EditPayment_effectiveDate_i18n").val();
        var date = new Date($("#EditPayment_effectiveDate_i18n").val());
        var yearOfDate = date.getFullYear();
        $("#EditPayment_effectiveDate_i18n").change(function () {
            var newDate = new Date($("#EditPayment_effectiveDate_i18n").val());
            newDate = newDate.getFullYear();
            if (yearOfDate != newDate) {
                $.smallBox({
                    title: "Wrong",
                    content: "Date must be in the same year (" + yearOfDate + ")",
                    color: "#5384AF",
                    timeout: 3000,
                    icon: "fa fa-bell"
                });
                $("#EditPayment_effectiveDate_i18n").val(oldDate);
                $("#EditPayment_effectiveDate").val(oldDate);
            }
        });
    }
    //payment AR
    if (document.getElementById('NewPaymentIn') != null) {
        $("#NewPaymentIn_effectiveDate_i18n").change(function () {
            var date = new Date($("#NewPaymentIn_effectiveDate_i18n").val());
            $("#NewPaymentIn_year").val(date.getFullYear());
        });
    }
    
});
function invoiceOverview() {
    //  invoice Overview
    if (document.getElementById('accountingInvoiceHeader') != null) {
        var invoiceId = getParameterByName('invoiceId');
        if (invoiceId != null) {
            $.ajax({
                'async': false,
                type: "post",
                url: "getDateOfInvoice",
                data: {invoiceId: invoiceId},
                success: function (data) {
                    var date = new Date(data);
                    sessionStorage.setItem("yearInvoice", date.getFullYear());
                    sessionStorage.setItem("invoiceIdDailog", invoiceId);
                }
            });
        } else {
            var invoiceOverviewHref = $("#invoiceOverview").attr("href");
            invoiceId = getParameterByName('invoiceId', invoiceOverviewHref);
            if (invoiceId != null) {
                $.ajax({
                    'async': false,
                    type: "post",
                    url: "getDateOfInvoice",
                    data: {invoiceId: invoiceId},
                    success: function (data) {
                        var date = new Date(data);
                        sessionStorage.setItem("yearInvoice", date.getFullYear());
                        sessionStorage.setItem("invoiceIdDailog", invoiceId);
                    }
                });
            }
        }
    }
}

function getParameterByName(name, url) {
    if (!url)
        url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
    if (!results)
        return null;
    if (!results[2])
        return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}