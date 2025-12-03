// FocusEnergy.java
package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final class FocusEnergy extends StatusMove {
    public FocusEnergy() {
        super(Type.NORMAL, 0, 1);
    }   
    
    @Override protected void applySelfEffects(Pokemon p) {
        super.applySelfEffects(p);
        p.setMod(Stat.SPEED, 1);
    }

    @Override protected String describe() {
        return "применяет Focus Energy, вероятность критического урона увеличена";
    }
}