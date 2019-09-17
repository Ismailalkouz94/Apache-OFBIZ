 var InsuranceComList_Delete="";
   

    function fillInsuranceListData(){
       var jqgrid_data=[];
              
              $.ajax({
                 'async': false,
                 type: "post",
                 url: "getInsuranceComList",
                 data: "",
                 dataType: "json",
                 success: function (data) {
                    for(item in data){
                     jqgrid_data.push ({
                                insListId : data[item].insListId,
                                companyName : data[item].companyName,
                                remark : data[item].remark
                        } );
                            
                      }
                    }
            }); // End of Ajax Request
    
     var colNames=["Insurance List ID","company Name","remark"];
         var colModel=[{
                        key:true,
                            name : 'insListId',
                            index : 'insListId',
                             editable : false,
                            hidden:true

                    }, {
                            name : 'companyName',
                            index : 'companyName',
                            editable : true,
                            editrules: { required: true },

                    }, {
                            name : 'remark',
                            index : 'remark',
                            editable : true
                    }];

     fillGridData(colNames,colModel,jqgrid_data,"jqgrid","jqgridDiv","Insurance Companies List","insListId","asc","Insurance Company");

} //End of function fillInsuranceData 
    
  
                
function rowSelected(rowId,gridId){
    if(gridId=="jqgrid"){
        var rowData = jQuery('#jqgrid').getRowData(rowId); 
        InsuranceComList_Delete=rowData['insListId'];
      }
}
      
function firstRowSelected(gridId){
    if(gridId=="jqgrid"){
    }
}
$(document).ready(function(){ 
    
    //    Call Success Response Notifications
    jQuery("#jqgrid").on("jqGridInlineSuccessSaveRow", function (response,request) {
                        getNotificationSuccess(response,request);
        } ); 
    
   jQuery("#jqgrid").on("jqGridInlineAfterSaveRow", function () {
        var jqgrid_data=[];
        $.ajax({
                 'async': false,
                 type: "post",
                 url: "getInsuranceComList",
                 data: "",
                 dataType: "json",
                 success: function (data) {
                    for(item in data){
                     jqgrid_data.push ({
                                insListId : data[item].insListId,
                                companyName : data[item].companyName,
                                remark : data[item].remark
                        } );
                            
                      }
                    }
            }); // End of Ajax Request
    


        jQuery("#jqgrid").jqGrid('clearGridData');
             jQuery("#jqgrid").jqGrid('setGridParam', {data: jqgrid_data});
                jQuery("#jqgrid").trigger('reloadGrid');

            });  
         });  
             
$(window).load(function() {
 // executes when complete page is fully loaded, including all frames, objects and images
  fillInsuranceListData();
  
    document.getElementById("jqgrid_iladd").addEventListener("click", add);
    document.getElementById("jqgrid_iledit").addEventListener("click", edit);
    document.getElementById("del_jqgrid").addEventListener("click", deleteInsuranceList);
    document.getElementById("refresh_jqgrid").addEventListener("click",refresh);

});   
    

 
       
function refresh()
       {
           fillInsuranceListData();
       }
       
  
      
             
 function add(){ 
     
        $("#jqgrid").setGridParam({ editurl: "createInsuranceList"});        
                    
}; 
      
function edit(){ 

      $("#jqgrid").setGridParam({ editurl: "updateInsuranceList"});

}

function deleteInsuranceList(){ 

      $("#jqgrid").setGridParam({ editurl: 'deleteInsuranceList?insListId='+ InsuranceComList_Delete});

}

function RefreshGrid (gridID){
    if(gridID=="jqgrid"){
       fillInsuranceListData();
    }
          
}                  
