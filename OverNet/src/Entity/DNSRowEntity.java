package Entity;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.http.HttpResponse;

public class DNSRowEntity {

        private int id;
        private InetAddress ip;
        private URL url;

        private String destination;

        public DNSRowEntity setId(int id) {
            this.id = id;
            return this;
        }

        public int getId() {
            return id;
        }

        public DNSRowEntity setIp(InetAddress ip) {
            this.ip = ip;
            return this;
        }

        public InetAddress getIp() {
            return ip;
        }

        public DNSRowEntity setUrl(URL url) {
            this.url = url;
            return this;
        }

        public URL getUrl() {
            return url;
        }

        public DNSRowEntity setDestination(String destination) {
            this.destination = destination;
            return this;
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

