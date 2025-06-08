package laba5.client.gui.panels;

import javafx.animation.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;
import javafx.geometry.Insets;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javafx.stage.Stage;
import laba5.client.Client;
import laba5.client.gui.DragonEditingDialog;
import laba5.client.gui.Refreshable;
import laba5.client.l18n.Messages;
import laba5.common.commands.IMultiLineCommand;
import laba5.common.dataExchanging.Request;
import laba5.common.dataExchanging.Response;
import laba5.common.commands.ICommand;
import laba5.common.commands.IServerSideCommand;
import laba5.common.dataExchanging.UnfinishedDragon;
import laba5.common.exceptions.CommandNotFound;
import laba5.common.genetics.DragonTypeResolver;
import laba5.common.genetics.Genome;
import laba5.common.genetics.PhenotypeResolver;
import laba5.common.model.Coordinates;
import laba5.common.model.Dragon;
import javafx.util.Duration;

public class DragonVisualizerPane extends Pane implements Refreshable {
    private final Canvas canvas = new Canvas(800, 600);
    private final Client client;
    private static final double MIN_SIZE = 50;
    private static final double MAX_SIZE = 100;
    private static final double SCALE_STEP = 1.1;
    private static final double MIN_SCALE = 0.3;
    private static final double MAX_SCALE = 3.0;
    private final Scale scaleTransform = new Scale(1.0, 1.0);
    private final Map<Long, Color> userColors = new HashMap<>();
    private long minAge = Long.MAX_VALUE;
    private long maxAge = Long.MIN_VALUE;
    private List<Dragon> currentDragons = new ArrayList<>();
    private final List<AnimatedDragon> animatedDragons = new ArrayList<>();
    private final List<Dragon> fullyAnimatedDragons = new ArrayList<>();


    private static final String BASE_IMAGE_PATH = "file:resources/dragonImages/";

    private PhenotypeResolver wingResolver;
    private PhenotypeResolver eyeResolver;
    private PhenotypeResolver hornResolver;
    private PhenotypeResolver patternResolver;

    private DragonTypeResolver typeResolver;

    private Button resetDataButton;

    private Locale locale;

    public DragonVisualizerPane(Client client, Locale locale) {
        this.client = client;

        this.locale = locale;

        this.wingResolver = new PhenotypeResolver(BASE_IMAGE_PATH + "wings/");
        this.eyeResolver = new PhenotypeResolver(BASE_IMAGE_PATH + "eyes/");
        this.hornResolver = new PhenotypeResolver(BASE_IMAGE_PATH + "horns/");
        this.patternResolver = new PhenotypeResolver(BASE_IMAGE_PATH + "pattern/");
        this.typeResolver = new DragonTypeResolver(BASE_IMAGE_PATH + "type/");

        canvas.getTransforms().add(scaleTransform);
        Pane canvasContainer = new Pane(canvas);
        canvasContainer.setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        ScrollPane scrollPane = new ScrollPane(canvasContainer);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        this.getChildren().add(scrollPane);
        canvas.widthProperty().addListener((obs, oldVal, newVal) -> redrawCanvas());
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> redrawCanvas());
        canvas.setOnScroll(this::handleScrollEvent);

        Button resetDataButton = new Button(getMessage("dragon.visualizer.button.reset"));
        resetDataButton.setOnAction(e -> resetCondition());

        HBox controlPanel = new HBox(10, resetDataButton);
        controlPanel.setPadding(new Insets(10));
        controlPanel.setStyle("-fx-background-color: #e0e0e0;");
        VBox layout = new VBox(controlPanel, scrollPane);
        this.getChildren().setAll(layout);

