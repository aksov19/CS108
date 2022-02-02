import hw05.ProductListDAO;
import junit.framework.TestCase;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.*;

public class ProductListDAOTest extends TestCase {

    private final static String testTable = "test_table";
    private final static String username = "aksov19";
    private final static String password = "achi2001";
    private final static String server   = "localhost";
    private final static String database = "uni_oop";


    protected void setUp() throws Exception {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:mysql://" + server + "/" + database);
        ds.setUsername(username);
        ds.setPassword(password);

        // Drop test table if it exists
        Connection con = ds.getConnection();
        PreparedStatement stmt = con.prepareStatement("DROP TABLE IF EXISTS " + database + "." + testTable + ";");
        stmt.execute();

        // Create a new test table
        stmt = con.prepareStatement("CREATE TABLE " + testTable + "  (\n" +
                "    metropolis CHAR(64),\n" +
                "    continent CHAR(64),\n" +
                "    population BIGINT\n" +
                ");");
        stmt.execute();
        super.setUp();
    }


    public void testBasic(){
        ProductListDAO dao = new ProductListDAO(testTable);
        assertEquals(dao.getProducts().size(), 0);

        dao = new ProductListDAO();
        assertEquals(dao.getProducts().size(), 14);
        assertEquals(dao.getProductById("TLCh").getName(), "Chinese Tee");
        assertEquals(dao.getProductById("TLAr").getId(), "TLAr");
        assertEquals(dao.getProductById("TLHe").getImageFile(), "store-images/HebrewTShirt.jpg");
        assertEquals(dao.getProductById("ALn").getPrice(), (Double)5.95);

        assertEquals(dao.getProductById("UNUSANFASFLKSAF"), null);
    }

}