package entities;

import Dao.ElementoCatalogoDao;
import Dao.PrestitoDao;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
        Optional<ElementoCatalogo> elementoOpt = elementoCatalogoDao.findByIsbn(isbn);
        if (elementoOpt.isPresent()) {
            elementoCatalogoDao.delete(elementoOpt.get());
            System.out.println("Elemento rimosso: " + isbn);
            return true;
        } else {
            System.out.println("Elemento non trovato per rimozione: " + isbn);
            return false;
        }
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
        return utenti.stream()
                .filter(u -> u.getId().equals(utenteId))
                .findFirst()
                .map(utente -> prestitoDao.findPrestitiAttuali(utente).stream()
                        .map(prestito -> elementoCatalogoDao.findById(prestito.getElementoPrestatoId()).orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    /**
     * Ricerca di tutti i prestiti scaduti e non ancora restituiti.
     */
    public List<Prestito> ricercaPrestitiScaduti() {
        return prestitoDao.findPrestitiScaduti();
    }

    public void registraPrestito(Prestito prestito) {
        PrestitoDao prestitoDao = new PrestitoDao();
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