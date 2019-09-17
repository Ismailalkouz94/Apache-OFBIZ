$(window).load(function () {
    getEmployeeDetails("${partyId}");
       check_If_isExists("partyId","${partyId}","UserLogin");
});

function getHiredDate(partyId){
     
    $.ajax({
        'async': false,
        type: "post",
        url: "getHiredDate",
        data: {partyId: partyId},
        success: function (data) {
           $("#hiriedDate").text(data);
        }
    });
}

var isExists = 0;
function check_If_isExists(fieldName, fieldId, entityName) {
    $.ajax({
        'async': false,
        type: "post",
        url: "isHaveRow_Validation",
        data: {fieldName: fieldName, fieldId: fieldId, entityName: entityName},
        success: function (data) {
            isExists = data.trim();
                  if(isExists=="true"){
                 $("#addUserLoginBtn").attr("class","hidden");
            } else if(isExists=="false"){
                 $("#addUserLoginBtn").attr("class","widget-icon");
            }
        }
    });
 
}

function getEmployeeDetails(partyId) {
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeePositionDetails",
        data: "partyId=" + partyId,
        dataType: "json",
        success: function (data) {
            if ($.trim(data)) {
                getHiredDate("${partyId}");
                $("#departmentId").text(data.depDesciption);
                $("#positionId").text(data.postionTypeDesc);
                $("#gradeId").text(data.gradeDesc);
                $("#degreeId").text(data.degreeDesc);
            }
        }
    });
}