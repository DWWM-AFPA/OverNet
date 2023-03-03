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
            BufferedReader br = new BufferedReader(new FileReader(maDB));
            String s="";
            while ( (s=br.readLine()) != null) {
                String data[];
                data=s.split("###");
                id = Integer.valueOf(data[0]);
            }
            PrintWriter pw = new PrintWriter(new FileOutputStream(maDB, true));
            pw.append(id +1 + "###" + ip + "###" + address + "###" + destination);
            pw.append("\n");
            pw.close();
        }
        catch (Exception ex){
            System.err.println("Une exception a été relevé lors de la création : " + ex);
        }
    }

    public static ArrayList <DNSRowEntity> readAll(){
        ArrayList <DNSRowEntity> retour = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(maDB));
            String s = "";
            while ( (s=br.readLine()) != null){

                String data[] ;
                data= s.split("###");
                DNSRowEntity dnsRowEntity = new DNSRowEntity(Integer.valueOf(data[0]), (InetAddress) fromString(data[1]),(URL) fromString(data[2]), (String) fromString(data[3]));
                retour.add(dnsRowEntity);
            }
        }catch (Exception ex){
            System.err.println("Exception lors du readAll : " + ex);
        }
        return retour;
    }

    public static DNSRowEntity readOne(int id){
        DNSRowEntity retour = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(maDB));
            String s = "";
            while ( (s=br.readLine()) != null){
                String data[] ;
                data= s.split("###");

                if (Integer.valueOf(data[0]).equals(id)) {
                    for (int i = 0; i < data.length; i++) {
                        retour =  new DNSRowEntity(Integer.valueOf(data[0]),(InetAddress) fromString(data[1]),(URL) fromString(data[2]), (String) fromString(data[3]));
                    }
                }

            }
        }catch (Exception ex){
            System.err.println("Exception lors du readAll : " + ex);
        }
        return retour;
    }

    public static void printAll(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(maDB));
            String s = "";
            while ( (s=br.readLine()) != null){
                String data[] ;
                data= s.split("###");
                for (int i = 0; i < data.length; i++){
                    System.out.print(data[i] + " ");
                }
                System.out.println();
            }
        }catch (Exception ex){
            System.err.println("Exception lors du printAll : " + ex);
        }
    }

    public static void printOne(int id){
        try {
            BufferedReader br = new BufferedReader(new FileReader(maDB));
            String s = "";
            while ( (s=br.readLine()) != null){
                String data[] ;
                data= s.split("###");
                if (Integer.valueOf(data[0]).equals(id)) {
                    for (int i = 0; i < data.length; i++) {
                        System.out.print(data[i] + " ");
                    }
                    System.out.println();
                }
            }
        }catch (Exception ex){
            System.err.println("Exception lors du printOne : " + ex);
        }
    }

    public static void update(int id, String ip, String address, String destination){
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(maDB));
            String s = "";
            while ( (s=br.readLine()) != null){
                String data[] ;
                data= s.split("###");
                if (Integer.valueOf(data[0]).equals(id)) {
                    String row = data[0] + "###" + ip + "###" + address + "###" + destination;
                    sb.append(row);
                    sb.append("\n");
                }else{
                    sb.append(s);
                    sb.append("\n");
                }
            }
            PrintWriter pw = new PrintWriter(new FileOutputStream(maDB, false));
            pw.print(sb);
            pw.close();
        }catch (Exception ex){
            System.err.println("Exception lors de l'update : " + ex);
        }
    }

    public static void delete(int id){
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(maDB));
            String s = "";
            while ( (s=br.readLine()) != null){
                String data[] ;
                data= s.split("###");
                if (Integer.valueOf(data[0]).equals(id)) {
                    String row = "";
                    sb.append(row);
                }else{
                    sb.append(s);
                    sb.append("\n");
                }
            }
            PrintWriter pw = new PrintWriter(new FileOutputStream(maDB, false));
            pw.print(sb);
            pw.close();
        }catch (Exception ex){
            System.err.println("Exception lors du delete : " + ex);
        }
    }

    public static void deleteAll(){
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(maDB));
            while ( (br.readLine()) != null){
                    String row = "";
                    sb.append(row);
            }
            PrintWriter pw = new PrintWriter(new FileOutputStream(maDB, false));
            pw.print(sb);
            pw.close();
        }catch (Exception ex){
            System.err.println("Exception lors du deleteAll : " + ex);
        }
    }

}
