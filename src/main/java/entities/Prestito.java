package entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Prestito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int version;

    @ManyToOne
    @JoinColumn(name = "utente_id") // Nome della colonna foreign key nella tabella Prestito

    private Utente utente;
    private String elementoPrestatoTipo;
    private Long elementoPrestatoId;
    private LocalDate dataInizioPrestito;
    private LocalDate dataRestituzionePrevista;
    private LocalDate dataRestituzioneEffettiva;

    public Prestito() {
    }

    public Prestito(Long id, Utente utente, String elementoPrestatoTipo, Long elementoPrestatoId, LocalDate
            dataInizioPrestito, LocalDate dataRestituzionePrevista, LocalDate dataRestituzioneEffettiva) {
        this.id = id;
        this.utente = utente;
        this.elementoPrestatoTipo = elementoPrestatoTipo;
        this.elementoPrestatoId = elementoPrestatoId;
        this.dataInizioPrestito = dataInizioPrestito;
        this.dataRestituzionePrevista = dataRestituzionePrevista;
        this.dataRestituzioneEffettiva = dataRestituzioneEffettiva;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public String getElementoPrestatoTipo() {
        return elementoPrestatoTipo;
    }

    public void setElementoPrestatoTipo(String elementoPrestatoTipo) {
        this.elementoPrestatoTipo = elementoPrestatoTipo;
    }

    public Long getElementoPrestatoId() {
        return elementoPrestatoId;
    }

    public void setElementoPrestatoId(Long elementoPrestatoId) {
        this.elementoPrestatoId = elementoPrestatoId;
    }

    public LocalDate getDataInizioPrestito() {
        return dataInizioPrestito;
    }

    public void setDataInizioPrestito(LocalDate dataInizioPrestito) {
        this.dataInizioPrestito = dataInizioPrestito;
    }

    public LocalDate getDataRestituzionePrevista() {
        return dataRestituzionePrevista;
    }

    public void setDataRestituzionePrevista(LocalDate dataRestituzionePrevista) {
        this.dataRestituzionePrevista = dataRestituzionePrevista;
    }

    public LocalDate getDataRestituzioneEffettiva() {
        return dataRestituzioneEffettiva;
    }

    public void setDataRestituzioneEffettiva(LocalDate dataRestituzioneEffettiva) {
        this.dataRestituzioneEffettiva = dataRestituzioneEffettiva;
    }

    @Override
    public String toString() {
        return "Prestito{" +
                "id=" + id +
                ", utente=" + (utente != null ? utente.getNumeroTessera() : "null") + // Mostra solo il numero di tessera
                ", elementoPrestatoTipo='" + elementoPrestatoTipo + '\'' +
                ", elementoPrestatoId=" + elementoPrestatoId +
                ", dataInizioPrestito=" + dataInizioPrestito +
                ", dataRestituzionePrevista=" + dataRestituzionePrevista +
                ", dataRestituzioneEffettiva=" + dataRestituzioneEffettiva +
                '}';
    }
}
