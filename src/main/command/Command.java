package main.command;

import main.console.Request;
import main.console.Response;

public abstract class Command {
    private final String name;
    private final String help;

    public Command(String name, String help) {
        this.name = name;
        this.help = help;
    }
    public Command(String name) {
        this(name, String.format("No help for '%s' command", name));
    }

    public String getName() {
        return name;
    }

    public String getHelp() {
        return help;
    }

    public abstract Response execute(Request request);
}
