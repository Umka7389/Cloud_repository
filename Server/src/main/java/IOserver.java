import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IOserver {

    private ConcurrentLinkedQueue<ClientHandler> queue;
    private boolean isRunning = true;



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

    public void transfer (String fileName, String fromPathname, String toPathname ) throws IOException {
        File from = new File(fromPathname + "/" + fileName);
        File to = new File(toPathname + "/"  + fileName);
        InputStream is = new FileInputStream(from);
        OutputStream os = new FileOutputStream(to);
        byte [] buffer = new byte[1024]; // 8Kb
        int count = 0;

        if (!to.exists()) to.createNewFile();
        while ((count = is.read(buffer)) != -1) {
            os.write(buffer, 0, count);
        }
        os.close();
        is.close();
    }

}
