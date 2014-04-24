package main;

import frontend.Frontend;
import message.MessageSystem;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import accountService.AccountService;




public class Main {
    public static void main(String[] args) throws Exception {
        MessageSystem ms = new MessageSystem();

        AccountService accountService = new AccountService(ms);
        AccountService accountService2 = new AccountService(ms);
        Frontend frontend = new Frontend(ms);

        (new Thread(frontend)).start();
        (new Thread(accountService)).start();
        (new Thread(accountService2)).start();

        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(frontend), "/*");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(false);
        resource_handler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
