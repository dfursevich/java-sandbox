package by.fdf.proxy.dispatcher.http;

/**
 * @author Dmitry Fursevich
 */
public class HttpResponse extends HttpMessage {
    private String statusCode;
    private String extensionCode;
    private String reasonPhrase;
    //todo:trailer

    HttpResponse(String statusCode, String extensionCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.extensionCode = extensionCode;
        this.reasonPhrase = reasonPhrase;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getExtensionCode() {
        return extensionCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
