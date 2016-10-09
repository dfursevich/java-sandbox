package by.fdf.proxy;

import by.fdf.proxy.dispatcher.Connection;
import by.fdf.proxy.dispatcher.ConnectionListener;
import by.fdf.proxy.dispatcher.MessageHandler;
import by.fdf.proxy.dispatcher.http.HttpChunk;
import by.fdf.proxy.dispatcher.http.HttpRequest;
import by.fdf.proxy.dispatcher.http.HttpResponse;
import by.fdf.proxy.dispatcher.http.HttpUtils;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Dmitry Fursevich
 */
public class HttpRequestHandler implements MessageHandler {
    private static final Logger log = Logger.getLogger(HttpRequestHandler.class.getName());
    private ConnectionFactory outgoingConnectionFactory;
    private CacheManager cacheManager;

    public HttpRequestHandler(ConnectionFactory outgoingConnectionFactory, CacheManager cacheManager) {
        this.outgoingConnectionFactory = outgoingConnectionFactory;
        this.cacheManager = cacheManager;
    }

    @Override
    public void onMessageReceived(Object message, Connection incomingConnection) throws Exception{
        if (message instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) message;
            log.fine("Received request: " + request + ". On connection: " + incomingConnection);
            //if request is chunked read it full.
            if (request.isChunked()) {
                return;
            }

            processRequest(request, incomingConnection);
        } else if (message instanceof HttpChunk) {
            HttpChunk chunk = (HttpChunk) message;
            if (chunk.isLast()) {
                processRequest((HttpRequest) chunk.getMessage(), incomingConnection);
            }
        }
    }

    private void processRequest(final HttpRequest request, final Connection incomingConnection) throws IOException {
        HttpResponse response = cacheManager.get(request);
        if (response != null) {
            log.fine("Hit cache for request: " + request);
            incomingConnection.writeMessage(response, new ConnectionListener() {
                @Override
                public void onComplete(Connection connection) {
                    try {
                        incomingConnection.close();
                    } catch (IOException e) {
                        //ignore
                    }
                }
            });

        } else {
//            ProxyUtils.setProxyHeaders(request);
            Connection outgoingConnection = outgoingConnectionFactory.newConnection(HttpUtils.getHost(request), HttpUtils.getPort(request));
            ConnectionContext outgoingContext = (ConnectionContext) outgoingConnection.attachment();
            outgoingContext.setPeerConnection(incomingConnection);
            outgoingConnection.writeMessage(request, new ConnectionListener() {
                @Override
                public void onComplete(Connection connection) {
                }
            });
        }
    }
}
