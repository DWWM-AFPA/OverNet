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


    public static HttpRequest httpRequestFromString(String monUrl){
        HttpRequest retour = null;
        try {
            retour =  HttpRequest.newBuilder()
                    .uri(URI.create(monUrl))
                    .build();
        }
        catch (IllegalStateException e){
            System.err.println("httpRequestFromString ne fonctionne pas :" + e);
        }
        catch (Exception e){
            System.err.println("CA MARCHE PAS : " + e);
        }
        System.out.println(retour);
        return retour;
    }

    public static HttpResponse monProxy(String myRequest, InetAddress addr, int port){

        HttpRequest request = httpRequestFromString(myRequest);
        InetSocketAddress socket = new InetSocketAddress(addr, port);
        ProxySelector myProxyAdress = ProxySelector.of(socket);
        try{
            return HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20))
                    .proxy(myProxyAdress)
                    .authenticator(Authenticator.getDefault())
                    .build().send(request, HttpResponse.BodyHandlers.ofInputStream());
        }
        catch (IOException e){
            System.err.println("IOException :" + e);
            return null;
        }
        catch (InterruptedException e){
            System.err.println("InterruptedException :" + e);
            return null;
        }
        catch (IllegalArgumentException e){
            System.err.println("L'argument de mon socket est pas bon :" + e);
            return null;
        }
        catch (Exception e){
            System.err.println("CA MARCHE PAS : " + e);
            return null;
        }
    }

    public static HttpResponse monProxy(String myRequest, String addr, int port){

        HttpRequest request = httpRequestFromString(myRequest);
        InetSocketAddress socket = new InetSocketAddress(addr, port);
        ProxySelector myProxyAdress = ProxySelector.of(socket);
        try{
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20))
                    .proxy(myProxyAdress)
                    .authenticator(Authenticator.getDefault())
                    .build();
            return client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        }
            catch (IOException e){
            System.err.println("IOException :" + e);
            return null;
        }
            catch (InterruptedException e){
            System.err.println("InterruptedException :" + e);
            return null;
        }
            catch (IllegalArgumentException e){
            System.err.println("L'argument de mon socket est pas bon :" + e);
            return null;
        }
            catch (Exception e){
            System.err.println("CA MARCHE PAS : " + e);
            return null;
        }
    }

    public static HttpResponse monProxy(String myRequest){

        HttpRequest request = httpRequestFromString(myRequest);
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20))
                    .authenticator(Authenticator.getDefault())
                    .build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
            catch (IOException e){
            System.err.println("IOException :" + e);
            return null;
        }
            catch (InterruptedException e){
            System.err.println("InterruptedException :" + e);
            return null;
        }
            catch (IllegalArgumentException e){
            System.err.println("L'argument de mon socket est pas bon :" + e);
            return null;
        }
            catch (Exception e){
            System.err.println("CA MARCHE PAS : " + e);
            System.out.println("request " + request);
            System.out.println("pasrequest " + HttpResponse.BodyHandlers.ofString());
            return null;
        }

    }

    public void run(String myRequest){
        __ProxyServer test = new __ProxyServer();

    System.out.println(test.monProxy(myRequest));

    }
}
