package cm.polytech.poof2.Frontend.views;

import cm.polytech.poof2.Frontend.MainController;
import cm.polytech.poof2.classes.Evenement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EventDetailView extends DetailView {
    private final Evenement event;
    private final MainController controller;

    public EventDetailView(Evenement event, MainController controller) {
        this.event = event;
        this.controller = controller;
    }

    @Override
    protected void buildUI() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f9f6ff; -fx-background-radius: 10;");

        Label titleLabel = new Label("Détails de l'événement");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #9C27B0;");

        TextArea detailsArea = new TextArea(event.getDetails());
        detailsArea.setEditable(false);
        detailsArea.setPrefRowCount(12);
        detailsArea.setWrapText(true);
        detailsArea.setStyle("-fx-font-size: 14px;");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        Button cancelBtn = new Button("Annuler Événement");
        cancelBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 20;");
        cancelBtn.setOnAction(e -> {
            controller.cancelEvent(event.getId());
            stage.close();
        });
        buttonBox.getChildren().add(cancelBtn);

        root.getChildren().addAll(titleLabel, detailsArea, buttonBox);

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.setTitle("Détails de l'Événement");
    }
}