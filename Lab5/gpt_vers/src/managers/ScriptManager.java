package managers;

import java.util.HashSet;
import java.util.Set;

public class ScriptManager {

    private final Set<String> executingScripts = new HashSet<>();

    public boolean enterScript(String file) {

        if (executingScripts.contains(file)) {
            return false;
        }

        executingScripts.add(file);

        return true;
    }

    public void exitScript(String file) {

        executingScripts.remove(file);
    }
}