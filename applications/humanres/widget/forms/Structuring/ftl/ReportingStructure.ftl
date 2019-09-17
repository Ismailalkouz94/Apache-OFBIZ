<#ftl encoding="utf-8">

<!--Position Structuring Tree-->

<script src="/images/BusinessInnovation/js/plugin/jqgrid/grid.locale-en.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/jqgrid/jquery.jqGrid.min.js"></script>

<script type="text/javascript">
        
 //<![CDATA[
     function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));     
}
     $(function(){

       var mydata=[];
               <#assign reportingStructureTree = Static["org.apache.ofbiz.humanres.positionStructure.ReportingStructureTree"].reportingStructureTree(request, response)>

        var foo1 = getParameterByName('emplPositionId');
       <#list reportingStructureTree as list>
           if(foo1=='${(list.MANAGED_BY)!}')
           {
               if(${list.LVL}!=0){var par1 = "${(list.REPORTING_TO)!}";}
               else {var par1 = 0;}
           }
                
       </#list>
       <#list reportingStructureTree as list>
           if(par1=='${(list.MANAGED_BY)!}')
           {
               if(${list.LVL}!=0){var par2 = "${(list.REPORTING_TO)!}";}
               else {var par2 = 0;}
           }
               
       </#list>
       <#list reportingStructureTree as list>
           if(par2=='${(list.MANAGED_BY)!}')
           {
               if(${list.LVL}!=0){var par3 = "${(list.REPORTING_TO)!}";}
               else {var par3 = 0;}
           }
               
       </#list>
       <#list reportingStructureTree as list>
            var theidis = "${list.MANAGED_BY}";
            var theparentidis = "${(list.REPORTING_TO)!}";
            var thenameis = " ${(list.POS_NAME)!}   ${(list.partyId)!}  ${(list.partyName)!} ";
            var theidandname = theidis+thenameis;
                var theidandname = "<font style='font-size:12px;'>"+theidandname+"</font>"
            var foo1 = getParameterByName('emplPositionId');
            var isittrue = "false";
            if(${(list.leaf)!}==0){var isitleaf = "false";} else {var isitleaf = "true";}
            if(theidis==foo1 || theidis==par1 || theidis==par2 || theidis==par3){isittrue="true";}

                
          mydata.push ({
              id :"${list.MANAGED_BY}",
              empPostion:theidandname,
              level: "${list.LVL}",            
              parent:"${(list.REPORTING_TO)!}",
              isLeaf:isitleaf,
              expanded:isittrue,   
              loaded:true                    
               } );                           
     </#list>
          
             grid = $("#PostionStructureTree");
        
var ffg = "namey";
  
         grid.jqGrid({
//               datatype: "local",
             data: mydata, // will not used at the loading,
                           // but during expanding/collapsing the nodes   , key:true,sortable: true
             
             colModel:[
                 {name:'empPostion', index:'empPostion', width:250, formatter:'showlink', formatter: function (cellvalue, options, rowObject) {
                    
                    if(foo1==rowObject.id)
                    {
                        return "<a href='EditEmplPositionStructuring?emplPositionId="+rowObject.id+"' class='anchor usergroup_name link'><font style='font-weight:bold; font-size:16px; border:1px solid #333333; padding:5px; border-radius: 5px; box-shadow: 2px 2px 1px grey;'>" +
                           rowObject.empPostion + '<font></a>';
                    }
                    else
                    {
                        return "<a href='EditEmplPositionStructuring?emplPositionId="+rowObject.id+"' class='anchor usergroup_name link'>" +
                           rowObject.empPostion + '</a>';
                    }
                },}
                     
             ],
             height:'100%',
             rowNum: 10000,
             sortname: 'id',
             treeGrid: true,
             treeGridModel: 'adjacency',
             treedatatype: "local",
             ExpandColumn: 'empPostion',
             caption: "Position Structuring"
                
         });
           
         // we have to use addJSONData to load the data
         grid[0].addJSONData({
             total: 1,
             page: 1,
             records: mydata.length,
             rows: mydata
         });
     });
 //]]>
        

            
    </script>
<style>
    .ui-jqgrid-labels {display:none;}
    .ui-jqgrid-titlebar {display:none;}
    #jqgh_treegrid_accountName {display:none;}
    #treegrid_accountName {display:none;}
    .ui-state-highlight a {font-size:14px !important;}
    .ui-jqgrid {border: none !important;}
