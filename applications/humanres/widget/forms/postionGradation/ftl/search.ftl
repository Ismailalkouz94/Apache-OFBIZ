<link rel="stylesheet" href="/images/BusinessInnovation/css/searchBar.css">
<div class="row">
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
      <div class="search_bar" style="display: -webkit-box">
       <form method="post"  id="myform" name="LookupEmployeeName" action="">
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
    </article>
</div>
<#include "component://humanres/widget/forms/postionGradation/ftl/MainGrid.ftl"/>
<script>
   <#include "component://humanres/widget/forms/postionGradation/js/search.js"/>
</script>