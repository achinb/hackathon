package hack;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Date: 5/30/13
 */
public class Main extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.getWriter().print("pong!\n");
    }

    public static void main(String[] args) throws Exception{
        Server server = new Server(5000);


        HandlerList handlerList = new HandlerList();
        ResourceHandler resourceHandler = new ResourceHandler();
        handlerList.addHandler(resourceHandler);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new Main()), "/ping");
        context.addServlet(new ServletHolder(new AddShoppingCart()),"/addShoppingCart");
        context.addServlet(new ServletHolder(new ViewAppointments()),"/appointments");
        handlerList.addHandler(context);

        server.setHandler(handlerList);
        server.start();
        server.join();
    }
}
