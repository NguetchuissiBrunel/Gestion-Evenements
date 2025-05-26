package cm.polytech.poof2.classes;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Conference extends Evenement {
    private String theme;
    private List<Intervenant> intervenants;

    public Conference() {
        super();
        this.intervenants = new ArrayList<>();
    }

    public Conference(String id, String nom, LocalDateTime date, String lieu, int capaciteMax, String theme) {
        super(id, nom, date, lieu, capaciteMax);
        this.theme = theme;
        this.intervenants = new ArrayList<>();
    }

    public List<Intervenant> getIntervenants() {
        return intervenants;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void addIntervenant(Intervenant intervenant) {
        if (!intervenants.contains(intervenant)) {
            intervenants.add(intervenant);
        }
    }

    public void removeIntervenant(Intervenant intervenant) {
        intervenants.remove(intervenant);
    }

    @Override
    public String getDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder details = new StringBuilder();
        details.append("CONFÉRENCE\n\n");
        details.append("ID: ").append(getId()).append("\n");
        details.append("Nom: ").append(getNom()).append("\n");
        details.append("Date: ").append(getDate().format(formatter)).append("\n");
        details.append("Lieu: ").append(getLieu()).append("\n");
        details.append("Capacité: ").append(getParticipants().size()).append("/").append(getCapaciteMax()).append("\n");
        details.append("Thème: ").append(theme).append("\n\n");

        details.append("Intervenants:\n");
        if (intervenants.isEmpty()) {
            details.append("- Aucun intervenant\n");
        } else {
            for (Intervenant intervenant : intervenants) {
                details.append("- ").append(intervenant).append("\n");
            }
        }

        return details.toString();
    }


    @Override
    public void notifyObservers(String message) {
    }
}