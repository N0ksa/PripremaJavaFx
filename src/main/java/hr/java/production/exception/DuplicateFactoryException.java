package hr.java.production.exception;

/**
 * Baci se kada postoji duplicirana tvornica.
 */
public class DuplicateFactoryException extends RuntimeException{
    public DuplicateFactoryException() {
    }

    public DuplicateFactoryException(String message) {
        super(message);
    }

    public DuplicateFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateFactoryException(Throwable cause) {
        super(cause);
    }
}
