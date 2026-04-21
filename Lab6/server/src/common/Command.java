package common;

import java.io.Serializable;
import models.HumanBeing;
import models.CommandType;

public class Command implements Serializable {
    private static final long serialVersionUID = 1L;

    private CommandType type;
    private Serializable argument; // Для ID (Long) или других простых объектов
    private HumanBeing object;     // Тот самый "объект, который должен храниться в коллекции"

    public Command(CommandType type, Serializable argument, HumanBeing object) {
        this.type = type;
        this.argument = argument;
        this.object = object;
    }

    public CommandType getType() { return type; }
    public Serializable getArgument() { return argument; }
    public HumanBeing getObject() { return object; }
}