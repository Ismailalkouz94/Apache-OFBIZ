<#ftl encoding="utf-8">
<#assign data = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getApproverActionData(request, response)>
<#assign instanceProcessId = session.getAttribute('processInstanceId') />


<#assign processName = session.getAttribute('processName') />

<#assign paymentId= "${data.get('paymentId')}" />

           <#assign partyID= "${data.get('partyId')}" />

<#assign description = "${data.get('description')}" />


${session.setAttribute('partyId',"${(partyID)!''}")}
${session.setAttribute('paymentId',"${(paymentId)!''}")}




    <div class="row">
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
            <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
                <header>
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2>Payment [ ${(paymentId)!''} ]</h2>
                    </header>
                <div role="content">

                    <div  class="table-responsive">
                        <form name="activiti" method="post" action="<@ofbizUrl>completeTask</@ofbizUrl>"  class="form-style-9" >

                            <table width="100%">

                                    <tr>
                                    <td><label> Party ID : </label></td>
                                    <td>
                                        <input class="form-control" type="text" name="partyId" id="partyId"  value="${(partyID)!''}" class="email" required="true" disabled />
                                        </td>
                                    <td>
                                        <label id="partyName">
                                            </label>
                                        </td>

                                    </tr>

                                <tr>
                                    <td><label>  Payment ID :</label>  </td>
                                    <td>
                                        <input class="form-control" type="text" name="paymentId" id="paymentId"  value="${(paymentId)!''}" class="email" required="true" disabled/>
                                        </td>
                                    
                                    <td> <label> description : </label> </td>
                                     <td>
                                        <input class="form-control" type="text" name="description" id="description"  value="${(description)!''}" class="email" required="true" disabled/>
                                        </td>
                                    </tr>
                                <tr>
                                    <td> <label> Total Amount :</label>  </td>
                                    <td>
                                        <input class="form-control" type="text" name="totalAmount" id="totalAmount"   value="" class="email" required="true" disabled />
                                        </td>
                                  

                             
                                    <td> <label> Amount Due :</label> </td>
                                    <td width="25%">
                                        <input class="form-control" type="text" name="amountDue" id="amountDue"  value="" class="email" required="true"  disabled/>
                                        </td>
                                    </tr>
                                <tr>
                        
                                    <td> <label>Payment Amount :</label> </td>
                                    <td width="25%">
                                        <input class="form-control" type="text" name="paymentAmount" id="paymentAmount"  value="" class="email" required="true"  disabled/>
                                        </td>
                                   

                                    </tr>
                              
                                
                                </table>
                            
                            </form>

                        </div>
                    </div>
                </div>
            </article>
        </div>
    <script>
     
$.ajax({
type: "post",
url: "getPartyName",
data: "partyId=" + '${partyID}',
success: function (data) {
document.getElementById('partyName').innerHTML= data.trim();
}
});

$.ajax({
type: "post",
url: "getTotal",
data: "partyId=" + '${partyID}',
success: function (data) {
document.getElementById("totalAmount").value = data.trim(); 
}
});

$.ajax({
type: "post",
url: "getAmountApplied",
data: "paymentId=" + '${paymentId}',
success: function (data) {

document.getElementById("paymentAmount").value = data.trim(); 

}
});




$.ajax({
type: "post",
url: "getAmountDue",
data: "partyId=" + '${partyID}',
success: function (data) {
document.getElementById("amountDue").value = data.trim(); 

}
});
        </script>
  <#assign invoiceDetails = Static["org.apache.ofbiz.accounting.workFlow.AccountingWorkflow"].getInvoiceDetails(request, response) />

    <#assign paymentApp = Static["org.apache.ofbiz.accounting.workFlow.AccountingWorkflow"].getPaymentApp(request, response) />
    <div class="row">
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable"">
            <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
                <header>
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2>Payment Amount</h2>
                    </header>

                <div role="content">


                    <div header="Payment Applications" id="yyy" style="width:100%" align="center">
                        <table cellpaddin="0" cellspacing="0" id="tab101" class="table table-bordered table-striped"> 
                            <thead>
                                <tr>
                                    <th><label>Payment ID</label></th>
                                    <th><label>Invoice ID </label></th>
                                    <th><label>Description</label></th>
                                    <th><label> Amount Applied </label></th>
                                    </tr>
                                </thead>

                  <#list  paymentApp as data>

                            <tr>
                                <td> ${(data.PAYMENT_ID)!}</td>
                                <td> ${(data.INVOICE_ID)!}</td>
                                <td> ${(data.DESCRIPTION)!}</td>
                                <td> ${(data.AMOUNT_APPLIED)!}</td>

                                </tr>
        </#list> 
                            </table>
                        </div>
                    </div>
                </div>
            </article>
        </div>
    <div class="row">
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable"">
            <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
                <header>
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2>Remarks</h2>
                    </header>
                <div role="content">
                    <div id="grid" style="width:100%" align="center">
                        <table cellpaddin="0" cellspacing="0" id="tab101" class="table table-bordered table-striped"> 
                            <thead>
                                <tr>

                                    <th><label>Employee ID</label> </th>
                                    <th> <label>Action Date </label></th>
                                    <th><label> Action </label></th>
                                    <th><label> Remark</label></th>
                                    </tr>
                                </thead>
