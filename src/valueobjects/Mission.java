package valueobjects;

import java.io.Serializable;
import java.util.List;

import java.util.Vector;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 27.04.2017.
 */
public abstract class Mission implements Serializable {

    protected Player player;
    protected String description;
    protected int id;

    public Mission() {
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

    public int getId() {
        return id;
    }
}
