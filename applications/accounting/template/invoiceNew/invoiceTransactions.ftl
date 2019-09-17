        <script>
function findAllowencesEmployee (){


}
    var link="<a type='link' onclick='show()' value='' id='show'>"+"${uiLabelMap.AccountingTransactions}" +"<span id='arrow'> << </span></a>";
   function show (){
        if(jQuery("#jqgrid1")[0].childNodes[0].childNodes[0].cells[3].style.display !="none")
            {jQuery("#jqgrid1").jqGrid('hideCol',["glFiscalTypeId","paymentId","fixedAssetId","description","productId","origAmount","accountCode"]);
                document.getElementById("arrow").innerHTML=" >>";
               }
        else
           {jQuery("#jqgrid1").jqGrid('showCol',["glFiscalTypeId","paymentId","fixedAssetId","description","productId","origAmount","accountCode"]);
              document.getElementById("arrow").innerHTML=" <<"; }
   }

</script>
     


                <div class="row">

                                                <!-- NEW WIDGET START -->
                    <article class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                        <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-5" data-widget-editbutton="false">
			    <header>
			    <span class="jarviswidget-loader"><i class="fa fa-refresh fa-spin"></i></span>
			    <span><h2>&ensp;${uiLabelMap.ListInvoicePaymentInfo}</h2></span>
			    </header>
                              <div role="content">
				<table id="jqgrid6"></table>
				<div id="pjqgrid6"></div>
			      </div>
                      </div>
                  </article>


        <!--================================================== -->


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
                 dueDate : "${(list.dueDate).toString().substring(0,10)!}",
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
                 colNames : ['${uiLabelMap.FormFieldTitle_termTypeId}', '${uiLabelMap.FormFieldTitle_dueDate}','${uiLabelMap.FormFieldTitle_invoiceAmount}','${uiLabelMap.FormFieldTitle_paidAmount}','${uiLabelMap.FormFieldTitle_outstandingAmount}'],
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
                                          


                         caption : "${uiLabelMap.ListInvoicePaymentInfo}",
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
         $(".ui-jqgrid").removeClass("ui-widget ui-widget-content6");
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
         $("#jqgrid6").jqGrid('setGridWidth', $("#content1").width());
 })

  


                     

</script>
   
        <!--InvoiceRole.........................-->
      
 


                                                <!-- NEW WIDGET START -->
                    <article class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                      <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">								
			<header>
                         <span><h2>&ensp;${uiLabelMap.AccountingInvoiceRoles}</h2></span>
			</header>
                          <div role="content">
			    <table id="jqgrid4"></table>
			    <div id="pjqgrid4"></div>
			 </div>
		     </div>
                 </article>
          </div>



        <!--================================================== -->
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
                 colNames : ['${uiLabelMap.PartyId}', '${uiLabelMap.AccountingRoleType}','${uiLabelMap.FormFieldTitle_percentage}','${uiLabelMap.FormFieldTitle_datetimePerformed}'],
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
         $("#jqgrid4").jqGrid('setGridWidth', $("#content4").width());
 })

  

                     

