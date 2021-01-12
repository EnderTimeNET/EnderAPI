package net.labymod.serverapi.bukkit.event;

import com.google.gson.JsonElement;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.beans.ConstructorProperties;

/**
 * Class created by qlow | Jan
 */
public class MessageSendEvent extends Event implements Cancellable {
    @ConstructorProperties({"player", "messageKey", "jsonElement", "cancelled"})
    public MessageSendEvent(Player player, String messageKey, JsonElement jsonElement, boolean cancelled) {
        this.player = player;
        this.messageKey = messageKey;
        this.jsonElement = jsonElement;
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    private static final HandlerList handlerList = new HandlerList();

    private Player player;

    private String messageKey;

    private JsonElement jsonElement;

    private boolean cancelled;

    public Player getPlayer() {
        return this.player;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public JsonElement getJsonElement() {
        return this.jsonElement;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }
}
