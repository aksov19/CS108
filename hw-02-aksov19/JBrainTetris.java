import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class JBrainTetris extends JTetris{
    private JCheckBox brainMode;
    private JSlider adversary;
    private JLabel adversaryText;
    private DefaultBrain brain;
    private Brain.Move curMove;
    private int tmp_count;
    Random rand;

    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     */
    JBrainTetris(int pixels) {
        super(pixels);
        brain = new DefaultBrain();
        rand = new Random();
        tmp_count = 0;
    }


    @Override
    public JComponent createControlPanel(){
        JComponent panel = super.createControlPanel();

        // Add a CheckBox for AI
        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        panel.add(brainMode);

        // Add a slider and labels for adversary
        JComponent mini = new JPanel();
        mini.add(new JLabel("Adversary:"));
        adversary = new JSlider(0, 100, 0);
        adversary.setPreferredSize(new Dimension(100, 15));
        mini.add(adversary);
        adversaryText = new JLabel("Ok");
        panel.add(mini);
        panel.add(adversaryText);

        return panel;
    }


    @Override
    public Piece pickNextPiece(){
        // Return a random piece normally
        if(rand.nextInt(101) >= adversary.getValue()){
            adversaryText.setText("Ok");
            return super.pickNextPiece();
        }

        // Return a piece with the highest score (higher scores are worse)
        double maxScore = 0;
        Piece maxScorePiece = null;
        Brain.Move move = null;

        for(Piece p : Piece.getPieces()){
            move = brain.bestMove(board, p, getHeight()-4, move);
            if(move != null && move.score >= maxScore){
                maxScore = move.score;
                maxScorePiece = p;
            }
        }

        adversaryText.setText("*Ok*");
        return maxScorePiece;
    }


    @Override
    public void tick(int verb){
        if(brainMode.isSelected() && verb == DOWN){
            if(tmp_count != count){
                board.undo();
                curMove = brain.bestMove(board, currentPiece, HEIGHT, curMove);
                tmp_count = count;
            }

            // Stop the game if cant find a move
            if(curMove == null){
                stopGame();
                return;
            }

            if(!currentPiece.equals(curMove.piece)){
                currentPiece = currentPiece.fastRotation();
            }

            if(curMove.x > currentX){
                super.tick(RIGHT);
            }
            else if(curMove.x < currentX){
                super.tick(LEFT);
            }
            super.tick(DOWN);
        }
        else {
            super.tick(verb);
        }
    }

    private void testTick(){
        if(tmp_count != count){
            board.undo();
            curMove = brain.bestMove(board, currentPiece, HEIGHT, curMove);
            tmp_count = count;
        }

        // Rotate once if necessary
        if(!curMove.piece.equals(currentPiece)){
            super.tick(ROTATE);
        }

        // Move left or right once or drop
        if(currentX > curMove.x){
            super.tick(LEFT);
            super.tick(DOWN);
        }
        else if(currentX < curMove.x){
            super.tick(RIGHT);
            super.tick(DOWN);
        }
        else{
            super.tick(DROP);
        }
    }

    public static void main(String[] args){
        // Set GUI Look And Feel Boilerplate.
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JTetris tetris = new JBrainTetris(16);
        JFrame frame = JBrainTetris.createFrame(tetris);
        frame.setVisible(true);
    }
}
