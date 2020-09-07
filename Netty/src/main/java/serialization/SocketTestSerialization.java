package serialization;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketTestSerialization {

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 8888)) {
            ObjectEncoderOutputStream os = new ObjectEncoderOutputStream(socket.getOutputStream());
            ObjectDecoderInputStream is = new ObjectDecoderInputStream(socket.getInputStream());
            os.writeObject(FileMessage.builder()
                    .name("file.txt")
                    .data("qeasd".getBytes())
                    .build());
            os.flush();
            os.close();
        }
    }
}
