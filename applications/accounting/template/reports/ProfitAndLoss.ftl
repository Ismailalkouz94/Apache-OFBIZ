<#ftl encoding="utf-8">
 
  

            <section id="widget-grid" class="">

                                        <!-- row -->
                <div class="row">

                                                <!-- NEW WIDGET START -->
                    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                      <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
			    <header>
			    <span class="jarviswidget-loader"><i class="fa fa-refresh fa-spin"></i></span>
			    </header>
			    <div role="content">
                        <table id="jqgrid"></table>
                        <div id="pjqgrid"></div>

                        </div>
                        </div>
                        </article>
                        <!-- WIDGET END -->

                    </div>
    
    

        <script src="/images/BusinessInnovation/js/plugin/jqgrid/jquery.jqGrid.min.js"></script>
        <script src="/images/BusinessInnovation/js/plugin/jqgrid/grid.locale-en.min.js"></script>






        <script>
            
            var jqgrid_data=[];
                var decription_date="";
function pushData(){
   

                         
                        

                    
                     <#assign Grade = delegator.findAll("Grade",true) />
                       
                        
                         <#list Grade as list>
                             
                        pageSetUp();
                            
                     jqgrid_data.push ({
                                accountcode :"${(list.gradeId)!''}",
                                accountname : "${(list.description)!}",
                                balance : "${(list.refId)!''}",
                               

                                 
                        } );
                            
                       
    
                 </#list>           
}

           pushData();
                        jQuery("#jqgrid").jqGrid({
                                datastr: jqgrid_data,
                                datatype : "jsonstring",
                                height : 'auto',
                                colNames : ['Account Name','Account Code', 'Balance', ],
                                colModel : [ {
                                        name : 'accountcode',
                                        index : 'accountcode',
                                        editable : false,
                                                 
                                                    
                                },{
                                    key:true,
                                        name : 'accountname',
                                        index : 'accountname',
                                         editable : false,
                                                           
                                }, {
                                        name : 'balance',
                                        index : 'balance',
                                        editable : true
                                                    
                                }],
                                          
//          onSelectRow: function(id) {
//    $(this).jqGrid('editGridRow', id, {afterSubmit: reloadGrid});
//},                          

  onSelectRow : function (id){
      
              },    
                                rowNum : 100,
                                rowList : [10, 20, 30],
//                                pager : '#pjqgrid',
                                sortname : 'gradeId',
                                toolbarfilter : true,
                                viewrecords : true,
                                onAfterSaveCell: true,
                                loadonce:true,
                                                       
                                           
                                  reloadAfterSubmit:true,
                                sortorder : "asc",
                                    scrollable:true,
                                        caption : "${uiLabelMap.Income}",
                                        multiselect : false,
                                        autowidth : true


                        }
                      
                        ),
                            

     
                 jQuery("#jqgrid").jqGrid('inlineNav', "#pjqgrid");
                        /* Add tooltips */
                        $('.navtable .ui-pg-button').tooltip({
                                container : 'body'
                        });

                        jQuery("#m1").click(function() {
                                var s;
                                s = jQuery("#jqgrid").jqGrid('getGridParam', 'selarrrow');
                                alert(s);
                        });
                    
                       


                        jQuery("#m1s").click(function() {
                                jQuery("#jqgrid").jqGrid('setSelection', "13");
                        });

                        
                $(window).on('resize.jqGrid', function() {
                        $("#jqgrid").jqGrid('setGridWidth', $("#content").width());
                })

 
