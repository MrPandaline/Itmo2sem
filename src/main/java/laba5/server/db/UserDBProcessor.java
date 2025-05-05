package laba5.server.db;

import laba5.common.model.Dragon;
import laba5.common.model.Person;
import laba5.common.model.User;
import laba5.server.logic.DBManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

public class UserDBProcessor {
    private final DBManager dbManager;

    public UserDBProcessor(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public HashMap<Integer, User> select() throws SQLException {
        HashMap<Integer, User> linkedListUsers = new HashMap<>();
        try {
            ResultSet rs = dbManager.getStatement().executeQuery("SELECT * FROM DragonUser;");
            while (rs.next()) {
                linkedListUsers.put(rs.getInt("id"),new User(rs.getInt("id"), rs.getString("login"), rs.getString("password")));
            }
            rs.close();
            return linkedListUsers;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return linkedListUsers;
        }
    }

    public boolean insert(User user) throws SQLException {
        try (PreparedStatement stmt = dbManager.getPreparedStatementRGK("INSERT INTO DragonUser(login, password) VALUES (?, ?)"); ) {
            stmt.setString(1, user.login());
            stmt.setString(2, user.password());
            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Не удалось создать пользователя! Таблица не изменена");
            }
            try (ResultSet gk = stmt.getGeneratedKeys()) {
                if (gk.next()) {
                    user.id(gk.getLong(1));
                    return true;
                } else {
                    throw new SQLException("Не удалось создать пользователя! ID не изменены!");
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean update(User user) throws SQLException {
        try {
            PreparedStatement stmt = dbManager.getPreparedStatement("UPDATE DragonUser SET login = ?, password = ? WHERE id = ?");
            stmt.setString(1, user.login());
            stmt.setString(2, user.password());
            stmt.setLong(3, user.id());
            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Не удалось создать пользователя! Таблица не изменена!");
            }
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
