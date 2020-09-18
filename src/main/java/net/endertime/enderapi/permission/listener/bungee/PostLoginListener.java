package net.endertime.enderapi.permission.listener.bungee;

import net.endertime.enderapi.bungee.api.PermAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PostLoginListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCheck (PostLoginEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        PermAPI.getInstance().getUsers().createUser(player.getUniqueId(), -1);

        PermAPI.getInstance().getPermissions().put(player.getUniqueId(), PermAPI.getInstance().getUserPermissions().getPerms(player.getUniqueId(),
                PermAPI.getInstance().getRankPermissions().getPerms(PermAPI.getInstance().getUsers().getRank(player.getUniqueId()))));

        if (PermAPI.getInstance().getUsers().getTime(player.getUniqueId()) != -1) {
            if (PermAPI.getInstance().getUsers().getRank(player.getUniqueId()).equals("Ender")) {
                long millis = PermAPI.getInstance().getUsers().getTime(player.getUniqueId());
                long set = PermAPI.getInstance().getUsers().getTimeSet(player.getUniqueId());
                long current = System.currentTimeMillis();

                if (millis + set < current) {
                    PermAPI.getInstance().getUsers().updateTime(player.getUniqueId(), -1);
                    PermAPI.getInstance().getUsers().updateRank(player.getUniqueId(), "default");
                }
            } else {
                PermAPI.getInstance().getUsers().updateTimeSet(player.getUniqueId(), System.currentTimeMillis());
            }
        }

    }
}
