package Entity;
import Repository.ConnectionRepository;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.util.*;

public class DNSEntity implements Serializable {

    public static HashMap<InetAddress, URL> ipDNS;
    public static HashMap<URL, String> addressDNS;


    public static void setIpDNS(HashMap<InetAddress, URL> ipDNS) {
        DNSEntity.ipDNS = ipDNS;
        //System.err.println(ipDNS.hashCode());
    }

    public static HashMap<InetAddress, URL> getIpDNS() {
        //System.err.println(ipDNS.hashCode());
        return ipDNS;
    }

    public static void setAddressDNS(HashMap<URL, String> addressDNS) {
        DNSEntity.addressDNS = addressDNS;
    }

    public static HashMap<URL, String> getAddressDNS() {
        return addressDNS;
    }

    public DNSEntity(){
        new ConnectionEntity();

        HashMap<InetAddress, URL> ipDNS = new HashMap<>();
        HashMap<URL, String> addressDNS = new HashMap<>();

        ArrayList <DNSRowEntity> r =  ConnectionRepository.readAll();

        int size = r.size();
        for (int i = 0; i < size; i++){
            DNSRowEntity row = r.get(i);
            ipDNS.put(row.getIp(), row.getUrl());
            addressDNS.put(row.getUrl(), row.getDestination());
        }

        DNSEntity.ipDNS = ipDNS;
        DNSEntity.addressDNS = addressDNS;
    }

}
