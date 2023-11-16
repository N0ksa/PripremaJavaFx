package hr.java.production.enums;

/**
 * Predstavlja maksimalna ograničenja za različite kategorije.
 */
public enum MaxLimit {
     NUMBER_OF_CATEGORIES (3),
     NUMBER_OF_ITEMS (5),
     NUMBER_OF_FACTORIES (2),
     NUMBER_OF_STORES(2);

     private Integer maxNumber;
     MaxLimit(Integer maxNumber){
         this.maxNumber = maxNumber;
     }

    public Integer getMaxNumber() {
        return maxNumber;
    }
}
