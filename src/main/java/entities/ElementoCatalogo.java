package entities;

import jakarta.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class ElementoCatalogo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)

  @Column(unique = true)
    private String isbn;
  private String titolo;
  private int annoPubblicazione;
  private int pagine;

    public ElementoCatalogo(String isbn, String titolo, int annoPubblicazione, int pagine) {
        this.isbn = isbn;
        this.titolo = titolo;
        this.annoPubblicazione = annoPubblicazione;
        this.pagine = pagine;
    }



    public String getTitle() {
        return titolo;
    }

    public void setTitle(String title) {
        this.titolo = title;
    }


    public int getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    public void setAnnoPubblicazione(int annoPubblicazione) {
        this.annoPubblicazione = annoPubblicazione;
    }

    public int getPages() {
        return pagine;
    }

    public void setPages(int pages) {
        this.pagine= pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "CatalogueElement{" +
                "isbn=" + isbn +
                ", title='" + titolo + '\'' +
                ", annoPubblicazione=" + annoPubblicazione +
                ", pages=" + pagine +
                '}';
    }
}
