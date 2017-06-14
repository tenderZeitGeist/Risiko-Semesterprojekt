package valueobjects.Missions;

import valueobjects.Continent;
import valueobjects.Country;
import valueobjects.Mission;
import valueobjects.Player;
import valueobjects.*;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;


public class ContinentMissions extends Mission implements Serializable {

    int[] continentIDs;

    public ContinentMissions ( Player player, String description, int id, int[] continentIDs ) {
        super ();
        this.player = player;
        this.description = description;
        this.id = id;
        this.continentIDs = continentIDs;
    }


    @Override
    public boolean isFulfilled ( Player player, List < Player > playerList, Vector < Continent > continentList ) {
        Vector < Continent > copyContinentList = new Vector <> ( continentList );

        for ( int n : continentIDs ) {
            copyContinentList.remove ( n );
        }

        for ( int n : continentIDs ) {
            if ( n == 0 ) {
                for ( Continent currentContinent : copyContinentList ) {
                    boolean owned = true;
                    for ( Country currentCountry : currentContinent.getContinentCountries ( ) ) {

                        if ( ! currentCountry.getOwningPlayer ( ).equals ( player ) ) {
                            owned = false;
                        }
                    }
                    if ( owned ) return true;
                }
                return false;
            } else {
                for ( Country c : continentList.get ( n ).getContinentCountries ( ) ) {
                    if ( ! c.getOwningPlayer ( ).equals ( player ) ) {
                        return false;
                    }
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

