package clientProxy.src;

import src.monProxy;

public class Main {
    public static void main(String[] args) {

        monProxy test2 = new monProxy();
        // test2.run("http://www.gobland.fr", "127.0.0.1", 5000);
        // test2.run("https://www.gobland.fr", "127.0.0.1", 5000);
        test2.run("www.gobland.fr", "127.0.0.1", 5000);
        // test2.runHtml("www.gobland.fr", "127.0.0.1", 5000);
        // test2.run("http://www.gobland.fr");

    }

}