package Entity;
import java.io.File;

public class ConnectionEntity {
    public static File maDB;

    public static boolean exist;

    public static void setExist(boolean exist) {
        ConnectionEntity.exist = exist;
    }

    public ConnectionEntity(){
        if (!exist) {
            maDB = new File("Overnet/maDB/Database.txt").getAbsoluteFile();
            exist = true;
        }
    }
}
