import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer implements Runnable {

    private final ByteBuffer buffer = ByteBuffer.allocate(256);
    private static int cnt = 0;


    public void run() {
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(8888));
            System.out.println("Server started on port " + server.socket().getLocalPort());
            Selector selector = Selector.open();
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
            while (server.isOpen()) {
                selector.select();
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isAcceptable()) {
                        cnt++;
                        SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ, "user#" + cnt);
                        System.out.println("Accepted connection " + "user#" + cnt);
                    }
                    if (key.isReadable()) {
                        System.out.println("Handled read operation");
                        SocketChannel channel = ((SocketChannel) key.channel());
                        StringBuilder message = new StringBuilder();
                        int read;
                        while (true) {
                            read = channel.read(buffer);
                            if (read <= 0) {
                                if (read == -1) {
                                    System.out.println("Client " + key.attachment() + "disconnected");
                                    channel.close();
                                    break;
                                }
                                break;
                            }
                            buffer.flip();
                            while (buffer.hasRemaining()) {
                                message.append((char) buffer.get());
                            }
                            buffer.rewind();
                        }
                        String msg = key.attachment() + ": " + message.toString();
                        for (SelectionKey selectionKey : selector.keys()) {
                            if (key.isValid() && selectionKey.channel() instanceof SocketChannel) {
                                ((SocketChannel) selectionKey.channel())
                                        .write(ByteBuffer.wrap(msg.getBytes()));
                            }
                        }
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new NIOServer()).start();
    }
}
