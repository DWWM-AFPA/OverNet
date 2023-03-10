package Entity;

import java.net.InetAddress;
import java.net.URL;

public class DNSRowEntity {

        private int id;
        private InetAddress ip;
        private URL url;

        private String destination;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setIp(InetAddress ip) {
            this.ip = ip;
        }

        public InetAddress getIp() {
            return ip;
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

        public DNSRowEntity(int id, InetAddress ip, URL url, String destination){
            this.setId(id);
            this.setIp(ip);
            this.setUrl(url);
            this.setDestination(destination);
        }
        public DNSRowEntity(InetAddress ip, URL url, String destination){
            this.setIp(ip);
            this.setUrl(url);
            this.setDestination(destination);
        }
    }

