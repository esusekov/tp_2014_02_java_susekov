package dao.Impl;


import javax.swing.JOptionPane;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import dao.UsersDAO;
import logic.UsersDataSet;

public class UsersDAOImpl implements UsersDAO{
    private SessionFactory sessionFactory;
    public UsersDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public UsersDataSet getUserByLogin(String login) {
        Session session = null;
        UsersDataSet user = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(UsersDataSet.class);
            user = (UsersDataSet) criteria.add(Restrictions.eq("login", login)).uniqueResult();
        } catch (Exception e) {
            System.out.println("test");
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    public void addUser(UsersDataSet dataSet) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(dataSet);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void deleteUser(UsersDataSet dataSet) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(dataSet);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
