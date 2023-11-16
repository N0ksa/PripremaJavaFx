package hr.java.production.exception;

/**
 * Baci se u slučaju kada je isti artikl već odabran.
 */
public class SameItemSelectedException extends Exception{
    public SameItemSelectedException(String message) {
        super(message);
    }

    public SameItemSelectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SameItemSelectedException(Throwable cause) {
        super(cause);
    }
}
