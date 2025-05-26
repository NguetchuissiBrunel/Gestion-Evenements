package cm.polytech.poof2.Frontend;

import cm.polytech.poof2.classes.Evenement;
import cm.polytech.poof2.classes.Organisateur;
import cm.polytech.poof2.classes.Participant;
import cm.polytech.poof2.Exception.CapaciteMaxAtteinteException;
import cm.polytech.poof2.Services.GestionEvenements;
import cm.polytech.poof2.Services.NotificationService;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    private final GestionEvenements gestion = GestionEvenements.getInstance();
    private final NotificationService notificationService;

    public MainController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void addParticipantToEvent(Participant p, Evenement e) {
        try {
            gestion.addParticipantToEvent(p, e);
            notificationService.envoyerNotification("Participant " + p.getNom() + " ajouté à l'événement " + e.getNom());
            MainApp.refreshUI();
        } catch (CapaciteMaxAtteinteException ex) {
            showError("Capacité dépassée", ex.getMessage());
        } catch (Exception ex) {
            showError("Erreur inattendue", "Une erreur est survenue : " + ex.getMessage());
        }
    }

    public void removeParticipantFromEvent(Participant p, Evenement e) {
        if (p.getId() == null || e.getId() == null) {
            showError("Erreur", "Identifiant invalide pour le participant ou l'événement.");
            return;
        }

        // trouver l'instance du participant
        Participant canonicalParticipant = gestion.getParticipants().stream()
                .filter(part -> part.getId() != null && part.getId().equals(p.getId()))
                .findFirst()
                .orElse(p);

        // trouver l'instance de l'evenement
        Evenement canonicalEvent = gestion.getEvenements().get(e.getId());
        if (canonicalEvent == null) {
            showError("Erreur", "Événement non trouvé.");
            return;
        }

        // retirer l'evenement de la liste des evenement du participant
        canonicalParticipant.getEvenements().removeIf(evt -> evt.getId() != null && evt.getId().equals(canonicalEvent.getId()));


        // retirer le participant de la liste de participant de l'evenement
        canonicalEvent.removeParticipant(canonicalParticipant.getId());

        gestion.saveData(); // sauvegarder fichier
        notificationService.envoyerNotification("Participant " + canonicalParticipant.getNom() + " retiré de l'événement " + canonicalEvent.getNom());
        MainApp.refreshUI();
    }

    public void removeParticipant(Participant participant) {
        if (participant.getId() == null) {
            showError("Erreur", "Identifiant du participant invalide.");
            return;
        }


        Participant canonicalParticipant = gestion.getParticipants().stream()
                .filter(p -> p.getId() != null && p.getId().equals(participant.getId()))
                .findFirst()
                .orElse(participant);


        new ArrayList<>(canonicalParticipant.getEvenements()).forEach(evt -> {
            Evenement canonicalEvent = gestion.getEvenements().get(evt.getId());
            if (canonicalEvent != null) {
                removeParticipantFromEvent(canonicalParticipant, canonicalEvent);
            }
        });

        // effacer le participant du system
         gestion.getParticipants().removeIf(p -> p.getId() != null && p.getId().equals(canonicalParticipant.getId()));

        gestion.saveData();
        notificationService.envoyerNotification("Participant " + canonicalParticipant.getNom() + " supprimé du système");
        MainApp.refreshUI();
    }

    public void removeOrganisateur(Organisateur organisateur) {
        try {
            gestion.supprimerOrganisateur(organisateur.getId());
            notificationService.envoyerNotification("Organisateur " + organisateur.getNom() + " supprimé");
            MainApp.refreshUI();
        } catch (IllegalArgumentException ex) {
            showError("Erreur", ex.getMessage());
        }
    }

    public void updateParticipantEvents(Participant participant, List<Evenement> newEvents) throws Exception {
        gestion.updateParticipantEvents(participant, newEvents);
        notificationService.envoyerNotification("Événements mis à jour pour le participant " + participant.getNom());
        MainApp.refreshUI();
    }

    public void addEvent(Evenement e) {
        try {
            gestion.ajouterEvenement(e);
            notificationService.envoyerNotification("Nouvel événement créé : " + e.getNom());
            MainApp.refreshUI();
        } catch (Exception ex) {
            showError("Erreur", ex.getMessage());
        }
    }

    public void addOrg(Participant e) {
        try {
            gestion.ajouterOrganisateur(e);
            notificationService.envoyerNotification("Nouvel organisateur ajouté : " + e.getNom());
            MainApp.refreshUI();
        } catch (Exception ex) {
            showError("Erreur", ex.getMessage());
        }
    }

    public void cancelEvent(String eventId) {
        gestion.supprimerEvenement(eventId);
        MainApp.refreshUI();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}