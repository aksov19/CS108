
// Test cases for CharGrid -- a few basic tests are provided.

import junit.framework.TestCase;

public class CharGridTest extends TestCase {
	
	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
			};
		
		
		CharGrid cg = new CharGrid(grid);
				
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
	}
	
	
	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
	}


	public void testCharArea3(){
		char[][] grid = new char[][] {
				{'c', 'a', ' ', 'a', '\n'},
				{'b', ' ', 'b', 'c', ' '},
				{' ', ' ', 'a', 'x', 'p'}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(9, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(8, cg.charArea('c'));
		assertEquals(15, cg.charArea(' '));
		assertEquals(1, cg.charArea('p'));
		assertEquals(0, cg.charArea('l'));
	}
	
	
	public void testCountPlus1(){
		char[][] grid = new char[][] {
				{' ', ' ', ' '},
				{' ', 'p', ' '},
				{' ', ' ', ' '}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(0, cg.countPlus());
	}

	public void testCountPlus2(){
		char[][] grid = new char[][] {
				{' ', 'p', ' '},
				{'p', 'p', 'p'},
				{' ', 'p', ' '}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(1, cg.countPlus());
	}

	public void testCountPlus3(){
		char[][] grid = new char[][] {
				{' ', 'p', 'p', ' '},
				{'p', 'p', 'p', 'p'},
				{' ', 'p', 'p', ' '}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(0, cg.countPlus());
	}


	public void testCountPlus4(){
		char[][] grid = new char[][] {
				{' ', 'p', ' ', ' '},
				{'p', 'p', 'p', ' '},
				{' ', 'p', 'x', ' '},
				{' ', 'x', 'x', 'x'},
				{' ', ' ', 'x', ' '}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(2, cg.countPlus());
	}


	public void testCountPlus5(){
		char[][] grid = new char[][] {
				{' ', ' ', 'p', ' ', ' ', ' ', ' ', ' ', ' '},
				{' ', ' ', 'p', ' ', ' ', ' ', ' ', 'x', ' '},
				{'p', 'p', 'p', 'p', 'p', ' ', 'x', 'x', 'x'},
				{' ', ' ', 'p', ' ', ' ', 'y', ' ', 'x', ' '},
				{' ', ' ', 'p', ' ', 'y', 'y', 'y', ' ', ' '},
				{'z', 'z', 'z', 'z', 'z', 'y', 'z', 'z', 'z'},
				{' ', 'x', 'x', ' ', ' ', 'y', ' ', ' ', ' '},
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(2, cg.countPlus());
	}
	
}
