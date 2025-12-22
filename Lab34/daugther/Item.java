package daugther;

import enums.ItemStatus;
import enums.PersonStatus;
import exception.NotFound;
import parents.Object;
import daugther.Location;

public class Item extends Object {
    protected boolean isMineralFound = false;
    public Item(String name, ItemStatus itemStatus, PersonStatus pstat) {
        super(name, itemStatus, pstat);
    }

    public void isFound() throws NotFound {
            if (this.getCondition() == ItemStatus.LUNIT) {
                System.out.println(this.getName() + " найден!!");
                this.isMineralFound = true;
            } else {
                this.isMineralFound = false;
                throw new NotFound(this.getName() + " не найден");
            }
    }

    public void goTo(Location l) {
        setPersonStat(PersonStatus.MOVE);
        System.out.println(this.getName() + " направляется на " + l);
    }

    public void doThis(String a) {
        setPersonStat(PersonStatus.DOING);
        System.out.println(this.getName() + " " + a);
    }
}


