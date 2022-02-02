package hw05.manager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ManagerContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        AccountManager manager = new AccountManager();
        manager.addAccount("Patrick", "1234");
        manager.addAccount("Molly", "FloPup");
        servletContextEvent.getServletContext().setAttribute("manager", manager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
