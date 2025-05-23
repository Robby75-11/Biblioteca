package Dao;
import entities.ElementoCatalogo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class ElementoCatalogoDao {

    private final EntityManagerFactory emf;

    public ElementoCatalogoDao() {
        this.emf = Persistence.createEntityManagerFactory("biblioteca-unit");
    }

    public Optional<ElementoCatalogo> findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return Optional.ofNullable(em.find(ElementoCatalogo.class, id));
        } finally {
            em.close();
        }
    }

    public Optional<ElementoCatalogo> findByIsbn(String isbn) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ElementoCatalogo> query = em.createQuery(
                    "SELECT e FROM ElementoCatalogo e WHERE e.isbn = :isbn", ElementoCatalogo.class);
            query.setParameter("isbn", isbn);
            return query.getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public List<ElementoCatalogo> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ElementoCatalogo> query = em.createQuery("SELECT e FROM ElementoCatalogo e", ElementoCatalogo.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void save(ElementoCatalogo elemento) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(elemento);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void update(ElementoCatalogo elemento) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            elemento = em.merge(elemento);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void delete(ElementoCatalogo elemento) {
        EntityManager em = emf.createEntityManager();
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
        } finally {
            em.close();
        }
    }

    public List<ElementoCatalogo> findByAnnoPubblicazione(int anno) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ElementoCatalogo> query = em.createQuery(
                    "SELECT e FROM ElementoCatalogo e WHERE e.annoPubblicazione = :anno", ElementoCatalogo.class);
            query.setParameter("anno", anno);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ElementoCatalogo> findByAutore(String autore) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ElementoCatalogo> query = em.createQuery(
                    "SELECT e FROM ElementoCatalogo e WHERE TYPE(e) = Libro AND LOWER(e.autore) = LOWER(:autore)", ElementoCatalogo.class);
            query.setParameter("autore", autore);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ElementoCatalogo> findByTitoloContaining(String titolo) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ElementoCatalogo> query = em.createQuery(
                    "SELECT e FROM ElementoCatalogo e WHERE LOWER(e.titolo) LIKE LOWER(:titolo)", ElementoCatalogo.class);
            query.setParameter("titolo", "%" + titolo + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
