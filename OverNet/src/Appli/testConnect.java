package Appli;

import Entity.DNSEntity;
import Entity.DNSRowEntity;
import Repository.ConnectionRepository;

public class testConnect {
    public static void main(String[] args) throws Exception{
        new ConnectionRepository();
        new DNSEntity();

        //Connection__.insert("8.8.8.8","www.simon.com", "www.gobland.fr");
        //Connection__.delete(18);
        //Connection__.printAll();
        //System.out.println(DNSEntity.getIpDNS());
        //System.out.println(DNSEntity.getAddressDNS());
//        try {
//            DNSRepository.insert(InetAddress.getByName("8.8.8.8"), new URL("http://www.simon.com"), "www.gobland.fr");
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }

//        InetAddress testIp = InetAddress.getByName("www.google.fr");
//        System.out.println(testIp.getHostName());
//        System.out.println(ConnectionRepository.readAll());
        for (DNSRowEntity entry : ConnectionRepository.readAll()) {

            System.out.println("Mon Id : "+entry.getId()+"\nMon Ip : " + entry.getIp().getHostAddress() + "\nMon Adresse : " + entry.getUrl() + "\nMa destination : " + entry.getDestination());
            System.out.println("-------------------------------------------");

        }
        //System.out.println("Mon Id : "+ConnectionRepository.readOne(2).getId()+"\nMon Ip : " + ConnectionRepository.readOne(2).getIp().getHostAddress() + "\nMon Adresse : " + ConnectionRepository.readOne(2).getUrl() + "\nMa destination : " + ConnectionRepository.readOne(2).getDestination());
        //System.out.println(DNSEntity.getIpDNS());
        //System.out.println(DNSEntity.getAddressDNS());
/*
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

        Connection__.printAll();
        System.out.println("--------------Avant update--------------------------------");
        Connection__.printOne(3);

        Connection__.update(3,"--.---.--.--", "www.sylvie.com","www.gobland.fr");

        System.out.println("--------------Apres update--------------------------------");
        Connection__.printOne(3);

        Connection__.delete(8);

        System.out.println("--------------Apres delete id 8 --------------------------");
        Connection__.printAll();

        //Connection__.deleteAll();

        System.out.println("--------------Apres deleteAll-----------------------------");
        Connection__.printAll();
/*

        //Connection__.delete(12);
        Connection__.insert("**.***.**.**", "www.florian.com","www.google.com");
/*


        ArrayList <Entity.DNSEntry> r =  Connection__.readAll();

            int size = r.size();
            for (int i = 1; i < size; i++){
                Entity.DNSEntry row = r.get(i);
                System.out.print(row.getId());
                System.out.print(row.getIp());
                System.out.print(row.getUrl());
                System.out.println();
            }


System.out.println(Connection__.readOne(3).getId());
        System.out.println(Connection__.readOne(3).getIp());
        System.out.println(Connection__.readOne(3).getUrl());
        System.out.println(Connection__.readOne(3).getDestination());
 */


/*



        for (HashMap.Entry<InetAddress, URL> entry : DNSEntity.getIpDNS().entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("----------------------------------------------------------------------");

        for (HashMap.Entry<URL, String> entry : DNSEntity.getAddressDNS().entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }


        ConnectionRepository.printAll();

/**/
    }
}
