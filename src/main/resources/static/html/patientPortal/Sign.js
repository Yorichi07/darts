const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

signUpButton.addEventListener('click', () => {
	container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
	container.classList.remove("right-panel-active");
});

document.getElementById('sinbut').addEventListener('click', async()=>{
	const email = document.getElementById('email').value;
	const password = document.getElementById('password').value;
	const response = await fetch('http://192.168.8.4:8080/api/user/userLogin',{
		method: 'POST',
		headers: {
			'Content-Type' : 'application/json'
		},
		body: JSON.stringify({'UserName' :email, 'PassWord':password})
	});
	if(response.ok){
		const data = await response.json();
		document.cookie = data.token;
		window.alert(data.msg)
		//redirection
		location.replace('http://192.168.8.4:8080/api/pages/patientDashboard');
	}else{
		const errorMessage = await response.text();
		console.error('Sign-in failed:', errorMessage);
	}
});

document.getElementById('sigbut').addEventListener('click', async()=>{
	const name = document.getElementById('nme').value;
	const email = document.getElementById('emil').value;
	const password = document.getElementById('pssword').value;

	const response = await fetch('http://192.168.8.4:8080/api/user/addUsers', {
		method: 'POST',
		headers:{
			'Content-Type' : 'application/json'
		},
		body: JSON.stringify({'Name':name, 'UserName':email, 'PassWord' :password})
	});
	if(response.ok){
		//handle sign up lakshit batayega
		const data = await response.json();
		document.cookie = data.token;
		//redirection
		location.replace('http://192.168.8.4:8080/api/pages/patientDashboard');
	}else{
		const errorMessage = await response.text();
		console.error('Sign-up failed:', errorMessage);
	}
});