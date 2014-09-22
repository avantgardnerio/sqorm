package net.squarelabs.sqorm.demo;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Enumeration;

public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        Enumeration<String> names = ctx.getInitParameterNames();
        while(names.hasMoreElements()) {
            String key = names.nextElement();
            String val = ctx.getInitParameter(key);
            System.setProperty(key, val);
        }

        AppContext.initialize();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        AppContext.destroy();
    }
}
