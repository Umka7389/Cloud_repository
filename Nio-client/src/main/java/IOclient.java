import com.sun.org.apache.xpath.internal.operations.String;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.Buffer;

public class IOclient implements Runnable {

    static Socket socket;

    public IOclient() throws IOException {
    }


    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[256];
            while (true) {
                int cnt = is.read(buffer);
                System.out.println(new java.lang.String(buffer, 0, cnt));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        socket = new Socket("localhost", 8888);
        new Thread(new IOclient()).start();
        new Thread(new ConsoleReader(socket)).start();
    }
}
