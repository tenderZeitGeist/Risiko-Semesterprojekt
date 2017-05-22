package domain.Persistence;

import valueobjects.Country;
import valueobjects.Card;
import valueobjects.Mission;
import valueobjects.Player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Intersection on 08/05/2017.
 */
public class FilePersistenceManager implements PersistenceManager {


    /**
     * @author teschke
     * <p>
     * Realisierung einer Schnittstelle zur persistenten Speicherung von
     * Daten in Dateien.
     * @see bib.local.persistence.PersistenceManager
     */


    private BufferedReader reader = null;
    private PrintWriter writer = null;

    public void openForReading ( String file ) throws FileNotFoundException {
        reader = new BufferedReader ( new FileReader ( file ) );
    }

    public void openForWriting ( String file ) throws IOException {
        writer = new PrintWriter ( new BufferedWriter ( new FileWriter ( file ) ) );
    }

    public boolean close ( ) {
        if ( writer != null )
            writer.close ( );

        if ( reader != null ) {
            try {
                reader.close ( );
            } catch ( IOException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace ( );

                return false;
            }
        }

        return true;
    }

    /**
     * Methode zum Einlesen der Buchdaten aus einer externen Datenquelle.
     * Das Verfügbarkeitsattribut ist in der Datenquelle (Datei) als "t" oder "f"
     * codiert abgelegt.
     *
     * @return Buch-Objekt, wenn Einlesen erfolgreich, false null
     */
    public Country loadCountry ( ) throws IOException {
        String name;
        String IDString;
        String forcesString;
        String continentIDString;
        String[] integersInString;
        int ID;
        int forces;
        int continentID;

        // read country name
        name = readLine ( );
        if ( name == null ) {
            // keine Daten mehr vorhanden
            return null;
        }
        // read country ID
        IDString = readLine ( );
        //Integer id = Integer.valueOf(IDString);
        // ... und von String in int konvertieren
        ID = Integer.parseInt ( IDString );

        // read forces
        forcesString = readLine ( );
        // ... und von String in int konvertieren
        forces = Integer.parseInt ( forcesString );

        // read continentID
        continentIDString = readLine ( );
        // ... und von String in int konvertieren
        continentID = Integer.parseInt ( continentIDString );

        // read neighbours array
        integersInString = readLine ( ).split ( ", " );
        int a[] = new int[ integersInString.length ];
        for ( int i = 0 ; i < integersInString.length ; i++ ) {
            a[ i ] = Integer.parseInt ( integersInString[ i ] );
        }

        // create new country object and return it

        return new Country ( name, ID, forces, null, continentID, a );
    }


    /**
     * Methode zum Schreiben der Buchdaten in eine externe Datenquelle.
     * Das Verfügbarkeitsattribut wird in der Datenquelle (Datei) als "t" oder "f"
     * codiert abgelegt.
     *
     * @param b Buch-Objekt, das gespeichert werden soll
     * @return true , wenn Schreibvorgang erfolgreich, false sonst
     */
    public boolean saveCountry ( Country country ) throws IOException {

        // Titel, Nummer und Verfügbarkeit schreiben

        writeLine ( country.getCountryName ( ) );
        writeLine ( country.getCountryID ( ) + "" );
        writeLine ( country.getLocalForces ( ) + "" );
        writeLine ( country.getContinentID ( ) + "" );
        writeLine ( Arrays.toString ( country.getNeighbouringCountries ( ) )
                .replace ( "[", "" )
                .replace ( "]", "" ) );

        // schreibeZeile(Integer.valueOf(b.getNummer()).toString());

        return true;
    }

    public Card loadCard ( ) throws IOException {
        String idString;
        String typeString;
        String cardName;
        int id;
        int type;

        idString = readLine ( );
        if ( idString == null ) {
            return null;
        } else {
            id = Integer.parseInt ( idString );
        }

        typeString = readLine ( );
        type = Integer.parseInt ( typeString );

        cardName = readLine ( );

        return new Card ( id, type, cardName );

    }

    public boolean saveCard ( Card card ) throws IOException {

        writeLine ( card.getCardID ( ) + "" );
        writeLine ( card.getCardType ( ) + "" );
        writeLine ( card.getCardName ( ) );

        return true;
    }

    public Player loadPlayer ( ) throws IOException {
        String playerName;
        String playerIDString;
        int playerID;

        playerIDString = readLine ( );
        playerID = Integer.parseInt ( playerIDString );

        playerName = readLine ( );

        return new Player ( playerID, playerName );
    }

    public boolean savePlayer ( Player player ) throws IOException {
        writeLine ( player.getPlayerID ( ) + "" );
        writeLine ( player.getPlayerName ( ) );
        return true;
    }

/*    public Mission loadMission ( ) throws IOException {

        String missionDescription;
        String missionIDString;
        String player;
        int missionID;

        player = readLine ();

        missionDescription = readLine ();

        missionIDString = readLine ();
        missionID = Integer.parseInt ( missionIDString );

        if( missionDescription.contains ( "" ) ){

        }
        return new  ( player, missionDescription, missionID );
    }*/


    private String readLine ( ) throws IOException {
        if ( reader != null )
            return reader.readLine ( );
        else
            return "";
    }

    private void writeLine ( String daten ) {
        if ( writer != null )
            writer.println ( daten );
    }
}


