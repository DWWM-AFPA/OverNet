package Appli;


import Entity.__ProxyServS;
import Entity.__ProxyServer;

public class ProxyLauncher {
    public static void main(String[] args) {

        __ProxyServer test2 = new __ProxyServer();
        __ProxyServS testS = new __ProxyServS();
        test2.runProxy(4521);
        //testS.runProxy(5000);
        //test2.runProxy(5000,15 , "10.113.28.69");

    }
}
