import junit.framework.TestCase;

// Provided testing for simple Point class.

public class PointTest extends TestCase {
	public void test1() {
		// test basic x/y/shift behavior
		Point p = new Point(1, 2);
		assertEquals(1.0, p.getX());
		assertEquals(2.0, p.getY());
		Point p2 = p.shiftedPoint(10, -10);
		assertEquals(11.0, p2.getX());
		assertEquals(-8.0, p2.getY());	
	}
	
	public void test2() {
		// test distance() and equals()
		Point p1 = new Point(1, 1);
		Point p2 = new Point(1, 4);
		assertEquals(3.0, p1.distance(p2));
		assertEquals(3.0, p2.distance(p1));
		assertFalse(p1.equals(p2));
		assertTrue(p1.equals(new Point(p1)));
		assertEquals(p1, p2.shiftedPoint(0, -3));
	}
}
