import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}



	// Added variables
	private long timeElapsed;
	private Spot[][] grid;
	private ArrayList<Spot> spotList;
	private String solutionString;
	int solutions;
	

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		this.grid = new Spot[SIZE][SIZE];
		this.spotList = new ArrayList<>();
		timeElapsed = 0;
		solutions = 0;
		solutionString = null;

		// Copy ints to grid array
		// Also adds empty spots to an array list (used in solve())
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				grid[i][j] = new Spot(i, j, ints[i][j]);
				if(ints[i][j] == 0) spotList.add(grid[i][j]);
			}
		}
	}


	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		timeElapsed = 0;
		solutions = 0;
		solutionString = null;

		long tick = System.currentTimeMillis();

		// Sort empty spots by their legal number count (descending)
		for(Spot s : spotList){
			s.getLegalNumbers();
		}
		spotList.sort(new Comparator<Spot>() {
			@Override
			public int compare(Spot o1, Spot o2) {
				return o2.legalNumbers.size() - o1.legalNumbers.size();
			}
		});

		// start solving process
		solveHelper(spotList.size());

		long tock = System.currentTimeMillis();
		this.timeElapsed = tock-tick;

		return solutions;
	}

	private void solveHelper(int emptySpotsLeft){
		if(solutions >= MAX_SOLUTIONS) return;
		if(emptySpotsLeft == 0){
			solutions++;
			if(solutionString == null) solutionString = this.toString();
			return;
		}

		Spot minSpot = spotList.get(emptySpotsLeft-1);
		minSpot.getLegalNumbers();

		for(Integer i : minSpot.legalNumbers){
			minSpot.val = i;
			solveHelper(emptySpotsLeft-1);
			minSpot.val = 0;
		}
	}
	
	public String getSolutionText() {
		return solutionString;
	}
	
	public long getElapsed() {
		return timeElapsed;
	}


	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++) {
				str.append(grid[i][j].val);
				str.append(" ");
			}
			str.append("\n");
		}
		return str.toString();
	}



	// Spot class
	private class Spot{
		public int ii;
		public int jj;
		public int val;
		public HashSet<Integer> legalNumbers;


		public Spot(int ii, int jj, int val){
			this.ii = ii;
			this.jj = jj;
			this.val = val;
		}


		public HashSet<Integer> getLegalNumbers(){
			/*if(this.val != 0) {
				this.legalNumbers = new HashSet<>();
				return this.legalNumbers;
			}*/

			HashSet<Integer> nums = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

			// Remove numbers from the same row and column
			for(int k = 0; k < SIZE; k++){
				nums.remove(grid[k][jj].val);
				nums.remove(grid[ii][k].val);
			}
			// Remove numbers from the 3x3 grid
			for(int i = 0, si = ii/PART * PART; i < PART; i++){
				for(int j = 0, sj = jj/PART * PART; j < PART; j++){
					nums.remove(grid[si + i][sj + j].val);
				}
			}

			this.legalNumbers = nums;
			return this.legalNumbers;
		}
	}
}
