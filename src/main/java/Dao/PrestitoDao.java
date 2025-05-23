package Dao;

import entities.Prestito;
import entities.Utente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PrestitoDao {

    private final EntityManagerFactory emf;

    public PrestitoDao() {
        this.emf = Persistence.createEntityManagerFactory("biblioteca-unit");
    }

    public Optional<Prestito> findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Prestito prestito = em.find(Prestito.class, id);
            return Optional.ofNullable(prestito);
        } finally {
            em.close();
        }
    }

    public List<Prestito> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Prestito> query = em.createQuery("SELECT p FROM Prestito p", Prestito.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Prestito prestito) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(prestito);
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

    public void update(Prestito prestito) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            prestito = em.merge(prestito);
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

    public void delete(Prestito prestito) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (!em.contains(prestito)) {
                prestito = em.merge(prestito);
            }
            em.remove(prestito);
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
            Prestito prestito = em.find(Prestito.class, id);
            if (prestito != null) {
                em.remove(prestito);
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

    public List<Prestito> findByUtente(Utente utente) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Prestito> query = em.createQuery(
                    "SELECT p FROM Prestito p WHERE p.utente = :utente", Prestito.class);
            query.setParameter("utente", utente);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Prestito> findPrestitiAttuali(Utente utente) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Prestito> query = em.createQuery(
                    "SELECT p FROM Prestito p WHERE p.utente = :utente AND p.dataRestituzioneEffettiva IS NULL", Prestito.class);
            query.setParameter("utente", utente);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Prestito> findPrestitiScaduti() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Prestito> query = em.createQuery(
                    "SELECT p FROM Prestito p WHERE p.dataRestituzionePrevista < :oggi AND p.dataRestituzioneEffettiva IS NULL", Prestito.class);
            query.setParameter("oggi", LocalDate.now());
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