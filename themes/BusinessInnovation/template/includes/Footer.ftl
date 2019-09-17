<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
	   <form class="smart-form" name="InvoiceSubTabBar_copyInvoice_LF_111_" method="post" action="/ap/control/copyInvoice" id="copyDailogInvoice">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                    </button>
                <h4 class="modal-title" id="myModalLabel">Copy Invoice</h4>
                </div>
            <div class="modal-body">
                    <input name="invoiceIdToCopyFrom" id="invoiceIdToCopyFrom" value="" type="hidden">
                    <input name="yearMenu" id="yearMenu" value="" type="hidden">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                Invoice Date    
                                  <@htmlTemplate.renderDateTimeField name="invoiceDate" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="${requestParameters.invoiceDate!}" size="25" maxlength="30" id="invoiceDate" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                                </div>
                            </div>
                        </div>
                </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default modCan" data-dismiss="modal">
                    Cancel
                    </button>
		    <input class="btn btn-primary" type="submit" id="submit" value="submit" >           
                </div>
	        </form>
               </div><!-- /.modal-content -->
              </div><!-- /.modal-dialog -->
             </div><!-- /.modal -->

</section>
</div>
</div>
<div class="modal fade" id="myModalTemp" tabindex="-1" role="dialog" aria-labelledby="myModalLabelTemp" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
	   <form class="smart-form" name="InvoiceSubTabBar_saveInvoiceAsTemplate_LF_161_" method="post" action="/ap/control/copyInvoiceToTemplate" id="TempDailogInvoice">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                    </button>
                <h4 class="modal-title" id="myModalLabelTemp">Save Invoice As Template </h4>
                </div>
            <div class="modal-body">
                    <input name="invoiceId" id="invoiceIdTemp" value="" type="hidden">
                    <input name="invoiceTypeId" id="invoiceTypeIdTemp" value="" type="hidden">
                    <input name="yearMenu" id="yearMenuTemp" value="" type="hidden">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                Invoice Date    
                                  <@htmlTemplate.renderDateTimeField name="invoiceDate" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="${requestParameters.invoiceDate!}" size="25" maxlength="30" id="invoiceDateTemp" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                                </div>
                            </div>
                        </div>
                </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default modCan" data-dismiss="modal">
                    Cancel
                    </button>
		     <input class="btn btn-primary" type="submit" id="submit2" value="submit" >
                </div>
	       </form>
               </div><!-- /.modal-content -->
              </div><!-- /.modal-dialog -->
             </div><!-- /.modal -->

</section>
</div>
</div>
<div class="modal fade" id="myModalTrans" tabindex="-1" role="dialog" aria-labelledby="myModalLabelTrans" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
	  <form class="smart-form" name="EditGlAcctgTransSubTabBar_DuplicateAccountingTransaction" method="post" action="/accounting/control/CustomcopyAcctgTransAndEntries" id="TempDailogTrans">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                    </button>
                <h4 class="modal-title" id="myModalLabelTrans">Duplicate Accounting Transaction</h4>
                </div>
            <div class="modal-body">
                    <input name="organizationPartyId" value="Company" type="hidden">
                    <input name="revert" value="N" type="hidden">
                    <input name="fromAcctgTransId" id="fromAcctgTransId" value="" type="hidden">
                    <input name="year" id="yearMenuTrans" value="" type="hidden">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                Transaction Date   
                                  <@htmlTemplate.renderDateTimeField name="transactionDate" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="${requestParameters.transactionDate!}" size="25" maxlength="30" id="transactionDateTrans" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                                </div>
                            </div>
                        </div>                                   
                </div>
            <div class="modal-footer">
		<button type="button" class="btn btn-default modCan" data-dismiss="modal">
                    Cancel
                </button>
		<input class="btn btn-primary" type="submit" id="submit3" value="submit" > 
                </div>
	       </form>
               </div><!-- /.modal-content -->
              </div><!-- /.modal-dialog -->
             </div><!-- /.modal -->

</section>
</div>
</div>
<div class="modal fade" id="myModalTransRevert" tabindex="-1" role="dialog" aria-labelledby="myModalLabelTransRevert" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
	  <form class="smart-form" name="EditGlAcctgTransSubTabBar_RevertAccountingTransaction" method="post" action="/accounting/control/CustomcopyAcctgTransAndEntries" id="TempDailogTransRevert">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                    </button>
                <h4 class="modal-title" id="myModalLabelTransRevert">Revert Accounting Transaction</h4>
                </div>
            <div class="modal-body">
                    <input name="organizationPartyId" value="Company" type="hidden">
                    <input name="revert" value="Y" type="hidden">
                    <input name="fromAcctgTransId" id="fromAcctgTransIdRevert" value="" type="hidden">
                    <input name="year" id="yearMenuTransRevert" value="" type="hidden">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                Transaction Date   
                                  <@htmlTemplate.renderDateTimeField name="transactionDate" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="${requestParameters.transactionDate!}" size="25" maxlength="30" id="transactionDateTransRevert" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                                </div>
                            </div>
                        </div>
                </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default modCan" data-dismiss="modal">
                    Cancel
                    </button>
		<input class="btn btn-primary" type="submit" id="submit4" value="submit" >
                </div>
	       </form>
               </div><!-- /.modal-content -->
              </div><!-- /.modal-dialog -->
             </div><!-- /.modal -->
</section>
</div>
</div>

<script>
     <#assign prefUserLogin = (delegator.findOne("UserLogin", {"userLoginId" : userLogin.userLoginId }, true))!>
//        $("body").addClass("${prefUserLogin.lastLocale}");  
    </script>

<#if userLogin?has_content>

