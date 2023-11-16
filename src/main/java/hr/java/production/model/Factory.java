package hr.java.production.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

/**
 * Predstavlja tvornicu koja proizvodi ili distribuira artikle.
 */
public class Factory extends NamedEntity implements Serializable {
    private Address address;
    private Set<Item> items;


    /**
     * Konstruktor za stvaranje instance tvornice.
     * @param name Ime tvornice.
     * @param address Adresa tvornice.
     * @param items Niz artikala proizvedenih od strane tvornice.
     */
    public Factory(Long id, String name, Address address, Set<Item> items) {
        super(id, name);
        this.address = address;
        this.items = items;
    }

    public Address getAdress() {
        return address;
    }

    public void setAdress(Address address) {
        this.address = address;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factory factory = (Factory) o;
        return Objects.equals(address, factory.address) && Objects.equals(items, factory.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, items);
    }

    /**
     * Služi za izračun najvećeg artikla u tvornici.
     * @return Item - najveći artikl u tvornici.
     */
    public Item getItemWithLargestVolume() {

        return items.stream()
                .max(Comparator.comparing(Item::getVolumeOfItem))
                .orElse(null);

    }

    /**
     * Služi za izračun najmanjeg artikla u tvornici.
     * @return Item - najmanji artikl u tvornici.
     */
    public Item getItemWithSmallestVolume() {

        return items.stream()
                .min(Comparator.comparing(Item::getVolumeOfItem))
                .orElse(null);

    }
}
