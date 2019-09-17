$( document ).ready(function() {
ChangeContent();
SaveContent();
DeleteContent();
});


$("#addList").click(function(){
  var toAdd='<li class="changed"><span class="handle" style="width: 100%;"><i class="fa fa-ellipsis-v" style="padding-right: 4px;"></i><i class="fa fa-ellipsis-v" style="padding-right: 4px;"></i><input class="editME text" type="text"/><button type="button" class="saveBtn">Save</button><div class="colorZone">If you want to add time ? <span>Pick color  </span><span><select class="colorPick"><option value="label-info">Dark Blue</option><option value="label-danger">Red</option><option value="label-warning">Light Brown</option><option value="label-success">Green</option><option value="label-primary">Light Blue</option><option value="label-default">Gray</option></select></span><input type="text" id="owrtTime" placeholder="add your time"></div><div class="tools"><i class="fa fa-edit"></i><i class="fa fa-trash-o"></i></div></li>';
  if($("#todolist li").hasClass("changed")){
  }else{
  $("#todolist").append(toAdd);
  }
  ChangeContent();
  DeleteContent();
  SaveContent();
});


function ChangeContent(){
$(".tools i:first-child").click(function(){
var innerNote=$(this).parent().parent().find("span.text").text();
var innerSmall=$(this).parent().parent().find("small").text();
var innerColor=$(this).parent().parent().find("small").attr("class").split(' ')[1];
if(!$("#todolist li input.editME").hasClass("editME")){
$(this).parent().parent().find("span.text").replaceWith('<input class="editME text" type="text"/><button type="button" class="saveBtn">Save</button>');
$(".editME").val($.trim(innerNote));
$(this).parent().parent().find("small").replaceWith('<div class="colorZone">If you want to add time ? <span>Pick color  </span><span><select class="colorPick"><option value="label-info">Dark Blue</option><option value="label-danger">Red</option><option value="label-warning">Light Brown</option><option value="label-success">Green</option><option value="label-primary">Light Blue</option><option value="label-default">Gray</option></select></span><input type="text" id="owrtTime" placeholder="add your time"></div>');
$("#owrtTime").val(innerSmall);
$(".colorPick").val(innerColor);
}else{
}
SaveContent();
});
}


function SaveContent(){
$(".saveBtn").click(function(){
var innerNote2=$(this).parent().find(".editME").val();
var theColor =$('.colorPick option:selected').val();
$( '<small class="label '+theColor+'"><i class="fa fa-clock-o"></i> '+$("#owrtTime").val()+'</small>' ).insertAfter( ".editME" );
$(".colorZone").hide();
$(this).parent().find(".editME").replaceWith('<span class="text">'+innerNote2+'</span>');
$("li.changed").removeClass("changed");
$(this).hide();
});
}


function DeleteContent(){
$(".tools i:last-child").click(function(){
$(this).closest( "li" ).remove();
});
}