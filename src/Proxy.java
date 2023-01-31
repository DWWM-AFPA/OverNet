import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ThreadFactory;


/**
 *Le proxy crée un socket de serveur qui attendra les connexions sur le port spécifié.
 *Dès qu'une connexion arrive et qu'un socket est accepté, le proxy crée un objet RequestHandler
 *sur un nouveau thread et lui passe le socket pour qu'il soit traité.
 *Cela permet au proxy de continuer à accepter d'autres connexions pendant que les autres sont traitées.
 *
 *La classe Proxy est également responsable de la gestion dynamique du proxy via la console.
 *Elle est exécutée sur un thread séparé afin de ne pas interrompre l'acceptation des connexions de socket.
 *Cela permet à l'administrateur de bloquer dynamiquement des sites web en temps réel.
 *
 *Le serveur mandataire est également responsable du maintien des copies en cache de tous les sites Web demandés par les clients.
 *Le serveur mandataire est également chargé de maintenir des copies en cache de tous les sites Web demandés par les clients, y compris les balises HTML, les images, les fichiers css et js associés à chaque page Web.
 *
 *  A la fermeture du serveur proxy, les HashMaps qui contiennent les éléments en cache et les sites bloqués sont sérialisés et * écrits dans un fichier et sont chargés dans le serveur.
 *  écrites dans un fichier et sont rechargées lorsque le proxy est redémarré, ce qui signifie que les sites mis en cache et bloqués sont maintenus.
 * Les sites mis en cache et bloqués sont donc conservés.
 *
 */

public class Proxy implements Runnable {


    // Méthode principale du programme
    public static void main(String[] args) {
        // Créer une instance du proxy et commence à écouter les connexions
        Proxy myProxy = new Proxy(8080);  // spécifier le port

        //Proxy myProxy
        //myProxy.listen();
        System.out.println();

    }




    private ServerSocket serverSocket;

    /**
     * Système de gestion de communication des mandataires et des consoles
     */

    private volatile Boolean running = true; // volatile > partager entre différent Threads.

    /**
     * Structure de données pour la recherche par ordre constant des éléments du cache.
     * Clé : URL de la page/image demandée.
     * Valeur : Fichier dans le stockage associé à cette clé.
     */

    static HashMap<String, File> cache;

    /**
     * Structure de données pour la recherche par ordre constant les sites bloqués.
     * Clé : URL de la page/image demandée.
     * Valeur : URL de la page/image demandée.
     */

    static HashMap<String, String> blockedSites;


    /**
     * ArrayList des threads qui sont actuellement en cours d'exécution et qui traitent les demandes.
     * Cette liste est nécessaire pour joindre tous les threads à la fermeture du serveur
     */

    static ArrayList<Thread> servicingThreads;

    /**
     * Création du serveur Proxy
     *
     * @param port numéro de port de l'excécution du serveur Proxy.
     */

