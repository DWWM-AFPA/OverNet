package src;

/* code creer par Florian, commentaires Charlotte pour sa comprehension
* travail en groupe OverNet (Flo, Charlotte, Julien et Thomas)
*/

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

// classe monProxy permet d ecouter un port et de creer un proxy
public class monProxy extends Thread {

    private static HttpRequest request;

    // retourne le resultat de la demande
    private static HttpResponse response;

    // implemente l ip + le port
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

    // requete GET qui imprime le corps de la reponse en chaine de caractere
    public static HttpRequest httpRequestFromString(String monUrl) {
        // il verifie que l adresse commence bien par htt et l ajoute le cas echeant
        HttpRequest retour = null;
        if (!monUrl.startsWith("htt"))
            monUrl = "http://" + monUrl;

        try {
            // il genere des requetes http, en creant les instances de la demande
            retour = HttpRequest.newBuilder()
                    .uri(URI.create(monUrl))
                    .build();
        }
        // il gere les erreurs
        catch (IllegalStateException e) {
            System.err.println("httpRequestFromString ne fonctionne pas :" + e);
        } catch (Exception e) {
            System.err.println("CA MARCHE PAS : " + e);
            e.printStackTrace();

        }
        // System.out.println(retour);
        return retour;
    }

    // etat de la reponse
    public static HttpResponse monProxy(String myRequest, String addr, int port) {

        HttpRequest request = httpRequestFromString(myRequest);
        InetSocketAddress socket = new InetSocketAddress(addr, port);
        ProxySelector myProxyAdress = ProxySelector.of(socket);

        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20))
                    .proxy(myProxyAdress)
                    .build();
            // runProxy(port);
            // recuperation de la reponse en chaine de caractere
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            System.err.println("IOException :" + e);
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            System.err.println("InterruptedException :" + e);
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("L'argument de mon socket est pas bon :" + e);
            return null;
        } catch (Exception e) {
            System.err.println("CA MARCHE PAS : " + e);
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse monProxy(String myRequest) {
        // System.out.println("toto : " +myRequest);
        HttpRequest request = httpRequestFromString(myRequest);
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            System.err.println("IOException 139 :" + e);
            e.printStackTrace();

            return null;
        } catch (InterruptedException e) {
            System.err.println("InterruptedException :" + e);
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("L'argument de mon socket est pas bon :" + e);
            return null;
        } catch (Exception e) {
            System.err.println("CA MARCHE PAS : " + e);
            e.printStackTrace();
            System.err.flush();
            System.out.println("request " + request);
            System.out.println("pasrequest " + HttpResponse.BodyHandlers.ofString());
            return null;
        }

    }

    // les methodes run() runProxy() ruHtml() executent les traitements des threads
    public static void run(String myRequest) {
        System.out.println(monProxy(myRequest)
                .body());
    }

    public void run(String myRequest, InetAddress addr, int port) {
        monProxy test1 = new monProxy();
        monProxy test2 = new monProxy();

        // System.out.println(test1.monProxy(myRequest).body());

    }

    public void run(String myRequest, String addr, int port) {
        // runProxy(port);
        System.out.println(monProxy(myRequest, addr, port));
    }

    public void runHtml(String myRequest, String addr, int port) {
        // runProxy(port);
        System.out.println(monProxy(myRequest, addr, port)
                .body());
    }

    public static void runProxy(int localPort) {
        // creation d un socket
        ServerSocket myServerSocket = null;
        Socket mySocket = null;
        // envoie et traite des donnees envoyees par le serveur
        BufferedReader entreServer;
        BufferedWriter sortieServer;

        System.out.println("ON start proxy");
        try {
            // le proxy ecoute le client sur le localport
            myServerSocket = new ServerSocket(localPort);
            System.out.println("Server ouvert port : " + localPort);

            while (true) {
                // accepte la connexion demandee par le serveur
                mySocket = myServerSocket.accept();
                System.out.println("Socket Créé");
                // traite la lecture des données renvoyées par le serveur et permet de les
                // ecrire et de les envoyer
                entreServer = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                sortieServer = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));

                String request = "";
                try {
                    // permet de lire ligne par ligne et de retourner a la ligne inferieure a gauche
                    String[] buffer = entreServer.readLine().split(" ");
                    request = buffer[1];
                } catch (IOException e) {
                    System.err.println("Impossible de creer la requete depuis l'inputStream : " + e);
                }

                if (monProxy(request) != null) {
                    HttpResponse<String> response = monProxy(request);
                    // repond le head, information protocole + information complementaires si besoin
                    HttpHeaders responseHeader = response.headers();

                    // parser pour que le compilateur remplace les symboles par la bonne synthaxe
                    // pour le protocole
                    String version = response.version().toString().replaceFirst("_", "/").replaceFirst("_", ".");
                    int etat = response.statusCode();
                    // reponse du body html du site demande
                    String responseBody = response.body();

                    // System.out.println("avant : " + request);
                    // System.out.println("pouet : " + version + " " + etat +" OK ");

                    sortieServer.write(version + " " + etat + " OK ");
                    sortieServer.newLine();
                    // sortieServer.write(responseHeader.toString());
                    sortieServer.newLine();
                    sortieServer.write(responseBody);
                    sortieServer.close();
                    // System.out.println("apres : " +sortieServer);
                    if (mySocket != null)
                        try {
                            // fermeture du socket
                            mySocket.close();
                        } catch (Exception e) {
                            System.err.println(e);
                        }

                    System.out.println("Mon socket est fermé.");
                }
            }

        } catch (IOException e) {
            System.err.println("213 " + e);
            e.printStackTrace();

        } catch (Exception e) {
            System.err.println("218 " + e);
            e.printStackTrace();

        } finally {
            if (mySocket != null)
                try {
                    mySocket.close();
                } catch (Exception e) {
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
        } catch (IOException e) {
            System.err.println("IOException socket : " + e);
        }

    }
}
