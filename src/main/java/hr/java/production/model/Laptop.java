package hr.java.production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * Predstavlja laptop kao tehnički artikl sa specifičnim jamstvom.
 */
public final class Laptop extends Item implements Technical, Serializable {
    private Integer warantyDurationInMonths;

    /**
     * Konstruktor za stvaranje instance laptopa.
     * @param name Ime laptopa.
     * @param category Kategorija laptopa.
     * @param width  Širina laptopa (cm).
     * @param height Visina laptopa (cm).
     * @param length Duljina laptopa (cm).
     * @param productionCost Cijena proizvodnje laptopa u eurima.
     * @param sellingPrice Prodajna cijena laptopa u eurima.
     * @param discount Popust na laptopu u postotcima.
     * @param warrantyDurationInMonths Trajanje jamstva za laptop u mjesecima.
     */
    public Laptop(Long id, String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                  BigDecimal productionCost, BigDecimal sellingPrice, Discount discount, Integer warrantyDurationInMonths) {
        super(id, name, category, width, height, length, productionCost, sellingPrice, discount);
        this.warantyDurationInMonths = warrantyDurationInMonths;
    }

    /**
     * Konstruktor za stvaranje instance laptopa.
     * @param basicItem Osnovni artikl.
     * @param itemWarranty Trajanje jamstva za laptop u mjesecima.
     */
    public Laptop(Item basicItem, Integer itemWarranty) {
        super(basicItem.getId(), basicItem.getName(), basicItem.getCategory(), basicItem.getWidth(),
                basicItem.getHeight(), basicItem.getLength(), basicItem.getProductionCost(), basicItem.getSellingPrice(),
                basicItem.getDiscount());

        this.warantyDurationInMonths = itemWarranty;
    }


    public void setWarantyDurationInMonths(Integer warantyDurationInMonths) {
        this.warantyDurationInMonths = warantyDurationInMonths;
    }

    @Override
    public Integer getWarrantyDurationInMonths() {
        return warantyDurationInMonths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Laptop laptop = (Laptop) o;
        return Objects.equals(warantyDurationInMonths, laptop.warantyDurationInMonths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), warantyDurationInMonths);
    }
}
