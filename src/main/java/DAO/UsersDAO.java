package dao;

import logic.UsersDataSet;


public interface UsersDAO {
    public UsersDataSet getUserByLogin(String login);
    public void addUser(UsersDataSet dataSet);
    public void deleteUser(UsersDataSet dataSet);
}
