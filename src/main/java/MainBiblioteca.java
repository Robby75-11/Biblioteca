
import entities.*;
import enumeration.Periodicita;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainBiblioteca {

    public static void main(String[] args) {
        // Inizializzazione dell'Archivio
        Archivio archivio = new Archivio();

        // Creazione di alcuni elementi del catalogo
        Libro libro1 = new Libro("978-8808817204", "L'ombra del vento", 2004, 496, "Carlos Ruiz Zafón", "Narrativa");
        Libro libro2 = new Libro("978-8854176747", "Il codice da Vinci", 2003, 432, "Dan Brown", "Thriller");
        Rivista rivista1 = new Rivista("1124-894X", "National Geographic", 2023, 150);
        rivista1.setPeriodicita(Periodicita .MENSILE);
        Rivista rivista2 = new Rivista("2039-7549", "Focus", 2024, 120);
        rivista2.setPeriodicita(Periodicita.MENSILE);


        // Aggiunta degli elementi all'archivio
        archivio.aggiungiElemento(libro1);
        archivio.aggiungiElemento(libro2);
        archivio.aggiungiElemento(rivista1);
        archivio.aggiungiElemento(rivista2);

        // Ricerca per ISBN
        System.out.println("Ricerca per ISBN 978-8808817204: " + archivio.ricercaPerISBN("978-8808817204"));
        System.out.println("Ricerca per ISBN 1124-894X: " + archivio.ricercaPerISBN("1124-894X"));
        System.out.println("Ricerca per ISBN non esistente: " + archivio.ricercaPerISBN("123-4567890123"));

        // Ricerca per anno pubblicazione
        System.out.println("\nRicerca per anno pubblicazione 2003: " + archivio.ricercaPerAnnoPubblicazione(2003));

        // Ricerca per autore
        System.out.println("\nRicerca per autore Dan Brown: " + archivio.ricercaPerAutore("Dan Brown"));

        // Ricerca per titolo
        System.out.println("\nRicerca per titolo contenente 'vento': " + archivio.ricercaPerTitolo("vento"));

        // Creazione di utenti
        Utente utente1 = new Utente(1L, "Mario", "Rossi", LocalDate.of(1990, 5, 15), "MR12345");
        Utente utente2 = new Utente(2L, "Anna", "Verdi", LocalDate.of(1985, 10, 20), "AV67890");

        List<Utente> utenti = new ArrayList<>();
        utenti.add(utente1);
        utenti.add(utente2);

        // Creazione di prestiti
        Prestito prestito1 = new Prestito(101L, utente1, "978-8808817204", LocalDate.now(), LocalDate.now().plusDays(30), null);
        Prestito prestito2 = new Prestito(102L, utente2, "1124-894X", LocalDate.now().minusDays(5), LocalDate.now().plusDays(25), null);
        Prestito prestito3 = new Prestito(103L, utente1, "978-8854176747", LocalDate.now().minusDays(40), LocalDate.now().minusDays(10), LocalDate.now().minusDays(5)); // Già restituito
        Prestito prestito4 = new Prestito(104L, utente2, "2039-7549", LocalDate.now().minusDays(60), LocalDate.now().minusDays(30), null); // Scaduto

        // Registrazione dei prestiti
        archivio.registraPrestito(prestito1);
        archivio.registraPrestito(prestito2);
        archivio.registraPrestito(prestito3);
        archivio.registraPrestito(prestito4);

        // Ricerca prestiti attuali per utente
        System.out.println("\nPrestiti attuali per utente Mario Rossi: " + archivio.ricercaPrestitiAttualiPerUtente("MR12345", utenti));
        System.out.println("Prestiti attuali per utente Anna Verdi: " + archivio.ricercaPrestitiAttualiPerUtente("AV67890", utenti));

        // Ricerca prestiti scaduti
        System.out.println("\nPrestiti scaduti: " + archivio.ricercaPrestitiScaduti());

        // Registrazione restituzione
        prestito1.setDataRestituzioneEffettiva(LocalDate.now().plusDays(10));
        archivio.registraRestituzione(prestito1);
        System.out.println("\nPrestiti attuali per utente Mario Rossi dopo restituzione: " + archivio.ricercaPrestitiAttualiPerUtente("MR12345", utenti));

        // Chiusura dell'Archivio (e quindi degli EntityManagerFactory)
        archivio.shutdown();
    }
}
