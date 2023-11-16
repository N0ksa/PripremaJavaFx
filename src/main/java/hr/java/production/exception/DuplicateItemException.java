package hr.java.production.exception;

/**
 * Baci se kada postoji duplicirani artikl.
 */
public class DuplicateItemException extends RuntimeException {
    public DuplicateItemException() {
    }

    public DuplicateItemException(String message) {
        super(message);
    }

    public DuplicateItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateItemException(Throwable cause) {
        super(cause);
    }
}

