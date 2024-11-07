package MultiThreaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;

public class Client {

    public Runnable getRunnable() throws UnknownHostException, IOException {
        return new Runnable(){
            @Override
            public void run() {
                try {
                    int port = 8080;
                    String baseURL = "http://localhost:" + port;
    
                    String indexURL = baseURL + "/index.html";
                    String indexContent = makeRequest(indexURL);
    
                    System.out.println("Received index.html:");
                    System.out.println(indexContent);
    
                    String cssURL = baseURL + "/style.css";
                    String cssContent = makeRequest(cssURL);
    
                    System.out.println("\nReceived style.css:");
                    System.out.println(cssContent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    } 

    
    private String makeRequest(String fileURL) throws IOException {
        try {
            URI uri = new URI(fileURL);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }


    public static void main(String[] args) {
        Client client = new Client();
        for(int i=0; i<100; i++){
            try{
                Thread thread = new Thread(client.getRunnable());
                thread.start();
            }catch(Exception ex){
                return;
            }
        }
        return;
    }
}
