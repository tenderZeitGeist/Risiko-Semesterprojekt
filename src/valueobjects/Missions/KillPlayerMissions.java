package valueobjects.Missions;

import valueobjects.Continent;
import valueobjects.Country;
import valueobjects.Mission;
import valueobjects.Player;

import java.util.List;
import java.util.Vector;

/**
 * Created by Felda on 25.05.2017.
 */
public class KillPlayerMissions extends Mission {

    int playerToKillID;

    public KillPlayerMissions(Player player, String description, int id, int playerToKillID) {
        super(player, description, id);
        this.playerToKillID = playerToKillID;
    }


    @Override
    public boolean isFulfilled(Player player, List<Player> playerList, Vector<Continent> continentList) {

        int counter = 0;

        for (Continent c : continentList) {
            for (Country co : c.getContinentCountries()) {
                if (co.getOwningPlayer().getPlayerID() == playerToKillID) {
                    counter++;
                }
            }
        }
        if (counter > 0) {
            return true;
        }
        return false;
    }


    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void setPlayer(Player p) {
        this.player = p;
    }


}
