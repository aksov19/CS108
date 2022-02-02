package hw05;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductListDAO {
    private final static String username = "aksov19";
    private final static String password = "achi2001";
    private final static String server   = "localhost";
    private final static String database = "uni_oop";
    private String table = "products";
    private BasicDataSource ds = null;
    private Connection con = null;

    public ProductListDAO(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ignored) {}

        ds = new BasicDataSource();
        ds.setUrl("jdbc:mysql://" + server + "/" + database);
        ds.setUsername(username);
        ds.setPassword(password);

        try {
            con = ds.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // used for testing
    public ProductListDAO(String table){
        this.table = table;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ignored) {}

        ds = new BasicDataSource();
        ds.setUrl("jdbc:mysql://" + server + "/" + database);
        ds.setUsername(username);
        ds.setPassword(password);

        try {
            con = ds.getConnection();
        } catch (Exception ignored) {}
    }


    public List<Product> getProducts(){
        try {
            //Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM " + table);
            ResultSet res = stmt.executeQuery();

            List<Product> products = new ArrayList<>();
            while(res.next()){
                products.add( new Product(
                        res.getString("productid"),
                        res.getString("name"),
                        res.getString("imagefile"),
                        res.getDouble("price")));
            }

            //con.close();
            return products;

        } catch (Exception ignored) {}
        return null;
    }


    public Product getProductById(String id){
        try{
            //Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM " + table + " WHERE productid = ?;");
            stmt.setString(1, id);
            ResultSet res = stmt.executeQuery();

            if(!res.next()) return null;

            Product p = new Product( res.getString("productid"),
                                res.getString("name"),
                                res.getString("imagefile"),
                                res.getDouble("price"));

            //con.close();
            return p;

        } catch (Exception ignored) {}
        return null;
    }
}