    public Proxy(int port) {

        // Charge la Hashmap contenant les caches et les sites bloqués précédents.

        cache = new HashMap<>();
        blockedSites = new HashMap<>();

        // Création d'une ArrayList contenant les threads qui sont actuellement en cours d'exécution et
        // qui traitent les demandes.

        servicingThreads = new ArrayList<>();

        // Start thread.

        new Thread(this).start(); // Démarre la surcharge de la méthode run() ci-dessous.

        try {
            // charge les sites cachés depuis le fichier cache "cachedSites.txt".
            File cachedSites = new File("cachedSites.txt");
            if (!cachedSites.exists()) {
                System.out.println("Le fichier cache 'cachedSites.txt' n'existe pas'");
                cachedSites.createNewFile();
            } else {
                FileInputStream fis = new FileInputStream(cachedSites);
                ObjectInputStream ois = new ObjectInputStream(fis);
                cache = (HashMap<String, File>) ois.readObject();
                ois.close();
                fis.close();
            }
            // charge les sites bloqués depuis le fichier "blockedSites.txt".

            File blockedSitesTxtFile = new File("blockedSites.txt");
            if (!blockedSitesTxtFile.exists()) {
                System.out.println("Le fichier 'blockedSites.txt' n'existe pas'");
                blockedSitesTxtFile.createNewFile();
            } else {
                FileInputStream fis = new FileInputStream(blockedSitesTxtFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                blockedSites = (HashMap<String, String>) ois.readObject();
                ois.close();
                fis.close();
            }
        } catch (IOException e) {
            System.out.println("Erreur de chargement du fichier 'cachedSites.txt'");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Classe non trouvée dans le fichier 'cachedSites.txt'");
            e.printStackTrace();
        }


        try {
            // Création du Server Socket pour le Proxy.
            serverSocket = new ServerSocket(port);

            // Mise en place d'un temps de fin pour l'exécution du serveur Proxy.
            // serverSocket.setSoTimeout(5000);     // debug

            System.out.println("attente du client qu'il soit sur le port" + serverSocket.getLocalPort() + "...");
            running = true;

            // Catch exceptions associées à l'exécution du serveur Proxy.
        } catch (SocketException se) {
            System.out.println("Erreur de connexion au serveur Proxy");
            se.printStackTrace();
        } catch (SocketTimeoutException ste) {
            System.out.println("Temps de connexion écoulé");
            ste.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("io exception lors de la connexion du client");
            ioe.printStackTrace();
        }
    }

    /**
     *  Ecoute du port et accepte les nouvelles connexions de socket.
     *  Création d'un nouveau thread pour traiter la demande,lui transmet la connexion au socket et continue à écouter.
     *  Méthode d'écoute == listen()
     */

    public void listen(){
        while (running) {
            try {
                // serverSocket.accept() bloque jusqu'à qu'une connexion soit établie.
                Socket socket = serverSocket.accept();

                // Création d'un nouveau thread et lui passer le Runnable RequestHandler.
                Thread thread = new Thread(new RequestHandler(socket));

                // Clé de reférence à chaque thread afin qu'ils puissent être joints ultérieurement si nécessaire.
                servicingThreads.add(thread);
                thread.start();

            }catch (SocketException e){
                // Socket Exception est déclenchée par le système de gestion pour arrêter le Proxy.
                System.out.println("Arrêt du serveur");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Enregistrement des sites bloqués et mis en cache dans un fichier "blockedSites.txt" afin qu'il soit été rechargé
     *ultérieurement.
     * Rejoint également tous les threads du RequestHandler qui traitent actuellement les demandes.
     */
    private void closeServer() {
        System.out.println("\n Arrêt du serveur");
        running = false;
        try {
            FileOutputStream fos = new FileOutputStream("cachedSites.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(cache);
            oos.close();
            fos.close();
            System.out.println("Ecriture du cache dans le fichier 'cachedSites.txt'");

            FileOutputStream fos2 = new FileOutputStream("blockedSites.txt");
            ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
            oos2.writeObject(blockedSites);
            oos2.close();
            fos2.close();
            System.out.println("Ecriture des sites bloqués dans le fichier 'blockedSites.'");

            try {
                // Clotûre de tous les services Threads.
                for (Thread thread : servicingThreads) {
                    if (thread.isAlive()) {
                        System.out.print("En attente" + thread.getId() + "pour fermeture");
                        thread.join();
                        System.out.println(" Fermer ");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
                System.out.println("Erreur de chargement du fichier 'blockedSites.txt'");
                e.printStackTrace();
            }
            // fermeture du Server Socket.
            try {
                System.out.println("Connexion terminé");
                serverSocket.close();
            } catch (Exception e) {
                System.out.println("Exception lors de la fermeture du socket du serveur du proxy");
                e.printStackTrace();
            }

        }
        /**
         *  Consulte les fichiers en cache.
         * @param url du fichier demandé (url request).
         * @return un fichier si le fichier est mis en cache , sinon il renvoie null.
         */
        public static File getCachedPage(String url){
            return cache.get(url);
        }

    /**
     * Cherche le fichier dans le cache
     * @param url du fichier demandé
     * @return File si le fichier est dans le cache ou null si le fichier n'existe pas.
     */
    public static void addCachedPage(String urlString,File fileToCache){
        cache.put(urlString, fileToCache);
    }
    /**
     *  Vérifie si une Url est bloquée par le proxy
     * @parmam url URL à vérifier
     * @return true si la page(URL) est bloquée, false sinon.
     */
    public static boolean isBlocked(String url){
        if(blockedSites.get(url) != null){
            return true;
        } else {
        return false;
        }
    }

/**
 *  Création d'une interface de gestion qui peut mettre à jour les configurations du proxy.
 *  blocked > Listes des sites actuellement bloquées.
 *  cached > Listes des sites actuellement mis en cache.
 *  close > ferme le serveur Proxy.
 *   *  add > ajoute à la liste des sites bloquées.
 *
 */

@Override
public void run(){
    Scanner scanner = new Scanner(System.in);

    String command;
    while (running) {
        System.out.print("Entrez le nom du site à bloquer, ou tapez \"blocked\" pour afficher la liste des sites bloquées, ou tapez \"cached\" pour afficher la liste des sites mis en cache, ou \"close\" pour fermer le serveur Proxy.");
        command = scanner.nextLine();
        if (command.toLowerCase().equals("blocked")){
            System.out.println("\n Liste actuelle des sites bloquées");
            for(String key : blockedSites.keySet()){
                System.out.println(key);
            }
            System.out.println("\n");
        }
        else if (command.toLowerCase().equals("cached")){
            System.out.println("\n Liste actuelle des sites mis en cache");
            for(String key : cache.keySet()){
                System.out.println(key);
            }
            System.out.println("\n");
        }
        else if (command.toLowerCase().equals("close")){
            running = false;
            closeServer();
        }
        else {
            blockedSites.put(command, command);
            System.out.println("\n Le site " + command + " a bien été bloqué");
        }
    }
    scanner.close();
  }
}



