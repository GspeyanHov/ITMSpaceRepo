package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.model.User;

import java.util.List;

public class UserServiceHibernateImpl implements UserService {
    private static final UserDao userHibernateDao = new UserDaoHibernateImpl();

    public void createUsersTable() {
        userHibernateDao.createUsersTable();
    }

    public void dropUsersTable() {
        userHibernateDao.dropUsersTable();
    }

    public void saveUser(String name, String lastName, byte age) {
        userHibernateDao.saveUser(name, lastName, age);
    }

    public void removeUserById(long id) {
        userHibernateDao.removeUserById(id);
    }

    public List<User> getAllUsers() {
        return userHibernateDao.getAllUsers();
    }

    public void cleanUsersTable() {
        userHibernateDao.cleanUsersTable();
    }
}
