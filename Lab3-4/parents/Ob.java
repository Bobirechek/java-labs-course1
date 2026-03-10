package parents;

import enums.ItemStatus;
import enums.PersonStatus;

// Обстрактный класс для объектов любого вида
public abstract class Ob {
    private String name;
    private ItemStatus condition;
    private PersonStatus personStat;

    public Ob(String name, ItemStatus condition, PersonStatus personStat){
        this.name = name;
        this.condition = condition;
        this.personStat = personStat;

    }

    @Override
    public String toString(){
        return this.name;
    }

    public String getName(){
        return this.name;
    }

    public void setCondition(ItemStatus newStat) {
        if (this.name.equals("ракета")) {
            System.out.println(name + " теперь имеет " + newStat);
        }
        this.condition = newStat;
    }
    public ItemStatus getCondition() {
        return this.condition;
    }
    public PersonStatus getPersonStat() {
        return this.personStat;
    }
    public void setPersonStat(PersonStatus newStat) {
        this.personStat = newStat;
    }

}
