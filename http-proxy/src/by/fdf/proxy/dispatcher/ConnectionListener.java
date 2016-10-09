package by.fdf.proxy.dispatcher;

import by.fdf.proxy.dispatcher.Connection;

import java.io.IOException;

/**
 * @author Dmitry Fursevich
 */
public interface ConnectionListener {
    void onComplete(Connection connection);
}