<div id="shortcut" style="display: none;">
    <div id='wrapper'>		
        <input class="form-control" style="color: #FFF;background: rgba(255,255,255,.2);border-color: rgba(255,255,255,.4);" id='email' placeholder='Email' type='text'>
        <input class="form-control" style="color: #FFF;background: rgba(255,255,255,.2);border-color: rgba(255,255,255,.4);" id='pass' placeholder='Password' type='password'>
        <input class="form-control" style="color: #FFF;background: rgba(255,255,255,.2);border-color: rgba(255,255,255,.4);" id='repass' placeholder='Repeat password' type='password'>
        </div>
    <button style="margin-top:5px;width:220px" class="btn btn-primary" type='submit'><span>submit</span></button>
    </div>
</#if>

<script>
var nodee = document.querySelectorAll("div[id='wid-id-']");
for (var i=0; i<nodee.length; i++){
     nodee[i].id="wid-id-"+i;    
}
    </script>


                <!-- IMPORTANT: APP CONFIG -->
<script src="/images/BusinessInnovation/js/app.config.js"></script>

                <!-- JS TOUCH : include this plugin for mobile drag / drop touch events-->
<script src="/images/BusinessInnovation/js/plugin/jquery-touch/jquery.ui.touch-punch.min.js"></script> 

                <!-- BOOTSTRAP JS -->
<script src="/images/BusinessInnovation/js/bootstrap/bootstrap.min.js"></script>

                <!-- CUSTOM NOTIFICATION -->
<script src="/images/BusinessInnovation/js/notification/SmartNotification.min.js"></script>

                <!-- JARVIS WIDGETS -->
<script src="/images/BusinessInnovation/js/smartwidgets/jarvis.widget.min.js"></script>

                <!-- EASY PIE CHARTS -->
<script src="/images/BusinessInnovation/js/plugin/easy-pie-chart/jquery.easy-pie-chart.min.js"></script>

                <!-- SPARKLINES -->
<script src="/images/BusinessInnovation/js/plugin/sparkline/jquery.sparkline.min.js"></script>

                <!-- JQUERY VALIDATE -->
<script src="/images/BusinessInnovation/js/plugin/jquery-validate/jquery.validate.min.js"></script>

                <!-- JQUERY MASKED INPUT -->
<script src="/images/BusinessInnovation/js/plugin/masked-input/jquery.maskedinput.min.js"></script>

                <!-- JQUERY SELECT2 INPUT -->
<script src="/images/BusinessInnovation/js/plugin/select2/select2.min.js"></script>

                <!-- JQUERY UI + Bootstrap Slider -->


                <!-- browser msie issue fix -->
<script src="/images/BusinessInnovation/js/plugin/msie-fix/jquery.mb.browser.min.js"></script>

                <!-- FastClick: For mobile devices -->
<script src="/images/BusinessInnovation/js/plugin/fastclick/fastclick.min.js"></script>

<script src="/images/BusinessInnovation/js/demo.min.js"></script>
<script src="/images/BusinessInnovation/js/app.min.js"></script>

                <!-- ENHANCEMENT PLUGINS : NOT A REQUIREMENT -->
                <!-- Voice command : plugin -->
<script src="/images/BusinessInnovation/js/speech/voicecommand.min.js"></script>

                <!-- SmartChat UI : plugin -->
<script src="/images/BusinessInnovation/js/smart-chat-ui/smart.chat.ui.min.js"></script>
<script src="/images/BusinessInnovation/js/smart-chat-ui/smart.chat.manager.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/morris/raphael.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/morris/morris.min.js"></script>
<script src="/images/jgridTable/jgridGlobal.js"></script>
<script src="/images/BusinessInnovation/js/plugin/jqgrid/grid.locale-en.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/jqgrid/jquery.jqGrid.min.js"></script>
<script src="/images/BusinessInnovation/for-popup/sweetalert2.all.min.js"></script>

<script src="/images/BusinessInnovation/js/plugin/datatables/jquery.dataTables.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/datatables/dataTables.colVis.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/datatables/dataTables.tableTools.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/datatables/dataTables.bootstrap.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/datatable-responsive/datatables.responsive.min.js"></script>

<script type="text/javascript">

    $("input[title='Format: yyyy-MM-dd']").datepicker({
          dateFormat: 'yy-mm-dd',
             changeMonth: true,
             changeYear: true,
                         yearRange: "-120:+50",
            buttonImageOnly: true
});


                           var parid;
                            $("input[value='Find']").click(function() {
                             
                             parid = $("input[value='Find']").parents("div[role='widget']").attr('id');                 
                             $("#" + parid +" > div[role='content']").hide();
                            $("#" + parid +"> header > div > a[data-original-title='Collapse'] > i").removeClass('fa fa-minus').addClass('fa fa-plus');
                            $("#" + parid).removeClass('jarviswidget jarviswidget-color-darken jarviswidget-sortable').addClass('jarviswidget jarviswidget-color-darken jarviswidget-sortable jarviswidget-collapsed');
                             localStorage.setItem('hide', 'true');
                             localStorage.setItem('parid', parid);
                          
                            });

