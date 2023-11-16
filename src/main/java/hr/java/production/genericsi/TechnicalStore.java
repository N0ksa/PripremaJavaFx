package hr.java.production.genericsi;

import hr.java.production.model.Item;
import hr.java.production.model.Store;
import hr.java.production.model.Technical;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Klasa predstavlja tehničku trgovinu koja proširuje osnovnu klasu trgovine.
 * Koristi generički tip <T extends Technical> kako bi označila da se može
 * koristiti samo s tipovima koji proširuju ili implementiraju klasu Technical.
 *
 * @param <T> Tip tehničkih predmeta koji se prodaju u trgovini.
 */
public class TechnicalStore <T extends Technical> extends Store implements Serializable {

    private List<T> technicalItems;


    /**
     * Konstruktor za stvaranje instance tehničke trgovine.
     * @param name Ime trgovine.
     * @param webAddress Web adresa trgovine.
     * @param items Niz artikala dostupnih u trgovini.
     */
    public TechnicalStore(Long id, String name, String webAddress, Set<Item> items) {
        super(id, name, webAddress, items);
        this.technicalItems = new ArrayList<>();
    }

    public List<T> getTechnicalItems() {
        return technicalItems;
    }

    public void setTechnicalItems(List<T> technicalItems) {
        this.technicalItems = technicalItems;
    }


    /**
     * Dodaje tehnički (<code>Technical</code>) predmet u listu tehničkih predmeta trgovine.
     * @param technicalItem Tehnički (<code>Technical</code>) predmet koji se dodaje u trgovinu.
     */
    public void addTechnicalItem(T technicalItem){
        technicalItems.add(technicalItem);
    }

    @Override
    public List<Item> sortItemsByVolume(){
        return technicalItems.stream()
                .map(item -> (Item) item)
                .sorted(Comparator.comparing(Item::getVolumeOfItem))
                .collect(Collectors.toList());
    }

    @Override
    public Integer getNumberOfItemsInStore(){
        return technicalItems.size();
    }
}
