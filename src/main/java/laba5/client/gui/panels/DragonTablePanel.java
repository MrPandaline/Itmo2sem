package laba5.client.gui.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import laba5.client.Client;
import laba5.client.gui.DragonEditingDialog;
import laba5.client.gui.logic.DragonFilter;
import laba5.common.commands.ICommand;
import laba5.common.commands.IMultiLineCommand;
import laba5.common.commands.IServerSideCommand;
import laba5.common.dataExchanging.Request;
import laba5.common.dataExchanging.Response;
import laba5.common.dataExchanging.UnfinishedDragon;
import laba5.common.exceptions.CommandNotFound;
import laba5.common.model.Dragon;
import laba5.common.model.Location;
import laba5.common.model.Person;
import laba5.common.model.modelEnums.DragonType;

import java.io.IOException;
import java.util.*;

public class DragonTablePanel extends BorderPane {

    private final Client client;
    private final ObservableList<Dragon> dragonData = FXCollections.observableArrayList();
    private final DragonFilter dragonFilter = new DragonFilter();

    private TableView<Dragon> tableView = new TableView<>();

    private final TextField nameFilter = new TextField();
    private final TextField ageFilter = new TextField();
    private final TextField coordsFilter = new TextField();
    private final TextField dateFilter = new TextField();
    private final TextField typeFilter = new TextField();
    private final TextField characterFilter = new TextField();
    private final TextField killerFilter = new TextField();
    private final TextField locationFilter = new TextField();

    private HBox filterBox;
    private final Map<String, HBox> filterControls = new HashMap<>();

    public DragonTablePanel(Client client, Locale locale) {
        this.client = client;
        refreshData(client);

        createFilterRow();
        setupMouseHandlers();
        setupFilters();
        createTable();

        tableView.setItems(FXCollections.observableArrayList(dragonFilter.apply(dragonData)));
        this.setCenter(tableView);
    }

    public void refreshData(Client client) {
        try {
            ICommand cmd = client.getCommandManager().getCommandByName("show");
            Request request = new Request((IServerSideCommand) cmd, client.userauth.getUser(), new String[]{"20"}, false, null);
            Response resp = client.communicateWithServer(request);

            dragonData.clear();
            dragonData.addAll(resp.responseClaster().objects());

            applyFilters();
        } catch (CommandNotFound | IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load dragons.");
        }
    }

