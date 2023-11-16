package hr.java.production.model;

/**
 * Sučelje koje predstavlja tehničke artikle i omogućuje dobivanje trajanja jamstva u mjesecima.
 */
public sealed interface Technical permits Laptop {

    /**
     * Služi za dobivanje trajanja jamstvenog roka.
     * @return Integer - duljina trajanja jamstvenog roka.
     */
    public default Integer getWarrantyDurationInMonths(){
        return 12;
    };
}
