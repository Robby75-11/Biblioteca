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
    private final EntityManager em;

    public PrestitoDao(EntityManager em) {
        this.emf = Persistence.createEntityManagerFactory("biblioteca-unit");
        this.em = em;
    }

    public Optional<Prestito> findById(Long id) {
        return Optional.ofNullable(em.find(Prestito.class, id));
    }

    public List<Prestito> findAll() {
        TypedQuery<Prestito> query = em.createQuery("SELECT p FROM Prestito p", Prestito.class);
        return query.getResultList();
    }

    public void save(Prestito prestito) {
        try {
            em.getTransaction().begin();
            em.persist(prestito);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(Prestito prestito) {
        try {
            em.getTransaction().begin();
            prestito = em.merge(prestito);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public void delete(Prestito prestito) {
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
            e.printStackTrace();
        }
    }

    public List<Prestito> findPrestitiAttuali(Utente utente) {
        TypedQuery<Prestito> query = em.createQuery(
                "SELECT p FROM Prestito p WHERE p.utente = :utente AND p.dataRestituzioneEffettiva IS NULL", Prestito.class);
        query.setParameter("utente", utente);
        return query.getResultList();
    }

    public List<Prestito> findPrestitiScaduti() {
        TypedQuery<Prestito> query = em.createQuery(
                "SELECT p FROM Prestito p WHERE p.dataRestituzionePrevista < :oggi AND p.dataRestituzioneEffettiva IS NULL", Prestito.class);
        query.setParameter("oggi", LocalDate.now());
        return query.getResultList();
    }

    public void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}