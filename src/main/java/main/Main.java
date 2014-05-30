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
import util.VFS.VFSImpl;
import util.resources.Connection;
import util.resources.ResourceFactory;

import java.util.Iterator;


public class Main {
    public static void main(String[] args) throws Exception {

        VFSImpl vfs = new VFSImpl("");
        Iterator<String> files = vfs.getIterator("data");
        while (files.hasNext()){
            String nextFile = files.next();
            if (!vfs.isDirectory(nextFile)){
                System.out.println(nextFile);
                ResourceFactory.getInstance().addResource(
                        nextFile,
                        ResourceFactory.getInstance().getResource(vfs.getAbsolutePath(nextFile))
                );
            }
        }

        MessageSystem ms = new MessageSystem();

        AccountService accountService = new AccountService(ms);
        AccountService accountService2 = new AccountService(ms);
        Frontend frontend = new Frontend(ms);

        (new Thread(frontend)).start();
        (new Thread(accountService)).start();
        (new Thread(accountService2)).start();

        Connection con = (Connection) ResourceFactory.getInstance().get("data/connection.xml");
        Server server = new Server(con.getPort());
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
