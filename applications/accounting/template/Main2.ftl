<#--for tabs-->
<link rel="stylesheet" type="text/css" href="/images/BusinessInnovation/sh2/css/tabs.css" />
<link rel="stylesheet" type="text/css" href="/images/BusinessInnovation/sh2/css/tabstyles.css" />
<#--for widget-->
<link href="/images/BusinessInnovation/sh/examples-offline.css" rel="stylesheet">
<link href="/images/BusinessInnovation/sh/kendo.common.min.css" rel="stylesheet">
<link href="/images/BusinessInnovation/sh/kendo.rtl.min.css" rel="stylesheet">
<link href="/images/BusinessInnovation/sh/kendo.default.min.css" rel="stylesheet">
<link href="/images/BusinessInnovation/sh/kendo.default.mobile.min.css" rel="stylesheet">
<script src="/images/BusinessInnovation/sh/jquery.min.js"></script>
<script src="/images/BusinessInnovation/sh/jszip.min.js"></script>
<script src="/images/BusinessInnovation/sh/kendo.all.min.js"></script>
<script src="/images/BusinessInnovation/sh/console.js"></script>
<link href="/images/BusinessInnovation/css/editablegraph.css" rel="stylesheet">
	<link href="/images/BusinessInnovation/theme-darkness-js/css/material-dashboard.css?v=1.2.0" rel="stylesheet" />



<section id="widget-grid" style="margin-top:20px">


	<div class="tabs tabs-style-tzoid">
        
	   <div class="content-wrap">
			
			
			
                           <div class="widget-body-toolbar bg-color-white smart-form" id="rev-toggles">
				    <div class="inline-group">

					    <label for="gra-0" class="checkbox">
						    <input type="checkbox" name="gra-0" id="gra-0" checked="checked">
						    <i></i> Actual </label>
					    <label for="gra-1" class="checkbox">
						    <input type="checkbox" name="gra-1" id="gra-1" checked="checked">
						    <i></i> Forecast </label>
					    <label for="gra-2" class="checkbox">
						    <input type="checkbox" name="gra-2" id="gra-2" checked="checked">
						    <i></i> Plan </label>
				    </div>											
			    </div>
<!--                             				<span class="button-label">Months:</span>
                <input type="radio" name="fill-graph" onclick='DrowChartRevenues(0)'  /><label for="f-none">Jan</label>
                <input type="radio" name="fill-graph" onclick='DrowChartRevenues(1)'/><label for="f-product1">Feb</label>
                <input type="radio" name="fill-graph" onclick='DrowChartRevenues(2)'/><label for="f-product2">Mar</label>
                <input type="radio" name="fill-graph" onclick='DrowChartRevenues(3)'/><label for="f-product3">Apr</label>
                <input type="radio" name="fill-graph" onclick='DrowChartRevenues(4)'/><label for="f-product4">May</label>
                <input type="radio" name="fill-graph" onclick='DrowChartRevenues(5)'/><label for="f-product5">Jun</label>
                <input type="radio" name="fill-graph" onclick='DrowChartRevenues(6)'/><label for="f-product6">Jul</label>
                <input type="radio" name="fill-graph" onclick='DrowChartRevenues(7)'/><label for="f-product7">Aug</label>
                <input type="radio" name="fill-graph" onclick='DrowChartRevenues(8)'/><label for="f-product8">Sept</label>
                <input type="radio" name="fill-graph" onclick='DrowChartRevenues(9)'/><label for="f-product9">Oct</label>
                <input type="radio" name="fill-graph" onclick='DrowChartRevenues(10)'/><label for="f-product10">Nov</label>
                <input type="radio" name="fill-graph" onclick='DrowChartRevenues(11)'/><label for="f-product11">Dec</label>
-->

			    <div class="padding-10">
				    <div id="flotcontainer" class="chart-large has-legend-unique"></div>
			    </div>	

                    

	   </div>
        </div>

<#--for tabs-->
<script src="/images/BusinessInnovation/sh2/js/cbpFWTabs.js"></script>
<script>
(function() {
[].slice.call( document.querySelectorAll( '.tabs' ) ).forEach( function( el ) {
new CBPFWTabs( el );
});
})();
</script>
		











<section id="widget-grid" style="margin-top:20px;display:none">

<!--my own  -->
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">



