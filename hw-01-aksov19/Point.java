// CS108 HW1 -- provided simple immutable Point 2-d point class
// that encapsulates a double x/y pair.
// Could also use the java.awt.Point2D class, but its
// interface is more messy.

public class Point {
	private double x;
	private double y;
	
	/**
	 * Constructs a new point.
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Copy constructor -- copies the given point.
	 * @param other
	 */
	public Point(Point other) {
		this.x = other.x;
		this.y = other.y;
	}

	/**
	 * Gets the x value.
	 * @return x
	 */
	public double getX() {
		return x;
	}


	/**
	 * Gets the y value.
	 * @return y
	 */
	public double getY() {
		return y;
	}

	
	/**
	 * Returns a new point which is dx/dy shifted
	 * from this point.
	 * @param dx
	 * @param dy
	 * @return new point dx/dy shifted from this point
	 */
	public Point shiftedPoint(double dx, double dy) {
		return new Point(x+dx, y+dy);
	}
	
	/**
	 * Returns the distance between this point an another.
	 * @param other
	 * @return distance to other point
	 */
	public double distance(Point other) {
		double x2 = Math.abs(x - other.x);
		double y2 = Math.abs(y - other.y);
		return Math.sqrt(x2*x2 + y2*y2);
	}
	
	/**
	 * Returns a "x y" string representation of the point.
	 * @return string representation
	 */
	public String toString() {
		return x + " " + y;
	}
	
	/**
	 * Compares two points. Note: uses == on x and y
	 * double values, which is a questionable practice.
	 * Consider using distance() for a more
	 * flexible way to compare two  points.
	 */
	public boolean equals(Object object) {
		if (! (object instanceof Point)) return false;
		Point other = (Point)object;
		// Note: here we == compare doubles, which is not a good practice
		return (other.x==x && other.y==y);
	}
}
