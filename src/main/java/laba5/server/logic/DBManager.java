package laba5.server.logic;

import laba5.common.Configuration;
import laba5.common.model.Dragon;
import laba5.common.model.User;
import laba5.common.model.modelEnums.Color;
import laba5.common.model.modelEnums.Country;
import laba5.common.model.modelEnums.DragonCharacter;
import laba5.common.model.modelEnums.DragonType;
import laba5.server.db.DragonDBProcessor;
import laba5.server.db.UserDBProcessor;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;

public class DBManager {
    private final String DB_URL;
    private final String USER;
    private final String PASS;
    private Connection connection = null;
    private final UserDBProcessor dbUserProcessor;
    private final DragonDBProcessor dbDragonProcessor;
    private static DBManager instance;


    private DBManager() {
        this.DB_URL = Configuration.DB_URL;
        this.USER = Configuration.DB_USER;
        this.PASS = Configuration.DB_PASS;

        this.dbUserProcessor = new UserDBProcessor(this);
        this.dbDragonProcessor = new DragonDBProcessor(this);

        connect();
        initializeTables();

        instance = this;
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public boolean connect() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(" Драйвер JDBC PostgreSQL не найден. Добавьте его в путь библиотеки.");
            return false;
        }

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
             }
        catch (SQLException e) {
            System.err.println("Ошибка при соединении с базой данных!");
            e.printStackTrace();
            return false;
        }

        if (connection == null) {
            System.err.println("Ошибка при соединении с базой данных!");
            return false;
        }

        return true;
    }
    public Statement getStatement() throws SQLException { return connection.createStatement(); }
    public PreparedStatement getPreparedStatement(String s) throws SQLException { return connection.prepareStatement(s); }
    public PreparedStatement  getPreparedStatementRGK(String s) throws SQLException { return connection.prepareStatement(s, Statement.RETURN_GENERATED_KEYS); }

    public boolean initializeTables() {
        if (connection != null) {
            try {
                //TODO: text и varchar привести к одному виду, понять как ограничить длину строк при вводе данных из консоли.
                Statement stmt = getStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS DragonUser(id serial PRIMARY KEY, login text NOT NULL UNIQUE, password text NOT NULL);");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Color(id serial PRIMARY KEY, name varchar(40) NOT NULL);");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Country(id serial PRIMARY KEY, name varchar(40) NOT NULL);");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS DragonCharacter(id serial PRIMARY KEY, name varchar(40) NOT NULL);");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS DragonType(id serial PRIMARY KEY, name varchar(40) NOT NULL);");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Location(id serial PRIMARY KEY, x FLOAT NOT NULL, y DOUBLE PRECISION NOT NULL, z INTEGER NOT NULL, name varchar(40));");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Coordinates(id serial PRIMARY KEY, x FLOAT NOT NULL, y INTEGER NOT NULL);");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Person(id serial PRIMARY KEY, name text NOT NULL, height INTEGER NOT NULL, id_eyeColor INTEGER REFERENCES Color, id_hairColor INTEGER REFERENCES Color, id_country INTEGER REFERENCES Country, id_location INTEGER REFERENCES Location);");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Dragon(id SERIAL PRIMARY KEY, name text NOT NULL, id_coordinates INTEGER REFERENCES Coordinates NOT NULL, creationDate timestamp NOT NULL, age BIGINT NOT NULL, description text, id_dragonType INTEGER REFERENCES DragonType, id_dragonCharacter INTEGER REFERENCES DragonCharacter, id_killer INTEGER REFERENCES Person, id_user INTEGER REFERENCES DragonUser);");
                stmt.close();


                PreparedStatement pstmt1 = getPreparedStatement("INSERT INTO Color (id, name) VALUES (?, ?) ON CONFLICT (id) DO UPDATE SET name = excluded.name;");
                for (Color color : Color.values()) {
                    pstmt1.setInt(1, color.ordinal() + 1);
                    pstmt1.setString(2, color.toString());
                    pstmt1.executeUpdate();
                }

                //TODO: придумать как улучшить код
                /*
                BiFunction<String, Enum<?>, Boolean> adder = (name, _enum) -> {
                    try {
                        var pstmt2 = getPreparedStatement("INSERT INTO "+ name + " (id, name) VALUES (?, ?) ON CONFLICT (id) DO UPDATE SET name = excluded.name;");
                        for (Country country : Country.values()) {
                            pstmt2.setInt(1, country.ordinal() + 1);
                            pstmt2.setString(2, country.toString());
                            pstmt2.executeUpdate();
                        }
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                    }
                };
                 */

                PreparedStatement pstmt2 = getPreparedStatement("INSERT INTO Country (id, name) VALUES (?, ?) ON CONFLICT (id) DO UPDATE SET name = excluded.name;");
                for (Country country : Country.values()) {
                    pstmt2.setInt(1, country.ordinal() + 1);
                    pstmt2.setString(2, country.toString());
                    pstmt2.executeUpdate();
                }

                PreparedStatement pstmt3 = getPreparedStatement("INSERT INTO DragonCharacter (id, name) VALUES (?, ?) ON CONFLICT (id) DO UPDATE SET name = excluded.name;");
                for (DragonCharacter character : DragonCharacter.values()) {
                    pstmt3.setInt(1, character.ordinal() + 1);
                    pstmt3.setString(2, character.toString());
                    pstmt3.executeUpdate();
                }

                PreparedStatement pstmt4 = getPreparedStatement("INSERT INTO DragonType (id, name) VALUES (?, ?) ON CONFLICT (id) DO UPDATE SET name = excluded.name;");
                for (DragonType type :DragonType.values()) {
                    pstmt4.setInt(1, type.ordinal() + 1);
                    pstmt4.setString(2, type.toString());
                    pstmt4.executeUpdate();
                }

                return true;
            } catch (SQLException e) {
                System.err.println(e.toString());
                return false;
            }
        }

        else {return false;}
    }

    public LinkedList<Dragon> selectDragon() {
        return dbDragonProcessor.select();
    }

    public LinkedList<Dragon> selectDragon(String name) {
        return dbDragonProcessor.select(name);
    }

    public boolean insertDragon(Dragon dragon, long userID) {
        return dbDragonProcessor.insert(dragon, userID);
    }
    public boolean updateDragon(Dragon dragon) {
        return dbDragonProcessor.update(dragon);
    }
    public boolean removeDragon(long id) {
        return dbDragonProcessor.remove(id);
    }

    public HashMap<Integer,User> selectUser() throws SQLException { return dbUserProcessor.select(); }
    public boolean insertUser(User user) throws SQLException { return dbUserProcessor.insert(user); }
    public boolean updateUser(User user) throws SQLException { return dbUserProcessor.update(user); }

}
