package entities;

import enumeration.Periodicita;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("Rivista")
public class Rivista extends ElementoCatalogo {

    @Enumerated(EnumType.STRING)
    private Periodicita periodicita;

    public Rivista() {
        super();
    }

    public Rivista(String isbn, String titolo, int annoPubblicazione, int pagine, Periodicita periodicita) {
        super(isbn, titolo, annoPubblicazione, pagine);
        this.periodicita = periodicita;

    }

    public Periodicita getPeriodicita() {
        return periodicita;
    }

    public void setPeriodicita(Periodicita periodicita) {
        this.periodicita = periodicita;
    }

    @Override
    public String toString() {
        return "Rivista{" +
                super.toString()+
                "periodicita=" + periodicita +
                '}';
    }
}
