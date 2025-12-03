// Stunky.java
package pokemon;

import ru.ifmo.se.pokemon.*;
import move.*;

public class Stunky extends Pokemon {
    public Stunky(String name, int level) {
        super(name, level);
        super.setType(Type.DARK, Type.POISON);
        super.setStats(63, 63, 47, 41, 41, 74);
        
        this.addMove(new FocusEnergy());
        this.addMove(new Confide());
        this.addMove(new AcidSpray());
    }
} 
