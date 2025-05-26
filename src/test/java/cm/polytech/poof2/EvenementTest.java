package cm.polytech.poof2;

import cm.polytech.poof2.Exception.CapaciteMaxAtteinteException;
import cm.polytech.poof2.classes.Evenement;
import cm.polytech.poof2.classes.Participant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EvenementTest {

    private class TestEvenement extends Evenement {
        public TestEvenement(String id, String nom, LocalDateTime date, String lieu, int capaciteMax) {
            super(id, nom, date, lieu, capaciteMax);
        }

        @Override
        public String getDetails() {
            return "Test Event";
        }
    }

    private TestEvenement evenement;
    private Participant participant;
    private LocalDateTime date;

    @BeforeEach
    void setUp() {
        date = LocalDateTime.of(2025, 5, 25, 15, 0);
        evenement = new TestEvenement("e1", "Test Event", date, "Venue", 2);
        participant = new Participant("p1", "John Doe", "john@example.com");
    }

    @AfterEach
    void tearDown() {
        evenement.getParticipants().clear();
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("e1", evenement.getId(), "ID should match");
        assertEquals("Test Event", evenement.getNom(), "Name should match");
        assertEquals(date, evenement.getDate(), "Date should match");
        assertEquals("Venue", evenement.getLieu(), "Location should match");
        assertEquals(2, evenement.getCapaciteMax(), "Capacity should match");
        assertTrue(evenement.getParticipants().isEmpty(), "Participants list should be empty");
    }

    @Test
    void testAjouterParticipant() throws CapaciteMaxAtteinteException {
        evenement.ajouterParticipant(participant);
        assertTrue(evenement.getParticipants().contains(participant), "Participant should be added");
        assertTrue(participant.getEvenements().contains(evenement), "Event should be added to participant's list");
    }

    @Test
    void testAjouterParticipant_Duplicate() throws CapaciteMaxAtteinteException {
        evenement.ajouterParticipant(participant);
        evenement.ajouterParticipant(participant);
        assertEquals(1, evenement.getParticipants().size(), "Duplicate participant should not be added");
    }

    @Test
    void testAjouterParticipant_CapaciteMax() throws CapaciteMaxAtteinteException {
        Participant p2 = new Participant("p2", "Jane Doe", "jane@example.com");
        evenement.ajouterParticipant(participant);
        evenement.ajouterParticipant(p2);
        Participant p3 = new Participant("p3", "Bob Smith", "bob@example.com");
        assertThrows(CapaciteMaxAtteinteException.class, () -> evenement.ajouterParticipant(p3),
                "Should throw exception when capacity is reached");
    }

    @Test
    void testRemoveParticipant() throws CapaciteMaxAtteinteException {
        evenement.ajouterParticipant(participant);
        evenement.removeParticipant("p1");
        assertFalse(evenement.getParticipants().contains(participant), "Participant should be removed");
    }

    @Test
    void testAnnuler() throws CapaciteMaxAtteinteException {
        evenement.ajouterParticipant(participant);
        evenement.annuler();
    }

    @Test
    void testIsCapaciteAtteinte() throws CapaciteMaxAtteinteException {
        assertFalse(evenement.isCapaciteAtteinte(), "Capacity should not be reached initially");
        evenement.ajouterParticipant(participant);
        Participant p2 = new Participant("p2", "Jane Doe", "jane@example.com");
        evenement.ajouterParticipant(p2);
        assertTrue(evenement.isCapaciteAtteinte(), "Capacity should be reached");
    }
}