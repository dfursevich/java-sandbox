package by.fdf.proxy.dispatcher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;

/**
 * @author Dmitry Fursevich
 */
public interface MessageEncoder {
    void encode(Object object, SocketChannel socketChannel) throws IOException;
}
