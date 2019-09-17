$(window).load(function () {
//    var actionCopy = $('[name="InvoiceSubTabBar_copyInvoice_LF_10_"]').attr("action");
//    var invoiceIdToCopyFrom = sessionStorage.getItem("invoiceIdDailog");
    var acctgTransId = $("#CustomEditAcctgTrans_acctgTransId").val();
    if (acctgTransId) {
    } else {
        acctgTransId = getParameterByName("acctgTransId", $("#acctgTransDetailReportPdf").attr("href"));
    }
    $("#DuplicateAccountingTransaction").attr("href", "javascript:void(0);"); // 1
    $("#DuplicateAccountingTransaction").wrap("<div  data-toggle='modal' data-target='#myModalTrans'></div>"); // 1
    $("#forNavigateTrans").attr("href", "javascript:document.EditGlAcctgTransSubTabBar_DuplicateAccountingTransaction.submit()");
    $("#fromAcctgTransId").val(acctgTransId);
//    $("#TempDailogTrans").attr("action", actionCopy);

    $("#RevertAccountingTransaction").attr("href", "javascript:void(0);"); // 1
    $("#RevertAccountingTransaction").wrap("<div  data-toggle='modal' data-target='#myModalTransRevert'></div>"); // 1
    $("#forNavigateTransRevert").attr("href", "javascript:document.EditGlAcctgTransSubTabBar_RevertAccountingTransaction.submit()");
    $("#fromAcctgTransIdRevert").val(acctgTransId);



    if (document.getElementById('TempDailogTrans') != null) {
        $("#transactionDateTrans_i18n").attr("required", "required");
        $("#transactionDateTrans_i18n").val("");
        $("#transactionDateTrans_i18n").change(function () {
            var date = new Date($("#transactionDateTrans_i18n").val());
            $("#yearMenuTrans").val(date.getFullYear());
//            sessionStorage.setItem("yearInvoice", date.getFullYear());
        });
    }
    if (document.getElementById('TempDailogTransRevert') != null) {
        $("#transactionDateTransRevert_i18n").attr("required", "required");
        $("#transactionDateTransRevert_i18n").val("");
        $("#transactionDateTransRevert_i18n").change(function () {
            var date = new Date($("#transactionDateTransRevert_i18n").val());
            $("#yearMenuTransRevert").val(date.getFullYear());
//            sessionStorage.setItem("yearInvoice", date.getFullYear());
        });
    }

});


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
