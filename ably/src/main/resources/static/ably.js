/* Set up a Realtime client that authenticates with the local web server auth endpoint */
let realtime = new Ably.Realtime({ authUrl: '/auth' });
let channel = realtime.channels.get(channelName);

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

/* Subscribe to changes from an Ably channel */
channel.subscribe(function(message) {
  if (message.name === 'add') {
    addNewTODO(message.data.id, message.data.text, message.data.clientID);
  } else if (message.name === 'remove') {
    removeTODO(message.data);
  } else if (message.name === 'complete') {
     completeTODO(message.data);
  } else if (message.name === 'incomplete') {
     incompleteTODO(message.data);
   }
});
