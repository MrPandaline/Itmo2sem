package laba5.common;

public class Configuration {
    public final static int SERVER_PORT = 6789;
    public final static String SERVER_HOST = "localhost";
    public final static String DB_URL = "jdbc:postgresql://" + SERVER_HOST + ":" + SERVER_PORT + "/postgres";
    public final static String DB_USER = "postgres";
    public final static String DB_PASS = "123123";
}
