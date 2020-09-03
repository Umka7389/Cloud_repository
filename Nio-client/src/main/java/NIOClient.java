import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NIOClient implements Runnable {
    private final ByteBuffer buffer = ByteBuffer.allocate(256);


    public void run() {
        try {
            SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 8888));
            System.out.println("Client connected on port " + client.socket().getPort());
            while (true) {
                Scanner in = new Scanner(System.in);
                String msg = in.next();
                client.write(buffer.wrap(msg.getBytes()));
                buffer.clear();
                client.read(buffer);
                System.out.println(new String(buffer.array()).trim());
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new NIOClient()).start();
    }
}