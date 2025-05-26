package cm.polytech.poof2.Frontend;

import cm.polytech.poof2.Exception.EvenementNonTrouveException;
import cm.polytech.poof2.Services.GestionEvenements;
import cm.polytech.poof2.classes.Conference;
import cm.polytech.poof2.classes.Evenement;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class SearchComponent extends HBox {
    private FlowPane eventPane;
    private MainController controller;
    private GestionEvenements gestionEvenements = GestionEvenements.getInstance();

    public SearchComponent(FlowPane eventPane, MainController controller) {
        if (eventPane == null) {
            throw new IllegalArgumentException("eventPane cannot be null");
        }
        this.eventPane = eventPane;
        this.controller = controller;
        setSpacing(10);
        setPadding(new Insets(10));

        // type de recherche
        ComboBox<String> searchTypeCombo = new ComboBox<>();
        searchTypeCombo.getItems().addAll("Tous", "Nom", "Lieu", "Type", "ID");
        searchTypeCombo.setValue("Tous");
        searchTypeCombo.setStyle("-fx-font-size: 14px;");

        // zone recherche
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher...");
        searchField.setPrefWidth(200);

        // logique de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSearchResults(searchTypeCombo.getValue(), newValue.trim());
        });

        searchTypeCombo.setOnAction(e -> {
            updateSearchResults(searchTypeCombo.getValue(), searchField.getText().trim());
        });

        getChildren().addAll(searchTypeCombo, searchField);
    }

    private void updateSearchResults(String searchType, String query) {
        if (eventPane == null) {
            showError("Erreur", "Le panneau des événements n'est pas initialisé.");
            return;
        }
        eventPane.getChildren().clear();

        if (searchType.equals("ID") && !query.isEmpty()) {
            try {
                boolean eventExists = gestionEvenements.rechercherEvenement(query);
                if (!eventExists) {
                    throw new EvenementNonTrouveException("Aucun événement trouvé avec l'ID: " + query);
                }
                // si l'evenement existe ,le reajouter
                Evenement event = gestionEvenements.getEvenements().get(query);
                eventPane.getChildren().add(new EventCard(event, controller));
            } catch (EvenementNonTrouveException e) {
                showError("Événement non trouvé", e.getMessage());
            }
        } else {
            // logiques de recherche
            for (Evenement event : gestionEvenements.getEvenements().values()) {
                boolean matches = false;

                if (query.isEmpty() || searchType.equals("Tous")) {
                    matches = true;
                } else {
                    switch (searchType) {
                        case "Nom":
                            matches = event.getNom().toLowerCase().contains(query.toLowerCase());
                            break;
                        case "Lieu":
                            matches = event.getLieu().toLowerCase().contains(query.toLowerCase());
                            break;
                        case "Type":
                            String eventType = event instanceof Conference ? "Conférence" : "Concert";
                            matches = eventType.toLowerCase().contains(query.toLowerCase());
                            break;
                    }
                }

                if (matches) {
                    eventPane.getChildren().add(new EventCard(event, controller));
                }
            }
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}