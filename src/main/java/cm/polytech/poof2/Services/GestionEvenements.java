package cm.polytech.poof2.Services;

import cm.polytech.poof2.Exception.CapaciteMaxAtteinteException;
import cm.polytech.poof2.Exception.EvenementDejaExistantException;
import cm.polytech.poof2.Exception.EvenementNonTrouveException;
import cm.polytech.poof2.Observeur.EventObservable;
import cm.polytech.poof2.Sauvegarde.Json;
import cm.polytech.poof2.classes.Evenement;
import cm.polytech.poof2.classes.Participant;
import cm.polytech.poof2.Sauvegarde.Persistance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class GestionEvenements {

    private static  GestionEvenements gest;
    private Map<String, Evenement> evenements;
    private Map<String, Participant> og;
    private List<Participant> participants;
    private final Persistance persistance;

    private GestionEvenements() {
        this.persistance = new Json();
        this.evenements = new HashMap<>();
        this.og = new HashMap<>();
        this.participants = new ArrayList<>();
        try {
            this.evenements = persistance.loadEvenements();
            this.og = persistance.loadOrganisateurs();
            this.participants = persistance.loadParticipants();
            validateLoadedData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GestionEvenements getInstance() {
        if (gest == null) {
            synchronized (GestionEvenements.class) {
                if (gest == null) {
                    gest = new GestionEvenements();
                }
            }
        }
        return gest;
    }


    private void validateLoadedData() {

        // Check for inconsistencies in participants' evenements
        for (Participant p : participants) {
            List<Evenement> invalidEvents = new ArrayList<>();
            for (Evenement e : p.getEvenements()) {
                if (!evenements.containsKey(e.getId())) {
                    invalidEvents.add(e);
                }
            }
            // Remove invalid events
            invalidEvents.forEach(e -> p.getEvenements().removeIf(evt -> evt.getId().equals(e.getId())));
        }

        // Check for inconsistencies in events' participants
        for (Evenement e : evenements.values()) {
            List<Participant> invalidParticipants = new ArrayList<>();
            for (Participant p : e.getParticipants()) {
                if (!participants.stream().anyMatch(part -> part.getId().equals(p.getId()))) {
                    invalidParticipants.add(p);
                }
            }
            // Remove invalid participants
            invalidParticipants.forEach(p -> e.getParticipants().removeIf(part -> part.getId().equals(p.getId())));
        }
        saveData();
    }


    public Map<String, Participant> getOg() {
        return og;
    }


    public Map<String, Evenement> getEvenements() {
        return evenements;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void ajouterOrganisateur(Participant org) {
        if (og.containsKey(org.getId())) {
            throw new IllegalArgumentException("Un organisateur avec l'ID " + org.getId() + " existe déjà.");
        }
        og.put(org.getId(), org);
        saveData();
    }

    public void supprimerOrganisateur(String id) {
        if (!og.containsKey(id)) {
            throw new IllegalArgumentException("L'organisateur avec l'ID " + id + " n'existe pas.");
        }
        og.remove(id);
        saveData();
    }

    public void ajouterEvenement(Evenement e) throws EvenementDejaExistantException {
        if (evenements.containsKey(e.getId())) {
            throw new EvenementDejaExistantException("Événement avec ID " + e.getId() + " existe déjà");
        }
        evenements.put(e.getId(), e);
        saveData();
    }

    public void supprimerEvenement(String id) {
        Evenement e = evenements.get(id);
        if (e != null) {
            // Remove the event from all participants' evenements lists
            new ArrayList<>(e.getParticipants()).forEach(p -> {
                Participant canonicalParticipant = participants.stream()
                        .filter(part -> part.getId().equals(p.getId()))
                        .findFirst()
                        .orElse(p);
                canonicalParticipant.getEvenements().removeIf(evt -> evt.getId().equals(e.getId()));
                // Unregister the participant as an observer
                if (e instanceof EventObservable) {
                    ((EventObservable) e).removeObserver(canonicalParticipant);
                }
            });
            e.annuler();
            evenements.remove(id);
            saveData();
        }
    }

    public boolean rechercherEvenement(String id) throws EvenementNonTrouveException {
        if (!evenements.containsKey(id)) {
            throw new EvenementNonTrouveException("Événement avec ID " + id + " non trouve");
        }
        return evenements.containsKey(id);
    }
    public void addParticipantToEvent(Participant p, Evenement e) throws Exception {
        Participant canonicalParticipant = participants.stream()
                .filter(part -> part.getId().equals(p.getId()))
                .findFirst()
                .orElse(p);
        if (e.getParticipants().stream().anyMatch(part -> part.getId().equals(canonicalParticipant.getId()))) {
            System.out.println("Participant " + canonicalParticipant.getNom() + " is already registered for event " + e.getNom());
            return;
        }
        e.ajouterParticipant(canonicalParticipant);
        if (!participants.stream().anyMatch(part -> part.getId().equals(canonicalParticipant.getId()))) {
            participants.add(canonicalParticipant);
        }
        saveData();
    }


    public void updateParticipantEvents(Participant participant, List<Evenement> newEvents) throws Exception {
        // Find the Participant instance
        Participant clParticipant = participants.stream()
                .filter(p -> p.getId().equals(participant.getId()))
                .findFirst()
                .orElse(participant);

        // Check if any existing events are missing from newEvents (prevent unchecking)
        List<Evenement> currentEvents = new ArrayList<>(clParticipant.getEvenements());
        for (Evenement currentEvent : currentEvents) {
            if (!newEvents.stream().anyMatch(ne -> ne.getId().equals(currentEvent.getId()))) {
                throw new IllegalArgumentException("Impossible de désinscrire le participant de l'événement " + currentEvent.getNom());
            }
        }

        // Identify new events to add (exclude events the participant is already registered for)
        List<Evenement> eventsToAdd = newEvents.stream()
                .filter(e -> !currentEvents.stream().anyMatch(ce -> ce.getId().equals(e.getId())))
                .collect(Collectors.toList());

        // Check capacity for new events only
        List<String> fullEvents = new ArrayList<>();
        for (Evenement e : eventsToAdd) {
            if (e.getParticipants().size() >= e.getCapaciteMax()) {
                fullEvents.add(e.getNom());
            }
        }
        if (!fullEvents.isEmpty()) {
            throw new CapaciteMaxAtteinteException("Les événements suivants ont atteint leur capacité maximale : " + String.join(", ", fullEvents));
        }

        // Add participant to new events only
        for (Evenement e : eventsToAdd) {
            e.ajouterParticipant(clParticipant);
        }

        // Ensure the participant is in the global participants list
        if (!participants.stream().anyMatch(p -> p.getId().equals(clParticipant.getId()))) {
            participants.add(clParticipant);
        }

        saveData();
    }

    public void saveData() {
        try {
            persistance.saveEvenements(evenements);
            persistance.saveOrganisateurs(og);
            persistance.saveParticipants(participants);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}