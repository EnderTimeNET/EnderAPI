package net.endertime.enderapi.permission.listener.spigot;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.api.PermAPI;
import net.endertime.enderapi.permission.utils.CustomPermissibleBase;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import java.lang.reflect.Field;

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onLogin (PlayerLoginEvent e) {
        final Player player = e.getPlayer();

        PermAPI.getInstance().getPermissions().put(player.getUniqueId(), PermAPI.getInstance().getUserPermissions().getPerms(player.getUniqueId(),
                PermAPI.getInstance().getRankPermissions().getPerms(PermAPI.getInstance().getUsers().getRank(player.getUniqueId()))));

        PermissionAttachment permissionAttachment = player.addAttachment(EnderAPI.getInstance().getPlugin());

        for (String s : PermAPI.getInstance().getPermissions().get(player.getUniqueId())) {
            permissionAttachment.setPermission(s, true);
        }

        try {
            Field f = CraftHumanEntity.class.getDeclaredField("perm");
            f.setAccessible(true);
            f.set(e.getPlayer(), new CustomPermissibleBase(e.getPlayer()));
            f.setAccessible(false);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ignore) {
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        PermAPI.getInstance().getPermissions().remove(player.getUniqueId());
    }
}
