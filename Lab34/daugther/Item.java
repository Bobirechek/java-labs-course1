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
                System.out.println(this.getName() + " найден!!\nВсех найдем и спасем");
                this.isMineralFound = true;
            } else {
                throw new NotFound(this.getName() + " не найден");
        }
    }

    public void goTo(Location l) {
        String res;
        if (this.getPersonStat() == PersonStatus.SIMPLE ) {
            res = " направляется на "; 
        } else {
            res = " направляются на ";
        }
        // setCondition(ItemStatus.DESTROYED);
        System.out.println(this.getName() + res + l);
    }

    public void doThis(String a) {
        // setCondition(ItemStatus.DESTROYED);
        System.out.println(this.getName() + " " + a);
    }
}
