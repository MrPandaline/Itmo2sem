package laba5.client.handlers;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.control.ScrollPane;

import laba5.common.ModelHandler;
import laba5.common.dataExchanging.UnfinishedDragon;
import laba5.common.model.Coordinates;
import laba5.common.model.Location;
import laba5.common.model.Person;
import laba5.common.model.modelEnums.Color;
import laba5.common.model.modelEnums.Country;
import laba5.common.model.modelEnums.DragonCharacter;
import laba5.common.model.modelEnums.DragonType;
import laba5.client.l18n.Messages;

import java.util.Locale;

public class GUIDragonHandler extends ScrollPane implements ModelHandler {


    private final TextField nameField = new TextField();
    private final TextField xCoordField = new TextField();
    private final TextField yCoordField = new TextField();
    private final TextField ageField = new TextField();
    private final TextArea descriptionArea = new TextArea();

    private final ComboBox<DragonType> typeBox = new ComboBox<>(FXCollections.observableArrayList(DragonType.values()));
    private final ComboBox<DragonCharacter> characterBox = new ComboBox<>(FXCollections.observableArrayList(DragonCharacter.values()));
    private final ComboBox<Color> eyeColorBox = new ComboBox<>(FXCollections.observableArrayList(Color.values()));
    private final ComboBox<Color> hairColorBox = new ComboBox<>(FXCollections.observableArrayList(Color.values()));
    private final ComboBox<Country> nationalityBox = new ComboBox<>(FXCollections.observableArrayList(Country.values()));

    private final CheckBox haveKillerBox = new CheckBox(getMessage("dragon.field.killer.has"));
    private final TextField killerNameField = new TextField();
    private final TextField killerHeightField = new TextField();
    private final TextField locationXField = new TextField();
    private final TextField locationYField = new TextField();
    private final TextField locationZField = new TextField();

    private UnfinishedDragon dragon = null;
    private final VBox content;
    private Locale locale;

