package by.fdf.proxy.dispatcher.http;

import by.fdf.proxy.dispatcher.MessageEncoder;

import java.nio.channels.SocketChannel;

/**
 * @author Dmitry Fursevich
 */
public class HttpResponseEncoder extends HttpMessageEncoder {

    @Override
    protected String encodeMessage(HttpMessage message) {
        HttpResponse httpResponse = (HttpResponse) message;

        return httpResponse.getStatusCode() + " " + httpResponse.getExtensionCode() + " " + httpResponse.getReasonPhrase() + "\r\n";
    }
}
