package cm.polytech.poof2.Frontend;

import cm.polytech.poof2.Frontend.views.DetailView;
import cm.polytech.poof2.Frontend.views.DetailViewFactory;
import cm.polytech.poof2.classes.Participant;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class ParticipantCard extends VBox {
    public ParticipantCard(Participant participant, MainController controller) {
        setAlignment(Pos.CENTER);
        setPrefSize(200, 150);
        setPadding(new Insets(10));
        setSpacing(8);
        getStyleClass().add("card");

        // Style de base
        setStyle("-fx-background-color: white; -fx-border-color: #00FF00; " +
                "-fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        // Cercle avec l'initiale du participant
        Circle avatarCircle = new Circle(30);
        avatarCircle.setFill(Color.web("#00FF00"));

        // Initiale
        String initial = participant.getNom().substring(0, 1).toUpperCase();
        Label initialLabel = new Label(initial);
        initialLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        initialLabel.setTranslateY(-128); // Pour positionner sur le cercle

        // Nom du participant
        Label nameLabel = new Label(participant.getNom());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Email
        Label emailLabel = new Label(participant.getEmail());
        emailLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");

        // Nombre d'événements inscrits
        int eventCount = participant.getEvenements().size();
        Label eventsLabel = new Label(eventCount + " événement" + (eventCount > 1 ? "s" : ""));
        eventsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        // Assemblage des éléments
        getChildren().addAll(avatarCircle, nameLabel, emailLabel, eventsLabel);

        // Le positionnement de l'initiale est spécial, on la met sur le cercle
        getChildren().add(initialLabel);

        // Événement de clic
        setOnMouseClicked(e -> DetailViewFactory.createDetailView(participant, controller).show());



        // Animation au survol
        setOnMouseEntered(e -> {
            setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #00FF00; " +
                    "-fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 12, 0, 0, 7);");
        });

        setOnMouseExited(e -> {
            setStyle("-fx-background-color: white; -fx-border-color: #00FF00; " +
                    "-fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        });

    }
}