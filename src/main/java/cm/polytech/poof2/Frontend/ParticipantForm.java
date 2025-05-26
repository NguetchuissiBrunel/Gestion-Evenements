package cm.polytech.poof2.Frontend;

import cm.polytech.poof2.Services.GestionEvenements;
import cm.polytech.poof2.Frontend.Service.ParticipantService;
import cm.polytech.poof2.classes.Evenement;
import cm.polytech.poof2.classes.Participant;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import java.util.stream.Collectors;

public class ParticipantForm {
    private Stage stage;
    private MainController controller;
    private List<Participant> participants;
    private Participant participantToEdit;
    private ParticipantService participantService;

    public ParticipantForm(MainController controller, List<Participant> participants, Participant participantToEdit) {
        this.participantToEdit = participantToEdit;
        this.controller = controller;
        this.participants = participants;
        this.participantService = new ParticipantService();
        stage = new Stage();

        // Configuration du layout principal
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Titre du formulaire
        Label titleLabel = new Label(participantToEdit == null ? "Inscription d'un nouveau participant" : "Modification participant");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Création de la grille pour le formulaire
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(15);
        grid.setPadding(new Insets(15));
        grid.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        // Champs du formulaire
        TextField idField = new TextField();
        idField.setPromptText("ID du participant");

        TextField nomField = new TextField();
        nomField.setPromptText("Nom complet");

        TextField emailField = new TextField();
        emailField.setPromptText("Adresse email");

        // Liste des événements disponibles avec cases à cocher
        VBox eventsContainer = new VBox(5);
        Label eventsLabel = new Label("Événements disponibles:");
        eventsLabel.setStyle("-fx-font-weight: bold;");

        for (Evenement e : GestionEvenements.getInstance().getEvenements().values()) {
            CheckBox checkBox = new CheckBox(e.getNom() + " (" + e.getId() + ")");
            checkBox.setUserData(e);
            eventsContainer.getChildren().add(checkBox);
        }

        ScrollPane eventsScroll = new ScrollPane(eventsContainer);
        eventsScroll.setFitToWidth(true);
        eventsScroll.setPrefHeight(150);

        // Labels explicatifs
        Label idLabel = new Label("ID:");
        idLabel.setStyle("-fx-font-weight: bold;");

        Label nomLabel = new Label("Nom:");
        nomLabel.setStyle("-fx-font-weight: bold;");

        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-weight: bold;");

        // Positionnement dans la grille
        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(nomLabel, 0, 1);
        grid.add(nomField, 1, 1);
        grid.add(emailLabel, 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(eventsLabel, 0, 3);
        grid.add(eventsScroll, 1, 3);

        // Boutons
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button submitBtn = new Button(participantToEdit == null ? "Inscrire" : "Modifier");
        submitBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");

        Button cancelBtn = new Button("Annuler");
        cancelBtn.setOnAction(e -> stage.close());

        buttonBox.getChildren().addAll(submitBtn, cancelBtn);

        //editer un participant
        if (participantToEdit != null) {
            idField.setText(participantToEdit.getId());
            nomField.setText(participantToEdit.getNom());
            emailField.setText(participantToEdit.getEmail());
            eventsContainer.getChildren().stream()
                    .filter(node -> node instanceof CheckBox)
                    .map(node -> (CheckBox) node)
                    .forEach(cb -> {
                        Evenement evt = (Evenement) cb.getUserData();
                        cb.setSelected(participantToEdit.getEvenements().stream().anyMatch(e -> e.getId().equals(evt.getId())));
                    });
        }

        // Action du bouton d'inscription
        submitBtn.setOnAction(e -> {
            submitBtn.setDisable(true);
            try {
                // Récupération des événements sélectionnés
                List<Evenement> selectedEvents = eventsContainer.getChildren().stream()
                        .filter(node -> node instanceof CheckBox)
                        .map(node -> (CheckBox) node)
                        .filter(CheckBox::isSelected)
                        .map(cb -> (Evenement) cb.getUserData())
                        .collect(Collectors.toList());

                Participant participant = new Participant(
                        idField.getText().trim(),
                        nomField.getText().trim(),
                        emailField.getText().trim()
                );

                participantService.saveParticipant(participant, selectedEvents, participants, controller, participantToEdit != null);
                MainApp.refreshUI(); // Rafraîchir l'interface après une inscription/modification réussie
                stage.close();
            } catch (Exception ex) {
                showError("Erreur", ex.getMessage());
            } finally {
                submitBtn.setDisable(false);
            }
        });

        // Assemblage du layout
        mainLayout.getChildren().addAll(titleLabel, grid, buttonBox);

        // Configuration de la scène
        Scene scene = new Scene(mainLayout, 450, 450);
        stage.setScene(scene);
        stage.setTitle(participantToEdit == null ? "Nouveau Participant" : "Modifier Participant");
    }

    public void show() {
        stage.show();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}