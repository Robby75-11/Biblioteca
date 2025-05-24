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
    private final EntityManager em;

    public UtenteDao(EntityManager em) {
        this.emf = Persistence.createEntityManagerFactory("biblioteca-unit");
        this.em = em;
    }

    public Optional<Utente> findById(Long id) {
        return Optional.ofNullable(em.find(Utente.class, id));
    }

    public Optional<Utente> findByNumeroTessera(String numeroTessera) {
        TypedQuery<Utente> query = em.createQuery(
                "SELECT u FROM Utente u WHERE u.numeroTessera = :numeroTessera", Utente.class);
        query.setParameter("numeroTessera", numeroTessera);
        List<Utente> results = query.getResultList();
        return results.stream().findFirst();
    }

    public List<Utente> findAll() {
        TypedQuery<Utente> query = em.createQuery("SELECT u FROM Utente u", Utente.class);
        return query.getResultList();
    }

    public void save(Utente utente) {
        try {
            em.getTransaction().begin();
            em.persist(utente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(Utente utente) {
        try {
            em.getTransaction().begin();
            utente = em.merge(utente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public void delete(Utente utente) {
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
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}