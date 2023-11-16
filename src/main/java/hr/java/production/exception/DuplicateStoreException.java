package hr.java.production.exception;

/**
 * Baci se kada postoji duplicirana trgovina.
 */
public class DuplicateStoreException extends RuntimeException{
    public DuplicateStoreException() {
    }

    public DuplicateStoreException(String message) {
        super(message);
    }

    public DuplicateStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateStoreException(Throwable cause) {
        super(cause);
    }
}
