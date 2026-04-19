package common;

import java.io.Serializable;

import models.CommandType;

public class Command implements Serializable {

    private CommandType type;
    private Object argument;

    public Command(CommandType type, Object argument) {
        this.type = type;
        this.argument = argument;
    }

    public CommandType getType() {
        return type;
    }

    public Object getArgument() {
        return argument;
    }
}