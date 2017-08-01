package domain.events;


import valueobjects.Player;

import java.io.Serializable;

public abstract class GameEvent implements Serializable {

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