.jqgrow {border: none !important;}
.cell-wrapper a {font-size:12px;}
.ui-jqgrid .ui-state-highlight td {background-color:#cccccc !important}




table.ui-jqgrid-btable tr {
  list-style: none;
  margin: 0px;
  padding: 0px;
 
}

table.ui-jqgrid-btable td{
	float: none; 
  width: 100%;
}
table.ui-jqgrid-btable td:first-child{
    display: -webkit-box;
}

table.ui-jqgrid-btable td a{ 
  width: 100%; 
  padding: 20px; 
  border-left: 0px solid; 
  position: relative; 
  z-index: 2;
  text-decoration: none;
  color: #444;
  box-sizing: border-box;  
  -moz-box-sizing: border-box;  
  -webkit-box-sizing: border-box; 
}
	
table.ui-jqgrid-btable td a:hover{ border-bottom: 0px; color: #fff !important;}
table.ui-jqgrid-btable td:first-child a{ border-left: 3px solid #266ead; }
table.ui-jqgrid-btable td:nth-child(2) a{ border-left: 3px solid #266ead; }
table.ui-jqgrid-btable td:nth-child(3) a{ border-left: 3px solid #266ead; }
table.ui-jqgrid-btable td:last-child a{ border-left: 3px solid #266ead; }

table.ui-jqgrid-btable td a:after { 
  content: "";
  height: 100%; 
  left: 0; 
  top: 0; 
  width: 0px;  
  position: absolute; 
  transition: all 0.3s ease 0s; 
  -webkit-transition: all 0.3s ease 0s; 
  z-index: -1;
}

table.ui-jqgrid-btable td a:hover:after{ width: 100%; }
table.ui-jqgrid-btable td:first-child a:after{ background: #266ead; }
table.ui-jqgrid-btable td:nth-child(2) a:after{ background: #266ead; }
table.ui-jqgrid-btable td:nth-child(3) a:after{ background: #266ead; }
table.ui-jqgrid-btable td:last-child a:after{ background: #266ead; }



#gbox_PostionStructureTree{border:1px solid #266ead;}
    </style>
<!--Position Structuring Tree-->

<div class="row">
    <article class="col-xs-6 col-sm-6 col-md-6 col-lg-6 sortable-grid ui-sortable">
        <div class="jarviswidget jarviswidget-color-blueDark jarviswidget-sortable" id="wid-id-0" data-widget-editbutton="false" role="widget">
            <header>
                <h2>${uiLabelMap.HumanResAddReportsToEmplPositionReportingStruct}</h2>
                </header>
            <div role="content">
                <form name="AddReportsToEmplPositionReportingStruct" action="javascript:submitReportingStruct()" class="smart-form" method="post">
                    <input type="hidden" id="emplPositionId" name="emplPositionId" value="${parameters.emplPositionId}">
                    <input type="hidden" id="emplPositionIdManagedBy" name="emplPositionIdManagedBy" value="${parameters.emplPositionId}">
                    <table class="basic-table">
                        <tr>
                            <td><label>Empl Position Id Reporting To</label></td>
                            <td><@htmlTemplate.lookupField formName="AddReportsToEmplPositionReportingStruct" className="required" name="emplPositionIdReportingTo" id="emplPositionIdReportingTo" fieldFormName="LookupEmplPosition" /></td>
                            </tr>
                        <tr>
                            <td><label>From Date</label></td>
                            <td><@htmlTemplate.renderDateTimeField name="fromDate" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="${requestParameters.fromDate!}" size="" maxlength="" id="fromDate" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                                </td>
                            </tr>
                        <tr>
                            <td><label>Through Date</label></td>
                            <td><@htmlTemplate.renderDateTimeField name="thruDate" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="${requestParameters.thruDate!}" size="25" maxlength="30" id="thruDate" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>            </td>
                            </td>
                            </tr>
                        <tr>
                            <td><label>Comments</label></td>
                            <td><input type="text" id="comments" name="comments" class="form-control"/></td>
                            </tr>
                        <tr>
                            <td><label>Primary Flag</label></td>
                            <td><select class="form-control" id="primaryFlag" name="primaryFlag">
                                    <option key="Y" value="Y">Y</option>
                                    <option key="N" value="N">N</option>
                                    </select>
                                </td>
                            </tr>
                        <tr>
                            <td></td>
                            <td><input class="btn btn-primary1" type="submit" name="submitButton" value="${uiLabelMap.CommonAdd}"/></td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </article>
    <article class="col-xs-6 col-sm-6 col-md-6 col-lg-6 sortable-grid ui-sortable">
        <div class="jarviswidget jarviswidget-color-blueDark jarviswidget-sortable" id="wid-id-0" data-widget-editbutton="false" role="widget">
            <header>
                <h2>Position Structuring Tree</h2>
            </header>
            <div role="content">
                <table id="PostionStructureTree">
                    <tr><td/></tr>
                </table>
            </div>
        </div>
    </article>
</div>

<script>
    <#include "component://humanres/widget/forms/Structuring/js/ReportingStructure.js"/> 
</script>