import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import javax.imageio.ImageIO;

public class RequestHandler implements Runnable {

    /**
     * Socket connectée au client transmis par le Serveur Proxy.
     */
    Socket clientSocket;

    /**
     * Lecture des données que le client envoie au Proxy
     *
     */
    BufferedReader proxyToClientBr;

    /**
     * Envoie des données au client transmis par le Serveur Proxy.
     *
     */
    BufferedWriter proxyToClientBw;

    /**
     * Thread qui est utilisé pour transmettre les données lues du client au serveur lors de l'utilisation de HTTPS.
     * Une référence à ce thread est nécessaire pour qu'il puisse être fermé une fois terminé.
     *
     */

    private Thread httpsClientToServer;

    /**
     * Création d'un objet RequestHandler capable de répondre aux requêtes HTTP(S) GET.
     * @param clientSocket Socket connecté au client.
     */
    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try{
            this.clientSocket.setSoTimeout(2000);
            proxyToClientBr = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            proxyToClientBw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * lit et examine la requestString et appelle la méthode appropriée en se basant sur le type de demande
     *
     */

    @Override
    public void run() {
        // Get requête du client
        String requestString = null;
        try {
            requestString = proxyToClientBr.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur de lecture de la requête du client");
            return;
        }

        // Analyse de l'URL

        System.out.println("Reception de la requête" + requestString);

        // Si la requête est un GET, appelle la méthode GET
        String requset = requestString.substring(0, requestString.indexOf(" "));

        // supprimer le type de demande et l'espace " ".
        urlString = requestString.substring(requestString.indexOf(" ") + 1);

        // supprime Tous ce qu'il y a après l'espace " ".
        urlString = urlString.substring(0, urlString.indexOf(" ")+1);

        // Ajoutez http:// si nécessaire pour créer une URL correcte.
        if(!urlString.substring(0,4).equals("http")) {
            String temp = "http://";
            urlString = temp + urlString;
        }
        // Vérifie si le site est bloqué.
        if(Proxy.isBlocked(urlString)) {
            System.out.println("requête site bloqué" + urlString);
            blockedSiteRequested();
            return;
        }
        // vérifie le type de requête
        if(request.equals("CONNECTER")){
            System.out.println("Requête pour HTTPS : " + urlString + "\n");
            handleHTTPSRequest(urlString);
        }
        else{
            // Vérifie si nous avons une copie en cache
            File file = new File(urlString);
            if((file = Proxy.getCachedFile(urlString)) !=null) {
            System.out.println("Requête récupérée en cache : " + urlString + "\n");
            sendCachedPageToClient(file);
            }
            else{
                System.out.println("HTTP GET pour : " + urlString + "\n");
                sendNonCachedToClient(urlString);
            }
        }
    }

    /**
     * Envoie le fichier en cache spécifié au client.
     * @pparam cachedFile Le fichier à envoyer (peut être une image/un texte
     *
     */
    private void sendCachedPageToClient(File cachedFile) {
        // Lecture d'un fichier contenant une page web en cache
        try{
            // Si le fichier est une image, écrit les données au client en utilisant une image en forme
            String fileExtension = cachedFile.getName().substring(cachedFile.getName().lastIndexOf("."));

            // réponse qui sera envoyéeau serveur
            String response;
            if((fileExtension.contains("jpg")) || (fileExtension.contains("jpeg")) || (fileExtension.contains("")) || (fileExtension.contains("png")) || (fileExtension.contains("gif"))){
                // Lecture de l'image stockéen cache
                BufferedImage bufferedImage = ImageIO.read(cachedFile);

                if(image == null){
                    System.out.println("Image" + cachedFile.getName()+ " was null");
                    response = "HTTP/1.0 404 NOT FOUND \n" +
                            "Proxy-agent: ProxyServer/1.0\n" + "\r\n";
                    proxyToClientBw.write(response);
                    proxyToClientBw.flush();
                }
                else {
                    response = "HTTP/1.0 200 OK \n" + "Proxy-agent: ProxyServer/1.0\n" + "\r\n";
                    proxyToClientBw.write(response);
                    proxyToClientBw.flush();
                    ImageIO.write(bufferedImage, fileExtension.substring(1), clientSocket.getOutputStream());
                }
            }
            // Fichier texte standard demandé
            else {
                BufferedReader cachedFileBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(cachedFile)));

                response = "HTTP/1.0 200 OK \n" + "Proxy-agent: ProxyServer/1.0\n" + "\r\n";
                proxyToClientBw.write(response);
                proxyToClientBw.flush();

                String line;
                while ((line = cachedFileBufferedReader.readLine())!= null) {
                    proxyToClientBw.write(line);
                }
            }
            proxyToClientBw.flush();

            // Fermeture des ressources
            if(cachedBufferedReader!= null) {
                cachedBufferedReader.close();
            }
        }
        // Fermeture des ressources
        if (proxyToClientBw!= null) {
            proxyToClientBw.close();
        }
        catch(IOException e){
            System.out.println("Erreur d'envoi du fichier en cache au client");
            e.printStackTrace();
        }
    }
    /**
     * Envoie au client le contenu du fichier spécifié par l'urlString
     * @param urlString URL du fichier demandé
     */
}
