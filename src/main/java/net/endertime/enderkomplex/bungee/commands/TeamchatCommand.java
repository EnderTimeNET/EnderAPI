package net.endertime.enderkomplex.bungee.commands;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.endertime.enderkomplex.bungee.core.ProxyData;
import net.endertime.enderkomplex.bungee.core.ProxyHandler;
import net.endertime.enderkomplex.bungee.enums.PluginMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TeamchatCommand extends Command implements Listener {

    public TeamchatCommand(String name) {
        super(name);
    }

    public static ArrayList<UUID> loggedIn = new ArrayList<>();

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;

        if(pp.hasPermission("proxy.teamchat")) {
            if(args.length >= 1) {
                if(args[0].equalsIgnoreCase("login")) {
                    if(!loggedIn.contains(pp.getUniqueId())) {
                        loggedIn.add(pp.getUniqueId());
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du bist nun eingeloggt");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_SUCCESS_SOUND, null);
                    } else {
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du bist §cbereits §7eingeloggt");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    }
                } else if(args[0].equalsIgnoreCase("logout")) {
                    if(loggedIn.contains(pp.getUniqueId())) {
                        loggedIn.remove(pp.getUniqueId());
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du bist nun ausgeloggt");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_SUCCESS_SOUND, null);
                    } else {
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Du bist §cbereits §7ausgeloggt");
                        ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
                    }
                } else {
                    if(loggedIn.contains(pp.getUniqueId())) {
                        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            if(all.hasPermission("teamserver.join") && loggedIn.contains(all.getUniqueId())) {
                                all.sendMessage(TextComponent.fromLegacyText("§8§l┃ §4TeamChat §8§l┃ §c" + pp.getName() + " §8» "
                                        + ChatColor.GRAY + ProxyHandler.argsToString(args)));
                            }
                        }
                    }
                }
            } else {
                ProxyHandler.sendPluginMessage(pp, PluginMessage.SEND_ACTIONBAR, "§7Benutze: §8/§ctc §8<§clogin§8/§clogout§8/§cmessage§8>");
                ProxyHandler.sendPluginMessage(pp, PluginMessage.PLAY_ERROR_SOUND, null);
            }
        }
    }

    @EventHandler
    public void onConnect(ServerConnectEvent e) {
        ProxiedPlayer p = e.getPlayer();

        if(e.getReason().equals(Reason.JOIN_PROXY)) {
            if(p.hasPermission("teamserver.join")) {
                if(!loggedIn.contains(p.getUniqueId())) {
                    ProxyServer.getInstance().getScheduler().schedule(ProxyData.Instance, new Runnable() {

                        @Override
                        public void run() {
                            loggedIn.add(p.getUniqueId());
                        }
                    }, 1, TimeUnit.SECONDS);
                }
            }
        }
    }

    @EventHandler
    public void onDisconnect(ServerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();

        if(e.getTarget() == null) {
            if(loggedIn.contains(p.getUniqueId())) {
                loggedIn.remove(p.getUniqueId());
            }
        }
    }

}
