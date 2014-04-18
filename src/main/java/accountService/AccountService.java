package accountService;

import dao.UsersDAO;
import logic.UsersDataSet;
import dao.Impl.UsersDAOImpl;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class AccountService {
    private SessionFactory sessionFactory;
    private static UsersDAO userDAO = null;
    public AccountService() {
        try {
            Configuration configuration = new Configuration().addResource("hibernate.cfg.xml");
            configuration.configure();
            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private UsersDAO getUserDAO() {
        if (userDAO == null){
            userDAO = new UsersDAOImpl(sessionFactory);
        }
        return userDAO;
    }
    public UsersDataSet getUserByLogin(String login) {
        return getUserDAO().getUserByLogin(login);
    }
    public boolean addUser(UsersDataSet dataSet) {
        if (getUserByLogin(dataSet.getLogin()) == null) {
            getUserDAO().addUser(dataSet);
            return true;
        }
        return false;
    }
    public boolean deleteUser(UsersDataSet dataSet) {
        if (getUserByLogin(dataSet.getLogin()) != null) {
            getUserDAO().deleteUser(dataSet);
            return true;
        }
        return false;
    }
}
