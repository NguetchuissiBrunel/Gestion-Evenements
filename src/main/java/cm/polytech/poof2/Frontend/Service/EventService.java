package cm.polytech.poof2.Frontend.Service;

import cm.polytech.poof2.Services.GestionEvenements;
import cm.polytech.poof2.Frontend.MainController;
import cm.polytech.poof2.classes.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventService {
    private GestionEvenements gestionEvenements;

    public EventService() {
        this.gestionEvenements = GestionEvenements.getInstance();
    }

    public void createEvent(String id, String nom, String lieu, String capaciteText, String dateText,
                            String type, String theme, String artiste, String genre,
                            List<Intervenant> intervenants, String idOrg, String nomOrg, String emailOrg,
                            MainController controller) throws Exception {
        // Validation des champs de l'organisateur
        if (idOrg.isEmpty() || nomOrg.isEmpty() || emailOrg.isEmpty()) {
            throw new IllegalArgumentException("Veuillez remplir tous les champs de l'organisateur.");
        }

        // Validation des champs de l'événement
        if (id.isEmpty() || nom.isEmpty() || lieu.isEmpty() || capaciteText.isEmpty() || dateText.isEmpty()) {
            throw new IllegalArgumentException("Veuillez remplir tous les champs obligatoires de l'événement.");
        }

        // Parse capacity
        int capacite;
        try {
            capacite = Integer.parseInt(capaciteText);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("La capacité doit être un nombre entier.");
        }

        // Parse date
        LocalDateTime date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            date = LocalDateTime.parse(dateText, formatter);
        } catch (Exception ex) {
            throw new IllegalArgumentException("La date doit être au format dd/MM/yyyy HH:mm.");
        }

        // Vérifier si l'organisateur existe déjà
        Participant org = gestionEvenements.getOg().get(idOrg);
        if (org == null) {
            // Créer un nouvel organisateur
            org = new Organisateur(idOrg, nomOrg, emailOrg);
            controller.addOrg(org);
        } else {
            // Vérifier si les informations correspondent
            if (!org.getNom().equals(nomOrg) || !org.getEmail().equals(emailOrg)) {
                throw new IllegalArgumentException("Un organisateur avec cet ID existe déjà avec des informations différentes.");
            }
        }

        Evenement event;
        if ("Conférence".equals(type)) {
            if (theme.isEmpty()) {
                throw new IllegalArgumentException("Veuillez spécifier un thème pour la conférence.");
            }
            event = new Conference(id, nom, date, lieu, capacite, theme);
            for (Intervenant intervenant : intervenants) {
                ((Conference) event).addIntervenant(intervenant);
            }
        } else if ("Concert".equals(type)) {
            if (artiste.isEmpty() || genre.isEmpty()) {
                throw new IllegalArgumentException("Veuillez spécifier l'artiste et le genre pour le concert.");
            }
            event = new Concert(id, nom, date, lieu, capacite, artiste, genre);
        } else {
            throw new IllegalArgumentException("Veuillez sélectionner un type d'événement.");
        }

        // Ajouter l'événement à GestionEvenements
        controller.addEvent(event);
        // Ajouter l'événement à la liste des événements organisés
        ((Organisateur) org).ajouterEvenementOrganise(event);
        // Mettre à jour l'organisateur dans GestionEvenements
        gestionEvenements.getOg().put(idOrg, org);
        gestionEvenements.saveData();
    }
}