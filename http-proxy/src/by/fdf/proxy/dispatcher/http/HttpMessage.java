package by.fdf.proxy.dispatcher.http;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Fursevich
 */
public abstract class HttpMessage {
    protected Map<String, String> headers = new LinkedHashMap<String, String>();
    protected ByteBuffer content;
    protected List<HttpChunk> chunks = new ArrayList<HttpChunk>();
    protected boolean chunked;

    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    void setContent(ByteBuffer content) {
        if (chunked) {
            throw new IllegalStateException("Response already has chunked content.");
        }

        this.content = content;
    }

    public ByteBuffer getContent() {
        return content;
    }

    void addChunk(HttpChunk chunk) {
        if (content != null) {
            throw new IllegalStateException("Response already has not chunked content.");
        }

        chunk.setMessage(this);
        chunks.add(chunk);
    }

    public List<HttpChunk> getChunks() {
        return chunks;
    }

    public boolean isChunked() {
        return chunked;
    }

    public void setChunked(boolean chunked) {
        this.chunked = chunked;
    }

    public void removeHeader(String header) {
        headers.remove(header);
    }
}
