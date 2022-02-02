import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MyTableModel extends AbstractTableModel {
    private String[] colNames = {"Metropolis", "Continent", "Population"};
    private List<Metropolis> data;
    private MetropolisDAO metropolisDAO;

    /**
     * Basic constructor.
     * @param dao DAO object for querying the database.
     */
    public MyTableModel(MetropolisDAO dao){
        this.metropolisDAO = dao;
        this.data = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0: return data.get(rowIndex).getMetropolis();
            case 1: return data.get(rowIndex).getContinent();
            case 2: return data.get(rowIndex).getPopulation();
        }
        throw new RuntimeException("Value out of bounds");
    }

    @Override
    public String getColumnName(int column) {
        return colNames[column];
    }

    /**
     * Adds a metropolis entry to the table,
     * updates the table display.
     * @param m Metropolis to add.
     */
    public void addMetropolis(Metropolis m){
        metropolisDAO.addMetropolis(m);
        data.add(m);
        fireTableRowsInserted(data.size()-1, data.size()-1);
    }

    /**
     * Queries the database with the given parameters and displays
     * the results on the table.
     * @param m  Metropolis object that contains search terms, can have null values.
     * @param compareIndex Parameter for comparing populations.
     * @param searchIndex Parameter for comparing metropolis names and continents.
     */
    public void queryAndSetData(Metropolis m, int compareIndex, int searchIndex){
        if(m.getMetropolis() == null && m.getPopulation() == null && m.getContinent() == null)
            return;

        this.data = metropolisDAO.searchDatabase(m, compareIndex, searchIndex);

        fireTableDataChanged();
    }
}
