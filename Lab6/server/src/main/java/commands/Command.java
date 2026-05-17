package commands;

public interface Command {

    String getName();

    String getDescription();

    void execute(String arg);

}