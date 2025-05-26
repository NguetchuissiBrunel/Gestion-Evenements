package cm.polytech.poof2.classes;

import cm.polytech.poof2.Observeur.EventObservable;
import cm.polytech.poof2.Observeur.ParticipantObserver;
import cm.polytech.poof2.Services.NotificationService;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Organisateur.class, name = "Organisateur"),
        @JsonSubTypes.Type(value = Participant.class, name="Participant")
})
public class Participant implements ParticipantObserver {

    private String id;
    private String nom;
    private String email;

    @JsonIgnoreProperties("participants")
    private List<Evenement> evenements = new ArrayList<>();

    private NotificationService notificationService;

    public Participant() {}

    public Participant(String id, String nom, String email) {
        this.id = id;
        this.nom = nom;
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setEvenements(List<Evenement> evenements) {
        this.evenements = evenements;
    }

    public void addEvenement(Evenement e) {
        if (e instanceof EventObservable) {
            ((EventObservable) e).registerObserver(this);
        }
        evenements.add(e);
    }


    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public List<Evenement> getEvenements() { return evenements; }

    @JsonIgnore
    public String getDetails() {
        return "Participant: " + nom + "\nEmail: " + email + "\nÉvénements: " +
                evenements.stream().map(Evenement::getNom).collect(Collectors.joining(", "));
    }

    @Override
    public void update(String message) {
        if (notificationService != null) {
            notificationService.envoyerNotification("Notification pour " + nom + ": " + message);
        }
    }
}