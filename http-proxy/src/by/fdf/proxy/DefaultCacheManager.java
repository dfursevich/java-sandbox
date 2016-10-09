package by.fdf.proxy;

import by.fdf.proxy.dispatcher.Connection;
import by.fdf.proxy.dispatcher.MessageHandler;
import by.fdf.proxy.dispatcher.http.HttpRequest;
import by.fdf.proxy.dispatcher.http.HttpResponse;

import java.nio.ByteBuffer;

/**
 * @author Dmitry Fursevich
 */
public class DefaultCacheManager implements MessageHandler, CacheManager {
    @Override
    public void onMessageReceived(Object message, Connection connection) {
        //cache here
    }

    @Override
    public HttpResponse get(HttpRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void put(HttpRequest request, HttpResponse response) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
