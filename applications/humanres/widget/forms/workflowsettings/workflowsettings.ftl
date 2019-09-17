 <#assign approverWorkflow = delegator.findAll("WfSysApprover",true) />      

<div class="row">
    <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-1" data-widget-editbutton="false">
            <header>
                <h2>Employee Settings</h2>
                </header>
            <div role="content">
                <form method="post" class="smart-form" name="LookupEmplPositionOCCUPIED" class="search_bar">
                    <table class="basic-table">
                              <tr>             
                            <td>
                                <label style="vertical-align: sub;">Approver WorkFlow </label>
                                </td>
                            <td>			    
                                <label class="input" > 
                                   <select id="approverWorkFlow">
                                       <#list approverWorkflow as row >
                                       <option id="${(row.wfSysApproverId)!}" value="${(row.wfSysApproverId)!}">${(row.description)!}</option>
                                       </#list>
                                       
                                       </select>
                                    </label>	
                                </td>

                            </tr>
                    
                        <tr>
                            <td width="10%"><label>Position ID</label></td>
                            <td colspan="3" width="23%">
                                <@htmlTemplate.lookupField value="${requestParameters.emplPositionId!}" formName="LookupEmplPositionOCCUPIED" name="emplPositionId" id="empPositionId"  fieldFormName="LookupEmplPositionOCCUPIED"/>
                                <span class="required">*</span>
                                </td>       
                            </tr> 
                  
                        </table>

                    </form> 
                <button class="btn btn-primary1"  type="button" id="submit_button" onclick="update()">update</button>
                </div>
        </article>
    </div>
</div>

<script>     
    <#include "component://humanres/widget/forms/workflowsettings/workflowsettings.js"/>
    </script>
