package by.fdf.proxy.dispatcher.http;

/**
 * Response = Status-Line
 *          *(( general-header
 *              | response-header
 *              | entity-header ) CRLF)
 *              CRLF
 *              [ message-body ]
 *
 * @author Dmitry Fursevich
 */
public class HttpResponseDecoder extends HttpMessageDecoder {

    @Override
    protected void decodeMessage(String firstLine) {
        String[] tokens = firstLine.split("[ ]");
        currentMessage = new HttpResponse(tokens[0], tokens.length > 0 ? tokens[1] : "", tokens.length > 1 ? tokens[2] : "");
    }
}
