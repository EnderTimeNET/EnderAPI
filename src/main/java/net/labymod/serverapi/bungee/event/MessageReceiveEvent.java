package net.labymod.serverapi.bungee.event;

import com.google.gson.JsonElement;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

import java.beans.ConstructorProperties;

/**
 * Class created by qlow | Jan
 */
public class MessageReceiveEvent extends Event {
    private ProxiedPlayer player;

    private String messageKey;

    private JsonElement jsonElement;

    @ConstructorProperties({"player", "messageKey", "jsonElement"})
    public MessageReceiveEvent(ProxiedPlayer player, String messageKey, JsonElement jsonElement) {
        this.player = player;
        this.messageKey = messageKey;
        this.jsonElement = jsonElement;
    }

    public MessageReceiveEvent() {}

    public ProxiedPlayer getPlayer() {
        return this.player;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public JsonElement getJsonElement() {
        return this.jsonElement;
    }
}
