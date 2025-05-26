package cm.polytech.poof2.classes;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Concert extends Evenement {
    private String artiste;
    private String genre;

    public Concert() {
        super();
    }

    public Concert(String id, String nom, LocalDateTime date, String lieu, int capaciteMax, String artiste, String genre) {
        super(id, nom, date, lieu, capaciteMax);
        this.artiste = artiste;
        this.genre = genre;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String getDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder details = new StringBuilder();
        details.append("CONCERT\n\n");
        details.append("ID: ").append(getId()).append("\n");
        details.append("Nom: ").append(getNom()).append("\n");
        details.append("Date: ").append(getDate().format(formatter)).append("\n");
        details.append("Lieu: ").append(getLieu()).append("\n");
        details.append("Capacité: ").append(getParticipants().size()).append("/").append(getCapaciteMax()).append("\n");
        details.append("Artiste: ").append(artiste).append("\n");
        details.append("Genre musical: ").append(genre).append("\n");

        return details.toString();
    }

    @Override
    public void notifyObservers(String message) {
    }
}