import junit.framework.TestCase;


public class BoardTest extends TestCase {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	
	protected void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);
	}
	
	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	// Place sRotated into the board, then check some measures
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
		b.undo();
	}


	public void testConstructor(){
		try {
			Board bb = new Board(0, 0);
			fail("Should have thrown an exception");
		}catch (Exception e){
			assertEquals(e.getMessage(), "Board dimensions (0, 0) are not positive"); }

		try {
			Board bb = new Board(-100, 10);
			fail("Should have thrown an exception");
		}catch (Exception e){
			assertEquals(e.getMessage(), "Board dimensions (-100, 10) are not positive"); }

		try {
			Board bb = new Board(10, -100);
			fail("Should have thrown an exception");
		}catch (Exception e){
			assertEquals(e.getMessage(), "Board dimensions (10, -100) are not positive"); }
	}

	public void testPrivateVariables(){
		assertEquals(3, b.getWidth());
		assertEquals(6, b.getHeight());
		assertEquals(2, b.getMaxHeight());

		try{
			b.getColumnHeight(-100);
			fail("Should have thrown an exception");
		}catch (Exception e){
			assertEquals(e.getMessage(), "Heights out of bounds exception: x = -100"); }

		try{
			b.getColumnHeight(100);
			fail("Should have thrown an exception");
		}catch (Exception e){
			assertEquals(e.getMessage(), "Heights out of bounds exception: x = 100");	}


		try{
			b.getRowWidth(-100);
			fail("Should have thrown an exception");
		}catch (Exception e){
			assertEquals(e.getMessage(), "Widths out of bounds exception: y = -100");	}

		try{
			b.getRowWidth(100);
			fail("Should have thrown an exception");
		}catch (Exception e){
			assertEquals(e.getMessage(), "Widths out of bounds exception: y = 100");	}
	}

	public void testGetGrid(){
		assertTrue(b.getGrid(0, 0));
		assertTrue(b.getGrid(1, 0));
		assertTrue(b.getGrid(2, 0));
		assertTrue(b.getGrid(1, 1));
		assertFalse(b.getGrid(0, 1));
		assertFalse(b.getGrid(1, 2));
		assertFalse(b.getGrid(2, 5));
		assertTrue(b.getGrid(1000, 10000));
		assertTrue(b.getGrid(-10000, -10000));
	}

	public void testDropHeight(){
		assertEquals(2, b.dropHeight(sRotated, 0));
		assertEquals(1, b.dropHeight(sRotated, 1));

		try {
			b.dropHeight(sRotated, 2);
			fail("Should have thrown an exception");
		}catch (Exception e){
			assertEquals(e.getMessage(), "Piece cannot fit on the board on position 2"); }

		try {
			b.dropHeight(sRotated, 200);
			fail("Should have thrown an exception");
		}catch (Exception e){
			assertEquals(e.getMessage(), "Piece cannot fit on the board on position 200"); }

		try {
			b.dropHeight(sRotated, -1);
			fail("Should have thrown an exception");
		}catch (Exception e){
			assertEquals(e.getMessage(), "Piece cannot fit on the board on position -1");	}
	}

	public void testPlace(){
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		b.undo();

		b.commit();
		result = b.place(sRotated, 1, 4);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		b.undo();

		b.commit();
		result = b.place(sRotated, 0, 3);
		assertEquals(Board.PLACE_OK, result);
		b.undo();

		b.commit();
		result = b.place(sRotated, 0, 0);
		assertEquals(Board.PLACE_BAD, result);
		b.undo();

		b.commit();
		result = b.place(sRotated, 1, 1000);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		b.undo();

		b.commit();
		result = b.place(sRotated, -1111, 1);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		b.undo();

		b.commit();
		b.place(sRotated, -1111, 1);
		try{
			b.place(sRotated, 1, 1);
			fail("Should have thrown an exception");
		}catch (Exception e){
			assertEquals(e.getMessage(), "place commit problem");	}
		b.undo();
	}

	public void testToString(){
		assertEquals(b.toString(),
				"""
						|   |
						|   |
						|   |
						|   |
						| + |
						|+++|
						-----""");

		b.commit();
		b.place(sRotated, 1, 1);
		assertEquals(b.toString(),
				"""
						|   |
						|   |
						| + |
						| ++|
						| ++|
						|+++|
						-----""");
		b.undo();
	}

	public void testClearRows(){
		Board bb = new Board(3, 6);
		bb.place(pyr1, 0, 0);
		bb.commit();

		int rowsCleared = bb.clearRows();
		assertEquals(1, rowsCleared);
		bb.commit();
		assertEquals(bb.toString(),
				"""
						|   |
						|   |
						|   |
						|   |
						|   |
						| + |
						-----""");

		rowsCleared = bb.clearRows();
		assertEquals(0, rowsCleared);
		bb.commit();
		assertEquals(bb.toString(),
				"""
						|   |
						|   |
						|   |
						|   |
						|   |
						| + |
						-----""");


		bb.place(sRotated, 1, 0);
		bb.commit();
		Piece stick = new Piece(Piece.STICK_STR);
		bb.place(stick, 0, 0);
		bb.commit();

		rowsCleared = bb.clearRows();
		assertEquals(2, rowsCleared);
		bb.commit();
		assertEquals(bb.toString(),
				"""
						|   |
						|   |
						|   |
						|   |
						|+  |
						|++ |
						-----""");
	}
}
