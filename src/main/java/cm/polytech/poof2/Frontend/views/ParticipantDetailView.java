package cm.polytech.poof2.Frontend.views;

import cm.polytech.poof2.Frontend.MainApp;
import cm.polytech.poof2.Frontend.MainController;
import cm.polytech.poof2.Frontend.ParticipantForm;
import cm.polytech.poof2.classes.Evenement;
import cm.polytech.poof2.classes.Participant;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ParticipantDetailView extends DetailView {
    private final Participant participant;
    private final MainController controller;

    public ParticipantDetailView(Participant participant, MainController controller) {
        this.participant = participant;
        this.controller = controller;
    }

    @Override
    protected void buildUI() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f9f6ff; -fx-background-radius: 10;");

        Label titleLabel = new Label("Détails du participant");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #9C27B0;");

        TextArea detailsArea = new TextArea(participant.getDetails());
        detailsArea.setEditable(false);
        detailsArea.setPrefRowCount(12);
        detailsArea.setWrapText(true);
        detailsArea.setStyle("-fx-font-size: 14px;");

        VBox eventsSection = new VBox(10);
        Label eventsTitle = new Label("Événements inscrits");
        eventsTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        ListView<Evenement> eventsList = new ListView<>();
        eventsList.getItems().addAll(participant.getEvenements());
        eventsList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Evenement evt, boolean empty) {
                super.updateItem(evt, empty);
                if (empty || evt == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    Label nameLabel = new Label(evt.getNom());
                    Button unregisterBtn = new Button("Se désinscrire");
                    unregisterBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                    unregisterBtn.setOnAction(e -> {
                        controller.removeParticipantFromEvent(participant, evt);
                        eventsList.getItems().remove(evt);
                    });
                    hbox.getChildren().addAll(nameLabel, unregisterBtn);
                    setGraphic(hbox);
                }
            }
        });

        Button editBtn = new Button("Modifier inscriptions");
        editBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        Button deleteBtn = new Button("Supprimer participant");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");

        editBtn.setOnAction(e -> {
            new ParticipantForm(controller, MainApp.participants, participant).show();
            stage.close();
        });

        deleteBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Supprimer ce participant ?");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    controller.removeParticipant(participant);
                    stage.close();
                }
            });
        });

        Button closeBtn = new Button("Fermer");
        closeBtn.setStyle("-fx-font-size: 14px; -fx-padding: 8 20;");
        closeBtn.setOnAction(e -> stage.close());

        HBox buttonHBox = new HBox(10, editBtn, deleteBtn, closeBtn);
        eventsSection.getChildren().addAll(eventsTitle, eventsList, buttonHBox);
        root.getChildren().addAll(titleLabel, detailsArea, eventsSection);

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.setTitle("Détails du Participant");
    }
}