package net.labymod.serverapi.bungee.event;

import com.google.gson.JsonElement;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

import java.beans.ConstructorProperties;

public class MessageSendEvent extends Event {
    private ProxiedPlayer player;

    private String messageKey;

    private JsonElement jsonElement;

    private boolean cancelled;

    @ConstructorProperties({"player", "messageKey", "jsonElement", "cancelled"})
    public MessageSendEvent(ProxiedPlayer player, String messageKey, JsonElement jsonElement, boolean cancelled) {
        this.player = player;
        this.messageKey = messageKey;
        this.jsonElement = jsonElement;
        this.cancelled = cancelled;
    }

    public MessageSendEvent() {}

    public ProxiedPlayer getPlayer() {
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
}
