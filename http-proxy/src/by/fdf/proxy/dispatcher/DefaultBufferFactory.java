package by.fdf.proxy.dispatcher;

import by.fdf.proxy.dispatcher.BufferFactory;

import java.nio.ByteBuffer;

/**
 * @author Dmitry Fursevich
 */
public class DefaultBufferFactory implements BufferFactory {
    private int capacity;

    public DefaultBufferFactory(int capacity) {
        this.capacity = capacity;
    }

    public ByteBuffer newBuffer() {
        return (ByteBuffer.allocate(capacity));
    }

    @Override
    public ByteBuffer newBuffer(int capacity) {
        return (ByteBuffer.allocate(capacity));
    }
}
