import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final DataOutputStream os;
    private final DataInputStream is;
    private final IOserver server;
    private static int counter = 0;
    private final String name = "user#" + counter;
    private final Socket socket;
    private final String clientStoragePath = "Client/ClientStorage";
    private final String serverStoragePath = "Server/ServerStorage";

    public ClientHandler(Socket socket, IOserver iOserver) throws IOException {
        server = iOserver;
        this.socket = socket;
        counter++;
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
        System.out.println("Client handled, ip = " + socket.getInetAddress() );
        System.out.println("Nick: " + name);


    }

    public void sendMessage(String message) throws IOException {
        os.writeUTF(message);
        os.flush();
    }

    public void run() {
        while (true){
            try {
                String message = is.readUTF();
                System.out.println("message from" + name + ": " + message);
                if (message.equals("quit")){
                    server.kick(this);
                    os.close();
                    is.close();
                    socket.close();
                    System.out.println(name + " disconnected");
                    break;
                } else {
                    String[] arr = message.split("±");
                    if (arr[0].equals("/upload")){
                        server.transfer(arr[1],clientStoragePath, serverStoragePath);
                    } else if (arr[0].equals("/download")){
                        server.transfer(arr[1], serverStoragePath,clientStoragePath);
                    } else sendMessage("Unknown command");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
