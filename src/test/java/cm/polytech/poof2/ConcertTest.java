package cm.polytech.poof2;

import cm.polytech.poof2.Exception.CapaciteMaxAtteinteException;
import cm.polytech.poof2.classes.Concert;
import cm.polytech.poof2.classes.Participant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ConcertTest {

    private Concert concert;
    private LocalDateTime date;
    private Participant participant;

    @BeforeEach
    void setUp() {
        date = LocalDateTime.of(2025, 5, 25, 20, 0);
        concert = new Concert("c1", "Rock Concert", date, "Stadium", 100, "The Band", "Rock");
        participant = new Participant("p1", "John Doe", "john@example.com");
    }

    @AfterEach
    void tearDown() {
        concert.getParticipants().clear();
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("c1", concert.getId(), "ID should match");
        assertEquals("Rock Concert", concert.getNom(), "Name should match");
        assertEquals(date, concert.getDate(), "Date should match");
        assertEquals("Stadium", concert.getLieu(), "Location should match");
        assertEquals(100, concert.getCapaciteMax(), "Capacity should match");
        assertEquals("The Band", concert.getArtiste(), "Artist should match");
        assertEquals("Rock", concert.getGenre(), "Genre should match");
    }

    @Test
    void testSetters() {
        concert.setArtiste("New Artist");
        concert.setGenre("Pop");
        assertEquals("New Artist", concert.getArtiste(), "Artist should be updated");
        assertEquals("Pop", concert.getGenre(), "Genre should be updated");
    }

    @Test
    void testGetDetails() {
        String details = concert.getDetails();
        assertTrue(details.contains("CONCERT"), "Details should contain type");
        assertTrue(details.contains("ID: c1"), "Details should contain ID");
        assertTrue(details.contains("Nom: Rock Concert"), "Details should contain name");
        assertTrue(details.contains("Lieu: Stadium"), "Details should contain location");
        assertTrue(details.contains("Capacité: 0/100"), "Details should contain capacity");
        assertTrue(details.contains("Artiste: The Band"), "Details should contain artist");
        assertTrue(details.contains("Genre musical: Rock"), "Details should contain genre");
    }

    @Test
    void testAjouterParticipant() throws Exception {
        concert.ajouterParticipant(participant);
        assertTrue(concert.getParticipants().contains(participant), "Participant should be added");
        assertTrue(participant.getEvenements().contains(concert), "Concert should be added to participant's events");
    }

    @Test
    void testAjouterParticipant_CapaciteMax() throws Exception {
        concert = new Concert("c1", "Rock Concert", date, "Stadium", 1, "The Band", "Rock");
        concert.ajouterParticipant(participant);
        Participant p2 = new Participant("p2", "Jane Doe", "jane@example.com");
        assertThrows(CapaciteMaxAtteinteException.class, () -> concert.ajouterParticipant(p2),
                "Should throw exception when capacity is reached");
    }

    @Test
    void testAnnuler() throws Exception {
        concert.ajouterParticipant(participant);
        concert.annuler();
    }
}