/*
 * CREATION PROXY EN PARTANT DE RIEN, CODER PAR CHARLOTTE,
 * LA REPONSE RENVOIE BIEN LE CODE HTML DU SITE EN CONSOLE.
 * 
 * package afpa.proxy;
 * 
 * import java.io.IOException;
 * import java.io.InputStream;
 * import java.io.OutputStream;
 * import java.net.*;
 * 
 * public class Myproxy implements Runnable {
 * /*
 * // initialisation du proxy, il écoute en permanence le navigateur et le
 * serveur
 * public static void main(String[] args) {
 * Myproxy myproxy = new Myproxy();
 * Thread thProxy = new Thread(myproxy);
 * thProxy.start();
 * }
 * 
 * // socket coté client
 * 
 * public static void main(String[] args) throws IOException {
 * Socket s = new Socket("www.help-info33.fr", 80);
 * // timeout en cas de saturation du serveur
 * // s.setSoTimeout(1000);
 * // récupération des flux
 * OutputStream oStream = s.getOutputStream();
 * InputStream iStream = s.getInputStream();
 * // retourne un champ en byte correspondant à l''adresse
 * byte[] b = new byte[1000];
 * 
 * // HEAD protocole utiliser + nom de domaine
 * String g = "GET / HTTP/1.1\n" + "Host: www.help-info33.fr\n\n";
 * 
 * try {
 * 
 * oStream.write(g.getBytes());
 * 
 * int bitsRecus = 0;
 * while ((bitsRecus = iStream.read(b)) >= 0) {
 * 
 * System.out.println("On a reçu : " + bitsRecus + " bits");
 * System.out.println("Recu : " + new String(b, 0, bitsRecus));
 * }
 * } catch (Exception e) {
 * e.printStackTrace();
 * } finally {
 * 
 * // fermeture des flux et des sockets
 * oStream.close();
 * iStream.close();
 * s.close();
 * }
 * 
 * }
 * 
 * @Override
 * public void run() {
 * //TODO
 * }
 * // coté serveur
 * /*
 * public class ServerSocket {
 * 
 * // Le proxy écoute le client sur le port local et la socket accepte la
 * connexion
 * // demandée par le serveur
 * ServerSocket myserver = new ServerSocket("80");while(!interrupted()) {
 * // la méthode accepte attend l'obtention d'une connexion extérieure
 * Socket client = myserver.accept();
 * ProxyServerClientThread proxyServerClientThread = new
 * ProxyServerClientTread(client);
 * thProxyServerClient.start();
 * sleep(5);
 * }
 * }
 * 
 * @Override
 * public void run() {
 * 
 * }
 * }
 */