function date(){
     return  function (elem) {
           
           
            setTimeout(function () {
                $(elem).datepicker({
                    dateFormat: 'yy-mm-dd',
                    changeYear: true,
                    changeMonth: true,     
                    buttonImageOnly: true,
                    
                    showButtonPanel: true,
                    onSelect: function (dateText, inst) {
                        inst.input.focus();
                        if (typeof (inst.id) === "string" && inst.id.substr(0, 3) === "gs_") {
                             $(inst.id).val(dateText);
                             grid[0].triggerToolbar();
                        }
                        else {
                            $(inst).trigger("change");
                        }
                    }
                });
            }, 100);
        }

}
    
            </script>

        
        
        
        <!--..........................22222...................................................................................................................-->
        

                <div class="row">

                                                <!-- NEW WIDGET START -->
                    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                       <div class="jarviswidget jarviswidget-color-blueDark"  id="wid-id-1" data-widget-editbutton="false">
			    <header>
			    <span class="jarviswidget-loader"><i class="fa fa-refresh fa-spin"></i></span>
			    </header>
			    <div role="content">
                        <table id="jqgrid1"></table>
                        <div id="pjqgrid1"></div>

                         </div>
                         </div>
                        </article>
                        <!-- WIDGET END -->

                    </div>



        <script>
            
            var jqgrid_data1=[];
                var decription_date="";
function pushData(){
   

                         
                        

                    
                     <#assign Grade = delegator.findAll("Grade",true) />
                       
                        
                         <#list Grade as list>
                             
                        pageSetUp();
                            
                     jqgrid_data1.push ({
                                accountcode :"${(list.gradeId)!''}",
                                accountname : "${(list.description)!}",
                                balance : "${(list.refId)!''}",
                               

                                 
                        } );
                            
                       
    
                 </#list>           
}

           pushData();
                        jQuery("#jqgrid1").jqGrid({
                                datastr: jqgrid_data1,
                                datatype : "jsonstring",
                                height : 'auto',
                                colNames : ['Account Name','Account Code', 'Balance', ],
                                colModel : [ {
                                        name : 'accountcode',
                                        index : 'accountcode',
                                        editable : false,
                                                 
                                                    
                                },{
                                    key:true,
                                        name : 'accountname',
                                        index : 'accountname',
                                         editable : false,
                                                           
                                }, {
                                        name : 'balance',
                                        index : 'balance',
                                        editable : true
                                                    
                                }],
                                          
//          onSelectRow: function(id) {
//    $(this).jqGrid('editGridRow', id, {afterSubmit: reloadGrid});
//},                          

  onSelectRow : function (id){
      
              },    
                                rowNum : 100,
                                rowList : [10, 20, 30],
//                                pager : '#pjqgrid1',
                                sortname : 'gradeId',
                                toolbarfilter : true,
                                viewrecords : true,
                                onAfterSaveCell: true,
                                loadonce:true,
                                                       
                                           
                                  reloadAfterSubmit:true,
                                sortorder : "asc",
                                    scrollable:true,
                                        caption : "${uiLabelMap.Revenue}",
                                        multiselect : false,
                                        autowidth : true


                        }
                      
                        ),
                            

     
                 jQuery("#jqgrid1").jqGrid('inlineNav', "#pjqgrid1");
                        /* Add tooltips */
                        $('.navtable .ui-pg-button').tooltip({
                                container : 'body'
                        });

                        jQuery("#m1").click(function() {
                                var s;
                                s = jQuery("#jqgrid1").jqGrid('getGridParam', 'selarrrow');
                                alert(s);
                        });
                    
                       


                        jQuery("#m1s").click(function() {
                                jQuery("#jqgrid1").jqGrid('setSelection', "13");
                        });

                        
                $(window).on('resize.jqGrid', function() {
                        $("#jqgrid1").jqGrid('setGridWidth', $("#content").width());
                })

 
