package cm.polytech.poof2.classes;



import java.util.ArrayList;
import java.util.List;


public class Organisateur extends Participant {


    private List<Evenement> evenementsOrganises=new ArrayList<>();

    public Organisateur() {
        super();
    }

    public Organisateur(String id, String nom, String email) {
        super(id, nom, email);
        this.evenementsOrganises = new ArrayList<>();
    }

    public List<Evenement> getEvenementsOrganises() { return evenementsOrganises; }

    public void ajouterEvenementOrganise(Evenement e) {
        evenementsOrganises.add(e);
    }
}