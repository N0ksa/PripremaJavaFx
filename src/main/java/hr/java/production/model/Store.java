package hr.java.production.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Predstavlja trgovinu sa svojim atributima, uključujući web adresu i artikle.
 */
public class Store extends NamedEntity implements Serializable {
    private String webAddress;
    private Set<Item> items;

    /**
     * Konstruktor za stvaranje instance trgovine.
     * @param name Ime trgovine.
     * @param webAddress Web adresa trgovine.
     * @param items Niz artikala dostupnih u trgovini.
     */
    public Store(Long id, String name, String webAddress, Set<Item> items) {
        super(id, name);
        this.webAddress = webAddress;
        this.items = items;
    }
    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }


    /**
     * Pronalazi i vraća artikl s najnižom prodajnom cijenom u trgovini.
     * @return Item - artikl s najnižom prodajnom cijenom.
     */
    public Item getItemWithCheapestPrice() {

        return items.stream()
                .min(Comparator.comparing(Item::getSellingPrice))
                .orElse(null);
    }

    /**
     * Pronalazi i vraća artikl s najvećom prodajnom cijenom u trgovini.
     * @return Item - artikl s najvećom prodajnom cijenom.
     */
    public Item getItemWithHighestPrice() {

        return items.stream()
                .max(Comparator.comparing(Item::getSellingPrice))
                .orElse(null);

    }

    /**
     * Sortira listu artikala prema volumenu - uzlazno.
     * @return List - sortirana lista artikala prema njihovom volumenu - uzlazno.
     */
    public List<Item> sortItemsByVolume(){
        return items.stream()
                .sorted((item1, item2) -> item1.getVolumeOfItem().compareTo(item2.getVolumeOfItem()))
                .collect(Collectors.toList());
    }


    /**
     * Vraća broj artikala u trgovini.
     * @return Integer - broj artikala u trgovini.
     */
    public Integer getNumberOfItemsInStore(){
        return items.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Objects.equals(name.toLowerCase(), store.name.toLowerCase())
                && Objects.equals(webAddress.toLowerCase(), store.webAddress.toLowerCase())
                && Objects.equals(items, store.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(webAddress, items);
    }

    @Override
    public String toString() {
        return getName();
    }
}
