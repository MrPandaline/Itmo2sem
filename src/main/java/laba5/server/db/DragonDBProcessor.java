package laba5.server.db;

import laba5.common.model.Coordinates;
import laba5.common.model.Dragon;
import laba5.common.model.Location;
import laba5.common.model.Person;
import laba5.common.model.modelEnums.Color;
import laba5.common.model.modelEnums.Country;
import laba5.common.model.modelEnums.DragonCharacter;
import laba5.common.model.modelEnums.DragonType;
import laba5.server.logic.DBManager;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;

public class DragonDBProcessor {
    private final DBManager dbManager;

    public DragonDBProcessor(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public LinkedList<Dragon> select() {
        LinkedList<Dragon> dragons = new LinkedList<>();
        try {
            ResultSet rs = dbManager.getStatement().executeQuery(
                    "SELECT Dragon.id AS id, Dragon.name AS name, "
                            + "Coordinates.x AS c_x, Coordinates.y AS c_y, Dragon.creationDate AS creationDate, "
                            + "Dragon.age AS age, Dragon.description AS description, dragonType.name AS d_type, "
                            + "dragonCharacter.name AS d_character, Person.name AS p_name, Person.height AS p_height, "
                            + "eyeColor.name AS p_eyeColor, hairColor.name AS p_hairColor, Country.name AS p_nationality, "
                            + "Location.x AS l_x, Location.y AS l_y, Location.z AS l_z , "
                            + "Dragon.id_killer AS p_id, Dragon.id_user AS creator_id FROM Dragon "
                            + "JOIN Coordinates ON Coordinates.id = Dragon.id_coordinates "
                            + "LEFT JOIN DragonType ON Dragon.id_dragonType = DragonType.id "
                            + "LEFT JOIN DragonCharacter ON Dragon.id_dragonCharacter = DragonCharacter.id "
                            + "LEFT JOIN Person ON Dragon.id_killer = Person.id "
                            + "LEFT JOIN Location ON Person.id_location = Location.id "
                            + "LEFT JOIN Color AS eyeColor ON Person.id_eyeColor = eyeColor.id "
                            + "LEFT JOIN Color AS hairColor ON Person.id_hairColor = hairColor.id "
                            + "LEFT JOIN Country ON Person.id_country = Country.id;"
            );

            dragons = getDragons(rs);
            return dragons;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return dragons;
        }
    }

    public LinkedList<Dragon> select(String name){
        LinkedList<Dragon> dragons = new LinkedList<>();
        try (PreparedStatement prst = dbManager.getPreparedStatement(
                    "SELECT Dragon.id AS id, Dragon.name AS name, "
                            + "Coordinates.x AS c_x, Coordinates.y AS c_y, Dragon.creationDate AS creationDate, "
                            + "Dragon.age AS age, Dragon.description AS description, dragonType.name AS d_type, "
                            + "dragonCharacter.name AS d_character, Person.name AS p_name, Person.height AS p_height, "
                            + "eyeColor.name AS p_eyeColor, hairColor.name AS p_hairColor, Country.name AS p_nationality, "
                            + "Location.x AS l_x, Location.y AS l_y, Location.z AS l_z , "
                            + "Dragon.id_killer AS p_id, Dragon.id_user AS creator_id FROM Dragon "
                            + "JOIN Coordinates ON Coordinates.id = Dragon.id_coordinates "
                            + "LEFT JOIN DragonType ON Dragon.id_dragonType = DragonType.id "
                            + "LEFT JOIN DragonCharacter ON Dragon.id_dragonCharacter = DragonCharacter.id "
                            + "LEFT JOIN Person ON Dragon.id_killer = Person.id "
                            + "LEFT JOIN Location ON Person.id_location = Location.id "
                            + "LEFT JOIN Color AS eyeColor ON Person.id_eyeColor = eyeColor.id "
                            + "LEFT JOIN Color AS hairColor ON Person.id_hairColor = hairColor.id "
                            + "LEFT JOIN Country ON Person.id_country = Country.id "
                            + "WHERE Dragon.name = ?;")){

            prst.setString(1, name);
            ResultSet rs = prst.executeQuery();
            dragons = getDragons(rs);
            return dragons;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return dragons;
        }
    }

    private LinkedList<Dragon> getDragons(ResultSet rs) throws SQLException {
        LinkedList<Dragon> dragons = new LinkedList<>();
        while (rs.next()) {
            String description = null;
            try {
                description = rs.getString("description");
            } catch (IllegalArgumentException | NullPointerException ignored) {}
            DragonType dragonType = null;
            try {
                dragonType = Enum.valueOf(DragonType.class, rs.getString("d_type"));
            } catch (IllegalArgumentException | NullPointerException ignored) {}

            DragonCharacter dragonCharacter = null;
            try {
                dragonCharacter = Enum.valueOf(DragonCharacter.class, rs.getString("d_character"));
            } catch (IllegalArgumentException | NullPointerException ignored) {}

            Person person = null;
            try {
                rs.getLong("p_id");
                Color eyeColor = Enum.valueOf(Color.class, rs.getString("p_eyeColor"));
                Color hairColor = Enum.valueOf(Color.class, rs.getString("p_hairColor"));
                Country nationality = Enum.valueOf(Country.class, rs.getString("p_nationality"));
                Location location = new Location(rs.getFloat("l_x"), rs.getDouble("l_y"),
                        rs.getInt("l_z"));
                person = new Person(rs.getString("p_name"), rs.getInt("p_height"), eyeColor,
                        hairColor, nationality, location);


            } catch (IllegalArgumentException | NullPointerException ignored) {}

            Coordinates coordinates = new Coordinates(rs.getFloat("c_x"), rs.getInt("c_y"));

            Dragon dragon = new Dragon(rs.getString("name"), coordinates, rs.getLong("age"),
                    description, dragonType, dragonCharacter, person, rs.getLong("id"),
                    rs.getTimestamp("creationDate").toInstant().atZone(ZoneId.systemDefault()),
                    rs.getLong("creator_id"));

            dragons.add(dragon);
        }
        rs.close();
        return dragons;
    }

    public boolean insert(Dragon dragon, long userID) {
        try ( PreparedStatement stmt = dbManager.getPreparedStatementRGK("INSERT INTO Dragon(name, id_coordinates, "
                +"creationDate, age, description, id_dragonType, id_dragonCharacter, id_killer, id_user) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)") ) {
            stmt.setString(1, dragon.name());
            stmt.setLong(2, insert(dragon.coordinates()));
            stmt.setObject(3, dragon.creationDate().toOffsetDateTime(), Types.TIMESTAMP_WITH_TIMEZONE);
            stmt.setLong(4, dragon.age());
            if (dragon.description() == null) {
                stmt.setNull(5, Types.VARCHAR);
            }
            else{
                stmt.setString(5, dragon.description());
            }

            if (dragon.dragonType() == null){
                stmt.setNull(6, Types.INTEGER);
            } else {
                stmt.setInt(6, dragon.dragonType().ordinal()+1);
            }
            if (dragon.dragonCharacter() == null){
                stmt.setNull(7, Types.INTEGER);
            } else {
                stmt.setInt(7, dragon.dragonCharacter().ordinal()+1);
            }
            if (dragon.killer() == null) {
                stmt.setNull(8, Types.INTEGER);
            } else {
                stmt.setLong(8, insert(dragon.killer()));
            }
            stmt.setLong(9, userID);

            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Вставка дракона не выполнена! Таблица не изменена.");
            }
            try (ResultSet gk = stmt.getGeneratedKeys()) {
                if (gk.next()) {
                    dragon.id(gk.getInt(1));
                    return true;
                } else {
                    throw new SQLException("Вставка дракона не выполнена! ID не получен."); }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private long insert(Location location) throws SQLException {
        try (PreparedStatement stmt = dbManager.getPreparedStatementRGK("INSERT INTO Location(x, y, z) "
                + "VALUES (?, ?, ?)")) {
            stmt.setFloat(1, location.x());
            stmt.setDouble(2, location.y());
            stmt.setInt(3, location.z());
            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Вставка локации не выполнена! Таблица не изменена.");
            }
            try (ResultSet gk = stmt.getGeneratedKeys()) {
                if (gk.next()) {
                    return gk.getLong(1);
                } else {
                    throw new SQLException("Вставка локации не выполнена! ID не получен.");
                }
            }
        }
    }

    private long insert(Coordinates coordinates) throws SQLException {
        try ( PreparedStatement stmt = dbManager.getPreparedStatementRGK("INSERT INTO Coordinates(x, y) VALUES (?, ?)")) {
            stmt.setFloat(1, coordinates.x());
            stmt.setLong(2, coordinates.y());
            if (stmt.executeUpdate() == 0) { throw new SQLException("Вставка координат не выполнена! Таблица не изменена."); }
            try (ResultSet gk = stmt.getGeneratedKeys()) {
                if (gk.next()) {
                    return gk.getLong(1);
                } else {
                    throw new SQLException("Вставка координат не выполнена! ID не получен."); }
            }
        }
    }

    private long insert(Person killer) throws SQLException {
        try ( PreparedStatement stmt = dbManager.getPreparedStatementRGK("INSERT INTO Person(name, height, "
                + "id_eyeColor, id_hairColor, id_country, id_location) VALUES (?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, killer.name());
            stmt.setInt(2, killer.height());
            stmt.setLong(3, killer.eyeColor().ordinal()+1);
            stmt.setLong(4, killer.hairColor().ordinal()+1);
            stmt.setLong(5, killer.nationality().ordinal()+1);
            stmt.setLong(6, insert(killer.location()));
            if (stmt.executeUpdate() == 0) { throw new SQLException("Вставка убийцы не выполнена! Таблица не изменена."); }
            try (ResultSet gk = stmt.getGeneratedKeys()) {
                if (gk.next()) {
                    return gk.getLong(1);
                } else {
                    throw new SQLException("Вставка убийцы не выполнена! ID не получен.");
                }
            }

        }
    }

    public boolean update(Dragon dragon) {
        try {
            PreparedStatement stmt = dbManager.getPreparedStatement("UPDATE Dragon SET name = ?, id_coordinates = ?,"
                    + " age = ?, description = ?, id_dragonType = ?, id_dragonCharacter = ?, id_killer = ?,"
                    + " WHERE id = ?");
            stmt.setString(1, dragon.name());
            stmt.setLong(2, insert(dragon.coordinates()));
            stmt.setLong(3, dragon.age());
            if (dragon.description() == null) {
                stmt.setNull(4, Types.VARCHAR);
            }
            else{
                stmt.setString(4, dragon.description());
            }

            if (dragon.dragonType() == null){
                stmt.setNull(5, Types.INTEGER);
            } else {
                stmt.setInt(5, dragon.dragonType().ordinal()+1);
            }
            if (dragon.dragonCharacter() == null){
                stmt.setNull(6, Types.INTEGER);
            } else {
                stmt.setInt(6, dragon.dragonCharacter().ordinal()+1);
            }
            if (dragon.killer() == null) {
                stmt.setNull(7, Types.INTEGER);
            } else {
                stmt.setLong(7, insert(dragon.killer()));
            }
            stmt.setLong(8, dragon.id());
            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Обновление дракона не выполнено! Таблица не изменена.");
            }
            return true;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean remove(long id) {
        try {
            PreparedStatement deleteStmt = dbManager.getPreparedStatement("DELETE FROM Dragon WHERE id = ?");
            deleteStmt.setLong(1, id);
            return deleteStmt.executeUpdate()>0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

}
