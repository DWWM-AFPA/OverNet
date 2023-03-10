package Repository;

import Entity.DNSEntity;

import java.net.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DNSRepositoryTest {

    @org.junit.jupiter.api.Test
    void update() {
    }

    @org.junit.jupiter.api.Test
    void insert() {
    }

    @org.junit.jupiter.api.Test
    void delete() {
    }

    @org.junit.jupiter.api.Test
    void testToString() {
    }

    @org.junit.jupiter.api.Test
    void fromString() {
    }

    @org.junit.jupiter.api.Test
    void testIP() throws MalformedURLException, UnknownHostException {
        new DNSEntity();
        HashMap<InetAddress, URL> ip = DNSEntity.ipDNS;
        assertNull(DNSRepository.testIP(ip, "Test"));
        assertNull(DNSRepository.testIP(ip, "192.2.1.28"));
        assertNull(DNSRepository.testIP(ip, "flo.dns.com"));
        assertEquals(new URL("http://www.chacha.com"),DNSRepository.testIP(ip, "10.10.10.10"));
        assertEquals(new URL("http://www.chacha.com"),DNSRepository.testIP(ip, InetAddress.getByName("10.10.10.10")));
                //;



    }

    @org.junit.jupiter.api.Test
    void testURL() {
    }

    @org.junit.jupiter.api.Test
    void resultDNS() {
    }
}