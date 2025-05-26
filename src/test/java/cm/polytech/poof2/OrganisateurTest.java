package cm.polytech.poof2;

import cm.polytech.poof2.classes.Concert;
import cm.polytech.poof2.classes.Evenement;
import cm.polytech.poof2.classes.Organisateur;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrganisateurTest {

    private Organisateur organisateur;
    private Evenement evenement;

    @BeforeEach
    void setUp() {
        organisateur = new Organisateur("o1", "Event Planner", "planner@example.com");
        evenement = new Concert("e1", "Concert", LocalDateTime.now(), "Venue", 100, "Artist", "Rock");
    }

    @AfterEach
    void tearDown() {
        organisateur.getEvenements().clear();
        organisateur.getEvenementsOrganises().clear();
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("o1", organisateur.getId(), "ID should match");
        assertEquals("Event Planner", organisateur.getNom(), "Name should match");
        assertEquals("planner@example.com", organisateur.getEmail(), "Email should match");
        assertTrue(organisateur.getEvenementsOrganises().isEmpty(), "Organized events list should be empty");
    }

    @Test
    void testAjouterEvenementOrganise() {
        organisateur.ajouterEvenementOrganise(evenement);
        assertTrue(organisateur.getEvenementsOrganises().contains(evenement), "Event should be added to organized list");
    }

    @Test
    void testAddEvenementAsParticipant() {
        organisateur.addEvenement(evenement);
        assertTrue(organisateur.getEvenements().contains(evenement), "Event should be added to participant's events");
        assertTrue(evenement.getObservers().contains(organisateur), "Organisateur should be registered as observer");
    }

    @Test
    void testGetDetails() {
        organisateur.addEvenement(evenement);
        String details = organisateur.getDetails();
        assertTrue(details.contains("Participant: Event Planner"), "Details should contain name");
        assertTrue(details.contains("Email: planner@example.com"), "Details should contain email");
        assertTrue(details.contains("Événements: Concert"), "Details should contain events");
    }
}