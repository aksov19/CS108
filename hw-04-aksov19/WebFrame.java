import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.Semaphore;

public class WebFrame extends JPanel {
    private DefaultTableModel model;
    private JButton singleBtn;
    private JButton concurrentBtn;
    private JButton stopBtn;
    private JTextField txtField;
    private JLabel runningLbl;
    private JLabel completedLbl;
    private JLabel elapsedLbl;
    private JProgressBar progressBar;

    public Integer runningCount;
    public Integer completedCount;
    private long elapsedCount;
    private WorkerLauncher launcher;


    public WebFrame(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setUpTable();
        setUpLowerHalf();
        setUpListeners(this);
    }


    private final String fileName = "links1.txt";

    /*
     * Sets up table for displaying links, reads links from fileName
     */
    private void setUpTable(){
        model = new DefaultTableModel(new String[] { "url", "status"}, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(600,300));
        add(scrollpane);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            while (true) {
                String link = reader.readLine();
                if (link == null) break;
                model.addRow(new Object[]{link, null});
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /*
     * Sets up buttons, text field and progress bar at the bottom
     */
    private void setUpLowerHalf(){
        singleBtn = new JButton("Single Thread Fetch");
        concurrentBtn = new JButton("Concurrent Fetch");
        txtField = new JTextField();
        txtField.setMaximumSize(new Dimension(100, 24));

        add(singleBtn);
        add(concurrentBtn);
        add(txtField);

        runningLbl = new JLabel("Running: 0");
        completedLbl = new JLabel("Completed: 0");
        elapsedLbl = new JLabel("Elapsed: 0");
        add(runningLbl);
        add(completedLbl);
        add(elapsedLbl);

        progressBar = new JProgressBar();
        progressBar.setMaximum(model.getRowCount());
        add(progressBar);

        stopBtn = new JButton("Stop");
        stopBtn.setEnabled(false);
        add(stopBtn);
    }

    /*
     * Sets up for start and stop buttons
     */
    private void setUpListeners(WebFrame webFrame){
        singleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopBtn.setEnabled(true);
                singleBtn.setEnabled(false);
                concurrentBtn.setEnabled(false);

                runningCount = 0;
                completedCount = 0;
                elapsedCount = 0;
                updateInfoLabels();

                for(int i = 0; i < model.getRowCount(); i++){
                    model.setValueAt(null, i, 1);
                }

                launcher = new WorkerLauncher(webFrame,1);
                launcher.start();
            }
        });

        concurrentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int num;
                try {
                    num = Integer.parseInt(txtField.getText());
                }catch (Exception ignored){ return; }

                stopBtn.setEnabled(true);
                singleBtn.setEnabled(false);
                concurrentBtn.setEnabled(false);

                runningCount = 0;
                completedCount = 0;
                elapsedCount = 0;
                updateInfoLabels();

                for(int i = 0; i < model.getRowCount(); i++){
                    model.setValueAt(null, i, 1);
                }

                launcher = new WorkerLauncher(webFrame, num);
                launcher.start();
            }
        });

        stopBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopBtn.setEnabled(false);
                singleBtn.setEnabled(true);
                concurrentBtn.setEnabled(true);

                if(launcher != null){
                    launcher.interrupt();
                }
            }
        });
    }

    /**
     * Sets status for a link in the table at the given index (Used in WebWorker.java)
     * @param index Index of the link
     * @param status Status of the link
     */
    public void setStatus(int index, String status){
        model.setValueAt(status, index, 1);
    }

    /**
     * Gets link from the table at the given index (Used in WebWorker.java)
     * @param index Index of the link
     * @return Link
     */
    public String getLink(int index){
        return (String) model.getValueAt(index, 0);
    }

    /**
     * Updates progress information in the labels and the progress bar
     */
    public void updateInfoLabels(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                runningLbl.setText("Running: " + runningCount);
                completedLbl.setText("Completed: " + completedCount);
                elapsedLbl.setText("Elapsed: " + elapsedCount);
                progressBar.setValue(completedCount);
            }
        });
    }


    public class WorkerLauncher extends Thread{
        public final Semaphore workerLock;
        public final WebFrame webFrame;
        public Boolean interrupted = false;
        private final int maxWorkers;


        public WorkerLauncher(WebFrame webFrame, int maxWorkers){
            this.webFrame = webFrame;
            this.workerLock = new Semaphore(maxWorkers);
            this.maxWorkers = maxWorkers;
        }


        @Override
        public void run(){
            long tick = System.currentTimeMillis();
            runningCount++;
            updateInfoLabels();

            // Process links in the table
            for(int i = 0; i < model.getRowCount(); i++){
                try {
                    workerLock.acquire();
                }
                catch (InterruptedException e) {
                    interrupted = true;
                    break;
                }

                WebWorker worker = new WebWorker(this, i);
                worker.start();

                if(isInterrupted()){
                    interrupted = true;
                    break;
                }
            }

            // Wait for all workers to finish or wait until interrupted
            try {
                workerLock.acquire(maxWorkers);
            }
            catch (InterruptedException e) {
                interrupted = true;
                try {
                    workerLock.acquire(maxWorkers);
                } catch (Exception ignored) { }
            }

            // Update information
            synchronized (runningCount) { runningCount--; }
            long tock = System.currentTimeMillis();
            elapsedCount = tock - tick;
            updateInfoLabels();

            stopBtn.setEnabled(false);
            singleBtn.setEnabled(true);
            concurrentBtn.setEnabled(true);
        }
    }


    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }


        JFrame frame = new JFrame("WebLoader");
        frame.add(new WebFrame());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
