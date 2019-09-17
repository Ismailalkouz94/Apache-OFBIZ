<#--for tabs-->
<link rel="stylesheet" type="text/css" href="/images/BusinessInnovation/sh2/css/tabs.css" />
<link rel="stylesheet" type="text/css" href="/images/BusinessInnovation/sh2/css/tabstyles.css" />
<#--for widget-->
<link href="/images/BusinessInnovation/sh/examples-offline.css" rel="stylesheet">
<link href="/images/BusinessInnovation/sh/kendo.common.min.css" rel="stylesheet">
<script src="/images/BusinessInnovation/sh/jszip.min.js"></script>
<script src="/images/BusinessInnovation/sh/kendo.all.min.js"></script>
<script src="/images/BusinessInnovation/sh/console.js"></script>
<link href="/images/BusinessInnovation/css/editablegraph.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/images/BusinessInnovation/for-accMain/style3.css" />
<script type="text/javascript" src="/images/BusinessInnovation/for-accMain/modernizr.custom.04022.js"></script>
<script type='text/javascript' src="/images/BusinessInnovation/js/jqBarGraph.1.1.js"></script>
<!-- Resources -->
<script src="/images/BusinessInnovation/js/amcharts.js"></script>
<script src="/images/BusinessInnovation/js/serial.js"></script>
<script src="/images/BusinessInnovation/js/export.min.js"></script>

<section id="widget-grid" style="margin-top:20px">
      <div id="chartdiv11"></div>
  <script>
        var chart = AmCharts.makeChart("chartdiv11", {
                                        "theme": "light",
                                        "type": "serial",
                                            "startDuration": 2,
                                        "dataProvider": [{
                                            "country": "Revenue",
                                            "visits": 10,
                                            "color": "#FF0F00"
                                        }, {
                                            "country": "Income",
                                            "visits": 25,
                                            "color": "#FF9E01"
                                        }, {
                                            "country": "Expense",
                                            "visits": 30,
                                            "color": "#F8FF01"
                                        }, {
                                            "country": "NetIncome",
                                            "visits": 45,
                                            "color": "#B0DE09"
                                        },],
                                        "valueAxes": [{
                                            "position": "left",
                                            "title": "Profit And Loss"
                                        }],
                                        "graphs": [{
                                            "balloonText": "[[category]]: <b>[[value]]</b>",
                                            "fillColorsField": "color",
                                            "fillAlphas": 1,
                                            "lineAlpha": 0.1,
                                            "type": "column",
                                            "valueField": "visits"
                                        }],
                                        "depth3D": 20,
                                            "angle": 30,
                                        "chartCursor": {
                                            "categoryBalloonEnabled": false,
                                            "cursorAlpha": 0,
                                            "zoomable": false
                                        },
                                        "categoryField": "country",
                                        "categoryAxis": {
                                            "gridPosition": "start",
                                            "labelRotation": 0
                                        },
                                        "export": {
                                            "enabled": true
                                         }

                                    });
      </script>

<div class="row">
<div class="col-sm-12" >
<div class="well" id="dashin">
	<div class="row">
            
					<div class="col-xs-12 col-sm-7 col-md-7 col-lg-4">
						<h1 class="page-title txt-color-blueDark pgb">
							&ensp;<i class="fa fa-bar-chart-o fa-fw ">Chart</i> 
								<!--Chart-->

						</h1>
					</div>
					<div class="col-xs-12 col-sm-5 col-md-5 col-lg-8">
						<ul id="sparks" class="" style="margin-right:28px">
							<li class="sparks-info">
								<h5> ${uiLabelMap.Revenue} <span class="txt-color-blue">$${revenueBalanceTotal}</span></h5>
								<div class="sparkline txt-color-blue hidden-mobile hidden-md hidden-sm">
									1300, 1877, 2500, 2577, 2000, 2100, 3000, 2700, 3631, 2471, 2700, 3631, 2471
								</div>
							</li>
							<li class="sparks-info">
								<h5>${uiLabelMap.NetIncome} <span class="txt-color-purple"><i class="fa fa-arrow-circle-up" data-rel="bootstrap-tooltip" title="Increased"></i>&nbsp;${netIncome}%</span></h5>
								<div class="sparkline txt-color-purple hidden-mobile hidden-md hidden-sm">
									110,150,300,130,400,240,220,310,220,300, 270, 210
								</div>
							</li>
							<li class="sparks-info">
								<h5> ${uiLabelMap.Expenses} <span class="txt-color-greenDark"><i class="fa fa-shopping-cart"></i>&nbsp;${expenseBalanceTotal}</span></h5>
								<div class="sparkline txt-color-greenDark hidden-mobile hidden-md hidden-sm">
									110,150,300,130,400,240,220,310,220,300, 270, 210
								</div>
							</li>
						</ul>
					</div>
				</div>
