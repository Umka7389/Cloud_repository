import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IOserver {

    private ConcurrentLinkedQueue<ClientHandler> queue;
    private boolean isRunning = true;
    private final String serverStoragePath = "Server/ServerStorage";



    public void stop(){
        isRunning = false;
    }


    public IOserver(){
        try {
            queue = new ConcurrentLinkedQueue<ClientHandler>();
            ServerSocket serverSocket = new ServerSocket(8189);
            System.out.println("Server started on port: " + serverSocket.getLocalPort());
            while (isRunning){
                Socket socket = serverSocket.accept();
                ClientHandler client = new ClientHandler(socket, this);
                queue.add(client);
                new Thread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage (String message) throws IOException {
        for (ClientHandler client: queue) {
            client.sendMessage(message);
            
        }
    }

    public void kick(ClientHandler clientHandler) {
        queue.remove(clientHandler);

    }

    public static void main(String[] args) {
        new IOserver();
    }

    public void upload(String fileName) throws IOException {
        File clFile = new File("Client/ClientStorage/" + fileName);
        File srvFile = new File("Server/ServerStorage/" + fileName);
        InputStream is = new FileInputStream(clFile);
        OutputStream os = new FileOutputStream(srvFile);
        byte [] buffer = new byte[1024]; // 8Kb
        int count = 0;

        if (!srvFile.exists()) srvFile.createNewFile();
        while ((count = is.read(buffer)) != -1) {
            os.write(buffer, 0, count);
        }
        os.close();
        is.close();
    }
}
