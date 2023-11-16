package hr.java.production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Predstavlja artikl sa svojim karakteristikama i cijenama.
 */
public class Item extends NamedEntity implements Serializable {
    private Category category;
    private BigDecimal width;

    BigDecimal weight;
    private BigDecimal height;
    private BigDecimal length;
    private BigDecimal productionCost;
    private BigDecimal sellingPrice;
    private Discount discount;

    /**
     * Konstruktor za stvaranje instance artikla.
     * @param name Ime artikla.
     * @param category Kategorija artikla.
     * @param width Širina artikla (cm).
     * @param height Visina artikla (cm).
     * @param length Duljina artikla (cm).
     * @param productionCost Cijena proizvodnje artikla u eurima.
     * @param sellingPrice Prodajna cijena artikla u eurima.
     * @param discount Popust na artiklu u postotcima.
     */
    public Item(Long id, String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                BigDecimal productionCost, BigDecimal sellingPrice, Discount discount) {
        super(id, name);
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
    }
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(BigDecimal productionCost) {
        this.productionCost = productionCost;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }


    /**
     * Služi za izračun volumena (veličine) artikla.
     * @return BigDecimal - volumen artikla.
     */
    public BigDecimal getVolumeOfItem (){
        return   getHeight()
                .multiply(getLength())
                .multiply(getWidth());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(category, item.category)
                && Objects.equals(width, item.width)
                && Objects.equals(height, item.height)
                && Objects.equals(length, item.length)
                && Objects.equals(productionCost, item.productionCost)
                && Objects.equals(sellingPrice, item.sellingPrice)
                && Objects.equals(discount, item.discount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, width, height, length, productionCost, sellingPrice, discount);
    }


    @Override
    public String toString() {
       return "Ime artikla: " + getName() + " Kategorija: " + getCategory().getName() + " Popust:" + getDiscount().discountAmount();
    }
}