function date(){
     return  function (elem) {
           
           
            setTimeout(function () {
                $(elem).datepicker({
                    dateFormat: 'yy-mm-dd',
                    changeYear: true,
                    changeMonth: true,     
                    buttonImageOnly: true,
                    
                    showButtonPanel: true,
                    onSelect: function (dateText, inst) {
                        inst.input.focus();
                        if (typeof (inst.id) === "string" && inst.id.substr(0, 3) === "gs_") {
                             $(inst.id).val(dateText);
                             grid[0].triggerToolbar();
                        }
                        else {
                            $(inst).trigger("change");
                        }
                    }
                });
            }, 100);
        }

}
    
            </script>

        
        
        <!--.......................33333......................................................................................................................-->
        

                <div class="row">

                                                <!-- NEW WIDGET START -->
                    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                      <div class="jarviswidget jarviswidget-color-blueDark"  id="wid-id-2" data-widget-editbutton="false">
			    <header>
			    <span class="jarviswidget-loader"><i class="fa fa-refresh fa-spin"></i></span>
			    </header>
			    <div role="content">
                        <table id="jqgrid2"></table>
                        <div id="pjqgrid2"></div>

                        </div>
                        </div>
                        </article>
                        <!-- WIDGET END -->

                    </div>
           




           <script>
            
            var jqgrid_data2=[];
                var decription_date="";
function pushData(){
   

                     
                        

                    
                     <#assign Grade = delegator.findAll("Grade",true) />
         <#assign ProfitAndLossExpense = Static["org.apache.ofbiz.accounting.report.ProfitAndLoss"].getReportXMLProfitAndLossExpense(request, response)>

                        
                         <#list ProfitAndLossExpense as list>
                             
                       
                            
                     jqgrid_data2.push ({
                                accountcode :"${(list.GL_ACCOUNT_ID)!''}",
                                accountname : "${(list.ACCOUNT_NAME)!}",
                                balance : "${(list.BALANCE)!''}",
                               

                                 
                        } );
                            
                       
    
                 </#list>           
}

           pushData();
                        jQuery("#jqgrid2").jqGrid({
                                datastr: jqgrid_data2,
                                datatype : "jsonstring",
                                height : 'auto',
                                colNames : ['Account Name','Account Code', 'Balance', ],
                                colModel : [ {
                                        name : 'accountcode',
                                        index : 'accountcode',
                                        editable : false,
                                                 
                                                    
                                },{
                                    key:true,
                                        name : 'accountname',
                                        index : 'accountname',
                                         editable : false,
                                                           
                                }, {
                                        name : 'balance',
                                        index : 'balance',
                                        editable : true
                                                    
                                }],
                                          
//          onSelectRow: function(id) {
//    $(this).jqGrid('editGridRow', id, {afterSubmit: reloadGrid});
//},                          

  onSelectRow : function (id){
      
              },    
                                rowNum : 100,
                                rowList : [10, 20, 30],
//                                pager : '#pjqgrid2',
                                sortname : 'gradeId',
                                toolbarfilter : true,
                                viewrecords : true,
                                onAfterSaveCell: true,
                                loadonce:true,
                                                       
                                           
                                  reloadAfterSubmit:true,
                                sortorder : "asc",
                                    scrollable:true,
                                        caption : "${uiLabelMap.Expenses}",
                                        multiselect : false,
                                        autowidth : true


                        }
                      
                        ),
                            

     
                 jQuery("#jqgrid2").jqGrid('inlineNav', "#pjqgrid2");
                        /* Add tooltips */
                        $('.navtable .ui-pg-button').tooltip({
                                container : 'body'
                        });

                        jQuery("#m1").click(function() {
                                var s;
                                s = jQuery("#jqgri2").jqGrid('getGridParam', 'selarrrow');
                                alert(s);
                        });
                    
                       


                        jQuery("#m1s").click(function() {
                                jQuery("#jqgrid2").jqGrid('setSelection', "13");
                        });

                        
                $(window).on('resize.jqGrid', function() {
                        $("#jqgrid2").jqGrid('setGridWidth', $("#content").width());
                })

 
