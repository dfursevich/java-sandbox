package by.fdf.proxy.dispatcher;

/**
 * @author Dmitry Fursevich
 */
public interface MessageHandler {
    void onMessageReceived(Object message, Connection connection) throws Exception;
}