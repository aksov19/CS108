package hw05;

import javax.jws.WebMethod;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class ShoppingCartServlet extends HttpServlet {

    @Override

    public void doPost(HttpServletRequest req,
                       HttpServletResponse resp) throws ServletException, IOException
    {
        String productId = req.getParameter("productId");
        ShoppingCart cart = (ShoppingCart) req.getSession().getAttribute("cart");

        HashMap<String, String[]> params = (HashMap<String, String[]>) req.getParameterMap();

        String action = params.get("action")[0];

        if ( action.equals("add") ) {
            cart.addItem(productId);
        }
        else if ( action.equals("update") ){
            for ( String id : params.keySet() ){
                if( !id.equals("action") ){
                    cart.setItemCount( id, Integer.parseInt( params.get(id)[0] ) );
                }
            }
        }

        req.getRequestDispatcher("/shopping-cart.jsp").forward(req, resp);
    }
}
