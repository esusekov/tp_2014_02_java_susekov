package accountService;

import logic.UsersDataSet;
import dao.DAOFactory;



public class AccountService {
    public static UsersDataSet getUserByLogin(String login) {
        return DAOFactory.getInstance().getUserDAO().getUserByLogin(login);
    }
    public static boolean addUser(UsersDataSet dataSet) {
        if (getUserByLogin(dataSet.getLogin()) == null) {
            DAOFactory.getInstance().getUserDAO().addUser(dataSet);
            return true;
        }
        return false;
    }
    public static boolean deleteUser(UsersDataSet dataSet) {
        if (getUserByLogin(dataSet.getLogin()) != null) {
            DAOFactory.getInstance().getUserDAO().deleteUser(dataSet);
            return true;
        }
        return false;
    }
}
