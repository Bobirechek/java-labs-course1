package models;

import java.io.Serializable;

public class Car implements Serializable {

    private String name;
    private Boolean cool;

    public Car(String name, Boolean cool) {
        this.name = name;
        this.cool = cool;
    }

    public String getName() {
        if (this.name == null) {
            return "";
        } else {
            return this.name;
        }

    }

    public Boolean getCool() {
        return this.cool;
    }
}