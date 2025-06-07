package laba5.client.handlers;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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



public class GUIDragonHandler extends ScrollPane implements ModelHandler {

    // Поля ввода
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

    private final CheckBox haveKillerBox = new CheckBox("Have Killer");
    private final TextField killerNameField = new TextField();
    private final TextField killerHeightField = new TextField();
    private final TextField locationXField = new TextField();
    private final TextField locationYField = new TextField();
    private final TextField locationZField = new TextField();

    private UnfinishedDragon dragon = null;
    private final VBox content;

    public GUIDragonHandler() {
        content = new VBox(10);
        content.setMinHeight(Region.USE_PREF_SIZE);

        Label title = new Label("Enter Dragon Details");
        content.getChildren().add(title);

        addLabelAndField("Name:", nameField);
        addLabelAndField("Coordinates X (> -589):", xCoordField);
        addLabelAndField("Coordinates Y:", yCoordField);
        addLabelAndField("Age (> 0):", ageField);
        addLabelAndField("Description (optional):", descriptionArea);
        addLabelAndField("Type:", typeBox);
        addLabelAndField("Character:", characterBox);


        content.getChildren().add(haveKillerBox);

        VBox killerFields = new VBox(10);
        addLabelAndField(killerFields, "Killer Name (optional):", killerNameField);
        addLabelAndField(killerFields, "Killer Height (> 0):", killerHeightField);
        addLabelAndField(killerFields, "Killer Eye Color:", eyeColorBox);
        addLabelAndField(killerFields, "Killer Hair Color:", hairColorBox);
        addLabelAndField(killerFields, "Killer Nationality:", nationalityBox);
        addLabelAndField(killerFields, "Killer Location X:", locationXField);
        addLabelAndField(killerFields, "Killer Location Y:", locationYField);
        addLabelAndField(killerFields, "Killer Location Z:", locationZField);

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

    private void addLabelAndField(VBox container, String labelText, Node inputField) {
        Label label = new Label(labelText);
        VBox.setVgrow(inputField, Priority.ALWAYS);
        container.getChildren().addAll(label, inputField);
    }

    private String validateString(String value, String fieldName) throws IllegalArgumentException {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return value.trim();
    }

    private Integer validateInteger(String value, String fieldName, java.util.function.Predicate<Integer> condition) throws IllegalArgumentException {
        Integer intValue = Integer.parseInt(value);
        if (!condition.test(intValue)) {
            throw new IllegalArgumentException(fieldName + " does not meet the requirements.");
        }
        return intValue;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public UnfinishedDragon handleDragon() {
        try {
            String name = validateString(nameField.getText(), "Name");
            Integer xCoord = validateInteger(xCoordField.getText(), "Coordinates X", val -> val > -589);
            int yCoord = Integer.parseInt(yCoordField.getText());
            long age = Long.parseLong(ageField.getText());
            if (age <= 0) throw new IllegalArgumentException("Age must be > 0");

            String description = descriptionArea.getText().trim();
            if (description.isEmpty()) description = null;

            DragonType type = typeBox.getValue();
            DragonCharacter character = characterBox.getValue();
            if (character == null) throw new IllegalArgumentException("Character is required");

            Person killer = null;
            if (haveKillerBox.isSelected()) {
                String killerName = killerNameField.getText().trim();
                if (killerName.isEmpty()) throw new IllegalArgumentException("Killer name cannot be empty when 'Have Killer' is selected");

                int killerHeight = Integer.parseInt(killerHeightField.getText());
                if (killerHeight <= 0) throw new IllegalArgumentException("Killer height must be > 0");

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
                    killer);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Dragon created successfully!");
            alert.showAndWait();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numeric values.");
            dragon = null;
        } catch (IllegalArgumentException e) {
            showAlert("Validation Error", e.getMessage());
            dragon = null;
        }
        return dragon;
    }
}