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
    private AtomicLong userIdGenerator = new AtomicLong();
    // добавить карту sessionid на класс userdata со всеми данными пользователя

    //временное решение: приватное поле с accountservice  задаваемое в конструкторе
    //фронтенда. Будет заменено на message system
    private AccountService accountService;
    public Frontend(AccountService accountService) {
        this.accountService = accountService;
    }
    public static String getTime() {
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(new Date());
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
        String path = request.getPathInfo();
        switch (path) {
            case "/timer":
                HttpSession session = request.getSession();
                Long userId = (Long) session.getAttribute("userId");
                if (userId == null) {
                    response.sendRedirect("/");
                    break;
                }
                pageVariables.put("refreshPeriod", "1000");
                pageVariables.put("serverTime", getTime());
                pageVariables.put("userId", userId);
                response.getWriter().println(PageGenerator.getPage("userid.tml", pageVariables));
                break;
            case "/register":
                pageVariables.put("msg", "");
                response.getWriter().println(PageGenerator.getPage("register.tml", pageVariables));
                break;
            case "/auth":
                pageVariables.put("msg", "");
                response.getWriter().println(PageGenerator.getPage("auth.tml", pageVariables));
                break;
            default:
                response.sendRedirect("/");
                break;
        }

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
        String path = request.getPathInfo();
        switch (path) {
            case "/register":
                if (!(login.isEmpty()) && !(password.isEmpty())) {
                    UsersDataSet user = new UsersDataSet(login, password);
                    if (accountService.addUser(user)) {
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
                break;
            case "/auth":
                if (login.isEmpty() || password.isEmpty()) {
                    pageVariables.put("msg", "Error! Please, fill in all fields.");
                    response.getWriter().println(PageGenerator.getPage("auth.tml", pageVariables));
                    break;
                }
                UsersDataSet user = accountService.getUserByLogin(login);
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
                break;
            default:
                response.sendRedirect("/");
                break;
        }
    }
}
