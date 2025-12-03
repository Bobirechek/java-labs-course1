// AcidSpray.java
package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final class AcidSpray extends SpecialMove {
    public AcidSpray() {
        super(Type.POISON, 40, 1);
    }   
    
    @Override protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        p.setMod(Stat.SPECIAL_DEFENSE, -2);
    }

    @Override protected String describe() {
        return "применяет Acid Spray, уровень специальной защиты противника понижен";
    }
}