package domain.exceptions;

import valueobjects.Country;

/**
 * Created by Felda on 16.05.2017.
 */

/**
 * Exception zur Signalisierung, dass ein Buch bereits existiert (z.B. bei einem Einfügevorgang).
 *
 * @author teschke
 */
public class CountryAlreadyExistsException extends Exception {

    /**
     * Konstruktor
     *
     * @param buch      Das bereits existierende Buch
     * @param zusatzMsg zusätzlicher Text für die Fehlermeldung
     */
    public CountryAlreadyExistsException(Country country, String zusatzMsg) {
        super("Buch mit Titel " + country.getCountryName() + " existiert bereits" + zusatzMsg);
    }
}
