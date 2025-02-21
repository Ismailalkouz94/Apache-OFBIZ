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

            </script>

        </head>
    <body class="">

        <div id="content" style="margin-top: 10px;">


            <section id="widget-grid" class="">

                                        <!-- row -->
                <div class="row">

                                                <!-- NEW WIDGET START -->
                    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">

                        <table id="jqgrid4"></table>
                        <div id="pjqgrid4"></div>


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


        
      <#assign partyNameView = delegator.findAll("PartyNameView",true) />              
      <#assign invoiceRole = delegator.findAll("InvoiceRole",true) />
         
      var jqgrid_data4=[];
           var c=0;
      var name="";

               
          <#list invoiceRole as list>               
          pageSetUp();
          var total=1;    
        if("${(list.invoiceId)! "0"}" === "${parameters.invoiceId}"){ 
            var myDefaul="ss";
         jqgrid_data4.push ({ 
                 partyId : '<a href="/partymgr/control/viewprofile?partyId=${(list.partyId)!}">${(list.partyId)!}</a>',
                 name : name,            
                 roleTypeId : "${(list.roleTypeId)!}",
                 percentage : "${(list.percentage)!}",
                 datetimePerformed : "${(listdatetimePerformedtermValue)!}"
         } );
               <#list partyNameView as list2>  
                    jqgrid_data4[c].name="${(list2.groupName)!} ${(list2.firstName)!} ${(list2.lastName)!}";
                </#list>
             c++;
       
            }//end if 
      

      
                 
    
  </#list>  

         jQuery("#jqgrid4").jqGrid({
                 data : jqgrid_data4,
                 datatype : "local",
                 height : 'auto',           
                 colNames : ['partyId', 'roleTypeId','percentage','datetimePerformed'],
                 colModel : [{
                         name : 'partyId',
                         index : 'partyId',
                         width: 200,             
                         editable : false
                 },{
                         name : 'roleTypeId',
                         index : 'roleTypeId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'percentage',
                         index : 'percentage',
                         width: 200, 
                         editable : false
                 },{
                         name : 'datetimePerformed',
                         index : 'datetimePerformed',
                         width: 200, 
                         editable : false
                 }],
                                           
                 rowNum : 10,
                 rowList : [10, 20, 30],
                 pager : '#pjqgrid4',
                 sortname : 'id',
                 toolbarfilter : true,
                 viewrecords : true,
                 sortorder : "asc",
                                          


                         caption : "${uiLabelMap.AccountingInvoiceRoles}",
                         multiselect : false,
                         autowidth : true,
                              
         });
 
             
         jQuery("#jqgrid4").jqGrid('inlineNav', "#pjqgrid4");
         /* Add tooltips */
         $('.navtable .ui-pg-button').tooltip({
                 container : 'body'
         });

         jQuery("#m1").click(function() {
                 var s;
                 s = jQuery("#jqgrid4").jqGrid('getGridParam', 'selarrrow');
//                 alert(s);
         });
                    
                       


         jQuery("#m1s").click(function() {
                 jQuery("#jqgrid4").jqGrid('setSelection', "13");
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
         $("#jqgrid4").jqGrid('setGridWidth', $("#content").width());
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
