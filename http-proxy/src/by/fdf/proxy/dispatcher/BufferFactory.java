package by.fdf.proxy.dispatcher;

import java.nio.ByteBuffer;

/**
 * @author Dmitry Fursevich
 */
public interface BufferFactory {
    ByteBuffer newBuffer();

    ByteBuffer newBuffer(int capacity);
}
