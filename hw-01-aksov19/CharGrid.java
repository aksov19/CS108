// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		boolean hasChar = false;
		int topI = this.grid.length, topJ = this.grid[0].length;
		int botI = 0, botJ = 0;

		for(int i = 0; i < this.grid.length; i++){
			for(int j = 0; j < this.grid[i].length; j++){
				if( this.grid[i][j] == ch ){
					if( !hasChar ){
						hasChar = true;
						topI = botI = i;
						topJ = botJ = j;
					}
					else{
						if( i < topI ) topI = i;
						if( i > botI ) botI = i;
						if( j < topJ ) topJ = j;
						if( j > botJ ) botJ = j;
					}
				}
			}
		}

		if( hasChar ) return (botI- topI + 1) * (botJ - topJ + 1);
		return 0; // YOUR CODE HERE
	}
	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int ans = 0;
		boolean is_plus = true;

		for(int i = 1; i < this.grid.length - 1; i++){
			for(int j = 1; j < this.grid[i].length - 1; j++){

				int length = getArmLength(i, j, 1, 0);
				if( getArmLength(i, j, -1, 0) != length ||
					getArmLength(i, j, 0, 1)  != length ||
					getArmLength(i, j, 0, -1) != length )
				{
					is_plus = false;
				}

				if( is_plus && length >= 2 ) ans++;
				is_plus = true;
			}
		}

		return ans; // YOUR CODE HERE
	}


	private int getArmLength(int i, int j, int dirI, int dirJ){
		int length = 0;

		if( dirI != 0 && dirJ == 0 ){
			for(int ii = i; ii >= 0 && ii < this.grid.length; ii += dirI ){
				if( this.grid[ii][j] == this.grid[i][j] ){
					length++;
				}
			}
		}
		else if( dirI == 0 && dirJ != 0 ){
			for(int jj = j; jj >= 0 && jj < this.grid[0].length; jj += dirJ){
				if( this.grid[i][jj] == this.grid[i][j] ){
					length++;
				}
			}
		}

		return length;
	}
	
}
