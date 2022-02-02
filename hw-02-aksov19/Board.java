// Board.java

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some vars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;

	private int[] widths;
	private int[] heights;
	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		if(width <= 0 || height <= 0)
			throw new RuntimeException("Board dimensions (" + width + ", " + height + ") are not positive");

		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;
		
		// YOUR CODE HERE
		widths = new int[height];
		heights = new int[width];

		backup_grid = new boolean[width][height];
		backup_widths = new int[height];
		backup_heights = new int[width];
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		int ans = heights[0];
		for(int h : heights){
			ans = Math.max(ans, h);
		}
		return ans;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			// Check widths
			for(int j = 0; j < height; j++){
				int w = 0;
				for(int i = 0; i < width; i++){
					w += grid[i][j] ? 1 : 0;
				}

				if( w != widths[j] ){ throw new RuntimeException("Widths array at position " + j + ": Expected " + widths[j] + ", got " + w + "." + "\n" + this.toString()); }
			}

			// Check heights
			for(int i = 0; i < width; i++){
				int h = 0;
				for(int j = 0; j < height; j++){
					if(grid[i][j]){
						h = j+1;
					}
				}

				if( h != heights[i] ){ throw new RuntimeException("Heights array at position " + i + "Expected " + heights[i] + ", got " + h + "." + "\n" + this.toString());
				}
			}
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		if(x + piece.getWidth() > width || x < 0)
			throw new RuntimeException("Piece cannot fit on the board on position " + x);

		int maxH = 0;
		int[] skirt = piece.getSkirt();
		for(int i = 0; i < skirt.length; i++){
			maxH = Math.max(maxH, heights[x + i] - skirt[i]);
		}
		return maxH;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		if(x < 0 || x >= width)
			throw new RuntimeException("Heights out of bounds exception: x = " + x);

		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		if(y < 0 || y >= height)
			throw new RuntimeException("Widths out of bounds exception: y = " + y);

		return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if( !inBounds(x, y) ) return true;
		return grid[x][y];
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");

		committed = false;
		int result = PLACE_OK;
		backup();
		
		// YOUR CODE HERE
		for(TPoint p : piece.getBody()){
			if( !inBounds(x + p.x, y + p.y) ){
				result = PLACE_OUT_BOUNDS;
				break;
			}

			if( grid[x + p.x][y + p.y] ){
				result = PLACE_BAD;
				break;
			}

			grid[x + p.x][y + p.y] = true;
			widths[y + p.y]++;
			// This works because the points are sorted by x first, then y (ascending)
			heights[x + p.x] = y + p.y + 1;

			if(widths[y + p.y] == width){
				result = PLACE_ROW_FILLED;
			}
		}

		sanityCheck();
		return result;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;

		committed = false;

		for(int j = 0; j < height; j++){
			if(widths[j] == width){
				// Shift the grid and widths array down by one
				for(int jj = j; jj < height-1; jj++){
					for(int ii = 0; ii < width; ii++){
						grid[ii][jj] = grid[ii][jj+1];
						widths[jj] = widths[jj+1];
					}
				}
				// Set top row to empty
				for(int ii = 0; ii < width; ii++){
					grid[ii][height-1] = false;
					widths[height-1] = 0;
				}

				// Adjust heights
				for(int ii = 0; ii < width; ii++){
					for(int jj = heights[ii]-1; jj >= 0 && !grid[ii][jj]; jj--){
						heights[ii]--;
					}
				}

				rowsCleared++;
				j--;
			}
		}

		sanityCheck();
		return rowsCleared;
	}



	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if (committed) return;

		committed = true;

		boolean[][] tmp_grid = grid;
		grid = backup_grid;
		backup_grid = tmp_grid;

		int[] tmp_w = widths;
		widths = backup_widths;
		backup_widths = tmp_w;

		int[] tmp_h = heights;
		heights = backup_heights;
		backup_heights = tmp_h;

		sanityCheck();
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}



	/**
	 *	Checks if the given x, y coordinates are within the bounds
	 *	of the tetris grid.
	 */
	private boolean inBounds(int x, int y){
		return !( x < 0 || x >= width || y < 0 || y >= height );
	}


	/**
	 *	Creates backups for the tetris grid, widths and heights array.
	 */
	private void backup(){
		for(int i = 0; i < grid.length; i++){
			System.arraycopy(grid[i], 0, backup_grid[i], 0, grid[i].length);
		}
		System.arraycopy(widths, 0, backup_widths, 0, widths.length);
		System.arraycopy(heights, 0, backup_heights, 0, heights.length);
	}


	private boolean[][] backup_grid;
	private int[] backup_widths;
	private int[] backup_heights;
}


