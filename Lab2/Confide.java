// Confide.java
package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final class Confide extends StatusMove {
    public Confide() {
        super(Type.NORMAL, 0, 1);
    }   
    
    @Override protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        p.setMod(Stat.SPECIAL_ATTACK, -1);
    }

    @Override protected String describe() {
        return "применяет Confide, уровень специальной атаки противника понижен";
    }
}