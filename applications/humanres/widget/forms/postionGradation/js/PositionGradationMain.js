
function fillSalaryGradeTable(partyId) {
  
    var employeeName = "";
    var jqgrid_data = [];
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeeSalaryGrade",
        data: {partyId: partyId},
        dataType: "json",
        success: function (EmployeeData) {
            if (EmployeeData.length != 0) {
                employeeName = EmployeeData[0].fullName;
            }
            for (item in EmployeeData) {
                jqgrid_data.push({
                    rowSeq: EmployeeData[item].rowSeq,
                    empId: EmployeeData[item].partyId,
                    transDate: EmployeeData[item].TransDate,
                    jobId: EmployeeData[item].emplPositionId + "-" + EmployeeData[item].emplPositionDescription,
                    grade: EmployeeData[item].gradeDesc,
                    degree: EmployeeData[item].degreeDesc,
                    transType: EmployeeData[item].TransType,
                    basicSalary: EmployeeData[item].basicSalary,
                    department: EmployeeData[item].department,
                    positionId: EmployeeData[item].emplPositionId,
                    notes:EmployeeData[item].notes

                });
            }


        }});
    var colNames = ['', 'Employee ID', 'Effect Date', 'Position', 'Grade', 'Degree', 'Transaction Type', 'Basic Salary', 'Department', 'Notes',''];
    var colModel = [{
            name: 'rowSeq',
            index: 'rowSeq',
            align: 'center',
            hidden: true


        }, {
            name: 'empId',
            index: 'empId',
            align: 'center',
            hidden: true


        }, {

            name: 'transDate',
            index: 'transDate',
            editable: false

        }, {
            name: 'jobId',
            index: 'jobId',
            editable: false

        }, {
            name: 'grade',
            index: 'grade',
            editable: false

        },
        {
            name: 'degree',
            index: 'degree',
            editable: false

        },
        {
            name: 'transType',
            index: 'transType',
            editable: false


        },
        {
            name: 'basicSalary',
            index: 'basicSalary',
            editable: false


        }, {
            name: 'department',
            index: 'department',
            editable: false


        }, {
            name: 'notes',
            index: 'notes',
            editable: false


        },{
            name: 'positionId',
            index: 'positionId',
            editable: false,
            hidden: true


        }
    ];

    deleteGridData(colNames, colModel, jqgrid_data, "SalaryGradeGrid", "SalaryGradeGridDiv", "Employee Salary Grade for [ " + employeeName + "]", "transDate", "desc", "this action ", 10);
if(partyId != 0){
    $('#SalaryGradeGrid').jqGrid('setCaption', 'Employee Salary Grade for [ '  + employeeName + ' ]');
    }else {
            $('#SalaryGradeGrid').jqGrid('setCaption', 'Employee Salary Grade for [ ]');

    }

}


function refreshGridData(partyId) {

    var jqgrid_data = [];
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeeSalaryGrade",
        data: {partyId: partyId},
        dataType: "json",
        success: function (EmployeeData) {

            for (item in EmployeeData) {

                jqgrid_data.push({
                    rowSeq: EmployeeData[item].rowSeq,
                    empId: EmployeeData[item].partyId,
                    transDate: EmployeeData[item].TransDate,
                    jobId: EmployeeData[item].emplPositionId + "-" + EmployeeData[item].emplPositionDescription,
                    grade: EmployeeData[item].gradeDesc,
                    degree: EmployeeData[item].degreeDesc,
                    transType: EmployeeData[item].TransType,
                    basicSalary: EmployeeData[item].basicSalary,
                    department: EmployeeData[item].department,
                    positionId: EmployeeData[item].emplPositionId,
                    notes:EmployeeData[item].notes



                });

            }
        }});

    jQuery("#SalaryGradeGrid").jqGrid('clearGridData');
    jQuery("#SalaryGradeGrid").jqGrid('setGridParam', {data: jqgrid_data});
    jQuery("#SalaryGradeGrid").trigger('reloadGrid');
}






function getMaxSequence(transType,partyId) {
    var maxSeq = 0;
    $.ajax({
        'async': false,
        type: "post",
        url: "getMaxSequence_PositionGradation",
        data: {transType: transType,
            partyId: partyId},
        success: function (data) {

            maxSeq = data;
        }
    });
    return maxSeq;
}

function isCalculated(transDate,partyId){
       $.ajax({
        'async': false,
        type: "post",
        url: "isCalculatedPositionGradation",
        data: {transDate: transDate,
            partyId: partyId},
        success: function (data) {
//                        $("#del_jqgrid").hide();
//                    $("#jqgrid_iledit").hide();         
            if (data.trim() == "false") {
                $("#del_SalaryGradeGrid").show();
            } else {
                $("#del_SalaryGradeGrid").hide();
            }

        }
    });
}



var emplStatus = "";
var isTerminated="";
function isEmployeeTerminated(partyId) {
  var fullName  = getPartyName(partyId);

    $.ajax({
        'async': false,
        type: "post",
        url: "isEmployeeTerminated",
        data: {partyId: partyId},
        success: function (data) {
          
            isTerminated= data.trim();
            
        }});
    if(isTerminated.trim()=="false"){
        return "false";
    } 
    else if(isTerminated.trim()=="true"){
           $.smallBox({
            title: "Warning",
            content: fullName + " was terminated from your company!",
            color: "#dfb56c",
            timeout: 3000,
            icon: "fa fa-bell"
        });
          document.getElementById("myform").reset();
          fillSalaryGradeTable(0);
        return 0;
    } 
   
}

function getEmployeeStatus(partyId) {
  var fullName  = getPartyName(partyId);

    $.ajax({
        'async': false,
        type: "post",
        url: "getEmployeeStatus",
        data: {partyId: partyId},
        success: function (data) {
          
            emplStatus= data.trim();
            
        }});
    if(emplStatus.trim()=="HIRED"){
        return "HIRED";
    }  else if(emplStatus.trim()=="UNDER_HIRING"){
             $.smallBox({
            title: "Warning",
            content: "The employee under hiring please hiring it!",
            color: "#dfb56c",
            timeout: 3000,
            icon: "fa fa-bell"
        });
        document.getElementById("myform").reset();
        fillSalaryGradeTable(0);
    }
     else if(emplStatus ==""){

        $.smallBox({
            title: "Warning",
            content: "The employee not found!",
            color: "#dfb56c",
            timeout: 3000,
            icon: "fa fa-bell"
        });
        document.getElementById("myform").reset();
        fillSalaryGradeTable(0);

    }
    
   
}

function getPartyName(partyId) {
    var fullName = "";
    $.ajax({
        'async': false,
        type: "post",
        url: "getPartyName",
        data: "partyId=" + partyId,
        success: function (data) {
            fullName = data;
        }
    });
    
    return fullName;
}

function getEmplStatus(partyId) {
var emplStatus="";
    $.ajax({
        'async': false,
        type: "post",
        url: "getEmplStatus",
        data: {partyId: partyId},
        dataType: "json",
        success: function (data) {
            if (Object.keys(data).length != 0) {
                emplStatus = data.emplStatus.trim();
                
            }
        }});
    return emplStatus;
}