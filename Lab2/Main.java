// Main.java
package lab2;

import ru.ifmo.se.pokemon.*;
import pokemon.*;
import move.*;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        
        b.addAlly(new Sewaddle("Севадл", 10));
        b.addAlly(new Swadloon("Свадлун", 20));
        b.addAlly(new Darkrai("Даркай", 57));
        
        b.addFoe(new Stunky("Стинки", 15));
        b.addFoe(new Leavanny("Левани", 32));
        b.addFoe(new Skuntank("Скунтанк", 80));
        
        b.go();
    }
}