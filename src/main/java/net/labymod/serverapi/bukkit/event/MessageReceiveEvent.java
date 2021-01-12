package net.labymod.serverapi.bukkit.event;

import com.google.gson.JsonElement;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.beans.ConstructorProperties;

/**
 * Class created by qlow | Jan
 */
public class MessageReceiveEvent extends Event {
    @ConstructorProperties({"player", "messageKey", "jsonElement"})
    public MessageReceiveEvent(Player player, String messageKey, JsonElement jsonElement) {
        this.player = player;
        this.messageKey = messageKey;
        this.jsonElement = jsonElement;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    private static final HandlerList handlerList = new HandlerList();

    private Player player;

    private String messageKey;

    private JsonElement jsonElement;

    public Player getPlayer() {
        return this.player;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public JsonElement getJsonElement() {
        return this.jsonElement;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }
}
