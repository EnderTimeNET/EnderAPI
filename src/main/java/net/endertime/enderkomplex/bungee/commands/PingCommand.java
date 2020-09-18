package net.endertime.enderkomplex.bungee.commands;

import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PingCommand extends Command {

    public PingCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(args.length == 0) {
            int ping = pp.getPing();
            String color = "§f";
            if(ping < 15) {
                color = "§2";
            } else if(ping >= 15 && ping < 30) {
                color = "§a";
            } else if(ping >= 30 && ping < 50) {
                color = "§e";
            } else if(ping >= 50 && ping < 80) {
                color = "§6";
            } else if(ping >= 80 && ping < 100) {
                color = "§c";
            } else if(ping >= 100) {
                color = "§4";
            }
            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dein Ping beträgt§8: " + color + ping + "§7ms");
        } else {
            ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§cping");
            ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
        }

    }

}