<#--			    $("input[value='Search111']").click(function() { 
                             $("#wid-id-0 > div[role='content']").hide();
                            $("#wid-id-0 > header > div > a[data-original-title='Collapse'] > i").removeClass('fa fa-minus').addClass('fa fa-plus');
                            $("#wid-id-0").removeClass('jarviswidget jarviswidget-color-darken jarviswidget-sortable').addClass('jarviswidget jarviswidget-color-darken jarviswidget-sortable jarviswidget-collapsed');
                             localStorage.setItem('hide', 'true');

                            });-->

                          $(document).ready(function() {
                           // Set cache = false for all jquery ajax requests.
			    $.ajaxSetup({
				cache: false,
			    });

                                var hide = localStorage.getItem('hide');
                                var parid1 = localStorage.getItem('parid');                             
                                if(hide === 'true'){
                                    $("#" + parid1+" > div[role='content']").hide();
                                    $("#" + parid1+" > header > div >a[data-original-title='Collapse'] > i").removeClass('fa fa-minus').addClass('fa fa-plus');
                                    $("#" + parid1).removeClass('jarviswidget jarviswidget-color-darken jarviswidget-sortable').addClass('jarviswidget jarviswidget-color-darken jarviswidget-sortable jarviswidget-collapsed');

                                   }
                                    if(window.location.replace){
                                    localStorage.setItem('hide', 'false');
                                    }


                                /* DO NOT REMOVE : GLOBAL FUNCTIONS!
                                 *
                                 * pageSetUp(); WILL CALL THE FOLLOWING FUNCTIONS
                                 *
                                 * // activate tooltips
                                 * $("[rel=tooltip]").tooltip();
                                 *
                                 * // activate popovers
                                 * $("[rel=popover]").popover();
                                 *
                                 * // activate popovers with hover states
                                 * $("[rel=popover-hover]").popover({ trigger: "hover" });
                                 *
                                 * // activate inline charts
                                 * runAllCharts();
                                 *
                                 * // setup widgets
                                 * setup_widgets_desktop();
                                 *
                                 * // run form elements
                                 * runAllForms();
                                 *
                                 ********************************
                                 *
                                 * pageSetUp() is needed whenever you load a page.
                                 * It initializes and checks for all basic elements of the page
                                 * and makes rendering easier.
                                 *
                                 */
				
                                 pageSetUp();
				 
                                /*
                                 * ALL PAGE RELATED SCRIPTS CAN GO BELOW HERE
                                 * eg alert("my home function");
                                 * 
                                 * var pagefunction = function() {
                                 *   ...
                                 * }
                                 * loadScript("/images/BusinessInnovation/js/plugin/_PLUGIN_NAME_.js", pagefunction);
                                 * 
                                 * TO LOAD A SCRIPT:
                                 * var pagefunction = function (){ 
                                 *  loadScript(".../plugin.js", run_after_loaded);	
                                 * }
                                 * 
                                 * OR
                                 * 
                                 * loadScript(".../plugin.js", run_after_loaded);
                                 */


				
                        });
    </script>

