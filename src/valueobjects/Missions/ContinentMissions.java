package valueobjects.Missions;

import valueobjects.Continent;
import valueobjects.Country;
import valueobjects.Mission;
import valueobjects.Player;

import java.awt.datatransfer.MimeTypeParseException;
import java.util.List;
import java.util.Vector;


public class ContinentMissions extends Mission {

    int[] continentIDs;

    public ContinentMissions ( Player player, String description, int id, int[] continentIDs ) {
        super ( player, description, id );
        this.continentIDs = continentIDs;
    }


    @Override
    public boolean isFulfilled ( Player player, List < Player > playerList, Vector < Continent > continentList ) {
        for ( int n : continentIDs ) {
            for ( Country c : continentList.get ( n ).getContinentCountries ( ) ) {
                if ( ! c.getOwningPlayer ( ).equals ( player ) ) {
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