<!-- row -->
					<div class="row">
						<article class="col-sm-12">
							<!-- new widget -->
							<div class="jarviswidget" id="wid-id-0" data-widget-togglebutton="false" data-widget-editbutton="false" data-widget-fullscreenbutton="false" data-widget-colorbutton="false" data-widget-deletebutton="false">
								<header>
									<span class="widget-icon"> <i class="glyphicon glyphicon-stats txt-color-darken"></i> </span>
									<h2>${uiLabelMap.ChartBar} </h2>

									<ul class="nav nav-tabs pull-right in" id="myTab" style="margin-top: 10px;">
										<li class="active">
											<a data-toggle="tab" href="#s1"><i class=""></i> <span class="hidden-mobile hidden-tablet">${uiLabelMap.AccountingTrialBalance}</span></a>
										</li>

										<li >
											<a id="ss" data-toggle="tab" href="#s2" ><i class=""></i> <span class="hidden-mobile hidden-tablet">${uiLabelMap.ProfitAndLoss}</span></a>
										</li>

										<li>
											<a data-toggle="tab" href="#s3"><i class=""></i> <span class="hidden-mobile hidden-tablet">${uiLabelMap.Revenue}</span></a>
										</li>
									</ul>

								</header>

								<!-- widget div-->
								<div class="no-padding">
									<!-- widget edit box -->
									<div class="jarviswidget-editbox">

										test
									</div>
									<!-- end widget edit box -->

									<div class="widget-body">
										<!-- content -->
										<div id="myTabContent" class="tab-content">
											<div class="tab-pane fade active in padding-10 no-padding-bottom" id="s1">
										<article class="col-xs-6 col-sm-12 col-md-6 col-lg-12">		
													<div>

											<!-- widget edit box -->
											<div class="jarviswidget-editbox">
												<!-- This area used as dropdown edit box -->
												<input class="form-control" type="text">	
											</div>
											<!-- end widget edit box -->

											<!-- widget content -->
											<div class="widget-body">


												<form  method="post" action="<@ofbizUrl>main</@ofbizUrl>">
												<table align="center" id="table1">
												    <tr>
													<td><label>${uiLabelMap.FormFieldTitle_datefrom}</label><span class="required">*</span>&nbsp;&nbsp;</td>
													<td><td><label class="input"><input class="form-control" type="date"   name="fromDate" id="fromDate"  required/></label></td></td>
													<!--<td> <input type="text" id="myInputPartyId" onkeyup="findAllowencesEmployee()" placeholder="Search for party id.." title="Type in a name" style="border-radius: 4px; padding: 10px 15px;"></td>-->
													<td>  &nbsp;&nbsp;</td>
													<td><label>${uiLabelMap.FormFieldTitle_dateThru}</label><span class="required">*</span>&nbsp;&nbsp;</td>
													<td><input type="date" class="form-control" name="toDate" id="toDate"  required/></td>
                                                                                                        <td><input type="text" class="form-control" name="selectedMonth" id="selectedMonth"  hidden="true" value="0"/></td>
                                                                                                        
                                                                                                        <td>  &nbsp;&nbsp;</td>
													<td><input class="min btn btn-primary" type="submit"  value="${uiLabelMap.CommonSubmit}" /></td>
												    </tr>
												    </table> 
												</form>
														<!-- this is what the user will see -->
                                                                                            
												<canvas  id="barChart" height="100"></canvas>

											</div>
											<!-- end widget content -->


										</div>
										<!-- end widget div -->	
										</article>
										<article class="col-xs-12 col-sm-6 col-md-6 col-lg-6">


																	<!-- Widget ID (each widget will need unique ID)-->
																		<!-- widget div-->
																		<div class="jarviswidget" id="wid-id-0" data-widget-togglebutton="false" data-widget-editbutton="false" data-widget-fullscreenbutton="false" data-widget-colorbutton="false" data-widget-deletebutton="false">
																			<!-- widget edit box -->
																			<div class="jarviswidget-editbox">
																				<!-- This area used as dropdown edit box -->
																				<input class="form-control" type="text">	
																			</div>
																			<!-- end widget edit box -->

																			<!-- widget content -->
																			<div class="widget-body">
                                                                                                                                                            
                                                                                                                                                            <label>${uiLabelMap.FormFieldTitle_debit}:</label>

																				<!-- this is what the user will see -->
																				<canvas id="doughnutChart" height="120"></canvas>

																			</div>
																			<!-- end widget content -->

																		</div>
																		<!-- end widget div -->

									</article>
                                                                 <article class="col-xs-12 col-sm-6 col-md-6 col-lg-6">


										    <!-- widget div-->
										<div class="jarviswidget" id="wid-id-0" data-widget-togglebutton="false" data-widget-editbutton="false" data-widget-fullscreenbutton="false" data-widget-colorbutton="false" data-widget-deletebutton="false">

											    <!-- widget edit box -->
											    <div class="jarviswidget-editbox">
												    <!-- This area used as dropdown edit box -->
												    <input class="form-control" type="text">	
											    </div>
											    <!-- end widget edit box -->

											    <!-- widget content -->
											    <div class="widget-body">

												    <!-- this is what the user will see -->
                                                                                                <label>${uiLabelMap.FormFieldTitle_credit}:</label>
												    <canvas id="pieChart" height="120"></canvas>

											    </div>
											    <!-- end widget content -->

										    </div>
										    <!-- end widget div -->
					
					   	                          </article>
					                                </div>
                                                                                    
									<div class="tab-pane fade" id="s2">
											<div class="col-sm-12 well">
											    <div class="col-sm-6">
                                                                                                
                                                                                                <form  method="post" action="<@ofbizUrl>main</@ofbizUrl>">
                                                                                                    <br>
												<table id="table2">
												    <tr>
													<td><label> ${uiLabelMap.SelectMonth} :</label></td>
													<td><select name="selectedMonth" id="months" > 
                                                                                                                        <option value="0">${uiLabelMap.AccountingFiscalMonth01}</option>
                                                                                                                        <option value="1">${uiLabelMap.AccountingFiscalMonth02}</option>
                                                                                                                        <option value="2">${uiLabelMap.AccountingFiscalMonth03}</option>
                                                                                                                        <option value="3">${uiLabelMap.AccountingFiscalMonth04}</option>
                                                                                                                        <option value="4">${uiLabelMap.AccountingFiscalMonth05}</option>
                                                                                                                        <option value="5">${uiLabelMap.AccountingFiscalMonth06}</option>
                                                                                                                        <option value="6">${uiLabelMap.AccountingFiscalMonth07}</option>
                                                                                                                        <option value="7">${uiLabelMap.AccountingFiscalMonth08}</option>
                                                                                                                        <option value="8">${uiLabelMap.AccountingFiscalMonth09}</option>
                                                                                                                        <option value="9">${uiLabelMap.AccountingFiscalMonth10}</option>
                                                                                                                        <option value="10">${uiLabelMap.AccountingFiscalMonth11}</option>
                                                                                                                        <option value="11">${uiLabelMap.AccountingFiscalMonth12}</option>
                                                                                                                      </select><span class="required">&nbsp;&nbsp;*</span></td>
                                                                                                        
													<!--<td> <input type="text" id="myInputPartyId" onkeyup="findAllowencesEmployee()" placeholder="Search for party id.." title="Type in a name" style="border-radius: 4px; padding: 10px 15px;"></td>-->
													
													<!--<td><input class="btn btn-primary" type="submit" value="submit"  /></td>-->
                                                                                                        
												    </tr>
												    </table> 
												</form>
                                                                                               
                                                                                               
											       <table class="highchart table table-hover table-bordered" data-graph-container=".. .. .highchart-container2" data-graph-type="column">
												  <caption</caption>
												  <thead style="color:white">
												    <tr>
												      <th align="center">${uiLabelMap.Month}</th> 
												      <th align="center" class="">${uiLabelMap.Revenue}</th>
												      <th align="center" class="">${uiLabelMap.Income}</th>
												      <th align="center" class="">${uiLabelMap.Expenses}</th>
												      <th align="center" class="">${uiLabelMap.NetIncome}</th>
                                                                                                      

												    </tr>
												  </thead>
												  <tbody>
												    <tr>
                                                                                                      <td align="center" id="m">
                                                                                                          <#if parameters.selectedMonth=='0' >
                                                                                                        ${uiLabelMap.AccountingFiscalMonth01}
                                                                                                        <#elseif parameters.selectedMonth=='1'>
                                                                                                        ${uiLabelMap.AccountingFiscalMonth02}
                                                                                                        <#elseif parameters.selectedMonth=='2'>
                                                                                                        ${uiLabelMap.AccountingFiscalMonth03} 
                                                                                                        <#elseif parameters.selectedMonth=='3'>
                                                                                                        ${uiLabelMap.AccountingFiscalMonth04} 
                                                                                                        <#elseif parameters.selectedMonth=='4'>
                                                                                                        ${uiLabelMap.AccountingFiscalMonth05}
                                                                                                        <#elseif parameters.selectedMonth=='5'>
                                                                                                        ${uiLabelMap.AccountingFiscalMonth06} 
                                                                                                        <#elseif parameters.selectedMonth=='6'>
                                                                                                        ${uiLabelMap.AccountingFiscalMonth07}
                                                                                                        <#elseif parameters.selectedMonth=='7'>
                                                                                                        ${uiLabelMap.AccountingFiscalMonth08}
                                                                                                        <#elseif parameters.selectedMonth=='8'>
                                                                                                        ${uiLabelMap.AccountingFiscalMonth09} 
                                                                                                        <#elseif parameters.selectedMonth=='9'>
                                                                                                        ${uiLabelMap.AccountingFiscalMonth010}
                                                                                                        <#elseif parameters.selectedMonth=='10'>
                                                                                                        ${uiLabelMap.AccountingFiscalMonth11}
                                                                                                        <#elseif parameters.selectedMonth=='11'>
                                                                                                        ${uiLabelMap.AccountingFiscalMonth12}
                                                                                                        </#if>
                                                                                                        
                                                                                                        
                                                                                                        </td>
                                                                                                        
												      <td class="" align="center">${revenueBalanceTotal}</td>
												      <td class="" align="center">${incomeBalanceTotal}</td>
												      <td class="" align="center">${expenseBalanceTotal}</td>
												      <td class="" align="center">${netIncome}</td>
                                                                                                          
												    </tr>
												  </tbody>
												</table>
												    </div>
												    <div class="col-sm-6">
													    <div class="highchart-container2"></div>
												    </div>
											     </div>
											</div>

											<div class="tab-pane fade" id="s3">
                                                                                           
											</div>

										</div>

										<!-- end content -->
									</div>

								</div>
								<!-- end widget div -->
							</div>
							<!-- end widget -->

						</article>
					</div>



 </div>

