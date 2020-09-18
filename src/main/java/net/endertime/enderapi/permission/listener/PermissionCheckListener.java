package net.endertime.enderapi.permission.listener;

import net.endertime.enderapi.permission.PermAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PermissionCheckListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onCheck (PermissionCheckEvent event) {
        if (event.getSender() instanceof ProxiedPlayer) {
            final ProxiedPlayer sender = (ProxiedPlayer) event.getSender();

            if (PermAPI.getInstance().getPermissions().get(sender.getUniqueId()) != null) {
                event.setHasPermission(PermAPI.getInstance().hasPermission(sender.getUniqueId(), event.getPermission()));
            }
        }
    }
}
