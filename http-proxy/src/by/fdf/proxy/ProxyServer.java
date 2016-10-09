package by.fdf.proxy;

import by.fdf.proxy.dispatcher.Connection;
import by.fdf.proxy.dispatcher.DefaultBufferFactory;
import by.fdf.proxy.dispatcher.DefaultDispatcher;
import by.fdf.proxy.dispatcher.Dispatcher;
import by.fdf.proxy.dispatcher.http.HttpRequestDecoder;
import by.fdf.proxy.dispatcher.http.HttpResponseEncoder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Dmitry Fursevich
 */
public class ProxyServer implements Runnable {
    private static final Logger log = Logger.getLogger(ProxyServer.class.getName());

    private int port;
    private boolean running = false;
    private Dispatcher incomingDispatcher;
    private Dispatcher outgoingDispatcher;
    private ServerSocketChannel serverChannel;
    private HttpRequestHandler httpRequestHandler;

    public ProxyServer(int port) throws IOException {
        this.port = port;

        incomingDispatcher = new DefaultDispatcher("BrowserToProxy", Executors.newCachedThreadPool(), new DefaultBufferFactory(200000));
        outgoingDispatcher = new DefaultDispatcher("ProxyToServer", Executors.newCachedThreadPool(), new DefaultBufferFactory(200000));
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler();
        DefaultCacheManager cacheManager = new DefaultCacheManager();
        httpRequestHandler = new HttpRequestHandler(new DefaultConnectionFactory(outgoingDispatcher, httpResponseHandler, cacheManager), cacheManager);
    }

    public synchronized void start() throws IOException {
        log.info("Starting proxy server on port:" + port);
        if (!running) {
            outgoingDispatcher.start();
            incomingDispatcher.start();

            serverChannel = ServerSocketChannel.open();
            serverChannel.socket().bind(new InetSocketAddress(port));
            running = true;
            new Thread(this).start();
        } else {
            throw new IllegalStateException("Server is already running.");
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                SocketChannel channel = serverChannel.accept();

                if (channel == null) {
                    continue;
                }

                Connection connection = incomingDispatcher.registerChannel(channel, new HttpResponseEncoder(), new HttpRequestDecoder(), httpRequestHandler);
                connection.attach(new ConnectionContext());
            } catch (ClosedByInterruptException e) {
                log.fine("ServerSocketChannel closed by interrupt: " + e);
                return;

            } catch (ClosedChannelException e) {
                log.log(Level.SEVERE,
                        "Exiting, serverSocketChannel is closed: " + e, e);
                return;

            } catch (Throwable t) {
                log.log(Level.SEVERE,
                        "Exiting, Unexpected Throwable doing accept: " + t, t);

                try {
                    serverChannel.close();
                } catch (Throwable e1) { /* nothing */ }

                running = false;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ProxyServer proxyServer = new ProxyServer(1234);
        proxyServer.start();
    }
}
