
$(window).load(function () {

    if (document.getElementById("NewEmpJob") != null || document.getElementById("NewGrade") != null
            || document.getElementById("NewDegree") != null || document.getElementById("NewAllowence") != null
            || document.getElementById("NewJob") != null || document.getElementById("NewAllowenceGrade") != null
            || document.getElementById("NewAllowenceEmployee") != null  ) {
        $.ajax({
            type: "post",
            url: "getUserLogin",
            data: "userLoginId=" + document.getElementsByName("userLoginId").value,
            success: function (data) {
                //document.getElementById("userLoginId").value=data;   
//             document.getElementsByName("userLoginId")[0].value = data;
//             alert(data);
                document.getElementById("userLoginId").value = data.trim();
             

            }
        });
    }
    
     if(document.getElementById("customNewAllowenceGrade") != null){       
          $.ajax({
            type: "post",
            url: "getUserLogin",
            data: "userLoginId=" + document.getElementsByName("userLoginId").value,
            success: function (data) {
                document.getElementById("userLoginId_customNewAllowenceGrade").value = data.trim();              
            }
        });
     }
       if(document.getElementById("customNewDegree") != null){       
          $.ajax({
            type: "post",
            url: "getUserLogin",
            data: "userLoginId=" + document.getElementsByName("userLoginId").value,
            success: function (data) {
                document.getElementById("customNewDegree_userLoginId").value = data.trim();              
            }
        });
     }  
}); 


 function udpateFlag()
        {
         alert("hello");
         //validation code to see State field is mandatory.  
        }  