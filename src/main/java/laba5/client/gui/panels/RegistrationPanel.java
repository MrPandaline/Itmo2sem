package laba5.client.gui.panels;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import laba5.client.Client;
import laba5.common.dataExchanging.Request;
import laba5.client.HashPassword;
import laba5.client.l18n.Messages;

import java.io.IOException;
import java.util.Locale;

public class RegistrationPanel extends GridPane {

    public interface RegistrationSuccessListener {
        void onRegistrationSuccess(boolean success);
    }

    public RegistrationPanel(Client client, Locale locale, RegistrationSuccessListener listener) {
        Messages.setLocale(locale); // Установка текущей локали

        setPadding(new Insets(20));
        setVgap(10);
        setHgap(10);

        Label usernameLabel = new Label(getMessage("registration.username"));
        TextField usernameField = new TextField();

        Label passwordLabel = new Label(getMessage("registration.password"));
        PasswordField passwordField = new PasswordField();

        Button registerButton = new Button(getMessage("registration.registerButton"));

        add(usernameLabel, 0, 0);
        add(usernameField, 1, 0);
        add(passwordLabel, 0, 1);
        add(passwordField, 1, 1);
        add(registerButton, 1, 3);

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = HashPassword.hashPassword(passwordField.getText());

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(getMessage("alert.title.error"), getMessage("registration.emptyFields"));
                return;
            }

            Request req = client.userauth.createUserRequest(username, password);
            try {
                var resp = client.communicateWithServer(req);
                String message = resp.responseClaster().message();

                if ("Вы авторизованы!".equals(message) || "Вы зарегистрированы!".equals(message)) {
                    showAlert(getMessage("alert.title.success"), message);
                    client.userauth.getUser().id(resp.statusCode());
                    listener.onRegistrationSuccess(true);
                } else {
                    showAlert(getMessage("alert.title.info"), message);
                }

            } catch (IOException ex) {
                showAlert(getMessage("alert.title.error"), getMessage("registration.connectionError"));
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getMessage(String key) {
        return (String) Messages.getBundle().handleGetObject(key);
    }
}