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
    private String login = null;
    private String password = null;
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
            pageVariables.put("msg", "");
            response.getWriter().println(PageGenerator.getPage("register.tml", pageVariables));
            return;
        }
        if (request.getPathInfo().equals("/auth")) {
            pageVariables.put("msg", "");
            response.getWriter().println(PageGenerator.getPage("auth.tml", pageVariables));
            return;
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        login = request.getParameter("login");
        password = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
        if (request.getPathInfo().equals("/register")) {
            if (!(login.isEmpty()) && !(password.isEmpty())) {
                UsersDataSet user = new UsersDataSet();
                user.setLogin(login);
                user.setPassword(password);
                if (AccountService.addUser(user)) {
                    pageVariables.put("msg", "You're successfully registered!");
                    response.getWriter().println(PageGenerator.getPage("register.tml", pageVariables));
                } else {
                    pageVariables.put("msg", "Error! This login is occupied.");
                    response.getWriter().println(PageGenerator.getPage("register.tml", pageVariables));
                }
            } else {
                pageVariables.put("msg", "Error! Please, fill in all fields.");
                response.getWriter().println(PageGenerator.getPage("register.tml", pageVariables));
            }
            return;
        }
        if (request.getPathInfo().equals("/auth")) {
            UsersDataSet user = AccountService.getUserByLogin(login);
            if (login.isEmpty() || password.isEmpty()) {
                pageVariables.put("msg", "Error! Please, fill in all fields.");
                response.getWriter().println(PageGenerator.getPage("auth.tml", pageVariables));
                return;
            }
            if (!(user == null) && (password.equals(user.getPassword()))) {
                HttpSession session = request.getSession();
                Long userId = (Long) session.getAttribute("userId");
                if (userId == null) {
                    userId = userIdGenerator.getAndIncrement();
                    session.setAttribute("userId", userId);
                }
                response.sendRedirect("/timer");
            } else if (!(user == null) && !(password.equals(user.getPassword()))) {
                pageVariables.put("msg", "Error! Wrong password.");
                response.getWriter().println(PageGenerator.getPage("auth.tml", pageVariables));
            } else {
                pageVariables.put("msg", "You're not registered!");
                response.getWriter().println(PageGenerator.getPage("auth.tml", pageVariables));
            }
        }

    }
}
