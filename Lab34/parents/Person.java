package parents;

import enums.PersonStatus;

public abstract class Person {
    protected String name;
    protected PersonStatus status;

    public Person(String name, PersonStatus status) {
        this.name = name;
        this.status = status;
    }

    @Override
    public String toString() {
        return this.name;
    }
    public String getName() {
        return this.name;
    }
    public PersonStatus getStatus() {
        return this.status;
    }
    public void addPerson(String name) {
        this.name += " и " + name;
        setStat(PersonStatus.GROUP);
    }
    public void setStat(PersonStatus newStat) {
        this.status = newStat;
    }
    public void talk(String a) {
        System.out.println(a);
    }
}
