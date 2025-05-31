package laba5.client.gui;// RegistrationWindow.java
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import laba5.client.Client;
import laba5.client.HashPassword;
import laba5.client.input.ConsoleIOManager;
import laba5.client.input.IIOManager;
import laba5.common.dataExchanging.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class RegistrationWindow {

    private final Client client;
    private final ResourceBundle bundle;

    public RegistrationWindow(Client client, Locale locale) {
        this.client = client;
        this.bundle = ResourceBundle.getBundle("laba5.client.l18n.Messages", locale);
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle(bundle.getString("registration.title"));

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label usernameLabel = new Label(bundle.getString("registration.username"));
        TextField usernameField = new TextField();

        Label passwordLabel = new Label(bundle.getString("registration.password"));
        PasswordField passwordField = new PasswordField();

        Button registerButton = new Button(bundle.getString("registration.registerButton"));
        Label statusLabel = new Label();

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(registerButton, 1, 3);
        grid.add(statusLabel, 1, 4);

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = HashPassword.hashPassword(passwordField.getText());

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText(bundle.getString("registration.emptyFields"));
                return;
            }

            Request req =  client.userauth.createUserRequest(username, password);
            try {
                client.communicateWithServer(req);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}