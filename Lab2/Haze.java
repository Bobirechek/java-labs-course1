// Haze.java
package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final class Haze extends StatusMove  {
    public Haze() {
        super(Type.ICE, 0, 1);
    }
    
    @Override protected void applyOppEffects(Pokemon p) {
       super.applyOppEffects(p);
       resetStats(p);
    }

    private void resetStats(Pokemon p) {
        for (Stat s : Stat.values()) {
            p.setMod(s, 0);
        }
    }

    @Override protected String describe() {
        return "применяет Haze, сбрасывает все характеристики";
    }
}