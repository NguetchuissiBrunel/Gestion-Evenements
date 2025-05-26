package cm.polytech.poof2.Frontend;
import cm.polytech.poof2.Frontend.views.DetailView;
import cm.polytech.poof2.Frontend.views.DetailViewFactory;
import cm.polytech.poof2.classes.Concert;
import cm.polytech.poof2.classes.Conference;
import cm.polytech.poof2.classes.Evenement;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class EventCard extends VBox {
    public EventCard(Evenement evenement, MainController controller) {
        setAlignment(Pos.CENTER);
        setPrefSize(200, 150);
        setPadding(new Insets(10));
        setSpacing(5);
        getStyleClass().add("card");

        // Différencier les types d'événements par couleur
        String typeColor = evenement instanceof Conference ? "#4B0086" : "#FFA500";
        setStyle("-fx-background-color: white; -fx-border-color: " + typeColor +
                "; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        // Icône ou indicateur de type
        Rectangle typeIndicator = new Rectangle(180, 10);
        typeIndicator.setFill(Color.web(typeColor));
        typeIndicator.setArcWidth(5);
        typeIndicator.setArcHeight(5);

        // Type d'événement
        Label typeLabel = new Label(evenement instanceof Conference ? "CONFÉRENCE" : "CONCERT");
        typeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + typeColor + "; -fx-font-weight: bold;");

        // Nom de l'événement
        Label nameLabel = new Label(evenement.getNom());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 5 0;");

        // Lieu
        Label lieuLabel = new Label(evenement.getLieu());
        lieuLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        // Informations spécifiques selon le type
        Label specificLabel;
        if (evenement instanceof Conference) {
            Conference conf = (Conference) evenement;
            specificLabel = new Label("Thème: " + conf.getTheme());

            // Nombre d'intervenants
            Label intervenantsLabel = new Label(conf.getIntervenants().size() + " intervenant(s)");
            intervenantsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
            getChildren().addAll(typeIndicator, typeLabel, nameLabel, lieuLabel, specificLabel, intervenantsLabel);
        } else {
            Concert concert = (Concert) evenement;
            specificLabel = new Label("Artiste: " + concert.getArtiste());

            // Genre musical
            Label genreLabel = new Label("Genre: " + concert.getGenre());
            genreLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
            getChildren().addAll(typeIndicator, typeLabel, nameLabel, lieuLabel, specificLabel, genreLabel);
        }
        specificLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");

        // Capacité
        Label capaciteLabel = new Label(evenement.getParticipants().size() + "/" + evenement.getCapaciteMax() + " participants");
        capaciteLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888;");
        getChildren().add(capaciteLabel);

        setOnMouseClicked(e -> DetailViewFactory.createDetailView(evenement, controller).show());

        // Animation au survol
        setOnMouseEntered(e -> {
            setStyle("-fx-background-color: #f9f9f9; -fx-border-color: " + typeColor +
                    "; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 12, 0, 0, 7);");
        });

        setOnMouseExited(e -> {
            setStyle("-fx-background-color: white; -fx-border-color: " + typeColor +
                    "; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        });
    }
}