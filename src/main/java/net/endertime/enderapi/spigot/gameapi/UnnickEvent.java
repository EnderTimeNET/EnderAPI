package net.endertime.enderapi.spigot.gameapi;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UnnickEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