<script>
                        $(document).ready(function() {

                                // DO NOT REMOVE : GLOBAL FUNCTIONS!
                                pageSetUp();

                                /*
                                 * PAGE RELATED SCRIPTS
                                 */

                                $(".js-status-update a").click(function() {
                                        var selText = $(this).text();
                                        var $this = $(this);
                                        $this.parents('.btn-group').find('.dropdown-toggle').html(selText + ' <span class="caret"></span>');
                                        $this.parents('.dropdown-menu').find('li').removeClass('active');
                                        $this.parent().addClass('active');
                                });

                                /*
                                * TODO: add a way to add more todo's to list
                                */

                                // initialize sortable
                        <#---	$(function() {
                                        $("#sortable1, #sortable2").sortable({
                                                handle : '.handle',
                                                connectWith : ".todo",
                                                update : countTasks
                                        }).disableSelection();
                                });-->

                                // check and uncheck
                                $('.todo .checkbox > input[type="checkbox"]').click(function() {
                                        var $this = $(this).parent().parent().parent();

                                        if ($(this).prop('checked')) {
                                                $this.addClass("complete");

                                                // remove this if you want to undo a check list once checked
                                                //$(this).attr("disabled", true);
                                                $(this).parent().hide();

                                                // once clicked - add class, copy to memory then remove and add to sortable3
                                                $this.slideUp(500, function() {
                                                        $this.clone().prependTo("#sortable3").effect("highlight", {}, 800);
                                                        $this.remove();
                                                        countTasks();
                                                });
                                        } else {
                                                // insert undo code here...
                                        }

                                })
                                // count tasks
                                function countTasks() {

                                        $('.todo-group-title').each(function() {
                                                var $this = $(this);
                                                $this.find(".num-of-tasks").text($this.next().find("li").size());
                                        });

                                }

                                /*
                                * RUN PAGE GRAPHS
                                */

                                /* TAB 1: UPDATING CHART */
                                // For the demo we use generated data, but normally it would be coming from the server

                                var data = [], totalPoints = 200, $UpdatingChartColors = $("#updating-chart").css('color');

                                function getRandomData() {
                                        if (data.length > 0)
                                                data = data.slice(1);

                                        // do a random walk
                                        while (data.length < totalPoints) {
                                                var prev = data.length > 0 ? data[data.length - 1] : 50;
                                                var y = prev + Math.random() * 10 - 5;
                                                if (y < 0)
                                                        y = 0;
                                                if (y > 100)
                                                        y = 100;
                                                data.push(y);
                                        }

                                        // zip the generated y values with the x values
                                        var res = [];
                                        for (var i = 0; i < data.length; ++i)
                                                res.push([i, data[i]])
                                        return res;
                                }

                                // setup control widget
                                var updateInterval = 1500;
                                $("#updating-chart").val(updateInterval).change(function() {

                                        var v = $(this).val();
                                        if (v && !isNaN(+v)) {
                                                updateInterval = +v;
                                                $(this).val("" + updateInterval);
                                        }

                                });

				
                                /*
                                 * FULL CALENDAR JS
                                 */

                                if ($("#calendar").length) {
                                        var date = new Date();
                                        var d = date.getDate();
                                        var m = date.getMonth();
                                        var y = date.getFullYear();

                                        var calendar = $('#calendar').fullCalendar({

                                                editable : true,
                                                draggable : true,
                                                selectable : false,
                                                selectHelper : true,
                                                unselectAuto : false,
                                                disableResizing : false,
                                                height: "auto",

                                                header : {
                                                        left : 'title', //,today
                                                        center : 'prev, next, today',
                                                        right : 'month, agendaWeek, agenDay' //month, agendaDay,
                                                },

                                                select : function(start, end, allDay) {
                                                        var title = prompt('Event Title:');
                                                        if (title) {
                                                                calendar.fullCalendar('renderEvent', {
                                                                        title : title,
                                                                        start : start,
                                                                        end : end,
                                                                        allDay : allDay
                                                                }, true // make the event "stick"
                                                                );
                                                        }
                                                        calendar.fullCalendar('unselect');
                                                },

                                                events : [{
                                                        title : 'All Day Event',
                                                        start : new Date(y, m, 1),
                                                        description : 'long description',
                                                        className : ["event", "bg-color-greenLight"],
                                                        icon : 'fa-check'
                                                }, {
                                                        title : 'Long Event',
                                                        start : new Date(y, m, d - 5),
                                                        end : new Date(y, m, d - 2),
                                                        className : ["event", "bg-color-red"],
                                                        icon : 'fa-lock'
                                                }, {
                                                        id : 999,
                                                        title : 'Repeating Event',
                                                        start : new Date(y, m, d - 3, 16, 0),
                                                        allDay : false,
                                                        className : ["event", "bg-color-blue"],
                                                        icon : 'fa-clock-o'
                                                }, {
                                                        id : 999,
                                                        title : 'Repeating Event',
                                                        start : new Date(y, m, d + 4, 16, 0),
                                                        allDay : false,
                                                        className : ["event", "bg-color-blue"],
                                                        icon : 'fa-clock-o'
                                                }, {
                                                        title : 'Meeting',
                                                        start : new Date(y, m, d, 10, 30),
                                                        allDay : false,
                                                        className : ["event", "bg-color-darken"]
                                                }, {
                                                        title : 'Lunch',
                                                        start : new Date(y, m, d, 12, 0),
                                                        end : new Date(y, m, d, 14, 0),
                                                        allDay : false,
                                                        className : ["event", "bg-color-darken"]
                                                }, {
                                                        title : 'Birthday Party',
                                                        start : new Date(y, m, d + 1, 19, 0),
                                                        end : new Date(y, m, d + 1, 22, 30),
                                                        allDay : false,
                                                        className : ["event", "bg-color-darken"]
                                                }, {
                                                        title : 'Smartadmin Open Day',
                                                        start : new Date(y, m, 28),
                                                        end : new Date(y, m, 29),
                                                        className : ["event", "bg-color-darken"]
                                                }],


                                                eventRender : function(event, element, icon) {
                                                        if (!event.description == "") {
                                                                element.find('.fc-title').append("<br/><span class='ultra-light'>" + event.description + "</span>");
                                                        }
                                                        if (!event.icon == "") {
                                                                element.find('.fc-title').append("<i class='air air-top-right fa " + event.icon + " '></i>");
                                                        }
                                                }
                                        });

                                };

                                /* hide default buttons */
                                $('.fc-toolbar .fc-right, .fc-toolbar .fc-center').hide();

                                // calendar prev
                                $('#calendar-buttons #btn-prev').click(function() {
                                        $('.fc-prev-button').click();
                                        return false;
                                });

                                // calendar next
                                $('#calendar-buttons #btn-next').click(function() {
                                        $('.fc-next-button').click();
                                        return false;
                                });

                                // calendar today
                                $('#calendar-buttons #btn-today').click(function() {
                                        $('.fc-button-today').click();
                                        return false;
                                });

                                // calendar month
                                $('#mt').click(function() {
                                        $('#calendar').fullCalendar('changeView', 'month');
                                });

                                // calendar agenda week
                                $('#ag').click(function() {
                                        $('#calendar').fullCalendar('changeView', 'agendaWeek');
                                });

                                // calendar agenda day
                                $('#td').click(function() {
                                        $('#calendar').fullCalendar('changeView', 'agendaDay');
                                });

                                /*
                                 * CHAT
                                 */

                                $.filter_input = $('#filter-chat-list');
                                $.chat_users_container = $('#chat-container > .chat-list-body')
                                $.chat_users = $('#chat-users')
                                $.chat_list_btn = $('#chat-container > .chat-list-open-close');
                                $.chat_body = $('#chat-body');

                                /*
                                * LIST FILTER (CHAT)
                                */

                                // custom css expression for a case-insensitive contains()
                                jQuery.expr[':'].Contains = function(a, i, m) {
                                        return (a.textContent || a.innerText || "").toUpperCase().indexOf(m[3].toUpperCase()) >= 0;
                                };

                                function listFilter(list) {// header is any element, list is an unordered list
                                        // create and add the filter form to the header

                                        $.filter_input.change(function() {
                                                var filter = $(this).val();
                                                if (filter) {
                                                        // this finds all links in a list that contain the input,
                                                        // and hide the ones not containing the input while showing the ones that do
                                                        $.chat_users.find("a:not(:Contains(" + filter + "))").parent().slideUp();
                                                        $.chat_users.find("a:Contains(" + filter + ")").parent().slideDown();
                                                } else {
                                                        $.chat_users.find("li").slideDown();
                                                }
                                                return false;
                                        }).keyup(function() {
                                                // fire the above change event after every letter
                                                $(this).change();

                                        });

                                }

				

                        });

