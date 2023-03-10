package Entity;
import java.io.File;

public class ConnectionEntity {
    private static File maDB;

    public static File getMaDB() {
        if (maDB==null) {
            maDB = new File("Overnet/maDB/Database.txt").getAbsoluteFile();
        }
        return maDB;
    }
}
