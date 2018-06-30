var postForm = "<div id='Summary' style='padding:10px;'>" +
    "<div>" +
        "<span >概述</span>" +
        "<em>(少于100字)</em>" + 
    "</div>" +
    "<input type='text' placeholder='输入问题概述...'>" +
"</div>" +
"<div id='details' style='padding:10px;'>" +
    "<span>具体内容</span>" +
  "<textarea ></textarea>" + 
"</div>" +
"<div id='show_my_name_as' style='padding:10px;'>" +
    "<span>发表身份</span>" +
    "<select>" +
        "<option value='default' selected>" + "username" + "</option>" +
        "<option value='anonymous'>匿名</option>" + 
    "</select>" + 
"</div>" +
"<div id='buttons' style='padding:10px;'>" +
    "<span></span>" + 
    "<button id='post_button' type='button' onclick='onClickNewPostPost()'>Post</button>" + 
    "<button id='cancel_button' 'type='button'>cancel</button>" +
"</div>";

var questionCardString = 
"<div id='question_card_container' class='card'>" +
    "<div id='question_header' class='header'>" + 
      "<div id='question_icon'>?</div>" +
      "<span class='title'>问题</span>" + 
      "<span id='name'></span>" +
    "</div>" +
    "<div id='question_body' class='body'>" +
        "<div id='question_summary'></div>" +
        "<div id='question_details'></div>" +
    "</div>" +
  "</div>";

var discussionCardString = "<div id='discussion_card_container' class='card'>" +
    "<div id='discussion_header' class='header title'>" +
      "讨论" +
    "</div>" +
    "<div id=discussion_body class='body'>" +
        "<h5>开始讨论</h5>" +
        "<div onclick='editDiscussion(this)'>回复</div>" +
    "</div>"
  "</div>" +
"</div>";
    
/*question edit form*/
var questionBodyString = "<h3>概述</h3>" +
    "<input type='text' id='question_edit_summary' name='question_edit_summary'></input>" +
    "<h3>具体内容</h3>" +
    "<textarea id='question_edit_details'></textarea>";




/* new post 页面post按钮回调 */
function onClickNewPostPost() {
    var summary = document.querySelector("#Summary input").value;
    var details = document.querySelector("#details textarea").value;
    var select = document.querySelector("#show_my_name_as select");
    var name = select.options[select.selectedIndex].text;

    var xhr = new XMLHttpRequest();
    var param = "requestType=post&courseCode=" + getCurrentUrlParam("courseCode") + "&title=" + summary + "&content=" + details;
    var method = "post";
    var url = "http://172.18.157.157:8080/one-course-server/course-post.jsp";
    xhr.open(method, url, true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send(param);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            if (xhr.status >= 200 && xhr.status < 300 || xhr.status == 304) {
                var jsonResponse = JSON.parse(xhr.responseText);
                if (jsonResponse.code == 201) {
                    var post = jsonResponse.content[0];
                    var postLi = document.createElement("li");
                    var postDiv = document.createElement("div");
                    var summaryDiv = document.createElement("div");
                    var rightIcon = document.createElement("div");
                    var data = document.createElement("div");

                    summaryDiv.setAttribute("id", "post_summary");
                    summaryDiv.innerHTML = summary;

                    rightIcon.setAttribute("id", "rightIcon");
                    if (post.userRole === "STUDENT") {
                        rightIcon.classList.add("green");
                        rightIcon.innerHTML = "s";
                    } else {
                        rightIcon.classList.add("yellow");
                        rightIcon.innerHTML = "i";
                    }

                    data.setAttribute("style", "display:none");
                    data.innerHTML = JSON.stringify(post);

                    postDiv.appendChild(summaryDiv);
                    postDiv.appendChild(rightIcon);
                    postDiv.appendChild(data);
                    postLi.appendChild(postDiv);
                    postLi.setAttribute("id", post.postId);

                    var ul = document.getElementById("post_list");
                    for (var i = 0; i < ul.childElementCount; i++) {
                        ul.childNodes[i].classList.remove("post_active");
                    }
                    postLi.classList.add("post_active");
                    postLi.onclick = function(event) {
                        return onClickPostListItem(event);
                    };
                    ul.appendChild(postLi);
                    
                    var center = document.getElementById("page_center");
                    removeAllChildren(center);
                    center.innerHTML = questionCardString + discussionCardString;
                    setQuestionCard(summary, details, name);
                } else {
                    alert(jsonResponse.code + ": " + jsonResponse.msg);
                }
            }
        } 
    };
}


