package laba5.client.gui.panels;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import laba5.client.Client;
import laba5.client.gui.Refreshable;
import laba5.client.handlers.GUIDragonHandler;
import laba5.client.l18n.Messages;
import laba5.common.commands.IClientSideCommand;
import laba5.common.commands.ICommand;
import laba5.common.commands.IMultiLineCommand;
import laba5.common.commands.IServerSideCommand;
import laba5.common.dataExchanging.Request;
import laba5.common.dataExchanging.Response;
import laba5.common.exceptions.CommandNotFound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class CommandsPanel extends GridPane implements Refreshable {

    private final Client client;
    private Locale locale;

    private ComboBox<String> commandsBox;
    private Button executeButton;
    private TextField commandTextField;
    private GUIDragonHandler handler;

    public CommandsPanel(Client client, Locale locale) {
        this.client = client;
        this.locale = locale;
        Messages.setLocale(locale);
        initializeUI();
    }

    @Override
    public void refreshWithNewLocale(Locale newLocale) {
        System.out.println(newLocale.getCountry());

        Messages.setLocale(newLocale);
        this.locale = newLocale;


        updateUITexts();

        if (handler != null) {
            handler.refreshWithNewLocale(newLocale);
        }
    }

    private void initializeUI() {
        setPadding(new Insets(20));
        setVgap(10);
        setHgap(10);

        ArrayList<String> commandNames = client.getCommandManager().getCommandNames();
        ObservableList<String> commandsObservable = FXCollections.observableArrayList(commandNames);

        commandsBox = new ComboBox<>(commandsObservable);
        executeButton = new Button(getMessage("commands.execute.button"));
        commandTextField = new TextField(getMessage("commands.arguments.placeholder"));
        handler = new GUIDragonHandler(locale);

        add(commandsBox, 0, 0);
        add(executeButton, 1, 0);
        add(commandTextField, 2, 0);

        if (!commandNames.isEmpty()) {
            commandsBox.setValue(commandNames.get(2));
        }

        commandsBox.valueProperty().addListener((observable, oldVal, newVal) -> {
            try {
                ICommand newCommand = client.getCommandManager().getCommandByName(newVal);
                ICommand oldCommand = client.getCommandManager().getCommandByName(oldVal);
                if (newCommand instanceof IMultiLineCommand && !(oldCommand instanceof IMultiLineCommand)) {
                    add(handler, 0, 3);
                    setColumnSpan(handler, 3);
                } else if (oldCommand instanceof IMultiLineCommand && !(newCommand instanceof IMultiLineCommand)) {
                    getChildren().remove(handler);
                }
            } catch (CommandNotFound ex) {
                showAlert("alert.title.error", "commands.error.command_not_found", true);
            }
        });

        executeButton.setOnAction(e -> {
            try {
                ICommand command = client.getCommandManager().getCommandByName(commandsBox.getValue());
                String[] args = commandTextField.getText().split(" ");
                if (command instanceof IMultiLineCommand) {
                    ((IMultiLineCommand) command).getAdditionalUserInput(client.getIoManager(), handler);
                }

                if (command instanceof IClientSideCommand) {
                    String message = ((IClientSideCommand) command).execute(client, args);
                    showAlert("alert.title.info", message, false);

                } else if (command instanceof IServerSideCommand) {
                    System.out.println(client.userauth.getUser().id());

                    Request req = new Request((IServerSideCommand) command, client.userauth.getUser(), args, false, null);
                    Response resp = client.communicateWithServer(req);
                    showAlert("alert.title.success", resp.responseClaster().message(), false);

                }
            } catch (CommandNotFound ex) {
                showAlert("alert.title.error", "commands.error.command_not_found", true);
            } catch (IOException ex) {
                showAlert("alert.title.error", "commands.error.communication_failed", true);
            }
        });
    }

    private void updateUITexts() {
        executeButton.setText(getMessage("commands.execute.button"));
        commandTextField.setPromptText(getMessage("commands.arguments.placeholder"));

        ArrayList<String> commandNames = client.getCommandManager().getCommandNames();
        commandsBox.setItems(FXCollections.observableArrayList(commandNames));

        if (!commandNames.isEmpty()) {
            commandsBox.setValue(commandNames.get(2));
        }
    }

    private void showAlert(String titleKey, String messageKey, boolean useAliases) {
        Stage stage = new Stage();
        stage.initModality(javafx.stage.Modality.APPLICATION_MODAL); // Блокируем взаимодействие с основным окном
        stage.setTitle(getMessage(titleKey));

        String contentText = useAliases ? getMessage(messageKey) : messageKey;

        TextArea textArea = new TextArea(contentText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(400, 300);

        Button okButton = new Button("OK");
        okButton.setOnAction(event -> stage.close());

        VBox vbox = new VBox(10, scrollPane, okButton);
        vbox.setStyle("-fx-padding: 10; -fx-background-color: white;");

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private String getMessage(String key) {
        return (String) Messages.getBundle().handleGetObject(key);
    }
}