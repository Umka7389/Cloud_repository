import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.ConcurrentLinkedDeque;

public class ChatHandler extends SimpleChannelInboundHandler<String> {

    private static final ConcurrentLinkedDeque<SocketChannel> channels = new ConcurrentLinkedDeque<>();
    private static int cnt = 0;
    private String userName = "user#";
    private Callback callback;

    public ChatHandler(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        cnt++;
        userName += cnt;
        System.out.printf("Client %s connected\n", userName);
        channels.add((SocketChannel) ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("Client %s disconnected\n",userName);
        channels.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.printf("Received message from %s: %s \n",userName, msg);
        channels.stream()
                //.filter(chanel -> !chanel.equals(ctx.channel()))
                .forEach(channel -> channel.writeAndFlush(String.format("[%s]: %s",userName,msg)));
        callback.call(msg);
    }
}
