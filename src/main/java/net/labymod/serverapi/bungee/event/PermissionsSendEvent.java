package net.labymod.serverapi.bungee.event;

import net.labymod.serverapi.Permission;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.Map;

/**
 * Class created by qlow | Jan
 */
public class PermissionsSendEvent extends Event implements Cancellable {
    private ProxiedPlayer player;

    private Map<Permission, Boolean> permissions;

    private boolean cancelled;

    @ConstructorProperties({"player", "permissions", "cancelled"})
    public PermissionsSendEvent(ProxiedPlayer player, Map<Permission, Boolean> permissions, boolean cancelled) {
        this.permissions = new HashMap<>();
        this.player = player;
        this.permissions = permissions;
        this.cancelled = cancelled;
    }

    public PermissionsSendEvent() {
        this.permissions = new HashMap<Permission, Boolean>();
    }

    public ProxiedPlayer getPlayer() {
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
}
