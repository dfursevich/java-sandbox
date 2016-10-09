package by.fdf.proxy;

import by.fdf.proxy.dispatcher.Connection;
import by.fdf.proxy.dispatcher.Dispatcher;
import by.fdf.proxy.dispatcher.MessageHandler;
import by.fdf.proxy.dispatcher.http.HttpRequestEncoder;
import by.fdf.proxy.dispatcher.http.HttpResponseDecoder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author Dmitry Fursevich
 */
public class DefaultConnectionFactory implements ConnectionFactory {
    private Dispatcher outgoingDispatcher;
    private MessageHandler[] handlers;

    DefaultConnectionFactory(Dispatcher outgoingDispatcher, MessageHandler... handlers) {
        this.outgoingDispatcher = outgoingDispatcher;
        this.handlers = handlers;
    }

    @Override
    public Connection newConnection(String host, int port) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(host, port));

        Connection connection = outgoingDispatcher.registerChannel(socketChannel, new HttpRequestEncoder(), new HttpResponseDecoder(), handlers);

        ConnectionContext outgoingContext = new ConnectionContext();
        connection.attach(outgoingContext);
        return connection;
    }


}
