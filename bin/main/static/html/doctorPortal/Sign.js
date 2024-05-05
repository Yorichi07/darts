function login(){
	fetch("http://192.168.8.4:8080/api/hospital/docLogin",{
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
		if(Object.keys(res).includes("token")){
			window.alert("login sucessfull")
			localStorage.setItem("token",res.token)
			location.replace(`http://192.168.8.4:8080/api/pages/docQrCode`)
		}
		window.alert(res.msg)
	}).catch(err=>{
		console.log(err)
		window.alert(err.msg)
	})
}