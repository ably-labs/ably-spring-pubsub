/* Create a new list item when clicking on the "Add" button */
function addNewTODO(id, todoText, username) {
  var ul = document.getElementById("todo-list");
  var li = document.createElement("li");

  var t = document.createTextNode(todoText + ' - Added by: ' + username);
  li.appendChild(t);
  li.id = id;
  ul.appendChild(li);

  /* Create a close icon */
  var span = document.createElement("SPAN");
  var txt = document.createTextNode("\u00D7");
  span.className = "close";
  span.appendChild(txt);
  li.appendChild(span);

  span.onclick = () => {
    publishToSpring('DELETE', '/api/' + id);
  }
}

let closeButtons = document.getElementsByClassName('close');
for(let i = 0; i < closeButtons.length; i++) {
  let closeButton = closeButtons[i];
    closeButton.onclick = function() {
      publishToSpring('DELETE', '/api/' + closeButton.parentElement.getAttribute('id'));
    }
}

function removeTODO(id) {
  var div = document.getElementById(id);
  div.style.display = "none";
}

function completeTODO(id) {
  document.getElementById(id).classList.add("checked");
}

function incompleteTODO(id) {
  document.getElementById(id).classList.remove("checked");
}

/* Send new TODO to server */
document.getElementById('publish').onclick = sendMessageToSpring;

const node = document.getElementById("message");
node.addEventListener("keyup", ({key}) => {
    if (key === "Enter") {
        sendMessageToSpring();
    }
});

function sendMessageToSpring() {
  let username = document.cookie.indexOf('username');
  let todoText = document.getElementById("message");
  let message = {
    'text': todoText.value,
    'username': username
  }
  todoText.value = "";
  publishToSpring('POST', '/api', JSON.stringify(message));
}

/* Mark a list element as complete/incomplete */
var list = document.querySelector('ul');
list.addEventListener('click',
  (ev) => {
  if (ev.target.tagName === 'LI') {
    clickEvent(ev.target.id);
  }
  }, false);

function clickEvent(id) {
  if (document.getElementById(id).classList.contains("checked")) {
    publishToSpring('PUT', '/api/' + id + '/uncomplete');
  } else {
    publishToSpring('PUT', '/api/' + id + '/complete');
  }
}

function publishToSpring(action, url, data) {
  var xhr = new XMLHttpRequest();
  xhr.open(action, url, true);
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.send(data);
}

getCurrentTodos();

function getCurrentTodos() {
  let request = new XMLHttpRequest();
  request.open('GET', '/api');
  request.responseType = 'json';
  request.onload = function() {
    console.log(request.response);
  };
  request.send();
}