package common;

import java.io.Serializable;
import models.HumanBeing;
import models.CommandType;


public class Command implements Serializable {
    private static final long serialVersionUID = 2L;

    private final CommandType type;
    private final Serializable argument;
    private final HumanBeing object;

    private final String login;
    private final String passwordHash;

    public Command(CommandType type, Serializable argument, HumanBeing object,
                   String login, String passwordHash) {
        this.type = type;
        this.argument = argument;
        this.object = object;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    public Command(CommandType type, Serializable argument, HumanBeing object) {
        this(type, argument, object, null, null);
    }

    public CommandType getType() {
        return type;
    }
    public Serializable getArgument() {
        return argument;
    }
    public HumanBeing getObject() {
        return object;
    }
    public String getLogin() {
        return login;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
}