function setQuestionCard(summary, details, name) {
    var question_summary = document.getElementById("question_summary");
    var question_details = document.getElementById("question_details");
    var question_poster = document.querySelector("#question_header #name");

    var header = document.createElement("h1");
    header.innerHTML = summary;
    question_summary.appendChild(header);

    var body = document.createElement("p");
    body.innerHTML = details;

    question_details.appendChild(body);
    
    if (name.indexOf("by") != 0) {
        question_poster.innerHTML = "by " + name;
    } else {
        question_poster.innerHTML = name;
    }
}

function getNewPostPage() {
    var center = document.getElementById("page_center");
    var oldStr = center.innerHTML;
    removeAllChildren(center);     
    center.innerHTML = postForm;
    var cancelButton = document.getElementById("cancel_button");
    cancelButton.onclick = function() {
        center.innerHTML = oldStr;
    }
}

function removeAllChildren(node) {
    var children = node.childNodes;
    for (var i = children.length - 1; i >=0; i--) {
        node.removeChild(children[i]);
    }
}

function addClassSelected(e) {
    e.currentTarget.parentNode.classList.toggle("selected");
}

function editDiscussion(element) {
    var parent = element.parentNode;
    var discussionEditDiv = document.createElement("div");
    var left = document.createElement("img");
    var right = document.createElement("div");
    var imageSrc = getUserImage();
    var username = getUserName();
    var textarea = document.createElement("textarea");
    var rightBottom = document.createElement("div");
    var post = document.createElement("a");
    var cancel = document.createElement("a");
    var text = document.createTextNode("as ");
    var select = document.createElement("select");
    var option1 = document.createElement("option");
    var option2 = document.createElement("option");
    
    parent.removeChild(element);
    discussionEditDiv.setAttribute("id", "discussion_edit_div");
    left.setAttribute("id", "user_image");
    left.setAttribute("src", imageSrc);
    post.innerHTML = "post ";
    post.onclick = postNewDiscussion;
    cancel.innerHTML = "cancel";
    cancel.onclick = cancelNewDiscussion;
    option1.setAttribute("value", username);
    option1.innerHTML = username;
    option2.setAttribute("value", "anonymous");
    option2.innerHTML = "anonymous";
    select.appendChild(option1);
    select.appendChild(option2);
    rightBottom.appendChild(post);
    rightBottom.appendChild(text);
    rightBottom.appendChild(select);
    rightBottom.appendChild(cancel);
    right.appendChild(textarea);
    right.appendChild(rightBottom);
    discussionEditDiv.appendChild(left);
    discussionEditDiv.appendChild(right);
    parent.appendChild(discussionEditDiv);
}

