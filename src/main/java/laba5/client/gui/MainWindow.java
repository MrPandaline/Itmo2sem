package laba5.client.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import laba5.client.Client;
import laba5.client.gui.panels.CommandsPanel;
import laba5.client.gui.panels.DragonTablePanel;
import laba5.client.gui.panels.DragonVisualizerPane;
import laba5.client.gui.panels.RegistrationPanel;

import java.util.Locale;

public class MainWindow extends Application{

    private final Client client;
    private final Locale locale;

    public MainWindow(Client client, Locale locale) {
        this.client = client;
        this.locale = locale;
    }

    @Override
    public void start(Stage primaryStage) {
        showRegistrationScene(primaryStage);
    }

    private void showRegistrationScene(Stage stage) {
        RegistrationPanel registrationPanel = new RegistrationPanel(client, locale, success -> {
            if (Boolean.TRUE.equals(success)) {
                showMainScene(stage);
            }
        });

        Scene scene = new Scene(registrationPanel, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Регистрация");
        stage.show();
    }

    private void showMainScene(Stage stage) {
        TabPane tabPane = new TabPane();

        Tab tabCommands = new Tab("Команды");
        tabCommands.setClosable(false);
        tabCommands.setContent(new CommandsPanel(client, locale));

        Tab tabDragonsTable = new Tab("Дракончики");
        tabDragonsTable.setClosable(false);
        DragonTablePanel dragonTablePanel = new DragonTablePanel(client, locale);
        tabDragonsTable.setContent(dragonTablePanel);

        Tab tabDragonsVisualization = new Tab("Визуализация");
        tabDragonsVisualization.setClosable(false);
        DragonVisualizerPane dragonVisualizer = new DragonVisualizerPane(client);
        tabDragonsVisualization.setContent(dragonVisualizer);


        tabPane.getTabs().addAll(tabCommands, tabDragonsTable,tabDragonsVisualization);

        Scene scene = new Scene(tabPane, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Основное окно");
        stage.show();

        tabDragonsTable.setOnSelectionChanged(event -> {
            if (tabDragonsTable.isSelected()) {
                dragonTablePanel.refreshData(client);
            }
        });

        tabDragonsVisualization.setOnSelectionChanged(event -> {
            if (tabDragonsVisualization.isSelected()) {
                dragonVisualizer.updateFromServer();
            }
        });
    }
}