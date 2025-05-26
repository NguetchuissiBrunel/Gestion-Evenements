package cm.polytech.poof2;

import cm.polytech.poof2.classes.Concert;
import cm.polytech.poof2.classes.Evenement;
import cm.polytech.poof2.classes.Participant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {

    private Participant participant;
    private Evenement evenement;

    @BeforeEach
    void setUp() {
        participant = new Participant("p1", "John Doe", "john@example.com");
        evenement = new Concert("e1", "Concert", LocalDateTime.now(), "Venue", 100, "Artist", "Rock");
    }

    @AfterEach
    void tearDown() {
        participant.getEvenements().clear();
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("p1", participant.getId(), "ID should match");
        assertEquals("John Doe", participant.getNom(), "Name should match");
        assertEquals("john@example.com", participant.getEmail(), "Email should match");
        assertTrue(participant.getEvenements().isEmpty(), "Events list should be empty");
    }

    @Test
    void testSetters() {
        participant.setId("p2");
        participant.setNom("Jane Doe");
        participant.setEmail("jane@example.com");
        assertEquals("p2", participant.getId(), "ID should be updated");
        assertEquals("Jane Doe", participant.getNom(), "Name should be updated");
        assertEquals("jane@example.com", participant.getEmail(), "Email should be updated");
    }

    @Test
    void testAddEvenement() {
        participant.addEvenement(evenement);
        assertTrue(participant.getEvenements().contains(evenement), "Event should be added");
        assertTrue(evenement.getObservers().contains(participant), "Participant should be registered as observer");
    }

    @Test
    void testUpdate() {
        participant.update("Test message");
    }

    @Test
    void testGetDetails() {
        participant.addEvenement(evenement);
        String details = participant.getDetails();
        assertTrue(details.contains("Participant: John Doe"), "Details should contain name");
        assertTrue(details.contains("Email: john@example.com"), "Details should contain email");
        assertTrue(details.contains("Événements: Concert"), "Details should contain events");
    }
}