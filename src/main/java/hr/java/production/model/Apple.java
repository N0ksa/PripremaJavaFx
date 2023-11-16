package hr.java.production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * Predstavlja jabuku kao jestivi artikal.
 */
public final class Apple extends Item implements Edible, Serializable {

    public static final Integer CALORIES_PER_KILOGRAM = 521;
    private BigDecimal weight;

    /**
     * Konstruktor za stvaranje instance jabuke.
     * @param name Sorta jabuke.
     * @param category Kategorija jabuke.
     * @param width Širina jabuke (cm).
     * @param height Visina jabuke (cm).
     * @param length Duljina jabuke (cm).
     * @param productionCost Cijena proizvodnje jabuke u eurima.
     * @param sellingPrice Prodajna cijena jabuke u eurima.
     * @param discountAmount Popust na jabuku u postotcima.
     * @param weight Težina jabuke u kilogramima.
     */
    public Apple(Long id, String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                 BigDecimal productionCost, BigDecimal sellingPrice, Discount discountAmount, BigDecimal weight) {
        super(id, name, category, width, height, length, productionCost, sellingPrice, discountAmount);
        this.weight = weight;
    }


    /**
     * Konstuktor za stvaranje instance jabuke.
     * @param basicItem Osnovni artikl.
     * @param weight Težina jabuke u kilogramima.
     */
    public Apple(Item basicItem, BigDecimal weight) {
        super(basicItem.getId(), basicItem.getName(), basicItem.getCategory(), basicItem.getWidth(),
                basicItem.getHeight(), basicItem.getLength(), basicItem.getProductionCost(), basicItem.getSellingPrice(),
                basicItem.getDiscount());

        this.weight = weight;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }


    @Override
    public Integer calculateKilocalories() {
        BigDecimal caloriesPerKilogram = new BigDecimal(CALORIES_PER_KILOGRAM);
        BigDecimal overallCalories = caloriesPerKilogram.multiply(weight);

        return  overallCalories.intValue();
    }

    @Override
    public BigDecimal calculatePrice() {
        Discount discount = getDiscount();
        BigDecimal pricePerKilogram = getSellingPrice();
        BigDecimal weightOfItem = getWeight();

        BigDecimal discountFraction = new BigDecimal(discount.discountAmount()).divide(new BigDecimal(100));
        BigDecimal discountedPricePerKilogram = pricePerKilogram.multiply(BigDecimal.ONE.subtract(discountFraction));

        return discountedPricePerKilogram.multiply(weightOfItem);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Apple apple = (Apple) o;
        return Objects.equals(weight, apple.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), weight);
    }
}

