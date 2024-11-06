import dao.UserDao;
import dao.UserDaoJDBCImpl;
import model.User;
import service.UserService;
import service.UserServicelmpl;
import util.Util;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Util.getConnection();
        UserService UserService = new UserServicelmpl();

        UserService.createUsersTable();

        UserService.saveUser("Name1", "LastName1", (byte) 20);
        UserService.saveUser("Name2", "LastName2", (byte) 25);
        UserService.saveUser("Name3", "LastName3", (byte) 31);
        UserService.saveUser("Name4", "LastName4", (byte) 38);

        UserService.removeUserById(3L);
        UserService.getAllUsers();
        UserService.cleanUsersTable();
        UserService.dropUsersTable();

    }
}
