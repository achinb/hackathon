package hack.form;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ShoppingItemTest {
    @Test
    public void parsesDomain() {
        parseDomain("google.com", "google.com");
        parseDomain("google.com", "http://google.com");
        parseDomain("bestbuy.com", "http://www.bestbuy.com/site/Apple%26%23174%3B+-+iPad%AE+mini+Wi-Fi+-+16GB+-+White+%26+Silver/6208541.p?id=1218720730193&skuId=6208541");
    }

    private void parseDomain(String expectedDomain, String url) {
        assertEquals(expectedDomain, new ShoppingItem("", "", url).getDomain());
    }
}
