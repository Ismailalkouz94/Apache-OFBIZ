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
        <div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
            <header>
                <h2>Party Vacation Transaction</h2>
            </header>
            <div role="content">

                <table id="vacationTransaction"></table>
                <div id="vacationTransactionDiv"></div>
            </div>
        </div>
    </article>
</div>

<script>
    <#include "component://humanres/widget/forms/leave-and-vacation/partyvacationtransaction/PartyVacationTransaction.js"/>
</script>
