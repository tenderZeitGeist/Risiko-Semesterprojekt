package domain.exceptions;

/**
 * Created by Christopher on 11.05.2017.
 */
public class NoAlliedCountriesNearException extends Throwable {


    public NoAlliedCountriesNearException() {

        super("No allied country near you!");
    }
}
