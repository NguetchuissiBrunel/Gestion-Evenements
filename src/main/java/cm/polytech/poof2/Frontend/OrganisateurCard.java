package cm.polytech.poof2.Frontend;


import cm.polytech.poof2.Frontend.views.DetailView;
import cm.polytech.poof2.Frontend.views.DetailViewFactory;
import cm.polytech.poof2.classes.Organisateur;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class OrganisateurCard extends VBox {

    public OrganisateurCard(Organisateur og, MainController controller) {
        setAlignment(Pos.CENTER);
        setPrefSize(200, 150);
        setPadding(new Insets(10));
        setSpacing(8);
        getStyleClass().add("card");

        // Style de base
        setStyle("-fx-background-color: white; -fx-border-color: #FF0000; " +
                "-fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        // Cercle avec l'initiale de l'organisateur
        Circle avatarCircle = new Circle(30);
        avatarCircle.setFill(Color.web("#FF0000"));

        // Initiale
        String initial = og.getNom().isEmpty() ? "?" : og.getNom().substring(0, 1).toUpperCase();
        Label initialLabel = new Label(initial);
        initialLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        initialLabel.setTranslateY(-128); // Pour positionner sur le cercle

        // Nom de l'organisateur
        Label nameLabel = new Label(og.getNom());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Email
        Label emailLabel = new Label(og.getEmail());
        emailLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");

        // Nombre d'événements organisés
        int eventCount = og.getEvenementsOrganises() != null ? og.getEvenementsOrganises().size() : 0;
        Label eventsLabel = new Label(eventCount + " événement" + (eventCount != 1 ? "s" : "") + " organisé" + (eventCount != 1 ? "s" : ""));
        eventsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        // Assemblage des éléments
        getChildren().addAll(avatarCircle, nameLabel, emailLabel, eventsLabel);

        // Le positionnement de l'initiale est spécial, on la met sur le cercle
        getChildren().add(initialLabel);

        // Événement de clic
        setOnMouseClicked(e -> DetailViewFactory.createDetailView(og, controller).show());
        // Animation au survol
        setOnMouseEntered(e -> {
            setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #FF0000; " +
                    "-fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 12, 0, 0, 7);");
        });

        setOnMouseExited(e -> {
            setStyle("-fx-background-color: white; -fx-border-color: #FF0000; " +
                    "-fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        });
    }
}