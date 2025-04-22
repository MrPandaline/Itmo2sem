package laba5.server;


import laba5.server.logging.ConsoleLogger;
import laba5.server.logging.IServerLogger;
import laba5.server.logic.DBManager;
import laba5.server.storage.IModelStorageManager;
import laba5.server.storage.ModelCSVStoragingManager;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("для Корректной работы добавьте название .csv файла при запуске сервера!");
        }
        else {
            DBManager dbManager = new DBManager("", "s468126", "");
            IServerLogger logger = new ConsoleLogger();
            IModelStorageManager storageManager = new ModelCSVStoragingManager(args[0]);
            Server server = new Server(storageManager, logger, dbManager);
            server.run();
        }
    }
}
