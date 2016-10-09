package by.fdf.proxy;

import by.fdf.proxy.dispatcher.Connection;
import by.fdf.proxy.dispatcher.MessageDecoder;

/**
 * @author Dmitry Fursevich
 */
public class ConnectionContext {
    //For client-proxy connection it's proxy-server connection and vice verse
    private Connection peerConnection;

    Connection getPeerConnection() {
        return peerConnection;
    }

    void setPeerConnection(Connection peerConnection) {
        this.peerConnection = peerConnection;
    }

    void reset() {
        peerConnection = null;
    }
}
