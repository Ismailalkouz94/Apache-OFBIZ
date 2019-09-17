
$( document ).ready(function() {
if(document.body.className=="desktop-detected menu-on-top pace-done" || document.body.className=="desktop-detected pace-running menu-on-top")
	{
document.getElementById("0").style.display="none"; 
document.getElementById("1").style.display="none";
document.getElementById("2").style.display="none";
document.getElementById("3").style.display="none";
document.getElementById("4").style.display="none";
document.getElementById("5").style.display="none";
document.getElementById("6").style.display="none";
document.getElementById("7").style.display="none";

document.getElementById("moreInfo").innerHTML=document.getElementById("conn12").innerHTML;}

else{
	document.getElementById("more").style.display="none"; 
       
	}    
});

