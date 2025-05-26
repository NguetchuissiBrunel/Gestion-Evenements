package cm.polytech.poof2.Frontend;

import cm.polytech.poof2.Frontend.Service.EventService;
import cm.polytech.poof2.classes.Intervenant;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class EventForm {
    private Stage stage;
    private MainController controller;
    private List<Intervenant> intervenants = new ArrayList<>();
    private EventService eventService;

    public EventForm(MainController controller) {
        this.controller = controller;
        this.eventService = new EventService();
        stage = new Stage();

        // Configuration du layout principal
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Création de la grille pour le formulaire
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(15);
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        // Champs communs
        TextField nomorg = new TextField();
        TextField emailorg = new TextField();
        TextField idorg = new TextField();
        TextField idField = new TextField();
        TextField nomField = new TextField();
        TextField lieuField = new TextField();
        TextField capaciteField = new TextField();
        TextField dateField = new TextField();
        dateField.setPromptText("dd/MM/yyyy HH:mm");

        // Sélecteur de type d'événement
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Conférence", "Concert");
        typeCombo.setStyle("-fx-font-size: 14px;");

        // Champs spécifiques pour Conférence
        TextField themeField = new TextField();

        // Champs spécifiques pour Concert
        TextField artisteField = new TextField();
        TextField genreField = new TextField();

        // Conteneur pour les champs spécifiques
        VBox specificFieldsContainer = new VBox(10);

        // Configuration de base de la grille
        grid.add(new Label("ID Organisateur:"), 0, 0);
        grid.add(idorg, 1, 0);
        grid.add(new Label("Nom Organisateur:"), 0, 1);
        grid.add(nomorg, 1, 1);
        grid.add(new Label("Email Organisateur:"), 0, 2);
        grid.add(emailorg, 1, 2);
        grid.add(new Label("ID:"), 0, 3);
        grid.add(idField, 1, 3);
        grid.add(new Label("Nom:"), 0, 4);
        grid.add(nomField, 1, 4);
        grid.add(new Label("Lieu:"), 0, 5);
        grid.add(lieuField, 1, 5);
        grid.add(new Label("Capacité:"), 0, 6);
        grid.add(capaciteField, 1, 6);
        grid.add(new Label("Date (dd/MM/yyyy HH:mm):"), 0, 7);
        grid.add(dateField, 1, 7);
        grid.add(new Label("Type:"), 0, 8);
        grid.add(typeCombo, 1, 8);
        grid.add(specificFieldsContainer, 0, 9, 2, 1);

        // Conteneur pour les intervenants (pour les conférences)
        VBox intervenantsContainer = new VBox(10);

        // Titre pour la section des intervenants
        Label intervenantsTitle = new Label("Intervenants");
        intervenantsTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Liste des intervenants ajoutés
        ListView<Intervenant> intervenantsList = new ListView<>();
        intervenantsList.setPrefHeight(100);

        // Champ pour ajouter un intervenant
        HBox addIntervenantBox = new HBox(10);
        TextField nom = new TextField();
        nom.setPromptText("Nom de l'intervenant");
        TextField specialite = new TextField();
        specialite.setPromptText("Spécialité");

        Button addIntervenantBtn = new Button("Ajouter");
        addIntervenantBtn.setOnAction(e -> {
            String name = nom.getText();
            String spe = specialite.getText();
            Intervenant intervenant = new Intervenant(name, spe);
            if (!intervenant.getNom().isEmpty() && !intervenant.getSpecialite().isEmpty()) {
                intervenants.add(intervenant);
                intervenantsList.getItems().add(intervenant);
                nom.clear();
                specialite.clear();
            }
        });
        addIntervenantBox.getChildren().addAll(nom, specialite, addIntervenantBtn);

        // Bouton pour supprimer un intervenant
        Button removeIntervenantBtn = new Button("Supprimer sélectionné");
        removeIntervenantBtn.setOnAction(e -> {
            Intervenant selected = intervenantsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                intervenants.remove(selected);
                intervenantsList.getItems().remove(selected);
            }
        });

        // Assembler la section des intervenants
        intervenantsContainer.getChildren().addAll(intervenantsTitle, intervenantsList, addIntervenantBox, removeIntervenantBtn);

        // Mise à jour des champs spécifiques selon le type sélectionné
        typeCombo.setOnAction(e -> updateSpecificFields(typeCombo.getValue(), specificFieldsContainer,
                themeField, artisteField, genreField, intervenantsContainer));

        // Affichage initial des champs pour le type par défaut
        updateSpecificFields(typeCombo.getValue(), specificFieldsContainer,
                themeField, artisteField, genreField, intervenantsContainer);

        // Bouton de soumission
        Button submitBtn = new Button("Créer l'événement");
        submitBtn.setOnAction(e -> {
            try {
                eventService.createEvent(
                        idField.getText().trim(),
                        nomField.getText().trim(),
                        lieuField.getText().trim(),
                        capaciteField.getText().trim(),
                        dateField.getText().trim(),
                        typeCombo.getValue(),
                        themeField.getText().trim(),
                        artisteField.getText().trim(),
                        genreField.getText().trim(),
                        intervenants,
                        idorg.getText().trim(),
                        nomorg.getText().trim(),
                        emailorg.getText().trim(),
                        controller
                );
                MainApp.refreshUI();
                stage.close();
            } catch (Exception ex) {
                showError("Erreur", ex.getMessage());
            }
        });

        // Ajout du formulaire et du bouton au layout principal
        mainLayout.getChildren().addAll(grid, submitBtn);

        // Configuration de la scène
        Scene scene = new Scene(mainLayout, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Nouvel Événement");
    }

    private void updateSpecificFields(String type, VBox container, TextField themeField,
                                      TextField artisteField, TextField genreField, VBox intervenantsContainer) {
        container.getChildren().clear();

        if ("Conférence".equals(type)) {
            Label themeLabel = new Label("Thème:");
            VBox themeBox = new VBox(5);
            themeBox.getChildren().addAll(themeLabel, themeField);
            container.getChildren().addAll(themeBox, intervenantsContainer);
        } else if ("Concert".equals(type)) {
            Label artisteLabel = new Label("Artiste:");
            VBox artisteBox = new VBox(5);
            artisteBox.getChildren().addAll(artisteLabel, artisteField);

            Label genreLabel = new Label("Genre musical:");
            VBox genreBox = new VBox(5);
            genreBox.getChildren().addAll(genreLabel, genreField);
            container.getChildren().addAll(artisteBox, genreBox);
        }
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