package domain;

import domain.exceptions.PlayerAlreadyExistsException;
import valueobjects.Player;

import java.util.List;
import java.util.Vector;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 06.04.2017.
 */
public class PlayerVerwaltung {

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


//check if this is correct!


}
