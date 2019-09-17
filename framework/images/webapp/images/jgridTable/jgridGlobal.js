
//  get data to show for user And set Data From user

function addGridData_create(colNames, colModel, jqgrid_data, gridId, divID, captionVar, sortName, sortOrder, responseName, colNumber) {
    var colAbove10 = 10;

    if (colNumber == null) {
        colNumber = 5;
    } else {
        if (colNumber > 10) {
            colAbove10 = colNumber + 5;
        }
    }
    var rowNum = 0;
    if (jqgrid_data.length > 100) {
        rowNum = jqgrid_data.length;
    } else {
        rowNum = 100;
    }
    jQuery("#" + gridId).jqGrid({
        datastr: jqgrid_data,
        datatype: "jsonstring",
        height: 'auto',
        colNames: colNames,
        colModel: colModel,

        rowNum: colNumber,
        rowList: [colNumber, colAbove10, 20 + colNumber, 30 + colNumber, rowNum],
        pager: '#' + divID,
        loadonce: true,
        sortname: sortName,
        toolbarfilter: true,
        viewrecords: true,
        rownumbers: true,
        rownumWidth: 20,
        ignoreCase: true,

        onSelectRow: function (rowId) {

            rowSelected(rowId, gridId);
        },
        loadComplete: function () {
            $("td[class='ui-search-input'] > input").attr("placeholder", "Search");
            $("td.ui-search-input  input").addClass("form-control");
        },
        sortorder: sortOrder,
        caption: captionVar,
        multiselect: false,
        autowidth: true
    }
    ).navGrid('#' + divID,
            {edit: false, add: false, del: false, search: false, refresh: true}, {}, {},
            delOption = {
            }, // settings for delete
            {}
    );
//$("#"+gridId).jqGrid('filterToolbar', { stringResult: true, searchOnEnter: false, defaultSearch: "cn" });

    $("#" + gridId).jqGrid('filterToolbar', {placeholder: "Search", searchOnEnter: false, stringResult: true, defaultSearch: "cn"});

    jQuery("#" + gridId).jqGrid('inlineNav', '#' + divID);
    // set CSS Classes To buttons ADD/Edit/Delete ..etc 
    $("#" + gridId + "_iledit").hide();

    /* Add tooltips */



    // remove classes
    $(".ui-jqgrid").removeClass("ui-widget ui-widget-content");
    $(".ui-jqgrid-view").children().removeClass("ui-widget-header ui-state-default");
    $(".ui-jqgrid-labels, .ui-search-toolbar").children().removeClass("ui-state-default ui-th-column ui-th-ltr");
    $(".ui-jqgrid-pager").removeClass("ui-state-default");
    $(".ui-jqgrid").removeClass("ui-widget-content");
    // add classes
//                if($('#'+gridId).height()>149){
//                  $('.ui-jqgrid .ui-jqgrid-bdiv').css({ 'overflow-y': 'scroll' });
//                }             
    $(".ui-jqgrid-htable").addClass("table table-bordered table-hover");
    $(".ui-jqgrid-btable").addClass("table table-bordered table-striped");

    $(".ui-icon.ui-icon-seek-prev").removeClass().addClass("fa fa-backward");
    $(".ui-icon.ui-icon-seek-first").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-first").removeClass().addClass("fa fa-fast-backward");
    $(".ui-icon.ui-icon-seek-next").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-next").removeClass().addClass("fa fa-forward");
    $(".ui-icon.ui-icon-seek-end").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-end").removeClass().addClass("fa fa-fast-forward");



    $(".ui-jqgrid-htable").addClass("table table-bordered table-hover");
    $(".ui-jqgrid-btable").addClass("table table-bordered table-striped");
    $(".ui-pg-div").removeClass().addClass("btn btn-sm btn-primary");
    $(".ui-icon.ui-icon-plus").removeClass().addClass("fa fa-plus");
    $(".ui-icon.ui-icon-pencil").removeClass().addClass("fa fa-pencil");
    $(".ui-icon.ui-icon-trash").removeClass().addClass("fa fa-trash-o");
    $(".ui-icon.ui-icon-search").removeClass().addClass("fa fa-search");
    $(".ui-icon.ui-icon-refresh").removeClass().addClass("fa fa-refresh");
    $(".ui-icon.ui-icon-disk").removeClass().addClass("fa fa-save").parent(".btn-primary").removeClass("btn-primary").addClass("btn-success");
    $(".ui-icon.ui-icon-cancel").removeClass().addClass("fa fa-times").parent(".btn-primary").removeClass("btn-primary").addClass("btn-danger");
    $(".ui-icon.ui-icon-seek-prev").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-prev").removeClass().addClass("fa fa-backward");
    $(".ui-icon.ui-icon-seek-first").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-first").removeClass().addClass("fa fa-fast-backward");
    $(".ui-icon.ui-icon-seek-next").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-next").removeClass().addClass("fa fa-forward");
    $(".ui-icon.ui-icon-seek-end").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-end").removeClass().addClass("fa fa-fast-forward");
    jQuery("#" + gridId).jqGrid('clearGridData');
    jQuery("#" + gridId).jqGrid('setGridParam', {data: jqgrid_data});
    jQuery("#" + gridId).trigger('reloadGrid');



}

