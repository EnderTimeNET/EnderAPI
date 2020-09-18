package net.endertime.enderapi.spigot.listener;

import net.endertime.enderapi.spigot.api.NickAPI;
import net.endertime.enderapi.spigot.utils.Nick;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteListener implements Listener {

    @EventHandler
    public void onTab (TabCompleteEvent event) {
        Player player = (Player) event.getSender();
        if (commands.contains(event.getBuffer())) {
            event.getCompletions().clear();
        } else {
            for (Player nicked : NickAPI.getInstance().getNickedPlayer().keySet()) {
                Nick nick = NickAPI.getInstance().getNick(nicked);
                if (NickAPI.getInstance().couldSee(nicked, player)) {
                    if (event.getCompletions().contains(nick.getName()))
                        event.getCompletions().remove(nick.getName());
                    if (!event.getCompletions().contains(nick.getNickedName()))
                        event.getCompletions().add(nick.getNickedName());
                }
            }
        }
    }

    private static List<String> commands = new ArrayList<>();

    public static void fillCommands() {
        commands.add("/about");
    }
}
