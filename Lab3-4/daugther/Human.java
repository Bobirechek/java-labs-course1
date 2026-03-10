package daugther;

import enums.PersonStatus;
import interfaces.Story;
import parents.Person;
import daugther.Location;

// Класс для людей
public class Human extends Person implements Story {
    public Human(String name, PersonStatus status) {
        super(name, status);
    }

    // Выдвижение предложения
    @Override
    public void offer(String a){
        setStat(PersonStatus.TALK);
        System.out.println(this.getName() + " предлагает: " + a);
    }
    // Надеется на что-то 
    @Override
    public void hope(String a){
        setStat(PersonStatus.TALK);
        System.out.println(this.getName() + " надеется " + a);
    }
    // Делать что-либо
    @Override
    public void doThis(String a){
        setStat(PersonStatus.DOING);
        System.out.println(this.getName() + " сделали " + a);
    }
    // Отправиться куда-то
    @Override
    public void goTo(Location l) {
        setStat(PersonStatus.MOVE);
        System.out.println(this.getName() + " направились в " + l);
    }
    // Начать какое-то дело
    @Override
    public void begin(String a) {
        setStat(PersonStatus.DOING);
        System.out.println(this.getName() + " начали " + a);
    }
    public void talk(String a) {
        System.out.println(this.getName()  + " сказал: " + a);
    }
}
