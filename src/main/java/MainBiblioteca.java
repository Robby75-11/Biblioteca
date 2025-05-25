import Dao.ElementoCatalogoDao;
import Dao.PrestitoDao;
import Dao.UtenteDao;
import entities.*;
import enumeration.Periodicita;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MainBiblioteca {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("biblioteca-unit");
    private static final ElementoCatalogoDao elementoDao = new ElementoCatalogoDao(emf.createEntityManager());
    private static final PrestitoDao prestitoDao = new PrestitoDao(emf.createEntityManager());
    private static final UtenteDao utenteDao = new UtenteDao(emf.createEntityManager());
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean continua = true;
        while (continua) {
            mostraMenu();
            if (scanner.hasNextInt()) {
                int scelta = scanner.nextInt();
                scanner.nextLine(); // Consuma la newline

                switch (scelta) {
                    case 1:
                        aggiungiElementoCatalogo();
                        break;
                    case 2:
                        rimuoviElementoCatalogo();
                        break;
                    case 3:
                        ricercaPerISBN();
                        break;
                    case 4:
                        ricercaPerAnnoPubblicazione();
                        break;
                    case 5:
                        ricercaPerAutore();
                        break;
                    case 6:
                        ricercaPerTitolo();
                        break;
                    case 7:
                        ricercaPrestitiAttualiPerTessera();
                        break;
                    case 8:
                        ricercaPrestitiScaduti();
                        break;
                    case 0:
                        continua = false;
                        System.out.println("Arrivederci!");
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            }
        }

        elementoDao.shutdown();
        prestitoDao.shutdown();
        utenteDao.shutdown();
        emf.close();
        scanner.close();
    }

    private static void mostraMenu() {
        System.out.println("\n--- Menu Biblioteca ---");
        System.out.println("1. Aggiungi elemento al catalogo (L=Libro, R=Rivista)");
        System.out.println("2. Rimuovi elemento dal catalogo (ISBN)");
        System.out.println("3. Ricerca elemento per ISBN");
        System.out.println("4. Ricerca elementi per anno di pubblicazione");
        System.out.println("5. Ricerca libri per autore");
        System.out.println("6. Ricerca elementi per titolo o parte di esso");
        System.out.println("7. Ricerca prestiti attuali per numero di tessera utente");
        System.out.println("8. Ricerca prestiti scaduti non restituiti");
        System.out.println("0. Esci");

        System.out.print("Inserisci la tua scelta: ");
    }

    private static void aggiungiUtente() {
        System.out.print("Inserisci nome utente: ");
        String nome = scanner.nextLine();
        System.out.print("Inserisci cognome utente: ");
        String cognome = scanner.nextLine();
        System.out.print("Inserisci numero tessera utente: ");
        String numeroTessera = scanner.nextLine();
        if (!isValidNumeroTessera(numeroTessera)) { // Esempio di una funzione di validazione
            System.out.println("Numero tessera non valido.");
            return; // Esci dal metodo senza salvare l'utente
        }

        System.out.print("Inserisci data di nascita (AAAA-MM-GG): ");
        LocalDate dataNascita = LocalDate.parse(scanner.nextLine()); // Leggi e parsa la data

        Utente utente = new Utente(null, nome, cognome, numeroTessera, dataNascita);
        utenteDao.save(utente);
        System.out.println("Utente aggiunto con numero tessera: " + utente.getNumeroTessera());
    }
    private static boolean isValidNumeroTessera(String numeroTessera) {
        System.out.println("DEBUG: Numero tessera inserito: '" + numeroTessera + "'");
        System.out.println("DEBUG: Non nullo: " + (numeroTessera != null));
        System.out.println("DEBUG: Non vuoto (trim): " + (!numeroTessera.trim().isEmpty()));
        System.out.println("DEBUG: Lunghezza: " + (numeroTessera != null ? numeroTessera.length() : -1));
        // Aggiungi altre stampe di debug per qualsiasi altra logica di validazione
        boolean isValid = numeroTessera != null && !numeroTessera.trim().isEmpty() && numeroTessera.length() == 8;
        System.out.println("DEBUG: Risultato validazione: " + isValid);
        return isValid;
    }
        private static void creaPrestito () {
            System.out.print("Inserisci il numero di tessera dell'utente che prende in prestito: ");
            String numeroTesseraUtente = scanner.nextLine();
            Optional<Utente> utente = utenteDao.findByNumeroTessera(numeroTesseraUtente);

            if (utente.isPresent()) {
                System.out.print("Inserisci il codice ISBN/ISSN dell'elemento da prestare: ");
                String codiceElemento = scanner.nextLine();
                Optional<ElementoCatalogo> elemento = elementoDao.findByCodiceISBN(codiceElemento);

                if (elemento.isPresent()) {
                    System.out.print("Inserisci la data di inizio del prestito (AAAA-MM-GG): ");
                    LocalDate dataInizio = LocalDate.parse(scanner.nextLine());
                    System.out.print("Inserisci la data di restituzione prevista (AAAA-MM-GG): ");
                    LocalDate dataPrevista = LocalDate.parse(scanner.nextLine());

                    Prestito prestito = new Prestito(null, utente.get(), elemento.get().getClass().getSimpleName(), elemento.get().getId(), dataInizio, dataPrevista, null);
                    prestitoDao.save(prestito);
                    System.out.println("Prestito creato con ID: " + prestito.getId());
                } else {
                    System.out.println("Elemento con codice " + codiceElemento + " non trovato.");
                }
            } else {
                System.out.println("Utente con numero tessera " + numeroTesseraUtente + " non trovato.");
            }
        }

        private static void aggiungiElementoCatalogo () {
            System.out.print("Tipo di elemento (L/R): ");
            String tipo = scanner.nextLine().toUpperCase();
            System.out.print("Codice ISBN: ");
            String codice = scanner.nextLine();
            System.out.print("Titolo: ");
            String titolo = scanner.nextLine();
            System.out.print("Anno di pubblicazione: ");
            int anno = scanner.nextInt();
            scanner.nextLine(); // Consuma la newline
            System.out.print("Numero di pagine: ");
            int pagine = scanner.nextInt();
            scanner.nextLine(); // Consuma la newline

            if (tipo.equals("L")) {
                System.out.print("Autore: ");
                String autore = scanner.nextLine();
                System.out.print("Genere: ");
                String genere = scanner.nextLine();
                Libro libro = new Libro(codice, titolo, anno, pagine, autore, genere);
                elementoDao.save(libro);
                System.out.println("Libro aggiunto al catalogo.");
            } else if (tipo.equals("R")) {

                System.out.print("Periodicita (MENSILE, SETTIMANALE, ANNUALE): ");
                Periodicita periodicita = Periodicita.valueOf(scanner.nextLine().toUpperCase());
                Rivista rivista = new Rivista(codice, titolo, anno, pagine, periodicita);
                elementoDao.save(rivista);
                System.out.println("Rivista aggiunta al catalogo.");
            } else {
                System.out.println("Tipo di elemento non valido.");
            }
        }

        private static void rimuoviElementoCatalogo () {
            System.out.print("Inserisci il codice ISBN dell'elemento da rimuovere: ");
            String codice = scanner.nextLine();
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                Query query = em.createQuery(
                        "DELETE FROM ElementoCatalogo e WHERE e.isbn = :codice");
                int deletedCount = query.setParameter("codice", codice).executeUpdate();
                em.getTransaction().commit();
                if (deletedCount > 0) {
                    System.out.println("Elemento con codice " + codice + " rimosso dal catalogo.");
                } else {
                    System.out.println("Nessun elemento trovato con codice " + codice + ".");
                }
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                e.printStackTrace();
            } finally {
                em.close();
            }
        }

        private static void ricercaPerISBN () {
            System.out.print("Inserisci il codice ISBN da cercare: ");
            String codice = scanner.nextLine();
            Optional<ElementoCatalogo> elemento = elementoDao.findByCodiceISBN(codice);
            elemento.ifPresentOrElse(System.out::println, () -> System.out.println("Nessun elemento trovato con codice " + codice + "."));
        }

        private static void ricercaPerAnnoPubblicazione () {
            System.out.print("Inserisci l'anno di pubblicazione da cercare: ");
            int anno = scanner.nextInt();
            scanner.nextLine(); // Consuma la newline
            List<ElementoCatalogo> elementi = elementoDao.findByAnnoPubblicazione(anno);
            if (!elementi.isEmpty()) {
                System.out.println("Elementi pubblicati nel " + anno + ":");
                elementi.forEach(System.out::println);
            } else {
                System.out.println("Nessun elemento trovato pubblicato nel " + anno + ".");
            }
        }

        private static void ricercaPerAutore () {
            System.out.print("Inserisci l'autore da cercare: ");
            String autore = scanner.nextLine();
            List<Libro> libri = elementoDao.findByAutore(autore);
            if (!libri.isEmpty()) {
                System.out.println("Libri di " + autore + ":");
                libri.forEach(System.out::println); // stampa libri per ricerca autore
            } else {
                System.out.println("Nessun libro trovato con autore " + autore + ".");
            }
        }

        private static void ricercaPerTitolo () {
            System.out.print("Inserisci il titolo o parte del titolo da cercare: ");
            String titolo = scanner.nextLine();
            List<ElementoCatalogo> elementi = elementoDao.findByTitolo(titolo);
            if (!elementi.isEmpty()) {
                System.out.println("Elementi con titolo contenente '" + titolo + "':");
                elementi.forEach(System.out::println);
            } else {
                System.out.println("Nessun elemento trovato con titolo contenente '" + titolo + "'.");
            }
        }

        private static void ricercaPrestitiAttualiPerTessera () {
            System.out.print("Inserisci il numero di tessera dell'utente: ");
            String numeroTessera = scanner.nextLine();
            Optional<Utente> utente = utenteDao.findByNumeroTessera(numeroTessera);
            utente.ifPresentOrElse(u -> {
                List<Prestito> prestitiAttuali = prestitoDao.findPrestitiAttuali(u);
                if (!prestitiAttuali.isEmpty()) {
                    System.out.println("Prestiti attuali per l'utente " + u.getNome() + " " + u.getCognome() + " (" + u.getNumeroTessera() + "):");
                    for (Prestito prestito : prestitiAttuali) {
                        ElementoCatalogo elemento = null;
                        if (prestito.getElementoPrestatoTipo().equals("Libro")) {
                            Optional<ElementoCatalogo> libroOpt = elementoDao.findById(prestito.getElementoPrestatoId());
                            elemento = libroOpt.orElse(null);
                        } else if (prestito.getElementoPrestatoTipo().equals("Rivista")) {
                            Optional<ElementoCatalogo> rivistaOpt = elementoDao.findById(prestito.getElementoPrestatoId());
                            elemento = rivistaOpt.orElse(null);
                        }

                        System.out.println("  ID Prestito: " + prestito.getId() +
                                ", Titolo: " + (elemento != null ? elemento.getTitolo() : "Sconosciuto") +
                                ", ISBN/ISSN: " + (elemento != null ? elemento.getIsbn() : "Sconosciuto") +
                                ", Inizio: " + prestito.getDataInizioPrestito() +
                                ", Scadenza: " + prestito.getDataRestituzionePrevista());
                    }
                } else {
                    System.out.println("Nessun prestito attuale trovato per l'utente " + u.getNome() + " " + u.getCognome() + ".");
                }
            }, () -> System.out.println("Nessun utente trovato con il numero di tessera " + numeroTessera + "."));
        }

        private static void ricercaPrestitiScaduti () {
            List<Prestito> prestitiScaduti = prestitoDao.findPrestitiScaduti();
            if (!prestitiScaduti.isEmpty()) {
                System.out.println("Prestiti scaduti non ancora restituiti:");
                prestitiScaduti.forEach(System.out::println);
            } else {
                System.out.println("Nessun prestito scaduto non ancora restituito trovato.");
            }
        }
    }

