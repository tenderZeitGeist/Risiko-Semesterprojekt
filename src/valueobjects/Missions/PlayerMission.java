package valueobjects.Missions;

import valueobjects.Continent;
import valueobjects.Country;
import valueobjects.Mission;
import valueobjects.Player;

import java.util.List;
import java.util.Vector;

/**
 * Created by ZeitGeist on 24.05.2017.
 */
public class PlayerMission extends Mission {

    private int playerID;

    public PlayerMission ( Player player, String description, int id, int playerID ) {
        super ();

    }

    @Override
    public boolean isFulfilled ( Player player, List < Player > playerList, Vector < Continent > continentList ) {
        for ( Player p : playerList ) {
            if ( p.getPlayerID ( ) == playerID ) {
                player = p;
            }
        }
        for ( Continent currentContinent : continentList ) {
            for ( Country currentCountry : currentContinent.getContinentCountries ( ) ) {
                if ( player.equals ( currentCountry.getOwningPlayer ( ) ) ) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Player getPlayer ( ) {
        return this.player;
    }

    @Override
    public void setPlayer ( Player p ) {
        this.player = p;
    }
}
