package by.fdf.proxy.dispatcher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Dmitry Fursevich
 */
public interface MessageDecoder {
    Object decode(ByteBuffer socketChannel) throws IOException;
}
