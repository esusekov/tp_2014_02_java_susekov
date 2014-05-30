package accountService;

import dao.UsersDAO;
import logic.UsersDataSet;
import dao.Impl.UsersDAOImpl;
import message.Abonent;
import message.Address;
import message.MessageSystem;
import message.TimeHelper;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import util.resources.DBResource;
import util.resources.ResourceFactory;

public class AccountService implements Abonent, Runnable{
    private Address address;
    private MessageSystem ms;
    private SessionFactory sessionFactory;

    public AccountService(MessageSystem ms) {
        this.ms = ms;
        this.address = new Address();
        ms.addService(this);
        ms.getAddressService().setAccountService(address);
        try {
            DBResource db = (DBResource) ResourceFactory.getInstance().get("data/database.xml");
            Configuration configuration = new Configuration().addResource(db.getConfig());
            configuration.configure();
            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Address getAddress() {
        return address;
    }

    public MessageSystem getMessageSystem(){
        return ms;
    }

    private UsersDAO getUserDAO() {
        return new UsersDAOImpl(sessionFactory);
    }

    public UsersDataSet getUserByLogin(String login) {
        return getUserDAO().getUserByLogin(login);
    }

    public Long getUserId(String login) {
        UsersDataSet user = getUserByLogin(login);
        if (user != null)
            return user.getId();
        return null;
    }

    public boolean isRegistered(String login) {
        return getUserByLogin(login) == null;
    }
    public boolean addUser(UsersDataSet dataSet) {
        TimeHelper.sleep(2000);
        if (isRegistered(dataSet.getLogin())) {
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

    public void run(){
        while(true){
            ms.execForAbonent(this);
            TimeHelper.sleep(10);
        }
    }
}
