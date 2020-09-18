package net.labymod.serverapi.bukkit.event;

import net.labymod.serverapi.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.Map;

public class PermissionsSendEvent extends Event implements Cancellable {
    @ConstructorProperties({"player", "permissions", "cancelled"})
    public PermissionsSendEvent(Player player, Map<Permission, Boolean> permissions, boolean cancelled) {
        this.permissions = new HashMap<>();
        this.player = player;
        this.permissions = permissions;
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    private static final HandlerList handlerList = new HandlerList();

    private Player player;

    private Map<Permission, Boolean> permissions;

    private boolean cancelled;

    public Player getPlayer() {
        return this.player;
    }

    public Map<Permission, Boolean> getPermissions() {
        return this.permissions;
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
