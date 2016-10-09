package by.fdf.proxy;

import by.fdf.proxy.dispatcher.http.HttpMessage;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dmitry Fursevich
 */
public class ProxyUtils {
    private static final Set<String> hopByHopHeaders = new HashSet<String>();

    static {
        hopByHopHeaders.add("connection");
        hopByHopHeaders.add("keep-alive");
        hopByHopHeaders.add("proxy-authenticate");
        hopByHopHeaders.add("proxy-authorization");
//        hopByHopHeaders.add("te");
//        hopByHopHeaders.add("trailers");
        hopByHopHeaders.add("upgrade");
    }

    public static void stripHopByHopHeaders(HttpMessage message) {
        for (String hopByHopHeader : hopByHopHeaders) {
            message.removeHeader(hopByHopHeader);
        }
    }

    public static void setProxyHeaders(HttpMessage message) {
        message.addHeader("Connection", "close");
    }
}
