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
private  EntityManager em;
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
            TypedQuery<ElementoCatalogo> query = em.createQuery(
                    "SELECT e FROM ElementoCatalogo e WHERE e.isbn = :isbn", ElementoCatalogo.class);
            query.setParameter("isbn", elemento.getIsbn());

            List<ElementoCatalogo> resultList = query.getResultList();
            if (resultList.isEmpty()) {
                em.persist(elemento);
                em.getTransaction().commit();
                System.out.println("Elemento aggiunto: " + elemento.getTitolo());
            } else {
                System.out.println("Elemento con ISBN già presente: " + elemento.getIsbn());
            }
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

    public List<Libro> findByAutore(String autore) {
        TypedQuery<Libro> query = em.createQuery(
                "SELECT l FROM Libro l WHERE l.autore = :autore", Libro.class);
        query.setParameter("autore", autore);
        return query.getResultList();
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
