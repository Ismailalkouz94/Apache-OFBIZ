<#ftl encoding="utf-8">
<!-- row -->
<link rel="stylesheet" href="/images/BusinessInnovation/css/searchBar.css">
<div class="row">
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
    <div class="search_bar vView">
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
        <div class="div-button">
            <#if parameters.portalPageId?hasContent>
                <li>
                    <a class="button1 facebook" href="/myportal/control/main?portalPageId=${parameters.portalPageId}&parentPortalPageId=${parameters.parentPortalPageId}&&type=CV">Create Vacation</a>
                </li>
            <#else>
                <li>
                    <a class="button1 facebook" href="/humanres/control/EmplVacation">Create Vacation</a>
                </li>
            </#if>
        </div>
        <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
            <div role="content">
                <table id="vacationGrid"></table>
                <div id="vacationGridDiv"></div>
                </div>
            </div>


        <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
            <div role="content">
                <table id="vacationExtGrid"></table>
                <div id="vacationExtGridDiv"></div>
                </div>
            </div>

        <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
            <div role="content">
                <table id="vacationExtFMGrid"></table>
                <div id="vacationExtFMGridDiv"></div>
                </div>
            </div>

        </article>
        <!-- WIDGET END -->
    </div>
<script>
<#include "component://humanres/widget/forms/leave-and-vacation/vacations/vacationView/vacationsView.js"/>
</script>



