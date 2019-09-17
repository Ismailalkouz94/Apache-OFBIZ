<!DOCTYPE html>
<html lang="en-us">
    <head>
        <meta charset="utf-8">
        
        <meta name="description" content="">
        <meta name="author" content="">

        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

                <!-- #CSS Links -->
                <!-- Basic Styles -->
        <link rel="stylesheet" type="text/css" media="screen" href="/rainbowstone/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" media="screen" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css">

                <!-- SmartAdmin Styles : Caution! DO NOT change the order -->
        <link rel="stylesheet" type="text/css" media="screen" href="/rainbowstone/css/smartadmin-production-plugins.min.css">
        <link rel="stylesheet" type="text/css" media="screen" href="/rainbowstone/css/smartadmin-production.min.css">
        <link rel="stylesheet" type="text/css" media="screen" href="/rainbowstone/css/smartadmin-skins.min.css">



        <script>
function findAllowencesEmployee (){


}
    var link2="<button type='link' onclick='show()' value='' id='show2'><span>show</span></button>";
   function show (){
        if(jQuery("#jqgrid2")[0].childNodes[0].childNodes[0].cells[24].style.display !="none")
            {jQuery("#jqgrid2").jqGrid('hideCol',[""]);
               }
        else
           {jQuery("#jqgrid2").jqGrid('showCol',[""]);
               }
   }

            </script>

        </head>
    <body class="">

        <div id="content" style="margin-top: 10px;">


            <section id="widget-grid" class="">

                                        <!-- row -->
                <div class="row">

                                                <!-- NEW WIDGET START -->
                    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">

                        <table id="jqgrid2"></table>
                        <div id="pjqgrid2"></div>


                        </article>
                        <!-- WIDGET END -->

                    </div>
                </section>
            </div>

        <!--================================================== -->

                <!-- PACE LOADER - turn this on if you want ajax loading to show (caution: uses lots of memory on iDevices)-->
        <script data-pace-options='{ "restartOnRequestAfter": true }' src="/rainbowstone/js/plugin/pace/pace.min.js"></script>

                <!-- Link to Google CDN's jQuery + jQueryUI; fall back to local -->
        <script src="/rainbowstone/js/libs/jquery-2.1.1.min.js"></script>
        <script>
                if (!window.jQuery) {
                        document.write('<script src="/rainbowstone/js/libs/jquery-2.1.1.min.js"><\/script>');
                }
            </script>

        <script src="/rainbowstone/js/libs/jquery-ui-1.10.3.min.js"></script>
        <script>
                if (!window.jQuery.ui) {
                        document.write('<script src="/rainbowstone/js/libs/jquery-ui-1.10.3.min.js"><\/script>');
                }
            </script>

                <!-- IMPORTANT: APP CONFIG -->
        <script src="/rainbowstone/js/app.config.js"></script>

                <!-- JS TOUCH : include this plugin for mobile drag / drop touch events-->
        <script src="/rainbowstone/js/plugin/jquery-touch/jquery.ui.touch-punch.min.js"></script> 

                <!-- BOOTSTRAP JS -->
        <script src="/rainbowstone/js/bootstrap/bootstrap.min.js"></script>

                <!-- CUSTOM NOTIFICATION -->
        <script src="/rainbowstone/js/notification/SmartNotification.min.js"></script>

                <!-- JARVIS WIDGETS -->
        <script src="/rainbowstone/js/smartwidgets/jarvis.widget.min.js"></script>

                <!-- EASY PIE CHARTS -->
        <script src="/rainbowstone/js/plugin/easy-pie-chart/jquery.easy-pie-chart.min.js"></script>

                <!-- SPARKLINES -->
        <script src="/rainbowstone/js/plugin/sparkline/jquery.sparkline.min.js"></script>

                <!-- JQUERY VALIDATE -->
        <script src="/rainbowstone/js/plugin/jquery-validate/jquery.validate.min.js"></script>

                <!-- JQUERY MASKED INPUT -->
        <script src="/rainbowstone/js/plugin/masked-input/jquery.maskedinput.min.js"></script>

                <!-- JQUERY SELECT2 INPUT -->
        <script src="/rainbowstone/js/plugin/select2/select2.min.js"></script>

                <!-- JQUERY UI + Bootstrap Slider -->
        <script src="/rainbowstone/js/plugin/bootstrap-slider/bootstrap-slider.min.js"></script>

                <!-- browser msie issue fix -->
        <script src="/rainbowstone/js/plugin/msie-fix/jquery.mb.browser.min.js"></script>

                <!-- FastClick: For mobile devices -->
        <script src="/rainbowstone/js/plugin/fastclick/fastclick.min.js"></script>

                <!--[if IE 8]>

                <h1>Your browser is out of date, please update your browser by going to www.microsoft.com/download</h1>

                <![endif]-->

                <!-- Demo purpose only -->


                <!-- MAIN APP JS FILE -->
        <script src="/rainbowstone/js/app.min.js"></script>

                <!-- ENHANCEMENT PLUGINS : NOT A REQUIREMENT -->
                <!-- Voice command : plugin -->
        <script src="/rainbowstone/js/speech/voicecommand.min.js"></script>

                <!-- SmartChat UI : plugin -->
        <script src="/rainbowstone/js/smart-chat-ui/smart.chat.ui.min.js"></script>
        <script src="/rainbowstone/js/smart-chat-ui/smart.chat.manager.min.js"></script>

                <!-- PAGE RELATED PLUGIN(S)
                <script src="..."></script>-->

        <script src="/rainbowstone/js/plugin/jqgrid/jquery.jqGrid.min.js"></script>
        <script src="/rainbowstone/js/plugin/jqgrid/grid.locale-en.min.js"></script>



        <script type="text/javascript">

 $(document).ready(function() {

      <#assign invItemAndOrdItems = delegator.findAll("InvItemAndOrdItem",true) />
      var jqgrid_data2=[];

          <#list invItemAndOrdItems as list>               
          pageSetUp();
          var total=1;    
        if("${(list.invoiceId)! "0"}" === "${parameters.invoiceId}"){ 
            total=${(list.quantity)! 1} *${(list.amount)! 1};
            var myDefaul="ss";
         jqgrid_data2.push ({ 
                 invoiceItemSeqId : "${(list.invoiceItemSeqId)!}",
                 partyId : "${(list.partyId)!}",
                 origAmount : "${(list.origAmount)!}",
                 origCurrencyUomId : "${(list.origCurrencyUomId)!}",
                 amount : "${(list.currencyUomId)!}${(list.amount)!}",
                 currencyUomId : "${(list.currencyUomId)!}",
                 invoiceItemTypeId : "${(list.invoiceItemTypeId)!}",
                 overrideGlAccountId :'<a href="GlAccountNavigate?glAccountId=${(list.overrideGlAccountId)!}">${(list.overrideGlAccountId)!}</a>',
                 overrideOrgPartyId : "${(list.overrideOrgPartyId)!}",
                 inventoryItemId : "${(list.inventoryItemId)!}",
                 productId : '<a href="/catalog/control/EditProduct?productId=${(list.productId)!}">${(list.productId)!}</a>',
                 productFeatureId : "${(list.productFeatureId)!}",
                 parentInvoiceId : "${(list.parentInvoiceId)!}",
                 parentInvoiceItemSeqId : "${(list.parentInvoiceItemSeqId)!}",
                 uomId : "${(list.uomId)!}",
                 taxableFlag : "${(list.taxableFlag)!}",
                 quantity : "${(list.quantity)!}",
                 description : "${(list.description)!}",
                 taxAuthPartyId : "${(list.taxAuthPartyId)!}",
                 taxAuthGeoId : "${(list.taxAuthGeoId)!}",
                 taxAuthorityRateSeqId : "${(list.taxAuthorityRateSeqId)!}",
                 salesOpportunityId : "${(list.salesOpportunityId)!}",
                 orderId :'<a href="/ordermgr/control/orderview?orderId=${(list.orderId)!}">${(list.orderId)!}</a>',
                 orderItemSeqId : "${(list.orderItemSeqId)!}",
                 total : "${(list.currencyUomId)!}"+total,
                 invoiceId : "${(list.invoiceId)!}"
         } );
       
       
            }//end if 
      

      
                 
    
  </#list>  

         jQuery("#jqgrid2").jqGrid({
                 data : jqgrid_data2,
                 datatype : "local",
                 height : 'auto',           
                 colNames : ['Item No', 'Party Id','Orig Amount','Orig Currency','${uiLabelMap.AccountingUnitPrice}','Currency','invoiceItemTypeId','overrideGlAccountId','overrideOrgPartyId','inventoryItemId','productId','productFeatureId','parentInvoiceId','parentInvoiceItemSeqId'
                            ,'uomId','taxableFlag','quantity','description','taxAuthPartyId','taxAuthGeoId','taxAuthorityRateSeqId','salesOpportunityId','orderId','orderItemSeqId','Total','invoiceId'],
                 colModel : [{
                         name : 'invoiceItemSeqId',
                         index : 'invoiceItemSeqId',
                         width: 200,             
                         editable : false
                 },{
                         name : 'partyId',
                         index : 'partyId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'origAmount',
                         index : 'origAmount',
                         width: 200, 
                         editable : false
                 },{
                         name : 'origCurrencyUomId',
                         index : 'origCurrencyUomId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'amount',
                         index : 'amount',
                         width: 200, 
                         editable : false
                 },{
                         name : 'currencyUomId',
                         index : 'currencyUomId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'invoiceItemTypeId',
                         index : 'invoiceItemTypeId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'overrideGlAccountId',
                         index : 'overrideGlAccountId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'overrideOrgPartyId',
                         index : 'overrideOrgPartyId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'inventoryItemId',
                         index : 'inventoryItemId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'productId',
                         index : 'productId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'productFeatureId',
                         index : 'productFeatureId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'parentInvoiceId',
                         index : 'parentInvoiceId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'parentInvoiceItemSeqId',
                         index : 'parentInvoiceItemSeqId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'uomId',
                         index : 'uomId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'taxableFlag',
                         index : 'taxableFlag',
                         width: 200, 
                         editable : false
                 },{
                         name : 'quantity',
                         index : 'quantity',
                         width: 200, 
                         editable : false
                 },{
                         name : 'description',
                         index : 'description',
                         width: 200, 
                         editable : false
                 },{
                         name : 'taxAuthPartyId',
                         index : 'taxAuthPartyId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'taxAuthGeoId',
                         index : 'taxAuthGeoId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'taxAuthorityRateSeqId',
                         index : 'taxAuthorityRateSeqId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'salesOpportunityId',
                         index : 'salesOpportunityId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'orderId',
                         index : 'orderId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'orderItemSeqId',
                         index : 'orderItemSeqId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'total',
                         index : 'total',
                         width: 200, 
                         editable : false
                 },{
                         name : 'invoiceId',
                         index : 'invoiceId',
                         width: 200, 
                         editable : false,
                         hidden  :true 
                 }],
                                           
                 rowNum : 10,
                 rowList : [10, 20, 30],
                 pager : '#pjqgrid2',
                 sortname : 'id',
                 toolbarfilter : true,
                 viewrecords : true,
                 sortorder : "asc",
                                          


                         caption : "${uiLabelMap.AccountingInvoiceItems}",
                         multiselect : false,
                         autowidth : true,
                              
         });
 
             
         jQuery("#jqgrid2").jqGrid('inlineNav', "#pjqgrid2");
         /* Add tooltips */
         $('.navtable .ui-pg-button').tooltip({
                 container : 'body'
         });

         jQuery("#m1").click(function() {
                 var s;
                 s = jQuery("#jqgrid2").jqGrid('getGridParam', 'selarrrow');
//                 alert(s);
         });
                    
                       


         jQuery("#m1s").click(function() {
                 jQuery("#jqgrid2").jqGrid('setSelection', "13");
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
         $("#jqgrid2").jqGrid('setGridWidth', $("#content").width());
 })

  


                <!-- Your GOOGLE ANALYTICS CODE Below -->

                var _gaq = _gaq || [];
                _gaq.push(['_setAccount', 'UA-XXXXXXXX-X']);
                _gaq.push(['_trackPageview']);

                (function() {
                        var ga = document.createElement('script');
                        ga.type = 'text/javascript';
                        ga.async = true;
                        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                        var s = document.getElementsByTagName('script')[0];
                        s.parentNode.insertBefore(ga, s);
                 })();
                     

</script>



        </body>

    </html>
