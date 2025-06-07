package laba5.client.gui.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import laba5.client.Client;
import laba5.client.handlers.GUIDragonHandler;
import laba5.common.commands.IClientSideCommand;
import laba5.common.commands.ICommand;
import laba5.common.commands.IMultiLineCommand;
import laba5.common.commands.IServerSideCommand;
import laba5.common.dataExchanging.Request;
import laba5.common.dataExchanging.Response;
import laba5.common.exceptions.CommandNotFound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class CommandsPanel extends GridPane {

    public CommandsPanel(Client client, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("laba5.client.l18n.Messages", locale);

        setPadding(new Insets(20));
        setVgap(10);
        setHgap(10);

        ArrayList<String> commandNames = client.getCommandManager().getCommandNames();
        ObservableList<String> commandsObservable = FXCollections.observableArrayList(commandNames);
        ComboBox<String> commandsBox = new ComboBox<>(commandsObservable);
        Button executeButton = new Button("Выполнить команду");
        TextField commandTextField = new TextField("Дополнительные аргументы команды (если необходимо)");
        Label statusLabel = new Label();
        GUIDragonHandler handler = new GUIDragonHandler();

        add(commandsBox, 0, 0);
        add(executeButton, 1, 0);
        add(commandTextField, 2, 0);
        add(statusLabel, 1, 1);

        commandsBox.setValue(commandNames.get(2));

        commandsBox.valueProperty().addListener((observable, oldVal, newVal) -> {
            try {
                ICommand newCommand = client.getCommandManager().getCommandByName(new String(newVal));
                ICommand oldCommand = client.getCommandManager().getCommandByName(new String(oldVal));
                if (newCommand instanceof IMultiLineCommand  && !(oldCommand instanceof IMultiLineCommand)) {
                    add(handler, 0, 3);
                    setColumnSpan(handler, 3);
                }
                else if (oldCommand instanceof IMultiLineCommand  && !(newCommand instanceof IMultiLineCommand)) {
                    getChildren().remove(handler);
                }
            } catch (CommandNotFound e) {
                throw new RuntimeException(e);
            }
        });

        executeButton.setOnAction(e -> {
            boolean haveAdditionalInf;
            try {
                ICommand command = client.getCommandManager().getCommandByName(commandsBox.getValue());
                String[] args = commandTextField.getText().split(" ");
                if (command instanceof IMultiLineCommand) {

                    ((IMultiLineCommand) command).getAdditionalUserInput(client.getIoManager(), handler);
                    haveAdditionalInf = true;
                }
                if (command instanceof IClientSideCommand) {
                    ((IClientSideCommand) command).execute(client, args);
                    //TODO: придумать чё делать с клиентскими командами и их выводом
                }
                else if (command instanceof IServerSideCommand) {
                try {
                    Request req = new Request((IServerSideCommand) command, client.userauth.getUser(), args, false, null);
                    Response resp = client.communicateWithServer(req);
                    statusLabel.setText(resp.responseClaster().message());

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                }
            } catch (CommandNotFound ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}