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
    publishToSpring('/remove', id);
  }
}

let closeButtons = document.getElementsByClassName('close');
for(let i = 0; i < closeButtons.length; i++) {
  let closeButton = closeButtons[i];
    closeButton.onclick = function() {
      publishToSpring('/remove', closeButton.parentElement.getAttribute('id'));
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
document.getElementById('publish').onclick = () => {
  let message = {
    'text': document.getElementById("message").value,
    'channelName': channelName,
    'clientID': realtime.auth.clientId
  }
  publishToSpring('/create', JSON.stringify(message));
}

/* Mark a list element as complete/incomplete */
var list = document.querySelector('ul');
list.addEventListener('click', function(ev) {
  if (ev.target.tagName === 'LI') {
    if (document.getElementById(ev.target.id).classList.contains("checked")) {
      publishToSpring('/uncomplete', ev.target.id);
    } else {
      publishToSpring('/complete', ev.target.id);
    }
  }
}, false);

function publishToSpring(url, data) {
  var xhr = new XMLHttpRequest();
  xhr.open("POST", url, true);
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.send(data);
}