package by.fdf.proxy.dispatcher.http;

/**
 * @author Dmitry Fursevich
 */
public class HttpRequestDecoder extends HttpMessageDecoder {
    @Override
    protected void decodeMessage(String firstLine) {
        String[] tokens = firstLine.split("[ ]");
        currentMessage = new HttpRequest(tokens[0], tokens.length > 0 ? tokens[1] : "", tokens.length > 1 ? tokens[2] : "");
    }
}
