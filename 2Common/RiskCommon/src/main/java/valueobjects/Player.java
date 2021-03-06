package valueobjects;

import java.io.Serializable;
import java.rmi.Remote;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 23.03.2017.
 */
public class Player implements Serializable, Remote {

    private static final long serialVersionUID = -787945632L;

    private int playerID;
    private String playerName;

    public Player(int newPlayerID, String newPlayerName) {
        this.playerID = newPlayerID;
        this.playerName = newPlayerName;

    }


    public String getPlayerName() {
        return this.playerName;
    }



    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            Player otherPlayer = (Player) obj;
            return this.getPlayerName().equals(otherPlayer.getPlayerName());
        }
        return false;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
