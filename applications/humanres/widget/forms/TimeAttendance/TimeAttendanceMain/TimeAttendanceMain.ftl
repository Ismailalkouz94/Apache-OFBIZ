
<div class="row">
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">
            <header>
                <h2> Time Attendance Attach File</h2>
                </header>
            <div role="content">
                <div id="partyContent" class="screenlet">
                    <div class="screenlet-body">
                        <form id="uploadPartyContent" method="post" enctype="multipart/form-data" action="<@ofbizUrl>ImportTimeATTFile</@ofbizUrl>">
                            <table class="basic-table">
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
                                <tr>
                                    <td><label>${uiLabelMap.PartyAttachContent}</label></td>
                                    <td><input class="btn btn-default" style="width:220px" type="file" id="uploadedFile" name="uploadedFile" class="required error" size="25"/></td>
                                    </tr>
                                <tr>
                                    <td colspan="4"><input class="btn btn-primary1" type="submit" value="${uiLabelMap.CommonUpload}" /></td>    
                                    </tr>

                                </table>
                            <div id='progress_bar'  class="ui-progressbar ui-widget ui-widget-content ui-corner-all">
                                <div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </article>
    </div>

<div class="row">

    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
            <div role="content">
                <form class="smart-form">
                    <table class="basic-table">
                        <tr>      
                            <td>
                                <label>Year </label>
                                </td>
                            <td width="24%">
                                <select id="yearView" class="form-control"></select>
                                </td>
                            <td>
                                <label>Month </label>
                                </td>
                            <td  width="24%">
                                <select id="monthView" class="form-control">
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

                            <td style="vertical-align: inherit;">
                                <button class="btn btn-primary1"  type="button" id="submit_button" onclick="search()">Search</button>
                                </td>
                            </tr>
                        </table>
                    </form>

                <table id="TimeAttendance_jqgrid"></table>
                <div id="TimeAttendance_jqgridDiv"></div>
                </div>
            </div>
        </article>
    </div>

<script type="text/javascript">
        <#include "component://humanres/widget/forms/TimeAttendance/TimeAttendanceMain/TimeAttendanceMain.js"/>

  jQuery("#uploadPartyContent").validate({
      submitHandler: function(form) {
          <#-- call upload scripts - functions defined in PartyProfileContent.js -->
          uploadPartyContent();
          getUploadProgressStatus();
          form.submit();
      }
  });
    </script>