</script>
<script>
                        $(document).ready(function() {

                                // DO NOT REMOVE : GLOBAL FUNCTIONS!
                                pageSetUp();

                                /*
                                 * PAGE RELATED SCRIPTS
                                 */

                                $(".js-status-update a").click(function() {
                                        var selText = $(this).text();
                                        var $this = $(this);
                                        $this.parents('.btn-group').find('.dropdown-toggle').html(selText + ' <span class="caret"></span>');
                                        $this.parents('.dropdown-menu').find('li').removeClass('active');
                                        $this.parent().addClass('active');
                                });

                                /*
                                * TODO: add a way to add more todo's to list
                                */

                                // initialize sortable
                                <#--$(function() {
                                        $("#sortable1, #sortable2").sortable({
                                                handle : '.handle',
                                                connectWith : ".todo",
                                                update : countTasks
                                        }).disableSelection();
                                });0-->
                                // check and uncheck
                                $('.todo .checkbox > input[type="checkbox"]').click(function() {
                                        var $this = $(this).parent().parent().parent();

                                        if ($(this).prop('checked')) {
                                                $this.addClass("complete");

                                                // remove this if you want to undo a check list once checked
                                                //$(this).attr("disabled", true);
                                                $(this).parent().hide();

                                                // once clicked - add class, copy to memory then remove and add to sortable3
                                                $this.slideUp(500, function() {
                                                        $this.clone().prependTo("#sortable3").effect("highlight", {}, 800);
                                                        $this.remove();
                                                        countTasks();
                                                });
                                        } else {
                                                // insert undo code here...
                                        }

                                })
                                // count tasks
                                function countTasks() {

                                        $('.todo-group-title').each(function() {
                                                var $this = $(this);
                                                $this.find(".num-of-tasks").text($this.next().find("li").size());
                                        });

                                }

                                /*
                                * RUN PAGE GRAPHS
                                */

                                /* TAB 1: UPDATING CHART */
                                // For the demo we use generated data, but normally it would be coming from the server

                                var data = [], totalPoints = 200, $UpdatingChartColors = $("#updating-chart").css('color');

                                function getRandomData() {
                                        if (data.length > 0)
                                                data = data.slice(1);

                                        // do a random walk
                                        while (data.length < totalPoints) {
                                                var prev = data.length > 0 ? data[data.length - 1] : 50;
                                                var y = prev + Math.random() * 10 - 5;
                                                if (y < 0)
                                                        y = 0;
                                                if (y > 100)
                                                        y = 100;
                                                data.push(y);
                                        }

                                        // zip the generated y values with the x values
                                        var res = [];
                                        for (var i = 0; i < data.length; ++i)
                                                res.push([i, data[i]])
                                        return res;
                                }

                                // setup control widget
                                var updateInterval = 1500;
                                $("#updating-chart").val(updateInterval).change(function() {

                                        var v = $(this).val();
                                        if (v && !isNaN(+v)) {
                                                updateInterval = +v;
                                                $(this).val("" + updateInterval);
                                        }

                                });

                                // setup plot
                                var options = {
                                        yaxis : {
                                                min : 0,
                                                max : 100
                                        },
                                        xaxis : {
                                                min : 0,
                                                max : 100
                                        },
                                        colors : [$UpdatingChartColors],
                                        series : {
                                                lines : {
                                                        lineWidth : 1,
                                                        fill : true,
                                                        fillColor : {
                                                                colors : [{
                                                                        opacity : 0.4
                                                                }, {
                                                                        opacity : 0
                                                                }]
                                                        },
                                                        steps : false

                                                }
                                        }
                                };

				

                                /*
                                 * FULL CALENDAR JS
                                 */

                                if ($("#calendar").length) {
                                        var date = new Date();
                                        var d = date.getDate();
                                        var m = date.getMonth();
                                        var y = date.getFullYear();

                                        var calendar = $('#calendar').fullCalendar({

                                                editable : true,
                                                draggable : true,
                                                selectable : false,
                                                selectHelper : true,
                                                unselectAuto : false,
                                                disableResizing : false,
                                                height: "auto",

                                                header : {
                                                        left : 'title', //,today
                                                        center : 'prev, next, today',
                                                        right : 'month, agendaWeek, agenDay' //month, agendaDay,
                                                },

                                                select : function(start, end, allDay) {
                                                        var title = prompt('Event Title:');
                                                        if (title) {
                                                                calendar.fullCalendar('renderEvent', {
                                                                        title : title,
                                                                        start : start,
                                                                        end : end,
                                                                        allDay : allDay
                                                                }, true // make the event "stick"
                                                                );
                                                        }
                                                        calendar.fullCalendar('unselect');
                                                },

                                                events : [{
                                                        title : 'All Day Event',
                                                        start : new Date(y, m, 1),
                                                        description : 'long description',
                                                        className : ["event", "bg-color-greenLight"],
                                                        icon : 'fa-check'
                                                }, {
                                                        title : 'Long Event',
                                                        start : new Date(y, m, d - 5),
                                                        end : new Date(y, m, d - 2),
                                                        className : ["event", "bg-color-red"],
                                                        icon : 'fa-lock'
                                                }, {
                                                        id : 999,
                                                        title : 'Repeating Event',
                                                        start : new Date(y, m, d - 3, 16, 0),
                                                        allDay : false,
                                                        className : ["event", "bg-color-blue"],
                                                        icon : 'fa-clock-o'
                                                }, {
                                                        id : 999,
                                                        title : 'Repeating Event',
                                                        start : new Date(y, m, d + 4, 16, 0),
                                                        allDay : false,
                                                        className : ["event", "bg-color-blue"],
                                                        icon : 'fa-clock-o'
                                                }, {
                                                        title : 'Meeting',
                                                        start : new Date(y, m, d, 10, 30),
                                                        allDay : false,
                                                        className : ["event", "bg-color-darken"]
                                                }, {
                                                        title : 'Lunch',
                                                        start : new Date(y, m, d, 12, 0),
                                                        end : new Date(y, m, d, 14, 0),
                                                        allDay : false,
                                                        className : ["event", "bg-color-darken"]
                                                }, {
                                                        title : 'Birthday Party',
                                                        start : new Date(y, m, d + 1, 19, 0),
                                                        end : new Date(y, m, d + 1, 22, 30),
                                                        allDay : false,
                                                        className : ["event", "bg-color-darken"]
                                                }, {
                                                        title : 'Smartadmin Open Day',
                                                        start : new Date(y, m, 28),
                                                        end : new Date(y, m, 29),
                                                        className : ["event", "bg-color-darken"]
                                                }],


                                                eventRender : function(event, element, icon) {
                                                        if (!event.description == "") {
                                                                element.find('.fc-title').append("<br/><span class='ultra-light'>" + event.description + "</span>");
                                                        }
                                                        if (!event.icon == "") {
                                                                element.find('.fc-title').append("<i class='air air-top-right fa " + event.icon + " '></i>");
                                                        }
                                                }
                                        });

                                };

                                /* hide default buttons */
                                $('.fc-toolbar .fc-right, .fc-toolbar .fc-center').hide();

                                // calendar prev
                                $('#calendar-buttons #btn-prev').click(function() {
                                        $('.fc-prev-button').click();
                                        return false;
                                });

                                // calendar next
                                $('#calendar-buttons #btn-next').click(function() {
                                        $('.fc-next-button').click();
                                        return false;
                                });

                                // calendar today
                                $('#calendar-buttons #btn-today').click(function() {
                                        $('.fc-button-today').click();
                                        return false;
                                });

                                // calendar month
                                $('#mt').click(function() {
                                        $('#calendar').fullCalendar('changeView', 'month');
                                });

                                // calendar agenda week
                                $('#ag').click(function() {
                                        $('#calendar').fullCalendar('changeView', 'agendaWeek');
                                });

                                // calendar agenda day
                                $('#td').click(function() {
                                        $('#calendar').fullCalendar('changeView', 'agendaDay');
                                });

                                /*
                                 * CHAT
                                 */

                                $.filter_input = $('#filter-chat-list');
                                $.chat_users_container = $('#chat-container > .chat-list-body')
                                $.chat_users = $('#chat-users')
                                $.chat_list_btn = $('#chat-container > .chat-list-open-close');
                                $.chat_body = $('#chat-body');

                                /*
                                * LIST FILTER (CHAT)
                                */

                                // custom css expression for a case-insensitive contains()
                                jQuery.expr[':'].Contains = function(a, i, m) {
                                        return (a.textContent || a.innerText || "").toUpperCase().indexOf(m[3].toUpperCase()) >= 0;
                                };

                                function listFilter(list) {// header is any element, list is an unordered list
                                        // create and add the filter form to the header

                                        $.filter_input.change(function() {
                                                var filter = $(this).val();
                                                if (filter) {
                                                        // this finds all links in a list that contain the input,
                                                        // and hide the ones not containing the input while showing the ones that do
                                                        $.chat_users.find("a:not(:Contains(" + filter + "))").parent().slideUp();
                                                        $.chat_users.find("a:Contains(" + filter + ")").parent().slideDown();
                                                } else {
                                                        $.chat_users.find("li").slideDown();
                                                }
                                                return false;
                                        }).keyup(function() {
                                                // fire the above change event after every letter
                                                $(this).change();

                                        });

                                }

                        });

    </script>


