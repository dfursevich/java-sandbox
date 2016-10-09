package by.fdf.proxy.dispatcher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * @author Dmitry Fursevich
 */
class DefaultDispatcherHandler implements Connection, Runnable {
    private static final Logger log = Logger.getLogger(DefaultDispatcherHandler.class.getName());

    private DefaultDispatcher dispatcher;
    private BufferFactory bufferFactory;
    private MessageEncoder encoder;
    private MessageDecoder decoder;
    private MessageHandler[] handlers;
    private Object attachment;
    private SelectionKey key;
    private Queue<Object[]> writingMessageQueue = new ConcurrentLinkedQueue<Object[]>();
    private ByteBuffer readingMessage;
    private boolean running = false;
    private final Object lock = new Object();

    DefaultDispatcherHandler(DefaultDispatcher dispatcher, BufferFactory bufferFactory, MessageEncoder encoder, MessageDecoder decoder, MessageHandler... handlers) {
        this.dispatcher = dispatcher;
        this.bufferFactory = bufferFactory;
        this.encoder = encoder;
        this.decoder = decoder;
        this.handlers = handlers;
        this.readingMessage = bufferFactory.newBuffer();//TODO: too ugly solution, server hangs if for example response body exceeds buffer.
    }

    public void writeMessage(Object message, ConnectionListener listener) {
        synchronized (lock) {
            writingMessageQueue.add(new Object[]{message, listener});
            if (!running) {
                dispatcher.modifyInterestOps(this, SelectionKey.OP_WRITE);
            }
        }
    }

    public void attach(Object attachment) {
        this.attachment = attachment;
    }

    public Object attachment() {
        return attachment;
    }

    @Override
    public void close() throws IOException {
        dispatcher.unregisterChannel(this);
        key.channel().close();
    }

    void setKey(SelectionKey key) {
        this.key = key;
    }

    SelectionKey getKey() {
        return key;
    }

    void prepareToRun() {
        //Synchronization in dispatcher thread to not allow to change interestOps while connection is running.
        synchronized (lock) {
            running = true;
        }
    }

    @Override
    public void run() {
        try {

            SocketChannel channel = (SocketChannel) key.channel();
            if (key.isWritable()) {
                Object[] message = null;
                while ((message = writingMessageQueue.poll()) != null) {
                    encoder.encode(message[0], channel);
                    ((ConnectionListener) message[1]).onComplete(this);
                }
            }

            if (key.isReadable()) {
                int read = channel.read(readingMessage);
                if (read > 0) {
                    readingMessage.flip();
                    Object message = null;
                    while ((message = decoder.decode(readingMessage)) != null) {
                        for (MessageHandler handler : handlers) {
                            handler.onMessageReceived(message, this);
                        }
                    }
                    readingMessage.compact();
                } else if (read == -1) {
                    close();
                } else if (read == 0) {
                    //temporary
                    ByteBuffer newReadingMessage = bufferFactory.newBuffer(readingMessage.capacity() * 2);
                    readingMessage.flip();
                    while(readingMessage.hasRemaining()) {
                        newReadingMessage.put(readingMessage.get());
                    }

                    readingMessage = newReadingMessage;
                    log.severe("Increase buffer size to:" + newReadingMessage.capacity());
                }
            }
        } catch (Exception e) {
            log.severe("Error during during handling message. Channel wil be closed. " + e.getMessage());
            try {
                close();
            } catch (IOException e1) {
                //
            }
        } finally {
            synchronized (lock) {
                running = false;
                int interestOps = writingMessageQueue.isEmpty() ? SelectionKey.OP_READ : SelectionKey.OP_WRITE;
                dispatcher.modifyInterestOps(this, interestOps);
            }
        }
    }

    @Override
    public String toString() {
        return "[" + dispatcher.getName() + "]" + key.channel() + ":";
    }
}
