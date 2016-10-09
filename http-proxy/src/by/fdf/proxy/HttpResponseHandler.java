package by.fdf.proxy;

import by.fdf.proxy.dispatcher.Connection;
import by.fdf.proxy.dispatcher.ConnectionListener;
import by.fdf.proxy.dispatcher.MessageHandler;
import by.fdf.proxy.dispatcher.http.HttpChunk;
import by.fdf.proxy.dispatcher.http.HttpResponse;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Dmitry Fursevich
 */
public class HttpResponseHandler implements MessageHandler {
    private static final Logger log = Logger.getLogger(HttpResponseHandler.class.getName());

    public HttpResponseHandler() {
    }

    @Override
    public void onMessageReceived(Object message, Connection outgoingConnection) throws IOException {
        ConnectionContext outgoingContext = (ConnectionContext) outgoingConnection.attachment();
        Connection incomingConnection = outgoingContext.getPeerConnection();

        if (message instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) message;
            log.fine("Received response: " + response + ". On connection: " + outgoingConnection);
            if (!response.isChunked()) {
                processResponse(response, incomingConnection, outgoingConnection);
            }
        } else if (message instanceof HttpChunk) {
            HttpChunk chunk = (HttpChunk) message;
            if (chunk.isLast()) {
                processResponse((HttpResponse) chunk.getMessage(), incomingConnection, outgoingConnection);
            }
        }
    }

    private void processChunk(final HttpChunk chunk, Connection incomingConnection, Connection outgoingConnection) throws IOException {
        incomingConnection.writeMessage(chunk, new ConnectionListener() {
            @Override
            public void onComplete(Connection connection) {
                if (chunk.isLast()) {
                    try {
                        connection.close();
                    } catch (IOException e) {
                        //ignore
                    }
                }
            }
        });

        if (chunk.isLast()) {
            outgoingConnection.close();
        }
    }

    private void processResponse(final HttpResponse response, Connection incomingConnection, Connection outgoingConnection) throws IOException {
        ProxyUtils.stripHopByHopHeaders(response);
        ProxyUtils.setProxyHeaders(response); //close connection for a while

        incomingConnection.writeMessage(response, new ConnectionListener() {
            @Override
            public void onComplete(Connection connection) {
//                if (!response.isChunked()) {
                    try {
                        connection.close();
                    } catch (IOException e) {
                        //ignore
                    }
//                }
            }
        });

//        if (!response.isChunked()) {
            outgoingConnection.close();
//        }
    }
}
