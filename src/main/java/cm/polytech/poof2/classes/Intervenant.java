package cm.polytech.poof2.classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Intervenant {
    private String nom;
    private String specialite;

    @JsonCreator
    public Intervenant(@JsonProperty("nom") String nom, @JsonProperty("specialite") String specialite) {
        this.nom = nom;
        this.specialite = specialite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    @Override
    public String toString() {
        return nom + " (" + specialite + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intervenant that = (Intervenant) o;
        return nom.equals(that.nom) && specialite.equals(that.specialite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, specialite);
    }
}