</div>








      <section class="New-tabs">
	    <input id="tab-1" type="radio" name="radio-set" class="tab-selector-1" checked="checked" />
	    <label for="tab-1" class="tab-label-1">Trial Balance</label>
	    <input id="tab-2" type="radio" name="radio-set" class="tab-selector-2" />
	    <label for="tab-2" class="tab-label-2">Proft And Loss</label>
            <input id="tab-3" type="radio" name="radio-set" class="tab-selector-3" />
	    <label for="tab-3" class="tab-label-3">Revenue</label>
            
	    <div class="clear-shadow"></div>

            <div class="content">
		    <div class="content-1">
                                       <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable" style="top: 10px;">
                                         <div class="demo-section k-content wide">
					   <form  method="post" class="smart-form" action="<@ofbizUrl>main</@ofbizUrl>">
					   <table align="center" id="table1">
					       <tr>
						   <td><label>${uiLabelMap.FormFieldTitle_datefrom}</label><span class="required">*</span>&nbsp;&nbsp;</td>
						   <td>
						    <span class="view-calendar">
						    <fieldset>
                                                            <label class="input"><i class="icon-append fa fa-calendar"></i>
                                                            <input class="form-control" type="text"  name="fromDate" id="fromDate"  required/>
                                                            </label>
							</fieldset>
						     </span>

                                                   </td>
						   <!--<td> <input type="text" id="myInputPartyId" onkeyup="findAllowencesEmployee()" placeholder="Search for party id.." title="Type in a name" style="border-radius: 4px; padding: 10px 15px;"></td>-->
						   <td>  &nbsp;&nbsp;</td>
						   <td><label>${uiLabelMap.FormFieldTitle_dateThru}</label><span class="required">*</span>&nbsp;&nbsp;</td>
                                                   <td>   
						    <span class="view-calendar">
						    <fieldset>      
                                                            <label class="input"><i class="icon-append fa fa-calendar"></i>
                                                            <input class="form-control"  type="text" name="toDate" id="toDate"  required/>
                                                            </label>
							</fieldset>
						     </span>
                                                   </td>
						   <td><input type="text" class="form-control" name="selectedMonth" id="selectedMonth"  hidden="true" value="0"/></td>

						   <td>  &nbsp;&nbsp;</td>
						   <td><input class="min btn btn-primary" type="submit"  value="${uiLabelMap.CommonSubmit}" /></td>
					       </tr>
					       </table> 
					   </form>

					   <div id="chart2"></div>
</div>
                                         </article>

				<script>
