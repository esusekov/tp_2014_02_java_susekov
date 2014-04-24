package frontend;

import message.*;
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


public class Frontend extends HttpServlet implements Abonent, Runnable {
    private MessageSystem ms;
    private Address address;
    private Map<String, UserSession> sessionIdToUserSession = new HashMap<>();
    private Map<String, String> sessionIdToUserRegStatus = new HashMap<>();
    private Object lock = new Object();
    private int handleCount = 0;

    public Frontend(MessageSystem ms) {
        this.ms = ms;
        this.address = new Address();
        ms.addService(this);
    }

    public Address getAddress() {
        return address;
    }

    public static String getTime() {
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(new Date());
    }

    public void setUserRegStatus(String sessionId, String regStatus) {
        sessionIdToUserRegStatus.put(sessionId, regStatus);
    }

    public void setId(String sessionId, Long userId, String status) {
        UserSession userSession = sessionIdToUserSession.get(sessionId);
        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }
        userSession.setUserId(userId);
        userSession.setUserStatus(status);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        synchronized (lock){
            handleCount++;
        }
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String sessionId = request.getSession().getId();
        Map<String, Object> pageVariables = new HashMap<>();
        String path = request.getPathInfo();
        switch (path) {
            case "/timer":
                HttpSession session = request.getSession();
                UserSession userSession = sessionIdToUserSession.get(session.getId());
                pageVariables.put("refreshPeriod", "1000");
                pageVariables.put("serverTime", getTime());
                if (userSession == null) {
                    pageVariables.put("msg", "Auth error");
                } else if (userSession.getStatus() != "") {
                    pageVariables.put("msg", userSession.getStatus());
                } else if (userSession.getUserId() == null) {
                    pageVariables.put("msg", "wait for authorization");
                } else pageVariables.put("msg", "name = " + userSession.getName() + ", id = " + userSession.getUserId());
                response.getWriter().println(PageGenerator.getPage("userid.tml", pageVariables));
                break;
            case "/register":
                pageVariables.put("msg", "");
                response.getWriter().println(PageGenerator.getPage("register.tml", pageVariables));
                break;
            case "/registrating":
                String status = sessionIdToUserRegStatus.get(sessionId);
                if (status != null) {
                    response.getWriter().println(status);
                    sessionIdToUserRegStatus.remove(sessionId);
                }
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
        synchronized (lock){
            handleCount++;
        }
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
        String path = request.getPathInfo();
        switch (path) {
            case "/register":
                if (!(login.isEmpty()) && !(password.isEmpty())) {
                    Address frontendAddress = getAddress();
                    Address asAddress = ms.getAddressService().getAccountService();
                    String sessionId = request.getSession().getId();
                    ms.sendMessage(new MsgUserRegister(frontendAddress, asAddress, login, password, sessionId));
                    pageVariables.put("msg", "Wait for registration.");
                    response.getWriter().println(PageGenerator.getPage("register.tml", pageVariables));
                } else {
                    pageVariables.put("msg", "Error! Please, fill in all fields.");
                    response.getWriter().println(PageGenerator.getPage("register.tml", pageVariables));
                }
                break;
            case "/auth":
                if (!(login.isEmpty()) && !(password.isEmpty())) {
                    Address frontendAddress = getAddress();
                    Address asAddress = ms.getAddressService().getAccountService();
                    String sessionId = request.getSession().getId();
                    UserSession userSession = new UserSession(sessionId, login, ms.getAddressService());
                    sessionIdToUserSession.put(sessionId, userSession);
                    ms.sendMessage(new MsgGetUserId(frontendAddress, asAddress, login, password, sessionId));
                    response.sendRedirect("/timer");
                } else {
                    pageVariables.put("msg", "Error! Please, fill in all fields.");
                    response.getWriter().println(PageGenerator.getPage("auth.tml", pageVariables));
                }
                break;
            default:
                response.sendRedirect("/");
                break;
        }
    }
    public void run() {
        while (true) {
            ms.execForAbonent(this);
            //System.out.println(handleCount);
            TimeHelper.sleep(100);
        }
    }
}
