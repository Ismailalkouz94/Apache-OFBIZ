/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(window).load(function () {
    // executes when complete page is fully loaded, including all frames, objects and images

});





function update() {
    
    var emplPositionId= $("#0_lookupId_empPositionId").val();
    var wfSysApproverId =$("#approverWorkFlow").val();
    
    $.ajax({
        'async': false,
        type: "post",
        url: "updateWorkflowApproverPositionId",
        data: {
            emplPositionId: emplPositionId,
            wfSysApproverId: wfSysApproverId
         },
        dataType: "json",
        success: function (data) {
           if ('_ERROR_MESSAGE_' in data) {
                    ErrorMessage(data._ERROR_MESSAGE_);
                } else if ('_ERROR_MESSAGE_LIST_' in data) {

                    ErrorMessage(data._ERROR_MESSAGE_LIST_);
                } else if ('_EVENT_MESSAGE_' in data) {
                  
                    SuccessMessage(data._EVENT_MESSAGE_);
                }

        }
    }); // End of Ajax Request

}