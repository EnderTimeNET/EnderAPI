package net.endertime.enderkomplex.spigot.utils;

import java.util.ArrayList;
import java.util.UUID;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.spigot.commands.VanishCommand;
import net.endertime.enderkomplex.spigot.core.ServerData;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VanishListener implements Listener {

    public static ArrayList<UUID> reducedVanish = new ArrayList<>();

    @EventHandler (priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if(reducedVanish.contains(p.getUniqueId())) {
            e.setJoinMessage(null);
            Bukkit.getOnlinePlayers().forEach(online -> {
                if(!online.hasPermission("ek.commands.vanish")) {
                    EnderAPI.getInstance().hidePlayer(online, p);
                }
            });
            p.setGameMode(GameMode.SPECTATOR);
            Bukkit.getScheduler().scheduleSyncDelayedTask(ServerData.Instance, () -> {
                Bukkit.getOnlinePlayers().forEach(online -> {
                    if(!online.hasPermission("ek.commands.vanish")) {
                        EnderAPI.getInstance().hidePlayer(online, p);
                    }
                });
                p.setGameMode(GameMode.SPECTATOR);
                }, 20);
        }

        if(!VanishCommand.vanished.isEmpty()) {
            VanishCommand.vanished.keySet().forEach(v -> {
                if(!p.hasPermission("ek.commands.vanish")) {
                    EnderAPI.getInstance().hidePlayer(p, v);
                }
            });
        }

        if(!reducedVanish.isEmpty()) {
            reducedVanish.forEach(vname -> {
                Player v = Bukkit.getPlayer(vname);
                if(!p.hasPermission("ek.commands.vanish")) {
                    EnderAPI.getInstance().hidePlayer(p, v);
                }
            });
        }
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if(VanishCommand.vanished.containsKey(p)) {
            e.setQuitMessage(null);
            VanishCommand.vanished.remove(p);
            EnderAPI.getInstance().getVanish().remove(p);
        }
        if(reducedVanish.contains(p.getUniqueId())) {
            reducedVanish.remove(p.getUniqueId());
            e.setQuitMessage(null);
        }
    }

}
