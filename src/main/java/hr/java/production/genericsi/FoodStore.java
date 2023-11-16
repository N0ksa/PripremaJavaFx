package hr.java.production.genericsi;

import hr.java.production.model.Edible;
import hr.java.production.model.Item;
import hr.java.production.model.Store;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Klasa predstavlja trgovinu prehrambenih proizvoda koja proširuje osnovnu klasu trgovine.
 * Koristi generički tip <T extends Edible> kako bi označila da se može
 * koristiti samo s tipovima koji proširuju ili implementiraju klasu Edible.
 *
 * @param <T> Tip jestivih predmeta koji se prodaju u trgovini.
 */
public class FoodStore <T extends Edible> extends Store implements Serializable {

    private List<T> edibleItems;


    /**
     * Konstruktor za stvaranje instance trgovine.
     * @param name Ime trgovine.
     * @param webAddress Web adresa trgovine.
     * @param items Niz artikala dostupnih u trgovini.
     */
    public FoodStore(Long id, String name, String webAddress, Set<Item> items) {
        super(id, name, webAddress, items);
        this.edibleItems = new ArrayList<>();
    }

    public List<T> getEdibleItems() {
        return edibleItems;
    }

    public void setEdibleItems(List<T> edibleItems) {
        this.edibleItems = edibleItems;
    }

    /**
     * Dodaje jestiv (<code>Edible</code>) artikl u listu jestivih artikala trgovine.
     * @param edibleItem Jestiv (<code>Edible</code>) predmet koji se dodaje u trgovinu.
     */
    public void addEdibleItem(T edibleItem){
        edibleItems.add(edibleItem);
    }

    @Override
    public List<Item> sortItemsByVolume(){
        return edibleItems.stream()
                .map(item -> (Item) item)
                .sorted(Comparator.comparing(Item::getVolumeOfItem))
                .collect(Collectors.toList());

    }

    @Override
    public Integer getNumberOfItemsInStore(){
        return edibleItems.size();
    }

}
