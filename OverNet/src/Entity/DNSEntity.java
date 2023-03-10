package Entity;
import Repository.ConnectionRepository;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.util.*;

public class DNSEntity implements Serializable {

    public static HashMap<InetAddress, URL> ipDNS;
    public static HashMap<URL, String> addressDNS;

    public DNSEntity(){

        HashMap<InetAddress, URL> ipDNS = new HashMap<>();
        HashMap<URL, String> addressDNS = new HashMap<>();

        ArrayList <DNSRowEntity> r =  ConnectionRepository.readAll();

        for (DNSRowEntity row : r) {
            ipDNS.put(row.getIp(), row.getUrl());
            addressDNS.put(row.getUrl(), row.getDestination());
        }

        DNSEntity.ipDNS = ipDNS;
        DNSEntity.addressDNS = addressDNS;
    }

}
