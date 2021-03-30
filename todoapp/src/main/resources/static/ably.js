/* Set up a Realtime client that authenticates with the local web server auth endpoint */
let realtime = new Ably.Realtime({ authUrl: '/auth' });
let channelName = 'default';
let channel = realtime.channels.get(channelName);

/* Subscribe to changes from an Ably channel */
channel.subscribe(function(message) {
  if (message.name === 'add') {
    addNewTODO(message.data.id, message.data.text, message.data.username);
  } else if (message.name === 'remove') {
    removeTODO(message.data);
  } else if (message.name === 'complete') {
    completeTODO(message.data);
  } else if (message.name === 'incomplete') {
    incompleteTODO(message.data);
  }
});
