package domain;

import domain.Persistence.FilePersistenceManager;
import domain.Persistence.PersistenceManager;
import exceptions.PlayerAlreadyExistsException;
import valueobjects.Player;

import java.io.*;
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

    public void serializePlayers(Player p) throws IOException {
        Vector<Player> tempPlayerList = new Vector<>(playerList);
        Vector<Player> tempPlayerList2 = new Vector<>();
        //plist.clear();
        int playerNumber = p.getPlayerID();
        for (int i = 0; i < tempPlayerList.size(); i++) {
            if (i >= playerNumber) {
                tempPlayerList2.add(playerList.get(i));
            }
        }

        for (int i = 0; i < tempPlayerList.size(); i++) {
            if (i < playerNumber) {
                tempPlayerList2.add(playerList.get(i));
            }
        }

        try (FileOutputStream fos = new FileOutputStream("player.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Player pl : tempPlayerList2) {
                oos.writeObject(pl);
            }
        }
        tempPlayerList2.clear();
    }

    public Vector<Player> deSerializePlayers() throws IOException, ClassNotFoundException {
        Vector<Player> playerList = new Vector<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("player.ser"))) {
            while (true) {
                Player p = (Player) ois.readObject();
                //Following line stays the same
                playerList.add(p);
            }
        } catch (EOFException e) {
            e.getMessage();

        }
        return playerList;
    }
}
