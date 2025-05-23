package Dao;


import entities.Utente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UtenteDao {

    private final EntityManagerFactory emf;

    public UtenteDao() {
        // Il nome "biblioteca-unit" deve corrispondere al nome della tua unità di persistenza in persistence.xml
        this.emf = Persistence.createEntityManagerFactory("biblioteca-unit");
    }

    public Optional<Utente> findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Utente utente = em.find(Utente.class, id);
            return Optional.ofNullable(utente);
        } finally {
            em.close();
        }
    }

    public Optional<Utente> findByNumeroTessera(String numeroTessera) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Utente> query = em.createQuery(
                    "SELECT u FROM Utente u WHERE u.numeroTessera = :numeroTessera", Utente.class);
            query.setParameter("numeroTessera", numeroTessera);
            return query.getResultList().stream().findFirst();
        } finally {
            em.close();
        }
    }

    public List<Utente> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Utente> query = em.createQuery("SELECT u FROM Utente u", Utente.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Utente utente) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(utente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace(); // Loggare l'errore
        } finally {
            em.close();
        }
    }

    public void update(Utente utente) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            utente = em.merge(utente); // Merge per aggiornare l'entità detached
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace(); // Loggare l'errore
        } finally {
            em.close();
        }
    }

    public void delete(Utente utente) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (!em.contains(utente)) {
                utente = em.merge(utente);
            }
            em.remove(utente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace(); // Loggare l'errore
        } finally {
            em.close();
        }
    }

    public void deleteById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Utente utente = em.find(Utente.class, id);
            if (utente != null) {
                em.remove(utente);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace(); // Loggare l'errore
        } finally {
            em.close();
        }
    }

    // Metodo per chiudere l'EntityManagerFactory quando l'applicazione termina
    public void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

