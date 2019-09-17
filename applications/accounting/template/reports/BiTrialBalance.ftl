    <#assign parameter = Static["org.apache.ofbiz.accounting.report.BiTrialBalance"].parameter(request, response)>




<script src="/images/BusinessInnovation/js/plugin/jqgrid/grid.locale-en.min.js"></script>
<script src="/images/BusinessInnovation/js/plugin/jqgrid/jquery.jqGrid.min.js"></script>


<script type="text/javascript">
    window.onload = function() {
       
         document.getElementById('fromDate').value ="${(session.getAttribute("treeFromDate"))!}";
         document.getElementById('toDate').value ="${(session.getAttribute("treeToDate"))!}";
             AjaxReload(0);
        document.getElementById("lui_treegrid").setAttribute("class", "");
                     
      };
   
      function AjaxReload (pram){  
 //<![CDATA[
     $(function(){
     <#assign biTrialBalance = Static["org.apache.ofbiz.accounting.report.BiTrialBalance"].getReportXMLBiTrialBalance(request, response)>

       var mydata=[];
        var c=1,x=true;
               

       <#list biTrialBalance as list>
      if(pram ==1){
          if(${(list.DR)!} != 0 || ${(list.CR)!} != 0){
          mydata.push ({
              id :"${list.GL_ACCOUNT_ID}",
              accountName:"${(list.ACCOUNT_NAME)!}",
              DR:"${(list.DR)!}".replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),
              CR:"${(list.CR)!}".replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),   
              BALANCE:"${(list.BALANCE)!}".replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),
              level: "${list.LVL}",            
              parent:"${(list.PARENT_GL)!}",
              isLeaf:"${(list.leaf)!}",
              expanded:false ,   
              loaded:true                    
               } );   
               c++;
               x=true; }
          } else  if(pram ==0){
                      mydata.push ({
              id :"${list.GL_ACCOUNT_ID}",
              accountName:"${(list.ACCOUNT_NAME)!}",
              DR:"${(list.DR)!}".replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),
              CR:"${(list.CR)!}".replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),   
              BALANCE:"${(list.BALANCE)!}".replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"),
              level: "${list.LVL}",            
              parent:"${(list.PARENT_GL)!}",
              isLeaf:"${(list.leaf)!}",
              expanded:false ,   
              loaded:true                    
               } );   
               c++;
               x=true;
                  }       
     </#list>
            
          
             grid = $("#treegrid");
        

         grid.jqGrid({
//               datatype: "local",
             data: mydata, // will not used at the loading,
                           // but during expanding/collapsing the nodes   , key:true,sortable: true
             colNames:["${uiLabelMap.AccountingAccountId}","${uiLabelMap.FormFieldTitle_accountName}","${uiLabelMap.AccountingDebitFlag}","${uiLabelMap.AccountingCreditFlag}","${uiLabelMap.Balance}" ],
             colModel:[
                 {name:'id', index:'id', width:100 },
                 {name:'accountName', index:'accountName', width:250},
                 {name:'DR', index:'DR', width:180},
                 {name:'CR', index:'CR', width:180},
                 {name:'BALANCE', index:'BALANCE', width:180}  
                     
             ],
             height:'100%',
             rowNum: 10000,
             pager : "#ptreegrid",
             sortname: 'id',
             treeGrid: true,
             treeGridModel: 'adjacency',
             treedatatype: "local",
             ExpandColumn: 'id',
             caption: "${uiLabelMap.BalanceSheetTree}"
                
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
        
        }
     function mydate()
                    {
                    //alert("");
//                    document.getElementById("dt").hidden=false;
//                    document.getElementById("ndt").hidden=true;
                 
                    d=new Date(document.getElementById("fromDate").value);
                    fromDate=d.getDate();
                    mn=d.getMonth();
//                    mn++;
                    yy=d.getFullYear();
//                    document.getElementById("ndt").value=dt+"/"+mn+"/"+yy
//                    document.getElementById("ndt").hidden=false;
//                    document.getElementById("dt").hidden=true;
                    }
                   var id;     
                function saveData(){
                       
                           } 
                               
                               
                               
                               
                               
                            
                             
                    
                
    </script>

<form  method="post" class="smart-form" action="<@ofbizUrl>BiTrialBalance?organizationPartyId=Company</@ofbizUrl>">       
    <table id="table1" cellspacing="5px" cellpadding="5%" align="center">

        <tr>
            <td align="right" required="true"><label>${uiLabelMap.FormFieldTitle_datefrom}</label><span class="required">*</span>&nbsp;&nbsp;</td>
            <#-- <td><td><input class="form-control" type="date" name="fromDate" id="fromDate"  required/></td></td>-->
            <td>
               <@htmlTemplate.renderDateTimeField name="fromDate" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="${requestParameters.fromDate!}" size="25" maxlength="30" id="fromDate" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
            </td>
            <!--<td> <input class="form-control" type="text" id="myInputPartyId" onkeyup="findAllowencesEmployee()" placeholder="Search for party id.." title="Type in a name" style="border-radius: 4px; padding: 10px 15px;"></td>-->
            <td align="right" required="true">  &nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td align="right" required="true"><label>${uiLabelMap.FormFieldTitle_dateThru}</label><span class="required">*</span>&nbsp;&nbsp;</td>
            <td> 
                   <@htmlTemplate.renderDateTimeField name="toDate" event="" action="" className="required" alert="" title="Format: yyyy-MM-dd" value="${requestParameters.toDate!}" size="25" maxlength="30" id="toDate" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>            </td>
            <#-- <td><input class="form-control" type="date" name="toDate" id="toDate" required/></td>-->
	    <td>&nbsp;</td>
            <td align="right"><input class="btn btn-primary" type="submit" value="${uiLabelMap.CommonSubmit}"  /></td>
            <td align="right">   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td>
		<label class="checkbox-inline">
		<input class="checkbox style-0" type="checkbox" name="Show" id="Show" value="1">
		<span style="margin-left: 0.8px;"> ${uiLabelMap.HidezeroValues}</span></label>		
            </tr>
        <td>
             <!--<a href="<@ofbizUrl>reportBiTrialBalance.pdf</@ofbizUrl>" class="smallSubmit">Export as PDF</a>-->
            </td>

        </table> 
    </form>
<table id="treegrid"><tr><td/></tr></table>
<div id="ptreegrid"/>

<script type="text/javascript">
    
                        $(document).ready(function() {	
                            var id= ${(parameters.Show)!0};
                            if(${(parameters.Show)!0}==1){
                            document.getElementById("Show").checked =true;
                                id=1;
                                }
                                    else{
                                        document.getElementById("Show").checked =false;
                                            id=0;
                                        }
                           
                               document.getElementById("Show").addEventListener("click", function () {
//                                   id =document.getElementById("Show").value;
                                            if(document.getElementById("Show").checked ==true){
//                                                location.href="BiTrialBalance?organizationPartyId=Company&Show="+1;
                                                        AjaxReload(1);
                                                    }else
                                                        {
//                                                            location.href="BiTrialBalance?organizationPartyId=Company&Show="+0;
                                                            AjaxReload(0);
                                                            }
                          });
                        })
                     </script>             