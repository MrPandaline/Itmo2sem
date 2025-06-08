package laba5.client.gui.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.*;

import laba5.client.Client;
import laba5.client.gui.Refreshable;
import laba5.client.l18n.Messages;
import laba5.common.commands.ICommand;
import laba5.common.commands.IMultiLineCommand;
import laba5.common.commands.IServerSideCommand;
import laba5.common.dataExchanging.Request;
import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.UnfinishedDragon;
import laba5.common.exceptions.CommandNotFound;
import laba5.common.genetics.DragonBreeder;
import laba5.common.model.Dragon;

public class DragonBreedingPanel extends Pane implements Refreshable {

    private ComboBox<Dragon> parent1ComboBox;
    private ComboBox<Dragon> parent2ComboBox;
    private Client client;

    public DragonBreedingPanel(Client client, Locale locale) {
        this.client = client;
        Messages.setLocale(locale); // Установка текущей локали

        initializeUI();
    }

    private void initializeUI() {
        ObservableList<Dragon> dragons = FXCollections.observableArrayList(loadUserDragons());

        parent1ComboBox = new ComboBox<>(dragons);
        parent1ComboBox.setPromptText(getMessage("dragon.breeding.parent1"));

        parent2ComboBox = new ComboBox<>(dragons);
        parent2ComboBox.setPromptText(getMessage("dragon.breeding.parent2"));

        setupComboBox(parent1ComboBox);
        setupComboBox(parent2ComboBox);

        Button breedButton = new Button(getMessage("dragon.breeding.button.breed"));
        breedButton.setOnAction(event -> handleBreed());

        VBox panel = new VBox(10,
                new Label(getMessage("dragon.breeding.label.select")),
                parent1ComboBox,
                parent2ComboBox,
                breedButton
        );
        panel.setPadding(new Insets(10));

        this.getChildren().add(panel);
    }

    private List<Dragon> loadUserDragons() {
        ArrayList<Dragon> dragons = new ArrayList<>();
        try {
            ICommand cmd = client.getCommandManager().getCommandByName("show");
            Request request = new Request((IServerSideCommand) cmd, client.userauth.getUser(), new String[]{"200"}, false, null);
            Response resp = client.communicateWithServer(request);
            dragons.addAll(List.of(resp.responseClaster().objects()));
        } catch (CommandNotFound | IOException e) {
            showAlert("alert.title.error", "dragon.breeding.error.loading_dragons");
        }
        return dragons;
    }

    private void onDragonBred(UnfinishedDragon child) {
        try {
            ICommand cmd = client.getCommandManager().getCommandByName("add");
            IMultiLineCommand imlcmd = (IMultiLineCommand) cmd;
            ArrayDeque<Object> children = new ArrayDeque<>();
            children.addLast(child);
            imlcmd.setAdditionalUserInput(children);

            Request request = new Request((IServerSideCommand) cmd, client.userauth.getUser(), null, false, null);
            Response resp = client.communicateWithServer(request);

            if (resp.statusCode() == 200) {
                showAlert("alert.title.success", "dragon.breeding.success.dragon_added");
            } else {
                showAlert("alert.title.error", "dragon.breeding.error.failed_adding");
            }

        } catch (CommandNotFound | IOException e) {
            showAlert("alert.title.error", "dragon.breeding.error.failed_adding");
        }
    }

    private void handleBreed() {
        Dragon parent1 = parent1ComboBox.getValue();
        Dragon parent2 = parent2ComboBox.getValue();

        if (parent1 == null || parent2 == null) {
            showAlert("alert.title.error", "dragon.breeding.error.select_parents");
            return;
        }

        if (!parent1.isAlive() || !parent2.isAlive()) {
            showAlert("alert.title.error", "dragon.breeding.error.dragons_must_be_alive");
            return;
        }

        if (parent1.equals(parent2)) {
            showAlert("alert.title.error", "dragon.breeding.error.cannot_breed_self");
            return;
        }

        UnfinishedDragon child = DragonBreeder.breed(parent1, parent2);

        showDragonInfo(child);
        onDragonBred(child);
    }

    private void showDragonInfo(UnfinishedDragon dragon) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(getMessage("dragon.breeding.alert.title.new_dragon"));
        alert.setHeaderText(getMessage("dragon.breeding.alert.header.new_dragon"));

        String content = String.format(
                "%s: %s\n%s: %s\n%s: %s\n%s: %d %s\n%s: %s",
                getMessage("dragon.field.name"),
                dragon.name(),
                getMessage("dragon.field.type"),
                dragon.dragonType(),
                getMessage("dragon.field.character"),
                dragon.dragonCharacter(),
                getMessage("dragon.field.age"),
                dragon.age(),
                getMessage("dragon.unit.years"),
                getMessage("dragon.field.description"),
                dragon.description()
        );

        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlert(String titleKey, String messageKey) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(getMessage(titleKey));
        alert.setHeaderText(null);
        alert.setContentText(getMessage(messageKey));
        alert.showAndWait();
    }

    private void setupComboBox(ComboBox<Dragon> comboBox) {
        final int MAX_LENGTH = 20; // максимальная длина строки

        comboBox.setCellFactory(cb -> new ListCell<Dragon>() {
            @Override
            protected void updateItem(Dragon item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String name = item.name();
                    if (name.length() > MAX_LENGTH) {
                        setText(name.substring(0, MAX_LENGTH - 3) + "...");
                    } else {
                        setText(name);
                    }
                }
            }
        });

        comboBox.setConverter(new javafx.util.StringConverter<Dragon>() {
            @Override
            public String toString(Dragon dragon) {
                if (dragon == null) return "";
                String name = dragon.name();
                if (name.length() > MAX_LENGTH) {
                    return name.substring(0, MAX_LENGTH - 3) + "...";
                } else {
                    return name;
                }
            }

            @Override
            public Dragon fromString(String string) {
                return null;
            }
        });
    }

    private String getMessage(String key) {
        return (String) Messages.getBundle().handleGetObject(key);
    }

    @Override
    public void refreshWithNewLocale(Locale newLocale) {
        Messages.setLocale(newLocale);
        this.getChildren().clear();
        initializeUI();
    }
}