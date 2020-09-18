package net.endertime.enderapi.spigot.listener;

import net.endertime.enderapi.spigot.api.EnderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class VanishListener implements PluginMessageListener {

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subChannel = stream.readUTF();

            if (subChannel.equals("pluginmessage")) {
                if (Bukkit.getPlayer(stream.readUTF()) != null) {
                    String arg1 = stream.readUTF();
                    if (arg1.equals("SET_VANISH")) {
                        UUID uuid = EnderAPI.getInstance().getUUID(stream.readUTF());
                        EnderAPI.getInstance().getVanishUUID().add(uuid);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
