import junit.framework.TestCase;

public class MetropolisTest extends TestCase {

    private Metropolis m;

    protected void setUp() throws Exception{
        super.setUp();
    }

    public void testBasic(){
        m = new Metropolis("aaa", "bbb", 20L);
        assertEquals(m.getMetropolis(), "aaa");
        assertEquals(m.getContinent(), "bbb");
        assertEquals(m.getPopulation().longValue(), 20);

        m = new Metropolis(null, null, null);
        assertEquals(m.getMetropolis(), null);
        assertEquals(m.getContinent(), null);
        assertEquals(m.getPopulation(), null);
    }
}
