package net.endertime.enderkomplex.spigot.utils;

import net.endertime.enderapi.spigot.Spigot;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.mysql.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {



    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        EnderAPI.getInstance().getNoActionbar().remove(p);
        if (Spigot.reports.contains(p))
            Spigot.reports.remove(p);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (Database.isVerified(player.getUniqueId()))
            Database.updateRanksSpigot(player.getUniqueId());

        if (player.hasPermission("ek.commands.reports"))
            Spigot.reports.add(player);
    }

}
