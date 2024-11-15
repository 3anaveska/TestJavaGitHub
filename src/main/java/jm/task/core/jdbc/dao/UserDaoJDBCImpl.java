package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final Connection connection;

    static {
        Util.getInstance();
        connection = Util.getConnection();
    }

    public UserDaoJDBCImpl() {

    }
    @Override
    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS user " +
                    "(id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), lastname VARCHAR(255), age TINYINT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS user");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO user (NAME, LASTNAME, AGE) VALUES ( ?, ?, ? )")) {
            connection.setAutoCommit(false);
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public void removeUserById(long id) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM USER WHERE id = ?")) {
            connection.setAutoCommit(false);
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT ID, NAME, LASTNAME, AGE FROM user")) {
            while (resultSet.next()) {
                User user = new User(resultSet.getString("NAME"), resultSet.getString("LASTNAME"), resultSet.getByte("AGE"));
                user.setId(resultSet.getLong("ID"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    @Override
    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate("TRUNCATE TABLE user");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}