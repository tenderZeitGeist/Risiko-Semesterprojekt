/*
package valueobjects.Missions;

import valueobjects.Continent;
import valueobjects.Country;
import valueobjects.Mission;
import valueobjects.Player;

import java.awt.datatransfer.MimeTypeParseException;
import java.util.List;
import java.util.Vector;

*/
/**
 * Created by Christopher on 12.05.2017.
 *//*

public class ContinentMissions extends Mission {

    int countryCount;
    int forces;

    public ContinentMissions(Player player, String description, int id, int countryCount, int forces) {
        super(player, description, id);
        this.countryCount = countryCount;
        this.forces = forces;
    }


    @Override
    public boolean isFulfilled(Player player, List<Player> playerList, Vector<Continent> continentList) {

        int counter = 0;

        for (Continent c : continentList) {
            for (Country co : c.getContinentCountries()) {
                if (!co.getOwningPlayer().equals(player)) {
                    break;
                }
                if (co.getOwningPlayer().equals(player) && (co.getContinentID() == countryCount && co.getContinentID() == forces)) {
                    if ()
                    counter++;
                }
            }
        }
        if (counter >= countryCount) {
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

*/
