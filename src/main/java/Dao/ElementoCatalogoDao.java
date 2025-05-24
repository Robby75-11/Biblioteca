package Dao;

import entities.ElementoCatalogo;
import entities.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class ElementoCatalogoDao {

    private final EntityManagerFactory emf;
    private final EntityManager em;

    public ElementoCatalogoDao(EntityManager em) {
        this.emf = Persistence.createEntityManagerFactory("biblioteca-unit");
        this.em = em;
    }

    public Optional<ElementoCatalogo> findById(Long id) {
        return Optional.ofNullable(em.find(ElementoCatalogo.class, id));
    }

    public List<ElementoCatalogo> findAll() {
        TypedQuery<ElementoCatalogo> query = em.createQuery("SELECT e FROM ElementoCatalogo e", ElementoCatalogo.class);
        return query.getResultList();
    }

    public void save(ElementoCatalogo elemento) {
        try {
            em.getTransaction().begin();
            em.persist(elemento);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public Optional<ElementoCatalogo> findByCodiceISBN(String codice) {
        TypedQuery<ElementoCatalogo> query = em.createQuery(
                "SELECT e FROM ElementoCatalogo e WHERE e.codiceISBN = :codice", ElementoCatalogo.class);
        query.setParameter("codice", codice);
        List<ElementoCatalogo> results = query.getResultList();
        return results.stream().findFirst();
    }

    public List<ElementoCatalogo> findByAnnoPubblicazione(int anno) {
        TypedQuery<ElementoCatalogo> query = em.createQuery(
                "SELECT e FROM ElementoCatalogo e WHERE e.annoPubblicazione = :anno", ElementoCatalogo.class);
        query.setParameter("anno", anno);
        return query.getResultList();
    }

    public List<Libro> findByAutore(String autore) {
        TypedQuery<Libro> query = em.createQuery(
                "SELECT l FROM Libro l WHERE l.autore LIKE :autore", Libro.class);
        query.setParameter("autore", "%" + autore + "%");
        return query.getResultList();
    }

    public List<ElementoCatalogo> findByTitolo(String titolo) {
        TypedQuery<ElementoCatalogo> query = em.createQuery(
                "SELECT e FROM ElementoCatalogo e WHERE LOWER(e.titolo) LIKE LOWER(:titolo)", ElementoCatalogo.class);
        query.setParameter("titolo", "%" + titolo + "%");
        return query.getResultList();
    }

    public void delete(ElementoCatalogo elemento) {
        try {
            em.getTransaction().begin();
            if (!em.contains(elemento)) {
                elemento = em.merge(elemento);
            }
            em.remove(elemento);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}