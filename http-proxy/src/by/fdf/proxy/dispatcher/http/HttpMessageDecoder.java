package by.fdf.proxy.dispatcher.http;

import by.fdf.proxy.dispatcher.MessageDecoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Dmitry Fursevich
 */
public abstract class HttpMessageDecoder implements MessageDecoder {
    private static final Logger log = Logger.getLogger(HttpResponseDecoder.class.getName());

    //0 - decoding status line,
    //1 - decoding headers,
    //2 - decoding message body
    //3 - decoding chunked message body
    //4 - decoding chunk body
    //5 - final state
    protected int state = 0;
    protected HttpMessage currentMessage;
    protected HttpChunk currentChunk;
    protected int currentChunkLength = -1;

    public Object decode(ByteBuffer message) throws IOException {
        if (state == 5) {
            reset();
        }

        if (state < 2) {
            String line = null;
            while ((line = readLine(message)) != null) {
                if (line.isEmpty()) {
                    state = 2;
                    break;
                }
                switch (state) {
                    case 0:
                        decodeMessage(line);
                        state = 1;
                        break;
                    case 1:
                        decodeHeader(line);
                        break;
                }
            }
        }

        if (state == 2) {
            String contentLengthStr = currentMessage.getHeader("Content-Length");
            String transferEncoding = currentMessage.getHeader("Transfer-Encoding");
            if (contentLengthStr != null) {
                int contentLength = Integer.parseInt(contentLengthStr); //todo: restriction
                ByteBuffer body = read(message, contentLength, 0);
                if (body != null) {
                    state = 5;
                    currentMessage.setContent(body);
                    return currentMessage;
                }
            } else if (transferEncoding != null && "chunked".equals(transferEncoding)) {
                state = 3;
                currentMessage.setChunked(true);
                return currentMessage;
            } else {
                state = 5; //no body
                return currentMessage;
            }
        }

        if (state == 3) {
            String line = readLine(message);
            if (line != null) {

                currentChunk = new HttpChunk();
                currentMessage.addChunk(currentChunk);
                currentChunkLength = Integer.parseInt(line, 16);

                ByteBuffer chunkData = read(message, currentChunkLength, 2);
                if (chunkData != null) {
                    state = 3;
                    readLine(message); // read CRLF
                    currentChunk.setContent(chunkData);
                    if (currentChunkLength == 0) {
                        state = 5;
                        currentChunk.setLast(true);
                    }
                    return currentChunk;
                } else {
                    state = 4;
                    return null;
                }
            }
        }

        if (state == 4) {
            ByteBuffer chunkData = read(message, currentChunkLength, 2);
            if (chunkData != null) {
                state = 3;
                readLine(message); // read CRLF
                currentChunk.setContent(chunkData);
                return currentChunk;
            }
        }

        //todo:decode trailer

        return null;
    }

    private void reset() {
        state = 0;
        currentChunk = null;
        currentMessage = null;
        currentChunkLength = -1;
    }

    protected abstract void decodeMessage(String firstLine);

    protected  void decodeHeader(String header) {
        if (log.isLoggable(Level.FINEST)) {
            log.finest("Response header=" + header);
        }

        int sepIndex = header.indexOf(':');
        currentMessage.addHeader(header.substring(0, sepIndex), header.substring(sepIndex + 1).trim());
    }

    private ByteBuffer read(ByteBuffer message, int length, int sepLength) {
        ByteBuffer data = null;
        if (message.remaining() >= length + sepLength) { //2 CRLF after chunk
            data = ByteBuffer.allocate(length);
            for (int i = 0; i < length; i++) {
                data.put(message.get());
            }
            data.flip();
        } else {
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Not complete body in response.");
            }
        }

        return data;
    }

    private String readLine(ByteBuffer message) {
        String line = "";
        boolean isLineRead = false;
        message.mark();
        while (message.hasRemaining()) {
            byte cur = message.get();
            if (cur == '\r') {
                byte next = message.get();
                if (next == '\n') {
                    isLineRead = true;
                    break;
                } else {
                    line += cur;
                    line += next;
                }
            }

            line += (char) cur;
        }

        //If there is no line separator found
        if (!isLineRead) {
            if (log.isLoggable(Level.FINEST)) {
                log.fine("Not complete line in response.");
            }

            message.reset();
            line = null;
        }

        return line;
    }
}
