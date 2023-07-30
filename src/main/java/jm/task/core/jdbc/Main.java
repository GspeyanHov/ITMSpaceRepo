package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceHibernateImpl;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    private static final UserService userService = new UserServiceImpl();
    private static final UserServiceHibernateImpl userServiceHibernate = new UserServiceHibernateImpl();

    public static void main(String[] args) {
        // реализуйте алгоритм здесь
        userService.createUsersTable();
        for (int i = 1; i < 5; i++) {
            User user = new User("Lala" + i, "Bala" + i, (byte) ((byte) 15 + i));
            userService.saveUser(user.getName(), user.getLastName(), user.getAge());
        }
        System.out.println(userService.getAllUsers());
        userService.removeUserById(3);
        userService.cleanUsersTable();
        userService.dropUsersTable();

        //calling Hibernate methods

        userServiceHibernate.createUsersTable();
        for (int i = 1; i < 5; i++) {
            User userObj = new User("Lala" + i, "Bala" + i, (byte) ((byte) 15 + i));
            userServiceHibernate.saveUser(userObj.getName(), userObj.getLastName(), userObj.getAge());
        }
        System.out.println(userServiceHibernate.getAllUsers());
        userServiceHibernate.removeUserById(2);
        userServiceHibernate.cleanUsersTable();
//        userServiceHibernate.dropUsersTable();
    }

}
