import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;

	private Piece[] pieces;

	protected void setUp() throws Exception {
		super.setUp();
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();


		pieces = Piece.getPieces();
	}
	
	// Here are some sample tests to get you started
	
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}


	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));

		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}


	public void testParsePointsError(){
		String string = "4 5 6 6 5 1 2 3 aaaaa 5 4 8 9 9 9";
		try{
			Piece p = new Piece(string);
			fail("Should have thrown an exception");
		}
		catch (Exception e){
			assertEquals(e.getMessage(), "Could not parse x,y string:" + string);	}

		string = "1 2 3 \\";
		try{
			Piece p = new Piece(string);
			fail("Should have thrown an exception");
		}
		catch (Exception e){
			assertEquals(e.getMessage(), "Could not parse x,y string:" + string);	}
	}


	public void testGetBody(){
		TPoint[] points = new TPoint[]{
				new TPoint(0, 0),
				new TPoint(0, 1),
				new TPoint(110, 0),
				new TPoint(110, 21340),
				new TPoint(4440, 4440),
				new TPoint(666660, 111110),};

		Piece p1 = new Piece("0 0 0 1 110 0 110 21340 4440 4440 666660 111110");
		Piece p2 = new Piece(points);

		assertTrue(Arrays.equals(p1.getBody(), p2.getBody()));

		points = null;
		assertTrue(Arrays.equals(p1.getBody(), p2.getBody()));
	}


	/**
	 * Tests below test getWidth(), getHeight(), getSkirt(), fastRotation()
	 * for every basic piece
	 */
	public void testStickFull(){
		Piece p = pieces[Piece.STICK];

		// Check rotation 1
		assertEquals(1, p.getWidth());
		assertEquals(4, p.getHeight());
		assertTrue(Arrays.equals(new int[]{0}, p.getSkirt()));

		// Check rotation 2
		assertEquals(4, p.fastRotation().getWidth());
		assertEquals(1, p.fastRotation().getHeight());
		assertTrue(Arrays.equals(new int[]{0, 0, 0, 0}, p.fastRotation().getSkirt()));

		// Checks if a proper loop is created with fast rotations
		assertEquals(p, p.fastRotation().fastRotation());
		assertFalse(p.equals(p.fastRotation()));
	}


	public void testSquareFull(){
		Piece p = pieces[Piece.SQUARE];

		// Check rotation 1
		assertEquals(2, p.getWidth());
		assertEquals(2, p.getHeight());
		assertTrue(Arrays.equals(new int[]{0, 0}, p.getSkirt()));

		// Check rotation 2
		assertEquals(2, p.fastRotation().getWidth());
		assertEquals(2, p.fastRotation().getHeight());
		assertTrue(Arrays.equals(new int[]{0, 0}, p.fastRotation().getSkirt()));

		// Checks if a proper loop is created with fast rotations
		assertEquals(p, p.fastRotation());
		assertEquals(p, p.fastRotation().fastRotation());
	}


	public void testPyramidFull(){
		Piece p = pieces[Piece.PYRAMID];

		// Check rotation 1
		assertEquals(3, p.getWidth());
		assertEquals(2, p.getHeight());
		assertTrue(Arrays.equals(new int[]{0, 0, 0}, p.getSkirt()));

		// Check rotation 2
		assertEquals(2, p.fastRotation().getWidth());
		assertEquals(3, p.fastRotation().getHeight());
		assertTrue(Arrays.equals(new int[]{1, 0}, p.fastRotation().getSkirt()));

		// Check rotation 3
		assertEquals(3, p.getWidth());
		assertEquals(2, p.getHeight());
		assertTrue(Arrays.equals(new int[]{1, 0, 1}, p.fastRotation().fastRotation().getSkirt()));

		// Check rotation 4
		assertEquals(2, p.fastRotation().getWidth());
		assertEquals(3, p.fastRotation().getHeight());
		assertTrue(Arrays.equals(new int[]{0, 1}, p.fastRotation().fastRotation().fastRotation().getSkirt()));

		// Checks if a proper loop is created with fast rotations
		assertEquals(p, p.fastRotation().fastRotation().fastRotation().fastRotation());
	}


	public void testL1Full(){
		Piece p = pieces[Piece.L1];

		// Check rotation 1
		assertEquals(2, p.getWidth());
		assertEquals(3, p.getHeight());
		assertTrue(Arrays.equals(new int[]{0, 0}, p.getSkirt()));

		// Check rotation 2
		assertEquals(3, p.fastRotation().getWidth());
		assertEquals(2, p.fastRotation().getHeight());
		assertTrue(Arrays.equals(new int[]{0, 0, 0}, p.fastRotation().getSkirt()));

		// Check rotation 3
		assertEquals(2, p.fastRotation().fastRotation().getWidth());
		assertEquals(3, p.fastRotation().fastRotation().getHeight());
		assertTrue(Arrays.equals(new int[]{2, 0}, p.fastRotation().fastRotation().getSkirt()));

		// Check rotation 4
		assertEquals(3, p.fastRotation().fastRotation().fastRotation().getWidth());
		assertEquals(2, p.fastRotation().fastRotation().fastRotation().getHeight());
		assertTrue(Arrays.equals(new int[]{0, 1, 1}, p.fastRotation().fastRotation().fastRotation().getSkirt()));

		// Checks if a proper loop is created with fast rotations
		assertEquals(p, p.fastRotation().fastRotation().fastRotation().fastRotation());
	}


	public void testL2Full(){
		Piece p = pieces[Piece.L2];

		// Check rotation 1
		assertEquals(2, p.getWidth());
		assertEquals(3, p.getHeight());
		assertTrue(Arrays.equals(new int[]{0, 0}, p.getSkirt()));

		// Check rotation 2
		assertEquals(3, p.fastRotation().getWidth());
		assertEquals(2, p.fastRotation().getHeight());
		assertTrue(Arrays.equals(new int[]{1, 1, 0}, p.fastRotation().getSkirt()));

		// Check rotation 3
		assertEquals(2, p.fastRotation().fastRotation().getWidth());
		assertEquals(3, p.fastRotation().fastRotation().getHeight());
		assertTrue(Arrays.equals(new int[]{0, 2}, p.fastRotation().fastRotation().getSkirt()));

		// Check rotation 4
		assertEquals(3, p.fastRotation().fastRotation().fastRotation().getWidth());
		assertEquals(2, p.fastRotation().fastRotation().fastRotation().getHeight());
		assertTrue(Arrays.equals(new int[]{0, 0, 0}, p.fastRotation().fastRotation().fastRotation().getSkirt()));

		// Checks if a proper loop is created with fast rotations
		assertEquals(p, p.fastRotation().fastRotation().fastRotation().fastRotation());
	}


	public void testS1Full(){
		Piece p = pieces[Piece.S1];

		// Check rotation 1
		assertEquals(3, p.getWidth());
		assertEquals(2, p.getHeight());
		assertTrue(Arrays.equals(new int[]{0, 0, 1}, p.getSkirt()));

		// Check rotation 2
		assertEquals(2, p.fastRotation().getWidth());
		assertEquals(3, p.fastRotation().getHeight());
		assertTrue(Arrays.equals(new int[]{1, 0}, p.fastRotation().getSkirt()));

		// Checks if a proper loop is created with fast rotations
		assertEquals(p, p.fastRotation().fastRotation());
		assertEquals(p.fastRotation(), p.fastRotation().fastRotation().fastRotation());
	}


	public void testS2Full(){
		Piece p = pieces[Piece.S2];

		// Check rotation 1
		assertEquals(3, p.getWidth());
		assertEquals(2, p.getHeight());
		assertTrue(Arrays.equals(new int[]{1, 0, 0}, p.getSkirt()));

		// Check rotation 2
		assertEquals(2, p.fastRotation().getWidth());
		assertEquals(3, p.fastRotation().getHeight());
		assertTrue(Arrays.equals(new int[]{0, 1}, p.fastRotation().getSkirt()));

		// Checks if a proper loop is created with fast rotations
		assertEquals(p, p.fastRotation().fastRotation());
		assertEquals(p.fastRotation(), p.fastRotation().fastRotation().fastRotation());
	}

}
