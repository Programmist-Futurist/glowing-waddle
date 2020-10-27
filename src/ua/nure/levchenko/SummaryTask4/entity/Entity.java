package ua.nure.levchenko.SummaryTask4.entity;

import java.io.Serializable;

/**
 * Root of all entities which have identifier field
 * except of enums.
 *
 * @author K.Levchenko
 */
public abstract class Entity implements Serializable {
    private static final long serialVersionUID = -6756719204798261117L;

    private int id;

    public Entity(int id) {
        this.id = id;
    }

    public Entity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