function date(){
     return  function (elem) {
           
           
            setTimeout(function () {
                $(elem).datepicker({
                    dateFormat: 'yy-mm-dd',
                    changeYear: true,
                    changeMonth: true,     
                    buttonImageOnly: true,
                    
                    showButtonPanel: true,
                    onSelect: function (dateText, inst) {
                        inst.input.focus();
                        if (typeof (inst.id) === "string" && inst.id.substr(0, 3) === "gs_") {
                             $(inst.id).val(dateText);
                             grid[0].triggerToolbar();
                        }
                        else {
                            $(inst).trigger("change");
                        }
                    }
                });
            }, 100);
        }

}
    
            </script>
           
           
           <!--......................44444444...........................-->
           
           
           
                <div class="row">

                                                <!-- NEW WIDGET START -->
                    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                      <div class="jarviswidget jarviswidget-color-blueDark"  id="wid-id-2" data-widget-editbutton="false">
			    <header>
			    <span class="jarviswidget-loader"><i class="fa fa-refresh fa-spin"></i></span>
			    </header>
			    <div role="content">
                        <table id="jqgrid3"></table>
                        <div id="pjqgrid3"></div>

                        </div>
                        </div>
                        </article>
                        <!-- WIDGET END -->

                    </div>
           




           <script>
            
            var jqgrid_data3=[];
                var decription_date="";
function pushData(){
   

                         
                        

                    
                     <#assign Grade = delegator.findAll("Grade",true) />
                       
                        
                         <#list Grade as list>
                             
                        pageSetUp();
                            
                     jqgrid_data3.push ({
                                accountcode :"${(list.gradeId)!''}",
                                accountname : "${(list.description)!}",
                                balance : "${(list.refId)!''}",
                               

                                 
                        } );
                            
                       
    
                 </#list>           
}

           pushData();
                        jQuery("#jqgrid3").jqGrid({
                                datastr: jqgrid_data3,
                                datatype : "jsonstring",
                                height : 'auto',
                                colNames : ['Account Name','Account Code', 'Balance', ],
                                colModel : [ {
                                        name : 'accountcode',
                                        index : 'accountcode',
                                        editable : false,
                                                 
                                                    
                                },{
                                    key:true,
                                        name : 'accountname',
                                        index : 'accountname',
                                         editable : false,
                                                           
                                }, {
                                        name : 'balance',
                                        index : 'balance',
                                        editable : true
                                                    
                                }],
                                          
//          onSelectRow: function(id) {
//    $(this).jqGrid('editGridRow', id, {afterSubmit: reloadGrid});
//},                          

  onSelectRow : function (id){
      
              },    
                                rowNum : 100,
                                rowList : [10, 20, 30],
//                                pager : '#pjqgrid2',
                                sortname : 'gradeId',
                                toolbarfilter : true,
                                viewrecords : true,
                                onAfterSaveCell: true,
                                loadonce:true,
                                                       
                                           
                                  reloadAfterSubmit:true,
                                sortorder : "asc",
                                    scrollable:true,
                                        caption : "${uiLabelMap.CostOfSereviceSold}",
                                        multiselect : false,
                                        autowidth : true


                        }
                      
                        ),
                            

     
                 jQuery("#jqgrid3").jqGrid('inlineNav', "#pjqgrid2");
                        /* Add tooltips */
                        $('.navtable .ui-pg-button').tooltip({
                                container : 'body'
                        });

                        jQuery("#m1").click(function() {
                                var s;
                                s = jQuery("#jqgri2").jqGrid('getGridParam', 'selarrrow');
                                alert(s);
                        });
                    
                       


                        jQuery("#m1s").click(function() {
                                jQuery("#jqgrid3").jqGrid('setSelection', "13");
                        });

                        
                $(window).on('resize.jqGrid', function() {
                        $("#jqgrid3").jqGrid('setGridWidth', $("#content").width());
                })

 
function date(){
     return  function (elem) {
           
           
            setTimeout(function () {
                $(elem).datepicker({
                    dateFormat: 'yy-mm-dd',
                    changeYear: true,
                    changeMonth: true,     
                    buttonImageOnly: true,
                    
                    showButtonPanel: true,
                    onSelect: function (dateText, inst) {
                        inst.input.focus();
                        if (typeof (inst.id) === "string" && inst.id.substr(0, 3) === "gs_") {
                             $(inst.id).val(dateText);
                             grid[0].triggerToolbar();
                        }
                        else {
                            $(inst).trigger("change");
                        }
                    }
                });
            }, 100);
        }

}
    
            </script>
 </section>