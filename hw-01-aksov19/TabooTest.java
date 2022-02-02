// TabooTest.java
// Taboo class tests -- nothing provided.

import java.util.*;

import junit.framework.TestCase;

import javax.swing.plaf.TabbedPaneUI;

public class TabooTest extends TestCase {

    public void testNoFollow1(){
        List<String> l  = new ArrayList<>(Arrays.asList("a", "c", "a", "b"));
        Taboo<String> t = new Taboo<>(l);

        assertEquals( t.noFollow("a"), Set.of("c", "b") );
        assertEquals( t.noFollow("d"), Set.of() );
        assertEquals( t.noFollow("c"), Set.of("a") );
        assertEquals( t.noFollow(" "), Set.of() );
    }


    public void testNoFollow2(){
        List<String> l  = new ArrayList<>(Arrays.asList("a", "a", "a", "aaa", "a", "c", "a", "b"));
        Taboo<String> t = new Taboo<>(l);

        assertEquals( t.noFollow("a"), Set.of("a", "c", "b", "aaa") );
    }

    public void testNoFollow3(){
        List<String> l  = new ArrayList<>(Arrays.asList());
        Taboo<String> t = new Taboo<>(l);

        assertEquals( t.noFollow("a"), Set.of() );
        assertEquals( t.noFollow(" "), Set.of() );
    }


    public void testReduce1(){
        List<String> rules  = new ArrayList<>(Arrays.asList("a", "c", "a", "b"));
        List<String> before = new ArrayList<>(Arrays.asList("a", "c", "b", "x", "c", "a"));
        List<String> after  = new ArrayList<>(Arrays.asList("a", "x", "c"));

        Taboo<String> t = new Taboo<>(rules);
        t.reduce(before);

        assertEquals(before, after);

    }


    public void testReduce2(){
        List<String> rules  = new ArrayList<>(Arrays.asList());
        List<String> before = new ArrayList<>(Arrays.asList("a", "c", "b", "x", "c", "a"));
        List<String> after  = new ArrayList<>(Arrays.asList("a", "c", "b", "x", "c", "a"));

        Taboo<String> t = new Taboo<>(rules);
        t.reduce(before);

        assertEquals(before, after);

    }

    public void testReduce3(){
        List<String> rules  = new ArrayList<>(Arrays.asList("a", "a", "x"));
        List<String> before = new ArrayList<>(Arrays.asList("x", "a", "a", "a", "a", "a", "a", "x", "b"));
        List<String> after  = new ArrayList<>(Arrays.asList("x", "a", "b"));

        Taboo<String> t = new Taboo<>(rules);
        t.reduce(before);

        assertEquals(before, after);

    }

}
