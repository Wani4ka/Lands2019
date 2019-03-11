package me.wani4ka.lands.game;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.wani4ka.lands.Server;

import java.io.IOException;

public class Game implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        if (!"POST".equals(t.getRequestMethod())) {
            t.getResponseHeaders().set("Location", "/");
            t.sendResponseHeaders(301, 0);
            t.getResponseBody().close();
            return;
        }
        Server.sendFile(t, 200, "game.html");
    }
}
