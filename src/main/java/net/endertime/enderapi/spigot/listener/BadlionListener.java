package net.endertime.enderapi.spigot.listener;

import net.endertime.enderapi.permission.PermAPI;
import net.endertime.enderapi.spigot.api.EnderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BadlionListener implements Listener, PluginMessageListener {

    @EventHandler
    public void onLogout(final PlayerQuitEvent event) {
        if (EnderAPI.getInstance().getBadlion().contains(event.getPlayer())) {
            EnderAPI.getInstance().getBadlion().remove(event.getPlayer());
        }
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (channel.equals("BungeeCord")) {
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));

            try {
                if (dataInputStream.readUTF().equals("heartbeat")) {
                    String s = dataInputStream.readUTF();
                    if (s.equals("true")) {
                        if (!EnderAPI.getInstance().getBadlion().contains(player)) {
                            EnderAPI.getInstance().getBadlion().add(player.getUniqueId());
                            EnderAPI.getInstance().updateScoreboardGlobally();
                        }
                        if (PermAPI.getInstance().getGroup(player.getUniqueId()).equals("default")) {
                            if (!player.hasPermission("intave.bypass")) {
                                PermAPI.getInstance().addPermission(player.getUniqueId(), "intave.bypass");
                            }
                        }
                    } else if (s.equals("false")) {
                        if (PermAPI.getInstance().getGroup(player.getUniqueId()).equals("default")) {
                            if (player.hasPermission("intave.bypass")) {
                                PermAPI.getInstance().removePermission(player.getUniqueId(), "intave.bypass");
                            }
                        }
                    }
                }
            } catch (IOException ignored) {

            }
        }
    }
}
