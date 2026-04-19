package managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class ScriptManager {

    private static final Stack<Scanner> scanners = new Stack<>();

    static {
        scanners.push(new Scanner(System.in));
    }

    public static Scanner getScanner() {
        return scanners.peek();
    }

    public static boolean isScriptMode() {
        return scanners.size() > 1;
    }

    public static void pushScript(String filename) throws FileNotFoundException {
        scanners.push(new Scanner(new File(filename)));
    }

    public static void popScript() {
        if (scanners.size() > 1) {
            scanners.pop();
        }
    }
}