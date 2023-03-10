package Repository;

import Entity.DNSRowEntity;
import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;

import static Entity.ConnectionEntity.*;
import static Repository.DNSRepository.fromString;

public class ConnectionRepository {

    public static void insert(String ip, String address, String destination){
        long id = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(getMaDB()));
            String s;
            while ( (s=br.readLine()) != null) {
                String[] data;
                data=s.split("###");
                id = Integer.parseInt(data[0]);
            }
            br.close();
            PrintWriter pw = new PrintWriter(new FileOutputStream(getMaDB(), true));
            pw.append(String.valueOf(id+1)).append("###").append(ip).append("###").append(address).append("###").append(destination);
            pw.append("\n");
            pw.close();
        }
     catch (Exception ex){
            System.err.println("Une exception a été relevé lors de la création : " + ex);
        }
    }

    public static ArrayList <DNSRowEntity> readAll() {
        ArrayList <DNSRowEntity> retour = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(getMaDB()))) {

            String s;
            while ((s=br.readLine()) != null){

                String[] data= s.split("###");
                DNSRowEntity dnsRowEntity = new DNSRowEntity(Integer.parseInt(data[0]), (InetAddress) fromString(data[1]),(URL) fromString(data[2]), (String) fromString(data[3]));
                retour.add(dnsRowEntity);
            }
        }
        catch (NullPointerException ne) {
            System.err.println("Une null exception a été relevé lors de la création : " + ne);
        }
        catch (Exception ex) {
            System.err.println("Exception lors du readAll : " + ex);
        }
        return retour;
    }

    public static DNSRowEntity readOne(int id){
        for (DNSRowEntity row : readAll()) {
            if (row.getId() == id)return row;
        }
        return null;
    }

    public static void printAll(){
        ArrayList<DNSRowEntity> all = readAll();
        for (int i = 0; i< all.size(); i++)printOne(i);
    }

    public static void printOne(int id){
        for (DNSRowEntity row : readAll()) {
            if(row.getId() == id){
                String s = row.getId() + " " + row.getIp() + " " + row.getUrl() + " " + row.getDestination();
                System.out.println(s);
            }
        }

    }

    public static void update(int id, String ip, String address, String destination){
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(getMaDB()));
            String s;
            while ( (s=br.readLine()) != null){
                String[] data = s.split("###");
                if (Integer.valueOf(data[0]).equals(id)) {
                    String row = data[0] + "###" + ip + "###" + address + "###" + destination;
                    sb.append(row);
                    sb.append("\n");
                }else{
                    sb.append(s);
                    sb.append("\n");
                }
            }
            br.close();
            PrintWriter pw = new PrintWriter(new FileOutputStream(getMaDB(), false));
            pw.print(sb);
            pw.close();
        }catch (Exception ex){
            System.err.println("Exception lors de l'update : " + ex);
        }
    }

    public static void delete(int id){
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(getMaDB()));
            String s;
            while ((s=br.readLine()) != null){
                String[] data = s.split("###");
                if (Integer.valueOf(data[0]).equals(id)) {
                    String row = "";
                    sb.append(row);
                }else{
                    sb.append(s);
                    sb.append("\n");
                }
            }
            br.close();
            PrintWriter pw = new PrintWriter(new FileOutputStream(getMaDB(), false));
            pw.print(sb);
            pw.close();
        }catch (Exception ex){
            System.err.println("Exception lors du delete : " + ex);
        }
    }

    public static void deleteAll(){
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(getMaDB()));
            while ( (br.readLine()) != null){
                    String row = "";
                    sb.append(row);
            }
            br.close();
            PrintWriter pw = new PrintWriter(new FileOutputStream(getMaDB(), false));
            pw.print(sb);
            pw.close();
        }catch (Exception ex){
            System.err.println("Exception lors du deleteAll : " + ex);
        }
    }

}
