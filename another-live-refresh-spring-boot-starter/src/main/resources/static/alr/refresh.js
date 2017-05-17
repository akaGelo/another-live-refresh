var loc = window.location, new_uri,help_uri;
if (loc.protocol === "https:") {
    new_uri = "wss:";
} else {
    new_uri = "ws:";
}
new_uri += "//" + loc.host + "/alr/refresh";
help_uri = loc.protocol+"//" + loc.host + "/alr/help.html";


var socket = new WebSocket(new_uri );



socket.onopen = function() {
	console.log("another-live-refresh enabled. "  + help_uri);
};

socket.onmessage = function(event) {
	console.log("another-live-refresh refresh. "  + help_uri);
	window.location.reload(true);
}

socket.onclose = function(event) {
	console.log("another-live-refresh disabled. "  + help_uri);
};

