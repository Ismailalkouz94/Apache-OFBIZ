alert("jhg");
var url = location.href;
var arguments = url.split('&')[2].split('=');
arguments.shift();
if(arguments=="1")
$("#PP_MYPORTAL_EMPLOYEE1partyRelAccounts00001").hide();