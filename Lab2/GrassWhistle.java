// GrassWhistle.java
package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final class GrassWhistle extends StatusMove {
    public GrassWhistle() {
        super(Type.GRASS, 0, 0.55);
    }
    
    @Override protected void applyOppEffects(Pokemon p) {
        super.applyOppEffects(p);
        
        Effect.sleep(p);
    }

    @Override protected String describe() {
        return "применяет Grass Whistle";
    }
}