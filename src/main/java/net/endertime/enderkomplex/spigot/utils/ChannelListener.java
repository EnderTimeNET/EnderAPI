package net.endertime.enderkomplex.spigot.utils;

import net.endertime.enderapi.spigot.api.EnderAPI;
import net.endertime.enderkomplex.spigot.core.ServerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class ChannelListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player p, byte[] bytes) {
        if (!channel.equalsIgnoreCase("enderkomplex")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subChannel = in.readUTF();
        if (subChannel.equalsIgnoreCase("pluginmessage")) {
            String playerName = in.readUTF();
            String actionString = in.readUTF();
            Player target = Bukkit.getPlayer(playerName);
            switch(actionString) {
                case "SEND_ACTIONBAR":
                    if(target != null) {
                        String additionalText = in.readUTF();
                        EnderAPI.getInstance().sendActionBar(target, additionalText);
                    }
                    break;
                case "PLAY_ERROR_SOUND":
                    if(target != null) {
                        target.playSound(target.getLocation(), Sound.ITEM_SHIELD_BREAK, (float) 0.5, 1);
                    }
                    break;
                case "PLAY_SUCCESS_SOUND":
                    if(target != null) {
                        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, (float) 0.5, 2);
                    }
                    break;
                case "PLAY_NOTIFY_SOUND":
                    if(target != null) {
                        target.playSound(target.getLocation(), Sound.BLOCK_NOTE_CHIME, (float) 1, (float) 1.5);
                    }
                    break;
                case "CREATE_NPC":
                    if(target != null) {
                        String additionalText = in.readUTF();
                        ServerHandler.createNPC(target, additionalText);
                    }
                    break;
            }
        }
    }

}
