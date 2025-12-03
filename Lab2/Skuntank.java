// Skuntank.java
package pokemon;

import ru.ifmo.se.pokemon.*;
import move.*;

final class Skuntank extends Stunky {
    public Skuntank(String name, int level) {
        super(name, level);
        super.setStats(103, 93, 67, 71, 61, 84);
        
        this.addMove(new PoisonJab());
    }
} 
