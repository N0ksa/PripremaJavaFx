package hr.java.production.model;

import java.math.BigDecimal;

/**
 * Sučelje koje označava da je artikl jestiv i može se koristiti za izračun kalorija i cijene.
 */
public interface Edible {

    /**
     * Izračunava ukupan broj kilokalorija za jestivi artikl.
     * @return Integer - ukupan broj kilokalorija za artikl.
     */
    public Integer calculateKilocalories();


    /**
     * Izračunava cijenu za jestivi artikl.
     * @return BigDecimal - cijena jestivog artikla kao BigDecimal vrijednost.
     */
    public BigDecimal calculatePrice();
    public BigDecimal getWeight();
}
