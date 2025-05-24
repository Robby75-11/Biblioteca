import Dao.UtenteDao;
import entities.*;
import enumeration.Periodicita;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainBiblioteca {

    public static void main(String[] args) {
        // Inizializzazione dell'Archivio
        Archivio archivio = new Archivio();

        UtenteDao utenteDao = new UtenteDao();

        // Creazione di alcuni elementi del catalogo
        Libro libro1 = new Libro("978-8808817204", "L'ombra del vento", 2004, 496, "Carlos Ruiz Zafón", "Narrativa");
        Libro libro2 = new Libro("978-8854176747", "Il codice da Vinci", 2003, 432, "Dan Brown", "Thriller");
        Rivista rivista1 = new Rivista("1124-894X", "National Geographic", LocalDate.of(2023, 1, 1), 150, Periodicita.MENSILE);
        Rivista rivista2 = new Rivista("2039-7549", "Focus", LocalDate.of(2024, 1, 1), 120, Periodicita.MENSILE);

        // Aggiunta degli elementi all'archivio
        archivio.aggiungiElemento(libro1);
        archivio.aggiungiElemento(libro2);
        archivio.aggiungiElemento(rivista1);
        archivio.aggiungiElemento(rivista2);

        // Creazione di utenti
        Utente utente1 = new Utente(1L, "Mario", "Rossi", LocalDate.of(1990, 5, 15), "MR12345");
        Utente utente2 = new Utente(2L, "Anna", "Verdi", LocalDate.of(1985, 10, 20), "AV67890");


        List<Utente> utenti = new ArrayList<>();
        utenti.add(utente1);
        utenti.add(utente2);

        // Salvataggio degli utenti nel database utilizzando il UtenteDao
        utenteDao.save(utente1);
        utenteDao.save(utente2);

        System.out.println("Utenti salvati nel database.");
        System.out.println(utente1);
        System.out.println(utente2);

        utenteDao.shutdown();

        // Creazione di prestiti
        Prestito prestito1 = new Prestito(101L, utente1, "Libro", libro1.getId(), LocalDate.now(), LocalDate.now().plusDays(30), null);
        Prestito prestito2 = new Prestito(102L, utente2, "Rivista", rivista1.getId(), LocalDate.now().minusDays(5), LocalDate.now().plusDays(25), null);
        Prestito prestito3 = new Prestito(103L, utente1, "Libro", libro2.getId(), LocalDate.now().minusDays(40), LocalDate.now().minusDays(10), LocalDate.now().minusDays(5)); // Già restituito
        Prestito prestito4 = new Prestito(104L, utente2, "Rivista", rivista2.getId(), LocalDate.now().minusDays(60), LocalDate.now().minusDays(30), null); // Scaduto

        // Registrazione dei prestiti
        archivio.registraPrestito(prestito1);
        archivio.registraPrestito(prestito2);
        archivio.registraPrestito(prestito3);
        archivio.registraPrestito(prestito4);

        // Ricerca prestiti attuali per utente
        System.out.println("\nPrestiti attuali per utente Mario Rossi: " + archivio.ricercaPrestitiAttualiPerUtente(1L, utenti));
        System.out.println("Prestiti attuali per utente Anna Verdi: " + archivio.ricercaPrestitiAttualiPerUtente(2L, utenti));

        // Ricerca prestiti scaduti
        System.out.println("\nPrestiti scaduti: " + archivio.ricercaPrestitiScaduti());

        // Registrazione restituzione
        prestito1.setDataRestituzioneEffettiva(LocalDate.now());
        archivio.registraRestituzione(prestito1);
        System.out.println("\nPrestiti attuali per utente Mario Rossi dopo restituzione: " + archivio.ricercaPrestitiAttualiPerUtente(1L, utenti));

        // Chiusura dell'Archivio (e quindi degli EntityManagerFactory)
        archivio.shutdown();

    }
}