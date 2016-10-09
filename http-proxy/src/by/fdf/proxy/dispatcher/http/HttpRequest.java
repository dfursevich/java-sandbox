package by.fdf.proxy.dispatcher.http;

import java.nio.ByteBuffer;

/**
 * @author Dmitry Fursevich
 */
public class HttpRequest extends HttpMessage {
    private String method;
    private String requestUri;
    private String version;

    public HttpRequest(String method, String requestUri, String version) {
        this.method = method;
        this.requestUri = requestUri;
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getVersion() {
        return version;
    }
}
