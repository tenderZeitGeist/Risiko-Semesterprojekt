package valueobjects;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 23.03.2017.
 */
public class Player {
    private int playerID;
    private String playerName;
    private boolean turn; //? Maybe not in this Class


    public Player(int newPlayerID, String newPlayerName, boolean newTurn) {
        this.playerID = newPlayerID;
        this.playerName = newPlayerName;
        this.turn = newTurn; //?
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
