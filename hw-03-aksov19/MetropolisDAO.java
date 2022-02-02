import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetropolisDAO {
    private final BasicDataSource ds;
    private final String tableName;

    /**
     * Constructor for MetropolisDAO.
     * @param ds DataSource object for contacting the database.
     * @param tableName Name of the table to fetch information from.
     */
    public MetropolisDAO(BasicDataSource ds, String tableName) {
        this.ds = ds;
        this.tableName = tableName;
    }


    /**
     * Executes a query to add a Metropolis to the database,
     * can accept null values.
     * @param m Metropolis entry to be added to the database.
     */
    public void addMetropolis(Metropolis m){
        try{
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO " + tableName + " VALUES (?, ?, ?);");

            if(m.getMetropolis() != null) stmt.setString(1, m.getMetropolis());
            else stmt.setNull(1, Types.NULL);

            if(m.getContinent() != null ) stmt.setString(2, m.getContinent());
            else stmt.setNull(2, Types.NULL);

            if(m.getPopulation() != null) stmt.setLong(3, m.getPopulation());
            else stmt.setNull(3, Types.NULL);

            stmt.execute();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private final static int LARGER_THAN = 0;
    private final static int EXACT_MATCH = 0;

    /**
     * Executes a query to get a list of all Metropolises that satisfy
     * the given search criteria.
     * @param m Metropolis object that contains search terms, can have null values
     * @param compareIndex When equal to 0 lists metropolises with population larger than
     *                     the given parameter.
     *                     Otherwise lists metropolises with population less than the given
     *                     parameter.
     * @param matchIndex When equal to 0 lists metropolises that have the same name and
     *                   continent as the given parameter.
     *                   Otherwise lists metropolises that partially match the parameter's
     *                   name and continent.
     * @return List of Metropolises that satisfy the given search criteria.
     */
    public List<Metropolis> searchDatabase(Metropolis m, int compareIndex, int matchIndex){
        List<Metropolis> metropolises = new ArrayList<>();
        String queryString = buildQueryString(m, compareIndex, matchIndex);

        try{
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(queryString);
            int index = 1;
            if(m.getPopulation() != null) stmt.setLong(index++, m.getPopulation());
            if(m.getMetropolis() != null) stmt.setString(index++, m.getMetropolis());
            if(m.getContinent() != null)  stmt.setString(index, m.getContinent());
            ResultSet res = stmt.executeQuery();

            metropolises = new ArrayList<>();
            while(res.next()){
                metropolises.add(new Metropolis(
                                res.getString(1),
                                res.getString(2),
                                res.getLong(3)
                        )
                );
            }
            con.close();

            return metropolises;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return metropolises;
    }


    /**
     * Builds a query string for a PreparedStatement (used by the searchDatabase function).
     * @param m Metropolis object that contains search terms, can have null values.
     * @param compareIndex Parameter for comparing populations.
     * @param matchIndex Parameter for comparing metropolis names and continents.
     * @return A string which can be used to build a PreparedStatement.
     */
    private String buildQueryString(Metropolis m, int compareIndex, int matchIndex){
        StringBuilder queryString = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");

        String name = m.getMetropolis();
        String cont = m.getContinent();
        Long pop = m.getPopulation();

        if(pop != null){
            queryString.append("population ");
            if(compareIndex == LARGER_THAN) queryString.append("> ?");
            else queryString.append("< ?");

            if(name != null || cont != null) queryString.append(" AND ");
        }

        if(name != null){
            queryString.append("metropolis ");
            if(matchIndex == EXACT_MATCH) queryString.append("= ?");
            else queryString.append("LIKE ?");

            if(cont != null) queryString.append(" AND ");
        }

        if(cont != null){
            queryString.append("continent ");
            if(matchIndex == EXACT_MATCH) queryString.append("= ?");
            else queryString.append("LIKE ?");
        }
        queryString.append(";");

        return queryString.toString();
    }
}
