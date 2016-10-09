package by.fdf.proxy.dispatcher;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * @author Dmitry Fursevich
 */
public interface Dispatcher {
    Connection registerChannel(SocketChannel socketChannel, MessageEncoder encoder, MessageDecoder decoder, MessageHandler... handlers) throws IOException;

    void start() throws IOException;
}
