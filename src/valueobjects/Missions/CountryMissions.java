package valueobjects.Missions;

import domain.WorldVerwaltung;
import valueobjects.*;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;


public class CountryMissions extends Mission implements Serializable {

    int countryCount;
    int forces;

    public CountryMissions(Player player, String description, int id, int countryCount, int forces) {
        super();
        this.player = player;
        this.description = description;
        this.id = id;
        this.countryCount = countryCount;
        this.forces = forces;
    }


    @Override
    public boolean isFulfilled(Player player, List<Player> playerList, Vector<Continent> continentList) {

        int counter = 0;

        for (Continent c : continentList) {
            for (Country co : c.getContinentCountries()) {
                if (co.getOwningPlayer().equals(player) && co.getLocalForces() >= forces) {
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
