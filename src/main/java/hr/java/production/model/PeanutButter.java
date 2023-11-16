package hr.java.production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * Predstavlja kikiriki maslac kao jestiv artikl.
 */
public class PeanutButter extends Item implements Edible, Serializable {
    public static final Integer CALORIES_PER_KILOGRAM = 5970;
    private BigDecimal weight;

    /**
     * Konstruktor za stvaranje instance kikiriki maslaca.
     * @param name Ime kikiriki maslaca.
     * @param category Kategorija kikiriki maslaca.
     * @param width Širina kikiriki maslaca (cm).
     * @param height Visina kikiriki maslaca (cm).
     * @param length Duljina kikiriki maslaca (cm).
     * @param productionCost Cijena proizvodnje kikiriki maslaca u eurima.
     * @param sellingPrice Prodajna cijena kikiriki maslaca u eurima.
     * @param discountAmount Popust na kikiriki maslacu u postotcima.
     * @param weight Težina kikiriki maslaca u kilogramima.
     */
    public PeanutButter(Long id, String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                        BigDecimal productionCost, BigDecimal sellingPrice, Discount discountAmount, BigDecimal weight) {
        super(id, name, category, width, height, length, productionCost, sellingPrice, discountAmount);
        this.weight = weight;
    }

    /**
     * Konstruktor za stvaranje instance kikiriki maslaca.
     * @param basicItem Osnovni artikl.
     * @param weight Težina kikiriki maslaca u kilogramima.
     */
    public PeanutButter(Item basicItem, BigDecimal weight) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PeanutButter that = (PeanutButter) o;
        return Objects.equals(weight, that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), weight);
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

}
