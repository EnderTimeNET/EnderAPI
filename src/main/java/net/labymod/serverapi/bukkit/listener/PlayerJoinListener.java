package net.labymod.serverapi.bukkit.listener;

import net.endertime.enderapi.spigot.api.EnderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Class created by qlow | Jan
 */
public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        EnderAPI.getInstance().getPlugin().sendPermissions(player);
    }
}
