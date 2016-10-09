package by.fdf.proxy.dispatcher.http;

import by.fdf.proxy.dispatcher.MessageEncoder;

import java.nio.channels.SocketChannel;

/**
 * @author Dmitry Fursevich
 */
public class HttpRequestEncoder extends HttpMessageEncoder {
    @Override
    protected String encodeMessage(HttpMessage message) {
        HttpRequest httpRequest = (HttpRequest) message;
        return httpRequest.getMethod() + " " + httpRequest.getRequestUri() + " " + httpRequest.getVersion() + "\r\n";
    }
}
