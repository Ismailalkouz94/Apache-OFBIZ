$(window).load(function () {
      $("#NewFamilyMemebr_USER_WIFE_ISWORKING").hide();
            $("#NewFamilyMemebr_USER_WIFE_ISWORKING_title").hide();

});

document.addEventListener('DOMContentLoaded', function () {
    $.ajax({
        type: "post",
        url: "requsetCity",
        data: "countryGeoId=" + document.getElementById("NewFamilyMemebr_USER_COUNTRY").value,
        success: function (data) {
//               alert(data);
            document.getElementById("NewFamilyMemebr_USER_CITY").innerHTML = data;
            document.getElementById("NewFamilyMemebr_USER_ADDRESS1").value = document.getElementById("NewFamilyMemebr_USER_CITY").value;
        }

    });
    document.getElementById("NewFamilyMemebr_USER_COUNTRY").addEventListener("change", function () {
        $.ajax({
            type: "post",
            url: "requsetCity",
            data: "countryGeoId=" + document.getElementById("NewFamilyMemebr_USER_COUNTRY").value,
            success: function (data) {
//               alert(data);
                document.getElementById("NewFamilyMemebr_USER_CITY").innerHTML = data;

            }

        });

    });

    document.getElementById("NewFamilyMemebr_USER_CITY").addEventListener("change", function () {
        document.getElementById("NewFamilyMemebr_USER_ADDRESS1").value = document.getElementById("NewFamilyMemebr_USER_CITY").value;


    });


    document.getElementById("NewFamilyMemebr_USE_FAMILY_MEMBER_TYPE").addEventListener("change", function () {
        if ($("#NewFamilyMemebr_USE_FAMILY_MEMBER_TYPE").val() == "WIFE") {
            $("#NewFamilyMemebr_USER_WIFE_ISWORKING").show();
            $("#NewFamilyMemebr_USER_WIFE_ISWORKING_title").show();

        } else {
            $("#NewFamilyMemebr_USER_WIFE_ISWORKING").hide();
            $("#NewFamilyMemebr_USER_WIFE_ISWORKING_title").hide();
        }
    });



});




