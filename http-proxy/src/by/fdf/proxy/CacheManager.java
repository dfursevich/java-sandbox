package by.fdf.proxy;

import by.fdf.proxy.dispatcher.http.HttpRequest;
import by.fdf.proxy.dispatcher.http.HttpResponse;

/**
 * @author Dmitry Fursevich
 */
public interface CacheManager {
    HttpResponse get(HttpRequest request);

    void put(HttpRequest request, HttpResponse response);
}
