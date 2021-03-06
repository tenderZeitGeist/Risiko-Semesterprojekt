package events;

import valueobjects.Turn;


public class GameControlEvent extends GameEvent {

    private static final long serialVersionUID = 4833660998427328149L;

    public enum GameControlEventType {GAME_STARTED, GAME_SAVED, GAME_LOADED, NEXT_TURN, GAME_OVER}

    private GameControlEventType type;
    private Turn turn;

    public GameControlEvent(Turn turn, GameControlEventType type) {
        // GAME_STARTED: Spieler beginnt
        // GAME_OVER: Spieler hat gewonnen
        // NEXT_PLAYER: Spieler ist dran
        super(turn.getPlayer());

        this.type = type;
        this.turn = turn;
    }

    public GameControlEventType getType() {
        return type;
    }

    public Turn getTurn() {
        return turn;
    }
}
