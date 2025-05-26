package cm.polytech.poof2;

import cm.polytech.poof2.Sauvegarde.Json;
import cm.polytech.poof2.classes.Concert;
import cm.polytech.poof2.classes.Evenement;
import cm.polytech.poof2.classes.Participant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    private Json persistance;
    private Evenement evenement;
    private Participant participant;

    @BeforeEach
    void setUp() {
        persistance = new Json();
        evenement = new Concert("e1", "Concert", LocalDateTime.now(), "Venue", 10, "Artist", "Rock");
        participant = new Participant("p1", "John Doe", "john@example.com");
    }

    @AfterEach
    void tearDown() {
        // Delete JSON files
        new File("evenements.json").delete();
        new File("organisateurs.json").delete();
        new File("participants.json").delete();
    }

    @Test
    void testSaveAndLoadEvenements() throws Exception {
        Map<String, Evenement> evenements = new HashMap<>();
        evenements.put("e1", evenement);
        persistance.saveEvenements(evenements);
        Map<String, Evenement> loaded = persistance.loadEvenements();
        assertEquals(1, loaded.size(), "Should load one evenement");
        assertEquals("Concert", loaded.get("e1").getNom(), "Evenement name should match");
    }

    @Test
    void testLoadEvenements_FileNotExists() throws Exception {
        Map<String, Evenement> loaded = persistance.loadEvenements();
        assertTrue(loaded.isEmpty(), "Should return empty map when file does not exist");
    }

    @Test
    void testSaveAndLoadOrganisateurs() throws Exception {
        Map<String, Participant> organisateurs = new HashMap<>();
        organisateurs.put("p1", participant);
        persistance.saveOrganisateurs(organisateurs);
        Map<String, Participant> loaded = persistance.loadOrganisateurs();
        assertEquals(1, loaded.size(), "Should load one organisateur");
        assertEquals("John Doe", loaded.get("p1").getNom(), "Organisateur name should match");
    }

    @Test
    void testLoadOrganisateurs_FileNotExists() throws Exception {
        Map<String, Participant> loaded = persistance.loadOrganisateurs();
        assertTrue(loaded.isEmpty(), "Should return empty map when file does not exist");
    }

    @Test
    void testSaveAndLoadParticipants() throws Exception {
        List<Participant> participants = List.of(participant);
        persistance.saveParticipants(participants);
        List<Participant> loaded = persistance.loadParticipants();
        assertEquals(1, loaded.size(), "Should load one participant");
        assertEquals("John Doe", loaded.get(0).getNom(), "Participant name should match");
    }

    @Test
    void testLoadParticipants_FileNotExists() throws Exception {
        List<Participant> loaded = persistance.loadParticipants();
        assertTrue(loaded.isEmpty(), "Should return empty list when file does not exist");
    }
}