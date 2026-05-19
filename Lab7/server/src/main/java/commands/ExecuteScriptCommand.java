package commands;

public class ExecuteScriptCommand extends AbstractCommand {

    public ExecuteScriptCommand() {
        super("execute_script file_name", "read and execute a script from the specified file");
    }

    @Override
    public String execute(Object arg, String userLogin) {
        return "execute_script";
    }
}