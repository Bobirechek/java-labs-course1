// CalmMind.java
package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final class CalmMind extends StatusMove {
    public CalmMind() {
        super(Type.PSYCHIC, 0, 1.0);
    }
    
    @Override protected void applySelfEffects(Pokemon p) {
        super.applySelfEffects(p);
        
        p.setMod(Stat.SPECIAL_ATTACK, 1);
        p.setMod(Stat.SPECIAL_DEFENSE, 1);
    }

    @Override protected String describe() {
        return "применяет Calm Mind";
    }
}