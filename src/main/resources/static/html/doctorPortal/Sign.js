function login(){
	fetch("http://192.168.144.4:8080/api/user/userLogin",{
		method:"POST",
		headers:{
			"Content-Type":"application/json"
		},
		body:JSON.stringify({
			"UserName":document.querySelectorAll(".inp")[0].value,
			"PassWord":document.querySelectorAll(".inp")[1].value
		})
	}).then(resp=>{
		return resp.json()
	}).then(res=>{
		localStorage.setItem("token",res.token)
	}).catch(err=>{
		window.alert(err.msg)
	})
}