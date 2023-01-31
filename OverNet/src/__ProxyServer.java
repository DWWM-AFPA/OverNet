import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Date;

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
            e.printStackTrace();

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
            e.printStackTrace();

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
                    .build();
            //runProxy(port);
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
            catch (IOException e){
                System.err.println("IOException :" + e);
                e.printStackTrace();

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
                e.printStackTrace();

                return null;
            }
    }

    public static HttpResponse monProxy(String myRequest){

        HttpRequest request = httpRequestFromString(myRequest);
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20))
                    //.authenticator(Authenticator.getDefault())
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
            e.printStackTrace();
            System.err.flush();
            System.out.println("request " + request);
            System.out.println("pasrequest " + HttpResponse.BodyHandlers.ofString());
            return null;
        }

    }

    public static void run(String myRequest){
        System.out.println(monProxy(myRequest)
                    .body()
            );
    }

    public void run(String myRequest, InetAddress addr, int port){
        __ProxyServer test1 = new __ProxyServer();
        __ProxyServer test2 = new __ProxyServer();

        //System.out.println(test1.monProxy(myRequest).body());

    }

    public void run(String myRequest, String addr, int port){
        __ProxyServer test1 = new __ProxyServer();

        //runProxy(port);
        System.out.println(test1.monProxy(myRequest, addr, port)
                //.body()
        );
    }

    public static void runProxy(int localPort) {

        ServerSocket myServerSocket = null;
        Socket mySocket = null;
        BufferedReader entreServer;
        BufferedWriter sortieServer;

        System.out.println("ON start proxy");
        try {
            myServerSocket = new ServerSocket(localPort);
            System.out.println("Server ouvert port : " + localPort);

            while (true) {
                mySocket = myServerSocket.accept();
                System.out.println("Socket Créé");

                entreServer = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                sortieServer = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));

                String request = "";

                try {
                    String[] buffer = entreServer.readLine().split(" ");
                    request = buffer[1];
                }catch(IOException e) {
                    System.err.println("Impossible de creer la requete depuis l'inputStream : "+e);
                }


                HttpResponse response = monProxy(request);

                //sortieServer.write(response.toString());
                //sortieServer.flush();

                //run(request);

                /*   TODO !!

                utiliser la methode monProxy pour transformer l'httpresponse qu'il retourne en outputstream !
                monProxy(request);
                */


                //System.out.println(response.body());
                System.out.println(sortieServer);
                //System.out.println(sortieServer);

            }


        } catch (IOException e) {
            System.err.println("213 " + e);
            e.printStackTrace();

        } catch (Exception e) {
            System.err.println("218 " + e);
            e.printStackTrace();

        } finally {
            if(mySocket !=null)
                try {
                    mySocket.close();
                }catch(Exception e){
                    System.err.println(e);
                }

            System.out.println("Mon socket est fermé.");
        }

    }

    private static void getRequestFromSocket(int port) {
        try {
            ServerSocket myServerSocket = new ServerSocket(port);
            Socket mySocket = myServerSocket.accept();
            InputStream input = mySocket.getInputStream();

            System.out.println("mySocket : " + mySocket);
            System.out.println("myInput : " + input);
        }catch (IOException e){
            System.err.println("IOException socket : " + e);
        }

    }




}
