package frontend;

import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import accountService.AccountService;
import logic.UsersDataSet;


public class Frontend extends HttpServlet{
    private String login = "";
    private String password = "";
    private AtomicLong userIdGenerator = new AtomicLong();

    public static String getTime() {
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(new Date());
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        login = request.getParameter("login");
        password = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
        if (request.getPathInfo().equals("/timer")) {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.sendRedirect("/");
                return;
            }
            pageVariables.put("refreshPeriod", "1000");
            pageVariables.put("serverTime", getTime());
            pageVariables.put("userId", userId);
            response.getWriter().println(PageGenerator.getPage("userid.tml", pageVariables));
            return;
        }
        if (request.getPathInfo().equals("/register")) {
            response.getWriter().println(PageGenerator.getPage("register.tml", null));
            return;
        }
        response.sendRedirect("/");
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        login = request.getParameter("login");
        password = request.getParameter("password");
        if (request.getPathInfo().equals("/register")) {
            if (!(login == null) && !(password == null)) {
                UsersDataSet user = new UsersDataSet();
                user.setLogin(login);
                user.setPassword(password);
                if (AccountService.addUser(user)) {
                    response.sendRedirect("/");
                } else {
                    response.sendRedirect("/register");
                }
            } else {
                response.sendRedirect("/register");
            }
            return;
        }
        if (request.getPathInfo().equals("/")) {
            UsersDataSet user = AccountService.getUserByLogin(login);
            if (!(user == null) && (password.equals(user.getPassword()))) {
                HttpSession session = request.getSession();
                Long userId = (Long) session.getAttribute("userId");
                if (userId == null) {
                    userId = userIdGenerator.getAndIncrement();
                    session.setAttribute("userId", userId);
                }
                response.sendRedirect("/timer");
            } else {
                response.sendRedirect("/");
            }
        }

    }
}
