import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketTest {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8888);
        DataInputStream is = new DataInputStream(socket.getInputStream());
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        os.writeUTF("Hello");
        os.flush();
        byte [] buffer = new byte[256];
        int cnt = is.read(buffer);
        String resp = new String(buffer, 0, cnt, StandardCharsets.UTF_8);
        System.out.println(resp);
        os.close();
        is.close();
    }
}
