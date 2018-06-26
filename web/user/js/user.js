//页面跳转
var jump_profile_edit = document.getElementById("btn-profile-edit");
var jump_classes_edit = document.getElementById("btn-classes-edit");
var jump_classes_choose = document.getElementById("main-bar-2");
var jump_profile_display = document.getElementById("main-bar-1");

var class_choose_area = document.getElementById("class-choose-area");
var profile_display_area = document.getElementById("profile-display-area");
var profile_edit_area = document.getElementById("profile-edit-area");
var classes_edit_area = document.getElementById("classes-edit-area");

jump_profile_edit.onclick = function(){
    jump_classes_choose.style.color = "black";
    jump_profile_display.style.color = 'black';

    profile_edit_area.style.display = "block";
    class_choose_area.style.display ="none";
    profile_display_area.style.display ="none";
    classes_edit_area.style.display ="none";
}

jump_classes_edit.onclick = function(){
    jump_classes_choose.style.color = "black";
    jump_profile_display.style.color = 'black';

    profile_edit_area.style.display = "none";
    class_choose_area.style.display ="none";
    profile_display_area.style.display ="none";
    classes_edit_area.style.display ="block";
}

jump_classes_choose.onclick = function(){
    jump_classes_choose.style.color = "#428bca";
    jump_profile_display.style.color = 'black';

    profile_edit_area.style.display = "none";
    class_choose_area.style.display ="block";
    profile_display_area.style.display ="none";
    classes_edit_area.style.display ="none";
}

jump_profile_display.onclick = function(){
    jump_classes_choose.style.color = "black";
    jump_profile_display.style.color = '#428bca';

    profile_edit_area.style.display = "none";
    class_choose_area.style.display ="none";
    profile_display_area.style.display ="block";
    classes_edit_area.style.display ="none";
}
