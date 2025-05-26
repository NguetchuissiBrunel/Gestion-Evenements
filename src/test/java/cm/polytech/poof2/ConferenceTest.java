package cm.polytech.poof2;

import cm.polytech.poof2.Services.ConsoleNot;
import cm.polytech.poof2.Services.NotificationService;
import cm.polytech.poof2.classes.Conference;
import cm.polytech.poof2.classes.Intervenant;
import cm.polytech.poof2.classes.Participant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ConferenceTest {

    private Conference conference;
    private LocalDateTime date;
    private Participant participant;

    @BeforeEach
    void setUp() {
        date = LocalDateTime.of(2025, 5, 25, 10, 0);
        conference = new Conference("conf1", "Tech Conference", date, "Hall", 50, "AI Innovations");
        participant = new Participant("p1", "John Doe", "john@example.com");
    }

    @AfterEach
    void tearDown() {
        conference.getParticipants().clear();
        conference.getIntervenants().clear();
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("conf1", conference.getId(), "ID should match");
        assertEquals("Tech Conference", conference.getNom(), "Name should match");
        assertEquals(date, conference.getDate(), "Date should match");
        assertEquals("Hall", conference.getLieu(), "Location should match");
        assertEquals(50, conference.getCapaciteMax(), "Capacity should match");
        assertEquals("AI Innovations", conference.getTheme(), "Theme should match");
        assertTrue(conference.getIntervenants().isEmpty(), "Intervenants list should be empty");
    }

    @Test
    void testSetTheme() {
        conference.setTheme("Blockchain");
        assertEquals("Blockchain", conference.getTheme(), "Theme should be updated");
    }

    @Test
    void testAddIntervenant() {
        Intervenant intervenant = new Intervenant("Dr. Smith", "AI Expert");
        conference.addIntervenant(intervenant);
        assertTrue(conference.getIntervenants().contains(intervenant), "Intervenant should be added");
    }

    @Test
    void testAddIntervenant_Duplicate() {
        Intervenant intervenant = new Intervenant("Dr. Smith", "AI Expert");
        conference.addIntervenant(intervenant);
        conference.addIntervenant(intervenant);
        assertEquals(1, conference.getIntervenants().size(), "Duplicate intervenant should not be added");
    }

    @Test
    void testRemoveIntervenant() {
        Intervenant intervenant = new Intervenant("Dr. Smith", "AI Expert");
        conference.addIntervenant(intervenant);
        conference.removeIntervenant(intervenant);
        assertFalse(conference.getIntervenants().contains(intervenant), "Intervenant should be removed");
    }

    @Test
    void testGetDetails() {
        Intervenant intervenant = new Intervenant("Dr. Smith", "AI Expert");
        conference.addIntervenant(intervenant);
        String details = conference.getDetails();
        assertTrue(details.contains("CONFÉRENCE"), "Details should contain type");
        assertTrue(details.contains("ID: conf1"), "Details should contain ID");
        assertTrue(details.contains("Nom: Tech Conference"), "Details should contain name");
        assertTrue(details.contains("Lieu: Hall"), "Details should contain location");
        assertTrue(details.contains("Capacité: 0/50"), "Details should contain capacity");
        assertTrue(details.contains("Thème: AI Innovations"), "Details should contain theme");
        assertTrue(details.contains("Intervenants:\n- Dr. Smith (AI Expert)"), "Details should contain intervenants");
    }

    @Test
    void testAjouterParticipant() throws Exception {
        conference.ajouterParticipant(participant);
        assertTrue(conference.getParticipants().contains(participant), "Participant should be added");
        assertTrue(participant.getEvenements().contains(conference), "Conference should be added to participant's events");
    }
}