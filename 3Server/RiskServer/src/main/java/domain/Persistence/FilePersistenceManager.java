package domain.Persistence;

import valueobjects.Country;
import valueobjects.customCard;
import valueobjects.Player;

import java.io.*;

/**
 * Created by Intersection on 08/05/2017.
 */
public class FilePersistenceManager implements PersistenceManager {


    /**
     * Realisierung einer Schnittstelle zur persistenten Speicherung von
     * Daten in Dateien.
     */


    private BufferedReader reader = null;
    private PrintWriter writer = null;

    public void openForReading(String file) throws FileNotFoundException {

        InputStream is = getClass().getResourceAsStream(file);
        reader = new BufferedReader(new InputStreamReader(is));
    }

    public void openForWriting(String file) throws IOException {
        writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
    }

    public boolean close() {
        if (writer != null)
            writer.close();

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

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
    public Country loadCountry() throws IOException {
        String name;
        String IDString;
        String playerNameString;
        String forcesString;
        String continentIDString;
        String[] integersInString;
        int ID;
        int forces;
        int continentID;


        // read country name
        name = readLine();
        if (name.equals("###")) {
            // keine Daten mehr vorhanden
            return null;
        }

        // read country ID
        IDString = readLine();

        //Integer id = Integer.valueOf(IDString);
        // ... und von String in int konvertieren
        ID = Integer.parseInt(IDString);

        // read player
        playerNameString = readLine();

        // read forces
        forcesString = readLine();
        // ... und von String in int konvertieren
        forces = Integer.parseInt(forcesString);

        // read continentID
        continentIDString = readLine();

        // ... und von String in int konvertieren
        continentID = Integer.parseInt(continentIDString);

        // read neighbours array
        integersInString = readLine().split(", ");

        int a[] = new int[integersInString.length];
        for (int i = 0; i < integersInString.length; i++) {
            a[i] = Integer.parseInt(integersInString[i]);
        }

        String HEXvalue = readLine();

        String[] coordsString = readLine().split(", ");
        int coords[] = new int[coordsString.length];
        for (int i = 0; i < coordsString.length; i++) {
            coords[i] = Integer.parseInt(coordsString[i]);
        }

        // create new country object and return it
        if (playerNameString.contains("null")) {
            return new Country(name, ID, forces, null, continentID, a, HEXvalue, coords);
        } else {
            return new Country(name, ID, forces, new Player(0, playerNameString), continentID, a, HEXvalue, coords);
        }
    }


    /**
     * Methode zum Schreiben der Buchdaten in eine externe Datenquelle.
     * Das Verfügbarkeitsattribut wird in der Datenquelle (Datei) als "t" oder "f"
     * codiert abgelegt.
     *
     * @param
     * @return true , wenn Schreibvorgang erfolgreich, false sonst
     */
    /*public boolean saveCountry ( Country country ) throws IOException {

        // Titel, Nummer und Verfügbarkeit schreiben

        writeLine ( country.getCountryName ( ) );
        writeLine ( country.getCountryID ( ) + "" );
        if ( country.getOwningPlayer ( ) != null ) {
            writeLine ( country.getOwningPlayerName ( ) );
        } else {
            writeLine ( "null" );
        }
        writeLine ( country.getLocalForces ( ) + "" );
        writeLine ( country.getContinentID ( ) + "" );
        writeLine ( Arrays.toString ( country.getNeighbouringCountries ( ) )
                .replace ( "[", "" )
                .replace ( "]", "" ) );

        // schreibeZeile(Integer.valueOf(b.getNummer()).toString());

        return true;
    }*/
    public customCard loadCard() throws IOException {
        String idString;
        String typeString;
        String cardName;
        int id;
        int type;

        idString = readLine();
        if (idString == null) {
            return null;
        } else {
            id = Integer.parseInt(idString);
        }

        typeString = readLine();
        type = Integer.parseInt(typeString);

        cardName = readLine();

        return new customCard(id, type, cardName);

    }

    public boolean saveCard(customCard customCard) throws IOException {

        writeLine(customCard.getCardID() + "");
        writeLine(customCard.getCardType() + "");
        writeLine(customCard.getCardName());

        return true;
    }

    /*public Player loadPlayer ( ) throws IOException {
        String playerName;
        String playerIDString;
        int playerID;
        String blank="";


        do {
            playerIDString = readLine();

            if (playerIDString.length() > 0) {

                playerID = Integer.parseInt(playerIDString);

                playerName = readLine();

                return new Player(playerID, playerName);
            }
        } while (playerIDString.length()>0);
        return null;
    }

    public boolean savePlayer ( Player player ) throws IOException {
        writeLine ( player.getPlayerID ( ) + "" );
        writeLine ( player.getPlayerName ( ) );
        writeLine("");
        return true;
    }*/


    /*public Mission loadMission ( ) throws IOException {

        String missionDescription;
        String missionIDString;
        String player;
        int missionID;

        player = readLine ();

        missionDescription = readLine ();

        missionIDString = readLine ();
        missionID = Integer.parseInt ( missionIDString );

        *//*if( missionDescription.contains ( "" ) ){

        }*//*
        return new Mission ( player, missionDescription, missionID );
    }*/

    /*public boolean saveMission(Mission mission)  throws IOException {
        writeLine(mission.getPlayer().getPlayerName());
        writeLine(mission.getDescription());
        writeLine(mission.getId() + "");
        return true;
    }*/


    private String readLine() throws IOException {
        if (reader != null)
            return reader.readLine();
        else
            return "";
    }

    private void writeLine(String daten) {
        if (writer != null)
            writer.println(daten);
    }
}


