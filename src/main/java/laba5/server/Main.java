package laba5.server;


import laba5.server.logging.ConsoleLogger;
import laba5.server.logging.IServerLogger;
import laba5.server.logic.DBManager;
import laba5.server.storage.IModelStorageManager;
import laba5.server.storage.ModelCSVStoragingManager;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run();
    }
}
