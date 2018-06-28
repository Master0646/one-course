var submit = document.getElementById("submit");
submit.onclick = function(){
	var email = document.getElementById("email").value;
	var password = document.getElementById("password").value;
	var url = "http://172.18.157.244:55555/login.jsp";
	var method = "POST";
	var xmlHttpRequest = new XMLHttpRequest();

	xmlHttpRequest.open(method, url, true);
	xmlHttpRequest.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xmlHttpRequest.send("email="+email+"&pass="+password);

	xmlHttpRequest.onreadystatechange = function(){
		if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200){
			var returnData = JSON.parse(xmlHttpRequest.responseText);
			console.log(returnData.msg);      
        	if(returnData.msg == "Ok"){  
            	alert("登录成功！");
            	document.cookie = "email"+email;
            	window.location.href='../user/user.html';      
        	}else{      
            	alert("登录失败！");      
        	}
		}     
        	
    }
}