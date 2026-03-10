package daugther;

import enums.ItemStatus;
import enums.PersonStatus;
import parents.Ob;

// Дочерний класс Объекта - локация, представляет какое-то место
public class Location extends Ob {
    public Location(String name, PersonStatus pstat) {
        super(name, ItemStatus.LOCATION, pstat);
    }

    @Override
    public String toString() {
        return this.getName();
    }
    public String getLocation() {
        return this.getName();
    }
}
