import java.io.*;

public class Connection__ {
    protected static File maDB;

    protected static boolean exist;

    public static void setMaDB(File maDB) {
        Connection__.maDB = maDB;
    }

    public static void setExist(boolean exist) {
        Connection__.exist = exist;
    }

    protected static void create(){
        maDB = new File("Overnet/maDB/Database.txt");
        setMaDB(maDB.getAbsoluteFile());
        try {
            BufferedReader br = new BufferedReader(new FileReader(maDB));
        if (br.readLine() == null) {
            try{
                PrintWriter pw = new PrintWriter(new FileOutputStream(maDB.getAbsolutePath(), false));
                pw.print("0###IP###Address###Destination\n");
                pw.close();
            }catch (Exception ex){
                System.err.println("Probleme de creation de l'entête de la table : " + ex);
            }
            //System.out.println(maDB.exists());
            setExist(maDB.exists());
        }
        }catch(Exception ex){

        }
    }

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

    public static void readAll(){
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
            System.err.println("Exception lors du readAll : " + ex);
        }
    }

    public static void readOne(int id){
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
            System.err.println("Exception lors du readOne : " + ex);
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
