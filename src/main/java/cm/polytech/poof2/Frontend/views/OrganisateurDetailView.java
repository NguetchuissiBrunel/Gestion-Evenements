package cm.polytech.poof2.Frontend.views;

import cm.polytech.poof2.Frontend.MainController;
import cm.polytech.poof2.classes.Evenement;
import cm.polytech.poof2.classes.Organisateur;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class OrganisateurDetailView extends DetailView {
    private final Organisateur organisateur;
    private final MainController controller;

    public OrganisateurDetailView(Organisateur organisateur, MainController controller) {
        this.organisateur = organisateur;
        this.controller = controller;
    }

    @Override
    protected void buildUI() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f9f6ff; -fx-background-radius: 10;");

        Label titleLabel = new Label("Détails de l'organisateur");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #9C27B0;");

        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(20);
        mainGrid.setVgap(15);
        mainGrid.setPadding(new Insets(15));
        mainGrid.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        VBox infoSection = new VBox(12);
        infoSection.setPadding(new Insets(10));
        infoSection.setStyle("-fx-border-color: #9C27B0; -fx-border-width: 1; -fx-border-radius: 5;");

        Label idLabel = new Label("ID: " + organisateur.getId());
        idLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        Label nomLabel = new Label("Nom: " + organisateur.getNom());
        nomLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        Label emailLabel = new Label("Email: " + organisateur.getEmail());
        emailLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        Label eventCountLabel = new Label("Événements organisés: " + (organisateur.getEvenementsOrganises() != null ? organisateur.getEvenementsOrganises().size() : 0));
        eventCountLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        infoSection.getChildren().addAll(idLabel, nomLabel, emailLabel, eventCountLabel);

        VBox eventsSection = new VBox(10);
        Label eventsTitle = new Label("Événements organisés");
        eventsTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #9C27B0;");

        ListView<Evenement> eventsList = new ListView<>();
        eventsList.getItems().addAll(organisateur.getEvenementsOrganises() != null ? organisateur.getEvenementsOrganises() : new ArrayList<>());
        eventsList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Evenement evt, boolean empty) {
                super.updateItem(evt, empty);
                if (empty || evt == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    Label nameLabel = new Label(evt.getNom() + " (" + evt.getId() + ")");
                    nameLabel.setStyle("-fx-font-size: 14px;");
                    Button viewBtn = new Button("Voir détails");
                    viewBtn.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white; -fx-font-size: 12px;");
                    viewBtn.setOnAction(e -> new EventDetailView(evt, controller).show());
                    hbox.getChildren().addAll(nameLabel, viewBtn);
                    setGraphic(hbox);
                }
            }
        });
        eventsList.setPrefHeight(200);

        eventsSection.getChildren().addAll(eventsTitle, eventsList);

        mainGrid.add(infoSection, 0, 0);
        mainGrid.add(eventsSection, 1, 0);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(40);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        mainGrid.getColumnConstraints().addAll(col1, col2);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        Button deleteBtn = new Button("Supprimer organisateur");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        deleteBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Supprimer cet organisateur ?");
            confirm.setContentText("Cela ne supprimera pas les événements organisés.");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    controller.removeOrganisateur(organisateur);
                    stage.close();
                }
            });
        });

        Button closeBtn = new Button("Fermer");
        closeBtn.setStyle("-fx-background-color: #666; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        closeBtn.setOnAction(e -> stage.close());

        buttonBox.getChildren().addAll(deleteBtn, closeBtn);

        root.getChildren().addAll(titleLabel, mainGrid, buttonBox);

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.setTitle("Détails de l'Organisateur");
    }
}