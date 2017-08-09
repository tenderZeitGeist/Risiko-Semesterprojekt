package valueobjects;


import java.io.Serializable;
import java.rmi.Remote;

public class Turn implements Serializable, Remote {

    private static final long serialVersionUID = -45673384614984L;

    public enum Phase {

        PHASE1, PHASE2, PHASE3, PHASE4, PHASE5;

        public Phase next() {
            // return next phase based on ordinal value (0, 1, 2, ..., number of phases)
            // - an enum's values are stored in an array (see below: "values()")
            // - ordinal value is a phase's rank/index within the values array (see below: "ordinal()")
            return values().clone()[(this.ordinal() + 1) % values().length];
        }
    }

    ;

    private Player player;
    private Phase phase;

    public Turn(Player spieler, Phase phase) {
        this.player = spieler;
        this.phase = phase;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player newPlayer) {
        player = newPlayer;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase newPhase) {
        phase = newPhase;
    }
}