<script type="text/javascript">



$('.tree-toggle').click(function () {	$(this).parent().children('ul.tree').toggle(200);
});
$(function(){
$('.tree-toggle').parent().children('ul.tree').toggle(200);
})

    </script>




<script>
var con = document.getElementById("h2contenttext").innerHTML;
document.getElementById("newribbontext").innerHTML = con;

    </script>

<script>


function changemyproimage(){
                                $.SmartMessageBox({
                                        title : '<i class="fa fa-sign-out txt-color-orangeDark"></i> New Logo',
                                        content : "Uploading Logo Image, Recomended Dimensions 50X50",
                                        color : "#ff0000",
                                        buttons : '[Cancel][Browse]'
                                }, function(ButtonPressed) {
                                        if (ButtonPressed === "Browse") 
                                        {
                                            
                                          
                                          function readFile(mylogoo) 
                                          {
                                              var filesSelected = document.getElementById("logoo").files;
                                              if (filesSelected.length > 0)
                                                {
                                                    var fileToLoad = filesSelected[0];

                                                    var fileReader = new FileReader();

                                                    fileReader.onload = function(fileLoadedEvent) 
                                                    {
                                                        var onlydataimage = fileLoadedEvent.target.result.split(',')[1];
                                                            
                                                        
                                                        $.ajax({
                                                        url: "setUserPreferenceImage",
                                                        type: "post",
                                                        dataType: 'json',
                                                        data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'IMAGE_LOGO' , userPrefValue: onlydataimage},
                                                         success: 
                                                                     $.SmartMessageBox({
                                                                        title : '<i class="fa fa-sign-out txt-color-orangeDark"></i> New Logo',
                                                                        content : "Logo image changed successfuly",
                                                                        color : "#0000ff",
                                                                        buttons : '[Reload]'
                                                                }, function(ButtonPressed) {
                                                                        if (ButtonPressed === "Reload") 
                                                                        {
                                                                                window.location.reload();                                                                           
                                                                        }
                                                                })                                                          
                                                        })
                                                    }

                                                    fileReader.readAsDataURL(fileToLoad);
                                                }
                                          }
                                          $('#logoo').trigger('click');
                                              $("#logoo").change(function(){
                                              var mylogoo = $("#logoo")[0].files;
                                              readFile(mylogoo);
                                                });             
                                        }
                                        if (ButtonPressed === "Cancel") {
						
                                        }		
                                });
                                }
