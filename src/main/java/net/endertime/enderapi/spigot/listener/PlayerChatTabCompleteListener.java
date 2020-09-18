package net.endertime.enderapi.spigot.listener;

import net.endertime.enderapi.spigot.api.NickAPI;
import net.endertime.enderapi.spigot.utils.Nick;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

public class PlayerChatTabCompleteListener implements Listener {

    @EventHandler
    public void onTab (PlayerChatTabCompleteEvent event) {
        Player player = event.getPlayer();
        for (Player nicked : NickAPI.getInstance().getNickedPlayer().keySet()) {
            Nick nick = NickAPI.getInstance().getNick(nicked);
            if (NickAPI.getInstance().couldSee(nicked, player)) {
                if (event.getTabCompletions().contains(nick.getName()))
                    event.getTabCompletions().remove(nick.getName());
                if (nick.getNickedName().toLowerCase().startsWith(event.getChatMessage().toLowerCase())) {
                    if (!event.getTabCompletions().contains(nick.getNickedName())) {
                        event.getTabCompletions().add(nick.getNickedName());
                    }
                }
                continue;
            }
            if (event.getTabCompletions().contains(nick.getNickedName()))
                event.getTabCompletions().remove(nick.getNickedName());
            if (nick.getName().toLowerCase().startsWith(event.getChatMessage().toLowerCase())) {
                if (!event.getTabCompletions().contains(nick.getName())) {
                    event.getTabCompletions().add(nick.getName());
                }
            }
        }
    }
}