<#assign criteria = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getAuditRemarks(request, response)/> 


                   <#list  criteria as data>

                            <tr>
                                <td> ${(data.empId)!}</td>
                                <td> ${(data.actionDate)!}</td>
                                <td>
         <#if '${(data.action)!}'=='0'>
                                    Start
<#elseIf '${(data.action)!}'=='1'>
                                    Approved
<#elseIf '${(data.action)!}'=='2'>
                                    Rejected
    <#elseIf '${(data.action)!}'=='3'>
                                    Re-Assigne
</#if>
                                    </td>
                                <td> ${(data.remark)!}</td>
                                </tr>
      </#list> 



                            </table>
                        </div>



                    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                        &times;
                                        </button>
                                    <h4 class="modal-title" id="myModalLabel">Article Post</h4>
                                    </div>
                                <div class="modal-body">

                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <input type="text" class="form-control" placeholder="Title" required />
                                                </div>
                                            <div class="form-group">
                                                <textarea class="form-control" placeholder="Content" rows="5" required></textarea>
                                                </div>
                                            </div>
                                        </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="category"> Category</label>
                                                <select class="form-control" id="category">
                                                    <option>Articles</option>
                                                    <option>Tutorials</option>
                                                    <option>Freebies</option>
                                                    </select>
                                                </div>
                                            </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="tags"> Tags</label>
                                                <input type="text" class="form-control" id="tags" placeholder="Tags" />
                                                </div>
                                            </div>
                                        </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="well well-sm well-primary">
                                                <form class="form form-inline " role="form">
                                                    <div class="form-group">
                                                        <input type="text" class="form-control" value="" placeholder="Date" required />
                                                        </div>
                                                    <div class="form-group">
                                                        <select class="form-control">
                                                            <option>Draft</option>
                                                            <option>Published</option>
                                                            </select>
                                                        </div>
                                                    <div class="form-group">
                                                        <button type="submit" class="btn btn-success btn-sm">
                                                            <span class="glyphicon glyphicon-floppy-disk"></span> Save
                                                            </button>
                                                        <button type="button" class="btn btn-default btn-sm">
                                                            <span class="glyphicon glyphicon-eye-open"></span> Preview
                                                            </button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">
                                        Cancel
                                        </button>
                                    <button type="button" class="btn btn-primary">
                                        Post Article
                                        </button>

