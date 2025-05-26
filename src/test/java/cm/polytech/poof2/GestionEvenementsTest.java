package cm.polytech.poof2;

import cm.polytech.poof2.Exception.CapaciteMaxAtteinteException;
import cm.polytech.poof2.Exception.EvenementDejaExistantException;
import cm.polytech.poof2.Exception.EvenementNonTrouveException;
import cm.polytech.poof2.Sauvegarde.Json;
import cm.polytech.poof2.Services.GestionEvenements;
import cm.polytech.poof2.classes.Concert;
import cm.polytech.poof2.classes.Evenement;
import cm.polytech.poof2.classes.Participant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestionEvenementsTest {

    private GestionEvenements gestionEvenements;
    private Participant participant;
    private Evenement evenement;

    @BeforeEach
    void setUp() throws Exception {
        // Reset singleton instance
        Field gestField = GestionEvenements.class.getDeclaredField("gest");
        gestField.setAccessible(true);
        gestField.set(null, null);

        // Initialize with real Json persistance
        gestionEvenements = GestionEvenements.getInstance();
        Field persistanceField = GestionEvenements.class.getDeclaredField("persistance");
        persistanceField.setAccessible(true);
        persistanceField.set(gestionEvenements, new Json());

        participant = new Participant("p1", "John Doe", "john@example.com");
        evenement = new Concert("e1", "Concert", LocalDateTime.now(), "Venue", 10, "Artist", "Rock");
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clear in-memory data
        gestionEvenements.getEvenements().clear();
        gestionEvenements.getOg().clear();
        gestionEvenements.getParticipants().clear();
        gestionEvenements.saveData();

        // Delete JSON files
        new File("evenements.json").delete();
        new File("organisateurs.json").delete();
        new File("participants.json").delete();

    }

    @Test
    void testGetInstance() {
        GestionEvenements instance1 = GestionEvenements.getInstance();
        GestionEvenements instance2 = GestionEvenements.getInstance();
        assertSame(instance1, instance2, "Should return the same instance (singleton)");
    }

    @Test
    void testAjouterOrganisateur() throws Exception {
        gestionEvenements.ajouterOrganisateur(participant);
        assertEquals(participant, gestionEvenements.getOg().get("p1"), "Organisateur should be added");
        Json persistance = new Json();
        assertEquals(participant.getNom(), persistance.loadOrganisateurs().get("p1").getNom(), "Organisateur should be persisted");
    }

    @Test
    void testAjouterOrganisateur_AlreadyExists() {
        gestionEvenements.ajouterOrganisateur(participant);
        assertThrows(IllegalArgumentException.class, () -> gestionEvenements.ajouterOrganisateur(participant),
                "Should throw exception for duplicate organisateur");
    }

    @Test
    void testSupprimerOrganisateur() throws Exception {
        gestionEvenements.ajouterOrganisateur(participant);
        gestionEvenements.supprimerOrganisateur("p1");
        assertFalse(gestionEvenements.getOg().containsKey("p1"), "Organisateur should be removed");
        Json persistance = new Json();
        assertFalse(persistance.loadOrganisateurs().containsKey("p1"), "Organisateur should be removed from persistence");
    }

    @Test
    void testSupprimerOrganisateur_NotExists() {
        assertThrows(IllegalArgumentException.class, () -> gestionEvenements.supprimerOrganisateur("p1"),
                "Should throw exception for non-existent organisateur");
    }

    @Test
    void testAjouterEvenement() throws Exception {
        gestionEvenements.ajouterEvenement(evenement);
        assertEquals(evenement, gestionEvenements.getEvenements().get("e1"), "Evenement should be added");
        Json persistance = new Json();
        assertEquals(evenement.getNom(), persistance.loadEvenements().get("e1").getNom(), "Evenement should be persisted");
    }

    @Test
    void testAjouterEvenement_AlreadyExists() throws Exception {
        gestionEvenements.ajouterEvenement(evenement);
        assertThrows(EvenementDejaExistantException.class, () -> gestionEvenements.ajouterEvenement(evenement),
                "Should throw exception for duplicate evenement");
    }

    @Test
    void testSupprimerEvenement() throws Exception {
        gestionEvenements.ajouterEvenement(evenement);
        gestionEvenements.addParticipantToEvent(participant, evenement);
        gestionEvenements.supprimerEvenement("e1");
        assertFalse(gestionEvenements.getEvenements().containsKey("e1"), "Evenement should be removed");
        assertFalse(participant.getEvenements().contains(evenement), "Evenement should be removed from participant's list");
        Json persistance = new Json();
        assertFalse(persistance.loadEvenements().containsKey("e1"), "Evenement should be removed from persistence");
    }

    @Test
    void testRechercherEvenement() throws Exception {
        gestionEvenements.ajouterEvenement(evenement);
        assertTrue(gestionEvenements.rechercherEvenement("e1"), "Evenement should be found");
        assertThrows(EvenementNonTrouveException.class, () -> gestionEvenements.rechercherEvenement("e2"),
                "Should throw exception for non-existent evenement");
    }

    @Test
    void testAddParticipantToEvent() throws Exception {
        gestionEvenements.ajouterEvenement(evenement);
        gestionEvenements.addParticipantToEvent(participant, evenement);
        assertTrue(evenement.getParticipants().contains(participant), "Participant should be added to evenement");
        assertTrue(participant.getEvenements().contains(evenement), "Evenement should be added to participant's list");
        assertTrue(gestionEvenements.getParticipants().contains(participant), "Participant should be in global list");
        Json persistance = new Json();
        assertTrue(persistance.loadParticipants().stream().anyMatch(p -> p.getId().equals("p1")), "Participant should be persisted");
          }

    @Test
    void testUpdateParticipantEvents() throws Exception {
        gestionEvenements.ajouterEvenement(evenement);
        gestionEvenements.addParticipantToEvent(participant, evenement);
        Evenement newEvent = new Concert("e2", "New Concert", LocalDateTime.now(), "New Venue", 10, "New Artist", "Pop");
        gestionEvenements.ajouterEvenement(newEvent);
        gestionEvenements.updateParticipantEvents(participant, List.of(newEvent));
        assertFalse(participant.getEvenements().contains(evenement), "Old evenement should be removed");
        assertTrue(participant.getEvenements().contains(newEvent), "New evenement should be added");
        assertFalse(evenement.getParticipants().contains(participant), "Participant should be removed from old evenement");
        assertTrue(newEvent.getParticipants().contains(participant), "Participant should be added to new evenement");
        Json persistance = new Json();
        assertTrue(persistance.loadParticipants().stream().anyMatch(p -> p.getId().equals("p1")), "Participant should be persisted");
    }
}