package ml.jadss.jadgens.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("unused")
public class onProduce extends Event {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;

    public onProduce() { }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
    public boolean isCancelled() {
        return cancelled;
    }
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

}