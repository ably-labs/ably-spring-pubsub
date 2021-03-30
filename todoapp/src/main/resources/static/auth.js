/* Change the display based on logged in status */
let loggedIn = document.cookie.indexOf('username') >= 0;
document.getElementById('panel-anonymous').
  setAttribute('style', "display: " + (loggedIn ? 'none' : 'block'));
document.getElementById('panel-anonymous-msg').
  setAttribute('style', "display: " + (loggedIn ? 'none' : 'block'));

document.getElementById('panel-logged-in').
  setAttribute('style', "display: " + (loggedIn ? 'block' : 'none'));
document.getElementById('panel-logged-in-msg').
  setAttribute('style', "display: " + (loggedIn ? 'block' : 'none'));