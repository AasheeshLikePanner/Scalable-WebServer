package MultiThreaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Consumer;

public class Server {

    private static Server server;
    
    private HashMap<String, String> cache = new HashMap<>();

    public String getCachedContent(String filePath) throws IOException {
        if (cache.containsKey(filePath)) {
            return cache.get(filePath);
        } else {
            String htmlContent = new String(Files.readAllBytes(Paths.get(filePath)));
            cache.put(filePath, htmlContent);
            return htmlContent;
        }
    }

    private Server() {}

    public static Server getServer() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public Consumer<Socket> getConsumer(){
        return (clientSocket) -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);

                String line = reader.readLine();
                if (line != null && line.contains("GET")) {
                    String[] requestParts = line.split(" ");
                    String requestedFile = requestParts[1];

                    String htmlFilePath = "./utils/index.html";
                    String cssFilePath = "./utils/style.css";

                    if (requestedFile.equals("/index.html")) {
                        String content = getCachedContent(htmlFilePath);
                        toClient.println("HTTP/1.1 200 OK");
                        toClient.println("Content-Type: text/html");
                        toClient.println(""); 
                        toClient.println(content);
                    } else if (requestedFile.equals("/style.css")) {
                        String content = getCachedContent(cssFilePath);
                        toClient.println("HTTP/1.1 200 OK");
                        toClient.println("Content-Type: text/css");
                        toClient.println("");
                        toClient.println(content);
                    } else {
                        toClient.println("HTTP/1.1 404 Not Found");
                        toClient.println(""); 
                        toClient.println("<h1>404 Not Found</h1>");
                    }
                }

                clientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    public void run() throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(100000);
        System.out.println("Server is listening on port " + port);
        while (true) {
            Socket acceptedConnection = serverSocket.accept();
            Thread thread = new Thread(() -> getServer().getConsumer().accept(acceptedConnection));
            thread.start();
        }
    }

    public static void main(String[] args) {
        try {
            getServer().run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
