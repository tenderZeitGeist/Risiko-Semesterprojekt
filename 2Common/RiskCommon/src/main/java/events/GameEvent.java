package events;


import valueobjects.Player;

import java.io.Serializable;
import java.rmi.Remote;

public abstract class GameEvent implements Serializable, Remote {

	private static final long serialVersionUID = -5234940566773150168L;
	
	private Player player;

	public GameEvent(Player player) {
		super();
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
}
