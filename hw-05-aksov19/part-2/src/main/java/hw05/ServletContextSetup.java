package hw05;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServletContextSetup implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ProductListDAO dao = new ProductListDAO();
        servletContextEvent.getServletContext().setAttribute("productDAO", dao);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
