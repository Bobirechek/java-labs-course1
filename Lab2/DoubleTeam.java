// DoubleTeam.java
package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final class DoubleTeam extends StatusMove {
    public DoubleTeam() {
        super(Type.NORMAL, 0, 1.0);
    }
    
    @Override protected void applySelfEffects(Pokemon p) {
        super.applySelfEffects(p);
        
        p.setMod(Stat.EVASION, 1);
    }

    @Override protected String describe() {
        return "применяет Double Team";
    }
}