package daugther;

import enums.ItemStatus;
import enums.PersonStatus;
import exception.NotFound;
import parents.Ob;
import daugther.Location;

// Класс для предметов
public class Item extends Ob {
    protected boolean isMineralFound = false;
    public Item(String name, ItemStatus itemStatus, PersonStatus pstat) {
        super(name, itemStatus, pstat);
    }

    // Метод, который может искусственно вызывать ошибку
    public void isFound() throws NotFound {
            if (this.getCondition() == ItemStatus.LUNIT) {
                System.out.println(this.getName() + " найден!!");
                this.isMineralFound = true;
            } else {
                this.isMineralFound = false;
                throw new NotFound(this.getName() + " не найден");
            }
    }
    // Отправиться куда-то
    public void goTo(Location l) {
        setPersonStat(PersonStatus.MOVE);
        System.out.println(this.getName() + " направляется на " + l);
    }
    // Делать что-то, или скорее над предметом что-то делают
    public void doThis(String a) {
        setPersonStat(PersonStatus.DOING);
        System.out.println(this.getName() + " " + a);
    }
}


