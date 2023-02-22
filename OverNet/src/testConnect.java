public class testConnect {
    public static void main(String[] args) {
        Connection__.create();
/**/
        Connection__.insert("**.***.**.**", "www.yannick.com","www.google.com");
        Connection__.insert("**.***.**.**", "www.thomas.com","www.google.com");
        Connection__.insert("**.***.**.**", "www.julien.com","www.google.com");
        Connection__.insert("**.***.**.**", "www.chacha.com","www.google.com");
        Connection__.insert("**.***.**.**", "www.florian.com","www.google.com");
        Connection__.insert("**.***.**.**", "www.yannick.com","www.google.com");
        Connection__.insert("**.***.**.**", "www.thomas.com","www.google.com");
        Connection__.insert("**.***.**.**", "www.julien.com","www.google.com");
        Connection__.insert("**.***.**.**", "www.chacha.com","www.google.com");
        Connection__.insert("**.***.**.**", "www.florian.com","www.google.com");
        Connection__.insert("**.***.**.**", "www.yannick.com","www.google.com");
        Connection__.insert("**.***.**.**", "www.thomas.com","www.google.com");
        Connection__.insert("**.***.**.**", "www.julien.com","www.google.com");
        Connection__.insert("**.***.**.**", "www.chacha.com","www.google.com");
        Connection__.insert("**.***.**.**", "www.florian.com","www.google.com");

        Connection__.readAll();
        System.out.println("--------------Avant update--------------------------------");
        Connection__.readOne(3);

        Connection__.update(3,"--.---.--.--", "www.sylvie.com","www.gobland.fr");

        System.out.println("--------------Apres update--------------------------------");
        Connection__.readOne(3);

        Connection__.delete(8);

        System.out.println("--------------Apres delete id 8 --------------------------");
        Connection__.readAll();

        //Connection__.deleteAll();

        System.out.println("--------------Apres deleteAll-----------------------------");
        Connection__.readAll();
/**/

        //Connection__.delete(12);
        Connection__.insert("**.***.**.**", "www.florian.com","www.google.com");

        Connection__.readAll();
    }
}