    public GUIDragonHandler(Locale locale) {
        this.locale = locale;
        Messages.setLocale(locale); // Установка текущей локали

        content = new VBox(10);
        content.setMinHeight(Region.USE_PREF_SIZE);

        Label title = new Label(getMessage("dragon.edit.title"));
        content.getChildren().add(title);

        addLabelAndField(getMessage("dragon.field.name"), nameField);
        addLabelAndField(getMessage("dragon.field.coordinate.x") + " (> -589)", xCoordField);
        addLabelAndField(getMessage("dragon.field.coordinate.y"), yCoordField);
        addLabelAndField(getMessage("dragon.field.age") + " (> 0)", ageField);
        addLabelAndField(getMessage("dragon.field.description") + " (" + getMessage("optional") + "):", descriptionArea);
        addLabelAndField(getMessage("dragon.field.type"), typeBox);
        addLabelAndField(getMessage("dragon.field.character"), characterBox);

        content.getChildren().add(haveKillerBox);

        VBox killerFields = new VBox(10);
        addLabelAndField(killerFields, getMessage("dragon.field.killer.name") + " (" + getMessage("optional") + "):", killerNameField);
        addLabelAndField(killerFields, getMessage("dragon.field.killer.height") + " (> 0):", killerHeightField);
        addLabelAndField(killerFields, getMessage("dragon.field.killer.eyecolor"), eyeColorBox);
        addLabelAndField(killerFields, getMessage("dragon.field.killer.haircolor"), hairColorBox);
        addLabelAndField(killerFields, getMessage("dragon.field.killer.nationality"), nationalityBox);
        addLabelAndField(killerFields, getMessage("dragon.field.killer.location.x"), locationXField);
        addLabelAndField(killerFields, getMessage("dragon.field.killer.location.y"), locationYField);
        addLabelAndField(killerFields, getMessage("dragon.field.killer.location.z"), locationZField);

        content.getChildren().add(killerFields);

        Region spacer = new Region();
        spacer.setPrefHeight(20);
        content.getChildren().add(spacer);

        haveKillerBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            killerFields.setVisible(isNowSelected);
            killerFields.setManaged(isNowSelected);
        });

        killerFields.setVisible(false);
        killerFields.setManaged(false);

        this.setContent(content);
        this.setFitToWidth(true);
        this.setMinSize(400, 300);
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    private void addLabelAndField(String labelText, Node inputField) {
        Label label = new Label(labelText);
        VBox.setVgrow(inputField, Priority.ALWAYS);
        content.getChildren().addAll(label, inputField);
    }

    public void refreshWithNewLocale(Locale newLocale) {
        this.locale = newLocale;
        Messages.setLocale(newLocale); // Установка новой локали

        // Обновляем тексты всех меток и подсказок
        updateUITexts();
    }

    private void updateUITexts() {
        Platform.runLater(() -> {
            // Обновляем заголовок
            ((Label) content.getChildren().get(0)).setText(getMessage("dragon.edit.title"));

            // Обновляем метки и подсказки для полей
            updateLabelAndField(content, getMessage("dragon.field.name"), nameField);
            updateLabelAndField(content, getMessage("dragon.field.coordinate.x") + " (> -589)", xCoordField);
            updateLabelAndField(content, getMessage("dragon.field.coordinate.y"), yCoordField);
            updateLabelAndField(content, getMessage("dragon.field.age") + " (> 0)", ageField);
            updateLabelAndField(content, getMessage("dragon.field.description") + " (" + getMessage("optional") + "):", descriptionArea);
            updateLabelAndField(content, getMessage("dragon.field.type"), typeBox);
            updateLabelAndField(content, getMessage("dragon.field.character"), characterBox);

            haveKillerBox.setText(getMessage("dragon.field.killer.has"));

            // Обновляем метки внутри killerFields
            VBox killerFields = (VBox) content.getChildren().stream()
                    .filter(node -> node instanceof VBox)
                    .findFirst()
                    .orElse(null);
            if (killerFields != null) {
                updateLabelAndField(killerFields, getMessage("dragon.field.killer.name") + " (" + getMessage("optional") + "):", killerNameField);
                updateLabelAndField(killerFields, getMessage("dragon.field.killer.height") + " (> 0):", killerHeightField);
                updateLabelAndField(killerFields, getMessage("dragon.field.killer.eyecolor"), eyeColorBox);
                updateLabelAndField(killerFields, getMessage("dragon.field.killer.haircolor"), hairColorBox);
                updateLabelAndField(killerFields, getMessage("dragon.field.killer.nationality"), nationalityBox);
                updateLabelAndField(killerFields, getMessage("dragon.field.killer.location.x"), locationXField);
                updateLabelAndField(killerFields, getMessage("dragon.field.killer.location.y"), locationYField);
                updateLabelAndField(killerFields, getMessage("dragon.field.killer.location.z"), locationZField);
            }
        });
    }

    private void updateLabelAndField(VBox container, String labelText, Node inputField) {
        for (int i = 0; i < container.getChildren().size(); i++) {
            Node child = container.getChildren().get(i);
            if (child instanceof Label && container.getChildren().get(i + 1) == inputField) {
                ((Label) child).setText(labelText);
                break;
            }
        }
    }

    private void addLabelAndField(VBox container, String labelText, Node inputField) {
        Label label = new Label(labelText);
        VBox.setVgrow(inputField, Priority.ALWAYS);
        container.getChildren().addAll(label, inputField);
    }

    private String validateString(String value, String fieldName) throws IllegalArgumentException {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(getMessage("error.empty_field") + " " + fieldName);
        }
        return value.trim();
    }

    private Integer validateInteger(String value, String fieldName, java.util.function.Predicate<Integer> condition) throws IllegalArgumentException {
        Integer intValue = Integer.parseInt(value);
        if (!condition.test(intValue)) {
            throw new IllegalArgumentException(fieldName + " " + getMessage("error.does_not_meet_requirements"));
        }
        return intValue;
    }

    private void showAlert(String titleKey, String messageKey) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(getMessage(titleKey));
        alert.setHeaderText(null);
        alert.setContentText(getMessage(messageKey));
        alert.showAndWait();
    }

    @Override
    public UnfinishedDragon handleDragon() {
        try {
            String name = validateString(nameField.getText(), getMessage("dragon.field.name"));
            Integer xCoord = validateInteger(xCoordField.getText(), getMessage("dragon.field.coordinate.x"), val -> val > -589);
            int yCoord = Integer.parseInt(yCoordField.getText());
            long age = Long.parseLong(ageField.getText());
            if (age <= 0) throw new IllegalArgumentException(getMessage("error.dragon.age_must_be_greater_than_zero"));

            String description = descriptionArea.getText().trim();
            if (description.isEmpty()) description = null;

            DragonType type = typeBox.getValue();
            DragonCharacter character = characterBox.getValue();
            if (character == null) throw new IllegalArgumentException(getMessage("error.dragon.character_required"));

            Person killer = null;
            if (haveKillerBox.isSelected()) {
                String killerName = killerNameField.getText().trim();
                if (killerName.isEmpty()) throw new IllegalArgumentException(getMessage("error.killer_name_cannot_be_empty"));

                int killerHeight = Integer.parseInt(killerHeightField.getText());
                if (killerHeight <= 0) throw new IllegalArgumentException(getMessage("error.killer_height_must_be_greater_than_zero"));

                Color eyeColor = eyeColorBox.getValue();
                Color hairColor = hairColorBox.getValue();
                Country nationality = nationalityBox.getValue();

                Integer locX = Integer.parseInt(locationXField.getText());
                float locY = Float.parseFloat(locationYField.getText());
                Integer locZ = Integer.parseInt(locationZField.getText());

                Location location = new Location(locX, locY, locZ);
                killer = new Person(killerName, killerHeight, eyeColor, hairColor, nationality, location);
            }

            Coordinates coordinates = new Coordinates(xCoord, yCoord);
            dragon = new UnfinishedDragon(
                    name,
                    coordinates,
                    age,
                    description,
                    type,
                    character,
                    killer,
                    null);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(getMessage("alert.title.success"));
            alert.setContentText(getMessage("dragon.success.created"));
            alert.showAndWait();
        } catch (NumberFormatException e) {
            showAlert("alert.title.error", "error.enter_valid_numeric_values");
            dragon = null;
        } catch (IllegalArgumentException e) {
            showAlert("alert.title.error", "error.validation_failed");
            dragon = null;
        }
        return dragon;
    }

    private String getMessage(String key) {
        return (String) Messages.getBundle().handleGetObject(key);
    }
}