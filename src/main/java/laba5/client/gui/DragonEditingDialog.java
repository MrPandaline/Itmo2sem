package laba5.client.gui;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import laba5.common.dataExchanging.UnfinishedDragon;
import laba5.common.model.Dragon;
import laba5.common.model.Coordinates;
import laba5.common.model.Location;
import laba5.common.model.Person;
import laba5.common.model.modelEnums.Color;
import laba5.common.model.modelEnums.Country;
import laba5.common.model.modelEnums.DragonCharacter;
import laba5.common.model.modelEnums.DragonType;

public class DragonEditingDialog {

    public static UnfinishedDragon showEditingDialog(Dragon originalDragon, Stage owner) {
        Dialog<UnfinishedDragon> dialog = new Dialog<>();
        dialog.setTitle("Edit Dragon");
        dialog.setHeaderText("Modify dragon parameters");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // --- Основной layout с тремя столбцами ---
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(15);
        grid.setPadding(new Insets(20));

        int row = 0;

        // --- Левый столбец: основные данные о драконе ---
        TextField nameField = new TextField(originalDragon.name());
        grid.add(createLabelAndField("Name:", nameField), 0, row);

        TextField xCoordField = new TextField(String.valueOf(originalDragon.coordinates().x()));
        grid.add(createLabelAndField("Coordinates X:", xCoordField), 0, ++row);

        TextField yCoordField = new TextField(String.valueOf(originalDragon.coordinates().y()));
        grid.add(createLabelAndField("Coordinates Y:", yCoordField), 0, ++row);

        TextField ageField = new TextField(String.valueOf(originalDragon.age()));
        grid.add(createLabelAndField("Age:", ageField), 0, ++row);

        ComboBox<DragonType> typeBox = new ComboBox<>();
        typeBox.getItems().setAll(DragonType.values());
        typeBox.setValue(originalDragon.dragonType());
        grid.add(createLabelAndField("Type:", typeBox), 0, ++row);

        ComboBox<DragonCharacter> characterBox = new ComboBox<>();
        characterBox.getItems().setAll(DragonCharacter.values());
        characterBox.setValue(originalDragon.dragonCharacter());
        grid.add(createLabelAndField("Character:", characterBox), 0, ++row);

        // --- Средний столбец: описание и наличие убийцы ---
        TextArea descriptionArea = new TextArea(originalDragon.description());
        descriptionArea.setWrapText(true);
        descriptionArea.setMaxHeight(100);
        grid.add(createLabelAndField("Description:", descriptionArea), 1, 0, 1, 3); // объединение строк

        CheckBox haveKillerBox = new CheckBox("Has Killer");
        haveKillerBox.setSelected(originalDragon.killer() != null);
        grid.add(haveKillerBox, 1, 4);

        // --- Правый столбец: поля убийцы ---
        VBox killerFields = new VBox(8);

        TextField killerNameField = new TextField();
        killerFields.getChildren().add(createLabelAndField("Killer Name:", killerNameField));

        TextField killerHeightField = new TextField();
        killerFields.getChildren().add(createLabelAndField("Killer Height:", killerHeightField));

        ComboBox<Color> eyeColorBox = new ComboBox<>();
        eyeColorBox.getItems().setAll(Color.values());
        killerFields.getChildren().add(createLabelAndField("Eye Color:", eyeColorBox));

        ComboBox<Color> hairColorBox = new ComboBox<>();
        hairColorBox.getItems().setAll(Color.values());
        killerFields.getChildren().add(createLabelAndField("Hair Color:", hairColorBox));

        ComboBox<Country> nationalityBox = new ComboBox<>();
        nationalityBox.getItems().setAll(Country.values());
        killerFields.getChildren().add(createLabelAndField("Nationality:", nationalityBox));

        TextField locXField = new TextField();
        killerFields.getChildren().add(createLabelAndField("Location X:", locXField));

        TextField locYField = new TextField();
        killerFields.getChildren().add(createLabelAndField("Location Y:", locYField));

        TextField locZField = new TextField();
        killerFields.getChildren().add(createLabelAndField("Location Z:", locZField));

        if (originalDragon.killer() != null) {
            Person killer = originalDragon.killer();
            killerNameField.setText(killer.name());
            killerHeightField.setText(String.valueOf(killer.height()));
            eyeColorBox.setValue(killer.eyeColor());
            hairColorBox.setValue(killer.hairColor());
            nationalityBox.setValue(killer.nationality());

            Location loc = killer.location();
            locXField.setText(String.valueOf(loc.x()));
            locYField.setText(String.valueOf(loc.y()));
            locZField.setText(String.valueOf(loc.z()));
        }

        // По умолчанию скрыто
        killerFields.setVisible(originalDragon.killer() != null);
        killerFields.setManaged(originalDragon.killer() != null);

        grid.add(killerFields, 2, 0, 1, 6); // правый столбец, занимает все строки

        // Слушатель изменения состояния "убийцы"
        haveKillerBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            boolean visible = newVal;
            killerFields.setVisible(visible);
            killerFields.setManaged(visible);
        });

        // --- Добавляем всё в диалог ---
        dialog.getDialogPane().setContent(grid);
        dialog.initOwner(owner);

        dialog.getDialogPane().setMinSize(750, 500); // Увеличенная высота
        dialog.getDialogPane().setMaxSize(1920, 1080);

        // --- Результат ---
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    Coordinates coordinates = new Coordinates(
                            Float.parseFloat(xCoordField.getText()),
                            Integer.parseInt(yCoordField.getText())
                    );

                    Person killer = null;
                    if (haveKillerBox.isSelected()) {
                        Location location = new Location(
                                Float.parseFloat(locXField.getText()),
                                Double.parseDouble(locYField.getText()),
                                Integer.parseInt(locZField.getText())
                        );
                        killer = new Person(
                                killerNameField.getText(),
                                Integer.parseInt(killerHeightField.getText()),
                                eyeColorBox.getValue(),
                                hairColorBox.getValue(),
                                nationalityBox.getValue(),
                                location
                        );
                    }

                    return new UnfinishedDragon(
                            nameField.getText(),
                            coordinates,
                            Long.parseLong(ageField.getText()),
                            descriptionArea.getText(),
                            typeBox.getValue(),
                            characterBox.getValue(),
                            killer
                    );
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Invalid input format!");
                    alert.showAndWait();
                }
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    // Вспомогательный метод: создаёт пару Label + Node
    private static VBox createLabelAndField(String labelText, Node inputField) {
        Label label = new Label(labelText);
        VBox container = new VBox(2, label, inputField);
        VBox.setVgrow(inputField, Priority.NEVER);
        return container;
    }
}