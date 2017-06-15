package domain.Persistence;

import valueobjects.*;

import java.io.IOException;


/**
 * Created by Felda on 16.05.2017.
 */
public interface PersistenceManager {


    /**
     * @author teschke
     * <p>
     * Allgemeine Schnittstelle für den Zugriff auf ein Speichermedium
     * (z.B. Datei oder Datenbank) zum Ablegen von beispielsweise
     * Bücher- oder Kundendaten.
     * <p>
     * Das Interface muss von Klassen implementiert werden, die eine
     * Persistenz-Schnittstelle realisieren wollen.
     */


    public void openForReading(String datenquelle) throws IOException;

    public void openForWriting(String datenquelle) throws IOException;

    public boolean close();

    /**
     * Methode zum Einlesen der Buchdaten aus einer externen Datenquelle.
     *
     * @return Buch-Objekt, wenn Einlesen erfolgreich, false null
     */
    public Country loadCountry() throws IOException;

    /**
     * Methode zum Schreiben der Buchdaten in eine externe Datenquelle.
     *
     * @param b Buch-Objekt, das gespeichert werden soll
     * @return true , wenn Schreibvorgang erfolgreich, false sonst
     */
    //public boolean saveCountry ( Country country ) throws IOException;

    /*
     *  Wenn später mal eine Kundenverwaltung ergänzt wird:

    public Kunde ladeKunde() throws IOException;

    public boolean speichereKunde(Kunde k) throws IOException;

    */
    public Card loadCard() throws IOException;

    public boolean saveCard(Card card) throws IOException;

    //public Player loadPlayer () throws IOException;

    //public boolean savePlayer ( Player player ) throws IOException;

    //public boolean saveMission (Mission mission) throws IOException;
}