<!DOCTYPE html>
<html lang="en">
<head>
    <title>display hand</title>
    <meta charset="utf-8" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet"  href="/css/style.css"/>
</head>

<body>
    <div class=" bg-secondary lobbyGrid">
        <div class="lbyuserContainer">
            <#list users as id, user>
                <div class="user">${user.username}</div>
            </#list>
        </div> 
    </div>

    <script type="application/javascript" src="/scripts/jquery-3.3.1.min.js"></script>
    <script src="/scripts/lobby.js"></script>
    <script>
        var wsHost = window.location.hostname + (window.location.port != "" ? ":" + window.location.port : "");
        var ws = new WebSocket("ws://" + wsHost + "/GroupGames_Web_exploded/playWS");

        ws.onmessage = function (event) { //Receive from websocket
            console.log(event.data);

            var updateEvent = JSON.parse(event.data);

            if (updateEvent.method == "refresh") {
                location.reload();
            } else {
                updateUsers(updateEvent.users, false);
            }
        };

        ws.onopen = function(event){
            ws.send(JSON.stringify({
                "player_id": "${uid}",
                "gamecode" : "${gamecode}"
            }));
        };
    </script>
</body>
</html>