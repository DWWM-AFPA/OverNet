import Entity.DNSEntity;
import Entity.DNSRowEntity;
import Entity.__ProxyServS;
import Entity.__ProxyServer;
import Repository.ConnectionRepository;
import Repository.DNSRepository;

public class Client {
    public static void main(String[] args) {

        __ProxyServer test2 = new __ProxyServer();
        __ProxyServS testS = new __ProxyServS();
        DNSRepository.run();
        //DNSEntity toto = new DNSEntity();
        //test2.test("http://www.gobland.fr", "127.0.0.1", 5000);
        //test2.test("https://www.gobland.fr", "127.0.0.1", 5000);
        test2.test("8.8.8.8", "127.0.0.1", 5000);
        //test2.test("www.gobland.fr", "127.0.0.1", 5000);
        //test2.testhtml("www.google.fr", "127.0.0.1", 5000);
        //testS.testhtml("https://www.google.fr");
        //test2.test("http://www.gobland.fr");

    }


}