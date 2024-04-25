const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

signUpButton.addEventListener('click', () => {
    console.log("Lakshit Randi");
	container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
	container.classList.remove("right-panel-active");
});

document.getElementById('signIn').addEventListener('click', async()=>{
	const email = document.getElementById('email').value;
	const password = document.getElementById('password').value;
	const response = await fetch('http://localhost:8080/api/user/userLogin',{
		method: 'POST',
		headers: {
			'Content-Type' : 'application/json'
		},
		body: JSON.stringify({'UserName' :'email', 'PassWord':'password'})
	});
	if(response.ok){
		const data = await response.json();
		document.cookie = data.token;
		//redirection
		window.location.href = 'http://localhost:8080/api/pages/patientDashboard';
	}else{
		const errorMessage = await response.text();
		console.error('Sign-in failed:', errorMessage);
	}
});

document.getElementById('signUp').addEventListener('click', async()=>{
	const name = document.getElementById('name').value;
	const email = document.getElementById('email').value;
	const password = document.getElementById('password').value;

	const response = await fetch('http://localhost:8080/api/user/addUser', {
		method: 'POST',
		headers:{
			'Content-Type' : 'application/json'
		},
		body: JSON.stringify({'Name':'name', 'UserName':'email', 'PassWord' :'password'})
	});
	if(response.ok){
		//handle sign up lakshit batayega
		const data = await response.json();
		document.cookie = data.token;
		//redirection
		window.location.href = 'http://localhost:8080/api/pages/patientDashboard';
	}else{
		const errorMessage = await response.text();
		console.error('Sign-up failed:', errorMessage);
	}
});