package managers;

import java.util.Stack;

public class ScriptManager {
    private static final Stack<String> scriptStack = new Stack<>();

    public static void pushScript(String path) {
        scriptStack.push(path);
    }

    public static void popScript() {
        scriptStack.pop();
    }

    public static boolean isRecursive(String path) {
        return scriptStack.contains(path);
    }
}