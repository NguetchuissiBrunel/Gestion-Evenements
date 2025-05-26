package cm.polytech.poof2.Sauvegarde;

import cm.polytech.poof2.classes.Evenement;
import cm.polytech.poof2.classes.Organisateur;
import cm.polytech.poof2.classes.Participant;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Persistance {
    Map<String, Evenement> loadEvenements() throws IOException;
    void saveEvenements(Map<String, Evenement> evenements) throws IOException;
    Map<String, Participant> loadOrganisateurs() throws IOException;
    void saveOrganisateurs(Map<String, Participant> organisateurs) throws IOException;
    List<Participant> loadParticipants() throws IOException;
    void saveParticipants(List<Participant> participants) throws IOException;
}
