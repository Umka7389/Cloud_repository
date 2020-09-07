package serialization;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileHandler extends SimpleChannelInboundHandler<AbstractMessage> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, AbstractMessage msg) throws Exception {
        if (msg instanceof FileMessage) {
            FileMessage message = (FileMessage) msg;
            if (!Files.exists(Paths.get("Netty/" + message.getName()))) {
                Files.createFile(Paths.get("Netty/" + message.getName()));
                Files.write(
                        Paths.get("Netty/" + message.getName()),
                        message.getData(),
                        StandardOpenOption.APPEND);
            }
        }
    }
}
