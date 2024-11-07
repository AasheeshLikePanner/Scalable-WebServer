package SingleThreaded;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Server {

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

    public void run() throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(30000);

        String htmlFilePath = "./utils/index.html";
        String cssFilePath = "./utils/style.css";

        while (true) {
            System.out.println("Server is listening on port " + port);
            Socket acceptedConnection = serverSocket.accept();
            System.out.println("Connected to " + acceptedConnection.getRemoteSocketAddress());

            BufferedReader reader = new BufferedReader(new InputStreamReader(acceptedConnection.getInputStream()));
            String line = reader.readLine();
            if (line != null && line.contains("GET")) {
                String[] requestParts = line.split(" ");
                String requestedFile = requestParts[1]; 

                PrintWriter toClient = new PrintWriter(acceptedConnection.getOutputStream(), true);

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

                acceptedConnection.close();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