$("#fromDate").datepicker({
          dateFormat: 'yy-mm-dd',
             changeMonth: true,
             changeYear: true,
             yearRange: "-120:+50",
            buttonImageOnly: true
});
$("#toDate").datepicker({
          dateFormat: 'yy-mm-dd',
             changeMonth: true,
             changeYear: true,
             yearRange: "-120:+50",
            buttonImageOnly: true
});


                   
			       function addNewlines(str) {
				 var result = '';
				 while (str.length > 0) {
				   result += str.substring(0, 12) + '\n';
				   str = str.substring(12);
				 }
				 return result;
				}
                                  
					     <#assign biTrialBalance = Static["org.apache.ofbiz.accounting.dashboard.Dashboard"].BiTrialBalance(request, response)>

						   document.getElementById('fromDate').value ="${(session.getAttribute("fDate"))!}";
						  document.getElementById('toDate').value ="${(session.getAttribute("tDate"))!}";  
					       function createChart2() {
						   $("#chart2").kendoChart({
						       legend: {
							   position: "bottom"
						       },

						       seriesDefaults: {
							   type: "column"
						       },

						       series: [{
							   name: "Debit",
							   data: [<#list biTrialBalance as list>${(list.DR)!},</#list>],
							   color: "#95af4c",

						       },

							 {
							   name: "Credit",
							   data: [<#list biTrialBalance as list>${(list.CR)!},</#list>],
							   color: "#eac864"
						       }, {
							   name: "Balance",
							   data: [<#list biTrialBalance as list>${(list.BALANCE)!},</#list>],
							   color: "#a9c4d9"
						       }],
						       categoryAxis: {
							   categories: [<#list biTrialBalance as list>addNewlines("${(list.ACCOUNT_NAME)!}"),</#list>],
							   majorGridLines: {
							       visible: false
							   },   labels: {
							       padding: {top: 90},
							   //                                rotation: -10 ,
								 font: "10px Arial,Helvetica,sans-serif"
								   },
							   select: {
							       from: 2,
							       to: 5
							   }


						       },

						       selectStart: onSelectStart,
						       select: onSelect,
						       selectEnd: onSelectEnd
						   });
					       }

					       function formatRange(e) {
						   var categories = e.axis.categories;

						   return kendo.format("{0} - {1} ({2} - {3})",
						       e.from, e.to,
						       categories[e.from],
						       // The last category included in the selection is at (to - 1)
						       categories[e.to - 1]
						   );
					       }

					       function onSelectStart(e) {
						   kendoConsole.log(kendo.format("Select start :: {0}", formatRange(e)));

					       }

					       function onSelect(e) {
						   kendoConsole.log(kendo.format("Select :: {0}", formatRange(e)));
					       }

					       function onSelectEnd(e) {
						   kendoConsole.log(kendo.format("Select end :: {0}", formatRange(e)));
					       }

					       function setOptions() {
						   var chart = $("#chart2").data("kendoChart");

						   $.extend(true /* deep */, chart.options, {
						       categoryAxis: {
							   select: {
							       mousewheel: {
								   reverse: $("#reverse").prop("checked"),

								   zoom: $("#zoom").val()
							       }
							   }
						       }
						   });

						   chart.refresh();
					       }

					       $("#reverse, #zoom").click(setOptions);

					       $(document).ready(createChart2);
					       $("#example").bind("kendo:skinChange", createChart2);

					   </script>
					    <article class="col-xs-6 col-sm-6 col-md-6 col-lg-6 sortable-grid ui-sortable">

						  <div class="demo-section k-content wide">
						    <div id="chart" style="margin-left:-10px"></div>
						</div>   

					      <script>
						    colors =['#c23531','#2f4554','#61a0a8','#d48265','#91c7ae','#749f83','#ca8622','#bda29a','#6e7074','#546570','#c4ccd3','#c23531','#749f83','#61a0a8','#d48265','#c4ccd3','#fed403','#cd82ad','#b8b941'];
						    function createChart() {
							    <#assign counter=0 />
							$("#chart").kendoChart({

							    title: {
								text: "Debit"
							    },
							    legend: {
							       position: "right",
								labels: {
								    font: "11px Verdana"
								}
							    },
							    chartArea: {
								width: 480,
								height: 300
							    },
							    series: [{
								type: "pie",

								data: [<#list biTrialBalance as list>{
								    category: "${(list.ACCOUNT_NAME)!}",
								    value: "${(list.DR)!}",
								    color: colors[${counter}]
									   <#assign counter=counter+1 />     
								},</#list>
					                         ]
							    }],
							    tooltip: {
								visible: true,
								template: "#= category # - #= kendo.format('{0:P}', percentage) #"
							    }
							});
						    }

						    $(document).ready(function() {
							createChart();
							$(document).bind("kendo:skinChange", createChart);
							$(".box").bind("change", refresh);
						    });

						    function refresh() {
							var chart = $("#chart").data("kendoChart"),
							    pieSeries = chart.options.series[0],
							    labels = $("#labels").prop("checked"),
							    alignInputs = $("input[name='alignType']"),
							    alignLabels = alignInputs.filter(":checked").val();

							chart.options.transitions = false;
							pieSeries.labels.visible = labels;
							pieSeries.labels.align = alignLabels;

							alignInputs.attr("disabled", !labels);

							chart.refresh();
						    }
						</script>
					    </article>
					    <article class="col-xs-6 col-sm-6 col-md-6 col-lg-6 sortable-grid ui-sortable">
						  <div class="demo-section k-content wide">
						    <div id="chart1" style="margin-left:-10px"></div>
						</div>   

					    <script>
					    function createChart1() {
							$("#chart1").kendoChart({
							    title: {
								text: "Credit"
							    },
							    legend: {
							       position: "right",
								labels: {
								    font: "11px Verdana"
								}
							    },
							    chartArea: {
								width: 480,
								height: 300
							    },
							    series: [{
								type: "pie",
							       data: [<#list biTrialBalance as list>{
								    category: "${(list.ACCOUNT_NAME)!}",
								    value: "${(list.CR)!}",
								   color: colors[${counter}]
									   <#assign counter=counter+1 />  
								},</#list>
					    ]
							    }],
							    tooltip: {
								visible: true,
								template: "#= category # - #= kendo.format('{0:P}', percentage) #"
							    }
							});
						    }

						    $(document).ready(function() {
							createChart1();
							$(document).bind("kendo:skinChange", createChart1);
							$(".box").bind("change", refresh);
						    });

						    function refresh() {
							var chart = $("#chart1").data("kendoChart"),
							    pieSeries = chart.options.series[0],
							    labels = $("#labels").prop("checked"),
							    alignInputs = $("input[name='alignType']"),
							    alignLabels = alignInputs.filter(":checked").val();

							chart.options.transitions = false;
							pieSeries.labels.visible = labels;
							pieSeries.labels.align = alignLabels;

							alignInputs.attr("disabled", !labels);

							chart.refresh();
						    }
                                                  
						</script>
					    </article>

		    </div>
		    <div class="content-2">
                        <div class="butAv">		
			    <input type="radio" name="fill-graph" onclick='DrowChartReloadR("R")' id="f-product12" /><label for="f-product12">Revenue</label>
			    <input type="radio" name="fill-graph" onclick='DrowChartReloadI("I")' id="f-product12" /><label for="f-product12">Income</label>
			    <input type="radio" name="fill-graph" onclick='DrowChartReloadE("E")' id="f-product12" /><label for="f-product12">Expense</label>
			    <input type="radio" name="fill-graph" onclick='DrowChartReloadN("N")' id="f-product12" /><label for="f-product12"  style="width: 90px;">Net Income</label>
			    <br>
			    <span class="button-label">Months:</span>
			    <input type="radio" name="fill-graph" onclick='DrowChart(0)' id="f-product0" checked/><label for="f-none">Jan</label>
			    <input type="radio" name="fill-graph" onclick='DrowChart(1)' id="f-product1"  /><label for="f-product1">Feb</label>
			    <input type="radio" name="fill-graph" onclick='DrowChart(2)' id="f-product2" /><label for="f-product2">Mar</label>
			    <input type="radio" name="fill-graph" onclick='DrowChart(3)' id="f-product3" /><label for="f-product3">Apr</label>
			    <input type="radio" name="fill-graph" onclick='DrowChart(4)' id="f-product4" /><label for="f-product4">May</label>
			    <input type="radio" name="fill-graph" onclick='DrowChart(5)' id="f-product5" /><label for="f-product5">Jun</label>
			    <input type="radio" name="fill-graph" onclick='DrowChart(6)' id="f-product6" /><label for="f-product6">Jul</label>
			    <input type="radio" name="fill-graph" onclick='DrowChart(7)' id="f-product7" /><label for="f-product7">Aug</label>
			    <input type="radio" name="fill-graph" onclick='DrowChart(8)' id="f-product8" /><label for="f-product8">Sept</label>
			    <input type="radio" name="fill-graph" onclick='DrowChart(9)' id="f-product9" /><label for="f-product9">Oct</label>
			    <input type="radio" name="fill-graph" onclick='DrowChart(10)' id="f-product10" /><label for="f-product10">Nov</label>
			    <input type="radio" name="fill-graph" onclick='DrowChart(11)' id="f-product11" /><label for="f-product11">Dec</label>
                        </div>

              
                
                <!-- Styles -->
<style>
#chartdiv {
  width: 600px;
  height: 400px;
  margin-left:180px
}
</style>



<!-- Chart code -->
      <script>
          
              $("#tab-3").one( "click", function() {
                        DrowChartRevenues(0);
                });
                                function DrowChart(selectedMonth){
                                    $("#chartdiv").show();
                                    $("#chart-3").hide();
                                    $("#chart-R").hide();    
                                    $("#chart-I").hide(); 
                                    $("#chart-E").hide(); 
                                    $("#chart-N").hide(); 
                              $.ajax({
                                    'async':false,
                                    type: "post",
                                    url: "main2",
                                    data: "selectedMonth="+selectedMonth,
                                    success: function (data) {
                                    console.log("** data  **");                                       
                                                                         
                                      
//                                      new way
                                      var n =data.search('Beginning');
                                      var n1 =data.search('Ending');    
                                      var str = data.substring(n,n1);    
                                        
                                      var n3 = str.search('revenueBalanceTotal=');     
                                      var n4 =str.search(';//end1');      
                                      var numRevenue =  str.substring(n3+21,n4); 
                                      
                                      
                                      var n5 = str.search('incomeBalanceTotal=');     
                                      var n6 =str.search(';//end2');      
                                      var numIncome =  str.substring(n5+20,n6); 
                                       
                                      var n7 = str.search('expenseBalanceTotal=');     
                                      var n8 =str.search(';//end3');      
                                      var numExpense =  str.substring(n7+21,n8); 
                                          
                                      var n9 = str.search('netIncome=');     
                                      var n10 =str.search(';//end4');      
                                      var numNetIncome =  str.substring(n9+11,n10); 
                                             console.log("numRevenue "+numRevenue); 
                                                  console.log("numIncome "+numIncome); 
                                                       console.log("numExpense "+numExpense); 
                                                            console.log("numNetIncome "+numNetIncome); 
                                                                
//                                    console.log("numNetIncome : "+numNetIncome); 
                                    DrowChartReload(numRevenue,numIncome,numExpense,numNetIncome);                  
                                    }
                                });  // End Of ajax
                             }
               function DrowChartReload(numRevenue,numIncome,numExpense,numNetIncome){
                                var chart = AmCharts.makeChart("chartdiv", {
                                        "theme": "light",
                                        "type": "serial",
                                            "startDuration": 2,
                                        "dataProvider": [{
                                            "country": "Revenue",
                                            "visits": numRevenue,
                                            "color": "#FF0F00"
                                        }, {
                                            "country": "Income",
                                            "visits": numIncome,
                                            "color": "#FF9E01"
                                        }, {
                                            "country": "Expense",
                                            "visits": numExpense,
                                            "color": "#F8FF01"
                                        }, {
                                            "country": "NetIncome",
                                            "visits": numNetIncome,
                                            "color": "#B0DE09"
                                        },],
                                        "valueAxes": [{
                                            "position": "left",
                                            "title": "Profit And Loss"
                                        }],
                                        "graphs": [{
                                            "balloonText": "[[category]]: <b>[[value]]</b>",
                                            "fillColorsField": "color",
                                            "fillAlphas": 1,
                                            "lineAlpha": 0.1,
                                            "type": "column",
                                            "valueField": "visits"
                                        }],
                                        "depth3D": 20,
                                            "angle": 30,
                                        "chartCursor": {
                                            "categoryBalloonEnabled": false,
                                            "cursorAlpha": 0,
                                            "zoomable": false
                                        },
                                        "categoryField": "country",
                                        "categoryAxis": {
                                            "gridPosition": "start",
                                            "labelRotation": 0
                                        },
                                        "export": {
                                            "enabled": true
                                         }

                                    });
                                        }   
                  function DrowChartReloadR(drowAllRevenue){
                                $("#chartdiv").hide();
                                $("#chart-3").hide();
                                $("#chart-R").show(); 
                                $("#chart-I").hide(); 
                                $("#chart-E").hide(); 
                                $("#chart-N").hide(); 
                                    
                       data = "${listOfRevenue}";                
                       var drowAllMonth = data.split(","); 
                       var month= ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"];       
                       var allMonthRevenue =  [];  
                            for (i = 0; i < 12; i++){
                                 var x1={
                                          "country": month[i],
                                          "visits": drowAllMonth[i],
                                          "color": "#FF0F00"
                                      };
                                allMonthRevenue.push(x1);
                            }
                           var chart = AmCharts.makeChart("chart-R", {
                                    "theme": "light",
                                    "type": "serial",
                                    "startDuration": 2,
                                    "dataProvider": allMonthRevenue,
                                    "valueAxes": [{
                                        "position": "left",
                                        "title": "Revenue"
                                    }],
                                    "graphs": [{
                                        "balloonText": "[[category]]: <b>[[value]]</b>",
                                        "fillColorsField": "color",
                                        "fillAlphas": 1,
                                        "lineAlpha": 0.1,
                                        "type": "column",
                                        "valueField": "visits"
                                    }],
                                    "depth3D": 20,
                                        "angle": 30,
                                    "chartCursor": {
                                        "categoryBalloonEnabled": false,
                                        "cursorAlpha": 0,
                                        "zoomable": false
                                    },
                                    "categoryField": "country",
                                    "categoryAxis": {
                                        "gridPosition": "start",
                                        "labelRotation": 90
                                    },
                                    "export": {
                                        "enabled": true
                                     }

                                });  
                     }   // end of function DrowChartReloadR
                     function DrowChartReloadI(drowAllMonth){
                                    $("#chartdiv").hide();
                                    $("#chart-3").hide();
                                    $("#chart-R").hide(); 
                                    $("#chart-I").show(); 
                                    $("#chart-E").hide(); 
                                    $("#chart-N").hide();  
                                            
                       data = "${listOfIncome}";                
                       var drowAllMonth = data.split(","); 
                       var month= ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"];       
                       var allMonthIncome =  [];  
                            for (i = 0; i < 12; i++){
                                 var x1={
                                          "country": month[i],
                                          "visits": drowAllMonth[i],
                                          "color": "#FF6600"
                                      };
                                allMonthIncome.push(x1);
                            }
                           var chart = AmCharts.makeChart("chart-I", {
                                    "theme": "light",
                                    "type": "serial",
                                    "startDuration": 2,
                                    "dataProvider": allMonthIncome,
                                    "valueAxes": [{
                                        "position": "left",
                                        "title": "Income"
                                    }],
                                    "graphs": [{
                                        "balloonText": "[[category]]: <b>[[value]]</b>",
                                        "fillColorsField": "color",
                                        "fillAlphas": 1,
                                        "lineAlpha": 0.1,
                                        "type": "column",
                                        "valueField": "visits"
                                    }],
                                    "depth3D": 20,
                                        "angle": 30,
                                    "chartCursor": {
                                        "categoryBalloonEnabled": false,
                                        "cursorAlpha": 0,
                                        "zoomable": false
                                    },
                                    "categoryField": "country",
                                    "categoryAxis": {
                                        "gridPosition": "start",
                                        "labelRotation": 90
                                    },
                                    "export": {
                                        "enabled": true
                                     }

                                });  
                     }   // end of function DrowChartReloadI   
                      function DrowChartReloadE(drowAllMonth){
                                    $("#chartdiv").hide();
                                    $("#chart-3").hide();
                                    $("#chart-R").hide(); 
                                    $("#chart-I").hide(); 
                                    $("#chart-E").show(); 
                                    $("#chart-N").hide();   
                       data = "${listOfExpense}"; 
                       var drowAllMonth = data.split(","); 
                       var month= ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"];       
                       var allMonthExpense =  [];  
                            for (i = 0; i < 12; i++){
                                 var x1={
                                          "country": month[i],
                                          "visits": drowAllMonth[i],
                                          "color": "#F8FF01"
                                      };
                                allMonthExpense.push(x1);
                            }
                           var chart = AmCharts.makeChart("chart-E", {
                                    "theme": "light",
                                    "type": "serial",
                                    "startDuration": 2,
                                    "dataProvider": allMonthExpense,
                                    "valueAxes": [{
                                        "position": "left",
                                        "title": "Expense"
                                    }],
                                    "graphs": [{
                                        "balloonText": "[[category]]: <b>[[value]]</b>",
                                        "fillColorsField": "color",
                                        "fillAlphas": 1,
                                        "lineAlpha": 0.1,
                                        "type": "column",
                                        "valueField": "visits"
                                    }],
                                    "depth3D": 20,
                                        "angle": 30,
                                    "chartCursor": {
                                        "categoryBalloonEnabled": false,
                                        "cursorAlpha": 0,
                                        "zoomable": false
                                    },
                                    "categoryField": "country",
                                    "categoryAxis": {
                                        "gridPosition": "start",
                                        "labelRotation": 90
                                    },
                                    "export": {
                                        "enabled": true
                                     }

                                });  
                     }   // end of function DrowChartReloadE   
                          function DrowChartReloadN(drowAllMonth){
                                    $("#chartdiv").hide();
                                    $("#chart-3").hide();
                                    $("#chart-R").hide(); 
                                    $("#chart-I").hide(); 
                                    $("#chart-E").hide(); 
                                    $("#chart-N").show();   
                       data = "${listOfNetIncome}";                
                       var drowAllMonth = data.split(","); 
                       var month= ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"];       
                       var allMonthNetIncome =  [];  
                            for (i = 0; i < 12; i++){
                                 var x1={
                                          "country": month[i],
                                          "visits": drowAllMonth[i],
                                          "color": "#B0DE09"
                                      };
                                allMonthNetIncome.push(x1);
                            }
                           var chart = AmCharts.makeChart("chart-N", {
                                    "theme": "light",
                                    "type": "serial",
                                    "startDuration": 2,
                                    "dataProvider": allMonthNetIncome,
                                    "valueAxes": [{
                                        "position": "left",
                                        "title": "Net Income"
                                    }],
                                    "graphs": [{
                                        "balloonText": "[[category]]: <b>[[value]]</b>",
                                        "fillColorsField": "color",
                                        "fillAlphas": 1,
                                        "lineAlpha": 0.1,
                                        "type": "column",
                                        "valueField": "visits"
                                    }],
                                    "depth3D": 20,
                                        "angle": 30,
                                    "chartCursor": {
                                        "categoryBalloonEnabled": false,
                                        "cursorAlpha": 0,
                                        "zoomable": false
                                    },
                                    "categoryField": "country",
                                    "categoryAxis": {
                                        "gridPosition": "start",
                                        "labelRotation": 90
                                    },
                                    "export": {
                                        "enabled": true
                                     }

                                });  
                     }   // end of function DrowChartReloadN     
                                 
</script>
 
<!-- HTML -->
<div id="chartdiv"></div>
<div id="chart-3"></div> 
<div id="chart-R"></div> 
<div id="chart-I"></div> 
<div id="chart-E"></div> 
<div id="chart-N"></div> 
<style>
#chart-R {
  width: 100%;
  height: 500px;
}
#chart-I {
  width: 100%;
  height: 500px;
}
#chart-E {
  width: 100%;
  height: 500px;
}
#chart-N {
  width: 100%;
  height: 500px;
}
</style>
		    </div>
		    <div class="content-3" style="width:100%;padding: 40px 0px 0px 14px;">
                        <div class="widget-body-toolbar bg-color-white smart-form" style="width: 100%;"  id="rev-toggles"> 
                   
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


			    <div class="padding-10" >
				    <div id="flotcontainer"  class="chart-large has-legend-unique"></div>
			    </div>	

		    </div>
	    </div>
      </section>























