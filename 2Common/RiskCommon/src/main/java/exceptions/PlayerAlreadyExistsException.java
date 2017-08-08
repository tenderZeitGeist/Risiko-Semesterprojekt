package exceptions;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 06.04.2017.
 */
public class PlayerAlreadyExistsException extends Exception {

    public PlayerAlreadyExistsException(String name) {
        super("Ein Spieler mit dem Namen " + name + " existiert bereits.");
    }
}
