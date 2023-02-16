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
     */
    BufferedReader proxyToClientBr;

    /**
     * Envoie des données au client transmis par le Serveur Proxy.
     */
    BufferedWriter proxyToClientBw;

    /**
     * Thread qui est utilisé pour transmettre les données lues du client au serveur lors de l'utilisation de HTTPS.
     * Une référence à ce thread est nécessaire pour qu'il puisse être fermé une fois terminé.
     */

    private Thread httpsClientToServer;

    /**
     * Création d'un objet RequestHandler capable de répondre aux requêtes HTTP(S) GET.
     *
     * @param clientSocket Socket connecté au client.
     */
    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.clientSocket.setSoTimeout(2000);
            proxyToClientBr = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            proxyToClientBw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * lit et examine la requestString et appelle la méthode appropriée en se basant sur le type de demande
     */

    @Override
    public void run() {
        // Get requête du client
        String requestString;
        try {
            requestString = proxyToClientBr.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur de lecture de la requête du client");
            return;
        }

        // Analyse de l'URL.

        System.out.println("Reception de la requête" + requestString);

        // Si la requête est un GET, appelle la méthode GET
        String request = requestString.substring(0, requestString.indexOf(' '));

        // supprimer le type de demande et l'espace " ".
        String urlString = requestString.substring(requestString.indexOf(' ') + 1);

        // supprime Tous ce qu'il y a après l'espace " ".
        urlString = urlString.substring(0, urlString.indexOf(' '));

        // Ajoutez http:// si nécessaire pour créer une URL correcte.
        if (!urlString.substring(0, 4).equals("http")) {
            String temp = "http://";
            urlString = temp + urlString;
        }
        // Vérifie si le site est bloqué.
        if (Proxy.isBlocked(urlString)) {
            System.out.println("requête site bloqué" + urlString);
            blockedSiteRequested();
            return;
        }
        // vérifie le type de requête.
        if (request.equals("CONNECTER")) {
            System.out.println("Requête pour HTTPS : " + urlString + "\n");
            handleHTTPSRequest(urlString);
        } else {
            // Vérifie si nous avons une copie en cache.
            File file;
            if ((file = Proxy.getCachedPage(urlString)) != null) {
                System.out.println("Requête récupérée en cache : " + urlString + "\n");
                sendCachedPageToClient(file);
            } else {
                System.out.println("HTTP GET pour : " + urlString + "\n");
                sendNonCachedToClient(urlString);
            }
        }
    }

    /**
     * Envoie le fichier en cache spécifié au client.
     *
     * @param cachedFile Le fichier à envoyer (peut être une image/un texte
     */
    private void sendCachedPageToClient(File cachedFile) {
        // Lecture d'un fichier contenant une page web en cache.
        try {
            // Si le fichier est une image, écrit les données au client en utilisant une image en forme.
            String fileExtension = cachedFile.getName().substring(cachedFile.getName().lastIndexOf('.'));

            // réponse qui sera envoyée au serveur.
            String response;
            if ((fileExtension.contains(".jpg")) || (fileExtension.contains(".jpeg")) || (fileExtension.contains(".png")) || (fileExtension.contains(".gif"))) {
                // Lecture de l'image stockée en cache.
                BufferedImage image = ImageIO.read(cachedFile);

                if (image == null) {
                    System.out.println("Image" + cachedFile.getName() + " est null");
                    response = "HTTP/1.0 404 NOT FOUND \n" +
                            "Proxy-agent: ProxyServer/1.0\n" + "\r\n";
                    proxyToClientBw.write(response);
                    proxyToClientBw.flush();
                } else {
                    response = "HTTP/1.0 200 OK \n" + "Proxy-agent: ProxyServer/1.0\n" + "\r\n";
                    proxyToClientBw.write(response);
                    proxyToClientBw.flush();
                    ImageIO.write(image, fileExtension.substring(1), clientSocket.getOutputStream());
                }
            }
            // Fichier texte standard demandé.
            else {
                BufferedReader cachedFileBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(cachedFile)));

                response = "HTTP/1.0 200 OK \n" + "Proxy-agent: ProxyServer/1.0\n" + "\r\n";
                proxyToClientBw.write(response);
                proxyToClientBw.flush();

                String line;
                while ((line = cachedFileBufferedReader.readLine()) != null) {
                    proxyToClientBw.write(line);
                }
                proxyToClientBw.flush();

                // Fermeture des ressources.
                if (cachedFileBufferedReader != null) {
                    cachedFileBufferedReader.close();
                }
            }
            // Fermeture des ressources.
            if (proxyToClientBw != null) {
                proxyToClientBw.close();

            }
        } catch (IOException e) {
            System.out.println("Erreur d'envoie du fichier en cache au client");
            e.printStackTrace();
        }
    }

    /**
     * Envoie au client le contenu du fichier spécifié par l'urlString
     *
     * @param urlString URL du fichier demandé
     */

    private void sendNonCachedToClient(String urlString) {
        try {

            // Calcul un nom de fichier logique conformément au schéma.
            // Cela permet aux fichiers stockés sur le disque de ressembler à ceux de l'URL d'où ils proviennent.
            int fileExtensionIndex = urlString.lastIndexOf(".");
            String fileExtension;

            // Obtenir le type de fichier.
            fileExtension = urlString.substring(fileExtensionIndex, urlString.length());

            // Obtenir le nom du fichier initial.
            String fileName = urlString.substring(0, fileExtensionIndex);

            // Supprimez http://www. qui n'est pas nécessaire dans le nom de fichier.
            fileName = fileName.substring(fileName.indexOf('.') + 1);

            // Supprimez tous les caractères illégaux du nom du fichier.
            fileName = fileName.replace("/", "__");
            fileName = fileName.replace('.', '_');

            // La fin de / entraîne l'extraction de l'index.html de ce répertoire.
            if (fileExtension.contains("/")) {
                fileExtension = fileExtension.replace("/", "__");
                fileExtension = fileExtension.replace('.', '_');
                fileExtension += ".html";
            }

            fileName = fileName + fileExtension;

            // Tentative de création d'un fichier de cache vers.
            boolean caching = true;
            File fileToCache = null;
            BufferedWriter fileToCacheBW = null;

            try {

                // Création d'un fichier de cache.
                fileToCache = new File("cached/" + fileName);

                if (!fileToCache.exists()) {
                    fileToCache.createNewFile();
                }

                // Créer un flux de sortie tamponée pour écrire dans la copie en cache du fichier.
                fileToCacheBW = new BufferedWriter(new FileWriter(fileToCache));
            } catch (IOException e) {
                System.out.println("Erreur d'envoie du fichier en cache au client" + fileName);
                e.printStackTrace();
                caching = false;
            } catch (NullPointerException e) {
                System.out.println("NullPointerException ouverture du fichier ");
            }

            // Vérifier si le fichier est une image.
            if ((fileExtension.contains(".png")) || fileExtension.contains(".jpg") || fileExtension.contains(".jpeg") || fileExtension.contains(".gif")) {
                // Création de l'URL.
                URL remoteURL = new URL(urlString);
                BufferedImage image = ImageIO.read(remoteURL);

                if (image != null) {
                    // Mise en cache de l'image sur le disque.

                    ImageIO.write(image, fileExtension.substring(1), fileToCache);

                    // Envoie du code réponse au client.
                    String line = "HTTP/1.0 200 OK \n" + "Proxy-agent: ProxyServer/1.0\n" + "\r\n";
                    proxyToClientBw.write(line);
                    proxyToClientBw.flush();

                    // Envoyez-leur les données de l'image.
                    ImageIO.write(image, fileExtension.substring(1), clientSocket.getOutputStream());

                    // Aucune image reçue du serveur distant.
                } else {
                    System.out.println("Envoi d'un message d'erreur 404 au client car l'image n'a pas été reçue du serveur" + fileName);
                    String error = "HTTP/1.0 404 NOT FOUND \n" + "Proxy-agent: ProxyServer/1.0\n" + "\r\n";
                    proxyToClientBw.write(error);
                    proxyToClientBw.flush();
                    return;
                }
            }
            // Le fichier est un fichier texte
            else {
                // Création d'un URL.
                URL remoteURL = new URL(urlString);
                // Créer une connexion au serveur distant.
                HttpURLConnection proxyToServerConnection = (HttpURLConnection) remoteURL.openConnection();
                proxyToServerConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                proxyToServerConnection.setRequestProperty("Content-Language", "fr-FR"); // remplir le champs libre "en-US " par le language.
                proxyToServerConnection.setUseCaches(false);
                proxyToServerConnection.setDoOutput(true);

                // Créer un lecteur tampon à partir d'un serveur distant

                BufferedReader proxyToServerBR = new BufferedReader(new InputStreamReader(proxyToServerConnection.getInputStream()));

                // Envoyer le code de réussite au client
                String line = "HTTP/1.0 200 OK \n" + "Proxy-agent: ProxyServer/1.0\n" + "\r\n";
                proxyToClientBw.write(line);

                // Lecture du flux d'entrée entre le proxy et le serveur distant
                while ((line = proxyToServerBR.readLine()) != null) {
                    // Envoie des données au client.
                    proxyToClientBw.write(line);

                    // Écriture dans notre copie en cache du fichier
                    if (caching) {
                        fileToCacheBW.write(line);
                    }
                }
                //Assurez-vous que toutes les données sont envoyées à ce stade
                proxyToClientBw.flush();

                // fermeture des ressources.
                if (proxyToServerBR != null) {
                    proxyToServerBR.close();
                }
            }
            if (caching) {
                // Assurer l'écriture des données et les ajoutent à notre haschMap en cache.
                fileToCacheBW.flush();
                Proxy.addCachedPage(urlString, fileToCache);
            }

            // fermeture des ressources.
            if (fileToCacheBW != null) {
                fileToCacheBW.close();
            }

            if (proxyToClientBw != null) {
                proxyToClientBw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gère les requêtes HTTPS entre le client et le serveur distant
     *
     * @param urlString fichier souhaité à transmettre par https
     */
    private void handleHTTPSRequest(String urlString) {
        // Extraire l'URL et le port du site distant
        String url = urlString.substring(7);
        String pieces[] = url.split(":");
        url = pieces[0];
        int port = Integer.valueOf(pieces[1]);

        try {
            // Seule la première ligne de la requête HTTPS a été lue à ce stade (CONNECT *).
            // Lire (et jeter) le reste des données initiales sur le flux.
            for (int i = 0; i < 5; i++) {
                proxyToClientBr.readLine();
            }

            // Obtenir l'IP réelle associée à cette URL par DNS.
            InetAddress address = InetAddress.getByName(url);

            // Ouverture d'un socket vers le serveur distant.
            Socket proxyToServerSocket = new Socket(address, port);
            proxyToServerSocket.setSoTimeout(10000);

            // Envoie du message d'une connexion établie au client.
            String line = "HTTP/1.0 200 connexion OK \r \n" + "Proxy-agent: ProxyServer/1.0\r \n" + "\r\n";
            proxyToClientBw.write(line);
            proxyToClientBw.flush();

            // Le client et le distant commenceront tous deux à envoyer des données au proxy à ce stade.
            // Le proxy doit lire de manière asynchrone les données de chaque partie et les envoyer à l'autre partie.
            // Créer une écriture en mémoire tampon entre le proxy et le distant.
            BufferedWriter proxyToServerBW = new BufferedWriter(new OutputStreamWriter(proxyToServerSocket.getOutputStream()));

            // Créer un lecteur tampon à partir du proxy et du distant.
            BufferedReader proxyToServerBR = new BufferedReader(new InputStreamReader(proxyToServerSocket.getInputStream()));

            // Créer un nouveau thread pour écouter le client et transmettre au serveur.
            ClientToServerHttpsTransmit clientToServerHttps = new ClientToServerHttpsTransmit(clientSocket.getInputStream(), proxyToServerSocket.getOutputStream());
            //Thread clientToServerHttpsThread = new Thread(clientToServerHttps); à tester.
            httpsClientToServer = new Thread(clientToServerHttps);
            httpsClientToServer.start();

            // Écoute du serveur distant et relais vers le client.
            try {
                byte[] buffer = new byte[4096]; // 4096 à se renseigner.
                int read;
                do {
                    read = proxyToServerSocket.getInputStream().read(buffer);
                    if (read > 0) {
                        clientSocket.getOutputStream().write(buffer, 0, read);
                        if (proxyToServerSocket.getInputStream().available() < 1) {
                            clientSocket.getOutputStream().flush();
                        }
                    }
                } while (read >= 0);
            } catch (SocketTimeoutException e) {
                System.out.println("SocketTimeoutException");
            } catch (IOException e) {
                System.out.println("IOException");
                e.printStackTrace();
            }

            // Fermeture des ressources
            if (proxyToServerSocket != null) {
                proxyToServerSocket.close();
            }
            if (proxyToServerBR != null) {
                proxyToServerBR.close();
            }
            if (proxyToServerBW != null) {
                proxyToServerBW.close();
            }
            if (proxyToClientBw != null) {
                proxyToClientBw.close();
            }
        } catch(SocketTimeoutException e){
                String line = "HTTP/1.0 504 Timeout Occured after Many Seconde \n" + "User-Agent: ProxyServer/1.0\r \n" + "\r\n";

                try {
                    proxyToClientBw.write(line);
                    proxyToClientBw.flush();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            } catch(Exception e){
                System.out.println("Erreur HTTPS" + urlString);
                e.printStackTrace();
            }
        }


        /**
         *
         * Écoute les données du client et les transmet au serveur.
         * Ceci est fait sur un thread séparé car cela doit être fait
         * de manière asynchrone à la lecture des données du serveur et à la transmission de
         * ces données au client.
         */

        class ClientToServerHttpsTransmit implements Runnable {

            InputStream proxyToClientIS;
            OutputStream proxyToServerOS;


        /**
         *
         * Créer un objet pour écouter le client et transmettre les données au serveur.
         * @param proxyToClientIS que le proxy utilise pour recevoir les données du client.
         * @param proxyToServerOS que le proxy utilise pour transmettre les données au serveur distant
         */
    public ClientToServerHttpsTransmit(InputStream proxyToClientIS, OutputStream proxyToServerOS) {
            this.proxyToClientIS = proxyToClientIS;
            this.proxyToServerOS = proxyToServerOS;
        }
        @Override
        public void run () {
            try {
                // Lecture octet par octet à partir du client et envoi direct au serveur
                byte[] buffer = new byte[4096]; // 4096 à se renseigner.
                int read;
                do {
                    read = proxyToClientIS.read(buffer);
                    if (read > 0) {
                        proxyToServerOS.write(buffer, 0, read);
                        if (proxyToClientIS.available() < 1) {
                            proxyToServerOS.flush();
                        }
                    }
                } while (read >= 0);
            } catch (SocketTimeoutException ste) {
                // To DO hanlde exception
            } catch (IOException e) {
                System.out.println("IOException : La lecture du proxy au client HTTPS a expiré.");
                e.printStackTrace();
            }
        }
    }
        /**
         *
         * Cette méthode est appelée lorsque l'utilisateur demande une page qui est bloquée par le proxy.
         * Renvoie un message d'accès interdit au client.
         *
         *
         */
        private void blockedSiteRequested () {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String line = "HTTP/1.0 403 accès refusé \n" + "User-Agent : ProxyServer/1.0\r \n" + "\r\n";
                bufferedWriter.write(line);
                bufferedWriter.flush();
            } catch (IOException e) {
                System.out.println("Erreur d'écriture au client lors de la demande d'un site bloqué");
                e.printStackTrace();
            }
        }

}

