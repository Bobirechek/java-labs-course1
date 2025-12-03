// RockTomb.java
package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final class RockTomb extends PhysicalMove {
    public RockTomb() {
        super(Type.ROCK, 60, 0.95);
    }   
    
    @Override protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        p.setMod(Stat.SPEED, -1);
    }

    @Override protected String describe() {
        return "применяет Rock Tomb";
    }
}