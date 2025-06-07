package laba5.client.gui.panels;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import laba5.client.Client;
import laba5.common.dataExchanging.Request;
import laba5.client.HashPassword;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class RegistrationPanel extends GridPane {

    public RegistrationPanel(Client client, Locale locale, RegistrationSuccessListener listener) {
        ResourceBundle bundle = ResourceBundle.getBundle("laba5.client.l18n.Messages", locale);

        setPadding(new Insets(20));
        setVgap(10);
        setHgap(10);

        Label usernameLabel = new Label(bundle.getString("registration.username"));
        TextField usernameField = new TextField();

        Label passwordLabel = new Label(bundle.getString("registration.password"));
        PasswordField passwordField = new PasswordField();

        Button registerButton = new Button(bundle.getString("registration.registerButton"));
        Label statusLabel = new Label();

        add(usernameLabel, 0, 0);
        add(usernameField, 1, 0);
        add(passwordLabel, 0, 1);
        add(passwordField, 1, 1);
        add(registerButton, 1, 3);
        add(statusLabel, 1, 4);

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = HashPassword.hashPassword(passwordField.getText());

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText(bundle.getString("registration.emptyFields"));
                return;
            }

            Request req = client.userauth.createUserRequest(username, password);
            try {
                var resp = client.communicateWithServer(req);
                String message = resp.responseClaster().message();
                statusLabel.setText(message);
                if ("Вы авторизованы!".equals(message) || ("Вы зарегистрированы!").equals(message)) {
                    listener.onRegistrationSuccess(true);
                    client.userauth.getUser().id(resp.statusCode());
                }

            } catch (IOException ex) {
                statusLabel.setText(bundle.getString("registration.connectionError"));
            }
        });
    }
}