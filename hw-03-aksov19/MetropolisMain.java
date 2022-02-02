import org.apache.commons.dbcp2.BasicDataSource;

import javax.swing.*;

public class MetropolisMain {

    private static final String username = "";
    private static final String password = "";
    private static final String server   = "";
    private static final String database = "";
    private static final String table    = "metropolises";


    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        try{
            Class.forName("com.mysql.jdbc.Driver");

            BasicDataSource ds = new BasicDataSource();
            ds.setUrl("jdbc:mysql://" + server + "/" + database);
            ds.setUsername(username);
            ds.setPassword(password);

            MetropolisDAO dao = new MetropolisDAO(ds, table);
            new MetropolisViewer(dao);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
