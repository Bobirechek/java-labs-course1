// Leavanny
package pokemon;

import ru.ifmo.se.pokemon.*;
import move.*;

final class Leavanny extends Swadloon {
    public Leavanny(String name, int level) {
        super(name, level);
        super.setStats(75, 103, 80, 70, 80, 92);

        this.addMove(new Confide());   
    }
}
