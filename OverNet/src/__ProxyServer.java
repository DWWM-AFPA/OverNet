import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

/**
 * Classe __ProxyServer permetant l'ecoute d'un port afin de creer un proxy
 * @author Florian
 * @version 1.0
 */

public class __ProxyServer extends Thread {


    /**
     * Un HttpRequest à visibilité privée
     */
    private HttpRequest request;

    /**
     * Un HttpResponse à visibilité privée
     */
    private HttpResponse response;

    /**
     * Un InetSocketAddress à visibilité privée
     */
    private InetSocketAddress socket;

    /**
     * Seteur de l'attribut request avec en paramètre HttpRequest
     * @param request une HttpRequest
     */
    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    /**
     * Geteur de l'attribut request
     * @return HttpRequest
     */
    public HttpRequest getRequest() {
        return request;
    }

    /**
     * Seteur de l'attribut response avec en paramètre HttpResponse
     * @param response une HttpResponse
     */
    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    /**
     * Geteur de l'attribut response
     * @return HttpResponse
     */
    public HttpResponse getResponse() {
        return response;
    }

    /**
     * Seteur de l'attribut socket avec en paramètre InetSocketAddress
     * @param socket une InetSocketAddress
     */
    public void setSocket(InetSocketAddress socket) {
        this.socket = socket;
    }

    /**
     * Geteur de l'attribut socket
     * @return InetSocketAddress
     */
    public InetSocketAddress getSocket() {
        return socket;
    }

    /**
     * Un constructeur avec un string, une InetAdress, et un entier
     * en paramètre.
     * @param myRequest un string
     * @param addr une InetAddress
     * @param port un entier
     * @return HttpResponse
     */
    public HttpResponse monProxy(String myRequest, InetAddress addr, int port){

        HttpRequest request = httpRequestFromString(myRequest);
        InetSocketAddress socket = new InetSocketAddress(addr, port);
        ProxySelector myProxyAdress = ProxySelector.of(socket);
        try{
            return HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20))
                    .proxy(myProxyAdress)
                    .build().send(request, HttpResponse.BodyHandlers.ofString());
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

    /**
     * Un constructeur avec un string, un string, et un entier
     * en paramètre.
     * @param myRequest un string
     * @param addr une string
     * @param port un entier
     * @return HttpResponse
     */
    public HttpResponse monProxy(String myRequest, String addr, int port){

        HttpRequest request = httpRequestFromString(myRequest);
        InetSocketAddress socket = new InetSocketAddress(addr, port);
        ProxySelector myProxyAdress = ProxySelector.of(socket);

        try{
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20))
                    .proxy(myProxyAdress)
                    .build();
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

    /**
     * Un constructeur avec un string
     * en paramètre.
     * @param myRequest un string
     * @return HttpResponse
     */
    public HttpResponse monProxy(String myRequest){

        HttpRequest request = httpRequestFromString(myRequest);
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (IOException e){
            System.err.println("IOException 139 :" + e);
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
            System.err.flush();
            //System.out.println("request " + request);
            //System.out.println("pasrequest " + HttpResponse.BodyHandlers.ofString());
            return null;
        }


    }

    /**
     * Une methode run avec en paramètre un string
     * et permet l'affichage de l'objet HttpResponse
     * de la requête HttpRequest
     * @param myRequest un string
     */
    public void run(String myRequest){
        System.out.println(monProxy(myRequest)
        );
    }

    /**
     * Une methode runHtml avec en paramètre un string
     * et permet l'affichage du body en html
     * de la requête HttpRequest
     * @param myRequest un string
     */
    public void runHtml(String myRequest){
        System.out.println(monProxy(myRequest)
                .body()
        );
    }

    /**
     * Une methode run avec en paramètre un string, une InetAddress, un entier
     * et permet l'affichage de l'objet HttpResponse
     * de la requête HttpRequest
     * @param myRequest un string
     * @param addr un InetAddress
     * @param port un entier
     */
    public void run(String myRequest, InetAddress addr, int port){
        System.out.println(monProxy(myRequest, addr, port)
        );
    }

    /**
     * Une methode runHtml avec en paramètre un string, une InetAddress, un entier
     * et permet l'affichage du body en html
     * de la requête HttpRequest
     * @param myRequest un string
     * @param addr un InetAddress
     * @param port un entier
     */
    public void runHtml(String myRequest, InetAddress addr, int port){
        System.out.println(monProxy(myRequest, addr, port)
                .body()
        );
    }