        setupCanvasClickHandler();
        updateFromServer();
    }

    public void updateFromServer() {
        try {
            ICommand cmd = client.getCommandManager().getCommandByName("show");
            Request request = new Request((IServerSideCommand) cmd, client.userauth.getUser(), new String[]{"200"}, false, null);
            Response resp = client.communicateWithServer(request);
            List<Dragon> dragonData = new ArrayList<>();
            dragonData.addAll(List.of(resp.responseClaster().objects()));
            updateCanvas(dragonData);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("alert.title.error", "dragon.visualizer.error.fetch");
        }
    }

    public void updateCanvas(List<Dragon> dragons) {
        this.currentDragons = dragons;
        minAge = Long.MAX_VALUE;
        maxAge = Long.MIN_VALUE;
        animatedDragons.clear();
        fullyAnimatedDragons.clear();

        for (Dragon dragon : dragons) {
            if (dragon == null || dragon.coordinates() == null) continue;
            minAge = Math.min(minAge, dragon.age());
            maxAge = Math.max(maxAge, dragon.age());
        }

        for (Dragon dragon : dragons) {
            if (dragon == null || dragon.coordinates() == null) continue;
            animateNewDragon(dragon);
        }
    }

    private void animateNewDragon(Dragon dragon) {
        if (dragon == null || dragon.coordinates() == null) return;

        SimpleDoubleProperty size = new SimpleDoubleProperty(1.0); // начальный размер
        double targetSize = calculateSize(dragon.age());

        KeyValue kv = new KeyValue(size, targetSize, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.millis(600), kv);

        Timeline timeline = new Timeline(kf);
        AnimatedDragon animatedDragon = new AnimatedDragon(dragon, size, timeline);
        animatedDragons.add(animatedDragon);

        timeline.setOnFinished(event -> {
            fullyAnimatedDragons.add(dragon);
            animatedDragons.remove(animatedDragon);
            redrawCanvas();
        });

        timeline.currentTimeProperty().addListener((obs, oldTime, newTime) -> redrawCanvas());
        timeline.play();
    }

    private void redrawCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#f5f5f5"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        // Отрисовка анимируемых драконов (кружки растут)
        for (AnimatedDragon animated : animatedDragons) {
            Dragon dragon = animated.dragon;
            Coordinates coords = dragon.coordinates();
            long userID = dragon.creatorId();
            double x = coords.x() + canvasWidth / 2 + canvas.getTranslateX();
            double y = coords.y() + canvasHeight / 2 + canvas.getTranslateY();
            double currentSize = animated.size.get();
            Color color = getUserColor(userID);

            gc.setFill(color);
            gc.fillOval(x - currentSize / 2, y - currentSize / 2, currentSize, currentSize);
        }

        for (Dragon dragon : fullyAnimatedDragons) {
            Coordinates coords = dragon.coordinates();
            long userID = dragon.creatorId();
            double x = coords.x() + canvasWidth / 2 + canvas.getTranslateX();
            double y = coords.y() + canvasHeight / 2 + canvas.getTranslateY();
            double size = calculateSize(dragon.age());
            Color color = getUserColor(userID);

            // Рисуем окружность
            gc.setFill(color);
            gc.fillOval(x - size / 2, y - size / 2, size, size);

            // Рисуем изображение дракона
            drawDragonImage(gc, dragon, x, y, size);

            // Рисуем имя
            gc.save();
            gc.setTransform(1, 0, 0, 1, 0, 0);
            gc.setFill(Color.BLACK);
            gc.setTextAlign(TextAlignment.CENTER);
            double textOffset = size / 2 + 10;
            gc.fillText(dragon.name(), x, y - textOffset);
            gc.restore();
        }
    }

    private void drawDragonImage(GraphicsContext gc, Dragon dragon, double x, double y, double size) {
        Genome genome = dragon.genome();

        Image wingImage = wingResolver.resolve(genome.getWingSizeGene());
        Image eyeImage = eyeResolver.resolve(genome.getEyeGene());
        Image hornImage = hornResolver.resolve(genome.getHornGene());
        Image patternImage = patternResolver.resolve(genome.getPatternGene());
        Image typeImage = typeResolver.resolve(dragon.dragonType());

        drawImage(gc, wingImage, x, y, size);
        drawImage(gc, eyeImage, x, y, size);
        drawImage(gc, hornImage, x, y, size);
        drawImage(gc, patternImage, x, y, size);
        drawImage(gc, typeImage, x, y, size);
    }

    private void drawImage(GraphicsContext gc, Image image, double x, double y, double size) {
        if (image != null && !image.isError()) {
            gc.drawImage(image, x - size / 2, y - size / 2, size, size);
        }
    }

    private double calculateSize(long age) {
        if (maxAge == minAge || currentDragons.isEmpty()) return MIN_SIZE;
        double normalized = (double) (age - minAge) / (maxAge - minAge);
        return MIN_SIZE + normalized * (MAX_SIZE - MIN_SIZE);
    }

    private Color getUserColor(long userID) {
        return userColors.computeIfAbsent(userID, id -> generateUniqueColor(id));
    }

    private Color generateUniqueColor(long userID) {
        int seed = Long.hashCode(userID);
        double hue = Math.abs(seed % 360);
        return Color.hsb(hue, 0.7, 0.9);
    }

    private void resetCondition() {
        currentDragons.clear();
        animatedDragons.forEach(a -> a.timeline.stop());
        animatedDragons.clear();
        fullyAnimatedDragons.clear();
        minAge = Long.MAX_VALUE;
        maxAge = Long.MIN_VALUE;
        canvas.setTranslateX(0);
        canvas.setTranslateY(0);
        scaleTransform.setX(1.0);
        scaleTransform.setY(1.0);
        scaleTransform.setPivotX(0);
        scaleTransform.setPivotY(0);
        updateFromServer();
    }

    private void handleScrollEvent(ScrollEvent event) {
        double zoomFactor = event.getDeltaY() > 0 ? SCALE_STEP : 1.0 / SCALE_STEP;
        double newScale = scaleTransform.getX() * zoomFactor;
        if (newScale < MIN_SCALE || newScale > MAX_SCALE) return;
        scaleTransform.setPivotX(event.getX());
        scaleTransform.setPivotY(event.getY());
        scaleTransform.setX(newScale);
        scaleTransform.setY(newScale);
        redrawCanvas();
    }

    private void setupCanvasClickHandler() {
        canvas.setOnMouseClicked(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            double canvasWidth = canvas.getWidth();
            double canvasHeight = canvas.getHeight();
            double translateX = canvas.getTranslateX();
            double translateY = canvas.getTranslateY();
            double unTranslatedX = mouseX - translateX;
            double unTranslatedY = mouseY - translateY;
            double logicalMouseX = unTranslatedX - canvasWidth / 2;
            double logicalMouseY = unTranslatedY - canvasHeight / 2;

            for (Dragon dragon : fullyAnimatedDragons) {
                if (dragon == null || dragon.coordinates() == null) continue;
                Coordinates coords = dragon.coordinates();
                long age = dragon.age();
                double dx = coords.x() - logicalMouseX;
                double dy = coords.y() - logicalMouseY;
                double size = calculateSize(age);
                double radius = size / 2;
                if (dx * dx + dy * dy <= radius * radius) {
                    UnfinishedDragon updatedDragon = DragonEditingDialog.showEditingDialog(dragon, (Stage) getScene().getWindow(), locale);
                    if (updatedDragon != null) {
                        try {
                            ICommand command = client.getCommandManager().getCommandByName("update");
                            ArrayDeque<Object> newDragonDeque = new ArrayDeque<>();
                            newDragonDeque.push(updatedDragon);
                            ((IMultiLineCommand) command).setAdditionalUserInput(newDragonDeque);
                            Request req = new Request((IServerSideCommand) command, client.userauth.getUser(), new String[]{String.valueOf(dragon.id())}, true, newDragonDeque);
                            Response resp = client.communicateWithServer(req);
                            if (resp.statusCode() == 200) {
                                showAlert("Success", "Dragon updated successfully.");
                                updateFromServer();
                            } else {
                                showAlert("alert.title.error", "dragon.visualizer.error.parsing");
                            }
                        } catch (CommandNotFound | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static class AnimatedDragon {
        Dragon dragon;
        Timeline timeline;
        SimpleDoubleProperty size;

        AnimatedDragon(Dragon dragon, SimpleDoubleProperty size, Timeline timeline) {
            this.dragon = dragon;
            this.size = size;
            this.timeline = timeline;
        }
    }
    private String getMessage(String key) {
        return (String) Messages.getBundle().handleGetObject(key);
    }

    @Override
    public void refreshWithNewLocale(Locale newLocale) {
        Messages.setLocale(newLocale);
        if (resetDataButton != null) {
            resetDataButton.setText(getMessage("dragon.visualizer.button.reset"));
        }

    }
}