package Entity;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.net.http.HttpResponse;
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

    public static boolean f(String ip){
        boolean retour=false;
        for (InetAddress in: ipDNS.keySet()) {
            if(Objects.equals(in.getHostAddress(), ip))
                retour=true;
        }
        return retour;
    }

}
