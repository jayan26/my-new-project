function validate(){
	var pd=document.loginform.pass.value;
	var cpd=document.loginform.confpass.value;
	var submit = document.registerform.registersubmit.value;
	
	if(submit=="register"){
		if(pd!=cpd){
			alert("password did not match");
			return false;
		}
	}
}

function validatePassChange(){
	var pass1 = document.forms["myform"]["field1"].value;
	var pass2 = document.forms["myform"]["field2"].value;
	
	if(pass1!=pass2){
		alert("password did not match");
		return false;
	}
}