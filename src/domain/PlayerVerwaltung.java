package domain;

import domain.Persistence.FilePersistenceManager;
import domain.Persistence.PersistenceManager;
import domain.exceptions.PlayerAlreadyExistsException;
import valueobjects.Player;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 06.04.2017.
 */
public class PlayerVerwaltung {

    private PersistenceManager pm = new FilePersistenceManager ( );
    private List < Player > playerList = new Vector < Player > ( );

    public void createPlayer ( int newPlayerID, String newPlayerName ) throws PlayerAlreadyExistsException {
        Player newPlayer = new Player ( newPlayerID, newPlayerName );
        if ( playerList.contains ( newPlayer ) )
            throw new PlayerAlreadyExistsException ( newPlayerName );
        playerList.add ( newPlayer );
    }

    public List < Player > getPlayerList ( ) {
        return playerList;
    }

    public void readData ( String file ) throws IOException {
        pm.openForReading ( file );

        Player player;
        do {
            player = pm.loadPlayer ( );
            if ( player != null ) {
                playerList.add ( player );
            }
        } while ( player != null );

        pm.close ();
    }

    public void writeData (  ) throws IOException{
        pm.openForWriting ( "PlayerList.txt" );

        for ( Player player : playerList ){
            pm.savePlayer ( player );
        }

        pm.close ();
    }


//check if this is correct!


}
