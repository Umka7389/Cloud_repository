import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class ConsoleReader implements Runnable {

    OutputStream os;
    Socket socket;

    public ConsoleReader(Socket socket) throws IOException, InterruptedException {
        this.socket = socket;
        while (socket == null || !socket.isConnected()) {
            Thread.sleep(200);
        }
        os = socket.getOutputStream();
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        while (true) {
            String msg = in.next();
            try {
                socket.getChannel().write(ByteBuffer.wrap(msg.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