    /**
     * Une methode run avec en paramètre un string, un string, un entier
     * et permet l'affichage de l'objet HttpResponse
     * de la requête HttpRequest
     * @param myRequest un string
     * @param addr un string
     * @param port un entier
     */
    public void run(String myRequest, String addr, int port){
        System.out.println(monProxy(myRequest, addr, port)
        );
    }

    /**
     * Une methode runHtml avec en paramètre un string, un string, un entier
     * et permet l'affichage du body en html
     * de la requête HttpRequest
     * @param myRequest un string
     * @param addr un string
     * @param port un entier
     */
    public void runHtml(String myRequest, String addr, int port){
        System.out.println(monProxy(myRequest, addr, port)
                .body()
        );
    }

    /**
     * Une methode runProxy avec en paramètre un entier
     * et permet l'activation de l'ecoute d'un port et
     * l'ouverture d'un socket puis d'un thread provisoire pour le traiter.
     * @param localPort un entier
     */
    public void runProxy(int localPort) {

        ServerSocket myServerSocket = null;
        Socket mySocket = null;


        System.out.println("ON start proxy");
        try {
            myServerSocket = new ServerSocket(localPort);
            System.out.println("Server ouvert port : " + localPort);

            while (true) {
                mySocket = myServerSocket.accept();
                System.out.println("Socket Créé");
                maCreationDeThread(mySocket);
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
            System.out.println("Mon proxy c'est arrêté.");
        }
    }

    /**
     * Une methode runProxy avec en paramètre un entier, un entier, une InetAddress
     * et permet l'activation de l'ecoute d'un port et
     * l'ouverture d'un socket puis d'un thread provisoire pour le traiter.
     * @param localPort un entier
     * @param backlog un entier
     * @param bindAddr une InetAddress
     */
    public void runProxy(int localPort, int backlog, String host) {

        ServerSocket myServerSocket = null;
        Socket mySocket = null;


        System.out.println("ON start proxy");
        try {
            InetAddress bindAddr = InetAddress.getByName(host);

            myServerSocket = new ServerSocket(localPort, backlog, bindAddr);
            System.out.println("Server ouvert port : " + localPort);

            while (true) {
                mySocket = myServerSocket.accept();
                System.out.println("Socket Créé");
                maCreationDeThread(mySocket);
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
            System.out.println("Mon proxy c'est arrêté.");
        }
    }

    /**
     * Une methode maCreationDeThread avec en paramètre un socket
     * et permet l'utilisation des données input en provenance du
     * socket et de renvoyer un resultat par ce meme socket.
     * @param mySocket un socket
     */
    public void maCreationDeThread(Socket mySocket){
        BufferedReader entreServer;
        BufferedWriter sortieServer;
        String request = "";

        try{
            entreServer = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            sortieServer = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));
            String[] buffer = entreServer.readLine().split(" ");
            request = buffer[1];

            if(monProxy(request) != null) {
                HttpResponse<String> response = monProxy(request);
                HttpHeaders responseHeader = response.headers();

                String version = response.version().toString().replaceFirst("_", "/").replaceFirst("_", ".");
                int etat = response.statusCode();
                Optional<SSLSession> sslSession = response.sslSession();
                String responseBody = response.body();

                sortieServer.write(version + " " + etat + " OK ");
                sortieServer.newLine();
                sortieServer.newLine();
                sortieServer.write(responseBody);
                sortieServer.close();
                if (mySocket != null)
                    try {
                        mySocket.close();
                    } catch (Exception e) {
                        System.err.println(e);
                    }

                System.out.println("Mon socket est fermé.");
            }

        }catch (IOException e){
            System.err.println("Impossible de se connecter (IOException) : "+e);
        }catch (Exception e){
            System.err.println("Impossible de se connecter : "+e);
        }
    }

    /**
     * Une methode httpRequestFromString avec en paramètre un string
     * et permet la création d'une HttpRequest à partir d'une adresse url.
     * Elle ajoute aussi le protocole utilisé au debut de la requête si il n'est pas présent.
     * @param monUrl un string
     * @return HttpRequest
     */
    public HttpRequest httpRequestFromString(String monUrl){
        HttpRequest retour = null;
        if(!monUrl.startsWith("htt"))
            monUrl = "http://"+monUrl;

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
        //System.out.println(retour);
        return retour;
    }
}
