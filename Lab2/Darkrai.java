// Darkrai
package pokemon;

import ru.ifmo.se.pokemon.*;
import move.*;

final class Darkrai extends Pokemon {
    public Darkrai(String name, int level) {
        super(name, level);
        super.setType(Type.DARK);
        super.setStats(70, 90, 90, 135, 90, 125);
        
        this.addMove(new RockTomb());
        this.addMove(new ThunderWave());
        this.addMove(new Haze());
        this.addMove(new Psychic());
    }
}
