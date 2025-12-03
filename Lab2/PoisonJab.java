// PoisonJab.java
package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final class PoisonJab extends PhysicalMove {
    public PoisonJab() {
        super(Type.POISON, 80, 1);
    }   
    
    @Override protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        if (Math.random() < 0.3) {
            Effect.poison(p);
        }
    }

    @Override protected String describe() {
        return "применяет Poison Jab";
    }
}