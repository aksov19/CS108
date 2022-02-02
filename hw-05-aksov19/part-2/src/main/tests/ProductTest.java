import hw05.Product;
import junit.framework.TestCase;

public class ProductTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }


    public void testBasic(){
        Product p = new Product("aa", "bb", "cc", 100);
        assertEquals(p.getId(), "aa");
        assertEquals(p.getName(), "bb");
        assertEquals(p.getImageFile(), "store-images/cc");
        assertEquals(p.getPrice(), 100.0);

        p = new Product(null, "bb", "cc", 100.1);
        assertEquals(p.getId(), null);
        assertEquals(p.getName(), "bb");
        assertEquals(p.getImageFile(), "store-images/cc");
        assertEquals(p.getPrice(), 100.1);

        p = new Product("aa", "bb", null, 100);
        assertEquals(p.getId(), "aa");
        assertEquals(p.getName(), "bb");
        assertEquals(p.getImageFile(), "store-images/null");
        assertEquals(p.getPrice(), 100.0);

        p = new Product("aa", null, "cc", 0);
        assertEquals(p.getId(), "aa");
        assertEquals(p.getName(), null);
        assertEquals(p.getImageFile(), "store-images/cc");
        assertEquals(p.getPrice(), 0.0);
    }

}