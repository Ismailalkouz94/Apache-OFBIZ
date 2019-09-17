<#ftl encoding="utf-8">
<div class="row">
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
            <header>
              <h2>Time Attendance Config</h2>
            </header>
            <div role="content">
                <table class="basic-table">
                  <tr>
                    <td width="5%">
                        <label>Year</label>
                    </td>
                    <td>
                        <select id="yearView" ></select>
                    </td>
                  </tr>
                </table>
                <table id="jqgrid"></table>
                <div id="jqgridDiv"></div>
                </div>
            </div>
        </article>
    </div>
<script>
    <#include "component://humanres/widget/forms/TimeAttendance/TimeAttendanceConifg/TimeAttendanceConifg.js"/>
</script>
