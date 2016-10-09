package by.fdf.proxy.dispatcher.http;

import by.fdf.proxy.dispatcher.MessageEncoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

/**
 * @author Dmitry Fursevich
 */
public abstract class HttpMessageEncoder implements MessageEncoder {

    @Override
    public void encode(Object message, SocketChannel socketChannel) throws IOException {
        if (message instanceof HttpMessage) {
            HttpMessage httpMessage = (HttpMessage) message;

            StringBuilder sb = new StringBuilder();

            sb.append(encodeMessage(httpMessage));

            for (Map.Entry<String, String> header : httpMessage.getHeaders().entrySet()) {
                sb.append(header.getKey())
                        .append(":")
                        .append(header.getValue())
                        .append("\r\n");
            }

            sb.append("\r\n");

            socketChannel.write(ByteBuffer.wrap(sb.toString().getBytes("US-ASCII")));

            if (httpMessage.isChunked()) {
                for (HttpChunk httpChunk : httpMessage.getChunks()) {
                    encode(httpChunk, socketChannel);
                }
            } else if (httpMessage.getContent() != null) {
                socketChannel.write(httpMessage.getContent());
            }
        } else if (message instanceof HttpChunk) {
            HttpChunk httpChunk = (HttpChunk) message;
            
            String chunkSize = Integer.toHexString(httpChunk.getContent().capacity()) + "\r\n";            
            socketChannel.write(ByteBuffer.wrap(chunkSize.getBytes("US-ASCII")));
            socketChannel.write(httpChunk.getContent());
            socketChannel.write(ByteBuffer.wrap(new byte[] {'\r', '\n'}));
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + message.getClass().getName());
        }
    }

    protected abstract String encodeMessage(HttpMessage message);
}
