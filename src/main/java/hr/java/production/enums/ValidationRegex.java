package hr.java.production.enums;

/**
 * Predstavlja regularne izraze za provjeru valjanosti.
 */
public enum ValidationRegex {
    VALID_WEB_ADDRESS("www\\.[A-Za-z0-9]+\\.[A-Za-z]+"),
    VALID_POSTAL_CODE("[0-9]+");

    private final String regex;

    ValidationRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}