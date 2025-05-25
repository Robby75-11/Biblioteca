package entities;

import Dao.ElementoCatalogoDao;
import Dao.PrestitoDao;
import Dao.UtenteDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import java.util.Optional;

public class Archivio {

    private final EntityManagerFactory emf;
    private final EntityManager em;
    private final ElementoCatalogoDao elementoDao;
    private final PrestitoDao prestitoDao;
    private final UtenteDao utenteDao;

    public Archivio() {
        this.emf = Persistence.createEntityManagerFactory("biblioteca-unit");
        this.em = emf.createEntityManager();
        this.elementoDao = new ElementoCatalogoDao(em);
        this.prestitoDao = new PrestitoDao(em);
        this.utenteDao = new UtenteDao(em);
    }

    public void aggiungiElemento(ElementoCatalogo elemento) {
        elementoDao.save(elemento);
    }

    public void rimuoviElementoPerCodiceISBN(String codiceISBN) {
        Optional<ElementoCatalogo> elementoDaRimuovere = elementoDao.findByCodiceISBN(codiceISBN);
        elementoDaRimuovere.ifPresent(elementoDao::delete);
    }

    public Optional<ElementoCatalogo> ricercaElementoPerISBN(String isbn) {
        return elementoDao.findByCodiceISBN(isbn);
    }

    public List<ElementoCatalogo> ricercaElementiPerAnnoPubblicazione(int anno) {
        return elementoDao.findByAnnoPubblicazione(anno);
    }

    public List<ElementoCatalogo> ricercaLibriPerAutore(String autore) {
        return elementoDao.findByAutore(autore).stream()
                .map(elemento -> (ElementoCatalogo) elemento) // Esegui il cast esplicito
                .collect(java.util.stream.Collectors.toList());
    }

    public List<ElementoCatalogo> ricercaElementiPerTitolo(String titolo) {
        return elementoDao.findByTitolo(titolo);
    }

    public void registraPrestito(Prestito prestito) {
        prestitoDao.save(prestito);
    }

    public Optional<Utente> ricercaUtentePerNumeroTessera(String numeroTessera) {
        return utenteDao.findByNumeroTessera(numeroTessera);
    }

    public List<Prestito> ricercaPrestitiAttualiPerUtente(Long utenteId, List<Utente> utenti) {
        Optional<Utente> utente = utenti.stream().filter(u -> u.getId().equals(utenteId)).findFirst();
        return utente.map(prestitoDao::findPrestitiAttuali).orElse(List.of());
    }

    public List<Prestito> ricercaPrestitiScaduti() {
        return prestitoDao.findPrestitiScaduti();
    }

    public void registraRestituzione(Prestito prestito) {
        prestitoDao.update(prestito);
    }

    public void shutdown() {
        elementoDao.shutdown();
        prestitoDao.shutdown();
        utenteDao.shutdown();
        if (em.isOpen()) {
            em.close();
        }
        if (emf.isOpen()) {
            emf.close();
        }
    }
}