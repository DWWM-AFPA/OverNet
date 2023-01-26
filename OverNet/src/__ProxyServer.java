import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class __ProxyServer extends Thread {

    private static HttpRequest request;
    private static HttpResponse response;
    private static InetSocketAddress socket;

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setSocket(InetSocketAddress socket) {
        this.socket = socket;
    }

    public static InetSocketAddress getSocket() {
        return socket;
    }

    public static HttpResponse monProxy(HttpRequest myRequest, InetAddress addr, int port) throws IllegalArgumentException{

            InetSocketAddress socket = new InetSocketAddress(addr, port);
            ProxySelector myProxyAdress = ProxySelector.of(socket);
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20))
                    .proxy(myProxyAdress)
                    .authenticator(Authenticator.getDefault())
                    .build();
            HttpResponse response = client.send(myRequest, HttpResponse.BodyHandlers.ofInputStream());

            return response;

    }

    public static HttpResponse monProxy(HttpRequest myRequest) throws IllegalArgumentException{

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .authenticator(Authenticator.getDefault())
                .build();
        HttpResponse response = client.send(myRequest, HttpResponse.BodyHandlers.ofInputStream());

        return response;

    }
}
