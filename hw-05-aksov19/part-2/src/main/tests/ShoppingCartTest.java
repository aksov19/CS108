import hw05.ShoppingCart;
import junit.framework.TestCase;

public class ShoppingCartTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testBasic(){
        ShoppingCart c = new ShoppingCart();

        c.addItem("aa");
        assertEquals(c.getItems().get("aa"), (Integer) 1);
        c.addItem("aa");
        assertEquals(c.getItems().get("aa"), (Integer) 2);
        c.addItem("aa");
        assertEquals(c.getItems().get("aa"), (Integer) 3);
        c.addItem("aa");
        assertEquals(c.getItems().get("aa"), (Integer) 4);

        c.setItemCount("aa", 2);
        assertEquals(c.getItems().get("aa"), (Integer) 2);

        c.setItemCount("aa", 0);
        assertEquals(c.getItems().get("aa"), null);

        c.addItem("aa");
        c.setItemCount("aa", -100);
        assertEquals(c.getItems().get("aa"), null);
    }
}
