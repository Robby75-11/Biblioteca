package entities;
import Dao.ElementoCatalogoDao;
import Dao.PrestitoDao;
import entities.ElementoCatalogo; // Importa ElementoCatalogo dal package corretto
import entities.Libro;           // Importa Libro dal package corretto
import entities.Prestito;        // Importa Prestito dal package corretto
import entities.Utente;
import java.util.List;
import java.util.stream.Collectors;

public class Archivio {

    private final ElementoCatalogoDao elementoCatalogoDao;
    private final PrestitoDao prestitoDao;

    public Archivio() {
        this.elementoCatalogoDao = new ElementoCatalogoDao();
        this.prestitoDao = new PrestitoDao();
    }

    public void aggiungiElemento(ElementoCatalogo elemento) {
        elementoCatalogoDao.save(elemento);
    }

    public boolean rimuoviElemento(String isbn) {
        return elementoCatalogoDao.findByIsbn(isbn)
                .map(elemento -> {
                    elementoCatalogoDao.delete(elemento);
                    return true;
                })
                .orElse(false);
    }

    public ElementoCatalogo ricercaPerISBN(String isbn) {
        return elementoCatalogoDao.findByIsbn(isbn).orElse(null);
    }

    public List<ElementoCatalogo> ricercaPerAnnoPubblicazione(int anno) {
        return elementoCatalogoDao.findByAnnoPubblicazione(anno);
    }

    public List<Libro> ricercaPerAutore(String autore) {
        return elementoCatalogoDao.findByAutore(autore).stream()
                .filter(e -> e instanceof Libro)
                .map(e -> (Libro) e)
                .collect(Collectors.toList());
    }

    public List<ElementoCatalogo> ricercaPerTitolo(String titolo) {
        return elementoCatalogoDao.findByTitoloContaining(titolo);
    }

    /**
     * Ricerca gli elementi attualmente in prestito dato il numero di tessera di un utente.
     * Nota: Assumiamo che Prestito contenga l'ISBN dell'elemento.
     */
    public List<ElementoCatalogo> ricercaPrestitiAttualiPerUtente(Long utenteId, List<Utente> utenti) {
        Utente utente = utenti.stream()
                .filter(u -> u.getId().equals(utenteId))
                .findFirst()
                .orElse(null);

        if (utente != null) {
            return prestitoDao.findPrestitiAttuali(utente).stream()
                    .map(prestito -> {
                        if ("Libro".equals(prestito.getElementoPrestatoTipo())) {
                            // Assumi che ElementoCatalogoDao abbia un metodo findById(Long id)
                            return elementoCatalogoDao.findById(prestito.getElementoPrestatoId()).orElse(null);
                        } else if ("Rivista".equals(prestito.getElementoPrestatoTipo())) {
                            // Assumi che ElementoCatalogoDao abbia un metodo findById(Long id)
                            return elementoCatalogoDao.findById(prestito.getElementoPrestatoId()).orElse(null);
                        }
                        return null; // Se il tipo non è riconosciuto
                    })
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * Ricerca di tutti i prestiti scaduti e non ancora restituiti.
     */
    public List<Prestito> ricercaPrestitiScaduti() {
        return prestitoDao.findPrestitiScaduti();
    }

    public void registraPrestito(Prestito prestito) {
        prestitoDao.save(prestito);
    }

    public void registraRestituzione(Prestito prestito) {
        prestitoDao.update(prestito); // Assumiamo che l'oggetto Prestito aggiornato venga passato
    }

    public void shutdown() {
        elementoCatalogoDao.shutdown();
        prestitoDao.shutdown();
    }
}