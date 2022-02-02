//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

public class TetrisGrid {
	private boolean[][] grid = null;
	
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}
	
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		int rowLen = this.grid.length;
		int height = this.grid[0].length;

		for(int j = 0; j < height; j++){
			int blocksOnRow = 0;
			for(int i = 0; i < rowLen; i++){
				if( this.grid[i][j] ) blocksOnRow++;
			}

			if( blocksOnRow == rowLen ){
				// Delete row
				for(int ii = 0; ii < rowLen; ii++) this.grid[ii][j] = false;

				// Move other rows down
				for(int jj = j; jj < height - 1; jj++){
					for(int ii = 0; ii < rowLen; ii++){
						this.grid[ii][jj] = this.grid[ii][jj+1];
					}
				}
				for(int ii = 0; ii < rowLen; ii++) this.grid[ii][height - 1] = false;

				j--;
			}
		}
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return this.grid; // YOUR CODE HERE
	}
}
