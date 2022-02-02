import junit.framework.TestCase;
import java.util.*;

public class SudokuTest extends TestCase {

    private Sudoku sudoku;
    private Random rand;

    protected void setUp() throws Exception{
        super.setUp();

        rand = new Random();
    }


    public void testTextToGrid(){
        // Test on sudoku filled with 0s
        int[][] grid = Sudoku.textToGrid
                ("000000000000000000000000000000000000000000000000000000000000000000000000000000000");

        assertTrue(Arrays.deepEquals(grid, new int[Sudoku.SIZE][Sudoku.SIZE]));


        // Test on non-full sudoku
        try {
            grid = Sudoku.textToGrid("0");
            fail();
        }
        catch (Exception e){
            assertEquals(e.getMessage(), "Needed 81 numbers, but got:1");
        }


        // Test on 10 random sudokus
        for(int k = 0; k < 10; k++){
            StringBuilder s = new StringBuilder("");

            for(int i = 0; i < Sudoku.SIZE; i++){
                for(int j = 0; j < Sudoku.SIZE; j++){
                    int a = rand.nextInt(10);
                    s.append((char)(a + '0'));
                    grid[i][j] = a;
                }
            }

            assertTrue(Arrays.deepEquals(grid, Sudoku.textToGrid(s.toString())));
        }
    }


    public void testDefaultGrids(){
        sudoku = new Sudoku(Sudoku.easyGrid);
        assertEquals(sudoku.solve(), 1);
        assertTrue(sudoku.getElapsed() >= 0);
        assertEquals(sudoku.getSolutionText(),
                "1 6 4 7 9 5 3 8 2 \n" +
                        "2 8 7 4 6 3 9 1 5 \n" +
                        "9 3 5 2 8 1 4 6 7 \n" +
                        "3 9 1 8 7 6 5 2 4 \n" +
                        "5 4 6 1 3 2 7 9 8 \n" +
                        "7 2 8 9 5 4 1 3 6 \n" +
                        "8 1 9 6 4 7 2 5 3 \n" +
                        "6 7 3 5 2 9 8 4 1 \n" +
                        "4 5 2 3 1 8 6 7 9 \n");


        sudoku = new Sudoku(Sudoku.mediumGrid);
        assertEquals(sudoku.solve(), 1);
        assertTrue(sudoku.getElapsed() >= 0);
        assertEquals(sudoku.getSolutionText(),
                "5 3 4 6 7 8 9 1 2 \n" +
                        "6 7 2 1 9 5 3 4 8 \n" +
                        "1 9 8 3 4 2 5 6 7 \n" +
                        "8 5 9 7 6 1 4 2 3 \n" +
                        "4 2 6 8 5 3 7 9 1 \n" +
                        "7 1 3 9 2 4 8 5 6 \n" +
                        "9 6 1 5 3 7 2 8 4 \n" +
                        "2 8 7 4 1 9 6 3 5 \n" +
                        "3 4 5 2 8 6 1 7 9 \n");


        sudoku = new Sudoku(Sudoku.hardGrid);
        assertEquals(sudoku.solve(), 1);
        assertTrue(sudoku.getElapsed() >= 0);
        assertEquals(sudoku.getSolutionText(),
                "3 7 5 1 6 2 4 8 9 \n" +
                        "8 6 1 4 9 3 5 2 7 \n" +
                        "2 4 9 7 8 5 1 6 3 \n" +
                        "4 9 3 8 5 7 6 1 2 \n" +
                        "7 1 6 2 4 9 8 3 5 \n" +
                        "5 2 8 3 1 6 7 9 4 \n" +
                        "6 5 7 9 2 1 3 4 8 \n" +
                        "1 8 2 5 3 4 9 7 6 \n" +
                        "9 3 4 6 7 8 2 5 1 \n");
    }


    public void testEdgeCaseGrids(){
        int[][] grid = Sudoku.textToGrid
                ("000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        sudoku = new Sudoku(grid);
        assertEquals(sudoku.solve(), 100);

        grid = Sudoku.textToGrid
                ("000000000000000000000000000000000000000000000000000000000000000000000000000001000");
        sudoku = new Sudoku(grid);
        assertEquals(sudoku.solve(), 100);

        grid = Sudoku.textToGrid
                ("128356974459271863763984152546138729982765341317429685274613598635892417891547236");
        sudoku = new Sudoku(grid);
        assertEquals(sudoku.solve(), 1);
    }
}
