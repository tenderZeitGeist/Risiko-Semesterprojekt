package valueobjects.Missions;

import valueobjects.Continent;
import valueobjects.Country;
import valueobjects.Mission;
import valueobjects.Player;

import java.awt.datatransfer.MimeTypeParseException;
import java.util.List;
import java.util.Vector;

public class TakeOverThreeContinents extends Mission {

    private int continentIDOne;
    private int continentIDTwo;

    public TakeOverThreeContinents(Player player, String description, int id, int continentIDOne, int continentIDTwo) {
        super(player, description, id);
        this.continentIDOne = continentIDOne;
        this.continentIDTwo = continentIDTwo;
    }

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
        return counter >= continentOne + continentTwo && thirdContinent(continentList);
    }

    public boolean thirdContinent(Vector<Continent> continentList) {

        Vector<Country> countryList = new Vector<>();
        int northAmerica = 0;
        int southAmerica = 0;
        int europe = 0;
        int africa = 0;
        int asia = 0;
        int australia = 0;

        for (Continent c : continentList) {
            for (Country co : c.getContinentCountries()) {
                if (co.getOwningPlayer().equals(player) && !(co.getContinentID() == continentIDOne || co.getContinentID() == continentIDTwo)) {
                    countryList.add(co);
                }
            }
        }
        for (Country c : countryList) {

            switch (c.getContinentID()) {
                case 1:
                    northAmerica++;
                    break;
                case 2:
                    southAmerica++;
                    break;
                case 3:
                    europe++;
                    break;
                case 4:
                    africa++;
                    break;
                case 5:
                    asia++;
                    break;
                case 6:
                    australia++;
                    break;
                default:
                    break;
            }
        }
        if (northAmerica == continentList.get(0).getContinentCountries().size()) {
            return true;
        }
        if (southAmerica == continentList.get(1).getContinentCountries().size()) {
            return true;
        }
        if (europe == continentList.get(2).getContinentCountries().size()) {
            return true;
        }
        if (africa == continentList.get(3).getContinentCountries().size()) {
            return true;
        }
        if (asia == continentList.get(4).getContinentCountries().size()) {
            return true;
        }
        if (australia == continentList.get(5).getContinentCountries().size()) {
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
