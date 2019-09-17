
$(window).load(function () {
//    alert("ssssssss");
    $.ajax({type:'post',url:'getOrganizationId',
                data:document.getElementsByName('selectStr2').value,              
                success:function(data){
                document.getElementById('CustomFindAcctgTrans_organizationPartyId').innerHTML=data;   
                }});
});
