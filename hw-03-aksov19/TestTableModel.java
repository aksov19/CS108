import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class TestTableModel extends TestCase {

    private MyTableModel model;
    private String username = "";
    private String password = "";
    private String server   = "";
    private String database = "";
    private String testTable = "test_table1";


    protected void setUp() throws Exception{
        super.setUp();

        try{
            Class.forName("com.mysql.jdbc.Driver");

            BasicDataSource ds = new BasicDataSource();
            ds.setUrl("jdbc:mysql://" + server + "/" + database);
            ds.setUsername(username);
            ds.setPassword(password);

            MetropolisDAO dao = new MetropolisDAO(ds, testTable);
            model = new MyTableModel(dao);

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

            con.close();
            stmt.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void testColumnMethods(){
        assertEquals("Metropolis", model.getColumnName(0));
        assertEquals("Continent", model.getColumnName(1));
        assertEquals("Population", model.getColumnName(2));
        assertEquals(3, model.getColumnCount());
    }


    public void testAdd(){
        assertEquals(0, model.getRowCount());

        model.addMetropolis(new Metropolis("Tbilisi", "Europe", 1000000L));
        assertEquals(1, model.getRowCount());

        model.addMetropolis(new Metropolis(null, "Europe", 3000000L));
        assertEquals(2, model.getRowCount());

        model.addMetropolis(new Metropolis("Batumi", null, 500000L));
        assertEquals(3, model.getRowCount());

        model.addMetropolis(new Metropolis("Tokyo", "Asia", null));
        assertEquals(4, model.getRowCount());

        model.addMetropolis(new Metropolis(null, null, null));
        assertEquals(5, model.getRowCount());
    }


    public void testGetIndex(){
        model.addMetropolis(new Metropolis("Tbilisi", "Europe", 1000000L));
        assertEquals("Tbilisi", model.getValueAt(0, 0));
        assertEquals("Europe", model.getValueAt(0, 1));
        assertEquals(1000000L, model.getValueAt(0, 2));

        try{
            model.getValueAt(1000, 1000);
            fail();
        }
        catch (Exception e){
            assertEquals(e.getMessage(), "Value out of bounds");
        }

        model.addMetropolis(new Metropolis(null, null, null));
        assertEquals(null, model.getValueAt(1, 0));
        assertEquals(null, model.getValueAt(1, 1));
        assertEquals(null, model.getValueAt(1, 2));
    }


    public void testQueryAndSet() {
        model.addMetropolis(new Metropolis("Mumbai", "Asia", 20400000L));
        model.addMetropolis(new Metropolis("New York", "North America", 21295000L));
        model.addMetropolis(new Metropolis("San Francisco", "North America", 5780000L));
        model.addMetropolis(new Metropolis("London", "Europe", 8580000L));
        model.addMetropolis(new Metropolis("Rome", "Europe", 2715000L));
        model.addMetropolis(new Metropolis("Melbourne", "Australia", 3900000L));
        model.addMetropolis(new Metropolis("San Jose", "North America", 7354555L));
        model.addMetropolis(new Metropolis("Rostov-on-Don", "Europe", 1052000L));


        model.queryAndSetData(
                new Metropolis("Mumbai", null, null),
                0, 0);
        assertEquals(model.getRowCount(), 1);
        assertEquals(model.getValueAt(0, 0), "Mumbai");
        assertEquals(model.getValueAt(0, 1), "Asia");
        assertEquals(model.getValueAt(0, 2), 20400000L);

        model.queryAndSetData(
                new Metropolis(null, null, 0L),
                0, 0);
        assertEquals(model.getRowCount(), 8);

        model.queryAndSetData(
                new Metropolis(null, null, 0L),
                1, 0);
        assertEquals(model.getRowCount(), 0);

        model.queryAndSetData(
                new Metropolis("%", null, 0L),
                0, 1);
        assertEquals(model.getRowCount(), 8);

        model.queryAndSetData(
                new Metropolis("%o%", null, 0L),
                0, 1);
        assertEquals(model.getRowCount(), 7);


        // Test with null values
        model.queryAndSetData(
                new Metropolis("ASFBASKJGBASF", null, null),
                0, 0);
        assertEquals(model.getRowCount(), 0);

        model.queryAndSetData(
                new Metropolis(null, "ASFASGKJASGASF", null),
                0, 0);
        assertEquals(model.getRowCount(), 0);

        model.queryAndSetData(
                new Metropolis(null, null, -99999L),
                1, 0);
        assertEquals(model.getRowCount(), 0);

        model.queryAndSetData(
                new Metropolis(null, null, null),
                0, 0);
        assertEquals(model.getRowCount(), 0);
    }
}
