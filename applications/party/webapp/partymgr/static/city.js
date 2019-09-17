

document.addEventListener('DOMContentLoaded', function () {

        $.ajax({
            type: "post",
            url: "requsetCity",
            data: "countryGeoId=" + document.getElementById("NewUser2_USER_COUNTRY").value,
            success: function (data) {
//               alert(data);
                document.getElementById("NewUser2_USER_CITY").innerHTML = data;
document.getElementById("NewUser2_USER_ADDRESS1").value=document.getElementById("NewUser2_USER_CITY").value;
            }

        });
    document.getElementById("NewUser2_USER_COUNTRY").addEventListener("change", function () {
        $.ajax({
            type: "post",
            url: "requsetCity",
            data: "countryGeoId=" + document.getElementById("NewUser2_USER_COUNTRY").value,
            success: function (data) {
//               alert(data);
                document.getElementById("NewUser2_USER_CITY").innerHTML = data;

            }

        });

    });
    
     document.getElementById("NewUser2_USER_CITY").addEventListener("change", function () {
                   document.getElementById("NewUser2_USER_ADDRESS1").value=document.getElementById("NewUser2_USER_CITY").value;


    });

});




