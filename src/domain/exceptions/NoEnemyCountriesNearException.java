package domain.exceptions;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 27.04.2017.
 */
public class NoEnemyCountriesNearException extends Exception {

    public NoEnemyCountriesNearException() {
        super("No attacking country available!");
    }
}
