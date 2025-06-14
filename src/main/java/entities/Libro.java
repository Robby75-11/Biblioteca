package entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("Libro")

public class Libro extends ElementoCatalogo {

    private String autore;
    private String genere;

    public Libro() {
        super();
    }

    public Libro(String isbn, String titolo, int annoPubblicazione, int pagine, String autore, String genere) {
        super(isbn, titolo, annoPubblicazione, pagine);
        this.autore = autore;
        this.genere = genere;
    }

     public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }


    @Override
    public String toString() {
        return "Book{" +
                 super.toString()+
                "autore='" + autore + '\'' +
                ", genere='" + genere + '\'' +
                '}';
    }
}
