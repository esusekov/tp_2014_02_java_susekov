package dao;

import dao.Impl.UsersDAOImpl;

public class DAOFactory {
    private static UsersDAO userDAO = null;
    private static DAOFactory instance = null;

    public static synchronized DAOFactory getInstance(){
        if (instance == null){
            instance = new DAOFactory();
        }
        return instance;
    }

    public UsersDAO getUserDAO(){
        if (userDAO == null){
            userDAO = new UsersDAOImpl();
        }
        return userDAO;
    }
}