function fillGridData(colNames, colModel, jqgrid_data, gridId, divID, captionVar, sortName, sortOrder, responseName, colNumber) {
    var colAbove10 = 10;
    if (colNumber == null) {
        colNumber = 5;
    } else {
        if (colNumber > 10) {
            colAbove10 = colNumber + 5;
        }
    }
    var rowNum = 0;
    if (jqgrid_data.length > 100) {
        rowNum = jqgrid_data.length;
    } else {
        rowNum = 100;
    }
    jQuery("#" + gridId).jqGrid({
        datastr: jqgrid_data,
        datatype: "jsonstring",
        height: 'auto',
        colNames: colNames,
        colModel: colModel,

        rowNum: colNumber,
        rowList: [colNumber, colAbove10, 20 + colNumber, 30 + colNumber, rowNum],
        pager: '#' + divID,
        loadonce: true,
        sortname: sortName,
        toolbarfilter: true,
        viewrecords: true,
        rownumbers: true,
        rownumWidth: 20,
        ignoreCase: true,

        onSelectRow: function (rowId) {

            rowSelected(rowId, gridId);
        },
        loadComplete: function () {
            $("td[class='ui-search-input'] > input").attr("placeholder", "Search");
            $("td.ui-search-input  input").addClass("form-control");
        },
        sortorder: sortOrder,
        caption: captionVar,
        multiselect: false,
        autowidth: true
    }
    ).navGrid('#' + divID,
            {edit: false, add: false, del: true, search: false, refresh: true}, {}, {},
            delOption = {
                caption: "Delete",
                align: "center",
                msg: "Do you want to delete " + responseName + " ?",
                bSubmit: "Delete",
                bCancel: "Cancel",
                mtype: 'POST',
                afterSubmit: function (response) {
                    if ('_ERROR_MESSAGE_' in response.responseJSON) {
                        ErrorMessage(response.responseJSON._ERROR_MESSAGE_);
                    } else if ('_ERROR_MESSAGE_LIST_' in response.responseJSON) {
                        ErrorMessage(response.responseJSON._ERROR_MESSAGE_LIST_);
                    } else if ('_EVENT_MESSAGE_' in response.responseJSON) {
                        SuccessMessage(response.responseJSON._EVENT_MESSAGE_);
                        $("#eData").click();
                        return true;
                    }

                },
                reloadAfterSubmit: true,
                closeAfterSubmit: true
            }, // settings for delete
            {}
    );
//$("#"+gridId).jqGrid('filterToolbar', { stringResult: true, searchOnEnter: false, defaultSearch: "cn" });

    $("#" + gridId).jqGrid('filterToolbar', {placeholder: "Search", searchOnEnter: false, stringResult: true, defaultSearch: "cn"});

    jQuery("#" + gridId).jqGrid('inlineNav', '#' + divID);
    // set CSS Classes To buttons ADD/Edit/Delete ..etc 

    /* Add tooltips */

    $('.navtable .ui-pg-button').tooltip({
        container: 'body'
    });
    jQuery("#m1").click(function () {
        var s;
        s = jQuery("#" + gridId).jqGrid('getGridParam', 'selarrrow');
        alert(s);
    });
    jQuery("#m1s").click(function () {
        jQuery("#" + gridId).jqGrid('setSelection', "13");
    });

    $(window).on('resize.jqGrid', function () {
        $("#" + gridId).jqGrid('setGridWidth', $("#content").width());
    })


    // remove classes
    $(".ui-jqgrid").removeClass("ui-widget ui-widget-content");
    $(".ui-jqgrid-view").children().removeClass("ui-widget-header ui-state-default");
    $(".ui-jqgrid-labels, .ui-search-toolbar").children().removeClass("ui-state-default ui-th-column ui-th-ltr");
    $(".ui-jqgrid-pager").removeClass("ui-state-default");
    $(".ui-jqgrid").removeClass("ui-widget-content");
    // add classes
//                if($('#'+gridId).height()>149){
//                  $('.ui-jqgrid .ui-jqgrid-bdiv').css({ 'overflow-y': 'scroll' });
//                }             
    $(".ui-jqgrid-htable").addClass("table table-bordered table-hover");
    $(".ui-jqgrid-btable").addClass("table table-bordered table-striped");
    $(".ui-pg-div").removeClass().addClass("btn btn-sm btn-primary");
    $(".ui-icon.ui-icon-plus").removeClass().addClass("fa fa-plus");
    $(".ui-icon.ui-icon-pencil").removeClass().addClass("fa fa-pencil");
    $(".ui-icon.ui-icon-trash").removeClass().addClass("fa fa-trash-o");
    $(".ui-icon.ui-icon-search").removeClass().addClass("fa fa-search");
    $(".ui-icon.ui-icon-refresh").removeClass().addClass("fa fa-refresh");
    $(".ui-icon.ui-icon-disk").removeClass().addClass("fa fa-save").parent(".btn-primary").removeClass("btn-primary").addClass("btn-success");
    $(".ui-icon.ui-icon-cancel").removeClass().addClass("fa fa-times").parent(".btn-primary").removeClass("btn-primary").addClass("btn-danger");
    $(".ui-icon.ui-icon-seek-prev").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-prev").removeClass().addClass("fa fa-backward");
    $(".ui-icon.ui-icon-seek-first").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-first").removeClass().addClass("fa fa-fast-backward");
    $(".ui-icon.ui-icon-seek-next").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-next").removeClass().addClass("fa fa-forward");
    $(".ui-icon.ui-icon-seek-end").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-end").removeClass().addClass("fa fa-fast-forward");


    jQuery("#" + gridId).jqGrid('clearGridData');
    jQuery("#" + gridId).jqGrid('setGridParam', {data: jqgrid_data});
    jQuery("#" + gridId).trigger('reloadGrid');



}


