<#ftl encoding="utf-8">
   <!-- row -->
<link rel="stylesheet" href="/images/BusinessInnovation/css/searchBar.css">
<div class="row">
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
      <div class="search_bar" style="display: -webkit-box">
        <form method="post" name="LookupEmployeeName" action="">
            <table id="table1" >
                <tr>
                    <td align="right" required="true" ><div class="search_dropdown" style="width:62px !important"><span>Employee ID </span><span class="required">*</span></div></td>
                    <td id="mmomes">
                           <@htmlTemplate.lookupField value='${requestParameters.partyId!}' formName="LookupEmployeeName" name="partyId" id="partyId" fieldFormName="LookupEmployeeName"/>
                        </td>
                    </tr>
                </table> 
            </form>
	</div>
        <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
            <div role="content">
                <table id="leaveGrid"></table>
                <div id="leaveGridDiv"></div>
                </div>
            </div>
        </article>
        <!-- WIDGET END -->
    </div>
<script>
<#include "component://humanres/widget/forms/leave-and-vacation/leaves/leaves.js"/>
</script>


