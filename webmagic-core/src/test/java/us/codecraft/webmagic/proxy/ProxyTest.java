package us.codecraft.webmagic.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author yxssfxwzy@sina.com May 30, 2014
 * 
 */
public class ProxyTest {

    private static List<String[]> httpProxyList = new ArrayList<String[]>();

    @BeforeClass
    public static void before() {
        // String[] source = { "0.0.0.1:0", "0.0.0.2:0", "0.0.0.3:0",
        // "0.0.0.4:0" };
        String[] source = { "::0.0.0.1:0", "::0.0.0.2:0", "::0.0.0.3:0", "::0.0.0.4:0" };
        for (String line : source) {
            httpProxyList.add(new String[] {line.split(":")[0], line.split(":")[1], line.split(":")[2], line.split(":")[3] });
        }
    }

    class Fetch extends Thread {
        HttpHost hp;

        public Fetch(HttpHost hp) {
            this.hp = hp;
        }

        @Override
        public void run() {
            try {
                System.out.println("fetch web page use proxy: " + hp.getHostName() + ":" + hp.getPort());
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testCreate() {
        Proxy proxy = Proxy.create(URI.create("//127.0.0.1:8080"));
        assertNull(proxy.getScheme());
        assertNull(proxy.getUsername());
        assertNull(proxy.getPassword());
        assertEquals("127.0.0.1", proxy.getHost());
        assertEquals(8080, proxy.getPort());

        proxy = Proxy.create(URI.create("http://127.0.0.1:8080"));
        assertEquals("http", proxy.getScheme());
        assertNull(proxy.getUsername());
        assertNull(proxy.getPassword());
        assertEquals("127.0.0.1", proxy.getHost());
        assertEquals(8080, proxy.getPort());

        proxy = Proxy.create(URI.create("//username:password@127.0.0.1:8080"));
        assertNull(proxy.getScheme());
        assertEquals("username", proxy.getUsername());
        assertEquals("password", proxy.getPassword());
        assertEquals("127.0.0.1", proxy.getHost());
        assertEquals(8080, proxy.getPort());

        proxy = Proxy.create(URI.create("//username@127.0.0.1:8080"));
        assertNull(proxy.getScheme());
        assertEquals("username", proxy.getUsername());
        assertNull(proxy.getPassword());
        assertEquals("127.0.0.1", proxy.getHost());
        assertEquals(8080, proxy.getPort());

        proxy = Proxy.create(URI.create("//:password@127.0.0.1:8080"));
        assertNull(proxy.getScheme());
        assertNull(proxy.getUsername());
        assertEquals("password", proxy.getPassword());
        assertEquals("127.0.0.1", proxy.getHost());
        assertEquals(8080, proxy.getPort());
    }

    @Test
    public void testToString() {
        assertEquals("//127.0.0.1:8080", new Proxy("127.0.0.1", 8080).toString());
        assertEquals("http://127.0.0.1:8080", new Proxy("127.0.0.1", 8080, "http").toString());
        assertEquals("//username:password@127.0.0.1:8080", new Proxy("127.0.0.1", 8080, "username", "password").toString());
        assertEquals("//username@127.0.0.1:8080", new Proxy("127.0.0.1", 8080, "username", null).toString());
        assertEquals("//:password@127.0.0.1:8080", new Proxy("127.0.0.1", 8080, null, "password").toString());
    }

    @Test
    public void testEquals(){
        String otherObject = "//127.0.0.1:8080";
        Proxy proxyInit =  new Proxy("127.0.0.1", 8080, "username", "password");
        Proxy proxyInitCpy =  new Proxy("127.0.0.1", 8080, "username", "password");

        //Test equals with the same object
        assertTrue(proxyInit.equals(proxyInit));
        //Test with identical Proxy
        assertTrue((proxyInit.equals(proxyInitCpy)));
        //Test equals with an object with other type
        assertFalse(proxyInit.equals(otherObject));

        //Test equals with a different port
        Proxy pWDPort = new Proxy("127.0.0.1", 8000, "username", "password");
        assertFalse(proxyInit.equals(pWDPort));

        //Test equals with username and password are null
        Proxy pWithoutUsernameAndPassWord = new Proxy("127.0.0.1", 8080);
        assertFalse(proxyInit.equals(pWithoutUsernameAndPassWord));

        //Test with different host
        Proxy pWDHost = new Proxy("127.0.0.15", 8080, "username", "password");
        assertFalse(proxyInit.equals(pWDHost));

        //Test equals with different username
        Proxy pWDUsername = new Proxy("127.0.0.1", 8080, "patrick", "password");
        assertFalse(proxyInit.equals(pWDUsername));

        //Test equals with different password
        Proxy pWDPassword = new Proxy("127.0.0.1", 8080, "username", "pass");
        assertFalse(proxyInit.equals(pWDPassword));

        //Test equals with different scheme and scheme is null
        proxyInit.setScheme("scheme");
        assertFalse(proxyInit.equals(proxyInitCpy));
        proxyInitCpy.setScheme("sc");
        assertFalse(proxyInit.equals(proxyInitCpy));
        proxyInitCpy.setScheme("scheme");
        assertTrue((proxyInit.equals(proxyInitCpy)));



    }


}
