import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	
	public SudokuFrame() {
		super("Sudoku Solver");

		setupGUI();
		setupListeners();
	}


	private void setupGUI(){
		setLayout(new BorderLayout(4, 4));

		// South panel for button and checkbox
		JPanel checkPanel = new JPanel();
		checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.X_AXIS));

		checkButton = new JButton("Check");
		autoCheckBox = new JCheckBox("Auto Check");

		checkPanel.add(checkButton);
		checkPanel.add(autoCheckBox);


		// Center panel for Puzzle and Solution text areas
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout());

		puzzleArea = new JTextArea(15, 20);
		puzzleArea.setBorder(new TitledBorder("Puzzle"));
		puzzleArea.setLineWrap(true);

		solutionArea = new JTextArea(15, 20);
		solutionArea.setBorder(new TitledBorder("Solution"));
		solutionArea.setEditable(false);
		solutionArea.setLineWrap(true);

		textPanel.add(puzzleArea);
		textPanel.add(solutionArea);

		// Add panels
		super.add(checkPanel, BorderLayout.SOUTH);
		super.add(textPanel, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}


	private void setupListeners(){
		// Listener for the check button
		checkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					tryToSolveSudoku();
				}
				catch (Exception ex){
					solutionArea.setText("Parsing problem.\n");
				}
			}
		});

		// Listener for input text area
		puzzleArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				try{
					if(autoCheckBox.isSelected()) tryToSolveSudoku();
				}
				catch (Exception ex){
					solutionArea.setText("Parsing problem.\n");
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				try{
					if(autoCheckBox.isSelected()) tryToSolveSudoku();
				}
				catch (Exception ex){
					solutionArea.setText("Parsing problem.\n");
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				try{
					if(autoCheckBox.isSelected()) tryToSolveSudoku();
				}
				catch (Exception ex){
					solutionArea.setText("Parsing problem.\n");
				}
			}
		});
	}


	private void tryToSolveSudoku(){
		sudoku = new Sudoku(Sudoku.stringsToGrid(puzzleArea.getText().split("\n")));
		solutionArea.setText("");
		int solutions = sudoku.solve();

		solutionArea.append(sudoku.getSolutionText());
		solutionArea.append("\nSolutions: " + solutions);
		solutionArea.append("\nElapsed time: " + sudoku.getElapsed() + "ms");
	}


	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }

		SudokuFrame frame = new SudokuFrame();
	}


	private JButton checkButton;
	private JCheckBox autoCheckBox;
	private JTextArea puzzleArea;
	private JTextArea solutionArea;
	private Sudoku sudoku;
}
