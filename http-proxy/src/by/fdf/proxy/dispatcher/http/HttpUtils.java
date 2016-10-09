package by.fdf.proxy.dispatcher.http;

/**
 * @author Dmitry Fursevich
 */
public class HttpUtils {
    public static String getHost(HttpRequest request) {
        String host = request.getHeader("Host");                
        return host.contains(":") ? host.substring(0, host.indexOf(":")) : host;
    }

    public static int getPort(HttpRequest request) {
        String host = request.getHeader("Host");
        return host.contains(":") ? Integer.valueOf(host.substring(host.indexOf(":") + 1)) : 80;
    }
}
