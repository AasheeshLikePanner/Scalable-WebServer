package ThreadPool;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ExecutorService threadPool;
    
    private ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    public String getCachedContent(String filePath) throws IOException {
        if (cache.containsKey(filePath)) {
            return cache.get(filePath);
        } else {
            String htmlContent = new String(Files.readAllBytes(Paths.get(filePath)));
            cache.put(filePath, htmlContent);
            return htmlContent;
        }
    }

    public Server(int poolSize) {
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public void handleClient(Socket clientSocket) {
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
    }

    public static void main(String[] args) {
        int port = 8080;
        int poolSize = 10;
        Server server = new Server(poolSize);

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(70000);
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                server.threadPool.execute(() -> server.handleClient(clientSocket));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            server.threadPool.shutdown();
        }
    }
}