</script>
<input type="file" id="logoo" style="display:none" />
<script>
var nodes = document.querySelectorAll("input[type=text],input[type=password]");
for (var i=0; i<nodes.length; i++){
   if(nodes[i].className==="required")
     nodes[i].className="required form-control";
   else
     nodes[i].className="form-control";
     
}

var nodes1 = document.querySelectorAll("select");
for (var i=0; i<nodes1.length; i++){
   if(nodes1[i].className==="required")
     nodes1[i].className="required form-control";
   else
     nodes1[i].className="form-control";
     
}
    </script>

<script>
var fillin= $('#tab111 tr').length;
 document.getElementById("numofrow").innerHTML= fillin;
    </script>

<script>
$("aside nav  ul  li  a").click(function() { 
var thisliid = $(this).parent().attr('id');
localStorage.setItem('thisliid', thisliid);
});

var liidme = localStorage.getItem('thisliid');
document.getElementById(liidme).className = "active open";


    </script>

<script>
        $("#minhide").click(function() {
            if ($("body").hasClass("minified")){document.getElementById("showfirst").removeAttribute("style");}
                else{document.getElementById("showfirst").style.display="none";}
          });
    
 </script>
<script>
    $('#smart-fixed-header').click(function(){
        if(document.getElementById('smart-fixed-header').checked)
        {
             $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'smart-fixed-header' , userPrefValue: '1'},
         dataType: 'json',
            });
          
        }
        else
        {
            $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'smart-fixed-header' , userPrefValue: '0'},
         dataType: 'json',
            });
        }
    });
    $('#smart-fixed-navigation').click(function(){
        if(document.getElementById('smart-fixed-navigation').checked)
        {
             $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'smart-fixed-navigation' , userPrefValue: '1'},
         dataType: 'json',
            });
        }
        else
        {
            $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'smart-fixed-navigation' , userPrefValue: '0'},
         dataType: 'json',
            });
        }
    });
    $('#smart-fixed-ribbon').click(function(){
        if(document.getElementById('smart-fixed-ribbon').checked)
        {
             $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'smart-fixed-ribbon' , userPrefValue: '1'},
         dataType: 'json',
            });

            $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'smart-fixed-navigation' , userPrefValue: '1'},
         dataType: 'json',
            });

            $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'smart-fixed-header' , userPrefValue: '1'},
         dataType: 'json',
            });
        }
        else
        {
            $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'smart-fixed-ribbon' , userPrefValue: '0'},
         dataType: 'json',
            });
        }
    });
    $('#smart-fixed-container').click(function(){
        if(document.getElementById('smart-fixed-container').checked)
        {
             $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'smart-fixed-container' , userPrefValue: '1'},
         dataType: 'json',
            });
        }
        else
        {
            $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'smart-fixed-container' , userPrefValue: '0'},
         dataType: 'json',
            });
        }
    });
    
    $('#smart-topmenu').click(function(){
        if(document.getElementById('smart-topmenu').checked)
        {
         $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'smart-topmenu' , userPrefValue: '1'},
         dataType: 'json',
            });
                
        }
        else
        {
            $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'smart-topmenu' , userPrefValue: '0'},
         dataType: 'json',
            });
        }
    });

    $('#colorblind-friendly').click(function(){
        if(document.getElementById('colorblind-friendly').checked)
        {
             $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'colorblind-friendly' , userPrefValue: '1'},
         dataType: 'json',
            });
        }
        else
        {
            $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'colorblind-friendly' , userPrefValue: '0'},
         dataType: 'json',
            });
        }
    });
        $('#toggleMenubd').click(function(){
          var toggleMenubd=$("body").attr("class");
          var pos58 = toggleMenubd.indexOf("hidden-menu");
           if(pos58 > 0){
                 $.ajax({
             type: "post",
             url: "setUserPreference",
             data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'hidden-menu' , userPrefValue: '0'},
             dataType: 'json',
                });
           }
           else{
                 $.ajax({
             type: "post",
             url: "setUserPreference",
             data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'hidden-menu' , userPrefValue: '1'},
             dataType: 'json',
                });
           }

        });

    $('#smart-style-0').click(function(){
             $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'skin' , userPrefValue: 'smart-style-0'},
         dataType: 'json',
            });
       
    });
    $('#smart-style-1').click(function(){
             $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'skin' , userPrefValue: 'smart-style-1'},
         dataType: 'json',
            });
       
    });
    $('#smart-style-2').click(function(){
             $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'skin' , userPrefValue: 'smart-style-2'},
         dataType: 'json',
            });
       
    });
    $('#smart-style-3').click(function(){
             $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'skin' , userPrefValue: 'smart-style-3'},
         dataType: 'json',
            });
       
    });
    $('#smart-style-4').click(function(){
             $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'skin' , userPrefValue: 'smart-style-4'},
         dataType: 'json',
            });
       
    });
    $('#smart-style-5').click(function(){
             $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'skin' , userPrefValue: 'smart-style-5'},
         dataType: 'json',
            });
                    $("#gview_treegrid").css("background", "none");
                        $("#gbox_treegrid").css("background", "none");
                            $(".ui-jqgrid").css("background", "none");
                                $(".ui-widget-content").addClass("changingtreebg");
       
    });
    $('#smart-style-6').click(function(){
             $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'skin' , userPrefValue: 'smart-style-6'},
         dataType: 'json',
            });
    });
    $('#smart-style-7').click(function(){
             $.ajax({
         type: "post",
         url: "setUserPreference",
         data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId:'skin' , userPrefValue: 'smart-style-7'},
         dataType: 'json',
            });
    });



    $('ul.ColVis_collection li label input[type="checkbox"]').live('click', function() {
    var hidcol=$(this).attr("id");

    if(document.getElementById(hidcol).checked)
    {

                 $.ajax({
             type: "post",
             url: "setUserPreference",
             data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId: hidcol , userPrefValue: '1'},
             dataType: 'json',
                });
    }
else{
                 $.ajax({
             type: "post",
             url: "setUserPreference",
             data: { userPrefGroupTypeId: 'GLOBAL_PREFERENCES',userPrefTypeId: hidcol , userPrefValue: '0'},
             dataType: 'json',
                });
    }

    });

