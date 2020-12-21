package net.endertime.enderapi.spigot.listener;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.channel.ChannelMessage;
import net.endertime.enderapi.spigot.api.PermAPI;
import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderapi.spigot.utils.ScoreBoard;
import org.bukkit.Bukkit;
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
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                if (!EnderAPI.getInstance().isVanished(all)) {
                                    ScoreBoard scoreBoard = EnderAPI.getInstance().getScoreboard(all);

                                    scoreBoard.b();
                                } else {
                                    EnderAPI.getInstance().updateVanishScore(all);
                                }
                            }
                        }
                        if (PermAPI.getInstance().getGroup(player.getUniqueId()).equals("default")) {
                            if (!player.hasPermission("intave.bypass")) {
                                PermAPI.getInstance().addPermission(player.getUniqueId(), "intave.bypass");
                            }
                        }
                        ChannelMessage.builder()
                                .channel("enderapi")
                                .message("badlion")
                                .json(JsonDocument.newDocument().append("uuid", player.getUniqueId()))
                                .targetAll()
                                .build()
                                .send();
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
