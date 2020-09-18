package net.endertime.enderkomplex.bungee.commands;

import java.util.ArrayList;

import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class ChatclearCommand extends Command implements TabExecutor {

    public ChatclearCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(pp.hasPermission("ek.commands.chatclear")) {
            if(args.length == 0) {
                pp.getServer().getInfo().getPlayers().forEach(online ->{
                    if(!online.hasPermission("teamserver.join")) {
                        for(int i = 0; i < 1050; i++) {
                            online.sendMessage(TextComponent.fromLegacyText("§c"));
                        }
                        online.sendMessage(TextComponent.fromLegacyText("§7Der §6Serverchat §7wurde von §c" + pp.getName() + "§7 geleert§8!"));
                    } else {
                        ProxyHandler.sendPluginMessage(online, PluginMessage.SEND_ACTIONBAR, "§7Der Serverchat auf §6"
                                + pp.getServer().getInfo().getName() + " §7wurde von §c" + pp.getName() + " §7geleert§8!");
                        ProxyHandler.sendPluginMessage(online, PluginMessage.PLAY_NOTIFY_SOUND, null);
                    }
                });
            } else if(args.length == 1) {
                String servername = args[0];
                if(ProxyServer.getInstance().getServers().containsKey(servername)) {
                    ProxyServer.getInstance().getServerInfo(servername).getPlayers().forEach(online -> {
                        if(!online.hasPermission("teamserver.join")) {
                            for(int i = 0; i < 1050; i++) {
                                online.sendMessage(TextComponent.fromLegacyText("§c"));
                            }
                            online.sendMessage(TextComponent.fromLegacyText("§7Der §6Serverchat §7wurde von §c" + pp.getName() + "§7 geleert§8!"));
                        } else {
                            ProxyHandler.sendPluginMessage(online, PluginMessage.SEND_ACTIONBAR, "§7Der Serverchat auf §6"
                                    + servername + " §7wurde von §c" + pp.getName() + " §7geleert§8!");
                            ProxyHandler.sendPluginMessage(online, PluginMessage.PLAY_NOTIFY_SOUND, null);
                        }
                    });
                } else {
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Dieser Server ist aktuell §cnicht §7online!");
                    ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                }
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§ccc §8<§cserver§8>");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer) sender;
        ArrayList<String> matches = new ArrayList<>();
        matches.clear();

        if(p.hasPermission("ek.commands.chatclear")) {
            if(args.length == 1) {
                ProxyServer.getInstance().getServers().keySet().forEach(server -> {
                    matches.add(server);
                });
            }
        }
        return matches;
    }

}
