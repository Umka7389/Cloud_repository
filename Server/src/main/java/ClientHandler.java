import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final DataOutputStream os;
    private final DataInputStream is;
    private final IOserver server;
    private static int counter = 0;
    private final String name = "user#" + counter;
    private final Socket socket;

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
                server.broadcastMessage(message);
                if(message.equals("quit")){
                    server.kick(this);
                    os.close();
                    is.close();
                    socket.close();
                    System.out.println(name + " disconnected");
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
