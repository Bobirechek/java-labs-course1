package daugther;

import enums.PersonStatus;
import interfaces.Story;
import parents.Person;
import daugther.Location;

public class Human extends Person implements Story {
    public Human(String name, PersonStatus status) {
        super(name, status);
    }

    @Override
    public void offer(String a){
        setStat(PersonStatus.TALK);
        System.out.println(this.getName() + " предлагает: " + a);
    }
    @Override
    public void hope(String a){
        setStat(PersonStatus.TALK);
        System.out.println(this.getName() + " надеется " + a);
    }
    @Override
    public void doThis(String a){
        setStat(PersonStatus.DOING);
        System.out.println(this.getName() + " сделали " + a);
    }
    @Override
    public void goTo(Location l) {
        setStat(PersonStatus.MOVE);
        System.out.println(this.getName() + " направились в " + l);
    }
    @Override
    public void begin(String a) {
        setStat(PersonStatus.DOING);
        System.out.println(this.getName() + " начали " + a);
    }
}