//      grid to getData and view to user 
function getGridData(colNames, colModel, jqgrid_data, gridId, divID, captionVar, sortName, sortOrder, colNumber) {
    var rowNum = 0;
    if (jqgrid_data.length > 100) {
        rowNum = jqgrid_data.length;
    } else {
        rowNum = 100;
    }
    var colAbove10 = 10;
    if (colNumber == null) {
        colNumber = 5;
    } else if (colNumber > 10)
    {
        colAbove10 = colNumber + 5;
    }


    jQuery("#" + gridId).jqGrid({
        datastr: jqgrid_data,
        datatype: "jsonstring",
        height: 'auto',
        colNames: colNames,
        colModel: colModel,
        pager: '#' + divID,
        rowNum: colNumber,
        rowList: [colNumber, colAbove10, 20 + colNumber, 30 + colNumber, rowNum],
        sortname: sortName,
//      toolbarfilter : true,
        viewrecords: true,
        sortorder: sortOrder,
        caption: captionVar,
        multiselect: false,
        autowidth: true,
        rownumbers: true,
        rownumWidth: 20,
//      scrollbar:true,
        ignoreCase: true,
        onSelectRow: function (rowId) {

            rowSelected(rowId, gridId);
        }, loadComplete: function () {
            $("td[class='ui-search-input'] > input").attr("placeholder", "Search");
            $("td.ui-search-input  input").addClass("form-control");
        }

    });



    $("#" + gridId).jqGrid('filterToolbar', {searchOnEnter: false, stringResult: true, defaultSearch: "cn"

    });
    jQuery("#" + gridId).jqGrid('clearGridData');
    jQuery("#" + gridId).jqGrid('setGridParam', {data: jqgrid_data});
    jQuery("#" + gridId).trigger('reloadGrid');


//     if($('#'+gridId).height()>160){
//       $('.ui-jqgrid .ui-jqgrid-bdiv').css({ 'overflow-y': 'scroll' });
//     }
    // add classes
    $(".ui-jqgrid-htable").addClass("table table-bordered table-hover");
    $(".ui-jqgrid-btable").addClass("table table-bordered table-striped");
    $(".ui-icon.ui-icon-seek-prev").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-prev").removeClass().addClass("fa fa-backward");
    $(".ui-icon.ui-icon-seek-first").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-first").removeClass().addClass("fa fa-fast-backward");
    $(".ui-icon.ui-icon-seek-next").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-next").removeClass().addClass("fa fa-forward");
    $(".ui-icon.ui-icon-seek-end").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-end").removeClass().addClass("fa fa-fast-forward");
    $("#" + divID + "_left").hide();

}



