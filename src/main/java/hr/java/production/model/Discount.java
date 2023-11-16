package hr.java.production.model;

import java.io.Serializable;

/**
 * Predstavlja popust na artikl.
 */
public record Discount(Integer discountAmount) implements Serializable {
}
