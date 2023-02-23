import java.net.URL;

public class Annuaire {

    private int iD;
    private String iP;
    private URL url;
    private String destination;



    public void setiD(int iD) {
        this.iD = iD;
    }

    public int getiD() {
        return iD;
    }

    public void setiP(String iP) {
        this.iP = iP;
    }

    public String getiP() {
        return iP;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }


    public static void main(String[] args) {

    }


    public void monDNS(int id, String ip, URL url, String destination){
        this.setiD(id);
        this.setiP(ip);
        this.setUrl(url);
        this.setDestination(destination);
    }
}
