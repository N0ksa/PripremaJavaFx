package hr.java.production.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Predstavlja kategoriju koja može biti dodijeljena artiklima.
 */
public class Category extends NamedEntity implements Serializable {
    private String description;

    /**
     * Konstruktor za stvaranje instance kategorije.
     * @param name Ime kategorije.
     * @param description Opis kategorije.
     */
    public Category(Long id, String name, String description) {
        super(id, name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name.toLowerCase(), category.name.toLowerCase())
                && Objects.equals(description.toLowerCase(), category.description.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return getName();
    }
}
