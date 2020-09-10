package ml.jadss.jadgens.events;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("unused")
public class CustomJadGensCommandArg extends Event {

    private static final HandlerList handlers = new HandlerList();

    private CommandSender sender;
    private String command;
    private String[] arguments;
    private boolean used;

    public CustomJadGensCommandArg(CommandSender sender, String argCommand, String[] args) {
        this.sender = sender;
        this.command = argCommand;
        this.arguments = args;
    }

    public void setUse(boolean use) {
        used = use;
    }
    public boolean isUsed() { return used; }

    public CommandSender getSender() {
        return sender;
    }
    public String getCommand() { return command; }
    public String[] getArguments() { return arguments; }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}