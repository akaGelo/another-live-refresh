var loc = window.location, new_uri;
if (loc.protocol === "https:") {
    new_uri = "wss:";
} else {
    new_uri = "ws:";
}
new_uri += "//" + loc.host + "/alr/refresh";

var socket = new WebSocket(new_uri );



socket.onopen = function() {
	console.log("another-live-refresh enabled");
};

socket.onmessage = function(event) {
	console.log("another-live-refresh refresh");
	window.location.href = window.location.href;
}

socket.onclose = function(event) {
	console.log("another-live-refresh disabled");
};