</div>

					
</section>

<script>
    $( document ).ready(function() {
//        alert( "ready!" );
//DrowChartRevenues(0);
    });
    $(window).load(function() {
 DrowChartRevenues(0);
        DrowChart(0);
        //Fires when the page is loaded completely

            
    });
    function DrowChartRevenues(selectedMonth){
                        var numRevenueList = [] , revenueListForecast = [] , revenueListPlan = [];
                            for (i = 0; i < 12; i++) { 
                            
                              $.ajax({
                                    'async':false,
                                    type: "post",
                                    url: "main2",
                                    data: {selectedMonth : i , glFiscalTypeId : "ACTUAL"},
                                    success: function (data) {
                                          
                                  
                                         
                                    var n =data.search('Beginning');
                                    var n1 =data.search('Ending');    
                                    var str = data.substring(n,n1);    

                                    var n3 = str.search('revenueBalanceTotal=');     
                                    var n4 =str.search(';//end1');      
                                    var numRevenue =  str.substring(n3+21,n4);
                                    var subRevenueList = [];    
                                    subRevenueList[0]=i;
                                    subRevenueList[1]=numRevenue;  
                                    numRevenueList.push(subRevenueList);   
                                    }
                            });  // End Of ajax ACTUAL
                            $.ajax({
                                    'async':false,
                                    type: "post",
                                    url: "main2",
                                    data: {selectedMonth : i , glFiscalTypeId : "FORECAST"},
                                    success: function (data) {
                                          
                                  
                                         
                                    var n =data.search('Beginning');
                                    var n1 =data.search('Ending');    
                                    var str = data.substring(n,n1);    

                                    var n3 = str.search('revenueBalanceTotal=');     
                                    var n4 =str.search(';//end1');      
                                    var numRevenue =  str.substring(n3+21,n4);
                                    var subRevenueList = [];    
                                    subRevenueList[0]=i;
                                    subRevenueList[1]=numRevenue;  
                                    revenueListForecast.push(subRevenueList);   
                                    }
                                });  // End Of ajax FORECAST
                                $.ajax({
                                    'async':false,
                                    type: "post",
                                    url: "main2",
                                    data: {selectedMonth : i , glFiscalTypeId : "PLAN"},
                                    success: function (data) {
                                          
                                  
                                         
                                    var n =data.search('Beginning');
                                    var n1 =data.search('Ending');    
                                    var str = data.substring(n,n1);    

                                    var n3 = str.search('revenueBalanceTotal=');     
                                    var n4 =str.search(';//end1');      
                                    var numRevenue =  str.substring(n3+21,n4);
                                    var subRevenueList = [];    
                                    subRevenueList[0]=i;
                                    subRevenueList[1]=numRevenue;  
                                    revenueListPlan.push(subRevenueList);   
                                    }
                                });  // End Of ajax PLAN   
                               }   // End Of for  
                                console.log(revenueListPlan);  
                                DrowChartRevenuesReload(numRevenueList,revenueListForecast,revenueListPlan);        
                                        } // end DrowChartRevenues
           function DrowChartRevenuesReload (numRevenueList,revenueListForecast,revenueListPlan){
               
                                   
                       $(function() {
//                                       [[1354586000000, 500], [1364587000000, 658], [1374588000000, 198], [1384589000000, 663], [1394590000000, 801], [1404591000000, 1080], [1414592000000, 353], [1424593000000, 749], [1434594000000, 523], [1444595000000, 258], [1454596000000, 688], [1464597000000, 364]], 
//                                       [[0, 500], [1, 658], [2, 198], [3, 663], [4, 801], [5, 1080], [6, 353], [7, 749], [8, 523], [9, 258], [10, 688], [11, 364]]
//                                       [[0, 53], [1, 65], [2, 98], [3, 83], [4, 980], [5, 808], [6, 720], [7, 674], [8, 23], [9, 79], [10, 88], [11, 36]]
//                                       [[0, 647], [1, 435], [2, 784], [3, 346], [4, 487], [5, 463], [6, 479], [7, 236], [8, 843], [9, 657], [10, 241], [11, 341]]
                                         var trgt = numRevenueList ,
                                             prft = revenueListForecast,
                                           sgnups = revenueListPlan,
                                         toggles = $("#rev-toggles"), target = $("#flotcontainer");

					var data = [{
						label : "Target Profit",
						data : trgt,
						bars : {
							show : true,
							align : "center",
							barWidth : 0.6
						}
					}, {
						label : "Actual Profit",
						data : prft,
						color : '#3276B1',
						lines : {
							show : true,
							lineWidth : 3
						},
						points : {
							show : true
						}
					}, {
						label : "Actual Signups",
						data : sgnups,
						color : '#71843F',
						lines : {
							show : true,
							lineWidth : 1
						},
						points : {
							show : true
						}
					}]

					var options = {
						grid : {
							hoverable : true
						},
						tooltip : true,
						tooltipOpts : {
							content: '<span>Target : </span>%y',
							//dateFormat: '%b %y',
							defaultTheme : false
						},
						xaxis : {
//                                                    [[1354586000000, "shatha"], [1364587000000, 658], [1374588000000, 198], [1384589000000, 663], [1394590000000, 801], [1404591000000, 1080], [1414592000000, 353], [1424593000000, 749], [1434594000000, 523], [1444595000000, 258], [1454596000000, 688], [1464597000000, 364]]
							    ticks: [[0, "Jan"], [1, "Feb"], [2, "Mar"], [3, "Apr"], [4, "May"], [5, "Jun"], [6, "Jul"], [7, "Aug"], [8, "Sept"], [9, "Oct"], [10, "Nov"], [11, "Dec"]]                                                                         

                                                    
						},
						yaxes : {
							tickFormatter : function(val, axis) {
								return "$" + val;
							}
							
						}
                                             
					};
                                            
					plot2 = null;
                                          
					function plotNow() {
                                            
						var d = [];
                                                    
						toggles.find(':checkbox').each(function() {
							if ($(this).is(':checked')) {
								d.push(data[$(this).attr("name").substr(4, 1)]);
                                                                  
							}
						});
						if (d.length > 0) {
							if (plot2) {
								plot2.setData(d);
								plot2.draw();
							} else {
								plot2 = $.plot(target, d, options);
                                                                                                                                       
							}
						}
                                               
					};
                                    
					toggles.find(':checkbox').on('change', function() {
						plotNow();
					});

					plotNow()

				});
                                    } // end DrowChartRevenuesReload                                     
    </script>
				
	<script src="/images/BusinessInnovation/js/plugin/moment/moment.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/chartjs/chart.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.cust.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.resize.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.time.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.tooltip.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/highChartCore/highcharts-custom.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/highchartTable/jquery.highchartTable.min.js"></script>
         
