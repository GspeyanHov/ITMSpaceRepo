package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/users";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "root";
    private static Connection connection;
    private static SessionFactory sessionFactory;
    private static final String POSTGRES_DIALECT = "org.hibernate.dialect.PostgreSQLDialect";

    private Util() {

    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
    //configuration method for hibernate
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            Configuration configuration = new Configuration();
            Properties props = new Properties();
            props.put(Environment.DRIVER, DRIVER);
            props.put(Environment.URL, DB_URL);
            props.put(Environment.USER, DB_USERNAME);
            props.put(Environment.PASS, DB_PASSWORD);
            props.put(Environment.DIALECT, POSTGRES_DIALECT);
            props.put(Environment.SHOW_SQL, true);
            props.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            props.put(Environment.HBM2DDL_AUTO, "update");

            configuration.setProperties(props);
            configuration.addAnnotatedClass(User.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }
}