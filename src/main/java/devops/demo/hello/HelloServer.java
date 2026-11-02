package devops.demo.hello;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HelloServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String html = """
                        <!DOCTYPE html>
                        <html>
                        <head><meta charset="utf-8"><title>DevOps Demo</title></head>
                        <body style="font-family: sans-serif; text-align: center; padding-top: 60px;">
                            <h1 style="color: #2a6ebb;">Hello, DevOps!</h1>
                            <p>Welcome to our first DevOps demo application.</p>
                            <p>My First CI/CD pipeline has been successfully built and deployed. It works!</p>
                            <hr style="width: 300px; margin: 40px auto;">
                            <p style="color: #888; font-size: 0.9em;">
                                &copy; DevOps Demo | Computer Science - Alexander Rodriguez
                            </p>
                        </body>
                        </html>
                        """;
                byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
        });

        server.start();
        System.out.println("Server started on http://localhost:8080");
    }
}