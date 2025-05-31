package laba5.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import laba5.client.gui.RegistrationWindow;
import laba5.client.l18n.Messages;
import laba5.common.commands.*;
import laba5.client.input.ConsoleIOManager;
import laba5.client.input.IIOManager;
import laba5.server.logic.CommandManager;

import java.util.Locale;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String historyCommands = "commandsFileName.csv";
        CommandManager commandManager = new CommandManager();
        commandManager.addCommands(
                new Help(), new Info(), new Show(), new Add(), new Update(),
                new RemoveById(), new Clear(), new Save(), new ExecuteScript(),
                new Exit(), new RemoveHead(), new RemoveGreater(), new History(),
                new GroupCountingByName(), new FilterGreaterThanType(),
                new PrintFieldDescendingKiller()
        );

        Locale locale = new Locale("ru");
        Messages.setLocale(locale);

        IIOManager ioManager = new ConsoleIOManager(commandManager.getCommandNames());
        Client client = new Client(ioManager, historyCommands, commandManager);
        /*
        new Thread(client::run).start();

        Platform.runLater(() -> {
            RegistrationWindow registrationWindow = new RegistrationWindow(client, locale);
            registrationWindow.show();
        });
        */
        RegistrationWindow registrationWindow = new RegistrationWindow(client, locale);
        registrationWindow.show();
    }
}