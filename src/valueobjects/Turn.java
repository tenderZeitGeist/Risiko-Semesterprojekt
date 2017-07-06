package valueobjects;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 06.07.2017.
 */
public class Turn {
    Player currentPlayer;
    Phase phase;


    public enum Phase {
        DISTRIBUTE,
        ATTACK,
        MOVE
    }

    public Turn(Player player){
        phase = Phase.DISTRIBUTE;
        currentPlayer = player;
    }

    public Phase getPhase() {
        return phase;
    }

    public void nextPhase() {
        switch (phase){
            case DISTRIBUTE:
                phase = Phase.ATTACK;
                break;
            case ATTACK:
                phase = Phase.MOVE;
                break;
        }
    }




}