<#--for tabs-->
<script src="/images/BusinessInnovation/sh2/js/cbpFWTabs.js"></script>
<script>
(function() {
[].slice.call( document.querySelectorAll( '.tabs' ) ).forEach( function( el ) {
new CBPFWTabs( el );
});
})();
</script>
		

<script>

    $(window).load(function() {
        DrowChart(0);  
       // DrowChartRevenues(0);
    });
    function DrowChartRevenues(selectedMonth){
			
                        var numRevenueList = [] , revenueListForecast = [] , revenueListPlan = [];
                           
                            listOfRevenue = "${listOfRevenue}";                
                            var drowAllMonth = listOfRevenue.split(",");      
                            var allMonthRevenue =  [];  
                            
                            listOfRevenueForecast = "${listOfRevenueForecast}";                
                            var drowAllMonthForecast = listOfRevenueForecast.split(",");      
                            var allMonthRevenueForecast =  [];
                                
                            listOfRevenuePlan = "${listOfRevenuePlan}";                
                            var drowAllMonthPlan = listOfRevenuePlan.split(",");      
                            var allMonthRevenuePlan =  [];    
                                
                            for (i = 0; i < 12; i++){
                                    var subRevenueList = [];    
                                    subRevenueList[0]=i;
                                    subRevenueList[1]=drowAllMonth[i];  
                                    numRevenueList.push(subRevenueList); 
                                     
                                    var subRevenueListForecast = []; 
                                    subRevenueListForecast[0]=i;
                                    subRevenueListForecast[1]=drowAllMonthForecast[i];  
                                    revenueListForecast.push(subRevenueListForecast);  
                                        
                                    var subRevenueListPlan = [];    
                                    subRevenueListPlan[0]=i;
                                    subRevenueListPlan[1]=drowAllMonthPlan[i];  
                                    revenueListPlan.push(subRevenueListPlan);      
                                        
                            }
                               
                                DrowChartRevenuesReload(numRevenueList,revenueListForecast,revenueListPlan);        
                                        } // end DrowChartRevenues
           function DrowChartRevenuesReload (numRevenueList,revenueListForecast,revenueListPlan){

                       $(function() {
//                        [[0, 647], [1, 435], [2, 784], [3, 346], [4, 487], [5, 463], [6, 479], [7, 236], [8, 843], [9, 657], [10, 241], [11, 341]]
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
				

         
<script type="text/javascript">
                        $(document).ready(function() 
                        {	
                           
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
                            document.getElementById('fromDate').value ="${(session.getAttribute("fDate"))!}";
                            document.getElementById('toDate').value ="${(session.getAttribute("tDate"))!}";  
			    
                                                                    
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




