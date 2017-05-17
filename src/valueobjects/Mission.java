package valueobjects;

import java.util.List;

import java.util.Vector;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 27.04.2017.
 */
public abstract class Mission {

    protected Player player;
    protected String description;
    protected int id;

    public Mission(Player player, String description, int id) {
        this.player = player;
        this.description = description;
        this.id = id;
    }

    public abstract boolean isFulfilled(Player player, List <Player> playerList, Vector <Continent> continentList);

    public abstract Player getPlayer();

    public abstract void setPlayer(Player p);

    public String getDescription() {
        return this.description;
    }
}
