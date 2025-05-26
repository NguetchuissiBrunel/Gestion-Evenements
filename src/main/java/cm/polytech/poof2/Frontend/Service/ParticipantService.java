package cm.polytech.poof2.Frontend.Service;

import cm.polytech.poof2.Sauvegarde.Json;
import cm.polytech.poof2.classes.Evenement;
import cm.polytech.poof2.classes.Participant;
import cm.polytech.poof2.Frontend.MainController;
import cm.polytech.poof2.Exception.CapaciteMaxAtteinteException;
import java.util.List;
import java.util.stream.Collectors;

public class ParticipantService {
    public Json persistance;

    public ParticipantService() {
        this.persistance = new Json();
    }

    public void saveParticipant(Participant participant, List<Evenement> selectedEvents, List<Participant> participants,
                                MainController controller, boolean isEditMode) throws Exception {
        // Validation des champs
        if (participant.getId().isEmpty() || participant.getNom().isEmpty() || participant.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Veuillez remplir tous les champs obligatoires.");
        }

        if (selectedEvents.isEmpty()) {
            throw new IllegalArgumentException("Veuillez sélectionner au moins un événement.");
        }

        // Vérification préalable de la capacité
        for (Evenement evt : selectedEvents) {
            if (evt.isCapaciteAtteinte()) {
                throw new IllegalArgumentException("L'événement '" + evt.getNom() + "' a atteint sa capacité maximale.");
            }
        }

        try {
            if (isEditMode) {
                // Mode édition
                Participant canonicalParticipant = participants.stream()
                        .filter(p -> p.getId().equals(participant.getId()))
                        .findFirst()
                        .orElse(participant);
                canonicalParticipant.setId(participant.getId());
                canonicalParticipant.setNom(participant.getNom());
                canonicalParticipant.setEmail(participant.getEmail());
                controller.updateParticipantEvents(canonicalParticipant, selectedEvents);
            } else {
                // Mode création
                participants.add(participant);
                for (Evenement evt : selectedEvents) {
                    controller.addParticipantToEvent(participant, evt);
                }
            }

            try {
                persistance.saveParticipants(participants);
            } catch (Exception ex) {
                throw new Exception("Erreur lors de la sauvegarde des participants: " + ex.getMessage());
            }
        } catch (CapaciteMaxAtteinteException ex) {
            throw new Exception("Impossible d'ajouter le participant à l'événement '" + ex.getMessage() + "': Capacité maximale atteinte.");
        }
    }
}