    private void setupMouseHandlers() {
        tableView.setRowFactory(tv -> {
            TableRow<Dragon> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1 && !event.isControlDown()) {
                    Dragon selectedDragon = row.getItem();
                    editAndSaveDragon(selectedDragon);
                }
            });
            return row;
        });

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                double clickX = event.getX();
                int colIndex = -1;
                double totalWidth = 0;

                for (int i = 0; i < tableView.getColumns().size(); i++) {
                    TableColumn<Dragon, ?> col = tableView.getColumns().get(i);
                    totalWidth += col.getWidth();
                    if (clickX <= totalWidth) {
                        colIndex = i;
                        break;
                    }
                }

                if (colIndex != -1) {
                    TableColumn<Dragon, ?> clickedCol = tableView.getColumns().get(colIndex);
                    String colName = clickedCol.getText();

                    showFilterForColumn(colName);
                }
            }
        });
    }

    private void createFilterRow() {
        filterBox = new HBox(5);
        filterBox.setSpacing(10);
        this.setTop(filterBox);
    }

    private void showFilterForColumn(String columnName) {
        TextField textField;
        switch (columnName) {
            case "Name" -> textField = nameFilter;
            case "Age" -> textField = ageFilter;
            case "Coordinates" -> textField = coordsFilter;
            case "Creation Date" -> textField = dateFilter;
            case "Type" -> textField = typeFilter;
            case "Character" -> textField = characterFilter;
            case "Killer" -> textField = killerFilter;
            case "Killer Location" -> textField = locationFilter;
            default -> {
                return;
            }
        }

        HBox control = filterControls.computeIfAbsent(columnName, k -> getFilterControl(k, textField));

        if (!filterBox.getChildren().contains(control)) {
            filterBox.getChildren().add(control);
        }

        textField.requestFocus();

        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isBlank()) {
                filterBox.getChildren().remove(control);
            } else if (!filterBox.getChildren().contains(control)) {
                filterBox.getChildren().add(control);
            }

            applyFilters();
        });
    }

    private void setupFilters() {
        nameFilter.textProperty().addListener((obs, o, n) -> dragonFilter.setNameFilter(n));
        ageFilter.textProperty().addListener((obs, o, n) -> dragonFilter.setAgeFilter(n));
        coordsFilter.textProperty().addListener((obs, o, n) -> dragonFilter.setCoordsFilter(n));
        dateFilter.textProperty().addListener((obs, o, n) -> dragonFilter.setDateFilter(n));
        typeFilter.textProperty().addListener((obs, o, n) -> dragonFilter.setTypeFilter(n));
        characterFilter.textProperty().addListener((obs, o, n) -> dragonFilter.setCharacterFilter(n));
        killerFilter.textProperty().addListener((obs, o, n) -> dragonFilter.setKillerFilter(n));
        locationFilter.textProperty().addListener((obs, o, n) -> dragonFilter.setLocationFilter(n));

        nameFilter.textProperty().addListener((obs, o, n) -> applyFilters());
        ageFilter.textProperty().addListener((obs, o, n) -> applyFilters());
        coordsFilter.textProperty().addListener((obs, o, n) -> applyFilters());
        dateFilter.textProperty().addListener((obs, o, n) -> applyFilters());
        typeFilter.textProperty().addListener((obs, o, n) -> applyFilters());
        characterFilter.textProperty().addListener((obs, o, n) -> applyFilters());
        killerFilter.textProperty().addListener((obs, o, n) -> applyFilters());
        locationFilter.textProperty().addListener((obs, o, n) -> applyFilters());
    }

    private void applyFilters() {
        tableView.setItems(FXCollections.observableArrayList(dragonFilter.apply(dragonData)));
    }


    private HBox getFilterControl(String columnName, TextField textField) {
        Label label = new Label(columnName + ":");
        textField.setPromptText("Filter");

        HBox box = new HBox(5);
        box.getChildren().addAll(label, textField);

        return box;
    }

    private Comparator<Dragon> getComparatorForColumn(String columnName) {
        return switch (columnName) {
            case "Name" -> Comparator.comparing(Dragon::name);
            case "Age" -> Comparator.comparingLong(Dragon::age);
            case "Coordinates" -> Comparator.comparing(d -> d.coordinates() != null ?
                    "X=" + d.coordinates().x() + ", Y=" + d.coordinates().y() : "");
            case "Creation Date" -> Comparator.comparing(d -> d.creationDate().toString());
            case "Type" -> Comparator.comparing(Dragon::dragonType);
            case "Character" -> Comparator.comparing(Dragon::dragonCharacter);
            case "Killer" -> Comparator.comparing(d -> d.killer() != null ? d.killer().name() : "");
            case "Killer Location" -> Comparator.comparing(d -> {
                Person killer = d.killer();
                return killer != null && killer.location() != null ?
                        "X=" + killer.location().x() + ", Y=" + killer.location().y() + ", Z=" + killer.location().z() : "";
            });
            default -> Comparator.comparing(Dragon::id);
        };
    }

    private void createTable() {
        TableColumn<Dragon, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().name()));

        TableColumn<Dragon, Number> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleLongProperty(cellData.getValue().age()));

        TableColumn<Dragon, String> coordsCol = new TableColumn<>("Coordinates");
        coordsCol.setCellValueFactory(cellData -> {
            var coords = cellData.getValue().coordinates();
            return new javafx.beans.property.SimpleStringProperty(
                    coords == null ? "—" : "X=" + coords.x() + ", Y=" + coords.y()
            );
        });

        TableColumn<Dragon, String> dateCol = new TableColumn<>("Creation Date");
        dateCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().creationDate().toString()));

        TableColumn<Dragon, DragonType> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().dragonType()));

        TableColumn<Dragon, String> characterCol = new TableColumn<>("Character");
        characterCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().dragonCharacter().toString()));

        TableColumn<Dragon, String> killerCol = new TableColumn<>("Killer");
        killerCol.setCellValueFactory(cellData -> {
            Person killer = cellData.getValue().killer();
            return new javafx.beans.property.SimpleStringProperty(
                    killer == null ? "—" : killer.name() + ", height: " + killer.height()
            );
        });

        TableColumn<Dragon, String> locationCol = new TableColumn<>("Killer Location");
        locationCol.setCellValueFactory(cellData -> {
            Person killer = cellData.getValue().killer();
            if (killer == null || killer.location() == null) {
                return new javafx.beans.property.SimpleStringProperty("—");
            }
            Location loc = killer.location();
            return new javafx.beans.property.SimpleStringProperty(
                    "X=" + loc.x() + ", Y=" + loc.y() + ", Z=" + loc.z()
            );
        });

        tableView.getColumns().addAll(nameCol, ageCol, coordsCol, dateCol, typeCol, characterCol, killerCol, locationCol);
    }

    private void editAndSaveDragon(Dragon dragon) {
        UnfinishedDragon updatedDragon = DragonEditingDialog.showEditingDialog(dragon, (Stage) getScene().getWindow());

        if (updatedDragon != null) {
            try {
                ICommand command = client.getCommandManager().getCommandByName("update");
                ArrayDeque<Object> newDragonDeque = new ArrayDeque<>();
                newDragonDeque.push(updatedDragon);

                ((IMultiLineCommand) command).setAdditionalUserInput(newDragonDeque);

                Request req = new Request((IServerSideCommand) command, client.userauth.getUser(),
                        new String[]{String.valueOf(dragon.id())}, true, newDragonDeque);

                Response resp = client.communicateWithServer(req);

                if (resp.statusCode() == 200) {
                    showAlert("Success", "Dragon updated successfully.");
                    refreshData(client);
                } else {
                    showAlert("Error", "Failed to update dragon: " + resp.responseClaster().message());
                }
            } catch (CommandNotFound | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}