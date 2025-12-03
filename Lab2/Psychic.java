// Psychic.java
package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final class Psychic extends SpecialMove {
    public Psychic() {
        super(Type.PSYCHIC, 90, 1);
    }
    
    @Override protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        if (Math.random() < 0.1) {
            p.setMod(Stat.SPECIAL_DEFENSE, -1);
       }
    }

    @Override protected String describe() {
        return "применяет Psychic, уменьшает специальную защиту противника";
    }
}