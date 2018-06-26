//页面跳转
var jump_classchoose = document.getElementById("CC");
var jump_userinfo = document.getElementById("UI");
var jump_userinfoedit = document.getElementById("edit-info");

var classes_choose = document.getElementById("classes-choose");
var user_information = document.getElementById("user-information");
var user_information_edit = document.getElementById("user-information-edit");

jump_classchoose.onclick = function(){
    jump_classchoose.style.color='#B2DFEE';
    jump_userinfo.style.color='#FFF';
    classes_choose.style.display = 'block';
    user_information.style.display = 'none';
    user_information_edit.style.display = 'none';
}
jump_userinfo.onclick = function(){
    jump_classchoose.style.color='#FFF';
    jump_userinfo.style.color='#B2DFEE';
    classes_choose.style.display = 'none';
    user_information.style.display = 'block';
    user_information_edit.style.display = 'none';
}
jump_userinfoedit.onclick = function(){
    classes_choose.style.display = 'none';
    user_information.style.display = 'none';
    user_information_edit.style.display = 'block';
}
        