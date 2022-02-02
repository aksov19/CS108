// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JCount extends JPanel {
	private JTextField field;
	private JLabel label;
	private JButton start;
	private JButton stop;
	private Worker worker;

	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// YOUR CODE HERE
		field = new JTextField(0);
		field.setPreferredSize(new Dimension(100, 24));
		label = new JLabel("0");
		start = new JButton("Start");
		stop = new JButton("Stop");
		worker = null;

		add(field);
		add(label);
		add(start);
		add(stop);
		add(Box.createRigidArea(new Dimension(0,40)));


		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int num = Integer.parseInt(field.getText());
				if (worker != null) worker.interrupt();

				worker = new Worker(num);
				worker.start();
			}
		});

		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (worker != null){
					worker.interrupt();
					worker = null;
				}
			}
		});
	}


	private class Worker extends Thread {
		private final int num;

		public Worker(int num){ this.num = num; }

		@Override
		public void run() {
			for(int i = 0; i <= num; i++){
				if(i % 10000 == 0){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						//e.printStackTrace();
						return;
					}

					if(isInterrupted()) return;

					int finalI = i;
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							label.setText(String.valueOf(finalI));
						}
					});
				}
			}
		}
	}

	
	static public void main(String[] args)  {
		// Creates a frame with 4 JCounts in it.
		// (provided)
		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

