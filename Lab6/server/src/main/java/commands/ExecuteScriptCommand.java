package commands;

public class ExecuteScriptCommand extends AbstractCommand {

    public ExecuteScriptCommand() {
        super("execute_script", "read and execute the script from the specified file.");
    }

    @Override
    public String execute(Object arg) {
        return "execute_script";
    }
}