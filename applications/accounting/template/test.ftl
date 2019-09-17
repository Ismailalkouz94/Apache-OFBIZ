<script>
    
    //alert("a7mad");
       $("#gbox_treegrid > .ui-jqgrid-titlebar").hide() 
</script>


<!--<script src="/rainbowstone/js/plugin/jqgrid/jqq/jquery.jqGrid.min.js"></script>
<script src="/rainbowstone/js/plugin/jqgrid/jqq/grid.locale-en.min.js"></script>-->



        <script src="/images/BusinessInnovation/js/plugin/jqgrid/grid.locale-en.min.js"></script>
  <script src="/images/BusinessInnovation/js/plugin/jqgrid/jquery.jqGrid.min.js"></script>


<script type="text/javascript">
    window.onload = function() {

         document.getElementById("lui_treegrid").setAttribute("class", "");

      };
   
        
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
               <#assign biTrialBalance = Static["org.apache.ofbiz.accounting.GlAccountsTree"].glAccountsTree(request, response)>

        var foo1 = getParameterByName('glAccountId');
       <#list biTrialBalance as list>
           if(foo1==${list.GL_ACCOUNT_ID})
           {
               if(${list.LVL}!=0){var par1 = "${(list.PARENT_GL_ACCOUNT_ID)!}";}
               else {var par1 = 0;}
           }
                
       </#list>
       <#list biTrialBalance as list>
           if(par1==${list.GL_ACCOUNT_ID})
           {
               if(${list.LVL}!=0){var par2 = "${(list.PARENT_GL_ACCOUNT_ID)!}";}
               else {var par2 = 0;}
           }
               
       </#list>
       <#list biTrialBalance as list>
           if(par2==${list.GL_ACCOUNT_ID})
           {
               if(${list.LVL}!=0){var par3 = "${(list.PARENT_GL_ACCOUNT_ID)!}";}
               else {var par3 = 0;}
           }
               
       </#list>
       <#list biTrialBalance as list>
            var theidis = "${list.GL_ACCOUNT_ID}";
            var theparentidis = "${(list.PARENT_GL_ACCOUNT_ID)!}";
            var thenameis = " ${(list.ACCOUNT_NAME)!}";
            var theidandname = theidis+thenameis;
                var theidandname = "<font style='font-size:12px;'>"+theidandname+"</font>"
            var foo1 = getParameterByName('glAccountId');
            var isittrue = "false";
            if(${(list.leaf)!}==0){var isitleaf = "false";} else {var isitleaf = "true";}
            if(theidis==foo1 || theidis==par1 || theidis==par2 || theidis==par3){isittrue="true";}

                
          mydata.push ({
              id :"${list.GL_ACCOUNT_ID}",
              accountName:theidandname,
              level: "${list.LVL}",            
              parent:"${(list.PARENT_GL_ACCOUNT_ID)!}",
              isLeaf:isitleaf,
              expanded:isittrue,   
              loaded:true                    
               } );                           
     </#list>
          
             grid = $("#treegrid");
        
var ffg = "namey";
  
         grid.jqGrid({
//               datatype: "local",
             data: mydata, // will not used at the loading,
                           // but during expanding/collapsing the nodes   , key:true,sortable: true
             
             colModel:[
                 {name:'accountName', index:'accountName', width:250, formatter:'showlink', formatter: function (cellvalue, options, rowObject) {
                    
                    if(foo1==rowObject.id)
                    {
                        return "<a href='GlAccountNavigate?glAccountId="+rowObject.id+"' class='anchor usergroup_name link'><font style='font-weight:bold; font-size:16px; border:1px solid #333333; padding:5px; border-radius: 5px; box-shadow: 2px 2px 1px grey;'>" +
                           rowObject.accountName + '<font></a>';
                    }
                    else
                    {
                        return "<a href='GlAccountNavigate?glAccountId="+rowObject.id+"' class='anchor usergroup_name link'>" +
                           rowObject.accountName + '</a>';
                    }
                },}
                     
             ],
             height:'100%',
             rowNum: 10000,
             pager : "#ptreegrid",
             sortname: 'id',
             treeGrid: true,
             treeGridModel: 'adjacency',
             treedatatype: "local",
             ExpandColumn: 'accountName',
             caption: "Balance Sheet"
                
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
    
    .ui-jqgrid-titlebar {display:none;}
    #jqgh_treegrid_accountName {display:none;}
    #treegrid_accountName {display:none;}
    .ui-state-highlight a {font-size:14px !important;}
    .ui-jqgrid {border: none !important;}
.jqgrow {border: none !important;}
.cell-wrapper a {font-size:12px;}
.ui-jqgrid .ui-state-highlight td {background-color:#cccccc !important}




table tr {
  list-style: none;
  margin: 0px;
  padding: 0px;
}

table td{
	float: none; 
  width: 100%;
}

table td a{ 
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
	
table td a:hover{ border-bottom: 0px; color: #fff !important;}
table td:first-child a{ border-left: 3px solid #266ead; }
table td:nth-child(2) a{ border-left: 3px solid #266ead; }
table td:nth-child(3) a{ border-left: 3px solid #266ead; }
table td:last-child a{ border-left: 3px solid #266ead; }

table td a:after { 
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

table td a:hover:after{ width: 100%; }
table td:first-child a:after{ background: #266ead; }
table td:nth-child(2) a:after{ background: #266ead; }
table td:nth-child(3) a:after{ background: #266ead; }
table td:last-child a:after{ background: #266ead; }



#gbox_treegrid {border:1px solid #266ead;}
    </style>

        
        <table id="treegrid"><tr><td/></tr></table>
        <div id="ptreegrid"/>
        </div>

    