package by.fdf.proxy.dispatcher;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

/**
 * time out, exceptions, shutdown, locks
 *
 * @author Dmitry Fursevich
 */
public class DefaultDispatcher implements Dispatcher, Runnable {
    private static final Logger log = Logger.getLogger(DefaultDispatcher.class.getName());

    private String name;
    private Executor executor;
    private BufferFactory bufferFactory;
    private Selector selector;
    private boolean running;

    private final Object lock = new Object();

    public DefaultDispatcher(String name, Executor executor, BufferFactory bufferFactory) throws IOException {
        this.name = name;
        this.executor = executor;
        this.bufferFactory = bufferFactory;
    }

    @Override
    public Connection registerChannel(SocketChannel socketChannel, MessageEncoder encoder, MessageDecoder decoder, MessageHandler... handlers) throws IOException {
        log.fine("[" + name + "] Registering channel: " + socketChannel);

        socketChannel.configureBlocking(false);

        DefaultDispatcherHandler handler = new DefaultDispatcherHandler(this, bufferFactory, encoder, decoder, handlers);

        synchronized (lock) {
            selector.wakeup();

            SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ, handler);

            handler.setKey(key);
        }

        return handler;
    }

    void unregisterChannel(DefaultDispatcherHandler handler) {
        SelectionKey key = handler.getKey();

        log.fine("[" + name + "] Unregistering channel: " + key.channel());

        synchronized (lock) {
            key.cancel();

            selector.wakeup();
        }
    }

    public synchronized void start() throws IOException {
        if (!running) {
            log.fine("[" + name + "] Starting dispatcher");
            running = true;
            selector = Selector.open();
            new Thread(this).start();
        } else {
            throw new IllegalStateException("Dispatcher is already started.");
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                synchronized (lock) {
                    //suspend thread
                }
                selector.select();

                Set<SelectionKey> keys = selector.selectedKeys();

                for (SelectionKey key : keys) {
                    DefaultDispatcherHandler handler = (DefaultDispatcherHandler) key.attachment();

                    handleConnection(handler);
                }

                keys.clear();
            } catch (IOException e) {
                running = false;
            }

        }
        Set<SelectionKey> keys = selector.keys();

        for (SelectionKey key : keys) {
            DefaultDispatcherHandler handler = (DefaultDispatcherHandler) key.attachment();

            unregisterChannel(handler);
        }

        try {
            selector.close();
        } catch (IOException e) {
            //ignore
        }
    }

    void modifyInterestOps(DefaultDispatcherHandler channel, int ops) {
        log.fine("[" + name + "] Changing InterestOps on channel.");
        synchronized (lock) {
            if (channel.getKey().isValid()) {
                selector.wakeup();

                channel.getKey().interestOps(ops);
            }
        }
    }

//    void enableRead(DefaultDispatcherHandler channel) {
//        log.fine("[" + name + "] Changing InterestOps on channel");
//        synchronized (lock) {
//            if (channel.getKey().isValid()) {
//                channel.getKey().interestOps(SelectionKey.OP_READ);
//
//                selector.wakeup();
//            }
//        }
//    }
//
//    void enableWrite(DefaultDispatcherHandler channel) {
//        log.fine("[" + name + "] Changing InterestOps on channel");
//        synchronized (lock) {
//            channel.getKey().interestOps(channel.getKey().interestOps() | SelectionKey.OP_WRITE);
//
//            selector.wakeup();
//        }
//    }
//
//    void disableWrite(DefaultDispatcherHandler channel) {
//        synchronized (lock) {
//            if (channel.getKey().isValid()) {
//                channel.getKey().interestOps(SelectionKey.OP_READ);
//
//                selector.wakeup();
//            }
//        }
//    }

    String getName() {
        return name;
    }

    private void handleConnection(final DefaultDispatcherHandler handler) {
        handler.prepareToRun();

        handler.getKey().interestOps(0);

        executor.execute(handler);
    }
}