</script>
        <!--.............................-->
         <!--ListInvoiceApplications..........................-->
        



                <div class="row">
                    <article class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                       <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-3" data-widget-editbutton="false">
			    <header>
                               <span><h2>&ensp;${uiLabelMap.AccountingAppliedPayments}</h2></span>
			    </header>
				<div role="content">
                                  <table id="jqgrid7"></table>
                                  <div id="pjqgrid7"></div>
				</div>
		       </div>
                  </article>


     

        <!--================================================== -->

        <script type="text/javascript">

 $(document).ready(function() {    
     <#assign invoiceItem = delegator.findAll("InvoiceItem",true) />    
         
      <#assign listInvoiceApplications = invoiceApplications />

          
                 
      var jqgrid_data7=[];
      var c=0;
               
          <#list listInvoiceApplications as list>               
          pageSetUp();

        if("${(list.invoiceId)! "0"}" === "${parameters.invoiceId}"){ 
            var myDefaul="ss";
         jqgrid_data7.push ({ 
                 invoiceItemSeqId : "${(list.invoiceItemSeqId)!}",
                 productId : "${(list.productId)!}",         
                 description : "${(list.description)!}",         
                 total : "${(list.total)!}",         
                 paymentId :'<a href="paymentOverview?paymentId=${(list.paymentId)!}">${(list.paymentId)!}</a>',        
                 amountApplied : "${(list.amountApplied)!}",         
                 invoiceId : "${(list.invoiceId)!}",         
                 billingAccountId : "${(list.billingAccountId)!}",         
                 paymentApplicationId : "${(list.paymentApplicationId)!}"
         } );
                <#list invoiceItem as list2>  
                    jqgrid_data7[c].total="${(list2.currencyUomId)!}${(list.total)!}";
                     jqgrid_data7[c].amountApplied="${(list2.currencyUomId)!}${(list.amountApplied)!}";
                </#list>
       c++;
            }//end if              
    
  </#list>  

         jQuery("#jqgrid7").jqGrid({
                 data : jqgrid_data7,
                 datatype : "local",
                 height : 'auto',           
                 colNames : ['${uiLabelMap.FormFieldTitle_invoiceItemSeqId}', '${uiLabelMap.FormFieldTitle_productId}','${uiLabelMap.AcctgGlJrnlDescLabel}','${uiLabelMap.AccountingTotalCapital}','${uiLabelMap.FormFieldTitle_paymentId}','${uiLabelMap.FormFieldTitle_amountApplied}','invoiceId','billingAccountId','paymentApplicationId'],
                 colModel : [{
                         name : 'invoiceItemSeqId',
                         index : 'invoiceItemSeqId',
                         width: 200,             
                         editable : false
                 },{
                         name : 'productId',
                         index : 'productId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'description',
                         index : 'description',
                         width: 200, 
                         editable : false
                 },{
                         name : 'total',
                         index : 'total',
                         width: 200, 
                         editable : false
                 },{
                         name : 'paymentId',
                         index : 'paymentId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'amountApplied',
                         index : 'amountApplied',
                         width: 200, 
                         editable : false
                 },{
                         name : 'invoiceId',
                         index : 'invoiceId',
                         width: 200, 
                         editable : false,
                         hidden  :true 
                 },{
                         name : 'billingAccountId',
                         index : 'billingAccountId',
                         width: 200, 
                         editable : false,
                         hidden  :true 
                 },{
                         name : 'paymentApplicationId',
                         index : 'paymentApplicationId',
                         width: 200, 
                         editable : false,
                         hidden  :true 
                 }],
                                           
                 rowNum : 10,
                 rowList : [10, 20, 30],
                 pager : '#pjqgrid7',
                 sortname : 'id',
                 toolbarfilter : true,
                 viewrecords : true,
                 sortorder : "asc",
                                          


                         caption : "${uiLabelMap.AccountingAppliedPayments}",
                         multiselect : false,
                         autowidth : true,
                              
         });
 
             
         jQuery("#jqgrid7").jqGrid('inlineNav', "#pjqgrid7");
         /* Add tooltips */
         $('.navtable .ui-pg-button').tooltip({
                 container : 'body'
         });

         jQuery("#m1").click(function() {
                 var s;
                 s = jQuery("#jqgrid7").jqGrid('getGridParam', 'selarrrow');
//                 alert(s);
         });
                    
                       


         jQuery("#m1s").click(function() {
                 jQuery("#jqgrid7").jqGrid('setSelection', "13");
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
         $("#jqgrid7").jqGrid('setGridWidth', $("#content7").width());
 })

  


</script>
         <!--..........................-->
         <!--ListInvoiceTerms..........................-->

                    <article class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                       <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-4" data-widget-editbutton="false" >							
			    <header>
                             <span><h2>&ensp;${uiLabelMap.ListInvoiceTerms}</h2></span>
			    </header>			
                              <div role="content">
				<table id="jqgrid3"></table>
				<div id="pjqgrid3"></div>
			      </div>
		      </div>
                  </article>
              </div>
 
  

        <!--================================================== -->

        <script type="text/javascript">

 $(document).ready(function() {    


        
                           
      <#assign invoiceTerm = delegator.findAll("InvoiceTerm",true) />
      var jqgrid_data3=[];

          <#list invoiceTerm as list>               
          pageSetUp();
          var total=1;    
        if("${(list.invoiceId)! "0"}" === "${parameters.invoiceId}"){ 
        
            var myDefaul="ss";
         jqgrid_data3.push ({ 
                 invoiceTermId : "${(list.invoiceTermId)!}",
                 termTypeId : "${(list.termTypeId)!}",
                 invoiceItemSeqId : "${(list.invoiceItemSeqId)!}",
                 termValue : "${(list.termValue)!}",
                 termDays : "${(list.termDays)!}",
                 textValue : "${(list.textValue)!}",
                 description : "${(list.description)!}",
                 uomId :"${(list.uomId)!}",
                 invoiceId :"${(list.invoiceId)!}"            
         } );
       
       
            }//end if 
      

      
                 
    
  </#list>  

         jQuery("#jqgrid3").jqGrid({
                 data : jqgrid_data3,
                 datatype : "local",
                 height : 'auto',           
                 colNames : ['${uiLabelMap.FormFieldTitle_invoiceTermId}', '${uiLabelMap.FormFieldTitle_termTypeId}','${uiLabelMap.FormFieldTitle_invoiceItemSeqId}','${uiLabelMap.FormFieldTitle_termValue}','${uiLabelMap.FormFieldTitle_termDays}','${uiLabelMap.FormFieldTitle_textValue}','${uiLabelMap.AcctgGlJrnlDescLabel}','${uiLabelMap.FormFieldTitle_amountUomId}','invoiceId'],
                 colModel : [{
                         name : 'invoiceTermId',
                         index : 'invoiceTermId',
                         width: 200,             
                         editable : false
                 },{
                         name : 'termTypeId',
                         index : 'termTypeId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'invoiceItemSeqId',
                         index : 'invoiceItemSeqId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'termValue',
                         index : 'termValue',
                         width: 200, 
                         editable : false
                 },{
                         name : 'termDays',
                         index : 'termDays',
                         width: 200, 
                         editable : false
                 },{
                         name : 'textValue',
                         index : 'textValue',
                         width: 200, 
                         editable : false
                 },{
                         name : 'description',
                         index : 'description',
                         width: 200, 
                         editable : false
                 },{
                         name : 'uomId',
                         index : 'uomId',
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
                 pager : '#pjqgrid3',
                 sortname : 'id',
                 toolbarfilter : true,
                 viewrecords : true,
                 sortorder : "asc",
                                          


                         caption : "",
                         multiselect : false,
                         autowidth : true,
                              
         });
 
             
         jQuery("#jqgrid3").jqGrid('inlineNav', "#pjqgrid3");
         /* Add tooltips */
         $('.navtable .ui-pg-button').tooltip({
                 container : 'body'
         });

         jQuery("#m1").click(function() {
                 var s;
                 s = jQuery("#jqgrid3").jqGrid('getGridParam', 'selarrrow');
//                 alert(s);
         });
                    
                       


         jQuery("#m1s").click(function() {
                 jQuery("#jqgrid3").jqGrid('setSelection', "13");
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
         $("#jqgrid3").jqGrid('setGridWidth', $("#content3").width());
 })

</script>


      <!--ListInvoiceStatus.........................-->
        
                <div class="row">
                    <article id="article1" class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                        <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">								
			    <header>
			    <span><h2>&ensp;${uiLabelMap.CommonStatus}</h2></span>
			    </header>
			    <div role="content">
				<table id="jqgrid5"></table>
				<div id="pjqgrid5"></div>
		            </div>
		        </div>
                   </article>
              </div>
           
     
        <!--================================================== -->
        <script type="text/javascript">

 $(document).ready(function() {    

      

              <#assign statusItem = delegator.findAll("StatusItem",true) />
      <#assign invoiceStatus = delegator.findAll("InvoiceStatus",true) />
          

                 
      var jqgrid_data5=[];
      var c=0;
               
          <#list invoiceStatus as list>               
          pageSetUp();

        if("${(list.invoiceId)! "0"}" === "${parameters.invoiceId}"){ 
            var myDefaul="ss";
         jqgrid_data5.push ({ 
                 statusDate : "${(list.statusDate).toString().substring(0,10)!}",
                 statusId : "${(list.statusId)!}",         
                 changeByUserLoginId : "${(list.changeByUserLoginId)!}"
         } );
        <#list statusItem as list2>   
        if("${list.statusId}"=="${list2.statusId}")
        jqgrid_data5[c].statusId="${list2.description}";
    </#list>
       c++;
            }//end if              
    
  </#list>  

         jQuery("#jqgrid5").jqGrid({
                 data : jqgrid_data5,
                 datatype : "local",
                 height : 'auto',           
                 colNames : ['${uiLabelMap.FormFieldTitle_statusDate}', '${uiLabelMap.statusId}','${uiLabelMap.changeByUserLoginId}'],
                 colModel : [{
                         name : 'statusDate',
                         index : 'statusDate',
                         width: 200,             
                         editable : false
                 },{
                         name : 'statusId',
                         index : 'statusId',
                         width: 200, 
                         editable : false
                 },{
                         name : 'changeByUserLoginId',
                         index : 'changeByUserLoginId',
                         width: 200, 
                         editable : false
                 }],
                                           
                 rowNum : 10,
                 rowList : [10, 20, 30],
                 pager : '#pjqgrid5',
                 sortname : 'id',
                 toolbarfilter : true,
                 viewrecords : true,
                 sortorder : "asc",
                                          


                         caption : "${uiLabelMap.CommonStatus}",
                         multiselect : false,
                         autowidth : true,
                              
         });
 
             
         jQuery("#jqgrid5").jqGrid('inlineNav', "#pjqgrid5");
         /* Add tooltips */
         $('.navtable .ui-pg-button').tooltip({
                 container : 'body'
         });

         jQuery("#m1").click(function() {
                 var s;
                 s = jQuery("#jqgrid5").jqGrid('getGridParam', 'selarrrow');
//                 alert(s);
         });
                    
                       


         jQuery("#m1s").click(function() {
                 jQuery("#jqgrid5").jqGrid('setSelection', "13");
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
         $("#jqgrid5").jqGrid('setGridWidth', $("#article1").width());
 })

  


                     

</script>
         <!--.........................-->
<#--...............................................Items..............................................................-->
     

 <#assign invItemAndOrdItems = delegator.findAll("InvItemAndOrdItem",true) />
         
  <div class="row">
  <article  class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
      <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-6"  data-widget-editbutton="false">								
    <header>
      <span><h2>&ensp;${uiLabelMap.AccountingBudgetItems}</h2></span>
    </header>		
	<div role="content">
		<table id="datatable_col_reorder1" class="table table-striped table-bordered table-hover" style="width:100% !important;table-layout: fixed;">
		<thead>
		<tr>
													<th>${uiLabelMap.FormFieldTitle_invoiceItemSeqId}</th>
													<th>${uiLabelMap.PartyId}</th>
													<th>${uiLabelMap.FormFieldTitle_origAmount}</th>
													<th>${uiLabelMap.FormFieldTitle_origCurrencyUomId}</th>
													<th>${uiLabelMap.AccountingUnitPrice}</th>
													<th>${uiLabelMap.AccountingCurrency}</th>
													<th>${uiLabelMap.FormFieldTitle_invoiceItemTypeId}</th>
													<th>${uiLabelMap.FormFieldTitle_overrideGlAccountId}</th>
													<th>${uiLabelMap.OverrideOrgPartyId}</th>
													<th>${uiLabelMap.InventoryItemId}</th>
													<th>${uiLabelMap.AccountingProductId}</th>
													<th>${uiLabelMap.ProductFeatureId}</th>
													<th>${uiLabelMap.FormFieldTitle_parentInvoiceId}</th>
													<th>${uiLabelMap.FormFieldTitle_parentInvoiceItemSeqId}</th>
													<th>${uiLabelMap.AccountingActualCurrencyUomId}</th>
													<th>${uiLabelMap.FormFieldTitle_taxableFlag}</th>
													<th>${uiLabelMap.AccountingQuantity}</th>
													<th>${uiLabelMap.AcctgGlJrnlDescLabel}</th>
													<th>${uiLabelMap.FormFieldTitle_toTaxAuthPartyId}</th>
													<th>${uiLabelMap.FormFieldTitle_toTaxAuthGeoId}</th>
													<th>${uiLabelMap.FormFieldTitle_taxAuthorityRateSeqId}</th>
													<th>${uiLabelMap.FormFieldTitle_salesOpportunityId}</th>
													<th>${uiLabelMap.FormFieldTitle_acquireOrderId}</th>
													<th>${uiLabelMap.FormFieldTitle_acquireOrderItemSeqId}</th>
													<th>${uiLabelMap.AccountingTotalCapital}</th>
												</tr>
											</thead>
											<tbody>
                                                                                  <#list invItemAndOrdItems as list>  
											<#if "${(list.invoiceId)! '0'}" == "${parameters.invoiceId}">
											<#assign total=(list.quantity)! 1 * (list.amount)! 1>                                                                                   
												<tr>
													<td>${(list.invoiceItemSeqId)!}</td>
													<td>${(list.partyId)!}</td>
													<td>${(list.origAmount)!}</td>
													<td>${(list.origCurrencyUomId)!}</td>
													<td>${(list.currencyUomId)!}${(list.amount)!}</td>
													<td>${(list.currencyUomId)!}</td>
													<td>${(list.invoiceItemTypeId)!}</td>
													<td><a href="GlAccountNavigate?glAccountId=${(list.overrideGlAccountId)!}">${(list.overrideGlAccountId)!}</a></td>
													<td>${(list.overrideOrgPartyId)!}</td>
													<td>${(list.inventoryItemId)!}</td>
													<td><a href="/catalog/control/EditProduct?productId=${(list.productId)!}">${(list.productId)!}</a></td>
													<td>${(list.productFeatureId)!}</td>
													<td>${(list.parentInvoiceId)!}</td>
													<td>${(list.parentInvoiceItemSeqId)!}</td>
													<td>${(list.uomId)!}</td>
													<td>${(list.taxableFlag)!}</td>
													<td>${(list.quantity)!}</td>
													<td>${(list.description)!}</td>
													<td>${(list.taxAuthPartyId)!}</td>
													<td>${(list.taxAuthGeoId)!}</td>
													<td>${(list.taxAuthorityRateSeqId)!}</td>
													<td>${(list.salesOpportunityId)!}</td>
													<td><a href="/ordermgr/control/orderview?orderId=${(list.orderId)!}">${(list.orderId)!}</a></td>
													<td>${(list.orderItemSeqId)!}</td>
													<td>${(list.currencyUomId)!}${total}</td>
												
												</tr> 
                                                                                           </#if>                                                      
                                                                                        </#list>
                                                                                           <tbody>
                                                                                        </table>
</div>
</div>									
</article>
</div>


<script type="text/javascript">

		
		$(document).ready(function() {
			
			pageSetUp();

	
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
		    
  
		    // Apply the filter
		    $("#datatable_fixed_column thead th input[type=text]").on( 'keyup change', function () {
		    	
		        otable
		            .column( $(this).parent().index()+':visible' )
		            .search( this.value )
		            .draw();
		            
		    } );
		    /* END COLUMN FILTER */   
	    
			/* COLUMN SHOW - HIDE */
			$('#datatable_col_reorder1').dataTable({
				"sDom": "<'dt-toolbar'<'col-xs-12 col-sm-6'f><'col-sm-6 col-xs-6 hidden-xs'C>r>"+
						"t"+
						"<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-sm-6 col-xs-12'p>>",
				"autoWidth" : false,

				"oLanguage": {
					"sSearch": '<span class="input-group-addon"><i class="glyphicon glyphicon-search"></i></span>'
				},
				"preDrawCallback" : function() {
					// Initialize the responsive datatables helper once.
					if (!responsiveHelper_datatable_col_reorder) {
						responsiveHelper_datatable_col_reorder = new ResponsiveDatatablesHelper($('#datatable_col_reorder1'), breakpointDefinition);
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
		    $("div.toolbar").html('<div class="text-right"><img src="img/logo.png" alt="SmartAdmin" style="width: 111px; margin-top: 3px; margin-right: 10px;"></div>');

	
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
		            "sSwfPath": "/images/BusinessInnovation/js/plugin/datatables/swf/copy_csv_xls_pdf.swf"
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

<#--.....................................Transaction....................................................-->

      <#assign acctgTransAndEntries = delegator.findAll("AcctgTransAndEntries",true) />
                  <div class="row">
                    <article  class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-7" data-widget-editbutton="false">
								
<header>
 <span><h2>&ensp;${uiLabelMap.AccountingFinAccountTransations}</h2></span>
</header>
				
								<!-- widget div-->
								<div role="content">
										<div id="datatable_col_reorder_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
										<table id="datatable_col_reorder" class="table table-striped table-bordered table-hover" style="width:100% !important;table-layout: fixed;">
											<thead>
												<tr>
													<th>${uiLabelMap.FormFieldTitle_acctgTransId}</th>
													<th>${uiLabelMap.FormFieldTitle_acctgTransEntryTypeId}</th>
													<th>${uiLabelMap.FormFieldTitle_isPosted}</th>
													<th>${uiLabelMap.FormFieldTitle_glFiscalType}</th>
													<th>${uiLabelMap.FormFieldTitle_transactionDate}</th>
													<th>${uiLabelMap.FormFieldTitle_postedDate}</th>
													<th>${uiLabelMap.FormFieldTitle_transTypeDescription}</th>
													<th>${uiLabelMap.FormFieldTitle_paymentId}</th>
													<th>${uiLabelMap.FormFieldTitle_fixedAssetId}</th>
													<th>${uiLabelMap.AcctgGlJrnlDescLabel}</th>
													<th>${uiLabelMap.FormFieldTitle_glAccountId}</th>
													<th>${uiLabelMap.FormFieldTitle_productId}</th>
													<th>${uiLabelMap.FormFieldTitle_debit}/${uiLabelMap.FormFieldTitle_credit}</th>
													<th>${uiLabelMap.FormFieldTitle_invoiceAmount}</th>
													<th>${uiLabelMap.FormFieldTitle_origAmount}</th>
													<th>${uiLabelMap.FormFieldTitle_accountCode}</th>
													<th>${uiLabelMap.FormFieldTitle_accountName}</th>
													<th>${uiLabelMap.PartyId}</th>
												</tr>
											</thead>
											<tbody>
                                                                                  <#list acctgTransAndEntries as list> 
                                                                                      <#if "${(list.invoiceId)! '0'}" == "${parameters.invoiceId}" >
												<tr>
													<td><a href="EditAcctgTrans?acctgTransId=${list.acctgTransId}&organizationPartyId=${list.organizationPartyId}">${(list.acctgTransId)!}</a></td>
													<td>${(list.acctgTransEntrySeqId)!}</td>
													<td>${(list.isPosted)!}</td>
													<td>${(list.glFiscalTypeId)!}</td>
													<td>${(list.transactionDate)!}</td>
													<td>${(list.postedDate)!}</td>
													<td>${(list.transTypeDescription)!}</td>
													<td><a href="paymentOverview?paymentId=${(list.paymentId)!}">${(list.paymentId)!}</a></td>
													<td>${(list.fixedAssetId)!}</td>
													<td>${(list.description)!}</td>
													<td>${(list.glAccountId)!}</td>
													<td>${(list.productId)!}</td>
													<td>${(list.debitCreditFlag)!}</td>
													<td>${(list.amount)!}${(list.currencyUomId)!}</td>
													<td>${(list.origCurrencyUomId)!}${(list.origAmount)!}</td>
													<td>${(list.accountCode)!}</td>
													<td>${(list.accountName)!}</td>
													<td>${(list.partyId)!}</td>
												
												</tr>
                                                                                           
											</#if>
                                                                                        </#list>
                                                                                           <tbody>
                                                                                        </table>
										    </div>
</div>
</div>

</article>
</div>


<script type="text/javascript">

		
		$(document).ready(function() {
			
			pageSetUp();

	
			/* BASIC ;*/
				var responsiveHelper_dt_basic1 = undefined;
				var responsiveHelper_datatable_fixed_column1 = undefined;
				var responsiveHelper_datatable_col_reorder1 = undefined;
				var responsiveHelper_datatable_tabletools1 = undefined;
				
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
						if (!responsiveHelper_dt_basic1) {
							responsiveHelper_dt_basic1 = new ResponsiveDatatablesHelper($('#dt_basic'), breakpointDefinition);
						}
					},
					"rowCallback" : function(nRow) {
						responsiveHelper_dt_basic1.createExpandIcon(nRow);
					},
					"drawCallback" : function(oSettings) {
						responsiveHelper_dt_basic1.respond();
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
					if (!responsiveHelper_datatable_fixed_column1) {
						responsiveHelper_datatable_fixed_column1 = new ResponsiveDatatablesHelper($('#datatable_fixed_column'), breakpointDefinition);
					}
				},
				"rowCallback" : function(nRow) {
					responsiveHelper_datatable_fixed_column1.createExpandIcon(nRow);
				},
				"drawCallback" : function(oSettings) {
					responsiveHelper_datatable_fixed_column1.respond();
				}		
			
		    });
		    
		    // custom toolbar
		    $("div.toolbar").html('<div class="text-right"><img src="img/logo.png" alt="SmartAdmin" style="width: 111px; margin-top: 3px; margin-right: 10px;"></div>');
		    	   
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
				"autoWidth" : false,

				"oLanguage": {
					"sSearch": '<span class="input-group-addon"><i class="glyphicon glyphicon-search"></i></span>'
				},
				"preDrawCallback" : function() {
					// Initialize the responsive datatables helper once.
					if (!responsiveHelper_datatable_col_reorder1) {
						responsiveHelper_datatable_col_reorder1 = new ResponsiveDatatablesHelper($('#datatable_col_reorder'), breakpointDefinition);
					}
				},
				"rowCallback" : function(nRow) {
					responsiveHelper_datatable_col_reorder1.createExpandIcon(nRow);
				},
				"drawCallback" : function(oSettings) {
					responsiveHelper_datatable_col_reorder1.respond();
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
		            "sSwfPath": "/images/BusinessInnovation/js/plugin/datatables/swf/copy_csv_xls_pdf.swf"
		        },
				"autoWidth" : true,
				"preDrawCallback" : function() {
					// Initialize the responsive datatables helper once.
					if (!responsiveHelper_datatable_tabletools1) {
						responsiveHelper_datatable_tabletools1 = new ResponsiveDatatablesHelper($('#datatable_tabletools'), breakpointDefinition);
					}
				},
				"rowCallback" : function(nRow) {
					responsiveHelper_datatable_tabletools1.createExpandIcon(nRow);
				},
				"drawCallback" : function(oSettings) {
					responsiveHelper_datatable_tabletools1.respond();
				}
			});
			
			/* END TABLETOOLS */
		
		})



		</script>

        <script src="/images/BusinessInnovation/js/plugin/jqgrid/jquery.jqGrid.min.js"></script>
        <script src="/images/BusinessInnovation/js/plugin/jqgrid/grid.locale-en.min.js"></script>

		<script src="/images/BusinessInnovation/js/plugin/datatables/jquery.dataTables.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/datatables/dataTables.colVis.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/datatables/dataTables.tableTools.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/datatables/dataTables.bootstrap.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/datatable-responsive/datatables.responsive.min.js"></script>

           
    