<script type="text/javascript">
                        $(document).ready(function() 
                        
                        {	
                        
//                                var id=6;
//                                  $.ajax({
//                                    type: "post",
//                                    url: "getPNL",
//                                    data: "selectedMonth=6",
//                                    success: function (data) {
//                                            alert(data);                 
//                                    }
//                                });   
//                         location.href="main?selectedMonth=" + id;
//                              $(".graph-container > li:nth-child(1) .bar-inner").css("height",${expenseBalanceTotal});
//                alert(${expenseBalanceTotal})
//                    $(".graph-container > li:nth-child(1) .bar-inner").css("height",${revenueBalanceTotal});
//                    $(".graph-container > li:nth-child(2) .bar-inner").css("height",${incomeBalanceTotal});
//                    $(".graph-container > li:nth-child(3) .bar-inner").css("height",${expenseBalanceTotal});
//                    $(".graph-container > li:nth-child(4) .bar-inner").css("height",${netIncome});
                         
                       
                            
                            var id= "";
                               document.getElementById("months").addEventListener("change", function () {
                                   id =document.getElementById("months").value;
                                           location.href="main?selectedMonth=" + id;
//                             
                          });
                              if(${parameters.selectedMonth}==null){
                         document.getElementById("months").selectedIndex = 0; 
                            }else{
                                document.getElementById("months").selectedIndex = ${parameters.selectedMonth}; 
                                }
 $('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
        localStorage.setItem('activeTab', $(e.target).attr('href'));
    });
    var activeTab = localStorage.getItem('activeTab');
    if(activeTab){
        $('#myTab a[href="' + activeTab + '"]').tab('show');
    }
        
                        $('table.highchart').highchartTable();    
         <#assign biTrialBalance = Static["org.apache.ofbiz.accounting.dashboard.Dashboard"].BiTrialBalance(request, response)>
          	
                         
                          
				 pageSetUp();		

				
				var randomScalingFactor = function() {
		            return Math.round(Math.random() * 100);
		            //return 0;
		        };
		        var randomColorFactor = function() {
		            return Math.round(Math.random() * 255);
		        };
		        var randomColor = function(opacity) {
		            return 'rgba(' + randomColorFactor() + ',' + randomColorFactor() + ',' + randomColorFactor() + ',' + (opacity || '.3') + ')';
		        };

		        var LineConfig = {
		            type: 'line',
		            data: {
		                labels: ["January", "February", "March", "April", "May", "June", "July"],
		                datasets: [{
		                    label: "My First dataset",
		                    data: [randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor()],
		                    
		                }, {
		                    label: "My Second dataset",
		                    data: [randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor(), randomScalingFactor()],
		                }]
		            },
		            options: {
		                responsive: true,

		                tooltips: {
		                    mode: 'label'
		                },
		                hover: {
		                    mode: 'dataset'
		                },
		                scales: {
		                    xAxes: [

                                        {
		                        display: true,
		                        scaleLabel: {
		                            show: true,
		                            labelString: 'Month'
		                        }
		                         }],
		                    yAxes: [{
		                        display: true,
		                        scaleLabel: {
		                            show: true,
		                            labelString: 'Value'
		                        },
		                        ticks: {
		                            suggestedMin: 0,
		                            suggestedMax: 100,
                                          
		                        }
		                    }]
		                }
		            }
		        };
                        


  

		        $.each(LineConfig.data.datasets, function(i, dataset) {
		            dataset.borderColor = 'rgba(0,0,0,0.15)';
		            dataset.backgroundColor = randomColor(0.5);
		            dataset.pointBorderColor = 'rgba(0,0,0,0.15)';
		            dataset.pointBackgroundColor = randomColor(0.5);
		            dataset.pointBorderWidth = 1;
		        });

		       // bar chart example
                          
                           
                                                                
//                                                  
                            document.getElementById('fromDate').value ="${(session.getAttribute("fromDate"))!}";
                            document.getElementById('toDate').value ="${(session.getAttribute("toDate"))!}";  
                                                                    
		        var barChartData = {
		            labels: [<#list biTrialBalance as list>"${(list.ACCOUNT_NAME)!}",</#list>], 
                                                                  

		            datasets: [{
		                label: 'Debit',
                              
		                backgroundColor: "rgba(220,220,220,0.5)",
		             data: [<#list biTrialBalance as list>${(list.DR)!},</#list>]
		            }, {
		                label: 'Credit',
		                backgroundColor: "rgba(151,187,205,0.5)",
		                  data: [<#list biTrialBalance as list>${(list.CR)!},</#list>]
		            }, {
                                label: 'Balance',
                                backgroundColor: "rgba(255,0,0,0.5)",
		                  data: [<#list biTrialBalance as list>${(list.BALANCE)!},</#list>]
		            },],
                                options: {
			          			           
			            scale: {
			              reverse: false,
			              ticks: {
			                beginAtZero: true,
                                        fontSize:40
			              }
			            }
			        }

		        };

		       
			    // doughnut example

			    var DoughtnutConfig = {
			        type: 'doughnut',
			        data: {
			            datasets: [{
			               data: [<#list biTrialBalance as list>${(list.DR)!},</#list>],
			                backgroundColor: [
			                    "#b3003b",
			                    "#e3e3e3",
			                    "#97bbcd",
                                                            "#80ff80",
                                                             "#4d94ff",
                                                             "#ffff99",
                                                             "#99ebff" ,
                                                              "#ffccff",
                                                              "#ff9999",
                                                              "#94b8b8",
                                                              "#d9b38c"            
			                ],
			                label: 'Depit'
			            }],
	                         labels: [<#list biTrialBalance as list>"${(list.ACCOUNT_NAME)!}",</#list>],
			        },

			        options: {
			            responsive: true,
			            legend: {
			                position: 'top',
			            }
                                    
			        }
			    };

			  
			    // pie chart example
			    var PieConfig = {
			        type: 'pie',
			        data: {
			            datasets: [{
			                data: [<#list biTrialBalance as list>${(list.CR)!},</#list>],
			                backgroundColor: [
			                    "#b3003b",
			                    "#e3e3e3",
			                    "#97bbcd",
                                                            "#80ff80",
                                                             "#4d94ff",
                                                             "#ffff99",
                                                             "#99ebff" ,
                                                              "#ffccff",
                                                              "#ff9999",
                                                              "#94b8b8",
                                                              "#d9b38c"  
			                ],
			            }],
			            labels: [<#list biTrialBalance as list>"${(list.ACCOUNT_NAME)!}",</#list>],
			        },
			        options: {
			            responsive: true
			        }
			    };
		       
				window.onload = function() {
		            
		            window.myBar = new Chart(document.getElementById("barChart"), {
		                type: 'bar',
		                data: barChartData,
		                options: {
		                    responsive: true,

		                }
		            });
        			window.myDoughnut = new Chart(document.getElementById("doughnutChart"), DoughtnutConfig);
        	
        			window.myPie = new Chart(document.getElementById("pieChart"), PieConfig);
		        };
			    
			})
		
		</script>


		<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.cust.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.resize.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.fillbetween.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.orderBar.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.pie.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.time.min.js"></script>
		<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.tooltip.min.js"></script>	

<script>
$(document).ready(function() {
$( "#myTab li" ).removeClass("tab-current active");
$( "#myTab li" ).removeClass("tab-current active");

$( "#myTab li a" ).click(function() {
$( "#myTab li" ).removeClass("tab-current active");
$( "#myTab li" ).removeClass("tab-current");
 localStorage.setItem('activeTab', $(this).attr('href'));
});
var activeTab = localStorage.getItem('activeTab');

$('a[href="' + activeTab + '"]').parent().addClass("tab-current active");
$(".tab-current.active").click();



</script>
<script>
$("input[name='fill-graph']").click(function() {
 localStorage.setItem('activebutton', $(this).attr('id'));
});
var activeTab11 = localStorage.getItem('activebutton');
$("#"+activeTab11).prop( "checked", true  );
    DrowChartAfterReload();
}) 
</script>