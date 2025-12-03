// Sewaddle
package pokemon;

import ru.ifmo.se.pokemon.*;
import move.*;

public class Sewaddle extends Pokemon {
    public Sewaddle(String name, int level) {
        super(name, level);
        super.setType(Type.BUG, Type.GRASS);
        super.setStats(45, 53, 70, 40, 60, 42);
        
        this.addMove(new CalmMind());   
        this.addMove(new DoubleTeam());   
    }
}
