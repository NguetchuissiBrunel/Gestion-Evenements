package cm.polytech.poof2.Frontend.views;

import cm.polytech.poof2.Frontend.MainController;
import cm.polytech.poof2.classes.Evenement;
import cm.polytech.poof2.classes.Organisateur;
import cm.polytech.poof2.classes.Participant;

public class DetailViewFactory {
    public static DetailView createDetailView(Object entity, MainController controller) {
        if (entity instanceof Evenement) {
            return new EventDetailView((Evenement) entity, controller);
        } else if (entity instanceof Organisateur) {
            return new OrganisateurDetailView((Organisateur) entity, controller);
        } else if (entity instanceof Participant) {
            return new ParticipantDetailView((Participant) entity, controller);
        }
        throw new IllegalArgumentException("Unknown entity type: " + entity.getClass().getName());
    }
}