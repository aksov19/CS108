import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MetropolisViewer extends JFrame {

    private final static Dimension TEXT_FIELD_DIMS  = new Dimension(100, 24);
    private final static Dimension TABLE_DIMS       = new Dimension(500, 150);
    private final static Dimension COMBO_BOX_DIMS   = new Dimension(220, 24);
    private final static Dimension RIGHT_PANEL_DIMS = new Dimension(220, 150);

    private final MetropolisDAO metropolisDAO;


    public MetropolisViewer(MetropolisDAO metropolisDAO){
        super("Metropolis Viewer");

        this.metropolisDAO = metropolisDAO;

        setUpGUI();
        setupListeners();
    }

    private void setUpGUI(){
        setLayout(new BorderLayout(4, 4));

        JComponent textFieldPanel = setUpTextFieldPanel();
        JComponent tablePanel = setUpTable();
        JComponent buttonsPanel = setUpButtonsPanel();

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(4, 4));
        bottomPanel.add(tablePanel, BorderLayout.CENTER);
        bottomPanel.add(buttonsPanel, BorderLayout.EAST);

        add(textFieldPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }


    private JComponent setUpTextFieldPanel(){
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new FlowLayout());

        metropolisFld   = new JTextField();
        continentFld    = new JTextField();
        populationFld   = new JTextField();
        metropolisFld.setPreferredSize(TEXT_FIELD_DIMS);
        continentFld.setPreferredSize(TEXT_FIELD_DIMS);
        populationFld.setPreferredSize(TEXT_FIELD_DIMS);

        textPanel.add(new JLabel("Metropolis:"));
        textPanel.add(metropolisFld);
        textPanel.add(new JLabel("Continent:"));
        textPanel.add(continentFld);
        textPanel.add(new JLabel("Population:"));
        textPanel.add(populationFld);

        return textPanel;
    }

    private JComponent setUpTable(){
        //List<Metropolis> data = metropolisDAO.getMetropolises();

        tableModel = new MyTableModel(metropolisDAO);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        scrollPane.setPreferredSize(TABLE_DIMS);

        return scrollPane;
    }

    private JComponent setUpButtonsPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        addBtn = new JButton("Add");
        searchBtn = new JButton("Search");

        compareBox = new JComboBox(new String[]{"Population larger than", "Population smaller than"});
        matchBox = new JComboBox(new String[]{"Exact match", "Partial match"});
        compareBox.setPreferredSize(COMBO_BOX_DIMS);
        matchBox.setPreferredSize(COMBO_BOX_DIMS);

        buttonPanel.add(addBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(compareBox);
        buttonPanel.add(matchBox);
        buttonPanel.setPreferredSize(RIGHT_PANEL_DIMS);

        return buttonPanel;
    }


    private void setupListeners(){
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.addMetropolis( parseMetropolisFromTextFields() );
            }
        });

        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.queryAndSetData(
                        parseMetropolisFromTextFields(),
                        compareBox.getSelectedIndex(),
                        matchBox.getSelectedIndex());
            }
        });
    }


    private Metropolis parseMetropolisFromTextFields(){
        String name = metropolisFld.getText();
        String cont = continentFld.getText();
        Long pop;

        if(name.equals("")) name = null;
        if(cont.equals("")) cont = null;
        try{
            pop = Long.parseLong(populationFld.getText());
        } catch (NumberFormatException e) {
            pop = null;
        }

        return new Metropolis(name, cont, pop);
    }


    private JButton searchBtn;
    private JButton addBtn;
    private JTextField metropolisFld;
    private JTextField continentFld;
    private JTextField populationFld;
    private JComboBox compareBox;
    private JComboBox matchBox;
    private JTable table;
    private MyTableModel tableModel;
}
