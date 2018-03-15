package sample;

import java.io.Serializable;

/**
 * Created by pwilkin on 30-Nov-17.
 */
public class Grupa implements Serializable {

    protected Integer id;
    protected String name;
    protected String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
