package ml.jadss.jadgens.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("unused")
public class onMachinePlaceEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private int type;
    private boolean cancelled;

    public onMachinePlaceEvent(Player player, int machine_type) {
        this.player = player;
        this.type = machine_type;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
    public boolean isCancelled() {
        return cancelled;
    }

    public Player getPlayer() {
        return player;
    }
    public int getType() {
        return type;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

}