//with delete button just
function deleteGridData(colNames, colModel, jqgrid_data, gridId, divID, captionVar, sortName, sortOrder, responseName, colNumber) {
    var rowNum = 0;
    if (jqgrid_data.length > 100) {
        rowNum = jqgrid_data.length;
    } else {
        rowNum = 100;
    }
    var colAbove10 = 10;
    if (colNumber == null) {
        colNumber = 5;
    } else if (colNumber > 10)
    {
        colAbove10 = colNumber + 5;
    }


    jQuery("#" + gridId).jqGrid({
        datastr: jqgrid_data,
        datatype: "jsonstring",
        height: 'auto',
        colNames: colNames,
        colModel: colModel,
        pager: '#' + divID,
        rowNum: colNumber,
        rowList: [colNumber, colAbove10, 20 + colNumber, 30 + colNumber, rowNum],
        sortname: sortName,

//             toolbarfilter : true,
        viewrecords: true,
        sortorder: sortOrder,
        caption: captionVar,
        multiselect: false,
        autowidth: true,
        rownumbers: true,
        rownumWidth: 20,
//scrollbar:true,
        ignoreCase: true,
        onSelectRow: function (rowId) {

            rowSelected(rowId, gridId);
        }, loadComplete: function () {
            $("td[class='ui-search-input'] > input").attr("placeholder", "Search");
            $("td.ui-search-input  input").addClass("form-control");
        }

    }).navGrid('#' + divID,
            {edit: false, add: false, del: true, search: false, refresh: true}, {}, {},
            delOption = {
                caption: "Delete",
                align: "center",
                msg: "Do you want to delete " + responseName + " ?",
                bSubmit: "Delete",
                bCancel: "Cancel",
                mtype: 'POST',
                afterSubmit: function (response) {

                    if ('_ERROR_MESSAGE_' in response.responseJSON) {
                        ErrorMessage(response.responseJSON._ERROR_MESSAGE_);
                    } else if ('_ERROR_MESSAGE_LIST_' in response.responseJSON) {
                        ErrorMessage(response.responseJSON._ERROR_MESSAGE_LIST_);
                    } else if ('_EVENT_MESSAGE_' in response.responseJSON) {
                        SuccessMessage(response.responseJSON._EVENT_MESSAGE_);
                        $("#eData").click();
                        afterDeleteEvents();
                        return true;
                    }
//     afterDelete(response);

                },
                reloadAfterSubmit: true,
                closeAfterSubmit: true}, // settings for delete
            {}
    );



    $("#" + gridId).jqGrid('filterToolbar', {searchOnEnter: false, stringResult: true, defaultSearch: "cn"

    });
    jQuery("#" + gridId).jqGrid('clearGridData');
    jQuery("#" + gridId).jqGrid('setGridParam', {data: jqgrid_data});
    jQuery("#" + gridId).trigger('reloadGrid');


//     if($('#'+gridId).height()>160){
//       $('.ui-jqgrid .ui-jqgrid-bdiv').css({ 'overflow-y': 'scroll' });
//     }
    // add classes
    $(".ui-pg-div").removeClass().addClass("btn btn-sm btn-primary");
    $(".ui-icon.ui-icon-plus").removeClass().addClass("fa fa-plus");
    $(".ui-icon.ui-icon-pencil").removeClass().addClass("fa fa-pencil");
    $(".ui-icon.ui-icon-trash").removeClass().addClass("fa fa-trash-o");
    $(".ui-icon.ui-icon-search").removeClass().addClass("fa fa-search");
    $(".ui-icon.ui-icon-refresh").removeClass().addClass("fa fa-refresh");

    $(".ui-jqgrid-htable").addClass("table table-bordered table-hover");
    $(".ui-jqgrid-btable").addClass("table table-bordered table-striped");
    $(".ui-icon.ui-icon-seek-prev").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-prev").removeClass().addClass("fa fa-backward");
    $(".ui-icon.ui-icon-seek-first").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-first").removeClass().addClass("fa fa-fast-backward");
    $(".ui-icon.ui-icon-seek-next").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-next").removeClass().addClass("fa fa-forward");
    $(".ui-icon.ui-icon-seek-end").wrap("<div class='btn btn-sm btn-default'></div>");
    $(".ui-icon.ui-icon-seek-end").removeClass().addClass("fa fa-fast-forward");

}




