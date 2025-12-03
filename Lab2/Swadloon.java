// Swadloon
package pokemon;

import ru.ifmo.se.pokemon.*;
import move.*;

public class Swadloon extends Sewaddle {
    public Swadloon(String name, int level) {
        super(name, level);
        super.setStats(55, 63, 90, 50, 80, 42);
         
        this.addMove(new GrassWhistle());   
    }
}
