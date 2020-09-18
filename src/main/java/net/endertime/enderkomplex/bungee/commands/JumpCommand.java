package net.endertime.enderkomplex.bungee.commands;

import java.util.ArrayList;

import net.endertime.enderapi.bungee.api.EnderAPI;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class JumpCommand extends Command implements TabExecutor {

    public JumpCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(pp.hasPermission("ek.commands.jump")) {
            if(args.length == 1) {
                String arg = args[0];
                if(ProxyServer.getInstance().getServers().containsKey(arg)) {
                    if(pp.getServer().getInfo().getName().equals(arg)) {
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du §cbist bereits §7auf dem betroffenen Server!");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    } else {
                        pp.connect(ProxyServer.getInstance().getServerInfo(arg));
                    }
                } else if(ProxyServer.getInstance().getPlayer(arg) != null) {
                    if(pp.getServer().getInfo().getName().equals(arg)) {
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du §cbist bereits §7auf dem betroffenen Server!");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    } else {
                        pp.connect(ProxyServer.getInstance().getServerInfo(ProxyServer.getInstance().getPlayer(arg).getServer().getInfo().getName()));
                    }
                } else if(EnderAPI.getInstance().getUUID(arg) != null) {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Spieler ist aktuell §cnicht §7online§8!");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                } else {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Deine Angabe ist §cungültig§8!");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                }
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§cjump §8<§cserver§8/§cspieler§8>");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer) sender;
        ArrayList<String> matches = new ArrayList<>();
        matches.clear();

        if(p.hasPermission("ek.commands.jump")) {
            if(args.length == 1) {
                ProxyServer.getInstance().getServers().keySet().forEach(server -> {
                    matches.add(server);
                });
            }
        }
        return matches;
    }

}
