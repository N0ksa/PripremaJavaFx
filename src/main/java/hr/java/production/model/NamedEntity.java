package hr.java.production.model;

import java.io.Serializable;

/**
 * Apstraktna klasa koja predstavlja entitet s nazivom.
 */
public abstract class NamedEntity implements Serializable{
   protected String name;

   private Long id;



    public NamedEntity(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
