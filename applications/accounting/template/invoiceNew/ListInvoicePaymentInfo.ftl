

        <div id="content" style="margin-top: 10px;">


            <section id="widget-grid" class="">

                                        <!-- row -->
                <div class="row">

                                                <!-- NEW WIDGET START -->
                    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">

                        <table id="jqgrid6"></table>
                        <div id="pjqgrid6"></div>


                        </article>
                        <!-- WIDGET END -->

                    </div>
                </section>
            </div>

        <!--================================================== -->
        <script src="/BusinessInnvoation/js/plugin/jqgrid/jquery.jqGrid.min.js"></script>
        <script src="/BusinessInnvoation/js/plugin/jqgrid/grid.locale-en.min.js"></script>



        <script type="text/javascript">

 $(document).ready(function() {    

     <#assign invoiceItem = delegator.findAll("InvoiceItem",true) />    
      <#assign listRes = listResult />

             
                 
      var jqgrid_data6=[];
      var c=0;
               
          <#list listRes as list>               
          pageSetUp();

        if("${(list.invoiceId)! "0"}" === "${parameters.invoiceId}"){ 
            var myDefaul="ss";
         jqgrid_data6.push ({ 
                 dueDate : "${(list.dueDate)!}",
                 amount : "${(list.amount)!}",         
                 paidAmount : "${(list.paidAmount)!}",         
                 outstandingAmount : "${(list.outstandingAmount)!}"
         } );
             
                 <#list invoiceItem as list2>  
                    jqgrid_data6[c].amount="${(list2.currencyUomId)!}${(list.amount)!}";
                    jqgrid_data6[c].outstandingAmount="${(list2.currencyUomId)!}${(list.outstandingAmount)!}";    
                </#list>
       c++;
            }//end if              
    
  </#list>  

         jQuery("#jqgrid6").jqGrid({
                 data : jqgrid_data6,
                 datatype : "local",
                 height : 'auto',           
                 colNames : ['termTypeId', 'dueDate','amount','paidAmount','outstandingAmount'],
                 colModel : [{
                         name : 'termTypeId',
                         index : 'termTypeId',
                         width: 200,             
                         editable : false
                 },{
                         name : 'dueDate',
                         index : 'dueDate',
                         width: 200, 
                         editable : false
                 },{
                         name : 'amount',
                         index : 'amount',
                         width: 200, 
                         editable : false
                 },{
                         name : 'paidAmount',
                         index : 'paidAmount',
                         width: 200, 
                         editable : false
                 },{
                         name : 'outstandingAmount',
                         index : 'outstandingAmount',
                         width: 200, 
                         editable : false
                 }],
                                           
                 rowNum : 10,
                 rowList : [10, 20, 30],
                 pager : '#pjqgrid6',
                 sortname : 'id',
                 toolbarfilter : true,
                 viewrecords : true,
                 sortorder : "asc",
                                          


                         caption : "ListInvoicePaymentInfo",
                         multiselect : false,
                         autowidth : true,
                              
         });
 
             
         jQuery("#jqgrid6").jqGrid('inlineNav', "#pjqgrid6");
         /* Add tooltips */
         $('.navtable .ui-pg-button').tooltip({
                 container : 'body'
         });

         jQuery("#m1").click(function() {
                 var s;
                 s = jQuery("#jqgrid6").jqGrid('getGridParam', 'selarrrow');
//                 alert(s);
         });
                    
                       


         jQuery("#m1s").click(function() {
                 jQuery("#jqgrid6").jqGrid('setSelection', "13");
         });

         // remove classes
         $(".ui-jqgrid").removeClass("ui-widget ui-widget-content");
         $(".ui-jqgrid-view").children().removeClass("ui-widget-header ui-state-default");
         $(".ui-jqgrid-labels, .ui-search-toolbar").children().removeClass("ui-state-default ui-th-column ui-th-ltr");
         $(".ui-jqgrid-pager").removeClass("ui-state-default");
         $(".ui-jqgrid").removeClass("ui-widget-content");

         // add classes
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

  })

    
 $(window).on('resize.jqGrid', function() {
         $("#jqgrid6").jqGrid('setGridWidth', $("#content").width());
 })

  

