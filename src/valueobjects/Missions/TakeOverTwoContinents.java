package valueobjects.Missions;

import valueobjects.Continent;
import valueobjects.Country;
import valueobjects.Mission;
import valueobjects.Player;

import java.awt.datatransfer.MimeTypeParseException;
import java.util.List;
import java.util.Vector;


public class TakeOverTwoContinents extends Mission {

    private int continentIDOne;
    private int continentIDTwo;

    public TakeOverTwoContinents(Player player, String description, int id, int continentIDOne, int continentIDTwo) {
        super(player, description, id);
        this.continentIDOne = continentIDOne;
        this.continentIDTwo = continentIDTwo;
    }


    @Override
    public boolean isFulfilled(Player player, List<Player> playerList, Vector<Continent> continentList) {

        int continentOne = continentList.get(continentIDOne-1).getContinentCountries().size();
        int continentTwo = continentList.get(continentIDTwo-1).getContinentCountries().size();
        int counter = 0;

        for (Continent c : continentList) {
            for (Country co : c.getContinentCountries()) {
                if (co.getOwningPlayer().equals(player) && (co.getContinentID() == continentIDOne || co.getContinentID() == continentIDTwo)) {
                    counter++;
                }
            }
        }
        return counter >= continentOne + continentTwo;
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

