package Repository;

import Entity.ConnectionEntity;
import Entity.DNSEntity;
import Entity.DNSRowEntity;
//import Entity.HttpResponseSer;

import java.io.*;
import java.net.*;
import java.util.*;

import static Entity.DNSEntity.addressDNS;
import static Entity.DNSEntity.ipDNS;

public class DNSRepository {
    public static void run() {

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

        DNSEntity.setIpDNS(ipDNS);
        DNSEntity.setAddressDNS(addressDNS);
    }

    public static void update(int id, InetAddress ip, URL address, String destination){
        ipDNS.put(ip, address);
        DNSEntity.addressDNS.put(address, destination);
        ConnectionRepository.update(id, toString(ip), toString(address), toString(destination));
    }

    public static void insert(InetAddress ip, URL address, String destination){
        ipDNS.put(ip, address);
        DNSEntity.addressDNS.put(address, destination);
        ConnectionRepository.insert(toString(ip), toString(address), toString(destination));
    }

    public static void delete(int id, InetAddress ip, URL address, String destination){
        ipDNS.remove(ip, address);
        DNSEntity.addressDNS.remove(address, destination);
        ConnectionRepository.delete(id);
    }

    public static String toString (Serializable o){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            oos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
        catch(FileNotFoundException ex){
            System.err.println("File not found exception : "+ex);
            return null;
        }
        catch(IOException ex){
            System.err.println("IOException oos : "+ex);
            return null;
        }
        catch(SecurityException ex){
            System.err.println("SecurityException : "+ex);
            return null;
        }
    }

    public static Object fromString(Object s){

        try{
            byte[] data = Base64.getDecoder().decode( s.toString());
            ByteArrayInputStream bais = new ByteArrayInputStream(data);


            ObjectInputStream ois = new ObjectInputStream(bais);
            Object o = ois.readObject();
            ois.close();
            return o;
        }
            catch(FileNotFoundException ex){
            System.err.println("File not found exception : "+ex);
            return null;
        }
            catch(IOException ex){
                ex.printStackTrace();
                System.err.println("IOException ois : "+ex);
            return null;
        }
        catch(ClassNotFoundException ex){
            System.err.println("ClassNotFoundException : "+ex);
            return null;
        }
    }

    public static URL testIP(HashMap<InetAddress, URL> map, Object monTest) {
        URL url;
        if ((url = map.get(monTest)) == null){
            for (InetAddress inet: map.keySet()) {
                if(Objects.equals(inet.getHostAddress(), monTest)) {
                    url = map.get(inet);
                    break;
                }
            }
        }
        return url;
    }

    public static String testURL(HashMap<URL, String> map, Object monTest){
        String s = null;
        for (URL url: map.keySet()) {
            if(Objects.equals(url.getHost(),monTest)) {
                s = map.get(url);
                break;
            }
            if(Objects.equals(url, monTest)) {
                s = map.get(url);
                break;
            }
        }
        return s;
    }


    public static Object resultDNS(Object request){
        URL url = testIP(ipDNS, request);
        String dest = url != null ? addressDNS.get(url):testURL(addressDNS, request);
        return dest;
    }

    

}