//   get Date Picker
function date() {
    return  function (elem) {
        $(elem).datepicker({
            changeYear: true,
            changeMonth: true,
            buttonImageOnly: true,
            yearRange: "-120:+50",
            dateFormat: 'yy-mm-dd'
        });
    }

}

function getNotificationSuccess(response, request) {
    console.log(request.responseJSON);
    if ('_ERROR_MESSAGE_' in request.responseJSON) {
        ErrorMessage(request.responseJSON._ERROR_MESSAGE_);
    } else if ('_ERROR_MESSAGE_LIST_' in request.responseJSON) {
        ErrorMessage(request.responseJSON._ERROR_MESSAGE_LIST_);
    } else if ('_EVENT_MESSAGE_' in request.responseJSON) {
        SuccessMessage(request.responseJSON._EVENT_MESSAGE_);

        return [true, ""];
    }

//       / getNotificationError(response, request)

}

function getNotificationError(response, request) {

    // dont delete comment
    //notOfbizErrorMessage    
    var first = (request.responseText).indexOf("<i id='ofbizErrorMessage'>");
    var last = (request.responseText).indexOf("<i id='ofbizErrorMessage1'></i>");
    var flag = (request.responseText).indexOf("//notOfbizErrorMessage");
    var result = (request.responseText).substring(first, last);
//          console.log(result);
//          alert(first +" "+  flag);

    if (result.includes("ORA-")) {
        first = (result).indexOf("ORA-");
        last = (result).indexOf("&lt;");
        result = (result).substring(first, last);
    }
    $.smallBox({
        title: "Error",
        content: result,
        color: "#C46A69",
        iconSmall: "fa fa-times fa-2x fadeInRight animated",
        timeout: 5000
    });
    //end if error
    return [false, ""];
}

function getCurrentDate() {
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!
    var yyyy = today.getFullYear();

    if (dd < 10) {
        dd = '0' + dd
    }

    if (mm < 10) {
        mm = '0' + mm
    }
    return today = yyyy + '-' + mm + '-' + dd;
}





function dateTime() {
    return  function (elem) {
        $(elem).datetimepicker({
            timeFormat: 'HH:mm:ss',
            changeYear: true,
            changeMonth: true,
            buttonImageOnly: true,
            yearRange: "-120:+50",
            dateFormat: 'yy-mm-dd',
            pickSeconds: true
        });
    }
}


function time() {
    return  function (elem) {
        $(elem).timepicker({
            timeFormat: 'HH:mm:ss',
            changeYear: true,
            changeMonth: true,
            buttonImageOnly: true,
            yearRange: "-120:+50",
            dateFormat: 'yy-mm-dd',
            pickSeconds: true
        });
    }
}