package laba5.client.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import laba5.client.Client;
import laba5.client.gui.panels.*;
import laba5.client.l18n.Messages;

import java.util.Locale;

public class MainWindow extends Application {

    private final Client client;
    private Locale locale;

    // Панели сохраняются как поля, чтобы можно было их обновить
    private CommandsPanel commandsPanel;
    private DragonTablePanel dragonTablePanel;
    private DragonVisualizerPane dragonVisualizer;
    private DragonBreedingPanel dragonBreedingPanel;

    private Tab tabCommands;
    private Tab tabDragonsTable;
    private Tab tabDragonsVisualization;
    private Tab tabDragonBreeding;

    // Добавляем поля для лейблов
    private Label userLabel;
    private Label langLabel; // Новый Label для надписи "Язык"

    public MainWindow(Client client, Locale locale) {
        this.client = client;
        this.locale = locale;
        Messages.setLocale(locale);
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
        stage.setTitle(getMessage("registration.title"));
        stage.show();
    }

    private void showMainScene(Stage stage) {
        this.stage = stage;

        HBox languageBox = createLanguageComboBox();

        // Создаем лейбл активного пользователя и сохраняем его как поле
        userLabel = new Label(getMessage("main.user.label") + client.userauth.getUser().login());
        userLabel.setStyle("-fx-padding: 10; -fx-font-weight: bold;");

        TabPane tabPane = new TabPane();

        // Инициализируем все панели
        commandsPanel = new CommandsPanel(client, locale);
        dragonTablePanel = new DragonTablePanel(client, locale);
        dragonVisualizer = new DragonVisualizerPane(client, locale);
        dragonBreedingPanel = new DragonBreedingPanel(client, locale);

        // Создаём вкладки
        tabCommands = new Tab(getMessage("main.tab.commands"));
        tabCommands.setClosable(false);
        tabCommands.setContent(new CommandsPanel(client, locale));

        tabDragonsTable = new Tab(getMessage("main.tab.dragons"));
        tabDragonsTable.setClosable(false);
        dragonTablePanel = new DragonTablePanel(client, locale);
        tabDragonsTable.setContent(dragonTablePanel);

        tabDragonsVisualization = new Tab(getMessage("main.tab.visualization"));
        tabDragonsVisualization.setClosable(false);
        dragonVisualizer = new DragonVisualizerPane(client, locale);
        tabDragonsVisualization.setContent(dragonVisualizer);

        tabDragonBreeding = new Tab(getMessage("main.tab.breeding"));
        tabDragonBreeding.setClosable(false);
        dragonBreedingPanel = new DragonBreedingPanel(client, locale);
        tabDragonBreeding.setContent(dragonBreedingPanel);

        tabPane.getTabs().addAll(tabCommands, tabDragonsTable, tabDragonsVisualization, tabDragonBreeding);

        VBox root = new VBox(languageBox, userLabel, tabPane);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle(getMessage("main.window.title"));
        stage.show();

        // Обновление данных при переключении вкладок
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

    // ComboBox для выбора языка
    private HBox createLanguageComboBox() {
        // Создаем Label для надписи "Язык" и сохраняем его как поле
        langLabel = new Label(getMessage("menu.language") + ":");
        ComboBox<Locale> langComboBox = new ComboBox<>();
        langComboBox.getItems().addAll(
                new Locale("ru"),
                new Locale("en", "ZA"),
                new Locale("fr"),
                new Locale("no", "NO")
        );
        langComboBox.setValue(locale);
        langComboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Locale l) {
                return switch (l.toString()) {
                    case "ru" -> "Русский";
                    case "en_ZA" -> "English (ZA)";
                    case "fr" -> "Français";
                    case "no_NO" -> "Norsk";
                    default -> "Unknown";
                };
            }

            @Override
            public Locale fromString(String s) {
                return switch (s) {
                    case "Русский" -> new Locale("ru");
                    case "English (ZA)" -> new Locale("en", "ZA");
                    case "Français" -> new Locale("fr");
                    case "Norsk" -> new Locale("no", "NO");
                    default -> Locale.getDefault();
                };
            }
        });

        langComboBox.setOnAction(e -> {
            Locale selectedLocale = langComboBox.getValue();
            if (!selectedLocale.equals(this.locale)) {
                updateLocale(selectedLocale);
                refreshUI();
            }
        });

        HBox box = new HBox(10, langLabel, langComboBox); // Добавляем Label и ComboBox в HBox
        box.setPadding(new Insets(10));
        return box;
    }

    private void updateLocale(Locale newLocale) {
        this.locale = newLocale;
        Messages.setLocale(newLocale);
    }

    private void refreshUI() {
        Platform.runLater(() -> {

            commandsPanel.refreshWithNewLocale(locale);
            dragonTablePanel.refreshWithNewLocale(locale);
            dragonVisualizer.refreshWithNewLocale(locale);
            dragonBreedingPanel.refreshWithNewLocale(locale);

            tabCommands.setText(getMessage("main.tab.commands"));
            tabDragonsTable.setText(getMessage("main.tab.dragons"));
            tabDragonsVisualization.setText(getMessage("main.tab.visualization"));
            tabDragonBreeding.setText(getMessage("main.tab.breeding"));

            if (stage != null) {
                stage.setTitle(getMessage("main.window.title"));
            }

            if (userLabel != null) {
                userLabel.setText(getMessage("main.user.label") + client.userauth.getUser().login());
            }

            // Обновляем текст лейбла "Язык"
            if (langLabel != null) {
                langLabel.setText(getMessage("menu.language") + ":");
            }

        });
    }

    private String getMessage(String key) {
        return (String) Messages.getBundle().handleGetObject(key);
    }

    private Stage stage;
}