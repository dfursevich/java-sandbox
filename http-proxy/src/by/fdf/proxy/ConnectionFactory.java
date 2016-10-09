package by.fdf.proxy;

import by.fdf.proxy.dispatcher.Connection;

import java.io.IOException;

/**
 * @author Dmitry Fursevich
 */
public interface ConnectionFactory {
    Connection newConnection(String host, int port) throws IOException;
}
