package me.wani4ka.lands;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import me.wani4ka.lands.game.Game;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server {

    private static int getPort() {
        System.out.println(System.getenv("PORT"));
        if (System.getenv().containsKey("PORT"))
            return Integer.parseInt(System.getenv().get("PORT"));
        return 5000;
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(getPort()), 0);
        server.createContext("/", new MainPage());
        server.createContext("/storage", new Storage());
        server.createContext("/game", new Game());
        server.setExecutor(null);
        server.start();
        System.out.println(server.getAddress().getAddress().getHostAddress());
    }

    public static void sendFile(HttpExchange t, int code, String name) throws IOException {
        File f = new File(name);
        if (!f.exists() || f.isDirectory()) {
            if (!name.equals("error.html"))
                sendFile(t, 404, "error.html");
            return;
        }
        File canonicalFile = f.getCanonicalFile();
        FileInputStream input = new FileInputStream(canonicalFile);
        t.getResponseHeaders().set("Content-Type", Static.lookupMime(f.getCanonicalPath()));
        t.sendResponseHeaders(code, f.length());
        OutputStream output = t.getResponseBody();
        Static.copyStream(input, output);
        output.close();
        input.close();
    }

    static class MainPage implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if (!t.getRequestURI().getPath().equals("/")) {
                sendFile(t, 404, "error.html");
                return;
            }
            File f = new File("index.html");
            BufferedReader reader = new BufferedReader(new FileReader(f));
            StringBuilder s = new StringBuilder();
            while (reader.ready())
                s.append(reader.readLine());
            reader.close();
            t.sendResponseHeaders(200, s.toString().getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = t.getResponseBody();
            os.write(s.toString().getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

    static class Storage implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            File f = new File(t.getRequestURI().getPath().replaceFirst("/", ""));
            if (!f.exists() || f.isDirectory())
                sendFile(t, 404, "error.html");
            else sendFile(t, 200, f.getPath());
        }
    }
}