function postNewDiscussion() {
    var content = document.querySelector("#discussion_edit_div textarea").value;
    var discussionBody = document.querySelector("#discussion_body");
    var h5 = document.querySelector("#discussion_body h5");
    var discussionEditDiv = document.getElementById("discussion_edit_div");
    var select = document.querySelector("#discussion_body select");
    var discussionDiv = document.createElement("div");
    var left = document.createElement("img");
    var right = document.createElement("div");
    var rightTop = document.createElement("div");
    var rightMiddle = document.createElement("div");
    var rightBottom = document.createElement("div");
    var username = document.createElement("label");
    var opinion = document.createElement("div");
    var xhr = new XMLHttpRequest();
    var method = "post";
    var url = "http://172.18.157.157:8080/one-course-server/course-post-discussion.jsp";
    var param = "requestType=publish&courseCode=" + getCurrentUrlParam("courseCode") + "&postId=" + getPostId() + "&content=" + content;

    xhr.open(method, url, true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send(param);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            if (xhr.status >= 200 && xhr.status < 300 || xhr == 304) {
                var jsonResponse = JSON.parse(xhr.responseText);
                if (jsonResponse.code === 201) {
                    left.setAttribute("src", getUserImage());
                    username.setAttribute("id", "username");
                    username.innerHTML = select.options[select.selectedIndex].text;
                    rightTop.appendChild(username);
                    rightMiddle.setAttribute("id", "content");
                    rightMiddle.innerHTML = content;
                    right.setAttribute("id", "right");
                    right.appendChild(rightTop);
                    right.appendChild(rightMiddle);
                    right.appendChild(rightBottom);
                    discussionDiv.setAttribute("id", "discussion_leader");
                    discussionDiv.appendChild(left);
                    discussionDiv.appendChild(right);
                    discussionBody.insertBefore(discussionDiv, h5); 
                    
                    discussionBody.removeChild(discussionEditDiv);
                    var div = document.createElement("div");
                    div.innerHTML = "你的观点";
                    div.onclick = function() {
                       return editDiscussion(div);
                    }
                    discussionBody.appendChild(div);
                } else {
                    alert(jsonResponse.code + ": " + jsonResponse.msg);
                }
            }
        }
    };
}

function cancelNewDiscussion() {
    var discussionBody = document.querySelector("#discussion_body");
    var discussionEditDiv = document.getElementById("discussion_edit_div");
    discussionBody.removeChild(discussionEditDiv);
    var div = document.createElement("div");
    div.innerHTML = "你的观点";
    div.onclick = function() {
       return editDiscussion(div);
    }
    discussionBody.appendChild(div);
}

function getUserName() {
    return document.querySelector(".class-navbar-username").innerText;
}

function getUserImage() {
    return document.querySelector(".navbar-profile-picture").src;    
}

function getAllPost(element) {
    var postLi = document.createElement("li");
    var postDiv = document.createElement("div");
    var summary = document.createElement("div");
    var rightIcon = document.createElement("div");
    var data = document.createElement("div");

    summary.setAttribute("id", "post_summary");
    rightIcon.setAttribute("id", "rightIcon");
    data.setAttribute("style", "display:none");
    postDiv.appendChild(summary);
    postDiv.appendChild(rightIcon);
    postDiv.appendChild(data);
    postLi.appendChild(postDiv);
    // 获取所有post
        var xhr = new XMLHttpRequest();
        var param = "requestType=getall&courseCode=" + getCurrentUrlParam("courseCode") + "&title=&content=";
        var method = "post";
        var url = "http://172.18.157.157:8080/one-course-server/course-post.jsp";
        xhr.open(method, url, true);
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhr.send(param);
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4) {
                if (xhr.status >= 200 && xhr.status < 300 || xhr.status == 304) {
                    var jsonResponse = JSON.parse(xhr.responseText);
                    if (jsonResponse.code === 201) {
                        var posts = jsonResponse.content;
                        for (var i = 0; i < posts.length; i++) {
                            var newLi = postLi.cloneNode(true);
                            newLi.setAttribute("id", posts[i].postId);
                            newLi.childNodes[0].childNodes[2].innerHTML = JSON.stringify(posts[i]);
                            newLi.childNodes[0].childNodes[0].innerHTML = posts[i].title;
                            if (posts[i].userRole === "STUDENT") {
                                newLi.childNodes[0].childNodes[1].innerHTML = "s";
                                newLi.childNodes[0].childNodes[1].classList.add("green");
                            } else {
                                newLi.childNodes[0].childNodes[1].innerHTML = "i";
                                newLi.childNodes[0].childNodes[1].classList.add("yellow");
                            }
                            newLi.onclick = function(event) {
                                return onClickPostListItem(event);
                            }
                            if (i == 0) {
                                newLi.classList.add("post_active");
                                newLi.click();
                            }
                            element.appendChild(newLi);
                        }
                    } else {
                        alert(jsonResponse.code + ": " + jsonResponse.msg);
                    }
                }
            } 
        };
}

