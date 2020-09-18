package net.endertime.enderapi.permission.listener;

import net.endertime.enderapi.permission.PermAPI;
import net.endertime.enderapi.permission.utils.CustomPermissibleBase;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.lang.reflect.Field;

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onLogin (PlayerLoginEvent e) {
        final Player player = e.getPlayer();

        PermAPI.getInstance().getPermissions().put(player.getUniqueId(), PermAPI.getInstance().getUserPermissions().getPerms(player.getUniqueId(),
                PermAPI.getInstance().getRankPermissions().getPerms(PermAPI.getInstance().getUsers().getRank(player.getUniqueId()))));

        try {
            Field f = CraftHumanEntity.class.getDeclaredField("perm");
            f.setAccessible(true);
            f.set(e.getPlayer(), new CustomPermissibleBase(e.getPlayer()));
            f.setAccessible(false);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ignore) {
        }
    }
}