/*
var settings = {
                          "async": true,
                          "crossDomain": true,
                          "url": "getUserPrf",
                          "method": "post",
                          data: { userLoginId:'admin'},
dataType: 'json'
                          }
			
                        $.ajax(settings).done(function (response) {
			  
                             
                        }).fail(function (response) {
			  
                        });*/

    </script>

<script>
                        var str = document.body.className;
                        var pos = str.indexOf("menu-on-top");

                        if(pos > 0){
                        if ($('#left-panel  nav  ul > li').length > 9) { 
                        document.getElementById("more").style.display="block";
                        var con = document.getElementById("conn12").innerHTML;
                        document.getElementById("moreInfo").innerHTML = con;
                        document.getElementById("conn12").style.display="none";
                        document.getElementById("par1").style.display="none";
                        document.getElementById("pro1").style.display="none";
                        document.getElementById("sfa1").style.display="none";
                        document.getElementById("scr1").style.display="none";
                        document.getElementById("wor1").style.display="none";
                        document.getElementById("bir1").style.display="none";
                        document.getElementById("bi1").style.display="none";
                        document.getElementById("web1").style.display="none";
                        document.getElementById("ord1").style.display="none";
                        document.getElementById("mar1").style.display="none";
                        document.getElementById("payr1").style.display="none";
                        document.getElementById("port1").style.display="none";
                        }
                        }
    </script>




<script>
var str1 = document.body.className;
var pos0 = str1.indexOf("fixed-header");
var pos1 = str1.indexOf("fixed-navigation");
var pos2 = str1.indexOf("fixed-ribbon");
var pos3 = str1.indexOf("container");
var pos4 = str1.indexOf("menu-on-top");
var pos5 = str1.indexOf("style-0");
var pos6 = str1.indexOf("style-1");
var pos7 = str1.indexOf("style-2");
var pos8 = str1.indexOf("style-3");
var pos9 = str1.indexOf("style-4");
var pos10 = str1.indexOf("style-5");
var pos11 = str1.indexOf("style-6");
var pos12 = str1.indexOf("style-7");

if(pos0 > 0){
document.getElementById("smart-fixed-header").checked = true;
}
if(pos1 > 0){
document.getElementById("smart-fixed-navigation").checked = true;
}
if(pos2 > 0){
document.getElementById("smart-fixed-ribbon").checked = true;
}
if(pos3 > 0){
document.getElementById("smart-fixed-container").checked = true;
}
if(pos4 > 0){
document.getElementById("smart-topmenu").checked = true;
}
if(pos5 > 0){

$("#smart-style-0").prepend("<i class='fa fa-check fa-fw' id='skin-checked'></i>")
}
if(pos6 > 0){
$("#smart-style-1").prepend("<i class='fa fa-check fa-fw' id='skin-checked'></i>")
}
if(pos7 > 0){
$("#smart-style-2").prepend("<i class='fa fa-check fa-fw' id='skin-checked'></i>")
}
if(pos8 > 0){
$("#smart-style-3").prepend("<i class='fa fa-check fa-fw' id='skin-checked'></i>")
}
if(pos9 > 0){
$("#smart-style-4").prepend("<i class='fa fa-check fa-fw' id='skin-checked'></i>")
}
if(pos10 > 0){
$("#smart-style-5").prepend("<i class='fa fa-check fa-fw' id='skin-checked'></i>")
}
if(pos11 > 0){
$("#smart-style-6").prepend("<i class='fa fa-check fa-fw' id='skin-checked'></i>")
}
if(pos12 > 0){
$("#smart-style-7").prepend("<i class='fa fa-check fa-fw' id='skin-checked'></i>")
}

    </script>


<script>
jQuery(document).ready(function($){
        //open popup
        $('.cd-popup-trigger').on('click', function(event){
                event.preventDefault();
                $('.cd-popup').addClass('is-visible');
        });
	
        //close popup
        $('.cd-popup').on('click', function(event){
                if( $(event.target).is('.cd-popup-close') || $(event.target).is('.cd-popup') ) {
                        event.preventDefault();
                        $(this).removeClass('is-visible');
                }
        });
        //close popup when clicking the esc keyboard button
        $(document).keyup(function(event){
        if(event.which=='27'){
                $('.cd-popup').removeClass('is-visible');
            }
    });
                $('.not-vis').on('click', function(){

                $('.cd-popup').removeClass('is-visible');
                });
});
    </script>
<script>
    // if function exists
    if (typeof invoiceOverview == "function") { 
       invoiceOverview();
    }
     $('[name="yearMenu"]').val(sessionStorage.getItem("yearInvoice"));
    </script>

<script src="/images/BusinessInnovation/js/plugin/moment/moment.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/chartjs/chart.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/highChartCore/highcharts-custom.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/highchartTable/jquery.highchartTable.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.cust.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.resize.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.fillbetween.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.orderBar.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.pie.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.time.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/flot/jquery.flot.tooltip.min.js"></script>	

