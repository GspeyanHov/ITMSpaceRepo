package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final Connection connection = Util.getConnection();
    public UserDaoJDBCImpl() {

    }
    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            String query = """
                    CREATE TABLE IF NOT EXISTS users(id BIGSERIAL PRIMARY KEY,
                        first_name VARCHAR(50),
                        last_name VARCHAR(50),
                        age Smallint
                    )""";
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("SqlResolve")
    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            String query = "DROP TABLE IF EXISTS users";
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("SqlResolve")
    public void saveUser(String name, String lastName, byte age) {
        if (name == null || lastName == null || (age < 0 || age > 90)) {
            throw new RuntimeException("Bad credentials, please check! ");
        }
        String query = "INSERT INTO users (first_name, last_name, age) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
             ResultSet keys = statement.getGeneratedKeys()) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            if (keys.next()) {
                keys.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("User с именем " + name + " добавлен в базу данных");
    }
    @SuppressWarnings("SqlResolve")
    public void removeUserById(long id) {
        if (id <= 0) {
            throw new RuntimeException("id must be bigger than 0! ");
        }
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("SqlResolve")
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * from users";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
    @SuppressWarnings("SqlResolve")
    public void cleanUsersTable() {
        String query = "TRUNCATE TABLE users";
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Empty table can not be cleaned! ");
        }
    }
}
