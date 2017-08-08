package exceptions;

/**
 * Created by Christopher on 27.04.2017.
 */
public class IntegerTooLargeOrTooSmallException extends Exception {

    public IntegerTooLargeOrTooSmallException() {
        super("Integer too large or too small");
    }
}
