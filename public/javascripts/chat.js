const inputField = document.getElementById("chat-input");
const outputArea = document.getElementById("chat-area");
const socketRoute = document.getElementById("ws-route").value;
const socket = new WebSocket(socketRoute.replace("http","ws"));
const username = document.getElementById("user").value;

inputField.onkeydown = (event) => {

  if(event.key === 'Enter') {
    socket.send(username + " : " + inputField.value);
    inputField.value = '';
  }
}

socket.onopen = (event) => socket.send("***"+ username+" has joined the chat***");

socket.onmessage = (event) => {
  outputArea.value += '\n' + event.data;
}
socket.onclose = (event) => socket.send("***"+ username+" has left the chat***");
socket.onerror = (event) => console.log("Error: " + event.data);


function load(country){
  console.log(country);
  if(country == "India"){
    console.log("India");
    document.getElementById("india").hidden = false;
  }
  else if(country == "USA"){
    console.log("USA");
    document.getElementById("usa").hidden = false;
  }
  else if(country == "UK"){
    console.log("UK");
    document.getElementById("uk").hidden = false;
  }
  else if(country == "Australia"){
    console.log("Australia");
    document.getElementById("aus").hidden = false;
  }
}