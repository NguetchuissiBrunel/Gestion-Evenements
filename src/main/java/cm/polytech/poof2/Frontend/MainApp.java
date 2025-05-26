package cm.polytech.poof2.Frontend;

import cm.polytech.poof2.Frontend.Service.AppService;
import cm.polytech.poof2.Services.ConsoleNot;
import cm.polytech.poof2.Services.NotificationService;
import cm.polytech.poof2.classes.Participant;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {
    public static TabPane tabPane;
    public static List<Participant> participants=new ArrayList<>();
    private static FlowPane eventPane;
    private static FlowPane participantPane;
    private static FlowPane organisateurPane;
    private static NotificationService notificationService = new ConsoleNot();
    private static MainController controller = new MainController(notificationService);
    private static AppService appService;

    @Override
    public void start(Stage primaryStage) {
        appService = new AppService();
        try {
            appService.initializeData();
        } catch (Exception e) {
            // Consider logging instead of printing
            e.printStackTrace();
        }

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(0));

        // En-tête
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #1ABC9C; -fx-padding: 20px; -fx-alignment: center;");
        Label title = new Label("Système de Gestion d'Événements");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        header.getChildren().add(title);
        root.setTop(header);

        // Contenu principal avec TabPane
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-tab-min-width: 120px; -fx-tab-max-width: 120px; -fx-tab-min-height: 40px;");

        // Onglet Événements
        Tab eventTab = new Tab("Événements");
        BorderPane eventBorderPane = new BorderPane();

        // Contenu des événements
        eventPane = new FlowPane(15, 15);
        eventPane.setPadding(new Insets(20));
        ScrollPane eventScroll = new ScrollPane(eventPane);
        eventScroll.setFitToWidth(true);
        eventScroll.setStyle("-fx-background-color: #F5F5F5;");

        // Barre d'outils pour les événements
        ToolBar eventToolbar = createToolbar("e");
        eventBorderPane.setTop(eventToolbar);
        eventBorderPane.setCenter(eventScroll);
        eventTab.setContent(eventBorderPane);

        // Onglet Participants
        Tab participantTab = new Tab("Participants");
        BorderPane participantBorderPane = new BorderPane();

        // Barre d'outils pour les participants
        ToolBar participantToolbar = createToolbar("p");
        participantBorderPane.setTop(participantToolbar);

        // Contenu des participants
        participantPane = new FlowPane(15, 15);
        participantPane.setPadding(new Insets(20));
        ScrollPane participantScroll = new ScrollPane(participantPane);
        participantScroll.setFitToWidth(true);
        participantScroll.setStyle("-fx-background-color: #F5F5F5;");

        participantBorderPane.setCenter(participantScroll);
        participantTab.setContent(participantBorderPane);

        // Onglet Organisateur
        Tab organisateurTab = new Tab("Organisateur");
        BorderPane organisateurBorderPane = new BorderPane();

        // Barre d'outils pour les organisateurs
        ToolBar organisateurToolbar = createToolbar("o");
        organisateurBorderPane.setTop(organisateurToolbar);

        // Contenu des organisateurs
        organisateurPane = new FlowPane(15, 15);
        organisateurPane.setPadding(new Insets(20));
        ScrollPane organisateurScroll = new ScrollPane(organisateurPane);
        organisateurScroll.setFitToWidth(true);
        organisateurScroll.setStyle("-fx-background-color: #F5F5F5;");

        organisateurBorderPane.setCenter(organisateurScroll);
        organisateurTab.setContent(organisateurBorderPane);

        tabPane.getTabs().addAll(eventTab, participantTab, organisateurTab);
        root.setCenter(tabPane);

        // Pied de page
        HBox footer = new HBox();
        footer.setStyle("-fx-background-color: #333333; -fx-padding: 10px; -fx-alignment: center-right;");
        Label footerLabel = new Label("© 2025 - Système de Gestion d'Événements");
        footerLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 12px;");
        footer.getChildren().add(footerLabel);
        root.setBottom(footer);

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/cm/polytech/poof2/style.css").toExternalForm());

        primaryStage.setTitle("Système de Gestion d'Événements");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initial refresh to populate UI
        refreshUI();
    }

    private ToolBar createToolbar(String type) {
        ToolBar toolbar = new ToolBar();
        toolbar.setStyle("-fx-background-color: white; -fx-padding: 10px; -fx-spacing: 10px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if (type.equals("e")) {
            Button addEventBtn = new Button("Ajouter un événement");
            addEventBtn.setStyle("-fx-background-color: #1ABC9C; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 15;");
            addEventBtn.setOnAction(e -> new EventForm(controller).show());
            SearchComponent searchComponent = new SearchComponent(eventPane, controller);
            toolbar.getItems().addAll(addEventBtn, spacer, searchComponent);
        } else if (type.equals("p")) {
            Button addParticipantBtn = new Button("Ajouter un participant");
            addParticipantBtn.setStyle("-fx-background-color: #1ABC9C; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 15;");
            addParticipantBtn.setOnAction(e -> new ParticipantForm(controller, appService.getParticipants(), null).show());
            toolbar.getItems().addAll(addParticipantBtn, spacer);
        } else if (type.equals("o")) {
            toolbar.getItems().add(spacer);
        }

        return toolbar;
    }

    public static void refreshUI() {
        appService.refreshUI(eventPane, participantPane, organisateurPane, controller);
    }

    public static void main(String[] args) {
        launch(args);
    }
}