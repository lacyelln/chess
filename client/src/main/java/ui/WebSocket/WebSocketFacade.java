package ui.WebSocket;


import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import spark.Session;
import ui.ResponseException;
import websocket.commands.UserGameCommand;
import websocket.messages.Action;
import websocket.messages.ServerMessage;


//need to extend Endpoint for websocket to work properly
public class WebSocketFacade {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException, URISyntaxException {
        url = url.replace("http", "ws");
        URI socketURI = new URI(url + "/ws");
        this.notificationHandler = notificationHandler;
        }

        //Endpoint requires this method, but you don't have to do anything

    }

