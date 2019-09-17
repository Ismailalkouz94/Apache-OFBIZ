<div class="row">
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">
            <header>
                <h2>Employee Time Attendance</h2>
                </header>
            <div role="content">
                <form method="post" name="TimeAttendanceSummary" action=""  class="smart-form">
                    <table id="table1" class="basic-table">              
                        <tr>
                            <td required="true">                 
                                <label>Department</label>
                                </td>
                            <td colspan="3"  id="Dlook" >
			    <@htmlTemplate.lookupField value='${requestParameters.partyId!}' formName="TimeAttendanceSummary" name="LookupInternalOrganization" id="LookupInternalOrganization" fieldFormName="LookupInternalOrganization"/>
				 <span class="tooltip1">required</span>
                                </td>
                            </tr>
                        <tr>
                            <td width="10%">
                                <label>Year </label>
                                </td>
                            <td width="23%">
                                <select id="year" name="year" class="form-control"></select>
                                </td>
                            <td width="5%">
                                <label>Month </label>
                                </td>
                            <td>
                                <select id="month" name="month" class="form-control">
                                    <option value="January">January</option>
                                    <option value="February">February</option>
                                    <option value="March">March</option>
                                    <option value="April">April</option>
                                    <option value="May">May</option>
                                    <option value="June">June</option>
                                    <option value="July">July</option>
                                    <option value="August">August</option>
                                    <option value="September">September</option>
                                    <option value="October">October</option>
                                    <option value="November">November</option>
                                    <option value="December">December</option>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    <button class="btn btn-primary1"  type="button" id="submit_button" onclick="search()">Search</button>
                    </form>
                </div>
            </div>
        </article>
    </div>
<div class="row">
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
            <div role="content">
                <div>
                    <button class="ContainExel" type="button" id="save_button" onclick="exportToExcel()"><img class="excelIcon" src="/images/BusinessInnovation/images/excel.png"/></button>
					<button class="ContainExel"  type="button" id="exportToPDF" onclick="exportToPDF()"><img class="excelIcon" src="/images/BusinessInnovation/images/pdf2.png" alt="Export To PDF"></button>
                    <!--<button class="ContainExel"  type="button" id="" onclick=""><img class="excelIcon" src="/images/BusinessInnovation/images/iconsmart/icon 3d-04.png" alt="Export To PDF"></button>-->
                    </div>
                <table id="TimeAttendance_jqgrid"></table>
                <div id="TimeAttendance_jqgridDiv"></div>
                <div class="warning cancel">
                    <#--<button class="btn btn-primary1"  type="button" id="save_button" onclick="">Save</button>-->
                    </div>
                </div>
            </div>
        </article>
    </div>
<script>        
    <#include "component://humanres/widget/forms/TimeAttendance/TimeAttendanceSummary/TimeAttendanceSummary.js"/>
    </script>
