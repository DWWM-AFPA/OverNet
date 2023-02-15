
public class Client {
    public static void main(String[] args) {

        __ProxyServer test2 = new __ProxyServer();
        __ProxyServS testS = new __ProxyServS();
        //test2.test("http://www.gobland.fr", "127.0.0.1", 5000);
        //test2.test("https://www.gobland.fr", "127.0.0.1", 5000);
        //test2.test("www.gobland.fr", "127.0.0.1", 5000);
        //test2.testhtml("www.google.fr", "127.0.0.1", 5000);
        testS.testhtml("https://www.google.fr");
        //test2.test("http://www.gobland.fr");

    }


}