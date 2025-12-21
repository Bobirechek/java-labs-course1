package daugther;

import enums.ItemStatus;
import enums.PersonStatus;
import parents.Object;

public class Location extends Object {
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
