package hw05;

import javax.ejb.SessionContext;
import javax.jms.Session;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionContextSetup implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        ShoppingCart cart = new ShoppingCart();
        httpSessionEvent.getSession().setAttribute("cart", cart);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
