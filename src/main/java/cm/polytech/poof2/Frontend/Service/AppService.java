package cm.polytech.poof2.Frontend.Service;


import cm.polytech.poof2.Frontend.EventCard;
import cm.polytech.poof2.Frontend.MainController;
import cm.polytech.poof2.Frontend.OrganisateurCard;
import cm.polytech.poof2.Frontend.ParticipantCard;
import cm.polytech.poof2.Services.GestionEvenements;
import cm.polytech.poof2.classes.Evenement;
import cm.polytech.poof2.classes.Organisateur;
import cm.polytech.poof2.classes.Participant;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;

public class AppService {
    private GestionEvenements gestionEvenements;
    private List<Participant> participants;

    public AppService() {
        this.gestionEvenements = GestionEvenements.getInstance();
        this.participants = new ArrayList<>();
    }

    public void initializeData() throws Exception {
        // initialiser les evenements
        this.participants = gestionEvenements.getParticipants();
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void refreshUI(FlowPane eventPane, FlowPane participantPane, FlowPane organisateurPane, MainController controller) {
        eventPane.getChildren().clear();
        participantPane.getChildren().clear();
        organisateurPane.getChildren().clear();

        for (Evenement e : gestionEvenements.getEvenements().values()) {
            eventPane.getChildren().add(new EventCard(e, controller));
        }

        for (Participant p : participants) {
            participantPane.getChildren().add(new ParticipantCard(p, controller));
        }

        for (Participant o : gestionEvenements.getOg().values()) {
            organisateurPane.getChildren().add(new OrganisateurCard((Organisateur) o, controller));
        }
    }
}