</div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->
</div><!-- /.modal -->

                        </div>
                    </div>
            </article>
        </div>
    <div class="row">
        <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable"">
            <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
                <header>
                    <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                    <h2>Invoice Details</h2>
                    </header>

                <div role="content">

                    <table id="datatable_fixed_column" class="table table-striped table-bordered" width="100%">

                        <thead>
                            <tr>
                                <th class="hasinput" style="width:17%">
                                    <input type="text" class="form-control" placeholder="Filter Invoice ID " />
                                    </th>

                                <th class="hasinput" style="width:16%">
                                    <input type="text" class="form-control" placeholder="Filter Type" />
                                    </th>
                                <th class="hasinput" style="width:17%">
                                    <input type="text" class="form-control" placeholder="Filter Party From" />
                                    </th>
                                <th class="hasinput" style="width:16%">
                                    <input type="text" class="form-control" placeholder="Filter Party ID" />
                                    </th>
                                <th class="hasinput" style="width:16%">
                                    <input type="text" class="form-control" placeholder="Filter Status" />
                                    </th>
                                <th class="hasinput" style="width:16%">
                                    <input type="text" class="form-control" placeholder="Filter Invoice Date" />
                                    </th>
                                <th class="hasinput" style="width:16%">
                                    <input type="text" class="form-control" placeholder="Filter Due Date" />
                                    </th>
                                <th class="hasinput" style="width:16%">
                                    <input type="text" class="form-control" placeholder="Filter Total" />
                                    </th>

                                </tr>
                            <tr>
                                <th><label>Invoice ID </label></th>
                                <th> <label>Invoice Type </label></th>
                                <th><label> PARTY ID FROM </label></th>
                                <th><label>PARTY ID</label></th>
                                <th><label> STATUS ID </label></th>
                                <th><label> INVOICE DATE</label></th>
                                <th><label> DUE DATE</label></th>
                                <th><label> Total</label></th>
                                </tr>
                            </thead>

                        <tbody>
            <#list  invoiceDetails as data>

                            <tr>
                                <td> ${(data.INVOICE_ID)!}</td>
                                <td> ${(data.INVOICE_TYPE_ID)!}</td>
                                <td> ${(data.PARTY_ID_FROM)!}</td>
                                <td> ${(data.PARTY_ID)!}</td>
                                <td> ${(data.STATUS_ID)!}</td>
                                <td> ${(data.INVOICE_DATE)!}</td>
                                <td> ${(data.DUE_DATE)!}</td>
                                <td> ${(data.Total)!}</td>
                                </tr>
        </#list> 

                            </tbody>

                        </table>


                    </div>
                </div>
            </article>
        </div>

    <script type="text/javascript">

		
                    $(document).ready(function() {
			
                            pageSetUp();
			
                            /* // DOM Position key index //
		
                            l - Length changing (dropdown)
                            f - Filtering input (search)
                            t - The Table! (datatable)
                            i - Information (records)
                            p - Pagination (paging)
                            r - pRocessing 
                            < and > - div elements
                            <"#id" and > - div with an id
                            <"class" and > - div with a class
                            <"#id.class" and > - div with an id and class
			
                            Also see: http://legacy.datatables.net/usage/features
                            */	
	
                            /* BASIC ;*/
                                    var responsiveHelper_dt_basic = undefined;
                                    var responsiveHelper_datatable_fixed_column = undefined;
                                    var responsiveHelper_datatable_col_reorder = undefined;
                                    var responsiveHelper_datatable_tabletools = undefined;
				
                                    var breakpointDefinition = {
                                            tablet : 1024,
                                            phone : 480
                                    };
	
                                    $('#dt_basic').dataTable({
                                            "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-12 hidden-xs'l>r>"+
                                                    "t"+
                                                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                                            "autoWidth" : true,
                                    "oLanguage": {
                                                "sSearch": '<span class="input-group-addon"><i class="glyphicon glyphicon-search"></i></span>'
                                            },
                                            "preDrawCallback" : function() {
                                                    // Initialize the responsive datatables helper once.
                                                    if (!responsiveHelper_dt_basic) {
                                                            responsiveHelper_dt_basic = new ResponsiveDatatablesHelper($('#dt_basic'), breakpointDefinition);
                                                    }
                                            },
                                            "rowCallback" : function(nRow) {
                                                    responsiveHelper_dt_basic.createExpandIcon(nRow);
                                            },
                                            "drawCallback" : function(oSettings) {
                                                    responsiveHelper_dt_basic.respond();
                                            }
                                    });
	
                            /* END BASIC */
			
                            /* COLUMN FILTER  */
                        var otable = $('#datatable_fixed_column').DataTable({
                            //"bFilter": false,
                            //"bInfo": false,
                            //"bLengthChange": false
                            //"bAutoWidth": false,
                            //"bPaginate": false,
                            //"bStateSave": true // saves sort state using localStorage
                                    "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6 hidden-xs'f><'col-sm-6 col-xs-12 hidden-xs'<'toolbar'>>r>"+
                                                    "t"+
                                                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
                                    "autoWidth" : true,
                                    "oLanguage": {
                                            "sSearch": '<span class="input-group-addon"><i class="glyphicon glyphicon-search"></i></span>'
                                    },
                                    "preDrawCallback" : function() {
                                            // Initialize the responsive datatables helper once.
                                            if (!responsiveHelper_datatable_fixed_column) {
                                                    responsiveHelper_datatable_fixed_column = new ResponsiveDatatablesHelper($('#datatable_fixed_column'), breakpointDefinition);
                                            }
                                    },
                                    "rowCallback" : function(nRow) {
                                            responsiveHelper_datatable_fixed_column.createExpandIcon(nRow);
                                    },
                                    "drawCallback" : function(oSettings) {
                                            responsiveHelper_datatable_fixed_column.respond();
                                    }		
			
                        });
		    
                        // custom toolbar
		    	   
                        // Apply the filter
                        $("#datatable_fixed_column thead th input[type=text]").on( 'keyup change', function () {
		    	
                            otable
                                .column( $(this).parent().index()+':visible' )
                                .search( this.value )
                                .draw();
		            
                        } );
                        /* END COLUMN FILTER */   
	    
                            /* COLUMN SHOW - HIDE */
                            $('#datatable_col_reorder').dataTable({
                                    "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-6 hidden-xs'C>r>"+
                                                    "t"+
                                                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-sm-6 col-xs-12'p>>",
                                    "autoWidth" : true,
                                    "oLanguage": {
                                            "sSearch": '<span class="input-group-addon"><i class="glyphicon glyphicon-search"></i></span>'
                                    },
                                    "preDrawCallback" : function() {
                                            // Initialize the responsive datatables helper once.
                                            if (!responsiveHelper_datatable_col_reorder) {
                                                    responsiveHelper_datatable_col_reorder = new ResponsiveDatatablesHelper($('#datatable_col_reorder'), breakpointDefinition);
                                            }
                                    },
                                    "rowCallback" : function(nRow) {
                                            responsiveHelper_datatable_col_reorder.createExpandIcon(nRow);
                                    },
                                    "drawCallback" : function(oSettings) {
                                            responsiveHelper_datatable_col_reorder.respond();
                                    }			
                            });
			
                            /* END COLUMN SHOW - HIDE */
	
                            /* TABLETOOLS */
                            $('#datatable_tabletools').dataTable({
				
                                    // Tabletools options: 
                                    //   https://datatables.net/extensions/tabletools/button_options
                                    "sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-6 hidden-xs'T>r>"+
                                                    "t"+
                                                    "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-sm-6 col-xs-12'p>>",
                                    "oLanguage": {
                                            "sSearch": '<span class="input-group-addon"><i class="glyphicon glyphicon-search"></i></span>'
                                    },		
                            "oTableTools": {
                                     "aButtons": [
                                 "copy",
                                 "csv",
                                 "xls",
                                    {
                                        "sExtends": "pdf",
                                        "sTitle": "SmartAdmin_PDF",
                                        "sPdfMessage": "SmartAdmin PDF Export",
                                        "sPdfSize": "letter"
                                    },
                                    {
                                    "sExtends": "print",
                                    "sMessage": "Generated by SmartAdmin <i>(press Esc to close)</i>"
                                    }
                                 ],
                                "sSwfPath": "js/plugin/datatables/swf/copy_csv_xls_pdf.swf"
                            },
                                    "autoWidth" : true,
                                    "preDrawCallback" : function() {
                                            // Initialize the responsive datatables helper once.
                                            if (!responsiveHelper_datatable_tabletools) {
                                                    responsiveHelper_datatable_tabletools = new ResponsiveDatatablesHelper($('#datatable_tabletools'), breakpointDefinition);
                                            }
                                    },
                                    "rowCallback" : function(nRow) {
                                            responsiveHelper_datatable_tabletools.createExpandIcon(nRow);
                                    },
                                    "drawCallback" : function(oSettings) {
                                            responsiveHelper_datatable_tabletools.respond();
                                    }
                            });
			
                            /* END TABLETOOLS */
		
                    })


        </script>


    <script src="/images/BusinessInnovation/js/plugin/datatables/jquery.dataTables.min.js"></script>
    <script src="/images/BusinessInnovation/js/plugin/datatables/dataTables.colVis.min.js"></script>
    <script src="/images/BusinessInnovation/js/plugin/datatables/dataTables.tableTools.min.js"></script>
    <script src="/images/BusinessInnovation/js/plugin/datatables/dataTables.bootstrap.min.js"></script>
    <script src="/images/BusinessInnovation/js/plugin/datatable-responsive/datatables.responsive.min.js"></script>
















