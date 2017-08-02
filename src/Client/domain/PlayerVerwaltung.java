package Client.domain;

import Client.domain.Persistence.FilePersistenceManager;
import Client.domain.Persistence.PersistenceManager;
import test.exceptions.PlayerAlreadyExistsException;
import valueobjects.Player;

import java.util.Vector;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 06.04.2017.
 */
public class PlayerVerwaltung {


    private PersistenceManager pm = new FilePersistenceManager();
    private Vector < Player > playerList = new Vector<Player>();
    private  Player currentPlayer;

    public void createPlayer(int newPlayerID, String newPlayerName) throws PlayerAlreadyExistsException {
        Player newPlayer = new Player(newPlayerID, newPlayerName);
        if (playerList.contains(newPlayer))
            throw new PlayerAlreadyExistsException(newPlayerName);
        playerList.add(newPlayer);
        currentPlayer = playerList.get(0);
    }

    public Vector < Player > getPlayerList() {
        return playerList;
    }

    public void setPlayerList( Vector < Player > p) {
        this.playerList = p;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setNextPlayer() {
        int nextPlayer = currentPlayer.getPlayerID() % playerList.size();
        this.currentPlayer = playerList.get(nextPlayer);
    }

    public void setPlayerIDs(){
        int playerID = 0;
        for (Player p : playerList) {
            p.setPlayerID ( playerID++ );
        }
    }
}
