package cm.polytech.poof2.classes;

import cm.polytech.poof2.Exception.CapaciteMaxAtteinteException;
import cm.polytech.poof2.Observeur.EventObservable;
import cm.polytech.poof2.Observeur.ParticipantObserver;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Conference.class, name = "Conference"),
        @JsonSubTypes.Type(value = Concert.class, name = "Concert")
})
public abstract class Evenement implements EventObservable {
    protected String id;
    protected String nom;
    protected LocalDateTime date;
    protected String lieu;
    protected int capaciteMax;

    @JsonIgnoreProperties("evenements")
    protected List<Participant> participants = new ArrayList<>();


    @JsonIgnore
    public List<ParticipantObserver> getObservers() {
        return observers;
    }

    protected List<ParticipantObserver> observers = new ArrayList<>();

    //constructeur pour jackson
    public Evenement() {}

    public Evenement(String id, String nom, LocalDateTime date, String lieu, int capaciteMax) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.lieu = lieu;
        this.capaciteMax = capaciteMax;
    }

    public void ajouterParticipant(Participant p) throws CapaciteMaxAtteinteException {
        // verifier si le participant existe deja
        if (participants.stream().anyMatch(part -> part.getId().equals(p.getId()))) {
            return;
        }

        //verifier capacite
        if (participants.size() >= capaciteMax) {
            throw new CapaciteMaxAtteinteException("Capacité maximale atteinte pour " + nom);
        }

        participants.add(p);
        registerObserver(p);
        notifyObservers("Participant " + p.getNom() + " inscrit à " + nom);

        // ajouter l'evenement a la liste de celle du participant
        if (!p.getEvenements().stream().anyMatch(e -> e.getId().equals(this.getId()))) {
            p.addEvenement(this);
        }
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getLieu() {
        return lieu;
    }

    public void annuler() {
        notifyObservers("Événement " + nom + " annulé");
    }

    @Override
    public void registerObserver(ParticipantObserver observer) {
        observers.add(observer);
    }

    public void removeParticipant(String participantId) {
        boolean removed = participants.removeIf(p -> p.getId().equals(participantId));
        if (removed) {
            // Unregister the participant as an observer
            observers.removeIf(o -> o instanceof Participant && ((Participant) o).getId().equals(participantId));
        }
    }

    @Override
    public void removeObserver(ParticipantObserver observer) {
        observers.remove(observer);
    }


    @JsonIgnore
    public abstract String getDetails();

    @Override
    public void notifyObservers(String message) {
        for (ParticipantObserver observer : observers) {
            observer.update(message);
        }
    }

    public int getCapaciteMax(){
        return capaciteMax;
    }

    @JsonIgnore
    public boolean isCapaciteAtteinte() {
        return participants.size() >= capaciteMax;
    }
}