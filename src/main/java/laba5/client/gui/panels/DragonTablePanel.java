package laba5.client.gui.panels;

import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import laba5.client.Client;
import laba5.client.gui.DragonEditingDialog;
import laba5.client.gui.Refreshable;
import laba5.client.gui.logic.DragonFilter;
import laba5.client.l18n.Messages;
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
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class DragonTablePanel extends BorderPane implements Refreshable {
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
    private Locale locale;

    public DragonTablePanel(Client client, Locale locale) {
        this.client = client;
        this.locale = locale;
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
            Request request = new Request((IServerSideCommand) cmd, client.userauth.getUser(), new String[]{"200"}, false, null);
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
                    String colKey = clickedCol.getId(); // Получаем ID столбца как ключ локализации
                    if (colKey != null) {
                        showFilterForColumn(colKey); // Показываем фильтр для этого столбца
                    }
                }
            }
        });
    }

    private void createFilterRow() {
        filterBox = new HBox(5);
        filterBox.setSpacing(10);
        filterBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 5px;"); // Добавьте стиль для видимости
        this.setTop(filterBox);
    }

    private void showFilterForColumn(String columnName) {
        TextField textField;
        switch (columnName) {
            case "dragon.table.header.name" -> textField = nameFilter;
            case "dragon.table.header.age" -> textField = ageFilter;
            case "dragon.table.header.coordinates" -> textField = coordsFilter;
            case "dragon.table.header.creationDate" -> textField = dateFilter;
            case "dragon.table.header.type" -> textField = typeFilter;
            case "dragon.table.header.character" -> textField = characterFilter;
            case "dragon.table.header.killer" -> textField = killerFilter;
            case "dragon.table.header.killerLocation" -> textField = locationFilter;
            default -> {
                return;
            }
        }

        HBox control = filterControls.computeIfAbsent(columnName, k -> getFilterControl(getMessage(k), textField));
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
            case "dragon.table.header.name" -> Comparator.comparing(Dragon::name);
            case "dragon.table.header.age" -> Comparator.comparingLong(Dragon::age);
            case "dragon.table.header.coordinates" -> Comparator.comparing(d -> d.coordinates() != null ?
                    "X=" + d.coordinates().x() + ", Y=" + d.coordinates().y() : "");
            case "dragon.table.header.creationDate" -> Comparator.comparing(d -> d.creationDate().toString());
            case "dragon.table.header.type" -> Comparator.comparing(Dragon::dragonType);
            case "dragon.table.header.character" -> Comparator.comparing(Dragon::dragonCharacter);
            case "dragon.table.header.killer" -> Comparator.comparing(d -> d.killer() != null ? d.killer().name() : "");
            case "dragon.table.header.killerLocation" -> Comparator.comparing(d -> {
                Person killer = d.killer();
                return killer != null && killer.location() != null ?
                        "X=" + killer.location().x() + ", Y=" + killer.location().y() + ", Z=" + killer.location().z() : "";
            });
            default -> Comparator.comparing(Dragon::id);
        };
    }

    private void createTable() {
        TableColumn<Dragon, String> nameCol = new TableColumn<>(getMessage("dragon.table.header.name"));
        nameCol.setId("dragon.table.header.name");
        nameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().name()));

        TableColumn<Dragon, Number> ageCol = new TableColumn<>(getMessage("dragon.table.header.age"));
        ageCol.setId("dragon.table.header.age");
        ageCol.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().age()));
        ageCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(NumberFormat.getInstance(locale).format(item));
                }
            }
        });

        TableColumn<Dragon, String> coordsCol = new TableColumn<>(getMessage("dragon.table.header.coordinates"));
        coordsCol.setId("dragon.table.header.coordinates");
        coordsCol.setCellValueFactory(cellData -> {
            var coords = cellData.getValue().coordinates();
            return new SimpleStringProperty(
                    coords == null ? getMessage("dragon.table.emptyValue") :
                            "X=" + coords.x() + ", Y=" + coords.y()
            );
        });

        TableColumn<Dragon, String> dateCol = new TableColumn<>(getMessage("dragon.table.header.creationDate"));
        dateCol.setId("dragon.table.header.creationDate");
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(java.time.format.FormatStyle.LONG)
                .withLocale(locale);
        dateCol.setCellValueFactory(cellData -> {
            ZonedDateTime creationDate = cellData.getValue().creationDate();
            if (creationDate != null) {
                return new SimpleStringProperty(creationDate.format(formatter));
            } else {
                return new SimpleStringProperty(getMessage("dragon.table.emptyValue"));
            }
        });

        TableColumn<Dragon, DragonType> typeCol = new TableColumn<>(getMessage("dragon.table.header.type"));
        typeCol.setId("dragon.table.header.type");
        typeCol.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().dragonType()));
        typeCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(DragonType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(getMessage("dragon.type." + item.toString().toLowerCase()));
                }
            }
        });

        TableColumn<Dragon, String> characterCol = new TableColumn<>(getMessage("dragon.table.header.character"));
        characterCol.setId("dragon.table.header.character");
        characterCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        getMessage("dragon.character." + cellData.getValue().dragonCharacter().toString().toLowerCase())));

        TableColumn<Dragon, String> killerCol = new TableColumn<>(getMessage("dragon.table.header.killer"));
        killerCol.setId("dragon.table.header.killer");
        killerCol.setCellValueFactory(cellData -> {
            Person killer = cellData.getValue().killer();
            return new SimpleStringProperty(
                    killer == null ? getMessage("dragon.table.emptyValue") :
                            killer.name() + ", height: " + NumberFormat.getNumberInstance(locale).format(killer.height())
            );
        });

        TableColumn<Dragon, String> locationCol = new TableColumn<>(getMessage("dragon.table.header.killerLocation"));
        locationCol.setId("dragon.table.header.killerLocation");
        locationCol.setCellValueFactory(cellData -> {
            Person killer = cellData.getValue().killer();
            if (killer == null || killer.location() == null) {
                return new SimpleStringProperty(getMessage("dragon.table.emptyValue"));
            }
            Location loc = killer.location();
            return new SimpleStringProperty(
                    "X=" + NumberFormat.getNumberInstance(locale).format(loc.x()) +
                            ", Y=" + NumberFormat.getNumberInstance(locale).format(loc.y()) +
                            ", Z=" + NumberFormat.getNumberInstance(locale).format(loc.z())
            );
        });

        tableView.getColumns().addAll(nameCol, ageCol, coordsCol, dateCol, typeCol, characterCol, killerCol, locationCol);
    }

    private void editAndSaveDragon(Dragon dragon) {
        UnfinishedDragon updatedDragon = DragonEditingDialog.showEditingDialog(dragon, (Stage) getScene().getWindow(), locale);
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
                    showAlert("Success", getMessage("dragon.save.success"));
                    refreshData(client);
                } else {
                    showAlert("Error", getMessage("dragon.save.error") + resp.responseClaster().message());
                }
            } catch (CommandNotFound | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(getMessage("dragon.alert.title"));
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getMessage(String key) {
        return (String) Messages.getBundle().handleGetObject(key);
    }

    @Override
    public void refreshWithNewLocale(Locale newLocale) {
        Messages.setLocale(newLocale);
        this.locale = newLocale;
        getChildren().clear();
        tableView.getColumns().clear();
        createTable();
        setupFilters();
        this.setCenter(tableView);
    }
}