// ThunderWave.java
package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final class ThunderWave extends StatusMove  {
    public ThunderWave() {
        super(Type.ELECTRIC, 0, 0.9);
    }
    
    @Override protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        Effect.paralyze(p);
    }

    @Override protected String describe() {
        return "применяет Thunder Wave";
    }
}