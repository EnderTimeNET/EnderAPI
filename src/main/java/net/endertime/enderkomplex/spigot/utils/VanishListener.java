package net.endertime.enderkomplex.spigot.utils;

import java.util.ArrayList;

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

    public static ArrayList<String> reducedVanish = new ArrayList<>();

    @EventHandler (priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if(reducedVanish.contains(p.getName())) {
            e.setJoinMessage(null);
            Bukkit.getOnlinePlayers().forEach(online -> {
                if(!online.hasPermission("teamserver.join")) {
                    EnderAPI.getInstance().hidePlayer(online, p);
                }
            });
            p.setGameMode(GameMode.SPECTATOR);
            Bukkit.getScheduler().scheduleSyncDelayedTask(ServerData.Instance, new Runnable() {

                @Override
                public void run() {
                    p.setGameMode(GameMode.SPECTATOR);

                }
            }, 20);
        }

        if(!VanishCommand.vanished.isEmpty()) {
            VanishCommand.vanished.keySet().forEach(v -> {
                if(!p.hasPermission("teamserver.join")) {
                    EnderAPI.getInstance().hidePlayer(p, v);
                }
            });
        }

        if(!reducedVanish.isEmpty()) {
            reducedVanish.forEach(vname -> {
                Player v = Bukkit.getPlayer(vname);
                if(!p.hasPermission("teamserver.join")) {
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
        if(reducedVanish.contains(p.getName())) {
            reducedVanish.remove(p.getName());
            e.setQuitMessage(null);
        }
    }

}
