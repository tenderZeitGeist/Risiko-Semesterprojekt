/*
package valueobjects.Missions;

import domain.WorldVerwaltung;
import valueobjects.*;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;




public class KillPlayerOne extends Mission {

    public KillPlayerOne(Player player, List<Player> playerList) {
        super(player, playerList,"Conquer 24 countries of your choice!");

    }


    @Override
    public boolean isFulfilled(Vector < Continent > continentVector, Player player, List<Player> playerList) {

        int counter=0;

        for (int a=0; a < continentVector.size(); a++) {
            for (int i=0; i < continentVector.get(a).getContinentCountries().size(); i++) {
                if (continentVector.get(a).getContinentCountries().get(i).getOwningPlayer().equals(enemy)) {
                    counter++;
                }
            }
        }
        if ( counter > 0  ) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void setPlayer( Player p ) {
        this.player = p;
    }


}
*/
