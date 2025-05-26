package cm.polytech.poof2;

import cm.polytech.poof2.classes.Intervenant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntervenantTest {

    @Test
    void testConstructorAndGetters() {
        Intervenant intervenant = new Intervenant("Dr. Smith", "AI Expert");
        assertEquals("Dr. Smith", intervenant.getNom(), "Name should match");
        assertEquals("AI Expert", intervenant.getSpecialite(), "Specialty should match");
    }

    @Test
    void testSetters() {
        Intervenant intervenant = new Intervenant("Dr. Smith", "AI Expert");
        intervenant.setNom("Dr. Jones");
        intervenant.setSpecialite("Data Science");
        assertEquals("Dr. Jones", intervenant.getNom(), "Name should be updated");
        assertEquals("Data Science", intervenant.getSpecialite(), "Specialty should be updated");
    }

    @Test
    void testToString() {
        Intervenant intervenant = new Intervenant("Dr. Smith", "AI Expert");
        assertEquals("Dr. Smith (AI Expert)", intervenant.toString(), "toString should format correctly");
    }

    @Test
    void testEqualsAndHashCode() {
        Intervenant interv1 = new Intervenant("Dr. Smith", "AI Expert");
        Intervenant interv2 = new Intervenant("Dr. Smith", "AI Expert");
        Intervenant interv3 = new Intervenant("Dr. Jones", "Data Science");
        assertEquals(interv1, interv2, "Intervenants with same name and specialty should be equal");
        assertNotEquals(interv1, interv3, "Intervenants with different name or specialty should not be equal");
        assertEquals(interv1.hashCode(), interv2.hashCode(), "Equal intervenants should have same hash code");
    }
}