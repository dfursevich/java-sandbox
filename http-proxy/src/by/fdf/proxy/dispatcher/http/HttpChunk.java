package by.fdf.proxy.dispatcher.http;

import java.nio.ByteBuffer;

/**
 * @author Dmitry Fursevich
 */
public class HttpChunk  {
    private boolean last;
    private ByteBuffer content;
    private HttpMessage message;

    public HttpChunk() {
    }

    public boolean isLast() {
        return last;
    }

    void setLast(boolean last) {
        this.last = last;
    }

    ByteBuffer getContent() {
        return content;
    }

    void setContent(ByteBuffer content) {
        this.content = content;
    }

    public HttpMessage getMessage() {
        return message;
    }

    void setMessage(HttpMessage message) {
        this.message = message;
    }
}
