package hr.java.production.filter;

import hr.java.production.model.Category;

public class ItemFilter {
    private String name;
    private Category category;


    public ItemFilter(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