function getPostId() {
    var postList = document.getElementById("post_list");
    for (var i = 0; i < postList.childElementCount; i++) {
        if (postList.childNodes[i].classList.contains("post_active")) {
            return postList.childNodes[i].id;
        }
    }
}

function showMenu() {
    var ul = document.querySelector("ul.dropdown-menu");
    ul.style.display = "block";
}

function hideMenu() {
    var ul = document.querySelector("ul.dropdown-menu");
    ul.style.display = "none";
}

function getCookie(name) {
    var cookies = document.cookie.split("; ");
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i].split("=");
        if (cookie[0] === name) {
            return unescape(cookie[1]);
        }
    }
    return "unknow";
}

function getCurrentUrlParam(name) {
    var pattern = new RegExp("[?&]" + name + "\=([^&]+)", "g");
    var matcher = pattern.exec(window.location.href);
    var items = null;
    if (matcher != null) {
        try {
            items = decodeURIComponent(decodeURIComponent(matcher[1]));
        } catch (e) {
            try {
                items = decodeURIComponent(matcher[1]);
            } catch (e) {
                items = matcher[1];
            }
        }
    }
    return items;
}

function onClickPostListItem(event) {
    var postList = document.getElementById("post_list");
    for (var j = 0; j < postList.childElementCount; j++) {
        postList.childNodes[j].classList.remove("post_active");
    }
    event.currentTarget.classList.add("post_active");
    var center = document.getElementById("page_center");
    removeAllChildren(center);
    var post = JSON.parse(event.currentTarget.childNodes[0].childNodes[2].innerHTML);
    center.innerHTML = questionCardString + discussionCardString;
    setQuestionCard(post.title, post.content, post.authorName);

    var discussionBody = document.querySelector("#discussion_body");
    var h5 = document.querySelector("#discussion_body h5");
    var discussionEditDiv = document.getElementById("discussion_edit_div");
    var select = document.querySelector("#discussion_body select");
    var discussionDiv = document.createElement("div");
    var left = document.createElement("img");
    var right = document.createElement("div");
    var rightTop = document.createElement("div");
    var rightMiddle = document.createElement("div");
    var rightBottom = document.createElement("div");
    var username = document.createElement("label");
    left.setAttribute("src", getUserImage());
    username.setAttribute("id", "username");
    rightTop.appendChild(username);
    rightMiddle.setAttribute("id", "content");
    right.setAttribute("id", "right");
    right.appendChild(rightTop);
    right.appendChild(rightMiddle);
    right.appendChild(rightBottom);
    discussionDiv.setAttribute("id", "discussion_leader");
    discussionDiv.appendChild(left);
    discussionDiv.appendChild(right);

    var xhr = new XMLHttpRequest();
    var method = "post";
    var url = "http://172.18.157.157:8080/one-course-server/course-post-discussion.jsp";
    var param = "requestType=getall&courseCode=" + getCurrentUrlParam("courseCode") + "&postId=" + getPostId() + "&content=";
    xhr.open(method, url, true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send(param);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if (xhr.status >= 200 && xhr.status < 300 || xhr.status == 304) {
                var jsonResponse = JSON.parse(xhr.responseText);
                if (jsonResponse.code === 201) {
                    var discussions = jsonResponse.content;
                    for (var i = 0; i < discussions.length; i++) {
                        var newDiscussionDiv = discussionDiv.cloneNode(true);
                        newDiscussionDiv.childNodes[1].childNodes[0].childNodes[0].innerHTML = discussions[i].authorName;
                        newDiscussionDiv.childNodes[1].childNodes[1].innerHTML = discussions[i].content;
                        var discussionBody = document.querySelector("#discussion_body");
                        discussionBody.insertBefore(newDiscussionDiv, h5); 
                    }
                } 
            }
        }
    };
}

