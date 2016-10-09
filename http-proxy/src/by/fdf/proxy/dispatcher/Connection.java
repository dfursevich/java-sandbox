package by.fdf.proxy.dispatcher;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Dmitry Fursevich
 */
public interface Connection {
    void writeMessage(Object message, ConnectionListener listener);

    void attach(Object attachment);

    Object attachment();

    void close() throws IOException;
}
